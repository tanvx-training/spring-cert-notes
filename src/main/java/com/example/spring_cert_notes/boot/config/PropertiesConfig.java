package com.example.spring_cert_notes.boot.config;

import com.example.spring_cert_notes.boot.properties.AppProperties;
import com.example.spring_cert_notes.boot.properties.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Enable @ConfigurationProperties classes
 * 
 * Có 3 cách để enable:
 * 1. @EnableConfigurationProperties trên @Configuration class
 * 2. @ConfigurationPropertiesScan trên main class
 * 3. @Component trên @ConfigurationProperties class (không recommended)
 */
@Configuration
@EnableConfigurationProperties({
    AppProperties.class,
    MailProperties.class
})
public class PropertiesConfig {
    // Configuration properties are enabled
}
