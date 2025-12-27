package com.example.spring_cert_notes.testing.slice;

import com.example.spring_cert_notes.testing.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.*;

/**
 * BÀI 6: @JsonTest - Test JSON Serialization/Deserialization
 * <p>
 * Mục tiêu:
 * - Test JSON serialization (Object -> JSON)
 * - Test JSON deserialization (JSON -> Object)
 * - Sử dụng JacksonTester
 * 
 * @JsonTest:
 * - Chỉ load JSON-related components
 * - Auto-configure ObjectMapper
 * - Rất nhanh vì không load web layer
 */
@JsonTest
@DisplayName("JSON Serialization Tests")
class JsonTestExample {
    
    @Autowired
    private JacksonTester<User> json;
    
    // ============================================================
    // TEST: Serialization (Object -> JSON)
    // ============================================================
    
    @Test
    @DisplayName("Should serialize User to JSON")
    void shouldSerializeUser() throws Exception {
        // Given
        User user = new User("John Doe", "john@example.com");
        user.setId(1L);
        user.setActive(true);
        
        // When
        JsonContent<User> result = json.write(user);
        
        // Then
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john@example.com");
        assertThat(result).extractingJsonPathBooleanValue("$.active").isTrue();
    }
    
    @Test
    @DisplayName("Should serialize User with correct JSON structure")
    void shouldSerializeWithCorrectStructure() throws Exception {
        // Given
        User user = new User("Alice", "alice@example.com");
        user.setId(2L);
        
        // When
        JsonContent<User> result = json.write(user);
        
        // Then - kiểm tra JSON structure
        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).hasJsonPathStringValue("$.name");
        assertThat(result).hasJsonPathStringValue("$.email");
        assertThat(result).hasJsonPathBooleanValue("$.active");
    }
    
    // ============================================================
    // TEST: Deserialization (JSON -> Object)
    // ============================================================
    
    @Test
    @DisplayName("Should deserialize JSON to User")
    void shouldDeserializeUser() throws Exception {
        // Given
        String jsonContent = """
            {
                "id": 1,
                "name": "John Doe",
                "email": "john@example.com",
                "active": true
            }
            """;
        
        // When
        User user = json.parseObject(jsonContent);
        
        // Then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
        assertThat(user.isActive()).isTrue();
    }
    
    @Test
    @DisplayName("Should deserialize JSON with missing optional fields")
    void shouldDeserializeWithMissingFields() throws Exception {
        // Given - JSON without 'active' field
        String jsonContent = """
            {
                "name": "Bob",
                "email": "bob@example.com"
            }
            """;
        
        // When
        User user = json.parseObject(jsonContent);
        
        // Then
        assertThat(user.getName()).isEqualTo("Bob");
        assertThat(user.getEmail()).isEqualTo("bob@example.com");
        assertThat(user.isActive()).isTrue(); // default value
    }
    
    // ============================================================
    // TEST: Round-trip (Object -> JSON -> Object)
    // ============================================================
    
    @Test
    @DisplayName("Should maintain data integrity in round-trip")
    void shouldMaintainDataInRoundTrip() throws Exception {
        // Given
        User original = new User("Test User", "test@example.com");
        original.setId(100L);
        original.setActive(false);
        
        // When - serialize then deserialize
        String jsonString = json.write(original).getJson();
        User restored = json.parseObject(jsonString);
        
        // Then
        assertThat(restored.getId()).isEqualTo(original.getId());
        assertThat(restored.getName()).isEqualTo(original.getName());
        assertThat(restored.getEmail()).isEqualTo(original.getEmail());
        assertThat(restored.isActive()).isEqualTo(original.isActive());
    }
}
