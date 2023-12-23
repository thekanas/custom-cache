package by.stolybko.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Dao<K extends Serializable, E> {

    List<E> findAll();
    List<E> findAll(int limit, int offset);
    Optional<E> findById(K id);
    Optional<E> save(E entity);
    Optional<E> update(E entity);
    void delete(K id);
}
