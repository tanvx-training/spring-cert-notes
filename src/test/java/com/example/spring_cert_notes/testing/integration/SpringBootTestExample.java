package com.example.spring_cert_notes.testing.integration;

import com.example.spring_cert_notes.testing.entity.User;
import com.example.spring_cert_notes.testing.repository.UserRepository;
import com.example.spring_cert_notes.testing.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * BÀI 4: FULL INTEGRATION TEST VỚI @SpringBootTest
 * 
 * Mục tiêu:
 * - Sử dụng @SpringBootTest để test toàn bộ application
 * - @AutoConfigureMockMvc để test HTTP endpoints
 * - Test end-to-end flow
 * 
 * @SpringBootTest:
 * - Load toàn bộ application context
 * - Có thể test với real database hoặc embedded
 * - Chậm hơn test slices nhưng test được full integration
 * 
 * WebEnvironment options:
 * - MOCK (default): Mock servlet environment
 * - RANDOM_PORT: Start real server on random port
 * - DEFINED_PORT: Start real server on defined port
 * - NONE: No web environment
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional // Rollback after each test
@DisplayName("Full Integration Tests with @SpringBootTest")
class SpringBootTestExample {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }
    
    // ============================================================
    // END-TO-END TEST: Create and Retrieve User
    // ============================================================
    
    @Test
    @DisplayName("E2E: Should create user and retrieve it")
    void shouldCreateAndRetrieveUser() throws Exception {
        // Step 1: Create user via API
        User newUser = new User("Integration Test User", "integration@example.com");
        
        String response = mockMvc.perform(post("/api/test/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value("Integration Test User"))
            .andReturn()
            .getResponse()
            .getContentAsString();
        
        User createdUser = objectMapper.readValue(response, User.class);
        
        // Step 2: Verify user exists in database
        assertThat(userRepository.findById(createdUser.getId())).isPresent();
        
        // Step 3: Retrieve user via API
        mockMvc.perform(get("/api/test/users/{id}", createdUser.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Integration Test User"))
            .andExpect(jsonPath("$.email").value("integration@example.com"));
    }
    
    // ============================================================
    // END-TO-END TEST: Update User
    // ============================================================
    
    @Test
    @DisplayName("E2E: Should update user")
    void shouldUpdateUser() throws Exception {
        // Given: Create user directly in database
        User user = userService.createUser(new User("Original Name", "original@example.com"));
        
        // When: Update via API
        User updatedUser = new User("Updated Name", "updated@example.com");
        
        mockMvc.perform(put("/api/test/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Name"));
        
        // Then: Verify in database
        User fromDb = userRepository.findById(user.getId()).orElseThrow();
        assertThat(fromDb.getName()).isEqualTo("Updated Name");
        assertThat(fromDb.getEmail()).isEqualTo("updated@example.com");
    }
    
    // ============================================================
    // END-TO-END TEST: Delete User
    // ============================================================
    
    @Test
    @DisplayName("E2E: Should delete user")
    void shouldDeleteUser() throws Exception {
        // Given
        User user = userService.createUser(new User("To Delete", "delete@example.com"));
        Long userId = user.getId();
        
        // When
        mockMvc.perform(delete("/api/test/users/{id}", userId))
            .andExpect(status().isNoContent());
        
        // Then
        assertThat(userRepository.findById(userId)).isEmpty();
    }
    
    // ============================================================
    // END-TO-END TEST: Search Users
    // ============================================================
    
    @Test
    @DisplayName("E2E: Should search users by name")
    void shouldSearchUsers() throws Exception {
        // Given
        userService.createUser(new User("Alice Smith", "alice@example.com"));
        userService.createUser(new User("Bob Johnson", "bob@example.com"));
        userService.createUser(new User("Alice Brown", "alice.brown@example.com"));
        
        // When & Then
        mockMvc.perform(get("/api/test/users/search")
                .param("name", "Alice"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }
    
    // ============================================================
    // TEST: Service Layer Integration
    // ============================================================
    
    @Test
    @DisplayName("Service: Should handle duplicate email")
    void shouldHandleDuplicateEmail() {
        // Given
        userService.createUser(new User("First User", "duplicate@example.com"));
        
        // When & Then
        assertThatThrownBy(() -> 
            userService.createUser(new User("Second User", "duplicate@example.com"))
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("Email already exists");
    }
}
