package com.example.spring_cert_notes.actuator.service;

import com.example.spring_cert_notes.actuator.metrics.BusinessMetrics;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Service demo sử dụng custom metrics
 */
@Service
public class MetricsDemoService {
    
    private final BusinessMetrics businessMetrics;
    private final Random random = new Random();
    
    public MetricsDemoService(BusinessMetrics businessMetrics) {
        this.businessMetrics = businessMetrics;
    }
    
    /**
     * Simulate user login
     */
    @Timed(value = "user.login.time", description = "Time taken to login")
    public void login(String username) {
        // Simulate login process
        simulateDelay(100, 500);
        
        // Record metrics
        businessMetrics.recordUserLogin(username);
        businessMetrics.userLoggedIn();
        
        System.out.println("User logged in: " + username);
    }
    
    /**
     * Simulate user logout
     */
    public void logout(String username) {
        businessMetrics.userLoggedOut();
        System.out.println("User logged out: " + username);
    }
    
    /**
     * Simulate user registration
     */
    @Timed(value = "user.registration.time", description = "Time taken to register")
    public void register(String username, String source) {
        simulateDelay(200, 800);
        
        businessMetrics.recordUserRegistration(source);
        System.out.println("User registered: " + username + " from " + source);
    }
    
    /**
     * Simulate order creation with timer
     */
    public void createOrder(String productType, double amount) {
        Timer.Sample sample = businessMetrics.startTimer();
        
        try {
            // Simulate order processing
            simulateDelay(300, 1000);
            
            businessMetrics.recordOrderCreated(productType);
            businessMetrics.recordOrderAmount(amount);
            businessMetrics.orderCreated();
            
            System.out.println("Order created: " + productType + " - $" + amount);
        } finally {
            businessMetrics.stopTimer(sample, "order.creation.time");
        }
    }
    
    /**
     * Simulate order completion
     */
    public void completeOrder(String orderId) {
        businessMetrics.recordOrderProcessingTime(() -> {
            simulateDelay(500, 2000);
        });
        
        businessMetrics.orderCompleted();
        System.out.println("Order completed: " + orderId);
    }
    
    /**
     * Simulate error
     */
    public void simulateError(String errorType) {
        businessMetrics.recordError(errorType, "MetricsDemoService");
        System.out.println("Error recorded: " + errorType);
    }
    
    private void simulateDelay(int minMs, int maxMs) {
        try {
            Thread.sleep(random.nextInt(maxMs - minMs) + minMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
