package com.example.spring_cert_notes.testing.unit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.Duration;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * BÀI 2: JUNIT 5 FEATURES - Assertions, Assumptions, ParameterizedTest
 * <p>
 * Mục tiêu:
 * - Hiểu các loại Assertions trong JUnit 5
 * - Sử dụng Assumptions để skip tests có điều kiện
 * - @ParameterizedTest với nhiều data sources
 * - Conditional test execution
 */
@DisplayName("JUnit 5 Features Demo")
class JUnit5FeaturesTest {
    
    // ============================================================
    // 1. ASSERTIONS - Các phương thức kiểm tra
    // ============================================================
    
    @Nested
    @DisplayName("Assertions Examples")
    class AssertionsExamples {
        
        @Test
        @DisplayName("Basic assertions")
        void basicAssertions() {
            // assertEquals - so sánh bằng
            assertEquals(4, 2 + 2, "2 + 2 should equal 4");
            
            // assertNotEquals - so sánh khác
            assertNotEquals(5, 2 + 2);
            
            // assertTrue/assertFalse - kiểm tra boolean
            assertTrue(5 > 3, "5 should be greater than 3");
            assertFalse(3 > 5);
            
            // assertNull/assertNotNull - kiểm tra null
            String nullString = null;
            String notNullString = "Hello";
            assertNull(nullString);
            assertNotNull(notNullString);
            
            // assertSame/assertNotSame - kiểm tra reference
            String str1 = "test";
            String str2 = "test";
            String str3 = new String("test");
            assertSame(str1, str2);      // Same reference (string pool)
            assertNotSame(str1, str3);   // Different reference
        }
        
        @Test
        @DisplayName("Array assertions")
        void arrayAssertions() {
            int[] expected = {1, 2, 3};
            int[] actual = {1, 2, 3};
            
            assertArrayEquals(expected, actual, "Arrays should be equal");
        }
        
        @Test
        @DisplayName("Exception assertions")
        void exceptionAssertions() {
            // assertThrows - kiểm tra exception được throw
            Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    throw new IllegalArgumentException("Invalid input");
                }
            );
            
            assertEquals("Invalid input", exception.getMessage());
            
            // assertDoesNotThrow - kiểm tra không throw exception
            assertDoesNotThrow(() -> {
                int result = 10 / 2;
            });
        }
        
        @Test
        @DisplayName("Grouped assertions - assertAll")
        void groupedAssertions() {
            String name = "John Doe";
            int age = 30;
            String email = "john@example.com";
            
            // assertAll - chạy tất cả assertions, báo cáo tất cả failures
            assertAll("User properties",
                () -> assertEquals("John Doe", name),
                () -> assertTrue(age > 18),
                () -> assertTrue(email.contains("@"))
            );
        }
        
        @Test
        @DisplayName("Timeout assertions")
        void timeoutAssertions() {
            // assertTimeout - fail nếu vượt quá thời gian
            assertTimeout(Duration.ofMillis(100), () -> {
                // Code phải hoàn thành trong 100ms
                Thread.sleep(50);
            });
        }
    }
    
    // ============================================================
    // 2. ASSUMPTIONS - Skip tests có điều kiện
    // ============================================================
    
    @Nested
    @DisplayName("Assumptions Examples")
    class AssumptionsExamples {
        
        @Test
        @DisplayName("assumeTrue - skip if condition is false")
        void assumeTrueExample() {
            // Test chỉ chạy nếu điều kiện đúng
            assumeTrue(System.getProperty("os.name").contains("Mac"),
                "Test only runs on Mac");
            
            // Code này chỉ chạy trên Mac
            System.out.println("Running on Mac!");
        }
        
        @Test
        @DisplayName("assumeFalse - skip if condition is true")
        void assumeFalseExample() {
            // Test chỉ chạy nếu điều kiện sai
            assumeFalse(System.getenv("CI") != null,
                "Test skipped in CI environment");
            
            // Code này không chạy trong CI
            System.out.println("Not running in CI!");
        }
        
        @Test
        @DisplayName("assumingThat - conditional execution")
        void assumingThatExample() {
            String env = System.getenv("ENV");
            
            // Chỉ chạy assertion này nếu điều kiện đúng
            assumingThat(
                "DEV".equals(env),
                () -> assertEquals("DEV", env)
            );
            
            // Code này luôn chạy
            assertTrue(true, "This always runs");
        }
    }
    
    // ============================================================
    // 3. PARAMETERIZED TESTS - Test với nhiều input
    // ============================================================
    
    @Nested
    @DisplayName("Parameterized Tests")
    class ParameterizedTestExamples {
        
        @ParameterizedTest(name = "Value {0} should be positive")
        @ValueSource(ints = {1, 5, 10, 100, 0})
        @DisplayName("@ValueSource - simple values")
        void valueSourceTest(int number) {
            assertTrue(number > 0);
        }
        
        @ParameterizedTest(name = "String \"{0}\" should not be blank")
        @ValueSource(strings = {"Hello", "World", "JUnit5", ""})
        @DisplayName("@ValueSource with strings")
        void valueSourceStringsTest(String value) {
            assertFalse(value.isBlank());
        }
        
        @ParameterizedTest(name = "Value = {0}")
        @NullSource
        @DisplayName("@NullSource - test with null")
        void nullSourceTest(String value) {
            assertNull(value);
        }
        
        @ParameterizedTest(name = "Value = {0}")
        @EmptySource
        @DisplayName("@EmptySource - test with empty")
        void emptySourceTest(String value) {
            assertTrue(value.isEmpty());
        }
        
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"1", "2", "3"})
        @DisplayName("@NullAndEmptySource + @ValueSource")
        void nullEmptyAndBlankStrings(String value) {
            assertTrue(value == null || value.isBlank());
        }
        
        @ParameterizedTest(name = "{0} + {1} = {2}")
        @CsvSource({
            "1, 2, 3",
            "5, 5, 10",
            "10, -5, 5",
            "0, 0, 0"
        })
        @DisplayName("@CsvSource - multiple parameters")
        void csvSourceTest(int a, int b, int expected) {
            assertEquals(expected, a + b);
        }
        
        @ParameterizedTest(name = "Email {0} is valid: {1}")
        @CsvSource({
            "test@example.com, true",
            "invalid-email, false",
            "user@domain.org, true",
            "no-at-sign.com, false"
        })
        @DisplayName("@CsvSource - email validation")
        void emailValidationTest(String email, boolean expectedValid) {
            boolean isValid = email.contains("@");
            assertEquals(expectedValid, isValid);
        }
        
        @ParameterizedTest
        @CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)
        @DisplayName("@CsvFileSource - data from CSV file")
        @Disabled("Enable when test-data.csv exists")
        void csvFileSourceTest(String name, int age) {
            assertNotNull(name);
            assertTrue(age > 0);
        }
        
        @ParameterizedTest(name = "Month {0} has {1} days")
        @MethodSource("monthDaysProvider")
        @DisplayName("@MethodSource - custom data provider")
        void methodSourceTest(String month, int days) {
            assertTrue(days >= 28 && days <= 31);
        }
        
        static Stream<Arguments> monthDaysProvider() {
            return Stream.of(
                Arguments.of("January", 31),
                Arguments.of("February", 28),
                Arguments.of("April", 30),
                Arguments.of("December", 31)
            );
        }
        
        @ParameterizedTest
        @EnumSource(DayOfWeek.class)
        @DisplayName("@EnumSource - test with enum values")
        void enumSourceTest(DayOfWeek day) {
            assertNotNull(day);
        }
        
        @ParameterizedTest
        @EnumSource(value = DayOfWeek.class, names = {"SATURDAY", "SUNDAY"})
        @DisplayName("@EnumSource - specific enum values")
        void weekendTest(DayOfWeek day) {
            assertTrue(day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY);
        }
        
        enum DayOfWeek {
            MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
        }
    }
    
    // ============================================================
    // 4. CONDITIONAL TEST EXECUTION
    // ============================================================
    
    @Nested
    @DisplayName("Conditional Tests")
    class ConditionalTestExamples {
        
        @Test
        @EnabledOnOs(OS.MAC)
        @DisplayName("Only runs on Mac")
        void onlyOnMac() {
            assertTrue(true);
        }
        
        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        @DisplayName("Runs on Linux or Mac")
        void onLinuxOrMac() {
            assertTrue(true);
        }
        
        @Test
        @DisabledOnOs(OS.WINDOWS)
        @DisplayName("Disabled on Windows")
        void notOnWindows() {
            assertTrue(true);
        }
        
        @Test
        @EnabledOnJre(JRE.JAVA_17)
        @DisplayName("Only on Java 17")
        void onlyOnJava17() {
            assertTrue(true);
        }
        
        @Test
        @EnabledForJreRange(min = JRE.JAVA_11, max = JRE.JAVA_21)
        @DisplayName("Java 11 to 21")
        void java11To21() {
            assertTrue(true);
        }
        
        @Test
        @EnabledIfSystemProperty(named = "java.vm.vendor", matches = ".*Oracle.*")
        @DisplayName("Only on Oracle JVM")
        void onlyOnOracleJvm() {
            assertTrue(true);
        }
        
        @Test
        @EnabledIfEnvironmentVariable(named = "ENV", matches = "DEV|TEST")
        @DisplayName("Only in DEV or TEST environment")
        void onlyInDevOrTest() {
            assertTrue(true);
        }
    }
    
    // ============================================================
    // 5. TEST LIFECYCLE
    // ============================================================
    
    @Nested
    @DisplayName("Test Lifecycle")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class TestLifecycleExamples {
        
        @BeforeAll
        static void beforeAll() {
            System.out.println(">>> @BeforeAll - runs once before all tests");
        }
        
        @AfterAll
        static void afterAll() {
            System.out.println(">>> @AfterAll - runs once after all tests");
        }
        
        @BeforeEach
        void beforeEach() {
            System.out.println(">>> @BeforeEach - runs before each test");
        }
        
        @AfterEach
        void afterEach() {
            System.out.println(">>> @AfterEach - runs after each test");
        }
        
        @Test
        @Order(1)
        @DisplayName("First test")
        void firstTest() {
            System.out.println("Running first test");
            assertTrue(true);
        }
        
        @Test
        @Order(2)
        @DisplayName("Second test")
        void secondTest() {
            System.out.println("Running second test");
            assertTrue(true);
        }
        
        @Test
        @Order(3)
        @DisplayName("Third test")
        void thirdTest() {
            System.out.println("Running third test");
            assertTrue(true);
        }
    }
}
