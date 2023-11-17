package by.stolybko.cache.impl;

import by.stolybko.cache.Cache;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class LRUCache implements Cache {

    private final Map<Long, Object> mapCache = new HashMap<>();
    private final Deque<Long> deque = new LinkedList<>();
    private final int CACHE_CAPACITY;

    public LRUCache(int cache_capacity) {
        CACHE_CAPACITY = cache_capacity;
    }

    @Override
    public Object getFromCache(Long key) {
       Object current = null;

        if (mapCache.containsKey(key)) {
            current = mapCache.get(key);
            deque.remove(key);
            deque.addFirst(key);
        }
        return Optional.ofNullable(current);
    }

    @Override
    public void putInCache(Long key, Object value) {
        if (mapCache.containsKey(key)) {
            deque.remove(key);
        } else if (deque.size() == CACHE_CAPACITY) {
            Long temp = deque.removeLast();
            mapCache.remove(temp);
        }
        deque.addFirst(key);
        mapCache.put(key, value);
    }
}
