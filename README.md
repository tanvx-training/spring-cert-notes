# KẾ HOẠCH HỌC SPRING CERTIFIED PROFESSIONAL TRONG 1 THÁNG

## I. PHÂN TÍCH KỲ THI

**Thông tin cơ bản:**
- 60 câu hỏi, thời gian: 130 phút (~2 phút/câu)
- Điểm đạt: 300/500 (60%)
- Hình thức: Thi online qua Pearson VUE
- Yêu cầu: 6-12 tháng kinh nghiệm Spring Framework

**Trọng tâm kiến thức:** 6 phần với độ quan trọng ước tính:
1. **Spring Core** (~25-30% số câu) - QUAN TRỌNG NHẤT
2. **Data Management** (~20-25%)
3. **Spring MVC/REST** (~15-20%)
4. **Testing** (~10-15%)
5. **Security** (~10%)
6. **Spring Boot** (~15-20%)

---

## II. KẾ HOẠCH CHI TIẾT THEO TUẦN

### **TUẦN 1: SPRING CORE - NỀN TẢNG (40% thời gian)**

#### **Ngày 1-2: Java Configuration & Bean Management**
**Mục tiêu:**
- Hiểu sâu về ApplicationContext và Bean lifecycle
- Thành thạo @Configuration, @Bean, @ComponentScan
- Nắm vững 6 Bean Scopes (singleton, prototype, request, session, application, websocket)

**Thực hành:**
```java
// Bài 1: Tạo multi-module project với 3 config files
@Configuration
@Import({DataSourceConfig.class, ServiceConfig.class})
public class AppConfig {
    @Bean
    @Scope("prototype")
    public UserService userService() { }
}

// Bài 2: Xử lý circular dependency
// Bài 3: Lazy vs Eager initialization
```

**Checklist:**
- [ ] Tạo được 5 cách khác nhau để define Bean
- [ ] Giải thích được sự khác biệt giữa @Bean và @Component
- [ ] Xử lý được dependency injection với Constructor, Setter, Field injection

---

#### **Ngày 3-4: Properties, Profiles & SpEL**
**Mục tiêu:**
- Quản lý properties từ nhiều nguồn (application.properties, YAML, environment variables)
- Sử dụng thành thạo @Profile cho các môi trường dev/test/prod
- Viết được SpEL expressions phức tạp

**Thực hành:**
```java
// Bài 1: Multi-profile configuration
@Configuration
@Profile("dev")
public class DevConfig {
    @Value("${db.url}")
    private String dbUrl;
    
    @Value("#{systemProperties['user.country']}")
    private String country;
}

// Bài 2: @PropertySource với nhiều files
// Bài 3: SpEL với collections, conditions
```

**Checklist:**
- [ ] Setup được 3 profiles khác nhau
- [ ] Override properties theo thứ tự ưu tiên
- [ ] Viết được 10 SpEL expressions phổ biến

---

#### **Ngày 5-6: Annotation-Based Config & Stereotypes**
**Mục tiêu:**
- Hiểu rõ @Component, @Service, @Repository, @Controller
- Thành thạo Component Scanning với filters
- Nắm vững @PostConstruct, @PreDestroy lifecycle callbacks

**Thực hành:**
```java
// Bài 1: Custom component scanning với includeFilters/excludeFilters
@ComponentScan(
    basePackages = "com.example",
    includeFilters = @Filter(type = FilterType.ANNOTATION, 
                             classes = MyCustomAnnotation.class)
)

// Bài 2: Lifecycle hooks
@Component
public class MyBean {
    @PostConstruct
    public void init() { }
    
    @PreDestroy
    public void cleanup() { }
}
```

**Checklist:**
- [ ] Giải thích được khi nào dùng từng stereotype annotation
- [ ] Tạo được custom stereotype annotation
- [ ] Debug được component scanning issues

---

#### **Ngày 7: Spring Bean Lifecycle & Proxies**
**Mục tiêu:**
- Nắm vững 11 bước của Bean lifecycle
- Hiểu BeanFactoryPostProcessor vs BeanPostProcessor
- Phân biệt JDK Dynamic Proxy vs CGLIB Proxy

**Thực hành:**
```java
// Bài 1: Custom BeanPostProcessor
public class CustomBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        // Logic trước khi init
    }
}

// Bài 2: BeanFactoryPostProcessor để modify bean definitions
// Bài 3: Kiểm tra proxy type bằng AopUtils
```

**Checklist:**
- [ ] Vẽ được sơ đồ Bean lifecycle hoàn chỉnh
- [ ] Biết khi nào Spring dùng JDK Proxy vs CGLIB
- [ ] Tránh được circular dependency với @Lazy

---

### **TUẦN 2: AOP & DATA MANAGEMENT (30% thời gian)**

#### **Ngày 8-9: Aspect Oriented Programming**
**Mục tiêu:**
- Nắm vững concepts: Aspect, Join Point, Pointcut, Advice, Weaving
- Thành thạo 5 loại Advice: @Before, @After, @AfterReturning, @AfterThrowing, @Around
- Viết được Pointcut expressions phức tạp

**Thực hành:**
```java
// Bài 1: Logging aspect
@Aspect
@Component
public class LoggingAspect {
    @Around("execution(* com.example.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        return proceed;
    }
    
    // Bài 2: Security aspect với @annotation pointcut
    // Bài 3: Exception handling aspect
}
```

**Pointcut Expressions cần thuộc:**
```java
execution(* com.example..*.*(..))              // Tất cả methods trong package
@annotation(org.springframework.transaction.annotation.Transactional)
within(com.example.service..*)                 // Trong package
@within(org.springframework.stereotype.Service)
this(com.example.service.MyService)            // Proxy implements interface
target(com.example.service.MyServiceImpl)      // Target object type
args(java.lang.String,..)                      // Method arguments
```

**Checklist:**
- [ ] Viết được 5 aspects thực tế (logging, caching, security, retry, audit)
- [ ] Hiểu order của multiple aspects với @Order
- [ ] Debug được AOP proxy issues

---

#### **Ngày 10-11: Spring JDBC & Transaction Management**
**Mục tiêu:**
- Thành thạo JdbcTemplate với RowMapper, ResultSetExtractor
- Nắm vững @Transactional và 7 propagation levels
- Configure rollback rules cho checked/unchecked exceptions

**Thực hành:**
```java
// Bài 1: JdbcTemplate operations
@Repository
public class UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<User> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM users",
            (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("name"))
        );
    }
}

// Bài 2: Transaction propagation scenarios
@Service
public class TransferService {
    @Transactional(propagation = Propagation.REQUIRED)
    public void transferMoney() {
        debit();    // REQUIRED
        credit();   // REQUIRES_NEW - gây rollback như thế nào?
    }
}
```

**7 Propagation Levels cần nắm:**
1. **REQUIRED** (default) - Join existing or create new
2. **REQUIRES_NEW** - Always create new, suspend current
3. **SUPPORTS** - Join if exists, non-transactional otherwise
4. **NOT_SUPPORTED** - Execute non-transactionally
5. **MANDATORY** - Must have existing transaction
6. **NEVER** - Must NOT have transaction
7. **NESTED** - Nested transaction with savepoint

**Checklist:**
- [ ] Giải thích được 7 propagation levels với examples
- [ ] Configure được rollback cho checked exceptions
- [ ] Test transactions với @Transactional in tests

---

#### **Ngày 12-13: Spring Data JPA**
**Mục tiêu:**
- Tạo được Spring Data repositories với custom queries
- Sử dụng query methods, @Query, Specifications
- Hiểu N+1 problem và cách optimize với @EntityGraph

**Thực hành:**
```java
// Bài 1: Repository với query methods
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByLastNameAndFirstName(String lastName, String firstName);
    
    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain")
    List<User> findByEmailDomain(@Param("domain") String domain);
    
    @Query(value = "SELECT * FROM users WHERE created_at > ?1", nativeQuery = true)
    List<User> findRecentUsers(LocalDateTime date);
}

// Bài 2: Pagination & Sorting
Page<User> users = userRepository.findAll(
    PageRequest.of(0, 10, Sort.by("lastName").ascending())
);

// Bài 3: Specifications cho dynamic queries
```

**Checklist:**
- [ ] Tạo được 10 query methods phức tạp
- [ ] Sử dụng được Pageable, Sort
- [ ] Optimize được lazy loading với @EntityGraph

---

#### **Ngày 14: Ôn tập & Mini Test 1**
- Làm 30 câu hỏi trắc nghiệm về Spring Core + Data Management
- Review lại các concepts chưa vững
- Refactor code examples để tối ưu hơn

---

### **TUẦN 3: SPRING MVC, TESTING & SECURITY (25% thời gian)**

#### **Ngày 15-16: Spring MVC & REST APIs**
**Mục tiêu:**
- Hiểu request processing lifecycle (DispatcherServlet → Handler Mapping → Controller → View Resolver)
- Thành thạo @RestController, @RequestMapping, @PathVariable, @RequestParam
- Implement CRUD operations với proper HTTP methods

**Thực hành:**
```java
// Bài 1: Complete REST controller
@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) { }
    
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) { }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) { }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) { }
}

// Bài 2: Exception handling với @ControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }
}

// Bài 3: Content negotiation (JSON, XML)
// Bài 4: RestTemplate client
```

**Checklist:**
- [ ] Implement được complete CRUD API
- [ ] Xử lý validation errors với @Valid
- [ ] Hiểu Content-Type và Accept headers

---

#### **Ngày 17-18: Testing với JUnit 5 & Spring Test**
**Mục tiêu:**
- Viết unit tests với JUnit 5 (Assertions, Assumptions, @ParameterizedTest)
- Integration tests với @SpringBootTest, @WebMvcTest, @DataJpaTest
- Mock dependencies với @MockBean

**Thực hành:**
```java
// Bài 1: Unit test
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    void shouldCreateUser() {
        User user = new User("John");
        when(userRepository.save(any())).thenReturn(user);
        
        User result = userService.createUser(user);
        
        assertThat(result.getName()).isEqualTo("John");
        verify(userRepository).save(any());
    }
}

// Bài 2: Integration test với database
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void shouldFindUserByEmail() {
        userRepository.save(new User("test@example.com"));
        Optional<User> user = userRepository.findByEmail("test@example.com");
        assertThat(user).isPresent();
    }
}

// Bài 3: MockMVC test
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void shouldReturnUser() throws Exception {
        when(userService.getUser(1L)).thenReturn(new User("John"));
        
        mockMvc.perform(get("/api/users/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("John"));
    }
}
```

**Test Slices cần biết:**
- `@WebMvcTest` - Only MVC layer
- `@DataJpaTest` - Only JPA repositories
- `@RestClientTest` - REST clients
- `@JsonTest` - JSON serialization

**Checklist:**
- [ ] Viết được unit tests với 80%+ coverage
- [ ] Sử dụng thành thạo test slices
- [ ] Mock external dependencies properly

---

#### **Ngày 19-20: Spring Security**
**Mục tiêu:**
- Configure authentication (in-memory, JDBC, custom UserDetailsService)
- Implement authorization với roles và authorities
- Method-level security với @PreAuthorize, @Secured

**Thực hành:**
```java
// Bài 1: Security configuration
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable());
        return http.build();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }
}

// Bài 2: Method security
@EnableMethodSecurity
@Configuration
public class MethodSecurityConfig { }

@Service
public class AdminService {
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long id) { }
    
    @PostAuthorize("returnObject.owner == authentication.name")
    public Document getDocument(Long id) { }
}
```

**Checklist:**
- [ ] Configure được 3 loại authentication
- [ ] Implement role-based và permission-based authorization
- [ ] Secure REST APIs với JWT (bonus)

---

#### **Ngày 21: Mini Test 2**
- 30 câu hỏi về MVC, Testing, Security
- Review mistakes và làm lại

---

### **TUẦN 4: SPRING BOOT & TỔNG ÔN (25% thời gian)**

#### **Ngày 22-23: Spring Boot Essentials**
**Mục tiêu:**
- Hiểu Spring Boot starters và dependency management
- Nắm vững auto-configuration mechanism
- Override default configurations

**Thực hành:**
```java
// Bài 1: Custom auto-configuration
@Configuration
@ConditionalOnClass(DataSource.class)
@EnableConfigurationProperties(MyProperties.class)
public class MyAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public MyService myService(MyProperties properties) {
        return new MyService(properties);
    }
}

// Bài 2: Properties hierarchy
// application.properties
// application-{profile}.properties
// Command line args
// Environment variables

// Bài 3: @ConfigurationProperties
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private List<String> servers;
    // getters/setters
}
```

**Checklist:**
- [ ] Giải thích được 5 Spring Boot starters phổ biến
- [ ] Debug auto-configuration với `--debug`
- [ ] Tạo được custom starter

---

#### **Ngày 24-25: Spring Boot Actuator**
**Mục tiêu:**
- Configure và secure Actuator endpoints
- Create custom health indicators
- Define custom metrics với Micrometer

**Thực hành:**
```java
// Bài 1: Custom health indicator
@Component
public class CustomHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        boolean isHealthy = checkExternalService();
        if (isHealthy) {
            return Health.up()
                .withDetail("service", "available")
                .build();
        }
        return Health.down()
            .withDetail("error", "Service unavailable")
            .build();
    }
}

// Bài 2: Custom metrics
@Component
public class BusinessMetrics {
    private final MeterRegistry meterRegistry;
    
    public void recordUserLogin(String username) {
        meterRegistry.counter("user.logins", "username", username).increment();
    }
}

// Bài 3: Secure actuator
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: when-authorized
```

**Actuator Endpoints cần biết:**
- `/actuator/health` - Health status
- `/actuator/info` - Application info
- `/actuator/metrics` - Metrics data
- `/actuator/env` - Environment properties
- `/actuator/loggers` - Logging configuration
- `/actuator/beans` - All beans
- `/actuator/mappings` - Request mappings

**Checklist:**
- [ ] Configure được 5+ actuator endpoints
- [ ] Tạo custom health check cho database, external API
- [ ] Implement custom metrics cho business logic

---

#### **Ngày 26-27: TỔNG ÔN TẬP TOÀN BỘ**
**Chiến lược:**

1. **Sáng: Review theory** (3h)
    - Đọc lại notes từng section
    - Vẽ mind maps cho mỗi topic
    - Tạo flashcards cho khái niệm quan trọng

2. **Chiều: Làm bài tập thực hành** (3h)
    - Tạo 1 mini project tích hợp TẤT CẢ kiến thức
    - Project gợi ý: REST API cho quản lý thư viện với:
        - Spring Core: Configuration, Profiles, AOP logging
        - Data: JPA repositories, Transactions
        - MVC: CRUD endpoints
        - Testing: Unit + Integration tests
        - Security: Role-based access
        - Actuator: Custom health checks

3. **Tối: Practice tests** (2h)
    - Làm 60 câu hỏi mô phỏng kỳ thi thật
    - Time: 130 phút
    - Review sai lầm kỹ càng

---

#### **Ngày 28: MOCK EXAM & REVIEW**
**Full Mock Exam (130 phút):**
- 60 câu hỏi giống format thật
- Tính điểm theo thang 300-500
- Target: 350+ điểm (70%+)

**Phân tích kết quả:**
- Identify weak areas
- Review lại concepts sai >= 2 lần
- Làm lại các câu khó

---

#### **Ngày 29-30: SPRINT CUỐI**
**Ngày 29:**
- Sáng: Làm lại TẤT CẢ code examples từ tuần 1-4
- Chiều: Review tài liệu tham khảo chính thức (Spring docs)
- Tối: Mini test 20 câu về các topics yếu nhất

**Ngày 30:**
- Sáng: Đọc lướt toàn bộ notes một lần nữa
- Chiều: Nghỉ ngơi, thư giãn
- Tối: Đi ngủ sớm để sáng nay tỉnh táo

---

## III. CHIẾN LƯỢC HỌC TỐI ƯU

### **1. Phương pháp 40-30-30**
- **40% thời gian: Lý thuyết** (đọc docs, xem videos)
- **30% thời gian: Thực hành code** (hands-on labs)
- **30% thời gian: Làm bài test** (practice questions)

### **2. Kỹ thuật Pomodoro cho mỗi session**
- 50 phút học tập trung
- 10 phút nghỉ (đứng dậy, uống nước)
- Sau 4 Pomodoros: nghỉ 30 phút

### **3. Active Recall & Spaced Repetition**
- Sau mỗi section, tự hỏi: "Giải thích concept này như thế nào?"
- Review lại nội dung ngày 1-2 vào ngày 7
- Review lại tuần 1 vào ngày 14

### **4. Learning Resources (Ưu tiên)**

**Tài liệu chính thức (QUAN TRỌNG NHẤT):**
- Spring Framework 5.3 Documentation
- Spring Boot 2.5 Reference Guide
- Spring Data JPA Documentation
- Spring Security Reference

**Courses:**
- Udemy: Spring Framework Master Class (Tim Buchalka)
- Baeldung: Spring Certification Course
- LinkedIn Learning: Spring Framework paths

**Practice:**
- Enthuware Spring Mock Exams (HIGHLY RECOMMENDED)
- Whizlabs Spring Practice Tests
- GitHub: Spring certification sample questions

### **5. Code Repository Structure**
```
spring-cert-practice/
├── week1-core/
│   ├── bean-lifecycle/
│   ├── aop-examples/
│   └── configuration/
├── week2-data/
│   ├── jdbc-template/
│   ├── transactions/
│   └── jpa-repositories/
├── week3-mvc-testing/
│   ├── rest-api/
│   ├── testing/
│   └── security/
├── week4-boot/
│   ├── autoconfiguration/
│   └── actuator/
└── final-project/
```

---

## IV. CHECKLIST TỔNG QUAN (Đánh dấu hoàn thành)

### **Spring Core ✓**
- [ ] Java Configuration với @Configuration, @Bean
- [ ] Bean Scopes (6 loại)
- [ ] Properties & Profiles
- [ ] Component Scanning & Stereotypes
- [ ] Bean Lifecycle (11 bước)
- [ ] AOP: 5 advice types, pointcut expressions
- [ ] Proxies: JDK vs CGLIB

### **Data Management ✓**
- [ ] JdbcTemplate với RowMapper
- [ ] Transaction Management (7 propagation levels)
- [ ] Rollback rules
- [ ] Spring Data JPA repositories
- [ ] Query methods vs @Query
- [ ] Pagination & Sorting

### **Spring MVC ✓**
- [ ] Request lifecycle
- [ ] REST controllers (@RestController, @RequestMapping)
- [ ] CRUD operations với proper HTTP methods
- [ ] Exception handling (@ControllerAdvice)
- [ ] RestTemplate usage

### **Testing ✓**
- [ ] JUnit 5: Assertions, @ParameterizedTest
- [ ] Integration tests với @SpringBootTest
- [ ] Test slices: @WebMvcTest, @DataJpaTest
- [ ] MockMVC testing
- [ ] Mocking với @MockBean

### **Security ✓**
- [ ] Authentication configuration
- [ ] Authorization (roles vs authorities)
- [ ] Method-level security (@PreAuthorize)
- [ ] HTTP security configuration

### **Spring Boot ✓**
- [ ] Auto-configuration mechanism
- [ ] Starters & dependency management
- [ ] Properties loading hierarchy
- [ ] @ConfigurationProperties
- [ ] Actuator endpoints
- [ ] Custom health indicators
- [ ] Custom metrics

---

## V. TIPS THI CUỐI CÙNG

### **Trong khi thi:**
1. **Đọc kỹ câu hỏi 2 lần** - nhiều câu có trick ở từ khóa nhỏ
2. **Loại trừ đáp án sai** - thường có 2 đáp án rõ ràng sai
3. **Đánh dấu câu khó** - skip qua, làm câu dễ trước
4. **Quản lý thời gian:**
    - 60 phút đầu: làm 40 câu dễ + trung bình
    - 40 phút tiếp: làm 20 câu khó
    - 30 phút cuối: review tất cả

5. **Các từ khóa cần chú ý:**
    - "ALWAYS", "NEVER" → thường sai
    - "CAN", "MAY", "SOMETIMES" → thường đúng
    - "BEST practice" vs "VALID" → khác nhau

### **Các lỗi thường gặp cần tránh:**
- Nhầm lẫn @Component vs @Configuration
- Quên propagation mặc định là REQUIRED
- Không biết khi nào Spring dùng CGLIB
- Nhầm @PreAuthorize vs @PostAuthorize
- Không hiểu @Transactional rollback rules

---

## VI. FINAL THOUGHTS

**Mức độ ưu tiên:**
1. **CRITICAL (70% effort):** Spring Core, Data Management, Spring Boot
2. **IMPORTANT (20%):** MVC/REST, Testing
3. **GOOD TO HAVE (10%):** Security, Actuator advanced features

**Công thức thành công:**
```
Consistency (1h/day lý thuyết + 2h/day code) 
+ Practice Tests (100+ câu) 
+ Hands-on Projects (3-5 projects nhỏ)
= PASS với 350+ điểm
```

**Remember:** Kỳ thi kiểm tra **depth of understanding**, không chỉ surface knowledge. Focus vào "WHY" và "WHEN to use", không chỉ "HOW"!

Good luck với kỳ thi! 🚀