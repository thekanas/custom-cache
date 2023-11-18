package by.stolybko.cache.impl;

import by.stolybko.cache.Cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LFUCache implements Cache {

    private final Map<Long, DoubleLinkedList> lists = new HashMap<>();
    private final Map<Long, Node> nodes = new HashMap<>();
    private final Map<Long, Long> freq = new HashMap<>();
    private Long minFreq = 1L;
    private Long size = 0L;
    private final int CACHE_CAPACITY;

    public LFUCache(int cache_capacity) {
        CACHE_CAPACITY = cache_capacity;
    }

    @Override
    public Object getFromCache(Long key) {
        if(!nodes.containsKey(key)) {
            return Optional.empty();
        }
        Node node = nodes.get(key);
        updateFrequency(key);

        return Optional.ofNullable(node.getValue());
    }

    @Override
    public void putInCache(Long key, Object value) {
        if(CACHE_CAPACITY <= 0 ) {
            return;
        }
        if(nodes.containsKey(key)) {
            nodes.get(key).setValue(value);
            updateFrequency(key);
            return;
        }
        if(size == CACHE_CAPACITY) {
            Node deleteNode = lists.get(minFreq).pop();
            nodes.remove(deleteNode.getKey());
            freq.remove(deleteNode.getKey());
            if(lists.get(minFreq).size == 0) {
                lists.remove(minFreq);
            }
            size--;
        }
        if (!lists.containsKey(1L)) {
            lists.put(1L, new DoubleLinkedList());
        }
        Node node = lists.get(1L).append(key, value);
        nodes.put(key, node);
        freq.put(key, 1L);
        size++;
        minFreq = 1L;
    }

    @Override
    public void removeFromCache(Long key) {
        nodes.remove(key);
        Long currentFreq = freq.remove(key);
        if(lists.get(currentFreq).size == 0) {
            lists.remove(currentFreq);
        }
        size--;
    }

    private void updateFrequency(Long key) {
        Node prevNode = nodes.get(key);
        Long prevFreq = freq.get(key);
        DoubleLinkedList list = lists.get(prevFreq);
        list.remove(prevNode);
        if (!lists.containsKey(prevFreq + 1L)) {
            lists.put(prevFreq + 1L, new DoubleLinkedList());
        }
        Node node = lists.get(prevFreq + 1L).append(prevNode.getKey(), prevNode.getValue());
        nodes.put(key, node);
        freq.put(key, prevFreq + 1L);
        if(lists.get(prevFreq).size == 0) {
            lists.remove(prevFreq);
            if(prevFreq.equals(minFreq)) {
                minFreq++;
            }
        }
    }
}