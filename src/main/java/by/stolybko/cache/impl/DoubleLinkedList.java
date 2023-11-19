package by.stolybko.cache.impl;

public class DoubleLinkedList<K, V> {

    private final Node<K, V>  head = new Node<>(null, null);
    private final Node<K, V>  tail = new Node<>(null, null);
    private Long size = 0L;

    public DoubleLinkedList() {
        head.setNext(tail);
        tail.setPrev(head);
    }

    public Node<K, V>  append(K key, V value) {
        Node<K, V>  node = new Node<>(key, value);

        Node<K, V>  temp = tail.getPrev();
        temp.setNext(node);
        tail.setPrev(node);
        node.setNext(tail);
        node.setPrev(temp);
        size++;

        return node;
    }

    public Node<K, V>  pop() {
        return remove(head.getNext());
    }

    public Node<K, V>  remove(Node<K, V>  node) {
        if (size <= 0) {
            return null;
        }
        //Node<K, V> temp = node.getPrev();
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
        size--;
        return node;
    }

    public Long getSize() {
        return size;
    }
}
