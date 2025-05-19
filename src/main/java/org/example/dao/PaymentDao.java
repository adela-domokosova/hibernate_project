package org.example.dao;

import org.example.entity.Payment;

import java.util.List;
import java.util.Optional;

public class PaymentDao implements Dao<Payment> {
    @Override
    public Optional<Payment> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<Payment> getAll() {
        return List.of();
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
