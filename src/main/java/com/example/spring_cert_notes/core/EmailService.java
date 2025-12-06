package com.example.spring_cert_notes.core;

import com.example.spring_cert_notes.Prefixes;
import lombok.Getter;
import lombok.Setter;

/**
 * Email service (no annotation - will be defined using @Bean)
 */
@Setter
@Getter
public class EmailService {

    private String smtpServer;

    public EmailService() {
        System.out.println(Prefixes.CORE_LIFECYCLE + "EmailService: Default constructor");
    }

    public EmailService(String smtpServer) {
        this.smtpServer = smtpServer;
        System.out.println(Prefixes.CORE_LIFECYCLE + "EmailService: Constructor with smtpServer=" + smtpServer);
    }

    public void sendWelcomeEmail(User user) {
        System.out.println(Prefixes.CORE_BEAN + "EmailService: Sending welcome email to " + user.getEmail() + 
                         " via server " + smtpServer);
    }

}
