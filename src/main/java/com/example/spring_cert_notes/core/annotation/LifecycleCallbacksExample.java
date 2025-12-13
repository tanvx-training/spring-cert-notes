package com.example.spring_cert_notes.core.annotation;

import com.example.spring_cert_notes.Prefixes;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * LIFECYCLE CALLBACKS EXAMPLES
 * <p>
 * Bean Lifecycle Order:
 * 1. Constructor
 * 2. Dependency Injection
 * 3. @PostConstruct
 * 4. InitializingBean.afterPropertiesSet()
 * 5. Custom init-method
 * 6. Bean is ready for use
 * ...
 * 7. @PreDestroy
 * 8. DisposableBean.destroy()
 * 9. Custom destroy-method
 */

// ============================================================
// METHOD 1: @PostConstruct and @PreDestroy (RECOMMENDED)
// ============================================================
@Getter
@Component
class AnnotationLifecycleBean {
    
    private String status;
    
    public AnnotationLifecycleBean() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "1. AnnotationLifecycleBean: Constructor called");
        this.status = "constructed";
    }
    
    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "2. AnnotationLifecycleBean: @PostConstruct - Initializing resources");
        this.status = "initialized";
        // Initialize resources: DB connections, caches, thread pools, etc.
    }
    
    @PreDestroy
    public void cleanup() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "3. AnnotationLifecycleBean: @PreDestroy - Cleaning up resources");
        this.status = "destroyed";
        // Cleanup: close connections, release resources, etc.
    }

}

// ============================================================
// METHOD 2: InitializingBean and DisposableBean interfaces
// ============================================================
@Getter
@Component
class InterfaceLifecycleBean implements InitializingBean, DisposableBean {
    
    private boolean initialized = false;
    
    public InterfaceLifecycleBean() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "1. InterfaceLifecycleBean: Constructor called");
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(Prefixes.CORE_LIFECYCLE + "2. InterfaceLifecycleBean: afterPropertiesSet() - InitializingBean");
        this.initialized = true;
    }
    
    @Override
    public void destroy() throws Exception {
        System.out.println(Prefixes.CORE_LIFECYCLE + "3. InterfaceLifecycleBean: destroy() - DisposableBean");
    }

}

// ============================================================
// METHOD 3: Combined - All lifecycle methods
// ============================================================
@Component
class CombinedLifecycleBean implements InitializingBean, DisposableBean {
    
    private int initOrder = 0;
    
    public CombinedLifecycleBean() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "[" + (++initOrder) + "] CombinedLifecycleBean: Constructor");
    }
    
    @PostConstruct
    public void postConstruct() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "[" + (++initOrder) + "] CombinedLifecycleBean: @PostConstruct");
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(Prefixes.CORE_LIFECYCLE + "[" + (++initOrder) + "] CombinedLifecycleBean: afterPropertiesSet()");
    }
    
    // Note: Custom init-method would be called here if configured via @Bean(initMethod="...")
    
    @PreDestroy
    public void preDestroy() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "[1] CombinedLifecycleBean: @PreDestroy");
    }
    
    @Override
    public void destroy() throws Exception {
        System.out.println(Prefixes.CORE_LIFECYCLE + "[2] CombinedLifecycleBean: destroy()");
    }
}

// ============================================================
// PRACTICAL EXAMPLE: Resource Management
// ============================================================
@Component
class DatabaseConnectionPool {
    
    private boolean connected = false;
    private int poolSize = 10;

    public DatabaseConnectionPool() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "DatabaseConnectionPool: Constructor");
    }
    
    @PostConstruct
    public void initializePool() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "DatabaseConnectionPool: @PostConstruct - Creating " + poolSize + " connections");
        // Simulate creating connection pool
        this.connected = true;
        System.out.println(Prefixes.CORE_LIFECYCLE + "DatabaseConnectionPool: Connection pool ready");
    }
    
    @PreDestroy
    public void closePool() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "DatabaseConnectionPool: @PreDestroy - Closing all connections");
        // Simulate closing connections
        this.connected = false;
        System.out.println(Prefixes.CORE_LIFECYCLE + "DatabaseConnectionPool: All connections closed");
    }
    
    public boolean isConnected() {
        return connected;
    }
}

// ============================================================
// PRACTICAL EXAMPLE: Cache Initialization
// ============================================================
@Getter
@Component
class CacheManager {
    
    private boolean cacheWarmed = false;
    
    @PostConstruct
    public void warmUpCache() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "CacheManager: @PostConstruct - Warming up cache");
        // Load frequently accessed data into cache
        this.cacheWarmed = true;
        System.out.println(Prefixes.CORE_LIFECYCLE + "CacheManager: Cache warmed up successfully");
    }
    
    @PreDestroy
    public void flushCache() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "CacheManager: @PreDestroy - Flushing cache to disk");
        // Persist cache data if needed
        System.out.println(Prefixes.CORE_LIFECYCLE + "CacheManager: Cache flushed");
    }

}
