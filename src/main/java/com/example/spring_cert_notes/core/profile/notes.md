# NGÀY 3-4: PROPERTIES, PROFILES & SPEL

## 📚 MỤC TIÊU HỌC TẬP

### 1. Quản lý Properties từ nhiều nguồn
### 2. Sử dụng @Profile cho các môi trường khác nhau
### 3. Viết SpEL expressions phức tạp
### 4. Type-safe configuration với @ConfigurationProperties

---

## 🎯 PHẦN 1: PROPERTY MANAGEMENT

### Property Sources trong Spring (theo thứ tự ưu tiên)

```
1. Command line arguments (--property=value)
   ↓
2. System properties (-Dproperty=value)
   ↓
3. OS environment variables
   ↓
4. application-{profile}.properties
   ↓
5. application.properties
   ↓
6. @PropertySource files
   ↓
7. Default values in @Value
```

### Cách inject properties: @Value

```java
// Basic injection
@Value("${app.name}")
private String appName;

// With default value
@Value("${app.name:MyApp}")
private String appName;

// Numeric types
@Value("${app.max.users:100}")
private int maxUsers;

// Boolean
@Value("${app.feature.enabled:true}")
private boolean featureEnabled;

// Array/List (comma-separated)
@Value("${app.allowed.origins:localhost,example.com}")
private String[] allowedOrigins;

// System properties
@Value("${user.home}")
private String userHome;

// Environment variables
@Value("${PATH}")
private String path;
```

### @PropertySource - Load external files

```java
@Configuration
@PropertySource("classpath:custom.properties")
public class AppConfig {
    // Properties from custom.properties are now available
}

// Multiple files
@PropertySources({
    @PropertySource("classpath:app.properties"),
    @PropertySource("classpath:db.properties"),
    @PropertySource(value = "file:./config/override.properties", 
                    ignoreResourceNotFound = true)
})
```

**Xem code:** `PropertySourcesConfig.java`

---

## 🎯 PHẦN 2: PROFILES

### Profile là gì?

Profile cho phép bạn:
- Tách configuration cho các môi trường khác nhau (dev, test, prod)
- Kích hoạt/vô hiệu hóa beans dựa trên môi trường
- Load properties files khác nhau cho mỗi profile

### Cách sử dụng @Profile

```java
// Single profile
@Configuration
@Profile("dev")
public class DevConfig {
    // Only loaded when 'dev' profile is active
}

// Multiple profiles (OR logic)
@Profile({"dev", "test"})
public class NonProdConfig {
    // Loaded when 'dev' OR 'test' is active
}

// Negation (NOT logic)
@Profile("!prod")
public class DebugConfig {
    // Loaded when NOT in production
}

// Complex expressions (Spring 5.1+)
@Profile("dev & cloud")  // AND
@Profile("dev | test")   // OR
@Profile("!prod & !staging")  // NOT
```

### Kích hoạt Profile

**Cách 1: Command line**
```bash
java -jar app.jar --spring.profiles.active=dev
```

**Cách 2: System property**
```bash
java -Dspring.profiles.active=dev -jar app.jar
```

**Cách 3: application.properties**
```properties
spring.profiles.active=dev
```

**Cách 4: Programmatically**
```java
context.getEnvironment().setActiveProfiles("dev");
```

**Cách 5: Environment variable**
```bash
export SPRING_PROFILES_ACTIVE=dev
```

### Profile-specific Properties Files

```
application.properties          # Base properties
application-dev.properties      # Dev overrides
application-test.properties     # Test overrides
application-prod.properties     # Prod overrides
```

**Loading order:**
1. application.properties (base)
2. application-{profile}.properties (overrides base)

**Xem code:** `DatabaseConfig.java`, `application-dev.properties`, `application-test.properties`, `application-prod.properties`

---

## 🎯 PHẦN 3: SPRING EXPRESSION LANGUAGE (SpEL)

### SpEL Syntax

```java
// Property reference
@Value("${property.name}")

// SpEL expression
@Value("#{expression}")

// Combine both
@Value("#{'${property.name}'.toUpperCase()}")
```

### 10+ Common SpEL Expressions

#### 1. Literal Values
```java
@Value("#{100}")
private int number;

@Value("#{'Hello'}")
private String text;

@Value("#{true}")
private boolean flag;
```

#### 2. System Properties
```java
@Value("#{systemProperties['user.name']}")
private String userName;

@Value("#{systemProperties['user.country'] ?: 'US'}")
private String country;  // Elvis operator for default
```

#### 3. Environment Variables
```java
@Value("#{systemEnvironment['JAVA_HOME']}")
private String javaHome;
```

#### 4. Mathematical Operations
```java
@Value("#{10 + 20}")
private int sum;

@Value("#{100 * 2}")
private int product;

@Value("#{T(java.lang.Math).random() * 100}")
private double random;

@Value("#{T(java.lang.Math).max(10, 20)}")
private int max;
```

#### 5. String Operations
```java
@Value("#{'Hello'.concat(' World')}")
private String concat;

@Value("#{'SPRING'.toLowerCase()}")
private String lower;

@Value("#{'spring'.toUpperCase()}")
private String upper;

@Value("#{'Hello World'.substring(0, 5)}")
private String sub;

@Value("#{'Hello'.length()}")
private int length;
```

#### 6. Boolean Logic
```java
@Value("#{10 > 5}")
private boolean comparison;

@Value("#{10 > 5 and 20 < 30}")
private boolean and;

@Value("#{10 > 5 or 20 > 30}")
private boolean or;

@Value("#{!(10 > 5)}")
private boolean not;
```

#### 7. Conditional (Ternary) Operator
```java
@Value("#{10 > 5 ? 'Yes' : 'No'}")
private String ternary;

@Value("#{environment == 'prod' ? 100 : 10}")
private int conditionalValue;
```

#### 8. Regular Expressions
```java
@Value("#{'john@example.com' matches '[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}'}")
private boolean isValidEmail;
```

#### 9. Collections
```java
// Create list
@Value("#{T(java.util.Arrays).asList('dev', 'test', 'prod')}")
private List<String> environments;

// Inline list
@Value("#{{'dev', 'test', 'prod'}}")
private List<String> inlineList;

// Inline map
@Value("#{{key1: 'value1', key2: 'value2'}}")
private Map<String, String> inlineMap;
```

#### 10. Elvis Operator (null-safe default)
```java
@Value("#{systemProperties['non.existent'] ?: 'default'}")
private String elvis;
```

#### 11. Safe Navigation Operator
```java
// Returns null if property doesn't exist (no exception)
@Value("#{systemProperties['non.existent']?.toUpperCase()}")
private String safeNav;
```

#### 12. Type References (T())
```java
// Access static methods/fields
@Value("#{T(java.lang.Math).PI}")
private double pi;

@Value("#{T(java.time.LocalDateTime).now()}")
private LocalDateTime now;

@Value("#{T(java.util.UUID).randomUUID().toString()}")
private String uuid;
```

**Xem code:** `SpELExamples.java`

---

## 🎯 PHẦN 4: ADVANCED SpEL

### Collection Operations

#### Filtering (.?[])
```java
// Filter elements where condition is true
@Value("#{T(java.util.Arrays).asList(1,2,3,4,5,6,7,8,9,10).?[#this > 5]}")
private List<Integer> filtered;  // [6, 7, 8, 9, 10]
```

#### Projection - First Match (.^[])
```java
// Get first element matching condition
@Value("#{T(java.util.Arrays).asList(1,2,3,4,5).^[#this > 2]}")
private Integer first;  // 3
```

#### Projection - Last Match (.$[])
```java
// Get last element matching condition
@Value("#{T(java.util.Arrays).asList(1,2,3,4,5).$[#this > 2]}")
private Integer last;  // 5
```

### Bean References
```java
// Reference another bean
@Value("#{@beanName}")
private MyBean bean;

// Access bean property
@Value("#{@beanName.propertyName}")
private String property;

// Call bean method
@Value("#{@beanName.methodName()}")
private String result;
```

### Complex Conditionals
```java
@Value("#{systemProperties['os.name'].toLowerCase().contains('windows') ? 'Windows' : 'Unix'}")
private String osType;

@Value("#{T(java.lang.System).getProperty('java.version').substring(0, 2)}")
private String javaMajorVersion;
```

**Xem code:** `AdvancedSpELExamples.java`

---

## 🎯 PHẦN 5: @CONFIGURATIONPROPERTIES (Type-safe Configuration)

### Vấn đề với @Value

```java
// Scattered properties
@Value("${db.url}")
private String dbUrl;

@Value("${db.username}")
private String dbUsername;

@Value("${db.password}")
private String dbPassword;

// Problems:
// - No type safety
// - No validation
// - Hard to test
// - No IDE autocomplete
```

### Giải pháp: @ConfigurationProperties

```java
@Configuration
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {
    
    @NotBlank
    private String name;
    
    @Min(1)
    @Max(1000)
    private int maxUsers;
    
    private Database database = new Database();
    
    @NotEmpty
    private List<String> allowedOrigins;
    
    private Map<String, String> features;
    
    // Getters and Setters
    
    public static class Database {
        private String url;
        private String username;
        private String password;
        private int poolSize;
        
        // Getters and Setters
    }
}
```

### Properties file
```properties
# application.properties
app.name=MyApp
app.max-users=100
app.database.url=jdbc:mysql://localhost:3306/db
app.database.username=root
app.database.password=secret
app.database.pool-size=10
app.allowed-origins=localhost,example.com
app.features.feature1=enabled
app.features.feature2=disabled
```

### Ưu điểm

✅ **Type-safe**: Compile-time checking
✅ **Validation**: JSR-303 validation support
✅ **Nested properties**: Organize related properties
✅ **Relaxed binding**: kebab-case, camelCase, snake_case all work
✅ **IDE support**: Autocomplete and documentation
✅ **Easy testing**: Just create POJO and set values

### Relaxed Binding

All of these work:
```properties
app.maxUsers=100
app.max-users=100
app.max_users=100
APP_MAX_USERS=100  # Environment variable
```

**Xem code:** `ConfigurationPropertiesExample.java`

---

## 🎯 PHẦN 6: PROPERTY OVERRIDE ORDER

### Thứ tự ưu tiên (cao → thấp)

```
1. Command line arguments
   --server.port=9000
   
2. Java System properties
   -Dserver.port=9000
   
3. OS environment variables
   SERVER_PORT=9000
   
4. Profile-specific properties (outside jar)
   file:./config/application-{profile}.properties
   
5. Profile-specific properties (inside jar)
   classpath:application-{profile}.properties
   
6. Application properties (outside jar)
   file:./config/application.properties
   
7. Application properties (inside jar)
   classpath:application.properties
   
8. @PropertySource
   
9. Default values in @Value
   @Value("${app.name:DefaultName}")
```

### Ví dụ thực tế

**application.properties**
```properties
app.name=MyApp
app.version=1.0.0
```

**application-dev.properties**
```properties
app.name=MyApp-DEV
```

**Command line**
```bash
java -jar app.jar --app.name=MyApp-Override
```

**Kết quả:** `app.name = "MyApp-Override"` (command line wins)

---

## ✅ CHECKLIST HOÀN THÀNH

### Properties Management
- [ ] Hiểu property sources và thứ tự ưu tiên
- [ ] Sử dụng @Value với default values
- [ ] Load properties từ nhiều files với @PropertySource
- [ ] Inject system properties và environment variables
- [ ] Hiểu relaxed binding

### Profiles
- [ ] Tạo được 3 profiles: dev, test, prod
- [ ] Sử dụng @Profile trên class và method
- [ ] Profile negation (!prod)
- [ ] Multiple profiles (dev | test)
- [ ] Tạo profile-specific properties files
- [ ] Kích hoạt profile bằng nhiều cách

### SpEL (10+ expressions)
- [ ] 1. Literal values (number, string, boolean)
- [ ] 2. System properties (systemProperties['key'])
- [ ] 3. Environment variables (systemEnvironment['KEY'])
- [ ] 4. Mathematical operations (+, -, *, /, %)
- [ ] 5. String operations (concat, substring, length, case)
- [ ] 6. Boolean logic (and, or, not, comparisons)
- [ ] 7. Conditional/Ternary (? :)
- [ ] 8. Regular expressions (matches)
- [ ] 9. Collections (Arrays.asList, inline list/map)
- [ ] 10. Elvis operator (?:)
- [ ] 11. Safe navigation (?.)
- [ ] 12. Type references T()

### Advanced SpEL
- [ ] Collection filtering (.?[])
- [ ] Collection projection (.^[], .$[])
- [ ] Bean references (@beanName)
- [ ] Method invocation
- [ ] Complex conditionals

### @ConfigurationProperties
- [ ] Tạo type-safe configuration class
- [ ] Nested properties
- [ ] Validation với JSR-303
- [ ] Relaxed binding
- [ ] So sánh với @Value

---

## 🚀 CÁCH CHẠY DEMO

### Demo với profile mặc định (dev)
```bash
mvn exec:java -Dexec.mainClass="com.example.spring_cert_notes.core.ProfileDemo"
```

### Demo với profile test
```bash
mvn exec:java -Dexec.mainClass="com.example.spring_cert_notes.core.ProfileDemo" \
  -Dspring.profiles.active=test
```

### Demo với profile prod
```bash
mvn exec:java -Dexec.mainClass="com.example.spring_cert_notes.core.ProfileDemo" \
  -Dspring.profiles.active=prod
```

### Override properties
```bash
mvn exec:java -Dexec.mainClass="com.example.spring_cert_notes.core.ProfileDemo" \
  -Dspring.profiles.active=dev \
  -Dapp.name="Custom Name"
```

---

## 📝 GHI CHÚ QUAN TRỌNG

### Best Practices

1. **Sử dụng @ConfigurationProperties thay vì @Value** cho complex configuration
   - Type-safe
   - Validation
   - Better organization

2. **Luôn có default values** trong @Value
   ```java
   @Value("${app.name:DefaultName}")  // ✅ Good
   @Value("${app.name}")              // ❌ Fails if not set
   ```

3. **Tổ chức properties theo prefix**
   ```properties
   # Good
   app.database.url=...
   app.database.username=...
   app.cache.enabled=...
   
   # Bad
   databaseUrl=...
   dbUsername=...
   cacheEnabled=...
   ```

4. **Không hardcode sensitive data**
   ```properties
   # Bad
   db.password=secret123
   
   # Good - use environment variables
   db.password=${DB_PASSWORD}
   ```

5. **Profile naming convention**
   - dev: Development
   - test: Testing/QA
   - staging: Pre-production
   - prod: Production

### Common Mistakes

1. ❌ Quên default value trong @Value
2. ❌ Không sử dụng profile cho môi trường khác nhau
3. ❌ Hardcode configuration trong code
4. ❌ Không validate properties
5. ❌ Sử dụng @Value cho complex configuration (nên dùng @ConfigurationProperties)
6. ❌ Không hiểu property override order
7. ❌ SpEL syntax errors (quên #{} hoặc ${})

### SpEL Tips

- `${}` = Property placeholder
- `#{}` = SpEL expression
- `#{'${property}'.toUpperCase()}` = Combine both
- `T()` = Type reference for static access
- `@beanName` = Bean reference
- `?.` = Safe navigation (null-safe)
- `?:` = Elvis operator (default value)
- `.?[]` = Filter collection
- `.^[]` = First match
- `.$[]` = Last match

### Property Override Examples

```bash
# Lowest priority
application.properties: app.name=MyApp

# Higher priority
application-dev.properties: app.name=MyApp-DEV

# Even higher
-Dapp.name=MyApp-System

# Highest priority
--app.name=MyApp-CommandLine
```

---

## 📚 TÀI LIỆU THAM KHẢO

### Files code trong package này:

**Properties & Profiles:**
1. `PropertySourcesConfig.java` - Multiple property sources
2. `DatabaseConfig.java` - Profile-specific configurations
3. `ConfigurationPropertiesExample.java` - Type-safe configuration
4. `ProfileDemo.java` - Main demo class

**SpEL:**
5. `SpELExamples.java` - 10+ common SpEL expressions
6. `AdvancedSpELExamples.java` - Advanced SpEL features

**Properties files:**
7. `application-dev.properties` - Dev profile
8. `application-test.properties` - Test profile
9. `application-prod.properties` - Prod profile
10. `custom.properties` - Custom property source

### Đọc thêm:
- Spring Boot Reference: Externalized Configuration
- Spring Framework Reference: SpEL
- Baeldung: Spring Profiles
- Baeldung: Spring @Value
