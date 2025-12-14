package com.example.spring_cert_notes.testing.mock;

import com.example.spring_cert_notes.testing.entity.User;
import com.example.spring_cert_notes.testing.repository.UserRepository;
import com.example.spring_cert_notes.testing.service.UserService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * BÀI 7: @MockBean VÀ @SpyBean
 * 
 * Mục tiêu:
 * - Hiểu sự khác biệt giữa @Mock và @MockBean
 * - Sử dụng @MockBean trong Spring context
 * - Sử dụng @SpyBean để partial mock
 * - ArgumentCaptor để capture arguments
 * 
 * @MockBean vs @Mock:
 * - @Mock: Mockito annotation, không liên quan đến Spring
 * - @MockBean: Spring Boot annotation, thay thế bean trong context
 * 
 * @SpyBean:
 * - Wrap real bean với spy
 * - Có thể mock một số methods, giữ nguyên behavior của methods khác
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("@MockBean and @SpyBean Examples")
class MockBeanExamples {
    
    @MockBean
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Captor
    private ArgumentCaptor<User> userCaptor;
    
    // ============================================================
    // @MockBean Examples
    // ============================================================
    
    @Test
    @DisplayName("@MockBean replaces bean in Spring context")
    void mockBeanReplacesBean() {
        // Given - mock repository behavior
        User mockUser = new User("Mocked User", "mock@example.com");
        mockUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        
        // When - service uses mocked repository
        Optional<User> result = userService.findById(1L);
        
        // Then
        assertThat(result)
            .isPresent()
            .hasValueSatisfying(user -> 
                assertThat(user.getName()).isEqualTo("Mocked User")
            );
    }
    
    @Test
    @DisplayName("Verify mock interactions")
    void verifyMockInteractions() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        
        // When
        User newUser = new User("New User", "new@example.com");
        userService.createUser(newUser);
        
        // Then - verify interactions
        verify(userRepository).existsByEmail("new@example.com");
        verify(userRepository).save(any(User.class));
        verify(userRepository, times(1)).save(any());
        verify(userRepository, never()).deleteById(any());
    }
    
    // ============================================================
    // ArgumentCaptor Examples
    // ============================================================
    
    @Test
    @DisplayName("Capture arguments with ArgumentCaptor")
    void captureArguments() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        
        // When
        User newUser = new User("Captured User", "captured@example.com");
        userService.createUser(newUser);
        
        // Then - capture and verify argument
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        
        assertThat(capturedUser.getName()).isEqualTo("Captured User");
        assertThat(capturedUser.getEmail()).isEqualTo("captured@example.com");
    }
    
    // ============================================================
    // Stubbing Patterns
    // ============================================================
    
    @Test
    @DisplayName("Different stubbing patterns")
    void stubbingPatterns() {
        // Pattern 1: Return value
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User("User 1", "u1@test.com")));
        
        // Pattern 2: Return different values on consecutive calls
        when(userRepository.count())
            .thenReturn(0L)
            .thenReturn(1L)
            .thenReturn(2L);
        
        assertThat(userRepository.count()).isEqualTo(0L);
        assertThat(userRepository.count()).isEqualTo(1L);
        assertThat(userRepository.count()).isEqualTo(2L);
        
        // Pattern 3: Throw exception
        when(userRepository.findById(999L))
            .thenThrow(new RuntimeException("Database error"));
        
        assertThatThrownBy(() -> userRepository.findById(999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Database error");
        
        // Pattern 4: Answer - dynamic response
        when(userRepository.save(any(User.class)))
            .thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(System.currentTimeMillis());
                return user;
            });
    }
    
    // ============================================================
    // Argument Matchers
    // ============================================================
    
    @Test
    @DisplayName("Using argument matchers")
    void argumentMatchers() {
        // any() - matches any value
        when(userRepository.save(any(User.class))).thenReturn(new User("Any", "any@test.com"));
        
        // anyLong(), anyString(), etc.
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // eq() - exact match
        when(userRepository.findByEmail(eq("specific@test.com")))
            .thenReturn(Optional.of(new User("Specific", "specific@test.com")));
        
        // argThat() - custom matcher
        when(userRepository.save(argThat(user -> 
            user != null && user.getEmail().endsWith("@admin.com")
        ))).thenReturn(new User("Admin", "admin@admin.com"));
        
        // Verify with matchers
        userRepository.findById(100L);
        verify(userRepository).findById(argThat(id -> id > 0));
    }
}
