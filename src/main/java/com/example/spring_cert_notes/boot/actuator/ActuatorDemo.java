package com.example.spring_cert_notes.boot.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * BÀI 5: SPRING BOOT ACTUATOR
 * 
 * Actuator cung cấp production-ready features:
 * - Health checks
 * - Metrics
 * - Info
 * - Environment
 * - Logging levels
 * 
 * ============================================================
 * ACTUATOR ENDPOINTS
 * ============================================================
 * 
 * /actuator/health     - Application health
 * /actuator/info       - Application info
 * /actuator/metrics    - Application metrics
 * /actuator/env        - Environment properties
 * /actuator/beans      - All beans in context
 * /actuator/mappings   - All @RequestMapping paths
 * /actuator/loggers    - Logger configurations
 * /actuator/conditions - Auto-configuration conditions
 * /actuator/configprops - @ConfigurationProperties
 * /actuator/threaddump - Thread dump
 * /actuator/heapdump   - Heap dump
 * /actuator/shutdown   - Shutdown application (disabled by default)
 * 
 * ============================================================
 * CONFIGURATION
 * ============================================================
 * 
 * # Expose all endpoints
 * management.endpoints.web.exposure.include=*
 * 
 * # Expose specific endpoints
 * management.endpoints.web.exposure.include=health,info,metrics
 * 
 * # Exclude endpoints
 * management.endpoints.web.exposure.exclude=env,beans
 * 
 * # Show health details
 * management.endpoint.health.show-details=always
 * 
 * # Custom base path
 * management.endpoints.web.base-path=/manage
 * 
 * # Custom port
 * management.server.port=8081
 */
@Component
public class ActuatorDemo {
    // This class serves as documentation
    // See CustomHealthIndicator for custom health check implementation
}
