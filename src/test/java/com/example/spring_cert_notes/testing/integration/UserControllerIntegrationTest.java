package com.example.spring_cert_notes.testing.integration;

import com.example.spring_cert_notes.testing.controller.UserController;
import com.example.spring_cert_notes.testing.entity.User;
import com.example.spring_cert_notes.testing.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * BÀI 3: INTEGRATION TEST VỚI @WebMvcTest + MockMvc
 * 
 * Mục tiêu:
 * - Sử dụng @WebMvcTest để test MVC layer
 * - MockMvc để simulate HTTP requests
 * - @MockBean để mock dependencies
 * - Test các HTTP methods (GET, POST, PUT, DELETE)
 * 
 * @WebMvcTest:
 * - Chỉ load MVC components (controllers, filters, etc.)
 * - Không load @Service, @Repository, etc.
 * - Cần @MockBean cho dependencies
 * - Nhanh hơn @SpringBootTest
 */
@WebMvcTest(UserController.class)
@DisplayName("UserController Integration Tests")
class UserControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private UserService userService;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new User("John Doe", "john@example.com");
        testUser.setId(1L);
    }
    
    // ============================================================
    // TEST: GET /api/test/users/{id}
    // ============================================================
    
    @Test
    @DisplayName("GET /api/test/users/{id} - Should return user when exists")
    void shouldReturnUser_WhenExists() throws Exception {
        // Given
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        
        // When & Then
        mockMvc.perform(get("/api/test/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("john@example.com"));
        
        verify(userService).findById(1L);
    }
    
    @Test
    @DisplayName("GET /api/test/users/{id} - Should return 404 when not found")
    void shouldReturn404_WhenUserNotFound() throws Exception {
        // Given
        when(userService.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        mockMvc.perform(get("/api/test/users/{id}", 999L))
            .andExpect(status().isNotFound());
    }
    
    // ============================================================
    // TEST: GET /api/test/users
    // ============================================================
    
    @Test
    @DisplayName("GET /api/test/users - Should return all active users")
    void shouldReturnAllActiveUsers() throws Exception {
        // Given
        User user1 = new User("Alice", "alice@example.com");
        user1.setId(1L);
        User user2 = new User("Bob", "bob@example.com");
        user2.setId(2L);
        List<User> users = Arrays.asList(user1, user2);
        
        when(userService.findAllActiveUsers()).thenReturn(users);
        
        // When & Then
        mockMvc.perform(get("/api/test/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name").value("Alice"))
            .andExpect(jsonPath("$[1].name").value("Bob"));
    }
    
    // ============================================================
    // TEST: GET /api/test/users/search
    // ============================================================
    
    @Test
    @DisplayName("GET /api/test/users/search - Should search users by name")
    void shouldSearchUsersByName() throws Exception {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userService.searchByName("John")).thenReturn(users);
        
        // When & Then
        mockMvc.perform(get("/api/test/users/search")
                .param("name", "John"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name").value("John Doe"));
    }
    
    // ============================================================
    // TEST: POST /api/test/users
    // ============================================================
    
    @Test
    @DisplayName("POST /api/test/users - Should create user successfully")
    void shouldCreateUser() throws Exception {
        // Given
        User newUser = new User("New User", "new@example.com");
        User savedUser = new User("New User", "new@example.com");
        savedUser.setId(1L);
        
        when(userService.createUser(any(User.class))).thenReturn(savedUser);
        
        // When & Then
        mockMvc.perform(post("/api/test/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("New User"));
        
        verify(userService).createUser(any(User.class));
    }
    
    @Test
    @DisplayName("POST /api/test/users - Should return 400 when email exists")
    void shouldReturn400_WhenEmailExists() throws Exception {
        // Given
        when(userService.createUser(any(User.class)))
            .thenThrow(new IllegalArgumentException("Email already exists"));
        
        // When & Then
        mockMvc.perform(post("/api/test/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
            .andExpect(status().isBadRequest());
    }
    
    // ============================================================
    // TEST: PUT /api/test/users/{id}
    // ============================================================
    
    @Test
    @DisplayName("PUT /api/test/users/{id} - Should update user successfully")
    void shouldUpdateUser() throws Exception {
        // Given
        User updatedUser = new User("Updated Name", "updated@example.com");
        updatedUser.setId(1L);
        
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);
        
        // When & Then
        mockMvc.perform(put("/api/test/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Name"))
            .andExpect(jsonPath("$.email").value("updated@example.com"));
    }
    
    @Test
    @DisplayName("PUT /api/test/users/{id} - Should return 404 when not found")
    void shouldReturn404_WhenUpdatingNonExistentUser() throws Exception {
        // Given
        when(userService.updateUser(eq(999L), any(User.class)))
            .thenThrow(new IllegalArgumentException("User not found"));
        
        // When & Then
        mockMvc.perform(put("/api/test/users/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
            .andExpect(status().isNotFound());
    }
    
    // ============================================================
    // TEST: DELETE /api/test/users/{id}
    // ============================================================
    
    @Test
    @DisplayName("DELETE /api/test/users/{id} - Should delete user successfully")
    void shouldDeleteUser() throws Exception {
        // Given
        doNothing().when(userService).deleteUser(1L);
        
        // When & Then
        mockMvc.perform(delete("/api/test/users/{id}", 1L))
            .andExpect(status().isNoContent());
        
        verify(userService).deleteUser(1L);
    }
    
    @Test
    @DisplayName("DELETE /api/test/users/{id} - Should return 404 when not found")
    void shouldReturn404_WhenDeletingNonExistentUser() throws Exception {
        // Given
        doThrow(new IllegalArgumentException("User not found"))
            .when(userService).deleteUser(999L);
        
        // When & Then
        mockMvc.perform(delete("/api/test/users/{id}", 999L))
            .andExpect(status().isNotFound());
    }
}
