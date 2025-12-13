package com.example.spring_cert_notes.core.lifecycle;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuration for Lifecycle Demo
 */
@Configuration
@ComponentScan(basePackages = "com.example.spring_cert_notes.core.lifecycle")
@EnableAspectJAutoProxy
public class LifecycleConfig {
}
