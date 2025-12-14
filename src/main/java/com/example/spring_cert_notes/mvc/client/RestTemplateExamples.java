package com.example.spring_cert_notes.mvc.client;

import com.example.spring_cert_notes.Prefixes;
import com.example.spring_cert_notes.mvc.dto.UserDto;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * RESTTEMPLATE EXAMPLES
 * <p>
 * RestTemplate is a synchronous HTTP client for consuming REST APIs.
 * <p>
 * Note: RestTemplate is in maintenance mode.
 * For new projects, consider using WebClient (reactive) or RestClient (Spring 6.1+)
 */
@Component
public class RestTemplateExamples {
    
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/api/users";
    
    public RestTemplateExamples() {
        this.restTemplate = new RestTemplate();
    }
    
    // ============================================================
    // GET Requests
    // ============================================================
    
    /**
     * GET - Simple request
     */
    public UserDto getUser(Long id) {
        System.out.println(Prefixes.MVC_CLIENT + "GET /api/users/" + id);
        return restTemplate.getForObject(baseUrl + "/" + id, UserDto.class);
    }
    
    /**
     * GET - With ResponseEntity (access headers, status)
     */
    public ResponseEntity<UserDto> getUserWithResponse(Long id) {
        System.out.println(Prefixes.MVC_CLIENT + "GET /api/users/" + id + " (with ResponseEntity)");
        return restTemplate.getForEntity(baseUrl + "/" + id, UserDto.class);
    }
    
    /**
     * GET - List of objects
     */
    public List<UserDto> getAllUsers() {
        System.out.println(Prefixes.MVC_CLIENT + "GET /api/users");
        UserDto[] users = restTemplate.getForObject(baseUrl, UserDto[].class);
        return users != null ? Arrays.asList(users) : List.of();
    }
    
    /**
     * GET - With query parameters
     */
    public List<UserDto> searchUsers(String firstName) {
        System.out.println(Prefixes.MVC_CLIENT + "GET /api/users/search?firstName=" + firstName);
        String url = baseUrl + "/search?firstName={firstName}";
        UserDto[] users = restTemplate.getForObject(url, UserDto[].class, firstName);
        return users != null ? Arrays.asList(users) : List.of();
    }
    
    // ============================================================
    // POST Requests
    // ============================================================
    
    /**
     * POST - Create resource
     */
    public UserDto createUser(UserDto user) {
        System.out.println(Prefixes.MVC_CLIENT + "POST /api/users");
        return restTemplate.postForObject(baseUrl, user, UserDto.class);
    }
    
    /**
     * POST - With ResponseEntity
     */
    public ResponseEntity<UserDto> createUserWithResponse(UserDto user) {
        System.out.println(Prefixes.MVC_CLIENT + "POST /api/users (with ResponseEntity)");
        return restTemplate.postForEntity(baseUrl, user, UserDto.class);
    }
    
    /**
     * POST - With custom headers
     */
    public ResponseEntity<UserDto> createUserWithHeaders(UserDto user) {
        System.out.println(Prefixes.MVC_CLIENT + "POST /api/users (with headers)");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Custom-Header", "custom-value");
        
        HttpEntity<UserDto> request = new HttpEntity<>(user, headers);
        return restTemplate.postForEntity(baseUrl, request, UserDto.class);
    }
    
    // ============================================================
    // PUT Requests
    // ============================================================
    
    /**
     * PUT - Update resource
     */
    public void updateUser(Long id, UserDto user) {
        System.out.println(Prefixes.MVC_CLIENT + "PUT /api/users/" + id);
        restTemplate.put(baseUrl + "/" + id, user);
    }
    
    /**
     * PUT - With exchange (get response)
     */
    public ResponseEntity<UserDto> updateUserWithResponse(Long id, UserDto user) {
        System.out.println(Prefixes.MVC_CLIENT + "PUT /api/users/" + id + " (with exchange)");
        HttpEntity<UserDto> request = new HttpEntity<>(user);
        return restTemplate.exchange(baseUrl + "/" + id, HttpMethod.PUT, request, UserDto.class);
    }
    
    // ============================================================
    // DELETE Requests
    // ============================================================
    
    /**
     * DELETE - Delete resource
     */
    public void deleteUser(Long id) {
        System.out.println(Prefixes.MVC_CLIENT + "DELETE /api/users/" + id);
        restTemplate.delete(baseUrl + "/" + id);
    }
    
    /**
     * DELETE - With exchange (get response)
     */
    public ResponseEntity<Void> deleteUserWithResponse(Long id) {
        System.out.println(Prefixes.MVC_CLIENT + "DELETE /api/users/" + id + " (with exchange)");
        return restTemplate.exchange(baseUrl + "/" + id, HttpMethod.DELETE, null, Void.class);
    }
    
    // ============================================================
    // Exchange - Generic method for any HTTP method
    // ============================================================
    
    /**
     * exchange() - Most flexible method
     */
    public ResponseEntity<UserDto> exchangeExample(Long id) {
        System.out.println(Prefixes.MVC_CLIENT + "Using exchange() method");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer token123");
        
        HttpEntity<Void> request = new HttpEntity<>(headers);
        
        return restTemplate.exchange(
            baseUrl + "/" + id,
            HttpMethod.GET,
            request,
            UserDto.class
        );
    }
}
