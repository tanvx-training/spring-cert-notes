package com.example.spring_cert_notes.core;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import jakarta.annotation.PostConstruct;

/**
 * EXAMPLE 2: Multiple Property Sources
 * <p>
 * Demonstrates:
 * - @PropertySource for loading external property files
 * - @PropertySources for multiple files
 * - Property override order
 * - Default values with @Value
 */

@Configuration
@PropertySources({
    @PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true),
    @PropertySource(value = "classpath:custom.properties", ignoreResourceNotFound = true),
    @PropertySource(value = "file:./config/override.properties", ignoreResourceNotFound = true)
})
public class PropertySourcesConfig {
    
    // Basic property injection
    @Value("${app.name:Spring Cert Notes}")
    private String appName;
    
    @Value("${app.version:1.0.0}")
    private String appVersion;
    
    // Numeric properties
    @Value("${app.max.users:100}")
    private int maxUsers;
    
    @Value("${app.timeout:5000}")
    private long timeout;
    
    // Boolean properties
    @Value("${app.feature.enabled:true}")
    private boolean featureEnabled;
    
    // List/Array properties (comma-separated)
    @Value("${app.allowed.origins:http://localhost:3000,http://localhost:8080}")
    private String[] allowedOrigins;
    
    // System properties
    @Value("${user.home}")
    private String userHome;
    
    @Value("${java.version}")
    private String javaVersion;
    
    // Environment variables (with default)
    @Value("${PATH:not-set}")
    private String path;
    
    @PostConstruct
    public void displayProperties() {
        System.out.println("\n" + Prefixes.CORE_BEAN + "=== Property Sources Configuration ===");
        System.out.println(Prefixes.CORE_BEAN + "App Name: " + appName);
        System.out.println(Prefixes.CORE_BEAN + "App Version: " + appVersion);
        System.out.println(Prefixes.CORE_BEAN + "Max Users: " + maxUsers);
        System.out.println(Prefixes.CORE_BEAN + "Timeout: " + timeout + "ms");
        System.out.println(Prefixes.CORE_BEAN + "Feature Enabled: " + featureEnabled);
        System.out.println(Prefixes.CORE_BEAN + "Allowed Origins: " + String.join(", ", allowedOrigins));
        System.out.println(Prefixes.CORE_BEAN + "User Home: " + userHome);
        System.out.println(Prefixes.CORE_BEAN + "Java Version: " + javaVersion);
        System.out.println(Prefixes.CORE_BEAN + "PATH exists: " + !path.equals("not-set"));
    }
}
