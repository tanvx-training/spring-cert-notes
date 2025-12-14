package com.example.spring_cert_notes.mvc.service;

import com.example.spring_cert_notes.mvc.dto.UserDto;
import com.example.spring_cert_notes.mvc.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * User Service (in-memory implementation for demo)
 */
@Service
public class UserService {
    
    private final Map<Long, UserDto> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public UserService() {
        // Sample data
        create(new UserDto(null, "Alice", "Smith", "alice@example.com"));
        create(new UserDto(null, "Bob", "Johnson", "bob@example.com"));
        create(new UserDto(null, "Charlie", "Brown", "charlie@example.com"));
    }
    
    public List<UserDto> findAll() {
        return new ArrayList<>(users.values());
    }
    
    public Page<UserDto> findAll(Pageable pageable) {
        List<UserDto> all = new ArrayList<>(users.values());
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), all.size());
        List<UserDto> content = start < all.size() ? all.subList(start, end) : Collections.emptyList();
        return new PageImpl<>(content, pageable, all.size());
    }
    
    public Optional<UserDto> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }
    
    public boolean existsById(Long id) {
        return users.containsKey(id);
    }
    
    public List<UserDto> search(String firstName, String lastName, String email) {
        return users.values().stream()
            .filter(u -> firstName == null || u.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
            .filter(u -> lastName == null || u.getLastName().toLowerCase().contains(lastName.toLowerCase()))
            .filter(u -> email == null || u.getEmail().toLowerCase().contains(email.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    public UserDto create(UserDto dto) {
        dto.setId(idGenerator.getAndIncrement());
        users.put(dto.getId(), dto);
        return dto;
    }
    
    public UserDto update(UserDto dto) {
        users.put(dto.getId(), dto);
        return dto;
    }
    
    public UserDto partialUpdate(Long id, UserDto dto) {
        UserDto existing = users.get(id);
        if (existing == null) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        if (dto.getFirstName() != null) existing.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) existing.setLastName(dto.getLastName());
        if (dto.getEmail() != null) existing.setEmail(dto.getEmail());
        if (dto.getAge() != null) existing.setAge(dto.getAge());
        return existing;
    }
    
    public void delete(Long id) {
        users.remove(id);
    }
}
