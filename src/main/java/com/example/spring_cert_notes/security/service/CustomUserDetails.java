package com.example.spring_cert_notes.security.service;

import com.example.spring_cert_notes.security.entity.Permission;
import com.example.spring_cert_notes.security.entity.Role;
import com.example.spring_cert_notes.security.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * BÀI 2: CUSTOM USER DETAILS
 * 
 * Implement UserDetails interface để Spring Security hiểu user của bạn.
 * Chuyển đổi User entity thành UserDetails mà Spring Security sử dụng.
 */
public class CustomUserDetails implements UserDetails {
    
    private final User user;
    
    public CustomUserDetails(User user) {
        this.user = user;
    }
    
    /**
     * Trả về danh sách authorities (roles + permissions)
     * 
     * Roles: ROLE_USER, ROLE_ADMIN (prefix ROLE_)
     * Permissions: READ_USER, WRITE_USER (không có prefix)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        for (Role role : user.getRoles()) {
            // Thêm role (đã có prefix ROLE_)
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            
            // Thêm permissions của role
            for (Permission permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }
        
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired();
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }
    
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
    
    // Getter để truy cập User entity gốc
    public User getUser() {
        return user;
    }
    
    public Long getUserId() {
        return user.getId();
    }
    
    public String getEmail() {
        return user.getEmail();
    }
}
