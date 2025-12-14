package com.example.spring_cert_notes.data.jpa;

import com.example.spring_cert_notes.Prefixes;
import com.example.spring_cert_notes.data.jpa.entity.Order;
import com.example.spring_cert_notes.data.jpa.entity.User;
import com.example.spring_cert_notes.data.jpa.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

/**
 * DEMO: Spring Data JPA
 */
public class JpaDemo {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   SPRING DATA JPA                                          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(JpaConfig.class);
        
        UserService userService = context.getBean(UserService.class);
        
        setupData(userService);
        demoQueryMethods(userService);
        demoPagination(userService);
        demoSpecifications(userService);
        demoEntityGraph(userService);
        
        context.close();
        System.out.println("\n✓ Demo completed!");
    }
    
    private static void setupData(UserService userService) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SETUP: Creating sample data");
        System.out.println("=".repeat(60));
        
        User alice = userService.save(new User("Alice", "Smith", "alice@gmail.com"));
        User bob = userService.save(new User("Bob", "Johnson", "bob@yahoo.com"));
        User charlie = userService.save(new User("Charlie", "Smith", "charlie@gmail.com"));
        
        alice.getOrders().add(new Order(new BigDecimal("100.00"), alice));
        alice.getOrders().add(new Order(new BigDecimal("250.00"), alice));
        userService.save(alice);
        
        System.out.println(Prefixes.DATA_JPA + "Sample data created");
    }
    
    private static void demoQueryMethods(UserService userService) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 1: QUERY METHODS");
        System.out.println("=".repeat(60));
        
        userService.findByEmail("alice@gmail.com")
            .ifPresent(u -> System.out.println(Prefixes.DATA_JPA + "Found: " + u));
        
        List<User> smiths = userService.findByName(null, "Smith");
        System.out.println(Prefixes.DATA_JPA + "Smiths: " + smiths.size());
        
        List<User> gmailUsers = userService.findByEmailDomain("@gmail.com");
        System.out.println(Prefixes.DATA_JPA + "Gmail users: " + gmailUsers.size());
    }
    
    private static void demoPagination(UserService userService) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 2: PAGINATION & SORTING");
        System.out.println("=".repeat(60));
        
        Page<User> page = userService.findAllPagedAndSorted(0, 2, "lastName", true);
        System.out.println(Prefixes.DATA_JPA + "Page " + page.getNumber() + " of " + page.getTotalPages());
        System.out.println(Prefixes.DATA_JPA + "Total elements: " + page.getTotalElements());
        page.getContent().forEach(u -> System.out.println(Prefixes.DATA_JPA + "  " + u));
    }
    
    private static void demoSpecifications(UserService userService) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 3: SPECIFICATIONS (Dynamic Queries)");
        System.out.println("=".repeat(60));
        
        List<User> results = userService.searchUsers(null, "Smith", "@gmail.com", true);
        System.out.println(Prefixes.DATA_JPA + "Search results: " + results.size());
        results.forEach(u -> System.out.println(Prefixes.DATA_JPA + "  " + u));
    }
    
    private static void demoEntityGraph(UserService userService) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 4: @ENTITYGRAPH (N+1 Optimization)");
        System.out.println("=".repeat(60));
        
        List<User> users = userService.findAllWithOrders();
        System.out.println(Prefixes.DATA_JPA + "Users with orders (single query):");
        users.forEach(u -> {
            System.out.println(Prefixes.DATA_JPA + "  " + u.getFirstName() + " - Orders: " + u.getOrders().size());
        });
    }
}
