package com.example.spring_cert_notes.boot.autoconfigure.greeting;

/**
 * Default implementation của GreetingService
 */
public class DefaultGreetingService implements GreetingService {
    
    private final GreetingProperties properties;
    
    public DefaultGreetingService(GreetingProperties properties) {
        this.properties = properties;
    }
    
    @Override
    public String greet(String name) {
        String greeting = properties.getPrefix() + ", " + name;
        if (properties.getSuffix() != null && !properties.getSuffix().isEmpty()) {
            greeting += " " + properties.getSuffix();
        }
        return greeting;
    }
    
    @Override
    public String getGreetingPrefix() {
        return properties.getPrefix();
    }
}
