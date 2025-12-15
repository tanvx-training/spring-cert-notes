package com.example.spring_cert_notes.actuator.info;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * BÀI 3: CUSTOM INFO CONTRIBUTOR
 * 
 * Thêm thông tin custom vào /actuator/info endpoint.
 */
@Component
public class CustomInfoContributor implements InfoContributor {
    
    private final LocalDateTime startTime = LocalDateTime.now();
    
    @Override
    public void contribute(Info.Builder builder) {
        // Application info
        builder.withDetail("application", getApplicationInfo());
        
        // Runtime info
        builder.withDetail("runtime", getRuntimeInfo());
        
        // System info
        builder.withDetail("system", getSystemInfo());
        
        // Uptime info
        builder.withDetail("uptime", getUptimeInfo());
    }
    
    private Map<String, Object> getApplicationInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Spring Boot Actuator Demo");
        info.put("description", "Learning Spring Boot Actuator for certification");
        info.put("version", "1.0.0");
        info.put("author", "Developer");
        info.put("startTime", startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return info;
    }
    
    private Map<String, Object> getRuntimeInfo() {
        Runtime runtime = Runtime.getRuntime();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        
        Map<String, Object> info = new HashMap<>();
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("javaVendor", System.getProperty("java.vendor"));
        info.put("jvmName", runtimeMXBean.getVmName());
        info.put("jvmVersion", runtimeMXBean.getVmVersion());
        info.put("availableProcessors", runtime.availableProcessors());
        info.put("maxMemory", formatBytes(runtime.maxMemory()));
        info.put("totalMemory", formatBytes(runtime.totalMemory()));
        info.put("freeMemory", formatBytes(runtime.freeMemory()));
        return info;
    }
    
    private Map<String, Object> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("os", System.getProperty("os.name"));
        info.put("osVersion", System.getProperty("os.version"));
        info.put("osArch", System.getProperty("os.arch"));
        info.put("userTimezone", System.getProperty("user.timezone"));
        info.put("fileEncoding", System.getProperty("file.encoding"));
        return info;
    }
    
    private Map<String, Object> getUptimeInfo() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long uptimeMs = runtimeMXBean.getUptime();
        Duration uptime = Duration.ofMillis(uptimeMs);
        
        Map<String, Object> info = new HashMap<>();
        info.put("milliseconds", uptimeMs);
        info.put("seconds", uptime.getSeconds());
        info.put("formatted", formatDuration(uptime));
        return info;
    }
    
    private String formatBytes(long bytes) {
        return String.format("%.2f MB", bytes / (1024.0 * 1024));
    }
    
    private String formatDuration(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        
        if (days > 0) {
            return String.format("%dd %dh %dm %ds", days, hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        }
        return String.format("%ds", seconds);
    }
}
