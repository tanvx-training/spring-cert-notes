package com.example.spring_cert_notes.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * POINTCUT EXPRESSION EXAMPLES
 * 
 * This class contains examples of various pointcut expressions.
 * Not an active aspect - just for reference.
 */
@Aspect
public class PointcutExamples {
    
    // ============================================================
    // 1. EXECUTION - Match method execution
    // ============================================================
    
    // All methods in package
    @Pointcut("execution(* com.example.spring_cert_notes.aop.service.*.*(..))")
    public void allServiceMethods() {}
    
    // All public methods
    @Pointcut("execution(public * *(..))")
    public void allPublicMethods() {}
    
    // Methods returning void
    @Pointcut("execution(void *(..))")
    public void voidMethods() {}
    
    // Methods with specific name
    @Pointcut("execution(* *..find*(..))")
    public void findMethods() {}
    
    // Methods with specific return type
    @Pointcut("execution(String *(..))")
    public void stringReturningMethods() {}
    
    // Methods with specific arguments
    @Pointcut("execution(* *(String, ..))")
    public void methodsWithStringFirstArg() {}
    
    // Methods with exact arguments
    @Pointcut("execution(* *(String, int))")
    public void methodsWithStringAndInt() {}
    
    // ============================================================
    // 2. WITHIN - Match within certain types
    // ============================================================
    
    // All methods in specific class
    @Pointcut("within(com.example.spring_cert_notes.aop.service.OrderService)")
    public void withinOrderService() {}
    
    // All methods in package and subpackages
    @Pointcut("within(com.example.spring_cert_notes.aop.service..*)")
    public void withinServicePackage() {}
    
    // ============================================================
    // 3. @WITHIN - Match within annotated types
    // ============================================================
    
    // All methods in @Service classes
    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void withinServiceAnnotation() {}
    
    // All methods in @Repository classes
    @Pointcut("@within(org.springframework.stereotype.Repository)")
    public void withinRepositoryAnnotation() {}
    
    // ============================================================
    // 4. @ANNOTATION - Match annotated methods
    // ============================================================
    
    // Methods annotated with @Transactional
    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void transactionalMethods() {}
    
    // Methods annotated with custom annotation
    @Pointcut("@annotation(com.example.spring_cert_notes.aop.Auditable)")
    public void auditableMethods() {}
    
    // ============================================================
    // 5. THIS - Match proxy type
    // ============================================================
    
    // Proxy implements interface
    @Pointcut("this(com.example.spring_cert_notes.aop.service.PaymentService)")
    public void proxyImplementsPaymentService() {}
    
    // ============================================================
    // 6. TARGET - Match target object type
    // ============================================================
    
    // Target is specific type
    @Pointcut("target(com.example.spring_cert_notes.aop.service.OrderService)")
    public void targetIsOrderService() {}
    
    // ============================================================
    // 7. ARGS - Match method arguments
    // ============================================================
    
    // Methods with String argument
    @Pointcut("args(String)")
    public void methodsWithStringArg() {}
    
    // Methods with String first, any others
    @Pointcut("args(String, ..)")
    public void methodsWithStringFirst() {}
    
    // Methods with two arguments
    @Pointcut("args(*, *)")
    public void methodsWithTwoArgs() {}
    
    // ============================================================
    // 8. @ARGS - Match annotated argument types
    // ============================================================
    
    // Methods with @Entity annotated argument
    @Pointcut("@args(jakarta.persistence.Entity)")
    public void methodsWithEntityArg() {}
    
    // ============================================================
    // 9. BEAN - Match Spring bean names
    // ============================================================
    
    // Specific bean
    @Pointcut("bean(orderService)")
    public void orderServiceBean() {}
    
    // Beans matching pattern
    @Pointcut("bean(*Service)")
    public void allServiceBeans() {}
    
    // ============================================================
    // 10. COMBINING POINTCUTS
    // ============================================================
    
    // AND
    @Pointcut("execution(* *(..)) && @annotation(Auditable)")
    public void auditableExecution() {}
    
    // OR
    @Pointcut("@within(org.springframework.stereotype.Service) || @within(org.springframework.stereotype.Repository)")
    public void serviceOrRepository() {}
    
    // NOT
    @Pointcut("execution(* *(..)) && !@annotation(Auditable)")
    public void notAuditable() {}
    
    // Complex combination
    @Pointcut("within(com.example..*) && execution(public * *(..)) && !execution(* get*(..))")
    public void publicNonGetterMethods() {}
}
