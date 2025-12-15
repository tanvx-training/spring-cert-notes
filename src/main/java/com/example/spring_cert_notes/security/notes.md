# Spring Security

## 📚 Mục Lục
1. [Tổng Quan Spring Security](#1-tổng-quan-spring-security)
2. [Authentication Methods](#2-authentication-methods)
3. [Authorization](#3-authorization)
4. [Method-Level Security](#4-method-level-security)
5. [JWT Authentication](#5-jwt-authentication)
6. [Best Practices](#6-best-practices)

---

## 1. Tổng Quan Spring Security

### 1.1 Security Filter Chain

```
Request → [Security Filters] → Controller → Response
              ↓
    ┌─────────────────────────────────────┐
    │ 1. SecurityContextPersistenceFilter │
    │ 2. UsernamePasswordAuthFilter       │
    │ 3. BasicAuthenticationFilter        │
    │ 4. ExceptionTranslationFilter       │
    │ 5. FilterSecurityInterceptor        │
    └─────────────────────────────────────┘
```

### 1.2 Core Components

| Component | Mô tả |
|-----------|-------|
| `SecurityContext` | Lưu trữ Authentication object |
| `Authentication` | Đại diện cho user đã authenticated |
| `UserDetails` | Interface chứa thông tin user |
| `UserDetailsService` | Load user từ data source |
| `PasswordEncoder` | Encode/verify passwords |
| `AuthenticationManager` | Xử lý authentication |
| `AuthenticationProvider` | Thực hiện authentication logic |

### 1.3 Cấu Hình Cơ Bản

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
}
```

---

## 2. Authentication Methods

### 2.1 In-Memory Authentication

```java
@Bean
public UserDetailsService inMemoryUserDetailsService(PasswordEncoder encoder) {
    UserDetails user = User.builder()
        .username("user")
        .password(encoder.encode("password"))
        .roles("USER")
        .build();
    
    UserDetails admin = User.builder()
        .username("admin")
        .password(encoder.encode("admin123"))
        .roles("ADMIN", "USER")
        .build();
    
    return new InMemoryUserDetailsManager(user, admin);
}
```

**Ưu điểm:**
- Đơn giản, nhanh chóng
- Phù hợp cho development/testing

**Nhược điểm:**
- Không persistent
- Không scalable

### 2.2 JDBC Authentication

```java
@Bean
public UserDetailsService jdbcUserDetailsService(DataSource dataSource) {
    JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
    
    // Custom queries (optional)
    manager.setUsersByUsernameQuery(
        "SELECT username, password, enabled FROM users WHERE username = ?"
    );
    manager.setAuthoritiesByUsernameQuery(
        "SELECT username, authority FROM authorities WHERE username = ?"
    );
    
    return manager;
}
```

**Default Schema:**
```sql
CREATE TABLE users (
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    password VARCHAR(500) NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (username) REFERENCES users(username)
);
```

### 2.3 Custom UserDetailsService

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) 
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                "User not found: " + username
            ));
        
        return new CustomUserDetails(user);
    }
}
```

**Custom UserDetails:**
```java
public class CustomUserDetails implements UserDetails {
    
    private final User user;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            
            for (Permission permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }
        
        return authorities;
    }
    
    @Override
    public String getPassword() { return user.getPassword(); }
    
    @Override
    public String getUsername() { return user.getUsername(); }
    
    @Override
    public boolean isEnabled() { return user.isEnabled(); }
    
    // ... other methods
}
```

### 2.4 Password Encoding

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

// Các PasswordEncoder khác:
// - NoOpPasswordEncoder (không encode - chỉ dùng cho testing!)
// - Pbkdf2PasswordEncoder
// - SCryptPasswordEncoder
// - Argon2PasswordEncoder
```

**BCrypt Features:**
- Tự động thêm salt
- Configurable strength (default: 10)
- Recommended cho production

---

## 3. Authorization

### 3.1 URL-Based Authorization

```java
http.authorizeHttpRequests(auth -> auth
    // Permit all
    .requestMatchers("/public/**").permitAll()
    
    // Deny all
    .requestMatchers("/internal/**").denyAll()
    
    // Role-based (tự động thêm ROLE_ prefix)
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
    
    // Authority-based (không thêm prefix)
    .requestMatchers("/reports/**").hasAuthority("READ_REPORTS")
    .requestMatchers("/delete/**").hasAnyAuthority("DELETE_USER", "ADMIN")
    
    // HTTP method specific
    .requestMatchers(HttpMethod.GET, "/api/**").hasAuthority("READ")
    .requestMatchers(HttpMethod.POST, "/api/**").hasAuthority("WRITE")
    
    // Authenticated
    .anyRequest().authenticated()
);
```

### 3.2 Role vs Authority

| Aspect | Role | Authority |
|--------|------|-----------|
| Prefix | ROLE_ (tự động) | Không có |
| Check | hasRole("ADMIN") | hasAuthority("DELETE_USER") |
| Stored | ROLE_ADMIN | DELETE_USER |
| Use case | Coarse-grained | Fine-grained |

```java
// Role-based
.hasRole("ADMIN")           // Checks for ROLE_ADMIN
.hasAnyRole("ADMIN", "MOD") // Checks for ROLE_ADMIN or ROLE_MOD

// Authority-based
.hasAuthority("ROLE_ADMIN")     // Exact match
.hasAuthority("DELETE_USER")    // Permission
.hasAnyAuthority("READ", "WRITE")
```

### 3.3 SpEL Expressions

```java
// Access control expressions
.access("hasRole('ADMIN') and hasIpAddress('192.168.1.0/24')")
.access("@myBean.check(authentication, request)")

// Common expressions
permitAll()                    // Cho phép tất cả
denyAll()                      // Từ chối tất cả
authenticated()                // Đã đăng nhập
anonymous()                    // Chưa đăng nhập
rememberMe()                   // Remember-me authentication
fullyAuthenticated()           // Không phải remember-me
hasRole('ROLE')                // Có role
hasAnyRole('R1', 'R2')         // Có bất kỳ role nào
hasAuthority('AUTH')           // Có authority
hasAnyAuthority('A1', 'A2')    // Có bất kỳ authority nào
```

---

## 4. Method-Level Security

### 4.1 Enable Method Security

```java
@Configuration
@EnableMethodSecurity(
    prePostEnabled = true,   // @PreAuthorize, @PostAuthorize
    securedEnabled = true,   // @Secured
    jsr250Enabled = true     // @RolesAllowed
)
public class MethodSecurityConfig { }
```

### 4.2 @PreAuthorize

```java
// Role check
@PreAuthorize("hasRole('ADMIN')")
public void adminMethod() { }

// Multiple roles
@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
public void modMethod() { }

// Authority check
@PreAuthorize("hasAuthority('DELETE_USER')")
public void deleteUser(Long id) { }

// Combined conditions
@PreAuthorize("hasRole('ADMIN') and hasAuthority('WRITE_REPORTS')")
public void generateReport() { }

// Access method parameters
@PreAuthorize("#username == authentication.name or hasRole('ADMIN')")
public String getProfile(String username) { }

// Access object properties
@PreAuthorize("#doc.owner == authentication.name")
public void updateDocument(Document doc) { }

// Custom bean
@PreAuthorize("@securityChecker.canAccess(#id, authentication)")
public void accessResource(Long id) { }
```

### 4.3 @PostAuthorize

```java
// Check return value
@PostAuthorize("returnObject.owner == authentication.name")
public Document getDocument(Long id) {
    return documentRepository.findById(id);
}

// Check property
@PostAuthorize("returnObject.confidential == false or hasRole('ADMIN')")
public Document getSecretDocument(Long id) { }
```

### 4.4 @PreFilter / @PostFilter

```java
// Filter input collection
@PreFilter("filterObject.owner == authentication.name")
public void batchUpdate(List<Document> documents) { }

// Filter output collection
@PostFilter("filterObject.owner == authentication.name or hasRole('ADMIN')")
public List<Document> getAllDocuments() { }
```

### 4.5 @Secured và @RolesAllowed

```java
// @Secured - Spring annotation
@Secured("ROLE_ADMIN")
public void securedMethod() { }

@Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
public void multiRoleMethod() { }

// @RolesAllowed - JSR-250 standard
@RolesAllowed("ADMIN")
public void jsr250Method() { }

@RolesAllowed({"ADMIN", "USER"})
public void multiRoleJsr250() { }
```

### 4.6 So Sánh Annotations

| Annotation | SpEL | Return Value | Parameters |
|------------|------|--------------|------------|
| @PreAuthorize | ✅ | ❌ | ✅ |
| @PostAuthorize | ✅ | ✅ | ✅ |
| @Secured | ❌ | ❌ | ❌ |
| @RolesAllowed | ❌ | ❌ | ❌ |

---

## 5. JWT Authentication

### 5.1 JWT Structure

```
Header.Payload.Signature

Header: {
  "alg": "HS256",
  "typ": "JWT"
}

Payload: {
  "sub": "username",
  "iat": 1234567890,
  "exp": 1234567890,
  "roles": ["ROLE_USER"]
}

Signature: HMACSHA256(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret
)
```

### 5.2 JWT Service

```java
@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .subject(userDetails.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(getSigningKey())
            .compact();
    }
    
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
}
```

### 5.3 JWT Filter

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        // 1. Extract token from header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String jwt = authHeader.substring(7);
        
        // 2. Validate token
        String username = jwtService.extractUsername(jwt);
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // 3. Set authentication
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                    );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
```

### 5.4 JWT Security Config

```java
@Configuration
@EnableWebSecurity
public class JwtSecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

### 5.5 Login Endpoint

```java
@PostMapping("/login")
public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
    // 1. Authenticate
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        )
    );
    
    // 2. Generate tokens
    UserDetails userDetails = (UserDetails) auth.getPrincipal();
    String accessToken = jwtService.generateToken(userDetails);
    String refreshToken = jwtService.generateRefreshToken(userDetails);
    
    // 3. Return response
    return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
}
```

---

## 6. Best Practices

### 6.1 Password Security

```java
// ✅ DO: Use BCrypt
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12); // strength 12
}

// ❌ DON'T: Store plain text passwords
// ❌ DON'T: Use weak encoders in production
```

### 6.2 CSRF Protection

```java
// REST API (stateless) - có thể disable
http.csrf(csrf -> csrf.disable());

// Web app với sessions - KHÔNG disable!
http.csrf(Customizer.withDefaults());
```

### 6.3 CORS Configuration

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:3000"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
}
```

### 6.4 Exception Handling

```java
http.exceptionHandling(ex -> ex
    .authenticationEntryPoint((request, response, authException) -> {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Unauthorized");
    })
    .accessDeniedHandler((request, response, accessDeniedException) -> {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("Access Denied");
    })
);
```

### 6.5 Security Headers

```java
http.headers(headers -> headers
    .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
    .frameOptions(frame -> frame.deny())
    .xssProtection(xss -> xss.disable())
    .contentTypeOptions(Customizer.withDefaults())
);
```

---

## 📁 Cấu Trúc Files

```
src/main/java/com/example/spring_cert_notes/security/
├── config/
│   ├── InMemorySecurityConfig.java   # In-memory authentication
│   ├── JdbcSecurityConfig.java       # JDBC authentication
│   ├── CustomSecurityConfig.java     # Custom UserDetailsService
│   └── JwtSecurityConfig.java        # JWT authentication
├── controller/
│   ├── PublicController.java         # Public endpoints
│   ├── UserController.java           # User endpoints
│   ├── AdminController.java          # Admin endpoints
│   ├── ModeratorController.java      # Moderator endpoints
│   └── AuthController.java           # Login/Register
├── dto/
│   ├── AuthRequest.java
│   ├── AuthResponse.java
│   ├── RegisterRequest.java
│   └── Document.java
├── entity/
│   ├── User.java
│   ├── Role.java
│   └── Permission.java
├── jwt/
│   ├── JwtService.java               # JWT utilities
│   └── JwtAuthenticationFilter.java  # JWT filter
├── repository/
│   ├── UserRepository.java
│   └── RoleRepository.java
├── service/
│   ├── CustomUserDetailsService.java
│   ├── CustomUserDetails.java
│   ├── MethodSecurityService.java    # Method security demo
│   └── SecurityChecker.java          # Custom security bean
└── init/
    └── DataInitializer.java          # Sample data
```

---

## 🧪 Test Endpoints

### Chạy với profile:
```bash
# In-memory authentication
./mvnw spring-boot:run -Dspring-boot.run.profiles=inmemory

# Custom UserDetailsService
./mvnw spring-boot:run -Dspring-boot.run.profiles=custom

# JWT authentication
./mvnw spring-boot:run -Dspring-boot.run.profiles=jwt
```

### Test với curl:

```bash
# Public endpoint
curl http://localhost:8080/api/public/hello

# Basic Auth
curl -u user:password http://localhost:8080/api/user/profile
curl -u admin:admin123 http://localhost:8080/api/admin/dashboard

# JWT Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"password"}'

# JWT Protected endpoint
curl http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer <token>"
```

---

## 🎯 Checklist

- [ ] Configure In-Memory authentication
- [ ] Configure JDBC authentication
- [ ] Implement Custom UserDetailsService
- [ ] URL-based authorization với roles
- [ ] URL-based authorization với authorities
- [ ] Method-level security với @PreAuthorize
- [ ] Method-level security với @PostAuthorize
- [ ] @PreFilter và @PostFilter
- [ ] JWT authentication (bonus)
- [ ] Password encoding với BCrypt
