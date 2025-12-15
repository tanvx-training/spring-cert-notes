package com.example.spring_cert_notes.security.service;

import com.example.spring_cert_notes.security.dto.Document;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * BÀI 3: METHOD-LEVEL SECURITY
 * 
 * Sử dụng annotations để bảo vệ methods:
 * - @PreAuthorize: Kiểm tra trước khi method chạy
 * - @PostAuthorize: Kiểm tra sau khi method chạy (có thể access return value)
 * - @PreFilter: Lọc collection input
 * - @PostFilter: Lọc collection output
 * - @Secured: Simple role-based (không hỗ trợ SpEL)
 * - @RolesAllowed: JSR-250 annotation
 * 
 * Cần @EnableMethodSecurity trong config!
 */
@Service
public class MethodSecurityService {
    
    // ============================================================
    // @PreAuthorize - Kiểm tra TRƯỚC khi method chạy
    // ============================================================
    
    /**
     * Chỉ ADMIN mới được gọi
     */
    @PreAuthorize("hasRole('ADMIN')")
    public void adminOnlyMethod() {
        System.out.println("Admin method executed!");
    }
    
    /**
     * ADMIN hoặc MODERATOR
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public void adminOrModeratorMethod() {
        System.out.println("Admin or Moderator method executed!");
    }
    
    /**
     * Kiểm tra authority (permission)
     */
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public void deleteUser(Long userId) {
        System.out.println("Deleting user: " + userId);
    }
    
    /**
     * Kiểm tra nhiều điều kiện với AND/OR
     */
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('WRITE_REPORTS')")
    public void generateReport() {
        System.out.println("Generating report...");
    }
    
    /**
     * Sử dụng method parameter trong expression
     * 
     * #username là tham chiếu đến parameter
     * authentication.name là username của user hiện tại
     */
    @PreAuthorize("#username == authentication.name or hasRole('ADMIN')")
    public String getUserProfile(String username) {
        return "Profile of: " + username;
    }
    
    /**
     * Kiểm tra object parameter
     */
    @PreAuthorize("#document.owner == authentication.name or hasRole('ADMIN')")
    public void updateDocument(Document document) {
        System.out.println("Updating document: " + document.getTitle());
    }
    
    /**
     * Sử dụng custom bean trong expression
     * 
     * @securityService là bean name
     */
    @PreAuthorize("@securityChecker.canAccess(#resourceId, authentication)")
    public void accessResource(Long resourceId) {
        System.out.println("Accessing resource: " + resourceId);
    }
    
    // ============================================================
    // @PostAuthorize - Kiểm tra SAU khi method chạy
    // ============================================================
    
    /**
     * Kiểm tra return value
     * 
     * returnObject là reference đến giá trị trả về
     * Chỉ cho phép nếu owner của document là user hiện tại
     */
    @PostAuthorize("returnObject.owner == authentication.name or hasRole('ADMIN')")
    public Document getDocument(Long id) {
        // Giả lập lấy document từ database
        Document doc = new Document();
        doc.setId(id);
        doc.setTitle("Secret Document");
        doc.setOwner("user");  // Owner là "user"
        return doc;
    }
    
    /**
     * Kiểm tra property của return object
     */
    @PostAuthorize("returnObject.confidential == false or hasRole('ADMIN')")
    public Document getDocumentWithConfidentialCheck(Long id) {
        Document doc = new Document();
        doc.setId(id);
        doc.setConfidential(true);
        return doc;
    }
    
    // ============================================================
    // @PreFilter / @PostFilter - Lọc collections
    // ============================================================
    
    /**
     * Lọc input collection
     * 
     * filterObject là từng element trong collection
     * Chỉ giữ lại documents mà user hiện tại là owner
     */
    @PreFilter("filterObject.owner == authentication.name")
    public void batchUpdateDocuments(List<Document> documents) {
        System.out.println("Updating " + documents.size() + " documents");
        documents.forEach(doc -> System.out.println("  - " + doc.getTitle()));
    }
    
    /**
     * Lọc output collection
     * 
     * Chỉ trả về documents mà user hiện tại có quyền xem
     */
    @PostFilter("filterObject.owner == authentication.name or hasRole('ADMIN')")
    public List<Document> getAllDocuments() {
        List<Document> docs = new ArrayList<>();
        
        Document doc1 = new Document();
        doc1.setId(1L);
        doc1.setTitle("User's Document");
        doc1.setOwner("user");
        docs.add(doc1);
        
        Document doc2 = new Document();
        doc2.setId(2L);
        doc2.setTitle("Admin's Document");
        doc2.setOwner("admin");
        docs.add(doc2);
        
        Document doc3 = new Document();
        doc3.setId(3L);
        doc3.setTitle("Another User's Document");
        doc3.setOwner("user");
        docs.add(doc3);
        
        return docs;
    }
    
    // ============================================================
    // @Secured - Simple role-based (không hỗ trợ SpEL)
    // ============================================================
    
    /**
     * @Secured - Cách cũ, đơn giản hơn
     * 
     * Phải dùng full role name với prefix ROLE_
     */
    @Secured("ROLE_ADMIN")
    public void securedAdminMethod() {
        System.out.println("Secured admin method!");
    }
    
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public void securedMultipleRoles() {
        System.out.println("Secured for admin or moderator!");
    }
    
    // ============================================================
    // @RolesAllowed - JSR-250 annotation
    // ============================================================
    
    /**
     * @RolesAllowed - JSR-250 standard
     * 
     * Tương tự @Secured nhưng là Java EE standard
     */
    @RolesAllowed("ADMIN")
    public void jsr250AdminMethod() {
        System.out.println("JSR-250 admin method!");
    }
    
    @RolesAllowed({"ADMIN", "USER"})
    public void jsr250MultipleRoles() {
        System.out.println("JSR-250 for admin or user!");
    }
    
    // ============================================================
    // Programmatic Security Check
    // ============================================================
    
    /**
     * Kiểm tra security trong code (không dùng annotation)
     */
    public void programmaticSecurityCheck() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated()) {
            System.out.println("Current user: " + auth.getName());
            System.out.println("Authorities: " + auth.getAuthorities());
            
            boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            
            if (isAdmin) {
                System.out.println("User is admin!");
            }
        }
    }
}
