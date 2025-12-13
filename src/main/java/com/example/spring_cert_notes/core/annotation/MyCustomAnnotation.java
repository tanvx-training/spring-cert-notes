package com.example.spring_cert_notes.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for component scanning filter demo
 * <p>
 * Classes annotated with @MyCustomAnnotation will be picked up
 * by custom component scanning filters
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyCustomAnnotation {
    String value() default "";
}
