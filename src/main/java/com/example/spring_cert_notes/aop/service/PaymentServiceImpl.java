package com.example.spring_cert_notes.aop.service;

import com.example.spring_cert_notes.Prefixes;
import com.example.spring_cert_notes.aop.Auditable;
import org.springframework.stereotype.Service;

/**
 * Payment service implementation
 */
@Service
public class PaymentServiceImpl implements PaymentService {
    
    @Override
    @Auditable(action = "PROCESS_PAYMENT")
    public void processPayment(String orderId, double amount) {
        System.out.println(Prefixes.CORE_BEAN + 
            "PaymentService: Processing $" + amount + " for order " + orderId);
        
        // Simulate payment processing
        try { Thread.sleep(50); } catch (InterruptedException e) { }
        
        System.out.println(Prefixes.CORE_BEAN + 
            "PaymentService: Payment completed");
    }
    
    @Override
    public String getPaymentStatus(String paymentId) {
        System.out.println(Prefixes.CORE_BEAN + 
            "PaymentService: Getting status for payment " + paymentId);
        return "COMPLETED";
    }
}
