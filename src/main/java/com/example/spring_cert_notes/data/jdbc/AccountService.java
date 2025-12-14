package com.example.spring_cert_notes.data.jdbc;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * ACCOUNT SERVICE - Transaction Propagation Examples
 * <p>
 * Demonstrates all 7 propagation levels
 */
@Service
public class AccountService {
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private AuditLogService auditLogService;
    
    // ============================================================
    // 1. REQUIRED (default) - Join existing or create new
    // ============================================================
    @Transactional(propagation = Propagation.REQUIRED)
    public void transferRequired(Long fromId, Long toId, double amount) {
        System.out.println(Prefixes.DATA_TX + "[REQUIRED] Starting transfer");
        
        debit(fromId, amount);
        credit(toId, amount);
        
        System.out.println(Prefixes.DATA_TX + "[REQUIRED] Transfer completed");
    }
    
    // ============================================================
    // 2. REQUIRES_NEW - Always create new, suspend current
    // ============================================================
    @Transactional(propagation = Propagation.REQUIRED)
    public void transferWithAudit(Long fromId, Long toId, double amount) {
        System.out.println(Prefixes.DATA_TX + "[REQUIRED] Starting transfer with audit");
        
        try {
            debit(fromId, amount);
            credit(toId, amount);
            
            // This runs in NEW transaction - commits even if outer fails
            auditLogService.logTransfer(fromId, toId, amount, "SUCCESS");
            
            // Simulate failure after audit
            if (amount > 1000) {
                throw new RuntimeException("Amount too large!");
            }
        } catch (Exception e) {
            // Audit log is ALREADY COMMITTED (REQUIRES_NEW)
            auditLogService.logTransfer(fromId, toId, amount, "FAILED: " + e.getMessage());
            throw e;
        }
    }
    
    // ============================================================
    // 3. SUPPORTS - Join if exists, non-transactional otherwise
    // ============================================================
    @Transactional(propagation = Propagation.SUPPORTS)
    public double getBalance(Long userId) {
        System.out.println(Prefixes.DATA_TX + "[SUPPORTS] Getting balance");
        return userDao.findById(userId)
            .map(User::getBalance)
            .orElse(0.0);
    }
    
    // ============================================================
    // 4. NOT_SUPPORTED - Execute non-transactionally
    // ============================================================
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void generateReport() {
        System.out.println(Prefixes.DATA_TX + "[NOT_SUPPORTED] Generating report (no transaction)");
        // Long-running read operation that doesn't need transaction
        userDao.findAll().forEach(u -> 
            System.out.println(Prefixes.DATA_TX + "  User: " + u.getName() + " - $" + u.getBalance()));
    }
    
    // ============================================================
    // 5. MANDATORY - Must have existing transaction
    // ============================================================
    @Transactional(propagation = Propagation.MANDATORY)
    public void debit(Long userId, double amount) {
        System.out.println(Prefixes.DATA_TX + "[MANDATORY] Debiting $" + amount + " from user " + userId);
        User user = userDao.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        
        if (user.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance!");
        }
        
        userDao.updateBalance(userId, user.getBalance() - amount);
    }
    
    // ============================================================
    // 6. NEVER - Must NOT have transaction
    // ============================================================
    @Transactional(propagation = Propagation.NEVER)
    public void healthCheck() {
        System.out.println(Prefixes.DATA_TX + "[NEVER] Health check (must not have transaction)");
        int count = userDao.count();
        System.out.println(Prefixes.DATA_TX + "  Total users: " + count);
    }
    
    // ============================================================
    // 7. NESTED - Nested transaction with savepoint
    // ============================================================
    @Transactional(propagation = Propagation.REQUIRED)
    public void transferWithBonus(Long fromId, Long toId, double amount) {
        System.out.println(Prefixes.DATA_TX + "[REQUIRED] Starting transfer with bonus");
        
        debit(fromId, amount);
        credit(toId, amount);
        
        try {
            // Nested transaction - can rollback independently
            addBonus(toId, amount * 0.01);  // 1% bonus
        } catch (Exception e) {
            // Bonus failed but main transfer continues
            System.out.println(Prefixes.DATA_TX + "Bonus failed, but transfer continues: " + e.getMessage());
        }
        
        System.out.println(Prefixes.DATA_TX + "[REQUIRED] Transfer completed");
    }
    
    @Transactional(propagation = Propagation.NESTED)
    public void addBonus(Long userId, double bonus) {
        System.out.println(Prefixes.DATA_TX + "[NESTED] Adding bonus $" + bonus + " to user " + userId);
        User user = userDao.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Simulate occasional failure
        if (bonus < 1) {
            throw new RuntimeException("Bonus too small!");
        }
        
        userDao.updateBalance(userId, user.getBalance() + bonus);
    }
    
    // Helper method
    @Transactional(propagation = Propagation.MANDATORY)
    public void credit(Long userId, double amount) {
        System.out.println(Prefixes.DATA_TX + "[MANDATORY] Crediting $" + amount + " to user " + userId);
        User user = userDao.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        userDao.updateBalance(userId, user.getBalance() + amount);
    }
}
