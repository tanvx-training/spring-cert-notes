package com.example.spring_cert_notes.mvc.controller;

import com.example.spring_cert_notes.mvc.dto.UserDto;
import com.example.spring_cert_notes.mvc.exception.ResourceNotFoundException;
import com.example.spring_cert_notes.mvc.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * USER REST CONTROLLER
 * <p>
 * Complete CRUD operations with proper HTTP methods:
 * - GET    /api/users      - Get all users
 * - GET    /api/users/{id} - Get user by ID
 * - POST   /api/users      - Create user
 * - PUT    /api/users/{id} - Update user
 * - DELETE /api/users/{id} - Delete user
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // ============================================================
    // GET - Read operations
    // ============================================================
    
    /**
     * GET /api/users - Get all users
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
    
    /**
     * GET /api/users/paged - Get users with pagination
     */
    @GetMapping("/paged")
    public ResponseEntity<Page<UserDto>> getUsersPaged(
            @PageableDefault(size = 10, sort = "lastName") Pageable pageable) {
        Page<UserDto> users = userService.findAll(pageable);
        return ResponseEntity.ok(users);
    }
    
    /**
     * GET /api/users/{id} - Get user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return ResponseEntity.ok(user);
    }
    
    /**
     * GET /api/users/search - Search users
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email) {
        List<UserDto> users = userService.search(firstName, lastName, email);
        return ResponseEntity.ok(users);
    }
    
    // ============================================================
    // POST - Create operations
    // ============================================================
    
    /**
     * POST /api/users - Create new user
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto created = userService.create(userDto);
        URI location = URI.create("/api/users/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }
    
    // ============================================================
    // PUT - Update operations
    // ============================================================
    
    /**
     * PUT /api/users/{id} - Update user
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto userDto) {
        
        if (!userService.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        
        userDto.setId(id);
        UserDto updated = userService.update(userDto);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * PATCH /api/users/{id} - Partial update
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> partialUpdateUser(
            @PathVariable Long id,
            @RequestBody UserDto userDto) {
        
        UserDto updated = userService.partialUpdate(id, userDto);
        return ResponseEntity.ok(updated);
    }
    
    // ============================================================
    // DELETE - Delete operations
    // ============================================================
    
    /**
     * DELETE /api/users/{id} - Delete user
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        if (!userService.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userService.delete(id);
    }
}
