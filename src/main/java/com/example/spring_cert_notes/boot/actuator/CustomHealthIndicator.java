package com.example.spring_cert_notes.boot.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Custom Health Indicator
 * 
 * Tạo custom health check cho /actuator/health endpoint.
 * Hiển thị dưới tên "customService" trong health response.
 */
@Component("customService")
public class CustomHealthIndicator implements HealthIndicator {
    
    private final Random random = new Random();
    
    @Override
    public Health health() {
        // Simulate health check
        boolean serviceUp = checkExternalService();
        
        if (serviceUp) {
            return Health.up()
                .withDetail("service", "Custom Service")
                .withDetail("status", "Running")
                .withDetail("version", "1.0.0")
                .build();
        } else {
            return Health.down()
                .withDetail("service", "Custom Service")
                .withDetail("error", "Service unavailable")
                .build();
        }
    }
    
    private boolean checkExternalService() {
        // Simulate external service check
        // In real application, this would check database, external API, etc.
        return random.nextInt(10) > 1; // 90% chance of being up
    }
}
