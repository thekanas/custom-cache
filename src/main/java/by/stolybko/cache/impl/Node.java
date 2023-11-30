package by.stolybko.cache.impl;

import lombok.Data;

@Data
class Node<K, V> {

    private K key;
    private V value;
    private Node<K, V> prev = null;
    private Node<K, V> next = null;

    public Node(K key, V value) {
        this.value = value;
        this.key = key;
    }
}
