# NGÀY 8-9: ASPECT ORIENTED PROGRAMMING (AOP)

## 📚 MỤC TIÊU HỌC TẬP

### 1. Nắm vững AOP concepts: Aspect, Join Point, Pointcut, Advice, Weaving
### 2. Thành thạo 5 loại Advice
### 3. Viết Pointcut expressions phức tạp
### 4. Hiểu @Order cho multiple aspects

---

## 🎯 PHẦN 1: AOP CONCEPTS

### Terminology

```
┌─────────────────────────────────────────────────────────────┐
│                    AOP CONCEPTS                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ASPECT = Cross-cutting concern (logging, security, etc.)  │
│           Combination of Pointcut + Advice                  │
│                                                             │
│  JOIN POINT = Point in program execution                    │
│               (method call, exception, field access)        │
│               In Spring AOP: only method execution          │
│                                                             │
│  POINTCUT = Expression that selects join points             │
│             "Where to apply the advice"                     │
│                                                             │
│  ADVICE = Action taken at join point                        │
│           "What to do"                                      │
│                                                             │
│  WEAVING = Process of applying aspects to target            │
│            Compile-time, Load-time, or Runtime              │
│            Spring uses Runtime weaving (proxies)            │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Visual Example

```
                    ASPECT (LoggingAspect)
                    ┌─────────────────────┐
                    │ POINTCUT:           │
                    │ execution(* *.*(..))│
                    │                     │
                    │ ADVICE:             │
                    │ @Before: log start  │
                    │ @After: log end     │
                    └─────────────────────┘
                              │
                              │ WEAVING
                              ↓
    ┌─────────────────────────────────────────────────────┐
    │                   TARGET OBJECT                      │
    │  ┌─────────────────────────────────────────────┐    │
    │  │ JOIN POINT: orderService.createOrder()      │    │
    │  │ JOIN POINT: orderService.cancelOrder()      │    │
    │  │ JOIN POINT: paymentService.process()        │    │
    │  └─────────────────────────────────────────────┘    │
    └─────────────────────────────────────────────────────┘
```

---

## 🎯 PHẦN 2: 5 LOẠI ADVICE

### Advice Execution Order

```
┌─────────────────────────────────────────────────────────────┐
│                    ADVICE ORDER                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  @Around (before proceed)                                   │
│      ↓                                                      │
│  @Before                                                    │
│      ↓                                                      │
│  ═══════════════════════════════════                       │
│  ║     TARGET METHOD EXECUTION     ║                       │
│  ═══════════════════════════════════                       │
│      ↓                                                      │
│  @AfterReturning (if success)                              │
│  @AfterThrowing (if exception)                             │
│      ↓                                                      │
│  @After (always - finally)                                 │
│      ↓                                                      │
│  @Around (after proceed)                                   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 1. @Before
```java
@Before("execution(* com.example.service.*.*(..))")
public void logBefore(JoinPoint joinPoint) {
    String method = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();
    System.out.println("Before: " + method + " with args: " + Arrays.toString(args));
}
```

**Use cases:**
- Logging method entry
- Security checks
- Validation
- Setting up context

### 2. @After (finally)
```java
@After("execution(* com.example.service.*.*(..))")
public void logAfter(JoinPoint joinPoint) {
    String method = joinPoint.getSignature().getName();
    System.out.println("After: " + method + " (always executes)");
}
```

**Use cases:**
- Cleanup resources
- Logging method exit
- Always-execute logic

### 3. @AfterReturning
```java
@AfterReturning(pointcut = "execution(* com.example.service.*.*(..))", 
                returning = "result")
public void logAfterReturning(JoinPoint joinPoint, Object result) {
    String method = joinPoint.getSignature().getName();
    System.out.println("AfterReturning: " + method + " returned: " + result);
}
```

**Use cases:**
- Logging return values
- Caching results
- Post-processing results

### 4. @AfterThrowing
```java
@AfterThrowing(pointcut = "execution(* com.example.service.*.*(..))", 
               throwing = "ex")
public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
    String method = joinPoint.getSignature().getName();
    System.out.println("AfterThrowing: " + method + " threw: " + ex.getMessage());
}
```

**Use cases:**
- Exception logging
- Error notifications
- Exception transformation

### 5. @Around (most powerful)
```java
@Around("execution(* com.example.service.*.*(..))")
public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    
    try {
        Object result = joinPoint.proceed();  // Execute method
        
        long time = System.currentTimeMillis() - start;
        System.out.println("Method executed in " + time + "ms");
        
        return result;
    } catch (Throwable ex) {
        System.out.println("Method failed: " + ex.getMessage());
        throw ex;
    }
}
```

**Use cases:**
- Performance monitoring
- Caching
- Retry logic
- Transaction management
- Security

**Xem code:** `LoggingAspect.java`

---

## 🎯 PHẦN 3: POINTCUT EXPRESSIONS

### Syntax

```
execution(modifiers? return-type declaring-type? method-name(params) throws?)
```

### Common Patterns

```java
// All methods in package
execution(* com.example.service.*.*(..))

// All public methods
execution(public * *(..))

// Methods returning String
execution(String *(..))

// Methods starting with "find"
execution(* find*(..))

// Methods with String first argument
execution(* *(String, ..))

// Methods with exactly 2 arguments
execution(* *(*, *))
```

### Pointcut Designators

| Designator | Description | Example |
|------------|-------------|---------|
| `execution` | Method execution | `execution(* *.*(..))` |
| `within` | Within type | `within(com.example.service..*)` |
| `@within` | Within annotated type | `@within(Service)` |
| `@annotation` | Annotated method | `@annotation(Transactional)` |
| `this` | Proxy type | `this(PaymentService)` |
| `target` | Target type | `target(OrderService)` |
| `args` | Method arguments | `args(String, ..)` |
| `@args` | Annotated args | `@args(Entity)` |
| `bean` | Bean name | `bean(*Service)` |

### Combining Pointcuts

```java
// AND
@Pointcut("execution(* *(..)) && @annotation(Auditable)")
public void auditableExecution() {}

// OR
@Pointcut("@within(Service) || @within(Repository)")
public void serviceOrRepository() {}

// NOT
@Pointcut("execution(* *(..)) && !execution(* get*(..))")
public void nonGetterMethods() {}
```

**Xem code:** `PointcutExamples.java`

---

## 🎯 PHẦN 4: 5 PRACTICAL ASPECTS

### Aspect 1: Logging
```java
@Aspect
@Component
@Order(1)
public class LoggingAspect {
    @Around("execution(* com.example.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint jp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = jp.proceed();
        long time = System.currentTimeMillis() - start;
        System.out.println(jp.getSignature() + " executed in " + time + "ms");
        return result;
    }
}
```

### Aspect 2: Security
```java
@Aspect
@Component
@Order(0)  // Highest priority
public class SecurityAspect {
    @Before("@annotation(secured)")
    public void checkSecurity(JoinPoint jp, Secured secured) {
        String[] roles = secured.roles();
        // Check if user has required roles
        if (!hasAccess(roles)) {
            throw new SecurityException("Access denied");
        }
    }
}
```

### Aspect 3: Audit
```java
@Aspect
@Component
@Order(2)
public class AuditAspect {
    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void audit(JoinPoint jp, Auditable auditable, Object result) {
        // Log: who, what, when, result
        System.out.println("User: " + getCurrentUser());
        System.out.println("Action: " + auditable.action());
        System.out.println("Result: " + result);
    }
}
```

### Aspect 4: Retry
```java
@Aspect
@Component
@Order(3)
public class RetryAspect {
    @Around("@annotation(retry)")
    public Object retryOnFailure(ProceedingJoinPoint jp, Retry retry) throws Throwable {
        int attempts = retry.maxAttempts();
        Throwable lastEx = null;
        
        for (int i = 0; i < attempts; i++) {
            try {
                return jp.proceed();
            } catch (Throwable ex) {
                lastEx = ex;
                Thread.sleep(retry.delay());
            }
        }
        throw lastEx;
    }
}
```

### Aspect 5: Exception Handling
```java
@Aspect
@Component
@Order(4)
public class ExceptionAspect {
    @AfterThrowing(pointcut = "within(com.example.service..*)", throwing = "ex")
    public void handleException(JoinPoint jp, Exception ex) {
        // Log exception
        // Send alert
        // Transform exception
    }
}
```

**Xem code:** `LoggingAspect.java`, `SecurityAspect.java`, `AuditAspect.java`, `RetryAspect.java`, `ExceptionHandlingAspect.java`

---

## 🎯 PHẦN 5: @ORDER - MULTIPLE ASPECTS

### Aspect Ordering

```
Lower @Order value = Higher priority = Executes first

@Order(0) SecurityAspect    ← First (outermost)
@Order(1) LoggingAspect
@Order(2) AuditAspect
@Order(3) RetryAspect
@Order(4) ExceptionAspect   ← Last (innermost)
```

### Execution Flow

```
SecurityAspect.before()
  └→ LoggingAspect.before()
       └→ AuditAspect.before()
            └→ RetryAspect.around()
                 └→ TARGET METHOD
            └→ RetryAspect.around()
       └→ AuditAspect.after()
  └→ LoggingAspect.after()
SecurityAspect.after()
```

---

## 🎯 PHẦN 6: DEBUGGING AOP

### Common Issues

**1. Aspect not applied**
- Missing @EnableAspectJAutoProxy
- Missing @Component on aspect
- Pointcut doesn't match
- Self-invocation (method calling itself)

**2. Self-invocation problem**
```java
@Service
public class OrderService {
    public void createOrder() {
        // This call BYPASSES the proxy!
        this.validateOrder();  // ❌ Aspect not applied
    }
    
    @Validated
    public void validateOrder() { }
}

// Solution: Inject self or use AopContext
@Service
public class OrderService {
    @Autowired
    private OrderService self;  // Inject proxy
    
    public void createOrder() {
        self.validateOrder();  // ✅ Goes through proxy
    }
}
```

**3. Check if proxy**
```java
import org.springframework.aop.support.AopUtils;

boolean isProxy = AopUtils.isAopProxy(bean);
boolean isJdk = AopUtils.isJdkDynamicProxy(bean);
boolean isCglib = AopUtils.isCglibProxy(bean);
```

---

## ✅ CHECKLIST HOÀN THÀNH

### AOP Concepts
- [ ] Aspect = Pointcut + Advice
- [ ] Join Point = Method execution point
- [ ] Pointcut = Expression selecting join points
- [ ] Advice = Action at join point
- [ ] Weaving = Applying aspects (runtime in Spring)

### 5 Advice Types
- [ ] @Before - Before method
- [ ] @After - After method (finally)
- [ ] @AfterReturning - After successful return
- [ ] @AfterThrowing - After exception
- [ ] @Around - Wraps method (most powerful)

### Pointcut Expressions
- [ ] execution(* com.example..*.*(..))
- [ ] @annotation(Transactional)
- [ ] within(com.example.service..*)
- [ ] @within(Service)
- [ ] this(PaymentService)
- [ ] target(OrderServiceImpl)
- [ ] args(String, ..)
- [ ] bean(*Service)
- [ ] Combining: &&, ||, !

### 5 Practical Aspects
- [ ] Logging aspect
- [ ] Security aspect
- [ ] Audit aspect
- [ ] Retry aspect
- [ ] Exception handling aspect

### Multiple Aspects
- [ ] @Order annotation
- [ ] Lower value = higher priority
- [ ] Execution order understanding

### Debugging
- [ ] Self-invocation problem
- [ ] AopUtils for proxy detection
- [ ] @EnableAspectJAutoProxy

---

## 🚀 CÁCH CHẠY DEMO

```bash
mvn exec:java -Dexec.mainClass="com.example.spring_cert_notes.aop.AopDemo"
```

---

## 📝 GHI CHÚ QUAN TRỌNG

### Best Practices

1. **Use @Order for multiple aspects**
   - Security first (lowest number)
   - Logging/Audit last

2. **Keep aspects focused**
   - One concern per aspect
   - Don't mix logging with security

3. **Be careful with @Around**
   - Always call proceed()
   - Handle exceptions properly
   - Return the result

4. **Avoid self-invocation**
   - Inject self reference
   - Or use AopContext.currentProxy()

### Common Mistakes

1. ❌ Forgetting @EnableAspectJAutoProxy
2. ❌ Missing @Component on aspect
3. ❌ Wrong pointcut expression
4. ❌ Self-invocation bypassing proxy
5. ❌ Not calling proceed() in @Around
6. ❌ Swallowing exceptions in @Around

---

## 📚 FILES TRONG PACKAGE

**Aspects:**
1. `LoggingAspect.java` - 5 advice types demo
2. `SecurityAspect.java` - @Secured annotation
3. `AuditAspect.java` - @Auditable annotation
4. `RetryAspect.java` - @Retry annotation
5. `ExceptionHandlingAspect.java` - Exception handling

**Annotations:**
6. `Secured.java` - Security annotation
7. `Auditable.java` - Audit annotation
8. `Retry.java` - Retry annotation

**Services:**
9. `OrderService.java` - Demo service
10. `PaymentService.java` - Interface
11. `PaymentServiceImpl.java` - Implementation

**Reference:**
12. `PointcutExamples.java` - All pointcut patterns
13. `AopConfig.java` - Configuration
14. `AopDemo.java` - Main demo
