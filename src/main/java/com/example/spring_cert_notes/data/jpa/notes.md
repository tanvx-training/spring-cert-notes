# NGÀY 12-13: SPRING DATA JPA

## 📚 MỤC TIÊU HỌC TẬP

### 1. Tạo Spring Data repositories với custom queries
### 2. Sử dụng query methods, @Query, Specifications
### 3. Hiểu N+1 problem và optimize với @EntityGraph

---

## 🎯 PHẦN 1: QUERY METHODS (Derived Queries)

### Naming Convention

```
findBy + Property + Condition
```

### 10+ Query Method Examples

```java
// 1. Simple find
Optional<User> findByEmail(String email);

// 2. Multiple properties (AND)
List<User> findByLastNameAndFirstName(String lastName, String firstName);

// 3. OR condition
List<User> findByFirstNameOrLastName(String firstName, String lastName);

// 4. LIKE - Contains
List<User> findByEmailContaining(String part);

// 5. LIKE - StartsWith
List<User> findByLastNameStartingWith(String prefix);

// 6. LIKE - EndsWith
List<User> findByFirstNameEndingWith(String suffix);

// 7. Comparison
List<User> findByCreatedAtAfter(LocalDateTime date);
List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

// 8. Boolean
List<User> findByActiveTrue();
List<User> findByActiveFalse();

// 9. NULL check
List<User> findByEmailIsNotNull();

// 10. IN clause
List<User> findByLastNameIn(List<String> lastNames);

// 11. Count
long countByActive(boolean active);

// 12. Exists
boolean existsByEmail(String email);

// 13. Delete
void deleteByEmail(String email);

// 14. Sorting
List<User> findByActiveOrderByLastNameAsc(boolean active);

// 15. First/Top
Optional<User> findFirstByOrderByCreatedAtDesc();
List<User> findTop3ByActiveOrderByCreatedAtDesc(boolean active);
```

**Xem code:** `UserRepository.java`

---

## 🎯 PHẦN 2: @QUERY ANNOTATION

### JPQL Queries

```java
// Simple JPQL
@Query("SELECT u FROM User u WHERE u.email LIKE %:domain")
List<User> findByEmailDomain(@Param("domain") String domain);

// Case-insensitive
@Query("SELECT u FROM User u WHERE LOWER(u.firstName) = LOWER(:name)")
List<User> findByNameIgnoreCase(@Param("name") String name);

// JOIN FETCH (N+1 solution)
@Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.id = :id")
Optional<User> findByIdWithOrders(@Param("id") Long id);

// Aggregate
@Query("SELECT SUM(o.amount) FROM Order o WHERE o.user.id = :userId")
BigDecimal getTotalAmountByUser(@Param("userId") Long userId);
```

### Native SQL Queries

```java
@Query(value = "SELECT * FROM users WHERE created_at > ?1", nativeQuery = true)
List<User> findRecentUsersNative(LocalDateTime date);

@Query(value = "SELECT COUNT(*) FROM users WHERE active = true", nativeQuery = true)
long countActiveUsersNative();
```

### @Modifying (UPDATE/DELETE)

```java
@Modifying
@Query("UPDATE User u SET u.active = :active WHERE u.id = :id")
int updateActiveStatus(@Param("id") Long id, @Param("active") boolean active);

@Modifying
@Query("DELETE FROM User u WHERE u.active = false")
int deleteInactiveUsers();
```

**Note:** @Modifying requires @Transactional

---

## 🎯 PHẦN 3: PAGINATION & SORTING

### Pageable

```java
// Repository
Page<User> findByActive(boolean active, Pageable pageable);

// Service
Pageable pageable = PageRequest.of(
    0,                              // page number (0-based)
    10,                             // page size
    Sort.by("lastName").ascending() // sorting
);

Page<User> page = userRepository.findByActive(true, pageable);

// Page info
page.getContent();        // List<User>
page.getTotalElements();  // Total count
page.getTotalPages();     // Total pages
page.getNumber();         // Current page
page.getSize();           // Page size
page.hasNext();           // Has next page
page.hasPrevious();       // Has previous page
```

### Sort

```java
// Simple sort
Sort sort = Sort.by("lastName");

// Direction
Sort sort = Sort.by("lastName").ascending();
Sort sort = Sort.by("lastName").descending();

// Multiple fields
Sort sort = Sort.by("lastName").ascending()
                .and(Sort.by("firstName").ascending());

// Using Sort.Order
Sort sort = Sort.by(
    Sort.Order.asc("lastName"),
    Sort.Order.desc("createdAt")
);
```

### Combined

```java
Pageable pageable = PageRequest.of(0, 10, Sort.by("lastName").ascending());
Page<User> page = userRepository.findAll(pageable);
```

---

## 🎯 PHẦN 4: SPECIFICATIONS (Dynamic Queries)

### Creating Specifications

```java
public class UserSpecifications {
    
    public static Specification<User> hasFirstName(String firstName) {
        return (root, query, cb) -> {
            if (firstName == null) {
                return cb.conjunction(); // Always true
            }
            return cb.like(cb.lower(root.get("firstName")), 
                          "%" + firstName.toLowerCase() + "%");
        };
    }
    
    public static Specification<User> isActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("active"), active);
        };
    }
    
    public static Specification<User> createdAfter(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            return cb.greaterThan(root.get("createdAt"), date);
        };
    }
}
```

### Using Specifications

```java
// Repository must extend JpaSpecificationExecutor
public interface UserRepository extends JpaRepository<User, Long>, 
                                        JpaSpecificationExecutor<User> { }

// Combining specifications
Specification<User> spec = Specification
    .where(UserSpecifications.hasFirstName(firstName))
    .and(UserSpecifications.hasLastName(lastName))
    .and(UserSpecifications.isActive(active));

List<User> users = userRepository.findAll(spec);

// With pagination
Page<User> page = userRepository.findAll(spec, pageable);
```

**Xem code:** `UserSpecifications.java`

---

## 🎯 PHẦN 5: N+1 PROBLEM & @ENTITYGRAPH

### N+1 Problem

```java
// BAD: N+1 queries
List<User> users = userRepository.findAll();  // 1 query
for (User user : users) {
    user.getOrders().size();  // N queries (one per user)
}
// Total: 1 + N queries!
```

### Solution 1: JOIN FETCH

```java
@Query("SELECT u FROM User u JOIN FETCH u.orders")
List<User> findAllWithOrders();
// Single query with JOIN
```

### Solution 2: @EntityGraph

```java
// On repository method
@EntityGraph(attributePaths = {"orders"})
@Query("SELECT u FROM User u")
List<User> findAllWithOrdersGraph();

// Using named entity graph
@Entity
@NamedEntityGraph(
    name = "User.withOrders",
    attributeNodes = @NamedAttributeNode("orders")
)
public class User { }

// In repository
@EntityGraph(value = "User.withOrders")
Optional<User> findWithOrdersById(Long id);
```

### Solution 3: Batch Fetching

```java
// In entity
@OneToMany(mappedBy = "user")
@BatchSize(size = 10)  // Fetch 10 at a time
private List<Order> orders;
```

### Comparison

| Solution | Pros | Cons |
|----------|------|------|
| JOIN FETCH | Single query | Cartesian product |
| @EntityGraph | Declarative | Same as JOIN FETCH |
| @BatchSize | Reduces queries | Still multiple queries |

---

## 🎯 PHẦN 6: REPOSITORY HIERARCHY

```
Repository (marker)
    └── CrudRepository
        └── PagingAndSortingRepository
            └── JpaRepository
                └── JpaSpecificationExecutor (interface)
```

### Methods by Interface

**CrudRepository:**
- save(), saveAll()
- findById(), findAll(), findAllById()
- count(), existsById()
- delete(), deleteById(), deleteAll()

**PagingAndSortingRepository:**
- findAll(Sort)
- findAll(Pageable)

**JpaRepository:**
- flush()
- saveAndFlush()
- deleteInBatch()
- getOne() / getById()

**JpaSpecificationExecutor:**
- findAll(Specification)
- findOne(Specification)
- count(Specification)

---

## ✅ CHECKLIST HOÀN THÀNH

### Query Methods (10+)
- [ ] findBy + Property
- [ ] findBy + And/Or
- [ ] findBy + Containing/StartingWith/EndingWith
- [ ] findBy + After/Before/Between
- [ ] findBy + True/False
- [ ] findBy + IsNull/IsNotNull
- [ ] findBy + In
- [ ] countBy / existsBy
- [ ] deleteBy
- [ ] findFirst / findTop

### @Query
- [ ] JPQL queries
- [ ] Native SQL queries
- [ ] @Param for named parameters
- [ ] @Modifying for UPDATE/DELETE

### Pagination & Sorting
- [ ] PageRequest.of()
- [ ] Sort.by()
- [ ] Page methods (getContent, getTotalPages, etc.)

### Specifications
- [ ] Create Specification classes
- [ ] Combine with and(), or()
- [ ] Use with findAll(Specification)

### N+1 Optimization
- [ ] Identify N+1 problem
- [ ] JOIN FETCH solution
- [ ] @EntityGraph solution
- [ ] @BatchSize solution

---

## 🚀 CÁCH CHẠY DEMO

```bash
mvn exec:java -Dexec.mainClass="com.example.spring_cert_notes.data.jpa.JpaDemo"
```

---

## 📝 GHI CHÚ QUAN TRỌNG

### Best Practices

1. **Use query methods for simple queries**
2. **Use @Query for complex queries**
3. **Use Specifications for dynamic queries**
4. **Always handle N+1 with @EntityGraph or JOIN FETCH**
5. **Use readOnly=true for read operations**

### Common Mistakes

1. ❌ N+1 queries (lazy loading in loop)
2. ❌ Missing @Transactional with @Modifying
3. ❌ Wrong query method naming
4. ❌ Cartesian product with multiple collections
5. ❌ Not using pagination for large datasets

---

## 📚 FILES TRONG PACKAGE

**Entities:**
1. `User.java` - User entity with @NamedEntityGraph
2. `Order.java` - Order entity
3. `OrderStatus.java` - Enum

**Repositories:**
4. `UserRepository.java` - 15+ query methods
5. `OrderRepository.java` - Order queries

**Specifications:**
6. `UserSpecifications.java` - Dynamic query specs

**Service & Config:**
7. `UserService.java` - Service layer
8. `JpaConfig.java` - JPA configuration
9. `JpaDemo.java` - Main demo
