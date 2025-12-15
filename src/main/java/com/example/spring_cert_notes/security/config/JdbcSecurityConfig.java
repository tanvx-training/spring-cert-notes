package com.example.spring_cert_notes.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

/**
 * BÀI 1B: JDBC AUTHENTICATION
 * 
 * Sử dụng JdbcUserDetailsManager để load users từ database.
 * Spring Security cung cấp schema mặc định cho users và authorities.
 * 
 * Kích hoạt: spring.profiles.active=jdbc
 * 
 * Cần tạo tables:
 * - users (username, password, enabled)
 * - authorities (username, authority)
 */
@Configuration
@EnableWebSecurity
@Profile("jdbc")
public class JdbcSecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/user/**").hasRole("USER")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
    
    /**
     * JDBC UserDetailsService
     * 
     * Sử dụng schema mặc định của Spring Security:
     * 
     * CREATE TABLE users (
     *     username VARCHAR(50) NOT NULL PRIMARY KEY,
     *     password VARCHAR(500) NOT NULL,
     *     enabled BOOLEAN NOT NULL
     * );
     * 
     * CREATE TABLE authorities (
     *     username VARCHAR(50) NOT NULL,
     *     authority VARCHAR(50) NOT NULL,
     *     FOREIGN KEY (username) REFERENCES users(username)
     * );
     */
    @Bean
    public UserDetailsService jdbcUserDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        
        // Custom queries (optional - nếu schema khác default)
        manager.setUsersByUsernameQuery(
            "SELECT username, password, enabled FROM users WHERE username = ?"
        );
        manager.setAuthoritiesByUsernameQuery(
            "SELECT username, authority FROM authorities WHERE username = ?"
        );
        
        // Query cho group-based authorities (optional)
        manager.setGroupAuthoritiesByUsernameQuery(
            "SELECT g.id, g.group_name, ga.authority " +
            "FROM groups g " +
            "JOIN group_members gm ON g.id = gm.group_id " +
            "JOIN group_authorities ga ON g.id = ga.group_id " +
            "WHERE gm.username = ?"
        );
        
        return manager;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
