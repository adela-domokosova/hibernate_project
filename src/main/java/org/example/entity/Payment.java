package org.example.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "subscription_id", referencedColumnName = "subscription_id", unique = true)
    private Subscription subscription;

    @Column(nullable = false, name = "payment_date")
    private LocalDate paymentDate;

    @Column(nullable = false, name = "amount")
    private Double amount;


    public Payment(){

    }

    public Payment(Long id, Subscription subscription, LocalDate paymentDate, Double amount) {
        this.id = id;
        this.subscription = subscription;
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                //", subscription=" + subscription +
                ", paymentDate=" + paymentDate +
                ", amount=" + amount +
                '}';
    }
}
