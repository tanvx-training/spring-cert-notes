package com.example.spring_cert_notes.security.controller;

import com.example.spring_cert_notes.security.dto.AuthRequest;
import com.example.spring_cert_notes.security.dto.AuthResponse;
import com.example.spring_cert_notes.security.dto.RegisterRequest;
import com.example.spring_cert_notes.security.entity.Role;
import com.example.spring_cert_notes.security.entity.User;
import com.example.spring_cert_notes.security.jwt.JwtService;
import com.example.spring_cert_notes.security.repository.RoleRepository;
import com.example.spring_cert_notes.security.repository.UserRepository;
import com.example.spring_cert_notes.security.service.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * BÀI 4: AUTHENTICATION CONTROLLER
 * 
 * Endpoints cho login và register.
 * Trả về JWT token khi authentication thành công.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    public AuthController(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    
    /**
     * Login endpoint
     * 
     * POST /api/auth/login
     * Body: { "username": "user", "password": "password" }
     * 
     * Returns: JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        // 1. Authenticate user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        
        // 2. Get UserDetails
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        
        // 3. Generate tokens
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        
        // 4. Return response
        return ResponseEntity.ok(new AuthResponse(
            accessToken,
            refreshToken,
            userDetails.getUsername(),
            userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .toList()
        ));
    }
    
    /**
     * Register endpoint
     * 
     * POST /api/auth/register
     * Body: { "username": "newuser", "password": "password", "email": "email@example.com" }
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // 1. Check if username exists
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Username already exists"));
        }
        
        // 2. Check if email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Email already exists"));
        }
        
        // 3. Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        
        // 4. Assign default role
        Role userRole = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.addRole(userRole);
        
        // 5. Save user
        userRepository.save(user);
        
        // 6. Generate token
        CustomUserDetails userDetails = new CustomUserDetails(user);
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        
        return ResponseEntity.ok(new AuthResponse(
            accessToken,
            refreshToken,
            user.getUsername(),
            userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .toList()
        ));
    }
    
    /**
     * Refresh token endpoint
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        
        if (refreshToken == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Refresh token is required"));
        }
        
        try {
            String username = jwtService.extractUsername(refreshToken);
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            CustomUserDetails userDetails = new CustomUserDetails(user);
            
            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                String newAccessToken = jwtService.generateToken(userDetails);
                
                return ResponseEntity.ok(Map.of(
                    "accessToken", newAccessToken,
                    "refreshToken", refreshToken
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid refresh token"));
        }
        
        return ResponseEntity.badRequest()
            .body(Map.of("error", "Invalid refresh token"));
    }
}
