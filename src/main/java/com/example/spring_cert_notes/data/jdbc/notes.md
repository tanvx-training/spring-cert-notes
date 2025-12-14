# NGÀY 10-11: SPRING JDBC & TRANSACTION MANAGEMENT

## 📚 MỤC TIÊU HỌC TẬP

### 1. Thành thạo JdbcTemplate với RowMapper, ResultSetExtractor
### 2. Nắm vững @Transactional và 7 propagation levels
### 3. Configure rollback rules cho checked/unchecked exceptions

---

## 🎯 PHẦN 1: JDBCTEMPLATE

### JdbcTemplate Methods

| Method | Use Case | Return |
|--------|----------|--------|
| `query()` | SELECT multiple rows | `List<T>` |
| `queryForObject()` | SELECT single row/value | `T` |
| `update()` | INSERT/UPDATE/DELETE | `int` (affected rows) |
| `batchUpdate()` | Batch operations | `int[]` |
| `execute()` | DDL statements | `void` |

### RowMapper - Map single row

```java
// Lambda RowMapper
RowMapper<User> mapper = (rs, rowNum) -> 
    new User(rs.getLong("id"), rs.getString("name"));

// Class RowMapper
@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        return user;
    }
}
```

### Query Examples

```java
// Find all
List<User> users = jdbcTemplate.query(
    "SELECT * FROM users", 
    userRowMapper
);

// Find by id
User user = jdbcTemplate.queryForObject(
    "SELECT * FROM users WHERE id = ?",
    userRowMapper,
    id
);

// Find with parameters
List<User> users = jdbcTemplate.query(
    "SELECT * FROM users WHERE name LIKE ?",
    userRowMapper,
    "%" + name + "%"
);

// Count
Integer count = jdbcTemplate.queryForObject(
    "SELECT COUNT(*) FROM users",
    Integer.class
);
```

### Update Examples

```java
// Insert
int rows = jdbcTemplate.update(
    "INSERT INTO users (name, email) VALUES (?, ?)",
    user.getName(), user.getEmail()
);

// Update
int rows = jdbcTemplate.update(
    "UPDATE users SET name = ? WHERE id = ?",
    user.getName(), user.getId()
);

// Delete
int rows = jdbcTemplate.update(
    "DELETE FROM users WHERE id = ?",
    id
);
```

### NamedParameterJdbcTemplate

```java
// More readable with named parameters
String sql = "SELECT * FROM users WHERE name = :name AND email = :email";

MapSqlParameterSource params = new MapSqlParameterSource()
    .addValue("name", name)
    .addValue("email", email);

List<User> users = namedTemplate.query(sql, params, userRowMapper);
```

### SimpleJdbcInsert

```java
// Auto-generate ID
SimpleJdbcInsert insert = new SimpleJdbcInsert(dataSource)
    .withTableName("users")
    .usingGeneratedKeyColumns("id");

Map<String, Object> params = new HashMap<>();
params.put("name", "John");
params.put("email", "john@example.com");

Long id = insert.executeAndReturnKey(params).longValue();
```

**Xem code:** `UserDao.java`, `UserRowMapper.java`

---

## 🎯 PHẦN 2: 7 TRANSACTION PROPAGATION LEVELS

### Propagation Overview

```
┌─────────────────────────────────────────────────────────────┐
│              TRANSACTION PROPAGATION                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  REQUIRED (default)                                         │
│  ├─ Existing TX? → Join it                                 │
│  └─ No TX? → Create new                                    │
│                                                             │
│  REQUIRES_NEW                                               │
│  ├─ Existing TX? → Suspend, create new                     │
│  └─ No TX? → Create new                                    │
│                                                             │
│  SUPPORTS                                                   │
│  ├─ Existing TX? → Join it                                 │
│  └─ No TX? → Run without TX                                │
│                                                             │
│  NOT_SUPPORTED                                              │
│  ├─ Existing TX? → Suspend it                              │
│  └─ Always runs without TX                                 │
│                                                             │
│  MANDATORY                                                  │
│  ├─ Existing TX? → Join it                                 │
│  └─ No TX? → EXCEPTION!                                    │
│                                                             │
│  NEVER                                                      │
│  ├─ Existing TX? → EXCEPTION!                              │
│  └─ No TX? → Run without TX                                │
│                                                             │
│  NESTED                                                     │
│  ├─ Existing TX? → Create savepoint                        │
│  └─ No TX? → Create new (like REQUIRED)                    │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 1. REQUIRED (Default)

```java
@Transactional(propagation = Propagation.REQUIRED)
public void transfer(Long from, Long to, double amount) {
    debit(from, amount);   // Joins this transaction
    credit(to, amount);    // Joins this transaction
}
```

**Behavior:**
- If transaction exists → Join it
- If no transaction → Create new
- Most common, default behavior

### 2. REQUIRES_NEW

```java
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void logAudit(String action) {
    // Always runs in NEW transaction
    // Commits independently of outer transaction
}
```

**Behavior:**
- Always creates new transaction
- Suspends current transaction (if any)
- Commits/rollbacks independently

**Use case:** Audit logs that must be saved even if main transaction fails

### 3. SUPPORTS

```java
@Transactional(propagation = Propagation.SUPPORTS)
public User findById(Long id) {
    // Joins transaction if exists
    // Runs without transaction otherwise
}
```

**Behavior:**
- If transaction exists → Join it
- If no transaction → Run without transaction

**Use case:** Read operations that can work with or without transaction

### 4. NOT_SUPPORTED

```java
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public void generateReport() {
    // Always runs WITHOUT transaction
    // Suspends current transaction if exists
}
```

**Behavior:**
- Always runs without transaction
- Suspends current transaction (if any)

**Use case:** Long-running read operations

### 5. MANDATORY

```java
@Transactional(propagation = Propagation.MANDATORY)
public void debit(Long userId, double amount) {
    // MUST be called within existing transaction
    // Throws exception if no transaction
}
```

**Behavior:**
- Must have existing transaction
- Throws `IllegalTransactionStateException` if no transaction

**Use case:** Methods that should never run standalone

### 6. NEVER

```java
@Transactional(propagation = Propagation.NEVER)
public void healthCheck() {
    // Must NOT have transaction
    // Throws exception if transaction exists
}
```

**Behavior:**
- Must NOT have transaction
- Throws exception if transaction exists

**Use case:** Operations that must not be transactional

### 7. NESTED

```java
@Transactional(propagation = Propagation.NESTED)
public void addBonus(Long userId, double amount) {
    // Creates savepoint within outer transaction
    // Can rollback independently
}
```

**Behavior:**
- Creates savepoint within outer transaction
- Can rollback to savepoint without affecting outer
- If no transaction → Creates new (like REQUIRED)

**Use case:** Optional operations that can fail independently

**Xem code:** `AccountService.java`, `AuditLogService.java`

---

## 🎯 PHẦN 3: ROLLBACK RULES

### Default Behavior

```
RuntimeException (unchecked) → ROLLBACK
Exception (checked)          → NO ROLLBACK
```

### Customize Rollback

```java
// Rollback for checked exception
@Transactional(rollbackFor = IOException.class)
public void method() throws IOException { }

// Rollback for all exceptions
@Transactional(rollbackFor = Exception.class)
public void method() throws Exception { }

// Don't rollback for specific exception
@Transactional(noRollbackFor = IllegalArgumentException.class)
public void method() { }

// Combined rules
@Transactional(
    rollbackFor = {IOException.class, BusinessException.class},
    noRollbackFor = {IllegalArgumentException.class}
)
public void method() { }
```

### Rollback Decision Table

| Exception Type | Default | With rollbackFor | With noRollbackFor |
|---------------|---------|------------------|-------------------|
| RuntimeException | ROLLBACK | ROLLBACK | NO ROLLBACK |
| Checked Exception | NO ROLLBACK | ROLLBACK | NO ROLLBACK |

**Xem code:** `RollbackService.java`

---

## 🎯 PHẦN 4: @TRANSACTIONAL ATTRIBUTES

### Full Syntax

```java
@Transactional(
    propagation = Propagation.REQUIRED,
    isolation = Isolation.DEFAULT,
    timeout = 30,
    readOnly = false,
    rollbackFor = {IOException.class},
    noRollbackFor = {IllegalArgumentException.class}
)
public void method() { }
```

### Attributes

| Attribute | Description | Default |
|-----------|-------------|---------|
| `propagation` | Transaction propagation | REQUIRED |
| `isolation` | Isolation level | DEFAULT |
| `timeout` | Timeout in seconds | -1 (no timeout) |
| `readOnly` | Read-only hint | false |
| `rollbackFor` | Exceptions to rollback | RuntimeException |
| `noRollbackFor` | Exceptions to not rollback | - |

### Isolation Levels

```java
@Transactional(isolation = Isolation.READ_UNCOMMITTED)  // Dirty reads
@Transactional(isolation = Isolation.READ_COMMITTED)    // No dirty reads
@Transactional(isolation = Isolation.REPEATABLE_READ)   // No non-repeatable reads
@Transactional(isolation = Isolation.SERIALIZABLE)      // Full isolation
```

### Read-Only Optimization

```java
@Transactional(readOnly = true)
public List<User> findAll() {
    // Hints to database for optimization
    // No flush needed
}
```

---

## ✅ CHECKLIST HOÀN THÀNH

### JdbcTemplate
- [ ] query() với RowMapper
- [ ] queryForObject() cho single value
- [ ] update() cho INSERT/UPDATE/DELETE
- [ ] batchUpdate() cho batch operations
- [ ] NamedParameterJdbcTemplate
- [ ] SimpleJdbcInsert

### 7 Propagation Levels
- [ ] REQUIRED - Join or create
- [ ] REQUIRES_NEW - Always new
- [ ] SUPPORTS - Join if exists
- [ ] NOT_SUPPORTED - Never transactional
- [ ] MANDATORY - Must have transaction
- [ ] NEVER - Must not have transaction
- [ ] NESTED - Savepoint

### Rollback Rules
- [ ] Default: RuntimeException → rollback
- [ ] Default: Checked → no rollback
- [ ] rollbackFor attribute
- [ ] noRollbackFor attribute
- [ ] Combined rules

### Other Attributes
- [ ] isolation levels
- [ ] timeout
- [ ] readOnly

---

## 🚀 CÁCH CHẠY DEMO

```bash
mvn exec:java -Dexec.mainClass="com.example.spring_cert_notes.data.jdbc.JdbcDemo"
```

---

## 📝 GHI CHÚ QUAN TRỌNG

### Best Practices

1. **Use REQUIRED for most cases**
2. **Use REQUIRES_NEW for audit logs**
3. **Use readOnly=true for queries**
4. **Always specify rollbackFor for checked exceptions**
5. **Keep transactions short**

### Common Mistakes

1. ❌ Self-invocation bypasses proxy
2. ❌ Forgetting @EnableTransactionManagement
3. ❌ Checked exception not rolling back
4. ❌ Long-running operations in transaction
5. ❌ Wrong propagation level

---

## 📚 FILES TRONG PACKAGE

1. `User.java` - Entity
2. `UserRowMapper.java` - RowMapper example
3. `UserDao.java` - JdbcTemplate examples
4. `AccountService.java` - Propagation examples
5. `AuditLogService.java` - REQUIRES_NEW example
6. `RollbackService.java` - Rollback rules
7. `JdbcConfig.java` - Configuration
8. `JdbcDemo.java` - Main demo
