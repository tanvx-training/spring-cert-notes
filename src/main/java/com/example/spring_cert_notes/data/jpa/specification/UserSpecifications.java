package com.example.spring_cert_notes.data.jpa.specification;

import com.example.spring_cert_notes.data.jpa.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * USER SPECIFICATIONS - Dynamic Queries
 * 
 * Specifications allow building dynamic queries at runtime.
 * Can be combined with and(), or(), not()
 */
public class UserSpecifications {
    
    // Private constructor - utility class
    private UserSpecifications() {}
    
    /**
     * Filter by first name (case-insensitive contains)
     */
    public static Specification<User> hasFirstName(String firstName) {
        return (root, query, cb) -> {
            if (firstName == null || firstName.isEmpty()) {
                return cb.conjunction(); // Always true
            }
            return cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
        };
    }
    
    /**
     * Filter by last name (case-insensitive contains)
     */
    public static Specification<User> hasLastName(String lastName) {
        return (root, query, cb) -> {
            if (lastName == null || lastName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
        };
    }
    
    /**
     * Filter by email domain
     */
    public static Specification<User> hasEmailDomain(String domain) {
        return (root, query, cb) -> {
            if (domain == null || domain.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(root.get("email"), "%" + domain);
        };
    }
    
    /**
     * Filter by active status
     */
    public static Specification<User> isActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("active"), active);
        };
    }
    
    /**
     * Filter by created date range
     */
    public static Specification<User> createdBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start == null && end == null) {
                return cb.conjunction();
            }
            if (start == null) {
                return cb.lessThanOrEqualTo(root.get("createdAt"), end);
            }
            if (end == null) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), start);
            }
            return cb.between(root.get("createdAt"), start, end);
        };
    }
    
    /**
     * Filter by created after date
     */
    public static Specification<User> createdAfter(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            return cb.greaterThan(root.get("createdAt"), date);
        };
    }
    
    /**
     * Filter users with orders
     */
    public static Specification<User> hasOrders() {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.isNotEmpty(root.get("orders"));
        };
    }
}
