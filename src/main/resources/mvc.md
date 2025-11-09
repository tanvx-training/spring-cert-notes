## ❓ Câu hỏi: MVC is an abbreviation for a design pattern. What does it stand for and what is the idea behind it?

(MVC là viết tắt của một mẫu thiết kế. Nó là viết tắt của cái gì và ý tưởng đằng sau nó là gì?)

### 1. MVC là gì?

`MVC` là viết tắt của **Model - View - Controller**.

Đây là một mẫu thiết kế kiến trúc (architectural design pattern) rất phổ biến, có ý tưởng cốt lõi là **phân tách các mối quan tâm** (Separation of Concerns) trong một ứng dụng. Thay vì gộp chung tất cả logic vào một nơi, MVC chia ứng dụng thành ba thành phần chính, có liên kết với nhau:

* **Model** (Mô hình)
* **View** (Khung nhìn)
* **Controller** (Bộ điều khiển)

### 2. Các thành phần trong MVC

Hãy tưởng tượng MVC giống như hoạt động của một nhà hàng:

#### 🧑‍🍳 Model (Mô hình)
* **Là gì?** Đây là "nhà bếp" của ứng dụng. Nó chứa tất cả **dữ liệu** và **logic nghiệp vụ** (business logic).
* **Nhiệm vụ:**
    * Đại diện cho cấu trúc dữ liệu (ví dụ: lớp `User`, `Product`).
    * Thực thi logic nghiệp vụ (ví dụ: tính toán giảm giá, kiểm tra mật khẩu).
    * Tương tác với cơ sở dữ liệu (Database) thông qua các logic CRUD (Create, Read, Update, Delete).
* **Trong Spring:** Đây thường là các lớp **Entity**, **Repository** (như `Spring Data JPA`, `JDBC`), và các lớp **Service** (nơi chứa business logic).

#### 🖥️ View (Khung nhìn)
* **Là gì?** Đây là "bàn ăn" và "cách bài trí món ăn". Nó là thứ mà người dùng **nhìn thấy và tương tác** (User Interface - UI).
* **Nhiệm vụ:**
    * Hiển thị dữ liệu được cung cấp bởi Model.
    * *Không* chứa bất kỳ logic nghiệp vụ nào. Nó chỉ đơn giản là "vẽ" ra những gì được bảo.
    * Có thể có nhiều View khác nhau cho cùng một dữ liệu (ví dụ: một trang web, một ứng dụng di động, hoặc một file JSON).
* **Trong Spring:** Đây là các công nghệ template engine như **Thymeleaf**, **FreeMarker**, hoặc `JSP`. (Trong các ứng dụng RESTful, View có thể chỉ là dữ liệu `JSON` hoặc `XML`).

#### 🤵 Controller (Bộ điều khiển)
* **Là gì?** Đây là "người phục vụ". Nó là bộ não điều phối, đứng giữa người dùng (View) và nhà bếp (Model).
* **Nhiệm vụ:**
    * **Tiếp nhận yêu cầu** từ người dùng (ví dụ: người dùng nhấp vào một nút).
    * **Ra lệnh** cho Model thực hiện một hành động (ví dụ: "lấy cho tôi danh sách sản phẩm" hoặc "lưu người dùng này vào database").
    * **Chọn View** thích hợp để hiển thị kết quả cho người dùng.
* **Trong Spring:** Đây chính là các lớp được đánh dấu bằng **`@Controller`** (dùng cho web truyền thống) hoặc **`@RestController`** (dùng cho các API trả về JSON/XML).

---

### 3. Luồng hoạt động (The Flow)

Đây là luồng xử lý một yêu cầu tiêu biểu trong Spring MVC:

1.  **Người dùng** (User) thực hiện một hành động (ví dụ: truy cập URL `/products`).
2.  `DispatcherServlet` (một Controller trung tâm của Spring) tiếp nhận yêu cầu này đầu tiên.
3.  `DispatcherServlet` chuyển yêu cầu đến **Controller** (lớp `@Controller` của bạn) mà bạn đã định nghĩa để xử lý URL `/products`.
4.  **Controller** gọi đến **Model** (cụ thể là lớp `Service` hoặc `Repository`) để yêu cầu dữ liệu (ví dụ: `productService.getAllProducts()`).
5.  **Model** tương tác với database, lấy dữ liệu, và trả về một danh sách các sản phẩm cho Controller.
6.  **Controller** nhận danh sách sản phẩm, đặt nó vào một đối tượng `Model` (giống như một cái khay), và quyết định "tôi muốn dùng view tên là `productList` để hiển thị".
7.  `DispatcherServlet` nhận lại tên view (`productList`) và dữ liệu (danh sách sản phẩm) từ Controller.
8.  Nó tìm đến công nghệ **View** (ví dụ: file `productList.html` của Thymeleaf), "nhồi" dữ liệu vào template đó.
9.  **View** tạo ra file HTML hoàn chỉnh và trả về cho trình duyệt của người dùng.



---

### 4. Lợi ích của việc sử dụng MVC

Câu trả lời của bạn đã liệt kê rất chính xác các lợi ích:

* **Separation of Concerns (Phân tách các mối quan tâm):** Đây là lợi ích lớn nhất. Logic nghiệp vụ (Model), logic hiển thị (View), và logic điều khiển (Controller) được tách biệt rõ ràng.
* **Increased Code Cohesion (Tăng tính gắn kết):** Các code liên quan đến nhau được nhóm lại một chỗ (ví dụ: tất cả logic về `User` nằm trong `UserService`, `UserRepository`).
* **Reduced Coupling (Giảm sự phụ thuộc):** View không cần biết Model lấy dữ liệu từ đâu (SQL hay NoSQL). Model cũng không cần biết dữ liệu sẽ được hiển thị trên web hay app. Chúng có thể thay đổi độc lập.
* **Increased Code Re-usability (Tăng khả năng tái sử dụng):** Bạn có thể tái sử dụng cùng một Model cho nhiều View khác nhau (ví dụ: cùng một `productService` có thể phục vụ cho cả trang web (HTML) và ứng dụng di động (JSON)).
* **Lower Maintenance Code (Dễ bảo trì):** Khi cần sửa lỗi hiển thị, bạn chỉ cần vào View. Khi cần thay đổi logic nghiệp vụ, bạn chỉ cần vào Model.
* **Increases Extensibility (Dễ mở rộng):** Dễ dàng thêm View mới hoặc thay đổi Model mà không ảnh hưởng đến các thành phần khác.

## ❓ Câu hỏi: What is the DispatcherServlet and what is it used for?

(DispatcherServlet là gì và nó được sử dụng để làm gì?)

### 1. DispatcherServlet là gì?

`DispatcherServlet` chính là **trái tim** và là **bộ điều khiển trung tâm** của toàn bộ Spring MVC.

Hãy hình dung nó như một **người lễ tân** (hoặc một tổng đài viên) của một công ty lớn. Mọi cuộc gọi (request) đến công ty đều phải đi qua người lễ tân này đầu tiên. Người lễ tân này sau đó sẽ chịu trách nhiệm chuyển cuộc gọi đến đúng phòng ban, đúng người cần gặp (Controller).

Về mặt kỹ thuật, như bạn đã nêu, nó là một lớp `Servlet` (kế thừa từ `HttpServlet` của Java). Nó là thành phần cốt lõi hiện thực hóa mẫu thiết kế (Design Pattern) có tên là **Front Controller**.

### 2. Ý tưởng "Front Controller"

Trước khi có Spring MVC, một ứng dụng web Java có thể có rất nhiều Servlet khác nhau:
* `LoginServlet` xử lý `/login`
* `ProductServlet` xử lý `/products`
* `RegisterServlet` xử lý `/register`

Điều này rất khó quản lý và lặp lại nhiều code (như kiểm tra bảo mật, logging).

Mẫu **Front Controller** giải quyết vấn đề này bằng cách: Chỉ có **một Servlet duy nhất** (chính là `DispatcherServlet`) đứng ra nhận **tất cả** các request gửi đến ứng dụng của bạn (thường là map với `/*`).

Sau đó, `DispatcherServlet` sẽ "phân phối" (dispatch) các request này đến các bộ xử lý (handler) cụ thể bên trong, chính là các phương thức trong lớp **`@Controller`** của bạn.

### 3. Nhiệm vụ chính của DispatcherServlet

Câu trả lời của bạn đã tóm tắt rất chính xác. Đây là các nhiệm vụ chính của nó:

* **Tiếp nhận mọi request:** Nó là điểm vào (entry point) duy nhất cho tất cả các HTTP request.
* **Phân phối request (Delegation):** Nó không tự mình xử lý logic nghiệp vụ. Thay vào đó, nó sử dụng các thành phần hỗ trợ (như `HandlerMapping`) để tìm ra phương thức **`@Controller`** nào nên xử lý request này.
* **Điều phối View (View Resolution):** Sau khi Controller xử lý xong và trả về tên của View (ví dụ: "home"), `DispatcherServlet` sẽ hỏi các `ViewResolver` (ví dụ: Thymeleaf, JSP) để tìm ra file template thực sự (ví dụ: `/templates/home.html`).
* **Tổng hợp và trả Response:** Nó lấy dữ liệu (Model) từ Controller, "nhồi" vào View đã được giải quyết, render ra HTML (hoặc JSON/XML) và gửi `HttpResponse` về cho trình duyệt.
* **Xử lý các vấn đề chung (Shared Concerns):** Vì nó là cửa ngõ duy nhất, nó là nơi lý tưởng để xử lý các logic chung như:
    * Xử lý ngoại lệ (Exception Handling)
    * Tích hợp bảo mật (Spring Security)
    * Xử lý file upload (Multipart requests)
    * Hỗ trợ `i18n` (đa ngôn ngữ)

### 4. Luồng hoạt động chi tiết (Rất quan trọng)

Để hiểu rõ, bạn cần nắm được luồng đi của một request bên trong `DispatcherServlet`:



1.  **HttpRequest đến:** Người dùng gửi một request (ví dụ: `GET /users/1`).
2.  **DispatcherServlet (DS)** tiếp nhận request.
3.  **DS** hỏi `HandlerMapping`: "Với request `GET /users/1`, ai sẽ xử lý?"
4.  `HandlerMapping` trả lời: "Phương thức `getUserById()` trong `UserController`."
5.  **DS** gọi `HandlerAdapter` để thực thi phương thức `getUserById(1)`.
6.  **`@Controller`** (phương thức của bạn) được thực thi. Nó gọi `Service`, `Repository`, lấy dữ liệu, và trả về một đối tượng `ModelAndView` (hoặc chỉ là một `String` tên view).
7.  **DS** nhận `ModelAndView` (chứa dữ liệu "user" và tên view là "userDetail").
8.  **DS** hỏi `ViewResolver`: "Tôi có tên view là 'userDetail', file thực sự là gì?"
9.  `ViewResolver` (ví dụ: Thymeleaf) trả lời: "Nó là file `/templates/userDetail.html`."
10. **DS** gọi `View` (file Thymeleaf) và truyền dữ liệu "user" vào.
11. `View` được render (tạo ra file HTML cuối cùng).
12. **DS** gửi `HttpResponse` (chuỗi HTML) về cho trình duyệt của người dùng.

> **Ghi chú (Note):** Nếu bạn dùng **`@RestController`** (cho các API), luồng này sẽ đơn giản hơn. Ở bước 6, Controller trả về một đối tượng (ví dụ: `User`). `DispatcherServlet` sẽ dùng `HttpMessageConverter` (như Jackson) để chuyển đối tượng đó thành `JSON` và gửi về, bỏ qua toàn bộ bước 7-11 (View Resolution).

## ❓ Câu hỏi: What is a web application context? What extra scopes does it offer?

(Web application context là gì? Nó cung cấp thêm những scope (phạm vi) nào?)

### 1\. WebApplicationContext là gì?

Một cách dễ hiểu:

* **`ApplicationContext`** (Context tiêu chuẩn): Hãy coi nó như "bộ não" hay "nhà máy" của Spring. Nó chịu trách nhiệm tạo ra, quản lý và "tiêm" (inject) các bean (các đối tượng) của bạn.
* **`WebApplicationContext`** (Context cho web): Đây là một phiên bản **mở rộng** của `ApplicationContext` được thiết kế đặc biệt cho các ứng dụng web.

Điểm khác biệt mấu chốt là `WebApplicationContext` **có khả năng nhận biết về `ServletContext`**.

> **`ServletContext` là gì?** Đó là một đối tượng tiêu chuẩn của Java Servlet API, đại diện cho toàn bộ ứng dụng web của bạn khi nó chạy trên một máy chủ (như Tomcat). Nó giống như "môi trường" hay "ngôi nhà" mà ứng dụng web của bạn sống bên trong.

Bằng cách liên kết với `ServletContext`, `WebApplicationContext` có thể quản lý các bean dựa trên vòng đời của các thành phần web (như request, session). Điều này dẫn đến việc nó cung cấp thêm các *scope* (phạm vi) mới.

-----

### 2\. Các Scope tiêu chuẩn (Để so sánh)

Trước hết, hãy nhớ lại 2 scope tiêu chuẩn có trong *mọi* `ApplicationContext`:

* **`singleton` (Mặc định):** Chỉ có **một** thực thể (instance) duy nhất của bean được tạo ra trong toàn bộ Spring container.
* **`prototype`:** Một thực thể **mới** được tạo ra *mỗi khi* bean đó được yêu cầu (được inject hoặc gọi `getBean()`).

-----

### 3\. Bốn (4) Scope bổ sung trong WebApplicationContext

Vì `WebApplicationContext` "hiểu" được môi trường web, nó cung cấp thêm 4 scope sau:

#### 1\. Request Scope

* **Annotation:** **`@RequestScope`**
* **Vòng đời:** Spring sẽ tạo một thực thể (instance) **mới** của bean này cho **mỗi một HTTP request**. Bean này chỉ tồn tại trong suốt thời gian của request đó. Khi response được gửi đi, bean này sẽ bị hủy.
* **Tưởng tượng:** Giống như một tờ giấy nháp. Mỗi khi có yêu cầu mới, bạn lấy một tờ giấy nháp mới, ghi chép vào đó, và khi xử lý xong yêu cầu thì vứt tờ giấy đó đi.
* **Ví dụ:** Dùng để lưu thông tin chỉ liên quan đến request hiện tại (ví dụ: thông tin `correlation-id` để logging).

<!-- end list -->

```java
@RequestScope
@Component
public class RequestScopeBean {
    // Bean này sẽ được tạo mới cho mỗi HTTP request
    private String data = "Request data: " + System.nanoTime();
}
```

#### 2\. Session Scope

* **Annotation:** **`@SessionScope`**
* **Vòng đời:** Spring sẽ tạo một thực thể **mới** cho **mỗi một HTTP Session của người dùng**. Bean này sẽ tồn tại xuyên suốt nhiều request từ *cùng một người dùng đó*. Nó chỉ bị hủy khi session hết hạn (timeout) hoặc bị hủy (invalidate, ví dụ khi người dùng logout).
* **Tưởng tượng:** Giống như **giỏ hàng** (shopping cart) của bạn. Bạn có thể thêm/bớt hàng (nhiều request) và giỏ hàng vẫn còn đó, cho đến khi bạn thanh toán hoặc rời đi quá lâu.
* **Ví dụ:** Lưu thông tin đăng nhập của người dùng, giỏ hàng, hoặc các cài đặt riêng của người dùng.

<!-- end list -->

```java
@SessionScope
@Component
public class SessionScopeBean {
    // Bean này sẽ tồn tại suốt session của 1 người dùng
    private ShoppingCart cart = new ShoppingCart();
}
```

#### 3\. Application Scope

* **Annotation:** **`@ApplicationScope`**
* **Vòng đời:** Spring sẽ tạo **một thực thể duy nhất** cho **toàn bộ ứng dụng web** (liên kết với vòng đời của `ServletContext`). Nó giống hệt `singleton` về mặt số lượng (chỉ có 1).
* **Tưởng tượng:** Giống như một "bảng thông báo chung" của toàn bộ ứng dụng. Bất kỳ ai, bất kỳ lúc nào cũng nhìn thấy cùng một bảng thông báo đó.
* **Ví dụ:** Lưu trữ các cấu hình chung, bộ đếm truy cập toàn trang, hoặc cache dữ liệu toàn ứng dụng.

> **⚠️ Điểm khác biệt quan trọng (Dễ bị hỏi thi): `Application Scope` vs. `Singleton`**
>
>   * **`Singleton`**: Là *một* bean trên *mỗi* `ApplicationContext` của Spring.
>   * **`Application Scope`**: Là *một* bean trên *toàn bộ* `ServletContext`.
>
> Trong 99% trường hợp, bạn chỉ có 1 `ApplicationContext` chạy trong 1 `ServletContext`, nên chúng hoạt động **giống hệt nhau**.
>
> Tuy nhiên, trong một số cấu hình nâng cao (ví dụ khi bạn có 1 *root context* và nhiều *servlet context* con), bạn có thể có nhiều `ApplicationContext` cùng chạy trong 1 `ServletContext`.
>
>   * Lúc này, `@Singleton` sẽ tạo ra 1 bean cho *mỗi* context (tức là có nhiều bean).
>   * Nhưng `@ApplicationScope` sẽ **chỉ tạo ra 1 bean duy nhất** và chia sẻ cho tất cả các context đó.

```java
@ApplicationScope
@Component
public class ApplicationScopeBean {
    // Chỉ có 1 bean này cho toàn bộ ứng dụng web
    private long hitCounter = 0;
}
```

#### 4\. WebSocket Scope

* **Annotation:** **`@Scope(scopeName = "websocket", ...)`**
* **Vòng đời:** Bean được tạo ra và liên kết với vòng đời của một **phiên WebSocket**. Nó tồn tại chừng nào kết nối WebSocket còn mở.
* **Tưởng tượng:** Giống như một "cuộc gọi điện thoại" riêng tư. Thông tin chỉ tồn tại trong phạm vi cuộc gọi đó, và mất đi khi bạn gác máy.
* **Ví dụ:** Lưu trạng thái của một người dùng trong một phòng chat real-time.

<!-- end list -->

```java
@Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class WebSocketScopeBean {
    // Bean này tồn tại suốt 1 phiên kết nối WebSocket
}
```

-----

### Tóm tắt các Scope

| Scope | Annotation | Vòng đời | Số lượng |
| :--- | :--- | :--- | :--- |
| **Singleton** | (Mặc định) | Toàn bộ `ApplicationContext` | 1 cho mỗi Spring Context |
| **Prototype** | `@Scope("prototype")` | Mỗi lần được yêu cầu | Rất nhiều (mỗi lần gọi là 1) |
| **Request** | `@RequestScope` | 1 HTTP Request | 1 cho mỗi Request |
| **Session** | `@SessionScope` | 1 HTTP Session | 1 cho mỗi User Session |
| **Application** | `@ApplicationScope` | Toàn bộ `ServletContext` | 1 cho toàn bộ Ứng dụng Web |
| **WebSocket** | `@Scope("websocket")` | 1 WebSocket Session | 1 cho mỗi Kết nối WebSocket |

## ❓ Câu hỏi: How is an incoming request mapped to a controller and mapped to a method?

(Một request đến được ánh xạ (map) tới một controller và một phương thức cụ thể như thế nào?)

### 1\. "Tổng đài viên" và các "Trợ lý"

Hãy tưởng tượng `DispatcherServlet` (DS) là **tổng đài viên** của toàn bộ ứng dụng. Khi một request (cuộc gọi) đến, DS không tự mình xử lý mà sẽ cần đến hai trợ lý đắc lực:

1.  **`HandlerMapping` (Người tìm đường):**

    * Đây là "cuốn sổ địa chỉ" của DS.
    * Nhiệm vụ: **TÌM**. Khi ứng dụng khởi động, `HandlerMapping` sẽ "quét" (scan) toàn bộ mã nguồn, tìm tất cả các lớp `@Controller` / `@RestController` và tất cả các phương thức có annotation ánh xạ (như `@RequestMapping`, `@GetMapping`...).
    * Nó tạo ra một bản đồ (map) chi tiết, ví dụ: "Request `GET /users/1` sẽ do phương thức `getUser()` trong `UserController` xử lý".

2.  **`HandlerAdapter` (Người thực thi):**

    * Đây là "người phiên dịch" hoặc "người thực thi" công việc.
    * Nhiệm vụ: **GỌI**. Sau khi `HandlerMapping` đã tìm ra *phương thức nào* cần gọi, `HandlerAdapter` sẽ làm nhiệm vụ *gọi phương thức đó*.
    * Tại sao cần `HandlerAdapter`? Vì phương thức của bạn có thể có các tham số phức tạp (như `@RequestBody`, `@RequestParam`, `ModelMap`...). `HandlerAdapter` biết cách lấy thông tin từ `HttpRequest` thô để "biên dịch" và "nhồi" vào các tham số Java đó một cách chính xác.

-----

### 2\. Luồng xử lý một Request

Câu trả lời của bạn đã mô tả đúng luồng này. Đây là cách diễn giải đơn giản hóa:

1.  Request (`GET /say/hello`) đến **`DispatcherServlet`** (DS) đầu tiên.
2.  DS hỏi `HandlerMapping`: "Ai xử lý `GET /say/hello`?"
3.  `HandlerMapping` tra "sổ địa chỉ" và trả lời: "Đó là phương thức `sayHello()` trong lớp `HelloController`."
4.  DS chuyển giao nhiệm vụ cho `HandlerAdapter` và nói: "Hãy gọi phương thức `sayHello()`."
5.  `HandlerAdapter` thực thi phương thức `sayHello()` (và xử lý các tham số nếu có).
6.  Phương thức `sayHello()` chạy và trả về kết quả (ví dụ: `ResponseEntity<String>`).
7.  `HandlerAdapter` trả kết quả này về cho DS.
8.  DS (với sự giúp đỡ của `ViewResolver` hoặc `HttpMessageConverter`) xử lý kết quả đó (ví dụ: render View hoặc chuyển thành JSON) và gửi `HttpResponse` về cho người dùng.

-----

### 3\. Cách định nghĩa ánh xạ: `@RequestMapping`

`@RequestMapping` là annotation (chú thích) "mẹ", là nền tảng cho tất cả các ánh xạ.

* Nó có thể dùng ở **cấp độ lớp (Class-level):** Dùng để định nghĩa một tiền tố (prefix) chung cho tất cả các phương thức bên trong.
* Nó có thể dùng ở **cấp độ phương thức (Method-level):** Dùng để định nghĩa đường dẫn cụ thể cho phương thức đó.

Các ví dụ của bạn về việc ánh xạ `GET /say/hello` là hoàn toàn chính xác và cho thấy sự linh hoạt này:

**Ví dụ 1: Tiền tố ở lớp, phương thức ở method**

```java
@Controller
@RequestMapping("/say") // Tiền tố
public class HelloController {
    
    @RequestMapping(path = "/hello", method = RequestMethod.GET) // Phần còn lại
    public ResponseEntity<String> sayHello() { ... }
}
```

**Ví dụ 2: Toàn bộ ở method**

```java
@Controller
public class HelloController {
    
    @RequestMapping(path = "/say/hello", method = RequestMethod.GET)
    public ResponseEntity<String> sayHello() { ... }
}
```

-----

### 4\. Các thuộc tính (attributes) của `@RequestMapping`

Để "lọc" request một cách chính xác, `@RequestMapping` cho phép bạn chỉ định nhiều điều kiện. Câu trả lời của bạn đã liệt kê rất đủ:

* `path` (hoặc `value`): Đường dẫn URI (ví dụ: `/say/hello`).
* `method`: Phương thức HTTP (ví dụ: `GET`, `POST`).
* `params`: Yêu cầu request phải có tham số nhất định (ví dụ: `params = "userId"` yêu cầu phải có `?userId=...`).
* `headers`: Yêu cầu request phải có header nhất định (ví dụ: `headers = "Accept=application/json"`).
* `consumes`: Định nghĩa kiểu `Content-Type` mà phương thức này có thể **nhận vào** (ví dụ: `consumes = "application/json"` cho `POST`).
* `produces`: Định nghĩa kiểu `Content-Type` mà phương thức này sẽ **trả về** (ví dụ: `produces = "application/pdf"`).

**Ví dụ phức tạp:**

```java
// Phương thức này CHỈ CHẠY khi:
// 1. Request là POST tới /api/v1/users
// 2. Header 'Content-Type' phải là application/json
// 3. Header 'Accept' phải là application/xml
// 4. Phải có query param là 'new=true'
@PostMapping(path = "/api/v1/users",
             consumes = "application/json",
             produces = "application/xml",
             params = "new=true")
public UserInXml createUser(...) { ... }
```

-----

### 5\. Các Annotation rút gọn (Composed Annotations)

Vì `@RequestMapping(method = GET)` quá dài dòng, Spring cung cấp các annotation rút gọn, tiện lợi hơn:

* **`@GetMapping`** (thay cho `@RequestMapping(method = GET)`)
* **`@PostMapping`**
* **`@PutMapping`**
* **`@DeleteMapping`**
* **`@PatchMapping`**

Chúng hoàn toàn tương đương và là cách làm được khuyên dùng (preferred) hiện nay vì code rõ ràng, dễ đọc hơn.

**Ví dụ (tương đương):**

```java
// Cách cũ
@RequestMapping(path = "/say/hello", method = RequestMethod.GET)
public ResponseEntity<String> sayHello() { ... }

// Cách mới (tốt hơn)
@GetMapping(path = "/say/hello")
public ResponseEntity<String> sayHello() { ... }
```

> **Lưu ý:** Như bạn đã chỉ ra, có một số trường hợp hiếm (như `HTTP HEAD` hoặc `OPTIONS`) mà bạn vẫn phải dùng `@RequestMapping` vì chúng không có annotation rút gọn.

## ❓ Câu hỏi: What is the difference between @RequestMapping and @GetMapping?

(Sự khác biệt giữa @RequestMapping và @GetMapping là gì?)

### 1\. Sự khác biệt cốt lõi: Tính tổng quát vs. Tính chuyên biệt

Sự khác biệt chính nằm ở mục đích sử dụng của chúng:

* **`@RequestMapping`** (Tổng quát): Đây là annotation "mẹ", linh hoạt nhất. Nó có thể được sử dụng để ánh xạ (map) **bất kỳ** phương thức HTTP nào (GET, POST, PUT, DELETE, v.v.). Bạn phải chỉ định phương thức bạn muốn thông qua thuộc tính `method`.
* **`@GetMapping`** (Chuyên biệt): Đây là một annotation "con", chuyên biệt. Nó là một lối viết tắt (shortcut) và *chỉ* dùng để ánh xạ các request có phương thức là **HTTP GET**.

-----

### 2\. @GetMapping là một "Composed Annotation"

Như bạn đã nói, `@GetMapping` là một *composed annotation* (chú thích ghép). Điều này có nghĩa là bản thân nó được "cấu tạo" từ `@RequestMapping`.

Nói cách khác, hai đoạn code sau đây là **tương đương 100%**:

**Cách 1: Dùng `@RequestMapping`**

```java
@RequestMapping(path = "/api/users", method = RequestMethod.GET)
public ResponseEntity<String> getUsers() {
    // ...
}
```

**Cách 2: Dùng `@GetMapping` (ngắn gọn và rõ ràng hơn)**

```java
@GetMapping(path = "/api/users")
public ResponseEntity<String> getUsers() {
    // ...
}
```

-----

### 3\. So sánh tính năng

| Tính năng | `@RequestMapping` | `@GetMapping` |
| :--- | :--- | :--- |
| **Chỉ định đường dẫn (path)** | **Có** (dùng `path` hoặc `value`) | **Có** (dùng `path` hoặc `value`) |
| **Chỉ định phương thức HTTP** | **Có** (dùng `method = ...`) | **Không** (Luôn luôn là GET) |
| **Lọc theo `params`** | **Có** | **Có** |
| **Lọc theo `headers`** | **Có** | **Có** |
| **Chỉ định `consumes`** | **Có** | **Có** |
| **Chỉ định `produces`** | **Có** | **Có** |

Như bạn thấy, `@GetMapping` hỗ trợ tất cả các thuộc tính lọc (filtering) mạnh mẽ của `@RequestMapping` (như `params`, `headers`...), chỉ trừ việc nó đã "chốt cứng" phương thức là `GET`.

-----

### 4\. ⚠️ Cảnh báo khi dùng `@RequestMapping`

Một điểm quan trọng cần lưu ý (và dễ bị hỏi thi):

> Nếu bạn dùng `@RequestMapping` mà **quên** chỉ định thuộc tính `method`, nó sẽ mặc định ánh xạ với **TẤT CẢ** các phương thức HTTP (GET, POST, PUT, v.v.).

**Ví dụ (KHÔNG NÊN LÀM):**

```java
// LỖI BẢO MẬT TIỀM ẨN!
// Ánh xạ này chấp nhận cả GET, POST, DELETE... tới /api/users
@RequestMapping("/api/users") 
public ResponseEntity<String> handleAllMethods() {
    // ...
}
```

Đây là lý do tại sao việc dùng các annotation chuyên biệt như `@GetMapping` lại được **khuyến khích**: Nó giúp code của bạn rõ ràng hơn và tránh các lỗi vô tình mở cổng cho các phương thức HTTP không mong muốn.

-----

### 5\. Các "Anh em" khác của @GetMapping

Spring cung cấp một bộ đầy đủ các composed annotation, như bạn đã liệt kê (tôi xin phép sửa lại một chút `PathMapping` thành `PatchMapping`):

* **`@PostMapping`**: Chuyên cho HTTP POST.
* **`@PutMapping`**: Chuyên cho HTTP PUT.
* **`@DeleteMapping`**: Chuyên cho HTTP DELETE.
* **`@PatchMapping`**: Chuyên cho HTTP PATCH.

**Kết luận:** Luôn ưu tiên sử dụng các annotation chuyên biệt (`@GetMapping`, `@PostMapping`, v.v.) vì chúng giúp code dễ đọc và an toàn hơn. Chỉ sử dụng `@RequestMapping` khi bạn có lý do đặc biệt (ví dụ: cần map nhiều phương thức HTTP vào cùng một method, hoặc map các phương thức hiếm như `HEAD`, `OPTIONS`).

## ❓ Câu hỏi: What is @RequestParam used for?

(@RequestParam được sử dụng để làm gì?)

### 1\. @RequestParam dùng để làm gì?

`@RequestParam` là một annotation (chú thích) dùng để **trích xuất và ràng buộc (bind) một giá trị từ tham số của request web** (web request parameter) vào một tham số (parameter) của phương thức trong Controller.

Nói một cách đơn giản nhất: Nó dùng để lấy dữ liệu từ **query string** (phần đằng sau dấu `?` trên URL) hoặc từ **dữ liệu form** (form data) được gửi lên.

-----

### 2\. Ví dụ cơ bản

Hãy xem xét URL này:
`GET /api/users?name=John&city=NYC`

Để lấy được giá trị "John" và "NYC" trong Controller, bạn sẽ dùng `@RequestParam`:

```java
@GetMapping("/api/users")
public String getUser(
    @RequestParam("name") String userName,  // Spring gán "John" vào biến userName
    @RequestParam("city") String userCity   // Spring gán "NYC" vào biến userCity
) {
    // Bây giờ bạn có thể dùng các biến:
    // userName sẽ là "John"
    // userCity sẽ là "NYC"
    // ...
    return "User: " + userName + " from " + userCity;
}
```

> **Ghi chú tiện lợi:** Nếu tên biến Java của bạn **giống hệt** tên tham số trong URL, bạn có thể bỏ qua phần `("name")`:
>
> ```java
> @GetMapping("/api/users")
> public String getUser(@RequestParam String name, @RequestParam String city) {
>     // Hoạt động y hệt!
> }
> ```

-----

### 3\. Các thuộc tính (attributes) quan trọng

`@RequestParam` có 3 thuộc tính chính bạn cần nắm rõ:

#### a. `name` (hoặc `value`)

Chỉ định tên của tham số trong URL cần lấy.

* `@RequestParam(name = "name") String userName` sẽ tìm `?name=...`

#### b. `required` (Quan trọng)

Xác định xem tham số này có **bắt buộc** hay không.

* **`required = true` (Mặc định):**
    * Nếu tham số bị thiếu trong URL (ví dụ: request chỉ là `/api/users` mà không có `?name=...`), Spring sẽ ném ra một ngoại lệ (Exception) và request sẽ thất bại (lỗi 400 Bad Request).
* **`required = false`:**
    * Nếu tham số bị thiếu, Spring sẽ gán giá trị `null` cho biến đó (hoặc `0` cho kiểu `int`, `false` cho `boolean`). Request vẫn được xử lý bình thường.

#### c. `defaultValue`

Chỉ hoạt động khi `required = false`. Nó cung cấp một giá trị **mặc định** nếu tham số bị thiếu.

```java
@GetMapping("/search")
public String search(
    @RequestParam("query") String query, // Bắt buộc
    @RequestParam(name = "page", required = false, defaultValue = "1") int pageNumber
) {
    // Nếu request là: /search?query=spring
    // query = "spring"
    // pageNumber = 1 (vì ta đã cung cấp defaultValue)
    
    // Nếu request là: /search?query=spring&page=3
    // query = "spring"
    // pageNumber = 3
}
```

-----

### 4\. Cách xử lý tham số tùy chọn (Optional)

Như bạn đã lưu ý, thay vì dùng `required = false`, cách làm hiện đại (từ Java 8) và an toàn hơn (tránh `null`) là sử dụng `Optional`.

Hai cách sau đây có ý nghĩa **tương tự nhau** (tham số "city" là không bắt buộc):

**Cách 1: Dùng `required = false` (Có thể bị `null`)**

```java
@GetMapping("/index")
public String index(@RequestParam(value = "city", required = false) String city) {
    if (city != null) {
        // ...
    }
}
```

**Cách 2: Dùng `Optional<String>` (Cách làm tốt hơn, không `null`)**

```java
@GetMapping("/index")
public String index(@RequestParam(value = "city") Optional<String> city) {
    if (city.isPresent()) {
        String cityValue = city.get();
        // ...
    }
    // hoặc city.ifPresent(cityValue -> { ... });
}
```

-----

### 5\. Các trường hợp sử dụng nâng cao

`@RequestParam` cũng rất mạnh mẽ trong việc xử lý nhiều giá trị:

#### a. Lấy tất cả tham số vào `Map`

Nếu bạn không biết trước có bao nhiêu tham số, bạn có thể lấy tất cả vào một `Map`.

URL: `/index?name=John&city=NYC&country=US`

```java
@GetMapping("/index")
public String index(@RequestParam Map<String, String> allParams) {
    // allParams sẽ là một Map chứa:
    // { "name": "John", "city": "NYC", "country": "US" }
}
```

#### b. Lấy nhiều giá trị vào `List` hoặc Mảng

Điều này xảy ra khi một tham số xuất hiện nhiều lần hoặc chứa danh sách phân tách bằng dấu phẩy.

**Trường hợp 1: Tên tham số lặp lại**
URL: `/filter?type=A&type=B&type=C`

```java
@GetMapping("/filter")
public String filter(@RequestParam("type") List<String> types) {
    // types sẽ là một List: ["A", "B", "C"]
}
```

**Trường hợp 2: Giá trị phân tách bằng dấu phẩy (như ví dụ của bạn)**
URL: `/index?cities=1,2,3`

```java
@GetMapping("/index")
public String index(@RequestParam("cities") List<String> cities) {
    // Spring đủ thông minh để chuyển đổi thành List: ["1", "2", "3"]
}
```

-----

### 6\. @RequestParam lấy dữ liệu từ đâu?

Như bạn đã tóm tắt chính xác, `Servlet API` gộp chung các nguồn này, vì vậy `@RequestParam` có thể lấy dữ liệu từ:

1.  **Query Parameters:** `GET /search?q=test` (Phần sau dấu `?`)
2.  **Form Data:** Khi bạn submit một form HTML (với `Content-Type: application/x-www-form-urlencoded`), các trường `input` sẽ được gửi dưới dạng tham số.
3.  **Multipart Request Parts:** (Ít phổ biến hơn) Các trường trong một form `multipart/form-data`.
    Chào bạn, đây là một câu hỏi rất cơ bản nhưng cực kỳ quan trọng để phân biệt cách Spring "đọc" một URL. Câu trả lời của bạn đã nêu bật được ý chính xác.

Chúng ta hãy dùng một ví dụ để thấy rõ sự khác biệt.

Hãy tưởng tượng bạn có một URL:
`https://api.example.com/users/123?sort=desc`

* Phần `/users/123` là **Đường dẫn (Path)**. Nó giống như "địa chỉ" của một tài nguyên cụ thể (người dùng số 123).
* Phần `?sort=desc` là **Tham số truy vấn (Query Parameter)**. Nó giống như một "yêu cầu tùy chọn" hoặc "bộ lọc" cho tài nguyên đó (lấy người dùng đó, nhưng sắp xếp theo thứ tự giảm dần).

## ❓ Câu hỏi: What are the differences between @RequestParam and @PathVariable?

(Sự khác biệt giữa @RequestParam và @PathVariable là gì?)

### 1\. `@PathVariable` (Biến trong đường dẫn)

* **Mục đích:** Dùng để **trích xuất giá trị từ chính đường dẫn (path) của URL**, dựa trên một khuôn mẫu (template) mà bạn định nghĩa.
* **Cách hoạt động:** Bạn phải định nghĩa các "biến" trong đường dẫn của mapping bằng dấu ngoặc nhọn `{...}`. `@PathVariable` sẽ "bắt" giá trị ở vị trí tương ứng.
* **Ví dụ:**
    * URL được gọi: `GET /countries/US/cities/DEN`
    * Mapping trong Controller:
  <!-- end list -->
  ```java
  @GetMapping("/countries/{country}/cities/{city}")
  public String getCityInfo(
      @PathVariable("country") String countryCode, // countryCode sẽ là "US"
      @PathVariable("city") String cityCode        // cityCode sẽ là "DEN"
  ) {
      // ...
  }
  ```
* **Khi nào dùng:** Khi bạn muốn xác định một **tài nguyên cụ thể** (ví dụ: lấy user theo ID, lấy bài viết theo "slug").

-----

### 2\. `@RequestParam` (Tham số truy vấn)

* **Mục đích:** Dùng để **trích xuất giá trị từ các tham số truy vấn** (phần đằng sau dấu `?`) hoặc từ dữ liệu của một form (form data).
* **Cách hoạt động:** Nó tìm một cặp `key=value` trong URL.
* **Ví dụ:**
    * URL được gọi: `GET /index?name=John&city=NYC`
    * Mapping trong Controller:
  <!-- end list -->
  ```java
  @GetMapping("/index")
  public String getUserInfo(
      @RequestParam("name") String userName,   // userName sẽ là "John"
      @RequestParam("city") String userCity,   // userCity sẽ là "NYC"
      @RequestParam(name = "age", required = false) Integer age // age sẽ là null nếu URL không có
  ) {
      // ...
  }
  ```
* **Khi nào dùng:** Khi bạn muốn cung cấp các **thông tin tùy chọn**, **lọc (filtering)**, **sắp xếp (sorting)**, hoặc **phân trang (pagination)**.

-----

### 3\. Bảng so sánh nhanh

| Tính năng | `@PathVariable` | `@RequestParam` |
| :--- | :--- | :--- |
| **Nguồn dữ liệu** | Từ **Đường dẫn (Path)** của URL | Từ **Query String** (sau dấu `?`) |
| **Ví dụ URL** | `/users/{id}` | `/users?id=123` |
| **Khai báo** | `@{Mapping}("/users/{id}")` | `@{Mapping}("/users")` |
| **Mặc định** | Luôn luôn **bắt buộc** (`required=true`) | Có thể tùy chọn (`required=false`) |
| **Giá trị mặc định** | **KHÔNG** có `defaultValue` | **CÓ** `defaultValue` |

-----

### 4\. Các điểm khác biệt và tương đồng chính (Như bạn đã nêu)

#### 🎯 Khác biệt chính

1.  **Mục đích:** `@PathVariable` để xác định tài nguyên (lấy `id` từ `/users/123`). `@RequestParam` để lọc/cấu hình tài nguyên đó (lấy `sort` từ `/users/123?sort=asc`).
2.  **`defaultValue`:** Đây là một điểm khác biệt quan trọng.
    * Bạn **có thể** dùng `defaultValue` với `@RequestParam` (ví dụ: `defaultValue="1"` cho số trang).
    * Bạn **không thể** dùng `defaultValue` với `@PathVariable`. Lý do là nếu một phần của đường dẫn bị thiếu, Spring sẽ không "khớp" (match) mapping đó ngay từ đầu (ví dụ: request `/countries/US/cities` sẽ không khớp với `/countries/{country}/cities/{city}`).

#### 👍 Tương đồng

Cả hai đều rất linh hoạt và có chung các đặc điểm:

* Đều có thể chỉ định `name` (tên biến) cần bind.
* Đều có thể đánh dấu là không bắt buộc (dùng `required = false` hoặc `Optional<T>`).
* Đều có thể map tất cả các giá trị vào một `Map<String, String>`.

## ❓ Câu hỏi: What are some of the parameter types for a controller method

(Một số kiểu tham số cho một phương thức controller là gì?)

### 1\. 🖥️ Truy cập Request / Response (Cấp thấp)

Đây là nhóm tham số cho phép bạn truy cập trực tiếp vào các đối tượng `Servlet` API gốc. Bạn nên dùng chúng khi cần kiểm soát "thô" (raw) hoặc truy cập vào các tính năng mà Spring không trừu tượng hóa.

* **`javax.servlet.ServletRequest`** (hoặc `HttpServletRequest`): Cho phép bạn truy cập đầy đủ thông tin request gốc, bao gồm headers, cookies, tham số (parameters), và các thuộc tính (attributes).
* **`javax.servlet.ServletResponse`** (hoặc `HttpServletResponse`): Cho phép bạn kiểm soát hoàn toàn response, ví dụ như set headers, status code, hoặc tự ghi dữ liệu vào `OutputStream`.
* **`java.io.InputStream`** / **`java.io.Reader`**: Cho phép bạn đọc nội dung (body) của request một cách trực tiếp, từng byte một.
* **`java.io.OutputStream`** / **`java.io.Writer`**: Cho phép bạn ghi nội dung (body) vào response một cách trực tiếp.

-----

### 2\. 📦 Lớp trừu tượng của Spring (Cách làm ưu tiên)

Đây là "cách làm của Spring", cung cấp các lớp trừu tượng (wrapper) để bạn không bị phụ thuộc cứng vào Servlet API, giúp cho việc viết unit test dễ dàng hơn.

* **`WebRequest`** / **`NativeWebRequest`**: Tương tự như `ServletRequest` nhưng là một interface của Spring. Nó cung cấp quyền truy cập vào request/session attributes mà không cần dùng API của Servlet.

* **`HttpEntity<B>`**: Một tham số rất mạnh mẽ. Nó là một đối tượng chứa cả **Request Headers** (tiêu đề) và **Request Body** (nội dung). Spring sẽ tự động sửu dụng `HttpMessageConverter` để chuyển đổi body (ví dụ: JSON) thành đối tượng kiểu `B` (ví dụ: `User`).

  ```java
  // Spring tự động chuyển đổi JSON trong body thành đối tượng User
  @PostMapping("/users")
  public String createUser(HttpEntity<User> userEntity) {
      User user = userEntity.getBody();
      HttpHeaders headers = userEntity.getHeaders();
      // ...
  }
  ```

  *(Điều này tương tự như việc dùng `@RequestBody User user` và `@RequestHeader HttpHeaders headers`)*

-----

### 3\. 🗂️ Truyền dữ liệu vào Model (Để hiển thị View)

Khi bạn không dùng `@RestController` (tức là bạn đang trả về tên View để render HTML), bạn cần một nơi để "gửi" dữ liệu từ Controller sang View.

* **`java.util.Map`**
* **`org.springframework.ui.Model`**
* **`org.springframework.ui.ModelMap`**

Về cơ bản, cả ba tham số này đều có **cùng một mục đích**: Chúng là một "cái túi" để bạn đặt dữ liệu vào. Spring sẽ tự động tạo ra một đối tượng `Model` và tiêm (inject) nó vào phương thức của bạn.

```java
@GetMapping("/welcome")
public String welcome(Model model) {
    // Thêm thuộc tính "name" vào model
    model.addAttribute("name", "Guest"); 
    // Trả về tên view "welcome". 
    // File welcome.html (Thymeleaf) bây giờ có thể truy cập biến ${name}.
    return "welcome"; 
}
```

-----

### 4\. 🗎 Quản lý Trạng thái (State) và Session

Nhóm này dùng để quản lý dữ liệu tồn tại lâu hơn một request, ví dụ như giỏ hàng hoặc thông báo sau khi chuyển hướng.

* **`javax.servlet.http.HttpSession`**: Cho phép truy cập trực tiếp vào đối tượng session của servlet.

* **`RedirectAttributes`**: Cực kỳ quan trọng cho mẫu thiết kế **Post-Redirect-Get (PRG)**. Nó cho phép bạn thêm thuộc tính vào URL chuyển hướng hoặc thêm "Flash Attributes".

    * **Flash Attributes:** Là các thuộc tính được lưu tạm (thường là trong session) và chỉ **tồn tại cho đến khi request *tiếp theo* (sau khi redirect) được hoàn thành**. Rất lý tưởng để hiển thị các thông báo thành công/lỗi.

  <!-- end list -->

  ```java
  @PostMapping("/users")
  public String saveUser(User user, RedirectAttributes redirectAttributes) {
      userService.save(user);
      // "message" sẽ sống sót qua 1 lần redirect
      redirectAttributes.addFlashAttribute("message", "Tạo người dùng thành công!");
      return "redirect:/users"; // Chuyển hướng về trang danh sách
  }

  @GetMapping("/users")
  public String listUsers(Model model) {
      // "message" từ flash attribute sẽ tự động được thêm vào model
      // ...
      return "user-list";
  }
  ```

* **`SessionStatus`**: Dùng kết hợp với annotation `@SessionAttributes` (đặt ở cấp độ class) để báo cho Spring biết: "Quá trình xử lý (ví dụ: một form nhiều bước) đã hoàn tất, hãy xóa các thuộc tính này khỏi session." (bằng cách gọi `status.setComplete()`).

-----

### 5\. ✅ Validation (Kiểm thực dữ liệu)

* **`Errors`** / **`BindingResult`**: Dùng để bắt các lỗi khi Spring cố gắng bind (ràng buộc) dữ liệu từ request vào một đối tượng (POJO) và các lỗi validation (nếu bạn dùng `@Valid`).

* **QUAN TRỌNG:** Tham số `BindingResult` **phải** được đặt **ngay sau** tham số mà nó kiểm tra.

  ```java
  // BindingResult phải đặt ngay sau @ModelAttribute "user"
  @PostMapping("/create")
  public String createUser(
          @Valid @ModelAttribute("user") User user, 
          BindingResult result, // Chứa kết quả validation của "user"
          Model model
  ) {
      if (result.hasErrors()) {
          // Nếu có lỗi, quay lại form "create-form"
          return "create-form"; 
      }
      //... lưu user
      return "redirect:/success";
  }
  ```

-----

### 6\. 👤 Bảo mật và Bối cảnh (Security & Context)

Các tham số này cung cấp thông tin về môi trường và người dùng.

* **`java.security.Principal`**: Nếu bạn đang dùng Spring Security, tham số này sẽ chứa thông tin về người dùng đã được xác thực (ví dụ: `principal.getName()` để lấy username).
* **`HttpMethod`**: Cho bạn biết request này được gọi bằng phương thức nào (`GET`, `POST`, v.v.).
* **`java.util.Locale`**, **`java.util.TimeZone`** / **`java.time.ZoneId`**: Hữu ích cho việc quốc tế hóa (i18n), để hiển thị ngôn ngữ hoặc múi giờ đúng cho người dùng.

-----

### 7\. 🛠️ Tiện ích và Các quy tắc mặc định (Rất quan trọng)

* **`UriComponentsBuilder`**: Một công cụ tiện ích mạnh mẽ để xây dựng các URL một cách an toàn (ví dụ: để tạo link HATEOAS) dựa trên thông tin của request hiện tại (host, port...).

* **`javax.servlet.http.PushBuilder`**: Dùng cho tính năng HTTP/2 Server Push (một chủ đề nâng cao).

* **Quy tắc Mặc định (Default Binding):** Đây là điểm cuối cùng trong danh sách của bạn và rất quan trọng.

    1.  **Kiểu đơn giản (Simple Type):** Nếu một tham số là kiểu đơn giản (như `String`, `int`, `boolean`...) và *không* có annotation nào, Spring sẽ tự động coi nó là **`@RequestParam`**.
        ```java
        // Tương đương với @RequestParam("name") String name
        public String hello(String name) { ... } 
        ```
    2.  **Kiểu phức tạp (Complex Type / POJO):** Nếu một tham số là một đối tượng phức tạp (POJO, ví dụ: lớp `User` của bạn) và *không* có annotation nào, Spring sẽ tự động coi nó là **`@ModelAttribute`**. Spring sẽ:
        * Tạo một thể hiện (instance) mới của `User`.
        * Cố gắng bind các tham số request (query params hoặc form data) vào các trường (fields) của `User` (ví dụ: `?username=john` sẽ gọi `user.setUsername("john")`).
        * Tự động thêm đối tượng `User` này vào `Model`.

Chào bạn, đây là một câu hỏi bao quát, yêu cầu bạn liệt kê và hiểu rõ các "công cụ" khác nhau mà Spring MVC cung cấp để trích xuất thông tin từ một HTTP request.

Danh sách của bạn rất đầy đủ và chính xác. Chúng ta có thể phân loại chúng thành các nhóm chính để dễ nhớ hơn:

-----

### 1\. 🗺️ Lấy dữ liệu từ đường dẫn (URL Path)

Nhóm này đọc thông tin được nhúng trực tiếp vào cấu trúc URL.

* **`@PathVariable`**:

    * **Công dụng:** Dùng để lấy các "biến" từ khuôn mẫu URI.
    * **Ví dụ:** URL `GET /users/123`

  <!-- end list -->

  ```java
  @GetMapping("/users/{userId}")
  public User getUser(@PathVariable("userId") long userId) {
      // userId sẽ là 123
  }
  ```

* **`@MatrixVariable`**:

    * **Công dụng:** Một tính năng ít dùng hơn, cho phép bạn lấy các cặp key-value *bên trong một phân đoạn (segment) của URL*, phân tách bằng dấu `;` (theo chuẩn RFC 3986).
    * **Ví dụ:** URL `GET /employees/id=1;name=John`

  <!-- end list -->

  ```java
  @GetMapping("/employees/{empData}")
  public Employee getEmployee(
      @MatrixVariable(name="id", pathVar="empData") int id,
      @MatrixVariable(name="name", pathVar="empData") String name
  ) {
      // id sẽ là 1, name sẽ là "John"
  }
  ```

-----

### 2\. ❓ Lấy dữ liệu từ Tham số (Query) và Form

* **`@RequestParam`**:
    * **Công dụng:** Dùng để lấy tham số truy vấn (sau dấu `?`) hoặc dữ liệu từ form (`application/x-www-form-urlencoded`).
    * **Ví dụ:** URL `GET /search?q=spring&page=1`
  <!-- end list -->
  ```java
  @GetMapping("/search")
  public String search(
      @RequestParam("q") String query,
      @RequestParam(name = "page", defaultValue = "1") int page
  ) {
      // query sẽ là "spring", page sẽ là 1
  }
  ```

-----

### 3\. 📦 Lấy dữ liệu từ "Thân" (Body) của Request

Nhóm này đọc nội dung chính của request, thường dùng cho `POST`, `PUT`, `PATCH`.

* **`@RequestBody`**:

    * **Công dụng:** Cực kỳ quan trọng cho các REST API. Nó lấy *toàn bộ* nội dung body của request (thường là JSON hoặc XML) và tự động chuyển đổi (deserialize) nó thành một đối tượng Java (POJO) bằng cách sử dụng `HttpMessageConverter`.
    * **Ví dụ:** `POST /users` với body là JSON `{"name":"John", "age":30}`

  <!-- end list -->

  ```java
  @PostMapping("/users")
  public void createUser(@Valid @RequestBody User user) {
      // Spring tự động tạo đối tượng User với name="John" và age=30
  }
  ```

* **`@RequestPart`**:

    * **Công dụng:** Dùng riêng cho các request `multipart/form-data` (thường là để upload file). Nó cho phép bạn lấy một "phần" (part) cụ thể của request đó. Bạn có thể dùng nó để lấy file (`MultipartFile`) hoặc lấy một trường dữ liệu (ví dụ: JSON) đi kèm với file.
    * **Ví dụ:** `POST /upload` (với 1 file tên "file" và 1 trường JSON tên "metadata")

  <!-- end list -->

  ```java
  @PostMapping("/upload")
  public void upload(
      @RequestPart("file") MultipartFile file,
      @RequestPart("metadata") MetaData meta
  ) {
      // Xử lý file và metadata...
  }
  ```

-----

### 4\. 📰 Lấy dữ liệu từ Metadata (Headers & Cookies)

* **`@RequestHeader`**:

    * **Công dụng:** Dùng để đọc giá trị của một HTTP Header cụ thể.
    * **Ví dụ:** `GET /me` với header `Authorization: Bearer <token>`

  <!-- end list -->

  ```java
  @GetMapping("/me")
  public String getMyInfo(@RequestHeader("Authorization") String authHeader) {
      // authHeader sẽ là "Bearer <token>"
      // Bạn cũng có thể lấy tất cả header vào Map<String, String>
  }
  ```

* **`@CookieValue`**:

    * **Công dụng:** Dùng để đọc giá trị của một HTTP Cookie cụ thể.
    * **Ví dụ:** `GET /dashboard` với cookie `sessionId=xyz123`

  <!-- end list -->

  ```java
  @GetMapping("/dashboard")
  public String getDashboard(@CookieValue("sessionId") String session) {
      // session sẽ là "xyz123"
  }
  ```

-----

### 5\. 🗄️ Lấy dữ liệu từ Thuộc tính (Attributes) phía Server

Nhóm này không lấy dữ liệu trực tiếp từ người dùng, mà lấy từ các đối tượng đã được *lưu trữ ở phía server* trong quá trình xử lý request (ví dụ: bởi một `Filter` hoặc `Interceptor`).

* **`@RequestAttribute`**:

    * **Công dụng:** Lấy một đối tượng đã được "gắn" vào `HttpServletRequest` (với `request.setAttribute(...)`). Thường dùng khi một `Filter` (ví dụ: Filter xác thực) đã xử lý thông tin và muốn truyền nó cho Controller.
    * **Ví dụ:** Một `AuthFilter` đặt `request.setAttribute("userId", 123)`.

  <!-- end list -->

  ```java
  @GetMapping("/my-endpoint")
  public String myEndpoint(@RequestAttribute("userId") long userId) {
      // userId sẽ là 123
  }
  ```

* **`@SessionAttribute`**:

    * **Công dụng:** Lấy một đối tượng đã được lưu trong `HttpSession`. (Lưu ý: đối tượng này phải *tồn tại từ trước*).
    * **Ví dụ:** Người dùng đăng nhập, server lưu `session.setAttribute("userCart", cart)`.

  <!-- end list -->

  ```java
  @GetMapping("/cart")
  public String getCart(@SessionAttribute("userCart") ShoppingCart cart) {
      // Lấy giỏ hàng từ session
  }
  ```

* **`@ModelAttribute`**:

    * **Công dụng (khi là tham số):** Đây là một annotation "ma thuật". Nó bảo Spring:
        1.  Hãy tìm một đối tượng trong `Model` có tên này.
        2.  Nếu không tìm thấy, hãy tạo một đối tượng mới (POJO).
        3.  Sau đó, tự động bind (gán) các tham số request (query params, form data) vào các trường (fields) của đối tượng đó.
    * **Ví dụ:** Dùng cho Form-based HTML (không phải JSON/API). `POST /register` với form data `username=test&pass=123`

  <!-- end list -->

  ```java
  @PostMapping("/register")
  public String register(@ModelAttribute("user") User user) {
      // Spring sẽ tự:
      // 1. new User()
      // 2. user.setUsername("test")
      // 3. user.setPass("123")
      // 4. Tự động thêm "user" này vào Model
      return "success";
  }
  ```

-----

### ⚠️ Lưu ý về `@SessionAttributes` (ở cấp độ Class)

* Bạn đã liệt kê **`@SessionAttributes`** (có chữ 's' ở cuối). Đây là một điểm rất hay\!
* **Khác biệt:** Annotation này **không phải là tham số của phương thức**. Nó được đặt *trên tên class* của Controller.
* **Công dụng:** Nó bảo Spring: "Nếu một `@ModelAttribute` nào đó được đặt vào `Model` và có tên khớp với tên trong `@SessionAttributes`, hãy tự động sao chép nó vào `HttpSession` để nó tồn tại qua nhiều request."
* Đây chính là cơ chế dùng cho các "wizard" (form nhiều bước).

## ❓ Câu hỏi: What are some of the valid return types of a controller method?

(Một số kiểu trả về hợp lệ của một phương thức controller là gì?)

Một phương thức trong Controller có thể trả về nhiều kiểu dữ liệu khác nhau. Kiểu trả về sẽ báo cho Spring MVC biết *phải làm gì tiếp theo*—liệu nó nên render một file HTML, chuyển đổi một đối tượng thành JSON, hay xử lý một tác vụ bất đồng bộ.

Dưới đây là các nhóm kiểu trả về chính:

### 1\. 🖥️ Trả về Dữ liệu (Dành cho REST APIs)

Khi bạn xây dựng một REST API (thường dùng với `@RestController`), mục tiêu của bạn là trả về dữ liệu (như JSON/XML), không phải HTML.

* **`HttpEntity<B>`** hoặc **`ResponseEntity<B>`**

    * Đây là cách làm **mạnh mẽ nhất**. Nó cho phép bạn kiểm soát *mọi thứ* trong HTTP Response:
        * **Body (Thân):** Dữ liệu bạn muốn gửi (sẽ được `HttpMessageConverter` chuyển đổi).
        * **Headers (Tiêu đề):** Bất kỳ HTTP header nào (ví dụ: `Content-Type`, `Location`).
        * **Status Code (Mã trạng thái):** Chỉ có ở `ResponseEntity`. Đây là cách chuẩn để trả về các mã như `201 Created`, `404 Not Found`, `400 Bad Request`.

  <!-- end list -->

  ```java
  @GetMapping("/users/{id}")
  public ResponseEntity<User> getUser(@PathVariable long id) {
      User user = userService.findById(id);
      if (user == null) {
          // Trả về 404 Not Found (không có body)
          return ResponseEntity.notFound().build(); 
      }
      // Trả về 200 OK với body là đối tượng user (sẽ thành JSON)
      return ResponseEntity.ok(user); 
  }
  ```

* **Một POJO hoặc Collection (với `@ResponseBody`)**

    * Nếu class của bạn được đánh dấu là `@RestController`, thì annotation `@ResponseBody` đã được ngầm định (implicit).
    * Bạn chỉ cần trả về đối tượng (`User`, `List<Product>`). Spring sẽ tự động dùng `HttpMessageConverter` (như Jackson) để chuyển nó thành JSON và trả về với mã `200 OK`.
    * Nếu bạn dùng `@Controller` (không phải `@RestController`), bạn phải tự thêm **`@ResponseBody`** vào phương thức để kích hoạt hành vi này.

* **`HttpHeaders`**

    * Một trường hợp đặc biệt khi bạn chỉ muốn trả về các HTTP header mà *không có body*.

-----

### 2\. 📄 Trả về Giao diện Web (Dành cho MVC truyền thống)

Khi bạn xây dựng một ứng dụng web server-side rendering (ví dụ: dùng Thymeleaf, JSP), mục tiêu của bạn là trả về HTML.

* **`String`**

    * Đây là cách phổ biến nhất. `String` trả về chính là **tên logic của View** (logical view name).
    * `DispatcherServlet` sẽ đưa tên này cho `ViewResolver` để tìm ra file template thực sự (ví dụ: trả về `"home"` -\> `ViewResolver` tìm file `/templates/home.html`).
    * Dữ liệu cho View được cung cấp bằng cách thêm tham số `Model` vào phương thức.

  <!-- end list -->

  ```java
  @GetMapping("/welcome")
  public String welcome(Model model) {
      model.addAttribute("message", "Hello World!");
      return "welcome"; // Spring sẽ tìm view tên "welcome" (ví dụ: welcome.html)
  }
  ```

* **`ModelAndView`**

    * Đây là cách làm "cổ điển" của Spring MVC. Nó là một đối tượng container chứa *cả* Model (dữ liệu) *lẫn* View (tên view hoặc đối tượng View).

  <!-- end list -->

  ```java
  @GetMapping("/welcome_mav")
  public ModelAndView welcome() {
      ModelAndView mav = new ModelAndView();
      mav.setViewName("welcome"); // Đặt tên view
      mav.addObject("message", "Hello from ModelAndView!"); // Thêm dữ liệu
      return mav;
  }
  ```

* **`View`**

    * Bạn có thể tự tạo và trả về một đối tượng `View` cụ thể (ví dụ: `new JstlView(...)`). Cách này ít dùng.

* **`Map`** hoặc **`Model`**

    * Nếu bạn chỉ trả về `Map` hoặc `Model` (tức là chỉ trả về dữ liệu), Spring sẽ **tự suy luận tên View** (implicit view name) dựa trên URL của request.
    * Ví dụ: Request đến `/users/list` sẽ tự động tìm view tên `users/list`.

* **`@ModelAttribute`** (trên phương thức)

    * Tương tự như trả về `Map/Model`, nếu bạn trả về một POJO và đánh dấu phương thức là `@ModelAttribute`, đối tượng đó sẽ được thêm vào model và Spring sẽ tự suy luận tên View.

-----

### 3\. ⏳ Xử lý Bất đồng bộ (Asynchronous Processing)

Dùng khi bạn có một tác vụ tốn thời gian (như gọi API bên ngoài, query database lớn) và bạn không muốn "block" (giữ) thread của web server.

* **`Callable<V>`**

    * Cách đơn giản nhất. Bạn trả về một `Callable`. Spring sẽ lấy `Callable` này và thực thi nó trên một thread khác (do Spring quản lý). Thread của server (ví dụ: Tomcat thread) được giải phóng ngay lập tức. Khi `Callable` thực thi xong, Spring sẽ hoàn thành response.

* **`DeferredResult<V>`**

    * Cách làm nâng cao hơn. Bạn trả về một `DeferredResult` (một "lời hứa"). Thread server được giải phóng.
    * Một thread *hoàn toàn khác* (ví dụ: từ một Message Queue, một tác vụ theo lịch) sẽ phải gọi phương thức `.setResult(V)` của `DeferredResult` để hoàn thành response.

* **`CompletableFuture<V>`**, `ListenableFuture<V>`, `CompletionStage<V>`

    * Đây là các kiểu bất đồng bộ hiện đại của Java 8+. Spring hỗ trợ chúng hoàn toàn, hoạt động tương tự như `Callable`.

-----

### 4\. 🌊 Truyền Dữ liệu (Streaming)

Dùng khi bạn muốn gửi dữ liệu về client *một cách liên tục* (streaming) thay vì gửi một cục lớn, ví dụ: tải file lớn hoặc gửi sự kiện real-time.

* **`ResponseBodyEmitter`** / **`SseEmitter`**

    * Cho phép bạn gửi nhiều đối tượng (events) về client theo thời gian.
    * **`SseEmitter`** được dùng đặc biệt cho **Server-Sent Events (SSE)**, một chuẩn W3C để server "đẩy" (push) dữ liệu về trình duyệt (rất tốt cho các dashboard real-time).

* **`StreamingResponseBody`**

    * Cho phép bạn ghi dữ liệu *trực tiếp* vào `OutputStream` của response một cách bất đồng bộ. Rất lý tưởng để stream các file lớn mà không cần tải toàn bộ file vào bộ nhớ RAM.

* **Reactive Types (ví dụ: `Flux<V>`, `Mono<V>`)**

    * Nếu bạn dùng Spring WebFlux, đây là kiểu trả về tiêu chuẩn. Ngay cả trong Spring MVC (Servlet), Spring cũng hỗg trợ trả về các kiểu reactive này để xử lý streaming.

-----

### 5\. 👻 Kiểu trả về đặc biệt

* **`void`**
    * Kiểu `void` có thể có 3 ý nghĩa:
        1.  **(REST):** Bạn tự thêm annotation `@ResponseStatus` (ví dụ: `@ResponseStatus(HttpStatus.NO_CONTENT)`) để báo thành công nhưng không có nội dung trả về.
        2.  **(MVC):** Bạn đã tự mình xử lý response bằng cách yêu cầu tham số `HttpServletResponse` hoặc `OutputStream` và tự ghi dữ liệu vào đó.
        3.  **(MVC):** Spring sẽ tự suy luận tên View từ URL (giống hệt như khi trả về `Map` hoặc `Model`).