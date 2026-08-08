package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String method; // e.g., CARD, CASH
    private LocalDateTime paidAt;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public Payment() {}

    public Payment(Double amount, String method, LocalDateTime paidAt, Reservation reservation) {
        this.amount = amount;
        this.method = method;
        this.paidAt = paidAt;
        this.reservation = reservation;
    }

    public Long getId() { return id; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }
}
