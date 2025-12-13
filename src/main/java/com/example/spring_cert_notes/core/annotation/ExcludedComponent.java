package com.example.spring_cert_notes.core.annotation;

import com.example.spring_cert_notes.Prefixes;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * Component that will be EXCLUDED from scanning
 * <p>
 * This demonstrates excludeFilters in @ComponentScan
 * Even though it has @Component, it can be excluded
 * using various filter types.
 */
@Component
public class ExcludedComponent {
    
    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_SCAN + "ExcludedComponent: This should NOT appear if excluded!");
    }
}
