package com.example.spring_cert_notes.actuator.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * BÀI 2B: HTTP REQUEST METRICS
 * 
 * Custom metrics cho HTTP requests.
 */
@Component
public class HttpMetrics {
    
    private final MeterRegistry meterRegistry;
    
    public HttpMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    
    /**
     * Record HTTP request
     */
    public void recordRequest(String method, String uri, int statusCode, long durationMs) {
        // Counter cho requests
        Counter.builder("http.requests.total")
            .description("Total HTTP requests")
            .tag("method", method)
            .tag("uri", uri)
            .tag("status", String.valueOf(statusCode))
            .tag("outcome", getOutcome(statusCode))
            .register(meterRegistry)
            .increment();
        
        // Timer cho response time
        Timer.builder("http.requests.duration")
            .description("HTTP request duration")
            .tag("method", method)
            .tag("uri", uri)
            .register(meterRegistry)
            .record(durationMs, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Record API endpoint call
     */
    public void recordApiCall(String endpoint, String method, boolean success) {
        meterRegistry.counter("api.calls",
            "endpoint", endpoint,
            "method", method,
            "success", String.valueOf(success)
        ).increment();
    }
    
    /**
     * Record slow request
     */
    public void recordSlowRequest(String uri, long durationMs) {
        meterRegistry.counter("http.requests.slow",
            "uri", uri
        ).increment();
        
        meterRegistry.gauge("http.requests.slow.duration", durationMs);
    }
    
    private String getOutcome(int statusCode) {
        if (statusCode >= 200 && statusCode < 300) return "SUCCESS";
        if (statusCode >= 400 && statusCode < 500) return "CLIENT_ERROR";
        if (statusCode >= 500) return "SERVER_ERROR";
        return "UNKNOWN";
    }
}
