package com.example.spring_cert_notes.boot.autoconfigure;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * BÀI 2C: CUSTOM CONDITION
 * 
 * Tạo custom condition bằng cách implement Condition interface.
 * Sử dụng với @Conditional annotation.
 */
public class CustomCondition implements Condition {
    
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // Lấy environment
        String env = context.getEnvironment().getProperty("app.environment");
        
        // Kiểm tra điều kiện
        boolean isProduction = "production".equalsIgnoreCase(env);
        
        // Có thể kiểm tra nhiều điều kiện khác:
        // - context.getBeanFactory() - access bean factory
        // - context.getClassLoader() - check classes on classpath
        // - context.getResourceLoader() - check resources
        // - context.getRegistry() - check registered beans
        
        return isProduction;
    }
}
