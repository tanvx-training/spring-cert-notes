package com.example.spring_cert_notes.boot.autoconfigure.greeting;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * BÀI 2: CUSTOM AUTO-CONFIGURATION
 * 
 * Auto-configuration class tự động cấu hình beans dựa trên conditions.
 * 
 * Các @Conditional annotations quan trọng:
 * - @ConditionalOnClass: Bean chỉ được tạo nếu class tồn tại trên classpath
 * - @ConditionalOnMissingClass: Bean chỉ được tạo nếu class KHÔNG tồn tại
 * - @ConditionalOnBean: Bean chỉ được tạo nếu bean khác đã tồn tại
 * - @ConditionalOnMissingBean: Bean chỉ được tạo nếu bean khác KHÔNG tồn tại
 * - @ConditionalOnProperty: Bean chỉ được tạo nếu property có giá trị cụ thể
 * - @ConditionalOnResource: Bean chỉ được tạo nếu resource tồn tại
 * - @ConditionalOnWebApplication: Bean chỉ được tạo trong web application
 * - @ConditionalOnNotWebApplication: Bean chỉ được tạo KHÔNG phải web app
 * - @ConditionalOnExpression: Bean chỉ được tạo nếu SpEL expression = true
 */
@AutoConfiguration
@EnableConfigurationProperties(GreetingProperties.class)
@ConditionalOnProperty(
    prefix = "greeting",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true  // Default enabled nếu property không được set
)
public class GreetingAutoConfiguration {
    
    /**
     * Tạo GreetingService bean
     * 
     * @ConditionalOnMissingBean: Chỉ tạo nếu user chưa define GreetingService bean
     * Điều này cho phép user override default implementation
     */
    @Bean
    @ConditionalOnMissingBean(GreetingService.class)
    public GreetingService greetingService(GreetingProperties properties) {
        System.out.println("[AUTO-CONFIG] Creating default GreetingService bean");
        return new DefaultGreetingService(properties);
    }
}
