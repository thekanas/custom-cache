package by.stolybko.cache.impl;

import lombok.Data;

@Data
class Node {
    Long key;
    Object value;
    Node prev = null;
    Node next = null;

    public Node(Long key, Object value) {
        this.value = value;
        this.key = key;
    }
}
