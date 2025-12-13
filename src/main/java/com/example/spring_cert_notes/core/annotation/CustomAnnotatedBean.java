package com.example.spring_cert_notes.core.annotation;

import com.example.spring_cert_notes.Prefixes;
import jakarta.annotation.PostConstruct;

/**
 * Bean annotated with @MyCustomAnnotation
 * <p>
 * This bean will be picked up by component scanning
 * when using includeFilters with FilterType.ANNOTATION
 * <p>
 * Note: This class is NOT annotated with @Component!
 * It will only be registered if the custom filter is configured.
 */
@MyCustomAnnotation("customBean")
public class CustomAnnotatedBean {
    
    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_SCAN + "CustomAnnotatedBean: Registered via @MyCustomAnnotation filter");
    }
    
    public void execute() {
        System.out.println(Prefixes.CORE_BEAN + "CustomAnnotatedBean: Executing custom logic");
    }
}
