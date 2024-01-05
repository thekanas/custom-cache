package by.stolybko.cache;

import by.stolybko.cache.impl.LFUCache;
import by.stolybko.cache.impl.LRUCache;
import by.stolybko.entity.BaseEntity;
import org.springframework.beans.factory.annotation.Lookup;

public class CacheFactory {


    /**
     * Возвращает реализацию алгоритма кеширования
     * по его названию.
     *
     * @param name название алгоритма кеширования.
     * @param capacity максимальное кол-во кэшируемых объектов.
     * @return объект реализующий алгоритм кеширования.
     */
    @Lookup
    public static Cache<Long, BaseEntity> getCache(String name, Integer capacity) {
        return switch (name) {
            case "LRU" -> new LRUCache<>(capacity);
            case "LFU" -> new LFUCache<>(capacity);
            default -> null;
        };
    }
}
