package com.example.spring_cert_notes.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Moderator Controller - Cần ROLE_ADMIN hoặc ROLE_MODERATOR
 */
@RestController
@RequestMapping("/api/moderator")
public class ModeratorController {
    
    @GetMapping("/pending-posts")
    public ResponseEntity<List<Map<String, Object>>> getPendingPosts() {
        return ResponseEntity.ok(List.of(
            Map.of("id", 1, "title", "Post 1", "status", "PENDING"),
            Map.of("id", 2, "title", "Post 2", "status", "PENDING"),
            Map.of("id", 3, "title", "Post 3", "status", "PENDING")
        ));
    }
    
    @PostMapping("/posts/{id}/approve")
    public ResponseEntity<Map<String, String>> approvePost(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of(
            "message", "Post " + id + " approved"
        ));
    }
    
    @PostMapping("/posts/{id}/reject")
    public ResponseEntity<Map<String, String>> rejectPost(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(Map.of(
            "message", "Post " + id + " rejected",
            "reason", reason
        ));
    }
    
    /**
     * Ban user - chỉ ADMIN mới được ban
     */
    @PostMapping("/users/{id}/ban")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> banUser(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of(
            "message", "User " + id + " has been banned"
        ));
    }
}
