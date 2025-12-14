package com.example.spring_cert_notes.aop;

import com.example.spring_cert_notes.Prefixes;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * ASPECT 1: LOGGING ASPECT
 * 
 * Demonstrates all 5 types of Advice:
 * 1. @Before - Before method execution
 * 2. @After - After method (finally)
 * 3. @AfterReturning - After successful return
 * 4. @AfterThrowing - After exception thrown
 * 5. @Around - Wraps method execution
 */
@Aspect
@Component
@Order(1)  // Lower number = higher priority
public class LoggingAspect {
    
    // ============================================================
    // POINTCUT DEFINITIONS
    // ============================================================
    
    // Match all methods in service package
    @Pointcut("execution(* com.example.spring_cert_notes.aop.service.*.*(..))")
    public void serviceLayer() {}
    
    // Match all public methods
    @Pointcut("execution(public * *(..))")
    public void publicMethod() {}
    
    // Combine pointcuts
    @Pointcut("serviceLayer() && publicMethod()")
    public void publicServiceMethod() {}
    
    // ============================================================
    // 1. @BEFORE ADVICE
    // ============================================================
    @Before("serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        
        System.out.println(Prefixes.CORE_AOP + 
            "[BEFORE] " + className + "." + methodName + "() - Args: " + Arrays.toString(args));
    }
    
    // ============================================================
    // 2. @AFTER ADVICE (finally - always executes)
    // ============================================================
    @After("serviceLayer()")
    public void logAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println(Prefixes.CORE_AOP + 
            "[AFTER] " + methodName + "() completed (finally)");
    }
    
    // ============================================================
    // 3. @AFTERRETURNING ADVICE
    // ============================================================
    @AfterReturning(pointcut = "serviceLayer()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println(Prefixes.CORE_AOP + 
            "[AFTER_RETURNING] " + methodName + "() returned: " + result);
    }
    
    // ============================================================
    // 4. @AFTERTHROWING ADVICE
    // ============================================================
    @AfterThrowing(pointcut = "serviceLayer()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println(Prefixes.CORE_AOP + 
            "[AFTER_THROWING] " + methodName + "() threw: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
    }
    
    // ============================================================
    // 5. @AROUND ADVICE (most powerful)
    // ============================================================
    @Around("execution(* com.example.spring_cert_notes.aop.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        System.out.println(Prefixes.CORE_AOP + 
            "[AROUND-START] " + className + "." + methodName + "()");
        
        long start = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();  // Execute the method
            
            long executionTime = System.currentTimeMillis() - start;
            System.out.println(Prefixes.CORE_AOP + 
                "[AROUND-END] " + methodName + "() executed in " + executionTime + "ms");
            
            return result;
        } catch (Throwable ex) {
            long executionTime = System.currentTimeMillis() - start;
            System.out.println(Prefixes.CORE_AOP + 
                "[AROUND-ERROR] " + methodName + "() failed after " + executionTime + "ms");
            throw ex;
        }
    }
}
