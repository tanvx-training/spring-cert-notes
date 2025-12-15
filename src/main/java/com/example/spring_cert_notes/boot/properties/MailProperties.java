package com.example.spring_cert_notes.boot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

/**
 * BÀI 1B: IMMUTABLE @ConfigurationProperties
 * 
 * Sử dụng constructor binding để tạo immutable configuration.
 * Recommended cho production vì thread-safe.
 * 
 * Properties prefix: mail
 */
@ConfigurationProperties(prefix = "mail")
public class MailProperties {
    
    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final boolean ssl;
    private final List<String> recipients;
    private final Smtp smtp;
    
    /**
     * Constructor binding - tất cả properties được inject qua constructor
     * 
     * @DefaultValue cung cấp giá trị mặc định
     */
    @ConstructorBinding
    public MailProperties(
            @DefaultValue("smtp.example.com") String host,
            @DefaultValue("587") int port,
            String username,
            String password,
            @DefaultValue("true") boolean ssl,
            List<String> recipients,
            @DefaultValue Smtp smtp) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.ssl = ssl;
        this.recipients = recipients != null ? List.copyOf(recipients) : List.of();
        this.smtp = smtp != null ? smtp : new Smtp(true, "TLS");
    }
    
    public static class Smtp {
        private final boolean auth;
        private final String starttls;
        
        @ConstructorBinding
        public Smtp(
                @DefaultValue("true") boolean auth,
                @DefaultValue("TLS") String starttls) {
            this.auth = auth;
            this.starttls = starttls;
        }
        
        public boolean isAuth() { return auth; }
        public String getStarttls() { return starttls; }
    }
    
    // Only getters - immutable
    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isSsl() { return ssl; }
    public List<String> getRecipients() { return recipients; }
    public Smtp getSmtp() { return smtp; }
    
    @Override
    public String toString() {
        return "MailProperties{host='" + host + "', port=" + port + 
               ", ssl=" + ssl + ", recipients=" + recipients + "}";
    }
}
