package com.example.spring_cert_notes.core.lifecycle;

import com.example.spring_cert_notes.Prefixes;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * LOGGING ASPECT
 * 
 * This aspect triggers proxy creation for beans it advises.
 * Used to demonstrate JDK vs CGLIB proxy behavior.
 */
@Aspect
@Component
public class LoggingAspect {
    
    @Around("execution(* com.example.spring_cert_notes.core.lifecycle.*Service*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        System.out.println(Prefixes.CORE_AOP + 
            "[ASPECT] Before: " + className + "." + methodName + "()");
        
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        
        System.out.println(Prefixes.CORE_AOP + 
            "[ASPECT] After: " + className + "." + methodName + "() - " + duration + "ms");
        
        return result;
    }
}
