package com.example.spring_cert_notes.boot;

import com.example.spring_cert_notes.Prefixes;
import com.example.spring_cert_notes.boot.autoconfigure.greeting.GreetingService;
import com.example.spring_cert_notes.boot.profiles.ProfileService;
import com.example.spring_cert_notes.boot.properties.AppProperties;
import com.example.spring_cert_notes.boot.properties.MailProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Demo Runner cho Spring Boot Essentials
 */
@Configuration
public class BootDemo {
    
    @Bean
    public CommandLineRunner bootDemoRunner(
            AppProperties appProperties,
            MailProperties mailProperties,
            ProfileService profileService,
            GreetingService greetingService) {
        
        return args -> {
            System.out.println("\n" + Prefixes.BOOT + "=== SPRING BOOT ESSENTIALS DEMO ===\n");
            
            // 1. @ConfigurationProperties Demo
            System.out.println(Prefixes.BOOT_AUTOCONFIG + "1. @ConfigurationProperties:");
            System.out.println("   App Name: " + appProperties.getName());
            System.out.println("   Version: " + appProperties.getVersion());
            System.out.println("   Debug Enabled: " + appProperties.isDebugEnabled());
            System.out.println("   Max Connections: " + appProperties.getMaxConnections());
            System.out.println("   Timeout: " + appProperties.getTimeout());
            System.out.println("   Servers: " + appProperties.getServers());
            System.out.println("   Features: " + appProperties.getFeatures());
            System.out.println("   Server Config: " + appProperties.getServer());
            
            // 2. Immutable Properties Demo
            System.out.println("\n" + Prefixes.BOOT_PROPERTIES + "2. Immutable @ConfigurationProperties:");
            System.out.println("   Mail Host: " + mailProperties.getHost());
            System.out.println("   Mail Port: " + mailProperties.getPort());
            System.out.println("   SSL Enabled: " + mailProperties.isSsl());
            System.out.println("   Recipients: " + mailProperties.getRecipients());
            
            // 3. Profiles Demo
            System.out.println("\n" + Prefixes.BOOT + "3. Profiles:");
            System.out.println("   Active Profiles: " + profileService.getActiveProfiles());
            System.out.println("   Default Profiles: " + profileService.getDefaultProfiles());
            System.out.println("   Is 'dev' active: " + profileService.isProfileActive("dev"));
            
            // 4. Auto-Configuration Demo
            System.out.println("\n" + Prefixes.BOOT_AUTOCONFIG + "4. Auto-Configuration:");
            System.out.println("   Greeting: " + greetingService.greet("World"));
            System.out.println("   Greeting Prefix: " + greetingService.getGreetingPrefix());
            
            System.out.println("\n" + Prefixes.BOOT + "=== DEMO COMPLETE ===\n");
        };
    }
}
