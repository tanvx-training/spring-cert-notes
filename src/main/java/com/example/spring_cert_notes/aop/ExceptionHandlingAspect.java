package com.example.spring_cert_notes.aop;

import com.example.spring_cert_notes.Prefixes;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * ASPECT 5: EXCEPTION HANDLING ASPECT
 * 
 * Centralized exception handling and logging
 * Can transform exceptions, send alerts, etc.
 */
@Aspect
@Component
@Order(4)
public class ExceptionHandlingAspect {
    
    // ============================================================
    // POINTCUT: All methods in service package
    // ============================================================
    @Pointcut("within(com.example.spring_cert_notes.aop.service..*)")
    public void inServicePackage() {}
    
    // ============================================================
    // EXCEPTION HANDLING ADVICE
    // ============================================================
    @AfterThrowing(pointcut = "inServicePackage()", throwing = "ex")
    public void handleException(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        System.out.println(Prefixes.CORE_AOP + "[EXCEPTION] ================================");
        System.out.println(Prefixes.CORE_AOP + "[EXCEPTION] Class: " + className);
        System.out.println(Prefixes.CORE_AOP + "[EXCEPTION] Method: " + methodName);
        System.out.println(Prefixes.CORE_AOP + "[EXCEPTION] Type: " + ex.getClass().getName());
        System.out.println(Prefixes.CORE_AOP + "[EXCEPTION] Message: " + ex.getMessage());
        System.out.println(Prefixes.CORE_AOP + "[EXCEPTION] ================================");
        
        // In real app: send alert, log to monitoring system, etc.
    }
}
