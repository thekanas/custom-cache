package by.stolybko.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Dao<K extends Serializable, E> {

    List<E> findAll();
    Optional<E> findById(K id);
    Optional<E> save(E entity);
    Optional<E> update(E entity);
    boolean delete(K id);
}
