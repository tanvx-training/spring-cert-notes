package com.example.spring_cert_notes.aop;

import com.example.spring_cert_notes.Prefixes;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * ASPECT 4: RETRY ASPECT
 * 
 * Automatically retries failed method calls
 * Useful for transient failures (network, database)
 */
@Aspect
@Component
@Order(3)
public class RetryAspect {
    
    // ============================================================
    // POINTCUT: Methods annotated with @Retry
    // ============================================================
    @Pointcut("@annotation(retry)")
    public void retryableMethod(Retry retry) {}
    
    // ============================================================
    // RETRY LOGIC ADVICE
    // ============================================================
    @Around("retryableMethod(retry)")
    public Object retryOnFailure(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        int maxAttempts = retry.maxAttempts();
        long delay = retry.delay();
        
        Throwable lastException = null;
        
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                System.out.println(Prefixes.CORE_AOP + 
                    "[RETRY] Attempt " + attempt + "/" + maxAttempts + " for " + methodName + "()");
                
                return joinPoint.proceed();
                
            } catch (Throwable ex) {
                lastException = ex;
                System.out.println(Prefixes.CORE_AOP + 
                    "[RETRY] Attempt " + attempt + " failed: " + ex.getMessage());
                
                if (attempt < maxAttempts) {
                    System.out.println(Prefixes.CORE_AOP + 
                        "[RETRY] Waiting " + delay + "ms before retry...");
                    Thread.sleep(delay);
                }
            }
        }
        
        System.out.println(Prefixes.CORE_AOP + 
            "[RETRY] All " + maxAttempts + " attempts failed for " + methodName + "()");
        throw lastException;
    }
}
