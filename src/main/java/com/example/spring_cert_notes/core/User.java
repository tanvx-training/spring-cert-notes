package com.example.spring_cert_notes.core;

import lombok.Getter;
import lombok.Setter;

/**
 * Model class đơn giản để sử dụng trong các ví dụ
 */
@Setter
@Getter
public class User {
    // Getters and Setters
    private Long id;
    private String username;
    private String email;

    public User() {
    }

    public User(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email + "'}";
    }
}
