package com.example.spring_cert_notes.aop;

import com.example.spring_cert_notes.Prefixes;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * ASPECT 2: SECURITY ASPECT
 * 
 * Demonstrates @annotation pointcut for custom annotations
 * Checks if user has required roles before method execution
 */
@Aspect
@Component
@Order(0)  // Highest priority - security check first!
public class SecurityAspect {
    
    // Simulated current user roles (in real app, get from SecurityContext)
    private static final List<String> CURRENT_USER_ROLES = Arrays.asList("USER", "ADMIN");
    
    // ============================================================
    // POINTCUT: Methods annotated with @Secured
    // ============================================================
    @Pointcut("@annotation(secured)")
    public void securedMethod(Secured secured) {}
    
    // ============================================================
    // SECURITY CHECK ADVICE
    // ============================================================
    @Before("securedMethod(secured)")
    public void checkSecurity(JoinPoint joinPoint, Secured secured) {
        String methodName = joinPoint.getSignature().getName();
        String[] requiredRoles = secured.roles();
        
        System.out.println(Prefixes.CORE_AOP + 
            "[SECURITY] Checking access for: " + methodName);
        System.out.println(Prefixes.CORE_AOP + 
            "[SECURITY] Required roles: " + Arrays.toString(requiredRoles));
        System.out.println(Prefixes.CORE_AOP + 
            "[SECURITY] User roles: " + CURRENT_USER_ROLES);
        
        // Check if user has any required role
        boolean hasAccess = false;
        for (String role : requiredRoles) {
            if (CURRENT_USER_ROLES.contains(role)) {
                hasAccess = true;
                break;
            }
        }
        
        if (!hasAccess) {
            System.out.println(Prefixes.CORE_AOP + 
                "[SECURITY] ACCESS DENIED!");
            throw new SecurityException("Access denied. Required roles: " + Arrays.toString(requiredRoles));
        }
        
        System.out.println(Prefixes.CORE_AOP + 
            "[SECURITY] ACCESS GRANTED");
    }
}
