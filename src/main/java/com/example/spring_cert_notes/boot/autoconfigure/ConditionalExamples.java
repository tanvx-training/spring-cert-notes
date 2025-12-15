package com.example.spring_cert_notes.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * BÀI 2B: @CONDITIONAL ANNOTATIONS EXAMPLES
 * <p>
 * Tất cả các @Conditional annotations và cách sử dụng.
 */
@Configuration
public class ConditionalExamples {
    
    // ============================================================
    // 1. @ConditionalOnClass / @ConditionalOnMissingClass
    // ============================================================
    
    /**
     * Bean chỉ được tạo nếu DataSource class tồn tại trên classpath
     */
    @Bean
    @ConditionalOnClass(DataSource.class)
    public String dataSourceAvailable() {
        return "DataSource is available on classpath";
    }
    
    /**
     * Bean chỉ được tạo nếu class KHÔNG tồn tại
     * Hữu ích để provide fallback implementation
     */
    @Bean
    @ConditionalOnMissingClass("com.mongodb.client.MongoClient")
    public String mongoNotAvailable() {
        return "MongoDB client is not on classpath";
    }
    
    // ============================================================
    // 2. @ConditionalOnBean / @ConditionalOnMissingBean
    // ============================================================
    
    /**
     * Bean chỉ được tạo nếu bean khác đã tồn tại
     */
    @Bean
    @ConditionalOnBean(name = "dataSource")
    public String requiresDataSource() {
        return "This bean requires DataSource bean";
    }
    
    /**
     * Bean chỉ được tạo nếu bean khác KHÔNG tồn tại
     * Thường dùng để provide default implementation
     */
    @Bean("defaultService")
    @ConditionalOnMissingBean(name = "customService")
    public String defaultServiceBean() {
        return "Default service (no custom service defined)";
    }
    
    // ============================================================
    // 3. @ConditionalOnProperty
    // ============================================================
    
    /**
     * Bean chỉ được tạo nếu property có giá trị cụ thể
     */
    @Bean
    @ConditionalOnProperty(
        prefix = "feature",
        name = "cache.enabled",
        havingValue = "true"
    )
    public String cacheEnabled() {
        return "Cache feature is enabled";
    }
    
    /**
     * matchIfMissing = true: Bean được tạo nếu property không được set
     */
    @Bean
    @ConditionalOnProperty(
        prefix = "feature",
        name = "logging.enabled",
        havingValue = "true",
        matchIfMissing = true  // Default enabled
    )
    public String loggingEnabled() {
        return "Logging is enabled (default)";
    }
    
    /**
     * Kiểm tra property tồn tại (không quan tâm giá trị)
     */
    @Bean
    @ConditionalOnProperty(name = "custom.config.path")
    public String customConfigPath(Environment env) {
        return "Custom config path: " + env.getProperty("custom.config.path");
    }
    
    // ============================================================
    // 4. @ConditionalOnResource
    // ============================================================
    
    /**
     * Bean chỉ được tạo nếu resource tồn tại
     */
    @Bean
    @ConditionalOnResource(resources = "classpath:custom.properties")
    public String customPropertiesExists() {
        return "custom.properties file exists";
    }
    
    // ============================================================
    // 5. @ConditionalOnWebApplication / @ConditionalOnNotWebApplication
    // ============================================================
    
    /**
     * Bean chỉ được tạo trong web application
     */
    @Bean
    @ConditionalOnWebApplication
    public String webAppOnly() {
        return "This is a web application";
    }
    
    /**
     * Bean chỉ được tạo KHÔNG phải web application
     */
    @Bean
    @ConditionalOnNotWebApplication
    public String nonWebAppOnly() {
        return "This is NOT a web application";
    }
    
    /**
     * Chỉ định loại web application cụ thể
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public String servletWebApp() {
        return "This is a Servlet-based web application";
    }
    
    // ============================================================
    // 6. @ConditionalOnExpression
    // ============================================================
    
    /**
     * Bean chỉ được tạo nếu SpEL expression = true
     */
    @Bean
    @ConditionalOnExpression("${feature.advanced:false} and ${feature.experimental:false}")
    public String advancedFeature() {
        return "Advanced experimental feature enabled";
    }
    
    @Bean
    @ConditionalOnExpression("'${spring.profiles.active}'.contains('dev')")
    public String devProfileActive() {
        return "Running in dev profile";
    }
    
    // ============================================================
    // 7. @ConditionalOnJava
    // ============================================================
    
    /**
     * Bean chỉ được tạo với Java version cụ thể
     */
    @Bean
    @ConditionalOnJava(range = ConditionalOnJava.Range.EQUAL_OR_NEWER, 
                       value = ConditionalOnJava.JavaVersion.SEVENTEEN)
    public String java17OrNewer() {
        return "Running on Java 17 or newer";
    }
    
    // ============================================================
    // 8. @ConditionalOnSingleCandidate
    // ============================================================
    
    /**
     * Bean chỉ được tạo nếu có đúng 1 candidate bean
     * hoặc có 1 primary bean
     */
    @Bean
    @ConditionalOnSingleCandidate(DataSource.class)
    public String singleDataSource() {
        return "Single DataSource bean available";
    }
}
