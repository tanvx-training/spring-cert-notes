package com.example.spring_cert_notes.data.jdbc;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * DEMO: Spring JDBC & Transaction Management
 */
public class JdbcDemo {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   SPRING DATA - JDBC & TRANSACTION MANAGEMENT              ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(JdbcConfig.class);
        
        UserDao userDao = context.getBean(UserDao.class);
        AccountService accountService = context.getBean(AccountService.class);
        
        demoJdbcTemplate(userDao);
        demoTransactionPropagation(accountService, userDao);
        
        context.close();
        System.out.println("\n✓ Demo completed!");
    }
    
    private static void demoJdbcTemplate(UserDao userDao) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 1: JDBCTEMPLATE OPERATIONS");
        System.out.println("=".repeat(60));
        
        // Find all
        List<User> users = userDao.findAll();
        System.out.println(Prefixes.DATA_JDBC + "All users:");
        users.forEach(u -> System.out.println(Prefixes.DATA_JDBC + "  " + u));
        
        // Find by id
        userDao.findById(1L).ifPresent(u -> 
            System.out.println(Prefixes.DATA_JDBC + "Found user 1: " + u));
        
        // Count
        System.out.println(Prefixes.DATA_JDBC + "Total users: " + userDao.count());
    }
    
    private static void demoTransactionPropagation(AccountService accountService, UserDao userDao) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 2: TRANSACTION PROPAGATION");
        System.out.println("=".repeat(60));
        
        // Show initial balances
        System.out.println("\n" + Prefixes.DATA_TX + "Initial balances:");
        userDao.findAll().forEach(u -> 
            System.out.println(Prefixes.DATA_TX + "  " + u.getName() + ": $" + u.getBalance()));
        
        // REQUIRED transfer
        System.out.println("\n" + Prefixes.DATA_TX + "--- REQUIRED Transfer ---");
        accountService.transferRequired(1L, 2L, 100.0);
        
        // Show balances after transfer
        System.out.println("\n" + Prefixes.DATA_TX + "After transfer:");
        userDao.findAll().forEach(u -> 
            System.out.println(Prefixes.DATA_TX + "  " + u.getName() + ": $" + u.getBalance()));
        
        // SUPPORTS - read operation
        System.out.println("\n" + Prefixes.DATA_TX + "--- SUPPORTS (read) ---");
        double balance = accountService.getBalance(1L);
        System.out.println(Prefixes.DATA_TX + "User 1 balance: $" + balance);
        
        // NOT_SUPPORTED - report
        System.out.println("\n" + Prefixes.DATA_TX + "--- NOT_SUPPORTED (report) ---");
        accountService.generateReport();
    }
}
