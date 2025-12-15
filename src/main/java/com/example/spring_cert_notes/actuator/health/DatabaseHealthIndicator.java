package com.example.spring_cert_notes.actuator.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * BÀI 1B: CUSTOM HEALTH INDICATOR - Database
 * 
 * Kiểm tra health của database connection.
 * Thực hiện simple query để verify connection.
 */
@Component("customDatabase")
public class DatabaseHealthIndicator implements HealthIndicator {
    
    private final DataSource dataSource;
    
    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public Health health() {
        long startTime = System.currentTimeMillis();
        
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT 1")) {
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            if (resultSet.next()) {
                return Health.up()
                    .withDetail("database", connection.getMetaData().getDatabaseProductName())
                    .withDetail("version", connection.getMetaData().getDatabaseProductVersion())
                    .withDetail("url", connection.getMetaData().getURL())
                    .withDetail("status", "Connected")
                    .withDetail("responseTime", responseTime + "ms")
                    .withDetail("validationQuery", "SELECT 1")
                    .build();
            }
            
            return Health.down()
                .withDetail("error", "Validation query returned no results")
                .build();
                
        } catch (Exception e) {
            return Health.down()
                .withDetail("database", "Unknown")
                .withDetail("error", e.getMessage())
                .withException(e)
                .build();
        }
    }
}
