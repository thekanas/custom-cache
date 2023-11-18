package by.stolybko.cache.impl;

import lombok.Data;

@Data
class Node {

    private Long key;
    private Object value;
    private Node prev = null;
    private Node next = null;

    public Node(Long key, Object value) {
        this.value = value;
        this.key = key;
    }
}
