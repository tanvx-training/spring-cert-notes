package com.example.spring_cert_notes.mvc.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Standard error response DTO
 */
@Setter
@Getter
public class ErrorResponse {

    // Getters and Setters
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldError> fieldErrors = new ArrayList<>();
    
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorResponse(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
    
    public void addFieldError(String field, String message) {
        fieldErrors.add(new FieldError(field, message));
    }

    public record FieldError(String field, String message) {

    }
}
