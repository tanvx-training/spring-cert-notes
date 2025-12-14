package com.example.spring_cert_notes.data.jdbc;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * AUDIT LOG SERVICE
 * <p>
 * Demonstrates REQUIRES_NEW propagation
 * Audit logs are committed independently of the main transaction
 */
@Service
public class AuditLogService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * REQUIRES_NEW: Always creates a new transaction
     * - Suspends current transaction (if any)
     * - Creates new transaction
     * - Commits/rollbacks independently
     * - Resumes outer transaction
     * 
     * Use case: Audit logs that must be saved even if main transaction fails
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logTransfer(Long fromId, Long toId, double amount, String status) {
        System.out.println(Prefixes.DATA_TX + "[REQUIRES_NEW] Logging transfer audit");
        
        String sql = "INSERT INTO audit_log (action, from_user, to_user, amount, status, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        
        jdbcTemplate.update(sql, "TRANSFER", fromId, toId, amount, status);
        
        System.out.println(Prefixes.DATA_TX + "[REQUIRES_NEW] Audit log committed (independent transaction)");
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAction(String action, String details) {
        System.out.println(Prefixes.DATA_TX + "[REQUIRES_NEW] Logging action: " + action);
        
        String sql = "INSERT INTO audit_log (action, status, created_at) VALUES (?, ?, CURRENT_TIMESTAMP)";
        jdbcTemplate.update(sql, action, details);
    }
}
