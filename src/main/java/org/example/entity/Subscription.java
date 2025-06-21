package org.example.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "active", nullable = false)
    private Boolean active = false;

    public Subscription(Member member) {
        this.member = member;
    }

    public Subscription(SubscriptionType subscriptionType, Double price, Member member) {
        this.subscriptionType = subscriptionType;
        this.price = price;
        this.member = member;
    }

    public Subscription() {

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


    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", subscriptionType=" + subscriptionType +
                ", price=" + price +
                ", startDate=" + startDate +
                ", createdDate=" + createdDate +
                ", active=" + active +
                '}';
    }
}
