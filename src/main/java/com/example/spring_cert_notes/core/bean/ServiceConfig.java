package com.example.spring_cert_notes.core.bean;

import com.example.spring_cert_notes.Prefixes;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * EXAMPLE 4: Configuration class for Service beans
 * Demonstrates defining beans using @Bean methods
 */
@Configuration
public class ServiceConfig {

    /**
     * EXAMPLE: Define Bean using @Bean method
     * - Can control the initialization process
     * - Can create beans from third-party classes
     * - Can inject dependencies through method parameters
     */
    @Bean
    public EmailService emailService() {
        System.out.println(Prefixes.CORE_BEAN + "ServiceConfig: Creating EmailService bean");
        return new EmailService("smtp.gmail.com");
    }

    /**
     * Bean with Prototype scope - creates new instance on each request
     */
    @Bean
    @Scope("prototype")
    public NotificationService notificationService() {
        System.out.println(Prefixes.CORE_BEAN + "ServiceConfig: Creating NotificationService bean (PROTOTYPE)");
        return new NotificationService();
    }

    public static class NotificationService {
        private final long createdTime;

        public NotificationService() {
            this.createdTime = System.currentTimeMillis();
            System.out.println(Prefixes.CORE_LIFECYCLE + "NotificationService instance created at: " + createdTime);
        }

        public void sendNotification(String message) {
            System.out.println(Prefixes.CORE_BEAN + "Notification [" + createdTime + "]: " + message);
        }

        @PreDestroy
        public void destroy() {
            System.out.println(Prefixes.CORE_BEAN + "NotificationService instance destroyed");
        }
    }
}
