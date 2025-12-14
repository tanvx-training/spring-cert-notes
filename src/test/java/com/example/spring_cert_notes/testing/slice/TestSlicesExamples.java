package com.example.spring_cert_notes.testing.slice;

import org.junit.jupiter.api.*;

/**
 * BÀI 5: TEST SLICES OVERVIEW
 * 
 * Test Slices là các annotations giúp load một phần của Spring context
 * thay vì toàn bộ application. Điều này giúp tests chạy nhanh hơn.
 * 
 * ============================================================
 * DANH SÁCH TEST SLICES QUAN TRỌNG
 * ============================================================
 * 
 * 1. @WebMvcTest
 *    - Load: Controllers, ControllerAdvice, JsonComponent, Converter, Filter, WebMvcConfigurer
 *    - Không load: @Service, @Repository, @Component
 *    - Use case: Test REST controllers
 *    - Cần: @MockBean cho dependencies
 * 
 * 2. @DataJpaTest
 *    - Load: JPA repositories, EntityManager, DataSource
 *    - Không load: @Service, @Controller
 *    - Use case: Test JPA repositories
 *    - Auto-configure: Embedded database (H2)
 *    - Transactions: Auto-rollback after each test
 * 
 * 3. @DataJdbcTest
 *    - Load: JDBC repositories, JdbcTemplate
 *    - Use case: Test Spring Data JDBC
 * 
 * 4. @JdbcTest
 *    - Load: JdbcTemplate, DataSource
 *    - Use case: Test raw JDBC operations
 * 
 * 5. @JsonTest
 *    - Load: ObjectMapper, JsonComponent
 *    - Use case: Test JSON serialization/deserialization
 * 
 * 6. @RestClientTest
 *    - Load: RestTemplateBuilder, MockRestServiceServer
 *    - Use case: Test REST clients
 * 
 * 7. @WebFluxTest
 *    - Load: WebFlux controllers
 *    - Use case: Test reactive controllers
 * 
 * ============================================================
 * SO SÁNH PERFORMANCE
 * ============================================================
 * 
 * @WebMvcTest      ~1-2 seconds
 * @DataJpaTest     ~2-3 seconds
 * @JsonTest        ~1 second
 * @SpringBootTest  ~5-10+ seconds (full context)
 * 
 * ============================================================
 * BEST PRACTICES
 * ============================================================
 * 
 * 1. Ưu tiên test slices khi có thể
 * 2. Sử dụng @SpringBootTest cho integration tests
 * 3. Kết hợp @MockBean để mock dependencies
 * 4. Sử dụng @ActiveProfiles("test") để load test config
 */
@DisplayName("Test Slices Documentation")
class TestSlicesExamples {
    
    @Test
    @DisplayName("This is a documentation class - see comments above")
    void documentationOnly() {
        // This class serves as documentation for test slices
        // See the actual test examples in other test classes
    }
}
