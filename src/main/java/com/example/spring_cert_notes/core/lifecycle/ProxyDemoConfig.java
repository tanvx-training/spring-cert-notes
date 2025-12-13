package com.example.spring_cert_notes.core.lifecycle;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * PROXY CONFIGURATION
 * 
 * @EnableAspectJAutoProxy options:
 * - proxyTargetClass = false (default): Use JDK proxy if interface exists
 * - proxyTargetClass = true: Always use CGLIB proxy
 * 
 * JDK Dynamic Proxy:
 * - Requires interface
 * - Creates proxy implementing the interface
 * - Faster to create
 * - Bean must be accessed via interface type
 * 
 * CGLIB Proxy:
 * - No interface required
 * - Creates subclass of target class
 * - Class cannot be final
 * - Methods cannot be final
 * - Slightly slower to create
 * - Can access via concrete class type
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = false)  // Default: prefer JDK proxy
public class ProxyDemoConfig {
    // With proxyTargetClass = false:
    // - PaymentServiceImpl → JDK Proxy (has interface)
    // - OrderService → CGLIB Proxy (no interface)
    
    // With proxyTargetClass = true:
    // - Both → CGLIB Proxy
}
