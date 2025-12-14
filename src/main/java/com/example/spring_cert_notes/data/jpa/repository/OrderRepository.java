package com.example.spring_cert_notes.data.jpa.repository;

import com.example.spring_cert_notes.data.jpa.entity.Order;
import com.example.spring_cert_notes.data.jpa.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ORDER REPOSITORY
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Query methods
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByUserId(Long userId);
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);
    
    // Amount queries
    List<Order> findByAmountGreaterThan(BigDecimal amount);
    List<Order> findByAmountBetween(BigDecimal min, BigDecimal max);
    
    // Date queries
    List<Order> findByCreatedAtAfter(LocalDateTime date);
    
    // JPQL
    @Query("SELECT o FROM Order o WHERE o.user.email = :email")
    List<Order> findByUserEmail(@Param("email") String email);
    
    @Query("SELECT SUM(o.amount) FROM Order o WHERE o.user.id = :userId")
    BigDecimal getTotalAmountByUser(@Param("userId") Long userId);
    
    @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.status = :status")
    List<Order> findByStatusWithUser(@Param("status") OrderStatus status);
    
    // Pagination
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}
