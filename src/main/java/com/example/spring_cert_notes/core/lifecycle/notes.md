# NGÀY 7: SPRING BEAN LIFECYCLE & PROXIES

## 📚 MỤC TIÊU HỌC TẬP

### 1. Nắm vững 11 bước của Bean Lifecycle
### 2. Hiểu BeanFactoryPostProcessor vs BeanPostProcessor
### 3. Phân biệt JDK Dynamic Proxy vs CGLIB Proxy
### 4. Giải quyết Circular Dependency

---

## 🎯 PHẦN 1: BEAN LIFECYCLE (11 BƯỚC)

### Sơ đồ Bean Lifecycle

```
┌─────────────────────────────────────────────────────────────┐
│                    BEAN LIFECYCLE                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ PHASE 1: INSTANTIATION                              │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │ 1. Constructor called                               │   │
│  │ 2. Dependencies injected (DI)                       │   │
│  └─────────────────────────────────────────────────────┘   │
│                          ↓                                  │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ PHASE 2: AWARE INTERFACES                           │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │ 3. BeanNameAware.setBeanName()                      │   │
│  │ 4. BeanFactoryAware.setBeanFactory()                │   │
│  │ 5. ApplicationContextAware.setApplicationContext()  │   │
│  └─────────────────────────────────────────────────────┘   │
│                          ↓                                  │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ PHASE 3: INITIALIZATION                             │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │ 6. BeanPostProcessor.postProcessBeforeInit()        │   │
│  │ 7. @PostConstruct                                   │   │
│  │ 8. InitializingBean.afterPropertiesSet()            │   │
│  │ 9. Custom init-method                               │   │
│  │ 10. BeanPostProcessor.postProcessAfterInit()        │   │
│  └─────────────────────────────────────────────────────┘   │
│                          ↓                                  │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ 11. BEAN IS READY FOR USE                           │   │
│  └─────────────────────────────────────────────────────┘   │
│                          ↓                                  │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ PHASE 4: DESTRUCTION                                │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │ 12. @PreDestroy                                     │   │
│  │ 13. DisposableBean.destroy()                        │   │
│  │ 14. Custom destroy-method                           │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Chi tiết từng bước

#### Phase 1: Instantiation
```java
// Step 1: Constructor
public MyBean() {
    System.out.println("1. Constructor called");
}

// Step 2: Dependency Injection
@Autowired
public MyBean(DependencyA a, DependencyB b) {
    this.a = a;
    this.b = b;
}
```

#### Phase 2: Aware Interfaces
```java
@Component
public class MyBean implements BeanNameAware, BeanFactoryAware, ApplicationContextAware {
    
    // Step 3
    @Override
    public void setBeanName(String name) {
        System.out.println("3. Bean name: " + name);
    }
    
    // Step 4
    @Override
    public void setBeanFactory(BeanFactory factory) {
        System.out.println("4. BeanFactory set");
    }
    
    // Step 5
    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        System.out.println("5. ApplicationContext set");
    }
}
```

#### Phase 3: Initialization
```java
@Component
public class MyBean implements InitializingBean {
    
    // Step 7: @PostConstruct (JSR-250)
    @PostConstruct
    public void postConstruct() {
        System.out.println("7. @PostConstruct");
    }
    
    // Step 8: InitializingBean
    @Override
    public void afterPropertiesSet() {
        System.out.println("8. afterPropertiesSet()");
    }
    
    // Step 9: Custom init-method (via @Bean)
    public void customInit() {
        System.out.println("9. Custom init-method");
    }
}
```

#### Phase 4: Destruction
```java
@Component
public class MyBean implements DisposableBean {
    
    // Step 12: @PreDestroy
    @PreDestroy
    public void preDestroy() {
        System.out.println("12. @PreDestroy");
    }
    
    // Step 13: DisposableBean
    @Override
    public void destroy() {
        System.out.println("13. destroy()");
    }
    
    // Step 14: Custom destroy-method
    public void customDestroy() {
        System.out.println("14. Custom destroy-method");
    }
}
```

**Xem code:** `SampleLifecycleBean.java`

---

## 🎯 PHẦN 2: BEANFACTORYPOSTPROCESSOR vs BEANPOSTPROCESSOR

### So sánh

| Feature | BeanFactoryPostProcessor | BeanPostProcessor |
|---------|-------------------------|-------------------|
| **Timing** | Before beans created | After each bean created |
| **Target** | Bean definitions | Bean instances |
| **Can modify** | Metadata, scope, properties | Bean instance, wrap proxy |
| **Use case** | Modify config | Add behavior |

### BeanFactoryPostProcessor
```java
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) {
        // Called BEFORE any beans are created
        // Can modify bean definitions
        
        BeanDefinition bd = factory.getBeanDefinition("myBean");
        bd.setScope("prototype");
        bd.setLazyInit(true);
        bd.getPropertyValues().add("property", "value");
    }
}
```

**Use cases:**
- Modify bean scope
- Change lazy initialization
- Override property values
- Register additional beans

### BeanPostProcessor
```java
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String name) {
        // Called BEFORE @PostConstruct
        // Can modify or replace bean
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String name) {
        // Called AFTER @PostConstruct
        // This is where AOP proxies are created
        return bean;  // or return proxy
    }
}
```

**Use cases:**
- Logging bean creation
- Wrapping beans with proxies (AOP)
- Validating bean configuration
- Custom initialization

**Xem code:** `CustomBeanFactoryPostProcessor.java`, `CustomBeanPostProcessor.java`

---

## 🎯 PHẦN 3: JDK DYNAMIC PROXY vs CGLIB PROXY

### Khi nào Spring dùng loại nào?

```
┌─────────────────────────────────────────────────────────────┐
│                    PROXY SELECTION                          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Bean implements interface?                                 │
│           │                                                 │
│     ┌─────┴─────┐                                          │
│     │           │                                          │
│    YES          NO                                         │
│     │           │                                          │
│     ↓           ↓                                          │
│  ┌──────┐   ┌──────┐                                       │
│  │ JDK  │   │CGLIB │                                       │
│  │Proxy │   │Proxy │                                       │
│  └──────┘   └──────┘                                       │
│                                                             │
│  (unless proxyTargetClass=true, then always CGLIB)         │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### JDK Dynamic Proxy

```java
// Interface
public interface PaymentService {
    void processPayment(String orderId);
}

// Implementation
@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public void processPayment(String orderId) { }
}

// Spring creates JDK proxy implementing PaymentService
// Must inject via interface type!
@Autowired
private PaymentService paymentService;  // ✅ Works
// private PaymentServiceImpl paymentService;  // ❌ Fails!
```

**Characteristics:**
- ✅ Requires interface
- ✅ Faster to create
- ✅ Standard Java feature
- ❌ Must use interface type for injection
- ❌ Cannot proxy class-only methods

### CGLIB Proxy

```java
// No interface
@Service
public class OrderService {
    public void createOrder(String orderId) { }
    
    // final methods cannot be proxied!
    public final void getDetails() { }
}

// Spring creates CGLIB proxy (subclass)
@Autowired
private OrderService orderService;  // ✅ Works
```

**Characteristics:**
- ✅ No interface required
- ✅ Can use concrete class type
- ❌ Class cannot be final
- ❌ Methods cannot be final
- ❌ Slightly slower to create
- ❌ Requires CGLIB library

### Configuration

```java
// Default: JDK proxy if interface exists
@EnableAspectJAutoProxy
@EnableAspectJAutoProxy(proxyTargetClass = false)

// Force CGLIB for all
@EnableAspectJAutoProxy(proxyTargetClass = true)

// Spring Boot default (application.properties)
spring.aop.proxy-target-class=true  // CGLIB for all
```

### Checking Proxy Type

```java
import org.springframework.aop.support.AopUtils;

// Check if proxy
boolean isProxy = AopUtils.isAopProxy(bean);

// Check proxy type
boolean isJdk = AopUtils.isJdkDynamicProxy(bean);
boolean isCglib = AopUtils.isCglibProxy(bean);

// Get target class
Class<?> targetClass = AopUtils.getTargetClass(bean);
```

**Xem code:** `ProxyInspector.java`, `PaymentService.java`, `OrderService.java`

---

## 🎯 PHẦN 4: CIRCULAR DEPENDENCY

### Vấn đề

```java
@Service
class ServiceA {
    @Autowired
    private ServiceB serviceB;  // Needs B
}

@Service
class ServiceB {
    @Autowired
    private ServiceA serviceA;  // Needs A
}

// Result: BeanCurrentlyInCreationException
```

### Solution 1: @Lazy (RECOMMENDED)

```java
@Service
class ServiceA {
    private final ServiceB serviceB;
    
    @Autowired
    public ServiceA(@Lazy ServiceB serviceB) {
        // serviceB is a PROXY here
        this.serviceB = serviceB;
    }
}
```

**How it works:**
1. Spring creates proxy for ServiceB
2. ServiceA gets the proxy (not real bean)
3. ServiceB is created normally
4. When ServiceA uses serviceB, proxy resolves to real bean

### Solution 2: Setter Injection

```java
@Service
class ServiceA {
    private ServiceB serviceB;
    
    @Autowired
    public void setServiceB(ServiceB serviceB) {
        this.serviceB = serviceB;
    }
}

@Service
class ServiceB {
    private ServiceA serviceA;
    
    @Autowired
    public void setServiceA(ServiceA serviceA) {
        this.serviceA = serviceA;
    }
}
```

**How it works:**
1. ServiceA created (no dependencies yet)
2. ServiceB created (no dependencies yet)
3. ServiceA.setServiceB() called
4. ServiceB.setServiceA() called

### Solution 3: ObjectProvider

```java
@Service
class ServiceA {
    private final ObjectProvider<ServiceB> serviceBProvider;
    
    @Autowired
    public ServiceA(ObjectProvider<ServiceB> serviceBProvider) {
        this.serviceBProvider = serviceBProvider;
    }
    
    public void doWork() {
        // Resolved lazily when needed
        serviceBProvider.getObject().doSomething();
    }
}
```

### Solution 4: Refactor (BEST)

```java
// Extract common logic to new service
@Service
class CommonService {
    // Shared logic
}

@Service
class ServiceA {
    @Autowired
    private CommonService commonService;
}

@Service
class ServiceB {
    @Autowired
    private CommonService commonService;
}
```

**Xem code:** `CircularDependencyDemo.java`

---

## ✅ CHECKLIST HOÀN THÀNH

### Bean Lifecycle (11 bước)
- [ ] 1. Constructor
- [ ] 2. Dependency Injection
- [ ] 3. BeanNameAware.setBeanName()
- [ ] 4. BeanFactoryAware.setBeanFactory()
- [ ] 5. ApplicationContextAware.setApplicationContext()
- [ ] 6. BeanPostProcessor.postProcessBeforeInitialization()
- [ ] 7. @PostConstruct
- [ ] 8. InitializingBean.afterPropertiesSet()
- [ ] 9. Custom init-method
- [ ] 10. BeanPostProcessor.postProcessAfterInitialization()
- [ ] 11. Bean is READY

### BeanFactoryPostProcessor vs BeanPostProcessor
- [ ] BeanFactoryPostProcessor modifies bean definitions
- [ ] BeanPostProcessor modifies bean instances
- [ ] BeanFactoryPostProcessor runs before beans created
- [ ] BeanPostProcessor runs for each bean

### Proxies
- [ ] JDK Proxy requires interface
- [ ] CGLIB Proxy creates subclass
- [ ] Check proxy type with AopUtils
- [ ] proxyTargetClass=true forces CGLIB
- [ ] final classes/methods cannot be CGLIB proxied

### Circular Dependency
- [ ] @Lazy creates proxy to break cycle
- [ ] Setter injection allows partial construction
- [ ] ObjectProvider for lazy resolution
- [ ] Refactoring is the best solution

---

## 🚀 CÁCH CHẠY DEMO

```bash
mvn exec:java -Dexec.mainClass="com.example.spring_cert_notes.core.lifecycle.LifecycleDemo"
```

---

## 📝 GHI CHÚ QUAN TRỌNG

### Best Practices

1. **Lifecycle callbacks:**
   - Prefer @PostConstruct/@PreDestroy
   - Use InitializingBean only when needed
   - Custom init-method for third-party classes

2. **Proxies:**
   - Program to interfaces when possible
   - Avoid final classes/methods if proxying needed
   - Use proxyTargetClass=true for consistency

3. **Circular dependencies:**
   - Refactor to eliminate (best)
   - Use @Lazy as quick fix
   - Avoid setter injection for required deps

### Common Mistakes

1. ❌ Expecting @PostConstruct before DI
2. ❌ Using final class with CGLIB proxy
3. ❌ Injecting concrete class when JDK proxy
4. ❌ Circular dependency without @Lazy
5. ❌ Confusing BeanFactoryPostProcessor with BeanPostProcessor

---

## 📚 FILES TRONG PACKAGE

1. `SampleLifecycleBean.java` - Complete lifecycle demo
2. `CustomBeanPostProcessor.java` - BeanPostProcessor example
3. `CustomBeanFactoryPostProcessor.java` - BeanFactoryPostProcessor
4. `ProxyInspector.java` - Proxy type detection
5. `PaymentService.java` - Interface for JDK proxy
6. `PaymentServiceImpl.java` - JDK proxy demo
7. `OrderService.java` - CGLIB proxy demo
8. `LoggingAspect.java` - Aspect to trigger proxies
9. `CircularDependencyDemo.java` - Circular dependency solutions
10. `LifecycleDemo.java` - Main demo class
