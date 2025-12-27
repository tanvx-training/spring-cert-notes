# SPRING BOOT
## Guideline Chi Tiết cho Spring Professional Certification

---

**Tài liệu ôn tập toàn diện về Spring Boot Framework**

*Tạo ngày: 26/12/2024*

---

## MỤC LỤC

1. [Giới thiệu về Spring Boot](#1-giới-thiệu-về-spring-boot)
2. [Spring Boot Architecture](#2-spring-boot-architecture)
3. [Spring Boot Application](#3-spring-boot-application)
4. [Auto-Configuration](#4-auto-configuration)
5. [Spring Boot Starters](#5-spring-boot-starters)
6. [Configuration Properties](#6-configuration-properties)
7. [Profiles](#7-profiles)
8. [Externalized Configuration](#8-externalized-configuration)
9. [Spring Boot Actuator](#9-spring-boot-actuator)
10. [Embedded Servers](#10-embedded-servers)
11. [Logging](#11-logging)
12. [DevTools](#12-devtools)
13. [Testing](#13-testing)
14. [Packaging và Deployment](#14-packaging-và-deployment)
15. [Spring Boot CLI](#15-spring-boot-cli)
16. [Monitoring và Management](#16-monitoring-và-management)
17. [Best Practices](#17-best-practices)
18. [Câu hỏi mẫu cho kỳ thi](#18-câu-hỏi-mẫu-cho-kỳ-thi)
19. [Tóm tắt và mẹo thi](#19-tóm-tắt-và-mẹo-thi)

---

## 1. GIỚI THIỆU VỀ SPRING BOOT

### 1.1. Spring Boot là gì?

**Spring Boot** là framework built on top of Spring Framework, giúp tạo production-ready Spring applications với minimal configuration.

**Core Philosophy:**
- **Convention over Configuration**
- **Opinionated defaults**
- **Production-ready out of the box**

### 1.2. Lợi ích của Spring Boot

```
┌────────────────────────────────────────┐
│      Spring Boot Advantages            │
├────────────────────────────────────────┤
│                                        │
│  ✅ Auto-configuration                 │
│  ✅ Embedded servers                   │
│  ✅ Starter dependencies               │
│  ✅ Production-ready features          │
│  ✅ No XML configuration               │
│  ✅ Simplified deployment              │
│  ✅ Metrics & health checks            │
│  ✅ Rapid development                  │
│                                        │
└────────────────────────────────────────┘
```

**Key Features:**
- ✅ Standalone applications
- ✅ Embedded Tomcat/Jetty/Undertow
- ✅ Opinionated 'starter' dependencies
- ✅ Auto-configuration
- ✅ Production-ready features (metrics, health checks)
- ✅ No code generation
- ✅ No XML configuration required

### 1.3. Spring vs Spring Boot

| Aspect | Spring Framework | Spring Boot |
|--------|------------------|-------------|
| **Configuration** | XML or Java Config | Auto-configuration |
| **Setup** | Manual | Convention-based |
| **Server** | External (Tomcat/JBoss) | Embedded |
| **Dependencies** | Manual management | Starter POMs |
| **Production Features** | Manual setup | Built-in (Actuator) |
| **Development Time** | Longer | Faster |
| **Learning Curve** | Steeper | Gentler |

---

## 2. SPRING BOOT ARCHITECTURE

### 2.1. Layered Architecture

```
┌─────────────────────────────────────────────┐
│        Spring Boot Application              │
├─────────────────────────────────────────────┤
│                                             │
│  ┌─────────────────────────────────────┐   │
│  │   Presentation Layer                │   │
│  │   (@Controller, @RestController)    │   │
│  └─────────────────────────────────────┘   │
│               ↓                             │
│  ┌─────────────────────────────────────┐   │
│  │   Business Layer                    │   │
│  │   (@Service)                        │   │
│  └─────────────────────────────────────┘   │
│               ↓                             │
│  ┌─────────────────────────────────────┐   │
│  │   Persistence Layer                 │   │
│  │   (@Repository)                     │   │
│  └─────────────────────────────────────┘   │
│               ↓                             │
│  ┌─────────────────────────────────────┐   │
│  │   Database                          │   │
│  └─────────────────────────────────────┘   │
│                                             │
└─────────────────────────────────────────────┘
```

### 2.2. Components

**Spring Boot Components:**
- **Spring Boot Starter**: Dependency descriptors
- **Spring Boot AutoConfigurator**: Automatic configuration
- **Spring Boot CLI**: Command-line interface
- **Spring Boot Actuator**: Production-ready features

---

## 3. SPRING BOOT APPLICATION

### 3.1. @SpringBootApplication

```java
@SpringBootApplication
public class Application {
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

**@SpringBootApplication combines:**
```java
@SpringBootApplication = 
    @Configuration +           // Marks as configuration class
    @EnableAutoConfiguration + // Enable auto-configuration
    @ComponentScan            // Enable component scanning
```

### 3.2. Detailed Annotations

```java
// Equivalent to @SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.example")
public class Application {
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

**@Configuration:**
- Indicates class contains @Bean definitions
- Spring processes class and generates Bean definitions

**@EnableAutoConfiguration:**
- Enables Spring Boot auto-configuration
- Attempts to guess and configure beans based on classpath

**@ComponentScan:**
- Scans for @Component, @Service, @Repository, @Controller
- Default: current package and sub-packages

### 3.3. Customizing SpringApplication

```java
@SpringBootApplication
public class Application {
    
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        
        // Set default properties
        app.setDefaultProperties(Collections.singletonMap(
            "server.port", "8081"
        ));
        
        // Add listeners
        app.addListeners(new ApplicationStartedListener());
        
        // Set banner mode
        app.setBannerMode(Banner.Mode.OFF);
        
        // Set additional profiles
        app.setAdditionalProfiles("dev");
        
        // Run
        app.run(args);
    }
}
```

### 3.4. Application Events

```java
@Component
public class ApplicationEventListeners {
    
    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        System.out.println("Context refreshed");
    }
    
    @EventListener
    public void handleContextStarted(ContextStartedEvent event) {
        System.out.println("Context started");
    }
    
    @EventListener
    public void handleContextStopped(ContextStoppedEvent event) {
        System.out.println("Context stopped");
    }
}

// Spring Boot specific events
@Component
public class SpringBootEventListeners implements ApplicationListener<ApplicationReadyEvent> {
    
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("Application is ready");
    }
}
```

**Event Sequence:**
1. `ApplicationStartingEvent`
2. `ApplicationEnvironmentPreparedEvent`
3. `ApplicationContextInitializedEvent`
4. `ApplicationPreparedEvent`
5. `ContextRefreshedEvent`
6. `ApplicationStartedEvent`
7. `ApplicationReadyEvent`
8. `ApplicationFailedEvent` (on failure)

### 3.5. CommandLineRunner và ApplicationRunner

```java
@Component
public class MyCommandLineRunner implements CommandLineRunner {
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("CommandLineRunner executed");
        System.out.println("Arguments: " + Arrays.toString(args));
    }
}

@Component
public class MyApplicationRunner implements ApplicationRunner {
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("ApplicationRunner executed");
        System.out.println("Option names: " + args.getOptionNames());
        System.out.println("Non-option args: " + args.getNonOptionArgs());
    }
}

// With order
@Component
@Order(1)
public class FirstRunner implements CommandLineRunner {
    @Override
    public void run(String... args) {
        System.out.println("First runner");
    }
}

@Component
@Order(2)
public class SecondRunner implements CommandLineRunner {
    @Override
    public void run(String... args) {
        System.out.println("Second runner");
    }
}
```

---

## 4. AUTO-CONFIGURATION

### 4.1. How Auto-Configuration Works

```
┌──────────────────────────────────────────────┐
│      Auto-Configuration Process              │
├──────────────────────────────────────────────┤
│                                              │
│  1. Check classpath for libraries           │
│  2. Check for beans already defined          │
│  3. Check properties/conditions              │
│  4. Configure beans if conditions met        │
│  5. Allow overriding with custom config      │
│                                              │
└──────────────────────────────────────────────┘
```

### 4.2. Auto-Configuration Example

```java
@Configuration
@ConditionalOnClass(DataSource.class)
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(DataSourceProperties properties) {
        return DataSourceBuilder.create()
            .url(properties.getUrl())
            .username(properties.getUsername())
            .password(properties.getPassword())
            .build();
    }
}
```

### 4.3. Conditional Annotations

```java
// Class-based conditions
@ConditionalOnClass(DataSource.class)        // If class exists
@ConditionalOnMissingClass("ClassName")      // If class doesn't exist

// Bean-based conditions
@ConditionalOnBean(DataSource.class)         // If bean exists
@ConditionalOnMissingBean                    // If bean doesn't exist
@ConditionalOnSingleCandidate(DataSource.class) // If single candidate bean

// Property-based conditions
@ConditionalOnProperty(name = "app.feature.enabled", havingValue = "true")
@ConditionalOnProperty(name = "app.feature.enabled", matchIfMissing = true)

// Resource-based conditions
@ConditionalOnResource(resources = "classpath:schema.sql")

// Expression-based conditions
@ConditionalOnExpression("${app.enabled:false} and ${app.mode} == 'production'")

// Web-based conditions
@ConditionalOnWebApplication              // If web application
@ConditionalOnNotWebApplication          // If not web application

// Cloud conditions
@ConditionalOnCloudPlatform(CloudPlatform.CLOUD_FOUNDRY)

// Java version
@ConditionalOnJava(JavaVersion.ELEVEN)
```

### 4.4. Excluding Auto-Configuration

```java
// Method 1: @SpringBootApplication
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
public class Application {
}

// Method 2: @EnableAutoConfiguration
@Configuration
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class
})
public class Application {
}

// Method 3: application.properties
spring.autoconfigure.exclude=\
  org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
  org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
```

### 4.5. Debug Auto-Configuration

```properties
# Enable debug mode
debug=true

# Or via command line
java -jar app.jar --debug
```

**Output shows:**
- **Positive matches**: Auto-configurations that were applied
- **Negative matches**: Auto-configurations that were not applied
- **Exclusions**: Explicitly excluded auto-configurations
- **Unconditional classes**: Always applied

---

## 5. SPRING BOOT STARTERS

### 5.1. Common Starters

| Starter | Purpose |
|---------|---------|
| **spring-boot-starter** | Core starter (logging, auto-config) |
| **spring-boot-starter-web** | Web applications (Spring MVC, Tomcat) |
| **spring-boot-starter-data-jpa** | JPA with Hibernate |
| **spring-boot-starter-data-jdbc** | JDBC |
| **spring-boot-starter-data-rest** | REST repositories |
| **spring-boot-starter-security** | Spring Security |
| **spring-boot-starter-test** | Testing (JUnit, Mockito, AssertJ) |
| **spring-boot-starter-actuator** | Production features |
| **spring-boot-starter-validation** | Bean Validation |
| **spring-boot-starter-thymeleaf** | Thymeleaf templating |
| **spring-boot-starter-cache** | Caching |
| **spring-boot-starter-mail** | Email |
| **spring-boot-starter-oauth2-client** | OAuth2 client |
| **spring-boot-starter-webflux** | Reactive web |
| **spring-boot-starter-batch** | Spring Batch |

### 5.2. Starter Example

```xml
<!-- Single starter includes multiple dependencies -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Includes:
    - spring-boot-starter
    - spring-boot-starter-json
    - spring-boot-starter-tomcat
    - spring-web
    - spring-webmvc
-->
```

### 5.3. Custom Starter

**Create custom starter:**

```xml
<!-- my-custom-spring-boot-starter -->
<project>
    <artifactId>my-custom-spring-boot-starter</artifactId>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>my-custom-autoconfigure</artifactId>
        </dependency>
    </dependencies>
</project>
```

**Auto-configuration:**

```java
@Configuration
@ConditionalOnClass(MyService.class)
@EnableConfigurationProperties(MyProperties.class)
public class MyAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public MyService myService(MyProperties properties) {
        return new MyService(properties);
    }
}
```

**META-INF/spring.factories:**

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.example.autoconfigure.MyAutoConfiguration
```

---

## 6. CONFIGURATION PROPERTIES

### 6.1. application.properties

```properties
# Server configuration
server.port=8080
server.servlet.context-path=/api

# DataSource
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=postgres
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Logging
logging.level.root=INFO
logging.level.com.example=DEBUG
logging.file.name=app.log

# Custom properties
app.name=My Application
app.version=1.0.0
app.features.feature1=true
app.features.feature2=false
```

### 6.2. application.yml

```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mydb
    username: postgres
    password: password
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

logging:
  level:
    root: INFO
    com.example: DEBUG
  file:
    name: app.log

app:
  name: My Application
  version: 1.0.0
  features:
    feature1: true
    feature2: false
```

### 6.3. @Value

```java
@Component
public class AppConfig {
    
    @Value("${app.name}")
    private String appName;
    
    @Value("${app.version}")
    private String version;
    
    @Value("${app.timeout:30}")
    private int timeout; // Default 30
    
    @Value("${app.servers}")
    private List<String> servers;
    
    @Value("#{${app.config}}")
    private Map<String, String> config;
}
```

### 6.4. @ConfigurationProperties

```java
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    
    private String name;
    private String version;
    private int timeout = 30;
    private List<String> servers = new ArrayList<>();
    private Map<String, String> config = new HashMap<>();
    private Features features = new Features();
    
    public static class Features {
        private boolean feature1;
        private boolean feature2;
        
        // Getters and setters
    }
    
    // Getters and setters
}

// Enable in configuration
@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class Config {
}

// Usage
@Service
public class AppService {
    
    @Autowired
    private AppProperties properties;
    
    public void doSomething() {
        System.out.println("App: " + properties.getName());
        System.out.println("Features: " + properties.getFeatures().isFeature1());
    }
}
```

### 6.5. @ConfigurationPropertiesScan

```java
@SpringBootApplication
@ConfigurationPropertiesScan("com.example.config")
public class Application {
    // Automatically scans and registers @ConfigurationProperties
}
```

### 6.6. Validation

```java
@Component
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {
    
    @NotBlank
    private String name;
    
    @Min(1)
    @Max(65535)
    private int port;
    
    @Email
    private String adminEmail;
    
    @Pattern(regexp = "^(dev|test|prod)$")
    private String environment;
    
    // Getters and setters
}
```

---

## 7. PROFILES

### 7.1. Profile Configuration

```properties
# application.properties (default)
app.name=My Application
app.environment=default

# application-dev.properties
app.environment=development
spring.datasource.url=jdbc:h2:mem:testdb

# application-prod.properties
app.environment=production
spring.datasource.url=jdbc:postgresql://prod-server/mydb
```

### 7.2. Activate Profiles

**Method 1: application.properties**
```properties
spring.profiles.active=dev
```

**Method 2: Command line**
```bash
java -jar app.jar --spring.profiles.active=dev

# Or
java -jar app.jar -Dspring.profiles.active=dev

# Multiple profiles
java -jar app.jar --spring.profiles.active=dev,debug
```

**Method 3: Environment variable**
```bash
export SPRING_PROFILES_ACTIVE=dev
```

**Method 4: Programmatically**
```java
SpringApplication app = new SpringApplication(Application.class);
app.setAdditionalProfiles("dev");
app.run(args);
```

### 7.3. @Profile Annotation

```java
@Configuration
@Profile("dev")
public class DevConfiguration {
    
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }
}

@Configuration
@Profile("prod")
public class ProdConfiguration {
    
    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://prod/mydb");
        return ds;
    }
}

// Multiple profiles
@Configuration
@Profile({"dev", "test"})
public class DevTestConfiguration {
}

// NOT profile
@Configuration
@Profile("!prod")
public class NonProdConfiguration {
}
```

### 7.4. Profile-specific Beans

```java
@Component
public class EmailService {
    
    @Bean
    @Profile("dev")
    public EmailSender mockEmailSender() {
        return new MockEmailSender();
    }
    
    @Bean
    @Profile("prod")
    public EmailSender realEmailSender() {
        return new SmtpEmailSender();
    }
}
```

### 7.5. Profile Groups

```java
@Configuration
public class ProfileConfig {
    
    // Define profile groups
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
```

```properties
# application.properties
spring.profiles.group.production=proddb,prodmq,prodcache
spring.profiles.group.development=devdb,devmq,devcache

# Activate group
spring.profiles.active=production
# Activates: proddb, prodmq, prodcache
```

---

## 8. EXTERNALIZED CONFIGURATION

### 8.1. Configuration Priority

Spring Boot loads properties in this order (later overrides earlier):

1. Default properties
2. `@PropertySource` on `@Configuration` classes
3. Config data (application.properties/yml)
4. Properties from `SPRING_APPLICATION_JSON`
5. Command line arguments
6. Properties from `ServletConfig` init parameters
7. Properties from `ServletContext` init parameters
8. JNDI attributes
9. Java System properties (`System.getProperties()`)
10. OS environment variables
11. RandomValuePropertySource
12. Profile-specific config files
13. Application properties outside jar
14. Application properties inside jar
15. `@PropertySource`
16. Default properties (SpringApplication.setDefaultProperties)

### 8.2. External Configuration Files

```bash
# Outside jar (higher priority)
./config/application.properties
./application.properties
classpath:/config/application.properties
classpath:/application.properties

# Profile-specific
./config/application-{profile}.properties
./application-{profile}.properties
```

### 8.3. Custom Config Location

```bash
java -jar app.jar --spring.config.location=classpath:/custom/,file:./custom/

# Or multiple locations
java -jar app.jar \
  --spring.config.location=\
  file:./config/,\
  file:./config/security/,\
  classpath:/config/
```

### 8.4. Environment Variables

```bash
# Environment variable
export SERVER_PORT=8081
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost/mydb

# Relaxed binding
export SERVER_PORT=8080          # server.port
export SERVERPORT=8080            # server.port  
export server.port=8080           # server.port
```

### 8.5. YAML Configuration

```yaml
# Single file with multiple profiles
spring:
  profiles:
    active: dev

---
spring:
  config:
    activate:
      on-profile: dev
  datasource:
    url: jdbc:h2:mem:testdb

---
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: jdbc:postgresql://prod/mydb
```

---

## 9. SPRING BOOT ACTUATOR

### 9.1. Enable Actuator

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### 9.2. Actuator Endpoints

| Endpoint | Description |
|----------|-------------|
| **/actuator** | Root, lists all endpoints |
| **/health** | Application health |
| **/info** | Application info |
| **/metrics** | Application metrics |
| **/env** | Environment properties |
| **/configprops** | Configuration properties |
| **/beans** | All beans |
| **/mappings** | Request mappings |
| **/threaddump** | Thread dump |
| **/heapdump** | Heap dump |
| **/loggers** | Loggers configuration |
| **/httptrace** | HTTP traces |
| **/auditevents** | Audit events |
| **/shutdown** | Shutdown application (POST) |

### 9.3. Configuration

```properties
# Expose endpoints
management.endpoints.web.exposure.include=*
management.endpoints.web.exposure.exclude=shutdown

# Or specific endpoints
management.endpoints.web.exposure.include=health,info,metrics

# Change base path
management.endpoints.web.base-path=/manage

# Change port
management.server.port=9090

# Health details
management.endpoint.health.show-details=always
management.endpoint.health.show-components=always

# Enable shutdown
management.endpoint.shutdown.enabled=true
```

### 9.4. Health Indicators

```java
@Component
public class CustomHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        // Custom health check logic
        boolean serviceUp = checkService();
        
        if (serviceUp) {
            return Health.up()
                .withDetail("service", "available")
                .withDetail("version", "1.0.0")
                .build();
        } else {
            return Health.down()
                .withDetail("service", "unavailable")
                .withDetail("error", "Service timeout")
                .build();
        }
    }
    
    private boolean checkService() {
        // Check external service
        return true;
    }
}

// Multiple health indicators
@Component
public class DatabaseHealthIndicator implements HealthIndicator {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    public Health health() {
        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(1)) {
                return Health.up()
                    .withDetail("database", "PostgreSQL")
                    .build();
            }
        } catch (Exception e) {
            return Health.down()
                .withException(e)
                .build();
        }
        return Health.down().build();
    }
}
```

### 9.5. Custom Metrics

```java
@Service
public class OrderService {
    
    private final Counter orderCounter;
    private final Timer orderProcessingTimer;
    
    public OrderService(MeterRegistry registry) {
        this.orderCounter = Counter.builder("orders.created")
            .description("Number of orders created")
            .tag("type", "online")
            .register(registry);
        
        this.orderProcessingTimer = Timer.builder("orders.processing.time")
            .description("Order processing time")
            .register(registry);
    }
    
    public Order createOrder(Order order) {
        return orderProcessingTimer.record(() -> {
            Order saved = orderRepository.save(order);
            orderCounter.increment();
            return saved;
        });
    }
}
```

### 9.6. Custom Info

```properties
# application.properties
info.app.name=My Application
info.app.version=1.0.0
info.app.description=Spring Boot application
info.app.developer=John Doe

# Maven info (automatic)
info.build.artifact=@project.artifactId@
info.build.name=@project.name@
info.build.version=@project.version@
```

```java
@Component
public class CustomInfoContributor implements InfoContributor {
    
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("custom", "Custom info value");
        builder.withDetail("timestamp", System.currentTimeMillis());
        builder.withDetail("environment", System.getenv());
    }
}
```

### 9.7. Security

```properties
# Require authentication for all endpoints
management.endpoints.web.exposure.include=*
spring.security.user.name=admin
spring.security.user.password=secret
```

```java
@Configuration
public class ActuatorSecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) 
            throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/actuator/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        
        return http.build();
    }
}
```

---

## 10. EMBEDDED SERVERS

### 10.1. Default Embedded Server

Spring Boot uses **Tomcat** by default when you include `spring-boot-starter-web`.

### 10.2. Switch to Jetty

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

### 10.3. Switch to Undertow

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-undertow</artifactId>
</dependency>
```

### 10.4. Server Configuration

```properties
# Port
server.port=8080
server.port=0  # Random port

# Context path
server.servlet.context-path=/api

# Session timeout
server.servlet.session.timeout=30m

# Compression
server.compression.enabled=true
server.compression.min-response-size=1024

# SSL
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=password
server.ssl.key-store-type=PKCS12

# Tomcat specific
server.tomcat.max-threads=200
server.tomcat.accept-count=100
server.tomcat.accesslog.enabled=true
```

### 10.5. Programmatic Configuration

```java
@Configuration
public class ServerConfig {
    
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> 
            tomcatCustomizer() {
        return factory -> {
            factory.setPort(8081);
            factory.setContextPath("/api");
            factory.addConnectorCustomizers(connector -> {
                connector.setMaxPostSize(10000000);
            });
        };
    }
}
```

---

## 11. LOGGING

### 11.1. Default Logging

Spring Boot uses **Logback** by default.

**Logging facade**: SLF4J
**Implementation**: Logback (default), Log4j2, or Java Util Logging

### 11.2. Configuration

```properties
# Logging level
logging.level.root=INFO
logging.level.com.example=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate=WARN

# Log file
logging.file.name=app.log
logging.file.path=/var/log

# Log pattern
logging.pattern.console=%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n

# Log file size and rotation
logging.logback.rollingpolicy.max-file-size=10MB
logging.logback.rollingpolicy.max-history=30
```

### 11.3. Usage in Code

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    
    public User createUser(User user) {
        log.debug("Creating user: {}", user.getName());
        
        try {
            User saved = userRepository.save(user);
            log.info("User created successfully: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Error creating user", e);
            throw e;
        }
    }
}

// Or with Lombok
@Service
@Slf4j
public class UserService {
    
    public User createUser(User user) {
        log.debug("Creating user: {}", user.getName());
        // ...
    }
}
```

### 11.4. Logback Configuration

**logback-spring.xml:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    
    <!-- Console appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <!-- File appender -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/app.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/app.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <!-- Profile-specific configuration -->
    <springProfile name="dev">
        <root level="DEBUG">
            <appender-ref ref="CONSOLE"/>
        </root>
    </springProfile>
    
    <springProfile name="prod">
        <root level="INFO">
            <appender-ref ref="FILE"/>
        </root>
    </springProfile>
    
    <!-- Package-specific loggers -->
    <logger name="com.example" level="DEBUG"/>
    <logger name="org.springframework.web" level="INFO"/>
    <logger name="org.hibernate" level="WARN"/>
    
</configuration>
```

---

## 12. DEVTOOLS

### 12.1. Enable DevTools

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

### 12.2. Features

**Auto-restart:**
- Automatically restarts application when files change
- Faster than full restart (only reloads changed classes)
- Monitors classpath resources

**LiveReload:**
- Embedded LiveReload server
- Auto-refresh browser when resources change
- Install browser extension for best experience

**Remote Development:**
- Remote debugging
- Remote update
- Remote application restart

### 12.3. Configuration

```properties
# Enable/disable restart
spring.devtools.restart.enabled=true

# Exclude patterns
spring.devtools.restart.exclude=static/**,public/**

# Additional paths to watch
spring.devtools.restart.additional-paths=/custom/path

# Trigger file
spring.devtools.restart.trigger-file=.trigger

# LiveReload
spring.devtools.livereload.enabled=true
spring.devtools.livereload.port=35729

# Remote debugging
spring.devtools.remote.secret=mysecret
```

### 12.4. Disabling in Production

DevTools automatically disables in production when:
- Application runs from `java -jar`
- Application runs from special classloader
- `spring.devtools.restart.enabled=false`

---

## 13. TESTING

### 13.1. Test Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- Includes:
    - JUnit 5
    - Spring Test & Spring Boot Test
    - AssertJ
    - Hamcrest
    - Mockito
    - JSONassert
    - JsonPath
-->
```

### 13.2. @SpringBootTest

```java
@SpringBootTest
class ApplicationTests {
    
    @Autowired
    private UserService userService;
    
    @Test
    void contextLoads() {
        assertNotNull(userService);
    }
    
    @Test
    void testCreateUser() {
        User user = new User("John");
        User saved = userService.create(user);
        assertNotNull(saved.getId());
    }
}

// With web environment
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class WebTests {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testGetUser() {
        ResponseEntity<User> response = restTemplate.getForEntity(
            "/api/users/1", User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
```

### 13.3. @WebMvcTest

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void testGetUser() throws Exception {
        User user = new User(1L, "John");
        when(userService.findById(1L)).thenReturn(user);
        
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"));
    }
}
```

### 13.4. @DataJpaTest

```java
@DataJpaTest
class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    void testFindByName() {
        User user = new User("John");
        entityManager.persist(user);
        entityManager.flush();
        
        User found = userRepository.findByName("John");
        assertEquals("John", found.getName());
    }
}
```

### 13.5. Test Configuration

```java
@TestConfiguration
public class TestConfig {
    
    @Bean
    @Primary
    public DataSource testDataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }
}

@SpringBootTest
@Import(TestConfig.class)
class MyTests {
    // Uses test configuration
}
```

### 13.6. @TestPropertySource

```java
@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "app.feature.enabled=true"
})
class ConfigTests {
    
    @Value("${app.feature.enabled}")
    private boolean featureEnabled;
    
    @Test
    void testConfig() {
        assertTrue(featureEnabled);
    }
}
```

---

## 14. PACKAGING VÀ DEPLOYMENT

### 14.1. Build Executable JAR

**Maven:**
```bash
mvn clean package

# Skip tests
mvn clean package -DskipTests

# Run
java -jar target/myapp-1.0.0.jar
```

**Gradle:**
```bash
./gradlew build

# Run
java -jar build/libs/myapp-1.0.0.jar
```

### 14.2. JAR Structure

```
myapp-1.0.0.jar
├── BOOT-INF
│   ├── classes                 # Application classes
│   │   ├── com
│   │   │   └── example
│   │   ├── application.properties
│   │   └── ...
│   └── lib                     # Dependencies
│       ├── spring-core.jar
│       ├── spring-boot.jar
│       └── ...
├── META-INF
│   ├── MANIFEST.MF
│   └── maven
└── org
    └── springframework
        └── boot
            └── loader          # Spring Boot loader
```

### 14.3. WAR Deployment

```java
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

**pom.xml:**
```xml
<packaging>war</packaging>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### 14.4. Docker

**Dockerfile:**

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/myapp-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Build and run:**
```bash
docker build -t myapp:1.0.0 .
docker run -p 8080:8080 myapp:1.0.0
```

**Layered Dockerfile (optimized):**

```dockerfile
FROM openjdk:17-jdk-slim as builder
WORKDIR /app
COPY target/myapp-1.0.0.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
```

### 14.5. Cloud Deployment

**Heroku:**
```bash
# Procfile
web: java -Dserver.port=$PORT -jar target/myapp-1.0.0.jar

# Deploy
heroku create myapp
git push heroku main
```

**AWS Elastic Beanstalk:**
```bash
# .ebextensions/environment.config
option_settings:
  aws:elasticbeanstalk:application:environment:
    SERVER_PORT: 5000
```

**Google Cloud Run:**
```bash
gcloud builds submit --tag gcr.io/PROJECT_ID/myapp
gcloud run deploy --image gcr.io/PROJECT_ID/myapp --platform managed
```

---

## 15. SPRING BOOT CLI

### 15.1. Installation

```bash
# SDKMAN
sdk install springboot

# Homebrew
brew tap spring-io/tap
brew install spring-boot

# Manual
wget https://repo.spring.io/release/org/springframework/boot/spring-boot-cli/...
unzip spring-boot-cli-*.zip
export PATH=$PATH:/path/to/spring-*/bin
```

### 15.2. Usage

```bash
# Create new project
spring init --dependencies=web,data-jpa myapp

# Run Groovy script
spring run app.groovy

# Test
spring test app.groovy

# Grab dependencies
spring grab app.groovy

# Create JAR
spring jar myapp.jar app.groovy
```

### 15.3. Groovy Script Example

**app.groovy:**
```groovy
@RestController
class HelloController {
    
    @GetMapping("/")
    String hello() {
        return "Hello from Spring Boot CLI!"
    }
}
```

Run:
```bash
spring run app.groovy
```

---

## 16. MONITORING VÀ MANAGEMENT

### 16.1. Prometheus Integration

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

```properties
management.endpoints.web.exposure.include=prometheus
management.metrics.export.prometheus.enabled=true
```

Access metrics: `http://localhost:8080/actuator/prometheus`

### 16.2. Grafana Dashboard

1. Install Prometheus
2. Configure Prometheus to scrape Spring Boot app
3. Install Grafana
4. Import Spring Boot dashboard (ID: 11378)

### 16.3. Application Properties Monitoring

```properties
# Enable specific metrics
management.metrics.enable.jvm=true
management.metrics.enable.process=true
management.metrics.enable.system=true
management.metrics.enable.http=true

# Export to various systems
management.metrics.export.prometheus.enabled=true
management.metrics.export.influx.enabled=true
management.metrics.export.graphite.enabled=true
```

### 16.4. Custom Metrics

```java
@Service
public class MetricsService {
    
    private final MeterRegistry meterRegistry;
    
    public MetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        // Gauge
        Gauge.builder("custom.gauge", this, MetricsService::getValue)
            .register(meterRegistry);
    }
    
    public void recordEvent() {
        // Counter
        meterRegistry.counter("custom.events").increment();
    }
    
    public void recordTime(long milliseconds) {
        // Timer
        meterRegistry.timer("custom.timer").record(milliseconds, TimeUnit.MILLISECONDS);
    }
    
    private double getValue() {
        return Math.random() * 100;
    }
}
```

---

## 17. BEST PRACTICES

### 17.1. Application Structure

```
com.example.myapp
├── MyApplication.java              # Main class
├── config                          # Configuration classes
│   ├── SecurityConfig.java
│   ├── WebConfig.java
│   └── DatabaseConfig.java
├── controller                      # Controllers
│   └── UserController.java
├── service                         # Business logic
│   ├── UserService.java
│   └── impl
│       └── UserServiceImpl.java
├── repository                      # Data access
│   └── UserRepository.java
├── model                          # Domain models
│   ├── entity
│   │   └── User.java
│   └── dto
│       └── UserDTO.java
├── exception                      # Custom exceptions
│   ├── UserNotFoundException.java
│   └── GlobalExceptionHandler.java
└── util                          # Utilities
    └── DateUtil.java
```

### 17.2. Configuration Management

✅ **DO:**
- Use `@ConfigurationProperties` for type-safe config
- Group related properties
- Use validation annotations
- Externalize configuration
- Use profiles appropriately

❌ **DON'T:**
- Hardcode values
- Mix configuration with business logic
- Use `@Value` for complex configurations
- Store secrets in properties files

```java
// ✅ GOOD
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {
    @NotBlank
    private String name;
    
    @Min(1)
    @Max(65535)
    private int port;
    
    private Database database = new Database();
    
    public static class Database {
        @NotBlank
        private String url;
        private int maxConnections = 10;
    }
}

// ❌ BAD
@Service
public class UserService {
    @Value("${db.url}")
    private String dbUrl;
    
    @Value("${db.username}")
    private String username;
    // Scattered configuration
}
```

### 17.3. Dependency Management

```xml
<!-- ✅ GOOD - Use starters -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- ❌ BAD - Manual dependencies -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <version>6.0.0</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>6.0.0</version>
</dependency>
<!-- Many more... -->
```

### 17.4. Error Handling

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal server error",
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

### 17.5. Security

```java
// ✅ Always use HTTPS in production
server.ssl.enabled=true

// ✅ Use Spring Security
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        // Configure security
    }
}

// ✅ Don't expose sensitive endpoints
management.endpoints.web.exposure.include=health,info

// ✅ Use secrets management
# Not in application.properties!
spring.datasource.password=${DB_PASSWORD}  # From environment
```

---

## 18. CÂU HỎI MẪU CHO KỲ THI

### 18.1. Câu hỏi lý thuyết

#### Câu 1: @SpringBootApplication bao gồm những annotation nào?

**Trả lời**: @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan

- **@Configuration**: Marks class as source of bean definitions
- **@EnableAutoConfiguration**: Enables auto-configuration
- **@ComponentScan**: Enables component scanning

---

#### Câu 2: Auto-configuration hoạt động như thế nào?

**Trả lời**: 
1. Spring Boot checks classpath for libraries
2. Checks if beans already defined
3. Checks conditions (@ConditionalOnClass, @ConditionalOnMissingBean, etc.)
4. Configures beans if all conditions met
5. Allows overriding with custom configuration

---

#### Câu 3: Sự khác biệt giữa @Value và @ConfigurationProperties?

**Trả lời**:

| Aspect | @Value | @ConfigurationProperties |
|--------|--------|-------------------------|
| **Use case** | Simple values | Complex hierarchical config |
| **Type safety** | No | Yes |
| **Validation** | No | Yes (@Validated) |
| **Relaxed binding** | No | Yes |
| **IDE support** | Limited | Good |
| **Recommended** | Simple cases | Complex config |

---

#### Câu 4: Làm thế nào để disable specific auto-configuration?

**Trả lời**:

**Method 1**: @SpringBootApplication
```java
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
```

**Method 2**: Properties
```properties
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```

---

#### Câu 5: DevTools có tính năng gì?

**Trả lời**:
- **Auto-restart**: Restart khi code thay đổi
- **LiveReload**: Auto-refresh browser
- **Property defaults**: Development-friendly defaults
- **Remote debugging**: Remote development support
- **Disabled in production**: Automatically disabled

---

### 18.2. Câu hỏi code-based

#### Câu 6: Code sau có vấn đề gì?

```java
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private String name;
    // Missing getters and setters
}
```

**Trả lời**: Missing getters and setters. @ConfigurationProperties requires them for property binding.

**Fix:**
```java
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private String name;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
```

---

#### Câu 7: Làm thế nào để run code sau khi application starts?

```java
@Component
public class StartupRunner implements CommandLineRunner {
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Application started!");
        // Initialization code
    }
}

// Or ApplicationRunner
@Component
public class MyApplicationRunner implements ApplicationRunner {
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Args: " + args.getOptionNames());
    }
}
```

---

#### Câu 8: Configure different DataSource cho dev và prod?

```java
@Configuration
@Profile("dev")
public class DevDataSourceConfig {
    
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }
}

@Configuration
@Profile("prod")
public class ProdDataSourceConfig {
    
    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(env.getProperty("spring.datasource.url"));
        return ds;
    }
}
```

---

### 18.3. Scenario-based Questions

#### Câu 9: Application startup rất chậm. Làm thế nào debug?

**Trả lời**:

1. Enable debug mode:
```properties
debug=true
```

2. Check auto-configuration report
3. Exclude unnecessary auto-configurations
4. Use lazy initialization:
```properties
spring.main.lazy-initialization=true
```

5. Check bean creation time with Actuator

---

#### Câu 10: Expose health và metrics endpoints với security?

```java
@Configuration
public class ActuatorSecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) 
            throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/actuator/info").permitAll()
                .requestMatchers("/actuator/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        
        return http.build();
    }
}
```

```properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=when-authorized
```

---

## 19. TÓM TẮT VÀ MẸO THI

### 19.1. Core Concepts Cheat Sheet

| Concept | Key Points |
|---------|-----------|
| **@SpringBootApplication** | @Configuration + @EnableAutoConfiguration + @ComponentScan |
| **Auto-configuration** | Conditional configuration based on classpath |
| **Starters** | Pre-configured dependency descriptors |
| **Properties** | application.properties/yml, profiles, externalized |
| **Actuator** | Production-ready features (health, metrics) |
| **Embedded Server** | Tomcat (default), Jetty, Undertow |
| **DevTools** | Auto-restart, LiveReload |
| **Profiles** | Environment-specific configuration |

### 19.2. Important Annotations

```java
// Core
@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan

// Configuration
@ConfigurationProperties
@EnableConfigurationProperties
@Value
@Profile

// Conditional
@ConditionalOnClass
@ConditionalOnMissingBean
@ConditionalOnProperty
@ConditionalOnWebApplication

// Testing
@SpringBootTest
@WebMvcTest
@DataJpaTest
@MockBean
@TestConfiguration

// Lifecycle
@PostConstruct
@PreDestroy
CommandLineRunner
ApplicationRunner
```

### 19.3. Configuration Priority

```
Highest Priority
    ↓
1. Command line args
2. SPRING_APPLICATION_JSON
3. ServletConfig init params
4. ServletContext init params
5. JNDI attributes
6. System properties
7. OS environment variables
8. Profile-specific outside jar
9. Profile-specific inside jar
10. application.properties outside jar
11. application.properties inside jar
12. @PropertySource
13. Default properties
    ↓
Lowest Priority
```

### 19.4. Common Pitfalls

❌ **Mistake 1**: Forgetting @EnableConfigurationProperties
```java
// BAD
@ConfigurationProperties(prefix = "app")
public class AppProperties { }

// GOOD
@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class Config { }
```

❌ **Mistake 2**: Wrong profile activation
```bash
# BAD
java -jar app.jar -spring.profiles.active=dev

# GOOD
java -jar app.jar --spring.profiles.active=dev
```

❌ **Mistake 3**: Exposing all Actuator endpoints
```properties
# BAD - Security risk!
management.endpoints.web.exposure.include=*

# GOOD
management.endpoints.web.exposure.include=health,info,metrics
```

### 19.5. Mẹo làm bài thi

1. ✅ **@SpringBootApplication** = 3 annotations combined
2. ✅ **Auto-configuration**: Conditional, can be excluded
3. ✅ **Starters**: Convenience dependency descriptors
4. ✅ **@ConfigurationProperties** > @Value for complex config
5. ✅ **Profiles**: Environment-specific configuration
6. ✅ **Actuator**: Production-ready features (health, metrics)
7. ✅ **Embedded servers**: Tomcat default, can switch to Jetty/Undertow
8. ✅ **DevTools**: Auto-restart, disabled in production
9. ✅ **CommandLineRunner**: Run code after startup
10. ✅ **Priority**: Command line > Environment > Properties file

### 19.6. Checklist ôn tập

- [ ] @SpringBootApplication và components
- [ ] Auto-configuration mechanism
- [ ] Conditional annotations
- [ ] Spring Boot starters
- [ ] Configuration properties (@Value vs @ConfigurationProperties)
- [ ] Profiles và activation
- [ ] Externalized configuration
- [ ] Configuration priority order
- [ ] Actuator endpoints
- [ ] Health indicators và metrics
- [ ] Embedded servers (Tomcat, Jetty, Undertow)
- [ ] Logging configuration
- [ ] DevTools features
- [ ] Testing annotations
- [ ] Packaging (JAR vs WAR)
- [ ] CommandLineRunner vs ApplicationRunner

### 19.7. Quick Reference

**Create Spring Boot app:**
```bash
spring init --dependencies=web,data-jpa myapp
```

**Run application:**
```bash
mvn spring-boot:run
./mvnw spring-boot:run

# With profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# With arguments
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

**Common properties:**
```properties
server.port=8080
spring.application.name=myapp
spring.profiles.active=dev
logging.level.root=INFO
management.endpoints.web.exposure.include=health,info
```

---

## KẾT LUẬN

Spring Boot là framework hiện đại nhất trong Spring ecosystem và là topic quan trọng nhất cho Spring Professional Certification. Để thành công:

- ✅ Hiểu auto-configuration mechanism
- ✅ Master configuration management
- ✅ Biết khi nào dùng profiles
- ✅ Understand Actuator endpoints
- ✅ Know testing strategies
- ✅ Familiar với deployment options

### Key Points:

> **Spring Boot = Convention over Configuration**
>
> Core principles:
> - Auto-configuration based on classpath
> - Opinionated defaults
> - Production-ready out of the box
> - Embedded servers
> - No XML configuration needed
> - Rapid development

**Essential Components:**
```
@SpringBootApplication
    ↓
Auto-configuration (Conditional)
    ↓
Starters (Dependencies)
    ↓
Properties (Configuration)
    ↓
Actuator (Monitoring)
    ↓
Production Ready!
```

**Development Workflow:**
1. Create project (Spring Initializr)
2. Add starters (dependencies)
3. Configure properties
4. Write code (@RestController, @Service, @Repository)
5. Test (@SpringBootTest)
6. Package (JAR/WAR)
7. Deploy (Cloud/Container)

Hãy thực hành với các examples trong tài liệu này. Spring Boot simplifies Spring development significantly và là must-know cho modern Java developers!

---

**Chúc bạn thành công với Spring Professional Certification!** 🚀🎓

*Tài liệu được tạo ngày 26/12/2024*
