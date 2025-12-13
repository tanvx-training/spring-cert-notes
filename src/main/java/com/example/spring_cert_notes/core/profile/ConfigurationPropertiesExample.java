package com.example.spring_cert_notes.core.profile;

import com.example.spring_cert_notes.Prefixes;
import lombok.Getter;
import lombok.Setter;
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
 * <p>
 * Better alternative to @Value for complex configurations
 * <p>
 * Advantages:
 * - Type-safe
 * - Validation support
 * - Nested properties
 * - Relaxed binding (kebab-case, camelCase, snake_case)
 * - IDE autocomplete support
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app")
@Validated
public class ConfigurationPropertiesExample {

    // Getters and Setters
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
    @Setter
    @Getter
    public static class Database {
        private String url = "jdbc:h2:mem:testdb";
        private String username = "sa";
        private String password = "";
        private int poolSize = 10;

    }
}
