package com.example.spring_cert_notes.aop.service;

import com.example.spring_cert_notes.Prefixes;
import com.example.spring_cert_notes.aop.Auditable;
import com.example.spring_cert_notes.aop.Retry;
import com.example.spring_cert_notes.aop.Secured;
import org.springframework.stereotype.Service;

/**
 * Sample service to demonstrate AOP aspects
 */
@Service
public class OrderService {
    
    private int failCount = 0;
    
    @Auditable(action = "CREATE_ORDER")
    @Secured(roles = {"USER", "ADMIN"})
    public String createOrder(String productId, int quantity) {
        System.out.println(Prefixes.CORE_BEAN + 
            "OrderService: Creating order for " + quantity + "x " + productId);
        
        // Simulate some work
        try { Thread.sleep(100); } catch (InterruptedException e) { }
        
        String orderId = "ORD-" + System.currentTimeMillis();
        System.out.println(Prefixes.CORE_BEAN + 
            "OrderService: Order created: " + orderId);
        
        return orderId;
    }
    
    @Secured(roles = {"ADMIN"})
    public void cancelOrder(String orderId) {
        System.out.println(Prefixes.CORE_BEAN + 
            "OrderService: Cancelling order " + orderId);
    }
    
    public String getOrderStatus(String orderId) {
        System.out.println(Prefixes.CORE_BEAN + 
            "OrderService: Getting status for " + orderId);
        return "PROCESSING";
    }
    
    @Retry(maxAttempts = 3, delay = 500)
    public void processPayment(String orderId) {
        failCount++;
        System.out.println(Prefixes.CORE_BEAN + 
            "OrderService: Processing payment for " + orderId + " (attempt " + failCount + ")");
        
        // Simulate transient failure (fails first 2 times)
        if (failCount < 3) {
            throw new RuntimeException("Payment gateway timeout");
        }
        
        System.out.println(Prefixes.CORE_BEAN + 
            "OrderService: Payment successful!");
        failCount = 0;  // Reset for next demo
    }
    
    public void failingMethod() {
        System.out.println(Prefixes.CORE_BEAN + 
            "OrderService: About to throw exception...");
        throw new IllegalStateException("Something went wrong!");
    }
}
