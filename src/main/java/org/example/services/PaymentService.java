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
                //members = em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                e.printStackTrace();
            }
            return payments;
    }
}
