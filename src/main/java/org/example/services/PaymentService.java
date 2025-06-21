package org.example.services;

import org.example.dao.PaymentDao;
import org.example.entity.Payment;

import javax.persistence.EntityManager;
import java.util.List;

public class PaymentService {
    private final PaymentDao paymentDao;


    public PaymentService(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }
    public List<Payment> getAllPayments(EntityManager em) {
            List<Payment> payments = null;
            try {
                em.getTransaction().begin();
                payments = paymentDao.getAll(em);
                System.out.println("from dao " + payments);
                em.getTransaction().commit();
            } catch (Exception e) {
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
        }catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return;
        }
    }
}
