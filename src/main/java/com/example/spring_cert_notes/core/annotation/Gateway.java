package com.example.spring_cert_notes.core.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom stereotype for Gateway services
 * <p>
 * Meta-annotated with @Service to inherit all its behavior
 * while providing domain-specific semantics
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface Gateway {
    
    @AliasFor(annotation = Service.class)
    String value() default "";
}
