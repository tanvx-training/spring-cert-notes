# Testing với JUnit 5 & Spring Test

## 📚 Mục Lục
1. [JUnit 5 Fundamentals](#1-junit-5-fundamentals)
2. [Mockito Basics](#2-mockito-basics)
3. [Spring Test Slices](#3-spring-test-slices)
4. [@MockBean vs @Mock](#4-mockbean-vs-mock)
5. [Best Practices](#5-best-practices)

---

## 1. JUnit 5 Fundamentals

### 1.1 Annotations Cơ Bản

```java
@Test                    // Đánh dấu method là test
@DisplayName("...")      // Tên hiển thị cho test
@Disabled                // Bỏ qua test
@BeforeEach              // Chạy trước mỗi test
@AfterEach               // Chạy sau mỗi test
@BeforeAll               // Chạy 1 lần trước tất cả tests (static)
@AfterAll                // Chạy 1 lần sau tất cả tests (static)
@Nested                  // Nhóm tests liên quan
@Tag("...")              // Gắn tag để filter tests
```

### 1.2 Assertions

```java
// Basic assertions
assertEquals(expected, actual);
assertNotEquals(unexpected, actual);
assertTrue(condition);
assertFalse(condition);
assertNull(object);
assertNotNull(object);
assertSame(expected, actual);      // Same reference
assertNotSame(unexpected, actual);

// Array assertions
assertArrayEquals(expected, actual);

// Exception assertions
assertThrows(ExceptionType.class, () -> { /* code */ });
assertDoesNotThrow(() -> { /* code */ });

// Grouped assertions - chạy tất cả, báo cáo tất cả failures
assertAll("group name",
    () -> assertEquals(expected1, actual1),
    () -> assertEquals(expected2, actual2),
    () -> assertTrue(condition)
);

// Timeout assertions
assertTimeout(Duration.ofMillis(100), () -> { /* code */ });
```

### 1.3 Assumptions

```java
// Skip test nếu điều kiện không thỏa mãn
assumeTrue(condition);           // Skip if false
assumeFalse(condition);          // Skip if true
assumingThat(condition, () -> {  // Conditional execution
    // Chỉ chạy nếu condition = true
});
```

### 1.4 Parameterized Tests

```java
// @ValueSource - giá trị đơn giản
@ParameterizedTest
@ValueSource(ints = {1, 2, 3})
void testWithInts(int number) { }

@ParameterizedTest
@ValueSource(strings = {"a", "b", "c"})
void testWithStrings(String value) { }

// @NullSource, @EmptySource, @NullAndEmptySource
@ParameterizedTest
@NullAndEmptySource
@ValueSource(strings = {"  ", "\t"})
void testBlankStrings(String value) { }

// @CsvSource - nhiều parameters
@ParameterizedTest
@CsvSource({
    "1, 2, 3",
    "5, 5, 10"
})
void testAddition(int a, int b, int expected) { }

// @MethodSource - custom data provider
@ParameterizedTest
@MethodSource("dataProvider")
void testWithMethodSource(String input, int expected) { }

static Stream<Arguments> dataProvider() {
    return Stream.of(
        Arguments.of("input1", 1),
        Arguments.of("input2", 2)
    );
}

// @EnumSource
@ParameterizedTest
@EnumSource(DayOfWeek.class)
void testWithEnum(DayOfWeek day) { }
```

### 1.5 Conditional Test Execution

```java
@EnabledOnOs(OS.MAC)                    // Chỉ chạy trên Mac
@DisabledOnOs(OS.WINDOWS)               // Không chạy trên Windows
@EnabledOnJre(JRE.JAVA_17)              // Chỉ Java 17
@EnabledForJreRange(min = JRE.JAVA_11)  // Java 11+
@EnabledIfSystemProperty(named = "...", matches = "...")
@EnabledIfEnvironmentVariable(named = "...", matches = "...")
```

---

## 2. Mockito Basics

### 2.1 Setup với JUnit 5

```java
@ExtendWith(MockitoExtension.class)
class MyServiceTest {
    
    @Mock
    private MyRepository repository;
    
    @InjectMocks
    private MyService service;
    
    @Captor
    private ArgumentCaptor<User> userCaptor;
}
```

### 2.2 Stubbing (Định nghĩa behavior)

```java
// Return value
when(repository.findById(1L)).thenReturn(Optional.of(user));

// Return different values
when(repository.count())
    .thenReturn(0L)
    .thenReturn(1L);

// Throw exception
when(repository.findById(999L))
    .thenThrow(new RuntimeException("Not found"));

// Dynamic response với Answer
when(repository.save(any(User.class)))
    .thenAnswer(invocation -> {
        User u = invocation.getArgument(0);
        u.setId(1L);
        return u;
    });

// void methods
doNothing().when(repository).deleteById(1L);
doThrow(new RuntimeException()).when(repository).deleteById(999L);
```

### 2.3 Argument Matchers

```java
any()                    // Bất kỳ giá trị nào
any(User.class)          // Bất kỳ User nào
anyLong(), anyString()   // Bất kỳ Long/String
eq(value)                // Giá trị chính xác
argThat(predicate)       // Custom matcher

// Ví dụ
when(repository.save(any(User.class))).thenReturn(user);
when(repository.findByEmail(eq("test@example.com"))).thenReturn(Optional.of(user));
when(repository.save(argThat(u -> u.getEmail().endsWith("@admin.com")))).thenReturn(admin);
```

### 2.4 Verification

```java
verify(repository).save(user);                    // Được gọi 1 lần
verify(repository, times(2)).findById(anyLong()); // Được gọi 2 lần
verify(repository, never()).deleteById(any());    // Không được gọi
verify(repository, atLeast(1)).findAll();         // Ít nhất 1 lần
verify(repository, atMost(3)).count();            // Nhiều nhất 3 lần
verifyNoMoreInteractions(repository);             // Không có interaction khác

// Capture arguments
verify(repository).save(userCaptor.capture());
User captured = userCaptor.getValue();
```

---

## 3. Spring Test Slices

### 3.1 Tổng Quan Test Slices

| Annotation | Load Components | Use Case |
|------------|-----------------|----------|
| `@WebMvcTest` | Controllers, Filters, ControllerAdvice | Test REST controllers |
| `@DataJpaTest` | JPA repositories, EntityManager | Test JPA repositories |
| `@DataJdbcTest` | JDBC repositories | Test Spring Data JDBC |
| `@JdbcTest` | JdbcTemplate | Test raw JDBC |
| `@JsonTest` | ObjectMapper | Test JSON serialization |
| `@RestClientTest` | RestTemplateBuilder | Test REST clients |
| `@WebFluxTest` | WebFlux controllers | Test reactive controllers |

### 3.2 @WebMvcTest

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean  // Mock dependencies
    private UserService userService;
    
    @Test
    void shouldReturnUser() throws Exception {
        when(userService.findById(1L)).thenReturn(Optional.of(user));
        
        mockMvc.perform(get("/api/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"));
    }
    
    @Test
    void shouldCreateUser() throws Exception {
        when(userService.create(any())).thenReturn(savedUser);
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists());
    }
}
```

### 3.3 @DataJpaTest

```java
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    void shouldFindByEmail() {
        // Setup với TestEntityManager
        User user = new User("John", "john@example.com");
        entityManager.persistAndFlush(user);
        
        // Test
        Optional<User> found = userRepository.findByEmail("john@example.com");
        
        // Assert
        assertThat(found).isPresent();
    }
}
```

**Đặc điểm @DataJpaTest:**
- Auto-configure embedded database (H2)
- Mỗi test chạy trong transaction
- Auto-rollback sau mỗi test
- Không load @Service, @Controller

### 3.4 @JsonTest

```java
@JsonTest
class UserJsonTest {
    
    @Autowired
    private JacksonTester<User> json;
    
    @Test
    void shouldSerialize() throws Exception {
        User user = new User("John", "john@example.com");
        
        JsonContent<User> result = json.write(user);
        
        assertThat(result).extractingJsonPathStringValue("$.name")
            .isEqualTo("John");
    }
    
    @Test
    void shouldDeserialize() throws Exception {
        String content = "{\"name\":\"John\",\"email\":\"john@example.com\"}";
        
        User user = json.parseObject(content);
        
        assertThat(user.getName()).isEqualTo("John");
    }
}
```

### 3.5 @SpringBootTest

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional  // Rollback after each test
class FullIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void endToEndTest() throws Exception {
        // Test full flow: Controller -> Service -> Repository -> Database
    }
}
```

**WebEnvironment options:**
- `MOCK` (default): Mock servlet environment
- `RANDOM_PORT`: Real server on random port
- `DEFINED_PORT`: Real server on defined port
- `NONE`: No web environment

---

## 4. @MockBean vs @Mock

### 4.1 @Mock (Mockito)

```java
@ExtendWith(MockitoExtension.class)
class ServiceTest {
    
    @Mock
    private Repository repository;  // Pure Mockito mock
    
    @InjectMocks
    private Service service;        // Inject mocks
}
```

**Đặc điểm:**
- Không liên quan đến Spring
- Nhanh hơn vì không load Spring context
- Dùng cho unit tests thuần túy

### 4.2 @MockBean (Spring Boot)

```java
@SpringBootTest
class IntegrationTest {
    
    @MockBean
    private Repository repository;  // Replaces bean in Spring context
    
    @Autowired
    private Service service;        // Uses mocked repository
}
```

**Đặc điểm:**
- Thay thế bean trong Spring context
- Cần Spring context đang chạy
- Dùng cho integration tests

### 4.3 @SpyBean

```java
@SpringBootTest
class SpyTest {
    
    @SpyBean
    private Service service;  // Wrap real bean
    
    @Test
    void partialMock() {
        // Mock một method cụ thể
        doReturn(mockResult).when(service).specificMethod();
        
        // Các methods khác vẫn chạy thật
        service.otherMethod();  // Real implementation
    }
}
```

---

## 5. Best Practices

### 5.1 Test Structure (AAA Pattern)

```java
@Test
void shouldDoSomething() {
    // Arrange (Given)
    User user = new User("John", "john@example.com");
    when(repository.save(any())).thenReturn(user);
    
    // Act (When)
    User result = service.createUser(user);
    
    // Assert (Then)
    assertThat(result.getName()).isEqualTo("John");
    verify(repository).save(any());
}
```

### 5.2 Naming Convention

```java
// Pattern: should[ExpectedBehavior]_when[Condition]
void shouldReturnUser_whenUserExists() { }
void shouldThrowException_whenEmailIsDuplicate() { }
void shouldReturnEmptyList_whenNoUsersFound() { }
```

### 5.3 Test Pyramid

```
        /\
       /  \      E2E Tests (ít nhất)
      /----\
     /      \    Integration Tests
    /--------\
   /          \  Unit Tests (nhiều nhất)
  /------------\
```

### 5.4 Chọn Test Slice Phù Hợp

| Scenario | Recommended |
|----------|-------------|
| Test business logic | Unit test với @Mock |
| Test controller endpoints | @WebMvcTest |
| Test repository queries | @DataJpaTest |
| Test JSON serialization | @JsonTest |
| Test full flow | @SpringBootTest |

### 5.5 Test Configuration

```properties
# application-test.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
logging.level.org.springframework.test=DEBUG
```

### 5.6 Common Mistakes to Avoid

```java
// ❌ Không mock static methods trực tiếp
// ❌ Không test private methods
// ❌ Không có quá nhiều mocks trong 1 test
// ❌ Không test implementation details

// ✅ Test behavior, không test implementation
// ✅ Mỗi test chỉ test 1 scenario
// ✅ Sử dụng meaningful test names
// ✅ Keep tests independent
```

---

## 📁 Cấu Trúc Test Files

```
src/test/java/com/example/spring_cert_notes/testing/
├── unit/
│   ├── JUnit5FeaturesTest.java      # JUnit 5 features demo
│   └── UserServiceUnitTest.java     # Unit test với Mockito
├── integration/
│   ├── UserControllerIntegrationTest.java  # @WebMvcTest
│   ├── UserRepositoryIntegrationTest.java  # @DataJpaTest
│   └── SpringBootTestExample.java          # @SpringBootTest
├── slice/
│   ├── TestSlicesExamples.java      # Documentation
│   └── JsonTestExample.java         # @JsonTest
└── mock/
    └── MockBeanExamples.java        # @MockBean, @SpyBean
```

---

## 🎯 Checklist

- [ ] Viết unit tests với JUnit 5 Assertions
- [ ] Sử dụng @ParameterizedTest cho multiple inputs
- [ ] Mock dependencies với Mockito
- [ ] Test controllers với @WebMvcTest
- [ ] Test repositories với @DataJpaTest
- [ ] Test JSON với @JsonTest
- [ ] Full integration test với @SpringBootTest
- [ ] Hiểu sự khác biệt @Mock vs @MockBean
- [ ] Đạt 80%+ code coverage
