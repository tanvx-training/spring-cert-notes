package com.example.spring_cert_notes.aop;

import com.example.spring_cert_notes.Prefixes;
import com.example.spring_cert_notes.aop.service.OrderService;
import com.example.spring_cert_notes.aop.service.PaymentService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * DEMO: Aspect Oriented Programming
 */
public class AopDemo {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   SPRING AOP - ASPECT ORIENTED PROGRAMMING                ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(AopConfig.class);
        
        OrderService orderService = context.getBean(OrderService.class);
        PaymentService paymentService = context.getBean(PaymentService.class);
        
        demo5AdviceTypes(orderService);
        demoSecurityAspect(orderService);
        demoAuditAspect(orderService, paymentService);
        demoRetryAspect(orderService);
        demoExceptionHandling(orderService);
        
        context.close();
        System.out.println("\n✓ Demo completed!");
    }
    
    private static void demo5AdviceTypes(OrderService orderService) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 1: 5 ADVICE TYPES (@Before, @After, @AfterReturning, @Around)");
        System.out.println("=".repeat(60));
        
        String status = orderService.getOrderStatus("ORD-001");
        System.out.println(Prefixes.CORE_BEAN + "Final result: " + status);
    }
    
    private static void demoSecurityAspect(OrderService orderService) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 2: SECURITY ASPECT (@Secured annotation)");
        System.out.println("=".repeat(60));
        
        orderService.cancelOrder("ORD-001");
    }
    
    private static void demoAuditAspect(OrderService orderService, PaymentService paymentService) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 3: AUDIT ASPECT (@Auditable annotation)");
        System.out.println("=".repeat(60));
        
        orderService.createOrder("PROD-123", 2);
        paymentService.processPayment("ORD-001", 99.99);
    }
    
    private static void demoRetryAspect(OrderService orderService) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 4: RETRY ASPECT (@Retry annotation)");
        System.out.println("=".repeat(60));
        
        orderService.processPayment("ORD-001");
    }
    
    private static void demoExceptionHandling(OrderService orderService) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 5: EXCEPTION HANDLING ASPECT (@AfterThrowing)");
        System.out.println("=".repeat(60));
        
        try {
            orderService.failingMethod();
        } catch (Exception e) {
            System.out.println(Prefixes.CORE_BEAN + "Exception caught in main: " + e.getMessage());
        }
    }
}
