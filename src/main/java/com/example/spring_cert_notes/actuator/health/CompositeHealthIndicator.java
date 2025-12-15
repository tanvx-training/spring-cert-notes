package com.example.spring_cert_notes.actuator.health;

import org.springframework.boot.actuate.health.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * BÀI 1E: COMPOSITE HEALTH INDICATOR
 * 
 * Kết hợp nhiều health checks thành một group.
 * Sử dụng CompositeHealthContributor để group related health checks.
 */
@Component("infrastructure")
public class CompositeHealthIndicator implements CompositeHealthContributor {
    
    private final Map<String, HealthContributor> contributors = new HashMap<>();
    
    public CompositeHealthIndicator() {
        // Add sub-health indicators
        contributors.put("cache", new CacheHealthIndicator());
        contributors.put("messageQueue", new MessageQueueHealthIndicator());
    }
    
    @Override
    public HealthContributor getContributor(String name) {
        return contributors.get(name);
    }
    
    @Override
    public java.util.Iterator<NamedContributor<HealthContributor>> iterator() {
        return contributors.entrySet().stream()
            .map(entry -> NamedContributor.of(entry.getKey(), entry.getValue()))
            .iterator();
    }
    
    // Sub-indicator: Cache
    private static class CacheHealthIndicator implements HealthIndicator {
        @Override
        public Health health() {
            // Simulate cache health check
            boolean cacheAvailable = true;
            int hitRate = 85;
            
            if (cacheAvailable) {
                return Health.up()
                    .withDetail("type", "In-Memory Cache")
                    .withDetail("hitRate", hitRate + "%")
                    .withDetail("size", "1024 entries")
                    .build();
            }
            return Health.down()
                .withDetail("error", "Cache unavailable")
                .build();
        }
    }
    
    // Sub-indicator: Message Queue
    private static class MessageQueueHealthIndicator implements HealthIndicator {
        @Override
        public Health health() {
            // Simulate message queue health check
            boolean queueAvailable = true;
            int pendingMessages = 42;
            
            if (queueAvailable) {
                return Health.up()
                    .withDetail("type", "In-Memory Queue")
                    .withDetail("pendingMessages", pendingMessages)
                    .withDetail("consumers", 3)
                    .build();
            }
            return Health.down()
                .withDetail("error", "Queue unavailable")
                .build();
        }
    }
}
