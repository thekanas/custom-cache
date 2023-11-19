package by.stolybko.cache.impl;

import by.stolybko.cache.Cache;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 */

public class LRUCache<K, V> implements Cache<K, V> {

    private final Map<K, Node<K, V>> nodes = new HashMap<>();
    private final DoubleLinkedList<K, V> list = new DoubleLinkedList<>();
    private final int CACHE_CAPACITY;

    public LRUCache(int cache_capacity) {
        CACHE_CAPACITY = cache_capacity;
    }

    @Override
    public Optional<V> getFromCache(K key) {
        if (!nodes.containsKey(key)) {
            return Optional.empty();
        }

        Node<K, V> node = nodes.get(key);
        list.remove(node);
        list.append(node.getKey(), node.getValue());

        return Optional.ofNullable(node.getValue());
    }

    @Override
    public void putInCache(K key, V value) {
        if (nodes.containsKey(key)) {
            list.remove(nodes.get(key));
        } else if (list.getSize() == CACHE_CAPACITY) {
            Node<K, V> temp = list.pop();
            nodes.remove(temp.getKey());
        }
        Node<K, V> node = list.append(key, value);
        nodes.put(key, node);
    }

    @Override
    public void removeFromCache(K key) {
        list.remove(nodes.remove(key));
    }
}
