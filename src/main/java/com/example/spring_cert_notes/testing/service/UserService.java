package com.example.spring_cert_notes.testing.service;

import com.example.spring_cert_notes.testing.entity.User;
import com.example.spring_cert_notes.testing.repository.UserRepository;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * User Service cho Testing Demo
 */
@Service
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        return userRepository.save(user);
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public List<User> findAllActiveUsers() {
        return userRepository.findByActiveTrue();
    }
    
    public List<User> searchByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User existing = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        
        existing.setName(updatedUser.getName());
        existing.setEmail(updatedUser.getEmail());
        existing.setActive(updatedUser.isActive());
        
        return userRepository.save(existing);
    }
    
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }
    
    @Transactional
    public void deactivateUser(Long id) {
        userRepository.updateActiveStatus(id, false);
    }
}
