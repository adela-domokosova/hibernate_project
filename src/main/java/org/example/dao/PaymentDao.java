package org.example.dao;

import org.example.entity.Member;
import org.example.entity.Payment;
import org.example.entity.Subscription;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
public class PaymentDao implements Dao<Payment> {
    @Override
    public Optional<Payment> get(EntityManager em, long id) {
        Payment payment = em.find(Payment.class, id);
        return Optional.ofNullable(payment);
    }

    @Override
    public List<Payment> getAll() {
        return List.of();
    }

    @Override
    public List<Payment> getAll(EntityManager em) {
        return em.createQuery("SELECT m FROM Payment m", Payment.class).getResultList();

    }

    @Override
    public void save(Payment payment) {

    }

    @Override
    public void update(Payment payment, String[] params) {

    }

    @Override
    public void delete(Payment payment) {

    }
}
