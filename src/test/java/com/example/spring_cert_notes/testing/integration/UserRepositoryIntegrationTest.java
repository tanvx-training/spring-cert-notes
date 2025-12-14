package com.example.spring_cert_notes.testing.integration;

import com.example.spring_cert_notes.testing.entity.User;
import com.example.spring_cert_notes.testing.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * BÀI 2: INTEGRATION TEST VỚI @DataJpaTest
 * 
 * Mục tiêu:
 * - Sử dụng @DataJpaTest để test JPA repositories
 * - TestEntityManager để setup test data
 * - Test các query methods
 * - Hiểu về test slices
 * 
 * @DataJpaTest:
 * - Chỉ load JPA components (repositories, entities)
 * - Tự động cấu hình embedded database (H2)
 * - Mỗi test method chạy trong transaction và rollback sau khi hoàn thành
 * - Không load @Service, @Controller, etc.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("UserRepository Integration Tests")
class UserRepositoryIntegrationTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        // Tạo test data sử dụng TestEntityManager
        testUser = new User("John Doe", "john@example.com");
        testUser.setActive(true);
        entityManager.persistAndFlush(testUser);
    }
    
    // ============================================================
    // TEST: Basic CRUD Operations
    // ============================================================
    
    @Test
    @DisplayName("Should save user successfully")
    void shouldSaveUser() {
        // Given
        User newUser = new User("Alice", "alice@example.com");
        
        // When
        User saved = userRepository.save(newUser);
        
        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Alice");
        assertThat(saved.getCreatedAt()).isNotNull(); // @PrePersist
    }
    
    @Test
    @DisplayName("Should find user by ID")
    void shouldFindById() {
        // When
        Optional<User> found = userRepository.findById(testUser.getId());
        
        // Then
        assertThat(found)
            .isPresent()
            .hasValueSatisfying(user -> {
                assertThat(user.getName()).isEqualTo("John Doe");
                assertThat(user.getEmail()).isEqualTo("john@example.com");
            });
    }
    
    @Test
    @DisplayName("Should return empty when user not found")
    void shouldReturnEmptyWhenNotFound() {
        // When
        Optional<User> found = userRepository.findById(999L);
        
        // Then
        assertThat(found).isEmpty();
    }
    
    // ============================================================
    // TEST: Query Methods
    // ============================================================
    
    @Test
    @DisplayName("Should find user by email")
    void shouldFindByEmail() {
        // When
        Optional<User> found = userRepository.findByEmail("john@example.com");
        
        // Then
        assertThat(found)
            .isPresent()
            .hasValueSatisfying(user -> 
                assertThat(user.getName()).isEqualTo("John Doe")
            );
    }
    
    @Test
    @DisplayName("Should find all active users")
    void shouldFindActiveUsers() {
        // Given - thêm inactive user
        User inactiveUser = new User("Inactive", "inactive@example.com");
        inactiveUser.setActive(false);
        entityManager.persistAndFlush(inactiveUser);
        
        // When
        List<User> activeUsers = userRepository.findByActiveTrue();
        
        // Then
        assertThat(activeUsers)
            .hasSize(1)
            .extracting(User::getName)
            .containsExactly("John Doe");
    }
    
    @Test
    @DisplayName("Should find users by name containing")
    void shouldFindByNameContaining() {
        // Given
        User user2 = new User("Johnny Bravo", "johnny@example.com");
        entityManager.persistAndFlush(user2);
        
        // When
        List<User> users = userRepository.findByNameContainingIgnoreCase("john");
        
        // Then
        assertThat(users)
            .hasSize(2)
            .extracting(User::getName)
            .containsExactlyInAnyOrder("John Doe", "Johnny Bravo");
    }
    
    @Test
    @DisplayName("Should check if email exists")
    void shouldCheckEmailExists() {
        // When & Then
        assertThat(userRepository.existsByEmail("john@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("nonexistent@example.com")).isFalse();
    }
    
    // ============================================================
    // TEST: Modifying Queries
    // ============================================================
    
    @Test
    @DisplayName("Should update active status")
    void shouldUpdateActiveStatus() {
        // When
        int updated = userRepository.updateActiveStatus(testUser.getId(), false);
        
        // Then
        assertThat(updated).isEqualTo(1);
        
        // Clear persistence context để force reload từ DB
        entityManager.clear();
        
        User reloaded = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(reloaded.isActive()).isFalse();
    }
    
    // ============================================================
    // TEST: Delete Operations
    // ============================================================
    
    @Test
    @DisplayName("Should delete user")
    void shouldDeleteUser() {
        // Given
        Long userId = testUser.getId();
        
        // When
        userRepository.deleteById(userId);
        entityManager.flush();
        
        // Then
        assertThat(userRepository.findById(userId)).isEmpty();
    }
    
    // ============================================================
    // TEST: Multiple Users
    // ============================================================
    
    @Test
    @DisplayName("Should find all users")
    void shouldFindAllUsers() {
        // Given
        User user2 = new User("Alice", "alice@example.com");
        User user3 = new User("Bob", "bob@example.com");
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();
        
        // When
        List<User> allUsers = userRepository.findAll();
        
        // Then
        assertThat(allUsers).hasSize(3);
    }
}
