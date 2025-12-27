# SPRING AOP - ASPECT-ORIENTED PROGRAMMING
## Guideline Chi Tiết cho Spring Professional Certification

---

**Tài liệu ôn tập toàn diện về AOP trong Spring Framework**

*Tạo ngày: 25/12/2024*

---

## MỤC LỤC

1. [Giới thiệu về AOP](#1-giới-thiệu-về-aop)
2. [Các khái niệm quan trọng trong AOP](#2-các-khái-niệm-quan-trọng-trong-aop)
3. [Các loại Advice chi tiết](#3-các-loại-advice-chi-tiết)
4. [Pointcut Expressions](#4-pointcut-expressions)
5. [Kích hoạt AOP trong Spring](#5-kích-hoạt-aop-trong-spring)
6. [Ví dụ thực tế](#6-ví-dụ-thực-tế)
7. [JoinPoint và ProceedingJoinPoint](#7-joinpoint-và-proceedingjoinpoint)
8. [Best Practices](#8-best-practices)
9. [Câu hỏi mẫu cho kỳ thi](#9-câu-hỏi-mẫu-cho-kỳ-thi)
10. [Tóm tắt và mẹo thi](#10-tóm-tắt-và-mẹo-thi)

---

## 1. GIỚI THIỆU VỀ AOP

### 1.1. AOP là gì?

**Aspect-Oriented Programming (AOP)** là một paradigm lập trình bổ sung cho Object-Oriented Programming (OOP). AOP cho phép tách biệt **cross-cutting concerns** khỏi business logic, giúp code dễ bảo trì và tái sử dụng hơn.

### 1.2. Cross-Cutting Concerns là gì?

Cross-cutting concerns là các chức năng xuất hiện ở nhiều nơi trong ứng dụng nhưng không liên quan trực tiếp đến business logic.

**Ví dụ về Cross-Cutting Concerns:**

- **Logging**: Ghi log khi vào/ra methods
- **Security**: Kiểm tra quyền truy cập
- **Transaction Management**: Quản lý transactions
- **Performance Monitoring**: Đo thời gian thực thi
- **Caching**: Cache kết quả methods
- **Error Handling**: Xử lý exceptions tập trung

### 1.3. Vấn đề không dùng AOP

Khi không dùng AOP, code sẽ bị duplicate và rối:

```java
public class UserService {
    public void createUser(User user) {
        // Logging
        logger.info("Creating user: " + user.getName());
        
        // Security check
        if (!securityContext.hasPermission("CREATE_USER")) {
            throw new SecurityException();
        }
        
        // Transaction start
        transactionManager.begin();
        
        try {
            // Business logic
            userRepository.save(user);
            
            // Transaction commit
            transactionManager.commit();
            
            // Logging
            logger.info("User created successfully");
        } catch (Exception e) {
            transactionManager.rollback();
            logger.error("Error creating user", e);
            throw e;
        }
    }
}
```

> ⚠️ **Vấn đề**: Code bị lẫn lộn giữa business logic và infrastructure concerns. Code này phải lặp lại ở mọi method!

### 1.4. Giải pháp với AOP

Với AOP, code trở nên sạch sẽ:

```java
public class UserService {
    @Transactional
    @Secured("ROLE_ADMIN")
    @Loggable
    public void createUser(User user) {
        // Chỉ business logic
        userRepository.save(user);
    }
}
```

> ✅ **Lợi ích**: Business logic rõ ràng, cross-cutting concerns được tách riêng, code dễ maintain!

---

## 2. CÁC KHÁI NIỆM QUAN TRỌNG TRONG AOP

### 2.1. Aspect

**Aspect** là một module hóa của cross-cutting concern. Đây là class chứa advice và pointcut.

```java
@Aspect
@Component
public class LoggingAspect {
    // Advice và Pointcut ở đây
}
```

### 2.2. Join Point

**Join Point** là một điểm trong execution của chương trình nơi aspect có thể được áp dụng.

Các loại Join Points:
- Method call
- Method execution
- Constructor call
- Field access

> ⚠️ **QUAN TRỌNG**: Spring AOP chỉ support **METHOD EXECUTION** join points!

### 2.3. Pointcut

**Pointcut** là một biểu thức xác định join points nào sẽ được áp dụng advice. Nó giống như một filter để chọn methods.

```java
@Pointcut("execution(* com.example.service.*.*(..))")
public void serviceMethods() {}

// Áp dụng cho tất cả methods trong package service
```

### 2.4. Advice

**Advice** là hành động được thực thi tại join point. Có **5 loại advice** trong Spring AOP:

| Loại Advice | Mô tả |
|-------------|-------|
| **@Before** | Chạy TRƯỚC khi method được thực thi |
| **@After** | Chạy SAU khi method kết thúc (dù success hay exception) |
| **@AfterReturning** | Chạy SAU khi method return thành công (không có exception) |
| **@AfterThrowing** | Chạy SAU khi method throw exception |
| **@Around** | Chạy TRƯỚC VÀ SAU method, có thể quyết định có gọi method hay không |

### 2.5. Target Object

**Target Object** là object được advised bởi một hoặc nhiều aspects. Còn gọi là advised object.

### 2.6. AOP Proxy

Spring AOP tạo **proxy objects** để implement aspect contracts. Spring AOP mặc định sử dụng:

- **JDK Dynamic Proxy**: Khi target object implement ít nhất một interface
- **CGLIB Proxy**: Khi target object không implement interface nào

### 2.7. Weaving

**Weaving** là quá trình liên kết aspects với application code.

Các loại weaving:
- **Compile-time weaving**: AspectJ compiler
- **Load-time weaving**: AspectJ weaver
- **Runtime weaving**: Spring AOP (sử dụng proxies)

---

## 3. CÁC LOẠI ADVICE CHI TIẾT

### 3.1. @Before Advice

`@Before` advice chạy trước khi method được thực thi. Không thể ngăn method execution (trừ khi throw exception).

```java
@Aspect
@Component
public class LoggingAspect {
    
    @Before("execution(* com.example.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        
        log.info("[BEFORE] {}.{}() called with args: {}",
                 className, methodName, Arrays.toString(args));
    }
}
```

**Use cases:**
- Logging method entry
- Security checks
- Parameter validation

### 3.2. @After Advice

`@After` advice chạy sau khi method kết thúc, bất kể thành công hay exception. Giống như finally block.

```java
@Aspect
@Component
public class ResourceManagementAspect {
    
    @After("execution(* com.example.service.*.*(..))")
    public void cleanupResources(JoinPoint joinPoint) {
        log.info("[AFTER] Cleaning up resources after {}",
                 joinPoint.getSignature().getName());
        
        // Cleanup code here
        // Luôn chạy dù có exception hay không
    }
}
```

**Use cases:**
- Resource cleanup
- Releasing locks
- Logging completion

### 3.3. @AfterReturning Advice

`@AfterReturning` advice chạy khi method return thành công. Có thể access return value.

```java
@Aspect
@Component
public class ResultLoggingAspect {
    
    @AfterReturning(
        pointcut = "execution(* com.example.service.*.*(..))",
        returning = "result"
    )
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("[AFTER RETURNING] {}.{}() returned: {}",
                 joinPoint.getTarget().getClass().getSimpleName(),
                 joinPoint.getSignature().getName(),
                 result);
    }
}
```

> ⚠️ **Lưu ý**: `returning` attribute phải match với tên parameter!

**Use cases:**
- Logging successful results
- Result validation
- Caching results

### 3.4. @AfterThrowing Advice

`@AfterThrowing` advice chạy khi method throw exception. Có thể access exception object.

```java
@Aspect
@Component
public class ExceptionHandlingAspect {
    
    @AfterThrowing(
        pointcut = "execution(* com.example.service.*.*(..))",
        throwing = "ex"
    )
    public void logException(JoinPoint joinPoint, Exception ex) {
        log.error("[EXCEPTION] {}.{}() threw exception: {}",
                  joinPoint.getTarget().getClass().getSimpleName(),
                  joinPoint.getSignature().getName(),
                  ex.getMessage());
        
        // Send notification, alert, etc.
    }
}
```

**Use cases:**
- Exception logging
- Error notifications
- Error recovery

### 3.5. @Around Advice

`@Around` advice là powerful nhất, chạy trước và sau method. Có thể quyết định có gọi method hay không, modify arguments và return value.

```java
@Aspect
@Component
public class PerformanceAspect {
    
    @Around("execution(* com.example.service.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint pjp) 
            throws Throwable {
        String methodName = pjp.getSignature().getName();
        
        // BEFORE
        long startTime = System.currentTimeMillis();
        log.info("[START] {} execution started", methodName);
        
        Object result = null;
        try {
            // Proceed với method execution
            result = pjp.proceed();
            
            // AFTER RETURNING
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            log.info("[END] {} executed in {} ms", 
                     methodName, duration);
            
            return result;
        } catch (Exception ex) {
            // AFTER THROWING
            log.error("[ERROR] {} failed", methodName, ex);
            throw ex;
        }
    }
}
```

> ⚠️ **QUAN TRỌNG**: Phải gọi `pjp.proceed()` để method thực thi. Không gọi = method không chạy!

**Use cases:**
- Performance monitoring
- Caching
- Transaction management
- Retry logic

---

## 4. POINTCUT EXPRESSIONS

### 4.1. execution()

`execution()` là designator phổ biến nhất, match method execution.

**Cú pháp:**
```
execution(modifiers? return-type declaring-type? method-name(params) throws?)
```

#### Ví dụ Pointcut Expressions

```java
// Match tất cả public methods
execution(public * *(..))

// Match tất cả methods có tên bắt đầu bằng "get"
execution(* get*(..))

// Match tất cả methods trong package service
execution(* com.example.service.*.*(..))

// Match tất cả methods trong service và sub-packages
execution(* com.example.service..*.*(..))

// Match methods return User
execution(com.example.domain.User *(..))

// Match methods trong UserService
execution(* com.example.service.UserService.*(..))

// Match methods có 1 parameter kiểu String
execution(* *(String))

// Match methods có parameter đầu tiên là String
execution(* *(String,..))

// Match methods throw Exception
execution(* *(..) throws Exception)
```

### 4.2. within()

`within()` giới hạn matching đến certain types:

```java
// Tất cả methods trong UserService
within(com.example.service.UserService)

// Tất cả methods trong package service
within(com.example.service.*)

// Tất cả methods trong service và sub-packages
within(com.example.service..*)
```

### 4.3. @annotation()

`@annotation()` match methods có annotation cụ thể:

```java
// Match methods có @Transactional
@annotation(org.springframework.transaction.annotation.Transactional)

// Match methods có custom annotation
@annotation(com.example.annotation.Loggable)
```

### 4.4. @within()

`@within()` match types (classes) có annotation:

```java
// Match tất cả methods trong classes có @Service
@within(org.springframework.stereotype.Service)

// Match tất cả methods trong classes có @RestController
@within(org.springframework.web.bind.annotation.RestController)
```

### 4.5. bean()

`bean()` match Spring beans by name (Spring-specific):

```java
// Match bean có tên userService
bean(userService)

// Match tất cả beans có tên kết thúc bằng Service
bean(*Service)

// Match tất cả beans NGOẠI TRỪ userService
bean(!userService)
```

### 4.6. Kết hợp Pointcuts

Có thể kết hợp pointcuts với `&&`, `||`, `!`:

```java
// AND: public methods trong service package
execution(public * *(..)) && within(com.example.service.*)

// OR: methods trong UserService hoặc OrderService
within(com.example.service.UserService) || 
within(com.example.service.OrderService)

// NOT: tất cả methods NGOẠI TRỪ getters
execution(* com.example.service.*.*(..)) && 
!execution(* get*(..))

// Complex: public methods trong @Service beans
execution(public * *(..)) && 
@within(org.springframework.stereotype.Service) && 
bean(*Service)
```

### 4.7. Reusable Pointcuts

Định nghĩa pointcuts có thể tái sử dụng:

```java
@Aspect
@Component
public class CommonPointcuts {
    
    @Pointcut("execution(* com.example.service.*.*(..))")
    public void serviceMethods() {}
    
    @Pointcut("execution(public * *(..))")
    public void publicMethods() {}
    
    @Pointcut("within(com.example.repository.*)")
    public void repositoryLayer() {}
    
    @Pointcut("@annotation(com.example.annotation.Loggable)")
    public void loggableMethods() {}
}

// Sử dụng
@Aspect
@Component
public class LoggingAspect {
    
    @Before("CommonPointcuts.serviceMethods()")
    public void logServiceMethod() {
        // ...
    }
    
    @Around("CommonPointcuts.publicMethods() && " +
            "CommonPointcuts.serviceMethods()")
    public Object aroundAdvice(ProceedingJoinPoint pjp) {
        // ...
    }
}
```

---

## 5. KÍCH HOẠT AOP TRONG SPRING

### 5.1. Java Configuration

Sử dụng `@EnableAspectJAutoProxy`:

```java
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {
    // Bean definitions
}
```

> 📝 **Spring Boot**: Tự động enable AOP nếu `spring-boot-starter-aop` có trong classpath!

### 5.2. XML Configuration

```xml
<aop:aspectj-autoproxy/>

<!-- Hoặc force CGLIB proxies -->
<aop:aspectj-autoproxy proxy-target-class="true"/>
```

### 5.3. Spring Boot Dependencies

Thêm dependency trong `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

### 5.4. Proxy Types

Force CGLIB proxies (cho tất cả beans, không chỉ interfaces):

```java
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppConfig {
}
```

Hoặc trong `application.properties`:

```properties
spring.aop.proxy-target-class=true
```

---

## 6. VÍ DỤ THỰC TẾ

### 6.1. Logging Aspect

```java
@Aspect
@Component
@Slf4j
public class LoggingAspect {
    
    @Before("execution(* com.example.service.*.*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        
        log.info("→ Entering {}.{}() with arguments: {}",
                 className, methodName, Arrays.toString(args));
    }
    
    @AfterReturning(
        pointcut = "execution(* com.example.service.*.*(..))",
        returning = "result"
    )
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        log.info("← Exiting {}.{}() with result: {}",
                 className, methodName, result);
    }
    
    @AfterThrowing(
        pointcut = "execution(* com.example.service.*.*(..))",
        throwing = "exception"
    )
    public void logException(JoinPoint joinPoint, Exception exception) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        log.error("✗ Exception in {}.{}(): {}",
                  className, methodName, exception.getMessage());
    }
}
```

### 6.2. Performance Monitoring Aspect

```java
@Aspect
@Component
@Slf4j
public class PerformanceAspect {
    
    @Around("@annotation(com.example.annotation.Monitored)")
    public Object monitorPerformance(ProceedingJoinPoint pjp) 
            throws Throwable {
        String methodName = pjp.getSignature().toShortString();
        
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        try {
            Object result = pjp.proceed();
            stopWatch.stop();
            
            long executionTime = stopWatch.getTotalTimeMillis();
            log.info("⏱ {} executed in {} ms", 
                     methodName, executionTime);
            
            // Alert if too slow
            if (executionTime > 1000) {
                log.warn("⚠ Slow execution detected: {} took {} ms",
                         methodName, executionTime);
            }
            
            return result;
        } catch (Exception ex) {
            stopWatch.stop();
            log.error("✗ {} failed after {} ms",
                      methodName, stopWatch.getTotalTimeMillis());
            throw ex;
        }
    }
}

// Custom annotation
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Monitored {
}
```

### 6.3. Security Aspect

```java
@Aspect
@Component
public class SecurityAspect {
    
    @Autowired
    private SecurityService securityService;
    
    @Before("@annotation(secured)")
    public void checkSecurity(JoinPoint joinPoint, Secured secured) {
        String[] requiredRoles = secured.value();
        
        if (!securityService.hasAnyRole(requiredRoles)) {
            throw new AccessDeniedException(
                "User does not have required roles: " + 
                Arrays.toString(requiredRoles)
            );
        }
        
        log.info("✓ Security check passed for {}",
                 joinPoint.getSignature().getName());
    }
}

// Custom annotation
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured {
    String[] value();
}

// Usage
@Service
public class UserService {
    
    @Secured({"ROLE_ADMIN"})
    public void deleteUser(Long id) {
        // Only accessible by admins
    }
}
```

### 6.4. Exception Translation Aspect

```java
@Aspect
@Component
public class ExceptionTranslationAspect {
    
    @AfterThrowing(
        pointcut = "execution(* com.example.repository.*.*(..))",
        throwing = "ex"
    )
    public void translateException(JoinPoint joinPoint, Exception ex) {
        if (ex instanceof SQLException) {
            throw new DataAccessException(
                "Database error in " + 
                joinPoint.getSignature().getName(), ex
            );
        }
    }
}
```

### 6.5. Caching Aspect

```java
@Aspect
@Component
public class CachingAspect {
    
    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    
    @Around("@annotation(cacheable)")
    public Object cacheResult(ProceedingJoinPoint pjp, Cacheable cacheable)
            throws Throwable {
        String cacheKey = generateKey(pjp);
        
        // Check cache
        if (cache.containsKey(cacheKey)) {
            log.info("✓ Cache hit for {}", cacheKey);
            return cache.get(cacheKey);
        }
        
        // Cache miss - proceed with method
        log.info("✗ Cache miss for {}", cacheKey);
        Object result = pjp.proceed();
        
        // Store in cache
        cache.put(cacheKey, result);
        
        return result;
    }
    
    private String generateKey(ProceedingJoinPoint pjp) {
        String methodName = pjp.getSignature().toString();
        String args = Arrays.toString(pjp.getArgs());
        return methodName + "_" + args;
    }
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {
}
```

---

## 7. JOINPOINT VÀ PROCEEDINGJOINPOINT

### 7.1. JoinPoint

`JoinPoint` cung cấp reflective access đến join point state. Available trong `@Before`, `@After`, `@AfterReturning`, `@AfterThrowing`.

```java
@Before("execution(* com.example.service.*.*(..))")
public void advice(JoinPoint joinPoint) {
    // Get method signature
    Signature signature = joinPoint.getSignature();
    String methodName = signature.getName();
    String declaringType = signature.getDeclaringTypeName();
    
    // Get target object
    Object target = joinPoint.getTarget();
    String className = target.getClass().getSimpleName();
    
    // Get method arguments
    Object[] args = joinPoint.getArgs();
    
    // Get this (proxy object)
    Object proxy = joinPoint.getThis();
    
    // Get kind (e.g., "method-execution")
    String kind = joinPoint.getKind();
    
    log.info("Method: {}, Class: {}, Args: {}",
             methodName, className, Arrays.toString(args));
}
```

### 7.2. ProceedingJoinPoint

`ProceedingJoinPoint` extends `JoinPoint`, chỉ available trong `@Around` advice. Cho phép proceed với method execution.

```java
@Around("execution(* com.example.service.*.*(..))")
public Object advice(ProceedingJoinPoint pjp) throws Throwable {
    // Có tất cả methods của JoinPoint
    String methodName = pjp.getSignature().getName();
    Object[] args = pjp.getArgs();
    
    // PLUS: Có thể proceed với method execution
    
    // Option 1: Proceed with original arguments
    Object result = pjp.proceed();
    
    // Option 2: Proceed with modified arguments
    Object[] modifiedArgs = new Object[]{"modified"};
    Object result2 = pjp.proceed(modifiedArgs);
    
    // Option 3: Don't proceed at all (skip method execution)
    // return someDefaultValue;
    
    return result;
}
```

### 7.3. So sánh JoinPoint vs ProceedingJoinPoint

| Feature | JoinPoint | ProceedingJoinPoint |
|---------|-----------|---------------------|
| **Available in** | @Before, @After, @AfterReturning, @AfterThrowing | @Around only |
| **Can proceed()** | No | Yes |
| **Modify args** | No | Yes |
| **Modify return** | No | Yes |
| **Skip execution** | No | Yes |

---

## 8. BEST PRACTICES

### 8.1. Chọn Advice type phù hợp

- Dùng **@Before** cho: logging entry, validation, security checks
- Dùng **@AfterReturning** cho: logging results, caching
- Dùng **@AfterThrowing** cho: exception logging, notifications
- Dùng **@After** cho: resource cleanup (luôn chạy)
- Dùng **@Around** cho: performance monitoring, transactions, caching với control

### 8.2. Pointcut Design

1. **Specific hơn generic**: Tránh pointcuts quá rộng
2. **Reuse pointcuts**: Tạo named pointcuts để tái sử dụng
3. **Use @annotation**: Cho fine-grained control
4. **Avoid complex expressions**: Chia nhỏ thành multiple pointcuts

### 8.3. Performance Considerations

- Aspects có overhead, sử dụng cẩn thận cho hot paths
- `@Around` advice tốn performance hơn `@Before`/`@After`
- Minimize logging trong aspects
- Cache aspect results khi có thể

### 8.4. Testing Aspects

Test aspects như test bình thường:

```java
@SpringBootTest
@EnableAspectJAutoProxy
class LoggingAspectTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    void testLoggingAspect() {
        // Aspect sẽ tự động apply
        User user = userService.createUser(new User("John"));
        
        // Verify logging occurred (check logs or use spy)
        assertNotNull(user);
    }
}
```

### 8.5. Common Pitfalls

#### 1. Self-invocation không work

```java
@Service
public class UserService {
    
    public void methodA() {
        this.methodB(); // AOP KHÔNG WORK!
    }
    
    @Transactional
    public void methodB() {
        // Transaction không active vì gọi từ this
    }
}
```

> ⚠️ **Solution**: Inject self hoặc gọi từ bean khác

#### 2. Quên @Component trên @Aspect

```java
// BAD - aspect không được Spring detect
@Aspect
public class MyAspect { }

// GOOD
@Aspect
@Component
public class MyAspect { }
```

#### 3. @Around không return value

```java
// BAD - method sẽ luôn return null
@Around("execution(* *(..))")
public Object advice(ProceedingJoinPoint pjp) throws Throwable {
    pjp.proceed();
    // Quên return!
}

// GOOD
@Around("execution(* *(..))")
public Object advice(ProceedingJoinPoint pjp) throws Throwable {
    return pjp.proceed();
}
```

---

## 9. CÂU HỎI MẪU CHO KỲ THI

### 9.1. Câu hỏi lý thuyết

#### Câu 1: Sự khác biệt giữa @Before và @Around advice là gì?

**Trả lời**: `@Before` chỉ chạy trước method, không thể control method execution hay modify return value. `@Around` chạy trước VÀ sau, có thể skip method execution, modify arguments và return value bằng `ProceedingJoinPoint.proceed()`.

---

#### Câu 2: Spring AOP sử dụng loại proxy nào?

**Trả lời**: Spring AOP mặc định sử dụng **JDK Dynamic Proxy** khi target implement interfaces, và **CGLIB proxy** khi target không implement interface nào. Có thể force CGLIB với `@EnableAspectJAutoProxy(proxyTargetClass=true)`.

---

#### Câu 3: Join point trong Spring AOP có thể là gì?

**Trả lời**: Trong Spring AOP, join point **CHỈ CÓ THỂ là method execution**. AspectJ support nhiều join point types hơn (field access, constructor call, etc.) nhưng Spring AOP chỉ support method execution.

---

#### Câu 4: Khi nào @AfterReturning advice chạy?

**Trả lời**: `@AfterReturning` chạy khi method return thành công (không throw exception). Nó có thể access return value qua `returning` attribute. Nếu method throw exception, `@AfterReturning` KHÔNG chạy.

---

#### Câu 5: Làm thế nào để enable AOP trong Spring?

**Trả lời**: Sử dụng `@EnableAspectJAutoProxy` trên `@Configuration` class, hoặc `<aop:aspectj-autoproxy/>` trong XML. Spring Boot tự động enable nếu có `spring-boot-starter-aop` dependency.

---

### 9.2. Câu hỏi code-based

#### Câu 6: Pointcut expression sau match methods nào?

```java
execution(* com.example.service..*.*(..))
```

**Trả lời**: Match **TẤT CẢ** methods trong package `com.example.service` và **TẤT CẢ sub-packages** (`..` = current package và sub-packages). Bất kể return type, method name, hoặc parameters.

---

#### Câu 7: Code sau có vấn đề gì?

```java
@Aspect
@Component
public class MyAspect {
    
    @Around("execution(* com.example.*.*(..))")
    public void logAround(ProceedingJoinPoint pjp) {
        System.out.println("Before");
        pjp.proceed();
        System.out.println("After");
    }
}
```

**Trả lời**: Có **3 vấn đề**:
1. Method nên return `Object` không phải `void`
2. Không return kết quả của `pjp.proceed()`, method sẽ luôn return null
3. Không handle `throws Throwable` cho `pjp.proceed()`

---

### 9.3. Scenario-based Questions

#### Câu 8: Bạn cần log thời gian thực thi của tất cả service methods. Nên dùng advice type nào?

**Trả lời**: Dùng `@Around` advice vì cần log TRƯỚC và SAU method execution để tính thời gian. `@Before`/`@After` không đủ vì cần wrap method execution và calculate duration.

---

#### Câu 9: Làm thế nào để aspect chỉ áp dụng cho methods có annotation @Loggable?

**Trả lời**: Sử dụng `@annotation()` pointcut:
```java
@Before("@annotation(com.example.annotation.Loggable)")
```
Điều này chỉ match methods được annotated với `@Loggable`.

---

#### Câu 10: Method trong service gọi method khác trong cùng class. AOP có hoạt động không?

**Trả lời**: **KHÔNG**. Đây là self-invocation problem. AOP chỉ hoạt động khi method được gọi từ bên ngoài qua proxy. Internal calls (`this.method()`) bypass proxy nên AOP không apply. 

**Solution**: Inject self hoặc restructure code để gọi từ bean khác.

---

## 10. TÓM TẮT VÀ MẸO THI

### 10.1. Các khái niệm quan trọng

| Khái niệm | Điểm cần nhớ |
|-----------|--------------|
| **Aspect** | Class chứa advice và pointcuts, cần @Aspect và @Component |
| **Join Point** | Trong Spring AOP = method execution ONLY |
| **Pointcut** | Expression để filter join points (execution, within, @annotation, etc.) |
| **Advice** | Action tại join point: @Before, @After, @AfterReturning, @AfterThrowing, @Around |
| **Weaving** | Spring AOP = Runtime weaving với proxies |
| **Proxy** | JDK Dynamic (interfaces) hoặc CGLIB (classes) |

### 10.2. Advice Types Cheat Sheet

| Advice | Khi nào chạy | Use case |
|--------|--------------|----------|
| **@Before** | Trước method | Logging, validation, security |
| **@After** | Sau method (luôn) | Cleanup resources |
| **@AfterReturning** | Sau return success | Log results, caching |
| **@AfterThrowing** | Sau throw exception | Error logging, notifications |
| **@Around** | Trước & sau, control all | Performance, transactions, caching |

### 10.3. Pointcut Expression Patterns

```java
// Tất cả public methods
execution(public * *(..))

// Methods trong package
execution(* com.example.service.*.*(..))

// Methods trong package và sub-packages
execution(* com.example.service..*.*(..))

// Methods có annotation
@annotation(com.example.annotation.Loggable)

// Classes có annotation
@within(org.springframework.stereotype.Service)

// Spring beans
bean(userService)
bean(*Service)
```

### 10.4. Mẹo làm bài thi

1. ✅ Nhớ rằng Spring AOP chỉ support method execution join points
2. ✅ `@Around` advice phải return Object và call `proceed()`
3. ✅ Self-invocation không trigger AOP (proxy bypass)
4. ✅ `@Aspect` cần `@Component` để Spring detect
5. ✅ JDK Proxy cho interfaces, CGLIB cho classes
6. ✅ Pointcuts có thể combine với `&&`, `||`, `!`
7. ✅ `@EnableAspectJAutoProxy` hoặc `spring-boot-starter-aop` để enable

### 10.5. Checklist ôn tập

- [ ] Các khái niệm: Aspect, Join Point, Pointcut, Advice, Weaving
- [ ] 5 loại advice và khi nào dùng từng loại
- [ ] Pointcut expressions: execution, within, @annotation, bean
- [ ] JoinPoint vs ProceedingJoinPoint
- [ ] JDK Dynamic Proxy vs CGLIB Proxy
- [ ] Self-invocation problem và giải pháp
- [ ] @EnableAspectJAutoProxy configuration
- [ ] Viết được @Around advice với proceed()
- [ ] Reusable pointcuts với @Pointcut
- [ ] Common pitfalls: quên @Component, không return trong @Around

---

## KẾT LUẬN

AOP là một trong những tính năng mạnh mẽ nhất của Spring Framework. Để thành công trong kỳ thi Spring Professional Certification về AOP, bạn cần:

- ✅ Hiểu rõ các khái niệm: Aspect, Join Point, Pointcut, Advice, Weaving
- ✅ Phân biệt 5 loại advice và biết khi nào dùng từng loại
- ✅ Thành thạo viết pointcut expressions
- ✅ Hiểu cách Spring AOP hoạt động với proxies
- ✅ Tránh các pitfalls phổ biến như self-invocation

### Điểm quan trọng nhất:

> **AOP không phải là magic, nó là proxies!** Hiểu rõ proxy mechanism sẽ giúp bạn giải thích được mọi behavior của Spring AOP.

Hãy thực hành viết aspects cho các use cases thực tế như logging, performance monitoring, security. Đây không chỉ là kiến thức cho kỳ thi mà còn là kỹ năng quý giá trong công việc thực tế.

---

**Chúc bạn thành công với Spring Professional Certification!** 🚀

*Tài liệu được tạo ngày 25/12/2024*
