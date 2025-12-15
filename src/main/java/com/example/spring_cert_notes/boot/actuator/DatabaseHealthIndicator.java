package com.example.spring_cert_notes.boot.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Database Health Indicator
 * 
 * Kiểm tra kết nối database.
 */
@Component("database")
public class DatabaseHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        try {
            // Simulate database connection check
            boolean connected = checkDatabaseConnection();
            long responseTime = measureResponseTime();
            
            if (connected && responseTime < 1000) {
                return Health.up()
                    .withDetail("database", "H2")
                    .withDetail("status", "Connected")
                    .withDetail("responseTime", responseTime + "ms")
                    .build();
            } else if (connected) {
                return Health.status("DEGRADED")
                    .withDetail("database", "H2")
                    .withDetail("status", "Slow response")
                    .withDetail("responseTime", responseTime + "ms")
                    .build();
            } else {
                return Health.down()
                    .withDetail("database", "H2")
                    .withDetail("error", "Connection failed")
                    .build();
            }
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .build();
        }
    }
    
    private boolean checkDatabaseConnection() {
        // Simulate database connection check
        return true;
    }
    
    private long measureResponseTime() {
        // Simulate response time measurement
        return (long) (Math.random() * 500);
    }
}
