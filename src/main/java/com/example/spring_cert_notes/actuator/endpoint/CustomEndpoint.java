package com.example.spring_cert_notes.actuator.endpoint;

import org.springframework.boot.actuate.endpoint.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BÀI 4: CUSTOM ACTUATOR ENDPOINT
 * 
 * Tạo custom endpoint tại /actuator/features
 * 
 * Annotations:
 * - @Endpoint: Đánh dấu class là actuator endpoint
 * - @ReadOperation: GET request
 * - @WriteOperation: POST request
 * - @DeleteOperation: DELETE request
 */
@Component
@Endpoint(id = "features")
public class CustomEndpoint {
    
    private final Map<String, Feature> features = new ConcurrentHashMap<>();
    
    public CustomEndpoint() {
        // Initialize default features
        features.put("caching", new Feature("caching", true, "Enable caching"));
        features.put("logging", new Feature("logging", true, "Enable detailed logging"));
        features.put("metrics", new Feature("metrics", true, "Enable metrics collection"));
        features.put("experimental", new Feature("experimental", false, "Enable experimental features"));
    }
    
    /**
     * GET /actuator/features
     * 
     * Trả về tất cả features
     */
    @ReadOperation
    public Map<String, Object> getAllFeatures() {
        Map<String, Object> result = new HashMap<>();
        result.put("features", features);
        result.put("count", features.size());
        result.put("timestamp", LocalDateTime.now().toString());
        return result;
    }
    
    /**
     * GET /actuator/features/{name}
     * 
     * Trả về feature theo tên
     */
    @ReadOperation
    public Feature getFeature(@Selector String name) {
        return features.get(name);
    }
    
    /**
     * POST /actuator/features
     * 
     * Tạo hoặc update feature
     */
    @WriteOperation
    public Feature setFeature(@Selector String name, boolean enabled, String description) {
        Feature feature = new Feature(name, enabled, description);
        features.put(name, feature);
        return feature;
    }
    
    /**
     * DELETE /actuator/features/{name}
     * 
     * Xóa feature
     */
    @DeleteOperation
    public Map<String, String> deleteFeature(@Selector String name) {
        Feature removed = features.remove(name);
        Map<String, String> result = new HashMap<>();
        if (removed != null) {
            result.put("status", "deleted");
            result.put("feature", name);
        } else {
            result.put("status", "not_found");
            result.put("feature", name);
        }
        return result;
    }
    
    // Feature class
    public static class Feature {
        private String name;
        private boolean enabled;
        private String description;
        private LocalDateTime lastModified;
        
        public Feature() {}
        
        public Feature(String name, boolean enabled, String description) {
            this.name = name;
            this.enabled = enabled;
            this.description = description;
            this.lastModified = LocalDateTime.now();
        }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public LocalDateTime getLastModified() { return lastModified; }
        public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }
    }
}
