package com.example.spring_cert_notes.security.dto;

import java.util.List;

/**
 * Authentication Response DTO
 */
public class AuthResponse {
    
    private String accessToken;
    private String refreshToken;
    private String username;
    private List<String> authorities;
    private String tokenType = "Bearer";
    
    public AuthResponse() {}
    
    public AuthResponse(String accessToken, String refreshToken, String username, List<String> authorities) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
        this.authorities = authorities;
    }
    
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public List<String> getAuthorities() { return authorities; }
    public void setAuthorities(List<String> authorities) { this.authorities = authorities; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
}
