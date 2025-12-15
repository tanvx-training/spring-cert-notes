package com.example.spring_cert_notes.testing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * User Entity cho Testing Demo
 */
@Setter
@Getter
@Entity
@Table(name = "test_users")
public class User {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private boolean active = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructors
    public User() {}
    
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    public User(String name) {
        this.name = name;
        this.email = name.toLowerCase().replace(" ", ".") + "@example.com";
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }
}
