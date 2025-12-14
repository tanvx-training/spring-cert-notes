package com.example.spring_cert_notes.data.jdbc;

import lombok.Getter;
import lombok.Setter;

/**
 * User entity for JDBC examples
 */
@Setter
@Getter
public class User {
    // Getters and Setters
    private Long id;
    private String name;
    private String email;
    private double balance;
    
    public User() {}
    
    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    public User(Long id, String name, String email, double balance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "', balance=" + balance + "}";
    }
}
