## ❓ Câu hỏi: Bạn có sử dụng Spring trong một unit test không?

Đây là một câu hỏi rất hay và câu trả lời của bạn là **cực kỳ chính xác và sâu sắc**. Nó chạm đến cốt lõi của triết lý testing.

Câu trả lời ngắn gọn là: **Thường là KHÔNG** cho phần **IoC Container (Context)**, nhưng **CÓ** cho các **tiện ích (utilities)** mà Spring cung cấp để hỗ trợ.

Phân tích của bạn về Unit, Integration, và System Test là hoàn toàn đúng. Hãy cùng diễn giải chi tiết các điểm này.

-----

### 1\. 🎯 Unit Test vs. Integration Test (Sự khác biệt mấu chốt)

Để trả lời câu hỏi này, chúng ta phải làm rõ sự khác biệt giữa hai loại test:

**💡 Ví dụ so sánh: Kiểm thử một chiếc xe hơi**

* **🚗 Unit Test (Kiểm thử đơn vị):**

    * **Mục tiêu:** Kiểm tra *một bộ phận duy nhất* (ví dụ: chỉ cái **Động cơ**) trong một môi trường "biệt lập" (trên bàn kỹ thuật).
    * **Cách làm:** Bạn **không** lắp động cơ vào xe. Bạn dùng các "dây cắm giả" (chính là **Mocks**) để cấp xăng, điện... và xem động cơ có chạy đúng không.
    * **Trong Spring:** Bạn chỉ kiểm tra *một class* (ví dụ: `UserService`). Bạn **không** khởi động `ApplicationContext` của Spring. Bạn dùng Mockito để **"giả lập" (mock)** các dependency của nó (ví dụ: `UserRepository`).
    * **Tốc độ:** Cực kỳ nhanh.

* **🏎️ Integration Test (Kiểm thử Tích hợp):**

    * **Mục tiêu:** Kiểm tra xem *nhiều bộ phận* (ví dụ: **Động cơ** và **Hộp số**) có *làm việc cùng nhau* một cách chính xác hay không.
    * **Cách làm:** Bạn lắp động cơ và hộp số lại với nhau và chạy thử.
    * **Trong Spring:** Bạn dùng Spring Test Framework (`@ExtendWith(SpringExtension.class)`, `@SpringBootTest`, `@DataJpaTest`...) để **khởi động một phần hoặc toàn bộ `ApplicationContext`**. Spring sẽ tự động `@Autowired` các bean "thật" (hoặc "test slices") lại với nhau.
    * **Tốc độ:** Chậm hơn đáng kể (vì phải khởi động Context).

-----

### 2\. Spring trong Unit Test (KHÔNG dùng Context)

Vậy, nếu Unit Test không khởi động Context, tại sao Spring lại "có liên quan"?

Như bạn đã chỉ ra, Spring cung cấp các **lớp tiện ích (utility classes)** cực kỳ hữu ích để *giúp* bạn viết Unit Test (POJO test) dễ dàng hơn.

**A. `ReflectionTestUtils` (Tiện ích Phản chiếu)**

* **Vấn đề:** Bạn cần test một `Controller`, nhưng nó dùng **Field Injection** (tiêm qua trường) cho `Service` (một thói quen không tốt nhưng phổ biến).
  ```java
  public class MyController {
      @Autowired
      private MyService myService; // Trường này là private!
  }
  ```
* **Giải pháp:** Trong Unit Test, bạn không thể gán `mockService` vào trường `private` đó. `ReflectionTestUtils` cho phép bạn "hack" (dùng reflection) để gán mock vào.
  ```java
  // Trong @Test
  MyController controller = new MyController();
  MyService mockService = Mockito.mock(MyService.class);

  // Dùng tiện ích của Spring để gán vào trường private
  ReflectionTestUtils.setField(controller, "myService", mockService);
  ```

**B. Các lớp Mock của Spring (Servlet, JNDI, Env)**

* **Vấn đề:** Bạn muốn test `Controller` nhưng không muốn chạy cả server Tomcat.
* **Giải pháp:** Spring cung cấp các "mock" (giả lập) cho môi trường Servlet.
    * `MockHttpServletRequest`: Giả lập một HTTP request.
    * `MockHttpServletResponse`: Giả lập một HTTP response.
    * `MockEnvironment`: Giả lập một `Environment` (chứa properties) cho test.

**C. `ModelAndViewAssert`**

* **Vấn đề:** Bạn muốn kiểm tra xem một phương thức `Controller` (không phải `@RestController`) có trả về đúng tên `View` và đúng `Model` hay không.
* **Giải pháp:** Spring cung cấp một lớp Assert chuyên dụng để test `ModelAndView`.

-----

### 3\. Spring trong Integration Test (CÓ dùng Context)

Đây mới là lúc bạn thực sự "chạy" Spring. Khi bạn thấy các annotation này, đó là **Integration Test**:

* `@ExtendWith(SpringExtension.class)` (JUnit 5) hoặc `@RunWith(SpringJUnit4ClassRunner.class)` (JUnit 4)
* `@SpringBootTest`: Tải *toàn bộ* `ApplicationContext` (giống như chạy app thật).
* `@WebMvcTest`: Chỉ tải "lát cắt" (slice) Web Layer (Controllers, Filters...).
* `@DataJpaTest`: Chỉ tải "lát cắt" JPA Layer (Repositories, `DataSource`...).

Những test này không còn là Unit Test vì chúng **không "biệt lập" (isolated)**. Chúng dựa vào IoC Container (Context) của Spring để khởi tạo và "kết nối" (wire) các bean lại với nhau.

-----

### 4\. 🔺 Tháp Kiểm thử (Testing Pyramid)

Phân tích của bạn về "Tháp Kiểm thử" là hoàn hảo và tóm tắt toàn bộ chiến lược:

* **Đáy tháp (Rộng): Unit Tests**

    * *Số lượng:* Rất nhiều.
    * *Cách làm:* `new MyService(mockRepo);`
    * *Tốc độ:* Siêu nhanh.
    * *Chi phí:* Rẻ.

* **Giữa tháp (Vừa): Integration Tests**

    * *Số lượng:* Vừa phải.
    * *Cách làm:* `@DataJpaTest` (Test Service + DB), `@WebMvcTest` (Test Controller + JSON).
    * *Tốc độ:* Chậm.
    * *Chi phí:* Trung bình.

* **Đỉnh tháp (Nhọn): System / E2E Tests**

    * *Số lượng:* Rất ít.
    * *Cách làm:* `@SpringBootTest` (chạy cả server) + Selenium (giả lập trình duyệt).
    * *Tốc độ:* Cực chậm.
    * *Chi phí:* Đắt đỏ, khó bảo trì.

**Kết luận:** Bạn đã hoàn toàn đúng. "Unit Test" thuần túy nên chạy bên ngoài container. Spring cung cấp *tiện ích* để giúp các test đó. Khi bạn *sử dụng container* (`@SpringBootTest`...), nó đã trở thành một "Integration Test".

## ❓ Câu hỏi: Loại test (kiểm thử) nào thường sử dụng Spring?

Câu trả lời của bạn là **hoàn toàn chính xác**: Loại test điển hình sử dụng Spring (IoC/DI container) là **Integration Tests (Kiểm thử Tích hợp)**.

Phân tích của bạn về sự khác biệt giữa Unit Test và Integration Test là lý do cốt lõi cho câu trả lời này. Hãy cùng diễn giải chi tiết hơn.

-----

### 1\. 💡 Tại sao là Integration Test, mà không phải Unit Test?

Sự lựa chọn này xuất phát từ **mục đích** của từng loại test.

**🚗 Unit Test (Kiểm thử Đơn vị)**

* **Mục đích:** Kiểm tra *một* lớp (class) duy nhất, một cách *biệt lập (isolated)*.
* **Ví dụ:** Kiểm tra logic bên trong `UserService`.
* **Cách làm (Không Spring):** Bạn **không** khởi động `ApplicationContext` của Spring. Thay vào đó, bạn dùng `new` để tạo `UserService` và "giả lập" (mock) tất cả các dependency của nó (như `UserRepository`).
  ```java
  // Đây là một Unit Test thuần túy (dùng Mockito)
  @Test
  void testMyServiceLogic() {
      // 1. Tạo Mocks (giả lập)
      UserRepository mockRepo = Mockito.mock(UserRepository.class);
      
      // 2. Tự tay "tiêm" (inject)
      UserService service = new UserService(mockRepo); 
      
      // 3. Chạy test
      service.doWork();
      
      // 4. Kiểm tra
      Mockito.verify(mockRepo, times(1)).save(...);
  }
  ```
* **Kết luận:** Dùng Spring (khởi động cả `ApplicationContext`) cho Unit Test là **không cần thiết** (overkill) và vi phạm nguyên tắc "biệt lập".

**🏎️ Integration Test (Kiểm thử Tích hợp)**

* **Mục đích:** Kiểm tra xem *nhiều* thành phần (components) có *làm việc (hợp tác) cùng nhau* một cách chính xác hay không.
* **Ví dụ:** Liệu `UserController` (lớp Web) có gọi đúng `UserService` (lớp Service), và `UserService` có ghi đúng vào `UserRepository` (lớp Data) hay không?
* **Cách làm (Dùng Spring):** Đây là lúc bạn *cần* Spring. Bạn cần Spring **IoC Container** (`ApplicationContext`) để:
    1.  Khởi tạo tất cả các bean "thật" (`UserController`, `UserService`, `UserRepository`).
    2.  Tự động "kết nối" (wire) chúng lại với nhau bằng `@Autowired`.
    3.  Cung cấp các dịch vụ "thật" (như `PlatformTransactionManager`).

Bạn không thể test sự "tích hợp" nếu các thành phần không được "tích hợp" (kết nối) với nhau. **`ApplicationContext` của Spring chính là "cái khung" (chassis) để tích hợp chúng lại.**

-----

### 2\. 🚀 Hỗ trợ của Spring cho Integration Test

Như bạn đã phân tích rất chi tiết, Spring Test Framework là một công cụ cực kỳ mạnh mẽ để làm cho Integration Test trở nên dễ dàng. Nó tập trung vào 3 mục tiêu chính:

#### A. Quản lý và Caching Context

* **Vấn đề:** Khởi động `ApplicationContext` (quét bean, tạo `DataSource`, `EntityManagerFactory`...) là việc rất **chậm**.
* **Giải pháp của Spring:**
    1.  **Quản lý Context:** Spring (thông qua `@ExtendWith(SpringExtension.class)` hoặc `@RunWith(SpringRunner.class)`) sẽ tự động tải `ApplicationContext` cho bạn dựa trên cấu hình (`@ContextConfiguration`, `@SpringBootTest`).
    2.  **Caching Context (Bộ đệm):** Đây là "phép thuật" lớn nhất. Spring sẽ **cache (lưu trữ) lại Context** sau khi tải lần đầu. Nếu các lớp test khác dùng *cùng một cấu hình*, chúng sẽ **tái sử dụng (reuse)** Context đã cache, giúp tiết kiệm thời gian khởi động.
    3.  **`@DirtiesContext`:** Như bạn nói, đây là cách bạn "bảo" Spring: "Test này đã làm 'bẩn' Context rồi, hãy vứt nó đi và tạo lại Context mới cho test tiếp theo."

#### B. Tiêm phụ thuộc (Dependency Injection) trong Test

* **Vấn đề:** Làm thế nào để test của bạn lấy được các bean "thật" (như `UserService`) từ Context?
* **Giải pháp của Spring:** Bạn có thể `@Autowired` các bean **trực tiếp vào trường (field) của lớp test**.
  ```java
  @SpringBootTest // Tải toàn bộ Context
  class MyIntegrationTest {
      @Autowired
      private UserService realUserService; // Bean "thật" từ Context
      
      @Autowired
      private MockMvc mockMvc; // Bean "giả lập" do Spring tạo
      
      @MockBean
      private EmailService mockEmailService; // Thay thế bean thật bằng mock
  }
  ```
* **`@MockBean` (Cực kỳ mạnh):** Cho phép bạn chạy Integration Test với *toàn bộ* Context, nhưng "thay thế" (replace) một bean cụ thể (ví dụ: `EmailService`) bằng một Mock (giả lập).

#### C. Quản lý Giao dịch (Transaction Management)

* **Vấn đề:** Integration Test thường ghi vào database. Điều này làm "bẩn" database và ảnh hưởng đến các test khác (như đã thảo luận ở câu trước).
* **Giải pháp của Spring:** Như bạn đã nói, Spring tự động "bọc" (wrap) mỗi phương thức `@Test` trong một `@Transactional` và **mặc định là `ROLLBACK`** khi test kết thúc.
* **`@Commit` / `@Rollback(false)`:** Cho phép bạn ghi đè (override) và `COMMIT` nếu muốn.

-----

### 3\. 🧰 Hộp công cụ Test (Your Annotation List)

Danh sách các annotation bạn cung cấp là một bản tóm tắt tuyệt vời về "hộp công cụ" mà Spring cung cấp cho Integration Test. Mỗi annotation giải quyết một vấn đề cụ thể:

* **Thiết lập Context:** `@ContextConfiguration`, `@WebAppConfiguration`, `@ContextHierarchy`.
* **Thiết lập Môi trường:** `@ActiveProfiles` (chọn profile `test`), `@TestPropertySource` (ghi đè properties).
* **Thiết lập Database:** `@Sql`, `@SqlConfig` (chạy script SQL để setup dữ liệu).
* **Quản lý Giao dịch:** `@Transactional`, `@Commit`, `@Rollback`, `@BeforeTransaction`.
* **Kiểm thử Web:** `MockMvc` (để "gọi" Controller mà không cần chạy server)
* **Kiểm thử REST:** `MockRestServiceServer` (để "giả lập" các API bên ngoài mà `RestTemplate` của bạn gọi).

**Tóm lại:** Câu trả lời của bạn là hoàn hảo. Spring được sử dụng chủ yếu cho **Integration Tests** vì mục đích của loại test này là kiểm tra các thành phần *bên trong* IoC Container, và Spring cungn cấp một bộ công cụ cực kỳ phong phú để hỗ trợ việc này.

## ❓ Câu hỏi: Làm thế nào để bạn tạo một "shared application context" (context ứng dụng chia sẻ) trong một JUnit integration test?

Câu trả lời của bạn là **hoàn toàn chính xác** và đã phân biệt rất rõ hai khái niệm quan trọng:

1.  **Shared Context *Instance*** (Chia sẻ "Đối tượng" Context): Spring tự động *tái sử dụng* (reuse) một Context đã được khởi động.
2.  **Shared Context *Definition*** (Chia sẻ "Mã cấu hình"): Lập trình viên tránh lặp lại (copy-paste) code cấu hình test.

Hãy cùng diễn giải chi tiết hai khái niệm này.

-----

### 1\. 🚀 Shared Context Instance (Chia sẻ "Đối tượng" Context - Tự động)

Đây là hành vi **mặc định** của Spring Test Framework.

**💡 Vấn đề:** Khởi động một `ApplicationContext` (quét bean, tạo `DataSource`, `EntityManagerFactory`...) là một quá trình **RẤT CHẬM**. Nếu bạn có 500 test, và mỗi test đều phải khởi động một Context mới, bộ test (test suite) của bạn có thể chạy mất hàng giờ.

**Giải pháp (Caching):**
Spring Test Framework có một cơ chế gọi là **`ContextCache`** (Bộ đệm Context).

Hãy nghĩ `ContextCache` như một **"bãi đỗ xe thông minh"**:

1.  **Test 1 chạy:** Nó cần một Context với cấu hình A (`@ContextConfiguration(classes=ConfigA.class)`).
2.  **`ContextCache` (Bãi đỗ):** "Tôi chưa có xe nào (Context) chạy cấu hình A. OK, tôi sẽ khởi động một xe mới." (Quá trình này **CHẬM**).
3.  `ContextCache` "đỗ" Context A này vào bãi.
4.  **Test 2 chạy:** Nó cũng cần một Context với cấu hình A.
5.  **`ContextCache` (Bãi đỗ):** "Aha\! Tôi đã có xe A đang chạy. Dùng lại xe này đi\!" (Quá trình này **SIÊU NHANH**).

#### "Chìa khóa" của Cache là gì?

Như bạn đã nói, Spring quyết định "tái sử dụng" dựa trên **`MergedContextConfiguration`**. "Chìa khóa" (key) của cache này được tạo ra từ *tất cả* các annotation cấu hình của bạn.

Một Context chỉ được tái sử dụng nếu **TẤT CẢ** các mục sau đây *giống hệt nhau*:

* `@ContextConfiguration` (ví dụ: `classes`, `locations`)
* `@ActiveProfiles` (ví dụ: `{"test", "aws"}`)
* `@TestPropertySource` (ví dụ: `properties = "my.prop=value"`)
* `@WebAppConfiguration`
* Và một số annotation khác...

Nếu *chỉ một* trong số này khác đi (ví dụ: Test B dùng `@ActiveProfiles("test")` còn Test C dùng `@ActiveProfiles("test", "db")`), Spring sẽ phải tạo một Context mới cho Test C.

#### Cách "Phá vỡ" Cache (Như bạn đã nêu)

Đôi khi, bạn *muốn* phá vỡ cache. Ví dụ, Test A làm "bẩn" (modifies) một bean `singleton` và bạn muốn Test B có một Context "sạch".

* **`@DirtiesContext`**: Đây là cách bạn "bảo" Spring: "Sau khi chạy test này, hãy vứt bỏ Context này đi. Đừng cache nó nữa."

-----

### 2\. 📋 Shared Context Definition (Chia sẻ "Mã cấu hình" - Thủ công)

Phần này giải quyết một vấn đề khác: **Sự lặp lại code (DRY - Don't Repeat Yourself)**.

Giả sử cả 500 test của bạn đều dùng *cùng* một cấu hình. Bạn không muốn copy-paste 4-5 dòng annotation này vào 500 file test:

```java
// KHÔNG NÊN: Lặp lại ở 500 file
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, TestDbConfig.class})
@ActiveProfiles("test")
@TestPropertySource(locations = "/test.properties")
public class UserServiceTest { ... }
```

Bạn có 2 cách "chia sẻ" cấu hình này (như bạn đã liệt kê):

#### A. Dùng Lớp cha (Base Class) - Phổ biến nhất

Bạn tạo một lớp `abstract` chứa tất cả các annotation chung, và tất cả các test của bạn sẽ `extends` (kế thừa) từ nó.

```java
// 1. Tạo Lớp cha "chia sẻ"
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, TestDbConfig.class})
@ActiveProfiles("test")
@TestPropertySource(locations = "/test.properties")
public abstract class AbstractIntegrationTest {
    // Thường chứa các @Autowired chung, @MockBean chung...
}

// 2. Các test kế thừa nó
public class UserServiceTest extends AbstractIntegrationTest {
    @Test
    void testSomething() { ... }
}

public class OrderServiceTest extends AbstractIntegrationTest {
    @Test
    void testSomethingElse() { ... }
}
```

#### B. Dùng Annotation Tùy chỉnh (Custom Annotation) - Nâng cao

Đây là cách "sạch" hơn. Bạn tạo ra một *annotation của riêng mình* (gọi là meta-annotation) và "gói" tất cả các annotation khác vào đó.

```java
// 1. Tạo Annotation tùy chỉnh
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(SpringExtension.class) // Gói tất cả vào đây
@ContextConfiguration(classes = {AppConfig.class, TestDbConfig.class})
@ActiveProfiles("test")
@TestPropertySource(locations = "/test.properties")
public @interface MyIntegrationTest {
    // (Interface này trống)
}

// 2. Các test chỉ cần dùng 1 annotation
@MyIntegrationTest
public class UserServiceTest {
    @Test
    void testSomething() { ... }
}

@MyIntegrationTest
public class OrderServiceTest {
    @Test
    void testSomethingElse() { ... }
}
```

Cả hai phương pháp này đều đảm bảo rằng **"chìa khóa" cache (`MergedContextConfiguration`) là giống hệt nhau** cho tất cả các test, do đó giúp tối đa hóa việc *tái sử dụng Context Instance* (Mục 1).

## ❓ Câu hỏi: Khi nào và ở đâu bạn sử dụng @Transactional trong testing?

Câu trả lời của bạn là **hoàn toàn chính xác**. Bạn đã nắm rõ mục đích và cách thức sử dụng `@Transactional` trong môi trường test.

Hãy cùng diễn giải chi tiết hơn các điểm này.

-----

### 1\. 🗓️ Khi nào (When) bạn dùng `@Transactional`?

Như bạn đã nói, bạn sử dụng `@Transactional` trong một bài test (thường là **Integration Test**) bất cứ khi nào phương thức test đó **thực hiện các thay đổi đối với một tài nguyên giao dịch (ví dụ: database)**.

**Mục đích chính:** Đảm bảo **Tính cô lập (Isolation)** và **Tính lặp lại (Repeatability)** của các bài test.

**💡 Ví dụ về kịch bản:**

Bạn có hai bài test:

1.  `testCreateUser()`: Tạo một user mới.
2.  `testCountUsers()`: Đếm số lượng user trong DB (mong đợi là 0).

**Vấn đề (Nếu KHÔNG dùng `@Transactional`):**

* Giả sử `testCreateUser()` chạy trước. Nó `INSERT` một user vào DB và `COMMIT` (vì `AutoCommit=true`).
* Sau đó, `testCountUsers()` chạy. Nó đếm và thấy có **1** user.
* **Kết quả:** `testCountUsers()` **THẤT BẠI** 😭. Kết quả test của bạn bị phụ thuộc vào thứ tự chạy.

**Giải pháp (Khi DÙNG `@Transactional`):**

1.  Spring thấy `@Transactional` trên `testCreateUser()`.
2.  Nó **Bắt đầu** một Giao dịch (Transaction) mới.
3.  Test của bạn chạy. Nó `INSERT` một user vào DB (bên trong giao dịch đó).
4.  Test kết thúc (thành công).
5.  Spring Test Framework kích hoạt chính sách mặc định: **LUÔN LUÔN `ROLLBACK`**.
6.  `INSERT` bị hoàn tác. Database trở lại trạng thái "sạch sẽ" như ban đầu.
7.  `testCountUsers()` chạy trên một DB trống và **THÀNH CÔNG** 😊.

Như bạn đã nói, đây là cơ chế mặc định. Bạn có thể thay đổi nó bằng cách:

* **`@Commit`** (hoặc `@Rollback(false)`): "Bảo" Spring: "Đừng rollback, hãy `COMMIT` giao dịch này." (Hữu ích khi bạn muốn gỡ lỗi (debug) và xem dữ liệu thật trong database sau khi test chạy).
* **`@BeforeTransaction` / `@AfterTransaction`**: Cho phép bạn chạy code setup/teardown (ví dụ: in log) *ngay trước khi* giao dịch bắt đầu hoặc *ngay sau khi* nó kết thúc (commit/rollback).

-----

### 2\. 📍 Ở đâu (Where) bạn dùng `@Transactional`?

Bạn có thể đặt annotation này ở hai cấp độ, như bạn đã minh họa chính xác:

#### A. Ở cấp độ Lớp (Class-level)

Đây là cách làm phổ biến nhất khi *toàn bộ* lớp test đều làm việc với database.

* **Cách làm:** Đặt `@Transactional` một lần duy nhất trên tên `class`.
* **Hành vi:** **Mọi** phương thức `@Test` bên trong lớp này sẽ tự động được "bọc" trong một giao dịch và được `rollback`.

<!-- end list -->

```java
@ExtendWith(SpringExtension.class) // JUnit 5
@ContextConfiguration(classes = AppConfig.class)
@Transactional // <-- ÁP DỤNG CHO TẤT CẢ CÁC PHƯƠNG THỨC BÊN DƯỚI
public class UserServiceIntegrationTest {

    @Test
    public void testCreateUser() {
        // (Chạy trong transaction và sẽ rollback)
    }
    
    @Test
    public void testDeleteUser() {
        // (Chạy trong transaction và sẽ rollback)
    }
}
```

#### B. Ở cấp độ Phương thức (Method-level)

Cách này hữu ích khi bạn muốn kiểm soát chi tiết: chỉ một số test cần giao dịch, hoặc bạn muốn cấu hình khác nhau cho từng test (ví dụ: một test `COMMIT`, các test khác `ROLLBACK`).

* **Cách làm:** Đặt `@Transactional` trên từng phương thức `@Test` cụ thể.

<!-- end list -->

```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
// KHÔNG có @Transactional ở đây
public class MixedServiceIntegrationTest {

    @Test
    @Transactional // <-- Chỉ test này được rollback
    public void testWriteToDb() {
        // (Chạy trong transaction và sẽ rollback)
    }

    @Test
    @Transactional
    @Commit // <-- Test này sẽ COMMIT
    public void testSaveAndKeepData() {
        // (Chạy trong transaction và sẽ commit)
    }
    
    @Test
    public void testReadOnlyLogic() {
        // (Test này không cần transaction)
    }
}
```

## ❓ Câu hỏi: Các mock framework như Mockito hay EasyMock được sử dụng như thế nào?

Chào bạn, câu trả lời của bạn là **rất chính xác và đầy đủ**. Bạn đã nắm được mục đích, cách thức hoạt động và cả cách nó tích hợp (thông qua annotation) để làm cho Unit Test nhanh và nhẹ.

Hãy cùng diễn giải chi tiết hơn bằng một ví dụ so sánh và code cụ thể để làm rõ các điểm này.

-----

### 1\. 💡 Mock Object là gì? (Phép ví von)

Như bạn đã nói, Mock là một "đối tượng động".

Cách dễ hiểu nhất là hãy nghĩ về Mock Object như một **"Diễn viên đóng thế" (Stunt Double)** trong một bộ phim.

* **Đối tượng Thật (`UserRepository`):** Là "Diễn viên chính". Anh ta rất đắt giá và yêu cầu một bối cảnh phức tạp (phải có kết nối Database thật, dữ liệu thật).
* **Đối tượng Mock (`Mock_UserRepository`):** Là "Diễn viên đóng thế". Anh ta trông *giống hệt* diễn viên chính (implement cùng interface), nhưng anh ta là đồ giả.
* **Mockito/EasyMock:** Là "Đạo diễn". Đạo diễn sẽ chỉ đạo diễn viên đóng thế.

Khi bạn đang quay (test) một cảnh (ví dụ: `UserService`), bạn không cần diễn viên chính (`UserRepository`) cho những cảnh đơn giản. Bạn chỉ cần diễn viên đóng thế (Mock).

-----

### 2\. 🎯 Tại sao chúng ta dùng Mock? (Mục đích)

Bạn dùng Mock (chủ yếu trong **Unit Test**) để **cô lập (isolate)** "đơn vị" (class) bạn đang test khỏi các dependency (các đối tượng cộng tác) của nó.

**Kịch bản:**
Bạn muốn test lớp `UserService`. Lớp `UserService` này lại phụ thuộc vào `UserRepository`.

```java
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUserNameById(Long id) {
        User user = userRepository.findById(id);
        if (user != null) {
            return user.getName().toUpperCase();
        }
        return "DEFAULT_USER";
    }
}
```

**Vấn đề khi KHÔNG dùng Mock (Test với `UserRepository` thật):**

1.  **Chậm:** Bạn phải khởi động một database (có thể là H2 in-memory).
2.  **Không cô lập:** Bạn phải chèn (insert) một user vào DB trước, sau đó mới test. Test A có thể ảnh hưởng đến Test B.
3.  **Khó giả lập lỗi:** Làm thế nào để bạn test trường hợp `userRepository.findById(id)` trả về `null`? Rất khó.

**Giải pháp (Dùng Mock):**
Bạn "bảo" Mockito: "Hãy tạo cho tôi một `UserRepository` GIẢ."
Bây giờ, bài test của bạn không cần database. Nó chạy **siêu nhanh** và **hoàn toàn biệt lập**.

-----

### 3\. ⚙️ Mockito/EasyMock làm gì? (Hai nhiệm vụ chính)

Các framework này cho phép "Đạo diễn" (bạn) làm 2 việc:

#### A. Dàn dựng (Stubbing): "Dạy" cho Mock biết phải làm gì

Đây là lúc bạn "lập trình" cho diễn viên đóng thế.

* **Cú pháp:** `when(mockObject.methodCall()).thenReturn(predefinedResult);`
* **Ví dụ:**
    * `when(mockRepo.findById(1L)).thenReturn(new User(1L, "Alice"));`
        * (Dạy: "Khi ai đó gọi `findById(1L)`, hãy trả về user 'Alice'").
    * `when(mockRepo.findById(2L)).thenReturn(null);`
        * (Dạy: "Khi ai đó gọi `findById(2L)`, hãy trả về `null`").
    * `when(mockRepo.findById(3L)).thenThrow(new RuntimeException("Lỗi DB"));`
        * (Dạy: "Khi ai đó gọi `findById(3L)`, hãy ném ra lỗi").

#### B. Kiểm chứng (Verification): "Kiểm tra" xem Mock có được gọi không

Sau khi chạy test, bạn muốn đảm bảo rằng `UserService` *thực sự đã gọi* `UserRepository`.

* **Cú pháp:** `verify(mockObject, times(1)).methodCall(expectedArguments);`
* **Ví dụ:**
    * `verify(mockRepo, times(1)).findById(1L);`
        * (Kiểm tra: "Có đúng là `findById(1L)` đã được gọi *đúng 1 lần* không?")
    * `verify(mockRepo, never()).delete(any());`
        * (Kiểm tra: "Có đúng là phương thức `delete` *không bao giờ* được gọi không?")

-----

### 4\. 🧩 Ví dụ Code (Unit Test với Mockito & JUnit 5)

Đây là cách bạn test `UserService` ở trên mà **không cần chạy Spring Context**.

```java
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) // Kích hoạt Mockito
class UserServiceTest {

    // 1. "Đạo diễn" tạo "Diễn viên đóng thế"
    @Mock
    private UserRepository mockUserRepository;

    // 2. "Đạo diễn" tạo "Đối tượng cần test"
    //    và tự động "tiêm" (inject) diễn viên đóng thế (mockUserRepository) vào
    @InjectMocks
    private UserService userService; // Đây là đối tượng THẬT (new UserService(...))

    @Test
    void testGetUserName_WhenUserExists() {
        // === 1. ARRANGE (Dàn dựng) ===
        User fakeUser = new User(1L, "Alice");
        // Dạy cho mock
        when(mockUserRepository.findById(1L)).thenReturn(fakeUser);

        // === 2. ACT (Hành động) ===
        String name = userService.getUserNameById(1L);

        // === 3. ASSERT (Kiểm chứng) ===
        // Kiểm tra kết quả trả về
        assertEquals("ALICE", name); 
        
        // Kiểm tra xem mock có được gọi đúng không
        verify(mockUserRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserName_WhenUserDoesNotExist() {
        // === 1. ARRANGE (Dàn dựng) ===
        // Dạy cho mock trả về null
        when(mockUserRepository.findById(2L)).thenReturn(null);

        // === 2. ACT (Hành động) ===
        String name = userService.getUserNameById(2L);

        // === 3. ASSERT (Kiểm chứng) ===
        assertEquals("DEFAULT_USER", name);
        verify(mockUserRepository, times(1)).findById(2L);
    }
}
```

### 5\. Mock trong Integration Test (Điểm của bạn)

Như bạn đã nói, mock cũng được dùng trong **Integration Test**. Lúc này, bạn *sẽ* khởi động Spring Context.

* **Cách làm:** Dùng **`@MockBean`** (của Spring Test).
* **Hành động:** `@MockBean` sẽ bảo Spring: "Hãy khởi động toàn bộ Context, nhưng khi nào thấy ai đó cần `UserRepository`, đừng dùng bean thật, hãy dùng cái **Mock** này thay thế."

<!-- end list -->

```java
@SpringBootTest // KHỞI ĐỘNG SPRING (Integration Test)
class UserServiceIntegrationTest {

    // Báo Spring: "Hãy tạo một Mock và thay thế bean UserRepository thật"
    @MockBean
    private UserRepository mockUserRepository;

    @Autowired
    private UserService userService; // Bean THẬT từ Spring Context

    @Test
    void testSomething() {
        // Dàn dựng (when...) và Kiểm chứng (verify...) y hệt như trên
    }
}
```

## ❓ Câu hỏi: @ContextConfiguration được sử dụng như thế nào?

Câu trả lời của bạn là **hoàn toàn chính xác**.

`@ContextConfiguration` là annotation (chú thích) quan trọng nhất để "bảo" Spring Test Framework (cụ thể là `SpringRunner` hoặc `SpringExtension`) rằng: **"Làm thế nào để tải (load) và cấu hình `ApplicationContext` cho bài test này?"**

Hãy nghĩ về nó như một **"tấm bản đồ"** bạn đưa cho Spring. Spring sẽ đọc "tấm bản đồ" này để xây dựng môi trường (Context) cho bài test của bạn.

-----

### 1\. 🎯 Mục đích chính

Như bạn đã nói, nó có hai "chế độ" (modes) chính để chỉ định "bản đồ" cấu hình:

#### A. ☕ Cách dùng Class (Java-based) - Phổ biến nhất

Bạn chỉ cho Spring các lớp `@Configuration` của mình.

* **Thuộc tính:** `classes = ...`
* **Ví dụ (Như của bạn):**
  ```java
  // Lớp Test của bạn
  @RunWith(SpringRunner.class) // Hoặc @ExtendWith(SpringExtension.class)
  // "Này Spring, hãy đọc lớp AppConfig.class để tìm bean"
  @ContextConfiguration(classes = AppConfig.class) 
  public class MyServiceIntegrationTest {
      @Autowired
      private MyService myService; // Sẽ được tiêm từ AppConfig
      // ...
  }

  // ---

  // File cấu hình của bạn
  @Configuration
  @ComponentScan("com.example.service") // Quét tìm @Service
  public class AppConfig {
      @Bean // Cung cấp một bean đặc biệt cho test
      public DataSource testDataSource() {
          return new EmbeddedDatabaseBuilder().build();
      }
  }
  ```

#### B. 🗃️ Cách dùng XML (Legacy)

Bạn chỉ cho Spring vị trí của các file cấu hình `.xml` (thường là trong `classpath`).

* **Thuộc tính:** `locations = ...`
* **Ví dụ (Như của bạn):**
  ```java
  // Lớp Test của bạn
  @RunWith(SpringRunner.class)
  // "Này Spring, hãy đọc file XML này trong classpath"
  @ContextConfiguration(locations = "/application-context-test.xml")
  public class MyServiceIntegrationTest {
      // ...
  }
  ```

-----

### 2\. 🔑 Vai trò trong "Context Caching" (Bộ đệm Context)

Đây là một vai trò *ngầm* (implicit) nhưng cực kỳ quan trọng.

Spring Test Framework sẽ **cache (lưu trữ) lại `ApplicationContext`** sau khi khởi động lần đầu tiên (vì việc này rất chậm).

**"Chìa khóa" (key) của cache này chính là cấu hình của `@ContextConfiguration`.**

* `Test A` dùng `@ContextConfiguration(classes = AppConfig.class)` -\> Spring khởi động Context (CHẬM).
* `Test B` dùng `@ContextConfiguration(classes = AppConfig.class)` -\> Cấu hình **GIỐNG HỆT**. Spring **tái sử dụng (reuse)** Context của Test A (SIÊU NHANH).
* `Test C` dùng `@ContextConfiguration(classes = {AppConfig.class, DbConfig.class})` -\> Cấu hình **KHÁC**. Spring phải khởi động một Context mới (CHẬM).

-----

### 3\. ⚙️ Các thuộc tính nâng cao

Bạn cũng đã liệt kê chính xác các thuộc tính nâng cao:

* **`initializers`**: Cho phép bạn cung cấp một lớp "khởi tạo" (initializer) để *thêm* hoặc *sửa đổi* cấu hình (ví dụ: thêm properties) một cách có lập trình (programmatically) *trước khi* Context được làm mới (refresh).
* **`loader`**: Rất hiếm khi dùng. Dùng để thay thế "người tải" (loader) mặc định của Spring (`DelegatingSmartContextLoader`) bằng một logic tùy chỉnh.
* **`inheritLocations` / `inheritInitializers`**: Mặc định là `true`. Nó cho phép một lớp Test con (subclass) *kế thừa* (inherit) cấu hình từ lớp Test cha (superclass), rất hữu ích khi tạo một `AbstractBaseTest`.

## ❓ Câu hỏi: Spring Boot đơn giản hóa việc viết test như thế nào?

Chào bạn, câu trả lời của bạn là một **bản tóm tắt xuất sắc**. Bạn đã liệt kê gần như tất cả các tính năng cốt lõi mà Spring Boot cung cấp để cách mạng hóa việc kiểm thử (testing).

Spring Boot đơn giản hóa việc test bằng cách áp dụng triết lý cốt lõi của nó: **"Quy ước hơn Cấu hình" (Convention over Configuration)**. Nó loại bỏ 90% công việc "cài đặt" (setup) nhàm chán, cho phép bạn tập trung vào logic "kiểm thử" (test).

Hãy cùng diễn giải các điểm chính trong danh sách của bạn:

-----

### 1\. 📦 `spring-boot-starter-test` (Gói "Tất cả trong một")

Đây là sự đơn giản hóa đầu tiên. Như bạn đã liệt kê, thay vì phải tự mình "săn lùng" và quản lý 8-10 dependencies (thư viện) khác nhau, Spring Boot cung cấp một "starter" duy nhất:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

Khi bạn thêm starter này, nó tự động kéo về một bộ công cụ kiểm thử "tiêu chuẩn vàng" (gold standard) đã được cấu hình sẵn để làm việc cùng nhau:

* **JUnit 5:** Framework kiểm thử tiêu chuẩn của Java.
* **Spring Test & Spring Boot Test:** Bộ công cụ để chạy Integration Test (như `@SpringBootTest`, `@WebMvcTest`).
* **AssertJ:** Một thư viện "khẳng định" (assertion) linh hoạt (ví dụ: `assertThat(user.getName()).isEqualTo("John")`).
* **Mockito:** Framework "giả lập" (mocking) phổ biến nhất.
* **Hamcrest:** Thư viện cho các "matchers".
* **JSONassert & JsonPath:** Các tiện ích chuyên dụng để test JSON.

**Sự đơn giản hóa:** Bạn không cần lo lắng về "xung đột phiên bản" (version conflicts) giữa các thư viện này.

-----

### 2\. 🪄 Tự động Cấu hình (Auto-Configuration) với `@SpringBootTest`

Đây là "phép thuật" chính.

**Cách cũ (Spring Framework):**
Như bạn đã nói, bạn phải dùng `@ContextConfiguration` và chỉ định *thủ công* (manual) từng lớp `@Configuration` hoặc file XML cần tải:
`@ContextConfiguration(classes = {AppConfig.class, DatabaseConfig.class, WebConfig.class})`

**Cách mới (Spring Boot):**
Bạn chỉ cần dùng **`@SpringBootTest`**.

* Nó **tự động tìm** lớp `@SpringBootApplication` chính của bạn.
* Nó **tự động khởi động (boots)** *toàn bộ* `ApplicationContext` y hệt như khi bạn chạy ứng dụng thật.
* Nó **tự động tải** `application.properties` và kích hoạt các profile (`@ActiveProfiles`).

Bạn không cần "chỉ" cho nó bất cứ điều gì. Nó tự tìm và tự chạy.

-----

### 3\. 🔪 "Test Slices" (Các lát cắt kiểm thử) - Nhanh và Tập trung

Đây là tính năng **thông minh nhất** mà bạn đã liệt kê.

**Vấn đề:** `@SpringBootTest` (Mục 2) rất mạnh, nhưng nó **chậm** vì nó tải *mọi thứ* (lớp Web, lớp Service, lớp Data, Caching, Security...).

**Giải pháp (Test Slices):** Spring Boot cung cấp các annotation "lát cắt" (slice) chỉ tải *một phần* của Context mà bạn cần test.

Đây là các annotation bạn đã liệt kê:

* **`@WebMvcTest(UserController.class)`:** (Test lớp Web)

    * **Chỉ tải:** Lớp `UserController`, Spring MVC (JSON Converters, `MockMvc`...).
    * **KHÔNG tải:** Lớp `@Service`, `@Repository`, `DataSource`.
    * (Nó sẽ tự động `@MockBean` các `@Service` mà Controller của bạn cần).

* **`@DataJpaTest`:** (Test lớp Data)

    * **Chỉ tải:** Cấu hình JPA (`@Entity` scan, `EntityManager`), `DataSource` (thường là H2 in-memory), và các `@Repository` của bạn.
    * **KHÔNG tải:** Lớp `@Service`, `@Controller`.
    * (Nó cũng tự động cấu hình transaction và mặc định là `rollback`).

* **`@JsonTest`:** (Test lớp JSON)

    * **Chỉ tải:** `ObjectMapper` (Jackson) để test xem đối tượng của bạn có được serialize/deserialize đúng không.

**Sự đơn giản hóa:** Bạn không cần phải tạo các file `@Configuration` tùy chỉnh cho từng "lát cắt" này. Spring Boot tự động làm cho bạn.

-----

### 4\. 🎭 Hỗ trợ Mocking tích hợp (`@MockBean` / `@SpyBean`)

Đây là một sự đơn giản hóa *cực lớn* cho Integration Test.

**Vấn đề (Cách cũ):** Trong một Integration Test (`@SpringBootTest`), nếu bạn muốn test `UserService` nhưng không muốn nó gọi `EmailService` (dịch vụ gửi email thật), bạn phải cấu hình rất phức tạp (ví dụ: dùng profile "test" để tạo một bean `MockEmailService`).

**Giải pháp (Spring Boot):**
Bạn dùng **`@MockBean`**.

```java
@SpringBootTest // Tải Context thật
class UserServiceTest {

    @Autowired
    private UserService userService; // Bean THẬT

    // Báo Spring: "Hãy tìm bean 'EmailService' thật trong Context,
    // VỨT NÓ ĐI, và THAY THẾ bằng một MOCK của Mockito."
    @MockBean
    private EmailService mockEmailService;

    @Test
    void testRegisterUser() {
        // Dàn dựng (stub) cho mock
        doNothing().when(mockEmailService).sendWelcomeEmail(anyString());
        
        // Chạy service thật
        userService.register("test@example.com");
        
        // Kiểm chứng (verify) mock
        verify(mockEmailService, times(1)).sendWelcomeEmail("test@example.com");
    }
}
```

**Sự đơn giản hóa:** `@MockBean` cho phép bạn "thay thế" các bộ phận của hệ thống một cách linh hoạt ngay trong bài test mà không cần thay đổi file `@Configuration`.

-----

### 5\. 🌐 Kiểm thử Web (Web Environments)

Như bạn đã liệt kê, Spring Boot cho phép bạn kiểm thử Web ở các cấp độ khác nhau thông qua `@SpringBootTest(webEnvironment = ...)`:

* `MOCK` (Mặc định): Tạo một môi trường web "giả lập". Bạn dùng `MockMvc` để "gọi" controller mà không cần chạy server thật. (Nhanh, tốt cho Integration Test).
* `RANDOM_PORT` / `DEFINED_PORT`: Khởi động một **server Tomcat thật** trên một cổng ngẫu nhiên (hoặc cổng cụ thể). Tốt cho End-to-End (E2E) test.

## ❓ Câu hỏi: @SpringBootTest làm gì? Nó tương tác với @SpringBootApplication và @SpringBootConfiguration như thế nào?

Chào bạn, câu trả lời của bạn là **rất chính xác và chi tiết về mặt kỹ thuật**. Bạn đã nắm được "phép thuật" cốt lõi của `@SpringBootTest`.

`@SpringBootTest` là annotation (chú thích) **quan trọng nhất và mạnh mẽ nhất** để viết **Integration Test** trong Spring Boot.

Nó giải quyết một câu hỏi lớn: "Làm thế nào để khởi động ứng dụng Spring Boot của tôi *bên trong* một bài test?"

-----

### 1\. 💡 @SpringBootTest làm gì?

Hãy nghĩ về các annotation "Test Slice" (lát cắt) như `@WebMvcTest` (chỉ test Web) hay `@DataJpaTest` (chỉ test Data). Chúng chỉ khởi động *một phần* của ứng dụng.

Ngược lại, **`@SpringBootTest`** (mặc định) sẽ **khởi động TOÀN BỘ `ApplicationContext`** của bạn.

Nó mô phỏng (simulates) việc chạy phương thức `main` trong lớp `Application` của bạn. Toàn bộ hệ thống (Web, Service, Data, Security, v.v.) sẽ được khởi động và "kết nối" (wire up) với nhau, y hệt như khi chạy ứng dụng thật.

-----

### 2\. 🤝 Tương tác với @SpringBootApplication (Phần quan trọng nhất)

Đây là "phép thuật" chính: `@SpringBootTest` **tự động tìm kiếm (auto-search)** cấu hình chính của bạn.

**Vấn đề:** Khi bạn viết `@SpringBootTest`, làm thế nào nó "biết" *cấu hình nào* cần tải? (Ví dụ: `AppConfig.class` nào?)

**Giải pháp (Thuật toán Tìm kiếm):**

1.  **Cơ chế:** `@SpringBootTest` (thông qua `SpringBootContextLoader`) sẽ tự động tìm một lớp được chú thích (annotated) bằng **`@SpringBootConfiguration`**.
2.  **Sự liên kết:** Như bạn đã nói, annotation **`@SpringBootApplication`** (mà bạn đặt trên lớp `Runner` hoặc `Application` chính) đã *bao gồm* (meta-annotated) `@SpringBootConfiguration`.
    ```java
    // Bên trong Spring Boot
    @SpringBootConfiguration // <-- @SpringBootTest đang tìm cái này
    @EnableAutoConfiguration
    @ComponentScan
    public @interface SpringBootApplication { ... }
    ```
3.  **Thuật toán tìm kiếm (Giống như "GPS"):**
    * `@SpringBootTest` sẽ bắt đầu tìm từ gói (package) chứa **lớp Test** của bạn.
    * Nếu không tìm thấy `@SpringBootConfiguration` ở đó, nó sẽ **đi ngược lên gói cha (parent package)**.
    * Nó tiếp tục "đi ngược lên" cho đến khi tìm thấy lớp được chú thích `@SpringBootApplication` (hoặc `@SpringBootConfiguration`) của bạn.

**Đây là lý do tại sao bạn nên (Best Practice):**
Đặt các lớp test của bạn vào cùng một gói (hoặc gói con) của lớp `@SpringBootApplication` chính.

**Ví dụ cấu trúc dự án:**

```
com.example.myproject
├── Application.java          <-- Chứa @SpringBootApplication
│
├── services
│   └── MyService.java
│
└── test
    └── MyServiceIntegrationTest.java  <-- Chứa @SpringBootTest
```

Khi `MyServiceIntegrationTest` chạy, nó sẽ tự động "tìm lên" và phát hiện ra `Application.java`, sau đó tải toàn bộ context được định nghĩa bởi lớp đó.

-----

### 3\. 🎛️ Các thuộc tính chính (Features)

Như bạn đã liệt kê chính xác, `@SpringBootTest` cung cấp các tính năng mạnh mẽ:

#### A. Thuộc tính `classes` (Ghi đè - Override)

Dùng để *tắt* thuật toán tìm kiếm tự động.

```java
// "Đừng tự tìm. Chỉ tải 2 lớp config này thôi."
@SpringBootTest(classes = {MyTestConfig.class, AnotherConfig.class})
public class CustomTest { ... }
```

#### B. Thuộc tính `webEnvironment` (Rất quan trọng)

Nó kiểm soát xem server "thật" có được khởi động hay không.

* **`MOCK` (Mặc định):**
    * **Không** khởi động server (Tomcat).
    * Nó "giả lập" (mocks) môi trường web.
    * Bạn phải dùng `MockMvc` để test Controller (gửi request "giả").
    * *Nhanh hơn, phù hợp cho Integration Test.*
* **`RANDOM_PORT` hoặc `DEFINED_PORT`:**
    * **Khởi động một server Tomcat THẬT** trên một cổng ngẫu nhiên (hoặc cổng cụ thể).
    * Bạn dùng `TestRestTemplate` để gửi các HTTP request "thật" (real) đến `localhost:PORT`.
    * *Chậm hơn, phù hợp cho End-to-End (E2E) Test.*
* **`NONE`**: Không tải môi trường web nào cả (dùng cho ứng dụng console).

#### C. Thuộc tính `properties`

Cho phép bạn ghi đè (override) các giá trị trong `application.properties` chỉ dành riêng cho bài test này.

```java
@SpringBootTest(properties = { "my.feature.enabled=true", "db.name=testdb" })
public class FeatureTest { ... }
```

#### D. Đăng ký Bean Test tự động

Khi `webEnvironment` được kích hoạt, Spring Boot sẽ tự động tạo và đưa vào Context các bean `TestRestTemplate` và/ax`để bạn`@Autowired\` và sử dụng ngay lập tức.