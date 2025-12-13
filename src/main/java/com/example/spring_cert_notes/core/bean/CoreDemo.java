package com.example.spring_cert_notes.core.bean;

import com.example.spring_cert_notes.Prefixes;
import com.example.spring_cert_notes.core.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * MAIN DEMO CLASS - Run this file to see all examples
 * <p>
 * Demonstrates:
 * - ApplicationContext lifecycle
 * - Bean creation and initialization
 * - Dependency Injection
 * - Bean Scopes
 */
public class CoreDemo {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   SPRING CORE - JAVA CONFIGURATION & BEAN MANAGEMENT       ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // Create ApplicationContext from Java Configuration
        System.out.println(Prefixes.CORE_CONTEXT + "Step 1: Initializing ApplicationContext");
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(AppConfig.class);
        
        System.out.println("\n" + Prefixes.CORE_CONTEXT + "Step 2: ApplicationContext is ready\n");

        // Demo 1: Bean Lifecycle
        demoLifecycle(context);

        // Demo 2: Dependency Injection
        demoDependencyInjection(context);

        // Demo 3: Bean Scopes
        demoBeanScopes(context);

        // Demo 4: Lazy Initialization
        demoLazyInitialization(context);

        // Demo 5: Circular Dependency
        demoCircularDependency(context);

        // Close context
        System.out.println("\n" + Prefixes.CORE_CONTEXT + "Final Step: Closing ApplicationContext");
        context.close();
        System.out.println("\n✓ Demo completed!");
    }

    private static void demoLifecycle(AnnotationConfigApplicationContext context) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 1: BEAN LIFECYCLE");
        System.out.println("=".repeat(60));
        
        UserRepository repository = context.getBean(UserRepository.class);
        repository.findById(1L);
    }

    private static void demoDependencyInjection(AnnotationConfigApplicationContext context) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 2: DEPENDENCY INJECTION (Constructor + Setter)");
        System.out.println("=".repeat(60));
        
        UserService userService = context.getBean(UserService.class);
        User user = userService.getUser(100L);
        System.out.println(Prefixes.CORE_BEAN + "Result: " + user);
    }

    private static void demoBeanScopes(AnnotationConfigApplicationContext context) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 3: BEAN SCOPES (Singleton vs Prototype)");
        System.out.println("=".repeat(60));
        
        // Singleton: Always returns the same instance
        System.out.println("\n--- SINGLETON SCOPE ---");
        AppConfig.CacheService cache1 = context.getBean(AppConfig.CacheService.class);
        AppConfig.CacheService cache2 = context.getBean(AppConfig.CacheService.class);
        
        cache1.cache("key1", "value1");
        cache2.cache("key2", "value2");
        
        System.out.println(Prefixes.CORE_BEAN + "cache1 == cache2? " + (cache1 == cache2));
        System.out.println(Prefixes.CORE_BEAN + "cache1 request count: " + cache1.getRequestCount());
        System.out.println(Prefixes.CORE_BEAN + "cache2 request count: " + cache2.getRequestCount());
        
        // Prototype: Creates new instance on each getBean()
        System.out.println("\n--- PROTOTYPE SCOPE ---");
        ServiceConfig.NotificationService notif1 =
            context.getBean(ServiceConfig.NotificationService.class);
        ServiceConfig.NotificationService notif2 = 
            context.getBean(ServiceConfig.NotificationService.class);
        
        notif1.sendNotification("Message 1");
        notif2.sendNotification("Message 2");
        
        System.out.println(Prefixes.CORE_BEAN + "notif1 == notif2? " + (notif1 == notif2));
    }

    private static void demoLazyInitialization(AnnotationConfigApplicationContext context) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 4: LAZY vs EAGER INITIALIZATION");
        System.out.println("=".repeat(60));
        
        System.out.println("\n" + Prefixes.CORE_BEAN + "@Lazy bean not created until requested...");
        System.out.println(Prefixes.CORE_BEAN + "Requesting ReportService...");
        
        AppConfig.ReportService reportService = context.getBean(AppConfig.ReportService.class);
        reportService.generateReport();
        
        System.out.println("\n" + Prefixes.CORE_BEAN + "Note: CacheService (eager) was created at context startup");
        System.out.println(Prefixes.CORE_BEAN + "      ReportService (lazy) was created on getBean()");
    }

    private static void demoCircularDependency(AnnotationConfigApplicationContext context) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 5: CIRCULAR DEPENDENCY (Resolved with @Lazy)");
        System.out.println("=".repeat(60));
        
        ServiceA serviceA = context.getBean(ServiceA.class);
        serviceA.doSomething();
        
        System.out.println("\n" + Prefixes.CORE_BEAN + "✓ Circular dependency resolved successfully!");
    }
}
