package com.example.spring_cert_notes.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AOP Configuration
 * 
 * @EnableAspectJAutoProxy enables Spring AOP with AspectJ annotations
 * - proxyTargetClass = false: JDK proxy if interface exists (default)
 * - proxyTargetClass = true: Always use CGLIB proxy
 * - exposeProxy = true: Expose proxy via AopContext.currentProxy()
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = "com.example.spring_cert_notes.aop")
public class AopConfig {
}
