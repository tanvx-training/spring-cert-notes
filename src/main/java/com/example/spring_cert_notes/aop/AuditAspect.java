package com.example.spring_cert_notes.aop;

import com.example.spring_cert_notes.Prefixes;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * ASPECT 3: AUDIT ASPECT
 * 
 * Logs audit trail for methods annotated with @Auditable
 * Records: who, what, when, result
 */
@Aspect
@Component
@Order(2)
public class AuditAspect {
    
    // Simulated current user (in real app, get from SecurityContext)
    private static final String CURRENT_USER = "john.doe";
    
    // ============================================================
    // POINTCUT: Methods annotated with @Auditable
    // ============================================================
    @Pointcut("@annotation(auditable)")
    public void auditableMethod(Auditable auditable) {}
    
    // ============================================================
    // AUDIT LOGGING ADVICE
    // ============================================================
    @AfterReturning(pointcut = "auditableMethod(auditable)", returning = "result")
    public void audit(JoinPoint joinPoint, Auditable auditable, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        String action = auditable.action().isEmpty() ? methodName : auditable.action();
        
        System.out.println(Prefixes.CORE_AOP + "[AUDIT] ================================");
        System.out.println(Prefixes.CORE_AOP + "[AUDIT] Timestamp: " + LocalDateTime.now());
        System.out.println(Prefixes.CORE_AOP + "[AUDIT] User: " + CURRENT_USER);
        System.out.println(Prefixes.CORE_AOP + "[AUDIT] Action: " + action);
        System.out.println(Prefixes.CORE_AOP + "[AUDIT] Class: " + className);
        System.out.println(Prefixes.CORE_AOP + "[AUDIT] Method: " + methodName);
        System.out.println(Prefixes.CORE_AOP + "[AUDIT] Args: " + Arrays.toString(args));
        System.out.println(Prefixes.CORE_AOP + "[AUDIT] Result: " + result);
        System.out.println(Prefixes.CORE_AOP + "[AUDIT] ================================");
    }
}
