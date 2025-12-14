package com.example.spring_cert_notes.mvc.dto;

import jakarta.validation.constraints.*;

/**
 * User DTO with validation
 */
public class UserDto {
    
    private Long id;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be 2-50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be 2-50 characters")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @Min(value = 0, message = "Age must be positive")
    @Max(value = 150, message = "Age must be less than 150")
    private Integer age;
    
    // Constructors
    public UserDto() {}
    
    public UserDto(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    @Override
    public String toString() {
        return "UserDto{id=" + id + ", name='" + firstName + " " + lastName + "', email='" + email + "'}";
    }
}
