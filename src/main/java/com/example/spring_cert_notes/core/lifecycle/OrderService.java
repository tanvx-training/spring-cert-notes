package com.example.spring_cert_notes.core.lifecycle;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.stereotype.Service;

/**
 * OrderService - No interface
 * 
 * When proxied:
 * - No interface → CGLIB Proxy
 * - Spring creates subclass of OrderService
 * - Class cannot be final!
 */
@Service
public class OrderService {
    
    public void createOrder(String orderId) {
        System.out.println(Prefixes.CORE_BEAN + 
            "OrderService: Creating order " + orderId);
    }
    
    public void cancelOrder(String orderId) {
        System.out.println(Prefixes.CORE_BEAN + 
            "OrderService: Cancelling order " + orderId);
    }
    
    // Note: final methods cannot be proxied by CGLIB!
    public final void getOrderDetails(String orderId) {
        System.out.println(Prefixes.CORE_BEAN + 
            "OrderService: Getting details for " + orderId + " (final method - not proxied)");
    }
}
