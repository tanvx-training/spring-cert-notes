package com.example.spring_cert_notes.security.dto;

/**
 * Document DTO cho Method Security Demo
 */
public class Document {
    
    private Long id;
    private String title;
    private String content;
    private String owner;
    private boolean confidential;
    
    // Constructors
    public Document() {}
    
    public Document(Long id, String title, String owner) {
        this.id = id;
        this.title = title;
        this.owner = owner;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public boolean isConfidential() { return confidential; }
    public void setConfidential(boolean confidential) { this.confidential = confidential; }
}
