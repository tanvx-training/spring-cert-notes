package com.example.spring_cert_notes.boot.profiles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Service để demo Profile và Environment
 */
@Service
public class ProfileService {
    
    private final Environment environment;
    
    @Value("${spring.profiles.active:default}")
    private String activeProfiles;
    
    @Value("${app.name:Spring Boot Demo}")
    private String appName;
    
    @Value("${app.environment:development}")
    private String appEnvironment;
    
    public ProfileService(Environment environment) {
        this.environment = environment;
    }
    
    /**
     * Lấy danh sách active profiles
     */
    public List<String> getActiveProfiles() {
        return Arrays.asList(environment.getActiveProfiles());
    }
    
    /**
     * Lấy danh sách default profiles
     */
    public List<String> getDefaultProfiles() {
        return Arrays.asList(environment.getDefaultProfiles());
    }
    
    /**
     * Kiểm tra profile có active không
     */
    public boolean isProfileActive(String profile) {
        return Arrays.asList(environment.getActiveProfiles()).contains(profile);
    }
    
    /**
     * Lấy property từ Environment
     */
    public String getProperty(String key) {
        return environment.getProperty(key);
    }
    
    /**
     * Lấy property với default value
     */
    public String getProperty(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }
    
    /**
     * Lấy property với type conversion
     */
    public <T> T getProperty(String key, Class<T> targetType) {
        return environment.getProperty(key, targetType);
    }
    
    /**
     * Kiểm tra property có tồn tại không
     */
    public boolean containsProperty(String key) {
        return environment.containsProperty(key);
    }
    
    /**
     * In thông tin environment
     */
    public void printEnvironmentInfo() {
        System.out.println("=== Environment Info ===");
        System.out.println("Active Profiles: " + getActiveProfiles());
        System.out.println("Default Profiles: " + getDefaultProfiles());
        System.out.println("App Name: " + appName);
        System.out.println("App Environment: " + appEnvironment);
        System.out.println("========================");
    }
}
