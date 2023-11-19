package by.stolybko.cache;

import by.stolybko.cache.impl.LFUCache;
import by.stolybko.cache.impl.LRUCache;
import by.stolybko.entity.BaseEntity;
import by.stolybko.util.PropertiesManager;

public class CacheFactory {

    private static final int CACHE_CAPACITY = Integer.parseInt(PropertiesManager.get("cacheCapacity"));

    public static Cache<Long, BaseEntity> getCache(String name) {
        return switch (name) {
            case "LRU" -> new LRUCache<>(CACHE_CAPACITY);
            case "LFU" -> new LFUCache<>(CACHE_CAPACITY);
            default -> null;
        };
    }
}
