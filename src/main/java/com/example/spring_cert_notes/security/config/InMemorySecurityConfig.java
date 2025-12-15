package com.example.spring_cert_notes.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * BÀI 1: IN-MEMORY AUTHENTICATION
 * 
 * Cấu hình đơn giản nhất - lưu users trong memory.
 * Phù hợp cho development, testing, hoặc ứng dụng nhỏ.
 * 
 * Kích hoạt: spring.profiles.active=inmemory
 */
@Configuration
@EnableWebSecurity
@Profile("inmemory")
public class InMemorySecurityConfig {
    
    /**
     * SecurityFilterChain - Cấu hình security rules
     * 
     * Thay thế WebSecurityConfigurerAdapter (deprecated từ Spring Security 5.7)
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public endpoints - không cần authentication
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                
                // Admin endpoints - chỉ ADMIN role
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // Moderator endpoints - ADMIN hoặc MODERATOR
                .requestMatchers("/api/moderator/**").hasAnyRole("ADMIN", "MODERATOR")
                
                // User endpoints - bất kỳ authenticated user
                .requestMatchers("/api/user/**").hasRole("USER")
                
                // Tất cả requests khác cần authenticated
                .anyRequest().authenticated()
            )
            
            // 2. HTTP Basic authentication
            .httpBasic(Customizer.withDefaults())
            
            // 3. Form login (optional)
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home")
                .permitAll()
            )
            
            // 4. Logout configuration
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )
            
            // 5. Disable CSRF cho REST APIs (cẩn thận trong production!)
            .csrf(csrf -> csrf.disable())
            
            // 6. Allow H2 console frames
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));
        
        return http.build();
    }
    
    /**
     * In-Memory UserDetailsService
     * 
     * Tạo users trực tiếp trong memory.
     * Password phải được encode!
     */
    @Bean
    public UserDetailsService inMemoryUserDetailsService(PasswordEncoder passwordEncoder) {
        // User thường
        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder.encode("password"))
            .roles("USER")  // Tự động thêm prefix ROLE_
            .build();
        
        // Admin user
        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin123"))
            .roles("ADMIN", "USER")
            .build();
        
        // Moderator user
        UserDetails moderator = User.builder()
            .username("mod")
            .password(passwordEncoder.encode("mod123"))
            .roles("MODERATOR", "USER")
            .build();
        
        // User với authorities (permissions) cụ thể
        UserDetails powerUser = User.builder()
            .username("poweruser")
            .password(passwordEncoder.encode("power123"))
            .roles("USER")
            .authorities("ROLE_USER", "READ_REPORTS", "WRITE_REPORTS")
            .build();
        
        return new InMemoryUserDetailsManager(user, admin, moderator, powerUser);
    }
    
    /**
     * Password Encoder
     * 
     * BCrypt là recommended encoder cho production.
     * Tự động thêm salt và có strength configurable.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
