# SPRING TESTING
## Guideline Chi Tiết cho Spring Professional Certification

---

**Tài liệu ôn tập toàn diện về Testing trong Spring Framework**

*Tạo ngày: 27/12/2024*
*Dựa trên Spring Professional Develop Exam Guide (Section 4)*

---

## MỤC LỤC

1. [Giới thiệu về Testing trong Spring](#1-giới-thiệu-về-testing-trong-spring)
2. [JUnit 5 Fundamentals](#2-junit-5-fundamentals)
3. [Spring TestContext Framework](#3-spring-testcontext-framework)
4. [Integration Testing với Spring](#4-integration-testing-với-spring)
5. [Testing với Spring Profiles](#5-testing-với-spring-profiles)
6. [Database Testing](#6-database-testing)
7. [Spring Boot Testing](#7-spring-boot-testing)
8. [MockMVC Testing](#8-mockmvc-testing)
9. [Slice Testing](#9-slice-testing)
10. [Mocking với Mockito](#10-mocking-với-mockito)
11. [Testing REST APIs](#11-testing-rest-apis)
12. [Testing Transactions](#12-testing-transactions)
13. [Testing Security](#13-testing-security)
14. [Testing Best Practices](#14-testing-best-practices)
15. [Câu hỏi mẫu cho kỳ thi](#15-câu-hỏi-mẫu-cho-kỳ-thi)
16. [Tóm tắt và mẹo thi](#16-tóm-tắt-và-mẹo-thi)

---

## 1. GIỚI THIỆU VỀ TESTING TRONG SPRING

### 1.1. Tại sao Testing quan trọng?

**Testing Benefits:**
- ✅ Catch bugs early
- ✅ Document behavior
- ✅ Enable refactoring
- ✅ Improve design
- ✅ Reduce maintenance cost
- ✅ Increase confidence

### 1.2. Testing Pyramid

```
        ┌─────────────┐
        │   E2E Tests │ (Few, Slow, Expensive)
        └─────────────┘
      ┌─────────────────┐
      │ Integration Tests│ (Some, Medium)
      └─────────────────┘
    ┌─────────────────────┐
    │    Unit Tests       │ (Many, Fast, Cheap)
    └─────────────────────┘
```

### 1.3. Testing Types trong Spring

| Type | Scope | Speed | Dependencies |
|------|-------|-------|--------------|
| **Unit Tests** | Single class | Fast | Mocked |
| **Integration Tests** | Multiple components | Medium | Real/Test |
| **Slice Tests** | Single layer | Medium | Partial context |
| **End-to-End Tests** | Full application | Slow | All real |

### 1.4. Spring Testing Support

**Spring provides:**
- TestContext Framework
- Spring Boot Test utilities
- Mock objects (MockMvc, MockBean)
- Test annotations
- Database testing support
- Transaction management for tests

---

## 2. JUNIT 5 FUNDAMENTALS

### 2.1. JUnit 5 Architecture

```
┌────────────────────────────────────┐
│         JUnit 5 Platform           │
│  (Foundation for test frameworks)  │
└────────────────────────────────────┘
           ↓
┌─────────────────┬──────────────────┐
│  JUnit Jupiter  │  JUnit Vintage   │
│ (New tests API) │  (JUnit 3/4)     │
└─────────────────┴──────────────────┘
```

### 2.2. Basic JUnit 5 Test

```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    
    private Calculator calculator;
    
    @BeforeAll
    static void setupAll() {
        // Run once before all tests
        System.out.println("@BeforeAll - setupAll");
    }
    
    @BeforeEach
    void setup() {
        // Run before each test
        calculator = new Calculator();
        System.out.println("@BeforeEach - setup");
    }
    
    @Test
    void testAddition() {
        int result = calculator.add(2, 3);
        assertEquals(5, result);
    }
    
    @Test
    @DisplayName("Test division by zero throws exception")
    void testDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> {
            calculator.divide(10, 0);
        });
    }
    
    @Test
    @Disabled("Not implemented yet")
    void testMultiplication() {
        // Will be skipped
    }
    
    @AfterEach
    void tearDown() {
        // Run after each test
        calculator = null;
        System.out.println("@AfterEach - tearDown");
    }
    
    @AfterAll
    static void tearDownAll() {
        // Run once after all tests
        System.out.println("@AfterAll - tearDownAll");
    }
}
```

### 2.3. JUnit 5 Annotations

| Annotation | Purpose | JUnit 4 Equivalent |
|------------|---------|-------------------|
| `@Test` | Mark test method | @Test |
| `@BeforeAll` | Before all tests | @BeforeClass |
| `@BeforeEach` | Before each test | @Before |
| `@AfterEach` | After each test | @After |
| `@AfterAll` | After all tests | @AfterClass |
| `@DisplayName` | Custom display name | - |
| `@Disabled` | Disable test | @Ignore |
| `@RepeatedTest` | Repeat test N times | - |
| `@ParameterizedTest` | Parameterized test | - |
| `@Tag` | Tag for filtering | @Category |
| `@Nested` | Nested test class | - |

### 2.4. Assertions

```java
class AssertionsTest {
    
    @Test
    void testAssertions() {
        // Basic assertions
        assertEquals(4, 2 + 2);
        assertNotEquals(5, 2 + 2);
        assertTrue(5 > 2);
        assertFalse(5 < 2);
        assertNull(null);
        assertNotNull("value");
        
        // Array assertions
        assertArrayEquals(new int[]{1, 2}, new int[]{1, 2});
        
        // Collection assertions
        List<String> list = Arrays.asList("a", "b", "c");
        assertIterableEquals(list, Arrays.asList("a", "b", "c"));
        
        // Exception assertions
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> { throw new IllegalArgumentException("Invalid"); }
        );
        assertEquals("Invalid", exception.getMessage());
        
        // Timeout assertions
        assertTimeout(Duration.ofSeconds(1), () -> {
            Thread.sleep(100);
        });
        
        // Group assertions
        assertAll("person",
            () -> assertEquals("John", person.getFirstName()),
            () -> assertEquals("Doe", person.getLastName())
        );
        
        // Fail
        fail("Test failed");
    }
}
```

### 2.5. Parameterized Tests

```java
class ParameterizedTestsExample {
    
    @ParameterizedTest
    @ValueSource(strings = {"hello", "world", "test"})
    void testWithValueSource(String word) {
        assertNotNull(word);
    }
    
    @ParameterizedTest
    @CsvSource({
        "1, 1, 2",
        "2, 3, 5",
        "5, 5, 10"
    })
    void testAddition(int a, int b, int expected) {
        assertEquals(expected, calculator.add(a, b));
    }
    
    @ParameterizedTest
    @MethodSource("provideStringsForIsBlank")
    void testWithMethodSource(String input, boolean expected) {
        assertEquals(expected, Strings.isBlank(input));
    }
    
    static Stream<Arguments> provideStringsForIsBlank() {
        return Stream.of(
            Arguments.of(null, true),
            Arguments.of("", true),
            Arguments.of("  ", true),
            Arguments.of("not blank", false)
        );
    }
    
    @ParameterizedTest
    @EnumSource(Month.class)
    void testWithEnumSource(Month month) {
        assertNotNull(month);
    }
}
```

### 2.6. Nested Tests

```java
@DisplayName("UserService Test")
class UserServiceTest {
    
    private UserService userService;
    
    @BeforeEach
    void setup() {
        userService = new UserService();
    }
    
    @Nested
    @DisplayName("When user is new")
    class WhenNew {
        
        @Test
        @DisplayName("Should save successfully")
        void shouldSaveSuccessfully() {
            User user = new User("John");
            User saved = userService.save(user);
            assertNotNull(saved.getId());
        }
    }
    
    @Nested
    @DisplayName("When user exists")
    class WhenExists {
        
        private User existingUser;
        
        @BeforeEach
        void setup() {
            existingUser = userService.save(new User("John"));
        }
        
        @Test
        @DisplayName("Should update successfully")
        void shouldUpdateSuccessfully() {
            existingUser.setName("Jane");
            User updated = userService.update(existingUser);
            assertEquals("Jane", updated.getName());
        }
        
        @Test
        @DisplayName("Should delete successfully")
        void shouldDeleteSuccessfully() {
            userService.delete(existingUser.getId());
            assertFalse(userService.exists(existingUser.getId()));
        }
    }
}
```

---

## 3. SPRING TESTCONTEXT FRAMEWORK

### 3.1. @SpringBootTest vs @ExtendWith(SpringExtension.class)

```java
// Spring Boot Test (includes SpringExtension)
@SpringBootTest
class ApplicationTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    void contextLoads() {
        assertNotNull(userService);
    }
}

// Pure Spring Test (manual SpringExtension)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
class PureSpringTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    void testService() {
        assertNotNull(userService);
    }
}
```

### 3.2. Test Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- Includes:
    - JUnit 5
    - Spring Test
    - AssertJ
    - Hamcrest
    - Mockito
    - JSONassert
    - JsonPath
-->
```

### 3.3. Application Context Caching

Spring caches ApplicationContext between tests để improve performance.

**Context is reused when:**
- Same configuration
- Same profiles
- Same property sources

**Context is recreated when:**
- Different @ContextConfiguration
- Different @ActiveProfiles
- @DirtiesContext is used

```java
@SpringBootTest
class FirstTest {
    // Uses cached context
}

@SpringBootTest
class SecondTest {
    // Reuses same context (if config matches)
}

@SpringBootTest
@DirtiesContext  // Force new context
class ThirdTest {
    // Creates new context
}
```

---

## 4. INTEGRATION TESTING VỚI SPRING

### 4.1. Basic Integration Test

```java
@SpringBootTest
class UserServiceIntegrationTest {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void testCreateUser() {
        User user = new User("John", "john@example.com");
        User saved = userService.createUser(user);
        
        assertNotNull(saved.getId());
        assertEquals("John", saved.getName());
        
        // Verify in database
        User found = userRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("John", found.getName());
    }
    
    @Test
    void testFindByEmail() {
        // Setup
        User user = new User("Jane", "jane@example.com");
        userRepository.save(user);
        
        // Test
        User found = userService.findByEmail("jane@example.com");
        
        // Verify
        assertNotNull(found);
        assertEquals("Jane", found.getName());
    }
}
```

### 4.2. @ContextConfiguration

```java
// Java configuration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, DatabaseConfig.class})
class ConfigTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    void testWithConfig() {
        assertNotNull(userService);
    }
}

// XML configuration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
class XmlConfigTest {
}

// Multiple configurations
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
@Import(TestConfig.class)
class MultiConfigTest {
}
```

### 4.3. Test Configuration

```java
@TestConfiguration
public class TestConfig {
    
    @Bean
    @Primary  // Override production bean
    public DataSource testDataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }
    
    @Bean
    public EmailService mockEmailService() {
        return Mockito.mock(EmailService.class);
    }
}

// Usage
@SpringBootTest
@Import(TestConfig.class)
class ServiceTest {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private EmailService emailService;  // Mock from TestConfig
}
```

### 4.4. @DirtiesContext

```java
@SpringBootTest
class ContextTest {
    
    @Test
    @DirtiesContext  // Mark context as dirty after this test
    void testThatModifiesContext() {
        // Test that modifies shared state
    }
    
    @Test
    void testWithCleanContext() {
        // Runs with fresh context
    }
}

// Class level
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class AlwaysDirtyTest {
    // Context recreated after each test
}
```

---

## 5. TESTING VỚI SPRING PROFILES

### 5.1. @ActiveProfiles

```java
@SpringBootTest
@ActiveProfiles("test")
class ProfileTest {
    
    @Autowired
    private DataSource dataSource;
    
    @Test
    void testWithTestProfile() {
        // Uses test profile configuration
        assertNotNull(dataSource);
    }
}

// Multiple profiles
@SpringBootTest
@ActiveProfiles({"test", "debug"})
class MultiProfileTest {
}
```

### 5.2. Profile-specific Configuration

```java
@Configuration
@Profile("test")
public class TestDatabaseConfig {
    
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("schema.sql")
            .addScript("test-data.sql")
            .build();
    }
}

@Configuration
@Profile("prod")
public class ProdDatabaseConfig {
    
    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://prod/mydb");
        return ds;
    }
}
```

### 5.3. @TestPropertySource

```java
@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "logging.level.com.example=DEBUG",
    "app.feature.enabled=true"
})
class PropertyTest {
    
    @Value("${app.feature.enabled}")
    private boolean featureEnabled;
    
    @Test
    void testProperty() {
        assertTrue(featureEnabled);
    }
}

// From file
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class FilePropertyTest {
}
```

### 5.4. @DynamicPropertySource

```java
@SpringBootTest
class DynamicPropertyTest {
    
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13");
    
    @BeforeAll
    static void startContainer() {
        postgres.start();
    }
    
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @Test
    void testWithDynamicProperties() {
        // Test with real PostgreSQL container
    }
}
```

---

## 6. DATABASE TESTING

### 6.1. @DataJpaTest

```java
@DataJpaTest
class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    void testFindByEmail() {
        // Given
        User user = new User("John", "john@example.com");
        entityManager.persist(user);
        entityManager.flush();
        
        // When
        User found = userRepository.findByEmail("john@example.com");
        
        // Then
        assertNotNull(found);
        assertEquals("John", found.getName());
    }
    
    @Test
    void testSaveUser() {
        User user = new User("Jane", "jane@example.com");
        User saved = userRepository.save(user);
        
        assertNotNull(saved.getId());
        
        User found = entityManager.find(User.class, saved.getId());
        assertEquals("Jane", found.getName());
    }
}
```

**@DataJpaTest features:**
- Configures in-memory database (H2 by default)
- Configures Hibernate, Spring Data, DataSource
- Performs `@Transactional` (rolls back after each test)
- Doesn't load full application context
- Uses TestEntityManager

### 6.2. TestEntityManager

```java
@DataJpaTest
class EntityManagerTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    void testEntityManager() {
        // Persist
        User user = new User("John");
        User saved = entityManager.persistAndFlush(user);
        
        // Clear persistence context
        entityManager.clear();
        
        // Find
        User found = entityManager.find(User.class, saved.getId());
        assertEquals("John", found.getName());
        
        // Refresh
        found.setName("Jane");
        entityManager.refresh(found);
        assertEquals("John", found.getName());  // Reverted
    }
}
```

### 6.3. @Sql

```java
@SpringBootTest
@Sql("/test-data.sql")  // Execute before test
class SqlTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void testWithSqlData() {
        List<User> users = userRepository.findAll();
        assertFalse(users.isEmpty());
    }
}

// Multiple scripts
@SpringBootTest
@Sql(scripts = {"/schema.sql", "/data.sql"})
class MultiSqlTest {
}

// Method level
@SpringBootTest
class MethodSqlTest {
    
    @Test
    @Sql("/users.sql")
    void testUsers() {
        // SQL executed before this test
    }
    
    @Test
    @Sql(scripts = "/cleanup.sql", 
         executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void testWithCleanup() {
        // SQL executed after this test
    }
}
```

### 6.4. Embedded Database

```java
@TestConfiguration
public class EmbeddedDatabaseConfig {
    
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .setName("testdb")
            .addScript("classpath:schema.sql")
            .addScript("classpath:test-data.sql")
            .build();
    }
}

// Usage
@SpringBootTest
@Import(EmbeddedDatabaseConfig.class)
class EmbeddedDbTest {
    
    @Autowired
    private DataSource dataSource;
    
    @Test
    void testDatabase() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            assertTrue(conn.isValid(1));
        }
    }
}
```

### 6.5. @AutoConfigureTestDatabase

```java
// Use embedded database
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
class EmbeddedDatabaseTest {
}

// Use real database
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class RealDatabaseTest {
}
```

---

## 7. SPRING BOOT TESTING

### 7.1. @SpringBootTest

```java
@SpringBootTest
class ApplicationTest {
    
    @Autowired
    private ApplicationContext context;
    
    @Test
    void contextLoads() {
        assertNotNull(context);
    }
}

// With specific classes
@SpringBootTest(classes = {UserService.class, UserRepository.class})
class SpecificClassesTest {
}

// With properties
@SpringBootTest(properties = {
    "app.name=Test App",
    "spring.datasource.url=jdbc:h2:mem:testdb"
})
class PropertiesTest {
}
```

### 7.2. Web Environment

```java
// MOCK (default) - Mock web environment
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class MockWebTest {
    
    @Autowired
    private MockMvc mockMvc;  // Available with @AutoConfigureMockMvc
}

// RANDOM_PORT - Real web environment with random port
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RandomPortTest {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testEndpoint() {
        String url = "http://localhost:" + port + "/api/users";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}

// DEFINED_PORT - Use server.port from properties
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class DefinedPortTest {
}

// NONE - No web environment
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class NoWebTest {
}
```

### 7.3. TestRestTemplate

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RestTemplateTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testGetUsers() {
        ResponseEntity<User[]> response = restTemplate.getForEntity(
            "/api/users", User[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
    void testCreateUser() {
        User user = new User("John", "john@example.com");
        
        ResponseEntity<User> response = restTemplate.postForEntity(
            "/api/users", user, User.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }
    
    @Test
    void testWithAuthentication() {
        TestRestTemplate authenticatedTemplate = restTemplate
            .withBasicAuth("admin", "password");
        
        ResponseEntity<String> response = authenticatedTemplate
            .getForEntity("/api/admin/users", String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
```

### 7.4. @LocalServerPort

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PortTest {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testWithPort() {
        String baseUrl = "http://localhost:" + port;
        ResponseEntity<String> response = restTemplate.getForEntity(
            baseUrl + "/api/health", String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
```

---

## 8. MOCKMVC TESTING

### 8.1. @WebMvcTest

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void testGetUser() throws Exception {
        // Given
        User user = new User(1L, "John", "john@example.com");
        when(userService.findById(1L)).thenReturn(user);
        
        // When & Then
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"))
            .andExpect(jsonPath("$.email").value("john@example.com"));
        
        verify(userService).findById(1L);
    }
    
    @Test
    void testGetUserNotFound() throws Exception {
        when(userService.findById(999L))
            .thenThrow(new UserNotFoundException(999L));
        
        mockMvc.perform(get("/api/users/999"))
            .andExpect(status().isNotFound());
    }
}
```

**@WebMvcTest features:**
- Loads only web layer (controllers)
- Auto-configures MockMvc
- Doesn't load full application context
- Services must be mocked with @MockBean
- Fast test execution

### 8.2. MockMvc Requests

```java
@WebMvcTest(UserController.class)
class MockMvcRequestsTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void testGetRequest() throws Exception {
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk());
    }
    
    @Test
    void testPostRequest() throws Exception {
        User user = new User("John", "john@example.com");
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "John",
                        "email": "john@example.com"
                    }
                    """))
            .andExpect(status().isCreated());
    }
    
    @Test
    void testPutRequest() throws Exception {
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "Jane",
                        "email": "jane@example.com"
                    }
                    """))
            .andExpect(status().isOk());
    }
    
    @Test
    void testDeleteRequest() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
            .andExpect(status().isNoContent());
    }
    
    @Test
    void testWithHeaders() throws Exception {
        mockMvc.perform(get("/api/users/1")
                .header("Authorization", "Bearer token")
                .header("Accept", "application/json"))
            .andExpect(status().isOk());
    }
    
    @Test
    void testWithParams() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "name"))
            .andExpect(status().isOk());
    }
}
```

### 8.3. MockMvc Expectations

```java
@WebMvcTest(UserController.class)
class MockMvcExpectationsTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void testStatusExpectations() throws Exception {
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(status().is(200));
    }
    
    @Test
    void testContentExpectations() throws Exception {
        mockMvc.perform(get("/api/users/1"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json("""
                {
                    "name": "John",
                    "email": "john@example.com"
                }
                """));
    }
    
    @Test
    void testJsonPathExpectations() throws Exception {
        mockMvc.perform(get("/api/users/1"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("John"))
            .andExpect(jsonPath("$.email").exists())
            .andExpect(jsonPath("$.age").doesNotExist());
    }
    
    @Test
    void testHeaderExpectations() throws Exception {
        mockMvc.perform(get("/api/users/1"))
            .andExpect(header().exists("Content-Type"))
            .andExpect(header().string("Content-Type", 
                containsString("application/json")));
    }
    
    @Test
    void testViewExpectations() throws Exception {
        mockMvc.perform(get("/users"))
            .andExpect(view().name("users/list"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attribute("title", "User List"));
    }
}
```

### 8.4. @AutoConfigureMockMvc

```java
@SpringBootTest
@AutoConfigureMockMvc
class FullContextMockMvcTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    // Full application context loaded
    // No need for @MockBean
    
    @Test
    void testWithFullContext() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk());
    }
}
```

### 8.5. Result Actions

```java
@WebMvcTest(UserController.class)
class ResultActionsTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void testResultActions() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andReturn();
        
        String content = result.getResponse().getContentAsString();
        assertNotNull(content);
        
        // Further assertions on content
        assertTrue(content.contains("John"));
    }
    
    @Test
    void testResultMatchers() throws Exception {
        mockMvc.perform(get("/api/users/1"))
            .andDo(print())  // Print request/response
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"));
    }
}
```

---

## 9. SLICE TESTING

### 9.1. Test Slices Overview

Spring Boot provides test slice annotations for testing specific layers:

| Annotation | Layer | Loaded Components |
|------------|-------|-------------------|
| `@WebMvcTest` | Web layer | Controllers, WebMVC config |
| `@DataJpaTest` | Persistence | JPA, Repositories, EntityManager |
| `@DataJdbcTest` | JDBC | JDBC, Repositories |
| `@DataMongoTest` | MongoDB | MongoDB, Repositories |
| `@DataRedisTest` | Redis | Redis, Repositories |
| `@JsonTest` | JSON | JSON serialization |
| `@RestClientTest` | REST Client | RestTemplate, WebClient |

### 9.2. @WebMvcTest (Detailed)

```java
// Test single controller
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void testController() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk());
    }
}

// Test multiple controllers
@WebMvcTest(controllers = {
    UserController.class,
    OrderController.class
})
class MultiControllerTest {
}

// Test all controllers
@WebMvcTest
class AllControllersTest {
}
```

### 9.3. @DataJpaTest (Detailed)

```java
@DataJpaTest
class RepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    void testRepository() {
        User user = new User("John");
        entityManager.persist(user);
        entityManager.flush();
        
        User found = userRepository.findByName("John");
        assertEquals("John", found.getName());
    }
}

// Include additional components
@DataJpaTest(includeFilters = @Filter(
    type = FilterType.ASSIGNABLE_TYPE,
    classes = UserService.class
))
class ExtendedRepositoryTest {
    
    @Autowired
    private UserService userService;
}
```

### 9.4. @JsonTest

```java
@JsonTest
class JsonTest {
    
    @Autowired
    private JacksonTester<User> json;
    
    @Test
    void testSerialization() throws Exception {
        User user = new User(1L, "John", "john@example.com");
        
        JsonContent<User> content = json.write(user);
        
        assertThat(content).hasJsonPathStringValue("@.name");
        assertThat(content).extractingJsonPathStringValue("@.name")
            .isEqualTo("John");
        assertThat(content).extractingJsonPathStringValue("@.email")
            .isEqualTo("john@example.com");
    }
    
    @Test
    void testDeserialization() throws Exception {
        String content = """
            {
                "id": 1,
                "name": "John",
                "email": "john@example.com"
            }
            """;
        
        User user = json.parse(content).getObject();
        
        assertEquals(1L, user.getId());
        assertEquals("John", user.getName());
        assertEquals("john@example.com", user.getEmail());
    }
}
```

### 9.5. @RestClientTest

```java
@RestClientTest(UserClient.class)
class RestClientTest {
    
    @Autowired
    private UserClient userClient;
    
    @Autowired
    private MockRestServiceServer server;
    
    @Test
    void testRestClient() {
        // Mock external API response
        server.expect(requestTo("/api/users/1"))
            .andRespond(withSuccess("""
                {
                    "id": 1,
                    "name": "John"
                }
                """, MediaType.APPLICATION_JSON));
        
        // Call client
        User user = userClient.getUser(1L);
        
        // Verify
        assertEquals("John", user.getName());
        server.verify();
    }
}
```

---

## 10. MOCKING VỚI MOCKITO

### 10.1. @MockBean vs @Mock

```java
// @MockBean - Spring managed mock
@SpringBootTest
class SpringMockTest {
    
    @MockBean
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;  // Will use mocked repository
    
    @Test
    void test() {
        when(userRepository.findById(1L))
            .thenReturn(Optional.of(new User("John")));
        
        User user = userService.findById(1L);
        assertEquals("John", user.getName());
    }
}

// @Mock - Mockito mock (no Spring context)
@ExtendWith(MockitoExtension.class)
class MockitoTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    void test() {
        when(userRepository.findById(1L))
            .thenReturn(Optional.of(new User("John")));
        
        User user = userService.findById(1L);
        assertEquals("John", user.getName());
    }
}
```

### 10.2. Mockito Stubbing

```java
@ExtendWith(MockitoExtension.class)
class StubbingTest {
    
    @Mock
    private UserRepository repository;
    
    @Test
    void testStubbing() {
        // Return value
        when(repository.findById(1L))
            .thenReturn(Optional.of(new User("John")));
        
        // Return different values
        when(repository.count())
            .thenReturn(5L)
            .thenReturn(10L);
        
        // Throw exception
        when(repository.findById(999L))
            .thenThrow(new UserNotFoundException(999L));
        
        // Answer
        when(repository.save(any(User.class)))
            .thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(1L);
                return user;
            });
        
        // Do nothing (void methods)
        doNothing().when(repository).deleteById(1L);
        
        // Throw on void method
        doThrow(new RuntimeException()).when(repository).deleteById(999L);
    }
}
```

### 10.3. Argument Matchers

```java
@ExtendWith(MockitoExtension.class)
class MatchersTest {
    
    @Mock
    private UserService userService;
    
    @Test
    void testMatchers() {
        // Any
        when(userService.save(any(User.class)))
            .thenReturn(new User(1L, "Saved"));
        
        // Specific value
        when(userService.findById(eq(1L)))
            .thenReturn(new User("John"));
        
        // Any of type
        when(userService.findByName(anyString()))
            .thenReturn(new User("Default"));
        
        // Null
        when(userService.findByEmail(isNull()))
            .thenThrow(new IllegalArgumentException());
        
        // Not null
        when(userService.findByEmail(notNull()))
            .thenReturn(new User("John"));
        
        // Collection matchers
        when(userService.findByIds(anyList()))
            .thenReturn(Collections.emptyList());
    }
}
```

### 10.4. Verification

```java
@ExtendWith(MockitoExtension.class)
class VerificationTest {
    
    @Mock
    private UserRepository repository;
    
    @InjectMocks
    private UserService service;
    
    @Test
    void testVerification() {
        // Arrange
        User user = new User("John");
        when(repository.save(any(User.class))).thenReturn(user);
        
        // Act
        service.createUser(user);
        
        // Verify method called
        verify(repository).save(user);
        
        // Verify number of invocations
        verify(repository, times(1)).save(user);
        verify(repository, atLeastOnce()).save(user);
        verify(repository, atMost(2)).save(user);
        verify(repository, never()).deleteById(anyLong());
        
        // Verify with argument matcher
        verify(repository).save(argThat(u -> u.getName().equals("John")));
        
        // Verify order
        InOrder inOrder = inOrder(repository);
        inOrder.verify(repository).save(user);
        inOrder.verify(repository).findById(anyLong());
        
        // Verify no more interactions
        verifyNoMoreInteractions(repository);
    }
}
```

### 10.5. @SpyBean

```java
@SpringBootTest
class SpyTest {
    
    @SpyBean
    private UserService userService;
    
    @Test
    void testSpy() {
        // Spy calls real methods by default
        User user = userService.findById(1L);
        assertNotNull(user);
        
        // Can stub specific methods
        doReturn(new User("Mocked"))
            .when(userService).findById(999L);
        
        User mocked = userService.findById(999L);
        assertEquals("Mocked", mocked.getName());
        
        // Verify calls
        verify(userService, times(2)).findById(anyLong());
    }
}
```

---

## 11. TESTING REST APIs

### 11.1. Integration Testing REST APIs

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RestApiIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testGetUsers() {
        ResponseEntity<User[]> response = restTemplate.getForEntity(
            "/api/users", User[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }
    
    @Test
    void testCreateUser() {
        User user = new User("John", "john@example.com");
        
        ResponseEntity<User> response = restTemplate.postForEntity(
            "/api/users", user, User.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User created = response.getBody();
        assertNotNull(created.getId());
        assertEquals("John", created.getName());
    }
    
    @Test
    void testUpdateUser() {
        User user = new User("Jane", "jane@example.com");
        
        restTemplate.put("/api/users/1", user);
        
        ResponseEntity<User> response = restTemplate.getForEntity(
            "/api/users/1", User.class);
        assertEquals("Jane", response.getBody().getName());
    }
    
    @Test
    void testDeleteUser() {
        restTemplate.delete("/api/users/1");
        
        ResponseEntity<User> response = restTemplate.getForEntity(
            "/api/users/1", User.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
```

### 11.2. Testing with Authentication

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AuthenticatedRestTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testWithBasicAuth() {
        TestRestTemplate authenticated = restTemplate
            .withBasicAuth("admin", "password");
        
        ResponseEntity<String> response = authenticated.getForEntity(
            "/api/admin/users", String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    void testWithJwtToken() {
        // Login to get token
        LoginRequest login = new LoginRequest("user", "password");
        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity(
            "/api/auth/login", login, LoginResponse.class);
        
        String token = loginResponse.getBody().getToken();
        
        // Use token for request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        
        ResponseEntity<User[]> response = restTemplate.exchange(
            "/api/users", HttpMethod.GET, entity, User[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
```

### 11.3. Testing Error Responses

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ErrorResponseTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testNotFound() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
            "/api/users/999", ErrorResponse.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse error = response.getBody();
        assertNotNull(error);
        assertEquals(404, error.getStatus());
        assertTrue(error.getMessage().contains("not found"));
    }
    
    @Test
    void testValidationError() {
        User invalidUser = new User("", "invalid-email");
        
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
            "/api/users", invalidUser, ErrorResponse.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
```

---

## 12. TESTING TRANSACTIONS

### 12.1. @Transactional in Tests

```java
@SpringBootTest
@Transactional  // Rolls back after each test
class TransactionalTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void testTransactionalRollback() {
        User user = new User("John");
        userRepository.save(user);
        
        assertEquals(1, userRepository.count());
        
        // Automatically rolled back after test
    }
    
    @Test
    void testAnotherTest() {
        // Clean state - previous test rolled back
        assertEquals(0, userRepository.count());
    }
}
```

### 12.2. @Commit

```java
@SpringBootTest
class CommitTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    @Transactional
    @Commit  // Don't rollback
    void testCommit() {
        User user = new User("John");
        userRepository.save(user);
        
        // Changes committed
    }
    
    @Test
    void testVerifyCommit() {
        // Data from previous test still exists
        assertTrue(userRepository.count() > 0);
    }
}
```

### 12.3. @Rollback

```java
@SpringBootTest
@Transactional
class RollbackTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    @Rollback(false)  // Don't rollback this test
    void testNoRollback() {
        User user = new User("John");
        userRepository.save(user);
    }
    
    @Test
    @Rollback  // Explicit rollback (default behavior)
    void testWithRollback() {
        User user = new User("Jane");
        userRepository.save(user);
    }
}
```

### 12.4. Testing Transaction Propagation

```java
@SpringBootTest
class PropagationTest {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    @Transactional
    void testRequiresNew() {
        // Setup
        User user = new User("John");
        userRepository.save(user);
        
        // Call method with REQUIRES_NEW
        try {
            userService.updateWithNewTransaction(user.getId(), "Jane");
        } catch (Exception e) {
            // Even if outer transaction rolls back,
            // REQUIRES_NEW transaction is committed
        }
        
        // Verify update was committed
        User updated = userRepository.findById(user.getId()).get();
        assertEquals("Jane", updated.getName());
    }
}
```

---

## 13. TESTING SECURITY

### 13.1. @WithMockUser

```java
@WebMvcTest(UserController.class)
class SecurityTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    @WithMockUser  // Default user
    void testWithDefaultUser() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testWithAdmin() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
            .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "user", authorities = {"READ"})
    void testWithAuthorities() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk());
    }
    
    @Test
    void testWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isUnauthorized());
    }
}
```

### 13.2. @WithAnonymousUser

```java
@WebMvcTest(PublicController.class)
class AnonymousUserTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @WithAnonymousUser
    void testAnonymousAccess() throws Exception {
        mockMvc.perform(get("/api/public/info"))
            .andExpect(status().isOk());
    }
    
    @Test
    @WithAnonymousUser
    void testAnonymousAccessDenied() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isUnauthorized());
    }
}
```

### 13.3. @WithUserDetails

```java
@SpringBootTest
@AutoConfigureMockMvc
class UserDetailsTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @WithUserDetails("john@example.com")  // Load from UserDetailsService
    void testWithRealUser() throws Exception {
        mockMvc.perform(get("/api/profile"))
            .andExpect(status().isOk());
    }
}
```

### 13.4. Custom Security Annotation

```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "admin", roles = {"ADMIN"})
public @interface WithMockAdmin {
}

// Usage
@WebMvcTest
class CustomSecurityTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @WithMockAdmin
    void testWithCustomAnnotation() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
            .andExpect(status().isOk());
    }
}
```

### 13.5. Testing Method Security

```java
@SpringBootTest
class MethodSecurityTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testAdminAccess() {
        // Method with @PreAuthorize("hasRole('ADMIN')")
        assertDoesNotThrow(() -> userService.deleteUser(1L));
    }
    
    @Test
    @WithMockUser(roles = "USER")
    void testUserAccessDenied() {
        // Method with @PreAuthorize("hasRole('ADMIN')")
        assertThrows(AccessDeniedException.class, 
            () -> userService.deleteUser(1L));
    }
}
```

---

## 14. TESTING BEST PRACTICES

### 14.1. Test Structure (AAA Pattern)

```java
class BestPracticeTest {
    
    @Test
    void testWithAAAPattern() {
        // Arrange (Given)
        User user = new User("John", "john@example.com");
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        // Act (When)
        User saved = userService.createUser(user);
        
        // Assert (Then)
        assertNotNull(saved);
        assertEquals("John", saved.getName());
        verify(userRepository).save(user);
    }
}
```

### 14.2. Test Naming

```java
class NamingConventionsTest {
    
    // ✅ GOOD - Clear what is tested
    @Test
    void shouldReturnUserWhenEmailExists() {
    }
    
    @Test
    void shouldThrowExceptionWhenUserNotFound() {
    }
    
    @Test
    void givenValidUser_whenSaving_thenSucceeds() {
    }
    
    // ❌ BAD - Unclear
    @Test
    void test1() {
    }
    
    @Test
    void testUser() {
    }
}
```

### 14.3. One Assertion per Test

```java
class AssertionBestPracticeTest {
    
    // ✅ GOOD - Single concern
    @Test
    void shouldSaveUserWithGeneratedId() {
        User saved = userService.save(new User("John"));
        assertNotNull(saved.getId());
    }
    
    @Test
    void shouldSaveUserWithCorrectName() {
        User saved = userService.save(new User("John"));
        assertEquals("John", saved.getName());
    }
    
    // OR use assertAll for related assertions
    @Test
    void shouldSaveUserCorrectly() {
        User saved = userService.save(new User("John"));
        assertAll(
            () -> assertNotNull(saved.getId()),
            () -> assertEquals("John", saved.getName())
        );
    }
    
    // ❌ BAD - Multiple unrelated assertions
    @Test
    void testEverything() {
        // Too many responsibilities
    }
}
```

### 14.4. Use Test Fixtures

```java
class FixtureTest {
    
    private User defaultUser;
    private List<User> users;
    
    @BeforeEach
    void setupFixtures() {
        defaultUser = new User("John", "john@example.com");
        users = Arrays.asList(
            new User("John", "john@example.com"),
            new User("Jane", "jane@example.com")
        );
    }
    
    @Test
    void testWithFixtures() {
        // Use defaultUser and users
    }
}
```

### 14.5. Avoid Test Dependencies

```java
// ❌ BAD - Tests depend on order
class DependentTests {
    
    @Test
    @Order(1)
    void createUser() {
        // Creates user with id=1
    }
    
    @Test
    @Order(2)
    void updateUser() {
        // Depends on user from previous test
    }
}

// ✅ GOOD - Independent tests
class IndependentTests {
    
    @Test
    void createUser() {
        User user = new User("John");
        User saved = userService.save(user);
        assertNotNull(saved.getId());
    }
    
    @Test
    void updateUser() {
        // Setup own data
        User user = userService.save(new User("John"));
        user.setName("Jane");
        User updated = userService.update(user);
        assertEquals("Jane", updated.getName());
    }
}
```

### 14.6. Test Coverage

```xml
<!-- Maven JaCoCo Plugin -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.8</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

**Coverage Goals:**
- **70-80%** overall coverage
- **90%+** for critical business logic
- **Don't aim for 100%** (diminishing returns)

---

## 15. CÂU HỎI MẪU CHO KỲ THI

### 15.1. Câu hỏi lý thuyết

#### Câu 1: Sự khác biệt giữa @SpringBootTest và @WebMvcTest?

**Trả lời**:

| Aspect | @SpringBootTest | @WebMvcTest |
|--------|----------------|-------------|
| **Context** | Full application context | Web layer only |
| **Components** | All beans | Controllers, WebMVC config |
| **Speed** | Slower | Faster |
| **Database** | Real/embedded | Not loaded |
| **MockMvc** | Need @AutoConfigureMockMvc | Auto-configured |
| **Services** | Real | Need @MockBean |
| **Use case** | Integration tests | Controller unit tests |

---

#### Câu 2: @Transactional trong tests hoạt động như thế nào?

**Trả lời**:
- Mặc định, test methods annotated với @Transactional sẽ **rollback** sau khi test
- Điều này ensure test isolation và clean state
- Có thể disable rollback với @Commit hoặc @Rollback(false)
- @DataJpaTest tự động transactional

---

#### Câu 3: @MockBean vs @Mock khác nhau như thế nào?

**Trả lời**:
- **@MockBean**: Spring-managed mock, registered trong ApplicationContext
- **@Mock**: Mockito mock, không liên quan đến Spring context
- **@MockBean** dùng khi cần inject mock vào Spring beans
- **@Mock** dùng cho pure unit tests không có Spring

---

#### Câu 4: Test slicing là gì và tại sao dùng?

**Trả lời**:
**Test slicing** loads only specific parts of application context:
- **@WebMvcTest**: Web layer
- **@DataJpaTest**: Persistence layer
- **@JsonTest**: JSON serialization
- **Benefits**: Faster tests, focused testing, less dependencies

---

#### Câu 5: TestEntityManager dùng khi nào?

**Trả lời**:
**TestEntityManager** dùng trong @DataJpaTest để:
- Persist entities outside repository
- Flush changes immediately
- Clear persistence context
- More control over test data
- Alternative to using repositories for test setup

---

### 15.2. Câu hỏi code-based

#### Câu 6: Fix code sau để test REST endpoint properly:

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    // Missing @MockBean!
    private UserService userService;
    
    @Test
    void testGetUser() throws Exception {
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk());
    }
}
```

**Fix:**
```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean  // ✅ Add this
    private UserService userService;
    
    @Test
    void testGetUser() throws Exception {
        // Setup mock
        User user = new User(1L, "John");
        when(userService.findById(1L)).thenReturn(user);
        
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"));
    }
}
```

---

#### Câu 7: Test repository method với @DataJpaTest:

```java
@DataJpaTest
class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    void testFindByEmail() {
        // Arrange
        User user = new User("John", "john@example.com");
        entityManager.persist(user);
        entityManager.flush();
        
        // Act
        User found = userRepository.findByEmail("john@example.com");
        
        // Assert
        assertNotNull(found);
        assertEquals("John", found.getName());
        assertEquals("john@example.com", found.getEmail());
    }
}
```

---

#### Câu 8: Test với Spring profiles:

```java
@SpringBootTest
@ActiveProfiles("test")
class ProfileTest {
    
    @Autowired
    private DataSource dataSource;
    
    @Test
    void testTestProfile() throws Exception {
        // Verify using test profile configuration
        try (Connection conn = dataSource.getConnection()) {
            String url = conn.getMetaData().getURL();
            assertTrue(url.contains("h2"));  // Test profile uses H2
        }
    }
}
```

---

### 15.3. Scenario-based Questions

#### Câu 9: Test REST API với authentication?

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AuthenticatedApiTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testProtectedEndpoint() {
        // Without auth - should fail
        ResponseEntity<String> response = restTemplate.getForEntity(
            "/api/users", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    
    @Test
    void testWithBasicAuth() {
        // With auth - should succeed
        TestRestTemplate authenticated = restTemplate
            .withBasicAuth("admin", "password");
        
        ResponseEntity<User[]> response = authenticated.getForEntity(
            "/api/users", User[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}

// Or với MockMvc
@WebMvcTest(UserController.class)
class MockMvcAuthTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testWithMockUser() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
            .andExpect(status().isOk());
    }
}
```

---

#### Câu 10: Test transaction rollback behavior?

```java
@SpringBootTest
@Transactional
class TransactionRollbackTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Test
    void testRollbackOnException() {
        // Initial state
        long initialCount = userRepository.count();
        
        // Try to create user (will fail)
        try {
            userService.createUserWithValidation(new User("")); // Invalid
        } catch (Exception e) {
            // Expected exception
        }
        
        // Verify rollback
        assertEquals(initialCount, userRepository.count());
    }
    
    @Test
    @Commit  // Explicitly commit
    void testCommit() {
        User user = new User("John");
        userRepository.save(user);
        
        // Will be committed
    }
}
```

---

## 16. TÓM TẮT VÀ MẸO THI

### 16.1. Core Testing Annotations

| Annotation | Purpose |
|------------|---------|
| `@SpringBootTest` | Full integration test |
| `@WebMvcTest` | Test web layer |
| `@DataJpaTest` | Test persistence layer |
| `@MockBean` | Mock Spring bean |
| `@SpyBean` | Spy Spring bean |
| `@Transactional` | Transactional test (rollback) |
| `@Commit` | Commit transaction |
| `@Sql` | Execute SQL scripts |
| `@ActiveProfiles` | Activate profiles |
| `@TestConfiguration` | Test-specific config |
| `@AutoConfigureMockMvc` | Enable MockMvc |
| `@WithMockUser` | Mock authenticated user |

### 16.2. Test Types Summary

```
Unit Tests (Fast)
    ↓
@ExtendWith(MockitoExtension.class)
@Mock, @InjectMocks
No Spring context

Slice Tests (Medium)
    ↓
@WebMvcTest, @DataJpaTest
Partial Spring context
Mocked dependencies

Integration Tests (Slow)
    ↓
@SpringBootTest
Full Spring context
Real dependencies
```

### 16.3. Common Patterns

**Controller Test:**
```java
@WebMvcTest(UserController.class)
@MockBean UserService
MockMvc.perform()
```

**Repository Test:**
```java
@DataJpaTest
TestEntityManager
@Transactional (auto)
```

**Service Test:**
```java
@SpringBootTest
@Transactional
Real dependencies
```

**REST API Test:**
```java
@SpringBootTest(webEnvironment = RANDOM_PORT)
TestRestTemplate
```

### 16.4. Common Pitfalls

❌ **Mistake 1**: Forgetting @MockBean
```java
// BAD
@WebMvcTest(UserController.class)
class Test {
    // Missing @MockBean for UserService
}

// GOOD
@WebMvcTest(UserController.class)
class Test {
    @MockBean
    private UserService userService;
}
```

❌ **Mistake 2**: Test dependencies
```java
// BAD - Order matters
@Test
@Order(1)
void createUser() { }

@Test
@Order(2)
void updateUser() { }  // Depends on test 1
```

❌ **Mistake 3**: Not using @Transactional
```java
// BAD - Data persists between tests
@DataJpaTest
class Test {
    // Tests affect each other
}

// GOOD - Automatic rollback
@DataJpaTest  // Already @Transactional
class Test { }
```

### 16.5. Mẹo làm bài thi

1. ✅ **@SpringBootTest** = Full context, integration test
2. ✅ **@WebMvcTest** = Web layer only, need @MockBean
3. ✅ **@DataJpaTest** = Persistence layer, auto @Transactional
4. ✅ **@MockBean** = Spring-managed mock
5. ✅ **@Mock** = Mockito mock, no Spring
6. ✅ **@Transactional** in tests = Auto rollback
7. ✅ **MockMvc** = Test controllers without server
8. ✅ **TestRestTemplate** = Test with real server
9. ✅ **TestEntityManager** = For @DataJpaTest
10. ✅ **@WithMockUser** = Mock authentication

### 16.6. Checklist ôn tập

- [ ] JUnit 5 basics (@Test, @BeforeEach, etc.)
- [ ] JUnit 5 assertions
- [ ] Parameterized tests
- [ ] @SpringBootTest variants
- [ ] @WebMvcTest
- [ ] @DataJpaTest
- [ ] MockMvc usage
- [ ] TestRestTemplate
- [ ] @MockBean vs @Mock
- [ ] Mockito stubbing và verification
- [ ] @Transactional in tests
- [ ] @ActiveProfiles
- [ ] @Sql
- [ ] TestEntityManager
- [ ] @WithMockUser
- [ ] Test slicing concept

### 16.7. Testing Strategy

```
1. Unit Tests (70%)
   - Pure logic
   - Mockito
   - Fast execution

2. Slice Tests (20%)
   - @WebMvcTest
   - @DataJpaTest
   - Focused layers

3. Integration Tests (10%)
   - @SpringBootTest
   - End-to-end
   - Full context
```

---

## KẾT LUẬN

Testing là crucial skill cho Spring developers và là important part của certification exam. Để thành công:

- ✅ Master JUnit 5 fundamentals
- ✅ Understand Spring testing annotations
- ✅ Know when to use each test type
- ✅ Practice với MockMvc và TestRestTemplate
- ✅ Understand transaction behavior in tests
- ✅ Know test slicing patterns

### Key Points:

> **Testing Pyramid: Unit > Integration > E2E**
>
> Essential patterns:
> - @SpringBootTest for integration
> - @WebMvcTest for controllers
> - @DataJpaTest for repositories
> - @MockBean for mocking dependencies
> - @Transactional for isolation
> - MockMvc for web tests

**Testing Framework:**
```
JUnit 5 (Jupiter)
    +
Spring TestContext
    +
Mockito
    +
AssertJ
    =
Complete Testing Solution
```

**Remember:**
- Test behavior, not implementation
- Keep tests independent
- Use appropriate test type
- Mock external dependencies
- Follow AAA pattern (Arrange-Act-Assert)

Hãy practice với các patterns trong tài liệu này. Good testing practices lead to better code quality and more confident deployments!

---

**Chúc bạn thành công với Spring Professional Certification!** 🎯✅

*Tài liệu được tạo ngày 27/12/2024*
*Dựa trên Spring Professional Develop Exam Guide - Section 4: Testing*
