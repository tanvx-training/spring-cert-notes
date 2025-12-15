# Spring Boot Actuator

## 📚 Mục Lục
1. [Tổng Quan Actuator](#1-tổng-quan-actuator)
2. [Actuator Endpoints](#2-actuator-endpoints)
3. [Custom Health Indicators](#3-custom-health-indicators)
4. [Custom Metrics với Micrometer](#4-custom-metrics-với-micrometer)
5. [Custom Endpoints](#5-custom-endpoints)
6. [Secure Actuator](#6-secure-actuator)
7. [Best Practices](#7-best-practices)

---

## 1. Tổng Quan Actuator

### 1.1 Actuator là gì?

Spring Boot Actuator cung cấp production-ready features:
- Health checks
- Metrics collection
- Application info
- Environment properties
- Logging configuration
- HTTP request tracing

### 1.2 Dependency

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### 1.3 Basic Configuration

```properties
# Expose endpoints
management.endpoints.web.exposure.include=health,info,metrics

# Health details
management.endpoint.health.show-details=always

# Info endpoint
management.info.env.enabled=true
```

---

## 2. Actuator Endpoints

### 2.1 Built-in Endpoints

| Endpoint | Mô tả | Default |
|----------|-------|---------|
| `/actuator/health` | Application health | Enabled |
| `/actuator/info` | Application info | Enabled |
| `/actuator/metrics` | Metrics data | Enabled |
| `/actuator/env` | Environment properties | Disabled |
| `/actuator/beans` | All beans | Disabled |
| `/actuator/mappings` | Request mappings | Disabled |
| `/actuator/loggers` | Logger configuration | Disabled |
| `/actuator/configprops` | @ConfigurationProperties | Disabled |
| `/actuator/conditions` | Auto-config conditions | Disabled |
| `/actuator/threaddump` | Thread dump | Disabled |
| `/actuator/heapdump` | Heap dump | Disabled |
| `/actuator/shutdown` | Shutdown app | Disabled |
| `/actuator/prometheus` | Prometheus metrics | Disabled |

### 2.2 Endpoint Exposure

```properties
# Expose all endpoints
management.endpoints.web.exposure.include=*

# Expose specific endpoints
management.endpoints.web.exposure.include=health,info,metrics,loggers

# Exclude endpoints
management.endpoints.web.exposure.exclude=env,beans

# Enable/disable specific endpoint
management.endpoint.shutdown.enabled=true
```

### 2.3 Health Endpoint

```properties
# Show details
management.endpoint.health.show-details=always
# Options: never, when-authorized, always

# Show components
management.endpoint.health.show-components=always

# Health groups
management.endpoint.health.group.liveness.include=livenessState
management.endpoint.health.group.readiness.include=readinessState,db
```

**Health Response:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "H2",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 500107862016,
        "free": 300000000000
      }
    }
  }
}
```

### 2.4 Metrics Endpoint

```bash
# List all metrics
curl http://localhost:8080/actuator/metrics

# Get specific metric
curl http://localhost:8080/actuator/metrics/jvm.memory.used

# With tags
curl http://localhost:8080/actuator/metrics/http.server.requests?tag=uri:/api/users
```

---

## 3. Custom Health Indicators

### 3.1 Basic Health Indicator

```java
@Component("myService")
public class MyHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        boolean isHealthy = checkService();
        
        if (isHealthy) {
            return Health.up()
                .withDetail("service", "My Service")
                .withDetail("status", "Available")
                .build();
        }
        
        return Health.down()
            .withDetail("error", "Service unavailable")
            .build();
    }
}
```

### 3.2 Health Status Levels

```java
Health.up()           // UP - healthy
Health.down()         // DOWN - unhealthy
Health.outOfService() // OUT_OF_SERVICE
Health.unknown()      // UNKNOWN
Health.status("DEGRADED") // Custom status
```

### 3.3 Health with Exception

```java
@Override
public Health health() {
    try {
        checkService();
        return Health.up().build();
    } catch (Exception e) {
        return Health.down()
            .withDetail("error", e.getMessage())
            .withException(e)
            .build();
    }
}
```

### 3.4 Composite Health Indicator

```java
@Component("infrastructure")
public class InfrastructureHealth implements CompositeHealthContributor {
    
    private final Map<String, HealthContributor> contributors = new HashMap<>();
    
    public InfrastructureHealth() {
        contributors.put("cache", new CacheHealthIndicator());
        contributors.put("queue", new QueueHealthIndicator());
    }
    
    @Override
    public HealthContributor getContributor(String name) {
        return contributors.get(name);
    }
    
    @Override
    public Iterator<NamedContributor<HealthContributor>> iterator() {
        return contributors.entrySet().stream()
            .map(e -> NamedContributor.of(e.getKey(), e.getValue()))
            .iterator();
    }
}
```

---

## 4. Custom Metrics với Micrometer

### 4.1 Metric Types

| Type | Mô tả | Use Case |
|------|-------|----------|
| Counter | Đếm (chỉ tăng) | Requests, errors |
| Gauge | Giá trị hiện tại | Active users, queue size |
| Timer | Đo thời gian | Response time |
| Distribution Summary | Phân phối giá trị | Request sizes |

### 4.2 Counter

```java
@Component
public class BusinessMetrics {
    
    private final Counter loginCounter;
    
    public BusinessMetrics(MeterRegistry registry) {
        this.loginCounter = Counter.builder("user.logins")
            .description("Total user logins")
            .tag("type", "authentication")
            .register(registry);
    }
    
    public void recordLogin(String username) {
        loginCounter.increment();
        
        // Dynamic tags
        meterRegistry.counter("user.logins.detailed",
            "username", username,
            "method", "password"
        ).increment();
    }
}
```

### 4.3 Gauge

```java
private final AtomicInteger activeUsers = new AtomicInteger(0);

public BusinessMetrics(MeterRegistry registry) {
    Gauge.builder("users.active", activeUsers, AtomicInteger::get)
        .description("Active users")
        .register(registry);
}

public void userLoggedIn() {
    activeUsers.incrementAndGet();
}

public void userLoggedOut() {
    activeUsers.decrementAndGet();
}
```

### 4.4 Timer

```java
private final Timer orderTimer;

public BusinessMetrics(MeterRegistry registry) {
    this.orderTimer = Timer.builder("order.processing.time")
        .description("Order processing time")
        .register(registry);
}

// Option 1: Record runnable
public void processOrder() {
    orderTimer.record(() -> {
        // processing logic
    });
}

// Option 2: Manual timing
public void processOrder() {
    Timer.Sample sample = Timer.start(registry);
    try {
        // processing logic
    } finally {
        sample.stop(orderTimer);
    }
}
```

### 4.5 @Timed Annotation

```java
// Enable @Timed
@Bean
public TimedAspect timedAspect(MeterRegistry registry) {
    return new TimedAspect(registry);
}

// Use on methods
@Timed(value = "user.login.time", description = "Login time")
public void login(String username) {
    // login logic
}
```

### 4.6 Distribution Summary

```java
private final DistributionSummary orderAmount;

public BusinessMetrics(MeterRegistry registry) {
    this.orderAmount = DistributionSummary.builder("order.amount")
        .description("Order amounts")
        .baseUnit("dollars")
        .publishPercentiles(0.5, 0.75, 0.95, 0.99)
        .register(registry);
}

public void recordOrder(double amount) {
    orderAmount.record(amount);
}
```

### 4.7 Common Tags

```java
@Bean
public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
    return registry -> registry.config()
        .commonTags("application", "my-app")
        .commonTags("environment", "production");
}
```

---

## 5. Custom Endpoints

### 5.1 @Endpoint

```java
@Component
@Endpoint(id = "features")
public class FeaturesEndpoint {
    
    @ReadOperation  // GET
    public Map<String, Object> getFeatures() {
        return Map.of("feature1", true, "feature2", false);
    }
    
    @ReadOperation  // GET with parameter
    public Feature getFeature(@Selector String name) {
        return features.get(name);
    }
    
    @WriteOperation  // POST
    public Feature setFeature(@Selector String name, boolean enabled) {
        return features.put(name, new Feature(name, enabled));
    }
    
    @DeleteOperation  // DELETE
    public void deleteFeature(@Selector String name) {
        features.remove(name);
    }
}
```

### 5.2 @RestControllerEndpoint

```java
@Component
@RestControllerEndpoint(id = "custom-api")
public class CustomApiEndpoint {
    
    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getStatus() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
    
    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> process(@RequestBody Map<String, Object> input) {
        return ResponseEntity.ok(Map.of("processed", input));
    }
}
```

---

## 6. Secure Actuator

### 6.1 Security Configuration

```java
@Configuration
public class ActuatorSecurityConfig {
    
    @Bean
    @Order(1)
    public SecurityFilterChain actuatorSecurity(HttpSecurity http) throws Exception {
        http
            .securityMatcher(EndpointRequest.toAnyEndpoint())
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers(EndpointRequest.to("health", "info")).permitAll()
                
                // Authenticated endpoints
                .requestMatchers(EndpointRequest.to("metrics")).authenticated()
                
                // Admin only endpoints
                .requestMatchers(EndpointRequest.to("env", "beans", "shutdown"))
                    .hasRole("ADMIN")
                
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        
        return http.build();
    }
}
```

### 6.2 Properties-based Security

```properties
# Show health details only when authorized
management.endpoint.health.show-details=when-authorized

# Require specific role
management.endpoint.health.roles=ADMIN,ACTUATOR
```

### 6.3 Separate Port

```properties
# Run actuator on different port
management.server.port=8081

# Bind to localhost only
management.server.address=127.0.0.1
```

---

## 7. Best Practices

### 7.1 Production Configuration

```properties
# Only expose necessary endpoints
management.endpoints.web.exposure.include=health,info,metrics,prometheus

# Health details for authorized users only
management.endpoint.health.show-details=when-authorized

# Disable dangerous endpoints
management.endpoint.shutdown.enabled=false

# Use separate port
management.server.port=8081
management.server.address=127.0.0.1
```

### 7.2 Health Check Guidelines

```java
// ✅ DO: Check actual connectivity
@Override
public Health health() {
    try (Connection conn = dataSource.getConnection()) {
        return Health.up().build();
    } catch (Exception e) {
        return Health.down().withException(e).build();
    }
}

// ❌ DON'T: Just return UP without checking
@Override
public Health health() {
    return Health.up().build(); // Bad!
}
```

### 7.3 Metrics Guidelines

```java
// ✅ DO: Use meaningful names and tags
meterRegistry.counter("orders.created",
    "type", orderType,
    "region", region
).increment();

// ❌ DON'T: Use generic names
meterRegistry.counter("counter1").increment(); // Bad!
```

---

## 📁 Cấu Trúc Files

```
src/main/java/com/example/spring_cert_notes/actuator/
├── health/
│   ├── ExternalServiceHealthIndicator.java
│   ├── DatabaseHealthIndicator.java
│   ├── MemoryHealthIndicator.java
│   ├── DiskSpaceHealthIndicator.java
│   └── CompositeHealthIndicator.java
├── metrics/
│   ├── BusinessMetrics.java
│   └── HttpMetrics.java
├── info/
│   └── CustomInfoContributor.java
├── endpoint/
│   ├── CustomEndpoint.java
│   └── WebCustomEndpoint.java
├── config/
│   ├── MetricsConfig.java
│   └── ActuatorSecurityConfig.java
├── service/
│   └── MetricsDemoService.java
├── controller/
│   └── ActuatorDemoController.java
└── notes.md
```

---

## 🧪 Test Endpoints

```bash
# Activate actuator profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=actuator

# Health check
curl http://localhost:8080/actuator/health

# Health with details
curl http://localhost:8080/actuator/health | jq

# Info
curl http://localhost:8080/actuator/info

# Metrics list
curl http://localhost:8080/actuator/metrics

# Specific metric
curl http://localhost:8080/actuator/metrics/jvm.memory.used

# Custom endpoint
curl http://localhost:8080/actuator/features

# Demo API - Login
curl -X POST "http://localhost:8080/api/actuator-demo/login?username=john"

# Demo API - Create order
curl -X POST "http://localhost:8080/api/actuator-demo/order?productType=electronics&amount=99.99"

# Check custom metrics
curl http://localhost:8080/actuator/metrics/user.logins
curl http://localhost:8080/actuator/metrics/orders.created
```

---

## 🎯 Checklist

- [ ] Configure 5+ actuator endpoints
- [ ] Tạo custom health indicator cho database
- [ ] Tạo custom health indicator cho external API
- [ ] Tạo custom health indicator cho memory
- [ ] Implement Counter metrics
- [ ] Implement Gauge metrics
- [ ] Implement Timer metrics
- [ ] Sử dụng @Timed annotation
- [ ] Tạo custom actuator endpoint
- [ ] Secure actuator endpoints
- [ ] Tạo custom InfoContributor
