package com.example.spring_cert_notes.boot.autoconfigure.greeting;

/**
 * Service interface cho Custom Auto-Configuration Demo
 */
public interface GreetingService {
    
    String greet(String name);
    
    String getGreetingPrefix();
}
