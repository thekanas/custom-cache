package by.stolybko.cache.impl;

import by.stolybko.entity.BaseEntity;

import java.io.Serializable;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class LRUCache<T extends BaseEntity<V>, V extends Serializable> {

    private final Map<V, T> mapCache = new HashMap<>();
    private final Deque<V> deque = new LinkedList<>();
    private final int CACHE_CAPACITY;

    public LRUCache(int cache_capacity) {
        CACHE_CAPACITY = cache_capacity;
    }

    public Optional<T> getFromCache(V key) {
        T current = null;

        if (mapCache.containsKey(key)) {
            current = mapCache.get(key);
            deque.remove(current.getId());
            deque.addFirst(current.getId());
        }
        return Optional.ofNullable(current);
    }

    public void putInCache(V key, T value) {
        if (mapCache.containsKey(key)) {
            deque.remove(key);
        } else if (deque.size() == CACHE_CAPACITY) {
            V temp = deque.removeLast();
            mapCache.remove(temp);
        }
        deque.addFirst(key);
        mapCache.put(key, value);
    }
}
