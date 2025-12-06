package com.example.spring_cert_notes.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 * EXERCISE 3: 6 BEAN SCOPES IN SPRING
 * <p>
 * 1. SINGLETON (default): Single instance for entire ApplicationContext
 * 2. PROTOTYPE: Creates new instance on each request
 * 3. REQUEST: 1 instance per HTTP request (web apps only)
 * 4. SESSION: 1 instance per HTTP session (web apps only)
 * 5. APPLICATION: 1 instance for entire ServletContext (web apps only)
 * 6. WEBSOCKET: 1 instance per WebSocket session
 */
@Configuration
public class BeanScopesExample {

    /**
     * SCOPE 1: SINGLETON (Default)
     * - Chỉ có 1 instance duy nhất
     * - Được tạo khi ApplicationContext khởi động (eager) hoặc khi được request lần đầu (lazy)
     * - Dùng cho stateless beans
     */
    @Bean
    @Scope("singleton")
    public SingletonBean singletonBean() {
        return new SingletonBean();
    }

    /**
     * SCOPE 2: PROTOTYPE
     * - Tạo instance mới mỗi khi getBean() được gọi
     * - Spring không quản lý lifecycle hoàn toàn (không gọi @PreDestroy)
     * - Dùng cho stateful beans
     */
    @Bean
    @Scope("prototype")
    public PrototypeBean prototypeBean() {
        return new PrototypeBean();
    }

    /**
     * SCOPE 3: REQUEST
     * - 1 instance mới cho mỗi HTTP request
     * - Chỉ hoạt động trong Spring Web application
     * - Bean bị destroy khi request hoàn thành
     */
    @Bean
    @RequestScope
    // @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public RequestBean requestBean() {
        return new RequestBean();
    }

    /**
     * SCOPE 4: SESSION
     * - 1 instance cho mỗi HTTP session
     * - Bean tồn tại trong suốt session
     * - Bị destroy khi session timeout hoặc invalidate
     */
    @Bean
    @SessionScope
    // @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SessionBean sessionBean() {
        return new SessionBean();
    }

    /**
     * SCOPE 5: APPLICATION
     * - 1 instance cho toàn bộ ServletContext
     * - Giống singleton nhưng trong context của ServletContext
     * - Dùng cho application-wide state
     */
    @Bean
    @ApplicationScope
    // @Scope(value = "application", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ApplicationBean applicationBean() {
        return new ApplicationBean();
    }

    /**
     * SCOPE 6: WEBSOCKET
     * - 1 instance cho mỗi WebSocket session
     * - Cần Spring WebSocket support
     */
    @Bean
    @Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public WebSocketBean webSocketBean() {
        return new WebSocketBean();
    }

    // Inner classes for demonstration
    public static class SingletonBean {
        private final long createdAt = System.currentTimeMillis();
        public SingletonBean() {
            System.out.println(com.example.spring_cert_notes.Prefixes.CORE_BEAN + "SingletonBean created at: " + createdAt);
        }
        public long getCreatedAt() { return createdAt; }
    }

    public static class PrototypeBean {
        private final long createdAt = System.currentTimeMillis();
        public PrototypeBean() {
            System.out.println(com.example.spring_cert_notes.Prefixes.CORE_BEAN + "PrototypeBean created at: " + createdAt);
        }
        public long getCreatedAt() { return createdAt; }
    }

    public static class RequestBean {
        private final long createdAt = System.currentTimeMillis();
        public RequestBean() {
            System.out.println(com.example.spring_cert_notes.Prefixes.CORE_BEAN + "RequestBean created at: " + createdAt);
        }
    }

    public static class SessionBean {
        private String sessionData;
        public SessionBean() {
            System.out.println(com.example.spring_cert_notes.Prefixes.CORE_BEAN + "SessionBean created");
        }
        public void setSessionData(String data) { this.sessionData = data; }
        public String getSessionData() { return sessionData; }
    }

    public static class ApplicationBean {
        private int visitorCount = 0;
        public ApplicationBean() {
            System.out.println(com.example.spring_cert_notes.Prefixes.CORE_BEAN + "ApplicationBean created");
        }
        public void incrementVisitor() { visitorCount++; }
        public int getVisitorCount() { return visitorCount; }
    }

    public static class WebSocketBean {
        public WebSocketBean() {
            System.out.println(com.example.spring_cert_notes.Prefixes.CORE_BEAN + "WebSocketBean created");
        }
    }
}
