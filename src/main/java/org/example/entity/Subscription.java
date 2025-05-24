package org.example.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "subscription_type")
    private SubscriptionType subscriptionType;

    @Column(nullable = false, name = "price")
    private Double price;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Payment> payments;

    public Subscription() {}

    public Subscription(SubscriptionType subscriptionType, Double price) {
        this.subscriptionType = subscriptionType;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", subscriptionType=" + subscriptionType +
                ", price=" + price +
                ", payments=" + payments +
                '}';
    }
}
