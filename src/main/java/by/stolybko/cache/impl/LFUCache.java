package by.stolybko.cache.impl;

import by.stolybko.cache.Cache;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Объект реализующий LFU алгоритм кеширования.
 * При сохранении объекта в кэше он попадает в список с частотностью = 1.
 * При обращении к кэшируемому объекту он перемещается в список с большей частотностью.
 * Если объем кэша заканчивается,
 * удаляются объекты из начала списка с наименьшей частотностью.
 *
 *
 */
public class LFUCache<K, V> implements Cache<K, V> {

    private final Map<Long, DoubleLinkedList<K, V>> lists = new HashMap<>();
    private final Map<K, Node<K, V>> nodes = new HashMap<>();
    private final Map<K, Long> frequency = new HashMap<>();
    private Long minFrequency = 1L;
    private Long size = 0L;
    private final int CACHE_CAPACITY;

    public LFUCache(int cache_capacity) {
        CACHE_CAPACITY = cache_capacity;
    }

    /**
     * Возвращает Optional объекта из кэша по ключу.
     * Обновляет частотность кэшируемого объекста.
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
        updateFrequency(key);

        return Optional.ofNullable(node.getValue());
    }

    /**
     * Помещает объект в кэш.
     * Обновляет частотность кэшируемого объекта,
     * если он уже содержится в кэше.
     *
     * @param key - ключ кэшируемого объекта.
     * @param value - кэшируемый объект.
     */
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
            Node<K, V> deleteNode = lists.get(minFrequency).pop();
            nodes.remove(deleteNode.getKey());
            frequency.remove(deleteNode.getKey());
            if (lists.get(minFrequency).getSize() == 0) {
                lists.remove(minFrequency);
            }
            size--;
        }
        if (!lists.containsKey(1L)) {
            lists.put(1L, new DoubleLinkedList<>());
        }
        Node<K, V> node = lists.get(1L).append(key, value);
        nodes.put(key, node);
        frequency.put(key, 1L);
        size++;
        minFrequency = 1L;
    }

    /**
     * Удаляет объект из кэша по ключу.
     *
     * @param key - ключ объекта из кэша.
     */
    @Override
    public void removeFromCache(K key) {
        nodes.remove(key);
        Long currentFreq = frequency.remove(key);
        if (lists.get(currentFreq).getSize() == 0) {
            lists.remove(currentFreq);
        }
        size--;
    }

    /**
     * Обновляет частотность кэшируемого объекта.
     *
     * @param key - ключ объекта из кэша.
     */
    private void updateFrequency(K key) {
        Node<K, V> prevNode = nodes.get(key);
        Long prevFreq = frequency.get(key);
        DoubleLinkedList<K, V> list = lists.get(prevFreq);
        list.remove(prevNode);
        if (!lists.containsKey(prevFreq + 1L)) {
            lists.put(prevFreq + 1L, new DoubleLinkedList<>());
        }
        Node<K, V> node = lists.get(prevFreq + 1L).append(prevNode.getKey(), prevNode.getValue());
        nodes.put(key, node);
        frequency.put(key, prevFreq + 1L);
        if (lists.get(prevFreq).getSize() == 0) {
            lists.remove(prevFreq);
            if (prevFreq.equals(minFrequency)) {
                minFrequency++;
            }
        }
    }
}