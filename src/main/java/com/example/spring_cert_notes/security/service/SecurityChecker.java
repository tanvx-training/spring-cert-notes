package com.example.spring_cert_notes.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Custom Security Checker Bean
 * 
 * Sử dụng trong @PreAuthorize expressions:
 * @PreAuthorize("@securityChecker.canAccess(#resourceId, authentication)")
 */
@Component("securityChecker")
public class SecurityChecker {
    
    /**
     * Kiểm tra user có quyền access resource không
     */
    public boolean canAccess(Long resourceId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        // Admin có quyền access tất cả
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (isAdmin) {
            return true;
        }
        
        // Logic kiểm tra quyền cụ thể
        // Ví dụ: kiểm tra user có sở hữu resource không
        return checkOwnership(resourceId, authentication.getName());
    }
    
    /**
     * Kiểm tra user có phải owner của resource không
     */
    public boolean isOwner(String owner, Authentication authentication) {
        return authentication != null && 
               authentication.getName().equals(owner);
    }
    
    /**
     * Kiểm tra user có permission cụ thể không
     */
    public boolean hasPermission(Authentication authentication, String permission) {
        return authentication != null &&
               authentication.getAuthorities().stream()
                   .anyMatch(a -> a.getAuthority().equals(permission));
    }
    
    private boolean checkOwnership(Long resourceId, String username) {
        // Giả lập kiểm tra ownership từ database
        // Trong thực tế, sẽ query database
        return resourceId != null && resourceId < 100;
    }
}
