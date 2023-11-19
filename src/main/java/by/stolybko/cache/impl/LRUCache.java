package by.stolybko.cache.impl;

import by.stolybko.cache.Cache;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Объект реализующий LRU алгоритм кеширования.
 * При обращении к кэшируемому объекту или сохранении объекта в кэше, он
 * переносится в конец списка. Если объем кэша заканчивается,
 * удаляются объекты из начала списка.
 *
 */
public class LRUCache<K, V> implements Cache<K, V> {

    private final Map<K, Node<K, V>> nodes = new HashMap<>();
    private final DoubleLinkedList<K, V> list = new DoubleLinkedList<>();
    private final int CACHE_CAPACITY;

    public LRUCache(int cache_capacity) {
        CACHE_CAPACITY = cache_capacity;
    }

    /**
     * Возвращает Optional объекта из кэша по ключу.
     * Обновляет позицию кэшируемого объекста в списке.
     *
     * @param key - ключ объекта из кэша.
     * @return - Optional объекта по переданному ключу.
     */
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

    /**
     * Помещает объект в кэш.
     * Обновляет значение и позицию кэшируемого объекта,
     * если он уже содержится в кэше.
     *
     * @param key - ключ кэшируемого объекта.
     * @param value - кэшируемый объект.
     */
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

    /**
     * Удаляет объект из кэша по ключу.
     *
     * @param key - ключ объекта из кэша.
     */
    @Override
    public void removeFromCache(K key) {
        list.remove(nodes.remove(key));
    }
}
