# SPRING DATA MANAGEMENT
## Guideline Chi Tiết cho Spring Professional Certification

---

**Tài liệu ôn tập toàn diện về Data Access trong Spring Framework**

*Tạo ngày: 26/12/2024*

---

## MỤC LỤC

1. [Giới thiệu về Spring Data](#1-giới-thiệu-về-spring-data)
2. [JPA và Hibernate](#2-jpa-và-hibernate)
3. [Spring Data JPA Configuration](#3-spring-data-jpa-configuration)
4. [Entity Mapping](#4-entity-mapping)
5. [Repository Interfaces](#5-repository-interfaces)
6. [Query Methods](#6-query-methods)
7. [Custom Queries với @Query](#7-custom-queries-với-query)
8. [Named Queries](#8-named-queries)
9. [Entity Relationships](#9-entity-relationships)
10. [Pagination và Sorting](#10-pagination-và-sorting)
11. [Specifications](#11-specifications)
12. [Projections và DTOs](#12-projections-và-dtos)
13. [Auditing](#13-auditing)
14. [Transaction Management](#14-transaction-management)
15. [Locking](#15-locking)
16. [Caching](#16-caching)
17. [Spring Data JDBC](#17-spring-data-jdbc)
18. [Best Practices](#18-best-practices)
19. [Câu hỏi mẫu cho kỳ thi](#19-câu-hỏi-mẫu-cho-kỳ-thi)
20. [Tóm tắt và mẹo thi](#20-tóm-tắt-và-mẹo-thi)

---

## 1. GIỚI THIỆU VỀ SPRING DATA

### 1.1. Spring Data là gì?

**Spring Data** là umbrella project cung cấp consistent, Spring-based programming model cho data access, giảm boilerplate code cần thiết để implement data access layers.

**Core Features:**
- ✅ Repository abstraction
- ✅ Query derivation from method names
- ✅ Support for multiple data stores
- ✅ Pagination và sorting
- ✅ Auditing
- ✅ Custom implementation

### 1.2. Spring Data Projects

```
┌────────────────────────────────────────┐
│         Spring Data Family             │
├────────────────────────────────────────┤
│                                        │
│  Spring Data JPA        (SQL)          │
│  Spring Data JDBC       (SQL)          │
│  Spring Data MongoDB    (NoSQL)        │
│  Spring Data Redis      (Key-Value)    │
│  Spring Data Elasticsearch (Search)    │
│  Spring Data Cassandra  (NoSQL)        │
│  Spring Data Neo4j      (Graph)        │
│                                        │
└────────────────────────────────────────┘
```

### 1.3. Repository Hierarchy

```
Repository<T, ID>
    ↓
CrudRepository<T, ID>
    ↓
PagingAndSortingRepository<T, ID>
    ↓
JpaRepository<T, ID>
```

---

## 2. JPA VÀ HIBERNATE

### 2.1. JPA (Java Persistence API)

**JPA** là specification cho ORM (Object-Relational Mapping) trong Java.

**Core Concepts:**
- Entities
- EntityManager
- Persistence Context
- JPQL (Java Persistence Query Language)
- Criteria API

### 2.2. Hibernate

**Hibernate** là most popular JPA implementation.

**Features:**
- Automatic table generation
- Lazy loading
- Caching (first-level, second-level)
- HQL (Hibernate Query Language)
- Criteria API

### 2.3. JPA vs JDBC

| Aspect | JPA/Hibernate | JDBC |
|--------|---------------|------|
| **Abstraction** | High-level ORM | Low-level SQL |
| **Code** | Less boilerplate | More code |
| **Object Mapping** | Automatic | Manual |
| **Caching** | Built-in | Manual |
| **Lazy Loading** | Supported | Manual |
| **Portability** | Database-independent | Database-specific |
| **Performance** | Overhead | Direct |
| **Learning Curve** | Steep | Moderate |

---

## 3. SPRING DATA JPA CONFIGURATION

### 3.1. Dependencies

**Maven:**

```xml
<dependencies>
    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- Database Driver -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Or PostgreSQL -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Or MySQL -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

**Gradle:**

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    runtimeOnly 'com.h2database:h2'
    // or
    runtimeOnly 'org.postgresql:postgresql'
}
```

### 3.2. Configuration Properties

**application.properties:**

```properties
# DataSource Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=postgres
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Connection Pool (HikariCP - default in Spring Boot)
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

**application.yml:**

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mydb
    username: postgres
    password: password
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

### 3.3. hibernate.ddl-auto Options

| Option | Description | Use Case |
|--------|-------------|----------|
| **none** | No action | Production |
| **validate** | Validate schema | Production |
| **update** | Update schema | Development |
| **create** | Drop and create | Testing |
| **create-drop** | Drop after session | Testing |

> ⚠️ **Production**: Use `validate` or `none`, manage schema with migration tools (Flyway, Liquibase)

### 3.4. Java Configuration

```java
@Configuration
@EnableJpaRepositories(basePackages = "com.example.repository")
@EnableTransactionManagement
public class JpaConfig {
    
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/mydb");
        dataSource.setUsername("postgres");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setMaximumPoolSize(10);
        return dataSource;
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = 
            new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.example.entity");
        
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", 
            "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.show_sql", "true");
        em.setJpaProperties(properties);
        
        return em;
    }
    
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
            entityManagerFactory().getObject());
        return transactionManager;
    }
}
```

---

## 4. ENTITY MAPPING

### 4.1. Basic Entity

```java
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Transient
    private String temporaryData;
    
    // Getters and setters
}

public enum Status {
    ACTIVE, INACTIVE, PENDING
}
```

### 4.2. Primary Key Strategies

```java
// Auto-increment (MySQL, PostgreSQL)
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

// Sequence (Oracle, PostgreSQL)
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
@SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", 
                   allocationSize = 1)
private Long id;

// Table generator
@Id
@GeneratedValue(strategy = GenerationType.TABLE, generator = "user_gen")
@TableGenerator(name = "user_gen", table = "id_generator", 
                pkColumnName = "gen_name", valueColumnName = "gen_value")
private Long id;

// UUID
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private UUID id;

// Custom generator
@Id
@GeneratedValue(generator = "custom-generator")
@GenericGenerator(name = "custom-generator", 
                  strategy = "com.example.CustomIdGenerator")
private String id;
```

### 4.3. Column Mappings

```java
@Entity
@Table(name = "products", 
       indexes = @Index(name = "idx_name", columnList = "name"))
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // String column
    @Column(name = "product_name", nullable = false, length = 100)
    private String name;
    
    // Decimal column
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    
    // Text column (unlimited length)
    @Column(columnDefinition = "TEXT")
    private String description;
    
    // Enum as string
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ProductType type;
    
    // Enum as ordinal (not recommended)
    @Enumerated(EnumType.ORDINAL)
    private Priority priority;
    
    // Date columns
    @Temporal(TemporalType.DATE)
    private Date manufactureDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    // Java 8 Time API
    private LocalDate releaseDate;
    private LocalDateTime lastModified;
    
    // Boolean
    @Column(name = "is_available")
    private Boolean available;
    
    // Large object
    @Lob
    private byte[] image;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String specifications;
}
```

### 4.4. Embedded Objects

```java
@Embeddable
public class Address {
    private String street;
    private String city;
    private String zipCode;
    private String country;
    
    // Getters and setters
}

@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Embedded
    private Address address;
    
    // Multiple embeddable of same type
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", 
                          column = @Column(name = "billing_street")),
        @AttributeOverride(name = "city", 
                          column = @Column(name = "billing_city")),
        @AttributeOverride(name = "zipCode", 
                          column = @Column(name = "billing_zip")),
        @AttributeOverride(name = "country", 
                          column = @Column(name = "billing_country"))
    })
    private Address billingAddress;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", 
                          column = @Column(name = "shipping_street")),
        @AttributeOverride(name = "city", 
                          column = @Column(name = "shipping_city"))
    })
    private Address shippingAddress;
}
```

---

## 5. REPOSITORY INTERFACES

### 5.1. Repository Hierarchy

```java
// Base interface - no methods
public interface Repository<T, ID> {
}

// CRUD operations
public interface CrudRepository<T, ID> extends Repository<T, ID> {
    <S extends T> S save(S entity);
    <S extends T> Iterable<S> saveAll(Iterable<S> entities);
    Optional<T> findById(ID id);
    boolean existsById(ID id);
    Iterable<T> findAll();
    Iterable<T> findAllById(Iterable<ID> ids);
    long count();
    void deleteById(ID id);
    void delete(T entity);
    void deleteAll(Iterable<? extends T> entities);
    void deleteAll();
}

// Pagination and sorting
public interface PagingAndSortingRepository<T, ID> 
        extends CrudRepository<T, ID> {
    Iterable<T> findAll(Sort sort);
    Page<T> findAll(Pageable pageable);
}

// JPA specific
public interface JpaRepository<T, ID> 
        extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {
    List<T> findAll();
    List<T> findAll(Sort sort);
    List<T> findAllById(Iterable<ID> ids);
    <S extends T> List<S> saveAll(Iterable<S> entities);
    void flush();
    <S extends T> S saveAndFlush(S entity);
    void deleteInBatch(Iterable<T> entities);
    void deleteAllInBatch();
    T getOne(ID id);
    <S extends T> List<S> findAll(Example<S> example);
    <S extends T> List<S> findAll(Example<S> example, Sort sort);
}
```

### 5.2. Basic Repository

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Inherited methods:
    // save, findById, findAll, delete, count, etc.
}

// Usage in service
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    public long countUsers() {
        return userRepository.count();
    }
    
    public boolean existsUser(Long id) {
        return userRepository.existsById(id);
    }
}
```

### 5.3. Custom Repository Methods

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find by single field
    Optional<User> findByEmail(String email);
    List<User> findByName(String name);
    List<User> findByAge(Integer age);
    
    // Multiple fields
    List<User> findByNameAndEmail(String name, String email);
    List<User> findByNameOrEmail(String name, String email);
    
    // Comparison
    List<User> findByAgeGreaterThan(Integer age);
    List<User> findByAgeLessThan(Integer age);
    List<User> findByAgeBetween(Integer start, Integer end);
    
    // Like
    List<User> findByNameContaining(String name);
    List<User> findByNameStartingWith(String prefix);
    List<User> findByNameEndingWith(String suffix);
    
    // Null checks
    List<User> findByEmailIsNull();
    List<User> findByEmailIsNotNull();
    
    // Collection
    List<User> findByAgeIn(Collection<Integer> ages);
    List<User> findByAgeNotIn(Collection<Integer> ages);
    
    // Boolean
    List<User> findByActiveTrue();
    List<User> findByActiveFalse();
    
    // Ordering
    List<User> findByAgeOrderByNameAsc(Integer age);
    List<User> findByActiveOrderByCreatedAtDesc(Boolean active);
    
    // First/Top
    User findFirstByOrderByCreatedAtDesc();
    List<User> findTop10ByOrderByCreatedAtDesc();
    
    // Exists
    boolean existsByEmail(String email);
    
    // Count
    long countByActive(Boolean active);
    
    // Delete
    long deleteByActive(Boolean active);
    void deleteByEmail(String email);
}
```

---

## 6. QUERY METHODS

### 6.1. Query Method Keywords

| Keyword | Example | JPQL snippet |
|---------|---------|--------------|
| **And** | `findByNameAndEmail` | `where x.name = ?1 and x.email = ?2` |
| **Or** | `findByNameOrEmail` | `where x.name = ?1 or x.email = ?2` |
| **Is, Equals** | `findByName` | `where x.name = ?1` |
| **Between** | `findByAgeBetween` | `where x.age between ?1 and ?2` |
| **LessThan** | `findByAgeLessThan` | `where x.age < ?1` |
| **LessThanEqual** | `findByAgeLessThanEqual` | `where x.age <= ?1` |
| **GreaterThan** | `findByAgeGreaterThan` | `where x.age > ?1` |
| **GreaterThanEqual** | `findByAgeGreaterThanEqual` | `where x.age >= ?1` |
| **After** | `findByCreatedAtAfter` | `where x.createdAt > ?1` |
| **Before** | `findByCreatedAtBefore` | `where x.createdAt < ?1` |
| **IsNull** | `findByEmailIsNull` | `where x.email is null` |
| **IsNotNull, NotNull** | `findByEmailIsNotNull` | `where x.email is not null` |
| **Like** | `findByNameLike` | `where x.name like ?1` |
| **NotLike** | `findByNameNotLike` | `where x.name not like ?1` |
| **StartingWith** | `findByNameStartingWith` | `where x.name like ?1%` |
| **EndingWith** | `findByNameEndingWith` | `where x.name like %?1` |
| **Containing** | `findByNameContaining` | `where x.name like %?1%` |
| **OrderBy** | `findByAgeOrderByNameDesc` | `order by x.name desc` |
| **Not** | `findByAgeNot` | `where x.age <> ?1` |
| **In** | `findByAgeIn` | `where x.age in ?1` |
| **NotIn** | `findByAgeNotIn` | `where x.age not in ?1` |
| **True** | `findByActiveTrue` | `where x.active = true` |
| **False** | `findByActiveFalse` | `where x.active = false` |
| **IgnoreCase** | `findByNameIgnoreCase` | `where UPPER(x.name) = UPPER(?1)` |

### 6.2. Complex Query Methods

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Complex AND/OR
    List<User> findByNameAndEmailOrAge(String name, String email, Integer age);
    // WHERE (name = ? AND email = ?) OR age = ?
    
    // Nested properties
    List<User> findByAddress_City(String city);
    List<User> findByAddress_CityAndAddress_Country(String city, String country);
    
    // Collection properties
    List<User> findByRoles_Name(String roleName);
    
    // Case insensitive
    List<User> findByNameIgnoreCase(String name);
    List<User> findByNameAndEmailAllIgnoreCase(String name, String email);
    
    // Distinct
    List<User> findDistinctByAge(Integer age);
    
    // Limiting results
    User findFirstByOrderByAgeDesc();
    List<User> findTop3ByOrderByCreatedAtDesc();
    List<User> findFirst10ByActive(Boolean active);
    
    // Streams (for large datasets)
    Stream<User> findByActive(Boolean active);
    
    // Async
    @Async
    CompletableFuture<List<User>> findByAge(Integer age);
}
```

### 6.3. Special Parameters

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Pageable
    Page<User> findByActive(Boolean active, Pageable pageable);
    Slice<User> findByAge(Integer age, Pageable pageable);
    
    // Sort
    List<User> findByActive(Boolean active, Sort sort);
    
    // Limit (Spring Data JPA 3.0+)
    List<User> findByActive(Boolean active, Limit limit);
    
    // Combined
    Page<User> findByActiveAndAgeGreaterThan(
        Boolean active, 
        Integer age, 
        Pageable pageable
    );
}

// Usage
Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
Page<User> users = userRepository.findByActive(true, pageable);

Sort sort = Sort.by("name").ascending().and(Sort.by("age").descending());
List<User> sorted = userRepository.findByActive(true, sort);
```

---

## 7. CUSTOM QUERIES VỚI @QUERY

### 7.1. JPQL Queries

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Simple JPQL
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByEmailJPQL(String email);
    
    // Named parameters
    @Query("SELECT u FROM User u WHERE u.name = :name AND u.age = :age")
    List<User> findByNameAndAge(@Param("name") String name, 
                                @Param("age") Integer age);
    
    // Multiple conditions
    @Query("SELECT u FROM User u WHERE u.active = :active " +
           "AND u.age BETWEEN :minAge AND :maxAge")
    List<User> findActiveUsersByAgeRange(
        @Param("active") Boolean active,
        @Param("minAge") Integer minAge,
        @Param("maxAge") Integer maxAge
    );
    
    // LIKE queries
    @Query("SELECT u FROM User u WHERE u.name LIKE %:name%")
    List<User> searchByName(@Param("name") String name);
    
    // JOIN
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);
    
    // LEFT JOIN
    @Query("SELECT u FROM User u LEFT JOIN u.orders o WHERE o.status = :status")
    List<User> findUsersWithOrderStatus(@Param("status") String status);
    
    // Aggregation
    @Query("SELECT COUNT(u) FROM User u WHERE u.active = :active")
    long countActiveUsers(@Param("active") Boolean active);
    
    @Query("SELECT AVG(u.age) FROM User u")
    Double getAverageAge();
    
    @Query("SELECT u.city, COUNT(u) FROM User u GROUP BY u.city")
    List<Object[]> countUsersByCity();
    
    // Sorting
    @Query("SELECT u FROM User u WHERE u.active = :active")
    List<User> findActiveUsers(@Param("active") Boolean active, Sort sort);
    
    // Pagination
    @Query("SELECT u FROM User u WHERE u.age > :age")
    Page<User> findUsersOlderThan(@Param("age") Integer age, Pageable pageable);
}
```

### 7.2. Native SQL Queries

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Simple native query
    @Query(value = "SELECT * FROM users WHERE email = ?1", 
           nativeQuery = true)
    Optional<User> findByEmailNative(String email);
    
    // Named parameters
    @Query(value = "SELECT * FROM users WHERE name = :name AND age = :age", 
           nativeQuery = true)
    List<User> findByNameAndAgeNative(
        @Param("name") String name,
        @Param("age") Integer age
    );
    
    // Complex native query
    @Query(value = "SELECT u.* FROM users u " +
                   "JOIN user_roles ur ON u.id = ur.user_id " +
                   "JOIN roles r ON ur.role_id = r.id " +
                   "WHERE r.name = :roleName",
           nativeQuery = true)
    List<User> findByRoleNameNative(@Param("roleName") String roleName);
    
    // Pagination with native query
    @Query(value = "SELECT * FROM users WHERE age > :age",
           countQuery = "SELECT COUNT(*) FROM users WHERE age > :age",
           nativeQuery = true)
    Page<User> findUsersOlderThanNative(
        @Param("age") Integer age, 
        Pageable pageable
    );
    
    // Projection with native query
    @Query(value = "SELECT name, email FROM users WHERE id = :id",
           nativeQuery = true)
    Map<String, Object> findUserProjection(@Param("id") Long id);
}
```

### 7.3. Modifying Queries

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Update query
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.active = :active WHERE u.id = :id")
    int updateUserStatus(@Param("id") Long id, @Param("active") Boolean active);
    
    // Batch update
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.active = false WHERE u.lastLogin < :date")
    int deactivateInactiveUsers(@Param("date") LocalDateTime date);
    
    // Delete query
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.active = false")
    int deleteInactiveUsers();
    
    // Native update
    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET status = :status WHERE id = :id",
           nativeQuery = true)
    int updateStatusNative(@Param("id") Long id, @Param("status") String status);
    
    // Clear automatically after modifying query
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User u SET u.email = :email WHERE u.id = :id")
    int updateEmail(@Param("id") Long id, @Param("email") String email);
}
```

> ⚠️ **Important**: @Modifying queries require @Transactional

---

## 8. NAMED QUERIES

### 8.1. @NamedQuery

```java
@Entity
@Table(name = "users")
@NamedQuery(
    name = "User.findByEmailDomain",
    query = "SELECT u FROM User u WHERE u.email LIKE CONCAT('%', :domain)"
)
@NamedQueries({
    @NamedQuery(
        name = "User.findActiveUsers",
        query = "SELECT u FROM User u WHERE u.active = true"
    ),
    @NamedQuery(
        name = "User.findByAgeRange",
        query = "SELECT u FROM User u WHERE u.age BETWEEN :minAge AND :maxAge"
    )
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    private Integer age;
    private Boolean active;
}

// Repository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Method name must match named query
    List<User> findByEmailDomain(@Param("domain") String domain);
    List<User> findActiveUsers();
    List<User> findByAgeRange(
        @Param("minAge") Integer minAge,
        @Param("maxAge") Integer maxAge
    );
}
```

### 8.2. Named Native Query

```java
@Entity
@NamedNativeQuery(
    name = "User.findUsersWithOrders",
    query = "SELECT u.* FROM users u " +
            "WHERE EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.id)",
    resultClass = User.class
)
public class User {
    // Entity fields
}

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersWithOrders();
}
```

---

## 9. ENTITY RELATIONSHIPS

### 9.1. One-to-One

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    // Unidirectional
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private UserProfile profile;
}

@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String bio;
    private String website;
}

// Bidirectional
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile profile;
}

@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
```

### 9.2. One-to-Many / Many-to-One

```java
// Unidirectional One-to-Many
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Post> posts = new ArrayList<>();
}

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String content;
}

// Bidirectional One-to-Many / Many-to-One (Recommended)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();
    
    // Helper methods
    public void addPost(Post post) {
        posts.add(post);
        post.setUser(this);
    }
    
    public void removePost(Post post) {
        posts.remove(post);
        post.setUser(null);
    }
}

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
```

### 9.3. Many-to-Many

```java
// Unidirectional
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @ManyToMany
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
}

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
}

// Bidirectional (Recommended)
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
    
    // Helper methods
    public void addCourse(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }
    
    public void removeCourse(Course course) {
        courses.remove(course);
        course.getStudents().remove(this);
    }
}

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();
}
```

### 9.4. Many-to-Many with Extra Columns

```java
// Join entity
@Entity
@Table(name = "student_course")
public class StudentCourse {
    @EmbeddedId
    private StudentCourseId id;
    
    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;
    
    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;
    
    private LocalDate enrollmentDate;
    private Integer grade;
}

@Embeddable
public class StudentCourseId implements Serializable {
    private Long studentId;
    private Long courseId;
    
    // equals and hashCode
}

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudentCourse> enrollments = new HashSet<>();
}

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudentCourse> enrollments = new HashSet<>();
}
```

### 9.5. Fetch Types

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // LAZY - Load on demand (default for collections)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();
    
    // EAGER - Load immediately (default for single-valued)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;
}
```

| Relationship | Default Fetch |
|--------------|---------------|
| **@OneToOne** | EAGER |
| **@ManyToOne** | EAGER |
| **@OneToMany** | LAZY |
| **@ManyToMany** | LAZY |

> 💡 **Best Practice**: Use LAZY loading and fetch data explicitly when needed

### 9.6. Cascade Types

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Cascade ALL operations
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();
    
    // Cascade specific operations
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Comment> comments = new ArrayList<>();
    
    // With orphan removal
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();
}
```

**Cascade Types:**
- **PERSIST**: Save child when parent is saved
- **MERGE**: Update child when parent is updated
- **REMOVE**: Delete child when parent is deleted
- **REFRESH**: Refresh child when parent is refreshed
- **DETACH**: Detach child when parent is detached
- **ALL**: All of the above

**orphanRemoval**: Delete child when removed from collection

---

## 10. PAGINATION VÀ SORTING

### 10.1. Pageable

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByActive(Boolean active, Pageable pageable);
}

// Usage
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public Page<User> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }
    
    public Page<User> getUsersSorted(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepository.findAll(pageable);
    }
    
    public Page<User> getUsersWithSort(int page, int size) {
        Sort sort = Sort.by("name").ascending()
                       .and(Sort.by("age").descending());
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }
}

// Controller
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public Page<User> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return userService.findAll(pageable);
    }
}
```

### 10.2. Page vs Slice

```java
// Page - knows total count
Page<User> findByActive(Boolean active, Pageable pageable);

// Slice - doesn't know total count (more efficient)
Slice<User> findByAge(Integer age, Pageable pageable);

// Usage
Page<User> page = userRepository.findByActive(true, pageable);
System.out.println("Total elements: " + page.getTotalElements());
System.out.println("Total pages: " + page.getTotalPages());
System.out.println("Current page: " + page.getNumber());
System.out.println("Size: " + page.getSize());
System.out.println("Has next: " + page.hasNext());
System.out.println("Has previous: " + page.hasPrevious());

Slice<User> slice = userRepository.findByAge(25, pageable);
System.out.println("Has next: " + slice.hasNext());
System.out.println("Content: " + slice.getContent());
// No total count available
```

### 10.3. Sorting

```java
// Single field
Sort sort = Sort.by("name");
Sort sort = Sort.by("name").ascending();
Sort sort = Sort.by("name").descending();

// Multiple fields
Sort sort = Sort.by("name", "age");

// Mixed directions
Sort sort = Sort.by("name").ascending().and(Sort.by("age").descending());

// Order
Sort.Order order1 = Sort.Order.asc("name");
Sort.Order order2 = Sort.Order.desc("age");
Sort sort = Sort.by(order1, order2);

// Case insensitive
Sort sort = Sort.by(Sort.Order.asc("name").ignoreCase());

// Null handling
Sort sort = Sort.by(Sort.Order.asc("name").nullsFirst());
Sort sort = Sort.by(Sort.Order.desc("age").nullsLast());
```

---

## 11. SPECIFICATIONS

### 11.1. JPA Specifications

```java
public interface UserRepository 
        extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}

// Specification class
public class UserSpecifications {
    
    public static Specification<User> hasName(String name) {
        return (root, query, cb) -> cb.equal(root.get("name"), name);
    }
    
    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) -> cb.equal(root.get("email"), email);
    }
    
    public static Specification<User> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }
    
    public static Specification<User> ageGreaterThan(Integer age) {
        return (root, query, cb) -> cb.greaterThan(root.get("age"), age);
    }
    
    public static Specification<User> ageBetween(Integer minAge, Integer maxAge) {
        return (root, query, cb) -> 
            cb.between(root.get("age"), minAge, maxAge);
    }
    
    public static Specification<User> nameLike(String pattern) {
        return (root, query, cb) -> 
            cb.like(root.get("name"), "%" + pattern + "%");
    }
}

// Usage
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<User> findUsers(String name, Integer minAge) {
        Specification<User> spec = Specification.where(null);
        
        if (name != null) {
            spec = spec.and(UserSpecifications.hasName(name));
        }
        
        if (minAge != null) {
            spec = spec.and(UserSpecifications.ageGreaterThan(minAge));
        }
        
        spec = spec.and(UserSpecifications.isActive());
        
        return userRepository.findAll(spec);
    }
    
    // With pagination
    public Page<User> searchUsers(UserSearchCriteria criteria, Pageable pageable) {
        Specification<User> spec = Specification
            .where(UserSpecifications.isActive())
            .and(UserSpecifications.ageBetween(criteria.getMinAge(), 
                                               criteria.getMaxAge()));
        
        if (criteria.getName() != null) {
            spec = spec.and(UserSpecifications.nameLike(criteria.getName()));
        }
        
        return userRepository.findAll(spec, pageable);
    }
}
```

### 11.2. Complex Specifications

```java
public class UserSpecifications {
    
    // Join
    public static Specification<User> hasRole(String roleName) {
        return (root, query, cb) -> {
            Join<User, Role> roles = root.join("roles");
            return cb.equal(roles.get("name"), roleName);
        };
    }
    
    // Nested property
    public static Specification<User> hasCity(String city) {
        return (root, query, cb) -> 
            cb.equal(root.get("address").get("city"), city);
    }
    
    // OR condition
    public static Specification<User> nameOrEmailLike(String pattern) {
        return (root, query, cb) -> cb.or(
            cb.like(root.get("name"), "%" + pattern + "%"),
            cb.like(root.get("email"), "%" + pattern + "%")
        );
    }
    
    // Complex condition
    public static Specification<User> complexSearch(
            String name, Integer minAge, String city) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            
            if (name != null) {
                predicate = cb.and(predicate, 
                    cb.like(root.get("name"), "%" + name + "%"));
            }
            
            if (minAge != null) {
                predicate = cb.and(predicate, 
                    cb.greaterThan(root.get("age"), minAge));
            }
            
            if (city != null) {
                predicate = cb.and(predicate, 
                    cb.equal(root.get("address").get("city"), city));
            }
            
            return predicate;
        };
    }
}
```

---

## 12. PROJECTIONS VÀ DTOs

### 12.1. Interface-based Projections

```java
// Closed projection
public interface UserSummary {
    String getName();
    String getEmail();
}

// Open projection (with SpEL)
public interface UserInfo {
    String getName();
    String getEmail();
    
    @Value("#{target.name + ' (' + target.email + ')'}")
    String getFullInfo();
}

// Nested projection
public interface UserWithAddress {
    String getName();
    AddressInfo getAddress();
    
    interface AddressInfo {
        String getCity();
        String getCountry();
    }
}

// Repository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    List<UserSummary> findByActive(Boolean active);
    UserInfo findProjectionById(Long id);
    List<UserWithAddress> findAllBy();
}

// Usage
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<UserSummary> getActiveuserSummaries() {
        return userRepository.findByActive(true);
    }
}
```

### 12.2. Class-based Projections (DTOs)

```java
// DTO class
public class UserDTO {
    private String name;
    private String email;
    private Integer age;
    
    public UserDTO(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
    
    // Getters
}

// Repository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query("SELECT new com.example.dto.UserDTO(u.name, u.email, u.age) " +
           "FROM User u WHERE u.active = :active")
    List<UserDTO> findUserDTOsByActive(@Param("active") Boolean active);
    
    @Query("SELECT new com.example.dto.UserDTO(u.name, u.email, u.age) " +
           "FROM User u WHERE u.id = :id")
    Optional<UserDTO> findUserDTOById(@Param("id") Long id);
}
```

### 12.3. Dynamic Projections

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    <T> List<T> findByActive(Boolean active, Class<T> type);
    <T> Optional<T> findById(Long id, Class<T> type);
}

// Usage
List<UserSummary> summaries = userRepository.findByActive(true, UserSummary.class);
List<UserDTO> dtos = userRepository.findByActive(true, UserDTO.class);
```

---

## 13. AUDITING

### 13.1. Enable Auditing

```java
@Configuration
@EnableJpaAuditing
public class JpaConfig {
    
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            // Get current user from SecurityContext
            Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
            
            if (auth == null || !auth.isAuthenticated()) {
                return Optional.of("system");
            }
            
            return Optional.of(auth.getName());
        };
    }
}
```

### 13.2. Auditable Entity

```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;
    
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;
    
    @LastModifiedBy
    @Column(nullable = false)
    private String lastModifiedBy;
    
    // Getters and setters
}

@Entity
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    
    // Inherited: createdDate, lastModifiedDate, createdBy, lastModifiedBy
}
```

### 13.3. Version Control (@Version)

```java
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private BigDecimal price;
    
    @Version
    private Long version;
    
    // Getters and setters
}

// Usage - Optimistic locking
@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Transactional
    public Product updatePrice(Long id, BigDecimal newPrice) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));
        
        product.setPrice(newPrice);
        
        return productRepository.save(product);
        // Throws OptimisticLockException if version doesn't match
    }
}
```

---

## 14. TRANSACTION MANAGEMENT

### 14.1. @Transactional

```java
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // Simple transaction
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    // Read-only transaction (optimization)
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // Custom timeout
    @Transactional(timeout = 5)
    public User updateUser(Long id, User user) {
        // Transaction times out after 5 seconds
        return userRepository.save(user);
    }
    
    // Propagation
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logActivity(String activity) {
        // Always creates new transaction
    }
    
    // Isolation
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
    
    // Rollback rules
    @Transactional(rollbackFor = Exception.class)
    public void dangerousOperation() {
        // Rollback on any Exception
    }
    
    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public void safeOperation() {
        // Don't rollback on IllegalArgumentException
    }
}
```

### 14.2. Propagation Types

| Propagation | Behavior |
|-------------|----------|
| **REQUIRED** | Join existing or create new (default) |
| **REQUIRES_NEW** | Always create new transaction |
| **NESTED** | Nested within existing transaction |
| **MANDATORY** | Must have existing transaction |
| **SUPPORTS** | Join if exists, non-transactional otherwise |
| **NOT_SUPPORTED** | Execute non-transactionally |
| **NEVER** | Throw exception if transaction exists |

### 14.3. Isolation Levels

| Isolation | Problems Prevented |
|-----------|-------------------|
| **READ_UNCOMMITTED** | None |
| **READ_COMMITTED** | Dirty reads |
| **REPEATABLE_READ** | Dirty reads, Non-repeatable reads |
| **SERIALIZABLE** | Dirty reads, Non-repeatable reads, Phantom reads |

### 14.4. Transaction Example

```java
@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private NotificationService notificationService;
    
    @Transactional
    public Order createOrder(OrderRequest request) {
        // 1. Create order
        Order order = new Order();
        order.setItems(request.getItems());
        order = orderRepository.save(order);
        
        // 2. Process payment (in same transaction)
        paymentService.processPayment(order.getId(), request.getPaymentInfo());
        
        // 3. Update inventory (in same transaction)
        inventoryService.reduceStock(request.getItems());
        
        // 4. Send notification (in new transaction - won't rollback)
        notificationService.sendOrderConfirmation(order.getId());
        
        return order;
    }
}

@Service
public class NotificationService {
    
    // New transaction - independent of parent
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendOrderConfirmation(Long orderId) {
        // Send email/SMS
        // This won't rollback even if parent transaction fails
    }
}
```

---

## 15. LOCKING

### 15.1. Optimistic Locking

```java
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private BigDecimal price;
    private Integer stock;
    
    @Version
    private Long version;
}

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Lock(LockModeType.OPTIMISTIC)
    Optional<Product> findById(Long id);
    
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<Product> findWithLockById(Long id);
}

// Usage
@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Transactional
    public void updateStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow();
        
        product.setStock(product.getStock() - quantity);
        
        productRepository.save(product);
        // Throws OptimisticLockException if version changed
    }
}
```

### 15.2. Pessimistic Locking

```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Pessimistic read lock
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithPessimisticRead(@Param("id") Long id);
    
    // Pessimistic write lock
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithPessimisticWrite(@Param("id") Long id);
    
    // Pessimistic force increment
    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    Optional<Product> findWithLockById(Long id);
}

// Usage
@Service
public class ProductService {
    
    @Transactional
    public void updateStockWithLock(Long productId, Integer quantity) {
        // Database-level lock
        Product product = productRepository
            .findByIdWithPessimisticWrite(productId)
            .orElseThrow();
        
        product.setStock(product.getStock() - quantity);
        
        productRepository.save(product);
        // Lock released at transaction end
    }
}
```

### 15.3. Lock Modes

| Lock Mode | Description |
|-----------|-------------|
| **OPTIMISTIC** | Optimistic lock check at transaction end |
| **OPTIMISTIC_FORCE_INCREMENT** | Force version increment |
| **PESSIMISTIC_READ** | Shared lock (other can read) |
| **PESSIMISTIC_WRITE** | Exclusive lock (block others) |
| **PESSIMISTIC_FORCE_INCREMENT** | Exclusive lock + force increment |
| **NONE** | No locking |
| **READ** | JPA 1.0 read lock |
| **WRITE** | JPA 1.0 write lock |

---

## 16. CACHING

### 16.1. Enable Caching

```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("users", "products");
    }
}

// With Caffeine
@Bean
public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("users");
    cacheManager.setCaffeine(Caffeine.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .maximumSize(100));
    return cacheManager;
}
```

### 16.2. Cache Annotations

```java
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // Cache result
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
    
    // Conditional caching
    @Cacheable(value = "users", key = "#id", 
               condition = "#id > 10")
    public User getUserByIdConditional(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
    
    // Update cache
    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    // Remove from cache
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    // Clear entire cache
    @CacheEvict(value = "users", allEntries = true)
    public void clearCache() {
        // Cache cleared
    }
    
    // Multiple cache operations
    @Caching(
        cacheable = @Cacheable("users"),
        evict = @CacheEvict(value = "usersummaries", allEntries = true)
    )
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
```

### 16.3. Second-Level Cache (Hibernate)

```properties
# application.properties
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory
spring.jpa.properties.hibernate.javax.cache.provider=org.ehcache.jsr107.EhcacheCachingProvider
```

```java
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
}
```

---

## 17. SPRING DATA JDBC

### 17.1. Configuration

```java
@Configuration
@EnableJdbcRepositories
public class JdbcConfig extends AbstractJdbcConfiguration {
    
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }
    
    @Bean
    public NamedParameterJdbcOperations jdbcOperations(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
    
    @Bean
    public TransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```

### 17.2. Entity

```java
@Table("users")
public class User {
    
    @Id
    private Long id;
    
    private String name;
    private String email;
    
    // No @Entity, @Column annotations needed
    // Simpler than JPA
}
```

### 17.3. Repository

```java
public interface UserRepository extends CrudRepository<User, Long> {
    
    @Query("SELECT * FROM users WHERE name = :name")
    List<User> findByName(@Param("name") String name);
    
    @Modifying
    @Query("UPDATE users SET email = :email WHERE id = :id")
    boolean updateEmail(@Param("id") Long id, @Param("email") String email);
}
```

---

## 18. BEST PRACTICES

### 18.1. Entity Design

✅ **DO:**
- Use meaningful names
- Keep entities focused (Single Responsibility)
- Use @Version for optimistic locking
- Use appropriate fetch types
- Implement equals() and hashCode() for entities in collections

❌ **DON'T:**
- Use bidirectional associations unnecessarily
- Expose entities directly in API
- Use EAGER loading everywhere
- Forget @Transactional for write operations

```java
// ✅ GOOD
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
    
    @Version
    private Long version;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
```

### 18.2. Repository Design

```java
// ✅ GOOD - Clear, specific methods
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByActiveTrue();
    Page<User> findByAgeGreaterThan(Integer age, Pageable pageable);
}

// ❌ BAD - Generic, unclear
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> find(String param);
    User get(Long id);
}
```

### 18.3. Query Optimization

```java
// ✅ GOOD - Fetch only needed data
@Query("SELECT new com.example.dto.UserDTO(u.id, u.name) FROM User u")
List<UserDTO> findAllUserDTOs();

// ✅ GOOD - Join fetch to avoid N+1
@Query("SELECT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id = :id")
User findByIdWithOrders(@Param("id") Long id);

// ❌ BAD - N+1 query problem
List<User> users = userRepository.findAll();
users.forEach(user -> {
    user.getOrders().size(); // Triggers separate query for each user
});
```

### 18.4. Transaction Management

```java
// ✅ GOOD
@Service
public class UserService {
    
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }
}

// ❌ BAD - Missing @Transactional
@Service
public class UserService {
    public User createUser(User user) {
        return userRepository.save(user); // May fail silently
    }
}
```

---

## 19. CÂU HỎI MẪU CHO KỲ THI

### 19.1. Câu hỏi lý thuyết

#### Câu 1: Sự khác biệt giữa JpaRepository và CrudRepository?

**Trả lời**:
- **CrudRepository**: Basic CRUD operations
- **JpaRepository**: Extends PagingAndSortingRepository, adds JPA-specific methods:
  - `saveAllAndFlush()`
  - `deleteInBatch()`
  - Returns `List` instead of `Iterable`
  - `findAll(Example)`

**Hierarchy**: CrudRepository → PagingAndSortingRepository → JpaRepository

---

#### Câu 2: @Transactional(readOnly = true) có ý nghĩa gì?

**Trả lời**: 
- Optimizes read-only operations
- Hibernate doesn't check for dirty objects
- Some databases optimize read-only transactions
- Prevents accidental modifications
- Better performance for queries

---

#### Câu 3: EAGER vs LAZY loading khác nhau như thế nào?

**Trả lời**:

| Aspect | EAGER | LAZY |
|--------|-------|------|
| **Loading** | Immediate | On-demand |
| **Performance** | Slower initial load | Faster initial, slower later |
| **N+1 Problem** | No | Yes (if not careful) |
| **Default for** | @ManyToOne, @OneToOne | @OneToMany, @ManyToMany |
| **Best for** | Small, always-needed data | Large, optional data |

**Best Practice**: Use LAZY by default, fetch explicitly when needed

---

#### Câu 4: @Modifying annotation dùng khi nào?

**Trả lời**: 
- Required for UPDATE, DELETE queries in @Query
- Must be used with @Transactional
- Optional parameters:
  - `clearAutomatically = true`: Clear persistence context
  - `flushAutomatically = true`: Flush before executing

```java
@Modifying
@Transactional
@Query("UPDATE User u SET u.status = :status WHERE u.id = :id")
int updateStatus(@Param("id") Long id, @Param("status") String status);
```

---

#### Câu 5: Cascade types trong JPA là gì?

**Trả lời**:
- **PERSIST**: Save child when parent saved
- **MERGE**: Update child when parent updated
- **REMOVE**: Delete child when parent deleted
- **REFRESH**: Reload child when parent refreshed
- **DETACH**: Detach child when parent detached
- **ALL**: All above operations

**orphanRemoval = true**: Delete child when removed from collection

---

### 19.2. Câu hỏi code-based

#### Câu 6: Code sau có vấn đề gì?

```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public void updateUser(Long id, String email) {
        User user = userRepository.findById(id).orElseThrow();
        user.setEmail(email);
        // Missing save?
    }
}
```

**Trả lời**: Missing `@Transactional` annotation. Changes won't be persisted.

**Fix:**
```java
@Transactional
public void updateUser(Long id, String email) {
    User user = userRepository.findById(id).orElseThrow();
    user.setEmail(email);
    // Saved automatically at transaction end
}
```

---

#### Câu 7: Làm thế nào để tránh N+1 query problem?

```java
// ❌ BAD - N+1 problem
@OneToMany(mappedBy = "user")
private List<Order> orders;

List<User> users = userRepository.findAll(); // 1 query
users.forEach(user -> {
    user.getOrders().size(); // N queries
});

// ✅ GOOD - Join fetch
@Query("SELECT u FROM User u LEFT JOIN FETCH u.orders")
List<User> findAllWithOrders();

// Or Entity Graph
@EntityGraph(attributePaths = {"orders"})
List<User> findAll();
```

---

#### Câu 8: Implement pagination và sorting?

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping
    public Page<User> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        
        Pageable pageable = PageRequest.of(page, size, 
            Sort.by(sortBy).ascending());
        
        return userRepository.findAll(pageable);
    }
}
```

---

### 19.3. Scenario-based Questions

#### Câu 9: Implement soft delete?

```java
@Entity
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private boolean deleted = false;
}

// Repository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // findAll() automatically filters deleted = false
    
    @Query("SELECT u FROM User u WHERE u.deleted = true")
    List<User> findDeleted();
}
```

---

#### Câu 10: Handle optimistic locking exception?

```java
@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public Product updatePrice(Long id, BigDecimal price) {
        int maxRetries = 3;
        int attempt = 0;
        
        while (attempt < maxRetries) {
            try {
                Product product = productRepository.findById(id)
                    .orElseThrow();
                product.setPrice(price);
                return productRepository.save(product);
            } catch (OptimisticLockException e) {
                attempt++;
                if (attempt >= maxRetries) {
                    throw new RuntimeException("Failed after " + maxRetries + " attempts");
                }
            }
        }
        
        throw new RuntimeException("Unexpected error");
    }
}
```

---

## 20. TÓM TẮT VÀ MẸO THI

### 20.1. Repository Hierarchy

```
Repository
    ↓
CrudRepository (save, findById, delete, count)
    ↓
PagingAndSortingRepository (findAll with Pageable)
    ↓
JpaRepository (batch operations, flush)
```

### 20.2. Key Annotations

| Annotation | Purpose |
|------------|---------|
| `@Entity` | JPA entity |
| `@Table` | Table mapping |
| `@Id` | Primary key |
| `@GeneratedValue` | ID generation strategy |
| `@Column` | Column mapping |
| `@Transient` | Not persisted |
| `@Enumerated` | Enum mapping |
| `@Temporal` | Date/time mapping |
| `@OneToOne` | One-to-one relationship |
| `@OneToMany` | One-to-many relationship |
| `@ManyToOne` | Many-to-one relationship |
| `@ManyToMany` | Many-to-many relationship |
| `@JoinColumn` | Foreign key column |
| `@JoinTable` | Join table |
| `@Query` | Custom query |
| `@Modifying` | UPDATE/DELETE query |
| `@Transactional` | Transaction boundary |
| `@Version` | Optimistic locking |
| `@Lock` | Pessimistic locking |
| `@Cacheable` | Cache result |

### 20.3. Common Pitfalls

❌ **Mistake 1**: Missing @Transactional
```java
// BAD
public void updateUser(User user) {
    userRepository.save(user);
}

// GOOD
@Transactional
public void updateUser(User user) {
    userRepository.save(user);
}
```

❌ **Mistake 2**: N+1 Query Problem
```java
// BAD
List<User> users = userRepository.findAll();
users.forEach(u -> u.getOrders().size()); // N queries

// GOOD
@Query("SELECT u FROM User u LEFT JOIN FETCH u.orders")
List<User> findAllWithOrders();
```

❌ **Mistake 3**: EAGER loading everywhere
```java
// BAD
@OneToMany(fetch = FetchType.EAGER)
private List<Order> orders;

// GOOD
@OneToMany(fetch = FetchType.LAZY)
private List<Order> orders;
```

### 20.4. Mẹo làm bài thi

1. ✅ **Repository Hierarchy**: JpaRepository > PagingAndSortingRepository > CrudRepository
2. ✅ **@Transactional**: Required for write operations and explicit reads
3. ✅ **LAZY Loading**: Default for collections, better performance
4. ✅ **@Modifying**: Required for UPDATE/DELETE with @Query
5. ✅ **Cascade Types**: PERSIST, MERGE, REMOVE, REFRESH, DETACH, ALL
6. ✅ **Fetch Strategies**: LAZY (default collections), EAGER (default single)
7. ✅ **Optimistic Locking**: @Version, check at commit
8. ✅ **Pessimistic Locking**: @Lock, database-level lock
9. ✅ **Pagination**: Pageable, Page, Slice
10. ✅ **Projections**: Interface-based, class-based (DTO)

### 20.5. Checklist ôn tập

- [ ] Repository interfaces và hierarchy
- [ ] Entity mapping annotations
- [ ] Query method naming conventions
- [ ] @Query với JPQL và native SQL
- [ ] @Modifying queries
- [ ] Entity relationships (@OneToOne, @OneToMany, etc.)
- [ ] Fetch types (LAZY vs EAGER)
- [ ] Cascade types
- [ ] Pagination và sorting
- [ ] Specifications
- [ ] Projections và DTOs
- [ ] Auditing
- [ ] Transaction management
- [ ] Locking (optimistic và pessimistic)
- [ ] N+1 query problem và solutions

---

## KẾT LUẬN

Spring Data là một phần quan trọng trong Spring Framework và certification exam. Để thành công:

- ✅ Hiểu repository hierarchy
- ✅ Master entity mapping và relationships
- ✅ Biết query methods và @Query
- ✅ Understand transaction management
- ✅ Know fetch strategies và performance optimization
- ✅ Master pagination và sorting

### Key Points:

> **Spring Data = Repository Pattern + ORM**
>
> Essential concepts:
> - JpaRepository > PagingAndSortingRepository > CrudRepository
> - @Entity + @Table for mapping
> - Query methods from method names
> - @Query for custom queries
> - @Transactional for consistency
> - LAZY loading for performance

**Common Patterns:**
```java
// Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Page<User> findByActive(Boolean active, Pageable pageable);
}

// Service
@Service
public class UserService {
    @Transactional(readOnly = true)
    public List<User> getAll() { }
    
    @Transactional
    public User create(User user) { }
}
```

Hãy practice với các examples trong tài liệu này và focus vào understanding concepts rather than memorization. Spring Data simplifies data access significantly!

---

**Chúc bạn thành công với Spring Professional Certification!** 🚀💾

*Tài liệu được tạo ngày 26/12/2024*
