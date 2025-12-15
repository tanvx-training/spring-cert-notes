package com.example.spring_cert_notes.security.controller;

import com.example.spring_cert_notes.security.dto.Document;
import com.example.spring_cert_notes.security.service.MethodSecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * User Controller - Cần ROLE_USER
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    private final MethodSecurityService methodSecurityService;
    
    public UserController(MethodSecurityService methodSecurityService) {
        this.methodSecurityService = methodSecurityService;
    }
    
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        return ResponseEntity.ok(Map.of(
            "username", auth.getName(),
            "authorities", auth.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .toList(),
            "authenticated", auth.isAuthenticated()
        ));
    }
    
    /**
     * User chỉ có thể xem profile của chính mình
     * Admin có thể xem profile của bất kỳ ai
     */
    @GetMapping("/profile/{username}")
    @PreAuthorize("#username == authentication.name or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> getUserProfile(@PathVariable String username) {
        return ResponseEntity.ok(Map.of(
            "username", username,
            "message", "Profile data for " + username
        ));
    }
    
    /**
     * Lấy documents của user hiện tại
     * @PostFilter sẽ lọc chỉ trả về documents của user
     */
    @GetMapping("/documents")
    public ResponseEntity<List<Document>> getMyDocuments() {
        List<Document> docs = methodSecurityService.getAllDocuments();
        return ResponseEntity.ok(docs);
    }
    
    /**
     * Lấy document theo ID
     * @PostAuthorize kiểm tra owner
     */
    @GetMapping("/documents/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable Long id) {
        Document doc = methodSecurityService.getDocument(id);
        return ResponseEntity.ok(doc);
    }
}
