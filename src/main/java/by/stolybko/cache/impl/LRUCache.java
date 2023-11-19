package by.stolybko.cache.impl;

import by.stolybko.cache.Cache;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LRUCache implements Cache {

    private final Map<Long, Node> nodes = new HashMap<>();
    private final DoubleLinkedList list = new DoubleLinkedList();
    private final int CACHE_CAPACITY;

    public LRUCache(int cache_capacity) {
        CACHE_CAPACITY = cache_capacity;
    }

    @Override
    public Object getFromCache(Long key) {
        if (!nodes.containsKey(key)) {
            return Optional.empty();
        }

        Node node = nodes.get(key);
        list.remove(node);
        list.append(node.getKey(), node.getValue());

        return Optional.ofNullable(node.getValue());
    }

    @Override
    public void putInCache(Long key, Object value) {
        if (nodes.containsKey(key)) {
            list.remove(nodes.get(key));
        } else if (list.getSize() == CACHE_CAPACITY) {
            Node temp = list.pop();
            nodes.remove(temp.getKey());
        }
        Node node = list.append(key, value);
        nodes.put(key, node);
    }

    @Override
    public void removeFromCache(Long key) {
        list.remove(nodes.remove(key));
    }
}
