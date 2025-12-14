package com.example.spring_cert_notes.mvc.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PARAMETER BINDING EXAMPLES
 * <p>
 * Demonstrates various ways to extract data from requests:
 * - @PathVariable - URL path segments
 * - @RequestParam - Query parameters
 * - @RequestHeader - HTTP headers
 * - @RequestBody - Request body
 * - @CookieValue - Cookies
 * - @MatrixVariable - Matrix variables
 */
@RestController
@RequestMapping("/api/examples")
public class ParameterExamplesController {
    
    // ============================================================
    // @PathVariable - URL path segments
    // ============================================================
    
    // /api/examples/users/123
    @GetMapping("/users/{id}")
    public Map<String, Object> pathVariable(@PathVariable Long id) {
        return Map.of("type", "PathVariable", "id", id);
    }
    
    // /api/examples/users/123/orders/456
    @GetMapping("/users/{userId}/orders/{orderId}")
    public Map<String, Object> multiplePathVariables(
            @PathVariable Long userId,
            @PathVariable Long orderId) {
        return Map.of("userId", userId, "orderId", orderId);
    }
    
    // Optional path variable with regex
    // /api/examples/files/document.pdf
    @GetMapping("/files/{filename:.+}")
    public Map<String, Object> pathVariableWithRegex(@PathVariable String filename) {
        return Map.of("filename", filename);
    }
    
    // ============================================================
    // @RequestParam - Query parameters
    // ============================================================
    
    // /api/examples/search?q=spring
    @GetMapping("/search")
    public Map<String, Object> requestParam(@RequestParam String q) {
        return Map.of("type", "RequestParam", "query", q);
    }
    
    // /api/examples/search2?q=spring&page=1&size=10
    @GetMapping("/search2")
    public Map<String, Object> multipleRequestParams(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Map.of("query", q, "page", page, "size", size);
    }
    
    // Optional parameter
    // /api/examples/filter?status=active
    @GetMapping("/filter")
    public Map<String, Object> optionalRequestParam(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) List<String> tags) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", status);
        result.put("tags", tags);
        return result;
    }
    
    // ============================================================
    // @RequestHeader - HTTP headers
    // ============================================================
    
    @GetMapping("/headers")
    public Map<String, Object> requestHeaders(
            @RequestHeader("User-Agent") String userAgent,
            @RequestHeader(value = "X-Custom-Header", required = false) String custom,
            @RequestHeader HttpHeaders allHeaders) {
        return Map.of(
            "userAgent", userAgent,
            "customHeader", custom != null ? custom : "not provided",
            "totalHeaders", allHeaders.size()
        );
    }
    
    // ============================================================
    // @CookieValue - Cookies
    // ============================================================
    
    @GetMapping("/cookies")
    public Map<String, Object> cookieValue(
            @CookieValue(value = "sessionId", required = false) String sessionId) {
        return Map.of("sessionId", sessionId != null ? sessionId : "no session");
    }
    
    // ============================================================
    // @RequestBody - Request body (JSON)
    // ============================================================
    
    @PostMapping("/body")
    public ResponseEntity<Map<String, Object>> requestBody(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(Map.of("received", body));
    }
    
    // ============================================================
    // Combined example
    // ============================================================
    
    @PostMapping("/users/{userId}/orders")
    public Map<String, Object> combinedExample(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "false") boolean notify,
            @RequestHeader("Authorization") String auth,
            @RequestBody Map<String, Object> orderData) {
        return Map.of(
            "userId", userId,
            "notify", notify,
            "authPresent", auth != null,
            "orderData", orderData
        );
    }
}
