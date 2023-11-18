package by.stolybko.cache;

public interface Cache {

    Object getFromCache(Long key);
    void putInCache(Long key, Object value);
    void removeFromCache(Long key);
}
