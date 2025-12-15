package com.example.spring_cert_notes.boot.autoconfigure.greeting;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties cho Greeting Auto-Configuration
 * 
 * Properties:
 * - greeting.enabled=true
 * - greeting.prefix=Hello
 * - greeting.suffix=!
 */
@ConfigurationProperties(prefix = "greeting")
public class GreetingProperties {
    
    /**
     * Enable/disable greeting service
     */
    private boolean enabled = true;
    
    /**
     * Greeting prefix (e.g., "Hello", "Hi", "Welcome")
     */
    private String prefix = "Hello";
    
    /**
     * Greeting suffix (e.g., "!", ".", "")
     */
    private String suffix = "!";
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }
    public String getSuffix() { return suffix; }
    public void setSuffix(String suffix) { this.suffix = suffix; }
}
