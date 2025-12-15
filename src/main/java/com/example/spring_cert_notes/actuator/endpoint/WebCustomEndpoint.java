package com.example.spring_cert_notes.actuator.endpoint;

import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * BÀI 4B: WEB-SPECIFIC CUSTOM ENDPOINT
 * 
 * @RestControllerEndpoint cho phép sử dụng Spring MVC annotations.
 * Linh hoạt hơn @Endpoint nhưng chỉ hoạt động với web.
 * 
 * Endpoint: /actuator/custom-api
 */
@Component
@RestControllerEndpoint(id = "custom-api")
public class WebCustomEndpoint {
    
    /**
     * GET /actuator/custom-api/status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now().toString());
        status.put("message", "Custom API endpoint is working");
        return ResponseEntity.ok(status);
    }
    
    /**
     * GET /actuator/custom-api/echo/{message}
     */
    @GetMapping("/echo/{message}")
    public ResponseEntity<Map<String, String>> echo(@PathVariable String message) {
        Map<String, String> response = new HashMap<>();
        response.put("original", message);
        response.put("echo", message.toUpperCase());
        response.put("length", String.valueOf(message.length()));
        return ResponseEntity.ok(response);
    }
    
    /**
     * POST /actuator/custom-api/process
     */
    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> process(@RequestBody Map<String, Object> input) {
        Map<String, Object> result = new HashMap<>();
        result.put("received", input);
        result.put("processedAt", LocalDateTime.now().toString());
        result.put("status", "processed");
        return ResponseEntity.ok(result);
    }
}
