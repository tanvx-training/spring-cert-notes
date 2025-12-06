package com.example.spring_cert_notes.core;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * EXAMPLE 1: Define Bean using @Component (@Repository)
 * - @Repository is a stereotype annotation that extends @Component
 * - This bean is automatically detected by Spring through component scanning
 */
@Repository
public class UserRepository {

    public UserRepository() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "1. UserRepository: Constructor called");
    }

    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "2. UserRepository: @PostConstruct - Bean initialized");
    }

    public User findById(Long id) {
        System.out.println(Prefixes.CORE_BEAN + "UserRepository: Finding user with id=" + id);
        return new User(id, "user" + id, "user" + id + "@example.com");
    }

    @PreDestroy
    public void cleanup() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "3. UserRepository: @PreDestroy - Bean about to be destroyed");
    }
}
