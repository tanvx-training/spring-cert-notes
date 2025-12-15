package com.example.spring_cert_notes.actuator.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * BÀI 5: SECURE ACTUATOR ENDPOINTS
 * 
 * Cấu hình security cho actuator endpoints.
 * 
 * Best practices:
 * - Health và info có thể public
 * - Các endpoints khác cần authentication
 * - Sensitive endpoints (shutdown, env) cần ADMIN role
 */
@Configuration
@Profile("actuator-secure")
public class ActuatorSecurityConfig {
    
    /**
     * Security configuration cho Actuator endpoints
     */
    @Bean
    @Order(1)
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // Chỉ áp dụng cho actuator endpoints
            .securityMatcher(EndpointRequest.toAnyEndpoint())
            .authorizeHttpRequests(auth -> auth
                // Health và info - public
                .requestMatchers(EndpointRequest.to("health", "info")).permitAll()
                
                // Metrics - cần authenticated
                .requestMatchers(EndpointRequest.to("metrics", "prometheus")).authenticated()
                
                // Sensitive endpoints - cần ADMIN role
                .requestMatchers(EndpointRequest.to("env", "beans", "configprops", "mappings"))
                    .hasRole("ADMIN")
                
                // Shutdown - cần ADMIN role
                .requestMatchers(EndpointRequest.to("shutdown")).hasRole("ADMIN")
                
                // Tất cả endpoints khác - cần authenticated
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        
        return http.build();
    }
}
