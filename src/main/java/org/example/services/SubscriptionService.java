package org.example.services;

import org.example.dao.SubscriptionDao;
import org.example.entity.Member;
import org.example.entity.Subscription;
import org.example.entity.SubscriptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class SubscriptionService {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionService.class);
    private final SubscriptionDao subscriptionDao;
    
    public SubscriptionService(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }

    public List<Subscription> getAllSubscriptions(EntityManager em) {
        List<Subscription> subscriptions = null;
        try {
            em.getTransaction().begin();
            subscriptions = subscriptionDao.getAll(em);
            em.getTransaction().commit();
            LOG.info("Retrieved all subscriptions");
        } catch (Exception e) {
            LOG.error("Couldn't retrieve subscriptions", e);
            em.getTransaction().rollback();
            e.printStackTrace();
        }
        return subscriptions;
    }
    
    public void saveSubscription(EntityManager em, SubscriptionType type, Double price, Member member) {
        try {
            em.getTransaction().begin();
            Subscription subscription = new Subscription();
            subscription.setSubscriptionType(type);
            subscription.setPrice(price);
            subscription.setMember(member);
            em.persist(subscription);
            em.getTransaction().commit();
            LOG.info("Created new {} subscription for member {}", type, member.getId());
        } catch (Exception e) {
            LOG.error("Couldn't create subscription", e);
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public Optional<Subscription> getSubscriptionById(EntityManager em, Long id) {
        Optional<Subscription> subscription = null;
        try {
            em.getTransaction().begin();
            subscription = subscriptionDao.get(em, id);
            em.getTransaction().commit();
            if (!subscription.isPresent()) {
                LOG.info("Subscription not found (ID: {})", id);
            }
        } catch (Exception e) {
            LOG.error("Problem retrieving subscription", e);
            em.getTransaction().rollback();
            e.printStackTrace();
        }
        return subscription;
    }
    
    public List<Subscription> getSubscriptionsByMember(EntityManager em, Member member) {
        List<Subscription> subscriptions = null;
        try {
            em.getTransaction().begin();
            subscriptions = subscriptionDao.getByMember(em, member);
            em.getTransaction().commit();
            LOG.info("Retrieved subscriptions for member {}", member.getId());
        } catch (Exception e) {
            LOG.error("Problem retrieving member's subscriptions", e);
            em.getTransaction().rollback();
            e.printStackTrace();
        }
        return subscriptions;
    }
}
