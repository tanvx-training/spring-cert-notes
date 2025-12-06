# NGÀY 1-2: JAVA CONFIGURATION & BEAN MANAGEMENT

## 📚 MỤC TIÊU HỌC TẬP

### 1. Hiểu sâu về ApplicationContext và Bean Lifecycle
### 2. Thành thạo @Configuration, @Bean, @ComponentScan  
### 3. Nắm vững 6 Bean Scopes
### 4. Xử lý Circular Dependency
### 5. Lazy vs Eager Initialization

---

## 🎯 PHẦN 1: APPLICATIONCONTEXT VÀ BEAN LIFECYCLE

### ApplicationContext là gì?
- **ApplicationContext** là container chính của Spring, quản lý toàn bộ beans
- Kế thừa từ BeanFactory nhưng có nhiều tính năng hơn
- Chịu trách nhiệm:
  - Khởi tạo beans
  - Quản lý dependencies
  - Quản lý lifecycle của beans

### Bean Lifecycle (Vòng đời Bean)

```
1. Constructor được gọi
   ↓
2. Dependencies được inject (DI)
   ↓
3. @PostConstruct được gọi (init method)
   ↓
4. Bean sẵn sàng sử dụng
   ↓
5. @PreDestroy được gọi (cleanup method)
   ↓
6. Bean bị destroy
```

### Ví dụ thực tế:
```java
@Repository
public class UserRepository {
    public UserRepository() {
        // Bước 1: Constructor
    }
    
    @PostConstruct
    public void init() {
        // Bước 3: Khởi tạo resources (DB connection, cache, etc.)
    }
    
    @PreDestroy
    public void cleanup() {
        // Bước 5: Dọn dẹp resources
    }
}
```

**Xem code:** `UserRepository.java`

---

## 🎯 PHẦN 2: 5 CÁCH ĐỊNH NGHĨA BEAN

### Cách 1: Sử dụng @Component (và các stereotype)
```java
@Component  // Hoặc @Service, @Repository, @Controller
public class UserRepository {
    // Spring tự động phát hiện qua component scanning
}
```

**Ưu điểm:**
- Đơn giản, nhanh chóng
- Phù hợp cho các class tự viết

**Nhược điểm:**
- Không dùng được cho third-party classes
- Ít control hơn quá trình khởi tạo

### Cách 2: Sử dụng @Bean trong @Configuration
```java
@Configuration
public class AppConfig {
    @Bean
    public EmailService emailService() {
        return new EmailService("smtp.gmail.com");
    }
}
```

**Ưu điểm:**
- Control hoàn toàn quá trình khởi tạo
- Dùng được cho third-party classes
- Có thể thêm logic phức tạp

**Nhược điểm:**
- Verbose hơn @Component

### Cách 3: @Bean với custom name và scope
```java
@Bean(name = "customName")
@Scope("prototype")
public UserService userService() {
    return new UserService();
}
```

### Cách 4: Factory Method Pattern
```java
@Bean
public DataSource dataSource() {
    return DataSourceFactory.createDataSource();
}
```

### Cách 5: Programmatic Registration
```java
@Bean
public NotificationService notificationService() {
    NotificationService service = new NotificationService();
    service.configure(...);
    return service;
}
```

**Xem code:** `BeanDefinitionDemo.java` - Chạy để xem tất cả 5 cách

---

## 🎯 PHẦN 3: @BEAN VS @COMPONENT

| Tiêu chí | @Component | @Bean |
|----------|-----------|-------|
| **Vị trí** | Trên class | Trên method trong @Configuration |
| **Component Scan** | Cần @ComponentScan | Không cần |
| **Third-party** | ❌ Không dùng được | ✅ Dùng được |
| **Control** | Ít | Nhiều |
| **Use case** | Class tự viết | Third-party, logic phức tạp |

### Khi nào dùng @Component?
- Class do bạn tự viết
- Không cần logic khởi tạo phức tạp
- Muốn code ngắn gọn

### Khi nào dùng @Bean?
- Third-party libraries (Jackson, Hibernate, etc.)
- Cần control quá trình khởi tạo
- Cần conditional bean creation
- Cần inject dependencies phức tạp

---

## 🎯 PHẦN 4: DEPENDENCY INJECTION (3 CÁCH)

### 1. Constructor Injection ⭐ RECOMMENDED
```java
@Service
public class UserService {
    private final UserRepository repository;
    
    @Autowired // Optional từ Spring 4.3
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

**Ưu điểm:**
- Immutable (dùng final)
- Dễ test (inject mock)
- Bắt buộc phải có dependency
- Không thể null

**Khi nào dùng:** Luôn luôn (best practice)

### 2. Setter Injection
```java
@Service
public class UserService {
    private EmailService emailService;
    
    @Autowired(required = false)
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}
```

**Ưu điểm:**
- Optional dependencies
- Có thể thay đổi sau khi khởi tạo

**Khi nào dùng:** Dependencies không bắt buộc

### 3. Field Injection ❌ KHÔNG KHUYẾN KHÍCH
```java
@Service
public class UserService {
    @Autowired
    private UserRepository repository;
}
```

**Nhược điểm:**
- Khó test (không inject mock được)
- Có thể null
- Vi phạm encapsulation

**Khi nào dùng:** Không nên dùng (chỉ cho demo/prototype)

**Xem code:** `UserService.java`

---

## 🎯 PHẦN 5: 6 BEAN SCOPES

### 1. SINGLETON (Default) ⭐
```java
@Bean
@Scope("singleton") // Có thể bỏ qua
public CacheService cacheService() {
    return new CacheService();
}
```

**Đặc điểm:**
- Chỉ có 1 instance duy nhất trong ApplicationContext
- Được tạo khi context khởi động (eager) hoặc khi request lần đầu (lazy)
- Shared state giữa tất cả requests

**Khi nào dùng:**
- Stateless beans
- Services, Repositories
- Configuration beans

### 2. PROTOTYPE
```java
@Bean
@Scope("prototype")
public NotificationService notificationService() {
    return new NotificationService();
}
```

**Đặc điểm:**
- Tạo instance mới mỗi khi getBean()
- Spring không quản lý lifecycle hoàn toàn (không gọi @PreDestroy)
- Mỗi instance độc lập

**Khi nào dùng:**
- Stateful beans
- Beans có state khác nhau cho mỗi request
- Heavy objects không nên share

### 3. REQUEST (Web only)
```java
@Bean
@RequestScope
public RequestBean requestBean() {
    return new RequestBean();
}
```

**Đặc điểm:**
- 1 instance mới cho mỗi HTTP request
- Bị destroy khi request hoàn thành
- Chỉ trong Spring Web

**Khi nào dùng:**
- Request-specific data
- Shopping cart trong e-commerce

### 4. SESSION (Web only)
```java
@Bean
@SessionScope
public SessionBean sessionBean() {
    return new SessionBean();
}
```

**Đặc điểm:**
- 1 instance cho mỗi HTTP session
- Tồn tại trong suốt session
- Bị destroy khi session timeout

**Khi nào dùng:**
- User session data
- Authentication info
- User preferences

### 5. APPLICATION (Web only)
```java
@Bean
@ApplicationScope
public ApplicationBean applicationBean() {
    return new ApplicationBean();
}
```

**Đặc điểm:**
- 1 instance cho toàn bộ ServletContext
- Giống singleton nhưng trong context của ServletContext
- Shared across all sessions

**Khi nào dùng:**
- Application-wide counters
- Global configuration

### 6. WEBSOCKET (Web only)
```java
@Bean
@Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
public WebSocketBean webSocketBean() {
    return new WebSocketBean();
}
```

**Đặc điểm:**
- 1 instance cho mỗi WebSocket session
- Cần Spring WebSocket support

**Khi nào dùng:**
- WebSocket-specific state
- Real-time chat applications

**Xem code:** `BeanScopesExample.java`

---

## 🎯 PHẦN 6: CIRCULAR DEPENDENCY

### Vấn đề
```java
// ServiceA phụ thuộc ServiceB
// ServiceB phụ thuộc ServiceA
// → Spring không thể khởi tạo!
```

### Giải pháp 1: Sử dụng @Lazy ⭐ RECOMMENDED
```java
@Component
class ServiceA {
    private final ServiceB serviceB;
    
    @Autowired
    public ServiceA(@Lazy ServiceB serviceB) {
        this.serviceB = serviceB; // Inject proxy, chỉ tạo khi dùng
    }
}

@Component
class ServiceB {
    private final ServiceA serviceA;
    
    @Autowired
    public ServiceB(ServiceA serviceA) {
        this.serviceA = serviceA;
    }
}
```

**Cách hoạt động:**
- @Lazy tạo proxy cho ServiceB
- ServiceA nhận proxy thay vì instance thực
- ServiceB được tạo thực sự khi được sử dụng lần đầu

### Giải pháp 2: Setter Injection
```java
@Component
class ServiceC {
    private ServiceD serviceD;
    
    @Autowired
    public void setServiceD(ServiceD serviceD) {
        this.serviceD = serviceD;
    }
}

@Component
class ServiceD {
    private ServiceC serviceC;
    
    @Autowired
    public void setServiceC(ServiceC serviceC) {
        this.serviceC = serviceC;
    }
}
```

### Giải pháp 3: Refactor Code ⭐ BEST PRACTICE
```java
// Tạo ServiceE chứa logic chung
// ServiceA và ServiceB đều phụ thuộc vào ServiceE
// → Loại bỏ circular dependency
```

**Xem code:** `CircularDependencyExample.java`

---

## 🎯 PHẦN 7: LAZY VS EAGER INITIALIZATION

### Eager Initialization (Default)
```java
@Bean
public CacheService cacheService() {
    return new CacheService();
}
```

**Đặc điểm:**
- Bean được tạo khi ApplicationContext khởi động
- Phát hiện lỗi sớm (fail-fast)
- Tốn memory ngay từ đầu

**Khi nào dùng:**
- Beans thường xuyên sử dụng
- Muốn fail-fast
- Beans nhẹ

### Lazy Initialization
```java
@Bean
@Lazy
public ReportService reportService() {
    return new ReportService();
}
```

**Đặc điểm:**
- Bean chỉ được tạo khi được request lần đầu
- Tiết kiệm memory ban đầu
- Startup nhanh hơn

**Khi nào dùng:**
- Beans ít khi sử dụng
- Heavy beans
- Conditional beans

**Xem code:** `AppConfig.java`, `CoreDemo.java`

---

## 🎯 PHẦN 8: @CONFIGURATION VÀ @IMPORT

### @Configuration
```java
@Configuration
public class AppConfig {
    @Bean
    public UserService userService() {
        return new UserService();
    }
}
```

**Đặc điểm:**
- Đánh dấu class là configuration class
- Methods được gọi qua CGLIB proxy để đảm bảo singleton
- Có thể inject dependencies vào @Bean methods

### @Import - Tổ chức Configuration
```java
@Configuration
@Import({DataSourceConfig.class, ServiceConfig.class})
public class AppConfig {
    // Main configuration
}
```

**Lợi ích:**
- Tách configuration thành nhiều files
- Dễ maintain
- Reusable configurations

### @ComponentScan
```java
@Configuration
@ComponentScan(basePackages = "com.example.spring_cert_notes.core")
public class AppConfig {
    // Tự động scan và đăng ký @Component, @Service, @Repository
}
```

**Xem code:** `AppConfig.java`, `DataSourceConfig.java`, `ServiceConfig.java`

---

## ✅ CHECKLIST HOÀN THÀNH

### Kiến thức cơ bản
- [ ] Hiểu được ApplicationContext là gì và vai trò của nó
- [ ] Nắm được Bean Lifecycle (Constructor → DI → @PostConstruct → Ready → @PreDestroy)
- [ ] Phân biệt được @Component và @Bean
- [ ] Biết khi nào dùng @Component, khi nào dùng @Bean

### 5 cách định nghĩa Bean
- [ ] Cách 1: @Component (và @Service, @Repository, @Controller)
- [ ] Cách 2: @Bean trong @Configuration
- [ ] Cách 3: @Bean với custom name và scope
- [ ] Cách 4: Factory method pattern
- [ ] Cách 5: Programmatic registration

### Dependency Injection
- [ ] Constructor Injection (RECOMMENDED)
- [ ] Setter Injection (cho optional dependencies)
- [ ] Field Injection (KHÔNG khuyến khích)
- [ ] Hiểu ưu nhược điểm của từng cách

### 6 Bean Scopes
- [ ] Singleton - 1 instance cho toàn bộ context
- [ ] Prototype - instance mới mỗi lần request
- [ ] Request - 1 instance cho mỗi HTTP request
- [ ] Session - 1 instance cho mỗi HTTP session
- [ ] Application - 1 instance cho ServletContext
- [ ] WebSocket - 1 instance cho WebSocket session

### Xử lý Circular Dependency
- [ ] Hiểu vấn đề circular dependency
- [ ] Giải pháp 1: @Lazy annotation
- [ ] Giải pháp 2: Setter Injection
- [ ] Giải pháp 3: Refactor code (BEST)

### Lazy vs Eager
- [ ] Eager initialization (default) - tạo khi startup
- [ ] Lazy initialization (@Lazy) - tạo khi request
- [ ] Biết khi nào dùng lazy, khi nào dùng eager

### Configuration
- [ ] @Configuration để define configuration class
- [ ] @Import để tổ chức nhiều config files
- [ ] @ComponentScan để tự động scan beans

---

## 🚀 CÁCH CHẠY DEMO

### Demo 1: Chạy tất cả ví dụ
```bash
# Compile và chạy CoreDemo
mvn clean compile
mvn exec:java -Dexec.mainClass="com.example.spring_cert_notes.core.CoreDemo"
```

### Demo 2: Chạy 5 cách định nghĩa Bean
```bash
mvn exec:java -Dexec.mainClass="com.example.spring_cert_notes.core.BeanDefinitionDemo"
```

### Hoặc chạy trực tiếp trong IDE
- Mở `CoreDemo.java` → Run main method
- Mở `BeanDefinitionDemo.java` → Run main method

---

## 📝 GHI CHÚ QUAN TRỌNG

### Best Practices
1. **Luôn dùng Constructor Injection** cho required dependencies
2. **Dùng Setter Injection** cho optional dependencies
3. **Tránh Field Injection** (khó test)
4. **Singleton cho stateless beans**, Prototype cho stateful beans
5. **Refactor code** thay vì dùng @Lazy để fix circular dependency
6. **Eager initialization** cho beans thường dùng, Lazy cho beans ít dùng

### Common Mistakes
1. ❌ Dùng Field Injection
2. ❌ Không hiểu sự khác biệt giữa Singleton và Prototype
3. ❌ Inject Prototype bean vào Singleton bean (sẽ chỉ có 1 instance)
4. ❌ Quên @Configuration khi dùng @Bean
5. ❌ Circular dependency không được xử lý

### Tips
- Dùng `@Autowired(required = false)` cho optional dependencies
- Dùng `@Lazy` để break circular dependency
- Dùng `@Primary` khi có nhiều beans cùng type
- Dùng `@Qualifier` để chỉ định bean cụ thể
- Check logs để hiểu Bean creation order

---

## 📚 TÀI LIỆU THAM KHẢO

### Files code trong package này:
1. `CoreDemo.java` - Main demo chạy tất cả ví dụ
2. `BeanDefinitionDemo.java` - 5 cách định nghĩa Bean
3. `AppConfig.java` - Main configuration với @Import
4. `DataSourceConfig.java` - Configuration riêng cho DataSource
5. `ServiceConfig.java` - Configuration riêng cho Services
6. `UserRepository.java` - Bean lifecycle với @PostConstruct/@PreDestroy
7. `UserService.java` - 3 cách Dependency Injection
8. `BeanScopesExample.java` - 6 Bean Scopes
9. `CircularDependencyExample.java` - Xử lý circular dependency

### Đọc thêm:
- Spring Framework Documentation: Core Technologies
- Baeldung: Spring Bean Scopes
- Spring in Action (Book)