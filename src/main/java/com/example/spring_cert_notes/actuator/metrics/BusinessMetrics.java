package com.example.spring_cert_notes.actuator.metrics;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * BÀI 2: CUSTOM METRICS VỚI MICROMETER
 * 
 * Micrometer là metrics facade cho Spring Boot.
 * Hỗ trợ nhiều monitoring systems: Prometheus, Datadog, New Relic, etc.
 * 
 * Các loại metrics:
 * - Counter: Đếm số lần (chỉ tăng)
 * - Gauge: Giá trị hiện tại (có thể tăng/giảm)
 * - Timer: Đo thời gian thực thi
 * - Distribution Summary: Phân phối giá trị
 */
@Component
public class BusinessMetrics {
    
    private final MeterRegistry meterRegistry;
    
    // Counters
    private final Counter userLoginCounter;
    private final Counter userRegistrationCounter;
    private final Counter orderCounter;
    private final Counter errorCounter;
    
    // Gauges
    private final AtomicInteger activeUsers = new AtomicInteger(0);
    private final AtomicInteger pendingOrders = new AtomicInteger(0);
    
    // Timers
    private final Timer orderProcessingTimer;
    private final Timer paymentProcessingTimer;
    
    // Distribution Summary
    private final DistributionSummary orderAmountSummary;
    
    public BusinessMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        // ============================================================
        // 1. COUNTERS - Đếm số lần (chỉ tăng)
        // ============================================================
        
        this.userLoginCounter = Counter.builder("user.logins")
            .description("Total number of user logins")
            .tag("type", "authentication")
            .register(meterRegistry);
        
        this.userRegistrationCounter = Counter.builder("user.registrations")
            .description("Total number of user registrations")
            .tag("type", "registration")
            .register(meterRegistry);
        
        this.orderCounter = Counter.builder("orders.created")
            .description("Total number of orders created")
            .register(meterRegistry);
        
        this.errorCounter = Counter.builder("errors.total")
            .description("Total number of errors")
            .register(meterRegistry);
        
        // ============================================================
        // 2. GAUGES - Giá trị hiện tại
        // ============================================================
        
        Gauge.builder("users.active", activeUsers, AtomicInteger::get)
            .description("Number of currently active users")
            .register(meterRegistry);
        
        Gauge.builder("orders.pending", pendingOrders, AtomicInteger::get)
            .description("Number of pending orders")
            .register(meterRegistry);
        
        // ============================================================
        // 3. TIMERS - Đo thời gian
        // ============================================================
        
        this.orderProcessingTimer = Timer.builder("order.processing.time")
            .description("Time taken to process an order")
            .tag("operation", "order")
            .register(meterRegistry);
        
        this.paymentProcessingTimer = Timer.builder("payment.processing.time")
            .description("Time taken to process a payment")
            .tag("operation", "payment")
            .register(meterRegistry);
        
        // ============================================================
        // 4. DISTRIBUTION SUMMARY - Phân phối giá trị
        // ============================================================
        
        this.orderAmountSummary = DistributionSummary.builder("order.amount")
            .description("Distribution of order amounts")
            .baseUnit("dollars")
            .publishPercentiles(0.5, 0.75, 0.95, 0.99)
            .register(meterRegistry);
    }
    
    // ============================================================
    // COUNTER METHODS
    // ============================================================
    
    /**
     * Record user login
     */
    public void recordUserLogin(String username) {
        userLoginCounter.increment();
        
        // Counter với dynamic tags
        meterRegistry.counter("user.logins.detailed", 
            "username", username,
            "method", "password"
        ).increment();
    }
    
    /**
     * Record user login với method
     */
    public void recordUserLogin(String username, String loginMethod) {
        meterRegistry.counter("user.logins.detailed",
            "username", username,
            "method", loginMethod
        ).increment();
    }
    
    /**
     * Record user registration
     */
    public void recordUserRegistration(String source) {
        userRegistrationCounter.increment();
        
        meterRegistry.counter("user.registrations.detailed",
            "source", source
        ).increment();
    }
    
    /**
     * Record order creation
     */
    public void recordOrderCreated(String productType) {
        orderCounter.increment();
        
        meterRegistry.counter("orders.created.detailed",
            "productType", productType
        ).increment();
    }
    
    /**
     * Record error
     */
    public void recordError(String errorType, String service) {
        errorCounter.increment();
        
        meterRegistry.counter("errors.detailed",
            "type", errorType,
            "service", service
        ).increment();
    }
    
    // ============================================================
    // GAUGE METHODS
    // ============================================================
    
    public void userLoggedIn() {
        activeUsers.incrementAndGet();
    }
    
    public void userLoggedOut() {
        activeUsers.decrementAndGet();
    }
    
    public void orderCreated() {
        pendingOrders.incrementAndGet();
    }
    
    public void orderCompleted() {
        pendingOrders.decrementAndGet();
    }
    
    public int getActiveUsers() {
        return activeUsers.get();
    }
    
    public int getPendingOrders() {
        return pendingOrders.get();
    }
    
    // ============================================================
    // TIMER METHODS
    // ============================================================
    
    /**
     * Record order processing time
     */
    public void recordOrderProcessingTime(Runnable operation) {
        orderProcessingTimer.record(operation);
    }
    
    /**
     * Record payment processing time
     */
    public void recordPaymentProcessingTime(Runnable operation) {
        paymentProcessingTimer.record(operation);
    }
    
    /**
     * Get timer for manual recording
     */
    public Timer.Sample startTimer() {
        return Timer.start(meterRegistry);
    }
    
    public void stopTimer(Timer.Sample sample, String timerName) {
        sample.stop(meterRegistry.timer(timerName));
    }
    
    // ============================================================
    // DISTRIBUTION SUMMARY METHODS
    // ============================================================
    
    /**
     * Record order amount
     */
    public void recordOrderAmount(double amount) {
        orderAmountSummary.record(amount);
    }
}
