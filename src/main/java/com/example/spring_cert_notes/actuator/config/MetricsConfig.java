package com.example.spring_cert_notes.actuator.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metrics Configuration
 * 
 * Cấu hình Micrometer metrics.
 */
@Configuration
public class MetricsConfig {
    
    /**
     * Enable @Timed annotation
     * 
     * Cho phép sử dụng @Timed trên methods để tự động record timing.
     */
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
    
    /**
     * Customize MeterRegistry
     * 
     * Thêm common tags cho tất cả metrics.
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config()
            .commonTags("application", "spring-cert-notes")
            .commonTags("region", "local");
    }
}
