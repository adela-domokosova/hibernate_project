package org.example.services;

import org.example.dao.PaymentDao;
import org.example.entity.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class PaymentService {
    private static final Logger LOG = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentDao paymentDao;


    public PaymentService(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }
    
    public List<Payment> getAllPayments(EntityManager em) {
            List<Payment> payments = null;
            try {
                em.getTransaction().begin();
                payments = paymentDao.getAll(em);
                em.getTransaction().commit();
                LOG.info("Retrieved all payments");
            } catch (Exception e) {
                LOG.error("Couldn't retrieve payments", e);
                em.getTransaction().rollback();
                e.printStackTrace();
            }
            return payments;
    }

    public void CreatePayment(EntityManager em, Payment payment) {
        try {
            em.getTransaction().begin();
            em.persist(payment);
            em.getTransaction().commit();
            LOG.info("Created payment of {} for subscription {}", 
                payment.getAmount(), payment.getSubscription().getId());
        }catch (Exception e) {
            LOG.error("Problem creating payment", e);
            em.getTransaction().rollback();
            e.printStackTrace();
            return;
        }
    }
}
