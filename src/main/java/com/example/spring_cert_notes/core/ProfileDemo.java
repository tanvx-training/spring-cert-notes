package com.example.spring_cert_notes.core;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

/**
 * DEMO: Profiles and Properties
 * <p>
 * Run with different profiles:
 * -Dspring.profiles.active=dev
 * -Dspring.profiles.active=test
 * -Dspring.profiles.active=prod
 */
public class ProfileDemo {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   SPRING CORE - PROPERTIES, PROFILES & SpEL                ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        // Create context
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        
        // Set active profile (can also be set via -Dspring.profiles.active=dev)
        String profile = System.getProperty("spring.profiles.active", "dev");
        context.getEnvironment().setActiveProfiles(profile);
        
        System.out.println(Prefixes.CORE_PROFILE + "Active Profile: " + profile);
        
        // Register configuration classes
        context.register(
            DevDatabaseConfig.class,
            TestDatabaseConfig.class,
            ProdDatabaseConfig.class,
            NonProdConfig.class,
            DebugConfig.class,
            PropertySourcesConfig.class,
            SpELExamples.class,
            AdvancedSpELExamples.class,
            ConfigurationPropertiesExample.class
        );
        
        context.refresh();
        
        // Display environment info
        displayEnvironmentInfo(context.getEnvironment());
        
        context.close();
        System.out.println("\n✓ Demo completed!");
    }
    
    private static void displayEnvironmentInfo(Environment env) {
        System.out.println("\n" + Prefixes.CORE_PROFILE + "=== Environment Information ===");
        System.out.println(Prefixes.CORE_PROFILE + "Active Profiles: " + 
            String.join(", ", env.getActiveProfiles()));
        System.out.println(Prefixes.CORE_PROFILE + "Default Profiles: " + 
            String.join(", ", env.getDefaultProfiles()));
    }
}
