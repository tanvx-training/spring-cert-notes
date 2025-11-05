## ❓ Câu hỏi: Phân biệt checked và unchecked exceptions? Tại sao Spring ưu tiên unchecked exceptions? Data access exception hierarchy là gì?

### 1. 📬 Checked vs. Unchecked Exceptions

#### A. Checked Exceptions (Ngoại lệ *bắt buộc* kiểm tra)

Đây là các ngoại lệ (extends `Exception` nhưng *không* extends `RuntimeException`) mà **trình biên dịch (compiler) của Java ép buộc bạn phải xử lý**.

* **Định nghĩa:** Như bạn nói, chúng phải được khai báo (`throws ...`) hoặc được xử lý (`try-catch`).
* **Ví dụ:** `IOException`, `SQLException`, `ClassNotFoundException`.
* **Phép ví von (Analogy):**
  Hãy nghĩ về Checked Exception như một **"Thư bảo đảm" (Certified Mail)**.
    1.  Khi một phương thức (ví dụ: `readFile()`) "ném" ra `IOException`, nó giống như gửi một lá thư bảo đảm.
    2.  Người gọi (caller) nhận được lá thư này **bắt buộc** phải ký nhận. "Ký nhận" ở đây có hai cách:
        * **Xử lý ngay (Dùng `try-catch`):** "Tôi mở lá thư ra, đọc, và xử lý vấn đề ngay tại đây."
        * **Khai báo `throws` (Đẩy trách nhiệm):** "Tôi ký nhận, nhưng tôi không mở. Tôi đưa lá thư này cho sếp của tôi (phương thức gọi tôi) xử lý."
    3.  Nếu bạn không làm 1 trong 2, **trình biên dịch (compiler) sẽ báo lỗi** và không cho chạy.

* **Ưu/Nhược điểm:** Như bạn đã nêu. Ưu điểm là "an toàn" (không bao giờ quên xử lý), nhưng nhược điểm là làm code rất "rườm rà" (cluttered) và "phụ thuộc cứng" (coupling).

#### B. Unchecked Exceptions (Ngoại lệ *không* bắt buộc kiểm tra)

Đây là các ngoại lệ (extends `RuntimeException`) mà **compiler không ép bạn xử lý**.

* **Định nghĩa:** Như bạn nói, chúng kế thừa từ `RuntimeException`.
* **Ví dụ:** `NullPointerException`, `IllegalArgumentException`, `ArrayIndexOutOfBoundsException`.
* **Phép ví von (Analogy):**
  Đây giống như một **"Bưu thiếp" (Postcard)**.
    1.  Khi một phương thức ném ra `NullPointerException`, nó chỉ "gửi" bưu thiếp đi.
    2.  Người gọi **không bắt buộc** phải `try-catch`. Bưu thiếp cứ thế được chuyển tiếp lên (gọi là "bubble up" - nổi bọt) qua từng lớp, cho đến khi có ai đó (thường là một bộ xử lý lỗi cấp cao nhất) bắt được nó, hoặc nó làm "sập" (crash) luồng hiện tại.
    3.  Trình biên dịch không quan tâm liệu bạn có xử lý nó hay không.

* **Ưu/Nhược điểm:** Như bạn nói. Ưu điểm là code "sạch" (bạn chỉ `catch` khi bạn *thực sự* muốn xử lý). Nhược điểm là bạn có thể "quên" xử lý một lỗi nghiêm trọng.

---

### 2. 💡 Tại sao Spring ưu tiên Unchecked Exceptions?

Đây là điểm mấu chốt. Spring cho rằng:
> Hầu hết các ngoại lệ về cơ sở hạ tầng (như lỗi data access) là **không thể phục hồi (unrecoverable)** ở tầng nghiệp vụ (business layer).

**Vấn đề của Checked Exception (Cách cũ):**
Hãy tưởng tượng `UserService` của bạn gọi `userRepository.save()`. Nếu `userRepository.save()` ném ra `SQLException` (một *checked* exception):

1.  `UserService` (lớp nghiệp vụ) của bạn **bị ép** phải `try-catch(SQLException)`.
2.  Nhưng `UserService` có thể làm gì khi database bị sập? **Không gì cả.** Nó không phải là trách nhiệm của nó.
3.  Kết quả là, code nghiệp vụ của bạn bị "ô nhiễm" (polluted) bởi logic xử lý lỗi của tầng *cơ sở hạ tầng (infrastructure)*. Thường thì bạn chỉ `catch (SQLException e) { throw new RuntimeException(e); }`, điều này rất vô nghĩa và rườm rà.

**Giải pháp của Spring (Dùng Unchecked):**
Spring "bọc" tất cả các lỗi cơ sở hạ tầng này (JDBC, Hibernate, v.v.) trong các `Unchecked Exception` (cụ thể là `DataAccessException`).

1.  Bây giờ, `userRepository.save()` (đã được Spring "proxy") ném ra `DataAccessException` (là một `RuntimeException`).
2.  Lớp `UserService` của bạn **không cần** `try-catch`. Code của bạn vẫn sạch sẽ, chỉ tập trung vào nghiệp vụ.
3.  Nếu lỗi xảy ra, `DataAccessException` sẽ "nổi bọt" (bubble up) qua `UserService`, qua `UserController`, và được xử lý ở một nơi duy nhất: **bộ xử lý ngoại lệ toàn cục (global exception handler)**.

Điều này giúp **tách biệt rõ ràng** (loose coupling) giữa logic nghiệp vụ và logic xử lý lỗi cơ sở hạ tầng.

---

### 3. 🏛️ Data Access Exception Hierarchy là gì?

Như bạn đã định nghĩa rất rõ, đây là một "họ" (hierarchy) các `RuntimeException` do Spring định nghĩa để **trừu tượng hóa (abstraction)** các lỗi truy cập dữ liệu.

**Vấn đề:**
* Nếu dùng JDBC, bạn bắt `SQLException`.
* Nếu dùng Hibernate, bạn bắt `HibernateException`.
* Nếu dùng JPA, bạn bắt `PersistenceException`.
* Nếu bạn đổi công nghệ (ví dụ: từ JDBC sang Hibernate), bạn phải **sửa lại toàn bộ code** `catch` của mình. Điều này là "phụ thuộc cứng" (tight coupling) vào công nghệ.

**Giải pháp của Spring:**
Spring (cụ thể là các bean được đánh dấu `@Repository`) sẽ tự động "bắt" các ngoại lệ cụ thể của công nghệ (như `SQLException`) và **"dịch" (translate)** chúng sang một ngoại lệ *chung* của Spring.

* `DataAccessException` là lớp "tổ tiên" (root) của tất cả.
* Các ví dụ của bạn là hoàn hảo:
    * Bất kể là `SQLException` (từ JDBC) hay `ConstraintViolationException` (từ Hibernate) báo lỗi "dữ liệu trùng lặp"...
    * ...Spring đều "dịch" nó thành **`DataIntegrityViolationException`**.
    * Một lỗi "không thể kết nối" sẽ được dịch thành `CannotAcquireLockException` hoặc tương tự.

**Lợi ích cuối cùng:**
Lớp nghiệp vụ (service) của bạn chỉ cần biết về `DataIntegrityViolationException` (của Spring), mà không cần quan tâm bạn đang dùng JDBC, Hibernate, JPA hay database Oracle, MySQL. Bạn có thể thay đổi toàn bộ công nghệ data access mà **không cần sửa một dòng code nào** trong lớp nghiệp vụ.

## ❓ Câu hỏi: Làm thế nào để cấu hình DataSource trong Spring? Bean nào hữu ích cho database development/test?

### 1\. 💡 `DataSource` là gì?

Như bạn đã nói, `DataSource` (`javax.sql.DataSource`) là một interface chuẩn của Java. Nó là một "lớp trừu tượng" (abstraction) cho một nguồn kết nối database.

Cách dễ hiểu nhất là hãy nghĩ `DataSource` như một **"vòi nước công cộng"** đã được kết nối sẵn.

* **Cách làm tồi:** Mỗi khi cần dùng nước (kết nối DB), bạn lại tự đi đào một cái giếng mới (tạo `Connection` mới). Việc này rất tốn kém và chậm chạp.
* **Cách làm tốt (Dùng `DataSource`):** Bạn đi đến "vòi nước" (do `DataSource` quản lý) và "mở vòi" để lấy nước (lấy `Connection` từ **Connection Pool**). Khi dùng xong, bạn "đóng vòi" (trả `Connection` về pool).

`DataSource` là một nhà máy (factory) chuyên cung cấp `Connection`, và nó thường được hỗ trợ bởi một **Connection Pool** (như HikariCP, c3p0, DBCP) để quản lý các kết nối này một cách hiệu quả.

-----

### 2\. ⚙️ Cách cấu hình `DataSource`

Cách bạn cấu hình phụ thuộc vào môi trường, như bạn đã phân loại rất chính xác.

#### A. Cách "Cũ" / "Thủ công" (Dùng `@Configuration` và `@Bean`)

Đây là cách làm trong một ứng dụng Spring "truyền thống" (không phải Spring Boot). Bạn phải tự tay khai báo bean `DataSource`.

**Lưu ý:** Bạn *không nên* dùng `DriverManagerDataSource` trong môi trường production vì nó **không có Connection Pool**. Thay vào đó, bạn nên cấu hình một pool thật (ví dụ `HikariDataSource`, là default của Spring Boot).

```java
@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        // Ví dụ cấu hình Hikari (Connection Pool hiệu suất cao)
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
        ds.setUsername("myuser");
        ds.setPassword("mypassword");
        ds.setMaximumPoolSize(10);
        return ds;
    }
}
```

#### B. Cách của Spring Boot (Tự động / `application.properties`)

Đây là cách phổ biến nhất hiện nay. Bạn **không cần** viết code Java ở trên.

Spring Boot (với `spring-boot-starter-data-jpa` hoặc `spring-boot-starter-jdbc`) sẽ tự động làm 3 việc:

1.  Nó thấy thư viện `HikariCP` (hoặc `Tomcat CP`) trong classpath.
2.  Nó **tự động tạo** một `DataSource` bean (chính là `HikariDataSource`).
3.  Nó tự động "tiêm" các cấu hình từ file `application.properties` vào bean đó.

Tất cả những gì bạn cần làm là:

**Trong `application.properties`:**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=myuser
spring.datasource.password=mypassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

#### C. Cách của App Server (Dùng JNDI)

Như bạn nói, trong môi trường "doanh nghiệp" (ví dụ: deploy file WAR lên JBoss, WebSphere, Tomcat), bản thân **Application Server** sẽ quản lý connection pool.

Ứng dụng Spring của bạn chỉ cần "tra cứu" (lookup) `DataSource` đó bằng tên JNDI của nó.

```java
@Configuration
public class JndiDataConfig {

    @Bean
    public DataSource dataSource() throws Exception {
        // Dùng JndiDataSourceLookup để tìm DataSource
        // mà server Tomcat/JBoss đã cấu hình
        JndiDataSourceLookup lookup = new JndiDataSourceLookup();
        return lookup.getDataSource("java:comp/env/jdbc/MyJndiDataSource");
    }
}
```

-----

### 3\. 🛠️ Các Bean hữu ích cho Development/Test

Đây là phần thứ hai của câu hỏi, và câu trả lời của bạn là **hoàn hảo**.

#### A. `EmbeddedDatabaseBuilder` (Dùng để TẠO Database)

Bean này cực kỳ hữu ích cho các bài **Unit Test** hoặc **Integration Test**.

* **Nó làm gì?** Nó tự động tạo một database **in-memory** (như H2, HSQLDB, hoặc Derby) ngay khi `ApplicationContext` khởi động.
* Bạn không cần cài đặt một server database nào cả.
* Nó cũng có thể tự động chạy các file script `.sql` để tạo bảng (schema) và chèn dữ liệu (data).

**Ví dụ trong cấu hình Test:**

```java
@Configuration
@Profile("test") // Chỉ chạy bean này khi test
public class TestDatabaseConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2) // Dùng H2
            .addScript("classpath:db/schema.sql") // Chạy script tạo bảng
            .addScript("classpath:db/test-data.sql") // Chèn dữ liệu mẫu
            .build();
    }
}
```

#### B. `DataSourceInitializer` (Dùng để KHỞI TẠO Database)

Bean này hơi khác một chút. Nó **không tạo** database. Nó dùng một `DataSource` *đã có* (ví dụ: `DataSource` H2 ở trên, hoặc `DataSource` MySQL thật) và **chạy các script** trên đó.

Bạn dùng `DataSourceInitializer` khi bạn muốn tự động "làm mới" (reset) database (ví dụ: `schema.sql`, `data.sql`) mỗi khi ứng dụng khởi động trong môi trường `dev`.

**Lưu ý của Spring Boot:**
Spring Boot làm việc này **hoàn toàn tự động** cho bạn. Nếu bạn dùng một `EmbeddedDatabase` (như H2), Spring Boot sẽ tự động tìm và chạy 2 file:

* `schema.sql`: Để tạo bảng.
* `data.sql`: Để chèn dữ liệu.

Bạn thường không cần phải tự cấu hình `EmbeddedDatabaseBuilder` hay `DataSourceInitializer` khi dùng Spring Boot.

## ❓ Câu hỏi: Template design pattern là gì và JDBC template là gì?

### 1\. 📋 Template Design Pattern (Mẫu Phương thức Template)

Như bạn nói, đây là một mẫu thiết kế hành vi. Mục đích của nó là định nghĩa **bộ xương (skeleton)** của một thuật toán trong một phương thức (gọi là "template method"), và cho phép các lớp con **định nghĩa lại (override)** một số bước cụ thể của thuật toán đó.

**💡 Ví dụ dễ hiểu: "Công thức làm Sandwich"**

Hãy tưởng tượng bạn có một lớp `abstract` tên là `SandwichMaker`. Nó có một phương thức `final` (không thể bị override) gọi là `makeSandwich()`.

```java
public abstract class SandwichMaker {

    // Đây chính là "Template Method"
    // Nó định nghĩa bộ xương của thuật toán: 5 bước
    public final void makeSandwich() {
        // 1. Bước chung:
        layBanhMi();
        
        // 2. Bước tùy chỉnh:
        addMainIngredient(); // (abstract)
        
        // 3. Bước tùy chỉnh:
        addCondiments();     // (abstract)
        
        // 4. Bước chung (optional, gọi là "hook"):
        if (customerWantsToGrill()) {
            grillIt();
        }

        // 5. Bước chung:
        wrapSandwich();
    }

    // Các bước chung (shared code)
    private void layBanhMi() { System.out.println("Lấy bánh mì"); }
    private void wrapSandwich() { System.out.println("Gói sandwich"); }
    private void grillIt() { System.out.println("Nướng sandwich"); }

    // Các bước "trừu tượng" (abstract) - buộc lớp con phải implement
    protected abstract void addMainIngredient();
    protected abstract void addCondiments();

    // Một "hook" - lớp con CÓ THỂ override nếu muốn (không bắt buộc)
    protected boolean customerWantsToGrill() {
        return true; // Mặc định là có nướng
    }
}
```

Bây giờ, các lớp con (concrete classes) sẽ *fill-in-the-blanks* (lấp vào chỗ trống):

```java
// Lớp con 1
public class TurkeySandwich extends SandwichMaker {
    @Override
    protected void addMainIngredient() {
        System.out.println("Thêm thịt gà tây");
    }
    @Override
    protected void addCondiments() {
        System.out.println("Thêm rau và sốt mayonnaise");
    }
}

// Lớp con 2
public class VeggieSandwich extends SandwichMaker {
    @Override
    protected void addMainIngredient() {
        System.out.println("Thêm đậu hũ và bơ");
    }
    @Override
    protected void addCondiments() {
        System.out.println("Thêm rau và sốt mù tạt");
    }
    
    @Override
    protected boolean customerWantsToGrill() {
        return false; // Sandwich chay không cần nướng
    }
}
```

**Ưu điểm (như bạn nói):**

* **Tái sử dụng code:** Logic chung (lấy bánh mì, gói) chỉ được viết một lần.
* **Kiểm soát:** Lớp cha kiểm soát toàn bộ thuật toán, lớp con không thể làm sai thứ tự các bước.

**Nhược điểm (như bạn nói):**

* **Phụ thuộc cứng (Tight Coupling):** Lớp con bị "dính" chặt vào lớp cha thông qua `extends` (kế thừa). Như bạn đã chỉ ra rất đúng, **Strategy Pattern** (dùng *composition* - kết hợp) thường linh hoạt hơn.

-----

### 2\. 🗃️ JdbcTemplate (Template của Spring JDBC)

**`JdbcTemplate` chính là một ví dụ kinh điển của Template Method Pattern.**

#### Vấn đề (The Problem):

Trước khi có `JdbcTemplate`, code JDBC "thuần" (plain JDBC) cực kỳ rườm rà và lặp đi lặp lại. Bạn phải tự tay quản lý "bộ xương" của thuật toán:

1.  Mở kết nối (Get Connection)
2.  Tạo câu lệnh (Create Statement)
3.  Thực thi câu lệnh (Execute Query)
4.  Lấy kết quả (Process ResultSet)
5.  **Đóng ResultSet** (trong `finally`)
6.  **Đóng Statement** (trong `finally`)
7.  **Đóng Connection** (trong `finally`)
8.  **Xử lý `SQLException`** (checked exception) ở khắp mọi nơi.

Đây chính là **"boilerplate code"** (code lặp đi lặp lại nhàm chán).

#### Giải pháp (The Solution):

`JdbcTemplate` nói:

> "Các bạn không cần làm 8 bước đó nữa. **Tôi (JdbcTemplate) sẽ là lớp `abstract`** và tôi sẽ xử lý **toàn bộ 8 bước** đó (mở, đóng, xử lý lỗi) bên trong "template method" của tôi (ví dụ: phương thức `query()`).
>
> Các bạn (lập trình viên) chỉ cần cung cấp 2 bước tùy chỉnh (giống như `addMainIngredient`):
>
> 1.  **Câu SQL là gì?** (Một `String`)
> 2.  **Cách biến một dòng `ResultSet` thành đối tượng Java?** (Một `RowMapper`)"

**Ví dụ: "Trước" và "Sau"**

**TRƯỚC (JDBC Thuần):**

```java
// Rất nhiều boilerplate...
Connection conn = null;
PreparedStatement stmt = null;
ResultSet rs = null;

try {
    conn = dataSource.getConnection();
    stmt = conn.prepareStatement("SELECT id, name FROM users WHERE id = ?");
    stmt.setLong(1, 123L);
    rs = stmt.executeQuery();
    
    User user = null;
    if (rs.next()) {
        user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
    }
    return user;
    
} catch (SQLException e) {
    // Phải bắt checked exception
    throw new RuntimeException("Lỗi DB", e);
} finally {
    // Phải tự dọn dẹp (cực kỳ dễ quên)
    if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
    if (stmt != null) try { stmt.close(); } catch (SQLException e) { /* ignore */ }
    if (conn != null) try { conn.close(); } catch (SQLException e) { /* ignore */ }
}
```

**SAU (Dùng `JdbcTemplate`):**

```java
@Autowired
private JdbcTemplate jdbcTemplate;

public User findUserById(long id) {
    String sql = "SELECT id, name FROM users WHERE id = ?";
    
    // 1. Cung cấp SQL
    // 2. Cung cấp "cách làm": biến 1 dòng rs thành 1 User
    // JdbcTemplate lo phần còn lại (mở, đóng, try-catch)
    return jdbcTemplate.queryForObject(sql, new Object[]{id}, 
        (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            return user;
        }
    );
}
```

**Kết luận:**
`JdbcTemplate` đã thực hiện **2 việc** quan trọng, như bạn đã nêu:

1.  **Đơn giản hóa code:** Nó quản lý toàn bộ vòng đời (mở/đóng) của tài nguyên JDBC.
2.  **Dịch ngoại lệ (Exception Translation):** Nó "bắt" các `SQLException` (checked) và "dịch" chúng thành các `DataAccessException` (unchecked, của Spring), giúp code nghiệp vụ của bạn sạch sẽ hơn.

## ❓ Câu hỏi: Callback là gì? Ba callback interface của JdbcTemplate dùng cho query là gì? Chúng dùng để làm gì?

### 1\. 📞 Callback là gì?

Định nghĩa của bạn là hoàn hảo: **Callback là một đoạn code được "truyền" (passed) như một tham số cho một phương thức khác, với mục đích là để phương thức đó "gọi lại" (call back) nó vào một thời điểm thích hợp.**

**💡 Ví dụ so sánh (Analogy):**
Hãy nghĩ `JdbcTemplate` như một **Dịch vụ Giao hàng**.

* **Template (Dịch vụ):** Dịch vụ này biết *cách* làm các việc nhàm chán: nhận hàng (`Connection`), lái xe đến kho (`PreparedStatement`), xử lý giấy tờ (`try-finally`), khóa cửa kho (`rs.close()`, `stmt.close()`), và lái xe về (`conn.close()`).
* **Callback (Bạn):** Bạn không muốn làm những việc đó. Bạn chỉ muốn *một* việc: **"Khi anh đến kho, hãy lấy cho tôi gói hàng màu đỏ."**

Ở đây, "lấy cho tôi gói hàng màu đỏ" chính là **callback**. Bạn "truyền" (pass) chỉ dẫn này cho `JdbcTemplate`. `JdbcTemplate` sẽ lo toàn bộ 8 bước mệt mỏi của JDBC, và khi nó đến đúng bước "lấy kết quả" (bước 4), nó sẽ "gọi lại" đoạn code của bạn.

Như bạn đã nói, trong Java, bạn "truyền" đoạn code này bằng cách:

* Implement một `interface`.
* Tạo một `anonymous class`.
* Sử dụng `lambda expression` (phổ biến nhất hiện nay).

-----

### 2\. ⚙️ Ba Callback Interface cho Query

Câu hỏi này tập trung vào các callback dùng để *truy vấn (query)*, và bạn đã xác định chính xác ba loại chính. Sự khác biệt mấu chốt nằm ở **"Mức độ kiểm soát"** và **"Cách trả về kết quả"**.

#### A. `RowMapper<T>` (Trình ánh xạ hàng)

* **💡 Tóm tắt:** Biến **một hàng** thành **một đối tượng**. Đây là callback bạn sẽ dùng 90% thời gian.
* **Analogy:** Một cái **"Khuôn cắt bánh" (Cookie Cutter)**.
* **Nhiệm vụ của bạn:** Cung cấp logic để biến 1 hàng (`ResultSet` hiện tại) thành 1 đối tượng Java (ví dụ: `User`).
* **Ai gọi `rs.next()`?** `JdbcTemplate` lo việc đó. Bạn **KHÔNG** được gọi `rs.next()`.
* **Nó làm gì?** `JdbcTemplate` lặp (`while(rs.next())`), gọi `RowMapper` của bạn cho mỗi hàng, tự động thu thập kết quả vào một `List`, và trả `List<T>` đó cho bạn.
* **Ví dụ (Lambda):**
  ```java
  String sql = "SELECT id, name FROM users";

  // RowMapper: (rs, rowNum) -> User
  List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
      User user = new User();
      user.setId(rs.getLong("id"));
      user.setName(rs.getString("name"));
      return user; // Trả về 1 "cái bánh"
  });
  // users bây giờ là List<User> chứa đầy "bánh"
  ```

#### B. `ResultSetExtractor<T>` (Trình trích xuất tập kết quả)

* **💡 Tóm tắt:** Biến **toàn bộ tập kết quả** thành **một đối tượng duy nhất** (bất kỳ thứ gì bạn muốn).
* **Analogy:** Một **"Công nhân lắp ráp" (Factory Worker)**.
* **Nhiệm vụ của bạn:** Bạn được trao toàn quyền kiểm soát *toàn bộ* `ResultSet`. Bạn phải tự mình lặp (loop) qua nó.
* **Ai gọi `rs.next()`?** **BẠN** phải gọi `while (rs.next()) { ... }`.
* **Nó làm gì?** Dùng khi bạn có logic phức tạp. Ví dụ, bạn muốn trả về một `Map<Long, List<String>>` hoặc một đối tượng `Order` chứa `List<OrderItem>`. Bạn tự tạo đối tượng, tự lặp và tự điền dữ liệu.
* **Ví dụ (Lambda):**
  ```java
  String sql = "SELECT id, name FROM users";

  // ResultSetExtractor: (rs) -> Map<Long, String>
  Map<Long, String> userMap = jdbcTemplate.query(sql, (rs) -> {
      Map<Long, String> map = new HashMap<>();
      while (rs.next()) { // BẠN phải gọi .next()
          map.put(rs.getLong("id"), rs.getString("name"));
      }
      return map; // Trả về 1 đối tượng phức tạp
  });
  // userMap bây giờ là Map<Long, String>
  ```

#### C. `RowCallbackHandler` (Trình xử lý hàng)

* **💡 Tóm tắt:** Xử lý **từng hàng** nhưng **không trả về bất cứ thứ gì** (void).
* **Analogy:** Một **"Kiểm toán viên" (Auditor)**.
* **Nhiệm vụ của bạn:** "Nhìn" vào từng hàng khi nó đi qua và thực hiện một hành động (side-effect), ví dụ: ghi vào file, tính tổng vào một biến bên ngoài.
* **Ai gọi `rs.next()`?** `JdbcTemplate` lo việc đó. Bạn **KHÔNG** được gọi `rs.next()`.
* **Nó làm gì?** Vì phương thức `processRow` trả về `void`, nên `jdbcTemplate.query()` cũng sẽ trả về `void`. Callback này *vốn dĩ* là `stateful` (như bạn nói), vì mục đích của nó là thay đổi trạng thái ở đâu đó bên ngoài.
* **Ví dụ (Anonymous Class, vì nó thường stateful):**
  ```java
  String sql = "SELECT id, name FROM users";

  // Thường dùng một đối tượng bên ngoài để lưu state
  List<User> externalList = new ArrayList<>();

  // RowCallbackHandler: (rs) -> void
  jdbcTemplate.query(sql, rs -> {
      // KHÔNG CÓ "return"
      // Chỉ xử lý và tạo "side effect"
      User user = new User();
      user.setId(rs.getLong("id"));
      user.setName(rs.getString("name"));
      externalList.add(user); // Thêm vào danh sách bên ngoài
  });
  // Kết quả nằm trong externalList
  ```

-----

### 📊 Tóm tắt so sánh

| Callback | Bạn làm gì? | Ai gọi `rs.next()`? | Kết quả trả về? | Khi nào dùng? |
| :--- | :--- | :--- | :--- | :--- |
| **`RowMapper<T>`** | Biến 1 hàng -\> 1 đối tượng `T` | `JdbcTemplate` | `List<T>` | 90% các trường hợp (lấy danh sách đối tượng). |
| **`ResultSetExtractor<T>`** | Biến *toàn bộ* `ResultSet` -\> 1 đối tượng `T` | **Bạn** | `T` (một đối tượng duy nhất) | Mapping phức tạp (ví dụ: gộp nhóm, join). |
| **`RowCallbackHandler`** | Xử lý 1 hàng (Side-effect) | `JdbcTemplate` | `void` | Xử lý dữ liệu lớn (ví dụ: stream/ghi file) mà không cần tải hết vào bộ nhớ. |


## ❓ Câu hỏi: Bạn có thể thực thi một câu lệnh SQL thuần (plain SQL) với JDBC template không?

Câu trả lời của bạn là **hoàn toàn chính xác**.

**Vâng, đó chính là mục đích chính** của `JdbcTemplate`.

Toàn bộ ý tưởng của `JdbcTemplate` là giúp bạn thoát khỏi 8 bước "lặp đi lặp lại nhàm chán" (boilerplate) của JDBC thuần (mở `Connection`, tạo `Statement`, `try-catch-finally`, đóng `ResultSet`, đóng `Statement`, đóng `Connection`...).

`JdbcTemplate` sẽ lo hết tất cả các "công việc bẩn thỉu" đó. Bạn chỉ cần cung cấp 2 thứ:

1.  **Câu SQL thuần** (dưới dạng `String`).
2.  **Một callback** (như `RowMapper`) để nói cho nó biết phải làm gì với kết quả.

Dưới đây là diễn giải chi tiết về các phương thức bạn đã liệt kê, được nhóm theo mục đích sử dụng:

-----

### 1\. 📈 Truy vấn (SELECT) - Lấy Dữ liệu

Đây là các phương thức bạn dùng khi muốn *đọc* dữ liệu từ database.

* **`query(String sql, RowMapper<T> rowMapper, Object... args)`**

  * **Mục đích:** Đây là phương thức "mẹ" (linh hoạt nhất) để lấy một **danh sách các đối tượng**.
  * **Sử dụng:** Bạn cung cấp SQL, một `RowMapper` (callback để biến 1 hàng thành 1 đối tượng), và các tham số (args) cho dấu `?`.
  * **Ví dụ:** Lấy tất cả `User` trên 18 tuổi.
    ```java
    String sql = "SELECT id, name, age FROM users WHERE age > ?";

    List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), 18);

    // Hoặc dùng lambda (phổ biến hơn)
    List<User> usersLambda = jdbcTemplate.query(sql, 
        (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("age")
        ),
        18
    );
    ```

* **`queryForObject(String sql, RowMapper<T> rowMapper, Object... args)`**

  * **Mục đích:** Lấy **một và chỉ một** đối tượng.
  * **Sử dụng:** Hoàn hảo cho các truy vấn `SELECT ... WHERE id = ?`.
  * **Lưu ý:** Nếu truy vấn trả về **0 hàng**, nó sẽ ném ra `EmptyResultDataAccessException`. Nếu trả về **nhiều hơn 1 hàng**, nó ném ra `IncorrectResultSizeDataAccessException`.
  * **Ví dụ:** Lấy `User` có ID là 1.
    ```java
    String sql = "SELECT id, name, age FROM users WHERE id = ?";
    User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), 1);
    ```

* **`queryForObject(String sql, Class<T> requiredType, Object... args)`**

  * **Mục đích:** Một biến thể CỰC KỲ hữu ích của `queryForObject` để lấy **một giá trị đơn lẻ** (ví dụ: `String`, `int`, `long`).
  * **Ví dụ:** Đếm số lượng user.
    ```java
    String sql = "SELECT COUNT(*) FROM users";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class);

    String sqlName = "SELECT name FROM users WHERE id = ?";
    String name = jdbcTemplate.queryForObject(sqlName, String.class, 1);
    ```

* **`queryForList(String sql, Object... args)`**

  * **Mục đích:** Lấy một danh sách, trong đó mỗi hàng là một `Map<String, Object>`.
  * **Sử dụng:** Hữu ích khi bạn không muốn tạo một lớp `User` đầy đủ, mà chỉ muốn dữ liệu "thô".
  * **Ví dụ:**
    ```java
    String sql = "SELECT id, name FROM users";
    List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
    // list.get(0).get("name") sẽ trả về tên của user đầu tiên
    ```

* **`queryForMap(String sql, Object... args)`**

  * **Mục đích:** Lấy **một hàng duy nhất** dưới dạng `Map<String, Object>`.
  * **Ví dụ:**
    ```java
    String sql = "SELECT id, name FROM users WHERE id = ?";
    Map<String, Object> userMap = jdbcTemplate.queryForMap(sql, 1);
    ```

* **`queryForRowSet(String sql, Object... args)`**

  * **Mục đích:** Một lựa chọn nâng cao. Nó trả về một `SqlRowSet`, hoạt động giống như một `ResultSet` (bạn tự lặp `while(rs.next())`) nhưng nó "ngắt kết nối" (disconnected).

-----

### 2\. ✏️ Cập nhật (INSERT, UPDATE, DELETE) - Thay đổi Dữ liệu

Các phương thức này không trả về dữ liệu, mà trả về số lượng hàng bị ảnh hưởng.

* **`update(String sql, Object... args)`**

  * **Mục đích:** Đây là phương thức "một cho tất cả" dùng cho `INSERT`, `UPDATE`, và `DELETE`.
  * **Kết quả:** Trả về một `int` (số lượng hàng bị thay đổi).
  * **Ví dụ `INSERT`:**
    ```java
    String sql = "INSERT INTO users (name, age) VALUES (?, ?)";
    int rowsAffected = jdbcTemplate.update(sql, "John Doe", 30);
    ```
  * **Ví dụ `UPDATE`:**
    ```java
    String sql = "UPDATE users SET age = ? WHERE name = ?";
    int rowsAffected = jdbcTemplate.update(sql, 31, "John Doe");
    ```

* **`batchUpdate(String sql, List<Object[]> batchArgs)`**

  * **Mục đích:** Thực thi **nhiều** câu lệnh (thường là `INSERT`) cùng một lúc.
  * **Sử dụng:** Cực kỳ quan trọng cho **hiệu năng** (performance) khi bạn cần chèn hàng nghìn hàng.
  * **Ví dụ:** Chèn 2 user mới.
    ```java
    String sql = "INSERT INTO users (name, age) VALUES (?, ?)";

    List<Object[]> batchArgs = new ArrayList<>();
    batchArgs.add(new Object[] { "Alice", 25 });
    batchArgs.add(new Object[] { "Bob", 42 });

    int[] rowsAffectedArray = jdbcTemplate.batchUpdate(sql, batchArgs);
    ```

-----

### 3\. ⚙️ Thực thi (DDL) - Chạy Lệnh Bất kỳ

* **`execute(String sql)`**
  * **Mục đích:** Dùng để chạy bất kỳ câu lệnh SQL nào, đặc biệt là DDL (Data Definition Language).
  * **Sử dụng:** Khi bạn muốn chạy `CREATE TABLE`, `ALTER TABLE`, `DROP TABLE`... Nó không trả về dữ liệu hay số lượng hàng.
  * **Ví dụ:** (Thường dùng trong các script khởi tạo test)
    ```java
    String sql = "CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(100), age INT)";
    jdbcTemplate.execute(sql);
    ```
## ❓ Câu hỏi: JdbcTemplate lấy (và giải phóng) connection khi nào? Mỗi lần gọi phương thức hay một lần cho mỗi template? Tại sao?

Câu trả lời ngắn gọn: **Nó cực kỳ thông minh.** Nó lấy và giải phóng connection theo cách hiệu quả nhất tùy thuộc vào hoàn cảnh.

Hãy dùng một phép ví von:

* `DataSource` (Connection Pool) là một **"Nhà kho chứa dụng cụ"** (ví dụ: 10 cái búa).
* `Connection` là một **"cái búa"**.
* `JdbcTemplate` là một **"Người thợ"**.

-----

### 1\. 🏃 Kịch bản 1: KHÔNG có Giao dịch (Transaction)

Đây là trường hợp bạn gọi `jdbcTemplate.query(...)` một cách độc lập.

> **Quy tắc:** Lấy (Acquire) và Trả (Release) **cho mỗi lần gọi phương thức.**

* **Hành động:**
  1.  Người thợ (`JdbcTemplate`) cần đóng một cái đinh (chạy 1 câu `query`).
  2.  Thợ đi đến nhà kho (`DataSource`), "mượn" (acquire) một cái búa (`Connection`).
  3.  Thợ dùng búa đóng *đúng một cái đinh đó*.
  4.  Thợ **ngay lập tức** "trả" (release) cái búa về nhà kho.
* **Tại sao? (Như bạn nói):** Để **giảm thiểu thời gian giữ tài nguyên**. Cái búa là tài nguyên dùng chung. Nếu bạn giữ nó cả ngày chỉ để đóng 1 cái đinh, thì 9 người thợ khác sẽ không có búa để dùng. Bằng cách "mượn-dùng-trả" ngay lập tức, tài nguyên (connection) được giải phóng cho các request khác.

**Code flow (đơn giản hóa):**

```java
public void myMethod() {
    // KHÔNG có @Transactional
    
    // LẦN 1:
    // 1. LẤY connection từ pool
    jdbcTemplate.query(...);
    // 2. TRẢ connection về pool

    // LẦN 2:
    // 3. LẤY (một) connection (có thể là cái khác)
    jdbcTemplate.update(...);
    // 4. TRẢ connection về pool
}
```

-----

### 2\. 🛒 Kịch bản 2: CÓ Giao dịch (Transaction)

Đây là trường hợp bạn gọi `jdbcTemplate.query(...)` từ bên trong một phương thức được đánh dấu `@Transactional`.

> **Quy tắc:** Lấy (Acquire) **một lần** khi giao dịch bắt đầu, và **sử dụng lại (reuse)** connection đó cho **tất cả** các lệnh, cuối cùng Trả (Release) **một lần** khi giao dịch kết thúc (commit/rollback).

* **Hành động:**
  1.  Người thợ (`@Transactional` AOP) bắt đầu một "Dự án" (transaction).
  2.  Thợ đi đến nhà kho (`DataSource`), "mượn" một cái búa (`Connection`).
  3.  Thợ "cất" cái búa này vào túi của mình (Spring "buộc" - **bind** - connection này vào **Thread** hiện tại thông qua `TransactionSynchronizationManager`).
  4.  **Lệnh 1:** Thợ (`jdbcTemplate.update`) cần búa. Thợ kiểm tra túi -\> "Aha, có búa\!" -\> Dùng búa (đóng đinh 1).
  5.  **Lệnh 2:** Thợ (`jdbcTemplate.query`) cần búa. Thợ kiểm tra túi -\> "Vẫn là cái búa đó\!" -\> Dùng búa (đóng đinh 2).
  6.  "Dự án" hoàn thành. Thợ (AOP) "commit" công việc và **lúc này mới "trả"** (release) cái búa về nhà kho.
* **Tại sao? (Như bạn nói):** Một **giao dịch (transaction) về bản chất PHẢI xảy ra trên CÙNG MỘT connection.** Bạn không thể `INSERT` trên Connection A, `UPDATE` trên Connection B, rồi `COMMIT` trên Connection A và mong Connection B cũng được commit. Tất cả công việc phải là một "đơn vị" (unit of work) duy nhất. Nếu bạn trả connection về pool giữa chừng, transaction sẽ bị `rollback` ngay lập...

**Code flow (với "phép thuật" của Spring):**

```java
@Autowired
private JdbcTemplate jdbcTemplate;

@Transactional // <-- AOP Proxy bắt đầu ở đây
public void myBigProject() {
    // 1. AOP Proxy LẤY Conn-123 từ pool.
    // 2. AOP Proxy "gắn" Conn-123 vào Thread.

    // 3. JdbcTemplate hỏi: "Có connection nào trên Thread không?"
    // 4. "Có! Dùng Conn-123"
    jdbcTemplate.update("INSERT...");
    // 5. JdbcTemplate "giả vờ" trả connection (nhưng AOP/Utils ngăn lại)

    // 6. JdbcTemplate hỏi: "Có connection nào trên Thread không?"
    // 7. "Vẫn là Conn-123! Dùng nó"
    jdbcTemplate.update("UPDATE...");
    // 8. JdbcTemplate "giả vờ" trả...

} // <-- AOP Proxy kết thúc ở đây
  // 9. AOP Proxy gọi "commit" trên Conn-123
  // 10. AOP Proxy "thực sự" TRẢ Conn-123 về pool.
```

-----

### 3\. Làm rõ: `DataSourceUtils` và Connection Pool (Điểm của bạn)

Câu trả lời của bạn là rất sâu khi đề cập đến `DataSourceUtils` và Connection Pool.

* `DataSourceUtils` chính là "bộ não" thông minh. `JdbcTemplate` không tự gọi `dataSource.getConnection()`. Nó gọi `DataSourceUtils.getConnection(dataSource)`.
* `DataSourceUtils` sẽ hỏi `TransactionSynchronizationManager` (Người quản lý "túi" của thợ) để xem có connection nào đang được "gắn" vào thread không.
  * Nếu có (đang trong transaction) -\> trả về connection đó.
  * Nếu không -\> lấy một connection mới từ `DataSource`.

Và như bạn nói, khi `JdbcTemplate` (thông qua `DataSourceUtils`) "trả" (release) connection, nếu `DataSource` là một **Connection Pool** (như Hikari, C3P0), thì `conn.close()` không thực sự *đóng* kết nối TCP. Nó chỉ là hành động **"trả cái búa về nhà kho"** (trả connection về pool) để người khác dùng tiếp. Điều này nhanh hơn rất nhiều so với việc "đập" cái búa đi và "rèn" một cái búa mới (mở kết nối TCP mới).

## ❓ Câu hỏi: JdbcTemplate hỗ trợ các truy vấn "generic" (chung) như thế nào? Làm thế nào nó trả về các đối tượng và danh sách/map của các đối tượng?

Chào bạn, câu trảGchào bạn, câu trả lời của bạn đã liệt kê chính xác các phương thức chính mà `JdbcTemplate` sử dụng.

Giải thích rõ hơn: `JdbcTemplate` hỗ trợ các truy vấn "chung" (generic) bằng cách cung cấp một bộ phương thức được thiết kế riêng cho **hình dạng (shape)** của kết quả mà bạn mong đợi.

Nó hoạt động dựa trên nguyên tắc: **"Bạn cho tôi biết bạn muốn *loại* kết quả nào (một đối tượng, một danh sách, một con số...), và tôi sẽ lo phần việc nặng nhọc."**

Dưới đây là diễn giải chi tiết về các phương thức đó, được nhóm theo loại kết quả trả về.

-----

### 1\. 🎯 Trả về MỘT Đối tượng Đơn lẻ

Bạn dùng các phương thức này khi bạn mong đợi **chính xác một hàng** (row) trả về (ví dụ: `SELECT ... WHERE id = ?`).

* **`queryForObject(String sql, RowMapper<T> rowMapper, ...args)`**

  * **Mục đích:** Lấy **một đối tượng Java đầy đủ** (ví dụ: `User`, `Product`).
  * **Cách hoạt động:** Bạn cung cấp SQL, các tham số (args), và một `RowMapper` (là một callback "dạy" Spring cách biến 1 hàng `ResultSet` thành 1 đối tượng `User`).
  * **Quy tắc:** Truy vấn **phải** trả về chính xác 1 hàng.
    * Nếu trả về 0 hàng -\> Ném `EmptyResultDataAccessException`.
    * Nếu trả về \> 1 hàng -\> Ném `IncorrectResultSizeDataAccessException`.
  * **Ví dụ:**
    ```java
    String sql = "SELECT * FROM users WHERE id = ?";
    User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), 1);
    ```

* **`queryForMap(String sql, ...args)`**

  * **Mục đích:** Lấy **một hàng duy nhất**, nhưng trả về dưới dạng `Map`.
  * **Cách hoạt động:** Bạn không cần `RowMapper`. Spring tự động tạo `Map<String, Object>`, trong đó `key` là tên cột (column name) và `value` là giá trị của cột đó.
  * **Quy tắc:** Giống hệt `queryForObject` (0 hoặc \> 1 hàng sẽ ném exception).
  * **Ví dụ:**
    ```java
    String sql = "SELECT id, name, age FROM users WHERE id = ?";
    // Kết quả: {id=1, name="John Doe", age=30}
    Map<String, Object> userMap = jdbcTemplate.queryForMap(sql, 1);
    ```

-----

### 2\. 🔢 Trả về MỘT Giá trị Đơn lẻ (Scalar Value)

Đây là một biến thể *cực kỳ hữu ích* của `queryForObject`.

* **`queryForObject(String sql, Class<T> requiredType, ...args)`**
  * **Mục đích:** Lấy **một giá trị đơn lẻ** (ví dụ: `String`, `Integer`, `long`).
  * **Cách hoạt động:** Bạn cung cấp SQL (thường là truy vấn 1 hàng, 1 cột) và *kiểu dữ liệu* bạn muốn (ví dụ: `Integer.class`).
  * **Quy tắc:** Phải trả về chính xác 1 hàng và 1 cột.
  * **Ví dụ:**
    ```java
    // Dùng để đếm
    String sqlCount = "SELECT COUNT(*) FROM users";
    Integer count = jdbcTemplate.queryForObject(sqlCount, Integer.class);

    // Dùng để lấy một tên
    String sqlName = "SELECT name FROM users WHERE id = ?";
    String name = jdbcTemplate.queryForObject(sqlName, String.class, 1);
    ```

-----

### 3\. 📋 Trả về DANH SÁCH các Đối tượng

Bạn dùng các phương thức này khi bạn mong đợi **0 hoặc nhiều hàng** trả về.

* **`query(String sql, RowMapper<T> rowMapper, ...args)`**

  * **Mục đích:** Đây là phương thức **chuẩn và phổ biến nhất** để lấy một `List<T>` (ví dụ: `List<User>`).
  * **Cách hoạt động:** `JdbcTemplate` chạy truy vấn, lặp qua `ResultSet`, gọi `RowMapper` của bạn cho *từng hàng*, và tự động thêm kết quả vào một `List` rồi trả về.
  * **Ví dụ:**
    ```java
    String sql = "SELECT * FROM users WHERE age > ?";
    List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), 18);
    ```

* **`queryForList(String sql, ...args)`**

  * **Mục đích:** Lấy `List<Map<String, Object>>`. Mỗi `Map` là một hàng.
  * **Cách hoạt động:** Giống như `queryForMap` nhưng áp dụng cho nhiều hàng. Hữu ích khi bạn "lười" không muốn tạo `RowMapper`.
  * **Ví dụ:**
    ```java
    String sql = "SELECT id, name FROM users";
    // Kết quả: [{id=1, name="John"}, {id=2, name="Jane"}]
    List<Map<String, Object>> userList = jdbcTemplate.queryForList(sql);
    ```

* **`queryForList(String sql, Class<T> elementType, ...args)`**

  * **Mục đích:** Đây là trường hợp **bạn đã đề cập**. Nó dùng để lấy một `List` các giá trị đơn lẻ (scalar).
  * **Cách hoạt động:** Giống `queryForObject(sql, Class<T>)` nhưng cho nhiều hàng.
  * **Quy tắc:** Truy vấn **phải** trả về đúng 1 cột.
  * **Ví dụ:**
    ```java
    String sql = "SELECT name FROM users WHERE age > ?";
    // Kết quả: ["Alice", "Bob", "Charlie"]
    List<String> names = jdbcTemplate.queryForList(sql, String.class, 18);
    ```

-----

### 4\. 🗂️ Trả về `SqlRowSet` (Nâng cao)

* **`queryForRowSet(String sql, ...args)`**
  * **Mục đích:** Trả về một đối tượng `SqlRowSet`.
  * **Cách hoạt động:** `SqlRowSet` giống như một `ResultSet` nhưng nó "ngắt kết nối" (disconnected). Nó cho phép bạn lặp (iterate) qua các hàng (`while (rowSet.next()) { ... }`) và lấy dữ liệu, nhưng nó an toàn hơn vì `Connection` đã được đóng. Hữu ích khi bạn cần xử lý phức tạp hoặc kiểm tra metadata của cột.

**Tóm lại:** `JdbcTemplate` rất "generic" vì nó cho phép bạn chọn phương thức chính xác dựa trên *hình dạng (shape)* dữ liệu bạn mong muốn (một đối tượng, một giá trị, một danh sách, hay một map).

Chào bạn, câu trả lời của bạn là **rất xuất sắc** và chính xác về mặt lý thuyết. Bạn đã nắm bắt hoàn hảo định nghĩa của Giao dịch (Transaction), nguyên lý ACID, và sự khác biệt mấu chốt giữa Giao dịch Cục bộ (Local) và Toàn cục (Global).

Phần diễn giải này sẽ dùng các **ví dụ so sánh (analogy)** để làm cho các khái niệm này trở nên dễ hiểu và dễ nhớ hơn.

---

## ❓ Câu hỏi: Giao dịch (transaction) là gì? Sự khác biệt giữa Giao dịch Cục bộ (local) và Toàn cục (global) là gì?

### 1. 🏦 Giao dịch (Transaction) là gì?

Như bạn đã định nghĩa, Giao dịch là một **"Đơn vị công việc" (Unit of Work)**, bao gồm một hoặc nhiều tác vụ, tuân theo nguyên tắc **"Tất cả hoặc Không gì cả" (All or Nothing)**.

**💡 Ví dụ so sánh: Chuyển tiền ngân hàng**

Hãy tưởng tượng một giao dịch "Chuyển 100.000đ từ Tài khoản A sang Tài khoản B". Giao dịch này bao gồm 2 tác vụ:
1.  **Task 1:** Trừ 100.000đ từ Tài khoản A.
2.  **Task 2:** Cộng 100.000đ vào Tài khoản B.

Spring (hoặc hệ thống quản lý giao dịch) sẽ "bọc" hai tác vụ này trong một Giao dịch.

* **Nếu cả 2 thành công:** Giao dịch **`COMMIT`** (lưu vĩnh viễn). Tiền được chuyển.
* **Nếu Task 1 thành công, nhưng Task 2 thất bại** (ví dụ: Tài khoản B bị khóa): Toàn bộ giao dịch phải **`ROLLBACK`** (hoàn tác). Task 1 (đã trừ tiền A) phải được *hủy bỏ*. Tiền phải được trả lại cho A.

Không thể có trường hợp tiền "biến mất" (bị trừ khỏi A nhưng không được cộng vào B).

#### Nguyên lý ACID

Bạn đã định nghĩa ACID rất chuẩn. Đây là 4 trụ cột đảm bảo Giao dịch đáng tin cậy:

* **A - Atomicity (Tính nguyên tử):** "Tất cả hoặc không gì cả". Đây chính là ví dụ chuyển tiền ở trên.
* **C - Consistency (Tính nhất quán):** "Hệ thống đi từ trạng thái hợp lệ này sang trạng thái hợp lệ khác." Ví dụ:
  * *Trước Giao dịch:* Tổng tiền (A+B) = 500.000đ.
  * *Sau Giao dịch:* Tổng tiền (A+B) phải *vẫn* là 500.000đ.
  * Một ràng buộc (constraint) như "số dư không được âm" phải luôn được đảm bảo.
* **I - Isolation (Tính cô lập):** "Giao dịch này không được thấy kết quả dang dở của giao dịch khác." Nếu hai người cùng lúc chuyển tiền vào Tài khoản A, kết quả cuối cùng phải chính xác như thể họ làm tuần tự.
* **D - Durability (Tính bền vững):** "Một khi đã `COMMIT`, dữ liệu không bao giờ mất." Nếu ngân hàng báo "Chuyển tiền thành công", thì ngay cả khi server mất điện 1 giây sau đó, tiền của bạn vẫn an toàn khi server khởi động lại.

---

### 2. 🌍 Sự khác biệt: Local vs. Global

Đây là sự khác biệt về **phạm vi (scope)** của Giao dịch.

#### A. Local Transactions (Giao dịch Cục bộ)

* **Định nghĩa:** Một giao dịch chỉ liên quan đến **một** tài nguyên giao dịch duy nhất.
* **Ví dụ phổ biến nhất:** Một giao dịch chỉ làm việc với **một** cơ sở dữ liệu (ví dụ: chỉ một `DataSource` MySQL).
* **💡 Ví dụ so sánh:** Bạn là Quản lý của **Kho A**. Bạn có toàn quyền di chuyển hàng hóa *bên trong* Kho A (ví dụ: chuyển 10 món hàng từ Kệ 1 sang Kệ 2). Bạn có thể tự mình `commit` (xác nhận) hoặc `rollback` (hoàn tác) thao tác đó.
* **Cơ chế:** Rất đơn giản. `java.sql.Connection` có các phương thức như `connection.commit()` và `connection.rollback()`. `DataSourceTransactionManager` của Spring sẽ gọi các phương thức này để quản lý giao dịch.

#### B. Global Transactions (Giao dịch Toàn cục)

* **Định nghĩa:** Một giao dịch "phối hợp" (spans) **nhiều** tài nguyên giao dịch.
* **Ví dụ phổ biến:**
  1.  Cập nhật dữ liệu trên **2 database khác nhau** (ví dụ: một `DataSource` Oracle VÀ một `DataSource` MySQL).
  2.  Ghi vào **1 database** VÀ gửi một tin nhắn đến **1 Message Queue** (ví dụ: JMS hoặc Kafka).
* **💡 Ví dụ so sánh (Tiếp):** Bây giờ, bạn cần di chuyển 10 món hàng từ **Kho A** (`DataSource 1`) sang **Kho B** (`Message Queue`).
* **Vấn đề:** Quản lý Kho A (MySQL) và Quản lý Kho B (Kafka) không biết gì về nhau. Nếu Kho A `commit` (xuất hàng đi), nhưng Kho B bị lỗi (không nhận được hàng), làm thế nào để "bảo" Kho A `rollback` lại?
* **Giải pháp (Two-Phase Commit - 2PC):** Bạn cần một **"Người Điều phối Toàn cục" (Global Transaction Manager)** (như bạn nói, JTA là một API chuẩn cho việc này).
  1.  **Giai đoạn 1 (Prepare):** Người điều phối hỏi: "Quản lý A, anh sẵn sàng xuất hàng chưa?" (A: "Sẵn sàng!"). "Quản lý B, anh sẵn sàng nhận hàng chưa?" (B: "Sẵn sàng!").
  2.  **Giai đoạn 2 (Commit):** *Sau khi tất cả đều "Sẵn sàng"*, Người điều phối ra lệnh: "Cả hai cùng thực hiện ngay!".
  * *Nếu có lỗi:* Nếu ở Giai đoạn 1, Kho B báo "Tôi bị lỗi, không nhận được", Người điều phối sẽ ra lệnh cho cả A và B cùng `ROLLBACK`.

**Kết luận:**
* Dùng **Local Transaction** (ví dụ: `@Transactional` của Spring với `DataSourceTransactionManager`) khi bạn chỉ làm việc với **một database**. (Đây là 95% các trường hợp).
* Dùng **Global Transaction** (ví dụ: `@Transactional` với `JtaTransactionManager`) khi bạn cần đảm bảo tính "Tất cả hoặc Không gì cả" trên **nhiều hệ thống** (2 DB, hoặc 1 DB + 1 Queue).

Chào bạn, câu trả lời của bạn là **hoàn toàn chính xác** và đã đi thẳng vào cơ chế kỹ thuật (technical mechanism) mà Spring sử dụng.

Đây là một trong những khái niệm quan trọng nhất của Spring Framework. Hãy cùng diễn giải chi tiết hơn để làm rõ "bức tranh toàn cảnh" này.

-----

## ❓ Câu hỏi: Giao dịch (transaction) có phải là một "cross-cutting concern" không? Spring triển khai nó như thế nào?

### 1\.  intersecting-circles: Giao dịch (Transaction) LÀ một Cross-Cutting Concern

Đúng vậy. **Cross-Cutting Concern** (khía cạnh xuyên suốt) là một "dịch vụ" hoặc "chức năng" (như logging, security, caching, transaction) mà nó *cắt ngang (cuts across)* nhiều lớp (layers) và nhiều module trong ứng dụng của bạn.

**💡 Ví dụ soê sánh: "An ninh Tòa nhà"**

Hãy tưởng tượng ứng dụng của bạn là một tòa nhà văn phòng, với nhiều phòng ban khác nhau:

* `UserService` (Phòng Nhân sự)
* `OrderService` (Phòng Kinh doanh)
* `PaymentService` (Phòng Kế toán)

Mỗi phòng ban (lớp Service) có một nghiệp vụ chính (business logic) riêng.

Bây giờ, hãy nghĩ về **An ninh (Security)** hoặc **Giao dịch (Transaction)**:

* Bạn muốn "An ninh" được áp dụng cho **tất cả** các phòng ban.
* Bạn muốn "Giao dịch" (tính toàn vẹn dữ liệu) được áp dụng cho **tất cả** các phòng ban.

**Vấn đề (Nếu không có AOP):**
Nếu không có AOP, bạn sẽ phải đặt một nhân viên an ninh *bên trong* mỗi phòng. Phòng Nhân sự tự thuê bảo vệ, phòng Kinh doanh cũng tự thuê. Code của bạn sẽ bị "rác" như thế này:

```java
// Logic nghiệp vụ bị "trộn lẫn" với logic giao dịch
public class UserService {
    public void createUser() {
        Transaction tx = null;
        try {
            // === BẮT ĐẦU logic Giao dịch ===
            tx = transactionManager.begin(); 
            
            // === Logic nghiệp vụ ===
            userRepo.save(...);
            auditRepo.save(...);
            // ======================
            
            // === KẾT THÚC logic Giao dịch ===
            tx.commit();
        } catch (Exception e) {
            tx.rollback(); // <-- Rất nhiều code lặp đi lặp lại
        }
    }
}
```

Bạn phải lặp lại khối `try-catch-commit-rollback` này ở *mọi* phương thức. Đây chính là "cross-cutting concern".

-----

### 2\. ⚙️ Spring triển khai nó như thế nào? (Dùng AOP và Proxy)

Spring giải quyết vấn đề này bằng cách sử dụng **AOP (Aspect-Oriented Programming - Lập trình hướng khía cạnh)**.

Thay vì đặt "bảo vệ" *bên trong* mỗi phòng, Spring đặt một **"Bảo vệ Trung tâm" (Proxy)** ở cửa ra vào chính của tòa nhà.

Đây là cách hoạt động chính xác như bạn đã mô tả:

1.  **Phát hiện (Detection):** Spring quét ứng dụng của bạn và thấy `@Transactional` trên lớp `UserService`.
2.  **Tạo Proxy (Wrapping):** Spring **không** đưa cho `UserController` một `UserService` thật. Thay vào đó, nó tạo ra một **Proxy** (một đối tượng "giả mạo") và "bọc" (wrap) `UserService` thật bên trong.
3.  **Can thiệp (Interception):**
  * Khi `UserController` gọi `userService.createUser()`, nó thực ra đang gọi `PROXY.createUser()`.
  * Bên trong cái Proxy này chính là logic của `TransactionInterceptor` (như bạn nói).

**Luồng thực thi (Flow of Execution):**

Hãy dùng lại phép ví von:

* **Proxy (`TransactionInterceptor`):** Là "anh bảo vệ" ở cửa.
* **`PlatformTransactionManager`:** Là "Sếp" (Giám đốc An ninh) mà anh bảo vệ sẽ báo cáo.
* **`UserService` thật (Target):** Là "nhân viên nghiệp vụ" ở bên trong.

Khi có một cuộc gọi đến `PROXY.createUser()`:

1.  **ANH BẢO VỆ (Proxy):** "Dừng lại\! Tôi thấy có dấu `@Transactional`."
2.  **ANH BẢO VỆ (Proxy):** "Alo Sếp (`PlatformTransactionManager`), tôi cần bắt đầu một giao dịch mới."
3.  **SẾP (Manager):** "Ok, lấy `Connection` A từ `DataSource`, tắt auto-commit. Bắt đầu giao dịch."
4.  **ANH BẢO VỆ (Proxy):** "Mời anh vào trong\!" -\> Gọi `UserService_THẬT.createUser()`.
5.  **NHÂN VIÊN (Target):** (Thực thi logic nghiệp vụ. Mọi lệnh `JdbcTemplate` bây giờ sẽ *tái sử dụng* `Connection` A).
6.  **NHÂN VIÊN (Target):** "Tôi làm xong rồi\!" (Trả kết quả, không ném exception).
7.  **ANH BẢO VỆ (Proxy):** "Tốt."
8.  **ANH BẢO VỆ (Proxy):** "Alo Sếp (`PlatformTransactionManager`), mọi thứ OK, `commit` giao dịch."
9.  **SẾP (Manager):** "Đã `commit` trên `Connection` A. Trả `Connection` A về pool."
10. **ANH BẢO VỆ (Proxy):** Trả kết quả về cho `UserController`.

**Trường hợp có Lỗi (Exception):**

* **Bước 6 (Thay đổi):** **NHÂN VIÊN (Target):** "Ối\! Lỗi `RuntimeException`\!" (Ném exception).
* **Bước 7 (Thay đổi):** **ANH BẢO VỆ (Proxy):** "Phát hiện lỗi\!"
* **Bước 8 (Thay đổi):** **ANH BẢO VỆ (Proxy):** "Alo Sếp (`PlatformTransactionManager`), có lỗi, `rollback` giao dịch ngay lập tức."
* **Bước 9 (Thay đổi):** **SẾP (Manager):** "Đã `rollback` trên `Connection` A. Trả `Connection` A về pool."
* **Bước 10 (Thay đổi):** Anh bảo vệ ném lại (re-throw) `RuntimeException` đó cho `UserController` xử lý.

**Tóm lại:**
Code nghiệp vụ của bạn (`UserService`) giờ đây **hoàn toàn sạch sẽ**. Nó không biết gì về `try-catch`, `commit`, hay `rollback`. Tất cả các logic "xuyên suốt" đó đã được chuyển giao cho **Proxy** (được triển khai bởi `TransactionInterceptor` và điều phối bởi `PlatformTransactionManager`).

## ❓ Câu hỏi: Bạn định nghĩa một giao dịch (transaction) trong Spring như thế nào? @Transactional làm gì? PlatformTransactionManager là gì?

Câu trả lời của bạn là **hoàn toàn chính xác**. Bạn đã vạch ra 3 bước thiết yếu để kích hoạt (enable) và sử dụng giao dịch trong Spring.

Hãy cùng diễn giải chi tiết 3 khái niệm này bằng một ví dụ so sánh.

### 💡 Ví dụ so sánh: "Hoạt động trong Kho bạc"

Hãy tưởng tượng `PlatformTransactionManager`, `@EnableTransactionManagement`, và `@Transactional` làm việc cùng nhau như một hệ thống an ninh cho một kho bạc ngân hàng (`DataSource`).

1.  **`PlatformTransactionManager` (Sếp An ninh):** Đây là "bộ não". Ông ấy là người *biết cách* khóa (`rollback`), mở (`commit`), và quản lý "cửa kho" (`Connection`). Bạn phải thuê (khai báo) một "Sếp An ninh" cụ thể (ví dụ: `DataSourceTransactionManager` cho kho bạc JDBC).

2.  **`@EnableTransactionManagement` (Công tắc Chính):** Đây là công tắc "Bật Hệ thống An ninh" cho toàn bộ tòa nhà. Nếu bạn không bật công tắc này (đặt nó trên lớp `@Configuration`), thì "Sếp An ninh" sẽ không bao giờ được kích hoạt.

3.  **`@Transactional` (Cảm biến Laser):** Đây là các cảm biến laser bạn đặt trên các cửa phòng cụ thể. Bạn đặt nó trên một phương thức (`public void transferMoney()`) để nói rằng: "Hoạt động này cực kỳ quan trọng. Hãy kích hoạt an ninh (bắt đầu giao dịch) khi ai đó bước vào, và tắt an ninh (commit/rollback) khi họ rời đi."

-----

### 1\. ⚙️ Cách bạn định nghĩa Giao dịch (3 bước)

Như bạn đã nêu, để làm cho giao dịch hoạt động, bạn cần 3 thứ:

**Bước 1: Bật Công tắc Chính (Chỉ 1 lần)**
Bạn đặt annotation này lên lớp `@Configuration` chính của mình để "bật" hệ thống AOP (Proxy) của Spring cho việc quản lý giao dịch.

```java
@Configuration
@EnableTransactionManagement // "Bật Hệ thống An ninh"
public class AppConfig {
    // ...
}
```

**Bước 2: Thuê "Sếp An ninh" (Chỉ 1 lần)**
Bạn phải cung cấp một `Bean` implement `PlatformTransactionManager`. Spring cần biết *loại* kho bạc nào nó đang quản lý.

* `DataSourceTransactionManager`: Dùng cho kho bạc JDBC (ví dụ: `JdbcTemplate`).
* `JpaTransactionManager`: Dùng cho kho bạc JPA (ví dụ: `EntityManager`).
* `JtaTransactionManager`: Dùng cho *nhiều* kho bạc (Global Transaction).

<!-- end list -->

```java
@Configuration
@EnableTransactionManagement
public class AppConfig {

    // "Đây là Sếp An ninh cho kho JDBC của tôi"
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        // ... cấu hình DataSource ...
    }
}
```

*(Lưu ý: Spring Boot sẽ tự động làm cả Bước 1 và Bước 2 cho bạn nếu nó thấy `spring-boot-starter-data-jpa` hoặc `jdbc` trong classpath).*

**Bước 3: Đặt "Cảm biến Laser"**
Bạn sử dụng `@Transactional` trên các phương thức (hoặc lớp) mà bạn muốn bảo vệ.

```java
@Service
public class BankService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // "Bảo vệ" phương thức này
    @Transactional
    public void transferMoney(long fromId, long toId, double amount) {
        // 1. TRỪ TIỀN A
        jdbcTemplate.update("UPDATE accounts SET balance = balance - ? WHERE id = ?", amount, fromId);
        
        // 2. CỘNG TIỀN B
        jdbcTemplate.update("UPDATE accounts SET balance = balance + ? WHERE id = ?", amount, toId);
        
        // Nếu không có lỗi, "Sếp An ninh" sẽ COMMIT
        // Nếu có lỗi (ví dụ: RuntimeException), "Sếp An ninh" sẽ ROLLBACK
    }
}
```

-----

### 2\. ⚡ @Transactional làm gì?

Như bạn đã nói, `@Transactional` là một "cờ hiệu" (marker). Khi Spring thấy nó, nó sẽ **tạo ra một Proxy** xung quanh lớp `BankService`.

Khi bạn gọi `bankService.transferMoney()`, bạn đang gọi **Proxy**, và Proxy này làm các việc sau (như bạn đã mô tả):

1.  **Trước khi chạy:** Proxy gọi `platformTransactionManager.getTransaction(...)` để **BẮT ĐẦU** một giao dịch (lấy `Connection`, tắt `autoCommit`).
2.  **Chạy code thật:** Proxy gọi `bankService_THẬT.transferMoney(...)`.
3.  **Sau khi chạy:**
  * Nếu phương thức chạy **thành công** (không ném exception): Proxy gọi `platformTransactionManager.commit(...)`.
  * Nếu phương thức ném ra một **`RuntimeException`** (hoặc `Error`): Proxy gọi `platformTransactionManager.rollback(...)`.

Như bạn đã liệt kê chính xác, annotation này cũng cho phép bạn cấu hình các hành vi nâng cao:

* **`propagation`:** (Quan trọng nhất) Giao dịch nên hành xử thế nào nếu một giao dịch khác đã tồn tại? (Ví dụ: `REQUIRED` - tham gia, `REQUIRES_NEW` - tạo cái mới).
* **`isolation`:** Mức độ cô lập (Isolation level) của giao dịch (ví dụ: `READ_COMMITTED`).
* **`readOnly`:** Đánh dấu là "chỉ đọc" để tối ưu hóa (optimization).
* **`rollbackFor` / `noRollbackFor`:** Tùy chỉnh việc exception nào sẽ (hoặc không) kích hoạt rollback.

-----

### 3\. 🕴️ PlatformTransactionManager là gì?

Như bạn đã định nghĩa, đây là **giao diện (interface) trừu tượng** cốt lõi của Spring cho việc quản lý giao dịch.

Nó là một sự **trừu tượng hóa (abstraction)**. Lớp nghiệp vụ (`BankService`) của bạn chỉ cần biết đến "Giao dịch", không cần biết bạn đang dùng JDBC, JPA, hay JTA.

`PlatformTransactionManager` giống như cái **"bảng điều khiển"** chuẩn cho mọi loại kho bạc. Nó có 3 nút chính:

* **`getTransaction(...)`:** (Nút "Bắt đầu") Lấy giao dịch hiện tại hoặc tạo mới.
* **`commit(...)`:** (Nút "Xác nhận") Lưu vĩnh viễn các thay đổi.
* **`rollback(...)`:** (Nút "Hoàn tác") Hủy bỏ mọi thay đổi.

`TransactionInterceptor` (anh bảo vệ proxy) chỉ cần biết cách nhấn 3 nút này. Nó không cần quan tâm "Sếp An ninh" (`DataSourceTransactionManager` hay `JpaTransactionManager`) thực sự thực hiện lệnh đó như thế nào bên trong.

## ❓ Câu hỏi: JdbcTemplate có thể tham gia vào một giao dịch (transaction) hiện có không?

**Vâng, hoàn toàn có thể.** Đây là một trong những tính năng mạnh mẽ và cốt lõi nhất của `JdbcTemplate`.

Câu trả lời của bạn đã nêu chính xác cơ chế kỹ thuật: nó hoạt động thông qua `DataSourceUtils` và `TransactionSynchronizationManager`.

Hãy cùng diễn giải điều này một cách dễ hiểu:

### 💡 Diễn giải: `JdbcTemplate` là một "Người thợ" thông minh

Hãy nghĩ về `JdbcTemplate` như một "người thợ" chuyên nghiệp. Và `Connection` (kết nối database) là "dụng cụ" (ví dụ: một cái búa).

**`JdbcTemplate` được thiết kế để "nhận biết giao dịch" (transaction-aware).** Nó không bao giờ tự ý quyết định, mà luôn *kiểm tra* xem có ai đang quản lý công việc hay không.

#### 1. Kịch bản KHÔNG có Giao dịch
* Bạn gọi `jdbcTemplate.update(...)`.
* **Thợ (`JdbcTemplate`) hỏi:** "Có ai đang quản lý giao dịch (transaction) cho tôi không?"
* **`TransactionSynchronizationManager` (Người giám sát):** "Không có."
* **Thợ (`JdbcTemplate`):** "OK. Tôi sẽ tự lo."
* Thợ (qua `DataSourceUtils`) đi đến kho (`DataSource`), **lấy (acquire)** một cái búa (`Connection`), dùng nó, rồi **trả (release)** ngay lập tức.
* *Kết quả: Lấy/Trả connection cho mỗi lần gọi.*

#### 2. Kịch bản CÓ Giao dịch (Câu trả lời cho câu hỏi của bạn)
* Một phương thức có `@Transactional` (chính là `TransactionInterceptor` / Proxy) được gọi.
* **Người quản lý (Proxy):** "Bắt đầu một dự án (transaction) lớn!"
* **Người quản lý (Proxy):** (Qua `PlatformTransactionManager`) đi đến kho, **lấy** một cái búa (`Connection-A`).
* **Người quản lý (Proxy):** Ghi vào sổ của **Người giám sát (`TransactionSynchronizationManager`):** "Dự án này (`Thread` này) đang sử dụng Búa-A (`Connection-A`)."
* Bây giờ, bên trong phương thức `@Transactional`, bạn gọi `jdbcTemplate.update(...)`.
* **Thợ (`JdbcTemplate`) hỏi:** "Có ai đang quản lý giao dịch cho tôi không?"
* **Người giám sát (`TransactionSynchronizationManager`):** "Có! Hãy dùng **Búa-A** đã được đăng ký."
* **Thợ (`JdbcTemplate`):** "Rõ!"
* Thợ (qua `DataSourceUtils`) **lấy Búa-A** (connection *đang tồn tại*), dùng nó, và **KHÔNG trả lại kho** (vì dự án chưa xong).
* Bạn gọi tiếp `jdbcTemplate.query(...)` (vẫn trong cùng transaction).
* **Thợ (`JdbcTemplate`) hỏi:** "Có ai...?"
* **Người giám sát (`TransactionSynchronizationManager`):** "Vẫn là **Búa-A**!"
* Thợ tiếp tục dùng **Búa-A**.

Cuối cùng, khi phương thức `@Transactional` kết thúc, **Người quản lý (Proxy)** sẽ là người ra lệnh `commit` (hoặc `rollback`) và **tự mình trả Búa-A** về kho.

**Tóm lại:**
Chính xác như bạn nói, `JdbcTemplate` sử dụng `DataSourceUtils`, và `DataSourceUtils` đủ thông minh để kiểm tra với `TransactionSynchronizationManager` xem có giao dịch nào đang hoạt động trên `Thread` hiện tại hay không.
* Nếu có, nó **tái sử dụng** `Connection` của giao dịch đó.
* Nếu không, nó lấy một `Connection` mới và tự quản lý.

Điều này cho phép `JdbcTemplate` tham gia một cách liền mạch vào bất kỳ giao dịch nào (dù là `@Transactional` hay bạn tự tạo) mà không cần bạn phải viết thêm bất kỳ dòng code nào.

## ❓ Câu hỏi: Mức độ cô lập giao dịch (transactional isolation level) là gì? Chúng ta có bao nhiêu mức và chúng được sắp xếp như thế nào?

Chào bạn, đây là một trong những khái niệm cốt lõi và quan trọng nhất trong quản lý giao dịch. Câu trả lời của bạn là **rất chi tiết và chính xác**. Bạn đã nắm rõ 3 vấn đề đọc (read phenomena) và 4 mức độ cô lập.

Phần diễn giải này sẽ dùng một **ví dụ so sánh** để giúp bạn dễ hình dung và ghi nhớ các khái niệm này.

-----

### 1\. 💡 Mức độ Cô lập Giao dịch là gì?

Như bạn đã nói, mức độ cô lập (Isolation) là một trong 4 trụ cột của **ACID**. Nó định nghĩa mức độ "nhìn thấy" (visibility) lẫn nhau giữa các giao dịch (transaction) đang chạy đồng thời.

**💡 Ví dụ so sánh: "Google Docs thời kỳ đầu"**

Hãy tưởng tượng Giao dịch là một **Người dùng (User)** đang mở và chỉnh sửa một tài liệu Google Doc (`Database`).

Isolation Level chính là "cài đặt" để trả lời câu hỏi:

> "Khi User A đang chỉnh sửa, thì User B (mở cùng lúc) sẽ **nhìn thấy** những thay đổi của User A *ngay lập tức* (đang gõ dở), *chỉ khi A lưu* (commit), hay *không thấy gì cả* cho đến khi A đóng tài liệu?"

Đây là sự đánh đổi (trade-off) kinh điển:

* **Isolation càng cao:** Dữ liệu càng **nhất quán (Consistent)** (User A không bị User B làm phiền), nhưng **hiệu năng (Concurrency)** càng thấp (User B phải chờ User A).
* **Isolation càng thấp:** **Hiệu năng** càng cao (nhiều user cùng làm), nhưng dữ liệu càng **không nhất quán** (dễ bị lỗi).

-----

### 2\. 😱 Ba "Vấn đề" Đọc (Read Phenomena)

Để hiểu các mức độ, trước tiên ta phải hiểu 3 "lỗi" có thể xảy ra khi isolation quá thấp, như bạn đã mô tả rất chính xác:

#### A. Dirty Read (Đọc bẩn)

* **Ví dụ của bạn:** TX A đọc dữ liệu mà TX B *vừa sửa nhưng chưa `COMMIT`*.
* **Analogy:** User A nhìn thấy User B đang **gõ dở** một câu. User B sau đó **xóa hết (ROLLBACK)** câu đó đi.
* **Hậu quả:** User A đã đọc phải "dữ liệu rác" (dirty data) không bao giờ thực sự tồn tại. Đây là mức độ nguy hiểm nhất.

#### B. Non-Repeatable Read (Đọc không lặp lại)

* **Ví dụ của bạn:** TX A đọc 1 hàng. TX B *cập nhật (UPDATE)* hàng đó và `COMMIT`. TX A đọc *lại chính hàng đó* và thấy giá trị đã bị thay đổi.
* **Analogy:** User A đọc Dòng 5. User B vào sửa Dòng 5 và lưu (commit). User A đọc lại Dòng 5 và thấy nội dung đã khác.
* **Hậu quả:** Trong cùng một giao dịch, việc đọc *cùng một dữ liệu* 2 lần lại cho ra 2 kết quả khác nhau.

#### C. Phantom Read (Đọc bóng ma)

* **Ví dụ của bạn:** TX A đọc một *phạm vi* (range) (ví dụ: `WHERE id BETWEEN 5 AND 10`), đếm được 5 hàng. TX B *chèn (INSERT)* một hàng mới (ID=7) vào *phạm vi đó* và `COMMIT`. TX A đọc *lại phạm vi đó* và đếm được 6 hàng.
* **Analogy:** User A đếm số lượng đoạn văn trên Trang 2 (có 5 đoạn). User B thêm một đoạn văn mới vào Trang 2 và lưu (commit). User A đếm lại số đoạn văn trên Trang 2 và "giật mình" thấy có 6 đoạn.
* **Hậu quả:** Một hàng "bóng ma" (phantom) tự nhiên xuất hiện, mặc dù các hàng A đã đọc trước đó không hề thay đổi (khác với Non-Repeatable Read).

-----

### 3\. 🚦 Bốn Mức độ Cô lập (Sắp xếp từ thấp đến cao)

Đây là 4 cấp độ tiêu chuẩn để giải quyết các vấn đề trên, như bạn đã liệt kê.

#### 1\. `READ_UNCOMMITTED` (Đọc chưa commit)

* **Mô tả:** Mức độ thấp nhất, "thấy tất cả".
* **Analogy:** "Chế độ xem trực tiếp." Bạn thấy mọi phím gõ của người khác, ngay cả khi họ chưa lưu.
* **Vấn đề:** Cho phép **Dirty Read**, **Non-Repeatable Read**, **Phantom Read**.
* **Hiệu năng:** Cao nhất (vì không khóa gì cả).

#### 2\. `READ_COMMITTED` (Đọc đã commit)

* **Mô tả:** Mức độ mặc định của hầu hết các database (PostgreSQL, Oracle). Bạn chỉ "nhìn thấy" dữ liệu đã được `COMMIT`.
* **Analogy:** "Chế độ bình luận." Bạn chỉ thấy thay đổi của người khác khi họ đã nhấn "lưu/commit".
* **Giải quyết:** Ngăn chặn được **Dirty Read**.
* **Vấn đề:** Vẫn còn **Non-Repeatable Read** và **Phantom Read**.

#### 3\. `REPEATABLE_READ` (Đọc lặp lại)

* **Mô tả:** Mức độ mặc định của MySQL (InnoDB). Nó đảm bảo rằng nếu bạn đọc *cùng một hàng* nhiều lần trong một giao dịch, bạn sẽ luôn nhận được kết quả giống hệt như lần đọc đầu tiên.
* **Analogy:** "Chế độ Snapshot." Khi bạn mở tài liệu, bạn nhận được một "ảnh chụp nhanh" (snapshot) của nó. Mọi thay đổi của người khác (dù đã commit) sẽ không ảnh hưởng đến "ảnh chụp" của bạn.
* **Giải quyết:** Ngăn chặn **Dirty Read** và **Non-Repeatable Read**.
* **Vấn đề:** Vẫn còn **Phantom Read** (theo chuẩn SQL). (Lưu ý: MySQL InnoDB giải quyết được cả Phantom Read ở mức này bằng *gap locking*, nhưng theo chuẩn chung thì vẫn bị).

#### 4\. `SERIALIZABLE` (Tuần tự hóa)

* **Mô tả:** Mức độ cao nhất, an toàn nhất. Nó "khóa" toàn bộ phạm vi dữ liệu bạn đang đọc.
* **Analogy:** "Chế độ Khóa độc quyền." Khi User A vào tài liệu, User B bị "chặn" ở cửa, không thể vào chỉnh sửa cho đến khi User A làm xong và rời đi.
* **Giải quyết:** Ngăn chặn **TẤT CẢ** 3 vấn đề.
* **Hiệu năng:** Thấp nhất (rất tệ cho concurrency).

-----

### 4\. 📊 Bảng tóm tắt (Rất quan trọng cho kỳ thi)

Bảng của bạn là hoàn hảo, đây là phiên bản tiếng Việt để bạn dễ nhớ:

| Mức độ Cô lập | Đọc bẩn (Dirty Read) | Đọc không lặp lại (Non-Repeatable) | Đọc bóng ma (Phantom) | Hiệu năng (Concurrency) |
| :--- | :---: | :---: | :---: | :---: |
| **`READ_UNCOMMITTED`** | 😱 Khả thi | 😱 Khả thi | 😱 Khả thi | Rất cao |
| **`READ_COMMITTED`** | ✅ Không thể | 😱 Khả thi | 😱 Khả thi | Cao |
| **`REPEATABLE_READ`** | ✅ Không thể | ✅ Không thể | 😱 Khả thi | Trung bình |
| **`SERIALIZABLE`** | ✅ Không thể | ✅ Không thể | ✅ Không thể | Rất thấp |

-----

### 5\. 🛠️ Cấu hình trong Spring

Bạn đã chỉ ra chính xác cách cấu hình nó trong Spring:

```java
// Đặt mức độ cô lập cho phương thức này là an toàn nhất
@Transactional(isolation = Isolation.SERIALIZABLE)
public void extremelyCriticalFinancialReport() {
    // ...
}

// Dùng mức độ mặc định của DB (thường là READ_COMMITTED)
@Transactional
public void normalUpdate() {
    // ...
}
```

## ❓ Câu hỏi: @EnableTransactionManagement dùng để làm gì?

Câu trả lời của bạn là **hoàn toàn chính xác**.

`@EnableTransactionManagement` chính là **"công tắc nguồn"** (on-switch) để bật hệ thống quản lý giao dịch dựa trên annotation (`@Transactional`) của Spring.

---

### 1. 💡 Diễn giải: "Công tắc Bật Hệ thống An ninh"

Hãy nghĩ về ứng dụng của bạn như một tòa nhà văn phòng:

* **`@Configuration`**: Là phòng điều khiển trung tâm.
* **`@Transactional`**: Là các "cảm biến laser" bạn đặt trên các cửa phòng quan trọng (ví dụ: `transferMoney()`).
* **`PlatformTransactionManager`**: Là "Sếp An ninh" (người biết cách khóa/mở kho).

Bạn có thể lắp đặt bao nhiêu "cảm biến" (`@Transactional`) tùy thích, nhưng nếu bạn không bật **"công tắc nguồn"** (`@EnableTransactionManagement`) trong phòng điều khiển, thì sẽ không có gì xảy ra cả.

Khi bạn thêm `@EnableTransactionManagement`, bạn đang nói với Spring:
> "Hãy kích hoạt hệ thống! Hãy tìm tất cả các bean có `@Transactional` và **tự động bọc (wrap) chúng trong một Proxy** (anh bảo vệ) để quản lý giao dịch (commit/rollback)."

---

### 2. ⚙️ Nó thực sự làm gì? (Kỹ thuật)

Như bạn đã nêu, khi annotation này được thêm vào, Spring sẽ tự động đăng ký các bean "hậu trường" cần thiết, quan trọng nhất là:

1.  **Một `BeanPostProcessor`:** Thằng này sẽ quét tất cả các bean của bạn sau khi chúng được tạo.
2.  **Kiểm tra:** Nếu nó thấy một bean (ví dụ: `BankService`) có đánh dấu `@Transactional`, nó sẽ **tạo ra một Proxy** (sử dụng AOP).
3.  **Proxy:** Đối tượng Proxy này chứa logic của `TransactionInterceptor` (anh bảo vệ). Khi một phương thức `@Transactional` được gọi, `TransactionInterceptor` sẽ can thiệp, gọi `PlatformTransactionManager` để bắt đầu/commit/rollback giao dịch.

Nếu không có `@EnableTransactionManagement`, bước "tạo Proxy" này sẽ không bao BGiờ xảy ra, và annotation `@Transactional` của bạn sẽ bị bỏ qua một cách "âm thầm".

*(**Lưu ý:** Spring Boot sẽ tự động thực hiện việc này cho bạn nếu nó thấy `spring-boot-starter-data-jpa` hoặc `spring-boot-starter-jdbc` trong classpath. Bạn không cần thêm `@EnableTransactionManagement` một cách thủ công trong Spring Boot).*

---

### 3. 🎛️ Các thuộc tính (Attributes)

Bạn đã nêu đúng các thuộc tính cấu hình nâng cao của nó:

* **`proxyTargetClass`**:
  * `false` (Mặc định): Spring dùng **JDK Dynamic Proxy** (dựa trên interface). Yêu cầu `BankService` của bạn phải implement `IBankService`.
  * `true`: Spring dùng **CGLIB Proxy** (dựa trên kế thừa). Không cần interface.

* **`mode`**:
  * `PROXY` (Mặc định): Sử dụng cơ chế Proxy như đã giải thích.
    * *Nhược điểm:* **Không hỗ trợ "tự gọi" (self-invocation)**. (Một phương thức `@Transactional` gọi một phương thức `@Transactional` khác *bên trong cùng một lớp* sẽ bỏ qua Proxy).
  * `ASPECTJ` (Nâng cao): Chuyển từ "runtime weaving" (tạo proxy lúc chạy) sang **"compile-time weaving" (dệt code lúc biên dịch)**. Nó sửa đổi bytecode của file `.class` của bạn.
    * *Ưu điểm:* **Hỗ trợ "tự gọi" (self-invocation)**, vì code quản lý giao dịch được "dệt" thẳng vào phương thức của bạn, không cần Proxy.
    * *Nhược điểm:* Cài đặt phức tạp hơn (cần AspectJ compiler).

## ❓ Câu hỏi: Transaction Propagation (Lan truyền Giao dịch) có nghĩa là gì?

Câu trả lời của bạn là **hoàn toàn chính xác**.

**Transaction Propagation** (Sự lan truyền giao dịch) là một bộ quy tắc (rule) định nghĩa hành vi của một phương thức `@Transactional` khi nó được gọi *từ bên trong* một phương thức `@Transactional` khác (tức là khi đã có một giao dịch đang chạy).

Nó trả lời câu hỏi:

> "Nếu một giao dịch (Transaction A) đang chạy, và nó gọi một phương thức cũng yêu cầu giao dịch (Method B), thì Method B nên:
>
> 1.  **Tham gia (Join)** vào Transaction A?
> 2.  **Tạo một giao dịch mới (New)** và tạm dừng (suspend) A?
> 3.  **Báo lỗi (Throw exception)**?
> 4.  Hay chạy **mà không cần** giao dịch?"

-----

### 💡 Ví dụ so sánh: "Hợp đồng Công việc"

Hãy tưởng tượng Giao dịch là một **"Hợp đồng Công việc"** (Project Contract).

* **Bắt đầu Giao dịch** = Bắt đầu một Hợp đồng mới.
* **Gọi phương thức `@Transactional`** = Giao một "nhiệm vụ" (Task) cho một nhân viên (`Method B`).

Khi bạn (Method A) đã có một "Hợp đồng A" đang chạy và bạn gọi "Nhân viên B", Propagation sẽ quyết định cách Nhân viên B hành xử.

-----

### 7 Mức độ Lan truyền (Propagation Levels)

Dưới đây là diễn giải chi tiết về 7 mức độ bạn đã liệt kê:

#### 1\. `REQUIRED` (Mặc định)

* **Ý nghĩa:** "Hỗ trợ giao dịch hiện tại; tạo mới nếu chưa có."
* **Analogy:** "Nếu bạn (A) đang làm theo Hợp đồng A, tôi (B) sẽ **tham gia** và làm việc theo Hợp đồng A của bạn. Nếu bạn không có hợp đồng nào, tôi sẽ **tự tạo** Hợp đồng B của riêng tôi."
* **Kết quả:** Đây là mức phổ biến nhất. Đảm bảo code *luôn* chạy trong một giao dịch.
  ```java
  @Transactional(propagation = Propagation.REQUIRED) // (Đây là mặc định)
  public void methodB() { ... }
  ```

#### 2\. `REQUIRES_NEW`

* **Ý nghĩa:** "Luôn tạo một giao dịch mới; tạm dừng giao dịch hiện tại nếu có."
* **Analogy:** "Tôi (B) **không quan tâm** đến Hợp đồng A của bạn. Hãy **tạm dừng** Hợp đồng A lại. Tôi sẽ **luôn bắt đầu Hợp đồng B MỚI** cho riêng mình. Khi tôi làm xong (commit/rollback B), bạn có thể tiếp tục Hợp đồng A."
* **Kết quả:** Giao dịch của B là hoàn toàn độc lập. Nếu B `rollback`, A *không* bị ảnh hưởng (và ngược lại). Rất hữu ích cho việc ghi log hoặc auditing (kiểm toán), nơi bạn muốn hành động log được `commit` *ngay cả khi* giao dịch chính bị `rollback`.
  ```java
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void logAudit(...) { 
      // Logic log này sẽ commit ngay cả khi giao dịch bên ngoài rollback
  }
  ```

#### 3\. `SUPPORTS`

* **Ý nghĩa:** "Hỗ trợ giao dịch hiện tại; thực thi không cần giao dịch nếu chưa có."
* **Analogy:** "Nếu bạn (A) có Hợp đồng A, tôi (B) sẽ tham gia. Nếu bạn không có, tôi cũng **không cần** hợp đồng. Tôi cứ thế làm việc thôi (non-transactionally)."
* **Kết quả:** Hữu ích cho các phương thức "chỉ đọc" (read-only) có thể được gọi từ cả bên trong và bên ngoài một giao dịch.

#### 4\. `MANDATORY`

* **Ý nghĩa:** "Hỗ trợ giao dịch hiện tại; ném ra exception nếu chưa có."
* **Analogy:** "Tôi (B) là nhân viên cấp cao. Tôi **bắt buộc** phải làm việc dưới một Hợp đồng. Nếu bạn (A) gọi tôi mà không có Hợp đồng A, tôi sẽ **báo lỗi (throw exception)**."
* **Kết quả:** Hiếm dùng. Dùng để khẳng định (assert) rằng một phương thức *phải* được gọi từ bên trong một giao dịch.

#### 5\. `NOT_SUPPORTED`

* **Ý nghĩa:** "Thực thi không cần giao dịch; tạm dừng giao dịch hiện tại nếu có."
* **Analogy:** "Tôi (B) không thích Hợp đồng. Nếu bạn (A) đang có Hợp đồng A, hãy **tạm dừng** nó. Tôi sẽ làm việc của mình **mà không cần hợp đồng** (non-transactionally). Khi tôi xong, bạn tiếp tục Hợp đồng A."
* **Kết quả:** Hữu ích khi bạn cần gọi một phương thức "ngoại đạo" không nên tham gia vào giao dịch.

#### 6\. `NEVER`

* **Ý nghĩa:** "Thực thi không cần giao dịch; ném ra exception nếu có giao dịch đang chạy."
* **Analogy:** "Tôi (B) ghét Hợp đồng. Nếu tôi phát hiện bạn (A) đang có Hợp đồng A, tôi sẽ **báo lỗi (throw exception)**."
* **Kết quả:** Hiếm dùng. Dùng để đảm bảo một phương thức *không bao giờ* được gọi từ bên trong một giao dịch.

#### 7\. `NESTED` (Lồng nhau)

* **Ý nghĩa:** "Thực thi trong một giao dịch lồng nhau (nested) nếu có giao dịch hiện tại, nếu không, hoạt động như `REQUIRED`."
* **Analogy:** "Tôi (B) sẽ tham gia Hợp đồng A của bạn, nhưng tôi muốn một **'Phụ lục hợp đồng' (Savepoint)** của riêng tôi. Nếu tôi (B) thất bại, tôi chỉ cần **quay lại 'Savepoint'** của mình. Nếu bạn (A) (giao dịch cha) thất bại, toàn bộ hợp đồng (cả tôi) đều bị hủy (`rollback`)."
* **Kết quả:** Giống `REQUIRED` nhưng cho phép rollback một phần (partial rollback) của giao dịch con.
* **Lưu ý:** Tính năng này **phải được hỗ trợ** bởi `DataSource` (ví dụ: JDBC Savepoints). Nhiều `JpaTransactionManager` không hỗ trợ nó.

-----

### 📊 Tóm tắt

| Propagation | Đã có Giao dịch (TX A)? | Chưa có Giao dịch? |
| :--- | :--- | :--- |
| **`REQUIRED`** (Default) | Tham gia TX A | **Tạo TX Mới** |
| **`REQUIRES_NEW`** | Tạm dừng A, **Tạo TX Mới (B)** | **Tạo TX Mới** |
| **`SUPPORTS`** | Tham gia TX A | Chạy *không* TX |
| **`MANDATORY`** | Tham gia TX A | **Báo lỗi (Exception)** |
| **`NOT_SUPPORTED`** | Tạm dừng A, Chạy *không* TX | Chạy *không* TX |
| **`NEVER`** | **Báo lỗi (Exception)** | Chạy *không* TX |
| **`NESTED`** | Tạo "Savepoint" (lồng nhau) | **Tạo TX Mới** |

## ❓ Câu hỏi: Điều gì xảy ra nếu một phương thức có `@Transactional` gọi một phương thức khác cũng có `@Transactional` trên cùng một instance đối tượng?

Câu trả lời của bạn là **hoàn toàn chính xác**. Đây là một "cái bẫy" kinh điển trong Spring gọi là **"self-invocation" (tự gọi chính mình)**.

**Kết quả:** Annotation `@Transactional` trên phương thức thứ hai sẽ **bị bỏ qua hoàn toàn**. Nó sẽ *không* tạo giao dịch mới, *không* tham gia, và *không* có hành vi propagation nào được áp dụng. Phương thức thứ hai sẽ chạy bên trong giao dịch (nếu có) của phương thức đầu tiên.

-----

### 1\. 💡 Tại sao nó bị bỏ qua? (Giải thích bằng Proxy)

Hãy dùng lại ví dụ "Bảo vệ Proxy" của chúng ta:

* **`UserServiceImpl` (Target):** Là đối tượng thật, chứa code nghiệp vụ.
* **`UserServiceProxy` (Proxy):** Là "anh bảo vệ" được Spring tạo ra, đứng bên ngoài `UserServiceImpl`. Anh ta là người xử lý `@Transactional`.
* **`UserController` (Bên ngoài):** Muốn gọi `UserService`.

**Dòng sự kiện:**

1.  `UserController` gọi `userService.methodA()`.
2.  Cuộc gọi này đến từ *bên ngoài*, vì vậy nó bị **Proxy** (anh bảo vệ) chặn lại.
3.  **Proxy** thấy `@Transactional` trên `methodA()`. Nó liền bắt đầu một Giao dịch (Transaction A).
4.  **Proxy** gọi `UserServiceImpl_THẬT.methodA()`.
5.  Bên trong `methodA()`, code của bạn chạy và gọi `this.methodB()`.

**Đây là điểm mấu chốt:**
6\.  Từ khóa `this` trỏ đến **instance `UserServiceImpl_THẬT`**, KHÔNG phải `Proxy`.
7\.  Cuộc gọi `this.methodB()` là một cuộc gọi *bên trong* đối tượng. Nó giống như bạn đi từ phòng làm việc A sang phòng làm việc B *bên trong* tòa nhà.
8\.  Cuộc gọi **hoàn toàn bỏ qua (bypasses)** "anh bảo vệ" (Proxy) đang đứng ở sảnh chính.
9\.  Vì Proxy không bao giờ thấy cuộc gọi đến `methodB()`, nó không thể đọc annotation `@Transactional` (ví dụ: `REQUIRES_NEW`) trên `methodB()`.

**Kết quả:** `methodB()` chạy như một phương thức Java bình thường, bên trong Giao dịch A (do `methodA()` đã tạo ra).

-----

### 2\. ⚙️ Các giải pháp (Như bạn đã nêu)

#### A. AspectJ (Cách "chuẩn" nhất)

* **Cách làm:** Như bạn đã mô tả: thêm `spring-aspects`, plugin Maven, và quan trọng nhất là `@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)`.
* **Tại sao hoạt động?** AspectJ không dùng Proxy. Nó là một "thợ dệt" (weaver). Nó **sửa đổi bytecode** (file `.class`) của bạn *lúc biên dịch (compile-time)*. Nó "dệt" (injects) code xử lý giao dịch *trực tiếp vào bên trong* `methodA` và `methodB`.
* Vì code nằm *bên trong* phương thức, nên ngay cả khi bạn gọi `this.methodB()`, code xử lý giao dịch vẫn được thực thi.

#### B. "Tự tiêm" (Self-Injection) - Một cách giải quyết

Đây là một "mẹo" phổ biến để giải quyết vấn đề này mà *không cần* AspectJ.

```java
@Service
public class UserServiceImpl implements UserService {

    // 1. Tiêm chính cái proxy của mình vào
    // (Spring đủ thông minh để tiêm Proxy, không phải "this")
    @Autowired
    private UserService self; 

    @Override
    @Transactional
    public void methodA() {
        // ... làm gì đó ...
        
        // 2. ĐỪNG GỌI "this.methodB()"
        // HÃY GỌI QUA PROXY ĐÃ TIÊM
        self.methodB();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void methodB() {
        // Code này sẽ chạy trong một giao dịch MỚI
    }
}
```

* **Tại sao hoạt động?** Khi `methodA()` gọi `self.methodB()`, nó đang gọi **Proxy** (vì `self` giữ tham chiếu đến Proxy). Cuộc gọi này đi "ra ngoài sảnh", bị "anh bảo vệ" (Proxy) chặn lại. Anh bảo vệ thấy `@Transactional(REQUIRES_NEW)` và xử lý nó một cách chính xác.

## ❓ Câu hỏi: Chính sách rollback (hoàn tác) mặc định là gì? Bạn có thể ghi đè (override) nó như thế nào?

Câu trả lời của bạn là **hoàn toàn chính xác**. Đây là một quy tắc quan trọng của Spring transaction.

### 1\. 📜 Chính sách Mặc định: Chỉ Rollback với Unchecked Exceptions

Theo mặc định, Spring AOP (`@Transactional`) sẽ "lắng nghe" các exception (ngoại lệ) ném ra từ phương thức của bạn. Nó tuân theo quy tắc:

1.  **Nếu phương thức ném ra `RuntimeException` (Unchecked) hoặc `Error`:**

  * **Hành động:** Giao dịch sẽ tự động **`ROLLBACK`** (hoàn tác).
  * **Lý do:** Spring coi đây là các lỗi hệ thống, lỗi lập trình (ví dụ: `NullPointerException`, `IllegalArgumentException`, `DataAccessException`). Đây là những lỗi không lường trước được, và an toàn nhất là hủy bỏ giao dịch để đảm bảo tính toàn vẹn dữ liệu.

2.  **Nếu phương thức ném ra một `Checked Exception` (ví dụ: `IOException`, hoặc `Exception` tùy chỉnh của bạn):**

  * **Hành động:** Giao dịch sẽ **`COMMIT`** (lưu lại).
  * **Lý do:** Đây là một di sản (legacy) từ thời EJB. Triết lý là: "Checked exceptions là các lỗi *nghiệp vụ* (business exceptions) đã được lường trước (ví dụ: `InsufficientFundsException` - Lỗi không đủ tiền). Lập trình viên có thể muốn `catch` lỗi này, ghi log, nhưng vẫn `commit` các phần khác của giao dịch."

### 2\. ⚙️ Cách Ghi đè (Override)

Như bạn đã nói, Spring cung cấp 2 thuộc tính mạnh mẽ trong `@Transactional` để bạn thay đổi hoàn toàn hành vi mặc định này.

* **`rollbackFor`**

  * **Mục đích:** "Ép buộc" Spring phải **`ROLLBACK`** khi gặp một `Checked Exception` cụ thể.
  * **Cách dùng:** Bạn nói: "Mặc dù đây là checked exception, nhưng tôi *muốn* nó rollback."

* **`noRollbackFor`**

  * **Mục đích:** "Ngăn chặn" Spring **`ROLLBACK`** khi gặp một `Unchecked Exception` cụ thể.
  * **Cách dùng:** Bạn nói: "Mặc dù đây là runtime exception, nhưng tôi *không muốn* nó rollback."

-----

### 3\. 💡 Ví dụ (Các ví dụ của bạn là hoàn hảo)

Các ví dụ của bạn đã minh họa chính xác 4 kịch bản:

**Kịch bản 1: Mặc định + Unchecked (Sẽ Rollback)**
Đây là hành vi phổ biến nhất.

```java
@Transactional
public void methodWithUncheckedExceptionCausingRollback() {
    // Ném ra một RuntimeException -> Giao dịch sẽ ROLLBACK
    throw new IllegalArgumentException("Lỗi, sẽ rollback");
}
```

**Kịch bản 2: Mặc định + Checked (Sẽ Commit)**
Đây là "cái bẫy" mà nhiều người mới gặp phải.

```java
// Một checked exception tùy chỉnh
class CustomException extends Exception {
    public CustomException(String msg) { super(msg); }
}

@Transactional
public void methodWithCheckedExceptionNotCausingRollback() throws CustomException {
    // Ném ra một Checked Exception -> Giao dịch sẽ COMMIT (Không rollback)
    throw new CustomException("Lỗi nghiệp vụ, sẽ commit");
}
```

**Kịch bản 3: Ghi đè `noRollbackFor` (Ngăn Rollback)**
Trường hợp này hiếm gặp, nhưng dùng để nói rằng một lỗi runtime cụ thể không nghiêm trọng.

```java
@Transactional(noRollbackFor = IllegalArgumentException.class)
public void methodWithUncheckedExceptionNotCausingRollback() {
    // Ném ra RuntimeException, nhưng ta đã chỉ định
    // "không rollback" cho nó -> Giao dịch sẽ COMMIT
    throw new IllegalArgumentException("Lỗi, nhưng vẫn commit");
}
```

**Kịch bản 4: Ghi đè `rollbackFor` (Ép Rollback)**
Đây là cách thực hành **rất tốt và phổ biến**. Bạn nên luôn dùng cách này cho các lỗi nghiệp vụ mà bạn muốn hoàn tác.

```java
@Transactional(rollbackFor = CustomException.class)
public void methodWithCheckedExceptionCausingRollback() throws CustomException {
    // Ném ra Checked Exception, nhưng ta đã chỉ định
    // "rollback" cho nó -> Giao dịch sẽ ROLLBACK
    throw new CustomException("Lỗi nghiệp vụ, và sẽ rollback");
}
```

**Khuyến nghị (Best Practice):** Nếu bạn định nghĩa các `Checked Exception` cho nghiệp vụ (ví dụ: `InsufficientFundsException`), hãy luôn thêm `(rollbackFor = InsufficientFundsException.class)` để đảm bảo giao dịch được hoàn tác đúng như mong đợi.

## ❓ Câu hỏi: Chính sách rollback mặc định trong JUnit test là gì, khi bạn dùng `@RunWith(SpringJUnit4ClassRunner.class)` hoặc `@ExtendWith(SpringExtension.class)` và chú thích (annotate) phương thức `@Test` của mình bằng `@Transactional`?

Câu trả lời của bạn là **hoàn toàn chính xác**.

Đây là một điểm khác biệt then chốt giữa môi trường *production* (sản xuất) và môi trường *test*.

-----

### 1\. 🏁 Chính sách Mặc định: Luôn Luôn `ROLLBACK`

Như bạn đã nói, trong một phương thức `@Test` được chú thích `@Transactional`, chính sách mặc định là **luôn luôn hoàn tác (ROLLBACK)** giao dịch khi phương thức test kết thúc.

* **Trong Production (`@Service`):** Mặc định là `COMMIT` khi thành công và `ROLLBACK` khi có `RuntimeException`.
* **Trong Test (`@Test`):** Mặc định là **luôn `ROLLBACK`**, bất kể test thành công (pass) hay thất bại (fail).

-----

### 2\. 💡 Tại sao? (Lý do: Đảm bảo Test Isolation)

Lý do, như bạn đã nêu, là để đảm bảo **tính lặp lại (repeatability)** và **tính cô lập (isolation)** của các bài test.

Các bài test (đặc biệt là unit test và integration test) phải được "cô lập" với nhau. Kết quả của Test A không bao giờ được ảnh hưởng đến kết quả của Test B.

**Hãy xem kịch bản "thảm họa" nếu mặc định là `COMMIT`:**

```java
@Test
@Transactional
public void testA_createUser() {
    // Test này tạo user mới với ID 1
    userRepository.save(new User(1L, "Alice"));
    // Giả sử nó COMMIT ở đây
}

@Test
@Transactional
public void testB_countUsers() {
    // Test này đếm số lượng user
    long count = userRepository.count();
    assertEquals(0, count); // Mong đợi là 0 user (DB trống)
}
```

* Nếu `testA_createUser` chạy trước `testB_countUsers` -\> `testB` sẽ **THẤT BẠI** (vì nó đếm được 1 user).
* Nếu `testB_countUsers` chạy trước `testA_createUser` -\> `testB` sẽ **THÀNH CÔNG** (vì nó đếm được 0 user).

Kết quả test của bạn trở nên *phụ thuộc vào thứ tự* (order-dependent) và không đáng tin cậy.

**Giải pháp (Mặc định `ROLLBACK`):**
Với chính sách rollback mặc định, luồng chạy sẽ là:

1.  `testA_createUser` chạy.
2.  Giao dịch (Transaction) bắt đầu.
3.  User "Alice" được `INSERT` vào DB.
4.  Test kết thúc.
5.  Spring Test Context tự động **`ROLLBACK`** giao dịch.
6.  User "Alice" bị **xóa** khỏi DB (như thể chưa bao giờ tồn tại).
7.  `testB_countUsers` chạy trên một database hoàn toàn "sạch sẽ".
8.  Test thành công.

Mỗi bài test đều được chạy trên một "sân chơi" sạch sẽ, đảm bảo tính nhất quán.

-----

### 3\. ⚙️ Cách Ghi đè (Override) Hanh vi

Như bạn đã nói, đôi khi bạn *muốn* commit. Ví dụ: bạn muốn chạy một bài test và sau đó *kiểm tra thủ công* trong DB xem dữ liệu đã được chèn đúng chưa.

Bạn có thể ghi đè hành vi này bằng cách sử dụng annotation **`@Rollback(false)`**.

```java
@Test
@Transactional
@Rollback(false) // Báo với Spring: "Đừng rollback, hãy COMMIT"
public void testCreateUserAndCheckManually() {
    userRepository.save(new User(1L, "Alice"));
    // Khi test này kết thúc, User "Alice" sẽ thực sự
    // được LƯU (COMMIT) vào database.
}
```

**Mẹo (Pro-Tip):**
Spring cũng cung cấp một annotation "viết tắt" (alias) cho `@Rollback(false)` là **`@Commit`**.

```java
@Test
@Transactional
@Commit // Tương đương với @Rollback(false)
public void testCreateUserAndCheckManually() {
    // ...
}
```

## ❓ Câu hỏi: "Unit of Work" (Đơn vị Công việc) là gì, tại sao nó quan trọng? Và tại sao JDBC AutoCommit vi phạm mô hình (pattern) này?

Câu trả lời của bạn là **hoàn toàn chính xác** trên mọi phương diện. Bạn đã nắm được bản chất của "Unit of Work" (UoW) ở cả cấp độ khái niệm (database transaction) và cấp độ triển khai (JPA/Hibernate).

Hãy cùng diễn giải chi tiết các điểm này bằng một ví dụ cụ thể.

---

### 1. "Unit of Work" (UoW) là gì?

Như bạn đã nói, UoW là một "bộ" các tác vụ phải được coi là một **đơn vị không thể chia nhỏ (atomic)**. Nó tuân theo nguyên tắc **"Tất cả hoặc Không gì cả" (All or Nothing)**.

**💡 Ví dụ so sánh: Giao dịch Chuyển tiền**

Hãy xem xét một "Đơn vị Công việc" (UoW) là "Chuyển 100.000đ từ Tài khoản A sang Tài khoản B".

UoW này bao gồm 2 tác vụ SQL:
1.  `UPDATE users SET balance = balance - 100000 WHERE id = 'A'`
2.  `UPDATE users SET balance = balance + 100000 WHERE id = 'B'`

* **"Tất cả" (All):** Cả hai câu `UPDATE` đều phải thành công. Khi đó, UoW được **`COMMIT`** (lưu vĩnh viễn).
* **"Không gì cả" (Nothing):** Nếu Tác vụ 2 thất bại (ví dụ: tài khoản B không tồn tại), thì Tác vụ 1 cũng **phải bị hủy bỏ** (gọi là **`ROLLBACK`**).

**Tại sao nó quan trọng?** Nó đảm bảo **Tính nhất quán (Consistency)** của dữ liệu. Sẽ là thảm họa nếu Tác vụ 1 thành công (A mất tiền) nhưng Tác vụ 2 thất bại (B không nhận được tiền). UoW ngăn chặn điều này.

#### Điểm nâng cao của bạn (JPA/Hibernate):
Bạn đã đưa ra một điểm rất hay: Trong JPA/Hibernate, UoW (do `PersistenceContext` / `EntityManager` quản lý) còn có một ý nghĩa nữa: nó hoạt động như một **"giỏ hàng cho các thay đổi"**.

1.  Bạn gọi `user.setName("A")`. JPA *không* chạy `UPDATE` ngay. Nó "ghi chú" thay đổi này vào "giỏ hàng" (UoW).
2.  Bạn gọi `user.setAge(30)`. JPA *không* chạy `UPDATE` ngay. Nó "cập nhật ghi chú" trong giỏ.
3.  Khi giao dịch `COMMIT` (khi phương thức `@Transactional` kết thúc), JPA (Hibernate) mới nhìn vào "giỏ hàng" và nói: "À, tôi chỉ cần 1 câu `UPDATE users SET name = 'A', age = 30 WHERE id = ?`".
4.  Như bạn nói, điều này **giảm thiểu số lượng cuộc gọi đến database**.

---

### 2. Tại sao JDBC `AutoCommit = true` vi phạm UoW?

Bạn đã giải thích chính xác: **Nó biến mỗi câu lệnh SQL thành một "Unit of Work" riêng lẻ.**

`AutoCommit = true` (là chế độ mặc định của hầu hết các JDBC driver) có nghĩa là:
> "Ngay sau khi bạn thực thi *một* câu lệnh SQL, hãy **`COMMIT`** (lưu vĩnh viễn) nó ngay lập tức."

**Hãy xem lại ví dụ Chuyển tiền với `AutoCommit = true`:**

1.  **Code Java:** `stmt.executeUpdate("UPDATE A SET balance = balance - 100000");`
2.  **JDBC (với AutoCommit):** Chạy SQL... Thành công. **OK, `COMMIT`!**
  * *Hậu quả:* Tiền của A **đã mất vĩnh viễn**. Giao dịch (nhỏ xíu) này đã kết thúc.
3.  **Code Java:** `stmt.executeUpdate("UPDATE B SET balance = balance + 100000");`
4.  **JDBC (với AutoCommit):** Chạy SQL... Thất bại! (Tài khoản B không tồn tại). Ném ra `SQLException`.
5.  **Code Java:** Bạn `catch (SQLException e)` và muốn `rollback()`?
6.  **Đã quá muộn!** Bạn không thể `rollback` Tác vụ 1. Tác vụ 1 đã ở trong một giao dịch khác và đã được `COMMIT`.

`AutoCommit` đã **phá vỡ** khả năng gộp 2 tác vụ thành *một* "Unit of Work".

**Spring giải quyết vấn đề này như thế nào?**
Khi Spring (`DataSourceTransactionManager`) bắt đầu một giao dịch `@Transactional`, hành động *đầu tiên* nó làm là:
`connection.setAutoCommit(false);`

Bằng cách tắt AutoCommit, Spring có thể toàn quyền kiểm soát khi nào nên gọi `connection.commit()` (khi phương thức kết thúc) hoặc `connection.rollback()` (khi có lỗi), qua đó bảo vệ "Unit of Work" của bạn.

## ❓ Câu hỏi: Bạn cần làm gì trong Spring nếu muốn làm việc với JPA?

Chào bạn, câu trả lời của bạn là **hoàn toàn chính xác**. Bạn đã liệt kê đầy đủ các bước "thủ công" (manual) cần thiết để tích hợp JPA vào một ứng dụng Spring "truyền thống" (không dùng Spring Boot).

Đây là một "công thức" kinh điển. Hãy cùng diễn giải chi tiết **tại sao** chúng ta cần từng bước này.

-----

### 💡 Diễn giải: "Bộ ba" Cốt lõi của JPA

Để JPA hoạt động, bạn cần cung cấp cho Spring 3 bean "cốt lõi". Hãy nghĩ về chúng như:

1.  **`DataSource` (Nguồn dữ liệu):** "Nguồn cung cấp nguyên vật liệu" (kết nối DB).
2.  **`EntityManagerFactory` (Nhà máy):** "Nhà máy" biết cách sản xuất các "phiên làm việc".
3.  **`JpaTransactionManager` (Quản lý):** "Người quản lý" điều phối các "phiên làm việc" đó (quản lý giao dịch).

### 1\. 📦 Khai báo Dependencies (Thư viện)

Phần này bạn đã liệt kê rất chuẩn. Đây là lý do cho từng cái:

* `javax.persistence:javax.persistence.api`: Đây là **JPA API** (các interface như `@Entity`, `@Id`, `EntityManager`). Nó chỉ là "bản thiết kế" (specification), không phải code chạy.
* `org.hibernate:hibernate.core`: Đây là **JPA Implementation** (bên cung cấp). Hibernate (hoặc EclipseLink) là cỗ máy *thực sự* thực thi các bản thiết kế JPA.
* `org.springframework:spring-orm`: Đây là "keo dán" của Spring, chứa các lớp tiện ích (`LocalContainerEntityManagerFactoryBean`, `JpaTransactionManager`) để Spring "nói chuyện" được với JPA.
* `org.hsqldb:hsqldb`: **Driver** của cơ sở dữ liệu.
* `org.springframework.data:spring-data-jpa`: (Tùy chọn nhưng rất nên dùng) Đây là một lớp "ma thuật" của Spring giúp bạn **không cần viết code DAO/Repository** nữa.

-----

### 2\. ⚙️ Cấu hình 3 Bean Cốt lõi (trong `@Configuration`)

Đây là phần quan trọng nhất trong câu trả lời của bạn.

#### A. `DataSource`

* **Bean:** `javax.sql.DataSource`
* **Mục đích:** Cấu hình **Connection Pool** (ví dụ: HikariCP, c3p0). Đây là "nguồn nguyên liệu" cung cấp các `Connection` (kết nối) thô đến database. Spring cần nó để đưa cho Hibernate.

<!-- end list -->

```java
@Bean
public DataSource dataSource() {
    HikariDataSource ds = new HikariDataSource();
    ds.setJdbcUrl("jdbc:h2:mem:testdb");
    ds.setUsername("sa");
    ds.setPassword("");
    return ds;
}
```

#### B. `EntityManagerFactory`

* **Bean:** `org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean`
* **Mục đích:** Đây là bean **phức tạp và quan trọng nhất**. Nó là "nhà máy" (factory) sản xuất ra các `EntityManager` (phiên làm việc).
* Nó cần được cấu hình để "biết":
  1.  **Dùng `DataSource` nào?** (Nguồn nguyên liệu ở đâu?)
  2.  **Dùng Provider nào?** (Ai là cỗ máy? -\> `HibernateJpaVendorAdapter`).
  3.  **Các `@Entity` ở đâu?** (Quét gói (package) nào để tìm bản thiết kế?).
  4.  **Các thuộc tính khác?** (Ví dụ: `hibernate.hbm2ddl.auto`, `hibernate.dialect`).

<!-- end list -->

```java
@Bean
public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
    
    // 1. Cần DataSource
    emfb.setDataSource(dataSource);
    
    // 2. Cần Provider (Hibernate)
    emfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    
    // 3. Quét tìm @Entity ở đâu?
    emfb.setPackagesToScan("com.example.myproject.domain");
    
    // 4. Các thuộc tính Hibernate
    Properties props = new Properties();
    props.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    props.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    emfb.setJpaProperties(props);
    
    return emfb;
}
```

#### C. `PlatformTransactionManager`

* **Bean:** `org.springframework.orm.jpa.JpaTransactionManager`
* **Mục đích:** Đây là "người quản lý" cho `@Transactional`.
* **Tại sao là `JpaTransactionManager`?**
  * Bạn *không thể* dùng `DataSourceTransactionManager` (dùng cho JDBC).
  * `DataSourceTransactionManager` chỉ quản lý `Connection`.
  * `JpaTransactionManager` quản lý **`EntityManager`**. Nó hiểu về "session", "first-level cache", "dirty checking" của Hibernate. Nó đảm bảo `EntityManager` được tạo, `commit`, hoặc `rollback` đúng cách.

<!-- end list -->

```java
@Bean
public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    // Nó cần "nhà máy" (EMF) để quản lý
    return new JpaTransactionManager(emf);
}
```

-----

### 3\. ✍️ Viết Code Ứng dụng

Bạn đã xác định đúng hai bước cuối cùng:

* **Tạo `@Entity`:** Các lớp Java của bạn được ánh xạ (map) tới các bảng trong database.
  ```java
  @Entity
  public class User {
      @Id
      private Long id;
      private String name;
      // ... getters/setters
  }
  ```
* **Tạo DAO/Repository:**
  * **Cách cũ (Manual DAO):** Bạn phải tự `@Autowired` một `EntityManager` và viết code `em.persist()`, `em.createQuery()`.
  * **Cách Spring Data JPA (Được khuyến nghị):** Bạn chỉ cần tạo một `interface`. Spring Boot sẽ *tự động viết code* cho bạn lúc runtime.
  <!-- end list -->
  ```java
  // Bạn CHỈ cần viết interface này
  public interface UserRepository extends JpaRepository<User, Long> {
      // Spring tự động tạo phương thức "findByName" cho bạn
      List<User> findByName(String name);
  }
  ```

-----

### 🚀 Ghi chú quan trọng: Vai trò của Spring Boot

Những gì bạn mô tả là **cực kỳ quan trọng** để hiểu cách Spring hoạt động "bên trong".

Tuy nhiên, trong **Spring Boot**, 90% công việc này là **tự động (auto-configuration)**.

Nếu bạn dùng Spring Boot:

1.  Bạn chỉ cần thêm dependency `spring-boot-starter-data-jpa`.
2.  Bạn chỉ cần cấu hình `spring.datasource.url` (và user/pass) trong `application.properties`.
3.  **HẾT\!**

Spring Boot sẽ **tự động tạo** `DataSource` (Hikari), `EntityManagerFactory` (tự quét tìm `@Entity`), và `JpaTransactionManager` cho bạn. Nó cũng tự động kích hoạt Spring Data JPA (`@EnableJpaRepositories`).

## ❓ Câu hỏi: Bạn có thể tham gia vào một giao dịch (transaction) hiện có trong Spring khi làm việc với JPA không?

**Vâng, hoàn toàn có thể.** Đây là một trong những tính năng mạnh mẽ nhất của Spring, cho phép bạn kết hợp (mix) các công nghệ truy cập dữ liệu khác nhau trong cùng một giao dịch.

Câu trả lời của bạn đã nêu chính xác hai kịch bản chính:

-----

### 1\. 🛒 Kịch bản 1: Giao dịch Cục bộ (Local) - Trộn lẫn JPA và JDBC

Đây là trường hợp phổ biến nhất. Bạn đang dùng `@Transactional` với **`JpaTransactionManager`** và muốn cả `EntityManager` (JPA) và `JdbcTemplate` (JDBC) cùng làm việc.

**Vâng, nó hoạt động hoàn hảo.**

**💡 Cách thức hoạt động:**

`JpaTransactionManager` (mà bạn đã cấu hình) thông minh hơn `DataSourceTransactionManager` (chỉ dành cho JDBC).

1.  **Bắt đầu Giao dịch:** Khi phương thức `@Transactional` của bạn bắt đầu, `JpaTransactionManager` (Sếp quản lý) bắt đầu hành động.
2.  **Lấy `EntityManager`:** Nó lấy một `EntityManager` (Phiên làm việc JPA) từ `EntityManagerFactory`.
3.  **Lấy `Connection`:** Nó "hỏi" `EntityManager`: "Anh đang dùng `Connection` (kết nối JDBC) nào?". `EntityManager` sẽ trả lời (ví dụ: `Connection-A`).
4.  **"Gắn" (Bind) tài nguyên:** "Sếp quản lý" (thông qua `TransactionSynchronizationManager`) sẽ "gắn" **cả hai** tài nguyên này vào `Thread` (luồng) hiện tại:
  * `EntityManager` (cho JPA)
  * `Connection-A` (cho JDBC)
5.  **Chạy Code:**
  * Khi bạn gọi code **JPA** (ví dụ: `entityManager.persist(...)`): JPA tìm thấy `EntityManager` đã được "gắn" và sử dụng nó.
  * Khi bạn gọi code **JDBC** (ví dụ: `jdbcTemplate.update(...)`): `JdbcTemplate` (thông qua `DataSourceUtils`) sẽ tìm thấy `Connection-A` đã được "gắn" và **tái sử dụng** chính xác `Connection` đó.

**Kết quả:** Cả JPA và `JdbcTemplate` đều đang làm việc trên **cùng một `Connection` vật lý**, do đó chúng tham gia vào **cùng một giao dịch**. Nếu giao dịch `rollback`, cả hai thay đổi (từ JPA và JDBC) đều sẽ bị hoàn tác.

```java
@Autowired
private EntityManager entityManager; // JPA

@Autowired
private JdbcTemplate jdbcTemplate; // JDBC

@Transactional // Được quản lý bởi JpaTransactionManager
public void createFullUser(User user, String legacyCode) {
    // 1. Tác vụ JPA
    // (Tham gia vào giao dịch, dùng EntityManager đã "gắn")
    entityManager.persist(user); 

    // 2. Tác vụ JDBC
    // (Tái sử dụng Connection của EntityManager, tham gia vào cùng giao dịch)
    jdbcTemplate.update("INSERT INTO legacy_table (code) VALUES (?)", legacyCode);

    // 3. Nếu có lỗi ở đây, cả persist() và update() đều bị ROLLBACK
}
```

-----

### 2\. 🌍 Kịch bản 2: Giao dịch Toàn cục (Global) - JTA

Như bạn đã nói, kịch bản này dùng khi bạn cần tham gia vào một giao dịch "lớn" hơn, thường là:

* Được quản lý bởi một **Java EE Application Server** (như JBoss, WebSphere).
* Giao dịch này có thể bao gồm **nhiều tài nguyên** (ví dụ: 2 database khác nhau, hoặc 1 database + 1 message queue).

**Cách làm:**

1.  Bạn cấu hình Spring để sử dụng **`JtaTransactionManager`**.
2.  `JtaTransactionManager` **không** tự mình quản lý giao dịch. Nó *ủy quyền (delegates)* việc đó cho "Người quản lý Giao dịch" (JTA) của Application Server.
3.  Khi code JPA của bạn chạy, nó sẽ tự động "nhìn" (lookup) và tham gia vào giao dịch JTA toàn cục đang hoạt động do Server quản lý.

**Kết luận:** Bằng cách sử dụng đúng `PlatformTransactionManager`, Spring đảm bảo rằng các công cụ truy cập dữ liệu (như JPA) có thể tham gia một cách liền mạch vào các giao dịch hiện có.

Bạn đã sẵn sàng cho câu hỏi tiếp theo chưa?

## ❓ Câu hỏi:

# Bạn có thể sử dụng (các) PlatformTransactionManager nào với JPA?

Câu trả lời của bạn là **hoàn toàn chính xác**.

Khi làm việc với JPA, bạn có hai lựa chọn chính cho `PlatformTransactionManager`, tùy thuộc vào độ phức tạp của giao dịch (transaction) mà bạn cần:

-----

### 1\. 🥇 `JpaTransactionManager` (Quản lý Cục bộ - Local)

Đây là lựa chọn **khuyến nghị và phổ biến nhất** (95% các trường hợp).

* **Khi nào dùng:** Như bạn nói, khi bạn đang làm việc với **một `DataSource` (database) duy nhất**.
* **Tại sao dùng nó?** Giao dịch JPA không chỉ là về `Connection` (như JDBC). Nó còn về `EntityManager` (phiên làm việc), first-level cache (bộ đệm cấp 1), và "dirty checking" (kiểm tra thay đổi).
  * `DataSourceTransactionManager` (dùng cho JDBC) **không** hiểu gì về `EntityManager`.
  * `JpaTransactionManager` được thiết kế đặc biệt để quản lý `EntityManager`. Nó "bọc" một giao dịch JDBC (từ `DataSource`) và một phiên làm việc JPA (`EntityManager`) vào cùng một "Đơn vị Công việc" (Unit of Work).
* **Điểm mạnh (như bạn đề cập):** Nó đủ thông minh để cho phép `JdbcTemplate` (JDBC) tham gia vào giao dịch JPA (vì nó cũng quản lý `Connection` bên dưới).

<!-- end list -->

```java
// Cấu hình thủ công (Spring Boot tự làm việc này)
@Bean
public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    // Nó "lấy" giao dịch từ nhà máy EntityManager
    return new JpaTransactionManager(emf);
}
```

-----

### 2\. 🌍 `JtaTransactionManager` (Quản lý Toàn cục - Global)

Đây là lựa chọn **nâng cao** dành cho các hệ thống phức tạp.

* **Khi nào dùng:** Như bạn đã nêu rất chính xác, khi giao dịch của bạn phải *phối hợp* (span) **nhiều tài nguyên giao dịch**. Ví dụ:
  1.  Ghi vào **hai Database khác nhau** (ví dụ: một DB Oracle và một DB MySQL) trong cùng một giao dịch.
  2.  Ghi vào **một Database** VÀ gửi tin nhắn đến một **Message Queue** (như JMS/Kafka).
* **Tại sao dùng nó?** `JpaTransactionManager` là "cục bộ" (local) - nó chỉ biết cách quản lý *một* `EntityManagerFactory` duy nhất. Nó không thể điều phối hai tài nguyên khác nhau.
* **Cách hoạt động:** `JtaTransactionManager` (Java Transaction API) **không** tự mình quản lý giao dịch. Nó *ủy quyền (delegates)* công việc đó cho một "Người điều phối Giao dịch" (Transaction Coordinator) mạnh mẽ hơn, thường được cung cấp bởi một Application Server (như JBoss, WebSphere) hoặc một thư viện JTA độc lập (như Atomikos). Nó sử dụng một quy trình gọi là "Two-Phase Commit" (2PC) để đảm bảo "Tất cả hoặc Không gì cả" trên tất cả các tài nguyên.

-----

### Tóm tắt

| Trình quản lý | Khi nào dùng | Loại giao dịch |
| :--- | :--- | :--- |
| **`JpaTransactionManager`** | **Một** `DataSource` (95% trường hợp) | **Local** (Cục bộ) |
| **`JtaTransactionManager`** | **Nhiều** tài nguyên (ví dụ: 2 DB, hoặc 1 DB + 1 Queue) | **Global** (Toàn cục) |

## ❓ Câu hỏi: Bạn cần cấu hình gì để dùng JPA với Spring? Spring Boot làm điều này dễ dàng hơn như thế nào?

1.  **Cách "Thủ công" (Manual) của Spring Framework.**
2.  **Cách "Tự động" (Automatic) của Spring Boot.**

Hãy cùng diễn giải chi tiết hơn về *tại sao* các bước này lại cần thiết và Spring Boot đã "giải cứu" chúng ta khỏi những công việc đó như thế nào.

-----

### 1\. 🛠️ Cách làm "Thủ công" (Spring Framework truyền thống)

Như bạn đã liệt kê, để "dạy" Spring cách nói chuyện với JPA, bạn phải tự mình kết nối rất nhiều "đường ống" lại với nhau. Cụ thể là "Bộ Ba Cấu hình" (The Big Three).

**Hãy tưởng tượng bạn đang xây một nhà máy:**

#### A. Khai báo Dependencies (Nhập nguyên vật liệu)

Bạn đã liệt kê chính xác. Bạn cần:

* `javax.persistence.api`: "Bản thiết kế" (Interface `@Entity`, `@Id` của JPA).
* `org.hibernate.core`: "Cỗ máy" (Implementation của Hibernate).
* `org.springframework.spring-orm`: "Keo dán" (Code của Spring để tích hợp).
* `org.hsqldb`: "Driver" (Kết nối đến database).
* `org.springframework.data:spring-data-jpa`: (Tùy chọn) "Robot tự động" (Giúp bạn không cần viết DAO).

#### B. Cấu hình "Bộ Ba" Beans (`@Configuration`)

Đây là 3 bean cốt lõi mà bạn *phải* tự tay định nghĩa:

**1. `DataSource` (Kho nguyên liệu)**

* **Mục đích:** Cung cấp các kết nối (connections) thô đến database. Thường là một **Connection Pool** (như HikariCP, c3p0).

<!-- end list -->

```java
@Bean
public DataSource dataSource() {
    // Cấu hình thủ công HikariCP (hoặc BasicDataSource, v.v.)
    HikariDataSource ds = new HikariDataSource();
    ds.setJdbcUrl("jdbc:h2:mem:testdb");
    ds.setUsername("sa");
    ds.setPassword("");
    return ds;
}
```

**2. `LocalContainerEntityManagerFactoryBean` (Cỗ máy chính)**

* **Mục đích:** Đây là bean **phức tạp nhất** như bạn đã chỉ ra. Nó là "Nhà máy" (Factory) sản xuất ra các `EntityManager` (phiên làm việc).
* **Nó cần biết:**
  * Dùng `DataSource` nào?
  * Dùng "cỗ máy" nào? (ví dụ: `HibernateJpaVendorAdapter`).
  * Các `@Entity` của bạn nằm ở đâu (để quét - `setPackagesToScan`).
  * Các thuộc tính của Hibernate (ví dụ: `hibernate.hbm2ddl.auto`).

<!-- end list -->

```java
@Bean
public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
    emfb.setDataSource(dataSource);
    emfb.setPackagesToScan("com.example.domain"); // Quét tìm @Entity
    emfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    
    Properties props = new Properties();
    props.setProperty("hibernate.hbm2ddl.auto", "create");
    emfb.setJpaProperties(props);
    
    return emfb;
}
```

**3. `JpaTransactionManager` (Quản đốc nhà máy)**

* **Mục đích:** Bean này chịu trách nhiệm cho `@Transactional`. Nó là "Quản đốc" biết cách *bắt đầu*, `commit`, hoặc `rollback` các phiên làm việc (`EntityManager`) do "Nhà máy" (ở trên) tạo ra.

<!-- end list -->

```java
@Bean
public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    // "Quản đốc" này cần biết "Nhà máy" của mình
    return new JpaTransactionManager(emf);
}
```

Cuối cùng, như bạn nói, bạn phải viết các lớp `@Entity` và các lớp DAO (hoặc `JpaRepository` interfaces).

-----

### 2\. 🚀 Spring Boot làm điều này dễ dàng hơn như thế nào?

Spring Boot áp dụng triết lý **"Quy ước hơn Cấu hình" (Convention over Configuration)**. Nó thấy bạn muốn dùng JPA và nó sẽ *tự động làm tất cả* các công việc thủ công ở trên.

Các điểm của bạn là hoàn toàn chính xác:

**A. `spring-boot-starter-data-jpa` (Gói "Tất cả trong một")**

* Như bạn nói, bạn không cần khai báo 5 dependencies riêng lẻ.
* Starter này là một "siêu dependency" tự động kéo về:
  1.  `spring-boot-starter-aop` (cho `@Transactional`).
  2.  `spring-boot-starter-jdbc` (cho `DataSource`).
  3.  `Hibernate` (cỗ máy).
  4.  `Spring Data JPA` (robot tự động).
  5.  `HikariCP` (Connection Pool mặc định).

**B. Auto-Configuration (Phép thuật chính)**

* Đây là "robot" của Spring Boot. Khi nó thấy `spring-boot-starter-data-jpa` trên classpath, nó sẽ kích hoạt `JpaRepositoriesAutoConfiguration`.
* Robot này sẽ **tự động tạo ra 3 bean cốt lõi** (ở Mục 1) cho bạn:
  1.  **Tự động tạo `DataSource`:** Nó tìm `HikariCP` (như bạn nói) và tạo một `DataSource` bean.
  2.  **Tự động cấu hình `DataSource`:** Nó đọc file `application.properties` của bạn (như `spring.datasource.url`) và "tiêm" (inject) chúng vào `DataSource`.
  3.  **Tự động tạo `EntityManagerFactory`:** Nó tự động cấu hình `LocalContainerEntityManagerFactoryBean`, tự động tìm `DataSource` đã tạo, và **tự động quét (scan)** các gói con để tìm `@Entity` của bạn.
  4.  **Tự động tạo `JpaTransactionManager`:** Nó tự động tạo `JpaTransactionManager` và "cắm" `EntityManagerFactory` ở trên vào.

**C. Tự động kích hoạt Repositories**

* Như bạn nói, Spring Boot cũng **tự động kích hoạt Spring Data JPA** (`@EnableJpaRepositories`).
* Nó sẽ tự động quét tìm các `interface` của bạn (ví dụ: `UserRepository`) và "tự động viết code" (tạo bean) cho các phương thức DAO cơ bản.

-----

### 📊 Tóm tắt (So sánh)

| Nhiệm vụ | Spring Framework (Thủ công) | Spring Boot (Tự động) |
| :--- | :--- | :--- |
| **Dependencies** | Thêm 5+ dependencies thủ công. | Thêm 1 "starter": `spring-boot-starter-data-jpa`. |
| **`DataSource`** | Tự viết `Code` `@Bean` để tạo `HikariDataSource`. | Chỉ cần viết `Properties`: `spring.datasource.url`. |
| **`EntityManagerFactory`** | Tự viết `Code` `@Bean` phức tạp (set
DataSource, packages, adapter...). | **Tự động 100%**. (Nó tự tìm `DataSource` và tự quét `@Entity`). |
| **`TransactionManager`** | Tự viết `Code` `@Bean` để tạo `JpaTransactionManager`. | **Tự động 100%**. |
| **Spring Data JPA** | Phải tự thêm `@EnableJpaRepositories` vào `@Configuration`. | **Tự động 100%**. |

## ❓ Câu hỏi: Repository interface là gì?

Câu trả lời của bạn là **rất chính xác và đầy đủ**. `Repository` interface là một trong những tính năng mạnh mẽ và tiện lợi nhất của **Spring Data JPA**.

Hãy cùng diễn giải chi tiết hơn về "phép thuật" đằng sau nó.

-----

### 1\. 💡 Repository Interface là gì?

Như bạn đã định nghĩa, `Repository` interface là một **"bản thiết kế"** (hoặc một **"bản yêu cầu"**) mà bạn, lập trình viên, cung cấp cho Spring.

**💡 Ví dụ so sánh: "Robot Chế tạo DAO"**

Hãy tưởng tượng Spring Data JPA là một **"Robot"** (Robot) thông minh chuyên chế tạo các đối tượng DAO (Data Access Object).

1.  **Bạn (Developer):** Bạn không cần phải tự mình viết code `DAOImpl` (với `EntityManager`, `try-catch`, `createQuery`, v.v.).
2.  **Thay vào đó:** Bạn chỉ cần viết một `interface` (ví dụ: `EmployeeRepository`) và đưa cho Robot.
3.  **Trên `interface`, bạn "đặt hàng":**
  * `extends CrudRepository<Employee, Long>`: "Này Robot, tôi muốn một DAO cho `Employee` (có khóa chính là `Long`). Làm ơn cung cấp cho tôi *tất cả* các phương thức CRUD cơ bản (Create, Read, Update, Delete) như `save()`, `findById()`, `findAll()`, `delete()`, `count()`."
  * `Employee findByEmail(String email);`: "À, tôi cũng cần một phương thức *đặc biệt* để tìm `Employee` theo `email`."
4.  **Robot (Spring Data JPA):** "Đã nhận bản yêu cầu\!"
5.  **Lúc khởi động (Runtime):** Robot sẽ **tự động viết code implement** cho `interface` đó, tạo ra một `bean` (đối tượng) thật, và đưa nó vào `ApplicationContext` để bạn `@Autowired` và sử dụng.

Đây chính là "phép thuật": **Bạn chỉ định nghĩa "CÁI GÌ" (WHAT) bạn muốn, và Spring Data sẽ lo phần "NHƯ THẾ NÀO" (HOW).**

-----

### 2\. 🪄 "Phép thuật" của Query Methods (Phương thức Truy vấn)

Đây là phần "đặt hàng đặc biệt" như `findByEmail` trong ví dụ của bạn.

Spring Data có một cơ chế **tự động phân tích tên phương thức (parses the method name)** để tạo ra câu lệnh truy vấn (JPQL).

* Nếu bạn viết `Employee findByEmail(String email);`:

  * Spring sẽ tự động tạo ra câu lệnh: `SELECT e FROM Employee e WHERE e.email = ?1`

* Nếu bạn viết `List<Employee> findByLastName(String lastName);`:

  * Spring sẽ tự động tạo ra: `SELECT e FROM Employee e WHERE e.lastName = ?1`

* Nếu bạn viết `List<Employee> findBySalaryBetween(float min, float max);`:

  * Spring sẽ tự động tạo ra: `SELECT e FROM Employee e WHERE e.salary BETWEEN ?1 AND ?2`

Bạn không cần viết một dòng JPQL hay SQL nào.

-----

### 3\. 🏛️ Các "Cấp độ Đặt hàng" (Interface Hierarchy)

Như bạn đã liệt kê, bạn có thể `extends` từ các interface khác nhau, tùy thuộc vào "gói" tính năng bạn muốn:

1.  **`Repository<T, ID>`:** Gói "Cơ bản". Nó là một *marker interface* (giao diện đánh dấu). Nó không cung cấp phương thức nào, nhưng vẫn cho phép bạn viết các phương thức `findBy...` tùy chỉnh.
2.  **`CrudRepository<T, ID>`:** Gói "Tiêu chuẩn" (Phổ biến nhất). Thêm các phương thức `save()`, `findById()`, `findAll()`, `delete()`, `count()`.
3.  **`PagingAndSortingRepository<T, ID>`:** Gói "Nâng cao". Thêm các phương thức để **phân trang (Paging)** (ví dụ: `findAll(Pageable pageable)`) và **sắp xếp (Sorting)** (ví dụ: `findAll(Sort sort)`).
4.  **`JpaRepository<T, ID>`:** (Dành riêng cho JPA) Gói "Đầy đủ". Nó kế thừa `PagingAndSortingRepository` VÀ thêm các tính năng đặc thù của JPA như `flush()` (đẩy thay đổi vào DB) và `saveAndFlush()`, `findAll()` (trả về `List<T>` thay vì `Iterable<T>`, tiện lợi hơn).

-----

### 4\. ⚙️ Kích hoạt "Robot" (Configuration)

Phần cấu hình của bạn là hoàn toàn chính xác.

Robot (Spring Data JPA) sẽ không tự chạy. Bạn phải "bật" nó lên và "chỉ" cho nó biết nơi tìm các "bản yêu cầu" (interfaces) của bạn.

```java
@Configuration
@EnableJpaRepositories(basePackages = "com.project.dao") // <--- "Robot, hãy quét gói này!"
public class JpaConfiguration {
    // ... (Cấu hình DataSource, EntityManagerFactory, v.v.)
}
```

*(**Lưu ý:** Nếu bạn dùng **Spring Boot**, nó sẽ **tự động** làm việc này (`@EnableJpaRepositories`) nếu nó thấy `spring-boot-starter-data-jpa`. Nó sẽ tự động quét (scan) các gói con (sub-packages) bên dưới lớp Application chính của bạn.)*

## ❓ Câu hỏi: Làm thế nào để bạn định nghĩa một Repository interface? Tại sao nó là một interface mà không phải là một class?

Chào bạn, câu trả lời của bạn đã nêu **rất chính xác** các bước cần thiết để định nghĩa (define) và kích hoạt (enable) một Spring Data Repository.

Hãy cùng diễn giải chi tiết hơn, đặc biệt là về câu hỏi "Tại sao lại là interface?"

-----

### 1\. 📋 Cách định nghĩa một Repository Interface

Các bước bạn liệt kê là một "công thức" hoàn hảo. Hãy xem code của bạn minh họa cho các bước này như thế nào:

**Bước 1+2+3: Tạo `interface` kế thừa và `@Entity` (Bản thiết kế & Dữ liệu)**

* Bạn định nghĩa một `interface` (ví dụ: `EmployeeDao`) và cho nó kế thừa (extends) một trong các interface cơ sở của Spring Data (ví dụ: `CrudRepository`).
* Bạn chỉ định 2 tham số generics (kiểu dữ liệu chung):
  1.  Lớp Domain (`Employee`): Đây phải là một class được đánh dấu `@Entity`.
  2.  Kiểu của Khóa chính (`Integer`): Đây phải là kiểu của trường (field) được đánh dấu `@Id` trong `Employee`.

<!-- end list -->

```java
// Bước 3: @Entity và @Id (Dữ liệu)
@Entity
public class Employee{
    @Id
    private int id; // <--- Kiểu "Integer" (hay int) này
    private String firstName;
    private String lastName;
}

// Bước 1: Interface (Bản thiết kế)
// Kế thừa CrudRepository<Kiểu Entity, Kiểu Id>
public interface EmployeeDao extends CrudRepository<Employee, Integer> {
    // Bạn cũng có thể thêm các phương thức truy vấn tùy chỉnh ở đây
    // List<Employee> findByLastName(String lastName);
}
```

**Bước 4: Kích hoạt Scanning (Bật "Robot")**

* Như bạn đã làm, bạn phải "bảo" Spring Data JPA hãy đi tìm các `interface` này.
* Bạn làm điều này bằng cách thêm `@EnableJpaRepositories` vào lớp `@Configuration` của mình và chỉ cho nó gói (package) cần quét.

<!-- end list -->

```java
@Configuration
// Bước 4: "Bảo Spring hãy quét gói 'com.project.dao' để tìm các interface"
@EnableJpaRepositories(basePackages = {"com.project.dao"})
public class JpaConfiguration{
    //... (Cần có bean DataSource, EntityManagerFactory, v.v.)
}
```

Khi ứng dụng khởi động, Spring sẽ thấy cấu hình này, quét gói `com.project.dao`, tìm thấy `EmployeeDao`, và **tự động tạo một bean triển khai (implementation) cho nó**.

-----

### 2\. 💡 Tại sao là `interface` mà không phải `class`?

Đây là một câu hỏi rất hay về triết lý thiết kế của Spring Data. Câu trả lời của bạn đã đề cập đến Proxy, điều này là một phần của câu chuyện.

Lý do chính là: **Bạn đang định nghĩa một "Bản hợp đồng" (Contract), không phải một "Cách thực hiện" (Implementation).**

Hãy nghĩ về nó theo cách này:

**1. Spring Data "Viết Code" Thay Bạn**

* Nếu `EmployeeDao` là một `class`, *bạn* sẽ phải tự mình viết code cho tất cả các phương thức như `save()`, `findById()`, `findAll()`... Bạn sẽ phải tự `@Autowired` `EntityManager` và tự viết các truy vấn.
* Bằng cách sử dụng `interface`, bạn đang nói với Spring: "Đây là *những gì tôi muốn* (I want...). Tôi muốn một bean có thể `save`, `findById`, v.v. cho `Employee`. Còn việc *làm thế nào (how)*, **tôi không quan tâm, anh tự lo đi**."
* Lúc khởi động, Spring Data JPA sẽ **tự động sinh ra (generates) một lớp** (như bạn nói, dựa trên `SimpleJpaRepository`) *implement* cái `interface` của bạn. Nó viết tất cả code "boilerplate" (lặp đi lặp lại) cho bạn.

**2. Sự linh hoạt của Proxy (Như bạn đã đề cập)**

* Lý do bạn đưa ra về **Proxy** là cực kỳ quan trọng.

* Bởi vì Spring *tự sinh ra* lớp implementation, nó có toàn quyền kiểm soát lớp đó. Điều này cho phép Spring **dễ dàng "bọc" (wrap) lớp implementation đó bằng một Proxy**.

* **Proxy này dùng để làm gì?** Nó dùng để thêm các **cross-cutting concerns** (các khía cạnh xuyên suốt) một cách "minh bạch" (transparently).

* **Ví dụ quan trọng nhất:** `@Transactional`. Khi bạn gọi `employeeDao.save(emp)`, bạn không gọi trực tiếp "lớp được sinh ra" đó. Bạn đang gọi **Proxy**. Cái Proxy này sẽ:

  1.  **Bắt đầu một Giao dịch (Transaction)** (nếu cần).
  2.  Gọi `lớp_thật.save(emp)`.
  3.  **Commit** (hoặc **Rollback**) giao dịch.

* Việc này dễ dàng hơn nhiều khi làm việc với `interface` (dùng JDK Dynamic Proxy) so với việc phải "bọc" một `class` do người dùng tự định nghĩa (phải dùng CGLIB, có nhiều hạn chế hơn).

**Tóm lại:**
Nó là một `interface` vì:

1.  Bạn chỉ cần định nghĩa **"Bản yêu cầu" (WHAT)**, Spring Data sẽ **tự động viết code (HOW)**.
2.  Nó cho phép Spring toàn quyền kiểm soát việc triển khai, giúp Spring dễ dàng **thêm các "phép thuật" (như Transactions) thông qua Proxy**.

## ❓ Câu hỏi: Quy ước đặt tên cho các phương thức "finder" trong Repository interface là gì?

Cách dễ hiểu nhất là hãy nghĩ Spring Data JPA như một **"Robot dịch thuật" (Query Translator)**. Nó "đọc" tên phương thức (method name) của bạn từ trái sang phải, giống như đọc một câu tiếng Anh, và "dịch" nó thành một câu lệnh truy vấn (JPQL hoặc SQL).

Quy ước này (như bạn đã viết) có thể được chia thành các phần:

---

### 1. 🎯 Tiền tố (Prefix): "Động từ" của bạn

Phần đầu tiên của phương thức cho Spring biết bạn muốn *làm gì*.
* `find...By...` (hoặc `get...By...`, `query...By...`): Phổ biến nhất, để **tìm và trả về** một hoặc nhiều đối tượng (`List<T>` hoặc `T`).
* `count...By...`: Chỉ **đếm** và trả về `long` (số lượng).
* `exists...By...`: Chỉ **kiểm tra** và trả về `boolean` (có tồn tại hay không).

---

### 2. 🔢 Giới hạn (Limit): (Tùy chọn)

Ngay sau "động từ", bạn có thể thêm một từ khóa để giới hạn số lượng kết quả trả về, như bạn đã liệt kê:
* **`findFirst...By...`** hoặc **`findTop...By...`**: Lấy 1 kết quả duy nhất.
  * Ví dụ: `findFirstByOrderByLastNameAsc()` (Tìm người đầu tiên theo thứ tự ABC của họ).
* **`findTop3...By...`** hoặc **`findFirst10...By...`**: Lấy *N* kết quả đầu tiên.
  * Ví dụ: `findTop3BySalaryGreaterThan(double amount)` (Tìm 3 người có lương cao nhất).

---

### 3. 🏷️ Thuộc tính (Property): "Chủ ngữ" của câu

Đây là phần **quan trọng nhất**. Nó nằm sau từ khóa `By`.
* Bạn phải đặt **tên trường (field)** trong `@Entity` của mình vào đây. Tên phải khớp (phân biệt hoa thường).
* Ví dụ: `findByLastName(...)` -> sẽ lọc trên trường `lastName` của Entity.
* **Nested Property (Thuộc tính lồng nhau):** Bạn có thể "đi sâu" vào các đối tượng liên quan.
  * Ví dụ: `findByAddressZipCode(...)` -> sẽ tự động "dịch" thành `... WHERE employee.address.zipCode = ?`

* **Nối nhiều thuộc tính (Như bạn nói):**
  * `...ByLastNameAndFirstName(...)` (Dùng `And`)
  * `...ByLastNameOrFirstName(...)` (Dùng `Or`)

---

### 4. ⚖️ Phép so sánh (Comparison): "Vị ngữ" của câu

Ngay sau tên thuộc tính, bạn có thể thêm một "vị ngữ" (predicate) để so sánh. Nếu bạn **không thêm gì** (ví dụ: `findByEmail(String email)`), Spring sẽ tự động hiểu là **`Equals`** (bằng).

Danh sách của bạn là hoàn hảo, đây là các ví dụ phổ biến nhất:

* **`Between`**: `findByStartDateBetween(Date start, Date end)`
* **`LessThan` / `GreaterThan`**: `findByAgeGreaterThanEqual(int age)`
* **`IsNull` / `IsNotNull`**: `findByManagerIsNull()`
* **`Like` / `Containing` / `StartingWith`**:
  * `findByFirstnameLike(String pattern)` (Bạn phải tự thêm `%`)
  * `findByFirstnameContaining(String infix)` (Tự động thêm `%` ở 2 đầu)
  * `findByFirstnameStartingWith(String prefix)` (Tự động thêm `%` ở cuối)
* **`In`**: `findByAgeIn(Collection<Integer> ages)` (Lọc theo một danh sách các giá trị)
* **`True` / `False`**: `findByActiveTrue()` (Rất tiện lợi cho cờ boolean)
* **`IgnoreCase`**: `findByUsernameIgnoreCase(String username)`

---

### 5. 🚦 Sắp xếp (Ordering): (Tùy chọn, ở cuối cùng)

Cuối cùng, bạn có thể thêm `OrderBy` để sắp xếp kết quả.
* `...OrderByLastNameAsc()` (Sắp xếp theo Họ (lastName) tăng dần - A->Z)
* `...OrderBySalaryDesc()` (Sắp xếp theo Lương (salary) giảm dần)
* Bạn có thể kết hợp nhiều trường: `...OrderByLastNameAscFirstNameAsc()`

---

### 💎 Ví dụ "Tất cả trong một"

Hãy "dịch" một tên phương thức phức tạp dựa trên các quy tắc của bạn:

**Tên phương thức:**
`findTop5ByAgeGreaterThanAndDepartmentNameContainingIgnoreCaseOrderBySalaryDesc`

**Spring "dịch" (tạo ra JPQL) thành:**
* `findTop5...`: "CHỌN 5..."
* `...ByAgeGreaterThan...`: "...CÓ `age` (tuổi) LỚN HƠN `?1`..."
* `...AndDepartmentNameContainingIgnoreCase...`: "...VÀ `department.name` CHỨA `?2` (không phân biệt hoa thường)..."
* `...OrderBySalaryDesc`: "...SẮP XẾP THEO `salary` (lương) GIẢM DẦN."

## ❓ Câu hỏi: Spring Data repositories được Spring triển khai (implement) lúc runtime như thế nào?

Câu trả lời của bạn là **rất chính xác và rất chi tiết về mặt kỹ thuật**. Bạn đã nêu tên đúng tất cả các "bánh răng" (gears) bên trong cỗ máy Spring: từ `SimpleJpaRepository` (lớp cơ sở) cho đến `JpaRepositoryFactoryBean` (nhà máy).

Hãy cùng diễn giải quá trình này một cách dễ hiểu hơn bằng một **ví dụ so sánh (analogy)**.

---

### 1. 💡 Phép ví von: "Nhà máy Chế tạo Robot DAO"

Hãy nghĩ về Spring Data JPA như một nhà máy tự động hóa cao cấp chuyên sản xuất robot (`DAO beans`).

* **`UserRepository` (Interface của bạn):** Đây là **"Bản thiết kế" (Blueprint)** mà bạn vẽ ra. Bạn nói: "Tôi cần một robot (`DAO`) có thể `save`, `delete`, `findAll`, và có một nút bấm đặc biệt `findByEmail(...)`."
* **`@EnableJpaRepositories` (Công tắc Nhà máy):** Đây là nút "ON" bạn nhấn trong lớp `@Configuration` của mình.
* **`JpaRepositoriesRegistrar` / `JpaRepositoryConfigExtension` (Quản đốc):** Khi bạn nhấn nút "ON", các "Quản đốc" này bắt đầu hoạt động. Họ lấy "bản đồ" (`basePackages`) và đi *quét (scan)* toàn bộ khu vực để tìm các "Bản thiết kế" (interfaces) của bạn.
* **`JpaRepositoryFactoryBean` (Cỗ máy Sản xuất):** Khi Quản đốc tìm thấy "Bản thiết kế" (`UserRepository`), họ không tự mình xây robot. Họ mang bản thiết kế này đến một **Cỗ máy Sản xuất** chuyên biệt.
* **`JpaRepositoryFactory` (Dây chuyền Sản xuất):** Đây là dây chuyền bên trong cỗ máy. Dây chuyền này có nhiệm vụ:
  1.  Nhìn vào "Bản thiết kế" của bạn (nó `extends CrudRepository`).
  2.  Lấy một **"Mô hình Tiêu chuẩn" (Standard Model)** có sẵn.
  3.  Tạo ra "Robot" (bean) cuối cùng.

---

### 2. ⚙️ `SimpleJpaRepository` là gì? (Câu trả lời của bạn)

Như bạn đã nói, **`SimpleJpaRepository`** chính là **"Mô hình Tiêu chuẩn" (Standard Model)** đó.

Nó là một **lớp (class) có thật** do Spring cung cấp, chứa *toàn bộ code đã được viết sẵn* cho các phương thức CRUD cơ bản như:
* `save(T entity)` (Bên trong nó là code gọi `entityManager.persist()`)
* `findById(ID id)` (Bên trong nó là code gọi `entityManager.find()`)
* `findAll()`
* `delete(T entity)`
* v.v...

---

### 3. 🎁 Sản phẩm cuối cùng (Bean được tiêm)

Đây là phần "phép thuật" nhất. Bean mà bạn `@Autowired` vào `Service` không phải *chính là* `SimpleJpaRepository`.

Nó là một **Proxy (Đối tượng Ủy quyền)** được tạo ra lúc runtime.

Dây chuyền sản xuất (`JpaRepositoryFactory`) làm 2 việc:

1.  **Tạo phần "Cốt lõi":** Nó tạo một instance của `SimpleJpaRepository` (hoặc lớp cơ sở tùy chỉnh của bạn) để xử lý các phương thức CRUD chuẩn (như `save()`, `delete()`).
2.  **Tạo phần "Vỏ bọc" (Proxy):** Nó tạo ra một **Proxy** (sử dụng JDK Dynamic Proxy, như bạn đã đề cập trong một câu trả lời trước) implement `interface UserRepository` của bạn.
  * Khi bạn gọi `userRepository.save(user)`: Proxy sẽ nói, "OK, đây là việc tiêu chuẩn" và **ủy quyền (delegates)** cuộc gọi đó cho "Cốt lõi" (`SimpleJpaRepository`).
  * Khi bạn gọi `userRepository.findByEmail("...")`: Proxy sẽ nói, "Aha, đây là phương thức tùy chỉnh!" và **tự mình "dịch"** (parses) tên phương thức này thành một câu truy vấn JPQL (`select u from User u where u.email = ?1`), sau đó thực thi nó.

---

### 4. 🛠️ Cách Tùy chỉnh (Như bạn đã nêu)

Bạn hoàn toàn có thể thay đổi "Mô hình Tiêu chuẩn" này:

1. **Thay đổi "Mô hình" cho TẤT CẢ Robot (Global):**
  * Dùng `repositoryBaseClass` trong `@EnableJpaRepositories`.
  * Bạn nói: "Này Nhà máy, đừng dùng `SimpleJpaRepository` nữa. Hãy dùng `MyCustomBaseRepository` (mà tôi đã viết) làm "Mô hình Tiêu chuẩn" mới cho *tất cả* robot."

2. **Thêm "Tay máy" đặc biệt cho MỘT Robot (Single Repository):**
  * Đây là cách bạn tạo một `interface` tùy chỉnh (ví dụ: `UserRepositoryCustom`) và một `class` implementation (ví dụ: `UserRepositoryCustomImpl`).
  * Bạn làm cho `UserRepository` kế thừa `UserRepositoryCustom`.
  * Spring sẽ "nhận diện" điều này và "ghép" (merge) các phương thức tùy chỉnh của bạn vào "Robot" cuối cùng.

## ❓ Câu hỏi: Annotation @Query dùng để làm gì?

`@Query` là một annotation "giải cứu" (escape hatch) mạnh mẽ. Nó cho phép bạn **hoàn toàn kiểm soát** câu lệnh truy vấn, thay vì phụ thuộc vào "robot dịch thuật" (query derivation) từ tên phương thức của Spring Data.

-----

### 1\. 💡 Vấn đề: Khi nào "Tên phương thức" là không đủ?

Như chúng ta đã thấy, Spring Data rất giỏi trong việc "dịch" các tên phương thức (method names) như `findByEmailAndLastName(...)`. Tuy nhiên, cách này có 2 hạn chế lớn:

1.  **Quá phức tạp:** Nếu bạn có một truy vấn phức tạp, tên phương thức sẽ trở nên **dài một cách lố bịch và không thể đọc nổi**.
  * *Ví dụ:* `findTop5ByAgeGreaterThanAndDepartmentNameContainingIgnoreCaseOrderBySalaryDesc`
2.  **Không đủ linh hoạt:** Tên phương thức không thể diễn tả mọi thứ. Ví dụ:
  * Bạn muốn `JOIN` các bảng với điều kiện `ON` tùy chỉnh?
  * Bạn muốn trả về một **DTO (Data Transfer Object)** tùy chỉnh, thay vì một Entity đầy đủ?
  * Bạn muốn sử dụng một hàm **SQL gốc (native SQL)** của database (ví dụ: `JSON_VALUE` của SQL Server)?

-----

### 2\. ⚙️ Giải pháp: Dùng `@Query`

Như bạn đã nói, `@Query` cho phép bạn **ghi đè (override)** cơ chế tự động đó. Bạn đang nói với Spring Data:

> "Đừng cố dịch tên phương thức của tôi. Tôi sẽ cung cấp cho anh **câu lệnh truy vấn chính xác** để chạy."

Ví dụ của bạn là một minh chứng hoàn hảo:

```java
public interface EmployeeDao extends CrudRepository<Employee, Integer>{

    // Spring sẽ bỏ qua tên "findFirstNameAndLastName".
    // Thay vào đó, nó sẽ thực thi câu lệnh JPQL này.
    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findFirstNameAndLastName(String firstName, String lastName);
}
```

-----

### 3\. Các loại tham số (Parameters)

`@Query` hỗ trợ hai cách để "điền" (bind) các tham số từ phương thức vào câu truy vấn:

**A. Tham số Vị trí (Positional Parameters) (Như ví dụ của bạn)**

* **Cú pháp:** `?1`, `?2`, ...
* **Cách hoạt động:** `?1` tham chiếu đến tham số *thứ nhất* của phương thức (`firstName`). `?2` tham chiếu đến tham số *thứ hai* (`lastName`).
* **Nhược điểm:** Rất "mong manh" (fragile). Nếu bạn thay đổi thứ tự tham số trong phương thức, truy vấn của bạn sẽ bị sai.

**B. Tham số Theo tên (Named Parameters) (Cách được khuyến nghị)**

* **Cú pháp:** `:ten_tham_so` và dùng `@Param`
* **Cách hoạt động:** Bạn đặt tên cho các tham số, làm cho truy vấn dễ đọc và an toàn hơn khi tái cấu trúc (refactor).
* **Ví dụ:**
  ```java
  @Query("select e from Employee e where e.firstName = :fname and e.lastName = :lname")
  Employee findByNames(
      @Param("fname") String firstName, 
      @Param("lname") String lastName
  );
  ```

-----

### 4\. Các trường hợp sử dụng nâng cao

`@Query` không chỉ dùng cho `SELECT` trong **JPQL** (ngôn ngữ truy vấn của JPA, tham chiếu đến *Entity* và *fields*).

* **Truy vấn SQL Gốc (Native SQL):**
  Bạn có thể bảo Spring hãy chạy một câu **SQL thô** (tham chiếu đến *table* và *columns*) bằng cách thêm `nativeQuery = true`.

  ```java
  @Query(
    value = "SELECT * FROM employees e WHERE e.first_name = ?1", 
    nativeQuery = true
  )
  Employee findByFirstNameNative(String firstName);
  ```

* **Truy vấn Cập nhật (UPDATE/DELETE) với `@Modifying`:**
  `@Query` cũng có thể dùng cho `UPDATE` hoặc `DELETE`, nhưng bạn **bắt buộc** phải thêm annotation `@Modifying`.

  ```java
  @Modifying
  @Transactional
  @Query("update Employee e set e.salary = e.salary * 1.1 where e.departmentId = ?1")
  int giveRaiseToDepartment(int departmentId);
  ```

* **DTO Projection (Constructor Expressions):**
  Đây là một trong những công dụng mạnh nhất: trả về một DTO tùy chỉnh thay vì một Entity.

  ```java
  // Trả về một DTO tên là "EmployeeDto"
  @Query("select new com.example.dto.EmployeeDto(e.name, d.name) " +
         "from Employee e join e.department d where e.id = ?1")
  EmployeeDto findEmployeeDtoById(int id);
  ```