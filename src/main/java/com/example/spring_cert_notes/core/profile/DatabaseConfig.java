package com.example.spring_cert_notes.core.profile;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import jakarta.annotation.PostConstruct;

/**
 * EXAMPLE 1: Profile-specific Configuration
 * <p>
 * Demonstrates:
 * - @Profile annotation for environment-specific beans
 * - @Value for property injection
 * - Different configurations for dev/test/prod
 */

@Configuration
@Profile("dev")
class DevDatabaseConfig {
    
    @Value("${db.url:jdbc:h2:mem:devdb}")
    private String dbUrl;
    
    @Value("${db.username:dev_user}")
    private String username;
    
    @Value("${db.password:dev_pass}")
    private String password;
    
    @Value("${db.pool.size:5}")
    private int poolSize;
    
    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_PROFILE + "DEV Database Configuration loaded");
        System.out.println(Prefixes.CORE_PROFILE + "  URL: " + dbUrl);
        System.out.println(Prefixes.CORE_PROFILE + "  Username: " + username);
        System.out.println(Prefixes.CORE_PROFILE + "  Pool Size: " + poolSize);
    }
}

@Configuration
@Profile("test")
class TestDatabaseConfig {
    
    @Value("${db.url:jdbc:h2:mem:testdb}")
    private String dbUrl;
    
    @Value("${db.username:test_user}")
    private String username;
    
    @Value("${db.password:test_pass}")
    private String password;
    
    @Value("${db.pool.size:3}")
    private int poolSize;
    
    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_PROFILE + "TEST Database Configuration loaded");
        System.out.println(Prefixes.CORE_PROFILE + "  URL: " + dbUrl);
        System.out.println(Prefixes.CORE_PROFILE + "  Username: " + username);
        System.out.println(Prefixes.CORE_PROFILE + "  Pool Size: " + poolSize);
    }
}

@Configuration
@Profile("prod")
class ProdDatabaseConfig {
    
    @Value("${db.url}")
    private String dbUrl;
    
    @Value("${db.username}")
    private String username;
    
    @Value("${db.password}")
    private String password;
    
    @Value("${db.pool.size:20}")
    private int poolSize;
    
    @Value("${db.connection.timeout:30000}")
    private int connectionTimeout;
    
    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_PROFILE + "PROD Database Configuration loaded");
        System.out.println(Prefixes.CORE_PROFILE + "  URL: " + dbUrl);
        System.out.println(Prefixes.CORE_PROFILE + "  Username: " + username);
        System.out.println(Prefixes.CORE_PROFILE + "  Pool Size: " + poolSize);
        System.out.println(Prefixes.CORE_PROFILE + "  Connection Timeout: " + connectionTimeout + "ms");
    }
}

/**
 * Multiple profiles can be combined
 */
@Configuration
@Profile({"dev", "test"})
class NonProdConfig {
    
    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_PROFILE + "Non-Production Configuration loaded (dev or test)");
        System.out.println(Prefixes.CORE_PROFILE + "  Debug mode: ENABLED");
        System.out.println(Prefixes.CORE_PROFILE + "  SQL logging: ENABLED");
    }
}

/**
 * Negation: Active when NOT in production
 */
@Configuration
@Profile("!prod")
class DebugConfig {
    
    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_PROFILE + "Debug Configuration loaded (NOT prod)");
    }
}
