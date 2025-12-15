package com.example.spring_cert_notes.actuator.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * BÀI 1C: CUSTOM HEALTH INDICATOR - Disk Space
 * 
 * Kiểm tra dung lượng disk còn trống.
 * Cảnh báo nếu disk space thấp.
 */
@Component("diskSpace")
public class DiskSpaceHealthIndicator implements HealthIndicator {
    
    private static final long THRESHOLD_BYTES = 100 * 1024 * 1024; // 100 MB
    private static final long WARNING_THRESHOLD_BYTES = 500 * 1024 * 1024; // 500 MB
    
    @Override
    public Health health() {
        File disk = new File("/");
        
        long totalSpace = disk.getTotalSpace();
        long freeSpace = disk.getFreeSpace();
        long usableSpace = disk.getUsableSpace();
        long usedSpace = totalSpace - freeSpace;
        double usedPercentage = (double) usedSpace / totalSpace * 100;
        
        Health.Builder builder = Health.up();
        
        if (freeSpace < THRESHOLD_BYTES) {
            builder = Health.down()
                .withDetail("error", "Disk space critically low!");
        } else if (freeSpace < WARNING_THRESHOLD_BYTES) {
            builder = Health.status("WARNING")
                .withDetail("warning", "Disk space is running low");
        }
        
        return builder
            .withDetail("total", formatBytes(totalSpace))
            .withDetail("free", formatBytes(freeSpace))
            .withDetail("usable", formatBytes(usableSpace))
            .withDetail("used", formatBytes(usedSpace))
            .withDetail("usedPercentage", String.format("%.2f%%", usedPercentage))
            .withDetail("threshold", formatBytes(THRESHOLD_BYTES))
            .build();
    }
    
    private String formatBytes(long bytes) {
        if (bytes >= 1024 * 1024 * 1024) {
            return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
        } else if (bytes >= 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else if (bytes >= 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        }
        return bytes + " bytes";
    }
}
