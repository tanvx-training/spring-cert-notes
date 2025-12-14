package com.example.spring_cert_notes.data.jpa.service;

import com.example.spring_cert_notes.Prefixes;
import com.example.spring_cert_notes.data.jpa.entity.User;
import com.example.spring_cert_notes.data.jpa.repository.UserRepository;
import com.example.spring_cert_notes.data.jpa.specification.UserSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * USER SERVICE - Demonstrates repository usage
 */
@Service
@Transactional(readOnly = true)
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // ============================================================
    // BASIC CRUD
    // ============================================================
    
    @Transactional
    public User save(User user) {
        System.out.println(Prefixes.DATA_JPA + "Saving user: " + user.getEmail());
        return userRepository.save(user);
    }
    
    public Optional<User> findById(Long id) {
        System.out.println(Prefixes.DATA_JPA + "Finding user by id: " + id);
        return userRepository.findById(id);
    }
    
    public List<User> findAll() {
        System.out.println(Prefixes.DATA_JPA + "Finding all users");
        return userRepository.findAll();
    }
    
    @Transactional
    public void delete(Long id) {
        System.out.println(Prefixes.DATA_JPA + "Deleting user: " + id);
        userRepository.deleteById(id);
    }
    
    // ============================================================
    // QUERY METHODS
    // ============================================================
    
    public Optional<User> findByEmail(String email) {
        System.out.println(Prefixes.DATA_JPA + "Finding by email: " + email);
        return userRepository.findByEmail(email);
    }
    
    public List<User> findByName(String firstName, String lastName) {
        System.out.println(Prefixes.DATA_JPA + "Finding by name: " + firstName + " " + lastName);
        return userRepository.findByLastNameAndFirstName(lastName, firstName);
    }
    
    public List<User> findActiveUsers() {
        System.out.println(Prefixes.DATA_JPA + "Finding active users");
        return userRepository.findByActiveTrue();
    }
    
    public List<User> findByEmailDomain(String domain) {
        System.out.println(Prefixes.DATA_JPA + "Finding by email domain: " + domain);
        return userRepository.findByEmailDomain(domain);
    }
    
    // ============================================================
    // PAGINATION & SORTING
    // ============================================================
    
    public Page<User> findAllPaged(int page, int size) {
        System.out.println(Prefixes.DATA_JPA + "Finding all users - page " + page + ", size " + size);
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }
    
    public Page<User> findAllPagedAndSorted(int page, int size, String sortBy, boolean ascending) {
        System.out.println(Prefixes.DATA_JPA + "Finding all users - sorted by " + sortBy);
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }
    
    public Page<User> findActiveUsersPaged(int page, int size) {
        System.out.println(Prefixes.DATA_JPA + "Finding active users - paged");
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastName").ascending());
        return userRepository.findByActive(true, pageable);
    }
    
    // ============================================================
    // SPECIFICATIONS (Dynamic Queries)
    // ============================================================
    
    public List<User> searchUsers(String firstName, String lastName, String emailDomain, Boolean active) {
        System.out.println(Prefixes.DATA_JPA + "Searching users with specifications");
        
        Specification<User> spec = Specification
            .where(UserSpecifications.hasFirstName(firstName))
            .and(UserSpecifications.hasLastName(lastName))
            .and(UserSpecifications.hasEmailDomain(emailDomain))
            .and(UserSpecifications.isActive(active));
        
        return userRepository.findAll(spec);
    }
    
    public List<User> findUsersCreatedBetween(LocalDateTime start, LocalDateTime end) {
        System.out.println(Prefixes.DATA_JPA + "Finding users created between dates");
        Specification<User> spec = UserSpecifications.createdBetween(start, end);
        return userRepository.findAll(spec);
    }
    
    // ============================================================
    // N+1 OPTIMIZATION
    // ============================================================
    
    public Optional<User> findByIdWithOrders(Long id) {
        System.out.println(Prefixes.DATA_JPA + "Finding user with orders (JOIN FETCH): " + id);
        return userRepository.findByIdWithOrders(id);
    }
    
    public List<User> findAllWithOrders() {
        System.out.println(Prefixes.DATA_JPA + "Finding all users with orders (@EntityGraph)");
        return userRepository.findAllWithOrdersGraph();
    }
    
    // ============================================================
    // MODIFYING QUERIES
    // ============================================================
    
    @Transactional
    public int updateActiveStatus(Long id, boolean active) {
        System.out.println(Prefixes.DATA_JPA + "Updating active status for user " + id);
        return userRepository.updateActiveStatus(id, active);
    }
    
    @Transactional
    public int deactivateOldUsers(LocalDateTime beforeDate) {
        System.out.println(Prefixes.DATA_JPA + "Deactivating users created before " + beforeDate);
        return userRepository.deactivateOldUsers(beforeDate);
    }
}
