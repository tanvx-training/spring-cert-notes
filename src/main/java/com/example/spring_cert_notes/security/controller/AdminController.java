package com.example.spring_cert_notes.security.controller;

import com.example.spring_cert_notes.security.service.MethodSecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Admin Controller - Cần ROLE_ADMIN
 * 
 * URL-based security: /api/admin/** requires ROLE_ADMIN
 * Method-level security: @PreAuthorize cho fine-grained control
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    private final MethodSecurityService methodSecurityService;
    
    public AdminController(MethodSecurityService methodSecurityService) {
        this.methodSecurityService = methodSecurityService;
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {
        return ResponseEntity.ok(Map.of(
            "message", "Welcome to Admin Dashboard",
            "stats", Map.of(
                "totalUsers", 100,
                "activeUsers", 85,
                "pendingRequests", 5
            )
        ));
    }
    
    /**
     * Delete user - cần cả ROLE_ADMIN và DELETE_USER authority
     */
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('DELETE_USER')")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of(
            "message", "User " + id + " deleted successfully"
        ));
    }
    
    /**
     * Generate report - cần WRITE_REPORTS authority
     */
    @PostMapping("/reports/generate")
    @PreAuthorize("hasAuthority('WRITE_REPORTS')")
    public ResponseEntity<Map<String, String>> generateReport() {
        methodSecurityService.generateReport();
        return ResponseEntity.ok(Map.of(
            "message", "Report generated successfully"
        ));
    }
    
    /**
     * System settings - chỉ ADMIN
     */
    @GetMapping("/settings")
    public ResponseEntity<Map<String, Object>> getSettings() {
        return ResponseEntity.ok(Map.of(
            "database", "H2 In-Memory",
            "cacheEnabled", true,
            "maxConnections", 100
        ));
    }
    
    @PutMapping("/settings")
    public ResponseEntity<Map<String, String>> updateSettings(@RequestBody Map<String, Object> settings) {
        return ResponseEntity.ok(Map.of(
            "message", "Settings updated successfully"
        ));
    }
}
