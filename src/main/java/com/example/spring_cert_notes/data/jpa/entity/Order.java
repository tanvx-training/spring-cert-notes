package com.example.spring_cert_notes.data.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order Entity
 */
@Getter
@Entity
@Table(name = "orders")
public class Order {

    // Getters and Setters
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Setter
    @Column(name = "order_number", unique = true)
    private String orderNumber;
    
    @Setter
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;
    
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.orderNumber = "ORD-" + System.currentTimeMillis();
    }
    
    // Constructors
    public Order() {}
    
    public Order(BigDecimal amount, User user) {
        this.amount = amount;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", orderNumber='" + orderNumber + "', amount=" + amount + ", status=" + status + "}";
    }
}
