package com.example.spring_cert_notes.core;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

/**
 * EXAMPLE 2: Define Bean using @Service
 * Demonstrates 3 ways of Dependency Injection:
 * 1. Constructor Injection (RECOMMENDED)
 * 2. Setter Injection
 * 3. Field Injection
 */
@Service
public class UserService {

    // Field Injection (NOT RECOMMENDED - hard to test)
    // @Autowired
    // private UserRepository userRepository;

    private final UserRepository userRepository;
    private EmailService emailService;

    // Constructor Injection (MOST RECOMMENDED)
    // - Immutable (final fields)
    // - Easy to test (can inject mocks)
    // - Required dependencies
    @Autowired // Optional since Spring 4.3 if only one constructor
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        System.out.println(Prefixes.CORE_DI + "UserService: Constructor Injection - UserRepository injected");
    }

    // Setter Injection (for optional dependencies)
    @Autowired(required = false)
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
        System.out.println(Prefixes.CORE_DI + "UserService: Setter Injection - EmailService injected");
    }

    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "UserService: @PostConstruct called");
    }

    public User getUser(Long id) {
        User user = userRepository.findById(id);
        if (emailService != null) {
            emailService.sendWelcomeEmail(user);
        }
        return user;
    }
}
