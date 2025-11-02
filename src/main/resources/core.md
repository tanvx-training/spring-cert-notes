## ❓ Câu hỏi: Dependency Injection (DI) là gì và ưu điểm của nó?

### 1\. Dependency Injection (DI) là gì?

**Dependency Injection** (Tiêm phụ thuộc) là một nguyên tắc thiết kế (design principle) và cũng là một pattern (mẫu thiết kế).

Hãy chia nhỏ cụm từ này:

* **Dependency (Sự phụ thuộc):** Khi một lớp (class) A cần sử dụng một chức năng của lớp B, ta nói A *phụ thuộc* vào B.

    * *Ví dụ:* Một lớp `Car` (Xe hơi) cần một lớp `Engine` (Động cơ) để hoạt động. Vậy, `Engine` là một **dependency** của `Car`.

* **Injection (Tiêm vào):** Thay vì `Car` tự mình tạo ra (khởi tạo bằng `new`) một `Engine`, một ai đó từ bên ngoài (chính là Spring IoC Container) sẽ *tạo* `Engine` và *đưa* (tiêm) nó cho `Car`.

#### 💡 Ví dụ dễ hiểu (Trước và Sau khi có DI)

Hãy xem cách code thay đổi như thế nào.

**Trước khi có DI (Cách làm thông thường):**

Lớp `Car` tự chịu trách nhiệm tạo ra `Engine`.

```java
public class Car {
    private Engine engine;

    public Car() {
        // Tự tạo dependency của mình.
        // Điều này gây ra "phụ thuộc cứng" (tight coupling).
        // Car bây giờ bị gắn chặt với V8Engine.
        this.engine = new V8Engine();
    }

    public void start() {
        engine.run();
    }
}
```

* **Vấn đề:** Nếu ngày mai bạn muốn `Car` sử dụng `ElectricEngine` (Động cơ điện) thì sao? Bạn **bắt buộc phải sửa đổi** code bên trong lớp `Car`.

**Sau khi có DI (Cách làm của Spring):**

Lớp `Car` chỉ khai báo "Tôi cần một `Engine`", và Spring sẽ cung cấp nó.

```java
@Component // Báo cho Spring biết đây là 1 bean, hãy quản lý nó
public class Car {
    private final Engine engine; // Chỉ phụ thuộc vào Interface, không phải implementation

    // Spring sẽ tìm một Bean (ví dụ V8Engine) và "tiêm" nó vào đây
    @Autowired
    public Car(Engine engine) {
        this.engine = engine;
    }

    public void start() {
        engine.run();
    }
}

// Ở một nơi khác, bạn định nghĩa các loại Engine
@Component("v8")
public class V8Engine implements Engine {
    // ...
}

@Component("electric")
@Primary // Ưu tiên chọn cái này nếu có nhiều Engine
public class ElectricEngine implements Engine {
    // ...
}
```

* **Cách hoạt động:** Khi Spring khởi tạo `Car`, nó thấy constructor của `Car` cần một `Engine`. Spring sẽ tìm trong "kho" (ApplicationContext) của nó, thấy `ElectricEngine` (do có `@Primary`) và "tiêm" vào cho `Car`.
* **Lợi ích:** Ngày mai, nếu bạn muốn đổi sang `V8Engine`, bạn chỉ cần đổi annotation `@Primary` sang lớp `V8Engine`. **Lớp `Car` hoàn toàn không cần sửa đổi.**

Đây chính là cốt lõi của **Inversion of Control (IoC)** - "Đảo ngược quyền điều khiển". Quyền điều khiển việc tạo `Engine` đã bị đảo ngược, từ `Car` chuyển sang cho Spring Framework. DI chính là *cách thức* để thực hiện IoC.

-----

### 2\. Các loại DI (Types of DI)

Bạn đã liệt kê đúng 3 loại, trong đó 2 loại đầu là phổ biến nhất trong Spring:

1.  **Constructor Injection (Tiêm qua Constructor):**

    * Đây là cách được **khuyến nghị** bởi đội ngũ Spring.
    * Các dependency được truyền vào qua tham số của constructor (như ví dụ `Car` ở trên).
    * **Ưu điểm:**
        * Bạn có thể khai báo dependency là `final`, đảm bảo tính bất biến (immutability).
        * Bean chỉ được tạo khi tất cả dependency **bắt buộc** đã sẵn sàng. Nó đảm bảo đối tượng luôn ở trạng thái hợp lệ.

2.  **Setter Injection (Tiêm qua Setter):**

    * Spring sẽ gọi hàm `set...()` của bạn sau khi bean được khởi tạo (bằng constructor rỗng).
    * ```java
        @Component
        public class Car {
            private Engine engine;

            @Autowired
            public void setEngine(Engine engine) {
                this.engine = engine;
            }
        }
        ```
    * **Ưu điểm:** Hữu ích cho các dependency **không bắt buộc** (optional).
    * **Nhược điểm:** Đối tượng có thể tồn tại ở trạng thái không hoàn chỉnh (ví dụ `Car` đã được tạo nhưng chưa được `setEngine`).

3.  **Field Injection (Tiêm qua Thuộc tính):**

    * *Lưu ý: Bạn liệt kê "Interface injection", nhưng trong Spring, "Field injection" phổ biến hơn nhiều.*
    * `@Autowired` được đặt trực tiếp trên thuộc tính (field).
    * ```java
        @Component
        public class Car {
            @Autowired
            private Engine engine;
        }
        ```
    * **Ưu điểm:** Code rất gọn.
    * **Nhược điểm (Tại sao không nên dùng):**
        * Rất **khó để viết Unit Test** (vì bạn không thể dễ dàng gán mock object cho field `private`).
        * Vi phạm nguyên tắc đóng gói (encapsulation).
        * Các dependency bị ẩn đi, khó thấy rõ một lớp cần những gì.

> **Tóm tắt:** Luôn ưu tiên **Constructor Injection** cho các dependency bắt buộc. Chỉ dùng **Setter Injection** cho các dependency tùy chọn. Hạn chế tối đa dùng **Field Injection**.

-----

### 3\. Ưu điểm của DI (Advantages)

Phân tích của bạn là chính xác. Hãy làm rõ *tại sao* DI lại đạt được những điều đó:

* **Giảm sự phụ thuộc (Reduces Coupling / Loose Coupling):**

    * Đây là ưu điểm **lớn nhất**. Như ví dụ trên, lớp `Car` không còn "biết" gì về `V8Engine` hay `ElectricEngine`. Nó chỉ biết về interface `Engine`. Điều này cho phép bạn thay đổi các implementation một cách linh hoạt mà không ảnh hưởng đến các lớp sử dụng chúng.

* **Tăng khả năng kiểm thử (Increases Testability):**

    * Đây là lợi ích trực tiếp từ "Loose Coupling". Khi viết Unit Test cho lớp `Car`, bạn không muốn khởi động cả một cái `Engine` thật.
    * Với DI (đặc biệt là Constructor Injection), bạn có thể dễ dàng "tiêm" một `MockEngine` (Engine giả) vào:
    * ```java
        // Trong file test
        @Test
        void testCarStart() {
            // 1. Tạo một Engine giả (mock)
            Engine mockEngine = Mockito.mock(Engine.class);
            
            // 2. Dạy cho mockEngine biết phải làm gì
            when(mockEngine.run()).thenReturn(true);
            
            // 3. Tiêm mockEngine vào Car khi test
            Car car = new Car(mockEngine);
            
            // 4. Chạy test
            car.start();
            
            // 5. Xác thực
            verify(mockEngine, times(1)).run();
        }
        ```
    * Nếu không có DI, bạn không thể làm điều này vì `Car` sẽ luôn tự tạo `new V8Engine()`.

* **Tăng khả năng tái sử dụng (Increases Re-usability):**

    * Lớp `Car` có thể được tái sử dụng với bất kỳ `Engine` nào.
    * Lớp `V8Engine` cũng có thể được tái sử dụng ở bất cứ đâu cần `Engine`.

* **Tăng khả năng bảo trì và đọc hiểu (Increases Maintainability & Readability):**

    * Tất cả dependency của một lớp được khai báo rõ ràng ở một nơi (constructor hoặc các hàm setter). Bạn nhìn vào là biết ngay lớp này cần những gì để hoạt động.
    * Khi cần thay đổi (ví dụ: nâng cấp `Engine`), bạn chỉ cần thay đổi ở file cấu hình của Spring, không cần "lùng sục" trong code nghiệp vụ.

* **Tăng tính gắn kết (Increases Cohesion):**

    * Một lớp nên làm tốt một việc duy nhất (Single Responsibility Principle).
    * Nhiệm vụ của `Car` là "lái". Nhiệm vụ "tạo ra Engine" không phải của nó. DI giúp `Car` tập trung vào đúng nhiệm vụ của mình, tăng tính gắn kết nội tại.

## ❓ Câu hỏi: Pattern là gì? Anti-pattern là gì? DI có phải là pattern không?

### 1\. Design Pattern (Mẫu thiết kế) là gì?

> Bạn định nghĩa: "Một design pattern là một giải pháp có thể tái sử dụng cho các vấn đề thường gặp trong thiết kế phần mềm."

Đây là định nghĩa **hoàn toàn chính xác**.

* **Diễn giải thêm:** Hãy nghĩ về Pattern như một **bản thiết kế (blueprint)** hoặc một **công thức nấu ăn** đã được kiểm chứng.
* Nó không phải là một đoạn code cụ thể bạn có thể sao chép, mà là một *cách tiếp cận* hoặc một *ý tưởng* để giải quyết một loại vấn đề.
* Khi các lập trình viên nói "Hãy dùng **Builder Pattern** ở đây", mọi người trong nhóm sẽ ngay lập tức hiểu được cấu trúc và mục đích, ngay cả trước khi viết code.

#### 💡 Ví dụ về Builder Pattern (Bạn có đề cập)

**Vấn đề:** Làm thế nào để tạo một đối tượng có rất nhiều tham số, một số bắt buộc, một số không bắt buộc, mà không cần tạo ra hàng chục constructor khác nhau (gọi là *telescoping constructors*)?

```java
// Vấn đề: Constructor "kính viễn vọng" (telescoping) - rất khó đọc
User user = new User("John", "Doe", "john.doe@email.com", "123 Main St", null, "USA", true, false);
```

**Giải pháp (Pattern):** Sử dụng một lớp `Builder` bên trong.

```java
// Giải pháp: Dùng Builder - rất rõ ràng và dễ đọc
User user = new User.Builder("John", "Doe", "john.doe@email.com")
                    .address("123 Main St")
                    .country("USA")
                    .isSubscribed(true)
                    .build();
```

-----

### 2\. Anti-Pattern là gì?

> Bạn định nghĩa: "Một anti-pattern là một giải pháp không hiệu quả và phản tác dụng."

**Chính xác**.

* **Diễn giải thêm:** Anti-pattern là một "thói quen xấu" trong lập trình. Nó trông có vẻ là một giải pháp cho một vấn đề, nhưng thực tế nó lại gây ra nhiều rắc rối hơn về lâu dài, đặc biệt là về bảo trì, mở rộng và kiểm thử.
* Nó thường xuất phát từ việc không hiểu rõ vấn đề hoặc áp dụng sai một pattern.

#### 💡 Phân tích các ví dụ của bạn:

* **God Object (Lớp Thượng Đế):** Rất chính xác. Đây là một lớp làm *quá nhiều việc*. Nó vi phạm **Nguyên tắc Đơn trách nhiệm (Single Responsibility Principle)**. Cực kỳ khó để test và bảo trì.
* **Circular Dependency (Phụ thuộc vòng):**
    * Ví dụ của bạn (`A -> B -> C -> A`) là hoàn hảo.
    * **Trong Spring, điều này đặc biệt nguy hiểm.** Khi Spring cố gắng tạo bean `A`, nó thấy `A` cần `B`. Nó tạm dừng `A` để đi tạo `B`. Nó thấy `B` cần `C`. Nó tạm dừng `B` để đi tạo `C`. Nó thấy `C` cần `A`... nhưng `A` vẫn *đang trong quá trình tạo*\!
    * Điều này sẽ gây ra lỗi `BeanCurrentlyInCreationException` lúc runtime.
    * *Lưu ý nhỏ:* Bạn nói "không thể compile". Điều này chỉ đúng nếu bạn dùng **constructor injection**. Nếu bạn dùng setter hoặc field injection, code **vẫn compile được**, nhưng sẽ "chết" (throw exception) khi chạy ứng dụng. Đây là một lý do nữa Spring khuyến khích dùng constructor injection, vì nó phát hiện lỗi phụ thuộc vòng ngay tại thời điểm compile.
* **Sequential Coupling (Khớp nối tuần tự):** Rất đúng. Ví dụ: một lớp bắt người dùng phải gọi `init()` trước, rồi mới được gọi `doWork()`, sau đó phải gọi `cleanup()`. API này rất "mong manh" (fragile) vì người dùng có thể quên hoặc gọi sai thứ tự.
* **Constant Interface (Interface chứa hằng số):** Chính xác. Interface là để định nghĩa *hợp đồng hành vi* (methods), không phải để lưu trữ hằng số. Làm vậy sẽ "làm ô nhiễm" mọi lớp implement nó.
    * *Cách làm đúng:* `public final class AppConstants { private AppConstants() {} public static final String MY_CONSTANT = "value"; }`

-----

### 3\. Dependency Injection (DI) có phải là một Pattern không?

> Bạn trả lời: "Đúng vậy, Dependency Injection là một pattern giải quyết vấn đề tạo dependency linh hoạt."

**Hoàn toàn chính xác.**

* **DI là một Design Pattern** cụ thể.
* Nó là cách thức phổ biến nhất để triển khai một **Design Principle** (Nguyên tắc thiết kế) lớn hơn gọi là **Inversion of Control (IoC) - Đảo ngược quyền điều khiển**.
* **Vấn đề nó giải quyết:** "Phụ thuộc cứng" (Hard-coded dependencies).
* **Giải pháp nó cung cấp:** Thay vì một đối tượng tự tạo (`new`) dependency của nó, các dependency đó sẽ được *tiêm* (inject) vào từ bên ngoài (bởi Spring IoC Container).

Bạn có thể kết hợp DI với các pattern khác. Ví dụ, bạn có thể "tiêm" một **Strategy** vào một lớp dịch vụ:

```java
// Interface của Strategy Pattern
public interface PaymentStrategy {
    void pay(double amount);
}

// Lớp sử dụng, nhận Strategy qua DI
@Service
public class OrderProcessor {
    private final PaymentStrategy paymentStrategy;

    // DI (Constructor Injection)
    // Spring sẽ tiêm một bean cụ thể (ví dụ: CreditCardStrategy) vào đây
    @Autowired
    public OrderProcessor(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void processOrder(Order order) {
        // ...
        paymentStrategy.pay(order.getTotal());
        // ...
    }
}
```

## ❓ Câu hỏi: Định nghĩa về OOP, định nghĩa kỹ thuật trong Java, và (quan trọng nhất) là lý do tại sao nó lại cốt lõi trong Spring.

### 1\. Interface (Giao diện) là gì?

Bạn định nghĩa rất hay: "Interface là một cách để **thực thi các hành động** cho các lớp implement nó."

Nói một cách đơn giản nhất, interface là một **bản hợp đồng (contract)**.

* Nó định nghĩa **CÁI GÌ** (WHAT) một lớp phải có khả năng thực hiện (ví dụ: một `PaymentGateway` *phải* có khả năng `charge()`).
* Nó không quan tâm đến **NHƯ THẾ NÀO** (HOW) lớp đó thực hiện nó (ví dụ: `PayPalGateway` và `StripeGateway` sẽ có logic `charge()` khác nhau).

Phần định nghĩa kỹ thuật của bạn (reference type, abstract methods, default/static methods từ Java 8) là hoàn toàn chính xác.

-----

### 2\. Ưu điểm của Interface trong Java (Nói chung)

Bạn đã liệt kê 4 ưu điểm, và tất cả chúng đều xoay quanh một khái niệm trung tâm: **Loose Coupling (Giảm sự phụ thuộc)**.

Hãy dùng một ví dụ để thấy rõ các ưu điểm của bạn:

```java
// 1. Bản hợp đồng (contract)
public interface NotificationService {
    void sendNotification(String message);
}

// 2. Các cách thức thực hiện (implementations)
public class EmailService implements NotificationService {
    public void sendNotification(String message) {
        // Gửi email...
        System.out.println("Sending EMAIL: " + message);
    }
}

public class SmsService implements NotificationService {
    public void sendNotification(String message) {
        // Gửi SMS...
        System.out.println("Sending SMS: " + message);
    }
}

// 3. Lớp sử dụng (caller)
public class OrderService {
    // Chỉ phụ thuộc vào HỢP ĐỒNG, không phải implementation
    private NotificationService notificationService;

    // Nhận dependency qua constructor
    public OrderService(NotificationService service) {
        this.notificationService = service;
    }

    public void placeOrder() {
        // ... xử lý logic đặt hàng ...
        notificationService.sendNotification("Your order is placed!");
    }
}
```

Bây giờ, hãy xem các ưu điểm của bạn hoạt động như thế nào:

* **Decoupling (Tách biệt):** Lớp `OrderService` hoàn toàn không biết gì về `EmailService` hay `SmsService`. Nó chỉ biết về `NotificationService`.
* **Interchangeability (Tính hoán đổi):** Đây là kết quả của Decoupling. Hôm nay, tôi có thể "tiêm" `EmailService` vào `OrderService`. Ngày mai, nếu sếp muốn đổi sang SMS, tôi chỉ cần "tiêm" `SmsService`. **Lớp `OrderService` không cần sửa một dòng code nào.**
* **Testability (Tính kiểm thử):** Khi viết Unit Test cho `OrderService`, tôi không muốn gửi một email thật. Tôi có thể tạo một lớp `MockNotificationService` và "tiêm" nó vào `OrderService` để kiểm tra xem phương thức `sendNotification` có được gọi hay không.

-----

### 3\. Tại sao được khuyến nghị cho Spring Beans?

Đây là phần quan trọng nhất cho kỳ thi. Các câu trả lời của bạn là **hoàn toàn chính xác**.

#### 💡 Cho phép Dễ dàng Chuyển đổi Beans (Allows to easily switch beans)

Đây chính là ví dụ `OrderService` ở trên, nhưng được quản lý bởi Spring. Bằng cách sử dụng interface, bạn có thể tận dụng tối đa sức mạnh của Dependency Injection.

Ví dụ, bạn có thể quyết định sử dụng `SmsService` bằng cách dùng `@Primary`:

```java
@Component
public class EmailService implements NotificationService {}

@Component
@Primary // Báo Spring: "Nếu có ai hỏi NotificationService, hãy ưu tiên dùng cái này"
public class SmsService implements NotificationService {}
```

Lớp `OrderService` của bạn sẽ tự động nhận `SmsService` mà không cần thay đổi code:

```java
@Service
public class OrderService {
    private final NotificationService notificationService;

    @Autowired // Spring tự động tìm bean @Primary
    public OrderService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    // ...
}
```

#### 💡 Cho phép Sử dụng JDK Dynamic Proxy

Đây là lý do kỹ thuật **quan trọng nhất** và là một chủ đề lớn trong kỳ thi.

**Spring AOP (Aspect-Oriented Programming)** là nền tảng cho rất nhiều tính năng "ma thuật" của Spring:

* `@Transactional` (Quản lý transaction)
* `@Cacheable` (Quản lý cache)
* `@PreAuthorize` (Quản lý bảo mật)
* `@Async` (Xử lý bất đồng bộ)

**Nó hoạt động như thế nào?**

Khi bạn khai báo một bean `MyServiceImpl` (implement `MyService`) và đánh dấu nó là `@Transactional`, Spring **không** tiêm (inject) `MyServiceImpl` thật vào các bean khác.

Thay vào đó, Spring làm như sau:

1.  Nó tạo ra một đối tượng **Proxy** (giả mạo) lúc runtime.
2.  Đối tượng Proxy này **cũng implement `MyService`**, nên nó trông giống hệt `MyService` thật.
3.  Đối tượng Proxy này "bọc" (wraps) `MyServiceImpl` thật bên trong nó.
4.  Spring tiêm đối tượng **Proxy** này vào các bean khác (ví dụ: vào `MyController`).

Khi `MyController` gọi `myService.doWork()`, nó thực chất đang gọi **Proxy**:

> **Proxy.doWork()** {
>
> 1.  Bắt đầu một Transaction (`BEGIN TRANSACTION`).
> 2.  Gọi `myServiceImpl_thật.doWork()`.
> 3.  Nếu không có lỗi: Commit Transaction (`COMMIT`).
> 4.  Nếu có lỗi: Rollback Transaction (`ROLLBACK`).
      >     }

**Tại sao điều này liên quan đến Interface?**

* Bởi vì cơ chế mặc định mà Spring dùng để tạo Proxy là **JDK Dynamic Proxy**.
* **JDK Dynamic Proxy** là một tính năng của Java, và nó **chỉ có thể tạo proxy cho các interface**. Nó không thể tạo proxy cho một class.

Nếu bạn *không* sử dụng interface (bean của bạn là một class cụ thể), Spring không thể dùng JDK Proxy. Nó sẽ phải chuyển sang một thư viện khác tên là **CGLIB**, hoạt động bằng cách tạo một *lớp con* (subclass) của bean của bạn. Mặc dù Spring đủ thông minh để làm điều này, việc lập trình dựa trên interface được coi là "sạch" hơn và là cách làm được khuyến nghị.

Câu trả lời của bạn rất tốt và chính xác. Bạn đã tóm tắt được gần như toàn bộ các vai trò quan trọng nhất của `ApplicationContext`.

Hãy cùng diễn giải chi tiết hơn để hiểu rõ "bức tranh toàn cảnh" của nó nhé.

---

## ❓ Câu hỏi: Application Context có nghĩa là gì?

### 1. 💡 Diễn giải & Ví dụ

Hãy nghĩ `ApplicationContext` như là **bộ não điều hành trung tâm** của ứng dụng Spring của bạn.

Nếu ứng dụng của bạn là một nhà máy lắp ráp ô tô:

* Các **beans** (ví dụ: `Engine`, `Wheel`, `Chassis`) là các bộ phận.
* Bạn (lập trình viên) là người cung cấp các **bản thiết kế** (các lớp Java với `@Component`, `@Service` hoặc các file XML).
* `ApplicationContext` chính là **người quản lý nhà máy**.

Công việc của người quản lý này là:

1.  **Đọc bản thiết kế** của bạn (scanning components, đọc file XML).
2.  **Tạo ra các bộ phận** (khởi tạo các bean).
3.  **Lắp ráp chúng lại với nhau** (thực hiện Dependency Injection, ví dụ: "tiêm" `Engine` và 4 `Wheel` vào `Chassis`).
4.  **Quản lý toàn bộ vòng đời** của chúng (biết khi nào cần khởi động, khi nào cần tắt đi).

Khi một bộ phận khác (ví dụ: `SteeringWheel`) cần một bộ phận `Wheel`, nó không tự đi tạo. Nó chỉ cần *hỏi* người quản lý (ApplicationContext): "Cho tôi một `Wheel`", và `ApplicationContext` sẽ cung cấp `Wheel` đã được tạo sẵn.

Đây chính là **IoC Container** (Thùng chứa IoC) mà chúng ta hay nhắc đến. `ApplicationContext` là cách triển khai (implementation) mạnh mẽ và phổ biến nhất của IoC Container trong Spring.

---

### 2. Phân tích chi tiết các vai trò (Từ danh sách của bạn)

Danh sách của bạn rất tuyệt, hãy nhóm chúng lại cho dễ hiểu:

#### A. Vai trò cốt lõi: Quản lý Bean (Bean Management)

> * Initiates, configures and assembles the beans
> * Manages the bean life cycle
> * Is a bean factory

Đây là vai trò cơ bản nhất.

* **Là một `BeanFactory`:** `ApplicationContext` là một *interface* kế thừa (extends) từ một interface cơ bản hơn tên là `BeanFactory`. `BeanFactory` chỉ định nghĩa những thứ cơ bản nhất: tạo, lấy và quản lý bean.
* **Quản lý vòng đời (Life Cycle):** `ApplicationContext` còn làm nhiều hơn thế. Nó quản lý toàn bộ vòng đời của bean, cho phép nó can thiệp vào các giai đoạn quan trọng bằng cách gọi các "callback" như:
    * Các phương thức được đánh dấu `@PostConstruct` (chạy ngay sau khi bean được tiêm dependency).
    * Các phương thức được đánh dấu `@PreDestroy` (chạy ngay trước khi bean bị hủy).
* **Bean Post-Processing:** Đây là một tính năng cực kỳ quan trọng. `ApplicationContext` sẽ "hậu xử lý" (post-process) các bean sau khi chúng được tạo. Đây chính là cách các "phép thuật" như AOP (`@Transactional`, `@Async`) hoạt động. Nó sẽ bọc (wrap) bean thật của bạn trong một **Proxy** để thêm các hành vi (ví dụ: quản lý transaction).

#### B. Vai trò "Mở rộng" (Enterprise Features)

Đây là những tính năng làm cho `ApplicationContext` mạnh hơn nhiều so với `BeanFactory` cơ bản:

> * Is a resource loader
> * Pushes event to register event listener
> * Exposes env which allows to resolve properties

* **Resource Loader (Bộ tải tài nguyên):**
    * `ApplicationContext` cung cấp một cơ chế thống nhất để đọc các file bên ngoài, bất kể chúng ở đâu. Bạn có thể yêu cầu:
    * `classpath:config/my-file.xml` (từ trong classpath)
    * `file:/var/data/my-file.txt` (từ hệ thống file)
    * `http://example.com/config.json` (từ URL)
* **Event Publisher (Nhà xuất bản Sự kiện):**
    * `ApplicationContext` cho phép các bean giao tiếp với nhau một cách **decoupled** (giảm phụ thuộc) thông qua cơ chế publish-subscribe (Observer Pattern).
    * *Ví dụ:* `OrderService` có thể "xuất bản" một `OrderPlacedEvent`. `NotificationService` và `InventoryService` (ở 2 module hoàn toàn khác nhau) có thể "lắng nghe" (`@EventListener`) sự kiện đó và tự động thực thi (gửi email, trừ kho) mà không cần `OrderService` biết đến sự tồn tại của chúng.
* **Environment & Property Resolution (Môi trường & Thuộc tính):**
    * Đây là một vai trò cực kỳ quan trọng trong thực tế. `ApplicationContext` quản lý `Environment` (Môi trường).
    * `Environment` chứa 2 thứ:
        1.  **Properties:** Tất cả các cấu hình từ `application.properties`, biến môi trường (system variables), v.v.
        2.  **Profiles:** Giúp bạn kích hoạt các bean khác nhau cho các môi trường khác nhau (ví dụ: `@Profile("dev")` và `@Profile("prod")`).

---

### 3. Các loại ApplicationContext

Danh sách của bạn là chính xác. Lý do có nhiều loại như vậy là để trả lời cho câu hỏi: **"Lấy bản thiết kế (cấu hình) từ đâu?"**

* **`ClassPathXmlApplicationContext`:** "Lấy cấu hình từ một file **XML** trên **classpath**." (Cách cũ)
* **`FileSystemXmlApplicationContent`:** "Lấy cấu hình từ một file **XML** trên **hệ thống file**." (Cách cũ)
* **`AnnotationConfigApplicationContext`:** "Lấy cấu hình từ các **lớp Java** có annotation (`@Configuration`, `@ComponentScan`)." (Cách hiện đại, phổ biến nhất hiện nay).
* **`...WebApplicationContext`:** Các phiên bản đặc biệt dùng cho ứng dụng web (chạy trong máy chủ như Tomcat). Spring Boot sẽ tự động chọn và khởi tạo loại này cho bạn.

---

**Tóm lại:** `ApplicationContext` không chỉ là một "kho chứa" bean. Nó là một framework thu nhỏ, chủ động quản lý, lắp ráp, và cung cấp các dịch vụ nền tảng (như events, properties, resources) cho toàn bộ ứng dụng của bạn.

## ❓ Câu hỏi: What is the concept of a "container". What is its lifecycle?

### 1. 💡 Khái niệm "Container" là gì?

> Bạn định nghĩa: "Container là một môi trường thực thi (execution environment) cung cấp các dịch vụ kỹ thuật bổ sung cho code của bạn sử dụng."

Đây là một định nghĩa chính xác.

Hãy dùng một **ví dụ so sánh** đơn giản:

* **Không có Container (Ví dụ: một hàm `main()` đơn giản):**
    * Giống như bạn tự xây một căn nhà trên một lô đất trống.
    * Bạn phải tự mình lo mọi thứ: tự đi kéo điện (quản lý thread), tự đi khoan giếng (kết nối database), tự xây hệ thống an ninh (bảo mật), tự dọn rác (quản lý bộ nhớ).
    * Code "nghiệp vụ" (business logic) của bạn (ví dụ: `ở trong nhà`) bị trộn lẫn với code "cơ sở hạ tầng" (technical services).

* **Có Container (VíDụ: Spring ApplicationContext):**
    * Giống như bạn dọn vào ở trong một **khu chung cư cao cấp**.
    * Container (ban quản lý chung cư) cung cấp sẵn cho bạn mọi "dịch vụ kỹ thuật": có sẵn điện, nước (quản lý transaction), có bảo vệ (`@PreAuthorize`), có dịch vụ dọn phòng (thu gom rác), và kết nối mọi thứ sẵn sàng.
    * **IoC (Đảo ngược quyền điều khiển):** Bạn không cần tự đi tìm các dịch vụ đó. Bạn chỉ cần *khai báo* với ban quản lý: "Tôi cần dịch vụ Internet" (ví dụ: `@Autowired WifiService`). Ban quản lý (Container) sẽ tự động tìm và kết nối cho bạn.
    * **Lợi ích:** Bạn (lập trình viên) chỉ cần tập trung 100% vào *business logic* (cách bạn "sống" và "sinh hoạt" trong căn hộ của mình), mà không cần lo lắng về cơ sở hạ tầng bên dưới.

**Spring Container** (chính là `ApplicationContext`) là một "khu chung cư" chuyên biệt dành cho các **Beans** (các đối tượng Java được Spring quản lý).

---

### 2. Vòng đời (Lifecycle) của Container

Danh sách 12 bước của bạn là rất chi tiết và chính xác, mô tả toàn bộ quá trình từ khi "khởi động" đến khi "tắt máy". Chúng ta có thể nhóm chúng lại thành 5 giai đoạn chính cho dễ nhớ:



#### Giai đoạn 1: Khởi động (Startup)
* (Bước 1) Ứng dụng được khởi chạy (ví dụ: `SpringApplication.run(...)`).
* (Bước 2) **Spring Container được tạo.** Một đối tượng `ApplicationContext` cụ thể (ví dụ: `AnnotationConfigApplicationContext`) được khởi tạo.

#### Giai đoạn 2: Đọc và Xử lý Cấu hình (Parsing)
* (Bước 3) Container **đọc cấu hình** của bạn (quét các file `application.properties`, tìm các lớp `@Configuration`, `@ComponentScan`, v.v.).
* (Bước 4) **Bean Definitions được tạo.**
    * Đây là một bước cực kỳ quan trọng. Tại thời điểm này, **chưa có bean nào được tạo ra.**
    * Container chỉ tạo ra các "bản thiết kế" hay "công thức" (gọi là `BeanDefinition`) cho mỗi bean. Bản thiết kế này chứa thông tin như: tên class, scope (singleton/prototype), các dependency, v.v.
* (Bước 5) **`BeanFactoryPostProcessor`s được xử lý.**
    * Đây là *điểm mở rộng (extension point)* đầu tiên và rất quan trọng.
    * Các bean đặc biệt này (thực thi `BeanFactoryPostProcessor`) được phép **đọc và sửa đổi các `BeanDefinition`** (các bản thiết kế) *trước khi* bất kỳ bean nào được tạo.
    * *Ví dụ:* `PropertySourcesPlaceholderConfigurer` là một `BeanFactoryPostProcessor` chạy ở đây. Nhiệm vụ của nó là quét tất cả các `BeanDefinition` và tìm các giá trị như `${database.url}` để chuẩn bị thay thế chúng bằng giá trị thật từ file `application.properties`.

#### Giai đoạn 3: Tạo và Lắp ráp Bean (Instantiation)
* (Bước 6) **Các instance của Bean được tạo.**
    * Container bắt đầu duyệt qua các `BeanDefinition` (thường là các bean `singleton`) và gọi `new` để tạo ra các đối tượng thật.
* (Bước 7) **Các Bean được cấu hình và lắp ráp.**
    * Đây là lúc **Dependency Injection** (DI) xảy ra. Spring tiêm (inject) các dependency (ví dụ: tiêm `Engine` vào `Car`).
* (Bước 8) **`BeanPostProcessor`s được gọi.**
    * Đây là *điểm mở rộng* thứ hai. Các bean đặc biệt này (thực thi `BeanPostProcessor`) được phép **can thiệp vào chính các đối tượng bean** *ngay sau khi* chúng được tạo (nhưng trước khi chúng sẵn sàng để sử dụng).
    * *Ví dụ quan trọng nhất:* Đây là lúc các "phép thuật" AOP của Spring xảy ra. Nếu bạn có `@Transactional` trên một bean, `BeanPostProcessor` sẽ phát hiện, tạo một **Proxy** "bọc" bên ngoài bean thật của bạn, và *trả về cái Proxy đó* cho container.

#### Giai đoạn 4: Ứng dụng chạy (Running)
* (Bước 9) **Ứng dụng chạy.** Container đã khởi động xong. Tất cả các bean singleton đã được tạo và lắp ráp. Ứng dụng (ví dụ: máy chủ web) bắt đầu nhận request và xử lý nghiệp vụ.

#### Giai đoạn 5: Đóng (Shutdown)
* (Bước 10) Ứng dụng nhận tín hiệu tắt (ví dụ: nhấn Ctrl+C, hoặc lệnh `shutdown`).
* (Bước 11) **Spring Context được đóng.** Container bắt đầu quá trình dọn dẹp.
* (Bước 12) **Các callback Hủy (Destruction) được gọi.**
    * Container gọi các phương thức được đánh dấu `@PreDestroy` hoặc các bean thực thi `DisposableBean`.
    * Đây là cơ hội để các bean "dọn dẹp" tài nguyên trước khi bị hủy (ví dụ: đóng một connection pool, giải phóng file lock, v.v.).

---

**Tóm lại:** Câu trả lời của bạn rất vững. Việc bạn phân biệt được `BeanFactoryPostProcessor` (hoạt động trên *định nghĩa*) và `BeanPostProcessor` (hoạt động trên *instance*) là một kiến thức rất quan trọng cho kỳ thi.

Câu trả lời của bạn đã phân loại rất chính xác các cách thức và các lớp `ApplicationContext` tương ứng với từng môi trường. Đây là một câu hỏi quan trọng để hiểu sự khác biệt giữa các loại ứng dụng.

Hãy cùng diễn giải chi tiết hơn "Làm thế nào" (How) để bạn khởi tạo chúng.

-----

## ❓ Câu hỏi: Bạn sẽ tạo một instance mới của ApplicationContext như thế nào?

Câu trả lời cho câu hỏi này hoàn toàn phụ thuộc vào **loại ứng dụng** bạn đang xây dựng.

### 1\. 🚀 Ứng dụng Non-Web (Ví dụ: Console, Desktop)

Trong môi trường này, bạn (lập trình viên) phải **chủ động và khởi tạo thủ công** (manually create) `ApplicationContext` trong hàm `main()` của mình.

Bạn đã liệt kê chính xác 3 "con đường" để làm điều này:

* **`AnnotationConfigApplicationContext` (Cách hiện đại, nên dùng):**

    * Bạn dùng cách này khi bạn định nghĩa cấu hình bằng các lớp Java (sử dụng `@Configuration`, `@Bean`, `@ComponentScan`).
    * **Cách tạo:**
      ```java
      public static void main(String[] args) {
          // 1. Tạo Context từ một hoặc nhiều lớp Configuration
          ApplicationContext context = 
              new AnnotationConfigApplicationContext(AppConfig.class, OtherConfig.class);

          // 2. Lấy bean và sử dụng
          MyService service = context.getBean(MyService.class);
          service.doSomething();

          // 3. Đóng context (quan trọng trong app non-web)
          ((AnnotationConfigApplicationContext) context).close();
      }
      ```

* **`ClassPathXmlApplicationContext` (Cách truyền thống, dùng XML):**

    * Bạn dùng cách này khi cấu hình của bạn được định nghĩa trong các file `.xml` nằm trong `classpath`.
    * **Cách tạo:**
      ```java
      public static void main(String[] args) {
          // 1. Tạo Context từ một hoặc nhiều file XML trong classpath
          ApplicationContext context = 
              new ClassPathXmlApplicationContext("application-context.xml", "database.xml");
          
          // 2. Lấy bean và sử dụng
          MyService service = context.getBean("myService", MyService.class);
          service.doSomething();

          // 3. Đóng context
          ((ClassPathXmlApplicationContext) context).close();
      }
      ```

* **`FileSystemXmlApplicationContext` (Ít dùng):**

    * Tương tự như trên, nhưng nó tải file XML từ một đường dẫn tuyệt đối trên hệ thống file, thay vì từ `classpath`.
    * **Cách tạo:**
      `ApplicationContext context = new FileSystemXmlApplicationContext("C:/config/app.xml");`

-----

### 2\. 🌐 Ứng dụng Web (Truyền thống, Non-Spring Boot)

Trong môi trường này, bạn **không** khởi tạo `ApplicationContext` trong hàm `main()`. Thay vào đó, **Servlet Container** (như Tomcat, Jetty) sẽ chịu trách nhiệm khởi tạo nó khi ứng dụng web của bạn được deploy.

Bạn cấu hình *cho* Servlet Container biết *cách* tạo Context:

* **Servlet 2.x (Cũ - Dùng `web.xml`):**

    * Bạn khai báo một `ContextLoaderListener` trong file `web.xml`.
    * Khi Tomcat khởi động, nó sẽ đọc file này, tạo ra `ContextLoaderListener`, và `ContextLoaderListener` sẽ **tạo ra một `ApplicationContext`** (thường là `XmlWebApplicationContext` vì nó đọc file `applicationContext.xml` mặc định).
    * Đây là "Root" ApplicationContext, chứa các bean (như Services, Repositories) dùng chung cho toàn ứng dụng.

* **Servlet 3+ (Hiện đại - Dùng Java Config):**

    * Bạn không cần `web.xml`. Thay vào đó, bạn tạo một lớp implement `WebApplicationInitializer`.
    * Tomcat 3+ sẽ tìm lớp này khi khởi động.
    * Bên trong lớp này, bạn **tạo thủ công** một `AnnotationConfigWebApplicationContext` (hoặc `XmlWebApplicationContext`) và "đăng ký" nó với Servlet Container.
    * ```java
        public class MyWebAppInitializer implements WebApplicationInitializer {
            @Override
            public void onStartup(ServletContext container) {
                // 1. Tạo Root Context (dùng Java Config)
                AnnotationConfigWebApplicationContext rootContext =
                    new AnnotationConfigWebApplicationContext();
                rootContext.register(AppConfig.class); // Đăng ký lớp config

                // 2. Đăng ký Listener để quản lý vòng đời của Root Context
                container.addListener(new ContextLoaderListener(rootContext));
            }
        }
        ```

-----

### 3\. 🤖 Ứng dụng Spring Boot (Cách phổ biến nhất)

Đây là cách đơn giản nhất, vì Spring Boot làm **tất cả** cho bạn.

Bạn **không bao giờ** viết `new AnnotationConfigApplicationContext()` hay cấu hình `web.xml`.

Bạn chỉ cần một dòng duy nhất trong hàm `main()`:

```java
@SpringBootApplication
public class MyApplication {

    public static void main(String[] args) {
        // CHỈ MỘT DÒNG NÀY!
        // Nó trả về chính ApplicationContext đã được khởi tạo
        ApplicationContext context = SpringApplication.run(MyApplication.class, args);
        
        // Bạn có thể dùng context nếu muốn, nhưng thường không cần
    }
}
```

**Điều gì xảy ra bên trong `SpringApplication.run()`?**

1.  Spring Boot **tự động phát hiện** (auto-detects) loại ứng dụng của bạn.
2.  **Nếu nó thấy `spring-boot-starter-web`** (và Tomcat) trong `classpath`:
    * Nó tự động **tạo ra một `AnnotationConfigWebApplicationContext`**.
    * Nó tự động **khởi chạy một Embedded Tomcat** (Tomcat nhúng).
    * Ứng dụng của bạn chạy như một ứng dụng web.
3.  **Nếu nó *không* thấy thư viện web**:
    * Nó tự động **tạo ra một `AnnotationConfigApplicationContext`** (giống hệt cách làm thủ công ở mục 1).
    * Ứng dụng của bạn chạy như một ứng dụng console (non-web).
    * Các bean `CommandLineRunner` hoặc `ApplicationRunner` của bạn sẽ được thực thi *sau khi* context đã được tạo xong, cho phép bạn chạy logic nghiệp vụ trong console.

-----

**Tóm lại:**

* **Non-Web:** Bạn tự gọi `new ...Context(...)` trong `main()`.
* **Web (Cũ):** Bạn bảo Tomcat tạo Context thông qua `web.xml` (dùng `ContextLoaderListener`).
* **Spring Boot:** Bạn gọi `SpringApplication.run()`, và nó tự động quyết định và tạo ra Context chính xác cho bạn.

## ❓ Câu hỏi: Thứ tự của các phương thức khởi tạo (init) và hủy (destroy)?

### 💡 Ví dụ so sánh: "Lắp ráp một Robot thông minh"

Hãy tưởng tượng mỗi **Spring Bean** là một **Robot** phức tạp. `ApplicationContext` là **Nhà máy** sản xuất robot này. Vòng đời của Bean chính là quy trình lắp ráp trong nhà máy.

---

### Giai đoạn 1: Context được tạo (Chuẩn bị bản thiết kế)

Đây là giai đoạn "lên kế hoạch" của nhà máy.

**1. Bean Definitions được tạo (Tạo bản thiết kế):**
* **Điều gì xảy ra:** Spring quét (scan) các lớp `@Component`, `@Configuration`, file XML... của bạn.
* **Quan trọng:** Tại thời điểm này, **chưa có bất kỳ robot (bean) nào được tạo ra.** Spring chỉ tạo ra các "Bản thiết kế" (gọi là `BeanDefinition`) cho mỗi robot. Bản thiết kế này ghi rõ: "Robot này tên là `userService`, làm từ class `UserServiceImpl`, cần phụ thuộc vào `userRepository`."
* **Tương tự:** Nhà máy thu thập tất cả các bản vẽ kỹ thuật.

**2. `BeanFactoryPostProcessor`s được gọi (Sửa đổi bản thiết kế):**
* **Điều gì xảy ra:** Đây là cơ hội *đầu tiên* để can thiệp. Spring sẽ tìm các bean đặc biệt (bean implement `BeanFactoryPostProcessor`) và cho phép chúng *sửa đổi các bản thiết kế* (các `BeanDefinition`) trước khi bất kỳ robot nào được lắp ráp.
* **Ví dụ thực tế:** `PropertySourcesPlaceholderConfigurer` là một `BeanFactoryPostProcessor`. Nhiệm vụ của nó là xem qua tất cả các bản thiết kế và tìm những chỗ ghi `${database.url}` và thay thế nó bằng giá trị thật từ file `application.properties`.
* **Tương tự:** Một kỹ sư trưởng xem xét *toàn bộ* các bản thiết kế và ra lệnh: "Tất cả các bản thiết kế ghi 'vật liệu: thép' hãy đổi thành 'vật liệu: nhôm'".

---

### Giai đoạn 2: Bean được tạo (Lắp ráp robot)

Bây giờ, nhà máy bắt đầu lắp ráp từng robot (các bean `singleton`).

**1. Instance của Bean được tạo (Dập khuôn):**
* **Điều gì xảy ra:** Spring gọi constructor của lớp (ví dụ: `new UserServiceImpl()`).
* **Tương tự:** Phần thân (vỏ) của robot được dập khuôn. Nó vẫn chỉ là một cái vỏ rỗng.

**2. Properties và Dependencies được thiết lập (Lắp ráp phụ kiện):**
* **Điều gì xảy ra:** **Dependency Injection** diễn ra. Spring tìm `userRepository` đã được tạo và "tiêm" nó vào `userService` (thông qua `@Autowired` trên constructor, setter, hoặc field).
* **Tương tự:** Các cánh tay, chân, dây điện (`userRepository`) được lắp vào thân robot (`userService`). Robot đã *hoàn chỉnh về mặt vật lý*.

**3. `BeanPostProcessor::postProcessorBeforeInitialization` được gọi (Kiểm tra trước khi bật nguồn):**
* **Điều gì xảy ra:** Đây là cơ hội can thiệp *sau khi* DI, nhưng *trước khi* bean tự khởi động (init).
* **Tương tự:** Một thanh tra viên chạy một bài kiểm tra nhanh: "Các cánh tay đã được vặn chặt chưa? Pin đã được lắp chưa?" *trước khi* nhấn nút "Bật nguồn".

**4. `@PostConstruct` được gọi**
**5. `InitializingBean::afterPropertiesSet` được gọi**
**6. `@Bean(initMethod)` được gọi (Bật nguồn / Tự khởi động):**
* **Điều gì xảy ra:** Đây là 3 cách (với thứ tự ưu tiên như trên) để bạn định nghĩa logic "khởi động" cho bean của mình. Bean đã được lắp ráp đầy đủ, bây giờ nó cần tự "bật nguồn" và "cài đặt".
* **Ví dụ thực tế:** Một `DataSource` bean có thể dùng phương thức `init` để **khởi tạo connection pool** của nó.
* **Lưu ý cho kỳ thi:** Phải nhớ đúng thứ tự: (1) `@PostConstruct` -> (2) `afterPropertiesSet` -> (3) `initMethod`. **Nên dùng `@PostConstruct`** vì nó là chuẩn chung (JSR-250) và không làm code của bạn bị "dính" vào Spring (như `InitializingBean`).
* **Tương tự:** Nhấn nút "Bật nguồn". Robot tự chạy trình tự khởi động, kiểm tra hệ thống, kết nối vào mạng...

**7. `BeanPostProcessor::postProcessAfterInitialization` được gọi (Kiểm tra cuối & Bọc proxy):**
* **Điều gì xảy ra:** Đây là một trong những bước **quan trọng nhất** trong Spring. Nó chạy *sau khi* robot đã tự khởi động xong.
* **Ví dụ thực tế (AOP):** Spring thấy robot `userService` này có đánh dấu `@Transactional`. Tại bước này, Spring sẽ **không** trả về con robot `userService` thật. Thay vào đó, nó tạo ra một **Proxy** (một "bộ giáp" hoặc "robot đóng thế") và "bọc" con robot thật bên trong.
* **Tại sao?** Để khi ai đó gọi phương thức `userService.createUser()`, họ thực ra đang gọi cái **Proxy**. Cái Proxy này sẽ: *Bắt đầu Transaction -> gọi phương thức `createUser()` thật -> Commit (hoặc Rollback)*.
* **Tương tự:** Robot đã khởi động xong. Thanh tra viên nói: "Tốt. Giờ hãy mặc cho nó bộ giáp `Transactional` này." Nhà máy sẽ đưa "bộ giáp" (Proxy) ra ngoài.

---

### Giai đoạn 3: Bean sẵn sàng để sử dụng

* **Điều gì xảy ra:** Bean (hoặc cái Proxy của nó) được đưa vào "kho" (Singleton Cache) và sẵn sàng để được tiêm (inject) vào các bean khác.
* **Tương tự:** Robot đã sẵn sàng, được đưa vào kho để chờ giao nhiệm vụ.

---

### Giai đoạn 4: Bean bị hủy (Tắt máy)

Giai đoạn này xảy ra khi `ApplicationContext` bị đóng (ví dụ: ứng dụng shutdown).

**1. `@PreDestroy` được gọi**
**2. `DisposableBean::destroy` được gọi**
**3. `@Bean(destroyMethod)` được gọi (Chạy trình tự tắt máy):**
* **Điều gì xảy ra:** Đây là 3 cách (với thứ tự ưu tiên như trên) để bạn định nghĩa logic "dọn dẹp" trước khi bean bị hủy.
* **Ví dụ thực tế:** `DataSource` sẽ đóng tất cả các kết nối trong pool.
* **Lưu ý cho kỳ thi:** Thứ tự ngược lại với init. **Nên dùng `@PreDestroy`**.
* **Tương tự:** Nhận lệnh "Tắt máy". Robot chạy trình tự tắt máy an toàn (ngắt kết nối, lưu trạng thái...) trước khi tắt nguồn hoàn toàn.

---

**Tóm lại:** Danh sách của bạn là hoàn hảo để ghi nhớ. Điều quan trọng nhất cho kỳ thi là phải hiểu rõ sự khác biệt:

* **`BeanFactoryPostProcessor`:** Hoạt động trên **Bản thiết kế** (`BeanDefinition`), *trước khi* bean được tạo.
* **`BeanPostProcessor`:** Hoạt động trên **Đối tượng Bean** (`instance`), *sau khi* bean được tạo nhưng *quanh* quá trình `init`. Đây là nơi AOP/Proxy được tạo ra.

## ❓ Câu hỏi: Bạn sẽ tạo một ApplicationContext trong integration test như thế nào?

### 💡 Diễn giải: Bạn KHÔNG tự tạo, bạn "bảo" Spring tạo.

Khi chạy ứng dụng thật, bạn (hoặc Spring Boot) sẽ gọi `new AnnotationConfigApplicationContext(...)` hoặc `SpringApplication.run()`.

Trong một bài test, bạn **không** làm điều đó. Thay vào đó, bạn sử dụng các **annotation đặc biệt** để *hướng dẫn* bộ chạy test (Test Runner) của Spring tự động làm việc đó cho bạn. `ApplicationContext` này sẽ được **tạo một lần** và được **tái sử dụng** (cache lại) cho tất cả các phương thức test trong cùng một lớp (hoặc thậm chí qua nhiều lớp nếu cấu hình giống hệt nhau).

Hãy cùng phân tích các bước của bạn.

-----

### 1\. 📦 Thêm Dependency (Bước 1 của bạn)

> **`spring-test`**

* **Chính xác.** Đây là thư viện chứa tất cả "ma thuật" test của Spring.
* Nó cung cấp các lớp như `SpringRunner`, `SpringExtension`, các annotation `@SpringBootTest`, `@ContextConfiguration`, và các utilities để mock/spy bean.
* Không có nó, JUnit không biết "Spring" là gì.

-----

### 2\. 🏃 Chọn "Người chạy" (Runner)

Đây là bước quan trọng nhất, và nó khác nhau giữa JUnit 4 và JUnit 5.

#### A. Cách của bạn: JUnit 4 (Vẫn còn dùng)

> **`@RunWith(SpringRunner.class)`**

* **Giải thích:** `@RunWith` là một annotation của JUnit 4, nói rằng: "Này JUnit, đừng dùng bộ chạy test mặc định của bạn. Hãy dùng `SpringRunner` để chạy lớp test này."
* **`SpringRunner` làm gì?** Nó sẽ:
    1.  Đọc annotation `@ContextConfiguration` của bạn.
    2.  **Tạo một `ApplicationContext`** dựa trên cấu hình đó.
    3.  Tự động `@Autowired` bất kỳ bean nào từ Context đó vào các trường (fields) trong lớp test của bạn.

#### B. Cách hiện đại: JUnit 5 (Dùng với Spring Boot 2+)

> **`@ExtendWith(SpringExtension.class)`**

* **Giải thích:** JUnit 5 (Jupiter) không dùng "Runners", nó dùng "Extensions". `SpringExtension` là phiên bản kế nhiệm của `SpringRunner`.
* Chức năng của nó y hệt: tìm cấu hình, tạo `ApplicationContext`, và tiêm (inject) bean.

-----

### 3\. 📝 Cung cấp Cấu hình (Configuration)

Bây giờ `SpringRunner` (hoặc `SpringExtension`) đã được kích hoạt, nó cần biết: **"Tôi nên tải (load) những bean nào?"**. Bạn cung cấp thông tin này qua annotation cấu hình.

#### A. Cách của bạn: `@ContextConfiguration` (Dùng cho Spring Test đơn thuần)

> **`@ContextConfiguration(classes = ApplicationConfiguration.class)`**

* **Giải thích:** Đây là cách chỉ định "rõ ràng" (explicit). Bạn đang bảo Spring: "Hãy tạo một Context **chỉ** chứa các bean được định nghĩa trong lớp `ApplicationConfiguration.class`."
* Cách này rất hữu ích cho các bài test đơn vị (unit test) hoặc integration test nhỏ, nơi bạn chỉ muốn load một vài service hoặc repository, chứ không phải toàn bộ ứng dụng.
* Bạn cũng có thể dùng nó để tải file XML: `@ContextConfiguration(locations = "classpath:test-context.xml")`.

#### B. Cách Spring Boot: `@SpringBootTest` (Cách phổ biến nhất)

Đây là cách bạn sẽ gặp nhiều nhất. `@SpringBootTest` là một "siêu annotation" (meta-annotation) làm tất cả mọi thứ:

1.  Nó đã bao gồm sẵn **`@ExtendWith(SpringExtension.class)`** (bạn không cần viết lại).
2.  Nó **tự động tìm** lớp cấu hình chính của bạn (lớp có `@SpringBootApplication`) và **tải toàn bộ `ApplicationContext`** y hệt như khi bạn chạy ứng dụng thật.
3.  Nó cho phép bạn mock bean với `@MockBean` và `@SpyBean`.

-----

### 💡 Ví dụ tổng hợp

Dưới đây là 3 cách để test cùng một `UserService`.

**1. Cách của bạn (JUnit 4 + `@ContextConfiguration`)**

```java
// Chỉ định file cấu hình chỉ chứa UserService và các dependency của nó
@Configuration
class TestConfig {
    @Bean
    public UserService userService() {
        return new UserServiceImpl(userRepository());
    }
    @Bean
    public UserRepository userRepository() {
        return new MockUserRepository(); // Dùng mock repo cho test
    }
}

// Lớp Test
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class) // CHỈ TẢI TestConfig
public class UserServiceJUnit4Test {

    @Autowired
    private UserService userService; // Được tiêm từ Context

    @Test
    public void testUserLogic() {
        // ...
    }
}
```

**2. Cách JUnit 5 + `@ContextConfiguration` (Tương tự trên)**

```java
@ExtendWith(SpringExtension.class) // Chỉ thay @RunWith
@ContextConfiguration(classes = TestConfig.class)
public class UserServiceJUnit5Test {
    // ...
}
```

**3. Cách Spring Boot (JUnit 5 + `@SpringBootTest`) - Tải toàn bộ ứng dụng**

```java
@SpringBootTest // Tự động tìm @SpringBootApplication, tải TOÀN BỘ context
public class UserServiceSpringBootTest {

    @Autowired
    private UserService userService; // Lấy UserService thật

    // Nếu bạn muốn thay thế UserRepository bằng mock
    @MockBean
    private UserRepository userRepository;

    @Test
    public void testUserLogic() {
        // Giả lập hành vi của mock
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        
        // Chạy test với UserService thật
        userService.doSomethingWithUser(1L);
        
        // ...
    }
}
```

**Tóm lại:** Câu trả lời của bạn (dùng `SpringRunner` và `@ContextConfiguration`) là hoàn toàn chính xác cho việc tạo một `ApplicationContext` "tùy chỉnh" (custom) trong môi trường JUnit 4. Trong các dự án Spring Boot hiện đại, bạn sẽ thường dùng `@SpringBootTest` (cho integration test toàn diện) hoặc các "test slice" (như `@WebMvcTest`, `@DataJpaTest`) để tải các phần nhỏ hơn của Context.

## 💡 Câu hỏi: Đâu là cách tốt nhất để đóng ApplicationContext? Spring Boot có tự làm điều này không?

Câu trả lời đúng như bạn nói: **Nó phụ thuộc vào loại ứng dụng.**

Việc gọi `context.close()` là cực kỳ quan trọng, vì đây là hành động kích hoạt toàn bộ giai đoạn "hủy" (destruction) của bean (gọi các phương thức `@PreDestroy`, `DisposableBean::destroy`, v.v.), giúp giải phóng tài nguyên (như connection pools, file locks) một cách an toàn.

-----

### 🖥️ 1. Ứng dụng Standalone (Non-Web)

Đây là bối cảnh duy nhất mà bạn (lập trình viên) phải chủ động suy nghĩ về việc đóng context.

* **Cách 1 (Không nên dùng): `context.close()` thủ công**

  ```java
  public static void main(String[] args) {
      ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
      MyService service = context.getBean(MyService.class);
      service.doWork();
      
      // VẤN ĐỀ:
      // Nếu service.doWork() ném ra một exception,
      // dòng code này sẽ không bao giờ được gọi!
      ((ConfigurableApplicationContext) context).close(); 
  }
  ```

  Như bạn đã chỉ ra, cách này rất "mong manh" (fragile).

* **Cách 2 (Tốt): `try-with-resources` (Từ Java 7+)**
  Vì `ConfigurableApplicationContext` kế thừa (extends) `AutoCloseable`, bạn có thể (và nên) dùng `try-with-resources`. Java sẽ **đảm bảo** `close()` *luôn* được gọi, bất kể `try` thành công hay thất bại (giống như `finally`).

  ```java
  public static void main(String[] args) {
      // Java sẽ tự động gọi .close() khi khối try này kết thúc
      try (ConfigurableApplicationContext context = 
               new AnnotationConfigApplicationContext(AppConfig.class)) {
          
          MyService service = context.getBean(MyService.class);
          service.doWork();
      
      } // context.close() được gọi tự động tại đây
  }
  ```

* **Cách 3 (Tốt nhất/Linh hoạt nhất): `registerShutdownHook()`**

  ```java
  public static void main(String[] args) {
      ConfigurableApplicationContext context = 
          new AnnotationConfigApplicationContext(AppConfig.class);

      // BÁO VỚI JVM: "Này JVM, khi nào bạn chuẩn bị tắt (ví dụ: do Ctrl+C
      // hoặc lệnh 'kill'), hãy nhớ chạy hàm .close() của context này nhé."
      context.registerShutdownHook();

      // Ứng dụng có thể chạy vô tận (ví dụ: một tiến trình lắng nghe)
      // Khi bạn nhấn Ctrl+C, hook sẽ được kích hoạt và đóng context an toàn.
  }
  ```

  Đây là cách "chuyên nghiệp" cho các ứng dụng chạy lâu dài. Nó đăng ký một "cái móc" (hook) với chính **JVM Runtime**. Khi JVM nhận được tín hiệu tắt, nó sẽ gọi hook này, và hook này sẽ gọi `context.close()`.

-----

### 🌐 2. Ứng dụng Web (Truyền thống, Non-Boot)

Như bạn nói, nó **hoàn toàn tự động** thông qua `ContextLoaderListener`.

* **Quá trình khởi động:**

    1.  Tomcat (Servlet Container) khởi động.
    2.  Tomcat đọc `web.xml` (hoặc `WebApplicationInitializer`).
    3.  Tomcat tạo ra `ContextLoaderListener`.
    4.  `ContextLoaderListener` nhận sự kiện "context initialized" và **tạo ra** `ApplicationContext`.

* **Quá trình tắt:**

    1.  Tomcat nhận tín hiệu "stop".
    2.  Tomcat thông báo cho tất cả các Listener, bao gồm `ContextLoaderListener`.
    3.  `ContextLoaderListener` nhận sự kiện "context destroyed" và **tự động gọi `context.close()`**.

Bạn không cần làm gì cả.

-----

### 🤖 3. Ứng dụng Spring Boot

Như bạn nói, nó **hoàn toàn tự động** và là trường hợp "thông minh" nhất.

`SpringApplication.run(...)` làm tất cả cho bạn:

* **Tự động đăng ký Shutdown Hook:** Spring Boot sẽ **tự động gọi `registerShutdownHook()`** cho bạn (bạn có thể tắt nó, nhưng mặc định là bật). Đây là lý do khi bạn chạy một ứng dụng Spring Boot từ console và nhấn `Ctrl+C`, bạn sẽ thấy các log shutdown chạy một cách trật tự (các bean `@PreDestroy` được gọi).
* **Tự động tích hợp với Web Container:** Nếu là ứng dụng web (chạy trên Tomcat nhúng), nó cũng tự động tích hợp vào vòng đời của Tomcat (tương tự như `ContextLoaderListener`) để đảm bảo `context.close()` được gọi khi máy chủ web tắt.

### Tóm tắt

| Loại ứng dụng | Ai chịu trách nhiệm đóng Context? | Cách thực hiện |
| :--- | :--- | :--- |
| **Non-Web** | **Bạn** (Lập trình viên) | Dùng **`try-with-resources`** (cho tác vụ ngắn) hoặc **`registerShutdownHook()`** (cho ứng dụng chạy lâu). |
| **Web (Cũ)** | **Servlet Container** (Tomcat) | Tự động, thông qua `ContextLoaderListener`. |
| **Spring Boot** | **Spring Boot** | **Hoàn toàn tự động** (tự đăng ký shutdown hook và/hoặc listener). |

## ❓ Câu hỏi: Khái niệm cốt lõi của Spring Core?

### 1\. ☕ Dependency Injection (DI) sử dụng Java Configuration

Đây là cách thức **rõ ràng (explicit)**, nơi bạn *chủ động* báo cho Spring biết "hãy tạo bean này" và "kết nối chúng như thế này".

* **`@Configuration`:** Đánh dấu một lớp là một "bản thiết kế". Spring sẽ đọc lớp này để tìm các định nghĩa bean.
* **`@Bean`:** Đánh dấu một phương thức bên trong lớp `@Configuration`. Spring sẽ **thực thi phương thức này** và đăng ký đối tượng được trả về (return) dưới dạng một bean trong `ApplicationContext`.

**Cách DI hoạt động (Bean Inter-dependency):**

Để tiêm (inject) bean A vào bean B, bạn chỉ cần gọi phương thức `@Bean` của A bên trong phương thức `@Bean` của B.

**Ví dụ:**

```java
// Giả sử chúng ta có 2 interface
public interface UserRepository { /* ... */ }
public interface UserService { /* ... */ }

// Và các implementation
public class UserRepositoryImpl implements UserRepository { /* ... */ }
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    // UserService CẦN một UserRepository
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

// Đây là phần Java Config
@Configuration
public class AppConfig {

    // 1. Định nghĩa bean userRepository
    @Bean
    public UserRepository userRepository() {
        return new UserRepositoryImpl();
    }

    // 2. Định nghĩa bean userService
    @Bean
    public UserService userService() {
        // DI xảy ra ở đây:
        // Spring đủ thông minh để biết "userRepository()"
        // là một lời gọi đến bean ở trên, KHÔNG phải là
        // một hàm Java bình thường. Nó sẽ lấy bean singleton
        // đã được tạo ở trên và tiêm vào đây.
        return new UserServiceImpl(userRepository()); 
    }
}
```

* **Ưu điểm:** Rất rõ ràng. Bạn kiểm soát 100% việc tạo đối tượng.
* **Nhược điểm:** Phải viết nhiều code "boilerplate" (code lặp đi lặp lại) cho mỗi bean.

-----

### 2\. 🚀 DI sử dụng Annotations (`@Component`, `@Autowired`)

Đây là cách thức **ẩn (implicit)** và hiện đại. Bạn "nhờ" Spring tự động phát hiện và kết nối các bean.

* **`@Component`:** Đánh dấu một lớp là một bean. Bạn nói với Spring: "Này Spring, tôi không biết *khi nào* hay *làm thế nào*, nhưng tôi muốn *bạn* tự tạo một instance của lớp này và quản lý nó."
* **`@Autowired`:** Đánh dấu một điểm cần tiêm. Bạn nói với Spring: "Tại vị trí này (constructor, field, hoặc setter), hãy tìm một bean trong Context mà *khớp với kiểu dữ liệu này* và tiêm nó vào đây."

**Ví dụ:** (Viết lại ví dụ trên)

```java
// 1. Đánh dấu bean data access
// (@Repository là một dạng đặc biệt của @Component)
@Repository
public class UserRepositoryImpl implements UserRepository { /* ... */ }

// 2. Đánh dấu bean business logic
// (@Service là một dạng đặc biệt của @Component)
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    // 3. DI xảy ra ở đây:
    // Đánh dấu constructor với @Autowired. Spring sẽ tự động
    // tìm một bean implement "UserRepository" (chính là
    // UserRepositoryImpl ở trên) và tiêm vào đây.
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

**Quan trọng:** Cách này chỉ hoạt động khi bạn bật **Component Scanning**.

-----

### 3\. 🔎 Component Scanning, Stereotypes, và Meta-Annotations

#### Component Scanning (`@ComponentScan`)

* **Vấn đề:** Làm sao Spring biết phải tìm các lớp `@Component` ở đâu?
* **Giải pháp:** Bạn phải chỉ cho nó "nơi để quét" (scan).
* **Cách làm:** Bạn đặt annotation `@ComponentScan` trên lớp `@Configuration` của mình.
  ```java
  @Configuration
  @ComponentScan(basePackages = "com.example.myproject") // Quét gói này và các gói con
  public class AppConfig {
      // (Có thể trống, hoặc chứa các @Bean thủ công nếu cần)
  }
  ```
* **Spring Boot:** Trong Spring Boot, annotation `@SpringBootApplication` đã bao gồm `@ComponentScan` cho bạn, nó tự động quét gói chứa lớp `Application` của bạn và tất cả các gói con.

#### Stereotypes

* Bạn đã thấy chúng ở trên: `@Repository`, `@Service`, `@Controller`, `@RestController`.
* Đây là các annotation chuyên biệt hóa của `@Component`. Chúng hoạt động y hệt `@Component` (làm cho lớp trở thành bean) nhưng thêm vào **ý nghĩa ngữ cảnh (semantic meaning)**.
* **`@Repository`:** Dành cho lớp DAO/Repository (Data Access). Nó còn có thêm "quà tặng" là tự động dịch các exception của database (ví dụ: `SQLException`) sang `DataAccessException` của Spring.
* **`@Service`:** Dành cho lớp Business Logic.
* **`@Controller` / `@RestController`:** Dành cho lớp Web Layer, xử lý HTTP request.

#### Meta-Annotations

* Đây là một "annotation của annotation".
* Cách dễ hiểu nhất là nhìn vào code của `@Service`:
  ```java
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @Component  // <--- ĐÂY RỒI!
  public @interface Service {
      String value() default "";
  }
  ```
* Như bạn thấy, **`@Service` là một meta-annotation vì nó được "chú thích" (annotate) bởi `@Component`**.
* Điều này có nghĩa là khi Spring quét, nó thấy `@Service` -\> nó nhìn vào định nghĩa của `@Service` -\> nó thấy `@Component` -\> nó hiểu rằng "À, lớp này cũng là một Component".
* `@SpringBootApplication` là một "siêu meta-annotation" vì nó chứa `@SpringBootConfiguration`, `@ComponentScan`, và `@EnableAutoConfiguration`.

-----

### 4\. 📦 Scopes cho Spring Beans

**Scope** (Phạm vi) định nghĩa **vòng đời** và **tính duy nhất** của một instance bean. Nó trả lời câu hỏi: "Khi tôi yêu cầu một bean, tôi sẽ nhận được *cái gì*?"

Có 5 scope chính bạn cần biết:

1.  **`singleton` (Mặc định):**

    * **Một và chỉ một** instance được tạo ra cho *toàn bộ* `ApplicationContext`.
    * Mỗi khi ai đó `@Autowired` hoặc gọi `context.getBean()`, họ đều nhận được **tham chiếu đến cùng một đối tượng**.
    * Đây là lựa chọn lý tưởng cho các service không trạng thái (stateless).

2.  **`prototype`:**

    * **Một instance mới** được tạo ra *mỗi khi* nó được yêu cầu (mỗi khi `@Autowired` hoặc `context.getBean()`).
    * Spring tạo ra nó, tiêm dependency, và "giao" nó cho bạn. Sau đó, Spring **không quản lý vòng đời** của nó nữa (sẽ *không* gọi `@PreDestroy` hay `destroy-method`). Bạn phải tự dọn dẹp nó.

3.  **`request` (Chỉ dùng trong Web):**

    * Một instance mới được tạo ra cho *mỗi HTTP request*.
    * Hữu ích khi bạn cần một đối tượng để chứa dữ liệu trong suốt quá trình xử lý một request, ví dụ `ShoppingCart` tạm thời.

4.  **`session` (Chỉ dùng trong Web):**

    * Một instance mới được tạo ra cho *mỗi HTTP session* (mỗi người dùng).
    * Hữu ích để lưu trữ thông tin dành riêng cho người dùng, ví dụ như giỏ hàng đã đăng nhập (`UserShoppingCart`).

5.  **`application` (Chỉ dùng trong Web):**

    * Một instance cho mỗi `ServletContext`. Gần như tương tự `singleton` trong bối cảnh web.

### ➡️ What is the default scope? (Phạm vi mặc định là gì?)

Phạm vi mặc định cho tất cả các Spring bean, dù bạn khai báo bằng `@Bean` hay `@Component`, đều là **`singleton`**.


## ❓ Câu hỏi: Are beans lazily or eagerly?

### 1\. 🏁 Trạng thái Mặc định: Eager (Háo hức) vs. Lazy (Lười biếng)

Đây là hành vi mặc định, và nó liên quan trực tiếp đến **Scope** của bean.

* **Các bean `singleton` (mặc định) là EAGER:**

    * **Ý nghĩa:** Ngay khi `ApplicationContext` khởi động, Spring sẽ **ngay lập tức** tạo ra instance của *tất cả* các bean `singleton`.
    * **Tại sao?** Đây là một tính năng quan trọng gọi là **"fail-fast"**. Nếu có bất kỳ lỗi cấu hình nào (ví dụ: một bean `singleton` cần dependency nhưng không tìm thấy), ứng dụng sẽ "chết" ngay lập tức lúc khởi động, thay vì chạy "nửa vời" rồi chết sau đó khi người dùng request.
    * *Ví dụ:* Một nhà hàng chuẩn bị (prep) tất cả nguyên liệu (`singleton` beans) trước khi mở cửa (app khởi động).

* **Các bean `prototype` là LAZY:**

    * **Ý nghĩa:** Spring sẽ **không** tạo ra instance của bean `prototype` khi khởi động. Nó chỉ tạo ra một instance mới *mỗi khi* có một bean khác yêu cầu (qua `context.getBean()` hoặc `@Autowired`).
    * **Tại sao?** Vì bean `prototype` được định nghĩa là "tạo mới mỗi lần gọi", việc tạo chúng trước là vô nghĩa và lãng phí tài nguyên.
    * *Ví dụ:* Một nhà hàng chỉ pha một ly cocktail (`prototype` bean) *khi* khách hàng gọi món.

-----

### 2\. 🎛️ Cách thay đổi Hành vi Mặc định

Bạn có hai cách chính để "bảo" các bean `singleton` (vốn là eager) trở nên lazy.

#### A. Thay đổi Toàn cục (Global) với `@ComponentScan`

Như bạn đã nói, bạn có thể thiết lập `lazyInit = true` trên annotation `@ComponentScan`.

```java
@Configuration
@ComponentScan(basePackages = "com.example", lazyInit = true)
public class AppConfig {
    // ...
}
```

* **Hậu quả:** Giờ đây, *tất cả* các bean được phát hiện bởi cơ chế scan (`@Service`, `@Component`...) sẽ trở thành `lazy` theo mặc định.
* **Cảnh báo:** Điều này thường **không được khuyến khích** vì bạn đã đánh mất lợi ích "fail-fast" (phát hiện lỗi sớm) của Spring.

#### B. Thay đổi Cụ thể (Specific) với `@Lazy`

Đây là cách làm phổ biến và được khuyến khích. Bạn giữ nguyên mặc định (eager) và chỉ đánh dấu `@Lazy` cho các bean *cụ thể* mà bạn muốn khởi tạo lười.

* **Trường hợp 1: Biến một `singleton` (eager) thành `lazy` (Phổ biến)**
  Bạn có thể có một bean rất nặng, tốn tài nguyên (ví dụ: kết nối đến một hệ thống bên ngoài) và không phải lúc nào cũng được sử dụng.

  ```java
  @Service
  @Lazy // Sẽ không được tạo khi khởi động
  public class HeavyReportingService {
      
      public HeavyReportingService() {
          // Giả sử việc này tốn 5 giây
          System.out.println("=== HeavyReportingService ĐANG KHỞI TẠO ===");
      }
  }
  ```

  `HeavyReportingService` sẽ chỉ được tạo khi có một bean khác `@Autowired` nó và gọi một phương thức trên đó.

* **Trường hợp 2: Biến một bean thành `eager` (Ít phổ biến)**
  Như bạn nói, đây là trường hợp để "ghi đè" lên một cấu hình `lazy` toàn cục.

  ```java
  @Configuration
  @ComponentScan(basePackages = "com.example", lazyInit = true) // Mọi thứ đều lazy
  public class AppConfig { }

  @Service
  @Lazy(false) // BẮT BUỘC bean này phải là EAGER
  public class CriticalCacheInitializer {
      public CriticalCacheInitializer() {
          // Cần chạy ngay lúc khởi động để làm nóng cache
          System.out.println("=== Cache đã được làm nóng ===");
      }
  }
  ```

-----

### 3\. 📍 Phạm vi áp dụng của `@Lazy`

Bạn đã liệt kê chính xác các nơi có thể đặt `@Lazy`:

1.  **Trên lớp `@Component` (hoặc `@Service`, `@Repository`...):**
    ```java
    @Lazy
    @Service
    public class MyLazyService {}
    ```
2.  **Trên phương thức `@Bean`:**
    ```java
    @Configuration
    public class Config {
        @Lazy
        @Bean
        public MyBean myBean() {
            return new MyBean();
        }
    }
    ```
3.  **Trên lớp `@Configuration`:**
    ```java
    @Lazy
    @Configuration
    public class LazyConfig {
        @Bean // Bean này sẽ lazy
        public BeanA beanA() {}
        
        @Bean // Bean này cũng sẽ lazy
        public BeanB beanB() {}
    }
    ```
    *Lưu ý:* Khi đặt `@Lazy` trên lớp `@Configuration`, nó sẽ ảnh hưởng đến *tất cả* các phương thức `@Bean` bên trong lớp đó.

-----

### 4\. ⚠️ Kịch bản "Bẫy" (Singleton phụ thuộc Prototype)

Phân tích của bạn ở đây rất sắc sảo và là một điểm dễ bị hỏi trong kỳ thi.

> "nếu singleton bean có dependency trên Prototype bean, thì Prototype bean instance sẽ được tạo Eagerly"

**Chính xác.** Hãy làm rõ điều này:

* Bean `SingletonA` (mặc định là `eager`).
* Bean `PrototypeB` (mặc định là `lazy`).
* `SingletonA` `@Autowired` một `PrototypeB` trong constructor của nó.

**Dòng sự kiện:**

1.  Context khởi động.
2.  Spring thấy `SingletonA` là `eager`, nên nó cố gắng tạo `SingletonA` **ngay lập tức**.
3.  Để tạo `SingletonA`, Spring thấy nó cần một `PrototypeB` (do constructor).
4.  Mặc dù `PrototypeB` có scope là `prototype`, Spring **bắt buộc** phải tạo *một instance* của `PrototypeB` **ngay lập tức** để hoàn thành việc tiêm dependency cho `SingletonA`.

**Kết quả:** Một instance của `PrototypeB` đã được tạo `eager` (dù nó là lazy) chỉ vì nó là dependency của một bean `eager`.

## ❓ Câu hỏi: Phân biệt `PropertySource` (khái niệm) và `@PropertySource` (cách triển khai).

### 1\. 🗂️ PropertySource là gì? (Một "Nguồn" thuộc tính)

Câu trả lời của bạn là hoàn hảo: Nó là một **"lớp trừu tượng" (abstraction)** cho bất kỳ nguồn key-value nào.

Để hiểu rõ hơn, hãy nghĩ về đối tượng `Environment` của Spring. `Environment` là một đối tượng trung tâm, giống như một "tấm bảng" lớn, chứa *tất cả* các thuộc tính (properties) mà ứng dụng của bạn có thể cần.

Nhưng `Environment` lấy các thuộc tính này từ đâu? Nó lấy từ nhiều `PropertySource` khác nhau.

**Thứ tự ưu tiên (Quan trọng):**
`Environment` không chỉ lưu trữ các `PropertySource` này, nó còn **sắp xếp chúng theo thứ tự ưu tiên**. Đây là một khái niệm cốt lõi. Một `PropertySource` có độ ưu tiên cao hơn (ví dụ: Biến môi trường) sẽ *ghi đè* (override) một `PropertySource` có độ ưu tiên thấp hơn (ví dụ: file `application.properties`).

Đây là thứ tự ưu tiên (đơn giản hóa) từ cao đến thấp:

1.  **System Properties** (Thuộc tính JVM, ví dụ: `-Ddb.host=jvm.host`)
2.  **OS Environment Variables** (Biến môi trường hệ điều hành, ví dụ: `export DB_HOST=os.host`)
3.  **(Spring Boot) `application-{profile}.properties`** (Ví dụ: `application-prod.properties`)
4.  **(Spring Boot) `application.properties`** (Giá trị mặc định của bạn, ví dụ: `db.host=default.host`)
5.  **Các file được tải bởi `@PropertySource`** (Thường có độ ưu tiên thấp)

Điều này giải thích *tại sao* bạn có thể đặt `db.host=default.host` trong code của mình, nhưng khi deploy lên production, bạn chỉ cần set một biến môi trường `DB_HOST` là giá trị `default.host` sẽ tự động bị ghi đè.

-----

### 2\. 🎯 Cách sử dụng `@PropertySource`

Cách dùng của bạn là hoàn toàn chính xác.

`@PropertySource` là một annotation bạn đặt trên một lớp `@Configuration` để nói với Spring: "Này, ngoài những nguồn mặc định, hãy **đọc thêm file này** và **thêm nó vào `Environment`**."

**Ví dụ của bạn:**

```java
@Configuration
@PropertySources({
        // Tải file từ một đường dẫn tuyệt đối (đường dẫn được lấy từ một biến khác)
        @PropertySource("file:${app-home}/app-db.properties"),

        // Tải file từ trong classpath (ví dụ: src/main/resources)
        @PropertySource("classpath:/app-defaults.properties")
})
public class AppConfig {
    // ...
}
```

**Lưu ý quan trọng (Spring Boot vs. Spring):**

* Trong một ứng dụng **Spring Boot**, các file `application.properties` và `application.yml` được tải **hoàn toàn tự động**.
* Bạn **chỉ** cần dùng `@PropertySource` khi bạn muốn tải một file *không* theo chuẩn (non-standard), ví dụ: `legacy-config.properties` hoặc một file config nằm ở một vị trí đặc biệt.

-----

### 3\. 🔩 Cách truy cập các thuộc tính

Bạn đã chỉ ra cách phổ biến nhất là dùng `@Value`. Có hai cách chính để lấy các giá trị này ra khỏi `Environment`:

#### A. Dùng `@Value` (Cách của bạn)

Hoàn hảo cho việc tiêm (inject) một vài giá trị cụ thể. Spring sẽ tự động tìm key (`db.host`) trong `Environment` và tiêm giá trị vào trường đó.

```java
@Service
public class MyDbService {

    @Value("${db.host}")
    private String dbHost;

    @Value("${db.port:3306}") // Cung cấp giá trị mặc định
    private int dbPort;

    public void connect() {
        // Dùng dbHost và dbPort...
    }
}
```

#### B. Dùng `Environment` (Cách linh hoạt)

Bạn cũng có thể tiêm (inject) chính đối tượng `Environment` và chủ động truy vấn nó.

```java
@Service
public class MyDbService {

    @Autowired
    private Environment env;

    public void connect() {
        // Tự lấy giá trị
        String host = env.getProperty("db.host");

        // Lấy giá trị với kiểu cụ thể và giá trị mặc định
        int port = env.getProperty("db.port", Integer.class, 3306);
        
        if (host != null) {
            // TODO
        }
    }
}
```

Bạn đã liên kết rất tốt các khái niệm `PropertySource` (nguồn), `@PropertySource` (cách tải), và `@Value` (cách dùng).

## ❓ Câu hỏi: What is a BeanFactoryPostProcessor?

### 1\. 🏭 `BeanFactoryPostProcessor` (BFPP) là gì và dùng để làm gì?

Như bạn nói, đây là một interface cho phép bạn **can thiệp và sửa đổi Metadata (siêu dữ liệu) của bean**.

Cách dễ hiểu nhất là dùng lại **ví dụ nhà máy** của chúng ta:

* **`BeanDefinition`:** Là các **bản thiết kế** cho mỗi bean (mỗi "robot").
* **`Bean Instance`:** Là các "robot" đã được lắp ráp hoàn chỉnh.
* **`BeanFactoryPostProcessor` (BFPP):** Là một **Kỹ sư trưởng kiểm duyệt**.

**Công việc của BFPP:**
Sau khi nhà máy đã *thu thập tất cả các bản thiết kế* (`BeanDefinition`), nhưng *trước khi* bắt đầu *sản xuất bất kỳ robot nào* (`Bean Instance`), Kỹ sư trưởng (BFPP) sẽ đi một vòng, xem xét **toàn bộ các bản thiết kế** và có quyền **sửa đổi chúng**.

Ví dụ, Kỹ sư trưởng có thể nói:

* "Bản thiết kế cho `DataSource` đang ghi là 'kết nối 10 pool', hãy sửa thành 'kết nối 20 pool'." (Thay đổi một property).
* "Bản thiết kế cho `ReportService` đang dùng class `LegacyReport`, hãy đổi sang dùng class `NewReport`." (Thay đổi class của bean).
* "Hãy đăng ký thêm một bản thiết kế mới cho `AuditService` mà chúng ta quên." (Thêm một `BeanDefinition` mới).

**Điểm mấu chốt:** BFPP làm việc với **Bản thiết kế** (`BeanDefinition`), KHÔNG làm việc với **Sản phẩm thật** (`Bean Instance`). (Việc can thiệp vào sản phẩm thật là nhiệm vụ của `BeanPostProcessor`).

-----

### 2\. ⏰ Khi nào nó được gọi?

Bạn đã trả lời chính xác: **Sau khi** tất cả `BeanDefinition` đã được tải (đọc từ file config, scan component...), nhưng **trước khi** bất kỳ bean (instance) nào được khởi tạo.

Đây là vị trí hoàn hảo để thực hiện các thay đổi *trước khi* quá trình "lắp ráp" (Dependency Injection, `@PostConstruct`...) bắt đầu.

-----

### 3\. 🥚 Vấn đề "Con gà & Quả trứng": Tại sao dùng `static @Bean`?

Đây là phần phức tạp nhưng rất quan trọng, và bạn đã giải thích đúng.

**Vấn đề (Chicken & Egg Problem):**

1.  `BeanFactoryPostProcessor` (BFPP) là một bean rất đặc biệt, nó phải được tạo và chạy **trước tiên**.
2.  Bản thân `BFPP` cũng là một bean, và nó thường được định nghĩa bên trong một lớp `@Configuration` (ví dụ: `AppConfig`).
3.  Lớp `@Configuration` (`AppConfig`) đó **cũng là một bean**\!
4.  **Vấn đề là:** Để tạo ra `BFPP` (bằng cách gọi phương thức `@Bean` của nó), Spring *đầu tiên* phải tạo ra instance của `AppConfig`.
5.  Nhưng việc tạo instance `AppConfig` có thể kích hoạt việc tạo các bean khác (ví dụ: nếu `AppConfig` có `@Autowired` một bean khác), điều này vi phạm quy tắc "BFPP phải chạy đầu tiên".

**Giải pháp: `static @Bean`**

Bằng cách đánh dấu phương thức `@Bean` của BFPP là `static`:

```java
@Configuration
public class AppConfig {

    // ... các @Bean khác ...

    @Bean
    public static CustomBeanFactoryPostProcessor customBeanFactoryPostProcessor() {
        // Spring có thể gọi hàm này MÀ KHÔNG CẦN
        // tạo instance của AppConfig trước
        return new CustomBeanFactoryPostProcessor();
    }
}
```

* Bạn đang nói với Spring: "Này, phương thức `customBeanFactoryPostProcessor` này là `static`. Bạn có thể gọi nó trực tiếp (`AppConfig.customBeanFactoryPostProcessor()`) **mà không cần tạo instance của `AppConfig`**."
* Điều này cho phép Spring tạo `BFPP` rất sớm trong vòng đời, phá vỡ vòng lặp "con gà quả trứng", và đảm bảo `BFPP` có thể chạy trước khi bất kỳ bean nào khác (bao gồm cả `AppConfig`) được khởi tạo hoàn chỉnh.

-----

### 4\. 📝 `PropertySourcesPlaceholderConfigurer` (PSPC) là gì?

Đây là ví dụ **kinh điển và phổ biến nhất** của một `BeanFactoryPostProcessor`.

* **Nhiệm vụ:** Như bạn nói, nó chịu trách nhiệm **tìm và thay thế** các placeholder (như `${...}`) trong các `BeanDefinition`.

* **Cách hoạt động:**

    1.  Spring tải `BeanDefinition` của bạn. Nó thấy:
        > `BeanDefinition` của `MyDbService` có một trường cần tiêm giá trị `@Value("${db.host}")`.
    2.  Spring khởi động `PSPC` (vì nó là một BFPP).
    3.  `PSPC` quét tất cả các `BeanDefinition`. Nó thấy cái placeholder `"${db.host}"`.
    4.  Nó tra cứu (lookup) key `"db.host"` trong `Environment` (từ file `application.properties`) và tìm thấy giá trị, ví dụ: `"localhost"`.
    5.  Nó **sửa đổi `BeanDefinition`**: thay thế `"${db.host}"` bằng giá trị thật là `"localhost"`.
    6.  Quá trình BFPP kết thúc.

* **Kết quả:** Vài bước sau, khi Spring bắt đầu tạo instance `MyDbService`, `BeanDefinition` đã được sửa. Spring chỉ đơn giản là tiêm (inject) chuỗi `"localhost"` vào trường đó. Nó không còn biết gì về `${db.host}` nữa.

**Lưu ý:** Trong **Spring Boot**, `PropertySourcesPlaceholderConfigurer` được **tự động cấu hình** (auto-configured) cho bạn. Bạn không cần phải khai báo `@Bean` cho nó. Nhưng trong một ứng dụng Spring "truyền thống", bạn phải tự làm việc này.

## ❓ Câu hỏi:  `BeanFactoryPostProcessor` và `BeanPostProcessor` khác nhau như thế nào?

### 1. 🏭 BeanPostProcessor (BPP) vs. BeanFactoryPostProcessor (BFPP)

Đây là sự khác biệt **quan trọng nhất** cần nhớ cho kỳ thi.

#### 💡 Ví dụ so sánh: Nhà máy Robot

Hãy tiếp tục với ví dụ nhà máy sản xuất robot của chúng ta:
* **`BeanDefinition`:** Là **Bản thiết kế** kỹ thuật của robot.
* **`Bean Instance`:** Là con **Robot thật** (sản phẩm) đã được lắp ráp.

**A. `BeanFactoryPostProcessor` (Kỹ sư trưởng):**
* **Họ làm gì?** Họ xem xét và sửa đổi **Bản thiết kế** (`BeanDefinition`).
* **Khi nào?** *Sau khi* tất cả bản thiết kế đã được thu thập, nhưng *trước khi* bất kỳ robot nào được sản xuất.
* **Ví dụ:** "Hãy sửa tất cả các bản thiết kế đang dùng `${db.url}` và thay thế nó bằng giá trị thật `jdbc:mysql://...`". (Đây chính là `PropertySourcesPlaceholderConfigurer`).

**B. `BeanPostProcessor` (Kỹ thuật viên QC):**
* **Họ làm gì?** Họ đứng trên dây chuyền lắp ráp và can thiệp trực tiếp vào con **Robot thật** (`Bean Instance`). Họ không sửa bản thiết kế.
* **Khi nào?** *Trong khi* robot đang được lắp ráp, tại 2 thời điểm cụ thể:

    1.  **`postProcessBeforeInitialization` (Trạm QC 1):**
        * Robot vừa được lắp ráp xong (đã gọi `new` và tiêm dependency xong).
        * QC kiểm tra nó *trước khi* robot tự bật nguồn (trước `@PostConstruct`).
    2.  **`postProcessAfterInitialization` (Trạm QC 2):**
        * Robot đã tự bật nguồn xong (đã gọi `@PostConstruct` và các hàm `init` xong).
        * QC kiểm tra nó lần cuối *trước khi* xuất xưởng (đưa vào `ApplicationContext`).

#### "Phép thuật" của BeanPostProcessor

Các BPP chính là thứ thực thi hầu hết các "phép thuật" của Spring:

* **`CommonAnnotationBeanPostProcessor`:** Là Kỹ thuật viên QC đi tìm nút `@PostConstruct` và `@PreDestroy` trên robot và *nhấn* chúng vào đúng thời điểm.
* **`AutowiredAnnotationBeanPostProcessor`:** Là Kỹ thuật viên QC chịu trách nhiệm tìm các cổng `@Autowired` và cắm dây (tiêm dependency) vào.
* **`AnnotationAwareAspectJAutoProxyCreator`:** Đây là BPP **quan trọng nhất** của AOP. Nó đứng ở **Trạm QC 2**. Nó thấy robot `UserService` này có đánh dấu `@Transactional`. Nó sẽ:
    1.  Không xuất xưởng robot thật.
    2.  Tạo ra một con **Robot đóng thế (Proxy)**.
    3.  Giấu con robot thật *bên trong* robot Proxy.
    4.  Xuất xưởng **robot Proxy** cho `ApplicationContext`.
        *Đây là lý do tại sao khi bạn gọi `userService.save()`, thực ra bạn đang gọi Proxy, và Proxy sẽ lo việc `BEGIN TRANSACTION`, `gọi userService.save() thật`, `COMMIT`, v.v.*

#### Về `static @Bean`

Lý do bạn nên khai báo BPP là `static @Bean` cũng giống hệt như với BFPP: Đó là vấn đề "con gà và quả trứng". Spring cần tạo Kỹ thuật viên QC (BPP) *trước khi* nó bắt đầu tạo các bean khác (robot), vì vậy việc dùng `static` cho phép Spring tạo BPP mà không cần phải khởi tạo toàn bộ lớp `@Configuration` chứa nó trước.

---

### 2. 🚀 Phương thức Khởi tạo (Initialization Method)

Như bạn nói, đây là logic cần chạy *sau khi* bean đã được tiêm (inject) đầy đủ dependency.

* **Ví dụ:** "Tôi là `DataSource`, tôi vừa được tiêm `db.url`, `user`, `password`. Bây giờ, tôi cần dùng chúng để **khởi tạo một connection pool**."

Bạn đã liệt kê đúng 3 cách và **thứ tự** chính xác mà Spring gọi chúng:

1.  **`@PostConstruct`** (Từ JSR-250)
2.  **`InitializingBean::afterPropertiesSet`** (Từ interface `InitializingBean` của Spring)
3.  **`@Bean(initMethod = "...")`** (Định nghĩa trong `@Configuration`)

**Khuyến nghị:** Luôn ưu tiên dùng **`@PostConstruct`**. Nó là một chuẩn Java (JSR-250), không làm code của bạn bị "dính" vào interface của Spring (như `InitializingBean`), và rõ ràng hơn là `initMethod`.

---

### 3. 🛑 Phương thức Hủy (Destroy Method)

Đây là logic "dọn dẹp" chạy khi `ApplicationContext` bị đóng (`context.close()`).

* **Ví dụ:** "Tôi là `DataSource`, ứng dụng đang tắt. Tôi cần **đóng tất cả các kết nối (connections)** trong pool của mình một cách an toàn."

Bạn đã liệt kê đúng 3 cách và **thứ tự** chính xác:

1.  **`@PreDestroy`** (Từ JSR-250)
2.  **`DisposableBean::destroy`** (Từ interface `DisposableBean` của Spring)
3.  **`@Bean(destroyMethod = "...")`** (Định nghĩa trong `@Configuration`)

**Khuyến nghị:** Luôn ưu tiên dùng **`@PreDestroy`** vì lý do tương tự như `@PostConstruct`.

**Lưu ý quan trọng cho kỳ thi:** Các phương thức `destroy` **CHỈ** được gọi tự động cho các bean `singleton`. Spring không quản lý toàn bộ vòng đời của bean `prototype`, vì vậy Spring **KHÔNG** gọi phương thức `destroy` của chúng. Bạn (lập trình viên) phải tự dọn dẹp bean `prototype` nếu cần.

---

### 4. ⚡ Bật JSR-250 (`@PostConstruct` và `@PreDestroy`)

Câu trả lời của bạn là hoàn hảo: `AnnotationConfigApplicationContext` (và Spring Boot) sẽ **tự động đăng ký** `CommonAnnotationBeanPostProcessor`.

Chính `BeanPostProcessor` này sẽ quét các bean để tìm và thực thi các annotation `@PostConstruct` và `@PreDestroy`.

---

### 5. 📜 Tóm tắt Vòng đời (Init & Destroy)

Danh sách của bạn là **hoàn hảo**, đây chính là thứ tự bạn cần nhớ cho kỳ thi. Nó kết hợp tất cả các khái niệm trên:

**Giai đoạn 1: Tạo Bean (Lắp ráp)**
1.  Gọi `new` để tạo Instance.
2.  Tiêm (inject) Properties và Dependencies (`@Autowired`).
3.  **`BeanPostProcessor::postProcessBeforeInitialization`** (Trạm QC 1).

**Giai đoạn 2: Khởi tạo Bean (Bật nguồn)**
4.  **`@PostConstruct`**
5.  **`InitializingBean::afterPropertiesSet`**
6.  **`@Bean(initMethod)`**

**Giai đoạn 3: Sẵn sàng (Xuất xưởng)**
7.  **`BeanPostProcessor::postProcessAfterInitialization`** (Trạm QC 2 - AOP Proxy được tạo ở đây).
8.  *Bean sẵn sàng để sử dụng.*

**Giai đoạn 4: Hủy Bean (Tắt máy)**
(Khi `context.close()` được gọi và bean là `singleton`)
1.  **`@PreDestroy`**
2.  **`DisposableBean::destroy`**
3.  **`@Bean(destroyMethod)`**


## ❓ Câu hỏi: Component scanning là gì?

### 1\. 🎯 Component Scanning là gì?

> Bạn định nghĩa: "Là quá trình Spring quét (scan) classpath để tìm các lớp được chú thích (annotated) bằng các stereotype... và dựa vào đó tạo ra các bean definition."

Đây là định nghĩa hoàn hảo.

Hãy nghĩ đơn giản thế này:

* **Cách cũ (Dùng XML hoặc `@Bean` thủ công):** Bạn phải "chỉ mặt" từng bean cho Spring. Giống như bạn đưa cho Spring một danh sách khách mời (guest list) được viết tay cẩn thận: "Hãy tạo `UserServiceImpl`, rồi tạo `UserRepositoryImpl`, rồi tiêm (inject) `UserRepositoryImpl` vào `UserServiceImpl`."
* **Cách mới (Dùng Component Scan):** Bạn chỉ cần "dán nhãn" (sticker) lên các lớp của mình (dùng `@Component`, `@Service`...). Sau đó, bạn đưa cho Spring một cái "máy quét" (`@ComponentScan`) và nói: "Hãy đi quét toàn bộ gói `com.example` này. Cứ thấy lớp nào có dán nhãn thì tự động mang về, tạo ra và coi nó là một bean."

Đây chính là cơ chế đằng sau **Dependency Injection tự động (`@Autowired`)**. Spring chỉ có thể `@Autowired` một `UserRepository` vào `UserService` là vì `@ComponentScan` đã tìm thấy lớp `UserRepositoryImpl` (được đánh dấu `@Repository`) và đăng ký nó làm bean trước đó.

-----

### 2\. 📖 Cách sử dụng `@ComponentScan`

Bạn đã cung cấp hai ví dụ rất tốt:

#### A. Cách đơn giản (Không có tham số)

```java
@Configuration
@ComponentScan
// Tương đương với @ComponentScan(basePackages = "com.example.config")
public class ApplicationConfiguration {
    // ...
}

// Lớp này nằm trong gói "com.example.config"
```

* **Hành vi:** Khi bạn dùng `@ComponentScan` mà không chỉ định `basePackages`, Spring sẽ ngầm định (default) lấy **gói (package) của chính lớp `@Configuration` đó** (ở đây là `com.example.config`) làm gói cơ sở để bắt đầu quét.
* **Quy tắc:** Nó sẽ quét gói đó **và tất cả các gói con (sub-packages)** bên trong nó.
* **Lưu ý:** Đây chính là cách `@SpringBootApplication` hoạt động. Nó ngầm định quét mọi thứ bên dưới gói chứa lớp Application chính của bạn.

#### B. Cách nâng cao (Dùng Filters)

Đây là cách bạn muốn kiểm soát chính xác những gì được quét, rất hữu ích trong các bài test hoặc các cấu hình phức tạp.

```java
@Configuration
@ComponentScan(
    // 1. CHỈ quét trong gói này (và các gói con)
    basePackages = "org.spring.cert.beans",
    
    // 2. BAO GỒM (Include):
    includeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX, // Lọc theo Biểu thức chính quy (Regex)
        pattern = ".*Bean"       // Chỉ lấy những lớp có tên kết thúc bằng "Bean"
    ),
    
    // 3. LOẠI TRỪ (Exclude):
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX, // Lọc theo Regex
        pattern = ".*(Controller|Service).*" // Bỏ qua bất kỳ lớp nào có tên chứa "Controller" hoặc "Service"
    )
)
public class ApplicationConfiguration {
}
```

* **`basePackages`:** Chỉ định rõ ràng nơi bắt đầu quét.
* **`includeFilters`:** Hoạt động như một "danh sách trắng". *Lưu ý quan trọng:* Khi bạn dùng `includeFilters`, bạn cũng thường phải tắt bộ lọc `@Component` mặc định bằng cách thêm `useDefaultFilters = false`, nếu không Spring sẽ lấy *cả* các `@Component` VÀ các filter của bạn.
* **`excludeFilters`:** Hoạt động như một "danh sách đen". Cái này rất hữu dụng. Ví dụ, trong một bài `@DataJpaTest`, Spring Boot tự động dùng `excludeFilters` để *loại bỏ* các `@Controller` và `@Service`, chỉ giữ lại các `@Repository`.

## ❓ Câu hỏi: Hành vi của @Autowired với field injection, constructor injection và method injection là gì?

`@Autowired` là một annotation báo cho Spring biết rằng "hãy tự động tìm một bean trong `ApplicationContext` và tiêm (inject) nó vào vị trí này".

Quá trình này được xử lý bởi một `BeanPostProcessor` đặc biệt tên là `AutowiredAnnotationBeanPostProcessor`.

-----

### 1\. 🎯 Quy trình "Tìm kiếm & Gỡ rối" (Resolution Logic)

Đây là phần **quan trọng nhất** bạn cần nhớ cho kỳ thi. Khi Spring thấy `@Autowired`, nó sẽ cố gắng tìm một bean phù hợp (chủ yếu dựa trên **kiểu dữ liệu**) theo các bước sau:

1.  **Tìm theo Kiểu (Type):** Spring quét `ApplicationContext` để tìm tất cả các bean khớp với *kiểu dữ liệu* được yêu cầu (ví dụ: tìm tất cả các bean implement `UserRepository`).

2.  **Phân tích kết quả:**

    * **Nếu tìm thấy 1 bean duy nhất:** Tuyệt vời\! Spring sẽ tiêm (inject) bean đó. (Hoàn thành).
    * **Nếu tìm thấy 0 bean:**
        * Nếu `@Autowired(required = true)` (đây là mặc định): Spring sẽ ném ra lỗi `NoSuchBeanDefinitionException` và ứng dụng sẽ **không thể khởi động**.
        * Nếu `@Autowired(required = false)`: Spring sẽ bỏ qua và không tiêm gì cả. Đối tượng sẽ là `null`.
    * **Nếu tìm thấy \> 1 bean (Nhiều bean):** Đây là trường hợp `NoUniqueBeanDefinitionException`. Spring không biết chọn bean nào, vì vậy nó sẽ cố gắng "gỡ rối" (tie-break) bằng các quy tắc sau (theo thứ tự):
        * **a. Kiểm tra `@Primary`:** Spring sẽ xem trong số các bean tìm được, có bean nào được đánh dấu là `@Primary` không. Nếu có **một** bean `@Primary`, nó sẽ được chọn.
        * **b. Kiểm tra `@Qualifier`:** Nếu không có `@Primary` (hoặc có nhiều `@Primary`), Spring sẽ tìm annotation `@Qualifier("ten_bean")` tại điểm tiêm (ví dụ: trên field). Nếu có, nó sẽ tìm bean có **tên (ID)** khớp chính xác với tên trong `@Qualifier`.
        * **c. Kiểm tra Tên biến (Variable Name):** Nếu không có `@Qualifier`, Spring sẽ dùng đến "phương án cuối": nó lấy **tên của biến** (variable name) hoặc tên tham số (parameter name) và tìm bean có **tên (ID)** khớp với tên đó.

3.  **Kết luận:** Nếu sau tất cả các bước gỡ rối mà vẫn còn nhiều hơn một bean phù hợp, Spring sẽ "bó tay" và ném ra lỗi `NoUniqueBeanDefinitionException`.

-----

### 2\. Các kiểu "Tiêm" (Injection Types)

#### 🥇 Constructor Injection (Cách được khuyến nghị)

Đây là cách tốt nhất và được đội ngũ Spring khuyên dùng.

* **Cách làm:** Đặt `@Autowired` trên constructor của lớp.
* **Ví dụ:**
  ```java
  @Service
  public class RecordService {
      private final DbRecordReader recordReader;
      private final DbRecordProcessor dbRecordProcessor;

      // Spring sẽ tìm 1 bean DbRecordReader và 1 bean DbRecordProcessor
      // để "tiêm" vào đây khi tạo RecordService
      @Autowired
      public RecordService(DbRecordReader recordReader, DbRecordProcessor dbRecordProcessor) {
          this.recordReader = recordReader;
          this.dbRecordProcessor = dbRecordProcessor;
      }
  }
  ```
* **Quy tắc VÀNG:** Nếu lớp của bạn **chỉ có MỘT constructor**, bạn **KHÔNG CẦN** dùng `@Autowired`. Spring sẽ tự động sử dụng constructor đó.
* **Quy tắc 2:** Nếu lớp có **nhiều hơn một constructor**, bạn **BẮT BUỘC** phải chỉ định `@Autowired` trên *một* (và chỉ một) constructor để Spring biết phải dùng cái nào.
* **Visibility:** Constructor có thể là `public`, `protected`, `private`, hoặc package-private.
* **Tính bắt buộc (Required):**
    * Mặc định, tất cả các tham số (ví dụ: `dbRecordReader`) đều là `required = true`.
    * Để coi một tham số là "tùy chọn" (optional), bạn có thể bọc nó trong `Optional<DbRecordReader>` hoặc dùng `@Nullable`.

#### 🥈 Method Injection (Setter Injection)

Cách này thường dùng cho các dependency "tùy chọn" (optional) có thể được thay đổi sau khi bean đã được tạo.

* **Cách làm:** Đặt `@Autowired` trên một phương thức (thường là setter).
* **Ví dụ:**
  ```java
  @Service
  public class RecordService {
      private DbRecordReader recordReader;

      @Autowired
      public void setRecordReader(DbRecordReader dbRecordReader) {
          this.recordReader = dbRecordReader;
      }
  }
  ```
* **Visibility:** Phương thức có thể có bất kỳ visibility nào.
* **Nhiều tham số:** Phương thức có thể có nhiều tham số, và Spring sẽ cố gắng tìm bean cho tất cả chúng.
* **Tính bắt buộc (Required):**
    * `@Autowired(required = true)` trên phương thức (mặc định): Spring *phải* tìm thấy bean cho *tất cả* các tham số của phương thức. Nếu thất bại, lỗi sẽ được ném ra.
    * `@Autowired(required = false)` trên phương thức: Đây là một điểm tinh tế. Nó có nghĩa là: "Này Spring, hãy cố gắng gọi phương thức này. Nhưng nếu bạn không tìm thấy bean cho *bất kỳ* tham số nào của nó, **hãy bỏ qua, đừng gọi phương thức này**."
    * Bạn cũng có thể làm cho các *tham số riêng lẻ* trở nên optional (bằng `Optional<T>`, `@Nullable`) trong khi phương thức vẫn `required=true`.

#### 🥉 Field Injection (Không khuyến nghị)

Cách này rất tiện lợi nhưng bị coi là "code lười" (poor practice) vì nó có nhiều nhược điểm.

* **Cách làm:** Đặt `@Autowired` trực tiếp trên trường (field).
* **Ví dụ:**
  ```java
  @Service
  public class RecordService {
      @Autowired
      private DbRecordReader recordReader;
      
      @Autowired
      private DbRecordProcessor dbRecordProcessor;
  }
  ```
* **Visibility:** Trường có thể có bất kỳ visibility nào (`private`, `protected`...). Spring sẽ dùng **reflection** để gán giá trị cho nó.
* **Vòng đời:** Việc tiêm (injection) xảy ra *sau khi* constructor được gọi, nhưng *trước khi* các phương thức `init` (như `@PostConstruct`) được gọi.
* **Nhược điểm:**
    1.  **Khó Unit Test:** Rất khó để gán một `Mock` (giả) cho `recordReader` trong Unit Test. Bạn phải dùng reflection.
    2.  **Vi phạm đóng gói (Encapsulation):** Lớp "giấu" các dependency của nó thay vì công khai chúng qua constructor.
    3.  **Rủi ro `NullPointerException`:** Có thể tạo một instance của lớp (bằng `new`) mà "quên" tiêm dependency, dẫn đến lỗi.

-----

### 3\. 🎁 Trường hợp đặc biệt: Tiêm Collections và Maps

Đây là một tính năng rất mạnh của `@Autowired` mà bạn đã đề cập:

* **`List` hoặc `Collection`:**

  ```java
  @Autowired
  private List<Plugin> allPlugins;
  ```

  Spring sẽ tìm **tất cả** các bean implement interface `Plugin` và tiêm chúng vào một `List`.

    * **Thứ tự:** Bạn có thể kiểm soát thứ tự của các bean trong `List` bằng cách dùng annotation `@Order` hoặc implement interface `Ordered`.

* **`Map<String, T>`:**

  ```java
  @Autowired
  private Map<String, Plugin> pluginMap;
  ```

  Spring sẽ tiêm tất cả các bean `Plugin`. **Key** của `Map` sẽ là **tên (ID) của bean** (ví dụ: `"emailPlugin"`, `"smsPlugin"`) và **Value** là chính instance của bean đó.

## ❓ Câu hỏi:

# Proxy object là gì, hai loại Spring có thể tạo là gì? Hạn chế của chúng? Ưu và nhược điểm của proxy?

### 1\. 🛡️ Proxy Object là gì?

Định nghĩa của bạn là hoàn hảo. Một Proxy (đối tượng ủy quyền) là một đối tượng "bao bọc" (wrapper) một đối tượng khác (đối tượng *target* - mục tiêu).

Hãy nghĩ về Proxy như một **nhân viên bảo vệ (doorman)** đứng trước văn phòng của một giám đốc (đối tượng *target*).

* **Đối tượng Target (Giám đốc):** `UserServiceImpl` - Chứa logic nghiệp vụ thực sự (ví dụ: `saveUser()`).
* **Đối tượng Proxy (Bảo vệ):** `UserServiceProxy` - Được Spring tạo ra.

Khi bạn (ví dụ: `UserController`) muốn gọi `userService.saveUser()`:

1.  Spring **không** đưa cho bạn `UserServiceImpl` thật. Nó đưa cho bạn `UserServiceProxy`.
2.  Bạn gọi `proxy.saveUser()`.
3.  **Bảo vệ (Proxy)** nhận lệnh và thực hiện "logic bổ sung" *trước khi* vào gặp giám đốc:
    * *Kiểm tra an ninh:* `@PreAuthorize("hasRole('ADMIN')")`
    * *Mở cửa (bắt đầu giao dịch):* `@Transactional`
4.  Nếu mọi thứ ổn, **Bảo vệ (Proxy)** mới *mở cửa* và gọi `target.saveUser()` (giám đốc làm việc).
5.  Sau khi giám đốc làm xong, **Bảo vệ (Proxy)** thực hiện "logic bổ sung" *sau khi* bạn rời đi:
    * *Đóng cửa (commit/rollback giao dịch):* `@Transactional`
    * *Ghi lại nhật ký:* `@Audit`

Đây chính là sức mạnh của Lập trình hướng khía cạnh (AOP). Giám đốc (code nghiệp vụ) chỉ tập trung vào nghiệp vụ, không cần biết gì về bảo vệ, giao dịch hay log.

-----

### 2\. ✌️ Hai loại Proxy Spring sử dụng

Spring sử dụng hai kỹ thuật khác nhau để tạo ra "nhân viên bảo vệ" này, tùy thuộc vào đối tượng *target* của bạn.

#### A. JDK Dynamic Proxy (Mặc định)

* **Khi nào dùng:** Như bạn nói, khi lớp *target* của bạn **có implement một interface** (ví dụ: `UserServiceImpl` implement `UserService`).
* **Cách hoạt động:** Spring sử dụng một tính năng chuẩn của Java (`java.lang.reflect.Proxy`) để tạo ra một lớp Proxy *mới* lúc runtime. Lớp Proxy này cũng **implement cùng interface** (`UserService`).
* **Ví dụ:**
    * `Client` (Controller) code dựa trên interface: `private UserService userService;`
    * Client không quan tâm liệu `userService` là `UserServiceImpl` thật hay là `Proxy$1` (do JDK tạo ra). Cả hai đều "là" (`is-a`) `UserService`.

#### B. CGLIB Proxy

* **Khi nào dùng:** Khi lớp *target* của bạn **không implement interface** nào (chỉ là một class bình thường, ví dụ `ReportGeneratorService`).
* **Cách hoạt động:** Spring dùng thư viện CGLIB để tạo ra một lớp Proxy *mới* lúc runtime. Lớp Proxy này **kế thừa (extends) từ lớp *target*** của bạn.
* **Ví dụ:** Spring tạo ra lớp `ReportGeneratorService$$EnhancerBySpringCGLIB` (là *lớp con* của `ReportGeneratorService`). Nó *ghi đè (overrides)* các phương thức của bạn để thêm logic (như `@Transactional`), sau đó nó gọi `super.method()` để chạy logic nghiệp vụ thật.

-----

### 3\. ⚠️ Hạn chế (Limitations) - Rất quan trọng

Phần này bạn trả lời rất chuẩn, đặc biệt là về "self-invocation".

#### Hạn chế của JDK Proxy (Dựa trên Interface)

* **Phải có Interface:** Rõ ràng, nếu không có interface, kỹ thuật này không dùng được.
* **Chỉ các phương thức trên Interface được "proxy":** Nếu lớp `UserServiceImpl` của bạn có một phương thức `public` tên là `doInternalWork()` *không* được định nghĩa trong `UserService` (interface), thì khi gọi phương thức `doInternalWork()` này (từ một bean khác), nó sẽ gọi thẳng vào *target*, **bỏ qua proxy**.

#### Hạn chế của CGLIB Proxy (Dựa trên Kế thừa)

* **Không thể proxy lớp `final`:** Java không cho phép kế thừa (extend) một lớp `final`.
* **Không thể proxy phương thức `final`:** Java không cho phép ghi đè (override) một phương thức `final`. Nếu bạn đánh dấu `@Transactional` trên một phương thức `final`, Spring sẽ không báo lỗi, nhưng transaction sẽ **không hoạt động** vì proxy không thể can thiệp.

#### Hạn chế Chung (Vấn đề "Self-Invocation")

Đây là một "cái bẫy" kinh điển. Như bạn nói, "self-invocation" (tự gọi chính mình) sẽ **không hoạt động**.

* **Tại sao?** Hãy xem lại ví dụ `UserServiceImpl`:
  ```java
  @Service
  public class UserServiceImpl implements UserService {

      @Transactional // (A)
      public void createUser(User user) {
          // ... logic nghiệp vụ ...
          
          // TỰ GỌI MÌNH
          this.logUser(user); // (B)
      }

      @Transactional(propagation = Propagation.REQUIRES_NEW) // (C)
      public void logUser(User user) {
          // ... logic ghi log trong một transaction MỚI ...
      }
  }
  ```
* **Luồng hoạt động sai:**
    1.  `Controller` gọi `userService.createUser()`. Nó đang gọi **Proxy**.
    2.  **Proxy** thấy `@Transactional` (A), nó **BẮT ĐẦU TX 1**.
    3.  **Proxy** gọi `target.createUser()` (đối tượng `UserServiceImpl` thật).
    4.  Bên trong `createUser()`, code chạy đến `this.logUser(user)`.
    5.  **VẤN ĐỀ:** Từ khóa `this` trỏ đến **đối tượng `target` thật**, không phải **Proxy**.
    6.  Cuộc gọi (B) đi thẳng đến phương thức `logUser()` thật, **hoàn toàn bỏ qua Proxy**.
* **Kết quả:** Annotation `@Transactional` (C) trên `logUser()` **bị bỏ qua**. Logic log sẽ chạy bên trong **TX 1** (transaction có sẵn), thay vì tạo `REQUIRES_NEW` (một transaction mới) như mong muốn.

-----

### 4\. Ưu/Nhược điểm của Proxy

#### 👍 Ưu điểm (Power)

* **Tách biệt mối quan tâm (Separation of Concerns):** Đây là ưu điểm lớn nhất, như bạn đã nói. Code nghiệp vụ (`UserService`) chỉ lo nghiệp vụ. Code "cơ sở hạ tầng" (transactions, security, caching, logging) được đưa ra ngoài (do Proxy xử lý). Điều này làm code nghiệp vụ **cực kỳ sạch sẽ** và dễ test.
* **Minh bạch (Transparency):** `Controller` (người gọi) không cần biết là nó đang nói chuyện với proxy hay đối tượng thật.

#### 👎 Nhược điểm (Disadvantages)

* **Khó gỡ lỗi (Hard to debug):** Khi bạn "step into" một phương thức, debugger có thể nhảy vào các lớp proxy do Spring tạo ra (như `UserService$$EnhancerBySpringCGLIB`), gây bối rối.
* **Vấn đề `==` (Equality):** Như bạn nói, `proxyObject == targetObject` sẽ luôn là `false`. Chúng là hai đối tượng khác nhau trong bộ nhớ.
* **Hiệu năng (Performance):** Có một chi phí nhỏ (overhead) khi gọi qua proxy. Tuy nhiên, trong 99% ứng dụng, chi phí này là không đáng kể so với lợi ích.
* **Vấn đề "Self-Invocation":** Như đã giải thích ở trên.

## ❓ Câu hỏi: Java Config có ưu điểm và hạn chế gì?

**Java Config** là cách bạn định nghĩa Spring bean bằng cách sử dụng các lớp Java thuần túy (POJO) được chú thích (annotated) bằng `@Configuration` và các phương thức `@Bean`.

-----

### 👍 1. Ưu điểm (Advantages)

Ưu điểm của Java Config thể hiện rõ nhất khi so sánh nó với 2 cách làm còn lại:

#### A. So với XML

Câu trả lời của bạn đã nêu đúng hai ưu điểm lớn nhất:

* **Kiểm tra lỗi tại thời điểm biên dịch (Compile-Time Feedback):**

    * Đây là ưu điểm **lớn nhất**.
    * **Với XML:** Nếu bạn gõ sai tên class (`<bean class="com.example.MyServicee">`) hoặc tên property, Spring sẽ **chỉ phát hiện ra lỗi khi bạn chạy ứng dụng** (runtime), gây ra `ClassNotFoundException` hoặc `BeanCreationException`.
    * **Với Java Config:** Nếu bạn gõ `return new MyServicee();`, **IDE và trình biên dịch (compiler) sẽ báo lỗi đỏ ngay lập tức** (compile-time). Bạn sửa lỗi ngay cả trước khi chạy ứng dụng.

* **Hỗ trợ Tái cấu trúc (Refactoring):**

    * Đây là hệ quả của ưu điểm trên. Khi bạn dùng công cụ "Rename" (Tái cấu trúc) của IDE để đổi tên lớp `MyServiceImpl` thành `LegacyServiceImpl`, IDE sẽ **tự động cập nhật** nó trong lớp `@Configuration` của bạn (từ `new MyServiceImpl()` thành `new LegacyServiceImpl()`).
    * IDE (thông thường) không đủ thông minh để "hiểu" và "sửa" chuỗi (string) `"com.example.MyServiceImpl"` bên trong một file XML.

#### B. So với Annotation (`@Component` / Component Scanning)

Đây là một so sánh tinh tế hơn, và các điểm của bạn rất chính xác.

* **Tách biệt mối quan tâm (Separation of Concerns):**

    * Lớp nghiệp vụ của bạn (ví dụ: `UserServiceImpl`) là một **POJO (Plain Old Java Object)** sạch sẽ. Nó không chứa bất kỳ annotation nào của Spring (`@Component`, `@Autowired`).
    * **Logic nghiệp vụ** nằm trong `UserServiceImpl`. **Logic cấu hình** (nói rằng `UserServiceImpl` là một bean và cần `UserRepository`) nằm hoàn toàn tách biệt trong `AppConfig.java`.
    * Với `@Component`, logic cấu hình (`@Component`) bị trộn lẫn bên trong logic nghiệp vụ.

* **Không phụ thuộc công nghệ (Technology Agnostic):**

    * Vì lớp `UserServiceImpl` của bạn là POJO "sạch", nó không phụ thuộc vào Spring.
    * Bạn có thể lấy lớp đó và sử dụng trong một ứng dụng không phải Spring, hoặc với một framework DI khác (như Google Guice), hoặc tự khởi tạo (`new`) trong một bài Unit Test đơn giản. Nó rất dễ mang đi (portable) và kiểm thử.

* **Khả năng tích hợp thư viện bên ngoài (Integrate External Libraries):**

    * Đây là trường hợp **bắt buộc** phải dùng `@Bean`.
    * Bạn muốn Spring quản lý một bean `ObjectMapper` (từ thư viện Jackson) hoặc một `DataSource`. Bạn **không thể** thêm `@Component` vào file `.java` của thư viện đó.
    * Java Config cho phép bạn "Spring-hóa" các lớp của bên thứ ba:
      ```java
      @Configuration
      public class ExternalLibConfig {
          @Bean // Báo Spring hãy tạo và quản lý bean này
          public ObjectMapper objectMapper() {
              // Bạn có toàn quyền kiểm soát việc khởi tạo
              ObjectMapper mapper = new ObjectMapper();
              mapper.enable(SerializationFeature.INDENT_OUTPUT);
              return mapper;
          }
      }
      ```

* **Quản lý tập trung (Centralized Location):**

    * Bạn có thể mở một lớp `@Configuration` (ví dụ `AppConfig`) và **nhìn thấy danh sách tất cả các bean** đang được định nghĩa.
    * Với Component Scanning, các định nghĩa bean (`@Component`) bị phân tán (scattered) trên hàng trăm file trong toàn bộ dự án.

-----

### 👎 2. Hạn chế (Limitations)

Những hạn chế bạn đưa ra là hoàn toàn chính xác.

* **Không được là `final` (Class và Method):**

    * **Tại sao?** Vì Spring cần tạo **CGLIB Proxy** cho lớp `@Configuration` của bạn.
    * **Proxy này làm gì?** Nó "bọc" (wraps) lớp `AppConfig` của bạn để đảm bảo ngữ nghĩa (semantics) của `singleton` bean. Hãy xem ví dụ:
      ```java
      @Configuration
      public class AppConfig {
          @Bean
          public UserRepository userRepo() {
              return new UserRepositoryImpl();
          }
          
          @Bean
          public UserService userService() {
              // Nếu đây là Java thuần túy, userRepo() sẽ được gọi
              // và tạo ra MỘT instance MỚI.
              return new UserServiceImpl(userRepo()); 
          }
          
          @Bean
          public ReportService reportService() {
              // ...và đây là instance MỚI THỨ HAI.
              return new ReportServiceImpl(userRepo());
          }
      }
      ```
    * Nhưng Spring **Proxy** sẽ can thiệp. Lần đầu `userRepo()` được gọi, nó tạo bean và **cache (lưu trữ) lại**. Lần thứ hai `userRepo()` được gọi, proxy sẽ **trả về bean đã cache** thay vì chạy lại phương thức.
    * CGLIB tạo proxy bằng cách **kế thừa (extending)** lớp của bạn. Java không cho phép kế thừa từ lớp `final` hoặc ghi đè (override) phương thức `final`. Do đó, cả lớp `@Configuration` và các phương thức `@Bean` của bạn đều không được là `final`.

* **"Nói nhiều" (Verbose) / Phải liệt kê tất cả:**

    * Đây là mặt trái của "quản lý tập trung". Nếu ứng dụng của bạn có 500 service, bạn sẽ không muốn viết 500 phương thức `@Bean`.
    * So với Component Scanning, bạn chỉ cần ném `@Service` vào 500 lớp đó và Spring tự động tìm thấy chúng. Component Scanning tiện lợi hơn rất nhiều.

-----

### 🌟 3. Kết luận: Cách làm tốt nhất (Best Practice)

Trong thực tế, không ai chỉ dùng một cách. Cách làm tốt nhất là **kết hợp (hybrid)**:

1.  Sử dụng **Component Scanning (`@ComponentScan`)** làm mặc định cho **tất cả các bean nghiệp vụ của riêng bạn** (`@Service`, `@Repository`, `@Controller`, `@Component`). (Tiện lợi và nhanh chóng).
2.  Sử dụng **Java Config (`@Configuration`/`@Bean`)** để định nghĩa các bean **cơ sở hạ tầng (infrastructure)** (như `DataSource`, `RestTemplate`, `ObjectMapper`) và bất kỳ bean nào từ **thư viện bên ngoài**. (Rõ ràng và bắt buộc).

## ❓ Câu hỏi: Annotation @Bean làm gì?

### 1\. 🏭 Vai trò chính: "Nhà máy" sản xuất Bean

Như bạn nói, `@Bean` là một annotation bạn đặt trên một **phương thức (method)** bên trong một lớp `@Configuration`.

Nó là một tín hiệu nói với Spring:

> "Này Spring, hãy **chạy phương thức này** của tôi. Bất cứ đối tượng nào mà phương thức này **trả về (return)**, hãy lấy nó, đăng ký (register) và quản lý nó như một **Spring Bean** trong `ApplicationContext`."

Đây là cách "thủ công" (explicit) để định nghĩa bean, trái ngược với cách "tự động" (implicit) của `@ComponentScan` và `@Component`.

**Ví dụ cơ bản:**

```java
@Configuration
public class AppConfig {

    // Spring sẽ gọi phương thức này...
    @Bean
    public MyService myService() {
        // ...và đối tượng MyServiceImpl được trả về
        // sẽ trở thành một bean được quản lý.
        return new MyServiceImpl();
    }
}
```

-----

### 2\. 🎛️ Các "Nút điều khiển" (Thuộc tính) của @Bean

Bạn đã liệt kê chính xác các thuộc tính quan trọng của `@Bean`, cho phép bạn tinh chỉnh hành vi của bean:

* **Tên (Name) và Bí danh (Aliases):**

    * **Mặc định:** Tên của bean sẽ là **tên của phương thức**. Trong ví dụ trên, tên bean là `myService`.
    * **Tùy chỉnh:** Bạn có thể chỉ định một tên khác hoặc thêm bí danh (alias).
      ```java
      @Bean(name = "mainService", aliases = { "primaryService", "entryPoint" })
      public MyService myService() {
          return new MyServiceImpl();
      }
      ```

* **`initMethod` (Phương thức khởi tạo):**

    * Bạn chỉ định tên của một phương thức (dưới dạng `String`) bên trong lớp `MyServiceImpl` để Spring gọi *sau khi* bean đã được tạo và tiêm (inject) xong.
    * Hữu ích để chạy logic khởi tạo (ví dụ: làm nóng cache, khởi tạo pool).
      ```java
      // Bên trong lớp MyServiceImpl:
      public class MyServiceImpl {
          public void initializeCache() {
              // ... logic làm nóng cache ...
          }
      }

      // Bên trong lớp @Configuration:
      @Bean(initMethod = "initializeCache")
      public MyService myService() {
          return new MyServiceImpl();
      }
      ```

* **`destroyMethod` (Phương thức hủy):**

    * Tương tự, đây là tên của phương thức mà Spring sẽ gọi khi `ApplicationContext` bị đóng (để dọn dẹp tài nguyên).
    * *Lưu ý:* Chỉ hoạt động cho bean `singleton` (mặc định).
      ```java
      // Bên trong lớp MyServiceImpl:
      public class MyServiceImpl {
          public void shutdownPool() {
              // ... logic đóng connection pool ...
          }
      }

      // Bên trong lớp @Configuration:
      @Bean(destroyMethod = "shutdownPool")
      public MyService myService() {
          return new MyServiceImpl();
      }
      ```

* **`autowireCandidate` (Ứng viên để Tiêm):**

    * Mặc định là `true`. Bean này sẽ được xem xét khi Spring tìm kiếm dependency cho `@Autowired`.
    * Nếu bạn đặt là `false`, bean này sẽ bị "ẩn" đi. Nó vẫn tồn tại trong context (bạn có thể lấy bằng `context.getBean("myService")`), nhưng Spring sẽ **bỏ qua nó** khi cố gắng `@Autowired` `MyService` ở nơi khác. Rất hữu ích khi bạn có nhiều bean cùng loại và muốn loại trừ một bean cụ thể khỏi việc tự động tiêm.

-----

### 3\. 💡 Tại sao `@Bean` lại quan trọng?

* **Tích hợp thư viện bên ngoài:** Đây là lý do sử dụng **phổ biến nhất**. Bạn không thể thêm `@Component` vào code của thư viện bên thứ ba (ví dụ: `ObjectMapper` của Jackson). `@Bean` cho phép bạn tạo và cấu hình các đối tượng đó và biến chúng thành bean của Spring.
* **Cấu hình rõ ràng (Explicit):** Mọi thứ được định nghĩa tập trung ở một nơi, giúp bạn dễ dàng thấy được các bean đang được tạo ra.
* **Logic tạo phức tạp:** Nếu việc tạo một đối tượng đòi hỏi logic phức tạp (ví dụ: gọi một factory, thiết lập nhiều thứ), một phương thức `@Bean` là nơi hoàn hảo để thực hiện điều đó.

## ❓ Câu hỏi: Cấu hình profile như thế nào? Các trường hợp sử dụng hữu ích là gì?

### 1\. 💡 Ý tưởng cốt lõi của Profile

Hãy nghĩ Profiles như là các **kịch bản cấu hình** (configuration scenarios) khác nhau cho ứng dụng của bạn. `ApplicationContext` của bạn giống như một chiếc xe hơi. **Profiles** cho phép bạn "thay thế" các bộ phận (beans) của chiếc xe đó tùy thuộc vào môi trường bạn đang ở đâu.

* Khi bạn **Lái xe ở nhà (Dev profile)**: Bạn cần "bánh xe mô phỏng" (ví dụ: database H2 in-memory) và "động cơ kiểm thử" (một dịch vụ mock, không gửi email thật).
* Khi bạn **Lái xe ra đường đua (Prod profile)**: Bạn cần "bánh xe thật" (kết nối đến database Oracle/Postgres) và "động cơ thật" (dịch vụ gửi email qua SendGrid).

Profile cho phép bạn định nghĩa *tất cả* các bộ phận này trong cùng một code, và sau đó quyết định "lắp ráp" bộ phận nào khi khởi động ứng dụng.

-----

### 2\. ⚙️ Các trường hợp sử dụng (Tại sao cần Profile?)

Đây là phần quan trọng nhất để hiểu "tại sao".

* **Cấu hình Database khác nhau (Phổ biến nhất):**

    * **"dev" profile:** Cấu hình một `DataSource` kết nối đến database H2 (in-memory), tự động tạo bảng (schema generation).
    * **"prod" profile:** Cấu hình một `DataSource` kết nối đến Oracle hoặc PostgreSQL production, sử dụng connection pool thật (như HikariCP) và không bao giờ tự động tạo bảng.

* **Mocking vs. Real Services (Giả lập vs. Dịch vụ thật):**

    * **"test" profile:** Cung cấp một `MockEmailService` (chỉ in ra console) để các bài test chạy mà không gửi email spam.
    * **"prod" profile:** Cung cấp `RealEmailService` (kết nối qua API đến SendGrid/AWS SES).

* **Bật/Tắt tính năng (Feature Toggles):**

    * **"beta" profile:** Kích hoạt một `NewFeatureService` (tính năng mới) cho một nhóm người dùng beta.
    * **Default (không có profile):** Kích hoạt `StableFeatureService` (tính năng ổn định).

* **Cấu hình cho môi trường cụ thể:**

    * **"local-cache" profile:** Cấu hình cache dùng Hazelcast hoặc EhCache (chạy trên một máy).
    * **"distributed-cache" profile:** Cấu hình cache dùng Redis (chạy trên nhiều máy).

-----

### 3\. Cách 1: Định nghĩa Beans cho Profile

Như bạn đã liệt kê, chúng ta dùng annotation `@Profile`.

#### A. `@Profile` trên phương thức `@Bean` (Cách linh hoạt nhất)

Đây là cách phổ biến nhất để định nghĩa các bean "thay thế" lẫn nhau.

```java
@Configuration
public class DatabaseConfig {

    @Bean
    @Profile("dev") // Chỉ tạo bean này nếu profile "dev" được kích hoạt
    public DataSource devDataSource() {
        // Cấu hình H2 in-memory
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }

    @Bean
    @Profile("prod") // Chỉ tạo bean này nếu profile "prod" được kích hoạt
    public DataSource prodDataSource() {
        // Cấu hình HikariCP kết nối đến PostgreSQL thật
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://prod-db.example.com:5432/mydb");
        ds.setUsername("prod_user");
        ds.setPassword("prod_pass");
        return ds;
    }
}
```

#### B. `@Profile` trên `@Component` hoặc `@Service`

Dùng để "bật" hoặc "tắt" toàn bộ các implementation của một service.

```java
public interface EmailService {
    void sendEmail(String to, String body);
}

// Bean này sẽ được tải trong BẤT KỲ profile NÀO KHÔNG PHẢI là 'prod'
@Service
@Profile("!prod") // Toán tử '!' (NOT) rất hữu ích
public class MockEmailService implements EmailService {
    public void sendEmail(String to, String body) {
        System.out.println("MOCK EMAIL to " + to + ": " + body);
    }
}

// Bean này CHỈ được tải khi profile 'prod' được kích hoạt
@Service
@Profile("prod")
public class RealEmailService implements EmailService {
    public void sendEmail(String to, String body) {
        // Gọi API SendGrid/AWS SES...
    }
}
```

#### C. `@Profile` trên `@Configuration`

Dùng để bật/tắt cả một nhóm bean.

```java
@Configuration
@Profile("enable-caching") // Cả 2 bean bên dưới chỉ được tạo
                           // khi profile "enable-caching" được kích hoạt
public class CachingConfig {
    
    @Bean
    public CacheManager cacheManager() { ... }
    
    @Bean
    public CacheStatistics cacheStatistics() { ... }
}
```

**Lưu ý quan trọng:** Như bạn nói, nếu một bean **không** có `@Profile`, nó sẽ được tải trong **tất cả** các profile (bean mặc định).

-----

### 4\. Cách 2: Kích hoạt (Activate) Profiles

Sau khi đã "dán nhãn" profile cho các bean, bạn cần báo cho Spring biết profile nào đang chạy.

Bạn đã liệt kê đầy đủ các cách. Đây là những cách phổ biến nhất:

* **Trong `application.properties` (Dùng cho Development/Mặc định):**

  ```properties
  spring.profiles.active=dev
  ```

* **Qua Biến môi trường (Environment Variable) (Dùng cho Production/QA):**
  Đây là cách tốt nhất khi deploy, vì bạn không cần sửa code.

  ```bash
  # Trên server Linux/macOS
  export SPRING_PROFILES_ACTIVE="prod,aws" # Có thể kích hoạt nhiều profile

  # Trên server Windows
  set SPRING_PROFILES_ACTIVE="prod,aws"

  # (Spring Boot cũng tự động đọc biến SPRING_PROFILES_ACTIVE)
  ```

* **Qua tham số JVM (Dùng cho Production/QA):**

  ```bash
  java -Dspring.profiles.active="prod,aws" -jar my-app.jar
  ```

* **Trong Integration Tests (Dùng cho Testing):**
  Cách tốt nhất để kích hoạt profile (`test`, `mock-db`...) khi chạy JUnit test.

  ```java
  @SpringBootTest
  @ActiveProfiles("test") // Bắt buộc Spring chạy với profile "test"
  class MyServiceIntegrationTest {
      // ...
  }
  ```
## ❓ Câu hỏi: Làm thế nào để tiêm (inject) các giá trị vô hướng / (scalar/literal) vào Spring Beans? @Value được dùng để làm gì?

Cách chính để tiêm các giá trị này là sử dụng annotation **`@Value`**.

Không giống như `@Autowired` (dùng để tiêm *các bean khác*), `@Value` được dùng để tiêm các giá trị "đơn giản" (như `String`, `int`, `boolean`) vào các trường (fields), tham số (parameters) của constructor, hoặc tham số của method.

`@Value` đặc biệt mạnh mẽ vì nó hỗ trợ 3 chế độ hoạt động chính:

-----

### 1\. 🔡 Tiêm một giá trị cố định (Literal Value)

Đây là trường hợp đơn giản nhất, bạn tiêm một giá trị "hard-code" trực tiếp.

* **Cách dùng:** `@Value("giá-trị-cố-định")`
* **Ví dụ:**
  ```java
  @Component
  public class MyComponent {

      // Tiêm một chuỗi
      @Value("Hello World!")
      private String message;

      // Spring sẽ tự động chuyển đổi kiểu
      @Value("100")
      private int maxConnections;
      
      @Value("true")
      private boolean featureEnabled;
  }
  ```

-----

### 2\. 🗃️ Tiêm một giá trị từ Property File (`${...}`)

Đây là cách sử dụng **phổ biến và quan trọng nhất**. Nó cho phép bạn tách biệt cấu hình (configuration) ra khỏi code.

Bạn định nghĩa các key-value trong file `application.properties` (hoặc `application.yml`) và tham chiếu đến chúng bằng cú pháp `${key}`.

* **Cách dùng:** `@Value("${tên.key.trong.file.properties}")`
* **File `application.properties`:**
  ```properties
  app.name=My Awesome Application
  app.server.port=8080
  ```
* **Code Java:**
  ```java
  @Service
  public class AppInfoService {
      
      private final String appName;
      private final int serverPort;

      // Tiêm (inject) qua Constructor (Cách được khuyến nghị)
      @Autowired
      public AppInfoService(
          @Value("${app.name}") String appName,
          @Value("${app.server.port}") int serverPort
      ) {
          this.appName = appName;
          this.serverPort = serverPort;
      }

      public void printInfo() {
          // Sẽ in ra: "My Awesome Application is running on port 8080"
          System.out.println(appName + " is running on port " + serverPort);
      }
  }
  ```

#### Cung cấp giá trị Mặc định (Default Value)

Bạn có thể cung cấp một giá trị mặc định phòng trường hợp key không được tìm thấy trong file properties.

* **Cách dùng:** `@Value("${key.không.tồn.tại:giá-trị-mặc-định}")`
* **Ví dụ:**
  ```java
  // Nếu "app.version" không được định nghĩa, appVersion sẽ là "1.0.0"
  @Value("${app.version:1.0.0}")
  private String appVersion;
  ```

-----

### 3\. ⚡ Tiêm bằng SpEL (Spring Expression Language - `#{...}`)

Đây là cách mạnh mẽ nhất, cho phép bạn viết các "biểu thức" (expressions) để tính toán giá trị. Nó giống như chạy một đoạn code nhỏ.

* **Cách dùng:** `@Value("#{biểu-thức-SpEL}")`

* **Ví dụ:**

    * **Thực hiện tính toán, gọi phương thức:**

      ```java
      // Tiêm một số (4500)
      @Value("#{5000 * 0.9}")
      private double discountedPrice;

      // Tiêm chuỗi "WALL STREET" (sau khi gọi .toUpperCase())
      @Value("#{'Wall Street'.toUpperCase()}")
      private String companyName;
      ```

    * **Truy cập vào các bean khác:**
      Giả sử bạn có một bean tên là `systemProperties` (đây là bean có sẵn của Spring):

      ```java
      // Lấy giá trị "java.version" từ một bean khác
      @Value("#{systemProperties['java.version']}")
      private String javaVersion;
      ```

    * **Tạo Collections (List, Map):**

      ```java
      @Value("#{{'dev':'H2', 'prod':'PostgreSQL'}}")
      private Map<String, String> dbConfigMap;

      @Value("#{{'one', 'two', 'three'}}")
      private List<String> numberList;
      ```

    * **Kết hợp SpEL và Property (`#{'${...}'}`) - Quan trọng\!**
      Bạn có thể kết hợp cả hai. SpEL sẽ chạy *sau khi* property được thay thế.

      ```properties
      # trong application.properties
      app.security.role=admin
      ```

      ```java
      // 1. `${app.security.role}` được thay thế thành "admin"
      // 2. SpEL `#{'admin'.toUpperCase()}` được thực thi
      // 3. Kết quả: "ADMIN"
      @Value("#{'${app.security.role}'.toUpperCase()}")
      private String adminRole;
      ```

-----

### 4\. 📍 Vị trí đặt `@Value`

Như bạn đã nói, `@Value` có thể được đặt ở:

1.  **Field (Trường):** Phổ biến, nhanh gọn, nhưng gây khó khăn cho Unit Test (Field Injection).
    ```java
    @Component
    public class MyComponent {
        @Value("${app.name}")
        private String appName;
    }
    ```
2.  **Tham số Constructor (Constructor Parameter):** **Cách được khuyến nghị**. Nó làm cho bean của bạn "bất biến" (immutable) và thể hiện rõ các dependency, giúp Unit Test dễ dàng.
    ```java
    @Component
    public class MyComponent {
        private final String appName;
        
        public MyComponent(@Value("${app.name}") String appName) {
            this.appName = appName;
        }
    }
    ```
3.  **Tham số Method (Method Parameter):** Thường dùng trên các phương thức setter (Setter Injection) hoặc các phương thức cấu hình.
    ```java
    @Component
    public class MyComponent {
        private String appName;

        @Value("${app.name}")
        public void setAppName(String appName) {
            this.appName = appName;
        }
    }
    ```

-----

### 📊 Tóm tắt: `${...}` vs. `#{...}` (Rất quan trọng cho kỳ thi)

| Cú pháp | Tên | Mục đích | Khi nào được xử lý? |
| :--- | :--- | :--- | :--- |
| **`${...}`** | **Property Placeholder** | Để lấy giá trị từ **`Environment`** (ví dụ: `application.properties`). | **Rất sớm**, bởi `BeanFactoryPostProcessor`. |
| **`#{...}`** | **SpEL** (Spring Expression Language) | Để **thực thi biểu thức**, tính toán, gọi phương thức. | **Muộn hơn**, trong quá trình xử lý bean (bean processing). |

## ❓ Câu hỏi: Spring Expression Language (SpEL) là gì?

### 1\. 💡 SpEL là gì?

Như bạn đã nói, SpEL là một ngôn ngữ biểu thức mạnh mẽ.

Hãy nghĩ về nó như một **"máy tính bỏ túi"** hoặc một **"công cụ truy vấn nhỏ"** được tích hợp ngay bên trong Spring. Nó cho phép bạn thực hiện các phép toán, gọi phương thức, và truy cập dữ liệu (object graphs) một cách *động* (dynamically) ngay tại thời điểm runtime.

-----

### 2\. Cú pháp: `#{...}` vs. `${...}` (Rất quan trọng)

Đây là điểm cực kỳ quan trọng và dễ nhầm lẫn nhất.

* **`${...}` (Property Placeholder):**

    * **Ý nghĩa:** "Hãy tìm một **key** tên là... trong file `application.properties` và **thay thế** nó."
    * **Ví dụ:** `@Value("${app.name}")` -\> Spring sẽ tìm `app.name=My App` và thay thế nó.
    * **Thời điểm:** Được xử lý **rất sớm** (bởi `BeanFactoryPostProcessor`).

* **`#{...}` (SpEL Expression):**

    * **Ý nghĩa:** "Hãy **thực thi (execute)** đoạn code biểu thức này."
    * **Ví dụ:** `@Value("#{10 * 5}")` -\> Spring sẽ thực thi phép toán và tiêm (inject) giá trị `50`.
    * **Thời điểm:** Được xử lý **muộn hơn**, trong quá trình xử lý bean.

Bạn có thể kết hợp cả hai:

```java
// Giả sử: app.timeout=100
@Value("#{${app.timeout} * 0.9}") // Tương đương: #{100 * 0.9}
private double discountedTimeout; // -> Kết quả là 90.0
```

-----

### 3\. 🚀 Các tính năng chính (Từ danh sách của bạn)

Danh sách của bạn rất đầy đủ. Dưới đây là các ví dụ thực tế cho các tính năng quan trọng nhất:

#### A. Biểu thức toán học & Logic

```java
// Toán tử
@Value("#{100 / 5}") // 20
private int division;

// Toán tử logic
@Value("#{someBean.count > 10 and someBean.enabled}") // true/false
private boolean isReady;

// Toán tử ba ngôi (Ternary)
@Value("#{someBean.name == 'admin' ? 'Chào Admin' : 'Chào Guest'}")
private String welcomeMessage;
```

#### B. Tham chiếu đến Bean khác

Đây là một trong những tính năng mạnh nhất. Bạn có thể truy cập các bean khác bằng tên (ID) của chúng.

```java
// Giả sử bạn có một bean tên là "appSettings"
@Component("appSettings")
public class AppSettings {
    public String getDefaultLocale() {
        return "vi-VN";
    }
}

@Service
public class ReportService {
    // Gọi phương thức của một bean khác
    @Value("#{appSettings.getDefaultLocale()}")
    private String defaultLocale; // -> "vi-VN"
}
```

#### C. Thao tác trên đối tượng

```java
// Gọi phương thức
@Value("#{'Hello World'.toUpperCase()}") // "HELLO WORLD"
private String upper;

// Truy cập thuộc tính
@Value("#{'Hello World'.bytes.length}") // 11
private int length;

// Tạo constructor
@Value("#{new java.util.Date()}")
private Date today;
```

#### D. Thao tác với Collection (List/Map)

* **Tạo inline:**
  ```java
  @Value("#{{'USA', 'Vietnam', 'Japan'}}")
  private List<String> countries;

  @Value("#{{'key1':'val1', 'key2':'val2'}}")
  private Map<String, String> configMap;
  ```
* **Collection Selection (Lựa chọn):**
  Giống như mệnh đề `WHERE` trong SQL. Cú pháp là `collection.?[biểu_thức_boolean]`.
  ```java
  // Giả sử 'allUsers' là một List<User> từ một bean khác
  // Lấy tất cả user đang "active"
  @Value("#{allUsers.?[active == true]}")
  private List<User> activeUsers;
  ```
* **Collection Projection (Chiếu):**
  Giống như mệnhD `SELECT` trong SQL. Cú pháp là `collection.![thuộc_tính]`.
  ```java
  // Lấy tất cả "email" từ danh sách activeUsers
  @Value("#{activeUsers.![email]}")
  private List<String> activeEmails;
  ```

#### E. Sử dụng trong Security (Rất phổ biến)

SpEL là nền tảng của Spring Security để kiểm tra quyền.

```java
@PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
public void updateUser(String username, UserData data) {
    // ...
    // SpEL ở đây có thể truy cập cả tham số (#username) và
    // đối tượng security (authentication.name)
}
```

-----

### 4\. ⚡ Chế độ Compiled (Nâng cao)

Phần này trong câu trả lời của bạn là rất tốt và thể hiện sự hiểu biết sâu.

* **Mặc định (Interpreted Mode):** Giống như JavaScript cũ. Spring "đọc" biểu thức của bạn và "diễn giải" nó mỗi lần chạy.
    * *Ưu điểm:* Rất linh hoạt.
    * *Nhược điểm:* Chậm nếu gọi lặp đi lặp lại (ví dụ: trong vòng `for` 1 triệu lần).
* **Compiled Mode (Bạn bật lên):** Spring sẽ "biên dịch" biểu thức SpEL của bạn thành **Java bytecode** thật (tạo ra một class `.java` ngầm) để nó chạy nhanh như code Java bình thường.
    * *Ưu điểm:* Nhanh hơn đáng kể.
    * *Nhược điểm:* Mất đi một số tính linh hoạt (như bạn đã liệt kê: không hỗ trợ assignment, selection/projection).

**Chế độ `Mixed` (lai):** Như bạn nói, đây là chế độ "thông minh". Nó chạy interpreted vài lần để "học" về các kiểu dữ liệu, sau đó nó thử biên dịch (compile). Nếu biên dịch lỗi (ví dụ: kiểu dữ liệu thay đổi), nó sẽ tự động quay lại (fallback) chế độ interpreted.

## ❓ Câu hỏi: Các thuộc tính (properties) trong `Environment` (env) có thể đến từ đâu?

### 1. 💡 Ý tưởng cốt lõi: `Environment` là một "Ngăn xếp" các Nguồn

`Environment` (Môi trường) của Spring là một đối tượng trừu tượng, nó chứa *tất cả* các thuộc tính (key-value) mà ứng dụng của bạn có thể truy cập.

Nó không lấy thuộc tính từ *một* nơi, mà từ **nhiều `PropertySource` (Nguồn thuộc tính)**. Hãy nghĩ về `Environment` như một **ngăn xếp các lớp (stack of layers)**.



**Quy tắc vàng:** Một thuộc tính được định nghĩa ở lớp có **độ ưu tiên cao hơn** (gần đỉnh hơn) sẽ **ghi đè (override)** thuộc tính có cùng tên ở lớp có **độ ưu tiên thấp hơn** (gần đáy hơn).

Đây chính là "phép thuật" của Spring. Nó cho phép bạn:
* Đặt giá trị mặc định (default) trong code (ví dụ: `server.port = 8080` trong `application.properties`).
* **Ghi đè** nó bằng Biến môi trường (Environment Variable) trên máy chủ QA (ví dụ: `SERVER_PORT = 9090`).
* **Ghi đè** nó một lần nữa bằng Tham số dòng lệnh (Command Line) khi chạy (ví dụ: `java -jar app.jar --server.port=1234`).

Kết quả: Ứng dụng sẽ chạy ở cổng `1234`.

---

### 2. Các Nguồn Thuộc tính (Property Sources)

Danh sách của bạn rất đầy đủ. Dưới đây là cách sắp xếp chúng theo bối cảnh và (quan trọng nhất) là **thứ tự ưu tiên**.

#### A. Ứng dụng Spring "Truyền thống" (Standalone/Servlet)

Trong một ứng dụng Spring không phải Spring Boot, bạn thường phải cấu hình nhiều thứ thủ công. Các nguồn (từ cao đến thấp) thường là:

1.  **JVM System Properties:** Các thuộc tính bạn truyền vào khi chạy Java (ví dụ: `java -Dmy.property=value ...`).
2.  **System Environment Variables:** Các biến môi trường của Hệ điều hành (ví dụ: `export MY_PROPERTY=value`).
3.  **JNDI Attributes:** (Chỉ cho ứng dụng web) Các thuộc tính được cấu hình trong máy chủ ứng dụng (Tomcat, JBoss) (ví dụ: `java:comp/env/myProperty`).
4.  **ServletContext / ServletConfig init-params:** (Chỉ cho ứng dụng web) Các tham số trong `web.xml`.
5.  **File properties (do bạn tải):** Các file được tải qua `@PropertySource` hoặc trong XML.

#### B. Ứng dụng Spring Boot (Tự động & Rất nhiều nguồn)

Spring Boot tự động cấu hình một danh sách các nguồn thuộc tính **rất dài** và đã được **sắp xếp thứ tự ưu tiên** cẩn thận. Danh sách bạn cung cấp là rất chính xác.

Dưới đây là phiên bản đơn giản hóa của danh sách đó, được sắp xếp **theo thứ tự ưu tiên từ CAO xuống THẤP** (Nguồn ở trên sẽ ghi đè nguồn ở dưới):

1.  **Devtools Global Settings:** Các thuộc tính trong file `~/.spring-boot-devtools.properties` (chỉ khi `spring-boot-devtools` được kích hoạt).
2.  **Test Properties:** (Ưu tiên cao nhất khi chạy test) Thuộc tính từ `@TestPropertySource` hoặc `properties` trong `@SpringBootTest`.
3.  **Command Line Arguments:** Các tham số dòng lệnh bắt đầu bằng `--` (ví dụ: `--server.port=9000`).
4.  `SPRING_APPLICATION_JSON`:** Một biến môi trường hoặc system property chứa một chuỗi JSON (ví dụ: `export SPRING_APPLICATION_JSON='{"server.port": 9001}'`).
5.  **`ServletConfig` / `ServletContext` init parameters.**
6.  **`JNDI` attributes** (từ `java:comp/env`).
7.  **JVM System Properties** (ví dụ: `-Dserver.port=9002`).
8.  **OS Environment Variables:** Biến môi trường hệ điều hành (ví dụ: `export SERVER_PORT=9003`). Spring Boot đủ thông minh để map `SERVER_PORT` thành `server.port`.
9.  `RandomValuePropertySource`:** Dùng để tạo các giá trị ngẫu nhiên (ví dụ: `${random.int}`, `${random.uuid}`).
10. **Application Properties (BÊN NGOÀI JAR):**
    * `application-{profile}.properties` (hoặc `.yml`) nằm bên ngoài JAR của bạn (ví dụ: trong cùng thư mục với file `.jar`).
    * `application.properties` (hoặc `.yml`) nằm bên ngoài JAR của bạn.
      *(Đây là cách rất hữu ích để admin hệ thống ghi đè cấu hình mà không cần mở file JAR).*
11. **Application Properties (BÊN TRONG JAR - Classpath):**
    * `application-{profile}.properties` (hoặc `.yml`) bên trong classpath.
    * `application.properties` (hoặc `.yml`) bên trong classpath.
      *(Đây là nơi bạn đặt các giá trị mặc định của ứng dụng).*
12. **`@PropertySource`:** Các file properties tùy chỉnh bạn tải trong các lớp `@Configuration` của mình. (Lưu ý: Chúng có độ ưu tiên thấp hơn `application.properties`).
13. **Default Properties:** Các giá trị mặc định được đặt qua `SpringApplication.setDefaultProperties(...)`.

---

**Tóm lại:** Bạn không cần phải nhớ chính xác *tất cả 17 nguồn* này, nhưng bạn **phải** nhớ quy tắc ưu tiên chung:

> **Test** > **Command Line** > **JVM Properties** > **OS Environment** > **External File** (bên ngoài JAR) > **Internal File** (bên trong JAR) > **@PropertySource** > **Defaults**.