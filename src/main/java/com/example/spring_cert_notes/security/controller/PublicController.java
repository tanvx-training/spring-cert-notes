package com.example.spring_cert_notes.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Public Controller - Không cần authentication
 */
@RestController
@RequestMapping("/api/public")
public class PublicController {
    
    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> hello() {
        return ResponseEntity.ok(Map.of(
            "message", "Hello from public endpoint!",
            "status", "No authentication required"
        ));
    }
    
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
            "app", "Spring Security Demo",
            "version", "1.0.0",
            "endpoints", Map.of(
                "public", "/api/public/**",
                "user", "/api/user/** (ROLE_USER)",
                "admin", "/api/admin/** (ROLE_ADMIN)",
                "moderator", "/api/moderator/** (ROLE_ADMIN or ROLE_MODERATOR)"
            )
        ));
    }
}
