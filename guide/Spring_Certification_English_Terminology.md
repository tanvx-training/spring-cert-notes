# SPRING PROFESSIONAL CERTIFICATION
## English Terminology Guide - Từ vựng Tiếng Anh Chuyên ngành

---

**Tổng hợp từ vựng và cụm từ tiếng Anh chuyên ngành trong bài thi Spring Professional**

*Tạo ngày: 27/12/2024*

---

## MỤC LỤC

1. [Thuật ngữ cơ bản](#1-thuật-ngữ-cơ-bản)
2. [Spring Core Terminology](#2-spring-core-terminology)
3. [Spring AOP Terminology](#3-spring-aop-terminology)
4. [Data Management Terminology](#4-data-management-terminology)
5. [Spring MVC Terminology](#5-spring-mvc-terminology)
6. [Spring Security Terminology](#6-spring-security-terminology)
7. [Testing Terminology](#7-testing-terminology)
8. [Spring Boot Terminology](#8-spring-boot-terminology)
9. [Động từ và Cụm động từ](#9-động-từ-và-cụm-động-từ)
10. [Patterns câu hỏi trong Exam](#10-patterns-câu-hỏi-trong-exam)
11. [Từ dễ nhầm lẫn](#11-từ-dễ-nhầm-lẫn)
12. [Tips đọc hiểu câu hỏi](#12-tips-đọc-hiểu-câu-hỏi)

---

## 1. THUẬT NGỮ CƠ BẢN

### 1.1. Programming Fundamentals

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Framework** | /ˈfreɪmwɜːrk/ | Khung làm việc | Cấu trúc code có sẵn để xây dựng ứng dụng |
| **Library** | /ˈlaɪbrəri/ | Thư viện | Tập hợp các hàm/class có thể dùng lại |
| **Container** | /kənˈteɪnər/ | Vùng chứa | Nơi quản lý vòng đời của objects |
| **Component** | /kəmˈpoʊnənt/ | Thành phần | Phần cấu thành của ứng dụng |
| **Configuration** | /kənˌfɪɡjəˈreɪʃən/ | Cấu hình | Thiết lập, cài đặt |
| **Instance** | /ˈɪnstəns/ | Thể hiện | Object được tạo từ class |
| **Implementation** | /ˌɪmplɪmenˈteɪʃən/ | Triển khai | Code thực thi interface |
| **Interface** | /ˈɪntərfeɪs/ | Giao diện | Contract định nghĩa methods |
| **Abstract** | /ˈæbstrækt/ | Trừu tượng | Không thể tạo instance trực tiếp |
| **Concrete** | /ˈkɑːnkriːt/ | Cụ thể | Class thực thi đầy đủ, có thể tạo instance |

### 1.2. OOP Concepts

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Inheritance** | /ɪnˈherɪtəns/ | Kế thừa | Class con kế thừa từ class cha |
| **Polymorphism** | /ˌpɑːliˈmɔːrfɪzəm/ | Đa hình | Một object có nhiều hình thái |
| **Encapsulation** | /ɪnˌkæpsjuˈleɪʃən/ | Đóng gói | Ẩn thông tin bên trong object |
| **Coupling** | /ˈkʌplɪŋ/ | Sự phụ thuộc | Mức độ phụ thuộc giữa các class |
| **Cohesion** | /koʊˈhiːʒən/ | Sự liên kết | Mức độ liên quan các methods trong class |
| **Tight coupling** | - | Phụ thuộc chặt | Phụ thuộc nhiều, khó thay đổi |
| **Loose coupling** | - | Phụ thuộc lỏng | Phụ thuộc ít, dễ thay đổi |

### 1.3. Common Verbs

| English | Phát âm | Tiếng Việt | Ví dụ |
|---------|---------|------------|-------|
| **Instantiate** | /ɪnˈstænʃieɪt/ | Tạo instance | Spring instantiates beans |
| **Inject** | /ɪnˈdʒekt/ | Tiêm vào | Inject dependencies |
| **Wire** | /waɪər/ | Nối, kết nối | Wire beans together |
| **Autowire** | /ˈɔːtoʊwaɪər/ | Tự động nối | Spring autowires dependencies |
| **Initialize** | /ɪˈnɪʃəlaɪz/ | Khởi tạo | Initialize the bean |
| **Destroy** | /dɪˈstrɔɪ/ | Hủy bỏ | Destroy the bean |
| **Invoke** | /ɪnˈvoʊk/ | Gọi | Invoke a method |
| **Trigger** | /ˈtrɪɡər/ | Kích hoạt | Trigger an event |
| **Handle** | /ˈhændl/ | Xử lý | Handle exceptions |
| **Resolve** | /rɪˈzɑːlv/ | Giải quyết | Resolve dependencies |

---

## 2. SPRING CORE TERMINOLOGY

### 2.1. Core Concepts

| English | Phát âm | Tiếng Việt | Giải thích & Ví dụ |
|---------|---------|------------|-------------------|
| **Bean** | /biːn/ | Bean | Object được quản lý bởi Spring Container |
| **IoC (Inversion of Control)** | /aɪ.oʊ.siː/ | Đảo ngược điều khiển | Framework điều khiển flow thay vì developer |
| **DI (Dependency Injection)** | /dɪˈpendənsi ɪnˈdʒekʃən/ | Tiêm phụ thuộc | Spring inject dependencies vào objects |
| **Application Context** | /ˌæplɪˈkeɪʃən ˈkɑːntekst/ | Ngữ cảnh ứng dụng | Container chứa và quản lý beans |
| **Bean Factory** | /biːn ˈfæktəri/ | Nhà máy Bean | Container cơ bản quản lý beans |
| **Scope** | /skoʊp/ | Phạm vi | Vòng đời và số lượng instances của bean |
| **Singleton** | /ˈsɪŋɡltən/ | Đơn thể | Chỉ có 1 instance trong container |
| **Prototype** | /ˈproʊtətaɪp/ | Nguyên mẫu | Tạo instance mới mỗi khi request |
| **Lifecycle** | /ˈlaɪfsaɪkəl/ | Vòng đời | Các giai đoạn từ tạo đến hủy bean |

**Câu ví dụ trong exam:**
```
"The ApplicationContext instantiates beans based on configuration."
→ ApplicationContext tạo beans dựa trên cấu hình.

"A singleton bean is shared across the entire container."
→ Bean singleton được chia sẻ trong toàn bộ container.
```

### 2.2. Configuration

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Java-based configuration** | - | Cấu hình Java | Dùng @Configuration và @Bean |
| **Annotation-based** | /ˌænəˈteɪʃən beɪst/ | Dựa trên annotation | Dùng @Component, @Autowired |
| **XML-based** | - | Dựa trên XML | Cấu hình trong file XML (legacy) |
| **Component scanning** | /kəmˈpoʊnənt ˈskænɪŋ/ | Quét component | Tự động tìm và đăng ký beans |
| **Base package** | /beɪs ˈpækɪdʒ/ | Package gốc | Package để bắt đầu scan |
| **Stereotype annotation** | /ˈsteriətaɪp/ | Annotation định kiểu | @Component, @Service, @Repository |

### 2.3. Dependency Injection

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Constructor injection** | /kənˈstrʌktər/ | DI qua constructor | Inject dependencies qua constructor |
| **Setter injection** | /ˈsetər/ | DI qua setter | Inject dependencies qua setter method |
| **Field injection** | /fiːld/ | DI qua field | Inject trực tiếp vào field (không khuyến khích) |
| **Required dependency** | /rɪˈkwaɪərd/ | Phụ thuộc bắt buộc | Dependency phải có |
| **Optional dependency** | /ˈɑːpʃənəl/ | Phụ thuộc tùy chọn | Có thể có hoặc không |
| **Circular dependency** | /ˈsɜːrkjələr/ | Phụ thuộc vòng | A phụ thuộc B và B phụ thuộc A |
| **Ambiguous dependency** | /æmˈbɪɡjuəs/ | Phụ thuộc mơ hồ | Nhiều beans cùng type |

**Câu ví dụ:**
```
"Constructor injection is preferred over field injection."
→ Constructor injection được ưu tiên hơn field injection.

"Use @Qualifier to resolve ambiguous dependencies."
→ Dùng @Qualifier để giải quyết phụ thuộc mơ hồ.
```

### 2.4. Bean Lifecycle

| English | Phát âm | Tiếng Việt | Giai đoạn |
|---------|---------|------------|-----------|
| **Instantiation** | /ɪnˌstænʃiˈeɪʃən/ | Khởi tạo | Tạo instance của bean |
| **Population** | /ˌpɑːpjuˈleɪʃən/ | Điền giá trị | Inject dependencies |
| **Initialization** | /ɪˌnɪʃəlaɪˈzeɪʃən/ | Khởi đầu | Gọi @PostConstruct, init-method |
| **Ready for use** | - | Sẵn sàng dùng | Bean đã sẵn sàng |
| **Destruction** | /dɪˈstrʌkʃən/ | Hủy bỏ | Gọi @PreDestroy, destroy-method |
| **Callback** | /ˈkɔːlbæk/ | Gọi lại | Method được gọi tự động |
| **Post-processor** | /poʊst ˈprɑːsesər/ | Xử lý sau | Xử lý beans sau khi tạo |

---

## 3. SPRING AOP TERMINOLOGY

### 3.1. AOP Core Terms

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Aspect** | /ˈæspekt/ | Khía cạnh | Module hóa cross-cutting concern |
| **Join point** | /dʒɔɪn pɔɪnt/ | Điểm nối | Điểm trong chương trình (method execution) |
| **Pointcut** | /ˈpɔɪntkʌt/ | Điểm cắt | Expression chọn join points |
| **Advice** | /ədˈvaɪs/ | Lời khuyên | Code thực thi tại join point |
| **Weaving** | /ˈwiːvɪŋ/ | Dệt, kết hợp | Quá trình áp dụng aspect |
| **Target object** | /ˈtɑːrɡɪt/ | Đối tượng đích | Object được áp dụng aspect |
| **Proxy** | /ˈprɑːksi/ | Proxy | Object trung gian wrap target |

**Câu ví dụ:**
```
"Advice is executed at join points matched by a pointcut."
→ Advice được thực thi tại các join point khớp với pointcut.

"Spring AOP uses proxies to apply aspects at runtime."
→ Spring AOP dùng proxy để áp dụng aspect lúc runtime.
```

### 3.2. Advice Types

| English | Phát âm | Tiếng Việt | Thời điểm |
|---------|---------|------------|-----------|
| **Before advice** | /bɪˈfɔːr/ | Advice trước | Chạy trước method |
| **After advice** | /ˈæftər/ | Advice sau | Chạy sau method (finally) |
| **After returning** | /rɪˈtɜːrnɪŋ/ | Sau khi trả về | Chạy khi method thành công |
| **After throwing** | /ˈθroʊɪŋ/ | Sau khi ném lỗi | Chạy khi có exception |
| **Around advice** | /əˈraʊnd/ | Advice bao quanh | Chạy trước và sau method |

### 3.3. Pointcut Expressions

| English | Phát âm | Tiếng Việt | Ý nghĩa |
|---------|---------|------------|---------|
| **Execution** | /ˌeksɪˈkjuːʃən/ | Thực thi | Method execution |
| **Within** | /wɪˈðɪn/ | Bên trong | Trong một package/class |
| **Args** | /ɑːrɡz/ | Tham số | Arguments của method |
| **Target** | /ˈtɑːrɡɪt/ | Đích | Target object type |
| **Wildcard** | /ˈwaɪldkɑːrd/ | Ký tự đại diện | * hoặc .. |

**Patterns thường gặp:**
```
"execution(* com.example.service.*.*(..))"
- execution: thực thi method
- *: any return type (bất kỳ kiểu trả về)
- com.example.service.*: any class in package (bất kỳ class)
- .*: any method (bất kỳ method)
- (..): any parameters (bất kỳ tham số)
```

### 3.4. Cross-cutting Concerns

| English | Phát âm | Tiếng Việt | Ví dụ |
|---------|---------|------------|-------|
| **Cross-cutting concern** | /krɔːs ˈkʌtɪŋ/ | Mối quan tâm cắt ngang | Logging, security, transaction |
| **Logging** | /ˈlɔːɡɪŋ/ | Ghi log | Ghi lại thông tin runtime |
| **Transaction management** | /trænˈzækʃən/ | Quản lý giao dịch | Đảm bảo ACID |
| **Security** | /sɪˈkjʊrəti/ | Bảo mật | Kiểm soát truy cập |
| **Performance monitoring** | /pərˈfɔːrməns/ | Giám sát hiệu năng | Đo thời gian thực thi |

---

## 4. DATA MANAGEMENT TERMINOLOGY

### 4.1. Database Concepts

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Persistence** | /pərˈsɪstəns/ | Lưu trữ | Lưu dữ liệu vào DB |
| **Entity** | /ˈentəti/ | Thực thể | Object ánh xạ với table |
| **Repository** | /rɪˈpɑːzətɔːri/ | Kho lưu trữ | Interface truy cập DB |
| **Data Access Object (DAO)** | - | Đối tượng truy cập DL | Pattern truy cập database |
| **ORM (Object-Relational Mapping)** | - | Ánh xạ đối tượng-quan hệ | Map objects với tables |
| **JPA (Java Persistence API)** | - | API lưu trữ Java | Specification cho ORM |
| **Hibernate** | /ˈhaɪbərneɪt/ | Hibernate | Implementation của JPA |

### 4.2. CRUD Operations

| English | Phát âm | Tiếng Việt | SQL tương ứng |
|---------|---------|------------|---------------|
| **Create** | /kriˈeɪt/ | Tạo mới | INSERT |
| **Read** | /riːd/ | Đọc | SELECT |
| **Update** | /ʌpˈdeɪt/ | Cập nhật | UPDATE |
| **Delete** | /dɪˈliːt/ | Xóa | DELETE |
| **Save** | /seɪv/ | Lưu | INSERT or UPDATE |
| **Find** | /faɪnd/ | Tìm | SELECT với WHERE |
| **Query** | /ˈkwɪri/ | Truy vấn | SELECT statement |

### 4.3. Transactions

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Transaction** | /trænˈzækʃən/ | Giao dịch | Đơn vị công việc nguyên tử |
| **Commit** | /kəˈmɪt/ | Xác nhận | Lưu thay đổi vào DB |
| **Rollback** | /ˈroʊlbæk/ | Hoàn tác | Hủy bỏ thay đổi |
| **Propagation** | /ˌprɑːpəˈɡeɪʃən/ | Truyền bá | Hành vi transaction |
| **Isolation** | /ˌaɪsəˈleɪʃən/ | Cô lập | Mức độ cô lập giữa transactions |
| **ACID** | - | ACID | Atomicity, Consistency, Isolation, Durability |
| **Dirty read** | /ˈdɜːrti riːd/ | Đọc bẩn | Đọc uncommitted data |
| **Phantom read** | /ˈfæntəm/ | Đọc ảo | Rows mới xuất hiện |

**Câu ví dụ:**
```
"A transaction is committed when all operations succeed."
→ Transaction được commit khi tất cả operations thành công.

"Use rollback to undo changes in case of errors."
→ Dùng rollback để hoàn tác thay đổi khi có lỗi.
```

### 4.4. Relationships

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **One-to-One** | - | Một-một | 1 entity liên kết 1 entity |
| **One-to-Many** | - | Một-nhiều | 1 entity liên kết nhiều entities |
| **Many-to-One** | - | Nhiều-một | Nhiều entities liên kết 1 entity |
| **Many-to-Many** | - | Nhiều-nhiều | Nhiều entities liên kết nhiều entities |
| **Unidirectional** | /ˌjuːnɪdəˈrekʃənəl/ | Một chiều | Chỉ 1 side biết relationship |
| **Bidirectional** | /ˌbaɪdəˈrekʃənəl/ | Hai chiều | Cả 2 sides biết relationship |
| **Cascade** | /kæsˈkeɪd/ | Thác, lan truyền | Operations lan sang related entities |
| **Orphan removal** | /ˈɔːrfən/ | Xóa mồ côi | Xóa entities không còn reference |

### 4.5. Fetch Types

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Lazy loading** | /ˈleɪzi ˈloʊdɪŋ/ | Tải lười | Load dữ liệu khi cần |
| **Eager loading** | /ˈiːɡər/ | Tải háo hức | Load dữ liệu ngay lập tức |
| **Fetch** | /fetʃ/ | Lấy về | Retrieve data từ DB |
| **N+1 problem** | - | Vấn đề N+1 | 1 query + N queries cho related data |
| **Join fetch** | /dʒɔɪn fetʃ/ | Fetch bằng join | Load data cùng lúc bằng JOIN |

---

## 5. SPRING MVC TERMINOLOGY

### 5.1. Web Concepts

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **MVC (Model-View-Controller)** | - | MVC | Pattern tách biệt UI, logic, data |
| **Request** | /rɪˈkwest/ | Yêu cầu | HTTP request từ client |
| **Response** | /rɪˈspɑːns/ | Phản hồi | HTTP response về client |
| **Endpoint** | /ˈendpɔɪnt/ | Điểm cuối | URL mà API nhận request |
| **Route** | /ruːt/ | Đường dẫn | Map URL với handler |
| **Handler** | /ˈhændlər/ | Bộ xử lý | Method xử lý request |
| **Dispatcher** | /dɪˈspætʃər/ | Bộ phân phối | Front controller dispatch requests |

### 5.2. HTTP Methods

| English | Phát âm | Tiếng Việt | Mục đích |
|---------|---------|------------|----------|
| **GET** | /ɡet/ | GET | Lấy dữ liệu |
| **POST** | /poʊst/ | POST | Tạo mới |
| **PUT** | /pʊt/ | PUT | Cập nhật toàn bộ |
| **PATCH** | /pætʃ/ | PATCH | Cập nhật một phần |
| **DELETE** | /dɪˈliːt/ | DELETE | Xóa |
| **Idempotent** | /aɪˈdempətənt/ | Bất biến | Gọi nhiều lần = gọi 1 lần |
| **Safe** | /seɪf/ | An toàn | Không thay đổi dữ liệu |

**Câu ví dụ:**
```
"GET requests are safe and idempotent."
→ GET requests an toàn và bất biến.

"Use POST to create a new resource."
→ Dùng POST để tạo resource mới.
```

### 5.3. REST API

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **REST (Representational State Transfer)** | /rest/ | REST | Architectural style cho web services |
| **RESTful** | /ˈrestfəl/ | RESTful | Tuân theo REST principles |
| **Resource** | /ˈriːsɔːrs/ | Tài nguyên | Entity được expose qua API |
| **URI (Uniform Resource Identifier)** | - | URI | Định danh resource |
| **Stateless** | /ˈsteɪtləs/ | Không trạng thái | Không lưu client state |
| **Payload** | /ˈpeɪloʊd/ | Tải trọng | Dữ liệu trong request/response body |
| **JSON (JavaScript Object Notation)** | /ˈdʒeɪsən/ | JSON | Format dữ liệu |
| **XML** | - | XML | Format dữ liệu (legacy) |

### 5.4. Status Codes

| Code | English | Tiếng Việt | Khi nào dùng |
|------|---------|------------|--------------|
| **200 OK** | /tuː ˈhʌndrəd/ | OK | Request thành công |
| **201 Created** | /kriˈeɪtɪd/ | Đã tạo | Resource được tạo thành công |
| **204 No Content** | /noʊ ˈkɑːntent/ | Không nội dung | Success nhưng không trả về data |
| **400 Bad Request** | /bæd rɪˈkwest/ | Request sai | Lỗi validation, format |
| **401 Unauthorized** | /ʌnˈɔːθəraɪzd/ | Chưa xác thực | Cần đăng nhập |
| **403 Forbidden** | /fərˈbɪdən/ | Cấm | Không có quyền |
| **404 Not Found** | /nɑːt faʊnd/ | Không tìm thấy | Resource không tồn tại |
| **500 Internal Server Error** | /ɪnˈtɜːrnəl ˈsɜːrvər/ | Lỗi server | Lỗi bên trong server |

### 5.5. Request/Response

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Query parameter** | /ˈkwɪri pəˈræmɪtər/ | Tham số truy vấn | ?key=value trong URL |
| **Path variable** | /pæθ ˈveriəbl/ | Biến đường dẫn | {id} trong URL |
| **Request body** | /rɪˈkwest ˈbɑːdi/ | Thân request | Dữ liệu gửi lên |
| **Response body** | /rɪˈspɑːns ˈbɑːdi/ | Thân response | Dữ liệu trả về |
| **Header** | /ˈhedər/ | Tiêu đề | Metadata của request/response |
| **Cookie** | /ˈkʊki/ | Cookie | Small data lưu trên client |
| **Content type** | /ˈkɑːntent taɪp/ | Loại nội dung | Format của data (JSON, XML) |

---

## 6. SPRING SECURITY TERMINOLOGY

### 6.1. Security Fundamentals

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Authentication** | /ɔːˌθentɪˈkeɪʃən/ | Xác thực | Xác định "bạn là ai?" |
| **Authorization** | /ˌɔːθərəˈzeɪʃən/ | Phân quyền | Xác định "bạn được làm gì?" |
| **Credentials** | /krɪˈdenʃəlz/ | Thông tin đăng nhập | Username và password |
| **Principal** | /ˈprɪnsəpəl/ | Chủ thể | User đã authenticated |
| **Authority** | /əˈθɔːrəti/ | Quyền hạn | Permission của user |
| **Role** | /roʊl/ | Vai trò | Nhóm authorities |
| **Permission** | /pərˈmɪʃən/ | Cho phép | Quyền làm hành động cụ thể |

**So sánh:**
```
Authentication (Xác thực):
- "Who are you?" - Bạn là ai?
- Login với username/password

Authorization (Phân quyền):
- "What can you do?" - Bạn được làm gì?
- Check user có role/permission không
```

### 6.2. Security Concepts

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Filter chain** | /ˈfɪltər tʃeɪn/ | Chuỗi bộ lọc | Các filters xử lý security |
| **Session** | /ˈseʃən/ | Phiên làm việc | Lưu trạng thái user |
| **Token** | /ˈtoʊkən/ | Mã thông báo | Credentials dạng string (JWT) |
| **Encoding** | /ɪnˈkoʊdɪŋ/ | Mã hóa (thuận) | Chuyển đổi format (có thể đảo ngược) |
| **Encryption** | /ɪnˈkrɪpʃən/ | Mã hóa (bảo mật) | Mã hóa an toàn (khó đảo ngược) |
| **Hashing** | /ˈhæʃɪŋ/ | Băm | One-way transformation |
| **Salt** | /sɔːlt/ | Muối | Random data thêm vào hash |
| **CSRF (Cross-Site Request Forgery)** | - | Giả mạo yêu cầu | Attack giả mạo request |
| **XSS (Cross-Site Scripting)** | - | Kịch bản xuyên site | Inject malicious scripts |

### 6.3. Access Control

| English | Phát âm | Tiếng Việt | Ví dụ |
|---------|---------|------------|-------|
| **Permit all** | /pərˈmɪt ɔːl/ | Cho phép tất cả | Public endpoints |
| **Deny all** | /dɪˈnaɪ ɔːl/ | Từ chối tất cả | Restricted endpoints |
| **Authenticated** | /ɔːˈθentɪkeɪtɪd/ | Đã xác thực | Logged in users |
| **Anonymous** | /əˈnɑːnɪməs/ | Ẩn danh | Not logged in |
| **Has role** | - | Có vai trò | User có role cụ thể |
| **Has authority** | - | Có quyền | User có permission cụ thể |

**Câu ví dụ:**
```
"Only authenticated users can access this endpoint."
→ Chỉ users đã xác thực mới truy cập endpoint này.

"Users must have ADMIN role to perform this action."
→ Users phải có role ADMIN để thực hiện hành động này.
```

---

## 7. TESTING TERMINOLOGY

### 7.1. Testing Types

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Unit test** | /ˈjuːnɪt test/ | Test đơn vị | Test một class riêng lẻ |
| **Integration test** | /ˌɪntɪˈɡreɪʃən/ | Test tích hợp | Test nhiều components cùng nhau |
| **End-to-end test** | /end tuː end/ | Test đầu-cuối | Test toàn bộ flow |
| **Slice test** | /slaɪs/ | Test lát cắt | Test một layer cụ thể |
| **Mock** | /mɑːk/ | Giả lập | Fake object thay thế dependency |
| **Stub** | /stʌb/ | Stub | Predefined responses |
| **Spy** | /spaɪ/ | Spy | Wrap real object, có thể verify |

### 7.2. Testing Concepts

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Test case** | /test keɪs/ | Trường hợp test | Một test cụ thể |
| **Test suite** | /test swiːt/ | Bộ test | Tập hợp test cases |
| **Assertion** | /əˈsɜːrʃən/ | Khẳng định | Verify expected vs actual |
| **Expected** | /ɪkˈspektɪd/ | Mong đợi | Giá trị expected |
| **Actual** | /ˈæktʃuəl/ | Thực tế | Giá trị actual |
| **Given-When-Then** | - | Cho-Khi-Thì | Pattern viết test (AAA) |
| **Arrange-Act-Assert** | - | Chuẩn bị-Hành động-Kiểm tra | Pattern viết test |
| **Test fixture** | /ˈfɪkstʃər/ | Cố định test | Test data setup |
| **Test double** | /ˈdʌbəl/ | Đôi test | Mock, stub, spy, fake |

**Pattern AAA:**
```
Arrange (Given): Chuẩn bị data và mocks
Act (When): Thực thi code cần test
Assert (Then): Verify kết quả
```

### 7.3. Test Execution

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Run** | /rʌn/ | Chạy | Execute tests |
| **Pass** | /pæs/ | Pass | Test thành công |
| **Fail** | /feɪl/ | Fail | Test thất bại |
| **Skip** | /skɪp/ | Bỏ qua | Không chạy test |
| **Timeout** | /ˈtaɪmaʊt/ | Hết giờ | Test chạy quá lâu |
| **Coverage** | /ˈkʌvərɪdʒ/ | Độ phủ | % code được test |
| **Flaky test** | /ˈfleɪki/ | Test không ổn định | Sometimes pass, sometimes fail |

### 7.4. Verification

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Verify** | /ˈverɪfaɪ/ | Xác minh | Check method được gọi |
| **Times** | /taɪmz/ | Số lần | Số lần method được gọi |
| **Never** | /ˈnevər/ | Không bao giờ | Method không được gọi |
| **At least once** | - | Ít nhất một lần | Gọi >= 1 lần |
| **At most** | - | Nhiều nhất | Gọi <= N lần |
| **In order** | - | Theo thứ tự | Methods gọi đúng thứ tự |

---

## 8. SPRING BOOT TERMINOLOGY

### 8.1. Boot Concepts

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Auto-configuration** | /ˈɔːtoʊ kənˌfɪɡjəˈreɪʃən/ | Tự động cấu hình | Spring Boot tự config beans |
| **Opinionated** | /əˈpɪnjəneɪtɪd/ | Có quan điểm | Có defaults được recommend |
| **Convention over configuration** | - | Quy ước hơn cấu hình | Ưu tiên conventions |
| **Starter** | /ˈstɑːrtər/ | Starter | Pre-configured dependencies |
| **Dependency management** | /dɪˈpendənsi/ | Quản lý phụ thuộc | Manage versions tự động |
| **Embedded server** | /ɪmˈbedɪd/ | Server nhúng | Tomcat/Jetty trong JAR |
| **Standalone** | /ˌstændəˈloʊn/ | Độc lập | Chạy mà không cần server external |

**Câu ví dụ:**
```
"Spring Boot provides opinionated defaults."
→ Spring Boot cung cấp các giá trị mặc định được recommend.

"Auto-configuration is enabled by default."
→ Auto-configuration được bật mặc định.
```

### 8.2. Configuration

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Properties file** | /ˈprɑːpərtiz faɪl/ | File thuộc tính | application.properties |
| **YAML** | /ˈjæməl/ | YAML | application.yml format |
| **Profile** | /ˈproʊfaɪl/ | Profile | Environment-specific config |
| **Active profile** | /ˈæktɪv/ | Profile đang hoạt động | Profile được kích hoạt |
| **Externalized configuration** | /ɪkˈstɜːrnəlaɪzd/ | Cấu hình bên ngoài | Config outside code |
| **Environment variable** | /ɪnˈvaɪrənmənt/ | Biến môi trường | OS-level variables |
| **Command line argument** | /kəˈmænd laɪn/ | Tham số dòng lệnh | Arguments khi start app |

### 8.3. Actuator

| English | Phát âm | Tiếng Việt | Giải thích |
|---------|---------|------------|------------|
| **Actuator** | /ˈæktʃueɪtər/ | Bộ kích hoạt | Production-ready features |
| **Health check** | /helθ tʃek/ | Kiểm tra sức khỏe | Check app health |
| **Metrics** | /ˈmetrɪks/ | Số liệu | Application metrics |
| **Endpoint** | /ˈendpɔɪnt/ | Điểm cuối | Management URL |
| **Exposure** | /ɪkˈspoʊʒər/ | Phơi bày | Make endpoints available |
| **Custom metric** | /ˈkʌstəm/ | Metric tùy chỉnh | User-defined metrics |
| **Health indicator** | /ˈɪndɪkeɪtər/ | Chỉ báo sức khỏe | Component health status |

---

## 9. ĐỘNG TỪ VÀ CỤM ĐỘNG TỪ

### 9.1. Configuration & Setup

| English | Tiếng Việt | Ví dụ |
|---------|------------|-------|
| **configure** | cấu hình | Configure the application |
| **enable** | bật | Enable auto-configuration |
| **disable** | tắt | Disable a feature |
| **set up** | thiết lập | Set up the database |
| **initialize** | khởi tạo | Initialize the bean |
| **bootstrap** | khởi động | Bootstrap the application |

### 9.2. Dependency Management

| English | Tiếng Việt | Ví dụ |
|---------|------------|-------|
| **inject** | tiêm | Inject dependencies |
| **autowire** | tự động nối | Autowire the bean |
| **wire** | nối | Wire beans together |
| **resolve** | giải quyết | Resolve dependencies |
| **provide** | cung cấp | Provide a bean |
| **supply** | cung cấp | Supply the dependency |

### 9.3. Lifecycle

| English | Tiếng Việt | Ví dụ |
|---------|------------|-------|
| **instantiate** | tạo instance | Instantiate the bean |
| **create** | tạo | Create a new object |
| **destroy** | hủy | Destroy the bean |
| **initialize** | khởi tạo | Initialize after creation |
| **dispose** | giải phóng | Dispose of resources |
| **clean up** | dọn dẹp | Clean up resources |

### 9.4. Data Operations

| English | Tiếng Việt | Ví dụ |
|---------|------------|-------|
| **persist** | lưu trữ | Persist the entity |
| **save** | lưu | Save to database |
| **update** | cập nhật | Update the record |
| **delete** | xóa | Delete the entity |
| **fetch** | lấy về | Fetch data from DB |
| **retrieve** | truy xuất | Retrieve the entity |
| **query** | truy vấn | Query the database |
| **load** | tải | Load the data |

### 9.5. Transaction Management

| English | Tiếng Việt | Ví dụ |
|---------|------------|-------|
| **commit** | xác nhận | Commit the transaction |
| **rollback** | hoàn tác | Rollback on error |
| **begin** | bắt đầu | Begin a transaction |
| **end** | kết thúc | End the transaction |
| **propagate** | truyền bá | Propagate the transaction |

### 9.6. Request Handling

| English | Tiếng Việt | Ví dụ |
|---------|------------|-------|
| **handle** | xử lý | Handle the request |
| **process** | xử lý | Process the data |
| **dispatch** | phân phối | Dispatch the request |
| **route** | định tuyến | Route to controller |
| **map** | ánh xạ | Map URL to handler |
| **bind** | ràng buộc | Bind request parameters |
| **validate** | xác thực | Validate the input |
| **serialize** | tuần tự hóa | Serialize to JSON |
| **deserialize** | giải tuần tự | Deserialize from JSON |

### 9.7. Testing

| English | Tiếng Việt | Ví dụ |
|---------|------------|-------|
| **mock** | giả lập | Mock the dependency |
| **stub** | stub | Stub the method |
| **verify** | xác minh | Verify method called |
| **assert** | khẳng định | Assert the result |
| **expect** | mong đợi | Expect certain behavior |
| **perform** | thực hiện | Perform the request |

---

## 10. PATTERNS CÂU HỎI TRONG EXAM

### 10.1. Definition Questions

**Pattern:**
```
"What is [concept]?"
"Which of the following describes [concept]?"
"[Concept] is used for..."
```

**Từ khóa:**
- **is used for** - được dùng để
- **is responsible for** - chịu trách nhiệm về
- **provides** - cung cấp
- **enables** - cho phép
- **manages** - quản lý

**Ví dụ:**
```
"What is Dependency Injection?"
→ Dependency Injection is used for providing dependencies to a class.

"Which component is responsible for managing beans?"
→ ApplicationContext is responsible for managing beans.
```

### 10.2. Comparison Questions

**Pattern:**
```
"What is the difference between A and B?"
"Which is preferred: A or B?"
"Compare A and B"
```

**Từ khóa:**
- **difference between** - khác biệt giữa
- **compared to** - so với
- **preferred** - được ưu tiên
- **better than** - tốt hơn
- **instead of** - thay vì

**Ví dụ:**
```
"What is the difference between @Component and @Service?"
→ @Service is a specialization of @Component for business logic.

"Constructor injection is preferred over field injection."
→ Constructor injection được ưu tiên hơn field injection.
```

### 10.3. When/Which Questions

**Pattern:**
```
"When should you use [feature]?"
"Which annotation is used for [purpose]?"
"In which scenario..."
```

**Từ khóa:**
- **when** - khi nào
- **which** - cái nào
- **should** - nên
- **must** - phải
- **appropriate** - thích hợp

**Ví dụ:**
```
"When should you use @Transactional?"
→ Use @Transactional for methods that modify data.

"Which annotation is used for REST controllers?"
→ @RestController is used for REST controllers.
```

### 10.4. True/False Questions

**Pattern:**
```
"Which statement is TRUE?"
"Which statement is FALSE?"
"Which of the following is correct?"
```

**Từ khóa:**
- **true** - đúng
- **false** - sai
- **correct** - đúng
- **incorrect** - sai
- **valid** - hợp lệ
- **invalid** - không hợp lệ

**Chú ý:**
- **NOT** - KHÔNG (phủ định)
- **EXCEPT** - TRỪ, NGOẠI TRỪ
- Đọc kỹ xem hỏi TRUE hay FALSE!

### 10.5. Best Practice Questions

**Pattern:**
```
"What is the best way to..."
"Which is the recommended approach..."
"What is the preferred method..."
```

**Từ khóa:**
- **best** - tốt nhất
- **recommended** - được khuyến nghị
- **preferred** - được ưu tiên
- **should** - nên
- **avoid** - tránh

**Ví dụ:**
```
"What is the best way to inject dependencies?"
→ Constructor injection is the best way.

"Which approach should be avoided?"
→ Field injection should be avoided.
```

### 10.6. Code Analysis Questions

**Pattern:**
```
"What will happen when..."
"What is wrong with this code?"
"What is missing in this code?"
```

**Từ khóa:**
- **will happen** - sẽ xảy ra
- **wrong** - sai
- **missing** - thiếu
- **problem** - vấn đề
- **issue** - vấn đề
- **error** - lỗi

**Ví dụ:**
```
"What is missing in this controller?"
→ The controller is missing @RestController annotation.

"What will happen if @Transactional is not used?"
→ Changes will not be committed to the database.
```

---

## 11. TỪ DỄ NHẦM LẪN

### 11.1. Similar Words

| Từ 1 | Từ 2 | Khác biệt |
|------|------|-----------|
| **affect** (ảnh hưởng - động từ) | **effect** (hiệu quả - danh từ) | "Changes affect performance" vs "The effect is significant" |
| **advise** (khuyên - động từ) | **advice** (lời khuyên - danh từ) | "I advise you" vs "Give me advice" |
| **accept** (chấp nhận) | **except** (ngoại trừ) | "Accept the request" vs "All except this one" |
| **assure** (đảm bảo với ai) | **ensure** (đảm bảo điều gì) | "Assure the client" vs "Ensure quality" |

### 11.2. Technical Terms

| Dễ nhầm | Nghĩa đúng | Ghi nhớ |
|---------|------------|---------|
| **Instantiate** | Tạo instance | Create an object |
| **Initialize** | Khởi tạo giá trị | Set initial values |
| **Implement** | Triển khai | Code implementation |
| **Deploy** | Triển khai (đưa lên server) | Put to production |
| **Expose** | Phơi bày, public | Make available |
| **Invoke** | Gọi | Call a method |
| **Persist** | Lưu trữ vào DB | Save to database |
| **Retrieve** | Truy xuất từ DB | Get from database |

### 11.3. Prepositions (Giới từ)

**Common prepositions với thuật ngữ:**

| Cụm từ | Nghĩa | Ví dụ |
|--------|-------|-------|
| **depend on** | phụ thuộc vào | Depend on Spring |
| **rely on** | dựa vào | Rely on auto-configuration |
| **based on** | dựa trên | Based on conditions |
| **inject into** | tiêm vào | Inject into the bean |
| **map to** | ánh xạ tới | Map to the table |
| **bind to** | ràng buộc với | Bind to properties |
| **connect to** | kết nối tới | Connect to database |
| **configure for** | cấu hình cho | Configure for production |

### 11.4. Modal Verbs (Động từ khuyết thiếu)

| Modal | Nghĩa | Mức độ | Ví dụ |
|-------|-------|--------|-------|
| **must** | phải | Bắt buộc 100% | "You must use @Transactional" |
| **should** | nên | Khuyến nghị | "You should use constructor injection" |
| **can** | có thể | Khả năng | "You can use @Qualifier" |
| **may** | có thể | Cho phép | "You may override this method" |
| **could** | có thể | Gợi ý | "You could use a different approach" |

**Mức độ bắt buộc:**
```
must > should > can > may > could
(bắt buộc → khuyến nghị → khả năng → cho phép → gợi ý)
```

---

## 12. TIPS ĐỌC HIỂU CÂU HỎI

### 12.1. Keywords to Watch

**Từ khóa CỰC KỲ QUAN TRỌNG:**

❌ **NOT** - KHÔNG, phủ định
```
"Which is NOT a valid bean scope?"
→ Tìm scope KHÔNG hợp lệ
```

❌ **EXCEPT** - TRỪ, NGOẠI TRỪ
```
"All are true EXCEPT:"
→ Tìm câu SAI
```

✅ **BEST** - TỐT NHẤT
```
"What is the BEST way..."
→ Có nhiều cách đúng, chọn cách TỐT NHẤT
```

✅ **MOST** - NHẤT
```
"Which is MOST appropriate?"
→ Chọn phù hợp NHẤT
```

✅ **ALWAYS** - LUÔN LUÔN
```
"This ALWAYS happens when..."
→ Xảy ra trong MỌI trường hợp
```

❌ **NEVER** - KHÔNG BAO GIỜ
```
"You should NEVER use..."
→ KHÔNG BAO GIỜ được dùng
```

### 12.2. Question Patterns

**Pattern 1: Definition**
```
"What is X?"
"Which describes X?"
→ Tìm định nghĩa
```

**Pattern 2: Purpose**
```
"X is used for..."
"The purpose of X is..."
→ Tìm mục đích sử dụng
```

**Pattern 3: Comparison**
```
"Difference between X and Y?"
"X vs Y?"
→ So sánh khác biệt
```

**Pattern 4: Sequence**
```
"What happens first?"
"Order of execution?"
→ Tìm thứ tự
```

**Pattern 5: Problem**
```
"What is wrong?"
"What is missing?"
→ Tìm lỗi, tìm điều thiếu
```

### 12.3. Elimination Strategy

**Chiến lược loại trừ:**

1. **Đọc câu hỏi kỹ** - Chú ý NOT, EXCEPT, BEST
2. **Loại đáp án SAI RÕ RÀNG** - Eliminate obviously wrong
3. **So sánh đáp án còn lại** - Compare remaining options
4. **Chọn đáp án PHÙ HỢP NHẤT** - Pick the MOST appropriate

**Ví dụ:**
```
Question: "Which is the BEST way to inject dependencies?"
A) XML configuration (Legacy, không khuyến nghị)
B) Field injection (Không tốt, khó test)
C) Setter injection (OK nhưng không tốt nhất)
D) Constructor injection (✓ TỐT NHẤT)

Loại A (legacy) → Loại B (bad practice) 
→ So sánh C và D → Chọn D (best practice)
```

### 12.4. Time Management Tips

**Phân bổ thời gian:**

```
Câu dễ (biết ngay): 30 giây
Câu trung bình: 2 phút
Câu khó: 3-4 phút

Chiến lược:
1. First pass: Trả lời câu dễ (60 phút)
2. Second pass: Câu khó (50 phút)
3. Review: Kiểm tra lại (20 phút)
```

**Đánh dấu câu:**
- ⭐ **Chắc chắn** - Đã trả lời đúng
- ⚠️ **Không chắc** - Cần review
- ❓ **Khó** - Cần suy nghĩ thêm

### 12.5. Common Traps

**Bẫy thường gặp:**

❌ **Bẫy 1: Đọc lướt**
```
Question: "Which is NOT recommended?"
Trap: Chọn đáp án ĐƯỢC recommend (đọc thiếu NOT)
Fix: Đọc kỹ, gạch chân NOT
```

❌ **Bẫy 2: Kiến thức cũ**
```
Question: "In Spring Boot 3..."
Trap: Áp dụng kiến thức Spring cũ
Fix: Chú ý version, best practices mới
```

❌ **Bẫy 3: Tất cả đều đúng**
```
Question: "Which is BEST?"
Trap: Chọn đáp án đúng nhưng không phải TỐT NHẤT
Fix: Tìm best practice, recommended approach
```

❌ **Bẫy 4: Multiple select**
```
Question: "Select TWO correct answers"
Trap: Chọn 1 hoặc 3 đáp án
Fix: Kiểm tra đã chọn đúng số lượng
```

---

## 13. PRACTICE EXERCISES

### Exercise 1: Vocabulary Matching

**Match the terms:**
1. Instantiate - A. Tạo instance
2. Persist - B. Lưu vào DB
3. Autowire - C. Tự động nối
4. Deploy - D. Đưa lên production

**Answers:** 1-A, 2-B, 3-C, 4-D

### Exercise 2: Fill in the Blanks

**Complete the sentences:**
1. Spring uses _____ injection to provide dependencies.
2. A _____ bean has only one instance per container.
3. @Transactional ensures _____ consistency.
4. REST APIs should be _____.

**Answers:**
1. dependency
2. singleton
3. data/transaction
4. stateless

### Exercise 3: True or False

**Mark T (True) or F (False):**
1. "Eager loading" means load immediately. (T/F)
2. "POST" is idempotent. (T/F)
3. "@SpringBootTest" loads full context. (T/F)
4. "Circular dependency" is recommended. (T/F)

**Answers:** 1-T, 2-F, 3-T, 4-F

### Exercise 4: Translate

**Dịch sang tiếng Việt:**
1. "The bean is instantiated by the container."
2. "Use constructor injection for required dependencies."
3. "This annotation enables auto-configuration."
4. "The transaction will be rolled back on exception."

**Answers:**
1. Bean được tạo bởi container.
2. Dùng constructor injection cho dependencies bắt buộc.
3. Annotation này bật auto-configuration.
4. Transaction sẽ được rollback khi có exception.

---

## 14. QUICK REFERENCE CARD

### Must-Know Verbs

```
✅ POSITIVE ACTIONS:
configure, enable, inject, create, persist, 
commit, handle, validate, authenticate

❌ NEGATIVE ACTIONS:
disable, destroy, rollback, deny, reject,
fail, throw, terminate

🔄 PROCESS:
initialize, process, execute, invoke,
trigger, resolve, fetch, retrieve
```

### Key Adjectives

```
✅ GOOD:
valid, correct, appropriate, proper,
recommended, preferred, best

❌ BAD:
invalid, incorrect, wrong, improper,
deprecated, obsolete

⚠️ STATUS:
active, enabled, required, optional,
available, ready, pending
```

### Common Prepositions

```
Dependency ON something
Based ON conditions
Inject INTO bean
Map TO table
Bind TO properties
Connect TO database
Configure FOR environment
Responsible FOR managing
```

---

## 15. FINAL TIPS

### Before Exam Day

**1 tuần trước:**
- [ ] Đọc lại guideline này 2-3 lần
- [ ] Làm practice exercises
- [ ] Học thuộc common verbs
- [ ] Ghi nhớ keywords (NOT, EXCEPT, BEST)

**1 ngày trước:**
- [ ] Quick review vocabulary
- [ ] Review patterns câu hỏi
- [ ] Nghỉ ngơi đủ giấc

### During Exam

**Đọc câu hỏi:**
1. ✅ Đọc CHẬM và KỸ
2. ✅ Gạch chân keywords
3. ✅ Chú ý NOT, EXCEPT, BEST
4. ✅ Đọc TẤT CẢ đáp án

**Trả lời:**
1. ✅ Loại đáp án sai rõ ràng
2. ✅ So sánh đáp án còn lại
3. ✅ Chọn đáp án phù hợp NHẤT
4. ✅ Đánh dấu nếu không chắc

**Time management:**
- ⏱️ Không dành quá 3 phút/câu
- ⏱️ Skip câu khó, quay lại sau
- ⏱️ Để 20 phút review cuối

### Common Mistakes to Avoid

❌ Đọc lướt câu hỏi
❌ Bỏ qua keywords (NOT, EXCEPT)
❌ Không đọc hết đáp án
❌ Thay đổi đáp án quá nhiều
❌ Để câu trống (no penalty!)
❌ Quá stress, không tập trung

### Success Mindset

```
"I have prepared well"
"I know the concepts"
"I will read carefully"
"I will manage my time"
"I will pass this exam"
```

---

## KẾT LUẬN

**Remember:**
- 📖 **Read carefully** - Đọc kỹ
- 🔍 **Watch keywords** - Chú ý từ khóa
- ⏱️ **Manage time** - Quản lý thời gian
- ✅ **Answer all** - Trả lời hết
- 💪 **Stay confident** - Tự tin

**Most Important:**
```
Understanding > Memorization
Concepts > Vocabulary
Practice > Theory
```

**You've got this! Good luck! 🍀**

---

**Chúc bạn thành công với Spring Professional Certification!** 🎓🚀

*Tài liệu được tạo ngày 27/12/2024*
*Cover tất cả vocabulary và patterns từ 7 modules*
