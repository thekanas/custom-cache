package by.stolybko.cache.impl;

public class DoubleLinkedList {

    Node head = new Node(0L, null);
    Node tail = new Node(0L, null);
    Long size = 0L;

    public DoubleLinkedList() {
        head.setNext(tail);
        tail.setPrev(head);
    }

    public Node append(Long key, Object value) {
        Node node = new Node(key, value);

        Node temp = tail.getPrev();
        temp.setNext(node);
        tail.setPrev(node);
        node.setNext(tail);
        node.setPrev(temp);
        size++;

        return node;
    }

    public Node pop() {
        return remove(head.getNext());
    }

    public Node remove(Node node) {
        if(size <= 0) {
            return null;
        }
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
        size--;
        return node;
    }
}
