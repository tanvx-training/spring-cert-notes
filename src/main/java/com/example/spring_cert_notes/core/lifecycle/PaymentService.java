package com.example.spring_cert_notes.core.lifecycle;

/**
 * Interface for PaymentService
 * Used to demonstrate JDK Dynamic Proxy
 */
public interface PaymentService {
    void processPayment(String orderId, double amount);
    String getPaymentStatus(String orderId);
}
