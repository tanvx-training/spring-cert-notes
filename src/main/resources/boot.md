## ❓ Câu hỏi: \#What is Spring Boot?

(Spring Boot là gì?)

### 1\. Vấn đề mà Spring Boot giải quyết

Để hiểu Spring Boot, trước tiên chúng ta cần hiểu "nỗi đau" của **Spring Framework** (phiên bản trước khi có Spring Boot):

* **Spring Framework** cực kỳ mạnh mẽ, nhưng việc cấu hình (configuration) nó rất phức tạp và tốn thời gian.
* Để làm một dự án web "Hello World", bạn có thể phải mất cả buổi chiều để:
    * Tạo file `web.xml`.
    * Cấu hình `DispatcherServlet`.
    * Tạo các file XML cấu hình (như `application-context.xml`) để định nghĩa `Beans`.
    * Cấu hình `ViewResolver`, `DataSource`, `TransactionManager`, v.v.
    * Đảm bảo hàng chục file `.jar` (dependencies) tương thích phiên bản với nhau.

Nó giống như bạn được đưa cho một hộp gạch Lego rời rạc—bạn có thể xây bất cứ thứ gì, nhưng bạn phải tự mình tìm và lắp ráp từng viên gạch nhỏ.

-----

### 2\. Spring Boot là gì? (Giải pháp)

**Spring Boot** là một framework (hoặc một module) giúp bạn *loại bỏ* gần như toàn bộ cấu hình phức tạp đó.

Nó đưa ra một **cách tiếp cận mang tính áp đặt (opinionated)**: Spring Boot "có ý kiến riêng" về cách tốt nhất để cấu hình một ứng dụng Spring. Nếu bạn đồng ý với "ý kiến" đó, bạn có thể tạo ra một ứng dụng chỉ trong vài phút.

Nó giống như một **bộ xếp hình Lego (Lego Kit)**. Thay vì gạch rời, bạn nhận được một bộ "Tàu chiến" với tất cả các mảnh cần thiết đã được phân loại và một hướng dẫn lắp ráp cực nhanh.

Như bạn đã nói, mục tiêu của nó là giúp bạn tạo ra các ứng dụng **"stand-alone" (độc lập)** và **"production-grade" (đủ chuẩn production)** một cách nhanh nhất có thể.

-----

### 3\. Các tính năng cốt lõi (The "Magic")

Spring Boot đạt được sự đơn giản này thông qua 4 cơ chế chính mà bạn đã liệt kê:

#### 1\. 📦 Spring Boot Starters (Các bộ khởi động)

* **Là gì:** Đây là các file `pom.xml` (hoặc `build.gradle`) được đóng gói sẵn. Chúng là một "bộ sưu tập" các dependency (thư viện) phổ biến thường đi chung với nhau.
* **Ví dụ:** Thay vì phải tự mình thêm `spring-web`, `spring-webmvc`, `jackson-databind` (để xử lý JSON), và `tomcat-embed` (để chạy server), bạn chỉ cần thêm **một** dependency duy nhất:
  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  ```
* **Lợi ích:** Spring Boot đảm bảo tất cả các thư viện trong "starter" này đều tương thích phiên bản với nhau. Bạn không bao giờ phải lo lắng về "địa ngục dependency" (dependency hell).

#### 2\. 🪄 Spring Boot Autoconfiguration (Tự động cấu hình)

* **Là gì:** Đây là phần "phép thuật" mạnh mẽ nhất. Spring Boot sẽ **tự động cấu hình** ứng dụng của bạn dựa trên các file JAR mà nó tìm thấy trong classpath.
* **Cách hoạt động (Opinionated):**
    * *"Ồ, tôi thấy bạn đã thêm `spring-boot-starter-web` (có chứa Tomcat)?"* ➡️ "Vậy chắc là bạn muốn chạy một ứng dụng web. Tôi sẽ tự động cấu hình `DispatcherServlet` và một `Tomcat server` nhúng cho bạn."
    * *"Ồ, tôi thấy bạn thêm `spring-boot-starter-data-jpa` VÀ một file JAR `h2database`?"* ➡️ "Vậy chắc là bạn muốn kết nối đến CSDL H2. Tôi sẽ tự động cấu hình một `DataSource` và `EntityManagerFactory` trỏ đến CSDL H2 đó cho bạn."
* **Lợi ích:** Bạn không cần phải viết bất kỳ file cấu hình `Bean` (XML hay `@Configuration`) nào cho các thiết lập phổ biến này.

#### 3\. 🖥️ Embedded Server & `java -jar` (Server nhúng)

* **Là gì:** Như đã đề cập ở trên, `spring-boot-starter-web` đã bao gồm một server (mặc định là **Tomcat**) *bên trong* nó.
* **Cách hoạt động:** Spring Boot đóng gói ứng dụng của bạn thành một file **`.jar`** (thay vì `.war` truyền thống). File `.jar` này chứa *tất cả mọi thứ*: code của bạn, các thư viện của Spring, và cả server Tomcat.
* **Lợi ích (Stand-alone):** Bạn không cần phải cài đặt một server Tomcat riêng biệt. Bạn chỉ cần chạy ứng dụng của mình bằng một lệnh duy nhất, ở bất cứ đâu có Java:
  ```bash
  java -jar my-application.jar
  ```
  Điều này hoàn hảo cho Microservices và các container (như Docker).

#### 4\. 🩺 Spring Boot Actuator (Giám sát ứng dụng)

* **Là gì:** Đây là tính năng "production-grade". Chỉ cần thêm `spring-boot-starter-actuator`, ứng dụng của bạn ngay lập tức có thêm các endpoint (đường dẫn) để **giám sát và quản lý** khi nó đang chạy.
* **Ví dụ:**
    * `/actuator/health`: Kiểm tra xem ứng dụng có "khỏe" không (ví dụ: CSDL còn kết nối không?).
    * `/actuator/metrics`: Xem các chỉ số (ví dụ: bộ nhớ đang dùng, số request đã xử lý).
    * `/actuator/env`: Xem các biến môi trường và cấu hình đang được nạp.

-----

## ❓ Câu hỏi: What things affect what Spring Boot sets up?

(Những điều gì ảnh hưởng đến những gì Spring Boot thiết lập?)

Một cách dễ hiểu, Spring Boot giống như một **"người trợ lý thông minh"**. Nó cố gắng tự động cấu hình (Autoconfiguration) ứng dụng Spring cho bạn. Những gì nó thiết lập (những `Bean` nó tạo ra) bị ảnh hưởng chủ yếu bởi **ba điều**:

1.  **"Bạn có gì trong túi?"** (Các thư viện trên Classpath, thường qua **Starters**)
2.  **"Bạn đã tự làm gì?"** (Các **`@Bean`** mà bạn tự định nghĩa)
3.  **"Bạn muốn tinh chỉnh gì?"** (Các thuộc tính trong **`application.properties`**)

-----

### 1\. 🪄 Cơ chế hoạt động: `@Conditional`

"Sự thông minh" của Spring Boot đến từ một loạt các annotation (chú thích) gọi là **`@Conditional...`**.

Mọi lớp Autoconfiguration của Spring Boot đều chứa các "công tắc" này. Bạn có thể tưởng tượng mỗi cấu hình Bean của Spring Boot đều có một câu lệnh "IF... THEN...":

> **IF** (NẾU) một điều kiện nào đó được thỏa mãn...
> **THEN** (THÌ) tôi (Spring Boot) sẽ tạo `Bean` này cho bạn.

Ba "điều" ở trên chính là thứ để kiểm tra các "điều kiện" (conditions) này.

-----

### 2\. Phân tích các yếu tố ảnh hưởng

#### 📦 1. Ảnh hưởng bởi Classpath (Bạn có gì trong túi?)

Đây là yếu tố ảnh hưởng mạnh mẽ nhất. Spring Boot dùng **`@ConditionalOnClass`** để kiểm tra.

* **Cơ chế:** Spring Boot quét classpath của bạn.

* **Ví dụ (Tự động cấu hình Web Server):**

    * *Spring Boot nói:* "**IF** (NẾU) tôi tìm thấy lớp `javax.servlet.Servlet` và `org.apache.catalina.startup.Tomcat` trên classpath..." (Điều này xảy ra khi bạn thêm `spring-boot-starter-web`)
    * *Spring Boot làm:* "**THEN** tôi sẽ tự động cấu hình và khởi chạy một `Tomcat` server nhúng cho bạn."
    * **Kết quả:** Bạn không cần cấu hình server, chỉ cần thêm "starter" là đủ.

* **Ví dụ (Tự động cấu hình Database):**

    * *Spring Boot nói:* "**IF** (NẾU) tôi tìm thấy lớp `org.h2.Driver` trên classpath..." (Điều này xảy ra khi bạn thêm `h2database`)
    * *Spring Boot làm:* "**THEN** tôi sẽ tự động cấu hình một `DataSource` `Bean` trỏ đến một CSDL H2 trong bộ nhớ cho bạn."

#### 🧢 2. Ảnh hưởng bởi Bean của bạn (Bạn đã tự làm gì?)

Spring Boot rất "lịch sự". Nó sẽ **không** cấu hình một thứ gì đó nếu nó thấy bạn đã **tự làm rồi**. Nó dùng **`@ConditionalOnMissingBean`** (Điều kiện: Thiếu Bean).

* **Cơ chế:** Đây là annotation `@Conditional` quan trọng và phổ biến nhất.
* **Ví dụ (Ghi đè `DataSource`):**
    * *Spring Boot nói:* "Tôi sẽ tự động cấu hình một `DataSource`... **IF** (NẾU) trong Application Context **chưa hề tồn tại** một `Bean` nào tên là `dataSource`."
    * *BẠN LÀM:* Bạn muốn dùng CSDL Oracle ở production, nên bạn tự định nghĩa `Bean` của mình:
      ```java
      @Configuration
      public class DatabaseConfig {
          
          @Bean
          public DataSource dataSource() {
              // Cấu hình phức tạp của bạn cho Oracle
              return new OracleDataSource(...); 
          }
      }
      ```
    * **Kết quả:** Spring Boot thấy bạn đã có `dataSource` `Bean`. Điều kiện `ConditionalOnMissingBean` **thất bại** (fail). Cơ chế tự động cấu hình `DataSource` của Spring Boot sẽ **tắt đi (backs off)** và sử dụng `Bean` do chính bạn cung cấp.

#### ⚙️ 3. Ảnh hưởng bởi Properties (Bạn muốn tinh chỉnh gì?)

Đôi khi bạn muốn giữ cấu hình tự động, nhưng muốn *tinh chỉnh* nó, hoặc *tắt* một tính năng cụ thể. Bạn làm điều này qua file `application.properties` (hoặc `.yml`). Spring Boot dùng **`@ConditionalOnProperty`**.

* **Cơ chế:** Bean chỉ được tạo NẾU một thuộc tính (property) có giá trị nhất định (hoặc tồn tại/không tồn tại).
* **Ví dụ (Tắt Security):**
    * *Spring Boot nói:* "Tôi sẽ tự động cấu hình `SecurityAutoConfiguration` (toàn bộ Spring Security)... **IF** (NẾU) thuộc tính `spring.security.enabled` có giá trị là `true`, hoặc thuộc tính này **bị thiếu** (matchIfMissing = true)."
    * *BẠN LÀM:* Bạn đang làm một dự án nhỏ và chưa muốn bảo mật. Bạn thêm vào `application.properties`:
      ```properties
      # Tắt tính năng tự động cấu hình bảo mật
      spring.security.enabled=false
      ```
    * **Kết quả:** Điều kiện `ConditionalOnProperty` **thất bại**. Toàn bộ lớp `SecurityAutoConfiguration` bị bỏ qua và không được thiết lập.

-----

### 4\. Danh sách các "Công tắc" `@Conditional`

Câu trả lời của bạn đã liệt kê rất đầy đủ các loại "công tắc" mà Spring Boot sử dụng. Dưới đây là tóm tắt các loại quan trọng nhất:

| Annotation | Ý nghĩa (Bean sẽ được tạo NẾU...) |
| :--- | :--- |
| **`@ConditionalOnClass`** | ...Một **lớp** (class) cụ thể tồn tại trên classpath. |
| **`@ConditionalOnMissingClass`** | ...Một **lớp** (class) cụ thể bị thiếu trên classpath. |
| **`@ConditionalOnBean`** | ...Một **Bean** loại/tên cụ thể đã tồn tại trong context. |
| **`@ConditionalOnMissingBean`** | ...Một **Bean** loại/tên cụ thể bị thiếu trong context. |
| **`@ConditionalOnProperty`** | ...Một **thuộc tính** (property) trong `application.properties` có giá trị khớp. |
| **`@ConditionalOnResource`** | ...Một **file tài nguyên** (ví dụ: `logback.xml`) tồn tại. |
| **`@ConditionalOnWebApplication`** | ...Ứng dụng này là một ứng dụng web. |
| **`@ConditionalOnNotWebApplication`** | ...Ứng dụng này *không* phải là ứng dụng web (ví dụ: console app). |
| **`@ConditionalOnExpression`** | ...Một biểu thức SpEL (Spring Expression Language) trả về `true`. |

-----

## ❓ Câu hỏi: Can you control logging with Spring Boot? How?

(Bạn có thể kiểm soát logging với Spring Boot không? Bằng cách nào?)

**Câu trả lời trực tiếp:** Vâng, bạn **hoàn toàn có thể**. Spring Boot cung cấp khả năng kiểm soát logging cực kỳ mạnh mẽ và linh hoạt.

Mặc định, Spring Boot sẽ:

1.  Sử dụng **Logback** làm thư viện logging.
2.  Log ra **Console (màn hình)**.
3.  Có mức log (level) mặc định là **`INFO`**.

Bạn có thể kiểm soát và thay đổi tất cả các hành vi mặc định này bằng **hai cách chính**, tùy thuộc vào mức độ phức tạp bạn cần:

-----

### 1\. ⚙️ Cách 1: Cấu hình qua `application.properties` (Cách đơn giản)

Đây là cách làm được Spring Boot khuyến khích cho các tùy chỉnh phổ biến (chiếm 80% nhu cầu). Bạn không cần tạo bất kỳ file XML nào.

#### a. Kiểm soát Mức độ Log (Logging Levels)

Bạn có thể set level cho `root` (toàn bộ ứng dụng) hoặc cho từng package/class cụ thể.

```properties
# 1. Đặt level chung cho toàn bộ ứng dụng là WARN
logging.level.root=WARN

# 2. Đặt level cho package cụ thể (ví dụ: code của bạn)
logging.level.com.mycompany.service=DEBUG

# 3. Đặt level cho code của Spring Framework
logging.level.org.springframework.web=INFO
```

#### b. Kiểm soát Nơi Log (File Output)

Mặc định, Spring Boot chỉ log ra console. Để log ra file, bạn chỉ cần thêm một trong hai thuộc tính sau (không cần cả hai):

* **`logging.file.name`**: (Cách ưu tiên) Ghi log ra một file cụ thể (ví dụ: `app.log`).
  ```properties
  logging.file.name=my-app.log
  ```
* **`logging.path`**: Ghi log vào một thư mục cụ thể (tên file mặc định sẽ là `spring.log`).
  ```properties
  logging.path=/var/logs/my-app/
  ```

#### c. Kiểm soát Xoay vòng Log (Log Rotation)

Khi bạn sử dụng `logging.file.name` hoặc `logging.path`, Spring Boot sẽ **tự động kích hoạt tính năng xoay vòng log (rotation)**. Bạn có thể tinh chỉnh nó:

```properties
# 1. Kích thước tối đa của một file log trước khi xoay vòng (ví dụ: 10MB)
logging.file.max-size=10MB

# 2. Số lượng file log lưu trữ tối đa (ví dụ: 7 file)
logging.file.max-history=7
```

#### d. Kiểm soát Mẫu Log (Logging Pattern) & Màu sắc (Color)

Bạn có thể thay đổi định dạng của dòng log:

```properties
# 1. Định dạng cho Console (với %clr để bật màu)
logging.pattern.console=%clr(%d{HH:mm:ss.SSS}){cyan} %-5p [%t] %c : %m%n

# 2. Định dạng cho File (không nên dùng màu)
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p [%t] %c : %m%n
```

#### e. Nhóm Logger (Logging Groups)

Đây là một tính năng rất hay của Spring Boot. Bạn có thể nhóm nhiều package lại và set level cho cả nhóm.

```properties
# 1. Định nghĩa một nhóm tên là "my-services"
logging.group.my-services=com.mycompany.service.a, com.mycompany.service.b

# 2. Set level cho cả nhóm đó
logging.level.my-services=DEBUG
```

-----

### 2\. 🗃️ Cách 2: Cấu hình qua File riêng (Cách kiểm soát toàn diện)

Khi `application.properties` không đủ đáp ứng (ví dụ: bạn muốn log ra CSDL, log ra Kafka, hoặc có chính sách xoay vòng log phức tạp), bạn cần dùng file cấu hình riêng của hệ thống log.

Spring Boot sẽ **tự động phát hiện** các file này nếu bạn đặt chúng vào `src/main/resources`:

* **Logback (Mặc định):** `logback-spring.xml`
* **Log4j2:** `log4j2-spring.xml`
* **Java Util Logging:** `logging.properties`

> **QUAN TRỌNG:** Ngay khi Spring Boot tìm thấy một trong các file này, nó sẽ **TẮT** toàn bộ cơ chế tự động cấu hình logging (trong `application.properties`) và **trao toàn quyền** cho file cấu hình đó.
>
> (Lưu ý: Tên file nên có `-spring` như `logback-spring.xml` để tận dụng các tính năng của Spring Boot như `profiles`).

Ví dụ của bạn về `logback-spring.xml` cho thấy rõ sức mạnh này. Bạn có thể định nghĩa các `Appender` (File, Console) và các `rollingPolicy` phức tạp (ví dụ: xoay vòng theo cả Kích thước và Thời gian).

```xml
<rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
  <fileNamePattern>${LOG_PATH}/archived/log_%d{dd-MM-yyyy}_%i.log</fileNamePattern>
  <maxFileSize>10MB</maxFileSize>
  <maxHistory>10</maxHistory>
  <totalSizeCap>100MB</totalSizeCap>
</rollingPolicy>
```

-----

### 3\. 🔄 Cách 3: Thay đổi Hệ thống Logging (Switching)

Spring Boot linh hoạt đến mức cho phép bạn đổi cả "engine" log. Mặc định là **Logback**. Nếu bạn muốn dùng **Log4j2**:

1.  Trong `pom.xml`, **loại trừ (exclude)** `spring-boot-starter-logging` (cái này chứa Logback).
2.  **Thêm vào (add)** `spring-boot-starter-log4j2`.

Ví dụ (trong `pom.xml`):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
```

-----

### 4\. 💡 Mẹo bổ sung (Debug / Trace nhanh)

Spring Boot cung cấp các "công tắc" nhanh để bật log gỡ lỗi (debug) mà không cần vào `application.properties`:

1.  **Qua dòng lệnh (command line):**
    ```bash
    java -jar myapp.jar --debug 
    # (Tương đương logging.level.root=DEBUG)

    java -jar myapp.jar --trace
    # (Tương đương logging.level.root=TRACE)
    ```
2.  **Qua `application.properties`:**
    ```properties
    debug=true
    # hoặc
    trace=true
    ```

-----

## ❓ Câu hỏi: Where does Spring Boot look for property file by default?

(Spring Boot tìm kiếm file property ở đâu theo mặc định?)

### 1\. Khái niệm cốt lõi: Thứ tự ưu tiên (Precedence)

Spring Boot tự động tải (load) các file `application.properties` (hoặc `application.yml`) từ nhiều vị trí. Điều này cho phép bạn đóng gói (package) một file cấu hình mặc định *bên trong* file `.jar` của mình và sau đó *ghi đè (override)* các cấu hình đó bằng một file *bên ngoài* file `.jar` khi chạy ở môi trường production, mà không cần build lại code.

**Quy tắc ghi đè** quan trọng nhất là:

1.  **Bên ngoài (External) sẽ ghi đè Bên trong (Internal).**
2.  **Profile-specific (ví dụ: `application-prod.properties`) sẽ ghi đè file không-profile (`application.properties`).**
3.  **Thư mục `/config` sẽ ghi đè thư mục gốc (root).**

-----

### 2\. Bốn (4) Vị trí Mặc định

Spring Boot sẽ tìm kiếm file `application.properties` (và `.yml`) ở 4 vị trí sau đây, theo thứ tự **ưu tiên từ cao nhất đến thấp nhất** (vị trí 1 sẽ ghi đè vị trí 2, 2 sẽ ghi đè 3, v.v.):

| Thứ tự Ưu tiên | Vị trí | Mô tả (Ví dụ) |
| :--- | :--- | :--- |
| **1 (Cao nhất)** | **Bên ngoài JAR**: `/config` | `config/application.properties` (nằm chung thư mục với file `.jar`) |
| **2** | **Bên ngoài JAR**: Gốc | `application.properties` (nằm chung thư mục với file `.jar`) |
| **3** | **Bên trong JAR**: `/config` | `classpath:/config/application.properties` (trong `src/main/resources/config`) |
| **4 (Thấp nhất)** | **Bên trong JAR**: Gốc | `classpath:/application.properties` (trong `src/main/resources`) |

#### 💡 Kịch bản thực tế

* **Khi phát triển (Development):** Bạn đặt file `application.properties` vào `src/main/resources` (Vị trí 4). Ứng dụng sẽ dùng file này.
* **Khi triển khai (Production):** Bạn build ra file `my-app.jar`. Người quản trị (Ops/DevOps) tạo một thư mục, đặt file `my-app.jar` và một thư mục con `config/` vào đó.
  ```
  /opt/my-app/
  ├── my-app.jar
  └── config/
      └── application.properties  <-- (Vị trí 1: Chứa cấu hình Production)
  ```
* **Kết quả:** Khi bạn chạy `java -jar my-app.jar`, Spring Boot sẽ tải file ở Vị trí 4 trước, sau đó nó tìm thấy file ở Vị trí 1 và **dùng các giá trị trong file này để ghi đè** lên các giá trị của Vị trí 4.

-----

### 3\. Ảnh hưởng của Profile

Như bạn đã nêu, Spring Boot cũng tìm các file `application-{profile}.properties`. Các file này tuân theo **quy tắc ưu tiên y hệt** như trên, nhưng chúng luôn có độ ưu tiên **cao hơn** file `application.properties` *tại cùng một vị trí*.

**Ví dụ (tại cùng Vị trí 4 - `classpath:/`):**

1.  Bạn có file `application.properties` (mặc định):
    ```properties
    server.port=8080
    spring.datasource.url=jdbc:h2:mem:db
    ```
2.  Bạn có file `application-prod.properties` (cho production):
    ```properties
    # Chỉ cần ghi đè những gì cần thay đổi
    server.port=80
    spring.datasource.url=jdbc:oracle:thin:@prod-db...
    ```
3.  **Kết quả:**
    * Nếu bạn chạy `java -jar my-app.jar`, port sẽ là `8080`.
    * Nếu bạn chạy `java -jar my-app.jar --spring.profiles.active=prod`, port sẽ là `80` và URL CSDL sẽ là của Oracle.

-----

### 4\. Cách Ghi đè Hành vi Mặc định

Bạn có thể thay đổi hoàn toàn hành vi mặc định này bằng cách sử dụng các thuộc tính khi khởi chạy:

#### a. Thay đổi Tên file (Sử dụng `spring.config.name`)

Thuộc tính này thay đổi tên "application" thành một tên khác. Spring Boot vẫn sẽ tìm ở 4 vị trí mặc định, nhưng với tên file mới.

```bash
# Sẽ tìm file "myproject.properties", "myproject-prod.properties", v.v.
java -jar myproject.jar --spring.config.name=myproject
```

#### b. Chỉ định Vị trí Cụ thể (Sử dụng `spring.config.location`)

Đây là thuộc tính "mạnh" nhất. Nó bảo Spring Boot: "**Hãy quên 4 vị trí mặc định đi** và chỉ tải cấu hình từ các vị trí mà tôi liệt kê ở đây."

* Các vị trí mặc định sẽ *không* được tải nữa.
* Bạn có thể chỉ định nhiều vị trí, phân cách bằng dấu phẩy (`,`).

<!-- end list -->

```bash
# Chỉ tải file này, không tìm ở đâu khác
java -jar myproject.jar --spring.config.location=classpath:/default.properties

# Tải cấu hình từ 2 vị trí này (file bên ngoài sẽ ghi đè file bên trong)
java -jar myproject.jar --spring.config.location=classpath:/override.properties,file:./my-config.properties
```

-----

## ❓ Câu hỏi: What properties do you have to define in order to configure external MySQl?

(Bạn phải định nghĩa những thuộc tính nào để cấu hình một CSDL MySQL bên ngoài?)

### 1\. ⚙️ Ba (3) Thuộc tính Cốt lõi

Để kết nối Spring Boot với một CSDL MySQL (hoặc bất kỳ CSDL quan hệ nào), bạn **chỉ cần cung cấp 3 thuộc tính (properties) cốt lõi** trong file `application.properties` (hoặc `application.yml`):

```properties
# 1. URL kết nối (Connection String)
#    (Bao gồm host, port, và tên database)
spring.datasource.url=jdbc:mysql://localhost:3306/spring-tutorial

# 2. Tên người dùng (Username)
spring.datasource.username=spring-tutorial

# 3. Mật khẩu (Password)
spring.datasource.password=spring-tutorial
```

-----

### 2\. 🪄 Tại sao chỉ cần 3 thuộc tính này là đủ?

Đây là một ví dụ kinh điển về **Spring Boot Autoconfiguration (Tự động cấu hình)**:

1.  **Trigger (Kích hoạt):** Khi bạn thêm `spring-boot-starter-data-jpa` hoặc `spring-boot-starter-data-jdbc` vào `pom.xml`, Spring Boot "thấy" rằng bạn muốn làm việc với CSDL.
2.  **Driver Check (Kiểm tra Driver):** Spring Boot tiếp tục quét classpath. Khi nó "thấy" file JAR `mysql-connector-java`...
3.  **Action (Hành động):** Nó tự động nói: "À, bạn muốn kết nối đến CSDL MySQL. Tôi sẽ **tự động tạo một `DataSource` `Bean`** (bộ quản lý kết nối) cho bạn."
4.  **Configuration (Cấu hình):** "Để tạo `DataSource` này, tôi chỉ cần 3 thông tin: `url`, `username`, và `password`. Hãy cung cấp chúng trong `application.properties`."

-----

### 3\. 📦 Các Dependency Bắt buộc

Như bạn đã lưu ý, để cơ chế tự động cấu hình này hoạt động, bạn **bắt buộc** phải có 2 nhóm dependency sau trong `pom.xml`:

**1. "Trigger" (Bộ khởi động JPA hoặc JDBC):**
Báo cho Spring Boot biết bạn muốn tương tác với CSDL.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId> 
    </dependency>
```

**2. "Driver" (Trình kết nối CSDL):**
Báo cho Spring Boot biết *cách* nói chuyện với CSDL cụ thể (ở đây là MySQL).

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope> </dependency>
```

-----

### 4\. 💡 Các Thuộc tính Tùy chọn (Optional)

Các thuộc tính bổ sung mà bạn đề cập cũng rất hữu ích:

#### a. `spring.datasource.driver-class-name`

```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

* **Thường là không cần thiết.** Spring Boot đủ thông minh để tự suy ra (deduce) class driver này (`com.mysql.cj.jdbc.Driver`) từ chuỗi `url` (bắt đầu bằng `jdbc:mysql://`).
* Bạn chỉ cần chỉ định nó một cách tường minh nếu bạn đang dùng một driver không chuẩn hoặc một phiên bản rất cũ.

#### b. `spring.datasource.initialization-mode`

```properties
spring.datasource.initialization-mode=always
```

* Thuộc tính này **không dùng để kết nối**, mà dùng để **khởi tạo CSDL** (tạo bảng, chèn dữ liệu) khi ứng dụng khởi động.
* Nó bảo Spring Boot tìm và chạy các file `schema.sql` (để tạo bảng) và `data.sql` (để chèn dữ liệu) trong thư mục `src/main/resources`.
* **`always`**: Luôn luôn chạy các file này. Rất hữu ích khi phát triển (development) hoặc kiểm thử (testing).
* **`embedded` (Mặc định):** Chỉ chạy nếu bạn dùng CSDL nhúng (như H2, HSQLDB).
* **`never`**: Không bao giờ chạy. Đây là cài đặt **an toàn cho production**.

-----

## ❓ Câu hỏi: What is a fat jar? How is it different from original jar?

(Fat jar là gì? Nó khác gì so với jar gốc?)

### 1\. "Fat Jar" là gì? (Một cách dễ hiểu)

Hãy tưởng tượng bạn đang đóng gói một món đồ nội thất (như một cái ghế) để vận chuyển.

* **Original JAR (Jar truyền thống):** Giống như bạn chỉ đóng gói **cái ghế** (code của bạn) và một tờ **hướng dẫn lắp ráp**. Khi cái ghế đến tay người nhận (môi trường runtime), người đó phải tự đi tìm mua ốc vít, tua vít, búa... (chính là các **dependencies** - thư viện phụ thuộc) mới có thể lắp ráp và sử dụng được.
* **Fat JAR (Jar "béo"):** Giống như bạn đóng gói **cái ghế**, **tờ hướng dẫn**, và **tất cả ốc vít, tua vít, búa...** (tất cả dependencies) vào chung một cái **thùng hàng (container)** duy nhất. Người nhận chỉ cần một cái thùng này là có thể sử dụng ngay lập tức.

Trong Spring Boot, **Fat Jar** (hay còn gọi là **Executable Jar** - Jar có thể thi hành) là một file `.jar` duy nhất, chứa đựng:

1.  Mã code đã biên dịch (`.class` files) của ứng dụng bạn.
2.  **Tất cả** các thư viện (dependencies) mà ứng dụng của bạn cần để chạy (ví dụ: `spring-web.jar`, `tomcat-embed.jar`, `jackson.jar`...).

Chính vì nó chứa "tất cả mọi thứ" nên nó "béo" (fat).

### 2\. Tại sao nó "Executable" (Có thể thi hành)?

Điều khiến "Fat Jar" của Spring Boot trở nên đặc biệt là nó có thể được chạy trực tiếp bằng một lệnh đơn giản:

```bash
java -jar my-application.jar
```

Điều này có thể thực hiện được là nhờ 2 thứ mà `spring-boot-maven-plugin` (hoặc Gradle plugin) tạo ra:

1.  **Một file `MANIFEST.MF` đặc biệt:** Đây là "tờ hướng dẫn" bên trong file `.jar`. Nó chỉ định một `Main-Class` không phải là lớp `Main` của bạn, mà là một "bộ mồi" (Launcher) của Spring Boot (ví dụ: `org.springframework.boot.loader.JarLauncher`).
2.  **Một "Bộ mồi" (Launcher):** Đoạn code `JarLauncher` này biết chính xác cách tìm và nạp (load) các thư viện (dependencies) đang bị "lồng" (nested) bên trong file `.jar` (thường nằm ở `BOOT-INF/lib/`).
3.  **Một `Start-Class`:** Sau khi `JarLauncher` nạp xong mọi thứ, nó sẽ tìm đến lớp `Main` *thực sự* của bạn (lớp có `@SpringBootApplication`)—được chỉ định là `Start-Class` trong manifest—và khởi chạy nó.

### 3\. Sự khác biệt: Fat Jar (Nested) vs. Uber Jar (Shaded)

Đây là một điểm kỹ thuật quan trọng mà câu trả lời của bạn đã nêu đúng:

* **Fat Jar (Nested - Lồng nhau) của Spring Boot:** Các file `.jar` của dependency được giữ **nguyên vẹn** và xếp lồng vào bên trong file jar chính (trong `BOOT-INF/lib/`).

    * *Ưu điểm:* Rất sạch sẽ. Bạn có thể dễ dàng mở file jar ra và xem chính xác bạn đang phụ thuộc vào thư viện nào, phiên bản bao nhiêu. Không bao giờ có xung đột tên file.

* **Uber Jar (Shaded - Giải nén/Trộn lẫn):** Đây là một cách làm cũ. Nó *giải nén tất cả* các file `.class` từ *mọi* thư viện dependency và "đổ" tất cả chúng vào chung một thư mục gốc bên trong file jar.

    * *Nhược điểm:* Một mớ hỗn độn. Nếu 2 thư viện dependency cùng có một file tên `config.properties`, một file sẽ bị file kia ghi đè, gây ra lỗi rất khó tìm.

Spring Boot đã giải quyết vấn đề này một cách thông minh bằng cách sử dụng phương pháp "nested jar".

### 4\. Bảng so sánh: Original JAR vs. Fat JAR

Đây là tóm tắt các khác biệt chính:

| Đặc điểm | Original JAR (Jar truyền thống) | Fat JAR (Spring Boot) |
| :--- | :--- | :--- |
| **Nội dung** | Chỉ chứa code của bạn. | Code của bạn + **Tất cả** dependencies + Server nhúng (Tomcat). |
| **Dependencies** | Nằm bên ngoài. Phải được cung cấp khi chạy (ví dụ: qua `-cp`). | Nằm **bên trong** (nested) tại `BOOT-INF/lib/`. |
| **Khả năng thực thi** | **Không** (theo mặc định). | **Có**. Được thiết kế để chạy với `java -jar`. |
| **Cách chạy (Ví dụ)** | `java -cp "lib/*:my-app.jar" com.mycompany.Main` | `java -jar my-app.jar` |
| **Tạo bởi (Maven)** | `maven-jar-plugin` | `spring-boot-maven-plugin` (mục tiêu `repackage`) |

-----

## ❓ Câu hỏi: What is the difference between an embedded container and a WAR?

(Sự khác biệt giữa một container nhúng và một file WAR là gì?)

### 1\. Phép so sánh: "Food Court" vs. "Food Truck"

Hãy tưởng tượng bạn muốn mở một nhà hàng (ứng dụng của bạn):

* **1. `WAR` (Cách truyền thống - "Food Court"):**

    * `WAR` (Web Application Archive) giống như bạn đã chuẩn bị sẵn tất cả **nguyên liệu và công thức** (`.class` files, `lib/`).
    * Nhưng bạn **không thể tự bán hàng**. Bạn phải mang "bó" nguyên liệu này đến một **khu ẩm thực (Food Court)** đã được xây dựng sẵn (chính là một **Application Server** như Tomcat, WildFly, Jetty).
    * Bạn thuê một gian hàng, và chính "Food Court" đó sẽ cung cấp điện, nước, bàn ghế (chính là **Servlet Container**) để bạn hoạt động.
    * Một "Food Court" (Tomcat Server) có thể chứa **nhiều** gian hàng (nhiều file `WAR`) chạy cùng lúc.

* **2. `Embedded Container` (Cách của Spring Boot - "Food Truck"):**

    * Đây là một file **Fat Jar** (hoặc Executable Jar).
    * Nó giống như một chiếc **xe bán hàng di động (Food Truck)**.
    * Bạn không chỉ đóng gói nguyên liệu (code của bạn) mà còn đóng gói **cả cái bếp, bình gas, và quầy tính tiền** (chính là **Embedded Tomcat/Jetty**) vào chung một chiếc xe.
    * Bạn không cần ai cung cấp gì cả. Bạn chỉ cần một chỗ đỗ (một JRE - Môi trường Java) là có thể chạy `java -jar my-app.jar` và bắt đầu bán hàng ngay lập tức.
    * Mỗi "Food Truck" (Fat Jar) là **một** ứng dụng độc lập.

-----

### 2\. So sánh chi tiết

Câu trả lời của bạn đã phân tích rất đúng về mặt kỹ thuật, đây là bảng tóm tắt:

| Đặc điểm | `WAR` (Traditional) | `Fat Jar` (Embedded Container) |
| :--- | :--- | :--- |
| **Server** | **Bên ngoài (External):** Cần một server (Tomcat, Jetty...) được cài đặt riêng biệt. | **Bên trong (Embedded):** Server (Tomcat...) là một thư viện (`.jar`) được đóng gói *bên trong* ứng dụng. |
| **File đóng gói** | `.war` | `.jar` |
| **Cách chạy** | **Phải "deploy"** (triển khai) file `.war` lên server. | **Chạy trực tiếp** bằng lệnh `java -jar my-app.jar`. |
| **Mô hình** | **Một-Server, Nhiều-Ứng-dụng:** Một Tomcat có thể chạy nhiều file `.war`. | **Một-Server, Một-Ứng-dụng:** File `.jar` tự nó là một server chỉ chạy chính nó. |
| **Phù hợp cho** | Ứng dụng truyền thống (Monolith). | **Microservices**, Cloud, **Docker** (vì nó độc lập). |
| **Cấu trúc (Như bạn đã nêu)** | Chứa thư mục `WEB-INF/` (chứa `classes/` và `lib/`). | Chứa thư mục `BOOT-INF/` (chứa `classes/` và `lib/`). |

-----

### 3\. Cấu hình Maven (Rất quan trọng cho kỳ thi)

Làm thế nào để build ra 1 trong 2 loại này?

#### a. Để tạo "Fat Jar" (Cách mặc định của Spring Boot)

Đây là cách làm mặc định khi bạn dùng `spring-boot-starter-web`.

1.  **Dependency:** Chỉ cần `spring-boot-starter-web` (nó đã bao gồm `spring-boot-starter-tomcat` bên trong).
2.  **Plugin:** **Bắt buộc** phải có `spring-boot-maven-plugin` với mục tiêu (goal) `repackage`. Chính plugin này sẽ "đóng thùng" mọi thứ thành "Food Truck".

<!-- end list -->

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <executions>...</executions>
        </plugin>
    </plugins>
</build>
```

#### b. Để tạo "WAR" (Cách truyền thống)

Bạn cần làm 3 điều sau:

1.  **Đổi packaging:** Phải nói cho Maven biết bạn muốn đóng gói thành `.war`.

    ```xml
    <packaging>war</packaging>
    ```

2.  **Đánh dấu Tomcat là "provided":**

    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
        <scope>provided</scope> </dependency>
    ```

    * **`<scope>provided</scope>`** có nghĩa là: "Này Maven, tôi cần Tomcat để code (dev) và chạy thử, nhưng **đừng đóng gói** nó vào file `.war` cuối cùng. Tôi (người deploy) hứa rằng cái "Food Court" (Server Tomcat bên ngoài) sẽ *cung cấp (provide)* nó."

3.  **(Optional but Recommended) Kế thừa `SpringBootServletInitializer`:**

    * Bạn cần phải "mồi" cho server Tomcat bên ngoài biết cách khởi chạy ứng dụng Spring Boot của bạn.

    <!-- end list -->

    ```java
    @SpringBootApplication
    public class MyWarApplication extends SpringBootServletInitializer {

        @Override
        protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
            return application.sources(MyWarApplication.class);
        }

        // ... main method
    }
    ```

-----

## ❓ Câu hỏi: What embedded containers does Spring Boot support?

(Spring Boot hỗ trợ những container nhúng nào?)

Spring Boot hỗ trợ ba (3) container nhúng (embedded servlet containers) chính:

* **Tomcat** (Mặc định)
* **Jetty**
* **Undertow**

-----

### 1\. 🍅 Tomcat (Mặc định)

Như bạn đã nói, **Tomcat** là lựa chọn mặc định. Bất cứ khi nào bạn thêm `spring-boot-starter-web` vào dự án, nó sẽ tự động kéo theo `spring-boot-starter-tomcat`.

Bạn không cần làm gì thêm. Chỉ cần một dependency này là đủ để chạy ứng dụng web:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

-----

### 2\. 🪄 "Phép thuật" đằng sau: Autoconfiguration

Cơ chế này hoạt động dựa trên **Spring Boot Autoconfiguration**. Spring Boot được cấu hình để:

1.  "Tôi thấy `spring-boot-starter-web`... vậy đây là một ứng dụng web."
2.  "Tôi cần một Servlet Container. Hãy để tôi quét `classpath`."
3.  "À, tôi thấy `spring-boot-starter-tomcat`\! Tôi sẽ tự động cấu hình và khởi chạy một **Tomcat** server nhúng."

Nếu Spring Boot *không* tìm thấy Tomcat, nó sẽ tìm Jetty. Nếu không thấy Jetty, nó sẽ tìm Undertow.

-----

### 3\. 🔄 Cách thay đổi (Switching) Container

Để "tráo đổi" (swap) container, bạn chỉ cần làm 2 bước đơn giản trong file `pom.xml`:

1.  **Loại trừ (Exclude)** `spring-boot-starter-tomcat` ra khỏi `spring-boot-starter-web`.
2.  **Thêm vào (Include)** "starter" cho container mà bạn muốn.

#### Chuyển sang 🚢 Jetty

Giống hệt như ví dụ của bạn, bạn loại bỏ Tomcat và thêm Jetty:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-tomcat</artifactId>
            </exclusion>
        </exclusions>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jetty</artifactId>
    </dependency>
</dependencies>
```

Bây giờ, khi ứng dụng khởi động, cơ chế Autoconfiguration sẽ nói: "Ồ, không thấy Tomcat, nhưng tôi thấy Jetty\! Tôi sẽ khởi chạy **Jetty**."

#### Chuyển sang 🚀 Undertow

Tương tự, để dùng Undertow (một server hiệu năng cao, nhẹ của JBoss):

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-tomcat</artifactId>
            </exclusion>
        </exclusions>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-undertow</artifactId>
    </dependency>
</dependencies>
```

Lần này, Spring Boot sẽ tìm và khởi chạy **Undertow**.

*(Lưu ý: Bạn không cần lặp lại phần `<build>` (plugin) trong các ví dụ, vì `spring-boot-maven-plugin` là cần thiết cho cả 3 trường hợp để đóng gói "fat jar", nó không liên quan đến việc *chọn* container nào.)*

-----

## ❓ Câu hỏi: How does Spring Boot know what to configure?

(Làm thế nào Spring Boot biết nó cần cấu hình cái gì?)

### 1. Phép so sánh: "Người trợ lý thông minh"

Hãy tưởng tượng **Spring Boot** là một **"người trợ lý thông minh"** (Smart Assistant) và bạn là sếp.

1.  **Bạn cung cấp "Starter" (Gói đồ nghề):**
    * Bạn nói: "Trợ lý, tôi muốn xây một ứng dụng web."
    * Bạn đưa cho nó một "gói đồ nghề" tên là `spring-boot-starter-web`.

2.  **Trợ lý kiểm tra "Đồ nghề" (Classpath Scanning):**
    * Trợ lý mở gói đồ nghề ra và thấy bên trong có: `spring-webmvc.jar`, `jackson.jar`, và quan trọng nhất là `tomcat-embed.jar`.

3.  **Trợ lý đọc "Sổ tay Hướng dẫn" (Autoconfiguration):**
    * Trợ lý có một cuốn sổ tay "công thức tự động" (được định nghĩa trong file `META-INF/spring.factories` của các `starter`).
    * Nó tìm đến công thức `TomcatServletWebServerFactoryAutoConfiguration`.

4.  **Trợ lý kiểm tra "Điều kiện" (Conditionals):**
    * Công thức này có một dòng điều kiện (chính là các annotation **`@Conditional...`**):
    * **`@ConditionalOnClass(Tomcat.class)`**: "Chỉ thực hiện công thức này **NẾU** (IF) tôi tìm thấy lớp `Tomcat.class` trong đống đồ nghề."
        * *Kết quả:* **Đúng!** (Vì `tomcat-embed.jar` có ở đó).
    * **`@ConditionalOnMissingBean(ServletWebServerFactory.class)`**: "Chỉ thực hiện công thức này **NẾU** (IF) sếp (là bạn) *chưa* tự mình tạo ra một `ServletWebServerFactory` `Bean` nào."
        * *Kết quả:* **Đúng!** (Bạn chưa tự tạo `Bean` nào cả).

5.  **Trợ lý thực hiện "Cấu hình" (Running `@Configuration`):**
    * Vì tất cả điều kiện đều **ĐÚNG**, người trợ lý sẽ thực thi công thức: Nó tạo ra một `@Bean` `TomcatServletWebServerFactory` cho bạn.
    * **Kết quả:** Ứng dụng của bạn bây giờ có một server Tomcat nhúng, sẵn sàng chạy, mà bạn không cần viết một dòng code cấu hình nào.

---

### 2. Cơ chế Kỹ thuật (Như bạn đã nêu)

Câu chuyện trên tương ứng chính xác với các bước kỹ thuật:

1.  **Tìm kiếm các ứng viên:** Spring Boot khởi động và tìm tất cả các file `META-INF/spring.factories` trong tất cả các file `.jar` trên classpath.
2.  **Tải danh sách:** Nó đọc tất cả các lớp được liệt kê dưới key `org.springframework.boot.autoconfigure.EnableAutoConfiguration`. Đây là danh sách *tất cả* các "công thức tự động" (Autoconfiguration Classes) tiềm năng.
3.  **Kiểm tra điều kiện:** Spring Boot duyệt qua danh sách này. Đối với mỗi "công thức" (vốn là một lớp `@Configuration`), nó kiểm tra các annotation `@Conditional...` gắn trên lớp đó.
4.  **Chấp nhận hoặc Từ chối:**
    * Nếu **tất cả** các điều kiện trên lớp đó đều `true` ➡️ Lớp `@Configuration` này được nạp, và tất cả các `@Bean` bên trong nó sẽ được tạo ra.
    * Nếu **chỉ một** điều kiện `false` ➡️ Toàn bộ lớp `@Configuration` này (và các `@Bean` bên trong nó) sẽ bị **bỏ qua (skipped)**.

---

### 3. Ví dụ kinh điển: Tự động cấu hình `DataSource`

Đây là một ví dụ khác:
Spring Boot có một lớp `DataSourceAutoConfiguration`.

* **Điều kiện 1 (`@ConditionalOnClass(DataSource.class)`):** Nó kiểm tra "Bạn có thêm `spring-boot-starter-data-jpa` không?" (Vì `starter` này chứa `DataSource.class`).
* **Điều kiện 2 (`@ConditionalOnMissingBean(DataSource.class)`):** Nó kiểm tra "Bạn đã tự mình tạo một `@Bean public DataSource myDataSource()` chưa?".
    * Nếu bạn **chưa tạo** ➡️ Spring Boot sẽ tự tạo một cái cho bạn (thường là một connection pool như HikariCP).
    * Nếu bạn **đã tạo** ➡️ Spring Boot sẽ "lịch sự rút lui" (backs off) và dùng `Bean` của bạn. Đây là cách bạn ghi đè (override) hành vi mặc định.
* **Điều kiện 3 (`@ConditionalOnProperty(...)`):** Nó cũng kiểm tra `application.properties` để xem bạn có cung cấp `spring.datasource.url` không.

---

### 4. Tóm tắt các "Điều kiện" (Conditional Annotations)

Danh sách của bạn rất đầy đủ. Đây là các "công tắc" quan trọng nhất mà Spring Boot dùng để kiểm tra:

| Annotation | "Công tắc" này kiểm tra NẾU... |
| :--- | :--- |
| **`@ConditionalOnClass`** | ...Một **lớp** (class) cụ thể tồn tại trên classpath. (Ví dụ: "Có `Tomcat.class` không?") |
| **`@ConditionalOnMissingBean`** | ...Một **Bean** loại/tên cụ thể *chưa* tồn tại trong context. (Ví dụ: "User đã tự tạo `DataSource` chưa?") |
| **`@ConditionalOnBean`** | ...Một **Bean** loại/tên cụ thể *đã* tồn tại. (Ngược lại với cái trên). |
| **`@ConditionalOnProperty`** | ...Một **thuộc tính** (property) trong `application.properties` có giá trị khớp. (Ví dụ: `spring.security.enabled=true`). |
| **`@ConditionalOnResource`** | ...Một **file tài nguyên** (ví dụ: `logback-spring.xml`) tồn tại. |
| **`@ConditionalOnWebApplication`** | ...Ứng dụng này là một ứng dụng web. |
| **`@ConditionalOnNotWebApplication`**| ...Ứng dụng này *không* phải là ứng dụng web. |

-----

## ❓ Câu hỏi: What does @EnableAutoConfiguration do?

(@EnableAutoConfiguration làm gì?)

### 1\. @EnableAutoConfiguration là gì?

Một cách dễ hiểu nhất, `@EnableAutoConfiguration` chính là **công tắc tổng (master switch)**.

Khi bạn bật "công tắc" này, bạn đang ra lệnh cho Spring Boot:

> "Hãy bắt đầu quá trình **Tự động cấu hình (Autoconfiguration)**. Hãy 'đoán' xem tôi cần những `Bean` nào dựa trên các thư viện (dependencies) tôi đã thêm, và tự động tạo chúng giúp tôi."

-----

### 2\. Cơ chế hoạt động (The "Magic")

Như bạn đã mô tả chính xác, quá trình này diễn ra theo 3 bước:

1.  **Kích hoạt (Trigger):** Spring Boot nhìn thấy annotation `@EnableAutoConfiguration`.
2.  **Quét (Scan):** Nó ngay lập tức quét tất cả các file `.jar` trong classpath (bao gồm tất cả các `starter` bạn đã thêm) để tìm file `META-INF/spring.factories`.
3.  **Tải "Công thức" (Load Candidates):** Bên trong các file `spring.factories` này, nó tìm đến key (khóa) `org.springframework.boot.autoconfigure.EnableAutoConfiguration` và đọc ra một danh sách *rất dài* các "ứng cử viên" cấu hình (ví dụ: `DataSourceAutoConfiguration`, `TomcatServletWebServerFactoryAutoConfiguration`, `JacksonAutoConfiguration`...).
4.  **Kiểm tra Điều kiện (Check Conditionals):** Đây là phần "thông minh". Spring Boot **không** tạo tất cả các `Bean` này. Thay vào đó, nó "phỏng vấn" từng "ứng cử viên" trong danh sách đó bằng cách kiểm tra các annotation `@ConditionalOn...`:
    * Nó hỏi `TomcatServletWebServerFactoryAutoConfiguration`: "Điều kiện của anh là gì?"
    * *Class trả lời:* "Tôi cần `@ConditionalOnClass(Tomcat.class)` (có `Tomcat.class` trên classpath) VÀ `@ConditionalOnMissingBean(ServletWebServerFactory.class)` (người dùng chưa tự tạo `Bean` server)."
    * *Spring Boot kiểm tra:* "Bạn đã thêm `starter-web` (có `Tomcat.class`) và bạn chưa tự tạo `Bean` server. OK, điều kiện **khớp\!**".
    * *Kết quả:* Spring Boot **thực thi** lớp cấu hình này và tạo ra một `Bean` `TomcatServletWebServerFactory` (server Tomcat nhúng).

-----

### 3\. 💡 Mối liên hệ quan trọng: `@SpringBootApplication`

Đây là một điểm **rất quan trọng** cho kỳ thi:

Trong 99% các dự án Spring Boot hiện đại, bạn sẽ **không bao giờ** tự tay gõ `@EnableAutoConfiguration`.

*Tại sao?*

Bởi vì annotation **`@SpringBootApplication`** mà bạn đặt ở lớp `main` đã là một "meta-annotation" (annotation gộp) bao gồm nó:

`@SpringBootApplication` thực chất là sự kết hợp của 3 annotation:

1.  `@SpringBootConfiguration` (Đánh dấu đây là một lớp `@Configuration` đặc biệt).
2.  `@ComponentScan` (Bật tính năng quét component, tìm các `@Controller`, `@Service`...).
3.  **`@EnableAutoConfiguration`** (Bật "công tắc" tự động cấu hình mà chúng ta vừa thảo luận).

**Do đó:** Khi bạn dùng `@SpringBootApplication`, bạn đã **ngầm (implicitly)** bật `@EnableAutoConfiguration`.

```java
// Thay vì phải gõ 3 dòng này:
// @SpringBootConfiguration
// @EnableAutoConfiguration
// @ComponentScan
// public class MyApplication { ... }

// Bạn chỉ cần gõ 1 dòng:
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

-----

### 4\. Cách kiểm soát (Tắt một phần)

Nếu bạn *muốn* tự động cấu hình, nhưng muốn *loại trừ (exclude)* một phần cụ thể (ví dụ: bạn không muốn Spring Boot tự động cấu hình CSDL), bạn có thể làm như sau:

```java
// Tắt (exclude) một Autoconfiguration cụ thể
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MyApplication { ... }
```

Tóm lại, `@EnableAutoConfiguration` là "bộ não" khởi động quá trình tự động cấu hình, nhưng nó thường được "ẩn" bên trong `@SpringBootApplication` để cho tiện lợi.

-----

## ❓ Câu hỏi: Does Spring Boot do component scanning? Where does it look by default?

(Spring Boot có thực hiện component scanning không? Nó tìm kiếm ở đâu theo mặc định?)

### 1\. 🎯 Câu trả lời: CÓ

Vâng, Spring Boot **thực hiện component scanning (quét thành phần) một cách tự động**.

Lý do là vì annotation `@SpringBootApplication` (mà bạn dùng ở lớp `main`) đã bao gồm cả annotation `@ComponentScan`.

Như chúng ta đã thảo luận, `@SpringBootApplication` là một annotation "gộp" của 3 annotation khác:

1.  `@SpringBootConfiguration`
2.  `@EnableAutoConfiguration`
3.  **`@ComponentScan`** ⬅️ Đây chính là "công tắc" bật tính năng component scanning.

-----

### 2\. 🗺️ Nó tìm ở đâu? (Mặc định)

Đây là phần quan trọng nhất:

Theo mặc định, Spring Boot sẽ quét (scan) **gói (package) nơi chứa lớp có annotation `@SpringBootApplication`** và **tất cả các gói con (sub-packages)** bên trong nó.

#### 💡 Ví dụ trực quan

Hãy tưởng tượng cấu trúc thư mục dự án của bạn như sau:

```
com
└── mycompany
    ├── app
    │   ├── MyCoolApplication.java   <-- (Lớp chứa @SpringBootApplication)
    │   ├── controller
    │   │   └── MyController.java    <-- (@RestController) SẼ ĐƯỢC TÌM THẤY
    │   └── service
    │       └── MyService.java       <-- (@Service) SẼ ĐƯỢC TÌM THẤY
    │
    └── utils
        └── StringUtils.java         <-- (@Component) SẼ KHÔNG ĐƯỢC TÌM THẤY
```

* **Lớp `main`:** `com.mycompany.app.MyCoolApplication`
* **Gói gốc (Root Package) mặc định:** `com.mycompany.app`

**Kết quả:**

* Spring Boot sẽ quét `com.mycompany.app` và tất cả các gói con của nó (`.controller`, `.service`).
* `MyController` và `MyService` (nằm bên trong `com.mycompany.app`) sẽ được tìm thấy và đăng ký làm Bean.
* `StringUtils` (nằm trong `com.mycompany.utils`) sẽ **KHÔNG** được tìm thấy, vì nó nằm *bên ngoài* gói gốc `com.mycompany.app`.

Đây là lý do tại sao quy ước (convention) là luôn luôn đặt lớp `main` (lớp `@SpringBootApplication`) của bạn ở gói (package) cao nhất trong cấu trúc code của bạn.

-----

### 3\. 🔄 Làm thế nào để thay đổi? (Ghi đè mặc định)

Như bạn đã nói, nếu bạn *muốn* Spring Boot quét cả gói `com.mycompany.utils` ở ví dụ trên, bạn có thể chỉ định một cách tường minh.

Bạn có thể "ghi đè" hành vi mặc định bằng cách sử dụng các thuộc tính của `@ComponentScan` (hoặc trực tiếp trên `@SpringBootApplication`):

#### Cách 1: Dùng `scanBasePackages` (Type-unsafe)

Bạn chỉ định tên gói (package) bằng `String`.

```java
@SpringBootApplication(scanBasePackages = {
    "com.mycompany.app", 
    "com.mycompany.utils"
})
public class MyCoolApplication {
    // ...
}
```

#### Cách 2: Dùng `scanBasePackageClasses` (Type-safe - an toàn hơn)

Bạn chỉ định một lớp (class) bất kỳ nằm trong gói mà bạn muốn quét. Spring sẽ tự tìm ra tên gói từ lớp đó. Cách này an toàn hơn vì nếu bạn đổi tên gói (refactor), code của bạn sẽ báo lỗi ngay (thay vì thất bại âm thầm).

```java
@SpringBootApplication(scanBasePackageClasses = {
    MyCoolApplication.class, 
    StringUtils.class
})
public class MyCoolApplication {
    // ...
}
```

> **⚠️ Cảnh báo:** Ngay khi bạn sử dụng `scanBasePackages` hoặc `scanBasePackageClasses`, **hành vi mặc định sẽ bị HỦY BỎ**.
>
> Đó là lý do tại sao trong ví dụ trên, tôi phải liệt kê lại cả `com.mycompany.app` (hoặc `MyCoolApplication.class`). Nếu tôi chỉ ghi `com.mycompany.utils`, Spring Boot sẽ *chỉ* quét gói `utils` và bỏ qua các controller, service của tôi\!

-----

## ❓ Câu hỏi: How are DataSource and JdbcTemplate auto-configured?

(DataSource và JdbcTemplate được tự động cấu hình như thế nào?)

### 🪄 Cơ chế hoạt động (Một chuỗi phản ứng)

Spring Boot tự động cấu hình `DataSource` và `JdbcTemplate` theo một chuỗi 2 bước. Nó giống như một người trợ lý thông minh:

1.  "À, tôi thấy sếp (bạn) muốn dùng CSDL." ➡️ *Nó tự tạo một `DataSource` (nguồn dữ liệu).*
2.  "À, vì sếp đã có `DataSource` rồi, nên chắc là sếp cũng muốn một `JdbcTemplate` để làm việc cho tiện." ➡️ *Nó tự tạo một `JdbcTemplate` và "cắm" `DataSource` vào đó.*

Quá trình này được thực hiện bởi hai lớp (class) mà bạn đã chỉ ra:

-----

### 1\. Tự động cấu hình `DataSource`

* **Lớp cấu hình:** `DataSourceAutoConfiguration`
* **Mục tiêu:** Tạo ra một `Bean` `DataSource` (thường là một connection pool `HikariDataSource`).

Lớp này được kích hoạt dựa trên một loạt các **điều kiện (`@Conditional...`)**:

* **Điều kiện 1: Phải có "Dụng cụ" (`@ConditionalOnClass`)**

    * Spring Boot kiểm tra: "Trên `classpath` có lớp `DataSource.class` và `EmbeddedDatabaseType.class` không?"
    * (Bạn đáp ứng điều kiện này khi thêm `spring-boot-starter-jdbc` hoặc `spring-boot-starter-data-jpa` vào `pom.xml`).

* **Điều kiện 2: Phải có "Driver" CSDL**

    * Spring Boot rất thông minh. Nó quét `classpath` để tìm driver.
    * **NẾU** nó tìm thấy `mysql-connector-java` ➡️ Nó biết bạn muốn dùng MySQL.
    * **NẾU** nó tìm thấy `postgresql.jar` ➡️ Nó biết bạn muốn dùng PostgreSQL.
    * **NẾU** nó tìm thấy `h2.jar` ➡️ Nó biết bạn muốn dùng H2 (và nó sẽ tự cấu hình CSDL H2 trong bộ nhớ - in-memory).

* **Điều kiện 3: Phải có "Thông tin" (`@EnableConfigurationProperties`)**

    * Spring Boot tự động nạp (load) các thuộc tính (properties) của bạn (bắt đầu bằng `spring.datasource.*`) vào một `Bean` đặc biệt tên là `DataSourceProperties`.
    * Đây chính là 3 thuộc tính cốt lõi bạn đã liệt kê:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/spring-tutorial
      spring.datasource.username=root
      spring.datasource.password=
      ```

* **Điều kiện 4: "Bạn chưa tự làm" (`@ConditionalOnMissingBean`)**

    * Đây là điều kiện **quan trọng nhất**.
    * Spring Boot kiểm tra: "Người dùng (bạn) đã tự mình tạo ra một `Bean` `DataSource` nào chưa?"
    * **NẾU CÓ RỒI** ➡️ Spring Boot sẽ "lịch sự rút lui" (backs off) và dùng `Bean` của bạn.
    * **NẾU CHƯA CÓ** ➡️ Spring Boot sẽ **thực thi** và tự tạo một `DataSource` (thường là `HikariDataSource`) dựa trên các thuộc tính ở Điều kiện 3.

-----

### 2\. Tự động cấu hình `JdbcTemplate`

* **Lớp cấu hình:** `JdbcTemplateAutoConfiguration`
* **Mục tiêu:** Tạo ra một `Bean` `JdbcTemplate`.

Lớp này cũng có các điều kiện, nhưng đơn giản hơn nhiều:

* **Điều kiện 1: Phải có "Nguyên liệu" (`@ConditionalOnBean(DataSource.class)`)**

    * Spring Boot kiểm tra: "Đã có một `Bean` `DataSource` (bất kể là do tôi tự tạo ở bước 1 hay do bạn tự tạo) trong Application Context chưa?"

* **Điều kiện 2: "Bạn chưa tự làm" (`@ConditionalOnMissingBean(JdbcTemplate.class)`)**

    * Spring Boot kiểm tra: "Người dùng (bạn) đã tự mình tạo một `Bean` `JdbcTemplate` nào chưa?"

* **Hành động (Action):**

    * **NẾU** cả 2 điều kiện trên đều đúng, Spring Boot sẽ tự động tạo một `Bean` `JdbcTemplate` mới cho bạn bằng cách lấy `DataSource` đã tồn tại và "tiêm" (inject) vào nó. Về cơ bản, nó làm điều này:
      ```java
      @Bean
      public JdbcTemplate jdbcTemplate(DataSource dataSource) {
          return new JdbcTemplate(dataSource);
      }
      ```

-----

### 🏁 Kết quả: Sẵn sàng để sử dụng

Vì `DataSourceAutoConfiguration` và `JdbcTemplateAutoConfiguration` đã chạy và tự động đăng ký các `Bean` đó vào "nhà máy" (Application Context) của Spring...

...Nên bạn chỉ cần yêu cầu Spring "tiêm" (inject) chúng vào bất kỳ `@Service` hay `@Repository` nào của bạn, như chính xác bạn đã nói:

```java
@Service
public class MyUserService {

    // Spring sẽ tìm Bean "DataSource" đã được tự động cấu hình và tiêm vào đây.
    @Autowired
    private DataSource dataSource; 

    // Spring sẽ tìm Bean "JdbcTemplate" đã được tự động cấu hình và tiêm vào đây.
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void doWork() {
        // ... bạn có thể dùng chúng ngay lập tức
    }
}
```

### 📦 Tóm tắt các Yêu cầu (Để kích hoạt)

Để "phép thuật" này xảy ra, bạn chỉ cần làm 3 việc:

1.  **Thêm Starter:** `spring-boot-starter-jdbc` (hoặc `data-jpa`).
2.  **Thêm Driver:** `mysql-connector-java` (hoặc driver CSDL khác).
3.  **Cung cấp Properties:** `spring.datasource.url`, `username`, `password`.

-----

## ❓ Câu hỏi: What is `spring.factories` file for?

(File `spring.factories` dùng để làm gì?)

### 1\. `spring.factories` là gì? (Khái niệm Cốt lõi)

Một cách dễ hiểu nhất, file `META-INF/spring.factories` là một **"danh bạ" (directory)** hoặc một **"bảng đăng ký dịch vụ" (service registration list)**.

Nó là một cơ chế **đa dụng (general-purpose)** mà Spring Boot sử dụng để cho phép các module (các file `.jar`) "đăng ký" các thành phần của chúng với "hệ thống" Spring Boot, mà Spring Boot không cần phải biết trước về các module đó.

Nó cho phép các thư viện (cả của Spring và của bên thứ ba) có thể **"cắm" (plug-in)** vào các giai đoạn khác nhau của quá trình khởi động Spring.

-----

### 2\. Trường hợp sử dụng Phổ biến nhất: Tự động cấu hình (Autoconfiguration)

Đây là chức năng quan trọng và nổi tiếng nhất của nó, như bạn đã mô tả chính xác:

**Luồng hoạt động (The Flow):**

1.  **Bật công tắc:** Bạn dùng `@SpringBootApplication`, nó đã ngầm bật **`@EnableAutoConfiguration`**.
2.  **Kích hoạt Bộ nạp:** `@EnableAutoConfiguration` kích hoạt một lớp gọi là `AutoConfigurationImportSelector`.
3.  **Quét "Danh bạ":** Lớp này sử dụng một công cụ gọi là **`SpringFactoriesLoader`** để "lùng sục" (scan) *tất cả* các file `.jar` trên classpath, tìm file `META-INF/spring.factories`.
4.  **Tìm Key:** Bên trong mỗi file `spring.factories` nó tìm thấy, nó sẽ tìm đến cái "chuyên mục" (key) có tên là:
    ```
    org.springframework.boot.autoconfigure.EnableAutoConfiguration
    ```
5.  **Tập hợp "Ứng cử viên":** Nó thu thập *tất cả* các tên lớp (class names) được liệt kê dưới key này từ *mọi* file `.jar`.

**Ví dụ (Bên trong file `spring-boot-autoconfigure.jar`):**
File `spring.factories` của Spring Boot sẽ trông như thế này:

```properties
# Auto-configuration "danh bạ"
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration,\
org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
# ... (và hàng trăm lớp khác)
```

**Kết quả:**
Spring Boot tạo ra một "danh sách ứng cử viên" khổng lồ gồm tất cả các lớp cấu hình tự động. Sau đó, nó mới "phỏng vấn" (kiểm tra) từng lớp trong danh sách này bằng các annotation **`@ConditionalOn...`** (như chúng ta đã thảo luận) để quyết định xem có nên tạo `Bean` hay không.

-----

### 3\. Trường hợp sử dụng Nâng cao (Quan trọng cho kỳ thi)

Như câu trả lời của bạn đã chỉ ra, `spring.factories` **không chỉ** dùng cho Autoconfiguration. Nó là một cơ chế đa dụng.

File `spring.factories` có thể chứa nhiều "chuyên mục" (key) khác để "cắm" vào các phần khác của Spring. Một số key quan trọng khác bao gồm:

* **`org.springframework.context.ApplicationListener`**:

    * **Mục đích:** Đăng ký các bộ lắng nghe (listeners) sự kiện của ứng dụng. Các listener này được nạp *rất sớm*, thậm chí trước cả khi Application Context được tạo xong.

* **`org.springframework.boot.env.EnvironmentPostProcessor`**:

    * **Mục đích:** Cực kỳ mạnh mẽ. Cho phép bạn "đăng ký" một lớp để *thay đổi hoặc thêm* các thuộc tính (properties) vào `Environment` *trước khi* ứng dụng bắt đầu sử dụng chúng. (Cơ chế nạp `application.properties` mới của Spring Boot cũng dùng cái này).

* **`org.springframework.boot.SpringApplicationRunListener`**:

    * **Mục đích:** Đăng ký các bộ lắng nghe bám sát vòng đời của chính `SpringApplication.run()` (ví dụ: `starting()`, `environmentPrepared()`, `contextLoaded()`, `failed()`, v.v.).

-----

## ❓ Câu hỏi: How do you customize Spring auto-configuration?

(Làm thế nào để bạn tùy chỉnh (customize) cấu hình tự động của Spring?)

Bạn có thể "tùy chỉnh" cấu hình tự động theo hai cách chính, tùy thuộc vào mục tiêu của bạn:

1.  **Cách 1 (Phổ biến):** Tinh chỉnh (Tweak) hoặc Ghi đè (Override) cấu hình tự động *hiện có* mà Spring Boot cung cấp.
2.  **Cách 2 (Nâng cao):** Tạo ra (Create) một cấu hình tự động *hoàn toàn mới* (giống như cách bạn đã mô tả).

-----

### 1\. ⚙️ Cách 1: Tinh chỉnh (Tweak) / Ghi đè (Override)

Đây là cách bạn làm với tư cách là một *nhà phát triển ứng dụng (application developer)*. Bạn không tạo "starter" mới, bạn chỉ muốn thay đổi hành vi của các "starter" bạn đang dùng (như `starter-web` hay `starter-data-jpa`).

Bạn có 3 cách chính để làm điều này:

#### a. Dùng `application.properties` (Cách dễ nhất)

Hầu hết các lớp auto-configuration đều được thiết kế để "lắng nghe" các thuộc tính trong `application.properties`. Bằng cách thay đổi các thuộc tính này, bạn đang "tùy chỉnh" hành vi của chúng.

* **Ví dụ:** Mặc định, `ServletWebServerFactoryAutoConfiguration` sẽ chạy Tomcat ở port `8080`. Bạn "tùy chỉnh" nó bằng cách:
  ```properties
  # Tùy chỉnh auto-configuration của server
  server.port=9000
  ```

#### b. Cung cấp Bean của riêng bạn (Ghi đè)

Đây là cơ chế **`@ConditionalOnMissingBean`**.
Hầu hết các auto-configuration của Spring Boot đều nói rằng: "Tôi sẽ tạo `@Bean` này... **CHỈ KHI** (IF) người dùng chưa tự tạo một `Bean` cùng loại."

* **Ví dụ:** `DataSourceAutoConfiguration` sẽ tự tạo một `DataSource` (nguồn dữ liệu) NẾU nó không tìm thấy `Bean` `DataSource` nào.
* **Cách bạn tùy chỉnh:** Bạn tự định nghĩa `DataSource` của riêng mình. Ngay lập tức, auto-configuration của Spring Boot sẽ "lịch sự rút lui" (backs off) và dùng `Bean` của bạn.
  ```java
  @Configuration
  public class MyDatabaseConfig {

      // Ghi đè cấu hình tự động của DataSource
      @Bean
      public DataSource dataSource() {
          // Trả về một DataSource được cấu hình thủ công 100%
          HikariDataSource ds = new HikariDataSource();
          ds.setJdbcUrl("my-custom-url");
          ds.setUsername("my-user");
          // ...
          return ds;
      }
  }
  ```

#### c. Loại trừ (Exclude) hoàn toàn

Nếu bạn không thích một auto-configuration nào đó, bạn có thể tắt nó hoàn toàn.

```java
// Tắt hoàn toàn tính năng tự động cấu hình DataSource
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MyApplication { ... }
```

-----

### 2\. ➕ Cách 2: Tạo ra một Auto-Configuration Mới

Đây là cách bạn làm với tư cách là một *nhà phát triển thư viện (library author)*, và cũng là cách mà bạn đã mô tả trong câu trả lời.

Mục tiêu là bạn muốn tạo ra `my-cool-starter` để người khác chỉ cần thêm nó vào `pom.xml` là mọi thứ "tự động chạy".

Bạn cần làm 3 bước:

#### Bước 1: Tạo Lớp Thuộc tính (`@ConfigurationProperties`)

Đây là lớp Java (POJO) sẽ "map" với các thuộc tính mà người dùng sẽ khai báo trong `application.properties` của họ.

```java
// Lớp này sẽ chứa các thuộc tính bắt đầu bằng "greeter.message"
@ConfigurationProperties(prefix = "greeter.message")
public class GreeterProperties {
    private String text = "Hello"; // Giá trị mặc định
    // ... getters and setters
}
```

#### Bước 2: Tạo Lớp Cấu hình Tự động (`@Configuration`)

Đây là "bộ não". Lớp này sẽ chỉ chạy khi thỏa mãn một số điều kiện (`@Conditional...`) và sẽ tạo ra các `Bean` dựa trên các thuộc tính ở Bước 1.

```java
// 1. Đây là một lớp cấu hình
@Configuration 
// 2. Kích hoạt lớp Properties ở Bước 1
@EnableConfigurationProperties(GreeterProperties.class) 
// 3. (Ví dụ) Chỉ chạy NẾU classpath có lớp "GreeterService"
@ConditionalOnClass(GreeterService.class) 
public class GreeterAutoConfiguration {

    // Tiêm (inject) các thuộc tính đã được nạp
    @Autowired
    private GreeterProperties properties;

    // 4. (Ví dụ) Chỉ tạo Bean NẾU người dùng chưa tự tạo Bean "greeter"
    @Bean
    @ConditionalOnMissingBean
    public GreeterService greeterService() {
        // Tạo Bean dựa trên thuộc tính
        return new GreeterService(properties.getText()); 
    }
}
```

#### Bước 3: Đăng ký Lớp Cấu hình (`spring.factories`)

Đây là bước "ma thuật". Bạn phải tạo một file trong module (JAR) của mình tại:
`src/main/resources/META-INF/spring.factories`

Bên trong file đó, bạn "đăng ký" Lớp Cấu hình Tự động (ở Bước 2) vào "danh bạ" của Spring Boot:

```properties
# Đăng ký "ứng cử viên" auto-configuration của bạn
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.mycompany.GreeterAutoConfiguration
```

**Kết quả:**
Bây giờ, bất kỳ ai thêm file JAR của bạn vào `pom.xml`, Spring Boot sẽ tự động tìm file `spring.factories`, nạp lớp `GreeterAutoConfiguration`, kiểm tra các điều kiện, và (nếu thỏa mãn) tự động tạo ra `GreeterService` `Bean` cho họ.

-----
Chào bạn, đây là một trong những "starter" quan trọng và hữu ích nhất của Spring Boot. Câu trả lời của bạn rất chính xác\!

**Actuator** là một tính năng "phải có" (must-have) cho bất kỳ ứng dụng nào chạy trên môi trường production.

Hãy cùng diễn giải chi tiết các giá trị này.

## ❓ Câu hỏi: What values does Spring Boot Actuator provide?

(Spring Boot Actuator cung cấp những giá trị gì?)

### 1\. 🩺 Spring Boot Actuator là gì?

Một cách dễ hiểu nhất, hãy tưởng tượng ứng dụng Spring Boot của bạn là một chiếc **ô tô**.

* **Code của bạn** (`@Controller`, `@Service`...) là **động cơ**, giúp xe chạy.
* **Spring Boot Actuator** chính là **bảng điều khiển (dashboard)** và **cổng chẩn đoán (OBD-II port)** của chiếc xe đó.

Nó cung cấp các tính năng "production-ready" (sẵn sàng cho vận hành) cho phép bạn **giám sát (monitor)** và **quản lý (manage)** ứng dụng của mình *sau khi nó đã được khởi chạy*.

Như bạn đã nói, giá trị lớn nhất là bạn nhận được tất cả những điều này **"miễn phí"**—bạn không cần phải tự viết code—chỉ bằng cách thêm một dependency duy nhất.

### 2\. 🔌 Làm thế nào để kích hoạt?

Chính xác như bạn đã nêu, bạn chỉ cần thêm "starter" của Actuator vào `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Ngay lập tức, Spring Boot sẽ tự động cấu hình (Autoconfiguration) một loạt các **endpoints** (các đường dẫn URL đặc biệt) bên trong ứng dụng của bạn.

-----

### 3\. Các "Giá trị" (Endpoints) chính mà Actuator cung cấp

"Giá trị" mà Actuator cung cấp chính là thông tin mà các endpoint này phơi bày ra. Chúng ta có thể nhóm chúng lại như sau:

#### a. 💚 Health-checks (Kiểm tra sức khỏe)

* **Endpoint:** `/actuator/health`
* **Giá trị:** Đây là endpoint **quan trọng nhất**. Nó kiểm tra "sức khỏe" tổng thể của ứng dụng và trả về `{"status": "UP"}` hoặc `{"status": "DOWN"}`.
* **Chi tiết:** "Sức khỏe" này là một tập hợp (composite) của nhiều thứ:
    * Kết nối CSDL (ví dụ: `DataSourceHealthIndicator`)
    * Dung lượng ổ đĩa (ví dụ: `DiskSpaceHealthIndicator`)
    * Kết nối đến các dịch vụ khác (Kafka, Redis, RabbitMQ...)
* Các hệ thống như Kubernetes (Liveness/Readiness probes) sẽ liên tục gọi endpoint này để quyết định xem có nên gửi traffic đến ứng dụng của bạn hay không.

#### b. 📊 Metrics (Số liệu chi tiết)

* **Endpoint:** `/actuator/metrics`
* **Giá trị:** Cung cấp hàng trăm số liệu chi tiết về "nội tạng" của ứng dụng, thường được tích hợp với các hệ thống giám sát như **Prometheus** hoặc Datadog.
* **Ví dụ:**
    * `/actuator/metrics/jvm.memory.used`: Lượng RAM mà JVM đang sử dụng.
    * `/actuator/metrics/system.cpu.usage`: Mức độ sử dụng CPU.
    * `/actuator/metrics/http.server.requests`: Số lượng, thời gian, và lỗi của các request HTTP.

#### c. 👁️ Monitoring & Management (Giám sát & Quản lý)

Đây là một nhóm lớn các endpoint giúp bạn "nhìn trộm" (introspect) vào bên trong ứng dụng đang chạy:

* **`/actuator/info`**:
    * Hiển thị thông tin chung mà bạn cung cấp (lấy từ `info.*` trong `application.properties` hoặc từ `git.properties`).
* **`/actuator/env`**:
    * Hiển thị **tất cả** các thuộc tính (properties) mà ứng dụng của bạn đang sử dụng (từ file config, biến môi trường, v.v.).
* **`/actuator/configprops`**:
    * Hiển thị tất cả các `Bean` `@ConfigurationProperties` và giá trị của chúng.
* **`/actuator/beans`**:
    * Liệt kê **tất cả** các `Bean` đã được Spring tạo ra trong Application Context.
* **`/actuator/mappings`**:
    * Hiển thị tất cả các đường dẫn (`@RequestMapping`, `@GetMapping`...) và controller nào đang xử lý chúng.
* **`/actuator/loggers`**: (Cực kỳ hữu ích)
    * Cho phép bạn **xem và thay đổi mức độ log (log levels)** của ứng dụng *ngay lúc đang chạy* mà không cần khởi động lại.
    * *Ví dụ:* Bạn có thể `POST` để đổi `logging.level.com.mycompany=DEBUG` để gỡ lỗi một sự cố trên production, sau đó `POST` để trả lại `INFO` khi xong.

#### d. 🛡️ Audit Events (Sự kiện Kiểm toán)

* **Endpoint:** `/actuator/auditevents`
* **Giá trị:** Ghi lại các sự kiện liên quan đến bảo mật (nếu bạn có Spring Security).
* **Ví dụ:** `AUTHORIZATION_FAILURE` (ai đó cố truy cập vào trang bị cấm), `AUTHENTICATION_SUCCESS` (ai đó đăng nhập thành công).

-----

### 4\. ⚠️ Lưu ý quan trọng về Bảo mật (Exposing Endpoints)

Như bạn đã lưu ý, Actuator cung cấp rất nhiều thông tin nhạy cảm.

Vì lý do bảo mật, kể từ Spring Boot 2.x, **chỉ có 2 endpoint được phơi bày (exposed) qua web (HTTP) theo mặc định**:

1.  `/actuator/health`
2.  `/actuator/info`

(Tất cả các endpoint khác vẫn được phơi bày qua **JMX** theo mặc định, nhưng web thì không).

**Để phơi bày các endpoint khác (ví dụ: `metrics`, `loggers`), bạn phải chỉ định rõ trong `application.properties`:**

````properties
# Phơi bày các endpoint cụ thể mà bạn muốn
management.endpoints.web.exposure.include=health,info,metrics,loggers

# Hoặc, phơi bày TẤT CẢ (NGUY HIỂM - chỉ dùng khi phát triển)
# management.endpoints.web.exposure.include=* 
````
Và bạn **luôn luôn** phải bảo mật các endpoint này (ví dụ: dùng Spring Security) trước khi triển khai lên production.

-----

## ❓ Câu hỏi: What are the actuator endpoints that are provided out of the box

(Các endpoint actuator nào được cung cấp sẵn?)

### 1\. 🛡️ Triết lý Bảo mật: JMX (Tin cậy) vs. Web (Không tin cậy)

Điều quan trọng nhất bạn cần hiểu từ bảng trên là sự khác biệt lớn giữa **JMX** và **Web (HTTP)**:

* **JMX (Java Management Extensions):** Đây là một công nghệ Java để quản lý ứng dụng. Spring Boot mặc định "tin tưởng" JMX, vì nó thường chỉ được truy cập bởi các công cụ giám sát nội bộ (internal) trong cùng một mạng.
* **Web (HTTP):** Các endpoint này được truy cập qua Internet (ví dụ: `my-app.com/actuator/health`). Vì chúng lộ ra ngoài, Spring Boot mặc định **cực kỳ cẩn trọng**.

Đây là lý do tại sao trong bảng của bạn:

* Hầu hết mọi endpoint đều có **"Exposed via JMX" = Yes**.
* Hầu hết mọi endpoint đều có **"Exposed via Web" = No**.

-----

### 2\. 🩺 Các Endpoint Mặc định (Web)

Như bảng của bạn đã chỉ ra, theo mặc định, chỉ có **hai** endpoint được phơi bày (exposed) qua Web. Đây là những endpoint được coi là "an toàn" và hữu ích cho việc giám sát cơ bản:

* **`/actuator/health`**:
    * **Mô tả:** Endpoint quan trọng nhất. Nó giống như "bộ theo dõi nhịp tim" của ứng dụng.
    * **Giá trị:** Trả về `{"status": "UP"}` nếu ứng dụng khỏe mạnh. Nó cũng kiểm tra các kết nối bên ngoài (như CSDL, RabbitMQ) và báo cáo nếu chúng có vấn đề.
    * **Tại sao mặc định:** Các hệ thống điều phối (như Kubernetes) và Bộ cân bằng tải (Load Balancers) *cần* endpoint này để biết có nên gửi traffic đến ứng dụng hay không.
* **`/actuator/info`**:
    * **Mô tả:** Hiển thị thông tin chung, "an toàn" về ứng dụng.
    * **Giá trị:** Mặc định là trống (`{}`). Bạn có thể điền thông tin vào đây bằng cách thêm các thuộc tính `info.*` vào `application.properties` (ví dụ: `info.app.version=1.0.0`) hoặc nó sẽ tự động lấy thông tin từ file `git.properties`.

-----

### 3\. 🚫 Endpoint Đặc biệt: `/actuator/shutdown`

Hãy chú ý đến endpoint này trong bảng của bạn:

* **`/actuator/shutdown`**:
    * **Mô tả:** Cho phép tắt ứng dụng một cách an toàn (gracefully) qua HTTP `POST`.
    * **Enabled by default = No**: Đây là endpoint duy nhất bị **TẮT (disabled)** hoàn toàn theo mặc định.
    * **Tại sao:** Vì nó quá nguy hiểm. Bất kỳ ai cũng có thể tắt ứng dụng của bạn nếu nó được bật. Bạn phải bật nó một cách tường minh (explicitly) nếu cần.

-----

### 4\. 🗃️ Phân loại các Endpoint "Bị ẩn" (Hidden Web Endpoints)

Hầu hết các endpoint còn lại đều rất mạnh mẽ nhưng cũng **nhạy cảm**. Chúng được bật (enabled) nhưng không được phơi bày (exposed) ra Web. Bạn phải tự phơi bày chúng một cách có chủ đích.

Chúng ta có thể nhóm chúng lại:

#### Nhóm 1: Chẩn đoán Cấu hình (Configuration Diagnostics)

* **`beans`**: Hiển thị tất cả các Bean đã được Spring tạo ra.
* **`configprops`**: Liệt kê tất cả các `@ConfigurationProperties` và giá trị của chúng.
* **`env`**: (Rất nhạy cảm) Hiển thị tất cả các biến môi trường và thuộc tính, **bao gồm cả mật khẩu CSDL nếu không được mã hóa đúng cách**.
* **`mappings`**: Liệt kê tất cả các URL (`@GetMapping`...) và controller nào đang xử lý chúng.
* **`conditions`**: (Cực kỳ hữu ích để gỡ lỗi) Hiển thị báo cáo chi tiết tại sao một `@Configuration` được (hoặc không được) tự động cấu hình.

#### Nhóm 2: Giám sát & Quản lý (Monitoring & Management)

* **`metrics`**: Cung cấp hàng trăm số liệu chi tiết (CPU, memory, request time...) để các hệ thống như Prometheus "cào" (scrape).
* **`loggers`**: Cho phép bạn **xem và thay đổi log level** (ví dụ: từ `INFO` sang `DEBUG`) *ngay lúc ứng dụng đang chạy* mà không cần khởi động lại. Rất hữu ích khi gỡ lỗi trên production.
* **`threaddump` / `heapdump`**: (Rất nhạy cảm) Cho phép bạn tải về một bản "chụp" bộ nhớ hoặc luồng của ứng dụng.

#### Nhóm 3: Tích hợp (Integrations)

* **`flyway` / `liquibase`**: Hiển thị trạng thái migration CSDL.
* **`auditevents`**: Hiển thị các sự kiện bảo mật (nếu dùng Spring Security).
* **`httptrace`**: Hiển thị 100 request HTTP gần nhất (rất hữu ích để gỡ lỗi).

-----

### 5\. ⚙️ Cách Tùy chỉnh (Controlling Endpoints)

Câu trả lời của bạn đã nêu rất chính xác cách kiểm soát các endpoint này trong `application.properties`:

**a. Bật/Tắt một Endpoint (Enable/Disable)**
(Hầu hết là `true` rồi, nhưng ví dụ `shutdown` là `false`)

```properties
# Bật endpoint 'shutdown' (vốn bị tắt mặc định)
management.endpoint.shutdown.enabled=true

# Tắt endpoint 'beans' (vốn được bật mặc định)
management.endpoint.beans.enabled=false
```

**b. Phơi bày (Expose) ra Web**
Đây là việc bạn sẽ làm thường xuyên nhất.

```properties
# Chỉ phơi bày 'health', 'info', 'metrics', và 'loggers' ra Web
management.endpoints.web.exposure.include=health,info,metrics,loggers

# KHÔNG NÊN LÀM ở Production: Phơi bày TẤT CẢ các endpoint ra Web
# management.endpoints.web.exposure.include=*
```

**c. Tích hợp HATEOAS (Để khám phá)**
Như bạn đã đề cập, nếu bạn thêm `spring-boot-starter-hateoas`, thì khi truy cập `/actuator`, bạn sẽ nhận được một JSON chứa các đường link (links) đến tất cả các endpoint *đã được phơi bày*, giúp bạn dễ dàng khám phá chúng.

-----

## ❓ Câu hỏi: What is info endpoint for? How do you supply data?

(Endpoint info dùng để làm gì? Làm thế nào để bạn cung cấp dữ liệu cho nó?)

### 1\. ℹ️ Endpoint `/info` dùng để làm gì?

Endpoint `/actuator/info` là một "tấm danh thiếp" (business card) của ứng dụng.

Nó dùng để hiển thị bất kỳ thông tin **tùy ý (arbitrary)** và **không nhạy cảm (non-sensitive)** nào về ứng dụng của bạn. Đây là nơi bạn (với tư cách là nhà phát triển) cung cấp thông tin "meta" (siêu dữ liệu) cho bất kỳ ai (người vận hành, API client khác) muốn "hỏi thăm" ứng dụng của bạn là gì.

Như bạn đã nói, nó được phơi bày (exposed) mặc định qua cả:

* **HTTP:** Tại `/actuator/info`
* **JMX:** Tại `org.springframework.boot/Endpoint/Info`

Các thông tin phổ biến thường được đặt ở đây bao gồm:

* Tên ứng dụng, phiên bản (version), mô tả.
* Thông tin build từ Git (như commit ID, branch).
* Thông tin môi trường (như phiên bản Java đang chạy).

### 2\. ✍️ Làm thế nào để cung cấp dữ liệu?

Bạn có hai cách chính để "thêm" dữ liệu vào nội dung của endpoint `/info`:

#### Cách 1: Qua `application.properties` (Cách đơn giản)

Đây là cách dễ nhất. Spring Boot sẽ tự động thu thập **tất cả** các thuộc tính (properties) có tiền tố (prefix) là **`info.`** và hiển thị chúng dưới dạng JSON.

**Ví dụ:**
Nếu bạn đặt trong file `application.properties`:

```properties
info.app.name=Spring Boot Application
info.app.description=Ung dung demo Actuator
info.app.version=1.0.0

# Bạn cũng có thể dùng các biến có sẵn (placeholder)
info.java.vendor=${java.specification.vendor}
```

**Kết quả:**
Khi bạn truy cập `GET /actuator/info`, bạn sẽ thấy:

```json
{
  "app": {
    "name": "Spring Boot Application",
    "description": "Ung dung demo Actuator",
    "version": "1.0.0"
  },
  "java": {
    "vendor": "Oracle Corporation"
  }
}
```

#### Cách 2: Qua Bean `InfoContributor` (Cách linh hoạt)

Khi bạn cần cung cấp thông tin động (dynamic) hoặc thông tin không có sẵn trong file properties (ví dụ: thông tin hệ điều hành), bạn có thể tạo một `Bean` implement interface `InfoContributor`.

Spring Boot sẽ tự động phát hiện tất cả các `Bean` này, chạy chúng, và gộp (merge) kết quả vào chung endpoint `/info`.

**Ví dụ (như code của bạn):**

```java
@Component
public class SystemNameInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        // Lấy thông tin OS một cách linh động khi endpoint được gọi
        String osName = System.getProperty("os.name");
        
        // Thêm một mục "system-name" vào JSON
        builder.withDetail("system-name", osName);

        // Bạn cũng có thể thêm một đối tượng phức tạp
        // Map<String, Object> details = new HashMap<>();
        // details.put("version", "11");
        // builder.withDetail("runtime", details);
    }
}
```

**Kết quả (gộp cả 2 cách):**
Khi truy cập `GET /actuator/info`, bạn sẽ thấy kết quả được gộp lại:

```json
{
  "app": {
    "name": "Spring Boot Application",
    "description": "Ung dung demo Actuator",
    "version": "1.0.0"
  },
  "java": {
    "vendor": "Oracle Corporation"
  },
  "system-name": "Windows 11" 
}
```

### 💡 Tích hợp Git (Rất phổ biến)

Như bạn đã đề cập, một cách sử dụng `info` rất phổ biến là tự động hiển thị thông tin build của Git. Bạn có thể làm điều này bằng cách thêm `git-commit-id-plugin` vào `pom.xml`. Plugin này sẽ tạo ra file `git.properties`, và Spring Boot (qua `InfoContributor` tích hợp sẵn) sẽ tự động đọc file đó và thêm vào `/info`.

-----

## ❓ Câu hỏi: How do you change logging level of a package using loggers endpoint?

(Làm thế nào để bạn thay đổi cấp độ log của một package bằng endpoint loggers?)

### 1\. ⚠️ Bước 1: Phơi bày (Expose) Endpoint (Việc cần làm đầu tiên)

Theo mặc định, Spring Boot *không* phơi bày (expose) endpoint `/loggers` qua web (HTTP) vì lý do bảo mật. Nó chỉ được phơi bày qua JMX.

Để bật nó cho HTTP, bạn phải thêm nó một cách tường minh vào file `application.properties`:

```properties
# Phơi bày các endpoint: health, info, và loggers
management.endpoints.web.exposure.include=health,info,loggers
```

Sau khi thêm dòng này và khởi động lại, endpoint `/actuator/loggers` mới có thể truy cập được qua web.

-----

### 2\. 🔎 Bước 2: Xem (GET) Cấp độ Log Hiện tại (Tùy chọn)

Trước khi thay đổi, bạn nên kiểm tra cấp độ log hiện tại. Bạn thực hiện việc này bằng cách gửi một request `GET` đến:

`/actuator/loggers/{LOGGER_NAME}`

**Ví dụ:**
`GET http://localhost:8080/actuator/loggers/org.spring.cert.question28`

**Kết quả (như bạn đã nêu):**

```json
{
  "configuredLevel": null,
  "effectiveLevel": "INFO"
}
```

* **`configuredLevel: null`**: Nghĩa là bạn chưa *chủ động* cài đặt (set) level cho package này.
* **`effectiveLevel: "INFO"`**: Nghĩa là mức log *thực tế* đang chạy là `INFO`, thường là do nó đang **kế thừa (inheriting)** từ một package cha (ví dụ: `org.spring.cert`) hoặc từ `root`.

-----

### 3\. 🔄 Bước 3: Thay đổi (POST) Cấp độ Log (Đây là câu trả lời chính)

Để thay đổi cấp độ log *ngay lập tức*, bạn phải gửi một request **`POST`** đến cùng một endpoint, mang theo một body (payload) dạng JSON chỉ định cấp độ mới.

* **Phương thức:** `POST`
* **Endpoint:** `/actuator/loggers/{LOGGER_NAME}`
* **Body (JSON):** `{"configuredLevel": "LEVEL_BAN_MUON"}`

**Ví dụ (dùng `curl` như của bạn):**
Bạn muốn đổi package `org.spring.cert.question28` sang `TRACE` để gỡ lỗi:

```bash
curl -i -X POST -H "Content-Type: application/json" \
     -d '{"configuredLevel": "TRACE"}' \
     http://localhost:8080/actuator/loggers/org.spring.cert.question28
```

* `-X POST`: Chỉ định phương thức là `POST`.
* `-H "Content-Type: application/json"`: Báo cho server biết body bạn gửi là JSON.
* `-d '{"configuredLevel": "TRACE"}'`: Đây là "payload" (nội dung) bạn gửi đi, yêu cầu đổi level thành `TRACE`.

**Kết quả:**
Ngay lập tức (không cần restart), tất cả log `TRACE` và `DEBUG` từ package `org.spring.cert.question28` sẽ bắt đầu xuất hiện trong console của bạn.

-----

### 💡 Làm thế nào để "Tắt" (Reset) về mặc định?

Sau khi gỡ lỗi xong, bạn chỉ cần `POST` lại endpoint đó với giá trị `configuredLevel` là `null`.

```bash
curl -i -X POST -H "Content-Type: application/json" \
     -d '{"configuredLevel": null}' \
     http://localhost:8080/actuator/loggers/org.spring.cert.question28
```

Việc này sẽ xóa cài đặt "cứng" của bạn, và package đó sẽ quay trở lại kế thừa (inherit) level từ `root` (thường là `INFO`).

### 🖥️ Ghi chú về JMX

Như bạn đã nói, tất cả các thao tác này (xem và thay đổi) cũng có thể được thực hiện qua **JMX** (ví dụ: dùng `JConsole` hoặc `VisualVM`). Bạn sẽ tìm đến MBean `org.springframework.boot` -\> `Endpoint` -\> `Loggers` và gọi các "Operations" (hoạt động) `loggerLevels` hoặc `configureLogLevel` như bạn đã mô tả.

-----

## ❓ Câu hỏi: How do you access an endpoint using a tag?

(Làm thế nào để bạn truy cập một endpoint bằng cách sử dụng tag?)

### 1\. Tag là gì?

Trong Spring Boot Actuator, "tag" là một **chiều (dimension)** của một số liệu (metric). Nó là một cặp **`Key:Value`** (Khóa:Giá trị) giúp bạn phân loại dữ liệu.

Ví dụ, hãy xem xét số liệu (metric) tên là `http.server.requests` (tổng số request HTTP đến server). Số liệu này có nhiều "chiều":

* **`status`**: Mã trạng thái (200, 404, 500...).
* **`method`**: Phương thức (GET, POST...).
* **`uri`**: Đường dẫn được gọi (ví dụ: `/api/users/{id}`).

Sử dụng tag cho phép bạn "hỏi" những câu hỏi cụ thể hơn, ví dụ: "Cho tôi xem số liệu request, nhưng *chỉ* cho những request có `status=200`."

-----

### 2\. Cú pháp sử dụng Tag

Như bạn đã nêu, bạn truy cập một metric cụ thể và sau đó thêm tag vào như một tham số truy vấn (query parameter) `?tag=...`.

* **Endpoint:** `/actuator/metrics/{METRIC_NAME}`
* **Cú pháp:** `?tag=KEY:VALUE`

#### 📖 Ví dụ 1: Lọc theo một (1) Tag

Bạn muốn xem chi tiết số liệu `http.server.requests`, nhưng *chỉ* cho các request có mã trạng thái `200` (OK).

`GET http://localhost:8080/actuator/metrics/http.server.requests?tag=status:200`

**Kết quả (ví dụ):**
Kết quả trả về sẽ chỉ chứa thông tin cho các request `200 OK`, như tổng số lượng (count) và tổng thời gian (total time).

```json
{
  "name": "http.server.requests",
  "measurements": [
    { "statistic": "COUNT", "value": 150.0 },
    { "statistic": "TOTAL_TIME", "value": 12.345 },
    // ...
  ],
  "availableTags": [
    // ... các tag khác có thể lọc tiếp
  ]
}
```

-----

#### 📖 Ví dụ 2: Lọc theo nhiều (Multiple) Tags

Bạn muốn xem chi tiết hơn nữa: *chỉ* các request `GET` mà có mã trạng thái `200`.

Bạn chỉ cần thêm nhiều tham số `&tag=...`, như bạn đã nói:

`GET http://localhost:8080/actuator/metrics/http.server.requests?tag=status:200&tag=method:GET`

-----

### 3\. Cách tìm (Discover) các Tag

Làm sao bạn biết một metric có những tag nào?

Bạn chỉ cần gọi tên metric mà **không có tag**:
`GET http://localhost:8080/actuator/metrics/http.server.requests`

Kết quả sẽ chứa một trường là `availableTags`, liệt kê tất cả các "chiều" (dimensions) mà bạn có thể dùng để lọc:

```json
{
  "name": "http.server.requests",
  "description": "...",
  "measurements": [ ... ],
  "availableTags": [
    { "tag": "exception", "values": ["none", "SomeException"] },
    { "tag": "method", "values": ["GET", "POST"] },
    { "tag": "outcome", "values": ["SUCCESS", "CLIENT_ERROR"] },
    { "tag": "status", "values": ["200", "404", "500"] },
    { "tag": "uri", "values": ["/api/users", "/actuator/health"] }
  ]
}
```

Bạn có thể thấy rõ các tag `status`, `method`, `uri`... đều có sẵn để bạn sử dụng.

-----

## ❓ Câu hỏi: What is metrics for?

(Endpoint metrics dùng để làm gì?)

### 1\. 📊 Endpoint `/metrics` dùng để làm gì?

Endpoint `/actuator/metrics` là **"phòng theo dõi chỉ số sống" (live vitals monitor)** của ứng dụng.

Mục đích chính của nó là thu thập và phơi bày (expose) hàng trăm **số liệu đo lường (metrics)** chi tiết về hiệu suất (performance) và trạng thái nội bộ (internal state) của ứng dụng bạn *khi nó đang chạy*.

Nó không chỉ báo "UP" hay "DOWN" (như `/health`), mà nó cho bạn biết *chi tiết* MỌI THỨ đang diễn ra:

* **CPU:** Đang dùng bao nhiêu %?
* **Memory (RAM):** Đang dùng bao nhiêu? Tối đa bao nhiêu?
* **Garbage Collector (GC):** Đã chạy bao nhiêu lần? Tốn bao lâu?
* **Threads:** Có bao nhiêu luồng đang chạy? Bao nhiêu luồng đang bị khoá?
* **HTTP Requests:** Có bao nhiêu request? Tốc độ phản hồi trung bình là bao nhiêu? Có bao nhiêu request bị lỗi 404, 500?

-----

### 2\. 🎯 Mục đích sử dụng (Tại sao nó quan trọng?)

Các số liệu này là "vàng" đối với đội vận hành (DevOps) và lập trình viên (Developers). Chúng được dùng để:

1.  **Giám sát & Cảnh báo (Monitoring & Alerting):**

    * Bạn có thể cắm các công cụ như **Prometheus**, **Grafana**, hoặc **Datadog** vào endpoint này.
    * Các công cụ này sẽ liên tục "cào" (scrape) dữ liệu và vẽ biểu đồ.
    * Bạn có thể thiết lập cảnh báo (alert), ví dụ: "Nếu `jvm.memory.used` vượt quá 80% trong 5 phút ➡️ gửi cảnh báo qua Slack\!"

2.  **Phân tích Hiệu năng (Performance Tuning):**

    * "Tại sao ứng dụng chậm?"
    * Bạn có thể xem `/actuator/metrics/http.server.requests` để thấy thời gian phản hồi trung bình của một endpoint cụ thể.
    * Bạn có thể xem `/actuator/metrics/jvm.gc.pause` để xem có phải do Garbage Collector chạy quá nhiều hay không.

3.  **Gỡ lỗi (Debugging):**

    * "Ứng dụng có vẻ sắp 'chết'?"
    * Bạn có thể xem `jvm.threads.live` để kiểm tra xem số luồng có đang tăng đột biến (thread leak) hay không.

-----

### 3\. 📖 Cách truy cập (Như bạn đã nêu)

#### Bước 1: Phơi bày (Expose) Endpoint

Quan trọng nhất, nó **không được phơi bày (exposed) qua web theo mặc định**. Bạn phải bật nó trong `application.properties`:

```properties
management.endpoints.web.exposure.include=health,info,metrics
```

#### Bước 2: Khám phá (Discover) Metrics

* `GET /actuator/metrics`
* Thao tác này sẽ trả về một JSON liệt kê **tên** của tất cả các metric có sẵn (ví dụ: `jvm.memory.used`, `http.server.requests`...).

#### Bước 3: Xem (View) Metric cụ thể

* `GET /actuator/metrics/{METRIC_NAME}`
* Ví dụ: `GET /actuator/metrics/jvm.memory.used`
* Thao tác này sẽ cho bạn xem chi tiết về metric đó, bao gồm các **tag (thẻ)** có sẵn để bạn lọc.

#### Bước 4: Lọc (Filter) bằng Tag

* `GET /actuator/metrics/{METRIC_NAME}?tag=KEY:VALUE`
* Như ví dụ của bạn: `GET /actuator/metrics/jvm.memory.used?tag=area:heap`
* Thao tác này "khoan" (drill down) vào dữ liệu, chỉ lấy số liệu bộ nhớ cho khu vực `heap`.

-----

## ❓ Câu hỏi: How do you create a custom metrics with or without tags

(Làm thế nào để bạn tạo một metric tùy chỉnh (custom) có hoặc không có tag?)

### 1\. ⚙️ "Nhà máy" `MeterRegistry`

Cách đơn giản nhất để tạo custom metrics là **tiêm (inject)** `Bean` `MeterRegistry` vào component hoặc service của bạn. `MeterRegistry` hoạt động như một trung tâm "đăng ký" (registration center) cho tất cả các "đồng hồ đo" (meters) của bạn.

```java
@Service
public class MyService {

    private final MeterRegistry meterRegistry;

    // 1. Tiêm (inject) MeterRegistry vào
    @Autowired
    public MyService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        // Bạn có thể đăng ký metrics ngay tại đây nếu chúng là "gauge"
    }

    // ...
}
```

-----

### 2\. 📏 Tạo Metric KHÔNG CÓ Tag (Simple)

Đây là trường hợp đơn giản nhất, bạn chỉ cần cung cấp một tên (name) duy nhất cho metric. Cách này hữu ích khi bạn chỉ cần một con số tổng duy nhất.

Ví dụ, bạn muốn đếm tổng số lần một phương thức được gọi:

```java
@Service
public class SimpleCounterService {

    // 2. Lưu lại "đồng hồ" (meter)
    private final Counter simpleCounter;

    @Autowired
    public SimpleCounterService(MeterRegistry meterRegistry) {
        // 1. Đăng ký một "Counter" (bộ đếm) mới với tên duy nhất
        this.simpleCounter = meterRegistry.counter("my.service.calls.total");
    }

    public void doWork() {
        // 3. Tăng bộ đếm lên
        this.simpleCounter.increment();
    }
}
```

**Kết quả:**
Bây giờ, khi bạn truy cập `GET /actuator/metrics/my.service.calls.total`, bạn sẽ thấy tổng số lần `doWork()` được gọi.

-----

### 3\. 🏷️ Tạo Metric CÓ Tag (With Dimensions)

Đây là cách làm được **khuyến khích (recommended)** vì nó mạnh mẽ hơn rất nhiều.

**Tag** (thẻ) là một cặp `key:value` cho phép bạn thêm **"chiều" (dimensions)** vào metric của mình. Thay vì chỉ có *một* con số tổng, bạn có thể "cắt lát" (slice and dice) dữ liệu của mình.

Ví dụ: Bạn muốn đếm số lần `doWork()` được gọi, nhưng bạn muốn phân biệt các lần gọi **thành công (success)** và **thất bại (failure)**.

```java
@Service
public class TaggedCounterService {

    private final MeterRegistry meterRegistry;

    @Autowired
    public TaggedCounterService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void doWork(boolean success) {
        try {
            // ... (logic nghiệp vụ)
            
            // 1. Nếu thành công, tăng bộ đếm có tag "status=success"
            if (success) {
                meterRegistry.counter("my.service.calls", "status", "success").increment();
            } else {
                throw new RuntimeException("Fake error");
            }
            
        } catch (Exception e) {
            // 2. Nếu thất bại, tăng bộ đếm có tag "status=failure"
            meterRegistry.counter("my.service.calls", "status", "failure").increment();
            throw e;
        }
    }
}
```

**Kết quả:**
Bây giờ, bạn có thể truy vấn metric này theo nhiều cách:

* `GET /actuator/metrics/my.service.calls`: Xem tổng số lần gọi (cả thành công và thất bại).
* `GET /actuator/metrics/my.service.calls?tag=status:success`: **Chỉ** xem số lần gọi thành công.
* `GET /actuator/metrics/my.service.calls?tag=status:failure`: **Chỉ** xem số lần gọi thất bại.

-----

### 4\. 🛠️ Cách dùng Builder (Cách làm rõ ràng)

Như bạn đã nêu, việc sử dụng cú pháp `builder` (fluent API) thường rõ ràng hơn, đặc biệt khi bạn muốn thêm mô tả (description) hoặc nhiều tag:

```java
// Ví dụ đăng ký một metric có 2 tag
Counter myCounter = Counter.builder("storage.object.count")
    .description("Đếm số lượng object được lưu trữ")
    .tag("type", "image")       // Tag 1
    .tag("region", "us-east-1") // Tag 2
    .register(meterRegistry); // Đừng quên .register()
    
myCounter.increment();
```

### 5\. ⏱️ Các loại "Đồng hồ" (Meters) khác

Bạn đã liệt kê rất đầy đủ. Ngoài `Counter` (chỉ tăng), các loại phổ biến khác là:

* **`Gauge` (Đồng hồ đo):** Dùng để theo dõi một giá trị có thể *tăng hoặc giảm* (ví dụ: số lượng item hiện tại trong một `Queue`, hoặc % bộ nhớ đang dùng).
* **`Timer` (Đồng hồ bấm giờ):** Dùng để đo thời gian thực thi của một phương thức (ví dụ: thời gian xử lý của một API).

-----

## ❓ Câu hỏi: What is Health Indicator?

(Health Indicator là gì?)

### 1\. 🩺 Health Indicator là gì? (Bác sĩ chuyên khoa)

Một **`HealthIndicator`** là một "bác sĩ chuyên khoa". Nó là một `Bean` (component) của Spring có một nhiệm vụ duy nhất: kiểm tra sức khỏe của **một (1) thành phần cụ thể** trong ứng dụng của bạn.

Spring Boot cung cấp sẵn rất nhiều "bác sĩ chuyên khoa" này, ví dụ:

* **`DataSourceHealthIndicator`**: Chuyên khoa "Tim mạch", kiểm tra "Mạch máu" (kết nối CSDL) còn thông không?
* **`DiskSpaceHealthIndicator`**: Chuyên khoa "Dinh dưỡng", kiểm tra "Dạ dày" (ổ đĩa) còn dung lượng trống không?
* **`RedisHealthIndicator`**: Chuyên khoa "Trí nhớ", kiểm tra kết nối tới Redis còn tốt không?
* **`RabbitHealthIndicator`**: Chuyên khoa "Truyền tin", kiểm tra kết nối tới RabbitMQ còn tốt không?

-----

### 2\. 👨‍⚕️ Endpoint `/actuator/health` hoạt động thế nào? (Bác sĩ tổng quát)

Endpoint **`/actuator/health`** hoạt động như một "bác sĩ tổng quát". Khi bạn (hoặc một hệ thống giám sát) gọi đến `GET /actuator/health`, nó sẽ làm các việc sau:

1.  Nó đi đến "phòng đăng ký" (`HealthIndicatorRegistry`).
2.  Nó yêu cầu **tất cả** các "bác sĩ chuyên khoa" (`HealthIndicator` đã đăng ký) bắt đầu "khám" phần việc của mình.
3.  Nó thu thập tất cả các kết quả (ví dụ: CSDL: `UP`, Ổ đĩa: `UP`, Redis: `DOWN`).
4.  Nó **tổng hợp (aggregates)** các kết quả đó.

**Quy tắc tổng hợp:**

* Nếu **TẤT CẢ** các `HealthIndicator` đều trả về `UP` ➡️ Endpoint `/actuator/health` sẽ trả về `UP`.
* Nếu **CHỈ CẦN MỘT** `HealthIndicator` trả về `DOWN` ➡️ Endpoint `/actuator/health` sẽ trả về `DOWN`.

-----

### 3\. 🎯 Mục đích sử dụng (Tại sao nó quan trọng?)

Như bạn đã nói, đây là endpoint quan trọng bậc nhất cho việc vận hành:

1.  **Hệ thống Giám sát & Cảnh báo (Monitoring & Alerting):**

    * Một công cụ (như Prometheus, Nagios) sẽ liên tục "ping" endpoint này.
    * Nếu nó nhận được `DOWN`, nó sẽ ngay lập tức gửi cảnh báo (qua PagerDuty, Slack, Email...) cho đội vận hành (DevOps) để xử lý sự cố.

2.  **Bộ cân bằng tải & Tính sẵn sàng cao (Load Balancer & HA):**

    * Đây là mục đích sử dụng **quan trọng nhất**.
    * Trong một hệ thống lớn (ví dụ: Kubernetes), một Bộ cân bằng tải (Load Balancer) sẽ liên tục "hỏi thăm" `/actuator/health` của từng instance (phiên bản) ứng dụng.
    * Nếu Instance A báo `DOWN`, Bộ cân bằng tải sẽ **ngừng gửi traffic (lưu lượng người dùng) mới** đến Instance A và chuyển sang các Instance B, C đang `UP`.
    * Điều này giúp người dùng không gặp lỗi, ngay cả khi một phần hệ thống của bạn bị lỗi.

-----

### 4\. 🔒 Kiểm soát Chi tiết (Controlling Details)

Như bạn đã lưu ý, vì lý do bảo mật, Spring Boot mặc định **không hiển thị chi tiết** (show details) qua Web. Khi bạn gọi `/actuator/health`, bạn chỉ thấy:
`{"status": "UP"}`

Nó không cho bạn biết CSDL hay ổ đĩa đang `UP`, vì đây là thông tin nhạy cảm.

Để thay đổi hành vi này, bạn dùng thuộc tính trong `application.properties`:

`management.endpoint.health.show-details`
*(Thuộc tính `show-components` là một cách chi tiết hơn của `show-details`)*

Giá trị có thể là:

* `never` (Mặc định): Không bao giờ hiển thị chi tiết.
* `when-authorized` (Khuyên dùng): Chỉ hiển thị chi tiết nếu người dùng đã được xác thực (đăng nhập) và có vai trò (role) được chỉ định (ví dụ: `ACTUATOR_ADMIN`).
* `always` (Nguy hiểm ở production): Luôn luôn hiển thị chi tiết cho tất cả mọi người.

-----

### 5\. ✍️ Cách tạo `HealthIndicator` tùy chỉnh (Custom)

Nếu ứng dụng của bạn phụ thuộc vào một bên thứ 3 (ví dụ: một API Thời tiết) mà Spring Boot không có sẵn "bác sĩ" cho nó, bạn có thể **tự viết "bác sĩ chuyên khoa"** của riêng mình.

Cách làm chính xác như code của bạn:

1.  Tạo một `Bean` (thường là `@Component`).
2.  Implement interface `HealthIndicator`.
3.  Viết logic kiểm tra trong phương thức `health()`.
    * Nếu ổn, trả về `Health.up()...`.
    * Nếu có lỗi (ví dụ: `try-catch` bị lỗi), trả về `Health.down()...`.
    * Dùng `.withDetail(...)` để thêm thông tin tùy chỉnh.

<!-- end list -->

```java
@Component
public class MyApiHealthIndicator implements HealthIndicator {

    // (Bạn có thể tiêm RestTemplate vào đây để gọi API)

    @Override
    public Health health() {
        try {
            // ... (thử gọi API bên ngoài)
            // ... (nếu gọi thành công)
            return Health.up()
                .withDetail("api-status", "Đang hoạt động tốt")
                .build();

        } catch (Exception e) {
            // ... (nếu API bị lỗi)
            return Health.down()
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}
```

Spring Boot sẽ **tự động phát hiện** `Bean` này và thêm nó vào "danh sách bác sĩ" (`HealthIndicatorRegistry`) để cùng kiểm tra.

----

## ❓ Câu hỏi: What are the Health Indicators that are provided out of the box?

(Các Health Indicator nào được cung cấp sẵn?)

### 1. 🪄 Cơ chế Hoạt động: `@ConditionalOnClass`

Điều quan trọng nhất cần hiểu là Spring Boot **không** kích hoạt tất cả các `HealthIndicator` (Bác sĩ chuyên khoa) này cùng một lúc.

Thay vào đó, nó sử dụng **Autoconfiguration** (Tự động cấu hình) để "quan sát" `classpath` của bạn:

> **"NẾU (IF)** tôi thấy bạn đã thêm `spring-boot-starter-data-redis` vào `pom.xml`...
> **THÌ (THEN)** tôi đoán là bạn phụ thuộc vào Redis, vì vậy tôi sẽ **tự động kích hoạt** `RedisHealthIndicator` và thêm nó vào quy trình kiểm tra sức khỏe."

Nếu bạn không dùng Redis, `RedisHealthIndicator` sẽ không bao giờ được tạo ra.

---

### 2. 👨‍⚕️ Các "Bác sĩ Chuyên khoa" Chính (Phân loại)

Dựa trên danh sách rất đầy đủ của bạn, chúng ta có thể nhóm các `HealthIndicator` được cung cấp sẵn (out-of-the-box) theo chuyên môn của chúng:

#### 🖥️ Hệ thống Chung (General System)
Đây là các "bác sĩ" kiểm tra tình trạng cơ bản của máy chủ:

* **`ApplicationHealthIndicator`**: (Mặc định) "Bác sĩ tổng quát". Chỉ báo `UP`, trừ khi có lỗi khởi động.
* **`DiskSpaceHealthIndicator`**: 🩺 Kiểm tra dung lượng ổ đĩa. Sẽ báo `DOWN` nếu dung lượng trống quá thấp.

#### 🗃️ CSDL SQL (SQL Databases)
* **`DataSourceHealthIndicator`**: 🩺 Kiểm tra "sức khỏe" của `DataSource` (thường là một connection pool). Nó sẽ thử lấy một kết nối và (tùy chọn) chạy một câu query xác thực đơn giản (ví dụ: `SELECT 1`).

#### 🗄️ CSDL NoSQL (NoSQL Databases)
* **`MongoHealthIndicator`**: 🩺 Kiểm tra kết nối tới server MongoDB.
* **`RedisHealthIndicator`**: 🩺 Kiểm tra kết nối tới server Redis.
* **`CassandraHealthIndicator`**: 🩺 Kiểm tra kết nối tới cụm Cassandra.
* **`CouchbaseHealthIndicator`**: 🩺 Kiểm tra kết nối tới cụm Couchbase.
* **`ElasticsearchHealthIndicator`**: 🩺 Kiểm tra kết nối tới cụm Elasticsearch.
* **`InfluxDbHealthIndicator`**: 🩺 Kiểm tra kết nối tới server InfluxDB.
* **`Neo4jHealthIndicator`**: 🩺 Kiểm tra kết nối tới server Neo4j (thường bằng cách chạy một câu Cypher đơn giản).

#### ✉️ Nhắn tin & Email (Messaging & Mail)
* **`JmsHealthIndicator`**: 🩺 Kiểm tra kết nối tới JMS Broker (như ActiveMQ).
* **`RabbitHealthIndicator`**: 🩺 Kiểm tra kết nối tới server RabbitMQ.
* **`MailHealthIndicator`**: 🩺 Kiểm tra kết nối tới server mail (SMTP) (nếu bạn cấu hình `spring-boot-starter-mail`).

#### ⚡ Tìm kiếm & Cache (Search & Caching)
* **`SolrHealthIndicator`**: 🩺 Kiểm tra kết nối tới server Apache Solr.
* **`HazelcastHealthIndicator`**: 🩺 Kiểm tra kết nối tới cụm Hazelcast.

---

### 3. 💨 Phiên bản "Reactive" (Dành cho WebFlux)

Như bạn đã lưu ý, nếu Spring Boot phát hiện bạn đang dùng ứng dụng **Reactive** (với Spring WebFlux), nó sẽ ưu tiên sử dụng các `HealthIndicator` phiên bản "reactive" (không-blocking) nếu có thể:

* `MongoReactiveHealthIndicator`
* `RedisReactiveHealthIndicator`
* `CassandraReactiveHealthIndicator`
* `CouchbaseReactiveHealthIndicator`

**Tóm lại:**
Bằng cách tự động kích hoạt các "bác sĩ" này dựa trên `dependency` của bạn, endpoint `/actuator/health` ngay lập tức trở nên vô cùng mạnh mẽ. Nó tự động tổng hợp (aggregates) trạng thái của **tất cả các thành phần** mà ứng dụng của bạn phụ thuộc vào, cho bạn một cái nhìn tổng quan chính xác về "sức khỏe" của hệ thống.

----

## ❓ Câu hỏi: What is the Health Indicator status?

(Trạng thái của Health Indicator là gì?)

### 1. 🚦 Status là gì?

**Status (Trạng thái)** là "tín hiệu đèn giao thông" mà mỗi `HealthIndicator` ("bác sĩ chuyên khoa") trả về. Nó báo cáo nhanh cho Spring Actuator biết tình trạng của thành phần mà nó đang giám sát.

Mỗi `HealthIndicator` khi "khám" xong (ví dụ: `DataSourceHealthIndicator` kiểm tra CSDL) phải trả về một trong các trạng thái này.

---

### 2. Các Trạng thái (Status) Tích hợp sẵn

Như bạn đã liệt kê, Micrometer (thư viện mà Actuator sửng dụng) định nghĩa một số trạng thái tiêu chuẩn:

* **`UP`** (Xanh 💚)
    * **Ý nghĩa:** Thành phần đang hoạt động bình thường, khỏe mạnh.
    * **Ví dụ:** CSDL đã kết nối thành công.

* **`DOWN`** (Đỏ ❤️)
    * **Ý nghĩa:** Thành phần đã **gặp lỗi nghiêm trọng** và không hoạt động. Đây là trạng thái tồi tệ nhất.
    * **Ví dụ:** Không thể kết nối tới CSDL, mật khẩu sai, server CSDL bị sập.

* **`OUT_OF_SERVICE`** (Vàng 💛)
    * **Ý nghĩa:** Thành phần vẫn hoạt động, nhưng đang ở trạng thái "không sẵn sàng phục vụ" (ví dụ: đang bảo trì, sắp tắt...). Đây là một trạng thái "tạm thời".
    * **Ví dụ:** Một kết nối tới dịch vụ bên ngoài đang tạm thời bị ngắt để bảo trì theo lịch.

* **`UNKNOWN`** (Xám 🔘)
    * **Ý nghĩa:** Không rõ trạng thái. "Bác sĩ" không thể xác định được là `UP` hay `DOWN`.
    * **Ví dụ:** Một câu query kiểm tra sức khỏe trả về một kết quả lạ, không mong đợi.

* **Custom (Tùy chỉnh):**
    * Bạn cũng có thể tự định nghĩa các trạng thái của riêng mình (ví dụ: `WARNING`), nhưng 2 trạng thái `UP` và `DOWN` là phổ biến nhất.

---

### 3. ⚖️ Làm thế nào để ra quyết định cuối cùng? (HealthAggregator)

Đây là phần quan trọng mà bạn đã đề cập. Ứng dụng của bạn có thể có 5 "bác sĩ" (`HealthIndicator`):

1.  CSDL: `UP`
2.  Ổ đĩa: `UP`
3.  Redis: `UP`
4.  RabbitMQ: **`OUT_OF_SERVICE`**
5.  API bên ngoài: **`DOWN`**

Vậy, trạng thái cuối cùng của `/actuator/health` là gì?

Nó được quyết định bởi **`HealthAggregator`** (Bộ tổng hợp sức khỏe). Mặc định, Spring Boot dùng `OrderedHealthAggregator`.

#### `OrderedHealthAggregator` (Bộ tổng hợp theo thứ tự ưu tiên)

Bộ tổng hợp này hoạt động giống như một "hệ thống phân loại rủi ro". Nó định nghĩa một **thứ tự ưu tiên (sort order)** cho các trạng thái:

**`DOWN` > `OUT_OF_SERVICE` > `UP` > `UNKNOWN`**

*Nó hoạt động như sau:*

1.  Nó thu thập tất cả các trạng thái từ các "bác sĩ" (CSDL: `UP`, Ổ đĩa: `UP`, Redis: `UP`, RabbitMQ: `OUT_OF_SERVICE`, API: `DOWN`).
2.  Nó sắp xếp danh sách này theo thứ tự ưu tiên ở trên.
3.  Danh sách sau khi sắp xếp sẽ là: `[DOWN, OUT_OF_SERVICE, UP, UP, UP, UNKNOWN]` (nếu có `UNKNOWN`).
4.  Nó **chỉ lấy trạng thái đầu tiên** (trạng thái có mức độ ưu tiên cao nhất/nghiêm trọng nhất) trong danh sách đã sắp xếp.
5.  Trong ví dụ này, trạng thái đầu tiên là **`DOWN`**.

**Kết quả:** Endpoint `/actuator/health` sẽ trả về `{"status": "DOWN"}`.

> **Quy tắc dễ nhớ:** Chỉ cần **một (1)** thành phần bị `DOWN`, toàn bộ hệ thống sẽ bị coi là `DOWN`. Chỉ khi **tất cả** đều `UP`, hệ thống mới là `UP`.

----

## ❓ Câu hỏi: What are the Health Indicator Statuses that are provided out of the box

(Các Trạng thái (Status) của Health Indicator nào được cung cấp sẵn?)

Spring Boot (thông qua Micrometer) cung cấp 4 trạng thái tiêu chuẩn "out-of-the-box" (có sẵn) để các `HealthIndicator` ("bác sĩ chuyên khoa") sử dụng.

### 1\. 🚦 Các Trạng thái (Status)

* **`UP`** (Xanh 💚)

    * **Ý nghĩa:** Thành phần đang hoạt động bình thường, khỏe mạnh như mong đợi.
    * **Ví dụ:** Kết nối CSDL thành công, ổ đĩa còn nhiều dung lượng.

* **`DOWN`** (Đỏ ❤️)

    * **Ý nghĩa:** Thành phần đã gặp **lỗi nghiêm trọng, không mong muốn**. Nó đã bị "hỏng".
    * **Ví dụ:** Mật khẩu CSDL sai, server CSDL bị sập, không thể kết nối.
    * **Hệ quả:** Đây là trạng thái có độ ưu tiên cao nhất. Nếu dù chỉ một thành phần báo `DOWN`, trạng thái tổng của `/actuator/health` cũng sẽ là `DOWN`.

* **`OUT_OF_SERVICE`** (Vàng 💛)

    * **Ý nghĩa:** Thành phần đã được **chủ động đưa ra khỏi dịch vụ** (ví dụ: để bảo trì). Nó không bị "hỏng", nhưng nó không nên được sử dụng ngay lúc này.
    * **Ví dụ:** Bạn có thể tạo một custom `HealthIndicator` để báo `OUT_OF_SERVICE` khi bạn bật "chế độ bảo trì" (maintenance mode) cho một API bên ngoài.
    * **Hệ quả:** Trạng thái này cũng được coi là lỗi, nhưng ít nghiêm trọng hơn `DOWN`. Nếu không có gì `DOWN` nhưng có một thứ `OUT_OF_SERVICE`, trạng thái tổng sẽ là `OUT_OF_SERVICE`.

* **`UNKNOWN`** (Xám 🔘)

    * **Ý nghĩa:** Trạng thái không xác định. "Bác sĩ" không thể kết luận là `UP` hay `DOWN`.
    * **Ví dụ:** Một câu query kiểm tra sức khỏe trả về một mã lạ, không lường trước được.

-----

### 2\. ↔️ Ánh xạ sang Mã Trạng thái HTTP (HTTP Status Mapping)

Đây là một điểm rất quan trọng\! Khi bạn gọi `GET /actuator/health`, trạng thái tổng (`UP`, `DOWN`...) được ánh xạ thành một mã trạng thái HTTP để các hệ thống giám sát (monitoring systems) có thể hiểu được.

Spring Boot sử dụng một `HealthStatusHttpMapper` để thực hiện việc này. Mặc định, ánh xạ này như sau:

| Trạng thái (Status) | Mã HTTP | Ý nghĩa |
| :--- | :--- | :--- |
| **`UP`** | **`200 OK`** | Mọi thứ đều ổn. |
| **`UNKNOWN`** | **`200 OK`** | Vẫn được coi là "ổn" (không có lỗi nghiêm trọng). |
| **`DOWN`** | **`503 Service Unavailable`** | Dịch vụ đang tạm thời không có sẵn (do lỗi). |
| **`OUT_OF_SERVICE`** | **`503 Service Unavailable`** | Dịch vụ đang tạm thời không có sẵn (do chủ động). |

**Tại sao `503`?**
Mã `503 Service Unavailable` (Dịch vụ Không có sẵn) là một lựa chọn rất thông minh. Nó báo cho các Bộ cân bằng tải (Load Balancer) và client biết rằng: "Tôi vẫn còn sống, nhưng hiện tại tôi **không thể xử lý request** của bạn. Hãy thử lại sau hoặc chuyển sang một server khác." Điều này tốt hơn nhiều so với `500 Internal Server Error` (Lỗi máy chủ nội bộ).

-----

### 3\. ⚙️ Tùy chỉnh (Customizing) Ánh xạ

Như bạn đã nói, nếu bạn không thích ánh xạ mặc định này (ví dụ: công ty của bạn quy ước rằng `OUT_OF_SERVICE` phải trả về mã `501`), bạn có thể dễ dàng thay đổi nó trong `application.properties`:

```properties
# Thay đổi mã HTTP cho các trạng thái cụ thể
management.health.status.http-mapping.DOWN=500
management.health.status.http-mapping.OUT_OF_SERVICE=501
```

----

## ❓ Câu hỏi: How do you change the Health Indicator status severity order?

(Làm thế nào để bạn thay đổi thứ tự ưu tiên (severity order) của Trạng thái Health Indicator?)

### 1\. "Severity Order" (Thứ tự Ưu tiên) là gì?

Như chúng ta đã thảo luận, Spring Boot dùng một `OrderedHealthAggregator` (Bộ tổng hợp theo thứ tự) để quyết định trạng thái "tổng" (final status) cho endpoint `/actuator/health`.

"Severity Order" chính là **danh sách ưu tiên (priority list)** mà bộ tổng hợp này sử dụng.

**Thứ tự ưu tiên mặc định (default) là:**
`DOWN`, `OUT_OF_SERVICE`, `UP`, `UNKNOWN`

Điều này có nghĩa là `DOWN` là nghiêm trọng nhất (ưu tiên cao nhất) và `UNKNOWN` là ít nghiêm trọng nhất (ưu tiên thấp nhất). Khi tổng hợp, Spring chỉ cần tìm trạng thái "nghiêm trọng nhất" xuất hiện trong danh sách và đó sẽ là kết quả cuối cùng.

### 2\. Khi nào cần thay đổi?

Bạn cần thay đổi thứ tự này khi bạn **tạo ra một Trạng thái (Status) tùy chỉnh (custom)** và bạn muốn nó được "ưu tiên" hơn các trạng thái mặc định.

**Ví dụ (như trong câu hỏi của bạn):**
Hãy tưởng tượng bạn tạo một `HealthIndicator` tùy chỉnh để kiểm tra một hệ thống thanh toán. Nếu hệ thống đó bị lỗi, bạn không chỉ muốn nó báo `DOWN`, mà bạn muốn nó báo một trạng thái tùy chỉnh, nghiêm trọng hơn, gọi là **`SYSTEM_HALTED`** (Hệ thống bị treo).

Nếu bạn không thay đổi thứ tự ưu tiên, Spring Boot sẽ không biết `SYSTEM_HALTED` là gì và có thể xếp nó vào sau `UNKNOWN`.

### 3\. ⚙️ Cách thay đổi (Giải pháp)

Chính xác như bạn đã nêu, bạn sử dụng thuộc tính (property) **`management.health.status.order`** trong file `application.properties` để định nghĩa lại *toàn bộ* danh sách ưu tiên.

**Ví dụ (từ câu trả lời của bạn):**

```properties
# Định nghĩa lại thứ tự ưu tiên, 
# đặt "SYSTEM_HALTED" (tùy chỉnh) lên làm nghiêm trọng nhất
management.health.status.order=SYSTEM_HALTED, DOWN, OUT_OF_SERVICE, UP, UNKNOWN
```

*(Lưu ý: Tên trạng thái tùy chỉnh thường là chữ hoa và dùng gạch dưới, Spring Boot sẽ tự động chuẩn hóa nó (ví dụ: `SYSTEM_HALTED` hoặc `system-halted` đều được).*

**Kết quả sau khi thay đổi:**
Bây giờ, "danh sách ưu tiên" mới của `OrderedHealthAggregator` là:
`SYSTEM_HALTED` \> `DOWN` \> `OUT_OF_SERVICE` \> `UP` \> `UNKNOWN`

Nếu hệ thống của bạn có:

* CSDL: `UP`
* Ổ đĩa: `UP`
* Hệ thống thanh toán (tùy chỉnh): **`SYSTEM_HALTED`**

Bộ tổng hợp (`Aggregator`) sẽ sắp xếp danh sách: `[SYSTEM_HALTED, UP, UP]` và lấy trạng thái đầu tiên.

**Kết quả cuối cùng:** `GET /actuator/health` sẽ trả về `{"status": "SYSTEM_HALTED"}`. Điều này cho phép các hệ thống giám sát của bạn biết chính xác mức độ nghiêm trọng của vấn đề.

-----

## ❓ Câu hỏi: Why do you want to leverage 3rd party external monitoring system?

(Tại sao bạn muốn tận dụng (leverage) một hệ thống giám sát bên thứ 3?)

### 1\. 🚗 Phép so sánh: "Cổng chẩn đoán" vs. "Gara sửa chữa"

Hãy nghĩ về **Spring Boot Actuator** (cụ thể là endpoint `/actuator/metrics`) giống như **cổng chẩn đoán (OBD-II port)** trên chiếc ô tô của bạn.

* **Actuator (`/metrics`):** Nó phơi bày (expose) hàng trăm điểm dữ liệu *thời gian thực* (real-time): tốc độ động cơ, nhiệt độ dầu, áp suất lốp, v.v. Nó **không lưu trữ** lịch sử, **không vẽ biểu đồ**, và **không tự gọi** cho bạn nếu có gì đó hỏng. Nó chỉ *cung cấp dữ liệu* cho bất cứ ai cắm vào.

* **Hệ thống Giám sát Bên ngoài (External Monitoring System):** (Ví dụ: **Prometheus, Datadog, Grafana**)

    * Đây chính là **"Gara sửa chữa"** hoặc **"Trung tâm điều hành cuộc đua" (Telemetry Center)**.
    * Họ cắm máy chẩn đoán chuyên dụng của mình vào "cổng" Actuator của bạn.
    * Tại sao bạn cần họ? Vì họ cung cấp tất cả những thứ mà Actuator *không* làm, như bạn đã liệt kê:

-----

### 2\. 🌟 Năm (5) Giá trị cốt lõi của Hệ thống Giám sát Bên ngoài

1.  **🗄️ Lưu trữ Lâu dài (Durable Persistent Storage)**

    * **Actuator:** Chỉ cho bạn biết `jvm.memory.used` (bộ nhớ) *ngay bâyC giờ*.
    * **Hệ thống bên ngoài:** Ghi lại giá trị này *mỗi 15 giây*. Điều này cực kỳ quan trọng để trả lời các câu hỏi như: "Bộ nhớ đã tăng đột biến lúc 3 giờ sáng thứ Ba tuần trước là do đâu?" hoặc "Bộ nhớ có đang bị rò rỉ (memory leak) theo thời gian không?"

2.  **📊 Trực quan hóa & Bảng điều khiển (Data Visualization & Dashboards)**

    * **Actuator:** Trả về một file `JSON` khổng lồ, khó đọc.
    * **Hệ thống bên ngoài:** Cung cấp các công cụ (như **Grafana**) để biến đống JSON đó thành các **biểu đồ (graphs)** đẹp, dễ hiểu. Bạn có thể thấy ngay "nhịp tim" (CPU, Memory, Request) của ứng dụng trên một màn hình duy nhất.

3.  **🔔 Cảnh báo (Alerting)**

    * **Actuator:** Không biết khi nào có lỗi.
    * **Hệ thống bên ngoài:** Bạn có thể thiết lập các "luật" (rules). Ví dụ: "NẾU `http.server.requests` có `status=500` (lỗi server) xảy ra nhiều hơn 10 lần trong 1 phút, HÃY GỬI CẢNH BÁO PagerDuty/Slack ngay lập Tức\!"

4.  **📈 Khả năng Xử lý (Ingesting Massive Data)**

    * **Actuator:** Chỉ là 1 endpoint trên 1 ứng dụng.
    * **Hệ thống bên ngoài:** Được thiết kế để "cào" (scrape) hoặc nhận (ingest) dữ liệu từ *hàng trăm* hoặc *hàng ngàn* microservice cùng một lúc và tổng hợp chúng lại.

5.  **🔗 Khả năng Tương quan (Correlation)**

    * Các hệ thống nâng cao (như Datadog, New Relic) có thể liên kết (correlate) các *metrics* (số liệu) với *logs* (nhật ký) và *traces* (dấu vết request). Bạn có thể thấy một biểu đồ CPU tăng vọt, và nhấp vào đó để xem chính xác những dòng log nào đã được ghi và những request nào đã chạy *tại đúng thời điểm đó*.

-----

### 3\. 🔌 Spring Boot & Micrometer làm điều này dễ dàng như thế nào?

Đây là phần "phép thuật" của Spring Boot.

1.  **Actuator dùng Micrometer:** Spring Boot Actuator sử dụng một thư viện tên là **Micrometer** làm "bộ mặt" (facade) cho metrics (giống như SLF4J cho logging).
2.  **"Registry" là "Bộ chuyển đổi":** Micrometer có một khái niệm gọi là **`MeterRegistry`** ("Bộ đăng ký đồng hồ đo"). Bạn chỉ cần thêm "bộ chuyển đổi" (registry) cho hệ thống bạn muốn.
3.  **Cách làm (Như code của bạn):**
    * **Bạn muốn dùng Prometheus?** Chỉ cần thêm `micrometer-registry-prometheus`. Micrometer sẽ tự động tạo ra endpoint `/actuator/prometheus` với định dạng mà Prometheus hiểu được.
    * **Bạn muốn dùng Datadog/Elastic?** Chỉ cần thêm `micrometer-registry-datadog` hoặc `micrometer-registry-elastic`. Micrometer sẽ tự động "đẩy" (push) metrics đến máy chủ đó.
    * **Cấu hình:** Như bạn đã chỉ ra, bạn chỉ cần cung cấp địa chỉ (host) hoặc API key cho hệ thống "push":
      ```properties
      management.metrics.export.elastic.host=http://localhost:9200
      ```

Tóm lại, Spring Boot Actuator **tạo ra** dữ liệu, và Micrometer **chuẩn bị** nó để các hệ thống giám sát bên ngoài **tiêu thụ** (consume), lưu trữ, và cảnh báo.

----

## ❓ Câu hỏi: When do you want to use @SpringBootTest annotation?

(Khi nào bạn muốn sử dụng annotation @SpringBootTest?)

### 1\. 🎯 Câu trả lời: Dùng cho Kiểm thử Tích hợp (Integration Tests)

Bạn nên sử dụng `@SpringBootTest` bất cứ khi nào bạn muốn viết một **Integration Test (Kiểm thử Tích hợp)**, thay vì một **Unit Test (Kiểm thử Đơn vị)**.

Sự khác biệt là gì?

* **Unit Test (Không dùng `@SpringBootTest`):**

    * Giống như bạn kiểm tra một *linh kiện* (ví dụ: một cái bánh răng) một cách riêng lẻ.
    * Bạn tự tay `new MyService()` và gọi các phương thức của nó.
    * Nó **không** khởi động Spring, không có `ApplicationContext`, không có "dependency injection" (`@Autowired`).
    * **Ưu điểm:** Rất nhanh.

* **Integration Test (Dùng `@SpringBootTest`):**

    * Giống như bạn kiểm tra **cả cỗ máy** (hoặc một phần lớn của cỗ máy) xem các bánh răng có ăn khớp với nhau không.
    * `@SpringBootTest` sẽ **khởi động toàn bộ ứng dụng Spring Boot** của bạn.
    * Nó sẽ tạo ra `ApplicationContext` (container của Spring), tự động quét (scan) và tạo ra tất cả các `Bean` (`@Controller`, `@Service`, `@Repository`...) và "tiêm" (`@Autowired`) chúng lại với nhau, y hệt như khi bạn chạy ứng dụng thật.
    * **Ưu điểm:** Bạn có thể kiểm tra luồng (flow) hoàn chỉnh, ví dụ: "Khi tôi gọi đến `@Controller` này, nó có gọi đúng `@Service` và `@Service` đó có gọi đúng `@Repository` không?"
    * **Nhược điểm:** Chạy chậm hơn Unit Test đáng kể.

-----

### 2\. 🪄 Các tính năng chính (Tại sao nó hữu ích?)

Như bạn đã liệt kê, `@SpringBootTest` mang lại rất nhiều tính năng mạnh mẽ:

#### a. Tự động tạo Context (Automated Context creation)

Đây là tính năng cốt lõi. Nó sẽ tự động tìm lớp (`class`) có `@SpringBootApplication` của bạn và dùng nó để khởi động môi trường Spring.

#### b. Kiểm thử Môi trường Web (Web Environment)

`@SpringBootTest` cho phép bạn chọn cách bạn muốn kiểm thử lớp web:

* **`webEnvironment = MOCK` (Mặc định):**

    * Spring Boot sẽ tạo một môi trường web *giả lập (mock)*.
    * Nó **không** khởi động một server Tomcat/Jetty thật.
    * Bạn dùng một `Bean` đặc biệt tên là **`MockMvc`** để "bắn" các request giả lập vào `@Controller` của mình.

* **`webEnvironment = RANDOM_PORT`** hoặc **`DEFINED_PORT`**:

    * Spring Boot sẽ khởi động một **server Tomcat/Jetty nhúng thật** và lắng nghe ở một cổng (port) ngẫu nhiên (hoặc cổng bạn chỉ định).
    * Cách này dùng để kiểm thử *toàn bộ* "stack" HTTP.
    * Bạn dùng một `Bean` đặc biệt tên là **`TestRestTemplate`** để gọi các request HTTP *thật* (ví dụ: `http://localhost:54321/api/users`).

#### c. Giả lập Bean (`@MockBean` / `@SpyBean`)

Đây là tính năng **cực kỳ quan trọng** cho kiểm thử tích hợp.

* **`@MockBean`**:
    * **Tình huống:** Bạn muốn kiểm thử luồng `@Controller` -\> `@Service`, nhưng bạn **không** muốn `@Service` gọi xuống CSDL (`@Repository`) thật (vì nó chậm và phức tạp).
    * **Giải pháp:** Bạn dùng `@MockBean` để thay thế `Repository` `Bean` *thật* trong `ApplicationContext` bằng một `Repository` *giả (mock)*.
    * Bây giờ, khi `@Service` gọi `repository.save()`, nó sẽ gọi cái "giả" mà bạn hoàn toàn kiểm soát được.

#### d. Tùy chỉnh Context (`@TestConfiguration`)

Nếu bạn muốn thêm hoặc ghi đè (override) một `Bean` nào đó *chỉ* trong khi chạy test, bạn có thể tạo một lớp `@TestConfiguration` lồng (nested) bên trong.

-----

### 3\. 🏃 Ghi chú về JUnit 4 vs. JUnit 5

Câu trả lời của bạn đã phân biệt rất đúng về cách chạy:

* **JUnit 5 (Hiện đại, được khuyên dùng):**

    * Bạn không cần làm gì thêm. `@SpringBootTest` đã tự "gói" (meta-annotated) sẵn `@ExtendWith(SpringExtension.class)`.

  <!-- end list -->

  ```java
  @SpringBootTest
  class HelloControllerTest {
      // ...
  }
  ```

* **JUnit 4 (Cũ):**

    * Bạn **bắt buộc** phải thêm `@RunWith(SpringRunner.class)` để "nói" cho JUnit 4 biết cách khởi động Spring.

  <!-- end list -->

  ```java
  @RunWith(SpringRunner.class)
  @SpringBootTest
  public class HelloControllerTest {
      // ...
  }
  ```

-----

## ❓ Câu hỏi: What does @SpringBootTest auto-configure?

(@SpringBootTest tự động cấu hình những gì?)

`@SpringBootTest` là annotation (chú thích) dùng cho **Kiểm thử Tích hợp (Integration Test)**. Mục tiêu của nó là nạp (load) và tự động cấu hình **toàn bộ (full)** `ApplicationContext` của Spring Boot, gần giống hệt như khi bạn chạy ứng dụng thật.

### 1\. ⚙️ Nó tự động cấu hình những gì (Mặc định)?

Khi bạn thêm `@SpringBootTest` vào một lớp test, nó sẽ tự động:

1.  **Tìm Cấu hình Chính:** Nó tự động tìm (scan) lên các gói (package) cha để tìm lớp có
    `@SpringBootApplication` (hoặc `@SpringBootConfiguration`) của bạn.
2.  **Khởi động Toàn bộ `ApplicationContext`:**
    * Nó chạy toàn bộ cơ chế **Autoconfiguration**.
    * Nó thực hiện **Component Scanning** (dựa trên cấu hình `@ComponentScan` của bạn).
    * Nó tạo ra **TẤT CẢ** các `Bean` của bạn: `@Controller`, `@Service`, `@Repository`, `@Component`, `@Configuration`, v.v.
3.  **Tích hợp JUnit:** Nó tích hợp với JUnit (thông qua `@ExtendWith(SpringExtension.class)` đã được tích hợp sẵn trong `@SpringBootTest` cho JUnit 5), cho phép bạn `@Autowired` bất kỳ `Bean` nào (ví dụ: `MyService` của bạn) vào thẳng lớp test.
4.  **Cung cấp Công cụ Test:** Nó cung cấp các công cụ kiểm thử cực kỳ mạnh mẽ:
    * **`@MockBean`**: Cho phép bạn thay thế một `Bean` thật (ví dụ: `MyRepository`) bằng một `Bean` giả (Mock) trong `ApplicationContext`.
    * **`@SpyBean`**: Tương tự `@MockBean` nhưng cho phép bạn "theo dõi" (spy) một `Bean` thật.
    * **`TestRestTemplate`**: Một `RestTemplate` đã được cấu hình sẵn để gọi các request HTTP đến server *thật* (nếu bạn bật `webEnvironment`).

**Ví dụ (Full Integration Test):**

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MyFullApplicationTest {

    // (1) Nạp (load) toàn bộ ApplicationContext

    // (2) @Autowired hoạt động vì Context đã chạy
    @Autowired 
    private MyController controller; 

    @Autowired
    private MyService service;

    // (3) Cung cấp một client HTTP để gọi server thật
    @Autowired
    private TestRestTemplate restTemplate; 

    @Test
    void testFullFlow() {
        // (4) Bạn có thể gọi server thật đang chạy ở cổng ngẫu nhiên
        ResponseEntity<String> response = restTemplate.getForEntity("/api/hello", String.class);
        
        // (5) Bạn cũng có thể kiểm tra các bean nội bộ
        assert(service.someMethod() == true);
    }
}
```

-----

### 2\. ⚠️ Phân biệt: `@SpringBootTest` vs. "Test Slices" (Rất quan trọng)

Câu trả lời của bạn đã liệt kê rất chính xác các annotation "Test Slice". Đây **không phải** là thứ mà `@SpringBootTest` tự cấu hình, mà chúng là các **giải pháp thay thế** nhẹ ký hơn.

| So sánh | `@SpringBootTest` (Mặc định) | `@WebMvcTest` (Một "Lát cắt") |
| :--- | :--- | :--- |
| **Mục đích** | Kiểm thử tích hợp **Toàn bộ** ứng dụng. | Kiểm thử **Chỉ lớp Web** (Controller). |
| **`ApplicationContext`** | Nạp **TẤT CẢ** các Bean. | **CHỈ** nạp các Bean liên quan đến Web (`@Controller`, `Filter`, `Converter`...). |
| **Các lớp khác** | `@Service`, `@Repository`... **ĐƯỢC** nạp. | `@Service`, `@Repository`... **KHÔNG** được nạp (Bạn phải `@MockBean` chúng). |
| **Công cụ (Tool)** | `TestRestTemplate` (nếu có server) hoặc `MockMvc`. | Tự động cấu hình **`MockMvc`**. |
| **Tốc độ** | **Chậm** (vì khởi động mọi thứ). | **Nhanh** (vì chỉ khởi động một phần). |

-----

### 3\. 🍰 Các "Lát cắt" (Slices) Auto-configure

Như bạn đã liệt kê, Spring Boot cung cấp các annotation "lát cắt" này để bạn không cần dùng `@SpringBootTest` cho mọi thứ. Mỗi annotation này tự động nạp (auto-configure) một "lát cắt" rất nhỏ của ứng dụng:

* **`@WebMvcTest(MyController.class)`**:

    * Chỉ nạp "lát cắt" Spring MVC.
    * Tự động cấu hình `MockMvc`.
    * **Không** nạp `@Service` hay `@Repository`.

* **`@DataJpaTest`**:

    * Chỉ nạp "lát cắt" Spring Data JPA.
    * Tự động cấu hình `EntityManager`, `TestEntityManager`, và tất cả các `@Repository` của bạn.
    * Mặc định, chạy trên CSDL H2 (in-memory) và tự động `rollback` transaction sau mỗi test.
    * **Không** nạp `@Service` hay `@Controller`.

* **`@JsonTest`**:

    * Chỉ nạp "lát cắt" xử lý JSON (Jackson).
    * Tự động cấu hình `JacksonTester`.
    * Dùng để kiểm tra xem đối tượng (POJO) của bạn có được serialize/deserialize ra JSON đúng như mong đợi không.

* **`@RestClientTest`**:

    * Chỉ nạp "lát cắt" `RestTemplate`.
    * Tự động cấu hình `MockRestServiceServer`.
    * Dùng để kiểm tra *client* (ví dụ: `Service` của bạn có gọi API bên ngoài đúng cách không).

**Kết luận:**
Bạn dùng **`@SpringBootTest`** khi bạn muốn kiểm tra sự tích hợp của *toàn bộ* ứng dụng.
Bạn dùng các "lát cắt" (như **`@WebMvcTest`**, **`@DataJpaTest`**) khi bạn muốn kiểm tra *chỉ một lớp* (layer) cụ thể một cách nhanh chóng và cô lập (isolated).

----

## ❓ Câu hỏi: What dependencies does spring-boot-starter-test bring to the classpath?

(`spring-boot-starter-test` mang theo những dependency nào vào classpath?)

`spring-boot-starter-test` là một "starter" tổng hợp (meta-starter). Nó không chứa code, mà chỉ là một "danh sách mua sắm" kéo theo tất cả các thư viện bạn cần để viết các bài kiểm thử (test) từ Unit Test đơn giản đến Integration Test phức tạp.

Dưới đây là vai trò của từng "món" chính trong danh sách đó:

### 1. 🏃 Khung sườn Chạy Test (Test Runner)

* **JUnit (hiện tại là JUnit 5 / Jupiter):**
    * **Vai trò:** Đây là "sàn đấu" hay "khung sườn" (framework) cơ bản nhất. Nó cung cấp các annotation `@Test`, `@BeforeEach`, `@AfterEach`... để định nghĩa và chạy các bài test.

### 2. 🧩 Tích hợp Spring (Spring Integration)

* **Spring Test:**
    * **Vai trò:** Cung cấp "cầu nối" giữa JUnit và Spring. Nó cung cấp các tính năng cốt lõi như `@ContextConfiguration` (để nạp `ApplicationContext`), `@ExtendWith(SpringExtension.class)` (để tích hợp JUnit 5), và `MockMvc`.
* **Spring Boot Test:**
    * **Vai trò:** Cung cấp các tính năng "ma thuật" dành riêng cho Spring Boot. Quan trọng nhất là annotation `@SpringBootTest` (để khởi động toàn bộ ứng dụng) và các "lát cắt" (slices) như `@WebMvcTest`, `@DataJpaTest`.

### 3. 🎭 Giả lập (Mocking)

* **Mockito:**
    * **Vai trò:** Đây là thư viện giả lập (mocking) phổ biến nhất. Nó cho phép bạn tạo ra các đối tượng "giả" (mock) của các dependency.
    * **Ví dụ:** Khi kiểm thử `MyService`, bạn có thể "giả lập" `MyRepository` để nó trả về dữ liệu giả, giúp bạn kiểm thử `MyService` một cách cô lập (isolated) mà không cần kết nối CSDL thật. Đây là thư viện cung cấp `@MockBean`.

### 4. ✅ Khẳng định (Assertion)

* **AssertJ:**
    * **Vai trò:** Một thư viện "khẳng định" (assertion) rất mạnh mẽ. Nó cung cấp các câu lệnh kiểm tra (ví dụ: `assertThat(...)`) với cú pháp "fluent" (trôi chảy) và rất dễ đọc.
    * **Ví dụ:** Thay vì viết `assertEquals(10, result.size())`, bạn viết:
      `assertThat(result).hasSize(10).contains("foo");`
* **Hamcrest:**
    * **Vai trò:** Một thư viện khác cung cấp các "bộ so khớp" (matchers). Nó thường được dùng ngầm (implicitly) bởi các thư viện khác (như Mockito) để định nghĩa các đối số linh hoạt (ví dụ: `when(myRepo.findById(anyInt()))...`).

### 5. 🔬 Công cụ Chuyên dụng (JSON / XML)

* **JSONassert:**
    * **Vai trò:** Chuyên dùng để "khẳng định" các chuỗi JSON. Nó rất thông minh, có thể bỏ qua thứ tự các trường (field order) hoặc khoảng trắng (whitespace) không quan trọng.
    * **Ví dụ:** `assertJsonEquals("{\"id\":1}", responseBody);`
* **JsonPath:**
    * **Vai trò:** Giống như XPath cho XML, đây là "XPath cho JSON". Nó cho phép bạn truy vấn các giá trị cụ thể bên trong một chuỗi JSON phức tạp.
    * **Ví dụ:** `jsonPath("$.user.name").isEqualTo("John")` (Kiểm tra xem trường `name` bên trong đối tượng `user` có phải là "John" không).
* **XMLUnit:**
    * **Vai trò:** Tương tự JSONassert, nhưng dùng để so sánh và "khẳng định" các tài liệu XML.

Tóm lại, `spring-boot-starter-test` cung cấp cho bạn một bộ công cụ đầy đủ: một **khung sườn** (JUnit), **chất kết dính** (Spring Test), **diễn viên đóng thế** (Mockito), **trọng tài** (AssertJ), và các **chuyên gia** (JSONassert, JsonPath).

----

## ❓ Câu hỏi: How do you perform integration testing with @SpringBootTest for a web application?

(Làm thế nào để bạn thực hiện kiểm thử tích hợp với @SpringBootTest cho một ứng dụng web?)

Khi bạn sử dụng **`@SpringBootTest`**, bạn đang khởi động toàn bộ "cỗ máy" Spring (`ApplicationContext`). Để kiểm thử lớp web (các `@Controller` của bạn), bạn có hai lựa chọn chính, tùy thuộc vào việc bạn muốn "thật" đến mức nào:

1.  **Cách 1: Giả lập Server (Mock Server) 🤖** - Dùng `MockMvc`
2.  **Cách 2: Chạy Server thật (Real Server) 🚗** - Dùng `TestRestTemplate`

-----

### 1\. 🖥️ Cách 1: Dùng `MockMvc` (Giả lập Server)

Đây là cách phổ biến nhất. Nó **KHÔNG** khởi động một server Tomcat/Jetty thật. Thay vào đó, nó "giả lập" (mock) môi trường Spring MVC, cho phép bạn "bắn" các request giả vào các `Controller` của mình mà không cần đến network (mạng).

* **Cách thiết lập:** Bạn dùng `@SpringBootTest` (mặc định) và thêm **`@AutoConfigureMockMvc`**.
* **Công cụ chính:** Spring sẽ tự động tiêm (inject) một `Bean` tên là **`MockMvc`** cho bạn.

<!-- end list -->

```java
// Dùng JUnit 5 (mặc định với Spring Boot), không cần @RunWith
@SpringBootTest
@AutoConfigureMockMvc // Báo Spring Boot: "Hãy cấu hình một MockMvc cho tôi"
public class CityControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc; // Tiêm (inject) công cụ giả lập

    @MockBean // (Optional) Giả lập lớp service, không gọi xuống DB thật
    private CityService cityService;

    @Test
    public void shouldReturnCities() throws Exception {
        // Giả lập: "Khi ai đó gọi cityService.getAll(), trả về..."
        when(cityService.getAll()).thenReturn(List.of(new City("Hanoi")));

        // Thực hiện một request GET giả lập
        mockMvc.perform(get("/api/cities"))
               .andExpect(status().isOk()) // Mong đợi HTTP 200 OK
               .andExpect(jsonPath("$[0].name").value("Hanoi")); // Kiểm tra nội dung JSON
    }
}
```

* **Ưu điểm:** Nhanh hơn vì không khởi động server thật. Dễ dàng kiểm tra các logic phức tạp (như JSON path, status code) bằng cú pháp `andExpect()`.
* **Nhược điểm:** Không kiểm thử *toàn bộ* "stack" (ví dụ: các `Filter`, network serialization).

-----

### 2\. 🚗 Cách 2: Dùng `TestRestTemplate` (Server thật)

Cách này sẽ khởi động một **server Tomcat/Jetty nhúng thật** trên một cổng (port) ngẫu nhiên. Sau đó, bạn dùng một HTTP client thật (`TestRestTemplate`) để "gọi" vào ứng dụng của mình, y hệt như một trình duyệt hoặc một client bên ngoài.

* **Cách thiết lập:** Bạn phải chỉ định `webEnvironment` là `RANDOM_PORT` (hoặc `DEFINED_PORT`).
* **Công cụ chính:** Spring sẽ tiêm (inject) **`TestRestTemplate`** (client) và **`@LocalServerPort`** (để biết server chạy ở port nào).

<!-- end list -->

```java
// Dùng JUnit 5
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // BÁO ĐỘNG: "Hãy khởi động một server THẬT ở cổng ngẫu nhiên"
public class CityControllerRealServerTest {

    @Autowired
    private TestRestTemplate restTemplate; // Tiêm (inject) client HTTP

    @LocalServerPort
    private int port; // Lấy cổng ngẫu nhiên mà server đang chạy

    @MockBean
    private CityService cityService;

    @Test
    public void shouldReturnCities() throws Exception {
        // Giả lập service (vẫn có thể dùng @MockBean ở đây)
        when(cityService.getAll()).thenReturn(List.of(new City("Hanoi")));
        
        // Tạo URL thật, trỏ đến server thật đang chạy
        String url = "http://localhost:" + port + "/api/cities";

        // Gửi một request GET THẬT qua mạng (network)
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Kiểm tra kết quả
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Hanoi");
    }
}
```

* **Ưu điểm:** Kiểm thử **thực tế nhất**. Nó kiểm tra *toàn bộ* stack, từ network, qua server, `Filter`, `Controller`, và ngược lại.
* **Nhược điểm:** Chạy **chậm hơn** `MockMvc` vì phải khởi động cả một server.

-----

### 💡 Ghi chú về `@MockBean` (Như bạn đã đề cập)

Cả hai phương pháp trên đều là **Integration Test** (Kiểm thử Tích hợp). Tuy nhiên, như bạn đã định nghĩa, bạn có thể muốn "giới hạn" phạm vi tích hợp.

Bạn có thể dùng `@SpringBootTest` để nạp (load) `@Controller` và `@Service`, nhưng bạn **không** muốn nó kết nối đến CSDL **thật**.

Lúc này, **`@MockBean`** là công cụ hoàn hảo. Nó sẽ "tìm và thay thế" `Bean` thật (vídụ: `MyRepository`) trong `ApplicationContext` bằng một đối tượng "giả" (mock) mà bạn có thể kiểm soát (như trong cả hai ví dụ trên).

### Tóm tắt

| Phương pháp | **`@AutoConfigureMockMvc`** | **`webEnvironment = RANDOM_PORT`** |
| :--- | :--- | :--- |
| **Server** | Giả lập (Mock) | Thật (Real Embedded Tomcat/Jetty) |
| **Network** | Không, gọi trực tiếp Controller | Có, gọi qua `localhost:port` |
| **Công cụ chính** | `MockMvc` | `TestRestTemplate` |
| **Tốc độ** | Nhanh | Chậm |
| **Độ thực tế** | 90% (Bỏ qua Network/Filter stack) | 100% (Kiểm thử toàn bộ stack) |

-----

## ❓ Câu hỏi: When do you want to use @WebMvcTest? What does it auto-configure?

(Khi nào bạn muốn sử dụng @WebMvcTest? Nó tự động cấu hình những gì?)

### 1\. 🍰 @WebMvcTest dùng để làm gì? (Kiểm thử "Lát cắt")

Bạn nên sử dụng **`@WebMvcTest`** khi bạn chỉ muốn kiểm thử **duy nhất lớp Web (Web Layer)** (chính là các `@Controller` hoặc `@RestController` của bạn) một cách **cô lập (isolated)**.

Hãy tưởng tượng ứng dụng của bạn là một chiếc bánh 3 lớp:

1.  **Lớp Web (Controller)** - Xử lý HTTP Request/Response.
2.  **Lớp Logic (Service)** - Xử lý nghiệp vụ.
3.  **Lớp Dữ liệu (Repository)** - Nói chuyện với CSDL.

Khi bạn dùng **`@SpringBootTest`** (kiểm thử tích hợp đầy đủ), bạn đang nạp (load) **cả 3 lớp** của chiếc bánh. Việc này rất chậm và phức tạp.

Khi bạn dùng **`@WebMvcTest`**, bạn chỉ "cắt" (slice) **duy nhất lớp 1 (Lớp Web)** để kiểm thử.

Nó **sẽ KHÔNG nạp (load)** các `Bean` `@Service`, `@Repository`, hay `@Component` khác. Điều này làm cho bài test của bạn **nhanh hơn rất nhiều**.

-----

### 2\. 🪄 @WebMvcTest tự động cấu hình những gì?

Như bạn đã nói, thay vì nạp toàn bộ `ApplicationContext`, `@WebMvcTest` chỉ nạp một "lát cắt" chứa các `Bean` cần thiết cho việc xử lý Web. Danh sách của bạn rất chính xác:

* **`MockMvc` (Quan trọng nhất):**

    * Đây là công cụ chính. Nó cho phép bạn "giả lập" (mock) việc gửi các HTTP request (GET, POST...) đến `Controller` của bạn mà **không cần khởi động server** thật.

* **Các thành phần Web Infrastructure (Hạ tầng Web):**

    * **`@ControllerAdvice`**: Các `Bean` xử lý ngoại lệ (exception) toàn cục.
    * **`@JsonComponent`**: Các `Bean` tùy chỉnh việc serialize/deserialize JSON (Jackson).
    * **`Filter`**: Các `Bean` `Filter` (ví dụ: `CharacterEncodingFilter`).
    * **`WebMvcConfigurer`**: Các `Bean` `@Configuration` implement `WebMvcConfigurer` (nơi bạn tùy chỉnh MVC, ví dụ như CORS).
    * **`HandlerMethodArgumentResolver`**: Các `Bean` giúp giải quyết (resolve) các tham số (argument) đặc biệt cho phương thức controller.
    * **`Converter` / `GenericConverter`**: Các `Bean` dùng để chuyển đổi kiểu dữ liệu (ví dụ: `String` sang `LocalDate` trong `@RequestParam`).

-----

### 3\. ⛔ @WebMvcTest KHÔNG cấu hình những gì? (Rất quan trọng)

Đây chính là điểm mấu chốt. Vì nó là một "lát cắt", nó sẽ **KHÔNG** quét và **KHÔNG** tạo ra các `Bean`:

* `@Service`
* `@Repository`
* `@Component` (thông thường)

**Hệ quả:**
Nếu `Controller` của bạn có một `@Autowired` đến `MyService`, bài test `@WebMvcTest` sẽ **thất bại (fail)** khi khởi động, vì Spring không thể tìm thấy `Bean` `MyService`.

**Giải pháp (Như bạn đã nêu):**
Bạn phải **giả lập (mock)** `Service` đó bằng **`@MockBean`**.

-----

### 4\. 📖 Ví dụ thực tế

Đây là cách bạn kết hợp tất cả các khái niệm này:

```java
// Chỉ định rõ chúng ta chỉ test CityController
// Điều này giúp test chạy nhanh hơn nữa
@WebMvcTest(CityController.class) 
class CityControllerTest {

    // 1. (Được auto-configure) Tiêm (inject) công cụ MockMvc
    @Autowired
    private MockMvc mockMvc;

    // 2. (KHÔNG được nạp) Giả lập (mock) service
    //    Vì @WebMvcTest không nạp @Service
    @MockBean
    private CityService cityService;

    @Test
    void testGetCityByName() throws Exception {
        // 3. "Dạy" cho Mock Bean biết phải làm gì
        City hanoi = new City("Hanoi", 8_000_000);
        when(cityService.findByName("Hanoi")).thenReturn(hanoi);

        // 4. Thực hiện một request GET giả lập
        mockMvc.perform(get("/api/cities/Hanoi"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Hanoi"))
               .andExpect(jsonPath("$.population").value(8000000));
    }
}
```

Bài test này kiểm tra `Controller` (`CityController`), JSON serialization, và HTTP status một cách cô lập, nhanh chóng, mà không cần quan tâm đến `Service` hay CSDL thật.

-----

### 5\. Hai cách sử dụng (Như bạn đã đề cập)

1.  **`@WebMvcTest(CityController.class)` (Khuyên dùng):**
    * Cách làm tốt nhất. Chỉ nạp *một* controller bạn muốn test. Rất nhanh.
2.  **`@WebMvcTest` (Không có tham số):**
    * Nạp *tất cả* các `@Controller`, `@ControllerAdvice`... trong ứng dụng của bạn.
    * Chậm hơn, và có thể bạn sẽ phải `@MockBean` cho *rất nhiều* service.

----

## ❓ Câu hỏi: What are the differences between @MockBean and @Mock?

(Sự khác biệt giữa @MockBean và @Mock là gì?)

### Phép so sánh: "Trợ lý Cá nhân" vs. "Kẻ đóng thế"

Hãy tưởng tượng `ApplicationContext` (hệ thống của Spring) là một "công ty" lớn.

* **`@Mock` (Trợ lý Cá nhân):**

    * Bạn (người viết test) thuê một "trợ lý cá nhân" (đối tượng mock) để giúp bạn làm bài test.
    * "Trợ lý" này **chỉ làm việc cho bạn** trong lớp test.
    * **Không ai** trong "công ty" (`ApplicationContext`) biết "trợ lý" này là ai hay thậm chí là họ có tồn tại. Nó nằm hoàn toàn *bên ngoài* công ty.
    * Đây là **Unit Test** truyền thống.

* **`@MockBean` (Kẻ đóng thế):**

    * Bạn đi đến phòng nhân sự (Spring Boot) và nói: "Hãy tìm nhân viên thật tên là 'MyRepository'. Sa thải anh ta (chỉ trong bài test này thôi), và thuê **'kẻ đóng thế' (mock)** này vào đúng vị trí đó."
    * "Kẻ đóng thế" này giờ là một nhân viên *chính thức* trong `ApplicationContext`.
    * Khi `MyService` (một nhân viên thật khác) nói: "Tôi cần `@Autowired` 'MyRepository'", phòng nhân sự (Spring) sẽ trỏ `MyService` đến làm việc với **"kẻ đóng thế" (`@MockBean`)** thay vì nhân viên thật.
    * Đây là **Integration Test** (Kiểm thử Tích hợp).

-----

### 1\. 🧑‍💼 `@Mock` (Của Mockito)

Annotation này đến từ thư viện **Mockito**, không phải của Spring.

* **Nó làm gì?** Chỉ đơn giản là **tạo ra** một đối tượng giả (mock object).
* **Phạm vi:** Đối tượng mock này **nằm ngoài (outside)** `ApplicationContext` của Spring.
* **Cách dùng:** Thường dùng cho **Unit Test** (Kiểm thử Đơn vị). Bạn *không* khởi động Spring (`@SpringBootTest`). Bạn dùng `@ExtendWith(MockitoExtension.class)` (cho JUnit 5) hoặc `@RunWith(MockitoJUnitRunner.class)` (cho JUnit 4) để kích hoạt nó.
* **Tiêm (Injection):** Bạn phải dùng **`@InjectMocks`** để "tiêm" (inject) `@Mock` này vào đối tượng mà bạn muốn test.

#### Ví dụ (Unit Test)

```java
// 1. Kích hoạt Mockito cho JUnit 5
@ExtendWith(MockitoExtension.class)
class MyServiceUnitTest {

    // 2. Tạo một "trợ lý cá nhân" (Mock)
    //    (Nó KHÔNG phải là Spring Bean)
    @Mock
    private MyRepository mockRepository;

    // 3. Tạo đối tượng service và "tiêm" trợ lý (mock) vào
    //    (MyService này cũng KHÔNG phải là Spring Bean)
    @InjectMocks
    private MyService myService;

    @Test
    void testMyServiceLogic() {
        // Dạy cho "trợ lý"
        when(mockRepository.findData()).thenReturn("mock data");
        
        // Kiểm tra
        String result = myService.getData();
        assertThat(result).isEqualTo("mock data");
    }
}
```

**Kết luận:** Bài test này chạy **siêu nhanh** vì nó không khởi động Spring.

-----

### 2\. 🕵️ `@MockBean` (Của Spring Boot Test)

Annotation này đến từ **Spring Boot Test** (`spring-boot-test`).

* **Nó làm gì?** Nó làm 2 việc:
    1.  Tạo ra một đối tượng Mockito mock.
    2.  **Đăng ký** mock đó vào `ApplicationContext` của Spring như một `Bean`.
* **Phạm vi:** Nó **thay thế (replaces)** bất kỳ `Bean` thật nào có cùng kiểu (type) trong `ApplicationContext`.
* **Cách dùng:** Dùng cho **Integration Test** (Kiểm thử Tích hợp) với `@SpringBootTest` hoặc "Slice Test" với `@WebMvcTest`.
* **Tiêm (Injection):** Bạn dùng `@Autowired` để lấy các `Bean` thật (như `MyService`), và Spring sẽ *tự động* tiêm `@MockBean` (`MyRepository`) vào bên trong `MyService`.

#### Ví dụ (Integration Test)

```java
// 1. Khởi động TOÀN BỘ Spring ApplicationContext
@SpringBootTest
class MyServiceIntegrationTest {

    // 2. Yêu cầu Spring thay thế Bean "MyRepository" THẬT
    //    bằng một Bean GIẢ (Kẻ đóng thế)
    @MockBean
    private MyRepository mockRepository;

    // 3. Lấy Bean "MyService" THẬT từ Spring
    //    (Spring đã tự động tiêm mockRepository ở trên vào service này)
    @Autowired
    private MyService myService;

    @Test
    void testMyServiceBean() {
        // Dạy cho "kẻ đóng thế"
        when(mockRepository.findData()).thenReturn("mock data");
        
        // Kiểm tra
        String result = myService.getData();
        assertThat(result).isEqualTo("mock data");
    }
}
```

**Kết luận:** `MyService` là `Bean` thật 100%, nhưng dependency `MyRepository` của nó đã bị thay thế bằng `Bean` giả.

### 3\. 📊 Tóm tắt (Điểm khác biệt chính)

| Đặc điểm | `@Mock` (Mockito) | `@MockBean` (Spring Boot Test) |
| :--- | :--- | :--- |
| **Nguồn gốc** | `org.mockito` | `org.springframework.boot.test` |
| **Mục đích** | Chỉ **tạo** một mock object. | **Tạo** mock VÀ **đăng ký** vào Spring Context. |
| **Phạm vi** | Nằm **ngoài** `ApplicationContext`. | Nằm **trong** `ApplicationContext`. |
| **Hành vi** | **Không** thay thế Bean. | **Thay thế (Replaces)** Bean thật cùng loại. |
| **Tiêm (Injection)** | Dùng `@InjectMocks` (tiêm vào class test). | Dùng `@Autowired` (Spring tự động tiêm). |
| **Bối cảnh (Context)** | **Không** cần `ApplicationContext`. | **Yêu cầu** `ApplicationContext` (ví dụ: `@SpringBootTest`). |
| **Loại Test** | **Unit Test** | **Integration Test** / **Slice Test** |

-----

## ❓ Câu hỏi: When do you want to use @DataJpaTest for? What does it auto-configure?

(Khi nào bạn muốn sử dụng @DataJpaTest? Nó tự động cấu hình những gì?)

### 1\. 🍰 @DataJpaTest dùng để làm gì? (Kiểm thử "Lát cắt" Dữ liệu)

Bạn nên sử dụng **`@DataJpaTest`** khi bạn chỉ muốn kiểm thử **duy nhất lớp truy cập dữ liệu (Data Access Layer)** của mình.

Hãy quay lại phép so sánh chiếc bánh 3 lớp:

1.  Lớp Web (Controller)
2.  Lớp Logic (Service)
3.  **Lớp Dữ liệu (Repository)** ⬅️ **Đây là mục tiêu của `@DataJpaTest`**

`@DataJpaTest` "cắt" lấy duy nhất Lớp 3 (và CSDL) để kiểm thử một cách cô lập. Nó dùng để trả lời các câu hỏi:

* `@Entity` (Thực thể) của tôi có được ánh xạ (map) đúng với các cột trong CSDL không?
* `@Repository` (Kho lưu trữ) của tôi có hoạt động không?
* Các câu query tùy chỉnh (custom query) mà tôi viết (dùng `@Query`) có chạy đúng cú pháp và trả về kết quả mong đợi không?

-----

### 2\. 🪄 @DataJpaTest tự động cấu hình những gì?

Như bạn đã nói, nó **KHÔNG** nạp (load) toàn bộ ứng dụng. Thay vào đó, nó chỉ nạp (load) một `ApplicationContext` "rất mỏng" chỉ chứa:

* **Một CSDL nhúng (Embedded Database):**
    * Mặc định, nó sẽ **bỏ qua (ignore)** cấu hình `DataSource` (ví dụ: MySQL, Postgres) trong file `application.properties` của bạn.
    * Thay vào đó, nó tự động cấu hình một **CSDL H2 trong bộ nhớ (in-memory)**.
    * *Lý do:* Giúp bài test chạy **siêu nhanh**, không phụ thuộc vào CSDL bên ngoài, và tự động "dọn dẹp" sau mỗi lần chạy.
* **Quét (scan) các `@Entity`:**
    * Nó tìm tất cả các lớp của bạn có đánh dấu `@Entity` (ví dụ: `User.class`, `Product.class`) để Hibernate/JPA có thể tạo các bảng tương ứng.
* **Cấu hình các Spring Data Repository:**
    * Nó tìm tất cả các interface (giao diện) của bạn có kế thừa `JpaRepository` (ví dụ: `UserRepository`) và tạo các `Bean` triển khai (implementation) cho chúng.
* **`TestEntityManager` (Công cụ hỗ trợ):**
    * Nó cung cấp một `Bean` đặc biệt tên là `TestEntityManager`. Đây là một phiên bản "thu gọn" của `EntityManager` được thiết kế riêng cho việc test.
    * Nó rất hữu ích để **thiết lập (setup)** dữ liệu (ví dụ: `persistAndFlush`) trước khi bạn chạy test.

-----

### 3\. ⛔ @DataJpaTest KHÔNG cấu hình những gì? (Rất quan trọng)

Đây chính là điểm mấu chốt của "lát cắt". Nó sẽ **KHÔNG** quét và **KHÔNG** tạo ra các `Bean`:

* `@Service`
* `@Controller` / `@RestController`
* `@Component` (thông thường)
* `@ControllerAdvice`

Nếu `Repository` của bạn cần một `Bean` nào đó (ví dụ: một `AuditorAware`), bạn phải `@Import` nó vào một cách thủ công.

-----

### 4\. ⚙️ Hai (2) hành vi mặc định quan trọng

#### a. Giao dịch & Rollback (Transactional & Rollback)

Như bạn đã nói, **mỗi phương thức `@Test` trong `@DataJpaTest` đều được bọc trong một giao dịch (transaction)**, và giao dịch đó sẽ **tự động bị ROLLBACK** (hoàn tác) khi test kết thúc.

* **Lợi ích:** Cơ sở dữ liệu sẽ "sạch sẽ" (clean) sau mỗi bài test. `testA` chèn 10 user sẽ không ảnh hưởng đến `testB` (vốn mong đợi 0 user). Nó giống như bạn viết lên bảng trắng rồi xóa ngay sau mỗi lần thử.
* **Tùy chỉnh:** Bạn có thể thay đổi hành vi này bằng `@Transactional(propagation = ...)` hoặc `@Rollback(false)` nếu bạn *thực sự* muốn (nhưng điều này hiếm khi được khuyên dùng).

#### b. Thay thế CSDL (Database Replacement)

Như đã nói, nó dùng CSDL H2 (in-memory). Nếu bạn muốn *ép* `@DataJpaTest` chạy trên CSDL thật (ví dụ: MySQL) mà bạn đã cấu hình trong `application.properties`, bạn phải **tắt** hành vi mặc định này:

```java
@DataJpaTest
// Yêu cầu: "Đừng thay thế CSDL của tôi, hãy dùng cái thật."
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 
class MyRepositoryIntegrationTest {
    // ...
}
```

-----

### 5\. 📖 Ví dụ thực tế

```java
@DataJpaTest
// Không cần @AutoConfigureTestDatabase, cứ để nó dùng H2
class UserRepositoryTest {

    // 1. (Được auto-configure) Tiêm (inject) helper
    @Autowired
    private TestEntityManager entityManager;

    // 2. (Được auto-configure) Tiêm (inject) Repository thật
    @Autowired
    private UserRepository userRepository;

    @Test
    void whenFindByName_thenReturnUser() {
        // ----- Giai đoạn 1: Sắp xếp (Arrange) -----
        // Dùng TestEntityManager để "nhồi" data vào CSDL
        // persistAndFlush = lưu và "commit" ngay vào DB
        User alex = new User("Alex");
        entityManager.persistAndFlush(alex); 

        // ----- Giai đoạn 2: Hành động (Act) -----
        // Gọi phương thức Repository thật mà bạn muốn test
        User found = userRepository.findByName("Alex");

        // ----- Giai đoạn 3: Khẳng định (Assert) -----
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Alex");
        
        // (Không cần dọn dẹp, vì @DataJpaTest sẽ tự động ROLLBACK "Alex")
    }
}
```