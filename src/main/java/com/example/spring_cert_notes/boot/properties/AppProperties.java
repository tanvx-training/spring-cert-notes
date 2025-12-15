package com.example.spring_cert_notes.boot.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BÀI 1: @ConfigurationProperties
 * 
 * Bind properties từ application.properties/yml vào Java object.
 * Type-safe configuration với validation support.
 * 
 * Properties prefix: app
 * Ví dụ: app.name, app.version, app.server.host
 */
@ConfigurationProperties(prefix = "app")
@Validated  // Enable validation
public class AppProperties {
    
    /**
     * Application name
     * Property: app.name
     */
    @NotBlank(message = "App name is required")
    private String name;
    
    /**
     * Application version
     * Property: app.version
     */
    private String version = "1.0.0";
    
    /**
     * Application description
     * Property: app.description
     */
    private String description;
    
    /**
     * Enable debug mode
     * Property: app.debug-enabled (kebab-case) hoặc app.debugEnabled (camelCase)
     */
    private boolean debugEnabled = false;
    
    /**
     * Max connections
     * Property: app.max-connections
     */
    @Min(1)
    @Max(1000)
    private int maxConnections = 100;
    
    /**
     * Timeout duration
     * Property: app.timeout (e.g., "30s", "5m", "1h")
     */
    private Duration timeout = Duration.ofSeconds(30);
    
    /**
     * List of servers
     * Property: app.servers[0], app.servers[1], ...
     */
    private List<String> servers = new ArrayList<>();
    
    /**
     * Map of features
     * Property: app.features.feature1=true, app.features.feature2=false
     */
    private Map<String, Boolean> features = new HashMap<>();
    
    /**
     * Nested object
     * Property: app.server.host, app.server.port
     */
    private Server server = new Server();
    
    /**
     * Database configuration
     * Property: app.database.*
     */
    private Database database = new Database();
    
    // ============================================================
    // NESTED CLASSES
    // ============================================================
    
    public static class Server {
        private String host = "localhost";
        private int port = 8080;
        private boolean ssl = false;
        
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public boolean isSsl() { return ssl; }
        public void setSsl(boolean ssl) { this.ssl = ssl; }
        
        @Override
        public String toString() {
            return "Server{host='" + host + "', port=" + port + ", ssl=" + ssl + "}";
        }
    }
    
    public static class Database {
        private String url = "jdbc:h2:mem:testdb";
        private String username = "sa";
        private String password = "";
        private Pool pool = new Pool();
        
        public static class Pool {
            private int minSize = 5;
            private int maxSize = 20;
            private Duration idleTimeout = Duration.ofMinutes(10);
            
            public int getMinSize() { return minSize; }
            public void setMinSize(int minSize) { this.minSize = minSize; }
            public int getMaxSize() { return maxSize; }
            public void setMaxSize(int maxSize) { this.maxSize = maxSize; }
            public Duration getIdleTimeout() { return idleTimeout; }
            public void setIdleTimeout(Duration idleTimeout) { this.idleTimeout = idleTimeout; }
        }
        
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public Pool getPool() { return pool; }
        public void setPool(Pool pool) { this.pool = pool; }
    }
    
    // ============================================================
    // GETTERS AND SETTERS
    // ============================================================
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isDebugEnabled() { return debugEnabled; }
    public void setDebugEnabled(boolean debugEnabled) { this.debugEnabled = debugEnabled; }
    public int getMaxConnections() { return maxConnections; }
    public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }
    public Duration getTimeout() { return timeout; }
    public void setTimeout(Duration timeout) { this.timeout = timeout; }
    public List<String> getServers() { return servers; }
    public void setServers(List<String> servers) { this.servers = servers; }
    public Map<String, Boolean> getFeatures() { return features; }
    public void setFeatures(Map<String, Boolean> features) { this.features = features; }
    public Server getServer() { return server; }
    public void setServer(Server server) { this.server = server; }
    public Database getDatabase() { return database; }
    public void setDatabase(Database database) { this.database = database; }
    
    @Override
    public String toString() {
        return "AppProperties{" +
            "name='" + name + '\'' +
            ", version='" + version + '\'' +
            ", debugEnabled=" + debugEnabled +
            ", maxConnections=" + maxConnections +
            ", timeout=" + timeout +
            ", servers=" + servers +
            ", features=" + features +
            ", server=" + server +
            '}';
    }
}
