# NGÀY 5-6: ANNOTATION-BASED CONFIG & STEREOTYPES

## 📚 MỤC TIÊU HỌC TẬP

### 1. Hiểu rõ @Component, @Service, @Repository, @Controller
### 2. Thành thạo Component Scanning với filters
### 3. Nắm vững @PostConstruct, @PreDestroy lifecycle callbacks
### 4. Tạo Custom Stereotype Annotations

---

## 🎯 PHẦN 1: STEREOTYPE ANNOTATIONS

### Stereotype Hierarchy

```
@Component (base)
    ├── @Service      - Business logic layer
    ├── @Repository   - Data access layer (+ exception translation)
    └── @Controller   - Web/MVC layer
        └── @RestController (@Controller + @ResponseBody)
```

### Khi nào dùng từng Stereotype?

| Annotation | Layer | Use Case | Special Features |
|------------|-------|----------|------------------|
| `@Component` | Generic | Utility classes, helpers | Base stereotype |
| `@Service` | Business | Business logic, transactions | Semantic clarity |
| `@Repository` | Data | DAO, data access | Exception translation |
| `@Controller` | Web | MVC controllers | Request mapping |
| `@RestController` | REST | REST APIs | Auto @ResponseBody |

### @Component - Generic Component
```java
@Component
public class EmailValidator {
    // Generic utility component
    public boolean isValid(String email) {
        return email.matches(".*@.*\\..*");
    }
}

@Component("customName")  // Custom bean name
public class MyComponent { }
```

**Khi nào dùng:**
- Utility classes
- Helper components
- Classes không thuộc layer cụ thể

### @Service - Business Layer
```java
@Service
public class OrderService {
    
    private final OrderRepository repository;
    
    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }
    
    public void processOrder(Order order) {
        // Business logic
        validateOrder(order);
        calculateTotal(order);
        repository.save(order);
    }
}
```

**Khi nào dùng:**
- Business logic
- Service layer classes
- Transaction boundaries
- Orchestration logic

### @Repository - Data Access Layer
```java
@Repository
public class OrderRepository {
    
    @PersistenceContext
    private EntityManager em;
    
    public Order findById(Long id) {
        return em.find(Order.class, id);
    }
    
    public void save(Order order) {
        em.persist(order);
    }
}
```

**Khi nào dùng:**
- DAO classes
- Data access logic
- Database operations

**Special Feature: Exception Translation**
```java
// @Repository enables automatic exception translation
// PersistenceException → DataAccessException
// SQLException → DataAccessException
```

### @Controller - Web MVC Layer
```java
@Controller
public class OrderController {
    
    @GetMapping("/orders")
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "orders/list";  // View name
    }
}

@RestController  // = @Controller + @ResponseBody
public class OrderRestController {
    
    @GetMapping("/api/orders")
    public List<Order> getOrders() {
        return orderService.findAll();  // Returns JSON
    }
}
```

**Xem code:** `StereotypeExamples.java`

---

## 🎯 PHẦN 2: COMPONENT SCANNING

### Basic Component Scanning
```java
@Configuration
@ComponentScan(basePackages = "com.example.myapp")
public class AppConfig { }

// Or using type-safe class reference
@ComponentScan(basePackageClasses = MyService.class)
```

### Filter Types

| FilterType | Description | Example |
|------------|-------------|---------|
| `ANNOTATION` | By annotation | `@MyAnnotation` |
| `ASSIGNABLE_TYPE` | By class/interface | `MyInterface.class` |
| `REGEX` | By class name pattern | `".*Repository"` |
| `ASPECTJ` | By AspectJ expression | `"com.example..*Service"` |
| `CUSTOM` | Custom TypeFilter | `MyTypeFilter.class` |

### Include Filters
```java
@ComponentScan(
    basePackages = "com.example",
    includeFilters = @Filter(
        type = FilterType.ANNOTATION,
        classes = MyCustomAnnotation.class
    )
)
```

### Exclude Filters
```java
// Exclude by class type
@ComponentScan(
    basePackages = "com.example",
    excludeFilters = @Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = ExcludedClass.class
    )
)

// Exclude by regex pattern
@ComponentScan(
    basePackages = "com.example",
    excludeFilters = @Filter(
        type = FilterType.REGEX,
        pattern = ".*Test.*"
    )
)
```

### Multiple Filters
```java
@ComponentScan(
    basePackages = "com.example",
    includeFilters = {
        @Filter(type = FilterType.ANNOTATION, classes = MyAnnotation.class),
        @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MyInterface.class)
    },
    excludeFilters = {
        @Filter(type = FilterType.REGEX, pattern = ".*Mock.*"),
        @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TestConfig.class)
    }
)
```

### Disable Default Filters
```java
@ComponentScan(
    basePackages = "com.example",
    useDefaultFilters = false,  // Disable @Component, @Service, etc.
    includeFilters = @Filter(
        type = FilterType.ANNOTATION,
        classes = MyCustomAnnotation.class
    )
)
// Only scans @MyCustomAnnotation, ignores standard stereotypes
```

**Xem code:** `ComponentScanConfig.java`

---

## 🎯 PHẦN 3: LIFECYCLE CALLBACKS

### Bean Lifecycle Order

```
1. Instantiation (Constructor)
   ↓
2. Dependency Injection
   ↓
3. @PostConstruct
   ↓
4. InitializingBean.afterPropertiesSet()
   ↓
5. Custom init-method (@Bean(initMethod="..."))
   ↓
6. Bean is READY
   ↓
   ... Bean in use ...
   ↓
7. @PreDestroy
   ↓
8. DisposableBean.destroy()
   ↓
9. Custom destroy-method (@Bean(destroyMethod="..."))
```

### Method 1: @PostConstruct & @PreDestroy (RECOMMENDED)
```java
@Component
public class MyBean {
    
    @PostConstruct
    public void init() {
        // Called after dependency injection
        // Initialize resources, connections, caches
        System.out.println("Bean initialized");
    }
    
    @PreDestroy
    public void cleanup() {
        // Called before bean destruction
        // Close connections, release resources
        System.out.println("Bean destroyed");
    }
}
```

**Ưu điểm:**
- ✅ Standard Java annotations (JSR-250)
- ✅ Clean, declarative
- ✅ No Spring dependency in code
- ✅ Works with any DI framework

### Method 2: InitializingBean & DisposableBean
```java
@Component
public class MyBean implements InitializingBean, DisposableBean {
    
    @Override
    public void afterPropertiesSet() throws Exception {
        // Called after properties set
        System.out.println("afterPropertiesSet called");
    }
    
    @Override
    public void destroy() throws Exception {
        // Called on context close
        System.out.println("destroy called");
    }
}
```

**Ưu điểm:**
- ✅ Explicit contract
- ✅ IDE support for method names

**Nhược điểm:**
- ❌ Couples code to Spring
- ❌ More verbose

### Method 3: @Bean init/destroy methods
```java
@Configuration
public class AppConfig {
    
    @Bean(initMethod = "init", destroyMethod = "cleanup")
    public MyBean myBean() {
        return new MyBean();
    }
}

public class MyBean {
    public void init() {
        System.out.println("Custom init method");
    }
    
    public void cleanup() {
        System.out.println("Custom destroy method");
    }
}
```

**Ưu điểm:**
- ✅ No annotations needed on bean class
- ✅ Good for third-party classes
- ✅ Flexible method names

### Practical Examples

**Database Connection Pool:**
```java
@Component
public class ConnectionPool {
    
    private List<Connection> connections;
    
    @PostConstruct
    public void initPool() {
        connections = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            connections.add(createConnection());
        }
        System.out.println("Connection pool initialized with 10 connections");
    }
    
    @PreDestroy
    public void closePool() {
        for (Connection conn : connections) {
            conn.close();
        }
        System.out.println("All connections closed");
    }
}
```

**Cache Warming:**
```java
@Component
public class CacheManager {
    
    @PostConstruct
    public void warmUpCache() {
        // Load frequently accessed data
        loadUserCache();
        loadProductCache();
        System.out.println("Cache warmed up");
    }
    
    @PreDestroy
    public void flushCache() {
        // Persist cache to disk if needed
        persistToDisk();
        System.out.println("Cache flushed");
    }
}
```

**Xem code:** `LifecycleCallbacksExample.java`

---

## 🎯 PHẦN 4: CUSTOM STEREOTYPE ANNOTATIONS

### Tạo Custom Stereotype
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component  // Meta-annotation - makes this a stereotype
public @interface Gateway {
    
    @AliasFor(annotation = Component.class)
    String value() default "";
}
```

### Sử dụng Custom Stereotype
```java
@Gateway
public class PaymentGateway {
    // This class is now a Spring bean
    // Scanned automatically like @Component
}

@Gateway("customGatewayName")
public class ShippingGateway {
    // With custom bean name
}
```

### Common Custom Stereotypes

```java
// For API Gateways
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface Gateway { }

// For Validators
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Validator { }

// For Mappers
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Mapper { }

// For Adapters
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Adapter { }

// For Schedulers
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Scheduler { }
```

**Xem code:** `CustomStereotype.java`, `Gateway.java`

---

## 🎯 PHẦN 5: DEBUGGING COMPONENT SCANNING

### Common Issues

**1. Bean not found**
```
NoSuchBeanDefinitionException: No qualifying bean of type 'MyService'
```

**Causes:**
- Missing @Component/@Service annotation
- Package not in @ComponentScan path
- Excluded by filter
- Profile not active

**Debug:**
```java
// List all registered beans
String[] beanNames = context.getBeanDefinitionNames();
for (String name : beanNames) {
    System.out.println(name);
}
```

**2. Multiple beans found**
```
NoUniqueBeanDefinitionException: expected single matching bean but found 2
```

**Solutions:**
- Use @Primary on preferred bean
- Use @Qualifier to specify bean name
- Use @Profile to separate environments

**3. Circular dependency**
```
BeanCurrentlyInCreationException: Circular reference
```

**Solutions:**
- Use @Lazy on one dependency
- Use setter injection instead of constructor
- Refactor to remove circular dependency

### Enable Debug Logging
```properties
# application.properties
logging.level.org.springframework.context=DEBUG
logging.level.org.springframework.beans=DEBUG
```

---

## ✅ CHECKLIST HOÀN THÀNH

### Stereotype Annotations
- [ ] Hiểu @Component là base stereotype
- [ ] Biết khi nào dùng @Service (business layer)
- [ ] Biết khi nào dùng @Repository (data layer + exception translation)
- [ ] Biết khi nào dùng @Controller/@RestController (web layer)
- [ ] Tạo được custom stereotype annotation

### Component Scanning
- [ ] Cấu hình @ComponentScan với basePackages
- [ ] Sử dụng basePackageClasses (type-safe)
- [ ] Include filters với FilterType.ANNOTATION
- [ ] Include filters với FilterType.ASSIGNABLE_TYPE
- [ ] Exclude filters với FilterType.REGEX
- [ ] Multiple filters (include + exclude)
- [ ] Disable default filters (useDefaultFilters = false)

### Lifecycle Callbacks
- [ ] @PostConstruct - initialization
- [ ] @PreDestroy - cleanup
- [ ] InitializingBean.afterPropertiesSet()
- [ ] DisposableBean.destroy()
- [ ] @Bean(initMethod, destroyMethod)
- [ ] Hiểu thứ tự gọi các lifecycle methods

### Debugging
- [ ] List all registered beans
- [ ] Debug NoSuchBeanDefinitionException
- [ ] Debug NoUniqueBeanDefinitionException
- [ ] Enable Spring debug logging

---

## 🚀 CÁCH CHẠY DEMO

```bash
mvn exec:java -Dexec.mainClass="com.example.spring_cert_notes.core.annotation.AnnotationDemo"
```

---

## 📝 GHI CHÚ QUAN TRỌNG

### Best Practices

1. **Chọn đúng stereotype:**
   - @Service cho business logic
   - @Repository cho data access
   - @Controller cho web layer
   - @Component cho generic

2. **Sử dụng @PostConstruct/@PreDestroy:**
   - Standard Java annotations
   - Không couple với Spring
   - Clean và declarative

3. **Component scanning:**
   - Sử dụng basePackageClasses thay vì string
   - Refactoring-safe
   - Type-safe

4. **Custom stereotypes:**
   - Tạo domain-specific annotations
   - Improve code readability
   - Consistent naming

### Common Mistakes

1. ❌ Quên @ComponentScan
2. ❌ Package không nằm trong scan path
3. ❌ Dùng @Component cho tất cả (không semantic)
4. ❌ Không cleanup resources trong @PreDestroy
5. ❌ Circular dependencies
6. ❌ Multiple beans without @Primary/@Qualifier

### Tips

- `@Repository` tự động translate exceptions
- `@Controller` cần `@RequestMapping` để handle requests
- `@RestController` = `@Controller` + `@ResponseBody`
- Custom stereotype phải meta-annotate với `@Component`
- `@AliasFor` để forward attributes

---

## 📚 TÀI LIỆU THAM KHẢO

### Files trong package này:

**Stereotypes:**
1. `StereotypeExamples.java` - All stereotype examples
2. `CustomStereotype.java` - Custom stereotype annotation
3. `Gateway.java` - Custom @Gateway stereotype

**Component Scanning:**
4. `ComponentScanConfig.java` - Various scan configurations
5. `MyCustomAnnotation.java` - Custom annotation for filtering
6. `CustomAnnotatedBean.java` - Bean with custom annotation
7. `ExcludedComponent.java` - Component to be excluded

**Lifecycle:**
8. `LifecycleCallbacksExample.java` - All lifecycle methods

**Demo:**
9. `AnnotationDemo.java` - Main demo class

### Đọc thêm:
- Spring Framework Reference: Classpath Scanning
- Baeldung: Spring Component Scanning
- Baeldung: Spring Bean Lifecycle
