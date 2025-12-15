package com.example.spring_cert_notes.actuator.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * BÀI 1: CUSTOM HEALTH INDICATOR - External Service
 * 
 * Kiểm tra health của external service (API bên ngoài).
 * Hiển thị trong /actuator/health dưới tên "externalService"
 */
@Component("externalService")
public class ExternalServiceHealthIndicator implements HealthIndicator {
    
    private static final String SERVICE_URL = "https://httpbin.org/status/200";
    private static final int TIMEOUT_MS = 5000;
    
    @Override
    public Health health() {
        long startTime = System.currentTimeMillis();
        
        try {
            boolean isAvailable = checkServiceAvailability();
            long responseTime = System.currentTimeMillis() - startTime;
            
            if (isAvailable) {
                return Health.up()
                    .withDetail("service", "External API")
                    .withDetail("url", SERVICE_URL)
                    .withDetail("status", "Available")
                    .withDetail("responseTime", responseTime + "ms")
                    .build();
            } else {
                return Health.down()
                    .withDetail("service", "External API")
                    .withDetail("url", SERVICE_URL)
                    .withDetail("error", "Service returned non-200 status")
                    .build();
            }
        } catch (Exception e) {
            return Health.down()
                .withDetail("service", "External API")
                .withDetail("url", SERVICE_URL)
                .withDetail("error", e.getMessage())
                .withException(e)
                .build();
        }
    }
    
    private boolean checkServiceAvailability() {
        try {
            URL url = new URL(SERVICE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(TIMEOUT_MS);
            connection.setReadTimeout(TIMEOUT_MS);
            
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            
            return responseCode == 200;
        } catch (Exception e) {
            return false;
        }
    }
}
