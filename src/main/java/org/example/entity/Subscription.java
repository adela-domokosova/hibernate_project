package org.example.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id;

    @Column(nullable = false, name = "subscription_type")
    private SubscriptionType subscriptionType;

    @Column(nullable = false, name = "price")
    private Double price;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Payment> payments;

}
