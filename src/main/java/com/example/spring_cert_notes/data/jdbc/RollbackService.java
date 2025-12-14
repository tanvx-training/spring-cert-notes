package com.example.spring_cert_notes.data.jdbc;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * ROLLBACK RULES EXAMPLES
 * <p>
 * Default behavior:
 * - RuntimeException (unchecked) → ROLLBACK
 * - Exception (checked) → NO ROLLBACK
 * <p>
 * Can customize with:
 * - rollbackFor: Rollback for specified exceptions
 * - noRollbackFor: Don't rollback for specified exceptions
 */
@Service
public class RollbackService {
    
    @Autowired
    private UserDao userDao;
    
    // ============================================================
    // DEFAULT: RuntimeException causes rollback
    // ============================================================
    @Transactional
    public void defaultRollbackBehavior(Long userId, double amount) {
        System.out.println(Prefixes.DATA_TX + "Default rollback behavior");
        
        userDao.updateBalance(userId, amount);
        
        // RuntimeException → ROLLBACK
        throw new RuntimeException("Unchecked exception - will rollback!");
    }
    
    // ============================================================
    // DEFAULT: Checked exception does NOT rollback
    // ============================================================
    @Transactional
    public void checkedExceptionNoRollback(Long userId, double amount) throws IOException {
        System.out.println(Prefixes.DATA_TX + "Checked exception - no rollback by default");
        
        userDao.updateBalance(userId, amount);
        
        // Checked Exception → NO ROLLBACK (by default)
        throw new IOException("Checked exception - will NOT rollback by default!");
    }
    
    // ============================================================
    // CUSTOM: Rollback for checked exception
    // ============================================================
    @Transactional(rollbackFor = IOException.class)
    public void rollbackForCheckedException(Long userId, double amount) throws IOException {
        System.out.println(Prefixes.DATA_TX + "Rollback for checked exception");
        
        userDao.updateBalance(userId, amount);
        
        // Now IOException → ROLLBACK
        throw new IOException("Checked exception - WILL rollback (rollbackFor)!");
    }
    
    // ============================================================
    // CUSTOM: Rollback for all exceptions
    // ============================================================
    @Transactional(rollbackFor = Exception.class)
    public void rollbackForAllExceptions(Long userId, double amount) throws Exception {
        System.out.println(Prefixes.DATA_TX + "Rollback for all exceptions");
        
        userDao.updateBalance(userId, amount);
        
        throw new Exception("Any exception - WILL rollback!");
    }
    
    // ============================================================
    // CUSTOM: No rollback for specific runtime exception
    // ============================================================
    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public void noRollbackForSpecificException(Long userId, double amount) {
        System.out.println(Prefixes.DATA_TX + "No rollback for IllegalArgumentException");
        
        userDao.updateBalance(userId, amount);
        
        // IllegalArgumentException → NO ROLLBACK (noRollbackFor)
        throw new IllegalArgumentException("This exception will NOT cause rollback!");
    }
    
    // ============================================================
    // COMBINED: Multiple rollback rules
    // ============================================================
    @Transactional(
        rollbackFor = {IOException.class, CustomBusinessException.class},
        noRollbackFor = {IllegalArgumentException.class}
    )
    public void combinedRollbackRules(Long userId, double amount, String exceptionType) throws Exception {
        System.out.println(Prefixes.DATA_TX + "Combined rollback rules");
        
        userDao.updateBalance(userId, amount);
        
        switch (exceptionType) {
            case "IO":
                throw new IOException("IOException - WILL rollback");
            case "BUSINESS":
                throw new CustomBusinessException("Business exception - WILL rollback");
            case "ILLEGAL":
                throw new IllegalArgumentException("IllegalArgument - will NOT rollback");
            default:
                throw new RuntimeException("Runtime - WILL rollback (default)");
        }
    }
    
    // Custom checked exception
    public static class CustomBusinessException extends Exception {
        public CustomBusinessException(String message) {
            super(message);
        }
    }
}
