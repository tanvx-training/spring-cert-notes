## ❓ Câu hỏi: Authentication (Xác thực) và Authorization (Ủy quyền) là gì? Cái nào phải có trước?

Chào bạn, đây là hai khái niệm nền tảng của bảo mật, và câu trả lời của bạn là **cực kỳ xuất sắc, chi tiết và chính xác**. Bạn đã bao gồm tất cả mọi thứ, từ định nghĩa cơ bản đến các triển khai nâng cao (CAS, MFA) và cách Spring Security hỗ trợ chúng.

Hãy cùng diễn giải lại các khái niệm này bằng một **ví dụ so sánh (analogy)** đơn giản để củng cố kiến thức:

**Hãy tưởng tượng một Tòa nhà Văn phòng (Ứng dụng của bạn):**

---

### 1. 👤 Authentication (Xác thực)

Như bạn đã định nghĩa, đây là quá trình trả lời câu hỏi: **"Bạn là ai?"**

* **💡 Phép ví von:** Đây là quá trình bạn **quét thẻ (swipe keycard)** tại cửa chính của tòa nhà.
* **Quá trình:**
    1.  Bạn (User) chìa thẻ ra và nói: "Tôi là 'Nhân viên A'" (Đây là **Identity** - danh tính, ví dụ: `username`).
    2.  Hệ thống bảo vệ (Application) yêu cầu: "Hãy chứng minh" (Đây là **Credentials** - thông tin xác thực, ví dụ: `password`, mã OTP, hoặc vân tay).
    3.  Hệ thống kiểm tra thông tin của bạn với "Danh sách nhân viên" (Ví dụ: `UserDetailsService` hoặc database).
* **Kết quả:**
    * **Thành công (ACCERTS):** "Xác nhận đúng. Bạn là 'Nhân viên A'. Mời vào."
    * **Thất bại (REJECTS):** "Thẻ không hợp lệ. Bạn không được vào."

Các ví dụ của bạn về **Form Login**, **CAS (SSO)**, hay **MFA (Xác thực đa yếu tố)** chỉ là những cách "phức tạp" hoặc "an toàn" hơn để chứng minh bạn là ai, nhưng mục tiêu cuối cùng vẫn là *xác minh danh tính*.

---

### 2. 🔑 Authorization (Ủy quyền)

Như bạn đã định nghĩa, đây là quá trình trả lời câu hỏi: **"Bạn được phép làm gì?"**

* **💡 Phép ví von:** Sau khi đã vào được tòa nhà (bạn *đã được xác thực*), bạn dùng chính cái thẻ đó để **mở các cửa phòng cụ thể**.
* **Quá trình:**
    1.  Hệ thống *đã biết* bạn là 'Nhân viên A'.
    2.  Bạn cố gắng mở cửa "Phòng Kế toán".
    3.  Hệ thống kiểm tra "Chính sách Quyền hạn" (Access Control Rules): "Liệu 'Nhân viên A' (hoặc nhóm `ROLE_STAFF` của anh ta) có quyền `ACCESS_FINANCE` không?"
* **Kết quả:**
    * **Thành công (Access Granted):** "Được phép. Cửa mở."
    * **Thất bại (Access Denied):** "Không được phép. Cửa vẫn khóa." (HTTP 403 Forbidden).

Bạn đã phân biệt rất rõ các cách Spring Security thực thi việc này:

* **Web Security (`mvcMatcher("/admin/**").hasRole("ADMIN")`)**: Giống như "Bảo vệ" đứng chặn ở *cửa hành lang* (ví dụ: "Chỉ 'ADMIN' mới được vào *toàn bộ* khu vực `/admin`).
* **Method Security (`@PreAuthorize("hasRole('ADMIN')")`)**: Giống như "Ổ khóa" trên *từng cửa phòng* (ví dụ: "Chỉ 'ADMIN' mới được nhấn nút `deleteUser()`").

Và sự khác biệt của bạn giữa **Roles** (vai trò cấp cao, như `ROLE_ADMIN`) và **Authorities** (quyền cấp thấp, như `DELETE_USER`) là hoàn toàn chính xác.

---

### 3. 🥇 Cái nào phải có trước?

Câu trả lời của bạn là **chính xác 100%**:

**Authentication (Xác thực) LUÔN LUÔN phải đến trước.**

**Tại sao?**
Sử dụng lại phép ví von của chúng ta:
> Bạn **không thể** quyết định một người được phép vào phòng nào (Authorization) nếu bạn **chưa** biết người đó là ai (Authentication).

Hệ thống phải xác định danh tính (identity) của bạn ("Bạn là 'Nhân viên A'") trước khi nó có thể tra cứu các quyền (authorities) gắn liền với danh tính đó ("'Nhân viên A' được phép làm gì?").

## ❓ Câu hỏi: Bảo mật (Security) có phải là một "cross-cutting concern" không? Nó được triển khai (implement) bên trong như thế nào?

Vâng, câu trả lời của bạn là **hoàn toàn chính xác**.

**Bảo mật (Security)** là một ví dụ **kinh điển và hoàn hảo** về **Cross-Cutting Concern** (Khía cạnh xuyên suốt).

---

### 1.  intersecting-circles: Tại sao là "Cross-Cutting Concern"?

Như bạn đã định nghĩa, đây là một chức năng (như Giao dịch, Logging, hay Bảo mật) "cắt ngang" qua nhiều lớp (layers) và module của ứng dụng.

**💡 Ví dụ so sánh: "An ninh Tòa nhà"**

Hãy tưởng tượng ứng dụng của bạn là một tòa nhà văn phòng, với nhiều phòng ban khác nhau:
* `UserService` (Phòng Nhân sự)
* `OrderService` (Phòng Kinh doanh)
* `PaymentService` (Phòng Kế toán)

Mỗi phòng ban (lớp `Service`) có một nghiệp vụ chính (business logic) riêng.

**Vấn đề:** Bạn không muốn đặt một "anh bảo vệ" *bên trong* code của phòng Nhân sự, và một "anh bảo vệ" khác *bên trong* code của phòng Kế toán. Điều này sẽ làm "rác" (cluttered) code nghiệp vụ của bạn và lặp đi lặp lại (boilerplate).

**Giải pháp:** Bạn cần một **Hệ thống An ninh Trung tâm** (Central Security System) quản lý *toàn bộ* tòa nhà. Spring Security chính là hệ thống đó. Nó sử dụng AOP (Lập trình hướng khía cạnh) để *tách biệt* (separate) logic bảo mật ra khỏi logic nghiệp vụ.

---

### 2. ⚙️ Spring triển khai nó như thế nào? (Hai Cấp độ)

Như bạn đã nêu rất chính xác, Spring Security triển khai "Hệ thống An ninh" này ở **2 cấp độ (2 vành đai bảo vệ)**:

#### A. Cấp độ Web (Web Level) - "Bảo vệ Cổng chính"
* **Cách triển khai:** Dùng **Servlet Filters**.
* **Thành phần chính:** Một "siêu filter" (super-filter) tên là `FilterChainProxy`.
* **Nó làm gì?**
    * Đây là "anh bảo vệ" đứng ở **cổng chính** của tòa nhà.
    * Nó chặn **mọi** `HttpServletRequest` đi vào, *thậm chí trước khi* request đó chạm tới `DispatcherServlet` (cổng của Spring MVC).
    * **Nhiệm vụ của nó:**
        1.  **Xác thực (Authentication):** "Bạn là ai?" Nó kiểm tra request. Nếu chưa đăng nhập, nó sẽ chuyển hướng (redirect) bạn đến trang `/login`.
        2.  **Authorization (Ủy quyền) Cấp độ Web:** "Bạn có được phép vào *khu vực* này không?" (ví dụ: `mvcMatcher("/admin/**").hasRole("ADMIN")`).
        3.  **Tạo bối cảnh:** Nếu xác thực thành công, nó sẽ tạo `Authentication` object và đặt vào `SecurityContextHolder` (phần sau).

#### B. Cấp độ Phương thức (Method Level) - "Khóa cửa Phòng"
* **Cách triển khai:** Dùng **Spring AOP (Proxy)**, y hệt như `@Transactional`.
* **Kích hoạt:** Bạn phải bật nó lên bằng `@EnableGlobalMethodSecurity` (như bạn nói).
* **Nó làm gì?**
    * Đây là các **"ổ khóa điện tử"** trên cửa của *từng phòng ban* quan trọng (ví dụ: phòng Kế toán, phòng Giám đốc).
    * Khi một request (đã đi qua cổng chính) cố gắng gọi một phương thức (ví dụ: `deleteUser()`), AOP Proxy sẽ chặn nó lại.
    * **Nhiệmvụ của nó:**
        1.  **Authorization (Ủy quyền) Cấp độ Phương thức:** "Bạn là 'Nhân viên A', nhưng bạn có *quyền* (`Authority`) để mở *cánh cửa này* (`@PreAuthorize("hasRole('ADMIN')")`) không?"
        2.  Nó kiểm tra `Authentication` object (lấy từ `SecurityContextHolder`) để xem bạn có quyền đó không.

---

### 3. 🧩 Các Thành phần Cốt lõi (Như bạn đã liệt kê)

Để thực hiện 2 cấp độ bảo vệ trên, Spring Security sử dụng một bộ máy các "linh kiện" bên trong. Danh sách của bạn rất đầy đủ, đây là các thành phần quan trọng nhất:

**Khái niệm Cốt lõi (Core):**
* **`SecurityContextHolder` (Chiếc ví):**
    * Đây là **trái tim** của bảo mật. Nó là một `ThreadLocal` (một biến "toàn cục" chỉ dành cho request hiện tại).
    * Nó "giữ" (holds) `SecurityContext`.
* **`SecurityContext` (Ngăn trong ví):**
    * Nó "giữ" (holds) đối tượng `Authentication`.
* **`Authentication` (Tấm thẻ ID):**
    * Đây là đối tượng quan trọng nhất. Nó chứa 3 thứ về người dùng *đã được xác thực*:
        1.  **`Principal`:** Bạn là ai? (Thường là đối tượng `User` hoặc `UserDetails` của bạn).
        2.  **`Credentials`:** Bằng chứng (thường là `password`, sẽ bị xóa sau khi xác thực).
        3.  **`Authorities` (Quyền):** Bạn được phép làm gì? (ví dụ: `List<GrantedAuthority>` chứa `ROLE_ADMIN`, `READ_DATA`).

**Cỗ máy Xác thực (Authentication):**
* **`AuthenticationManager` (Sếp Bảo vệ - Xác thực):**
    * Giao diện (interface) chỉ có 1 nhiệm vụ: nhận một `Authentication` (chứa username/password thô) và trả về một `Authentication` (đã được xác thực đầy đủ, chứa `Authorities`).
* **`ProviderManager` (Triển khai mặc định):**
    * Ông Sếp này không tự làm. Ông ta có một *danh sách* các `AuthenticationProvider`.
* **`AuthenticationProvider` (Các đội bảo vệ chuyên biệt):**
    * `DaoAuthenticationProvider` (phổ biến nhất): "Tôi biết cách xác thực bằng Database (dùng `UserDetailsService`)."
    * `LdapAuthenticationProvider`: "Tôi biết cách xác thực qua LDAP."
* **`UserDetailsService` (Người tra cứu Danh bạ):**
    * Một interface đơn giản mà `DaoAuthenticationProvider` sử dụng. Nhiệm vụ của nó là: "Cho tôi một `username`, tôi sẽ trả về một đối tượng `UserDetails` (từ DB)."

**Cỗ máy Ủy quyền (Authorization):**
* **`AccessDecisionManager` (Sếp Bảo vệ - Ủy quyền):**
    * Nhiệm vụ: Trả lời câu hỏi "User này (`Authentication`) có được phép làm việc này không?"
    * Nó hỏi một "Hội đồng Bầu cử" (danh sách các `AccessDecisionVoter`).
* **`AccessDecisionVoter` (Người bỏ phiếu):**
    * Một "lá phiếu" (voter) có thể dựa trên `Role` (`RoleVoter`), hoặc dựa trên biểu thức SpEL (`WebExpressionVoter`).

---

### 4. 📜 Luồng hoạt động (Tổng hợp)

Luồng hoạt động bạn mô tả là hoàn hảo:
1.  User (request) ->
2.  **Filter (Bảo vệ Cổng)** chặn lại, yêu cầu đăng nhập.
3.  User (submit form) ->
4.  **`AuthenticationManager`** (Sếp Bảo vệ Xác thực) (thông qua `ProviderManager` -> `DaoAuthenticationProvider` -> `UserDetailsService`) kiểm tra username/password.
5.  Nếu OK, `Authentication` object (đầy đủ quyền) được tạo và lưu vào **`SecurityContextHolder` (Cái ví)**.
6.  User gọi phương thức `@PreAuthorize("hasRole('ADMIN')")` ->
7.  **AOP (Bảo vệ Phòng)** chặn lại.
8.  AOP hỏi **`AccessDecisionManager`** (Sếp Bảo vệ Ủy quyền).
9.  `AccessDecisionManager` nhìn vào **"Cái ví" (`SecurityContextHolder`)** và "Hội đồng Bầu cử" (`Voters`) để quyết định "CHO PHÉP" hay "TỪ CHỐI".

## ❓ Câu hỏi:
# DelegatingFilterProxy là gì?

Câu trả lời của bạn là **hoàn toàn chính xác** và rất chi tiết về mặt kỹ thuật. Bạn đã nắm được vai trò "cầu nối" (bridge) của nó.

Hãy cùng diễn giải lại khái niệm này bằng một **ví dụ so sánh (analogy)** để làm nó dễ hiểu hơn.

---

### 1. 💡 Ví dụ so sánh: "Bảo vệ Tòa nhà"

Hãy tưởng tượng 2 "thế giới" khác nhau:

1.  **Servlet Container (ví dụ: Tomcat):** Là "Ban quản lý Tòa nhà". Họ chỉ biết về các nhân viên do *họ* quản lý (các `Servlet` và `Filter` tiêu chuẩn).
2.  **Spring Application Context:** Là "Công ty An ninh Chuyên nghiệp" (Spring Security) hoạt động *bên trong* tòa nhà. Họ có các nhân viên "xịn" (các `Beans`) với nhiều kỹ năng (được `@Autowired`, quản lý transaction, v.v.).

**Vấn đề:**
* Ban quản lý (Tomcat) muốn có an ninh, nhưng họ không biết cách "nói chuyện" hay quản lý các nhân viên "xịn" của Spring.
* Nhân viên "xịn" (`FilterChainProxy` - bean bảo vệ thật) lại muốn được Spring quản lý (để có thể `@Autowired` các `UserDetailsService` hay `PasswordEncoder`).

**Giải pháp (DelegatingFilterProxy):**
`DelegatingFilterProxy` chính là một **"Nhân viên Lễ tân"** (hay "người ủy quyền").

1.  **Đăng ký (Registration):** "Công ty An ninh" (Spring) đăng ký "Nhân viên Lễ tân" này với "Ban quản lý Tòa nhà" (Tomcat). (Đây là lúc bạn đăng ký filter trong `web.xml` hoặc `SecurityApplicationInitializer` - như bạn đã nêu).
2.  **Vai trò:** "Nhân viên Lễ tân" này là một nhân viên "thật" của Tomcat, vì vậy Tomcat biết cách ra lệnh cho anh ta.
3.  **Hành động (Delegation):**
    * Khi một Khách (Request) đi vào tòa nhà, Tomcat sẽ nói với "Nhân viên Lễ tân" (`DelegatingFilterProxy`): "Hãy kiểm tra an ninh!"
    * "Nhân viên Lễ tân" **không tự làm gì cả**.
    * Anh ta chỉ quay vào văn phòng "Công ty An ninh" (Spring Context), tìm "Bảo vệ Trưởng" (bean `FilterChainProxy`), và nói: "Này sếp, có khách, sếp xử lý đi!"
    * Toàn bộ công việc kiểm tra (authentication, authorization) sẽ do "Bảo vệ Trưởng" (bean `FilterChainProxy`) thực hiện.
---

### 2. ⚙️ Tóm tắt Kỹ thuật (Như bạn đã nói)

* **Nó là gì?** Một `Filter` (biết "nói chuyện" với Tomcat) hoạt động như một **proxy** (ủy quyền).
* **Nó làm gì?** Nó **ủy quyền (delegates)** tất cả các lệnh gọi (`doFilter`) đến một **Bean** nằm bên trong Spring `ApplicationContext`.
* **Nó là "Cầu nối":** Như bạn nói, nó là "cầu nối" giữa vòng đời của Servlet Container (Tomcat) và vòng đời của Spring Context.
* **Nó tìm Bean nào?**
    * Mặc định, nó tìm một bean có **tên (ID) giống hệt** với `filter-name` của nó.
    * Trong Spring Security, tên mặc định này (như bạn nói) là **`springSecurityFilterChain`**.
* **Bean đó là gì?** Bean `springSecurityFilterChain` đó chính là `FilterChainProxy`, "Bảo vệ Trưởng" thật sự, người quản lý toàn bộ chuỗi filter (security chain) của Spring.

---

### 3. Cách Đăng ký (Như bạn đã liệt kê)

1.  **Spring Boot (Tự động):**
    * `SecurityFilterAutoConfiguration` (như bạn nói) tự động đăng ký một `DelegatingFilterProxyRegistrationBean`. Mọi thứ "cứ thế chạy".

2.  **Spring (Không Boot) - Servlet 3+ (Cách hiện đại):**
    * Bạn `extends AbstractSecurityWebApplicationInitializer`. Lớp này sẽ tự động đăng ký `DelegatingFilterProxy` cho bạn.

3.  **Spring (Không Boot) - Servlet 2 (Cách cũ):**
    * Bạn phải khai báo nó thủ công trong `web.xml`, chính xác như ví dụ XML của bạn.

## ❓ Câu hỏi: Security Filter Chain (Chuỗi Lọc Bảo mật) là gì?

Câu trả lời của bạn là **rất chính xác và chi tiết về mặt kỹ thuật**. Bạn đã nắm được toàn bộ kiến trúc, từ `DelegatingFilterProxy` đến `FilterChainProxy` và danh sách các filter mặc định.

Phần diễn giải này sẽ dùng một **ví dụ so sánh (analogy)** để làm cho các khái niệm phức tạp này trở nên trực quan và dễ nhớ hơn.

-----

### 1\. 💡 Phép ví von: "An ninh Sân bay"

Hãy tưởng tượng toàn bộ ứng dụng web của bạn là một **Sân bay (Airport)**.

* **Request (Yêu cầu):** Là các "Hành khách" đang cố gắng đi vào sân bay.
* **Tomcat (Servlet Container):** Là "Lối vào Chính" của sân bay, nơi *tất cả* hành khách đều phải đi qua.
* **`DelegatingFilterProxy` (Người Điều phối):**
    * Đây là "Nhân viên An ninh" đầu tiên đứng ở Lối vào Chính, do "Ban quản lý Sân bay" (Tomcat) quản lý.
    * Anh ta **không** tự mình kiểm tra hành khách. Anh ta là một "người ủy quyền".
    * Nhiệm vụ duy nhất của anh ta là "chỉ" hành khách đến đúng "Khu vực Kiểm tra An ninh Chuyên nghiệp" (Spring Security).
* **`FilterChainProxy` (Giám đốc An ninh):**
    * Đây là một `Bean` "xịn" của Spring. Anh ta là "Giám đốc" của Khu vực An ninh.
    * Anh ta **không** trực tiếp kiểm tra, mà *quản lý* nhiều **"Hàng rào Kiểm tra" (Security Filter Chains)**.
* **`List<SecurityFilterChain>` (Các Hàng rào Kiểm tra):**
    * Giám đốc An ninh (`FilterChainProxy`) có nhiều hàng rào. Mỗi hàng rào có một "biển chỉ dẫn" (Request Matcher).
    * *Hàng rào A (`SecurityFilterChain A`):* Dành cho "Khu vực Quản trị" (`/admin/**`).
    * *Hàng rào B (`SecurityFilterChain B`):* Dành cho "Khu vực API" (`/api/v1/**`).
    * *Hàng rào C (`SecurityFilterChain C`):* Dành cho "Khu vực Công cộng" (`/public/**`, `/login`).
* **Các `Filter` (Các bước Kiểm tra):**
    * Đây chính là "chuỗi" (chain). Mỗi "Hàng rào" (ví dụ: Hàng rào A) bao gồm một **danh sách các bước kiểm tra (filters)** theo thứ tự cố định.

-----

### 2\. ⚙️ Luồng hoạt động (Flow)

Bây giờ, hãy xem điều gì xảy ra khi một hành khách (Request) đến:

1.  **Hành khách (Request)** đến URL `/admin/dashboard`.

2.  **"Người Điều phối" (`DelegatingFilterProxy`)** chặn hành khách lại và "chỉ" họ đến **"Giám đốc An ninh" (`FilterChainProxy`)**.

3.  **"Giám đốc An ninh" (`FilterChainProxy`)** nhìn vào URL (`/admin/dashboard`) và xem xét các "Hàng rào" của mình:

    * Có khớp `/api/v1/**` không? Không.
    * Có khớp `/admin/**` không? **Có\!** (Sử dụng `AntPathRequestMatcher` như bạn nói).

4.  Giám đốc chỉ hành khách vào **Hàng rào A (`SecurityFilterChain A`)**.

5.  Hành khách bây giờ phải đi qua một **chuỗi (chain)** các "Cổng kiểm tra" (Filters) theo thứ tự:

    * **Cổng 1: `SecurityContextPersistenceFilter`**
        * "Hành khách này đã qua kiểm tra (đăng nhập) từ trước chưa? Hãy kiểm tra `Session` xem sao."
    * **Cổng 2: `CsrfFilter`**
        * "Vé (request) của hành khách có con dấu CSRF hợp lệ không?"
    * **Cổng 3: `UsernamePasswordAuthenticationFilter`**
        * "Nếu đây là request đến `/login`, tôi sẽ lấy `username` và `password` từ họ để xác thực."
    * ... (các cổng khác) ...
    * **Cổng cuối cùng: `FilterSecurityInterceptor`** (Rất quan trọng)
        * "OK, hành khách này *đã được xác thực* (authenticated) là 'Alice'. Nhưng 'Alice' có *quyền (`Role`) ADMIN* để vào phòng `/admin/dashboard` này không (authorization)?"

6.  Nếu hành khách vượt qua *tất cả* các cổng trong chuỗi, họ sẽ được phép vào "Phòng Chờ" (tức là `Controller` của bạn). Nếu họ thất bại ở bất kỳ cổng nào, họ sẽ bị "đá" ra (ví dụ: chuyển hướng đến `/login` hoặc báo lỗi 403 Forbidden).

-----

### 3\. 🛠️ Cách Tùy chỉnh (Như bạn đã nêu)

Bạn hoàn toàn đúng khi đề cập đến `WebSecurityConfigurerAdapter`. Đây là cách "cũ" (trước Spring Security 5.7) để tùy chỉnh các "Hàng rào" này.

**Cách Cũ (Dùng `WebSecurityConfigurerAdapter` - có thể vẫn xuất hiện trong kỳ thi):**
Bạn `extends` một lớp và `override` phương thức `configure(HttpSecurity http)`.

```java
// CÁCH CŨ (Đã bị deprecated)
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN") // Quy tắc cho /admin
                .antMatchers("/public/**").permitAll()   // Quy tắc cho /public
                .anyRequest().authenticated()           // Mọi thứ khác phải đăng nhập
                .and()
            .formLogin();
    }
}
```

**Cách Mới (Từ 5.7+ - Component-based - Rất quan trọng):**
Cách làm hiện đại là **bạn không `extends` gì cả**. Bạn chỉ cần định nghĩa một `Bean` kiểu `SecurityFilterChain`. Spring sẽ tự động tìm thấy nó.

```java
// CÁCH MỚI (Khuyến nghị)
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain myAdminFilterChain(HttpSecurity http) throws Exception {
        http
            .antMatcher("/admin/**") // CHỈ áp dụng cho /admin
            .authorizeRequests()
                .anyRequest().hasRole("ADMIN")
                .and()
            .httpBasic();
        return http.build();
    }

    @Bean
    public SecurityFilterChain myPublicFilterChain(HttpSecurity http) throws Exception {
        http
            .antMatcher("/**") // Áp dụng cho mọi thứ khác
            .authorizeRequests()
                .antMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin();
        return http.build();
    }
}
```

Cách mới này cho phép bạn định nghĩa *nhiều* `SecurityFilterChain` (nhiều "Hàng rào") một cách rõ ràng, thay vì nhồi nhét tất cả vào một phương thức `configure` duy nhất.

## ❓ Câu hỏi: SecurityContext là gì?

Chào bạn, câu trả lời của bạn là **rất xuất sắc** và cực kỳ chính xác về mặt kỹ thuật. Bạn đã nắm rõ các thành phần cốt lõi: `SecurityContext`, `SecurityContextHolder`, 3 "chế độ" (modes), và đối tượng `Authentication`.

Phần diễn giải này sẽ sử dụng một **ví dụ so sánh (analogy)** để liên kết tất cả các khái niệm kỹ thuật này lại với nhau một cách dễ hiểu.

---

### 💡 Phép ví von: "Cái ví và Tấm thẻ ID"

Hãy nghĩ về kiến trúc bảo mật của Spring như sau:
* **`SecurityContextHolder` (Người Bảo vệ / Chính sách):** "Người" (hoặc "Chính sách") chịu trách nhiệm *giữ* thông tin an ninh.
* **`SecurityContext` (Cái ví):** Một vật chứa. Nhiệm l_à "giữ" (hold) *một* tấm thẻ ID.
* **`Authentication` (Tấm thẻ ID):** Đối tượng chứa thông tin "Bạn là ai" và "Bạn được làm gì".

---

### 1. `SecurityContext` (Cái ví)

Như bạn đã nói, `SecurityContext` là một `interface` (giao diện) đơn giản. Nó thực sự chỉ là một cái "ví" (wallet).

* **Mục đích:** Nhiệm vụ duy nhất của nó là "giữ" (hold) đối tượng `Authentication` (tấm thẻ ID).
* **Phương thức:**
    * `setAuthentication(auth)`: "Đặt tấm thẻ ID vào ví."
    * `getAuthentication()`: "Lấy tấm thẻ ID ra khỏi ví."

---

### 2. `SecurityContextHolder` (Người Bảo vệ)

Đây là **trái tim** của hệ thống, như bạn đã nêu. Nó là "Người Bảo vệ" mà bạn sẽ "nói chuyện" (tương tác).

**Bạn (code của bạn) không bao giờ nên tự mình tạo hay giữ một "Cái ví" (`SecurityContext`).** Thay vào đó, bạn *luôn luôn* hỏi "Người Bảo vệ" (`SecurityContextHolder`):

> "Này Anh Bảo vệ, cho tôi xem **cái ví** của người dùng *hiện tại*."
> `SecurityContextHolder.getContext();`

Sau đó, bạn lấy "Tấm thẻ ID" (`Authentication`) từ "Cái ví" đó:
> `SecurityContextHolder.getContext().getAuthentication();`

---

### 3. "Chế độ" (Modes) - Quan trọng nhất: `MODE_THREADLOCAL`

Phần này của bạn là rất quan trọng. "Chế độ" (Mode) của `SecurityContextHolder` là "Chính sách" mà "Người Bảo vệ" tuân theo.

**Tại sao `MODE_THREADLOCAL` là Mặc định (và Hoàn hảo cho Web)?**
* **Bối cảnh:** Một máy chủ web (như Tomcat) xử lý *nhiều* request (yêu cầu) cùng một lúc. Nó gán **một `Thread` (luồng) riêng biệt** cho *mỗi* request.
* **Vấn đề:**
    * `Request 1` (từ User A) đang chạy trên `Thread 1`.
    * `Request 2` (từ User B) đang chạy trên `Thread 2`.
    * Làm thế nào để "Cái ví" (`SecurityContext`) của User A không bị lẫn lộn với "Cái ví" của User B?
* **Giải pháp (`MODE_THREADLOCAL`):**
    * `ThreadLocal` là một cấu trúc dữ liệu đặc biệt của Java. Hãy nghĩ nó như một **"chiếc túi vô hình"** mà *mỗi `Thread` (luồng)* đều có.
    * Khi `SecurityContextHolder` được đặt ở chế độ này, nó sẽ "gắn" (attach) "Cái ví" (`SecurityContext`) vào `Thread` hiện tại.
    * Khi `Thread 1` (User A) gọi `SecurityContextHolder.getContext()`, nó sẽ lấy "Cái ví" từ *chiếc túi của `Thread 1`*.
    * Khi `Thread 2` (User B) gọi `SecurityContextHolder.getContext()`, nó sẽ lấy "Cái ví" từ *chiếc túi của `Thread 2`*.

**Kết quả:** Thông tin bảo mật của User A được *cô lập (isolated)* hoàn toàn với User B. Đây chính là lý do tại sao nó là mặc định cho các ứng dụng web.

* **`MODE_GLOBAL` (Như bạn nói):** Sẽ là thảm họa cho web. Nó có nghĩa là chỉ có *một "Cái ví" duy nhất* cho *toàn bộ ứng dụng*. User A đăng nhập, User B (vào sau) cũng sẽ bị coi là User A. Nó chỉ dùng cho các ứng dụng desktop đơn giản (ví dụ: Swing).

---

### 4. `Authentication` (Tấm thẻ ID)

Như bạn đã phân tích, đây là đối tượng chứa thông tin *thực sự*. "Tấm thẻ ID" này có 3 phần chính:

1.  **`getPrincipal()` (Mặt trước):** Bạn là ai? (Ví dụ: Đối tượng `UserDetails` của bạn, chứa `username`, `email`, v.v.).
2.  **`getAuthorities()` (Mã vạch Quyền):** Bạn được phép làm gì? (Ví dụ: `List` chứa `ROLE_ADMIN`, `ROLE_USER`).
3.  **`getCredentials()` (Mật khẩu):** Bằng chứng (ví dụ: `password`). (Spring Security sẽ **xóa (clear)** thông tin này ngay sau khi xác thực thành công để bảo mật).

---

### 5. 📜 Luồng hoạt động (Tổng hợp)

1.  Một `Request` (User A) đi vào.
2.  Một `Filter` (thường là `SecurityContextPersistenceFilter`) sẽ tạo một `SecurityContext` (Cái ví rỗng) và đặt nó vào `SecurityContextHolder` (gắn vào `Thread` hiện tại).
3.  Một `Filter` khác (ví dụ: `UsernamePasswordAuthenticationFilter`) lấy username/password và đưa cho `AuthenticationManager`.
4.  `AuthenticationManager` xác thực thành công và trả về một đối tượng `Authentication` (Tấm thẻ ID xịn, có đủ quyền).
5.  `Filter` đó gọi `SecurityContextHolder.getContext().setAuthentication(auth)` (Đặt "Thẻ ID xịn" vào "Cái ví").
6.  Khi `Request` đi đến `Controller` hoặc `Service` có `@PreAuthorize("hasRole('ADMIN')")`:
    * Spring Security sẽ gọi `SecurityContextHolder.getContext().getAuthentication().getAuthorities()` để kiểm tra xem "Tấm thẻ ID" trong "Cái ví" của `Thread` này có quyền `ROLE_ADMIN` không.
7.  Khi `Request` kết thúc (trả `Response`), `Filter` (`SecurityContextPersistenceFilter`) sẽ dọn dẹp (clear) `SecurityContextHolder`. "Cái ví" bị hủy. `Thread` được "làm sạch" để tái sử dụng cho request tiếp theo.

## ❓ Câu hỏi: `**` pattern (mẫu) trong antMatcher hoặc mvcMatcher làm gì?

Câu trả lời của bạn là **hoàn toàn chính xác**. Bạn đã nắm rất rõ không chỉ `**` mà còn cả các ký tự đại diện (wildcards) khác.

Phần diễn giải này sẽ tập trung vào việc làm rõ *sự khác biệt* giữa `*` và `**` (điều quan ltrọng nhất) và cung cấp thêm bối cảnh.

-----

### 1\. 💡 `**` (Dấu sao đôi) - "Wildcard Siêu cấp"

Như bạn đã nói, `**` khớp với **0 hoặc nhiều phân đoạn đường dẫn (path segments)**.

Hãy nghĩ về đường dẫn URL như các "thư mục" (folders) được ngăn cách bởi dấu `/`.

* `*` (Dấu sao đơn): Là wildcard cho "tên file".
* `**` (Dấu sao đôi): Là wildcard cho "thư mục" (và cả "thư mục con", "cháu"...).

**Cách dùng phổ biến nhất (Ví dụ trong `HttpSecurity`):**

```java
http.authorizeRequests()
    .antMatcher("/admin/**").hasRole("ADMIN")
    .anyRequest().permitAll();
```

* `antMatcher("/admin/**")` có nghĩa là: "Áp dụng quy tắc này cho `/admin` VÀ *mọi thứ* bên dưới nó, bất kể nó sâu bao nhiêu cấp."

Nó sẽ khớp với TẤT CẢ các đường dẫn sau:

* `/admin` (khớp 0 segment)
* `/admin/dashboard`
* `/admin/users/list`
* `/admin/reports/sales/2025/details`

-----

### 2\. 🆚 So sánh `*` và `**` (Rất quan trọng)

Đây là điểm mấu chốt dễ gây nhầm lẫn nhất.

* **`*` (Dấu sao đơn):** Khớp với 0 hoặc nhiều ký tự **BÊN TRONG 1 PHÂN ĐOẠN**. Nó **KHÔNG** thể "vượt qua" dấu `/`.
* **`**` (Dấu sao đôi):** Khớp với 0 hoặc nhiều **PHÂN ĐOẠN**. Nó **CÓ THỂ** "vượt qua" dấu `/`.

Hãy xem bảng so sánh sau:

| Pattern (Mẫu) | Đường dẫn (Path) | Khớp? | Tại sao? |
| :--- | :--- | :--- | :--- |
| `/api/v1/*` | `/api/v1/users` | ✅ Có | `*` khớp với `users` (1 segment). |
| `/api/v1/*` | `/api/v1/users/1` | ❌ Không | `*` **không thể** vượt qua dấu `/` để khớp với `users/1`. |
| `/api/v1/**` | `/api/v1/users` | ✅ Có | `**` khớp với `users` (1 segment). |
| `/api/v1/**` | `/api/v1/users/1` | ✅ Có | `**` "vượt" qua dấu `/` và khớp với `users/1`. |

-----

### 3\. ⚙️ Phân tích các ví dụ của bạn

Phân tích của bạn là hoàn toàn chính xác. Hãy xem xét đường dẫn (URI) là: `/departments/delete/5`

* **`/departments/delete/*`**
    * **Khớp (Matches):** ✅ Vâng. `*` khớp với `5`.
* **`/departments/delete/**`**
    * **Khớp (Matches):** ✅ Vâng. `**` khớp với `5` (1 segment).
* **`/*/5`**
    * **Không khớp (Does not match):** ❌ Vâng. `*` khớp với `departments` (segment đầu tiên). Nhưng segment thứ hai (`delete`) **không khớp** với `5`.
* **`/**/5`**
    * **Khớp (Matches):** ✅ Vâng. `**` ở đầu tiên "tham lam" (greedy) khớp với *mọi thứ* nó có thể: `departments/delete`. Phần còn lại của pattern (`/5`) khớp với phần còn lại của đường dẫn (`/5`).
* **`/departments/dele??/*`**
    * **Khớp (Matches):** ✅ Vâng.
        * `?` (khớp 1 ký tự): `l` -\> `e`
        * `?` (khớp 1 ký tự): `e` -\> `t`
        * `*` (khớp 0+ ký tự trong 1 segment): `delete` -\> (không có gì)
        * `*` (khớp 1 segment): `5`

-----

### 4\. 📌 Mẹo nhỏ: `antMatcher` vs. `mvcMatcher`

Trong Spring Security, bạn sẽ thấy cả hai.

* **`antMatcher`** (từ Spring Core/Ant): Là kiểu so khớp "truyền thống" như chúng ta vừa thảo luận.
* **`mvcMatcher`** (từ Spring MVC): Đây là lựa chọn **thường được khuyến nghị hơn** khi dùng Spring MVC.

**Tại sao `mvcMatcher` tốt hơn?** Nó "thông minh" hơn. Nó tôn trọng các cấu hình của Spring MVC. Ví dụ:

* Nếu `mvcMatcher("/users")` được sử dụng, nó sẽ tự động khớp với `/users`, `/users/`, và `/users.html` (nếu bạn có cấu hình suffix matching).
* `antMatcher("/users")` sẽ **chỉ** khớp với `/users`.

Tuy nhiên, các ký tự đại diện (wildcards) `*`, `**`, và `?` hoạt động **giống hệt nhau** trong cả hai.

## ❓ Câu hỏi: Tại sao `mvcMatcher` được khuyến nghị sử dụng hơn `antMatcher`?

Câu trả lời của bạn là **hoàn toàn chính xác**.

Lý do chính là: **`mvcMatcher` "thông minh" hơn vì nó tích hợp (integrates) với các quy tắc so khớp (matching rules) của chính Spring MVC.**

`antMatcher` là một công cụ so khớp chuỗi (string) "cơ bản" (basic), trong khi `mvcMatcher` "hiểu" cách Spring MVC của bạn thực sự định tuyến (routes) các request.

-----

### 1\. 💡 Ví dụ về "Dấu gạch chéo" (Trailing Slash) - (Như bạn đã nêu)

Đây là ví dụ kinh điển và nguy hiểm nhất mà `mvcMatcher` giải quyết.

Giả sử bạn muốn bảo vệ trang nhân viên:

```java
http
    .authorizeRequests()
    // .antMatcher("/employees") // <--- Cách cũ, "nguy hiểm"
    .mvcMatchers("/employees")   // <--- Cách mới, "an toàn"
    .hasRole("ADMIN")
    ...
```

**Vấn đề (antMatcher):**
`antMatcher` là một trình so khớp mẫu (pattern) "ngu ngốc". Nó so khớp *chính xác* chuỗi bạn đưa ra.

* Request đến `/employees` -\> **KHỚP** (Matches) -\> Yêu cầu `ROLE_ADMIN`. (Đúng)
* Request đến `/employees/` (có dấu `/` ở cuối) -\> **KHÔNG KHỚP** (Does NOT Match) -\> Quy tắc `hasRole("ADMIN")` bị **bỏ qua**. Nếu quy tắc tiếp theo là `anyRequest().permitAll()`, kẻ tấn công đã **vượt qua (bypass)** được bảo mật của bạn\!

**Giải pháp (mvcMatcher):**
`mvcMatcher` "biết" rằng trong thế giới Spring MVC, `/employees` và `/employees/` thường trỏ đến *cùng một* `Controller`.

* Request đến `/employees` -\> **KHỚP** (Matches).
* Request đến `/employees/` -\> **VẪN KHỚP** (Matches).

Như bạn đã nói, `mvcMatcher` "tha thứ" (forgiving) cho các lỗi nhỏ này, khiến việc vô tình tạo ra lỗ hổng bảo mật trở nên khó khăn hơn.

-----

### 2\. 🚀 Các lợi ích khác của `mvcMatcher`

"Sự thông minh" của `mvcMatcher` không chỉ dừng lại ở dấu gạch chéo:

#### A. Xử lý "Tiền tố Servlet" (Servlet Path)

* **Bối cảnh:** Đôi khi, ứng dụng của bạn không chạy ở gốc (root) `/`, mà chạy trong một tiền tố, ví dụ: `/myapp/*`.
* **Vấn đề (antMatcher):** `antMatcher("/admin/**")` sẽ tìm kiếm đường dẫn bắt đầu bằng `/admin/**`. Nó không "biết" rằng ứng dụng của bạn thực sự nằm trong `/myapp`.
* **Giải pháp (mvcMatcher):** `mvcMatcher("/admin/**")` sẽ **tự động** tính cả tiền tố servlet path. Nó sẽ khớp chính xác với request `/myapp/admin/dashboard`.

#### B. Xử lý "Hậu tố" (Suffix Matching)

* **Bối cảnh:** Nếu Spring MVC của bạn được cấu hình để "tôn trọng" các hậu tố (ví dụ: `/employees.json` hoặc `/employees.xml`), `mvcMatcher` cũng sẽ "tôn trọng" điều đó.
* `mvcMatcher("/employees")` sẽ tự động khớp với:
    * `/employees`
    * `/employees/`
    * `/employees.json` (nếu được cấu hình)
    * `/employees.xml` (nếu được cấu hình)
* `antMatcher("/employees")` sẽ **CHỈ** khớp với `/employees`.

-----

### Tóm tắt

| Tính năng | `antMatcher` (Cơ bản) | `mvcMatcher` (Thông minh) |
| :--- | :--- | :--- |
| **So khớp** | Dựa trên Chuỗi (String) | Dựa trên Quy tắc của Spring MVC |
| `/path/` (Dấu `/` cuối) | ❌ Không khớp với `/path` | ✅ Khớp với `/path` |
| `servletPath` | ❌ Bỏ qua (Phải tự thêm) | ✅ Tự động tính toán |
| `.json` (Hậu tố) | ❌ Bỏ qua | ✅ Tự động khớp (nếu MVC được cấu hình) |

**Kết luận:** Luôn luôn ưu tiên sử dụng `mvcMatcher` khi bạn đang bảo vệ các endpoint (điểm cuối) của Spring MVC. Chỉ sử dụng `antMatcher` khi bạn cần so khớp các mẫu (pattern) không theo chuẩn MVC.

## ❓ Câu hỏi: Spring Security có hỗ trợ password hashing không? Salting là gì?

Câu trả lời của bạn là **rất xuất sắc** và **hoàn toàn chính xác**. Bạn đã nắm được không chỉ các khái niệm cơ bản mà còn cả các triển khai (implementation) nâng cao và rất quan trọng trong Spring Security (như `DelegatingPasswordEncoder`).

Phần diễn giải này sẽ dùng các **ví dụ so sánh (analogy)** để củng cố các khái niệm này.

---

### 1. ⚙️ Password Hashing (Băm Mật khẩu)

Vâng, Spring Security hỗ trợ mạnh mẽ việc này thông qua interface (giao diện) `PasswordEncoder`.

**💡 Hashing là gì? (Phép ví von: "Máy xay")**

* **Hashing (Băm)** *không phải* là **Encryption (Mã hóa)**.
    * **Encryption** (Mã hóa) là 2 chiều (ví dụ: khóa/mở két sắt). Bạn có thể "giải mã" (decrypt) để lấy lại dữ liệu gốc.
    * **Hashing** (Băm) là **một chiều (one-way)**. Nó giống như một **"máy xay"**.

1.  Bạn đưa "hạt cà phê" (mật khẩu thô `123456`) vào máy xay (`encode`).
2.  Bạn nhận được "bột cà phê" (chuỗi hash dài ngoằng `b$2a...xyz`).
3.  Bạn **không bao giờ** có thể biến "bột" trở lại thành "hạt" y hệt ban đầu.

**Spring (`PasswordEncoder`) dùng "máy xay" này như thế nào?**

Như bạn đã nói, `PasswordEncoder` có 2 phương thức chính:

1.  **`encode(rawPassword)` - (Dùng khi Đăng ký)**
    * Người dùng đăng ký với `123456`.
    * Bạn gọi `passwordEncoder.encode("123456")`.
    * Bạn lưu "bột" (`b$2a...xyz`) vào database. Bạn **không bao giờ** lưu `123456`.

2.  **`matches(rawPassword, encodedPassword)` - (Dùng khi Đăng nhập)**
    * Đây là phần "thông minh". Spring **không** cố gắng "mở khóa" cái hash trong DB.
    * Người dùng đăng nhập với `123456`.
    * Spring lấy "bột" (`b$2a...xyz`) từ DB.
    * Spring đưa `123456` (mật khẩu thô) vào *cùng một cái máy xay*.
    * Nó so sánh "bột mới" với "bột cũ" (`b$2a...xyz`).
    * Nếu hai đống bột *giống hệt nhau* -> `matches` = `true` -> Đăng nhập thành công.

---

### 2. 🌟 `DelegatingPasswordEncoder` (Điểm quan trọng của bạn)

Phần này trong câu trả lời của bạn là rất quan trọng cho kỳ thi.

* **Vấn đề:** Điều gì xảy ra nếu 5 năm trước, bạn "xay" (hash) tất cả mật khẩu bằng thuật toán cũ (ví dụ: `SHA-256`). Bây giờ, bạn muốn nâng cấp lên `bcrypt` (an toàn hơn) mà không bắt 1 triệu người dùng cũ phải đổi mật khẩu?
* **Giải pháp (Như bạn nói):** `DelegatingPasswordEncoder` là một "Trình quản lý" thông minh.
    * Khi nó hash một mật khẩu *mới*, nó sẽ dùng `bcrypt` (mặc định) và thêm một cái "nhãn" (prefix) vào:
      `{bcrypt}$2a$10...` (Đây là cái bạn lưu vào DB).
    * Khi một người dùng *cũ* đăng nhập, `DelegatingPasswordEncoder` nhìn vào hash trong DB:
      `{sha256}97kjfds...`
    * "Trình quản lý" (Delegating) sẽ nói: "Aha, đây là `sha256`. Tôi sẽ gọi `Sha256PasswordEncoder` để `matches()`."
    * Khi một người dùng *mới* đăng nhập, nó nhìn vào hash:
      `{bcrypt}$2a$10...`
    * "Aha, đây là `bcrypt`. Tôi sẽ gọi `BCryptPasswordEncoder` để `matches()`."

Như bạn đã nói, điều này cho phép bạn nâng cấp thuật toán mã hóa một cách "mượt mà" (graceful) mà vẫn giữ được khả năng tương thích ngược (backward compatibility).

---

### 3. 🧂 Salting (Rắc muối)

Bạn đã định nghĩa Salting một cách hoàn hảo: Nó là một cơ chế để chống lại các cuộc tấn công **Rainbow Table** (từ điển hash có sẵn).

* **Vấn đề (Nếu KHÔNG có Salt):**
    * Hacker biết rằng 1 triệu người dùng `123456`.
    * User A: `hash("123456")` -> `abc123hash`
    * User B: `hash("123456")` -> `abc123hash`
    * Hacker lấy trộm DB, thấy 1 triệu hàng có `abc123hash`.
    * Hacker dùng Rainbow Table (đã tính toán trước) và tìm ra `abc123hash` = `123456`.
    * **Thảm họa:** Hacker đã bẻ khóa 1 triệu tài khoản cùng một lúc.

* **Giải pháp (Salting - Rắc muối):**
    * Như bạn nói, chúng ta thêm một chuỗi **ngẫu nhiên (random)**, gọi là "muối" (salt), vào *mỗi* mật khẩu **trước khi** hash.
    * **User A:**
        * Salt (ngẫu nhiên): `salt_A_xyz`
        * Hash: `hash("123456" + "salt_A_xyz")` -> `hash_A_pqr`
    * **User B:**
        * Salt (ngẫu nhiên): `salt_B_789`
        * Hash: `hash("123456" + "salt_B_789")` -> `hash_B_lmn`

* **Kết quả:**
    * Mặc dù cả hai người dùng đều có mật khẩu `123456`, nhưng hai chuỗi hash trong DB **hoàn toàn khác nhau**.
    * Hacker lấy trộm DB. Rainbow Table của hắn (chỉ hash `123456`) trở nên **vô dụng**. Hắn phải tấn công (brute-force) *từng* cái hash một, tốn kém hơn rất nhiều.

**Làm thế nào Spring kiểm tra (matches)?**
* Như bạn nói, "salt" **không phải là bí mật**. Nó được lưu *cùng với* chuỗi hash trong database.
* **Tin tốt:** Các `PasswordEncoder` hiện đại mà bạn đã liệt kê (như `BCrypt`, `PBKDF2`, `Argon2`) **đã tự động tích hợp Salting**.
* Chuỗi hash của `bcrypt` (ví dụ: `$2a$10$dXHW28SHSDK...`) thực ra **đã chứa "salt"** bên trong nó.
* Khi bạn gọi `matches()`, `BCryptPasswordEncoder` đủ thông minh để tự động "trích xuất" (extract) salt từ chuỗi hash đó, "rắc" nó vào mật khẩu thô, "xay" (hash), và so sánh kết quả. Bạn không cần làm gì thủ công cả.

## ❓ Câu hỏi: Tại sao bạn cần bảo mật cấp độ phương thức (method security)? Loại đối tượng nào thường được bảo mật ở cấp độ này (nghĩ về mục đích của nó, không phải kiểu Java)?

Chào bạn, câu trả lời của bạn là **hoàn toàn chính xác**. Bạn đã nắm được điểm mấu chốt: bảo mật phương thức cần thiết cho các quy tắc **"chi tiết" (granular)**, và nó thường được áp dụng tại **"Lớp Dịch vụ" (Service Layer)**.

Phần diễn giải này sẽ dùng một **ví dụ so sánh (analogy)** để làm rõ *tại sao* hai cấp độ bảo mật này lại cần thiết.

-----

### 1\. 💡 Tại sao bạn cần Bảo mật Cấp độ Phương thức?

Hãy tưởng tượng ứng dụng của bạn là một **Tòa nhà Văn phòng** được bảo vệ. Spring Security cung cấp 2 "vành đai" bảo vệ:

#### A. Bảo mật Web (`antMatcher` / `mvcMatcher`) - "Bảo vệ Cổng chính"

* **Là gì?** Đây là "anh bảo vệ" đứng ở **cổng chính** của tòa nhà.
* **Nhiệm vụ:** Anh ta chỉ kiểm tra những thứ "thô" (coarse-grained) dựa trên **URL**.
* **Ví dụ:** `http.mvcMatcher("/admin/**").hasRole("ADMIN")`
* **Analogy:** "Bạn có thẻ 'Admin' à? OK, mời bạn đi vào *toàn bộ khu vực Admin* (ví dụ: tầng 5)."
* **Hạn chế:** Anh bảo vệ này **không biết (và không quan tâm)** bạn định làm gì *bên trong* tầng 5. Anh ta không biết bạn định vào phòng của Giám đốc hay phòng Vệ sinh.

#### B. Bảo mật Phương thức (`@PreAuthorize`) - "Ổ khóa trên cửa Phòng"

* **Là gì?** Đây là các **"ổ khóa điện tử"** trên cửa của *từng phòng ban* cụ thể (ví dụ: các phương thức trong `Service`).
* **Tại sao cần?** Như bạn nói, chúng ta cần các quy tắc "chi tiết" (fine-grained) mà "anh bảo vệ cổng chính" không thể xử lý được.
* **Ví dụ:**
  ```java
  // Trong UserService:
  @PreAuthorize("hasRole('ADMIN')")
  public void deleteUser(Long userId) {
      // ...
  }
  ```
* **Analogy:** "OK, bạn đã qua được cổng chính (Web Security). Nhưng để mở *cánh cửa này* (gọi `deleteUser()`), bạn phải có quyền 'ADMIN'."

**Ví dụ Nâng cao (Sức mạnh thật sự):**
Bảo mật phương thức "thông minh" hơn bảo mật web vì nó có thể truy cập vào **bối cảnh (context)** của phương thức (ví dụ: các tham số).

Hãy xem xét quy tắc: "Bạn chỉ được phép sửa hồ sơ *của chính mình*, trừ khi bạn là Admin."

* **Web Security:** `mvcMatcher("/users/123/edit")` **không thể** làm được điều này. Nó không biết "123" có phải là "bạn" hay không.
* **Method Security:** (Như bạn đã nêu, dùng `prePostEnabled = true`)
  ```java
  // Trong UserService:
  @PreAuthorize("hasRole('ADMIN') or #user.id == authentication.principal.id")
  public void updateUser(@Param("user") User user) {
      // ...
  }

  // Hoặc (phổ biến hơn):
  @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
  public void editProfile(String username, ProfileData data) {
      // ...
  }
  ```
* **Analogy:** "Ổ khóa này sẽ mở nếu: 1. Bạn là 'Admin', HOẶC 2. Tên trên thẻ của bạn (`authentication.name`) khớp với tên trên cánh cửa (`#username`)."

-----

### 2\. 🎯 Đối tượng nào thường được Bảo mật?

Câu trả lời của bạn là chính xác 100%: **Lớp Dịch vụ (Application Service Layer)**.

**Tại sao là Lớp Dịch vụ?**

* **Nó là "Cổng" của Lớp Nghiệp vụ (Business Logic):**
    * `Controller` (Web Layer) chỉ nên "mỏng" (thin). Nó nhận HTTP request và *ủy quyền (delegates)* cho `Service` làm việc.
    * `Repository` (Data Layer) chỉ nên lo việc truy cập dữ liệu.
    * **`Service`** là nơi chứa *toàn bộ logic nghiệp vụ* (business rules) của bạn. Do đó, nó cũng là nơi lý tưởng để đặt các *quy tắc bảo mật nghiệp vụ* (business security rules).
* **Bảo vệ Toàn diện:**
    * Nếu bạn đặt bảo mật ở `Controller`, điều gì xảy ra nếu một `Service` khác (hoặc một `Scheduled Task` - tác vụ định kỳ) gọi `UserService.deleteUser()`? Bảo mật sẽ bị **bỏ qua (bypassed)**.
    * Bằng cách đặt `@PreAuthorize` trên `UserService`, bạn đã bảo vệ phương thức đó. Bất kể ai gọi nó (Controller, Service khác, Task...), "ổ khóa" AOP sẽ luôn được kích hoạt.

-----

### 3\. ⚙️ Cách triển khai (Như bạn đã nêu)

Bạn đã tóm tắt hoàn hảo các bước:

1.  **Bật (Enable):** Dùng `@EnableGlobalMethodSecurity` trên một lớp `@Configuration`.
2.  **Chọn (Choose):** Kích hoạt loại annotation bạn muốn:
    * `prePostEnabled = true`: (Mạnh nhất) Kích hoạt `@PreAuthorize` / `@PostAuthorize`. Cho phép bạn sử dụng **SpEL** (Spring Expression Language) (như `authentication.name` hoặc `#username`).
    * `securedEnabled = true`: Kích hoạt `@Secured` (ví dụ: `@Secured("ROLE_ADMIN")`). Đơn giản, cũ hơn.
    * `jsr250Enabled = true`: Kích hoạt `@RolesAllowed` (chuẩn Java EE).

## ❓ Câu hỏi: @PreAuthorize và @RolesAllowed làm gì? Sự khác biệt giữa chúng là gì?

Chào bạn, câu trả lời của bạn là **hoàn toàn chính xác**. Bạn đã nắm được điểm khác biệt quan trọng nhất: một cái dùng cho **danh sách vai trò (roles) đơn giản**, còn một cái dùng **Spring Expression Language (SpEL) mạnh mẽ**.

Hãy cùng diễn giải chi tiết hơn về hai "ổ khóa" (locks) bảo mật cấp độ phương thức này.

-----

### 1\. 💡 Bối cảnh: "Ổ khóa trên cửa phòng"

Như chúng ta đã thảo luận, Bảo mật cấp độ Phương thức (Method Security) giống như đặt **"ổ khóa"** trên các "cánh cửa" (phương thức `Service`) quan trọng.

Cả `@PreAuthorize` và `@RolesAllowed` đều là các loại "ổ khóa". Chúng đều thực hiện kiểm tra *trước khi* (Pre-) phương thức được phép chạy.

-----

### 2\. 🔑 `@RolesAllowed` (Ổ khóa cơ đơn giản)

Như bạn đã nói, đây là một phần của chuẩn **JSR-250** (một chuẩn chung của Java, không phải của riêng Spring).

* **Cách hoạt động:** Nó là một "ổ khóa cơ" rất đơn giản. Nó chỉ kiểm tra: "Người dùng này có *bất kỳ* vai trò (Role) nào trong danh sách tôi đưa ra không?"
* **Kích hoạt (như bạn nói):** `@EnableGlobalMethodSecurity(jsr250Enabled = true)`
* **Quy tắc:**
    * Mặc định, nó tìm các quyền (authorities) có tiền tố `ROLE_`. (Ví dụ: `ROLE_ADMIN`, `ROLE_USER`).

**Ví dụ Code:**

```java
public interface BankService {

    // CHỈ những user có "ROLE_ADMIN" mới được gọi
    @RolesAllowed("ROLE_ADMIN")
    public void deleteAllAccounts();

    // User có "ROLE_ADMIN" HOẶC "ROLE_MANAGER" đều được gọi
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_MANAGER" })
    public void viewAuditLog();
}
```

* **Hạn chế:** Rất cứng nhắc. Nó không thể trả lời các câu hỏi phức tạp hơn, ví dụ: "Chỉ cho phép user xem log *của chính họ*."

-----

### 3\. ⚡ `@PreAuthorize` (Ổ khóa điện tử thông minh - SpEL)

Đây là "ổ khóa" **mạnh mẽ nhất và được khuyến nghị** của Spring Security.

* **Cách hoạt động:** Như bạn nói, nó không chỉ kiểm tra vai trò. Nó lấy một **biểu thức SpEL (Spring Expression Language)** và "thực thi" nó.
    * Nếu biểu thức trả về `true` -\> "Mở cửa" (cho phép chạy phương thức).
    * Nếu biểu thức trả về `false` -\> "Từ chối" (ném ra `AccessDeniedException`).
* **Kích hoạt (như bạn nói):** `@EnableGlobalMethodSecurity(prePostEnabled = true)`

**Tại sao nó mạnh mẽ?**

**A. Nó có thể làm những gì `@RolesAllowed` làm (và tốt hơn):**

```java
// Tương đương với @RolesAllowed("ROLE_ADMIN")
// (Lưu ý: hasRole() tự động thêm tiền tố "ROLE_")
@PreAuthorize("hasRole('ADMIN')")
public void deleteUser();

// Tương đương với @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public void viewAuditLog();

// Như ví dụ của bạn, nó có thể dùng logic AND/OR phức tạp
@PreAuthorize("isAuthenticated() and hasRole('EDITOR')")
public void editPost();
```

**B. "Sát thủ": Nó có thể truy cập vào Tham số (Arguments) của Phương thức**
Đây là điều `@RolesAllowed` **không thể** làm được. `PreAuthorize` cho phép bạn viết các quy tắc bảo mật *dựa trên dữ liệu* (data-aware).

Bạn có thể tham chiếu đến các tham số (arguments) của phương thức bằng cách dùng `#ten_tham_so`.

**Ví dụ Code:**

```java
public interface UserProfileService {

    // Quy tắc: "Bạn phải là ADMIN, HOẶC tên đăng nhập của bạn
    // phải khớp với tham số 'username' mà người ta đang cố xem."
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.principal.username")
    public UserProfile viewProfile(String username);

    // Quy tắc: "Bạn chỉ có thể cập nhật thông tin tài khoản
    // nếu ID trong đối tượng 'account' khớp với ID của bạn."
    @PreAuthorize("#account.ownerId == authentication.principal.id")
    public void updateAccount(@Param("account") Account account);
}
```

* **`#username`** và **`#account`**: Tham chiếu đến các tham số của phương thức.
* **`authentication`**: Một biến SpEL đặc biệt, trỏ đến đối tượng `Authentication` (tấm thẻ ID) trong `SecurityContextHolder`.

-----

### 4\. 📊 Tóm tắt: Sự khác biệt

| Tính năng | `@RolesAllowed` (JSR-250) | `@PreAuthorize` (Spring SpEL) |
| :--- | :--- | :--- |
| **Kích hoạt** | `jsr250Enabled = true` | `prePostEnabled = true` |
| **Cơ chế** | So khớp danh sách Role đơn giản | Thực thi biểu thức SpEL (trả về `true`/`false`) |
| **Khả năng** | **Cơ bản:** Chỉ kiểm tra Role. | **Cực kỳ mạnh mẽ:** Logic (AND/OR), `hasRole`, `hasAuthority`, `isAuthenticated`... |
| **Nhận biết Bối cảnh (Context-Aware)** | ❌ **Không** | ✅ **Có thể** truy cập vào tham số phương thức (ví dụ: `#username`) |
| **Khuyến nghị** | Dùng khi cần sự đơn giản tuyệt đối hoặc tuân thủ JSR-250. | **Cách được khuyến nghị.** Dùng cho hầu hết mọi trường hợp. |

## ❓ Câu hỏi: Các annotation @PreAuthorize và @RolesAllowed được triển khai (implement) như thế nào?

Chào bạn, câu trả lời của bạn là **hoàn toàn chính xác về mặt kỹ thuật**\! Bạn đã chỉ ra đúng *chuỗi (chain)* các lớp "hậu trường" (backend) mà Spring Security sử dụng.

Đây là một chủ đề nâng cao về "nội tạng" (internals) của Spring Security. Hãy cùng diễn giải chuỗi sự kiện này bằng một **ví dụ so sánh (analogy)** để làm rõ vai trò của từng lớp bạn đã nêu.

-----

### 1\. 💡 Phép ví von: "Hệ thống An ninh Tòa nhà"

Hãy tưởng tượng bạn gọi một phương thức được bảo vệ (ví dụ: `userService.deleteUser()`).

* **Bạn (Người gọi):** Là một "Nhân viên" đang cố gắng vào một "Căn phòng" (phương thức) đặc biệt.
* **`@PreAuthorize` / `@RolesAllowed`:** Là các "Quy tắc An ninh" (Security Rules) được dán trên cửa phòng.
* **`Authentication` (Tấm thẻ ID của bạn):** Thông tin của bạn (lấy từ `SecurityContextHolder`) cho biết bạn là ai và bạn có những quyền gì (`ROLE_USER`).

**Quá trình kiểm tra an ninh xảy ra như sau:**

-----

### 2\. ⚙️ Cách triển khai (Chuỗi sự kiện AOP)

#### Bước 1: Spring AOP (Proxy) - "Anh Bảo vệ"

* **Lớp:** `MethodSecurityInterceptor` (hoặc tương tự)
* **Việc nó làm:** Giống như `@Transactional`, phương thức của bạn được "bọc" (wrapped) trong một **Proxy**. "Anh Bảo vệ" (`MethodSecurityInterceptor`) này sẽ chặn (intercepts) cuộc gọi của bạn *trước khi* nó thực sự chạy code nghiệp vụ (`deleteUser()`).
* **Hành động:** "Anh Bảo vệ" thấy có "Quy tắc An ninh" (`@PreAuthorize`) dán trên cửa. Anh ta **không** tự mình quyết định. Anh ta gọi "Giám sát An ninh" (`AccessDecisionManager`).

#### Bước 2: `AccessDecisionManager` - "Giám sát An ninh"

* **Việc nó làm:** Đây là "Sếp" của "Anh Bảo vệ". Nhiệm vụ của "Sếp" là đưa ra quyết định cuối cùng: "CHO PHÉP" (Grant) hay "TỪ CHỐI" (Deny).
* **Hành động:** "Sếp" (`AccessDecisionManager`) cũng không tự mình quyết định. Thay vào đó, ông ta hỏi ý kiến của một "Hội đồng Chuyên gia" (một danh sách các `Voters`).

#### Bước 3: `AccessDecisionVoter` (s) - "Hội đồng Chuyên gia Bỏ phiếu"

* **Việc nó làm:** Đây là các "Chuyên gia" (`Voters`). Mỗi chuyên gia chỉ biết về *một* loại quy tắc an ninh.
* **Hành động:** "Sếp" (`AccessDecisionManager`) sẽ đưa "Quy tắc" (`@PreAuthorize`) và "Tấm thẻ ID" (`Authentication`) cho "Hội đồng".
* "Hội đồng" sẽ bỏ phiếu (VOTE):
    * **`VOTE_GRANTED`** (Phiếu Thuận)
    * **`VOTE_DENIED`** (Phiếu Chống)
    * **`VOTE_ABSTAIN`** (Phiếu Trắng - "Đây không phải chuyên môn của tôi")

-----

### 3\. 🎯 Sự khác biệt (Câu trả lời của bạn)

Đây chính là nơi triển khai (implementation) của `@PreAuthorize` và `@RolesAllowed` **khác nhau**:

#### A. `@RolesAllowed` -\> `Jsr250Voter` (Chuyên gia Đơn giản)

* **Lớp:** Như bạn nói, `Jsr250Voter` được đăng ký (nếu `jsr250Enabled = true`).
* **Chuyên môn:** Chuyên gia này **chỉ** hiểu annotation `@RolesAllowed`.
* **Logic (Đơn giản):**
    1.  `Jsr250Voter` nhìn vào quy tắc: `@RolesAllowed("ROLE_ADMIN")`.
    2.  Nó nhìn vào "Tấm thẻ ID" (`Authentication`) và hỏi: "Người này có quyền `ROLE_ADMIN` không?"
    3.  Nếu có -\> Bỏ phiếu `VOTE_GRANTED`.
    4.  Nếu không -\> Bỏ phiếu `VOTE_DENIED`.
    5.  Nếu annotation *không phải* là `@RolesAllowed` (ví dụ: là `@PreAuthorize`) -\> Bỏ phiếu `VOTE_ABSTAIN` ("Để chuyên gia khác lo").

#### B. `@PreAuthorize` -\> `PreInvocationAuthorizationAdviceVoter` (Chuyên gia Thông minh)

* **Lớp:** Như bạn nói, `PreInvocationAuthorizationAdviceVoter` được đăng ký (nếu `prePostEnabled = true`).
* **Chuyên môn:** Chuyên gia này **chỉ** hiểu `@PreAuthorize`.
* **Logic (Phức tạp - Dùng SpEL):**
    1.  `PreInvocationAuthorizationAdviceVoter` nhìn vào quy tắc: `@PreAuthorize("hasRole('ADMIN') or #user.id == authentication.principal.id")`.
    2.  Nó **KHÔNG** tự so sánh. Nó đưa biểu thức SpEL này cho một **`SpEL Parser`** (Bộ phân tích SpEL).
    3.  "Bộ phân tích" sẽ thực thi biểu thức đó (truy cập cả "Tấm thẻ ID" (`authentication`) và tham số phương thức (`#user`)).
    4.  Nếu biểu thức SpEL trả về `true` -\> Bỏ phiếu `VOTE_GRANTED`.
    5.  Nếu biểu thức SpEL trả về `false` -\> Bỏ phiếu `VOTE_DENIED`.
    6.  Nếu annotation *không phải* là `@PreAuthorize` -\> Bỏ phiếu `VOTE_ABSTAIN`.

-----

### Tóm tắt Luồng

1.  Gọi `userService.deleteUser()`.
2.  **Proxy (`MethodSecurityInterceptor`)** chặn lại.
3.  Proxy lấy "Quy tắc" (`@PreAuthorize(...)`) và "Tấm thẻ ID" (`Authentication`).
4.  Proxy đưa 2 thứ này cho **`AccessDecisionManager`** (Sếp).
5.  "Sếp" trình 2 thứ này cho "Hội đồng Bầu cử" (List\<Voter\>).
6.  `Jsr250Voter` (Chuyên gia 1) nói: "Phiếu Trắng" (Vì đây là `@PreAuthorize`).
7.  **`PreInvocationAuthorizationAdviceVoter`** (Chuyên gia 2) nói: "Để tôi xem. Tôi sẽ dùng *SpEL Parser*... Kết quả là `true` -\> Tôi bỏ **Phiếu Thuận**."
8.  "Sếp" (`AccessDecisionManager`) nhận Phiếu Thuận -\> "CHO PHÉP".
9.  "Anh Bảo vệ" (Proxy) cho phép cuộc gọi đi vào code nghiệp vụ thật.

## ❓ Câu hỏi: Trong annotation bảo mật (security annotation) nào bạn được phép sử dụng SpEL?

Câu trả lời của bạn là **hoàn toàn chính xác**.

Bạn đã liệt kê đúng 4 annotation (chú thích) hỗ trợ **Spring Expression Language (SpEL)**. Chúng là các công cụ "nâng cao" (so với `@Secured` hay `@RolesAllowed`) và được kích hoạt bằng:
`@EnableGlobalMethodSecurity(prePostEnabled = true)`

Việc sử dụng SpEL cho phép bạn viết các quy tắc bảo mật **động (dynamic)** và **nhận biết ngữ cảnh (context-aware)**, thay vì chỉ kiểm tra vai trò (role) tĩnh.

Hãy cùng diễn giải chi tiết 4 annotation này:

-----

### 1\. 🔑 Cặp `Authorize`: (Quyết định "CÓ" hoặc "KHÔNG")

Cặp này dùng để quyết định xem một phương thức có được phép thực thi hay không. Chúng giống như **"ổ khóa"** - kết quả chỉ có thể là `true` (mở) hoặc `false` (từ chối, ném `AccessDeniedException`).

#### A. `@PreAuthorize` (Kiểm tra *TRƯỚC KHI* chạy)

* **SpEL làm gì?** Nó thực thi biểu thức *trước khi* code bên trong phương thức được gọi.
* **Quyền năng:** Nó có thể truy cập vào các **tham số (arguments)** của phương thức bằng cách dùng cú pháp `#ten_tham_so`.
* **Khi nào dùng:** Đây là annotation phổ biến nhất. Dùng khi bạn muốn chặn một hành động *trước khi* nó xảy ra, dựa trên vai trò của người dùng VÀ/HOẶC dữ liệu họ gửi lên.
* **Ví dụ của bạn:**
  ```java
  @PreAuthorize("hasRole('ROLE_EMPLOYEE_CREATE') || 'TEST'.equals(#employee.getFirstName())")
  public void createEmployee(Employee employee) { ... }
  ```
    * **Diễn giải:** "Cho phép chạy nếu: người dùng có vai trò `ROLE_EMPLOYEE_CREATE` **HOẶC** tên (`firstName`) của đối tượng `employee` (tham số `#employee`) được gửi lên là 'TEST'."

#### B. `@PostAuthorize` (Kiểm tra *SAU KHI* chạy)

* **SpEL làm gì?** Nó thực thi biểu thức *sau khi* code bên trong phương thức đã chạy xong, nhưng *trước khi* trả kết quả về.
* **Quyền năng:** Nó có thể truy cập vào **kết quả trả về (return value)** của phương thức bằng cách dùng biến `returnObject`.
* **Khi nào dùng:** Dùng khi quy tắc bảo mật của bạn phụ thuộc vào *kết quả* (ví dụ: "Bạn chỉ được xem tài liệu này nếu bạn là chủ sở hữu của nó").
* **Cảnh báo:** Hãy cẩn thận\! Phương thức đã *chạy* rồi (ví dụ: một lệnh `DELETE` đã *xong*). Nó chủ yếu được dùng cho các phương thức chỉ đọc (read).
* **Ví dụ của bạn:**
  ```java
  @PostAuthorize("hasRole('ROLE_CUSTOMER_QA') && returnObject.firstName.equals('TEST')")
  public Customer findCustomer(long id) { ... }
  ```
    * **Diễn giải:** "Phương thức cứ chạy (lấy `Customer` từ DB). Sau đó, hãy kiểm tra: nếu người dùng *không* có vai trò `ROLE_CUSTOMER_QA` HOẶC `firstName` của `Customer` (đối tượng `returnObject`) *không* phải là 'TEST', thì hãy ném ra exception (không trả về kết quả)."

-----

### 2\. 🛡️ Cặp `Filter`: (Lọc "BỘ SƯU TẬP")

Cặp này **không** từ chối (deny) toàn bộ phương thức. Thay vào đó, nó **lọc (filters)** các phần tử *bên trong* một `Collection` (List, Set, Array) được truyền vào hoặc trả về. Chúng giống như một **"cái sàng"**.

#### A. `@PreFilter` (Lọc dữ liệu *ĐẦU VÀO*)

* **SpEL làm gì?** "Sàng" (lọc) một `Collection` *trước khi* nó được truyền vào phương thức.
* **Quyền năng:** Nó truy cập vào *mỗi phần tử* trong `Collection` bằng biến `filterObject`.
* **Khi nào dùng:** Khi bạn muốn "làm sạch" dữ liệu đầu vào. Ví dụ: một phương thức `saveAll(List<Document> docs)` và bạn muốn loại bỏ tất cả các `Document` mà người dùng không có quyền chỉnh sửa *trước khi* logic `saveAll` chạy.
* **Ví dụ của bạn:**
  ```java
  @PreFilter("hasRole('ROLE_CUSTOMER_QA') && filterObject.firstName.equals('TEST')")
  public void updateCustomers(List<Customer> customers) { ... }
  ```
    * **Diễn giải:** "Hãy lặp qua `List<Customer>` đầu vào. Chỉ giữ lại (`filterObject`) những `Customer` nào có `firstName` là 'TEST'. (Những `Customer` khác sẽ bị loại bỏ khỏi `List` trước khi phương thức `updateCustomers` chạy)."

#### B. `@PostFilter` (Lọc dữ liệu *ĐẦU RA*)

* **SpEL làm gì?** "Sàng" (lọc) một `Collection` mà phương thức *trả về*.
* **Quyền năng:** Nó truy cập vào *mỗi phần tử* trong `Collection` trả về bằng biến `filterObject`.
* **Khi nào dùng:** Đây là cách dùng phổ biến nhất. Dùng cho các phương thức `findAll()` hoặc `search()`.
* **Ví dụ của bạn:**
  ```java
  @PostFilter("hasRole('ROLE_CUSTOMER_QA') && filterObject.firstName.equals('TEST')")
  public Iterable<Customer> findAll() { ... }
  ```
    * **Diễn giải:** "Phương thức `findAll()` cứ chạy (lấy *tất cả* `Customer` từ DB). Sau đó, hãy lặp qua `List` kết quả. Chỉ trả về cho người dùng những `Customer` (`filterObject`) nào có `firstName` là 'TEST'."

-----

### 3\. Tóm tắt

| Annotation | Khi chạy? | Làm gì? | Biến SpEL đặc biệt |
| :--- | :--- | :--- | :--- |
| **`@PreAuthorize`** | **Trước khi** chạy | **Quyết định (CÓ/KHÔNG)** | `#ten_tham_so` |
| **`@PostAuthorize`** | **Sau khi** chạy | **Quyết định (CÓ/KHÔNG)** | `returnObject` |
| **`@PreFilter`** | **Trước khi** chạy | **Lọc (Filter)** Collection đầu vào | `filterObject` |
| **`@PostFilter`** | **Sau khi** chạy | **Lọc (Filter)** Collection đầu ra | `filterObject` |