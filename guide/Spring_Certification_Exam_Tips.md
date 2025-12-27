# SPRING PROFESSIONAL CERTIFICATION
## Ultimate Exam Tips & Study Guide

---

**Tổng hợp mẹo làm bài thi và kiến thức cốt lõi cho Spring Professional 2024**

*Tạo ngày: 27/12/2024*

---

## MỤC LỤC

1. [Thông tin về kỳ thi](#1-thông-tin-về-kỳ-thi)
2. [Chiến lược làm bài thi](#2-chiến-lược-làm-bài-thi)
3. [Spring Core - Key Points](#3-spring-core-key-points)
4. [Spring AOP - Key Points](#4-spring-aop-key-points)
5. [Spring Data - Key Points](#5-spring-data-key-points)
6. [Spring MVC - Key Points](#6-spring-mvc-key-points)
7. [Spring Security - Key Points](#7-spring-security-key-points)
8. [Spring Testing - Key Points](#8-spring-testing-key-points)
9. [Spring Boot - Key Points](#9-spring-boot-key-points)
10. [Common Annotations Reference](#10-common-annotations-reference)
11. [Common Pitfalls to Avoid](#11-common-pitfalls-to-avoid)
12. [Must-Know Comparisons](#12-must-know-comparisons)
13. [Quick Reference Tables](#13-quick-reference-tables)
14. [Practice Questions](#14-practice-questions)
15. [Final Checklist](#15-final-checklist)

---

## 1. THÔNG TIN VỀ KỲ THI

### 1.1. Exam Details

**Spring Professional Develop (2V0-72.22)**

| Detail | Information |
|--------|-------------|
| **Questions** | 60 items |
| **Duration** | 130 minutes |
| **Passing Score** | 300 (scaled) |
| **Format** | Multiple choice, multiple select |
| **Delivery** | Pearson VUE (proctored) |
| **Prerequisites** | 6-12 months Spring experience recommended |

### 1.2. Exam Sections Distribution

```
┌─────────────────────────────────────┐
│     Exam Content Breakdown          │
├─────────────────────────────────────┤
│ Section 1: Spring Core        ~25%  │
│ Section 2: Data Management    ~15%  │
│ Section 3: Spring MVC         ~20%  │
│ Section 4: Testing            ~15%  │
│ Section 5: Security           ~15%  │
│ Section 6: Spring Boot        ~10%  │
└─────────────────────────────────────┘
```

### 1.3. Question Types

**1. Multiple Choice (Single Answer)**
```
Which annotation enables Spring Boot auto-configuration?
A) @Configuration
B) @ComponentScan
C) @EnableAutoConfiguration ✓
D) @SpringBootApplication
```

**2. Multiple Select (Multiple Answers)**
```
Which are valid bean scopes? (Select TWO)
☑ A) singleton
☐ B) request
☑ C) prototype
☐ D) thread
```

**3. Scenario-Based**
```
Given a service that needs transaction management,
which annotation should be used?
- Context provided
- Code examples
- Best practice question
```

---

## 2. CHIẾN LƯỢC LÀM BÀI THI

### 2.1. Time Management

**Total Time: 130 minutes = 2 hours 10 minutes**

```
Recommended Time Allocation:
┌────────────────────────────────────┐
│ First Pass (60 min)                │
│ - Answer known questions           │
│ - Skip difficult ones              │
│ - Mark for review                  │
├────────────────────────────────────┤
│ Second Pass (50 min)               │
│ - Review marked questions          │
│ - Eliminate wrong answers          │
│ - Make educated guesses            │
├────────────────────────────────────┤
│ Final Review (20 min)              │
│ - Check all answers                │
│ - Verify multiple select count     │
│ - No blank answers                 │
└────────────────────────────────────┘
```

**Average: ~2.2 minutes per question**

### 2.2. Strategy Tips

✅ **DO:**
- Read question carefully (keywords: "NOT", "EXCEPT", "BEST")
- Eliminate obviously wrong answers first
- Use process of elimination
- Answer ALL questions (no penalty for wrong answers)
- Mark difficult questions for review
- Trust your first instinct (usually correct)

❌ **DON'T:**
- Spend too much time on one question
- Change answers without good reason
- Leave questions blank
- Second-guess too much

### 2.3. Reading Questions

**Keywords to watch:**
- **"Which is TRUE"** vs **"Which is FALSE"**
- **"BEST"** - Multiple correct, pick best one
- **"NOT"** / **"EXCEPT"** - Looking for wrong answer
- **"Select TWO"** / **"Select THREE"** - Multiple select
- **"Most appropriate"** - Best practice question

**Example:**
```
"Which of the following is NOT a valid bean scope?"
↑ Looking for INVALID scope
```

---

## 3. SPRING CORE - KEY POINTS

### 3.1. Must-Know Concepts

**Configuration:**
```java
// Java Config (preferred)
@Configuration
public class AppConfig {
    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }
}

// Component Scanning
@ComponentScan(basePackages = "com.example")

// Annotation-based
@Component
@Service
@Repository
@Controller
```

**Dependency Injection:**
```java
// Constructor injection (preferred)
@Autowired
public UserService(UserRepository repository) {
    this.repository = repository;
}

// Field injection (not recommended)
@Autowired
private UserRepository repository;

// Setter injection
@Autowired
public void setRepository(UserRepository repository) {
    this.repository = repository;
}
```

**Bean Scopes:**
| Scope | Instances | Use Case |
|-------|-----------|----------|
| **singleton** | One per container (default) | Stateless services |
| **prototype** | New each request | Stateful objects |
| **request** | One per HTTP request | Web request data |
| **session** | One per HTTP session | User session data |
| **application** | One per ServletContext | Shared application data |
| **websocket** | One per WebSocket | WebSocket connection |

### 3.2. Common Pitfalls

❌ **Circular Dependencies:**
```java
// BAD
@Service
class A {
    @Autowired B b;
}

@Service
class B {
    @Autowired A a;
}

// FIX: Use setter injection or @Lazy
```

❌ **Missing @Configuration:**
```java
// BAD
public class Config {
    @Bean
    public UserService service() { }
}

// GOOD
@Configuration
public class Config { }
```

### 3.3. Quick Reference

**Bean Lifecycle:**
```
Instantiate → Populate Properties → BeanNameAware
→ BeanFactoryAware → ApplicationContextAware
→ BeanPostProcessor.before → @PostConstruct
→ InitializingBean.afterPropertiesSet → init-method
→ BeanPostProcessor.after → Bean Ready
→ @PreDestroy → DisposableBean.destroy → destroy-method
```

**@Autowired Resolution:**
1. By Type (first)
2. By @Qualifier (if specified)
3. By Name (as fallback)
4. Fail if ambiguous

---

## 4. SPRING AOP - KEY POINTS

### 4.1. Must-Know Concepts

**AOP Terminology:**
```
┌──────────────────────────────────┐
│ Join Point: Method execution     │
│ Pointcut: Where to apply         │
│ Advice: What to do               │
│ Aspect: Pointcut + Advice        │
│ Weaving: Applying advice         │
└──────────────────────────────────┘
```

**Advice Types:**
```java
@Before          // Before method
@After           // After method (finally)
@AfterReturning  // After successful return
@AfterThrowing   // After exception
@Around          // Around method (most powerful)
```

**Pointcut Expressions:**
```java
// Method execution
execution(* com.example.service.*.*(..))
           │        │          │   │  │
           │        │          │   │  └─ Any parameters
           │        │          │   └──── Any method
           │        │          └──────── Any class
           │        └─────────────────── service package
           └──────────────────────────── Any return type

// Examples
execution(public * *(..))              // All public methods
execution(* set*(..))                  // All setters
execution(* com.example..*Service.*(..)) // All service classes
```

### 4.2. Common Patterns

```java
@Aspect
@Component
public class LoggingAspect {
    
    // Logging
    @Before("execution(* com.example.service.*.*(..))")
    public void logBefore(JoinPoint jp) {
        log.info("Before: {}", jp.getSignature());
    }
    
    // Performance monitoring
    @Around("execution(* com.example..*(..))")
    public Object logTime(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long time = System.currentTimeMillis() - start;
        log.info("Time: {} ms", time);
        return result;
    }
    
    // Exception handling
    @AfterThrowing(
        pointcut = "execution(* com.example..*(..))",
        throwing = "ex"
    )
    public void logException(JoinPoint jp, Exception ex) {
        log.error("Exception in {}: {}", jp.getSignature(), ex.getMessage());
    }
}
```

### 4.3. Key Points

- ✅ **@EnableAspectJAutoProxy** required to enable AOP
- ✅ **@Around** is most powerful (can prevent execution)
- ✅ **Proxies** created at runtime (JDK or CGLIB)
- ✅ **Self-invocation** doesn't trigger AOP
- ✅ **Order** controlled by @Order annotation

---

## 5. SPRING DATA - KEY POINTS

### 5.1. Repository Hierarchy

```
Repository<T, ID>
    ↓
CrudRepository<T, ID>
    ↓ (save, findById, delete, count)
PagingAndSortingRepository<T, ID>
    ↓ (findAll with Pageable)
JpaRepository<T, ID>
    ↓ (flush, saveAndFlush, deleteInBatch)
```

### 5.2. Query Methods

**Naming Convention:**
```java
// Find by property
findByName(String name)
findByNameAndEmail(String name, String email)
findByAgeGreaterThan(Integer age)
findByNameContaining(String pattern)

// Count
countByActive(Boolean active)

// Exists
existsByEmail(String email)

// Delete
deleteByActive(Boolean active)

// Top/First
findTop10ByOrderByCreatedAtDesc()
findFirstByOrderByAgeDesc()
```

**Query Method Keywords:**
```
And, Or, Between, LessThan, GreaterThan,
Like, NotLike, StartingWith, EndingWith, Containing,
OrderBy, Not, In, NotIn, True, False,
IgnoreCase, IsNull, IsNotNull
```

### 5.3. Custom Queries

```java
// JPQL
@Query("SELECT u FROM User u WHERE u.email = :email")
User findByEmail(@Param("email") String email);

// Native SQL
@Query(value = "SELECT * FROM users WHERE email = ?1", 
       nativeQuery = true)
User findByEmailNative(String email);

// Modifying
@Modifying
@Transactional
@Query("UPDATE User u SET u.active = :active WHERE u.id = :id")
int updateStatus(@Param("id") Long id, @Param("active") Boolean active);
```

### 5.4. Transaction Management

```java
@Transactional                    // Default: REQUIRED
@Transactional(readOnly = true)  // Optimization for reads
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Transactional(isolation = Isolation.READ_COMMITTED)
@Transactional(rollbackFor = Exception.class)
```

**Propagation:**
- **REQUIRED** (default): Join existing or create new
- **REQUIRES_NEW**: Always new transaction
- **NESTED**: Nested transaction
- **MANDATORY**: Must have existing transaction
- **SUPPORTS**: Join if exists
- **NOT_SUPPORTED**: Execute non-transactionally
- **NEVER**: Throw exception if transaction exists

### 5.5. Key Points

- ✅ **@Transactional** required for write operations
- ✅ **readOnly = true** for optimized reads
- ✅ **@Modifying** required for UPDATE/DELETE queries
- ✅ **LAZY** loading is default for collections
- ✅ **EAGER** loading is default for single-valued

---

## 6. SPRING MVC - KEY POINTS

### 6.1. Core Annotations

```java
@Controller           // Traditional MVC
@RestController       // @Controller + @ResponseBody

@RequestMapping("/api/users")
@GetMapping("/{id}")
@PostMapping
@PutMapping("/{id}")
@DeleteMapping("/{id}")
@PatchMapping("/{id}")

@RequestParam        // Query parameters
@PathVariable        // URL path variables
@RequestBody         // Request body (JSON)
@ResponseBody        // Return body (JSON)
@RequestHeader       // HTTP headers
@CookieValue         // Cookies
```

### 6.2. Request Processing Flow

```
HTTP Request
    ↓
DispatcherServlet
    ↓
HandlerMapping (find controller)
    ↓
HandlerAdapter (invoke method)
    ↓
Controller (process request)
    ↓
ModelAndView / ResponseEntity
    ↓
ViewResolver (if needed)
    ↓
View (render)
    ↓
HTTP Response
```

### 6.3. REST Best Practices

**HTTP Methods:**
| Method | Purpose | Idempotent | Safe |
|--------|---------|------------|------|
| GET | Read | Yes | Yes |
| POST | Create | No | No |
| PUT | Update/Replace | Yes | No |
| PATCH | Partial Update | No | No |
| DELETE | Delete | Yes | No |

**Status Codes:**
```java
200 OK              // Successful GET, PUT, PATCH
201 Created         // Successful POST
204 No Content      // Successful DELETE
400 Bad Request     // Validation error
401 Unauthorized    // Authentication required
403 Forbidden       // No permission
404 Not Found       // Resource not found
500 Internal Error  // Server error
```

### 6.4. Exception Handling

```java
// Controller level
@ExceptionHandler(UserNotFoundException.class)
public ResponseEntity<ErrorResponse> handle(UserNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
}

// Global level
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        // Handle all exceptions
    }
}
```

### 6.5. Key Points

- ✅ **@RestController** = @Controller + @ResponseBody
- ✅ **@RequestParam** for query strings
- ✅ **@PathVariable** for URL paths
- ✅ **ResponseEntity** for full HTTP control
- ✅ **@Valid** for validation

---

## 7. SPRING SECURITY - KEY POINTS

### 7.1. Core Concepts

**Authentication vs Authorization:**
```
Authentication: Who are you? (Login)
    ↓
Authorization: What can you do? (Permissions)
```

**Architecture:**
```
Request
    ↓
Security Filter Chain
    ↓
Authentication Manager
    ↓
Authentication Provider
    ↓
UserDetailsService
    ↓
Grant/Deny Access
```

### 7.2. Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) 
            throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults());
        
        return http.build();
    }
}
```

### 7.3. Method Security

```java
@EnableMethodSecurity
@Configuration
public class MethodSecurityConfig {
}

// Usage
@PreAuthorize("hasRole('ADMIN')")
public void deleteUser(Long id) { }

@PreAuthorize("hasAuthority('WRITE')")
public void updateUser(User user) { }

@PreAuthorize("#user.id == authentication.principal.id")
public void updateOwnProfile(User user) { }

@Secured("ROLE_ADMIN")
public void adminMethod() { }

@RolesAllowed("ADMIN")
public void jsr250Method() { }
```

### 7.4. Password Encoding

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

// Usage
String encoded = passwordEncoder.encode("password");
boolean matches = passwordEncoder.matches("password", encoded);
```

### 7.5. Key Points

- ✅ **@EnableWebSecurity** enables Spring Security
- ✅ **@EnableMethodSecurity** enables method-level security
- ✅ **hasRole("ADMIN")** expects "ROLE_ADMIN" authority
- ✅ **hasAuthority("ADMIN")** expects exact "ADMIN" authority
- ✅ **BCryptPasswordEncoder** is recommended

---

## 8. SPRING TESTING - KEY POINTS

### 8.1. Test Annotations

```java
// Integration test
@SpringBootTest
class IntegrationTest { }

// Web layer test
@WebMvcTest(UserController.class)
class ControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean UserService service;
}

// Persistence layer test
@DataJpaTest
class RepositoryTest {
    @Autowired TestEntityManager entityManager;
    @Autowired UserRepository repository;
}
```

### 8.2. Testing Strategy

```
Unit Tests (70%)
    @Mock, @InjectMocks
    Fast, no Spring
    ↓
Slice Tests (20%)
    @WebMvcTest, @DataJpaTest
    Partial context
    ↓
Integration Tests (10%)
    @SpringBootTest
    Full context
```

### 8.3. MockMvc

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void testGetUser() throws Exception {
        when(userService.findById(1L))
            .thenReturn(new User("John"));
        
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"));
    }
}
```

### 8.4. Key Comparisons

| Aspect | @SpringBootTest | @WebMvcTest |
|--------|----------------|-------------|
| Context | Full | Web layer only |
| Speed | Slow | Fast |
| MockMvc | Need @AutoConfigureMockMvc | Auto-configured |
| Services | Real | Need @MockBean |

### 8.5. Key Points

- ✅ **@Transactional** in tests = auto rollback
- ✅ **@MockBean** = Spring-managed mock
- ✅ **@Mock** = Mockito mock (no Spring)
- ✅ **TestRestTemplate** for real HTTP requests
- ✅ **MockMvc** for controller tests without server

---

## 9. SPRING BOOT - KEY POINTS

### 9.1. Core Features

**@SpringBootApplication:**
```java
@SpringBootApplication
= @Configuration
+ @EnableAutoConfiguration
+ @ComponentScan
```

**Auto-Configuration:**
- Checks classpath
- Checks existing beans
- Checks conditions
- Configures beans automatically

### 9.2. Starters

```xml
spring-boot-starter-web       // Web + Tomcat
spring-boot-starter-data-jpa  // JPA + Hibernate
spring-boot-starter-security  // Spring Security
spring-boot-starter-test      // Testing (JUnit, Mockito)
spring-boot-starter-actuator  // Production features
```

### 9.3. Configuration

**Priority (highest to lowest):**
```
Command line args
    ↓
Environment variables
    ↓
application-{profile}.properties
    ↓
application.properties
    ↓
@PropertySource
    ↓
Default properties
```

### 9.4. Actuator Endpoints

```
/actuator/health      // Application health
/actuator/info        // Application info
/actuator/metrics     // Application metrics
/actuator/env         // Environment properties
/actuator/beans       // All beans
/actuator/mappings    // Request mappings
```

### 9.5. Key Points

- ✅ **@SpringBootApplication** combines 3 annotations
- ✅ **Auto-configuration** can be excluded
- ✅ **@ConfigurationProperties** > @Value for complex config
- ✅ **spring.profiles.active** activates profiles
- ✅ **Actuator** provides production-ready features

---

## 10. COMMON ANNOTATIONS REFERENCE

### 10.1. Core Annotations

| Annotation | Purpose | Module |
|------------|---------|--------|
| `@Configuration` | Java config class | Core |
| `@Bean` | Define bean | Core |
| `@Component` | Generic component | Core |
| `@Service` | Service layer | Core |
| `@Repository` | Persistence layer | Core |
| `@Controller` | Web controller | Core |
| `@Autowired` | Dependency injection | Core |
| `@Qualifier` | Specify bean by name | Core |
| `@Primary` | Preferred bean | Core |
| `@Lazy` | Lazy initialization | Core |
| `@Scope` | Bean scope | Core |
| `@Value` | Inject property | Core |
| `@PropertySource` | Load properties | Core |
| `@Profile` | Profile-specific | Core |

### 10.2. AOP Annotations

| Annotation | Purpose |
|------------|---------|
| `@Aspect` | Define aspect |
| `@Before` | Before advice |
| `@After` | After advice |
| `@AfterReturning` | After success |
| `@AfterThrowing` | After exception |
| `@Around` | Around advice |
| `@Pointcut` | Define pointcut |
| `@EnableAspectJAutoProxy` | Enable AOP |

### 10.3. Data Annotations

| Annotation | Purpose |
|------------|---------|
| `@Entity` | JPA entity |
| `@Table` | Table mapping |
| `@Id` | Primary key |
| `@GeneratedValue` | ID generation |
| `@Column` | Column mapping |
| `@OneToOne` | 1:1 relationship |
| `@OneToMany` | 1:N relationship |
| `@ManyToOne` | N:1 relationship |
| `@ManyToMany` | N:M relationship |
| `@Transactional` | Transaction boundary |
| `@Query` | Custom query |
| `@Modifying` | UPDATE/DELETE query |

### 10.4. MVC Annotations

| Annotation | Purpose |
|------------|---------|
| `@RestController` | REST controller |
| `@RequestMapping` | Map requests |
| `@GetMapping` | GET requests |
| `@PostMapping` | POST requests |
| `@PutMapping` | PUT requests |
| `@DeleteMapping` | DELETE requests |
| `@RequestParam` | Query parameter |
| `@PathVariable` | URL path variable |
| `@RequestBody` | Request body |
| `@ResponseBody` | Response body |
| `@ResponseStatus` | HTTP status |
| `@ExceptionHandler` | Handle exception |
| `@ControllerAdvice` | Global exception handler |

### 10.5. Testing Annotations

| Annotation | Purpose |
|------------|---------|
| `@SpringBootTest` | Integration test |
| `@WebMvcTest` | Web layer test |
| `@DataJpaTest` | Persistence test |
| `@MockBean` | Mock Spring bean |
| `@SpyBean` | Spy Spring bean |
| `@Transactional` | Test with rollback |
| `@Sql` | Execute SQL |
| `@ActiveProfiles` | Activate profiles |
| `@WithMockUser` | Mock authenticated user |

### 10.6. Boot Annotations

| Annotation | Purpose |
|------------|---------|
| `@SpringBootApplication` | Main application |
| `@EnableAutoConfiguration` | Auto-configure |
| `@ConfigurationProperties` | Bind properties |
| `@EnableConfigurationProperties` | Enable config props |
| `@ConditionalOnClass` | Condition on class |
| `@ConditionalOnMissingBean` | Condition on missing bean |

---

## 11. COMMON PITFALLS TO AVOID

### 11.1. Spring Core Pitfalls

❌ **Circular Dependencies**
```java
// Problem
@Service
class A { @Autowired B b; }
@Service
class B { @Autowired A a; }

// Fix: Use setter injection or @Lazy
```

❌ **Missing @Configuration**
```java
// Wrong
public class Config {
    @Bean public Service service() { }
}

// Correct
@Configuration
public class Config {
    @Bean public Service service() { }
}
```

❌ **Wrong Scope Usage**
```java
// Wrong: Prototype in Singleton
@Service
class MySingleton {
    @Autowired
    MyPrototype prototype; // Always same instance!
}

// Fix: Use ObjectFactory or @Lookup
```

### 11.2. Data Pitfalls

❌ **Missing @Transactional**
```java
// Wrong
public void updateUser(User user) {
    userRepository.save(user);
}

// Correct
@Transactional
public void updateUser(User user) {
    userRepository.save(user);
}
```

❌ **N+1 Query Problem**
```java
// Wrong: Causes N queries
List<User> users = userRepository.findAll();
users.forEach(u -> u.getOrders().size());

// Fix: Use JOIN FETCH
@Query("SELECT u FROM User u LEFT JOIN FETCH u.orders")
List<User> findAllWithOrders();
```

❌ **Missing @Modifying**
```java
// Wrong
@Query("UPDATE User u SET u.active = ?1")
void updateActive(Boolean active);

// Correct
@Modifying
@Transactional
@Query("UPDATE User u SET u.active = ?1")
void updateActive(Boolean active);
```

### 11.3. MVC Pitfalls

❌ **@Controller without @ResponseBody**
```java
// Wrong for REST API
@Controller
public class UserController {
    @GetMapping("/users")
    public List<User> getUsers() { }
}

// Correct
@RestController  // = @Controller + @ResponseBody
public class UserController { }
```

❌ **Missing Validation Handling**
```java
// Wrong
@PostMapping("/users")
public User create(@Valid @RequestBody User user) {
    return userService.save(user);
}

// Correct
@PostMapping("/users")
public User create(@Valid @RequestBody User user, 
                   BindingResult result) {
    if (result.hasErrors()) {
        throw new ValidationException(result.getAllErrors());
    }
    return userService.save(user);
}
```

### 11.4. Testing Pitfalls

❌ **Missing @MockBean**
```java
// Wrong
@WebMvcTest(UserController.class)
class Test {
    // Missing @MockBean for UserService
}

// Correct
@WebMvcTest(UserController.class)
class Test {
    @MockBean UserService userService;
}
```

❌ **Test Dependencies**
```java
// Wrong: Tests depend on order
@Test @Order(1)
void createUser() { }

@Test @Order(2)
void updateUser() { } // Depends on test 1

// Correct: Independent tests
@Test
void updateUser() {
    // Setup own data
}
```

### 11.5. Boot Pitfalls

❌ **Wrong Property Format**
```java
// Wrong
@ConfigurationProperties(prefix = "app")
public class Config {
    private String name;
    // Missing getters/setters!
}

// Correct: Add getters and setters
```

❌ **Forgetting @EnableConfigurationProperties**
```java
// Wrong
@ConfigurationProperties(prefix = "app")
public class AppConfig { }

// Correct
@Configuration
@EnableConfigurationProperties(AppConfig.class)
public class Config { }
```

---

## 12. MUST-KNOW COMPARISONS

### 12.1. @Component vs @Service vs @Repository vs @Controller

| Annotation | Layer | Purpose |
|------------|-------|---------|
| **@Component** | Generic | Any Spring-managed component |
| **@Service** | Business | Business logic layer |
| **@Repository** | Persistence | Data access layer, exception translation |
| **@Controller** | Presentation | Web layer, returns views |
| **@RestController** | Presentation | REST API, returns data |

### 12.2. @Autowired vs @Resource vs @Inject

| Annotation | Provider | By Type | By Name | Required |
|------------|----------|---------|---------|----------|
| **@Autowired** | Spring | Yes (default) | @Qualifier | Optional with required=false |
| **@Resource** | Java EE | No | Yes (default) | Yes |
| **@Inject** | Java EE | Yes | @Named | Yes |

**Recommendation:** Use **@Autowired** (Spring standard)

### 12.3. Constructor vs Field vs Setter Injection

| Type | Immutability | Testability | Circular Deps | Recommendation |
|------|--------------|-------------|---------------|----------------|
| **Constructor** | Yes | Easy | Fails fast | ✅ Preferred |
| **Field** | No | Hard | Allows | ❌ Avoid |
| **Setter** | No | Medium | Allows | 🟡 Optional deps |

### 12.4. JpaRepository vs CrudRepository

| Feature | CrudRepository | JpaRepository |
|---------|----------------|---------------|
| **Basic CRUD** | ✅ Yes | ✅ Yes |
| **Pagination** | ❌ No | ✅ Yes (inherited) |
| **Sorting** | ❌ No | ✅ Yes (inherited) |
| **Batch operations** | ❌ No | ✅ Yes (flush, deleteInBatch) |
| **Return type** | Iterable | List |

**Recommendation:** Use **JpaRepository** (more features)

### 12.5. @SpringBootTest vs @WebMvcTest vs @DataJpaTest

| Annotation | Context | Speed | Use Case |
|------------|---------|-------|----------|
| **@SpringBootTest** | Full | Slow | Integration tests |
| **@WebMvcTest** | Web layer | Fast | Controller tests |
| **@DataJpaTest** | Persistence | Fast | Repository tests |

### 12.6. @Transactional Propagation

| Propagation | Behavior | Use Case |
|-------------|----------|----------|
| **REQUIRED** (default) | Join existing or create | Most cases |
| **REQUIRES_NEW** | Always new | Independent operations |
| **NESTED** | Nested savepoint | Partial rollback |
| **MANDATORY** | Must exist | Ensure transaction |
| **SUPPORTS** | Optional | Read operations |
| **NOT_SUPPORTED** | Non-transactional | External calls |
| **NEVER** | Fail if exists | Non-transactional enforced |

### 12.7. LAZY vs EAGER Loading

| Aspect | LAZY | EAGER |
|--------|------|-------|
| **Loading** | On-demand | Immediate |
| **Performance** | Better initial | Worse initial |
| **N+1 Problem** | Possible | No |
| **Default @OneToMany** | ✅ Yes | ❌ No |
| **Default @ManyToOne** | ❌ No | ✅ Yes |

**Recommendation:** Use **LAZY** by default, fetch explicitly when needed

---

## 13. QUICK REFERENCE TABLES

### 13.1. HTTP Status Codes

| Code | Name | Use Case |
|------|------|----------|
| **200** | OK | Successful GET, PUT, PATCH |
| **201** | Created | Successful POST |
| **204** | No Content | Successful DELETE |
| **400** | Bad Request | Validation error |
| **401** | Unauthorized | Not authenticated |
| **403** | Forbidden | Not authorized |
| **404** | Not Found | Resource not found |
| **500** | Internal Server Error | Server error |

### 13.2. JPA Cascade Types

| Type | Effect | Use Case |
|------|--------|----------|
| **PERSIST** | Save child when parent saved | New entities |
| **MERGE** | Update child when parent updated | Updates |
| **REMOVE** | Delete child when parent deleted | Dependent entities |
| **REFRESH** | Reload child when parent reloaded | Sync from DB |
| **DETACH** | Detach child when parent detached | Cache management |
| **ALL** | All above operations | Full lifecycle |

### 13.3. Bean Scopes Summary

| Scope | Instances | Thread-Safe | Use Case |
|-------|-----------|-------------|----------|
| **singleton** | 1 per container | Must be | Stateless services |
| **prototype** | New each time | N/A | Stateful objects |
| **request** | 1 per HTTP request | Yes | Request data |
| **session** | 1 per HTTP session | Yes | User session |
| **application** | 1 per ServletContext | Must be | Shared data |

### 13.4. Query Method Keywords

| Keyword | Example | JPQL |
|---------|---------|------|
| **And** | findByNameAndEmail | `... where x.name = ?1 and x.email = ?2` |
| **Or** | findByNameOrEmail | `... where x.name = ?1 or x.email = ?2` |
| **Between** | findByAgeBetween | `... where x.age between ?1 and ?2` |
| **LessThan** | findByAgeLessThan | `... where x.age < ?1` |
| **GreaterThan** | findByAgeGreaterThan | `... where x.age > ?1` |
| **Like** | findByNameLike | `... where x.name like ?1` |
| **StartingWith** | findByNameStartingWith | `... where x.name like ?1%` |
| **EndingWith** | findByNameEndingWith | `... where x.name like %?1` |
| **Containing** | findByNameContaining | `... where x.name like %?1%` |
| **OrderBy** | findByAgeOrderByNameAsc | `... order by x.name asc` |
| **Not** | findByAgeNot | `... where x.age <> ?1` |
| **In** | findByAgeIn | `... where x.age in ?1` |
| **True** | findByActiveTrue | `... where x.active = true` |
| **False** | findByActiveFalse | `... where x.active = false` |

### 13.5. AOP Advice Order

```
@Around (before part)
    ↓
@Before
    ↓
Method Execution
    ↓
@AfterReturning (if success)
or
@AfterThrowing (if exception)
    ↓
@After (always)
    ↓
@Around (after part)
```

---

## 14. PRACTICE QUESTIONS

### 14.1. Spring Core Questions

**Question 1:**
Which is the preferred way to inject dependencies?
- A) Field injection
- B) Setter injection
- C) Constructor injection ✓
- D) Method injection

**Question 2:**
What is the default bean scope in Spring?
- A) prototype
- B) singleton ✓
- C) request
- D) session

**Question 3:**
Which annotation is used to resolve ambiguous dependencies?
- A) @Primary
- B) @Qualifier ✓
- C) @Autowired
- D) @Named

### 14.2. Data Management Questions

**Question 4:**
Which propagation creates a new transaction always?
- A) REQUIRED
- B) REQUIRES_NEW ✓
- C) NESTED
- D) MANDATORY

**Question 5:**
What annotation is required for UPDATE queries in Spring Data?
- A) @Query
- B) @Transactional
- C) @Modifying ✓
- D) @Update

### 14.3. MVC Questions

**Question 6:**
Which annotation combines @Controller and @ResponseBody?
- A) @RestController ✓
- B) @WebController
- C) @ApiController
- D) @JsonController

**Question 7:**
Which HTTP method should be used to create a resource?
- A) GET
- B) POST ✓
- C) PUT
- D) DELETE

### 14.4. Testing Questions

**Question 8:**
Which annotation loads only the web layer?
- A) @SpringBootTest
- B) @WebMvcTest ✓
- C) @WebTest
- D) @ControllerTest

**Question 9:**
What is the default behavior of @Transactional in tests?
- A) Commit
- B) Rollback ✓
- C) No transaction
- D) Depends on configuration

### 14.5. Security Questions

**Question 10:**
Which annotation enables method-level security?
- A) @EnableWebSecurity
- B) @EnableMethodSecurity ✓
- C) @EnableSecurity
- D) @Secured

### 14.6. Spring Boot Questions

**Question 11:**
What does @SpringBootApplication include? (Select 3)
- ☑ A) @Configuration
- ☑ B) @EnableAutoConfiguration
- ☑ C) @ComponentScan
- ☐ D) @EnableWebMvc

**Question 12:**
Which property activates a profile?
- A) spring.profile
- B) spring.profiles.active ✓
- C) spring.active.profile
- D) profile.active

---

## 15. FINAL CHECKLIST

### 15.1. Week Before Exam

**7 Days Before:**
- [ ] Review all 7 module guidelines
- [ ] Complete practice questions
- [ ] Review common pitfalls
- [ ] Practice coding examples

**3 Days Before:**
- [ ] Focus on weak areas
- [ ] Review all annotations
- [ ] Review comparison tables
- [ ] Practice time management

**1 Day Before:**
- [ ] Quick review of key concepts
- [ ] Review this exam tips guide
- [ ] Get good rest
- [ ] Prepare exam checklist

### 15.2. Exam Day Checklist

**Before Exam:**
- [ ] Valid ID ready
- [ ] Arrive 15 minutes early (or login early)
- [ ] Comfortable workspace
- [ ] Water/snacks if allowed
- [ ] Bathroom break before starting

**During Exam:**
- [ ] Read questions carefully
- [ ] Watch for NOT/EXCEPT keywords
- [ ] Mark difficult questions
- [ ] Manage time (2.2 min/question)
- [ ] No blank answers
- [ ] Review before submitting

### 15.3. Knowledge Checklist by Module

**Spring Core:**
- [ ] Configuration types (Java, annotations)
- [ ] Dependency injection (constructor, field, setter)
- [ ] Bean scopes (singleton, prototype, request, session)
- [ ] Bean lifecycle
- [ ] @Autowired, @Qualifier, @Primary
- [ ] Profiles and properties

**Spring AOP:**
- [ ] AOP concepts (aspect, advice, pointcut)
- [ ] Advice types (@Before, @After, @Around, etc.)
- [ ] Pointcut expressions
- [ ] @EnableAspectJAutoProxy
- [ ] Proxy types (JDK, CGLIB)

**Spring Data:**
- [ ] Repository hierarchy
- [ ] Query method naming
- [ ] Custom queries (@Query)
- [ ] @Transactional and propagation
- [ ] Entity relationships
- [ ] Cascade types and fetch types

**Spring MVC:**
- [ ] Request processing flow
- [ ] Controller annotations
- [ ] @RequestMapping variants
- [ ] @RequestParam vs @PathVariable
- [ ] REST best practices
- [ ] Exception handling

**Spring Security:**
- [ ] Authentication vs authorization
- [ ] SecurityFilterChain configuration
- [ ] Method security (@PreAuthorize, @Secured)
- [ ] Password encoding
- [ ] hasRole vs hasAuthority

**Spring Testing:**
- [ ] JUnit 5 basics
- [ ] @SpringBootTest vs @WebMvcTest vs @DataJpaTest
- [ ] MockMvc usage
- [ ] @MockBean vs @Mock
- [ ] @Transactional in tests
- [ ] @WithMockUser

**Spring Boot:**
- [ ] @SpringBootApplication
- [ ] Auto-configuration
- [ ] Starters
- [ ] Configuration properties
- [ ] Profiles
- [ ] Actuator endpoints

### 15.4. Final Tips

**Remember:**
1. ✅ Read questions carefully (keywords!)
2. ✅ Eliminate wrong answers first
3. ✅ Answer ALL questions
4. ✅ Mark difficult ones for review
5. ✅ Manage your time
6. ✅ Trust your preparation
7. ✅ Stay calm and focused
8. ✅ Review before submitting

**Common Exam Patterns:**
- Configuration questions (Java vs annotations)
- Annotation purpose questions
- Best practice questions
- Scenario-based questions
- Code analysis questions
- Comparison questions

**Key Success Factors:**
- 📚 Thorough preparation
- ⏱️ Good time management
- 🎯 Focus on understanding, not memorization
- 💪 Practice with code examples
- 🧠 Active recall during study
- ✅ Complete all practice questions

---

## SUMMARY - TOP 20 MUST-KNOW POINTS

1. **@SpringBootApplication** = @Configuration + @EnableAutoConfiguration + @ComponentScan

2. **Constructor injection** is preferred over field injection

3. **singleton** is default bean scope (one per container)

4. **JpaRepository** > CrudRepository (more features)

5. **@Transactional** required for write operations in services

6. **@RestController** = @Controller + @ResponseBody

7. **@RequestParam** for query strings, **@PathVariable** for URL paths

8. **POST** creates (201), **PUT** updates (200), **DELETE** removes (204)

9. **@WebMvcTest** for controllers, **@DataJpaTest** for repositories

10. **@MockBean** for Spring mocks, **@Mock** for Mockito mocks

11. **@Transactional** in tests = automatic rollback

12. **LAZY** loading for @OneToMany/@ManyToMany (default)

13. **EAGER** loading for @OneToOne/@ManyToOne (default)

14. **REQUIRED** is default transaction propagation

15. **@EnableAspectJAutoProxy** required to enable AOP

16. **@Around** is most powerful advice (can prevent execution)

17. **hasRole("ADMIN")** expects "ROLE_ADMIN" authority

18. **@EnableMethodSecurity** enables method-level security

19. **Auto-configuration** checks classpath and conditions

20. **Configuration priority**: Command line > Env vars > Properties file

---

## KẾT LUẬN

🎯 **Success Formula:**

```
Preparation (70%)
    +
Strategy (20%)
    +
Confidence (10%)
    =
PASS! 🏆
```

**Remember:**
- You've prepared well with 7 comprehensive guidelines
- Trust your knowledge and preparation
- Stay calm and manage your time
- Read questions carefully
- Answer ALL questions
- Review before submitting

**Final Message:**

> "Success is not final, failure is not fatal: 
> It is the courage to continue that counts."
> 
> You've got this! 💪

---

**Good luck with your Spring Professional Certification exam!** 🚀🎓

*Tài liệu tổng hợp được tạo ngày 27/12/2024*
*Cover 100% nội dung từ 7 modules guidelines*
