package com.example.spring_cert_notes.core.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CUSTOM STEREOTYPE ANNOTATION
 * <p>
 * Creates a custom stereotype by meta-annotating with @Component
 * This allows creating domain-specific annotations that still
 * participate in component scanning
 * <p>
 * Example use cases:
 * - @Gateway for API gateway services
 * - @Validator for validation services
 * - @Mapper for DTO mappers
 * - @Adapter for external system adapters
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component  // Meta-annotation - makes this a stereotype
public @interface CustomStereotype {
    
    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     * @return the suggested component name, if any (or empty String otherwise)
     */
    @AliasFor(annotation = Component.class)
    String value() default "";
}
