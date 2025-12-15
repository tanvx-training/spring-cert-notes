package com.example.spring_cert_notes.actuator.controller;

import com.example.spring_cert_notes.actuator.metrics.BusinessMetrics;
import com.example.spring_cert_notes.actuator.service.MetricsDemoService;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller để demo và test Actuator metrics
 */
@RestController
@RequestMapping("/api/actuator-demo")
public class ActuatorDemoController {
    
    private final MetricsDemoService demoService;
    private final BusinessMetrics businessMetrics;
    
    public ActuatorDemoController(MetricsDemoService demoService, BusinessMetrics businessMetrics) {
        this.demoService = demoService;
        this.businessMetrics = businessMetrics;
    }
    
    /**
     * Simulate user login
     * POST /api/actuator-demo/login?username=john
     */
    @PostMapping("/login")
    @Timed(value = "api.login", description = "Login API timing")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username) {
        demoService.login(username);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "User " + username + " logged in",
            "activeUsers", String.valueOf(businessMetrics.getActiveUsers())
        ));
    }
    
    /**
     * Simulate user logout
     * POST /api/actuator-demo/logout?username=john
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestParam String username) {
        demoService.logout(username);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "User " + username + " logged out",
            "activeUsers", String.valueOf(businessMetrics.getActiveUsers())
        ));
    }
    
    /**
     * Simulate user registration
     * POST /api/actuator-demo/register?username=john&source=web
     */
    @PostMapping("/register")
    @Timed(value = "api.register", description = "Registration API timing")
    public ResponseEntity<Map<String, String>> register(
            @RequestParam String username,
            @RequestParam(defaultValue = "web") String source) {
        demoService.register(username, source);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "User " + username + " registered from " + source
        ));
    }
    
    /**
     * Simulate order creation
     * POST /api/actuator-demo/order?productType=electronics&amount=99.99
     */
    @PostMapping("/order")
    @Timed(value = "api.order", description = "Order API timing")
    public ResponseEntity<Map<String, String>> createOrder(
            @RequestParam String productType,
            @RequestParam double amount) {
        demoService.createOrder(productType, amount);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Order created for " + productType,
            "amount", String.valueOf(amount),
            "pendingOrders", String.valueOf(businessMetrics.getPendingOrders())
        ));
    }
    
    /**
     * Simulate order completion
     * POST /api/actuator-demo/order/complete?orderId=123
     */
    @PostMapping("/order/complete")
    public ResponseEntity<Map<String, String>> completeOrder(@RequestParam String orderId) {
        demoService.completeOrder(orderId);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Order " + orderId + " completed",
            "pendingOrders", String.valueOf(businessMetrics.getPendingOrders())
        ));
    }
    
    /**
     * Simulate error
     * POST /api/actuator-demo/error?type=validation
     */
    @PostMapping("/error")
    public ResponseEntity<Map<String, String>> simulateError(@RequestParam String type) {
        demoService.simulateError(type);
        return ResponseEntity.ok(Map.of(
            "status", "error_recorded",
            "errorType", type
        ));
    }
    
    /**
     * Get current metrics summary
     * GET /api/actuator-demo/metrics-summary
     */
    @GetMapping("/metrics-summary")
    public ResponseEntity<Map<String, Object>> getMetricsSummary() {
        return ResponseEntity.ok(Map.of(
            "activeUsers", businessMetrics.getActiveUsers(),
            "pendingOrders", businessMetrics.getPendingOrders(),
            "message", "Check /actuator/metrics for detailed metrics"
        ));
    }
}
