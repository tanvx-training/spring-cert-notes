package com.example.spring_cert_notes.boot.actuator;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom Info Contributor
 * 
 * Thêm thông tin custom vào /actuator/info endpoint.
 */
@Component
public class CustomInfoContributor implements InfoContributor {
    
    @Override
    public void contribute(Info.Builder builder) {
        // Add custom info
        Map<String, Object> customInfo = new HashMap<>();
        customInfo.put("description", "Spring Boot Certification Notes");
        customInfo.put("author", "Developer");
        customInfo.put("startTime", LocalDateTime.now().toString());
        
        builder.withDetail("custom", customInfo);
        
        // Add build info
        Map<String, Object> buildInfo = new HashMap<>();
        buildInfo.put("version", "1.0.0");
        buildInfo.put("javaVersion", System.getProperty("java.version"));
        buildInfo.put("springBootVersion", "3.5.7");
        
        builder.withDetail("build", buildInfo);
        
        // Add runtime info
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> runtimeInfo = new HashMap<>();
        runtimeInfo.put("availableProcessors", runtime.availableProcessors());
        runtimeInfo.put("freeMemory", runtime.freeMemory() / 1024 / 1024 + " MB");
        runtimeInfo.put("totalMemory", runtime.totalMemory() / 1024 / 1024 + " MB");
        runtimeInfo.put("maxMemory", runtime.maxMemory() / 1024 / 1024 + " MB");
        
        builder.withDetail("runtime", runtimeInfo);
    }
}
