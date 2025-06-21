package org.example.dao;

import org.example.entity.Member;
import org.example.entity.Subscription;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * DAO třída pro práci s předplatnými
 * Implementuje základní operace pro přístup k datům
 */
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

    public List<Subscription> getByMember(EntityManager em, Member member) {
        return em.createQuery("SELECT s FROM Subscription s WHERE s.member = :member", Subscription.class)
                .setParameter("member", member)
                .getResultList();
    }
}
