package by.stolybko.cache.impl;

import by.stolybko.cache.Cache;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LFUCache<K, V> implements Cache<K, V> {

    private final Map<Long, DoubleLinkedList<K, V>> lists = new HashMap<>();
    private final Map<K, Node<K, V>> nodes = new HashMap<>();
    private final Map<K, Long> freq = new HashMap<>();
    private Long minFreq = 1L;
    private Long size = 0L;
    private final int CACHE_CAPACITY;

    public LFUCache(int cache_capacity) {
        CACHE_CAPACITY = cache_capacity;
    }

    @Override
    public Optional<V> getFromCache(K key) {
        if (!nodes.containsKey(key)) {
            return Optional.empty();
        }
        Node<K, V> node = nodes.get(key);
        updateFrequency(key);

        return Optional.ofNullable(node.getValue());
    }

    @Override
    public void putInCache(K key, V value) {
        if (CACHE_CAPACITY <= 0) {
            return;
        }
        if (nodes.containsKey(key)) {
            nodes.get(key).setValue(value);
            updateFrequency(key);
            return;
        }
        if (size == CACHE_CAPACITY) {
            Node<K, V> deleteNode = lists.get(minFreq).pop();
            nodes.remove(deleteNode.getKey());
            freq.remove(deleteNode.getKey());
            if (lists.get(minFreq).getSize() == 0) {
                lists.remove(minFreq);
            }
            size--;
        }
        if (!lists.containsKey(1L)) {
            lists.put(1L, new DoubleLinkedList<>());
        }
        Node<K, V> node = lists.get(1L).append(key, value);
        nodes.put(key, node);
        freq.put(key, 1L);
        size++;
        minFreq = 1L;
    }

    @Override
    public void removeFromCache(K key) {
        nodes.remove(key);
        Long currentFreq = freq.remove(key);
        if (lists.get(currentFreq).getSize() == 0) {
            lists.remove(currentFreq);
        }
        size--;
    }

    private void updateFrequency(K key) {
        Node<K, V> prevNode = nodes.get(key);
        Long prevFreq = freq.get(key);
        DoubleLinkedList<K, V> list = lists.get(prevFreq);
        list.remove(prevNode);
        if (!lists.containsKey(prevFreq + 1L)) {
            lists.put(prevFreq + 1L, new DoubleLinkedList<>());
        }
        Node<K, V> node = lists.get(prevFreq + 1L).append(prevNode.getKey(), prevNode.getValue());
        nodes.put(key, node);
        freq.put(key, prevFreq + 1L);
        if (lists.get(prevFreq).getSize() == 0) {
            lists.remove(prevFreq);
            if (prevFreq.equals(minFreq)) {
                minFreq++;
            }
        }
    }
}