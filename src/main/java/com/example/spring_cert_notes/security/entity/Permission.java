package com.example.spring_cert_notes.security.entity;

import jakarta.persistence.*;

/**
 * Permission Entity cho Security Demo
 * Fine-grained permissions (authorities)
 */
@Entity
@Table(name = "security_permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;  // READ_USER, WRITE_USER, DELETE_USER, etc.
    
    private String description;
    
    // Constructors
    public Permission() {}
    
    public Permission(String name) {
        this.name = name;
    }
    
    public Permission(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
