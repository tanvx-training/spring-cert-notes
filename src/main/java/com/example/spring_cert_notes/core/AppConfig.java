package com.example.spring_cert_notes.core;

import com.example.spring_cert_notes.Prefixes;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 * MAIN CONFIGURATION CLASS
 * Demonstrates:
 * - @Configuration: Marks this class as a configuration class
 * - @ComponentScan: Automatically scans and registers @Component, @Service, @Repository
 * - @Import: Imports other configuration classes
 */
@Configuration
@ComponentScan(basePackages = "com.example.spring_cert_notes.core")
@Import({DataSourceConfig.class, ServiceConfig.class})
public class AppConfig {

    /**
     * EXAMPLE 5: Bean with Lazy Initialization
     * This bean is only created when first requested
     */
    @Bean
    @Lazy
    public ReportService reportService() {
        System.out.println(Prefixes.CORE_BEAN + "Creating ReportService bean (LAZY)");
        return new ReportService();
    }

    /**
     * EXAMPLE: Bean with Singleton scope (default)
     * Only 1 instance exists in the ApplicationContext
     */
    @Bean
    @Scope("singleton") // Can be omitted as this is the default
    public CacheService cacheService() {
        System.out.println(Prefixes.CORE_BEAN + "Creating CacheService bean (SINGLETON)");
        return new CacheService();
    }

    public static class ReportService {
        public ReportService() {
            System.out.println(Prefixes.CORE_LIFECYCLE + "ReportService: Constructor - Bean initialized");
        }

        public void generateReport() {
            System.out.println(Prefixes.CORE_BEAN + "ReportService: Generating report");
        }
    }

    @Getter
    public static class CacheService {
        private int requestCount = 0;

        public void cache(String key, Object value) {
            requestCount++;
            System.out.println(Prefixes.CORE_BEAN + "CacheService: Cached [" + key + "] - Request count: " + requestCount);
        }

        @PreDestroy
        public void destroy() {
            System.out.println(Prefixes.CORE_BEAN + "CacheService: Destroying CacheService bean");
        }
    }
}
