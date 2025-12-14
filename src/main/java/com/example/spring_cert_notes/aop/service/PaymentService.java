package com.example.spring_cert_notes.aop.service;

/**
 * Payment service interface
 */
public interface PaymentService {
    void processPayment(String orderId, double amount);
    String getPaymentStatus(String paymentId);
}
