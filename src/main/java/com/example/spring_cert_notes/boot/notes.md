# Spring Boot Essentials

## 📚 Mục Lục
1. [Spring Boot Starters](#1-spring-boot-starters)
2. [Auto-Configuration](#2-auto-configuration)
3. [@ConfigurationProperties](#3-configurationproperties)
4. [Profiles và Properties](#4-profiles-và-properties)
5. [Actuator](#5-actuator)
6. [Debug Auto-Configuration](#6-debug-auto-configuration)

---

## 1. Spring Boot Starters

### 1.1 Starter là gì?

Starters là dependency descriptors - một dependency kéo theo nhiều dependencies liên quan.

```xml
<!-- Một starter thay thế nhiều dependencies -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### 1.2 Top 10 Starters Phổ Biến

| Starter | Mô tả | Dependencies chính |
|---------|-------|-------------------|
| `spring-boot-starter-web` | Web applications | Spring MVC, Tomcat, Jackson |
| `spring-boot-starter-data-jpa` | JPA/Hibernate | Spring Data JPA, Hibernate, HikariCP |
| `spring-boot-starter-security` | Security | Spring Security |
| `spring-boot-starter-test` | Testing | JUnit 5, Mockito, AssertJ |
| `spring-boot-starter-actuator` | Monitoring | Health, Metrics, Info |
| `spring-boot-starter-validation` | Validation | Hibernate Validator |
| `spring-boot-starter-aop` | AOP | Spring AOP, AspectJ |
| `spring-boot-starter-cache` | Caching | Spring Cache |
| `spring-boot-starter-mail` | Email | JavaMail |
| `spring-boot-starter-thymeleaf` | Templates | Thymeleaf |

### 1.3 Naming Convention

```
Official:     spring-boot-starter-*
Third-party:  *-spring-boot-starter

Ví dụ:
- spring-boot-starter-web (official)
- mybatis-spring-boot-starter (third-party)
```

### 1.4 Dependency Management

```xml
<!-- Parent POM quản lý versions -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.7</version>
</parent>

<!-- Hoặc sử dụng BOM -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>3.5.7</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---

## 2. Auto-Configuration

### 2.1 Cách Auto-Configuration Hoạt Động

```
1. @SpringBootApplication bao gồm @EnableAutoConfiguration
2. Spring Boot scan META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
3. Mỗi auto-configuration class có @Conditional annotations
4. Beans được tạo nếu conditions thỏa mãn
```

### 2.2 @Conditional Annotations

```java
// Class-based conditions
@ConditionalOnClass(DataSource.class)        // Class tồn tại trên classpath
@ConditionalOnMissingClass("com.mongodb...")  // Class KHÔNG tồn tại

// Bean-based conditions
@ConditionalOnBean(DataSource.class)          // Bean đã tồn tại
@ConditionalOnMissingBean(MyService.class)    // Bean KHÔNG tồn tại

// Property-based conditions
@ConditionalOnProperty(
    prefix = "feature",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true
)

// Resource-based conditions
@ConditionalOnResource(resources = "classpath:config.xml")

// Web application conditions
@ConditionalOnWebApplication
@ConditionalOnNotWebApplication
@ConditionalOnWebApplication(type = Type.SERVLET)

// Expression-based conditions
@ConditionalOnExpression("${feature.enabled:false}")

// Java version conditions
@ConditionalOnJava(range = Range.EQUAL_OR_NEWER, value = JavaVersion.SEVENTEEN)
```

### 2.3 Custom Auto-Configuration

```java
@AutoConfiguration
@EnableConfigurationProperties(MyProperties.class)
@ConditionalOnProperty(prefix = "my", name = "enabled", havingValue = "true")
public class MyAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public MyService myService(MyProperties properties) {
        return new DefaultMyService(properties);
    }
}
```

**Đăng ký Auto-Configuration:**
```
# META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
com.example.MyAutoConfiguration
```

### 2.4 Custom Condition

```java
public class OnProductionCondition implements Condition {
    
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String env = context.getEnvironment().getProperty("app.environment");
        return "production".equalsIgnoreCase(env);
    }
}

// Sử dụng
@Bean
@Conditional(OnProductionCondition.class)
public ProductionService productionService() {
    return new ProductionService();
}
```

---

## 3. @ConfigurationProperties

### 3.1 Basic Usage

```java
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {
    
    @NotBlank
    private String name;
    
    @Min(1) @Max(1000)
    private int maxConnections = 100;
    
    private Duration timeout = Duration.ofSeconds(30);
    
    private List<String> servers = new ArrayList<>();
    
    private Map<String, Boolean> features = new HashMap<>();
    
    private Server server = new Server();
    
    // Nested class
    public static class Server {
        private String host = "localhost";
        private int port = 8080;
        // getters/setters
    }
    
    // getters/setters
}
```

**Properties file:**
```properties
app.name=My Application
app.max-connections=50
app.timeout=60s
app.servers[0]=server1.example.com
app.servers[1]=server2.example.com
app.features.caching=true
app.features.logging=true
app.server.host=localhost
app.server.port=8080
```

### 3.2 Immutable Configuration (Constructor Binding)

```java
@ConfigurationProperties(prefix = "mail")
public class MailProperties {
    
    private final String host;
    private final int port;
    private final boolean ssl;
    
    @ConstructorBinding
    public MailProperties(
            @DefaultValue("smtp.example.com") String host,
            @DefaultValue("587") int port,
            @DefaultValue("true") boolean ssl) {
        this.host = host;
        this.port = port;
        this.ssl = ssl;
    }
    
    // Only getters - immutable
    public String getHost() { return host; }
    public int getPort() { return port; }
    public boolean isSsl() { return ssl; }
}
```

### 3.3 Enable Configuration Properties

```java
// Option 1: @EnableConfigurationProperties
@Configuration
@EnableConfigurationProperties({AppProperties.class, MailProperties.class})
public class PropertiesConfig { }

// Option 2: @ConfigurationPropertiesScan (on main class)
@SpringBootApplication
@ConfigurationPropertiesScan("com.example.properties")
public class Application { }
```

### 3.4 Property Binding Rules

| Java | Properties |
|------|------------|
| `firstName` | `first-name` (kebab-case) |
| `firstName` | `firstName` (camelCase) |
| `firstName` | `first_name` (underscore) |
| `firstName` | `FIRST_NAME` (uppercase) |

**Duration formats:**
```properties
app.timeout=30s      # 30 seconds
app.timeout=5m       # 5 minutes
app.timeout=1h       # 1 hour
app.timeout=PT30S    # ISO-8601 format
```

**DataSize formats:**
```properties
app.max-size=10MB
app.max-size=1GB
```

---

## 4. Profiles và Properties

### 4.1 Properties Hierarchy (Ưu tiên cao → thấp)

```
1. Command line arguments (--property=value)
2. SPRING_APPLICATION_JSON
3. ServletConfig/ServletContext parameters
4. JNDI attributes
5. Java System properties (-Dproperty=value)
6. OS environment variables
7. RandomValuePropertySource (random.*)
8. Profile-specific properties (application-{profile}.properties)
9. Application properties (application.properties)
10. @PropertySource annotations
11. Default properties
```

### 4.2 Profile-Specific Properties

```
application.properties          # Base properties
application-dev.properties      # Development
application-test.properties     # Testing
application-prod.properties     # Production
```

**Activate profile:**
```bash
# Command line
java -jar app.jar --spring.profiles.active=dev

# Environment variable
export SPRING_PROFILES_ACTIVE=dev

# application.properties
spring.profiles.active=dev
```

### 4.3 Profile-Specific Beans

```java
@Bean
@Profile("dev")
public DataSource devDataSource() {
    return new H2DataSource();
}

@Bean
@Profile("prod")
public DataSource prodDataSource() {
    return new PostgresDataSource();
}

@Bean
@Profile("!prod")  // NOT production
public String devMarker() {
    return "Development mode";
}

@Bean
@Profile({"dev", "test"})  // dev OR test
public String devOrTestMarker() {
    return "Dev or Test mode";
}
```

### 4.4 @Value Annotation

```java
@Value("${app.name}")
private String appName;

@Value("${app.name:Default Name}")  // With default
private String appNameWithDefault;

@Value("${app.servers}")  // List
private List<String> servers;

@Value("#{${app.features}}")  // Map (SpEL)
private Map<String, Boolean> features;

@Value("${random.int}")  // Random value
private int randomInt;

@Value("${random.uuid}")  // Random UUID
private String randomUuid;
```

---

## 5. Actuator

### 5.1 Endpoints

| Endpoint | Mô tả |
|----------|-------|
| `/actuator/health` | Application health |
| `/actuator/info` | Application info |
| `/actuator/metrics` | Metrics |
| `/actuator/env` | Environment properties |
| `/actuator/beans` | All beans |
| `/actuator/mappings` | Request mappings |
| `/actuator/loggers` | Logger levels |
| `/actuator/conditions` | Auto-config conditions |
| `/actuator/configprops` | @ConfigurationProperties |
| `/actuator/threaddump` | Thread dump |
| `/actuator/heapdump` | Heap dump |
| `/actuator/shutdown` | Shutdown (disabled) |

### 5.2 Configuration

```properties
# Expose endpoints
management.endpoints.web.exposure.include=health,info,metrics
management.endpoints.web.exposure.include=*
management.endpoints.web.exposure.exclude=env,beans

# Health details
management.endpoint.health.show-details=always
management.endpoint.health.show-components=always

# Custom base path
management.endpoints.web.base-path=/manage

# Custom port
management.server.port=8081

# Info endpoint
management.info.env.enabled=true
info.app.name=My App
info.app.version=1.0.0
```

### 5.3 Custom Health Indicator

```java
@Component("myService")
public class MyHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        boolean serviceUp = checkService();
        
        if (serviceUp) {
            return Health.up()
                .withDetail("service", "My Service")
                .withDetail("status", "Running")
                .build();
        } else {
            return Health.down()
                .withDetail("error", "Service unavailable")
                .build();
        }
    }
}
```

### 5.4 Custom Info Contributor

```java
@Component
public class CustomInfoContributor implements InfoContributor {
    
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("custom", Map.of(
            "version", "1.0.0",
            "author", "Developer"
        ));
    }
}
```

---

## 6. Debug Auto-Configuration

### 6.1 Debug Mode

```bash
# Command line
java -jar app.jar --debug

# Properties
debug=true

# Logging
logging.level.org.springframework.boot.autoconfigure=DEBUG
```

### 6.2 Conditions Report

```
============================
CONDITIONS EVALUATION REPORT
============================

Positive matches:
-----------------
   DataSourceAutoConfiguration matched:
      - @ConditionalOnClass found required classes 'javax.sql.DataSource'

Negative matches:
-----------------
   MongoAutoConfiguration:
      - @ConditionalOnClass did not find required class 'com.mongodb.client.MongoClient'

Exclusions:
-----------
   org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
```

### 6.3 Actuator Conditions Endpoint

```bash
curl http://localhost:8080/actuator/conditions
```

### 6.4 Exclude Auto-Configuration

```java
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    SecurityAutoConfiguration.class
})
public class Application { }
```

```properties
spring.autoconfigure.exclude=\
  org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
  org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
```

---

## 📁 Cấu Trúc Files

```
src/main/java/com/example/spring_cert_notes/boot/
├── properties/
│   ├── AppProperties.java           # @ConfigurationProperties demo
│   └── MailProperties.java          # Immutable properties
├── config/
│   └── PropertiesConfig.java        # Enable properties
├── autoconfigure/
│   ├── ConditionalExamples.java     # @Conditional annotations
│   ├── CustomCondition.java         # Custom Condition
│   ├── OnProductionCondition.java   # SpringBootCondition
│   └── greeting/
│       ├── GreetingAutoConfiguration.java
│       ├── GreetingProperties.java
│       ├── GreetingService.java
│       └── DefaultGreetingService.java
├── profiles/
│   ├── ProfilesDemo.java            # @Profile demo
│   └── ProfileService.java          # Environment access
├── starters/
│   └── StartersOverview.java        # Starters documentation
├── actuator/
│   ├── ActuatorDemo.java            # Actuator overview
│   ├── CustomHealthIndicator.java   # Custom health check
│   ├── DatabaseHealthIndicator.java # Database health
│   └── CustomInfoContributor.java   # Custom info
└── BootDemo.java                    # Demo runner
```

---

## 🧪 Chạy Demo

```bash
# Chạy với boot profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=boot

# Debug auto-configuration
./mvnw spring-boot:run -Dspring-boot.run.arguments=--debug

# Test actuator endpoints
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/info
curl http://localhost:8080/actuator/conditions
```

---

## 🎯 Checklist

- [ ] Giải thích được 5 Spring Boot starters phổ biến
- [ ] Hiểu cách auto-configuration hoạt động
- [ ] Sử dụng @ConfigurationProperties với validation
- [ ] Tạo immutable configuration với constructor binding
- [ ] Hiểu properties hierarchy và profiles
- [ ] Sử dụng @Conditional annotations
- [ ] Tạo custom auto-configuration
- [ ] Debug auto-configuration với --debug
- [ ] Tạo custom Health Indicator
- [ ] Tạo custom Info Contributor
