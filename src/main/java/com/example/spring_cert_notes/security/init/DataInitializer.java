package com.example.spring_cert_notes.security.init;

import com.example.spring_cert_notes.security.entity.Permission;
import com.example.spring_cert_notes.security.entity.Role;
import com.example.spring_cert_notes.security.entity.User;
import com.example.spring_cert_notes.security.repository.RoleRepository;
import com.example.spring_cert_notes.security.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data Initializer - Tạo sample data cho Security Demo
 * 
 * Chạy khi application start với profile "custom" hoặc "jwt"
 */
@Configuration
@Profile({"custom", "jwt"})
public class DataInitializer {
    
    @Bean
    @Transactional
    public CommandLineRunner initSecurityData(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            EntityManager entityManager) {
        
        return args -> {
            // Skip if data already exists
            if (roleRepository.existsByName("ROLE_USER")) {
                return;
            }
            
            System.out.println("=== Initializing Security Data ===");
            
            // 1. Create Permissions
            Permission readUser = new Permission("READ_USER", "Can read user data");
            Permission writeUser = new Permission("WRITE_USER", "Can write user data");
            Permission deleteUser = new Permission("DELETE_USER", "Can delete users");
            Permission readReports = new Permission("READ_REPORTS", "Can read reports");
            Permission writeReports = new Permission("WRITE_REPORTS", "Can write reports");
            
            entityManager.persist(readUser);
            entityManager.persist(writeUser);
            entityManager.persist(deleteUser);
            entityManager.persist(readReports);
            entityManager.persist(writeReports);
            
            // 2. Create Roles
            Role roleUser = new Role("ROLE_USER", "Standard user role");
            roleUser.addPermission(readUser);
            
            Role roleModerator = new Role("ROLE_MODERATOR", "Moderator role");
            roleModerator.addPermission(readUser);
            roleModerator.addPermission(writeUser);
            roleModerator.addPermission(readReports);
            
            Role roleAdmin = new Role("ROLE_ADMIN", "Administrator role");
            roleAdmin.addPermission(readUser);
            roleAdmin.addPermission(writeUser);
            roleAdmin.addPermission(deleteUser);
            roleAdmin.addPermission(readReports);
            roleAdmin.addPermission(writeReports);
            
            roleRepository.save(roleUser);
            roleRepository.save(roleModerator);
            roleRepository.save(roleAdmin);
            
            // 3. Create Users
            User user = new User("user", passwordEncoder.encode("password"), "user@example.com");
            user.addRole(roleUser);
            
            User moderator = new User("mod", passwordEncoder.encode("mod123"), "mod@example.com");
            moderator.addRole(roleUser);
            moderator.addRole(roleModerator);
            
            User admin = new User("admin", passwordEncoder.encode("admin123"), "admin@example.com");
            admin.addRole(roleUser);
            admin.addRole(roleAdmin);
            
            userRepository.save(user);
            userRepository.save(moderator);
            userRepository.save(admin);
            
            System.out.println("=== Security Data Initialized ===");
            System.out.println("Users created:");
            System.out.println("  - user/password (ROLE_USER)");
            System.out.println("  - mod/mod123 (ROLE_USER, ROLE_MODERATOR)");
            System.out.println("  - admin/admin123 (ROLE_USER, ROLE_ADMIN)");
        };
    }
}
