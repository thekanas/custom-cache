package by.stolybko.cache;

import java.util.Optional;

public interface Cache<K, V> {

    Optional<V> getFromCache(K key);
    void putInCache(K key, V value);
    void removeFromCache(K key);
//    Object getFromCache(Long key);
//    void putInCache(Long key, Object value);
//    void removeFromCache(Long key);
}
