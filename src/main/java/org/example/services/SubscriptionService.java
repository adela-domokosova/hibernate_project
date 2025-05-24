package org.example.services;

import org.example.dao.SubscriptionDao;
import org.example.entity.Member;
import org.example.entity.Subscription;
import org.example.entity.SubscriptionType;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class SubscriptionService {

    private final SubscriptionDao subscriptionDao;
    public SubscriptionService(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }

    public List<Subscription> getAllSubscriptions(EntityManager em) {
        List<Subscription> subscriptions = null;
        try {
            em.getTransaction().begin();
            subscriptions = subscriptionDao.getAll(em);
            System.out.println("from dao " + subscriptions);
            //members = em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
        return subscriptions;
    }
  //přifat membra, kterému sub patři
    public void saveSubscription(EntityManager em, SubscriptionType type, Double price, Member member) {
        try {
            em.getTransaction().begin();
            Subscription subscription = new Subscription();
            subscription.setSubscriptionType(type);
            subscription.setPrice(price);
            subscription.setMember(member);
            em.persist(subscription);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
