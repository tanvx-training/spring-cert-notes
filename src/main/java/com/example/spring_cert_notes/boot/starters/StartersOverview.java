package com.example.spring_cert_notes.boot.starters;

/**
 * BÀI 4: SPRING BOOT STARTERS OVERVIEW
 * 
 * Starters là dependency descriptors - một dependency kéo theo nhiều dependencies khác.
 * Giúp đơn giản hóa dependency management.
 * 
 * ============================================================
 * TOP 10 SPRING BOOT STARTERS PHỔ BIẾN
 * ============================================================
 * 
 * 1. spring-boot-starter-web
 *    - Spring MVC
 *    - Embedded Tomcat
 *    - Jackson (JSON)
 *    - Validation
 *    
 * 2. spring-boot-starter-data-jpa
 *    - Spring Data JPA
 *    - Hibernate
 *    - HikariCP (connection pool)
 *    - Spring JDBC
 *    
 * 3. spring-boot-starter-security
 *    - Spring Security
 *    - Authentication/Authorization
 *    
 * 4. spring-boot-starter-test
 *    - JUnit 5
 *    - Mockito
 *    - AssertJ
 *    - Spring Test
 *    - JSONPath
 *    
 * 5. spring-boot-starter-actuator
 *    - Health checks
 *    - Metrics
 *    - Monitoring endpoints
 *    
 * 6. spring-boot-starter-validation
 *    - Hibernate Validator
 *    - Jakarta Validation API
 *    
 * 7. spring-boot-starter-aop
 *    - Spring AOP
 *    - AspectJ
 *    
 * 8. spring-boot-starter-cache
 *    - Spring Cache abstraction
 *    - JSR-107 support
 *    
 * 9. spring-boot-starter-mail
 *    - JavaMail
 *    - Spring Mail
 *    
 * 10. spring-boot-starter-thymeleaf
 *     - Thymeleaf template engine
 *     - Spring integration
 * 
 * ============================================================
 * STARTER NAMING CONVENTION
 * ============================================================
 * 
 * Official starters: spring-boot-starter-*
 * Third-party starters: *-spring-boot-starter
 * 
 * Ví dụ:
 * - spring-boot-starter-web (official)
 * - mybatis-spring-boot-starter (third-party)
 * 
 * ============================================================
 * STARTER STRUCTURE
 * ============================================================
 * 
 * Một starter thường bao gồm:
 * 1. pom.xml với dependencies
 * 2. Auto-configuration classes
 * 3. META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
 * 4. Default properties
 * 
 * ============================================================
 * TẠO CUSTOM STARTER
 * ============================================================
 * 
 * Cấu trúc:
 * my-spring-boot-starter/
 * ├── pom.xml
 * └── src/main/java/
 *     └── com/example/
 *         ├── MyAutoConfiguration.java
 *         ├── MyProperties.java
 *         └── MyService.java
 * 
 * META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports:
 * com.example.MyAutoConfiguration
 */
public class StartersOverview {
    
    /**
     * Starter dependencies được quản lý bởi spring-boot-dependencies BOM
     * (Bill of Materials)
     * 
     * BOM định nghĩa versions cho tất cả dependencies
     * Không cần specify version trong pom.xml
     */
    public static void main(String[] args) {
        System.out.println("=== Spring Boot Starters ===");
        System.out.println("1. spring-boot-starter-web - Web applications");
        System.out.println("2. spring-boot-starter-data-jpa - JPA/Hibernate");
        System.out.println("3. spring-boot-starter-security - Security");
        System.out.println("4. spring-boot-starter-test - Testing");
        System.out.println("5. spring-boot-starter-actuator - Monitoring");
        System.out.println("============================");
    }
}
