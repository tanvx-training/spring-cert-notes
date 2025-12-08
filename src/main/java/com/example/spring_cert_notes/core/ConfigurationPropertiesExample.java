package com.example.spring_cert_notes.core;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * EXAMPLE 4: @ConfigurationProperties (Type-safe Configuration)
 * 
 * Better alternative to @Value for complex configurations
 * 
 * Advantages:
 * - Type-safe
 * - Validation support
 * - Nested properties
 * - Relaxed binding (kebab-case, camelCase, snake_case)
 * - IDE autocomplete support
 */
@Configuration
@ConfigurationProperties(prefix = "app")
@Validated
public class ConfigurationPropertiesExample {
    
    // Simple properties
    @NotBlank
    private String name = "Spring Cert Notes";
    
    private String version = "1.0.0";
    
    @Min(1)
    @Max(1000)
    private int maxUsers = 100;
    
    private boolean debugMode = false;
    
    // Nested object
    private Database database = new Database();
    
    // List
    @NotEmpty
    private List<String> allowedOrigins = List.of("http://localhost:3000");
    
    // Map
    private Map<String, String> features = Map.of();
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public int getMaxUsers() {
        return maxUsers;
    }
    
    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }
    
    public boolean isDebugMode() {
        return debugMode;
    }
    
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
    
    public Database getDatabase() {
        return database;
    }
    
    public void setDatabase(Database database) {
        this.database = database;
    }
    
    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }
    
    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
    
    public Map<String, String> getFeatures() {
        return features;
    }
    
    public void setFeatures(Map<String, String> features) {
        this.features = features;
    }
    
    @PostConstruct
    public void displayConfig() {
        System.out.println("\n" + Prefixes.CORE_BEAN + "=== @ConfigurationProperties Example ===");
        System.out.println(Prefixes.CORE_BEAN + "App Name: " + name);
        System.out.println(Prefixes.CORE_BEAN + "Version: " + version);
        System.out.println(Prefixes.CORE_BEAN + "Max Users: " + maxUsers);
        System.out.println(Prefixes.CORE_BEAN + "Debug Mode: " + debugMode);
        System.out.println(Prefixes.CORE_BEAN + "Database URL: " + database.getUrl());
        System.out.println(Prefixes.CORE_BEAN + "Database Pool Size: " + database.getPoolSize());
        System.out.println(Prefixes.CORE_BEAN + "Allowed Origins: " + allowedOrigins);
        System.out.println(Prefixes.CORE_BEAN + "Features: " + features);
    }
    
    /**
     * Nested configuration class
     */
    public static class Database {
        private String url = "jdbc:h2:mem:testdb";
        private String username = "sa";
        private String password = "";
        private int poolSize = 10;
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
        
        public int getPoolSize() {
            return poolSize;
        }
        
        public void setPoolSize(int poolSize) {
            this.poolSize = poolSize;
        }
    }
}
