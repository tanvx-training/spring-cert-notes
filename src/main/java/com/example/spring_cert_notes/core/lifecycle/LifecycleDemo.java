package com.example.spring_cert_notes.core.lifecycle;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * DEMO: Bean Lifecycle & Proxies
 */
public class LifecycleDemo {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   SPRING CORE - BEAN LIFECYCLE & PROXIES                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        demoLifecycle();
        demoProxies();
        demoCircularDependency();
        
        System.out.println("\n✓ Demo completed!");
    }
    
    private static void demoLifecycle() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 1: COMPLETE BEAN LIFECYCLE");
        System.out.println("=".repeat(60));
        
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(LifecycleConfig.class);
        
        SampleLifecycleBean bean = context.getBean(SampleLifecycleBean.class);
        bean.doWork();
        
        System.out.println("\n" + Prefixes.CORE_CONTEXT + "Closing context...");
        context.close();
    }
    
    private static void demoProxies() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 2: JDK vs CGLIB PROXIES");
        System.out.println("=".repeat(60));
        
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(LifecycleConfig.class);
        
        ProxyInspector inspector = context.getBean(ProxyInspector.class);
        
        // JDK Proxy (has interface)
        PaymentService paymentService = context.getBean(PaymentService.class);
        inspector.inspect("paymentService", paymentService);
        paymentService.processPayment("ORD-001", 99.99);
        
        // CGLIB Proxy (no interface)
        OrderService orderService = context.getBean(OrderService.class);
        inspector.inspect("orderService", orderService);
        orderService.createOrder("ORD-002");
        
        context.close();
    }
    
    private static void demoCircularDependency() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 3: CIRCULAR DEPENDENCY SOLUTIONS");
        System.out.println("=".repeat(60));
        
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(LifecycleConfig.class);
        
        System.out.println("\n" + Prefixes.CORE_DI + "Testing @Lazy solution:");
        ServiceX serviceX = context.getBean(ServiceX.class);
        serviceX.doX();
        
        System.out.println("\n" + Prefixes.CORE_DI + "Testing Setter Injection solution:");
        ServiceM serviceM = context.getBean(ServiceM.class);
        serviceM.doM();
        
        context.close();
    }
}
