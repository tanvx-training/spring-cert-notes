package com.example.spring_cert_notes.actuator.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * BÀI 1D: CUSTOM HEALTH INDICATOR - Memory
 * 
 * Kiểm tra memory usage của JVM.
 */
@Component("memory")
public class MemoryHealthIndicator implements HealthIndicator {
    
    private static final double WARNING_THRESHOLD = 0.8; // 80%
    private static final double CRITICAL_THRESHOLD = 0.9; // 90%
    
    @Override
    public Health health() {
        Runtime runtime = Runtime.getRuntime();
        
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        double usedPercentage = (double) usedMemory / maxMemory;
        
        Health.Builder builder;
        
        if (usedPercentage >= CRITICAL_THRESHOLD) {
            builder = Health.down()
                .withDetail("error", "Memory usage critical!");
        } else if (usedPercentage >= WARNING_THRESHOLD) {
            builder = Health.status("WARNING")
                .withDetail("warning", "Memory usage high");
        } else {
            builder = Health.up();
        }
        
        return builder
            .withDetail("maxMemory", formatBytes(maxMemory))
            .withDetail("totalMemory", formatBytes(totalMemory))
            .withDetail("freeMemory", formatBytes(freeMemory))
            .withDetail("usedMemory", formatBytes(usedMemory))
            .withDetail("usedPercentage", String.format("%.2f%%", usedPercentage * 100))
            .withDetail("availableProcessors", runtime.availableProcessors())
            .build();
    }
    
    private String formatBytes(long bytes) {
        return String.format("%.2f MB", bytes / (1024.0 * 1024));
    }
}
