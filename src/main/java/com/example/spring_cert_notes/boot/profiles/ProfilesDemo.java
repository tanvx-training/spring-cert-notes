package com.example.spring_cert_notes.boot.profiles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * BÀI 3: PROFILES VÀ PROPERTIES HIERARCHY
 * 
 * Properties được load theo thứ tự ưu tiên (cao -> thấp):
 * 
 * 1. Command line arguments (--property=value)
 * 2. SPRING_APPLICATION_JSON (inline JSON)
 * 3. ServletConfig/ServletContext parameters
 * 4. JNDI attributes
 * 5. Java System properties (-Dproperty=value)
 * 6. OS environment variables
 * 7. RandomValuePropertySource (random.*)
 * 8. Profile-specific properties (application-{profile}.properties)
 * 9. Application properties (application.properties)
 * 10. @PropertySource annotations
 * 11. Default properties (SpringApplication.setDefaultProperties)
 * 
 * Profile-specific files:
 * - application-dev.properties
 * - application-test.properties
 * - application-prod.properties
 */
@Configuration
public class ProfilesDemo {
    
    // ============================================================
    // PROFILE-SPECIFIC BEANS
    // ============================================================
    
    /**
     * Bean chỉ được tạo khi profile "dev" active
     */
    @Bean
    @Profile("dev")
    public DataSourceConfig devDataSource() {
        return new DataSourceConfig(
            "jdbc:h2:mem:devdb",
            "dev_user",
            "dev_password",
            "Development DataSource"
        );
    }
    
    /**
     * Bean chỉ được tạo khi profile "test" active
     */
    @Bean
    @Profile("test")
    public DataSourceConfig testDataSource() {
        return new DataSourceConfig(
            "jdbc:h2:mem:testdb",
            "test_user",
            "test_password",
            "Test DataSource"
        );
    }
    
    /**
     * Bean chỉ được tạo khi profile "prod" active
     */
    @Bean
    @Profile("prod")
    public DataSourceConfig prodDataSource() {
        return new DataSourceConfig(
            "jdbc:postgresql://prod-server:5432/proddb",
            "prod_user",
            "prod_password",
            "Production DataSource"
        );
    }
    
    /**
     * Bean được tạo khi KHÔNG có profile "prod" active
     */
    @Bean
    @Profile("!prod")
    public String nonProductionMarker() {
        return "Running in non-production environment";
    }
    
    /**
     * Bean được tạo khi profile "dev" HOẶC "test" active
     */
    @Bean
    @Profile({"dev", "test"})
    public String devOrTestMarker() {
        return "Running in dev or test environment";
    }
    
    /**
     * Default bean khi không có profile cụ thể
     */
    @Bean
    @Profile("default")
    public DataSourceConfig defaultDataSource() {
        return new DataSourceConfig(
            "jdbc:h2:mem:defaultdb",
            "sa",
            "",
            "Default DataSource"
        );
    }
    
    // ============================================================
    // HELPER CLASS
    // ============================================================
    
    public static class DataSourceConfig {
        private final String url;
        private final String username;
        private final String password;
        private final String description;
        
        public DataSourceConfig(String url, String username, String password, String description) {
            this.url = url;
            this.username = username;
            this.password = password;
            this.description = description;
        }
        
        public String getUrl() { return url; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public String getDescription() { return description; }
        
        @Override
        public String toString() {
            return "DataSourceConfig{url='" + url + "', description='" + description + "'}";
        }
    }
}
