package org.example.dao;

import org.example.entity.Member;
import org.example.entity.Subscription;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class SubscriptionDao implements Dao<Subscription> {
    @Override
    public Optional<Subscription> get(EntityManager em, long id) {
        Subscription subscription = em.find(Subscription.class, id);
        return Optional.ofNullable(subscription);
    }

    @Override
    public List<Subscription> getAll() {
        return List.of();
    }

    @Override
    public List<Subscription> getAll(EntityManager em) {
        return em.createQuery("SELECT m FROM Subscription m", Subscription.class).getResultList();
    }

    @Override
    public void save(Subscription subscription) {

    }

    @Override
    public void update(Subscription subscription, String[] params) {

    }

    @Override
    public void delete(Subscription subscription) {

    }
}
