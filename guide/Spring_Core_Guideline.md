# SPRING CORE
## Guideline Chi Tiết cho Spring Professional Certification

---

**Tài liệu ôn tập toàn diện về Spring Framework Core**

*Tạo ngày: 26/12/2024*

---

## MỤC LỤC

1. [Giới thiệu về Spring Framework](#1-giới-thiệu-về-spring-framework)
2. [Inversion of Control (IoC) và Dependency Injection (DI)](#2-inversion-of-control-ioc-và-dependency-injection-di)
3. [Spring Container](#3-spring-container)
4. [Bean Configuration](#4-bean-configuration)
5. [Bean Lifecycle](#5-bean-lifecycle)
6. [Bean Scopes](#6-bean-scopes)
7. [Dependency Injection Methods](#7-dependency-injection-methods)
8. [Autowiring](#8-autowiring)
9. [Qualifiers và Primary](#9-qualifiers-và-primary)
10. [Component Scanning](#10-component-scanning)
11. [Profiles](#11-profiles)
12. [Property Sources và Environment](#12-property-sources-và-environment)
13. [SpEL - Spring Expression Language](#13-spel---spring-expression-language)
14. [Best Practices](#14-best-practices)
15. [Câu hỏi mẫu cho kỳ thi](#15-câu-hỏi-mẫu-cho-kỳ-thi)
16. [Tóm tắt và mẹo thi](#16-tóm-tắt-và-mẹo-thi)

---

## 1. GIỚI THIỆU VỀ SPRING FRAMEWORK

### 1.1. Spring Framework là gì?

**Spring Framework** là một powerful, lightweight framework cho việc xây dựng enterprise Java applications. Ra đời năm 2003, Spring đã trở thành tiêu chuẩn de-facto cho Java development.

**Core Principles:**
- ✅ **Inversion of Control (IoC)**: Container quản lý object lifecycle
- ✅ **Dependency Injection (DI)**: Dependencies được inject từ bên ngoài
- ✅ **Aspect-Oriented Programming (AOP)**: Tách biệt cross-cutting concerns
- ✅ **POJO-based**: Không yêu cầu extend/implement framework classes

### 1.2. Spring Modules

```
┌─────────────────────────────────────────────┐
│         Spring Framework Modules            │
├─────────────────────────────────────────────┤
│                                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐ │
│  │  Core    │  │   AOP    │  │   Data   │ │
│  │Container │  │ Aspects  │  │  Access  │ │
│  └──────────┘  └──────────┘  └──────────┘ │
│                                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐ │
│  │   Web    │  │   Test   │  │Integration││
│  │   MVC    │  │          │  │           │ │
│  └──────────┘  └──────────┘  └──────────┘ │
│                                             │
└─────────────────────────────────────────────┘
```

### 1.3. Tại sao dùng Spring?

**Lợi ích:**
- 🎯 Loose coupling thông qua DI
- 🎯 Easy testing với mock objects
- 🎯 Declarative programming (annotations)
- 🎯 Boilerplate code reduction
- 🎯 Transaction management
- 🎯 Large ecosystem và community
- 🎯 Enterprise-ready features

---

## 2. INVERSION OF CONTROL (IoC) VÀ DEPENDENCY INJECTION (DI)

### 2.1. Inversion of Control (IoC)

**IoC** là design principle nơi control flow được đảo ngược - thay vì object tự tạo dependencies, container sẽ tạo và inject chúng.

#### Without IoC (Tight Coupling):

```java
public class UserService {
    private UserRepository userRepository;
    
    public UserService() {
        // UserService tự tạo dependency
        this.userRepository = new UserRepositoryImpl();
    }
    
    public User findUser(Long id) {
        return userRepository.findById(id);
    }
}
```

**Vấn đề:**
- ❌ Tight coupling giữa UserService và UserRepositoryImpl
- ❌ Khó test (không thể mock UserRepository)
- ❌ Khó thay đổi implementation

#### With IoC (Loose Coupling):

```java
public class UserService {
    private UserRepository userRepository;
    
    // Container sẽ inject dependency
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User findUser(Long id) {
        return userRepository.findById(id);
    }
}
```

**Lợi ích:**
- ✅ Loose coupling
- ✅ Dễ test với mock
- ✅ Dễ thay đổi implementation
- ✅ Container quản lý lifecycle

### 2.2. Dependency Injection (DI)

**DI** là implementation của IoC principle. Có 3 types:

1. **Constructor Injection** (Recommended)
2. **Setter Injection**
3. **Field Injection** (Not recommended)

#### Constructor Injection (Best Practice):

```java
@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    
    // Constructor injection
    @Autowired  // Optional since Spring 4.3
    public UserService(UserRepository userRepository, 
                      EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }
}
```

**Ưu điểm:**
- ✅ Immutable (final fields)
- ✅ Đảm bảo dependencies không null
- ✅ Easy to test
- ✅ Clear dependencies

#### Setter Injection:

```java
@Service
public class UserService {
    private UserRepository userRepository;
    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

**Use cases:**
- Optional dependencies
- Dependencies có thể thay đổi sau khi object được tạo

#### Field Injection (Avoid):

```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
}
```

**Nhược điểm:**
- ❌ Không immutable
- ❌ Khó test (cần reflection)
- ❌ Hidden dependencies
- ❌ Không thể enforce required dependencies

### 2.3. So sánh DI Methods

| Type | Immutability | Testability | Required Dependencies | Recommended |
|------|--------------|-------------|----------------------|-------------|
| **Constructor** | ✅ Yes (final) | ⭐⭐⭐⭐⭐ | ✅ Enforced | ✅ YES |
| **Setter** | ❌ No | ⭐⭐⭐⭐ | ❌ Optional | For optional deps |
| **Field** | ❌ No | ⭐⭐ | ❌ Not enforced | ❌ AVOID |

---

## 3. SPRING CONTAINER

### 3.1. ApplicationContext

**ApplicationContext** là central interface trong Spring, đại diện cho IoC container. Nó chịu trách nhiệm:
- Instantiating beans
- Configuring beans
- Assembling beans
- Managing bean lifecycle

```
┌────────────────────────────────────────┐
│      ApplicationContext                │
│                                        │
│  ┌──────────────────────────────────┐ │
│  │    Bean Definition Registry      │ │
│  │  (Bean metadata from config)     │ │
│  └──────────────────────────────────┘ │
│              ↓                         │
│  ┌──────────────────────────────────┐ │
│  │    Bean Factory                  │ │
│  │  (Creates and manages beans)     │ │
│  └──────────────────────────────────┘ │
│              ↓                         │
│  ┌──────────────────────────────────┐ │
│  │    Application Beans             │ │
│  │  (Your objects)                  │ │
│  └──────────────────────────────────┘ │
│                                        │
└────────────────────────────────────────┘
```

### 3.2. ApplicationContext Implementations

#### AnnotationConfigApplicationContext

```java
// For Java-based configuration
@Configuration
public class AppConfig {
    @Bean
    public UserService userService() {
        return new UserService();
    }
}

// Create context
ApplicationContext context = 
    new AnnotationConfigApplicationContext(AppConfig.class);

UserService service = context.getBean(UserService.class);
```

#### ClassPathXmlApplicationContext

```java
// For XML configuration
ApplicationContext context = 
    new ClassPathXmlApplicationContext("applicationContext.xml");

UserService service = context.getBean("userService", UserService.class);
```

#### AnnotationConfigWebApplicationContext

```java
// For web applications
AnnotationConfigWebApplicationContext context = 
    new AnnotationConfigWebApplicationContext();
context.register(AppConfig.class);
context.refresh();
```

### 3.3. BeanFactory vs ApplicationContext

| Feature | BeanFactory | ApplicationContext |
|---------|-------------|-------------------|
| **Bean instantiation** | Lazy (on-demand) | Eager (on startup) |
| **Internationalization** | ❌ No | ✅ Yes |
| **Event publication** | ❌ No | ✅ Yes |
| **AOP integration** | Manual | Automatic |
| **Use case** | Resource-constrained | Enterprise apps |
| **Recommended** | ❌ No | ✅ YES |

> 💡 **Best Practice**: Always use ApplicationContext, not BeanFactory

---

## 4. BEAN CONFIGURATION

### 4.1. Java Configuration (Recommended)

```java
@Configuration
public class AppConfig {
    
    @Bean
    public UserRepository userRepository() {
        return new UserRepositoryImpl();
    }
    
    @Bean
    public EmailService emailService() {
        return new EmailServiceImpl();
    }
    
    @Bean
    public UserService userService() {
        return new UserService(userRepository(), emailService());
    }
    
    // Bean with dependencies
    @Bean
    public OrderService orderService(UserService userService) {
        return new OrderService(userService);
    }
    
    // Bean with initialization
    @Bean(initMethod = "init", destroyMethod = "cleanup")
    public CacheManager cacheManager() {
        return new CacheManager();
    }
    
    // Bean with name
    @Bean(name = "customUserService")
    public UserService customUserService() {
        return new UserService();
    }
}
```

### 4.2. Annotation-based Configuration

```java
@Component
public class UserRepositoryImpl implements UserRepository {
    // Implementation
}

@Service
public class UserService {
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

@Controller
public class UserController {
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
}
```

**Stereotype Annotations:**

| Annotation | Purpose | Layer |
|------------|---------|-------|
| `@Component` | Generic component | Any |
| `@Service` | Business logic | Service |
| `@Repository` | Data access | Persistence |
| `@Controller` | Web controller | Presentation |
| `@RestController` | REST API controller | Presentation |
| `@Configuration` | Configuration class | Config |

### 4.3. XML Configuration (Legacy)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- Simple bean -->
    <bean id="userRepository" 
          class="com.example.repository.UserRepositoryImpl"/>
    
    <!-- Bean with constructor injection -->
    <bean id="userService" 
          class="com.example.service.UserService">
        <constructor-arg ref="userRepository"/>
    </bean>
    
    <!-- Bean with setter injection -->
    <bean id="emailService" 
          class="com.example.service.EmailService">
        <property name="host" value="smtp.example.com"/>
        <property name="port" value="587"/>
    </bean>
    
    <!-- Bean with init and destroy methods -->
    <bean id="cacheManager" 
          class="com.example.cache.CacheManager"
          init-method="init" 
          destroy-method="cleanup"/>
</beans>
```

### 4.4. Mixed Configuration

```java
@Configuration
@ImportResource("classpath:legacy-config.xml")  // Import XML
@Import(DatabaseConfig.class)                   // Import Java config
public class AppConfig {
    
    @Bean
    public UserService userService() {
        return new UserService();
    }
}
```

---

## 5. BEAN LIFECYCLE

### 5.1. Bean Lifecycle Phases

```
┌────────────────────────────────────────────────┐
│           Bean Lifecycle                       │
├────────────────────────────────────────────────┤
│                                                │
│  1. Instantiation                              │
│     Container creates bean instance            │
│              ↓                                 │
│  2. Populate Properties                        │
│     DI occurs (setter/field injection)         │
│              ↓                                 │
│  3. BeanNameAware.setBeanName()                │
│              ↓                                 │
│  4. BeanFactoryAware.setBeanFactory()          │
│              ↓                                 │
│  5. ApplicationContextAware.setAppContext()    │
│              ↓                                 │
│  6. BeanPostProcessor.postProcessBeforeInit()  │
│              ↓                                 │
│  7. @PostConstruct / InitializingBean         │
│              ↓                                 │
│  8. Custom init-method                         │
│              ↓                                 │
│  9. BeanPostProcessor.postProcessAfterInit()   │
│              ↓                                 │
│  Bean ready to use                             │
│              ↓                                 │
│  Container shutdown                            │
│              ↓                                 │
│  10. @PreDestroy / DisposableBean              │
│              ↓                                 │
│  11. Custom destroy-method                     │
│                                                │
└────────────────────────────────────────────────┘
```

### 5.2. Initialization Methods

#### @PostConstruct (Recommended)

```java
@Component
public class CacheManager {
    
    @PostConstruct
    public void init() {
        System.out.println("Cache initialized");
        // Initialization logic
    }
}
```

#### InitializingBean Interface

```java
@Component
public class CacheManager implements InitializingBean {
    
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Cache initialized");
    }
}
```

#### Custom init-method

```java
@Configuration
public class AppConfig {
    
    @Bean(initMethod = "customInit")
    public CacheManager cacheManager() {
        return new CacheManager();
    }
}

public class CacheManager {
    public void customInit() {
        System.out.println("Cache initialized");
    }
}
```

### 5.3. Destruction Methods

#### @PreDestroy (Recommended)

```java
@Component
public class CacheManager {
    
    @PreDestroy
    public void cleanup() {
        System.out.println("Cache cleanup");
        // Cleanup logic
    }
}
```

#### DisposableBean Interface

```java
@Component
public class CacheManager implements DisposableBean {
    
    @Override
    public void destroy() throws Exception {
        System.out.println("Cache cleanup");
    }
}
```

#### Custom destroy-method

```java
@Configuration
public class AppConfig {
    
    @Bean(destroyMethod = "customCleanup")
    public CacheManager cacheManager() {
        return new CacheManager();
    }
}

public class CacheManager {
    public void customCleanup() {
        System.out.println("Cache cleanup");
    }
}
```

### 5.4. BeanPostProcessor

**BeanPostProcessor** cho phép custom modification của beans trước và sau initialization.

```java
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {
    
    @Override
    public Object postProcessBeforeInitialization(
            Object bean, String beanName) throws BeansException {
        System.out.println("Before init: " + beanName);
        // Modify bean before initialization
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(
            Object bean, String beanName) throws BeansException {
        System.out.println("After init: " + beanName);
        // Modify bean after initialization (e.g., create proxy)
        return bean;
    }
}
```

**Use cases:**
- AOP proxy creation
- Validation
- Logging
- Custom initialization logic

---

## 6. BEAN SCOPES

### 6.1. Các Bean Scopes

Spring cung cấp 6 bean scopes:

| Scope | Description | Lifecycle |
|-------|-------------|-----------|
| **singleton** | Một instance duy nhất per container | Container lifetime |
| **prototype** | Mỗi request tạo instance mới | Created on demand |
| **request** | Một instance per HTTP request | HTTP request |
| **session** | Một instance per HTTP session | HTTP session |
| **application** | Một instance per ServletContext | ServletContext |
| **websocket** | Một instance per WebSocket | WebSocket |

> 💡 **Default scope**: singleton

### 6.2. Singleton Scope (Default)

```java
@Component
@Scope("singleton")  // Optional, default
public class SingletonBean {
    // One instance per container
}

// Or
@Configuration
public class AppConfig {
    
    @Bean
    @Scope("singleton")
    public SingletonBean singletonBean() {
        return new SingletonBean();
    }
}
```

**Characteristics:**
- ✅ Shared across application
- ✅ Thread-safe concerns
- ✅ Cached by container
- ✅ Eager initialization by default

```java
// Test singleton
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

SingletonBean bean1 = context.getBean(SingletonBean.class);
SingletonBean bean2 = context.getBean(SingletonBean.class);

System.out.println(bean1 == bean2); // true - same instance
```

### 6.3. Prototype Scope

```java
@Component
@Scope("prototype")
public class PrototypeBean {
    // New instance for each request
}

// Or with ConfigurableBeanFactory constant
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeBean {
}
```

**Characteristics:**
- ✅ New instance per request
- ✅ Not cached
- ✅ Container doesn't manage destruction
- ✅ Lazy initialization

```java
// Test prototype
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

PrototypeBean bean1 = context.getBean(PrototypeBean.class);
PrototypeBean bean2 = context.getBean(PrototypeBean.class);

System.out.println(bean1 == bean2); // false - different instances
```

### 6.4. Web Scopes

#### Request Scope

```java
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, 
       proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestScopedBean {
    // New instance per HTTP request
}
```

#### Session Scope

```java
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION,
       proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionScopedBean {
    // New instance per HTTP session
}
```

#### Application Scope

```java
@Component
@Scope(value = WebApplicationContext.SCOPE_APPLICATION,
       proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ApplicationScopedBean {
    // One instance per ServletContext
}
```

### 6.5. Scoped Proxy

Khi inject shorter-lived scope vào longer-lived scope, cần proxy:

```java
@Service
public class UserService {
    // Singleton service
    
    @Autowired
    private RequestScopedBean requestBean; // Shorter-lived scope
    
    // Spring creates proxy for requestBean
    // Proxy delegates to actual request-scoped instance
}
```

**Proxy modes:**
- `ScopedProxyMode.TARGET_CLASS`: CGLIB proxy
- `ScopedProxyMode.INTERFACES`: JDK dynamic proxy
- `ScopedProxyMode.DEFAULT`: No proxy
- `ScopedProxyMode.NO`: No proxy

---

## 7. DEPENDENCY INJECTION METHODS

### 7.1. Constructor Injection (Best Practice)

```java
@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    
    // @Autowired optional since Spring 4.3 if only one constructor
    public UserService(UserRepository userRepository, 
                      EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }
}
```

**Advantages:**
- ✅ Immutability (final fields)
- ✅ Required dependencies enforced
- ✅ Easy to test
- ✅ Clear dependencies

**When to use:**
- Required dependencies
- Immutable objects
- Always (as default choice)

### 7.2. Setter Injection

```java
@Service
public class UserService {
    private UserRepository userRepository;
    private EmailService emailService;
    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Autowired(required = false)  // Optional dependency
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}
```

**When to use:**
- Optional dependencies
- Dependencies that can change
- Circular dependencies (not recommended)

### 7.3. Field Injection (Not Recommended)

```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired(required = false)
    private EmailService emailService;
}
```

**Why avoid:**
- ❌ Not immutable
- ❌ Hard to test
- ❌ Hidden dependencies
- ❌ Violates Single Responsibility Principle

### 7.4. Method Injection

```java
@Service
public class UserService {
    private UserRepository userRepository;
    private EmailService emailService;
    
    @Autowired
    public void setup(UserRepository userRepository, 
                     EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }
}
```

### 7.5. Lookup Method Injection

Dùng khi singleton bean cần prototype bean:

```java
@Component
public abstract class CommandManager {
    
    public void process() {
        Command command = createCommand(); // Prototype
        command.execute();
    }
    
    @Lookup
    protected abstract Command createCommand();
}

@Component
@Scope("prototype")
public class Command {
    public void execute() {
        // Execute command
    }
}
```

---

## 8. AUTOWIRING

### 8.1. @Autowired Annotation

```java
@Service
public class UserService {
    
    // Constructor autowiring (recommended)
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // Setter autowiring
    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
    
    // Field autowiring (not recommended)
    @Autowired
    private NotificationService notificationService;
    
    // Method autowiring
    @Autowired
    public void setup(LogService logService, CacheService cacheService) {
        this.logService = logService;
        this.cacheService = cacheService;
    }
}
```

### 8.2. @Autowired Properties

```java
@Service
public class UserService {
    
    // Required dependency (default)
    @Autowired
    private UserRepository userRepository;
    
    // Optional dependency
    @Autowired(required = false)
    private EmailService emailService;
    
    // Optional with Java 8 Optional
    @Autowired
    private Optional<EmailService> emailService;
    
    // Optional with @Nullable
    @Autowired
    public void setEmailService(@Nullable EmailService emailService) {
        this.emailService = emailService;
    }
}
```

### 8.3. Autowiring Collections

```java
@Service
public class NotificationService {
    
    // Inject all beans of type MessageSender
    @Autowired
    private List<MessageSender> senders;
    
    // Inject as Set
    @Autowired
    private Set<MessageSender> senderSet;
    
    // Inject as Map (key = bean name, value = bean)
    @Autowired
    private Map<String, MessageSender> senderMap;
    
    public void notifyAll(String message) {
        senders.forEach(sender -> sender.send(message));
    }
}
```

### 8.4. @Resource (JSR-250)

```java
@Service
public class UserService {
    
    // By name
    @Resource(name = "userRepository")
    private UserRepository userRepository;
    
    // By type if name not specified
    @Resource
    private EmailService emailService;
}
```

**@Autowired vs @Resource:**

| Feature | @Autowired | @Resource |
|---------|-----------|-----------|
| **From** | Spring | JSR-250 |
| **Matching** | By type first | By name first |
| **required** | Supports | No |
| **Qualifier** | @Qualifier | name attribute |

### 8.5. @Inject (JSR-330)

```java
import javax.inject.Inject;
import javax.inject.Named;

@Service
public class UserService {
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    @Named("emailService")
    private EmailService emailService;
}
```

**Dependency:**
```xml
<dependency>
    <groupId>javax.inject</groupId>
    <artifactId>javax.inject</artifactId>
    <version>1</version>
</dependency>
```

---

## 9. QUALIFIERS VÀ PRIMARY

### 9.1. Problem: Multiple Beans

```java
public interface MessageSender {
    void send(String message);
}

@Component
public class EmailSender implements MessageSender {
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

@Component
public class SmsSender implements MessageSender {
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

@Service
public class NotificationService {
    @Autowired
    private MessageSender sender; // ❌ Error! Which bean?
}
```

### 9.2. Solution 1: @Qualifier

```java
@Service
public class NotificationService {
    
    @Autowired
    @Qualifier("emailSender")
    private MessageSender sender;
    
    // Or constructor injection
    @Autowired
    public NotificationService(@Qualifier("emailSender") MessageSender sender) {
        this.sender = sender;
    }
}
```

### 9.3. Solution 2: @Primary

```java
@Component
@Primary  // This will be injected by default
public class EmailSender implements MessageSender {
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

@Component
public class SmsSender implements MessageSender {
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

@Service
public class NotificationService {
    @Autowired
    private MessageSender sender; // ✅ EmailSender (primary)
    
    @Autowired
    @Qualifier("smsSender")
    private MessageSender smsSender; // ✅ Explicit qualifier
}
```

### 9.4. Custom Qualifiers

```java
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Email {
}

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Sms {
}

// Usage
@Component
@Email
public class EmailSender implements MessageSender {
}

@Component
@Sms
public class SmsSender implements MessageSender {
}

@Service
public class NotificationService {
    
    @Autowired
    @Email
    private MessageSender emailSender;
    
    @Autowired
    @Sms
    private MessageSender smsSender;
}
```

### 9.5. @Primary vs @Qualifier

| Aspect | @Primary | @Qualifier |
|--------|----------|-----------|
| **Use case** | Default bean | Explicit selection |
| **Location** | Bean definition | Injection point |
| **Multiple** | Only one per type | Multiple qualifiers |
| **Override** | Can be overridden by @Qualifier | Final choice |

---

## 10. COMPONENT SCANNING

### 10.1. Enable Component Scanning

```java
@Configuration
@ComponentScan(basePackages = "com.example")
public class AppConfig {
}

// Or multiple packages
@Configuration
@ComponentScan(basePackages = {"com.example.service", "com.example.repository"})
public class AppConfig {
}

// Type-safe with classes
@Configuration
@ComponentScan(basePackageClasses = {UserService.class, OrderService.class})
public class AppConfig {
}
```

### 10.2. Filters

```java
@Configuration
@ComponentScan(
    basePackages = "com.example",
    
    // Include filters
    includeFilters = @Filter(
        type = FilterType.ANNOTATION,
        classes = MyCustomAnnotation.class
    ),
    
    // Exclude filters
    excludeFilters = @Filter(
        type = FilterType.REGEX,
        pattern = "com.example.test.*"
    )
)
public class AppConfig {
}
```

**Filter Types:**

| FilterType | Example |
|------------|---------|
| **ANNOTATION** | `@Filter(type = FilterType.ANNOTATION, classes = Service.class)` |
| **ASSIGNABLE_TYPE** | `@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UserService.class)` |
| **REGEX** | `@Filter(type = FilterType.REGEX, pattern = "com.example.*")` |
| **ASPECTJ** | `@Filter(type = FilterType.ASPECTJ, pattern = "com.example..*Service")` |
| **CUSTOM** | `@Filter(type = FilterType.CUSTOM, classes = MyFilter.class)` |

### 10.3. Custom Filter

```java
public class CustomTypeFilter implements TypeFilter {
    
    @Override
    public boolean match(MetadataReader metadataReader,
                        MetadataReaderFactory factory) throws IOException {
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        String className = classMetadata.getClassName();
        
        // Custom logic
        return className.endsWith("CustomService");
    }
}

@Configuration
@ComponentScan(
    basePackages = "com.example",
    includeFilters = @Filter(
        type = FilterType.CUSTOM,
        classes = CustomTypeFilter.class
    )
)
public class AppConfig {
}
```

### 10.4. Component Scan Strategy

```java
@Configuration
@ComponentScan(
    basePackages = "com.example",
    useDefaultFilters = false,  // Disable @Component, @Service, etc.
    includeFilters = @Filter(
        type = FilterType.ANNOTATION,
        classes = MyComponent.class
    )
)
public class AppConfig {
}
```

---

## 11. PROFILES

### 11.1. @Profile Annotation

```java
@Configuration
@Profile("development")
public class DevConfig {
    
    @Bean
    public DataSource dataSource() {
        return new H2DataSource(); // In-memory for dev
    }
}

@Configuration
@Profile("production")
public class ProdConfig {
    
    @Bean
    public DataSource dataSource() {
        return new PostgresDataSource(); // Real DB for prod
    }
}

// Multiple profiles
@Configuration
@Profile({"development", "test"})
public class DevTestConfig {
}

// NOT profile
@Configuration
@Profile("!production")
public class NonProdConfig {
}
```

### 11.2. Bean-level Profiles

```java
@Configuration
public class DataConfig {
    
    @Bean
    @Profile("development")
    public DataSource devDataSource() {
        return new H2DataSource();
    }
    
    @Bean
    @Profile("production")
    public DataSource prodDataSource() {
        return new PostgresDataSource();
    }
    
    @Bean  // Available in all profiles
    public UserRepository userRepository() {
        return new UserRepositoryImpl();
    }
}
```

### 11.3. Activate Profiles

#### Programmatically

```java
AnnotationConfigApplicationContext context = 
    new AnnotationConfigApplicationContext();

context.getEnvironment().setActiveProfiles("development", "debug");
context.register(AppConfig.class);
context.refresh();
```

#### application.properties

```properties
spring.profiles.active=development,debug
```

#### application.yml

```yaml
spring:
  profiles:
    active: development, debug
```

#### Environment Variable

```bash
export SPRING_PROFILES_ACTIVE=development,debug
```

#### JVM System Property

```bash
java -jar app.jar -Dspring.profiles.active=development,debug
```

#### Command Line Argument

```bash
java -jar app.jar --spring.profiles.active=development,debug
```

### 11.4. Profile-specific Properties

```
application.properties           # Default
application-dev.properties       # Development
application-prod.properties      # Production
application-test.properties      # Test
```

**Example:**

```properties
# application.properties
app.name=MyApp
app.version=1.0.0

# application-dev.properties
spring.datasource.url=jdbc:h2:mem:testdb
logging.level.com.example=DEBUG

# application-prod.properties
spring.datasource.url=jdbc:postgresql://prod-server/mydb
logging.level.com.example=INFO
```

### 11.5. @Profile with Components

```java
@Component
@Profile("development")
public class DevDataInitializer {
    
    @PostConstruct
    public void init() {
        // Initialize test data
    }
}

@Component
@Profile("production")
public class ProdDataInitializer {
    
    @PostConstruct
    public void init() {
        // Production initialization
    }
}
```

---

## 12. PROPERTY SOURCES VÀ ENVIRONMENT

### 12.1. @PropertySource

```java
@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {
    
    @Value("${app.name}")
    private String appName;
    
    @Value("${app.version}")
    private String appVersion;
}

// Multiple property sources
@Configuration
@PropertySource("classpath:app.properties")
@PropertySource("classpath:database.properties")
public class AppConfig {
}

// Or array
@Configuration
@PropertySources({
    @PropertySource("classpath:app.properties"),
    @PropertySource("classpath:database.properties")
})
public class AppConfig {
}
```

### 12.2. @Value Annotation

```java
@Component
public class AppConfig {
    
    // Simple property
    @Value("${app.name}")
    private String appName;
    
    // With default value
    @Value("${app.timeout:30}")
    private int timeout;
    
    // System property
    @Value("${java.home}")
    private String javaHome;
    
    // SpEL expression
    @Value("#{systemProperties['user.home']}")
    private String userHome;
    
    // List
    @Value("${app.servers}")
    private List<String> servers;
    
    // Array
    @Value("${app.servers}")
    private String[] serverArray;
    
    // Map (requires SpEL)
    @Value("#{${app.config}}")
    private Map<String, String> config;
}
```

**application.properties:**
```properties
app.name=MyApp
app.timeout=60
app.servers=server1,server2,server3
app.config={'key1':'value1','key2':'value2'}
```

### 12.3. Environment Abstraction

```java
@Configuration
public class AppConfig {
    
    @Autowired
    private Environment env;
    
    @Bean
    public DataSource dataSource() {
        String url = env.getProperty("db.url");
        String username = env.getProperty("db.username");
        
        // With default value
        int maxConnections = env.getProperty("db.max.connections", 
                                            Integer.class, 10);
        
        // Required property
        String password = env.getRequiredProperty("db.password");
        
        // Check if property exists
        if (env.containsProperty("db.pool.enabled")) {
            // Configure connection pool
        }
        
        return createDataSource(url, username, password);
    }
}
```

### 12.4. ConfigurationProperties (Spring Boot)

```java
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private String version;
    private int timeout;
    private List<String> servers;
    private Database database;
    
    // Nested properties
    public static class Database {
        private String url;
        private String username;
        private String password;
        
        // Getters and setters
    }
    
    // Getters and setters
}
```

**application.yml:**
```yaml
app:
  name: MyApp
  version: 1.0.0
  timeout: 30
  servers:
    - server1
    - server2
    - server3
  database:
    url: jdbc:postgresql://localhost/mydb
    username: admin
    password: secret
```

**Usage:**
```java
@Service
public class UserService {
    
    @Autowired
    private AppProperties appProperties;
    
    public void printConfig() {
        System.out.println("App: " + appProperties.getName());
        System.out.println("DB: " + appProperties.getDatabase().getUrl());
    }
}
```

### 12.5. Placeholder Resolution

```properties
# application.properties
app.name=MyApp
app.version=1.0.0
app.description=${app.name} version ${app.version}
app.home=${user.home}/myapp
```

---

## 13. SpEL - SPRING EXPRESSION LANGUAGE

### 13.1. Basic Syntax

```java
@Component
public class SpELExamples {
    
    // Literal values
    @Value("#{'Hello World'}")
    private String greeting;
    
    @Value("#{42}")
    private int number;
    
    @Value("#{true}")
    private boolean flag;
    
    // Arithmetic operations
    @Value("#{10 + 5}")
    private int sum;
    
    @Value("#{10 * 2}")
    private int product;
    
    @Value("#{100 / 4}")
    private int division;
    
    // String operations
    @Value("#{'Hello' + ' ' + 'World'}")
    private String concatenated;
    
    @Value("#{'Hello'.toUpperCase()}")
    private String upper;
    
    @Value("#{'Hello'.length()}")
    private int length;
}
```

### 13.2. Accessing Properties

```java
@Component
public class PropertyAccess {
    
    // System properties
    @Value("#{systemProperties['user.home']}")
    private String userHome;
    
    @Value("#{systemProperties['java.version']}")
    private String javaVersion;
    
    // Environment variables
    @Value("#{systemEnvironment['PATH']}")
    private String path;
    
    // Application properties
    @Value("#{environment['app.name']}")
    private String appName;
}
```

### 13.3. Accessing Beans

```java
@Component("userService")
public class UserService {
    public String getServiceName() {
        return "UserService";
    }
    
    public int getUserCount() {
        return 100;
    }
}

@Component
public class BeanAccess {
    
    // Call bean method
    @Value("#{userService.getServiceName()}")
    private String serviceName;
    
    @Value("#{userService.getUserCount()}")
    private int userCount;
    
    // Access bean property
    @Value("#{userService.serviceName}")
    private String name;
}
```

### 13.4. Collections

```java
@Component
public class CollectionExamples {
    
    // List
    @Value("#{{'a', 'b', 'c'}}")
    private List<String> list;
    
    // Access by index
    @Value("#{{'a', 'b', 'c'}[0]}")
    private String firstElement;
    
    // Map
    @Value("#{{key1: 'value1', key2: 'value2'}}")
    private Map<String, String> map;
    
    // Access map value
    @Value("#{{key1: 'value1', key2: 'value2'}['key1']}")
    private String mapValue;
    
    // Collection selection
    @Value("#{userList.?[age > 18]}")  // Filter
    private List<User> adults;
    
    @Value("#{userList.![name]}")  // Projection
    private List<String> names;
}
```

### 13.5. Conditional Expressions

```java
@Component
public class ConditionalExamples {
    
    // Ternary operator
    @Value("#{userCount > 0 ? 'Users exist' : 'No users'}")
    private String message;
    
    // Elvis operator (null-safe)
    @Value("#{user.name ?: 'Unknown'}")
    private String userName;
    
    // Logical operators
    @Value("#{userCount > 0 and userCount < 100}")
    private boolean inRange;
    
    @Value("#{isAdmin or isModerator}")
    private boolean hasAccess;
    
    @Value("#{!isGuest}")
    private boolean isNotGuest;
}
```

### 13.6. Type Operators

```java
@Component
public class TypeExamples {
    
    // instanceof
    @Value("#{user instanceof T(com.example.domain.User)}")
    private boolean isUser;
    
    // Type reference
    @Value("#{T(java.lang.Math).PI}")
    private double pi;
    
    @Value("#{T(java.lang.Math).random()}")
    private double random;
    
    // Static method
    @Value("#{T(java.lang.Integer).parseInt('123')}")
    private int number;
}
```

### 13.7. Safe Navigation

```java
@Component
public class SafeNavigationExamples {
    
    // Without safe navigation (may throw NullPointerException)
    @Value("#{user.address.city}")
    private String city1;
    
    // With safe navigation (returns null if any intermediate is null)
    @Value("#{user?.address?.city}")
    private String city2;
}
```

---

## 14. BEST PRACTICES

### 14.1. Dependency Injection

✅ **DO:**
- Use constructor injection for required dependencies
- Use setter injection for optional dependencies
- Keep constructors simple
- Use final fields with constructor injection

❌ **DON'T:**
- Use field injection
- Create dependencies manually in code
- Have circular dependencies

```java
// ✅ GOOD
@Service
public class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

// ❌ BAD
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
}
```

### 14.2. Bean Configuration

✅ **DO:**
- Use Java configuration over XML
- Use meaningful bean names
- Keep configuration classes focused
- Use @Primary for default implementations

❌ **DON'T:**
- Mix configuration styles unnecessarily
- Create circular dependencies
- Have complex logic in @Bean methods

```java
// ✅ GOOD
@Configuration
public class DatabaseConfig {
    
    @Bean
    @Primary
    public DataSource primaryDataSource() {
        return new HikariDataSource();
    }
}

// ❌ BAD
@Configuration
public class AllConfig {
    // Hundreds of @Bean methods mixing different concerns
}
```

### 14.3. Component Naming

```java
// ✅ GOOD - Clear, specific names
@Service
public class UserRegistrationService { }

@Repository
public class JpaUserRepository { }

@Controller
public class UserProfileController { }

// ❌ BAD - Generic, unclear names
@Service
public class ServiceImpl { }

@Repository
public class RepoImpl { }
```

### 14.4. Bean Scopes

✅ **DO:**
- Use singleton (default) for stateless beans
- Use prototype for stateful beans
- Consider thread-safety for singleton beans

❌ **DON'T:**
- Make singletons stateful without synchronization
- Use prototype unnecessarily

### 14.5. Property Management

✅ **DO:**
- Externalize configuration
- Use profiles for environment-specific config
- Provide default values
- Use type-safe @ConfigurationProperties

❌ **DON'T:**
- Hardcode values
- Use @Value for complex configurations
- Ignore missing properties without defaults

```java
// ✅ GOOD
@Value("${app.timeout:30}")
private int timeout;

// ❌ BAD
private int timeout = 30; // Hardcoded
```

### 14.6. Testing

```java
// ✅ GOOD - Test with mocks, not Spring context
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    void testCreateUser() {
        // Test logic
    }
}

// Use Spring context only for integration tests
@SpringBootTest
class UserServiceIntegrationTest {
}
```

---

## 15. CÂU HỎI MẪU CHO KỲ THI

### 15.1. Câu hỏi lý thuyết

#### Câu 1: Sự khác biệt giữa BeanFactory và ApplicationContext?

**Trả lời**:
- **BeanFactory**: Interface cơ bản cho IoC container, lazy initialization
- **ApplicationContext**: Extends BeanFactory, thêm enterprise features:
  - Eager bean initialization
  - Internationalization (i18n)
  - Event publication
  - Declarative AOP support
  - WebApplicationContext cho web apps

**Recommendation**: Luôn dùng ApplicationContext

---

#### Câu 2: Tại sao constructor injection được recommend hơn field injection?

**Trả lời**:
1. **Immutability**: Có thể dùng final fields
2. **Testability**: Dễ test với constructor parameters
3. **Explicitness**: Dependencies rõ ràng
4. **Required dependencies**: Constructor enforcement
5. **No reflection**: Không cần reflection để inject

---

#### Câu 3: Khi nào dùng @Qualifier và khi nào dùng @Primary?

**Trả lời**:
- **@Primary**: Đánh dấu bean default khi có multiple candidates. Dùng ở bean definition
- **@Qualifier**: Chọn specific bean tại injection point

**Use case**:
- @Primary cho default implementation
- @Qualifier khi cần specific implementation

---

#### Câu 4: Sự khác biệt giữa singleton và prototype scope?

**Trả lời**:

| Aspect | Singleton | Prototype |
|--------|-----------|-----------|
| Instances | One per container | New per request |
| Initialization | Eager (default) | Lazy |
| Destruction | Container manages | Container doesn't manage |
| Thread-safety | Must be thread-safe | Each instance independent |
| Use case | Stateless beans | Stateful beans |

---

#### Câu 5: @Component, @Service, @Repository khác nhau như thế nào?

**Trả lời**:
- **@Component**: Generic stereotype, base annotation
- **@Service**: Business logic layer, semantic annotation
- **@Repository**: Persistence layer, enables exception translation
- **@Controller**: Presentation layer

Về functionality, chúng giống nhau (đều tạo beans), nhưng khác về semantic meaning và một số features đặc biệt (@Repository có exception translation).

---

### 15.2. Câu hỏi code-based

#### Câu 6: Code sau có vấn đề gì?

```java
@Service
public class UserService {
    @Autowired
    private OrderService orderService;
}

@Service
public class OrderService {
    @Autowired
    private UserService userService;
}
```

**Trả lời**: **Circular dependency**. Hai services depend lẫn nhau.

**Solutions**:
1. Refactor to remove circular dependency (best)
2. Use setter injection instead of constructor
3. Use @Lazy annotation
4. Restructure code

```java
// Solution with @Lazy
@Service
public class UserService {
    private final OrderService orderService;
    
    public UserService(@Lazy OrderService orderService) {
        this.orderService = orderService;
    }
}
```

---

#### Câu 7: Làm thế nào để inject tất cả beans implement một interface?

```java
public interface MessageSender {
    void send(String message);
}

@Component
public class EmailSender implements MessageSender { }

@Component
public class SmsSender implements MessageSender { }

@Service
public class NotificationService {
    
    @Autowired
    private List<MessageSender> senders; // ✅ All implementations
    
    public void notifyAll(String message) {
        senders.forEach(sender -> sender.send(message));
    }
}
```

---

#### Câu 8: Configure DataSource khác nhau cho dev và prod?

```java
@Configuration
public class DataSourceConfig {
    
    @Bean
    @Profile("development")
    public DataSource devDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:testdb");
        return dataSource;
    }
    
    @Bean
    @Profile("production")
    public DataSource prodDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://prod-server/mydb");
        return dataSource;
    }
}
```

**Activate:**
```properties
# application-dev.properties
spring.profiles.active=development

# application-prod.properties
spring.profiles.active=production
```

---

### 15.3. Scenario-based Questions

#### Câu 9: Singleton bean inject prototype bean. Vấn đề gì xảy ra và cách fix?

**Vấn đề**: Singleton bean chỉ được created một lần, nên prototype bean cũng chỉ được inject một lần.

```java
@Service // Singleton
public class SingletonService {
    
    @Autowired
    private PrototypeBean prototypeBean; // Only injected once!
    
    public void doSomething() {
        prototypeBean.process(); // Always same instance
    }
}

@Component
@Scope("prototype")
public class PrototypeBean {
    public void process() { }
}
```

**Solutions**:

1. **Lookup Method Injection**:
```java
@Service
public abstract class SingletonService {
    
    public void doSomething() {
        PrototypeBean bean = getPrototypeBean();
        bean.process();
    }
    
    @Lookup
    protected abstract PrototypeBean getPrototypeBean();
}
```

2. **ObjectFactory**:
```java
@Service
public class SingletonService {
    
    @Autowired
    private ObjectFactory<PrototypeBean> prototypeBeanFactory;
    
    public void doSomething() {
        PrototypeBean bean = prototypeBeanFactory.getObject();
        bean.process();
    }
}
```

3. **ApplicationContext**:
```java
@Service
public class SingletonService {
    
    @Autowired
    private ApplicationContext context;
    
    public void doSomething() {
        PrototypeBean bean = context.getBean(PrototypeBean.class);
        bean.process();
    }
}
```

---

#### Câu 10: Làm thế nào để change bean behavior based on property value?

```java
@Configuration
public class ServiceConfig {
    
    @Bean
    @ConditionalOnProperty(
        name = "service.type",
        havingValue = "fast"
    )
    public MessageService fastMessageService() {
        return new FastMessageService();
    }
    
    @Bean
    @ConditionalOnProperty(
        name = "service.type",
        havingValue = "reliable"
    )
    public MessageService reliableMessageService() {
        return new ReliableMessageService();
    }
}
```

**application.properties:**
```properties
service.type=fast  # or reliable
```

---

## 16. TÓM TẮT VÀ MẸO THI

### 16.1. Core Concepts Cheat Sheet

| Concept | Key Points |
|---------|-----------|
| **IoC** | Container controls object creation and lifecycle |
| **DI** | Dependencies injected from outside |
| **Bean** | Object managed by Spring container |
| **ApplicationContext** | Main Spring container interface |
| **@Configuration** | Declares configuration class |
| **@Bean** | Declares bean in Java config |
| **@Component** | Generic stereotype annotation |
| **@Autowired** | Automatic dependency injection |

### 16.2. Annotations Quick Reference

**Stereotypes:**
```java
@Component      // Generic component
@Service        // Business logic
@Repository     // Data access
@Controller     // Web controller
@Configuration  // Configuration class
```

**Dependency Injection:**
```java
@Autowired      // Automatic injection
@Qualifier      // Specify which bean
@Primary        // Default bean
@Value          // Inject properties
@Resource       // JSR-250
@Inject         // JSR-330
```

**Configuration:**
```java
@Bean                    // Define bean
@Scope                   // Bean scope
@Lazy                    // Lazy initialization
@DependsOn               // Bean dependency order
@Profile                 // Environment profiles
@PropertySource          // Load properties
@ComponentScan           // Enable scanning
@Import                  // Import config
@ImportResource          // Import XML
```

**Lifecycle:**
```java
@PostConstruct   // After construction
@PreDestroy      // Before destruction
```

### 16.3. DI Best Practices

```
┌─────────────────────────────────────────┐
│     Dependency Injection Priority       │
├─────────────────────────────────────────┤
│                                         │
│  1. Constructor Injection               │
│     ✅ ALWAYS PREFER THIS               │
│     - Immutable (final)                 │
│     - Required deps enforced            │
│     - Easy to test                      │
│                                         │
│  2. Setter Injection                    │
│     - Optional dependencies             │
│     - Reconfigurable beans              │
│                                         │
│  3. Field Injection                     │
│     ❌ AVOID                            │
│     - Hard to test                      │
│     - Not immutable                     │
│     - Hidden dependencies               │
│                                         │
└─────────────────────────────────────────┘
```

### 16.4. Bean Scopes Summary

```java
// Singleton (default) - One instance per container
@Scope("singleton")

// Prototype - New instance per request
@Scope("prototype")

// Web scopes
@Scope("request")      // Per HTTP request
@Scope("session")      // Per HTTP session
@Scope("application")  // Per ServletContext
```

### 16.5. Common Pitfalls

❌ **Mistake 1**: Field injection
```java
// BAD
@Autowired
private UserRepository userRepository;

// GOOD
private final UserRepository userRepository;

public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
}
```

❌ **Mistake 2**: Circular dependencies
```java
// BAD - Circular dependency
@Service
public class A {
    @Autowired private B b;
}

@Service
public class B {
    @Autowired private A a;
}

// GOOD - Refactor or use @Lazy
```

❌ **Mistake 3**: Multiple beans without @Primary or @Qualifier
```java
// BAD - Ambiguous
@Autowired
private MessageSender sender;  // Multiple implementations!

// GOOD - Clear
@Autowired
@Qualifier("emailSender")
private MessageSender sender;
```

❌ **Mistake 4**: Stateful singleton beans
```java
// BAD - Not thread-safe
@Service
public class CounterService {
    private int count = 0;  // Shared state!
    
    public void increment() {
        count++;
    }
}

// GOOD - Prototype or thread-safe
@Service
@Scope("prototype")
public class CounterService {
    private int count = 0;
}
```

### 16.6. Mẹo làm bài thi

1. ✅ **IoC = Container controls**, DI = Inject dependencies
2. ✅ **ApplicationContext > BeanFactory** (always)
3. ✅ **Constructor injection > Setter > Field**
4. ✅ **@Component** is generic, others are specialized
5. ✅ **Singleton** = one instance, **Prototype** = many instances
6. ✅ **@Primary** for default, **@Qualifier** for specific
7. ✅ **@PostConstruct** after creation, **@PreDestroy** before destruction
8. ✅ **@Profile** for environments
9. ✅ **@Value** for properties, **Environment** for programmatic access
10. ✅ **Circular dependencies** → refactor or @Lazy

### 16.7. Checklist ôn tập

- [ ] IoC và DI concepts
- [ ] ApplicationContext vs BeanFactory
- [ ] Bean configuration (Java, Annotations, XML)
- [ ] Bean lifecycle và callbacks
- [ ] Bean scopes (singleton, prototype, web)
- [ ] Dependency injection methods
- [ ] @Autowired, @Qualifier, @Primary
- [ ] Component scanning và filters
- [ ] Profiles cho environments
- [ ] Property sources và @Value
- [ ] SpEL basics
- [ ] Constructor vs Setter vs Field injection
- [ ] Common pitfalls

### 16.8. Configuration Strategy

```java
// Modern Spring application structure
@SpringBootApplication  // Includes @Configuration, @ComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

@Configuration
public class DataSourceConfig {
    // Infrastructure beans
}

@Configuration
public class SecurityConfig {
    // Security beans
}

@Service
public class UserService {
    // Business logic with constructor injection
}

@Repository
public class UserRepository {
    // Data access
}
```

---

## KẾT LUẬN

Spring Core là nền tảng của toàn bộ Spring Framework và là chủ đề quan trọng nhất trong Spring Professional Certification. Để thành công, bạn cần:

- ✅ Hiểu rõ IoC và DI principles
- ✅ Nắm vững ApplicationContext và bean lifecycle
- ✅ Master các configuration methods
- ✅ Biết khi nào dùng scope nào
- ✅ Understand autowiring và ambiguity resolution
- ✅ Proficient với profiles và properties

### Điểm quan trọng nhất:

> **Spring Core = IoC Container + Dependency Injection**
>
> Key principles:
> - Container quản lý objects (beans)
> - Dependencies được injected, không tự tạo
> - Configuration có thể là Java, Annotations, hoặc XML
> - Constructor injection là best practice
> - Singleton là default scope
> - @Primary và @Qualifier giải quyết ambiguity

**Remember the fundamentals:**
- 🎯 IoC = Inversion of Control (Container controls)
- 🎯 DI = Dependency Injection (Inject from outside)
- 🎯 Bean = Spring-managed object
- 🎯 ApplicationContext = Main container
- 🎯 Constructor injection = Best practice

**Configuration hierarchy:**
```
1. Java Config (@Configuration, @Bean)
2. Annotations (@Component, @Service, etc.)
3. XML (legacy, avoid in new projects)
```

Hãy thực hành với các ví dụ trong tài liệu này và hiểu rõ WHY, không chỉ HOW. Spring Core là foundation - nắm vững nó sẽ giúp bạn hiểu tất cả các modules khác!

---

**Chúc bạn thành công với Spring Professional Certification!** 🚀🎓

*Tài liệu được tạo ngày 26/12/2024*
