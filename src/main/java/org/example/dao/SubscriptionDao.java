package org.example.dao;

import org.example.entity.Subscription;

import java.util.List;
import java.util.Optional;

public class SubscriptionDao implements Dao<Subscription> {
    @Override
    public Optional<Subscription> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<Subscription> getAll() {
        return List.of();
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
