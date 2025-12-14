package com.example.spring_cert_notes.testing.unit;

import com.example.spring_cert_notes.testing.entity.User;
import com.example.spring_cert_notes.testing.repository.UserRepository;
import com.example.spring_cert_notes.testing.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * BÀI 1: UNIT TEST VỚI JUNIT 5 + MOCKITO
 * 
 * Mục tiêu:
 * - Sử dụng @ExtendWith(MockitoExtension.class) để tích hợp Mockito
 * - @Mock để tạo mock objects
 * - @InjectMocks để inject mocks vào class cần test
 * - Assertions với AssertJ
 * - Verify mock interactions
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceUnitTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new User("John Doe", "john@example.com");
        testUser.setId(1L);
    }
    
    // ============================================================
    // TEST: createUser
    // ============================================================
    
    @Test
    @DisplayName("Should create user successfully when email is unique")
    void shouldCreateUser_WhenEmailIsUnique() {
        // Given (Arrange)
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // When (Act)
        User result = userService.createUser(testUser);
        
        // Then (Assert)
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        
        // Verify interactions
        verify(userRepository).existsByEmail(testUser.getEmail());
        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }
    
    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowException_WhenEmailExists() {
        // Given
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(true);
        
        // When & Then
        assertThatThrownBy(() -> userService.createUser(testUser))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Email already exists");
        
        // Verify save was never called
        verify(userRepository, never()).save(any());
    }
    
    // ============================================================
    // TEST: findById
    // ============================================================
    
    @Test
    @DisplayName("Should find user by ID when exists")
    void shouldFindUserById_WhenExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        
        // When
        Optional<User> result = userService.findById(1L);
        
        // Then
        assertThat(result)
            .isPresent()
            .hasValueSatisfying(user -> {
                assertThat(user.getName()).isEqualTo("John Doe");
                assertThat(user.getEmail()).isEqualTo("john@example.com");
            });
    }
    
    @Test
    @DisplayName("Should return empty when user not found")
    void shouldReturnEmpty_WhenUserNotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When
        Optional<User> result = userService.findById(999L);
        
        // Then
        assertThat(result).isEmpty();
    }
    
    // ============================================================
    // TEST: findAllActiveUsers
    // ============================================================
    
    @Test
    @DisplayName("Should return all active users")
    void shouldReturnAllActiveUsers() {
        // Given
        User user1 = new User("Alice", "alice@example.com");
        User user2 = new User("Bob", "bob@example.com");
        List<User> activeUsers = Arrays.asList(user1, user2);
        
        when(userRepository.findByActiveTrue()).thenReturn(activeUsers);
        
        // When
        List<User> result = userService.findAllActiveUsers();
        
        // Then
        assertThat(result)
            .hasSize(2)
            .extracting(User::getName)
            .containsExactly("Alice", "Bob");
    }
    
    // ============================================================
    // TEST: updateUser
    // ============================================================
    
    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUser_WhenExists() {
        // Given
        User updatedUser = new User("John Updated", "john.updated@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        User result = userService.updateUser(1L, updatedUser);
        
        // Then
        assertThat(result.getName()).isEqualTo("John Updated");
        assertThat(result.getEmail()).isEqualTo("john.updated@example.com");
    }
    
    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void shouldThrowException_WhenUpdatingNonExistentUser() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> userService.updateUser(999L, testUser))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("User not found");
    }
    
    // ============================================================
    // TEST: deleteUser
    // ============================================================
    
    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUser_WhenExists() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);
        
        // When
        userService.deleteUser(1L);
        
        // Then
        verify(userRepository).deleteById(1L);
    }
    
    @Test
    @DisplayName("Should throw exception when deleting non-existent user")
    void shouldThrowException_WhenDeletingNonExistentUser() {
        // Given
        when(userRepository.existsById(999L)).thenReturn(false);
        
        // When & Then
        assertThatThrownBy(() -> userService.deleteUser(999L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("User not found");
        
        verify(userRepository, never()).deleteById(any());
    }
}
