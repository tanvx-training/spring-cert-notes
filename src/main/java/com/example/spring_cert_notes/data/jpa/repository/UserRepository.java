package com.example.spring_cert_notes.data.jpa.repository;

import com.example.spring_cert_notes.data.jpa.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * USER REPOSITORY - Spring Data JPA Examples
 * <p>
 * Demonstrates:
 * 1. Query Methods (derived queries)
 * 2. @Query with JPQL
 * 3. @Query with native SQL
 * 4. Pagination & Sorting
 * 5. @EntityGraph for N+1 optimization
 * 6. @Modifying for UPDATE/DELETE
 * 7. JpaSpecificationExecutor for dynamic queries
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
    // ============================================================
    // 1. QUERY METHODS (Derived Queries)
    // ============================================================
    
    // Simple find by property
    Optional<User> findByEmail(String email);
    
    // Find by multiple properties (AND)
    List<User> findByLastNameAndFirstName(String lastName, String firstName);
    
    // Find by property with OR
    List<User> findByFirstNameOrLastName(String firstName, String lastName);
    
    // Find with LIKE
    List<User> findByEmailContaining(String emailPart);
    List<User> findByLastNameStartingWith(String prefix);
    List<User> findByFirstNameEndingWith(String suffix);
    
    // Find with comparison
    List<User> findByCreatedAtAfter(LocalDateTime date);
    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    // Find with boolean
    List<User> findByActiveTrue();
    List<User> findByActiveFalse();
    
    // Find with NULL check
    List<User> findByEmailIsNotNull();
    
    // Find with IN
    List<User> findByLastNameIn(List<String> lastNames);
    
    // Count
    long countByActive(boolean active);
    
    // Exists
    boolean existsByEmail(String email);
    
    // Delete
    void deleteByEmail(String email);
    
    // Find with sorting
    List<User> findByActiveOrderByLastNameAsc(boolean active);
    
    // Find first/top
    Optional<User> findFirstByOrderByCreatedAtDesc();
    List<User> findTop3ByActiveOrderByCreatedAtDesc(boolean active);
    
    // ============================================================
    // 2. @QUERY WITH JPQL
    // ============================================================
    
    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain")
    List<User> findByEmailDomain(@Param("domain") String domain);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) = LOWER(:name) OR LOWER(u.lastName) = LOWER(:name)")
    List<User> findByNameIgnoreCase(@Param("name") String name);
    
    @Query("SELECT u FROM User u WHERE u.active = true AND u.createdAt > :date")
    List<User> findActiveUsersCreatedAfter(@Param("date") LocalDateTime date);
    
    @Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.id = :id")
    Optional<User> findByIdWithOrders(@Param("id") Long id);
    
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders")
    List<User> findAllWithOrders();
    
    // ============================================================
    // 3. @QUERY WITH NATIVE SQL
    // ============================================================
    
    @Query(value = "SELECT * FROM users WHERE created_at > ?1", nativeQuery = true)
    List<User> findRecentUsersNative(LocalDateTime date);
    
    @Query(value = "SELECT * FROM users WHERE email LIKE %?1%", nativeQuery = true)
    List<User> findByEmailPatternNative(String pattern);
    
    @Query(value = "SELECT COUNT(*) FROM users WHERE active = true", nativeQuery = true)
    long countActiveUsersNative();
    
    // ============================================================
    // 4. PAGINATION & SORTING
    // ============================================================
    
    Page<User> findByActive(boolean active, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.lastName LIKE :prefix%")
    Page<User> findByLastNamePrefix(@Param("prefix") String prefix, Pageable pageable);
    
    List<User> findByActive(boolean active, Sort sort);
    
    // ============================================================
    // 5. @ENTITYGRAPH - N+1 Optimization
    // ============================================================
    
    @EntityGraph(attributePaths = {"orders"})
    @Query("SELECT u FROM User u")
    List<User> findAllWithOrdersGraph();
    
    @EntityGraph(value = "User.withOrders")
    Optional<User> findWithOrdersById(Long id);
    
    // ============================================================
    // 6. @MODIFYING - UPDATE/DELETE
    // ============================================================
    
    @Modifying
    @Query("UPDATE User u SET u.active = :active WHERE u.id = :id")
    int updateActiveStatus(@Param("id") Long id, @Param("active") boolean active);
    
    @Modifying
    @Query("UPDATE User u SET u.active = false WHERE u.createdAt < :date")
    int deactivateOldUsers(@Param("date") LocalDateTime date);
    
    @Modifying
    @Query("DELETE FROM User u WHERE u.active = false")
    int deleteInactiveUsers();
}
