package org.example.dao;

import org.example.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * Generické rozhraní pro přístup k datům
 * Definuje základní CRUD operace pro všechny entity
 */
public interface Dao<T> {
    Optional<T> get(EntityManager em, long id);

    List<T> getAll();

    List<T> getAll(EntityManager em);

    void save(T t);

    void update(T t, String[] params);

    void delete(T t);
}
