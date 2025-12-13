package com.example.spring_cert_notes.core.lifecycle;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.stereotype.Service;

/**
 * Implementation of PaymentService
 * 
 * When proxied:
 * - If interface exists → JDK Dynamic Proxy
 * - Spring creates proxy implementing PaymentService interface
 */
@Service
public class PaymentServiceImpl implements PaymentService {
    
    @Override
    public void processPayment(String orderId, double amount) {
        System.out.println(Prefixes.CORE_BEAN + 
            "PaymentServiceImpl: Processing payment for order " + orderId + ", amount: $" + amount);
    }
    
    @Override
    public String getPaymentStatus(String orderId) {
        return "COMPLETED";
    }
}
