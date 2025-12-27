# SPRING SECURITY
## Guideline Chi Tiết cho Spring Professional Certification

---

**Tài liệu ôn tập toàn diện về Security trong Spring Framework**

*Tạo ngày: 25/12/2024*

---

## MỤC LỤC

1. [Giới thiệu về Spring Security](#1-giới-thiệu-về-spring-security)
2. [Authentication vs Authorization](#2-authentication-vs-authorization)
3. [Spring Security Architecture](#3-spring-security-architecture)
4. [Configuration cơ bản](#4-configuration-cơ-bản)
5. [Authentication](#5-authentication)
6. [Authorization](#6-authorization)
7. [Password Encoding](#7-password-encoding)
8. [CSRF Protection](#8-csrf-protection)
9. [Session Management](#9-session-management)
10. [OAuth2 và JWT](#10-oauth2-và-jwt)
11. [Best Practices](#11-best-practices)
12. [Câu hỏi mẫu cho kỳ thi](#12-câu-hỏi-mẫu-cho-kỳ-thi)
13. [Tóm tắt và mẹo thi](#13-tóm-tắt-và-mẹo-thi)

---

## 1. GIỚI THIỆU VỀ SPRING SECURITY

### 1.1. Spring Security là gì?

**Spring Security** là framework bảo mật mạnh mẽ và có thể tùy chỉnh cao cho các ứng dụng Java. Nó là tiêu chuẩn de-facto cho việc bảo vệ các ứng dụng Spring-based.

**Tính năng chính:**
- ✅ **Authentication**: Xác thực người dùng
- ✅ **Authorization**: Phân quyền truy cập
- ✅ **Protection**: Bảo vệ khỏi các attacks (CSRF, Session Fixation, Clickjacking)
- ✅ **Integration**: Tích hợp dễ dàng với Spring Framework
- ✅ **Extensibility**: Có thể mở rộng và tùy chỉnh

### 1.2. Core Security Concepts

```
┌────────────────────────────────────────────────┐
│           Security Flow                        │
├────────────────────────────────────────────────┤
│  1. User sends credentials                     │
│  2. Authentication (Who are you?)              │
│  3. Authorization (What can you do?)           │
│  4. Access granted/denied                      │
└────────────────────────────────────────────────┘
```

### 1.3. Tại sao cần Spring Security?

**Bảo vệ ứng dụng khỏi:**
- 🔒 Unauthorized access
- 🔒 SQL Injection
- 🔒 Cross-Site Scripting (XSS)
- 🔒 Cross-Site Request Forgery (CSRF)
- 🔒 Session Fixation
- 🔒 Clickjacking

---

## 2. AUTHENTICATION VS AUTHORIZATION

### 2.1. Authentication (Xác thực)

**Authentication** trả lời câu hỏi: **"Bạn là ai?"**

```java
// Authentication example
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
String username = auth.getName();
Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
```

**Các phương thức Authentication:**
- Username/Password
- OAuth2
- LDAP
- JWT
- Certificate-based
- Biometric

### 2.2. Authorization (Phân quyền)

**Authorization** trả lời câu hỏi: **"Bạn có quyền làm gì?"**

```java
// Authorization example
@PreAuthorize("hasRole('ADMIN')")
public void deleteUser(Long id) {
    // Only users with ADMIN role can execute this
}
```

**Các level Authorization:**
- URL-based security
- Method-level security
- Domain object security

### 2.3. So sánh

| Aspect | Authentication | Authorization |
|--------|----------------|---------------|
| **Question** | Who are you? | What can you do? |
| **Process** | Xác minh identity | Kiểm tra permissions |
| **Happens** | First | After authentication |
| **Example** | Login with username/password | Access admin panel |
| **Failure** | 401 Unauthorized | 403 Forbidden |

---

## 3. SPRING SECURITY ARCHITECTURE

### 3.1. Core Components

```
┌──────────────────────────────────────────────────┐
│         Spring Security Architecture             │
├──────────────────────────────────────────────────┤
│                                                  │
│  HTTP Request                                    │
│       ↓                                          │
│  SecurityFilterChain                             │
│       ↓                                          │
│  AuthenticationManager                           │
│       ↓                                          │
│  AuthenticationProvider                          │
│       ↓                                          │
│  UserDetailsService                              │
│       ↓                                          │
│  SecurityContext                                 │
│                                                  │
└──────────────────────────────────────────────────┘
```

### 3.2. SecurityFilterChain

Spring Security sử dụng một chuỗi filters để xử lý security:

**Các Filter quan trọng:**

| Filter | Chức năng |
|--------|-----------|
| **SecurityContextPersistenceFilter** | Load/save SecurityContext từ session |
| **UsernamePasswordAuthenticationFilter** | Xử lý form login |
| **BasicAuthenticationFilter** | Xử lý HTTP Basic authentication |
| **BearerTokenAuthenticationFilter** | Xử lý JWT/OAuth2 tokens |
| **CsrfFilter** | CSRF protection |
| **ExceptionTranslationFilter** | Handle security exceptions |
| **FilterSecurityInterceptor** | Authorization decisions |

### 3.3. Key Interfaces

#### Authentication

```java
public interface Authentication extends Principal, Serializable {
    Collection<? extends GrantedAuthority> getAuthorities();
    Object getCredentials();
    Object getDetails();
    Object getPrincipal();
    boolean isAuthenticated();
    void setAuthenticated(boolean isAuthenticated);
}
```

#### UserDetails

```java
public interface UserDetails extends Serializable {
    Collection<? extends GrantedAuthority> getAuthorities();
    String getPassword();
    String getUsername();
    boolean isAccountNonExpired();
    boolean isAccountNonLocked();
    boolean isCredentialsNonExpired();
    boolean isEnabled();
}
```

#### UserDetailsService

```java
public interface UserDetailsService {
    UserDetails loadUserByUsername(String username) 
        throws UsernameNotFoundException;
}
```

### 3.4. SecurityContext

```java
// Get current authentication
Authentication auth = SecurityContextHolder.getContext().getAuthentication();

// Get username
String username = auth.getName();

// Get principal
UserDetails principal = (UserDetails) auth.getPrincipal();

// Check if authenticated
boolean isAuthenticated = auth.isAuthenticated();

// Get authorities/roles
Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
```

---

## 4. CONFIGURATION CƠ BẢN

### 4.1. Dependencies

**Maven:**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- For testing -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

**Gradle:**

```gradle
implementation 'org.springframework.boot:spring-boot-starter-security'
testImplementation 'org.springframework.security:spring-security-test'
```

### 4.2. Java Configuration

#### Basic Configuration

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
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
            )
            .logout(logout -> logout
                .permitAll()
            );
        
        return http.build();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build();
        
        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin"))
            .roles("ADMIN", "USER")
            .build();
        
        return new InMemoryUserDetailsManager(user, admin);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 4.3. Default Security

> ⚠️ **Quan trọng**: Khi thêm `spring-boot-starter-security`, Spring Boot tự động:
> - Enable security cho tất cả endpoints
> - Tạo default user với username `user`
> - Generate random password (in console)
> - Enable CSRF protection
> - Enable session fixation protection

**Disable security (chỉ dùng cho development):**

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
```

---

## 5. AUTHENTICATION

### 5.1. In-Memory Authentication

```java
@Bean
public UserDetailsService userDetailsService() {
    UserDetails user1 = User.builder()
        .username("john")
        .password(passwordEncoder().encode("password123"))
        .roles("USER")
        .build();
    
    UserDetails user2 = User.builder()
        .username("admin")
        .password(passwordEncoder().encode("admin123"))
        .roles("ADMIN", "USER")
        .authorities("READ", "WRITE", "DELETE")
        .build();
    
    return new InMemoryUserDetailsManager(user1, user2);
}
```

### 5.2. Database Authentication

#### Entity

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private boolean enabled = true;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    
    // Getters and setters
}

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;
    
    // Getters and setters
}
```

#### UserDetailsService Implementation

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) 
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                "User not found: " + username));
        
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.isEnabled(),
            true, // accountNonExpired
            true, // credentialsNonExpired
            true, // accountNonLocked
            getAuthorities(user.getRoles())
        );
    }
    
    private Collection<? extends GrantedAuthority> getAuthorities(
            Set<Role> roles) {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
    }
}
```

#### Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults())
            .userDetailsService(userDetailsService);
        
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

### 5.3. Custom Authentication Provider

```java
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public Authentication authenticate(Authentication authentication) 
            throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        UserDetails user = userDetailsService.loadUserByUsername(username);
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        
        return new UsernamePasswordAuthenticationToken(
            user, 
            password, 
            user.getAuthorities()
        );
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class
            .isAssignableFrom(authentication);
    }
}
```

### 5.4. Form Login Configuration

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .formLogin(form -> form
            .loginPage("/login")                    // Custom login page
            .loginProcessingUrl("/perform-login")   // URL to submit username/password
            .defaultSuccessUrl("/dashboard", true)  // After login success
            .failureUrl("/login?error=true")        // After login failure
            .usernameParameter("username")          // Request parameter name for username
            .passwordParameter("password")          // Request parameter name for password
            .permitAll()
        );
    
    return http.build();
}
```

### 5.5. HTTP Basic Authentication

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/**").authenticated()
        )
        .httpBasic(Customizer.withDefaults());
    
    return http.build();
}
```

**Testing with cURL:**

```bash
curl -u username:password http://localhost:8080/api/users
```

### 5.6. Remember-Me Authentication

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .rememberMe(remember -> remember
            .key("uniqueAndSecret")
            .tokenValiditySeconds(86400) // 24 hours
            .rememberMeParameter("remember-me")
            .rememberMeCookieName("my-remember-me")
        );
    
    return http.build();
}
```

---

## 6. AUTHORIZATION

### 6.1. URL-Based Authorization

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            // Public access
            .requestMatchers("/", "/home", "/public/**").permitAll()
            
            // Specific roles
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
            
            // Specific authorities
            .requestMatchers("/api/delete/**").hasAuthority("DELETE")
            .requestMatchers("/api/write/**").hasAnyAuthority("WRITE", "ADMIN")
            
            // HTTP methods
            .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
            
            // Path patterns
            .requestMatchers("/api/users/{id}").access(
                new WebExpressionAuthorizationManager("#id == authentication.principal.id")
            )
            
            // All other requests
            .anyRequest().authenticated()
        );
    
    return http.build();
}
```

### 6.2. Method-Level Security

#### Enable Method Security

```java
@Configuration
@EnableMethodSecurity(
    prePostEnabled = true,      // Enable @PreAuthorize, @PostAuthorize
    securedEnabled = true,       // Enable @Secured
    jsr250Enabled = true         // Enable @RolesAllowed
)
public class MethodSecurityConfig {
}
```

#### @PreAuthorize

```java
@Service
public class UserService {
    
    // Check role
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long id) {
        // Only ADMIN can execute
    }
    
    // Check multiple roles
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public void banUser(Long id) {
        // ADMIN or MODERATOR can execute
    }
    
    // Check authority
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public void removeUser(Long id) {
        // Only users with DELETE_USER authority
    }
    
    // SpEL expressions
    @PreAuthorize("#userId == authentication.principal.id")
    public User updateProfile(Long userId, UserDTO dto) {
        // Users can only update their own profile
    }
    
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public User getUser(Long userId) {
        // ADMIN can view any user, users can view themselves
    }
    
    // Complex expressions
    @PreAuthorize("hasRole('ADMIN') and #user.email.endsWith('@company.com')")
    public void approveUser(User user) {
        // ADMIN can approve only company emails
    }
}
```

#### @PostAuthorize

```java
@Service
public class UserService {
    
    // Check return value
    @PostAuthorize("returnObject.username == authentication.name")
    public User getUserById(Long id) {
        // Method executes, but throws AccessDeniedException 
        // if returned user doesn't match current user
        return userRepository.findById(id).orElse(null);
    }
    
    @PostAuthorize("hasRole('ADMIN') or returnObject.author == authentication.name")
    public Post getPost(Long id) {
        // ADMIN or post author can view
        return postRepository.findById(id).orElse(null);
    }
}
```

#### @PreFilter and @PostFilter

```java
@Service
public class UserService {
    
    // Filter input collection
    @PreFilter("filterObject.author == authentication.name")
    public void deleteMultiplePosts(List<Post> posts) {
        // Only posts where author matches current user will be processed
        posts.forEach(post -> postRepository.delete(post));
    }
    
    // Filter return collection
    @PostFilter("filterObject.owner == authentication.name or hasRole('ADMIN')")
    public List<Document> getAllDocuments() {
        // Returns all documents, but filters them after execution
        // Users see only their own documents, ADMIN sees all
        return documentRepository.findAll();
    }
}
```

#### @Secured

```java
@Service
public class UserService {
    
    @Secured("ROLE_ADMIN")
    public void deleteUser(Long id) {
        // Only ADMIN
    }
    
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public void banUser(Long id) {
        // ADMIN or MODERATOR
    }
}
```

> ⚠️ **Note**: `@Secured` không support SpEL expressions như `@PreAuthorize`

#### @RolesAllowed (JSR-250)

```java
@Service
public class UserService {
    
    @RolesAllowed("ADMIN")
    public void deleteUser(Long id) {
        // Only ADMIN
    }
    
    @RolesAllowed({"ADMIN", "MODERATOR"})
    public void banUser(Long id) {
        // ADMIN or MODERATOR
    }
}
```

### 6.3. Custom Security Expression

```java
@Component("customSecurity")
public class CustomSecurityExpression {
    
    public boolean isOwner(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        
        UserDetails principal = (UserDetails) auth.getPrincipal();
        // Custom logic to check ownership
        return userId.equals(getCurrentUserId(principal));
    }
    
    public boolean hasPermission(String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals(permission));
    }
}

// Usage
@Service
public class UserService {
    
    @PreAuthorize("@customSecurity.isOwner(#userId)")
    public void updateProfile(Long userId, UserDTO dto) {
        // Custom ownership check
    }
    
    @PreAuthorize("@customSecurity.hasPermission('DELETE_USER')")
    public void deleteUser(Long id) {
        // Custom permission check
    }
}
```

---

## 7. PASSWORD ENCODING

### 7.1. Password Encoders

Spring Security cung cấp nhiều password encoders:

| Encoder | Description | Security Level |
|---------|-------------|----------------|
| **BCryptPasswordEncoder** | Uses BCrypt strong hashing | ⭐⭐⭐⭐⭐ Recommended |
| **Argon2PasswordEncoder** | Uses Argon2 algorithm | ⭐⭐⭐⭐⭐ Very Strong |
| **Pbkdf2PasswordEncoder** | Uses PBKDF2 algorithm | ⭐⭐⭐⭐ Strong |
| **SCryptPasswordEncoder** | Uses scrypt algorithm | ⭐⭐⭐⭐⭐ Very Strong |
| **NoOpPasswordEncoder** | Plain text (NEVER use in production!) | ⭐ Deprecated |

### 7.2. BCryptPasswordEncoder

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

// Usage in service
@Service
public class UserService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User createUser(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(user);
    }
    
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
```

### 7.3. DelegatingPasswordEncoder

Spring Security 5+ uses `DelegatingPasswordEncoder` by default, supporting multiple encoding formats:

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
}
```

**Password format:**
```
{id}encodedPassword
```

**Examples:**
- `{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG`
- `{pbkdf2}5d923b44a6d129f3ddf3e3c8d29412723dcbde72445e8ef6bf3b508fbf17fa4ed4d6b99ca763d8dc`
- `{noop}password` (plain text - only for testing!)

### 7.4. Custom Password Encoder

```java
public class CustomPasswordEncoder implements PasswordEncoder {
    
    @Override
    public String encode(CharSequence rawPassword) {
        // Custom encoding logic
        return customEncode(rawPassword.toString());
    }
    
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Custom matching logic
        return customEncode(rawPassword.toString()).equals(encodedPassword);
    }
    
    private String customEncode(String password) {
        // Your custom encoding algorithm
        return password; // Example only!
    }
}
```

---

## 8. CSRF PROTECTION

### 8.1. CSRF là gì?

**Cross-Site Request Forgery (CSRF)** là một attack nơi attacker lừa user thực hiện unwanted actions.

```
┌─────────────────────────────────────────────┐
│           CSRF Attack Flow                  │
├─────────────────────────────────────────────┤
│  1. User logs into bank.com                 │
│  2. User visits malicious.com               │
│  3. malicious.com sends request to bank.com │
│  4. Browser includes bank.com cookies       │
│  5. bank.com processes fraudulent request   │
└─────────────────────────────────────────────┘
```

### 8.2. CSRF Protection in Spring Security

> ✅ Spring Security enables CSRF protection by default!

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // CSRF enabled by default
        .csrf(Customizer.withDefaults());
    
    return http.build();
}
```

### 8.3. Disable CSRF (chỉ cho stateless REST APIs)

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable());
    
    return http.build();
}
```

> ⚠️ **Warning**: Chỉ disable CSRF cho stateless REST APIs sử dụng token-based authentication!

### 8.4. CSRF Token trong Forms

**Thymeleaf (tự động inject CSRF token):**

```html
<form method="post" th:action="@{/users}">
    <input type="text" name="username"/>
    <input type="password" name="password"/>
    <!-- CSRF token automatically included -->
    <button type="submit">Submit</button>
</form>
```

**JSP:**

```jsp
<form method="post" action="/users">
    <input type="text" name="username"/>
    <input type="password" name="password"/>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <button type="submit">Submit</button>
</form>
```

### 8.5. CSRF Token với AJAX

```javascript
// Get CSRF token from meta tag
const token = document.querySelector('meta[name="_csrf"]').content;
const header = document.querySelector('meta[name="_csrf_header"]').content;

// Include in AJAX request
fetch('/api/users', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
        [header]: token
    },
    body: JSON.stringify(userData)
});

// jQuery
$.ajax({
    url: '/api/users',
    type: 'POST',
    beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token);
    },
    data: JSON.stringify(userData),
    contentType: 'application/json'
});
```

**HTML meta tags:**

```html
<head>
    <meta name="_csrf" th:content="${_csrf.token}"/>
    <meta name="_csrf_header" th:content="${_csrf.headerName}"/>
</head>
```

### 8.6. Custom CSRF Configuration

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .ignoringRequestMatchers("/api/public/**")
        );
    
    return http.build();
}
```

---

## 9. SESSION MANAGEMENT

### 9.1. Session Fixation Protection

Spring Security tự động bảo vệ khỏi session fixation attacks bằng cách tạo session ID mới sau khi login.

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .sessionManagement(session -> session
            .sessionFixation().newSession()  // Create new session (default)
            // .sessionFixation().migrateSession()  // Migrate session
            // .sessionFixation().changeSessionId()  // Change session ID only
            // .sessionFixation().none()  // No protection (not recommended)
        );
    
    return http.build();
}
```

### 9.2. Concurrent Session Control

Giới hạn số lượng sessions đồng thời cho mỗi user:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .sessionManagement(session -> session
            .maximumSessions(1)  // Only 1 session per user
            .maxSessionsPreventsLogin(true)  // Prevent new login
            // .maxSessionsPreventsLogin(false)  // Invalidate old session (default)
            .expiredUrl("/session-expired")
        );
    
    return http.build();
}

// Required for concurrent session control
@Bean
public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
}
```

### 9.3. Session Timeout

**application.properties:**

```properties
# Session timeout (minutes)
server.servlet.session.timeout=30m

# Or in seconds
server.servlet.session.timeout=1800
```

**Java Configuration:**

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .sessionManagement(session -> session
            .invalidSessionUrl("/session-invalid")
        );
    
    return http.build();
}
```

### 9.4. Stateless Session (REST APIs)

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
    
    return http.build();
}
```

**Session Creation Policies:**

| Policy | Description |
|--------|-------------|
| **ALWAYS** | Always create session |
| **IF_REQUIRED** | Create session if required (default) |
| **NEVER** | Never create, but use if exists |
| **STATELESS** | Never create or use sessions |

---

## 10. OAUTH2 VÀ JWT

### 10.1. OAuth2 Overview

**OAuth2** là một authorization framework cho phép applications obtain limited access đến user accounts.

```
┌────────────────────────────────────────────┐
│         OAuth2 Flow                        │
├────────────────────────────────────────────┤
│  1. User → Authorization Server            │
│  2. User grants permission                 │
│  3. Authorization Server → Access Token    │
│  4. Client uses Access Token               │
│  5. Resource Server validates Token        │
└────────────────────────────────────────────┘
```

### 10.2. OAuth2 Roles

- **Resource Owner**: User who owns the data
- **Client**: Application requesting access
- **Authorization Server**: Issues access tokens
- **Resource Server**: Hosts protected resources

### 10.3. OAuth2 Grant Types

1. **Authorization Code**: For web apps
2. **Implicit**: For public clients (deprecated)
3. **Resource Owner Password Credentials**: For trusted clients
4. **Client Credentials**: For machine-to-machine

### 10.4. OAuth2 Login Configuration

**Dependencies:**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

**application.yml:**

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: your-client-id
            client-secret: your-client-secret
            scope:
              - email
              - profile
          github:
            client-id: your-client-id
            client-secret: your-client-secret
            scope:
              - user:email
              - read:user
```

**Configuration:**

```java
@Configuration
@EnableWebSecurity
public class OAuth2Config {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard")
            );
        
        return http.build();
    }
}
```

### 10.5. JWT (JSON Web Token)

**JWT Structure:**
```
header.payload.signature
```

**Example JWT:**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

### 10.6. JWT Implementation

**Dependencies:**

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

**JWT Utility:**

```java
@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact();
    }
    
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
    
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
```

**JWT Filter:**

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        final String jwt = authHeader.substring(7);
        final String username = jwtUtil.extractUsername(jwt);
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
```

**Security Configuration with JWT:**

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;
    
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

**Authentication Controller:**

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid credentials");
        }
        
        final UserDetails userDetails = userDetailsService
            .loadUserByUsername(request.getUsername());
        
        final String jwt = jwtUtil.generateToken(userDetails);
        
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}
```

---

## 11. BEST PRACTICES

### 11.1. Security Best Practices

1. ✅ **Always use HTTPS in production**
2. ✅ **Never store passwords in plain text**
3. ✅ **Use strong password encoders (BCrypt, Argon2)**
4. ✅ **Enable CSRF protection for stateful apps**
5. ✅ **Implement proper session management**
6. ✅ **Use method-level security for fine-grained control**
7. ✅ **Validate and sanitize all user inputs**
8. ✅ **Keep dependencies up to date**
9. ✅ **Use security headers (X-Frame-Options, CSP, etc.)**
10. ✅ **Implement proper error handling (don't leak sensitive info)**

### 11.2. Security Headers

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .headers(headers -> headers
            .frameOptions(frame -> frame.deny())
            .xssProtection(xss -> xss.block(true))
            .contentSecurityPolicy(csp -> 
                csp.policyDirectives("default-src 'self'"))
            .referrerPolicy(referrer -> 
                referrer.policy(ReferrerPolicy.SAME_ORIGIN))
        );
    
    return http.build();
}
```

### 11.3. Proper Error Handling

```java
@ControllerAdvice
public class SecurityExceptionHandler {
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body("Access denied");
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthentication(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Authentication failed");
    }
}
```

### 11.4. Audit Logging

```java
@Component
public class SecurityAuditListener implements ApplicationListener<AbstractAuthenticationEvent> {
    
    private static final Logger log = LoggerFactory.getLogger(SecurityAuditListener.class);
    
    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        if (event instanceof AuthenticationSuccessEvent) {
            log.info("Login successful: {}", event.getAuthentication().getName());
        } else if (event instanceof AbstractAuthenticationFailureEvent) {
            log.warn("Login failed: {}", 
                ((AbstractAuthenticationFailureEvent) event).getException().getMessage());
        }
    }
}
```

### 11.5. Testing Security

```java
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testPublicEndpoint() throws Exception {
        mockMvc.perform(get("/public"))
            .andExpect(status().isOk());
    }
    
    @Test
    void testSecureEndpointWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testSecureEndpointWithAuth() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testAdminEndpointWithUserRole() throws Exception {
        mockMvc.perform(get("/api/admin"))
            .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminEndpointWithAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin"))
            .andExpect(status().isOk());
    }
}
```

---

## 12. CÂU HỎI MẪU CHO KỲ THI

### 12.1. Câu hỏi lý thuyết

#### Câu 1: Sự khác biệt giữa Authentication và Authorization là gì?

**Trả lời**: 
- **Authentication** (Xác thực) trả lời câu hỏi "Bạn là ai?" - Xác minh identity của user
- **Authorization** (Phân quyền) trả lời câu hỏi "Bạn có quyền làm gì?" - Kiểm tra permissions của user
- Authentication xảy ra trước, Authorization xảy ra sau
- Authentication failure → 401 Unauthorized, Authorization failure → 403 Forbidden

---

#### Câu 2: @PreAuthorize và @PostAuthorize khác nhau như thế nào?

**Trả lời**:
- **@PreAuthorize**: Kiểm tra permission TRƯỚC khi method thực thi. Nếu fail, method không chạy
- **@PostAuthorize**: Method thực thi trước, kiểm tra permission SAU đó dựa trên return value. Nếu fail, throw AccessDeniedException
- @PreAuthorize dùng cho input validation, @PostAuthorize dùng khi cần kiểm tra return value

---

#### Câu 3: Tại sao cần CSRF protection và khi nào có thể disable?

**Trả lời**:
- CSRF protection ngăn chặn attacks nơi attacker lừa user thực hiện unwanted actions
- Spring Security enable CSRF by default
- Chỉ disable CSRF cho **stateless REST APIs** sử dụng token-based authentication (JWT, OAuth2)
- KHÔNG disable CSRF cho web applications sử dụng session-based authentication

---

#### Câu 4: PasswordEncoder nào được recommend cho production?

**Trả lời**: **BCryptPasswordEncoder** được recommend vì:
- Sử dụng strong hashing algorithm
- Tự động add salt
- Configurable strength
- Chống brute-force attacks
- Industry standard

Alternatives: **Argon2PasswordEncoder** hoặc **SCryptPasswordEncoder** cũng rất mạnh

**NEVER use**: NoOpPasswordEncoder (plain text)

---

#### Câu 5: SessionCreationPolicy.STATELESS có ý nghĩa gì?

**Trả lời**: 
- Spring Security sẽ KHÔNG tạo hoặc sử dụng HTTP sessions
- Dùng cho stateless REST APIs với token-based authentication (JWT)
- Mỗi request phải tự chứa authentication information (token trong header)
- Improve scalability vì không cần session storage

---

### 12.2. Câu hỏi code-based

#### Câu 6: Code sau có vấn đề gì?

```java
@PreAuthorize("hasRole('USER')")
public void deleteUser(Long id) {
    userRepository.deleteById(id);
}
```

**Trả lời**: Có 2 vấn đề:
1. **Security issue**: USER role có thể delete users - nên dùng ADMIN role
2. **Missing @EnableMethodSecurity**: Method security chưa được enable

**Fixed:**
```java
@Configuration
@EnableMethodSecurity
public class SecurityConfig { }

@PreAuthorize("hasRole('ADMIN')")
public void deleteUser(Long id) {
    userRepository.deleteById(id);
}
```

---

#### Câu 7: Làm thế nào để restrict users chỉ có thể update profile của chính họ?

```java
@PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
public User updateUser(Long userId, UserDTO dto) {
    // Users can update their own profile
    // or ADMIN can update any profile
}

// Alternative
@PreAuthorize("@customSecurity.isOwner(#userId)")
public User updateUser(Long userId, UserDTO dto) {
    // Custom ownership check
}
```

---

#### Câu 8: Configure security để:
- Public access: `/`, `/home`, `/public/**`
- Authenticated: `/api/**`
- ADMIN only: `/admin/**`

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/home", "/public/**").permitAll()
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/**").authenticated()
            .anyRequest().denyAll()
        )
        .formLogin(Customizer.withDefaults());
    
    return http.build();
}
```

---

### 12.3. Scenario-based Questions

#### Câu 9: Bạn đang build REST API với JWT. Nên configure security như thế nào?

**Trả lời**:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF cho stateless API
            .csrf(csrf -> csrf.disable())
            
            // Configure endpoints
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            
            // Stateless session
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Add JWT filter
            .addFilterBefore(jwtAuthFilter, 
                UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

---

#### Câu 10: Application bị lỗi "Access Denied" dù user đã login. Nguyên nhân và cách fix?

**Trả lời**: 

**Possible causes:**
1. User không có required role/authority
2. Method security chưa enable (@EnableMethodSecurity)
3. Role format sai (cần prefix ROLE_ hoặc không)
4. SpEL expression sai trong @PreAuthorize

**Debug steps:**
```java
// 1. Check current authentication
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
System.out.println("User: " + auth.getName());
System.out.println("Authorities: " + auth.getAuthorities());

// 2. Enable @EnableMethodSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig { }

// 3. Check role format
// hasRole("ADMIN") expects "ROLE_ADMIN" in authorities
// hasAuthority("ADMIN") expects exact "ADMIN"

// 4. Fix annotation
@PreAuthorize("hasRole('ADMIN')")  // Will match "ROLE_ADMIN"
// or
@PreAuthorize("hasAuthority('ROLE_ADMIN')")  // Exact match
```

---

## 13. TÓM TẮT VÀ MẸO THI

### 13.1. Core Concepts Cheat Sheet

| Concept | Key Points |
|---------|-----------|
| **Authentication** | Who are you? Credentials verification. 401 if fails |
| **Authorization** | What can you do? Permission check. 403 if fails |
| **SecurityContext** | Holds Authentication object for current thread |
| **UserDetails** | Core user information (username, password, authorities) |
| **PasswordEncoder** | BCryptPasswordEncoder recommended |
| **CSRF** | Enabled by default, disable only for stateless APIs |
| **Session** | STATELESS for REST APIs, IF_REQUIRED for web apps |

### 13.2. Annotations Quick Reference

**Method Security:**
```java
@EnableMethodSecurity(prePostEnabled = true)

@PreAuthorize("hasRole('ADMIN')")
@PostAuthorize("returnObject.owner == authentication.name")
@PreFilter("filterObject.owner == authentication.name")
@PostFilter("filterObject.public or hasRole('ADMIN')")
@Secured("ROLE_ADMIN")
@RolesAllowed("ADMIN")
```

**Testing:**
```java
@WithMockUser(username = "user", roles = {"USER"})
@WithMockUser(authorities = {"READ", "WRITE"})
@WithAnonymousUser
```

### 13.3. Configuration Patterns

**Basic Web Security:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults())
            .logout(Customizer.withDefaults());
        return http.build();
    }
}
```

**REST API with JWT:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
```

### 13.4. Common Pitfalls

❌ **Mistake 1**: Quên @EnableMethodSecurity
```java
// BAD
@PreAuthorize("hasRole('ADMIN')")
public void deleteUser() { }

// GOOD
@Configuration
@EnableMethodSecurity
public class SecurityConfig { }
```

❌ **Mistake 2**: Role format confusion
```java
// hasRole() expects "ROLE_" prefix in authorities
@PreAuthorize("hasRole('ADMIN')")  // Matches "ROLE_ADMIN"

// hasAuthority() expects exact match
@PreAuthorize("hasAuthority('ADMIN')")  // Matches "ADMIN"
```

❌ **Mistake 3**: Enable CSRF cho REST APIs
```java
// BAD for REST API
.csrf(Customizer.withDefaults())  // Don't do this for stateless APIs

// GOOD for REST API
.csrf(csrf -> csrf.disable())
.sessionManagement(session -> 
    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
```

❌ **Mistake 4**: Plain text passwords
```java
// BAD
user.setPassword("password123");

// GOOD
user.setPassword(passwordEncoder.encode("password123"));
```

### 13.5. Mẹo làm bài thi

1. ✅ **Phân biệt Authentication vs Authorization**
2. ✅ **Nhớ enable method security với @EnableMethodSecurity**
3. ✅ **hasRole() vs hasAuthority() - prefix difference**
4. ✅ **CSRF: enable cho web apps, disable cho REST APIs**
5. ✅ **Session: STATELESS cho REST, IF_REQUIRED cho web**
6. ✅ **PasswordEncoder: BCrypt recommended, NoOp NEVER**
7. ✅ **401 = Authentication failed, 403 = Authorization failed**
8. ✅ **SecurityContext holds current Authentication**
9. ✅ **@PreAuthorize check before, @PostAuthorize check after**
10. ✅ **JWT cho stateless, Session cho stateful**

### 13.6. Checklist ôn tập

- [ ] Authentication vs Authorization
- [ ] SecurityFilterChain configuration
- [ ] UserDetailsService implementation
- [ ] PasswordEncoder (BCrypt recommended)
- [ ] @PreAuthorize, @PostAuthorize, @Secured
- [ ] Method security với @EnableMethodSecurity
- [ ] CSRF protection và khi nào disable
- [ ] Session management (STATELESS vs IF_REQUIRED)
- [ ] OAuth2 flow và roles
- [ ] JWT implementation
- [ ] Security testing với @WithMockUser
- [ ] Common security headers
- [ ] Error handling (401 vs 403)

---

## KẾT LUẬN

Spring Security là một trong những topics quan trọng nhất trong Spring Professional Certification. Để thành công, bạn cần:

- ✅ Hiểu rõ Authentication vs Authorization
- ✅ Nắm vững SecurityFilterChain configuration
- ✅ Biết khi nào dùng method security annotations
- ✅ Hiểu CSRF và Session management
- ✅ Implement được JWT authentication
- ✅ Testing security properly

### Điểm quan trọng nhất:

> **Security không phải là optional, nó là MANDATORY!**
>
> Key principles:
> - Defense in depth (multiple layers)
> - Principle of least privilege (minimum permissions)
> - Fail securely (deny by default)
> - Don't trust user input
> - Keep it simple (complexity = vulnerabilities)

**Remember:**
- 🔒 Authentication = "Who are you?" (401)
- 🔒 Authorization = "What can you do?" (403)
- 🔒 Always encode passwords (BCrypt)
- 🔒 CSRF for web apps, disable for REST APIs
- 🔒 STATELESS sessions for REST APIs

Hãy thực hành với các ví dụ trong tài liệu này và đọc kỹ Spring Security documentation. Security là kỹ năng quan trọng không chỉ cho kỳ thi mà còn cho career development!

---

**Chúc bạn thành công với Spring Professional Certification!** 🚀🔒

*Tài liệu được tạo ngày 25/12/2024*
