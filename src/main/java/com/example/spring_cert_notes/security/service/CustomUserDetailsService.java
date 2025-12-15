package com.example.spring_cert_notes.security.service;

import com.example.spring_cert_notes.security.entity.User;
import com.example.spring_cert_notes.security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * BÀI 2: CUSTOM USER DETAILS SERVICE
 * 
 * Implement UserDetailsService để load user từ database.
 * Spring Security sẽ gọi loadUserByUsername() khi authenticate.
 * 
 * Đây là cách phổ biến nhất để integrate với database thực tế.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Load user từ database theo username
     * 
     * @param username username để tìm kiếm
     * @return UserDetails object
     * @throws UsernameNotFoundException nếu không tìm thấy user
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameWithRolesAndPermissions(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                "User not found with username: " + username
            ));
        
        return new CustomUserDetails(user);
    }
}
