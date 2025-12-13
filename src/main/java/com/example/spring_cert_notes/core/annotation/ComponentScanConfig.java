package com.example.spring_cert_notes.core.annotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * COMPONENT SCANNING CONFIGURATION EXAMPLES
 * <p>
 * Demonstrates various @ComponentScan configurations:
 * 1. Basic scanning
 * 2. Include filters
 * 3. Exclude filters
 * 4. Filter types (ANNOTATION, ASSIGNABLE_TYPE, REGEX, CUSTOM)
 */

// ============================================================
// CONFIG 1: Basic Component Scanning
// ============================================================
@Configuration
@ComponentScan(basePackages = "com.example.spring_cert_notes.core.annotation")
class BasicScanConfig {
    // Scans all @Component, @Service, @Repository, @Controller in package
}

// ============================================================
// CONFIG 2: Include Filter - Custom Annotation
// ============================================================
@Configuration
@ComponentScan(
    basePackages = "com.example.spring_cert_notes.core.annotation",
    includeFilters = @ComponentScan.Filter(
        type = FilterType.ANNOTATION,
        classes = MyCustomAnnotation.class
    ),
    // useDefaultFilters = true means @Component etc. are still scanned
    useDefaultFilters = true
)
class IncludeFilterConfig {
    // Also includes classes annotated with @MyCustomAnnotation
}

// ============================================================
// CONFIG 3: Exclude Filter - By Class
// ============================================================
@Configuration
@ComponentScan(
    basePackages = "com.example.spring_cert_notes.core.annotation",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = ExcludedComponent.class
    )
)
class ExcludeByClassConfig {
    // Excludes ExcludedComponent even though it has @Component
}

// ============================================================
// CONFIG 4: Exclude Filter - By Regex Pattern
// ============================================================
@Configuration
@ComponentScan(
    basePackages = "com.example.spring_cert_notes.core.annotation",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = ".*Excluded.*"
    )
)
class ExcludeByRegexConfig {
    // Excludes any class with "Excluded" in its name
}

// ============================================================
// CONFIG 5: Multiple Filters
// ============================================================
@Configuration
@ComponentScan(
    basePackages = "com.example.spring_cert_notes.core.annotation",
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyCustomAnnotation.class)
    },
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ExcludedComponent.class),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Test.*")
    }
)
class MultipleFiltersConfig {
    // Complex filtering: include custom annotation, exclude specific classes and patterns
}

// ============================================================
// CONFIG 6: Disable Default Filters
// ============================================================
@Configuration
@ComponentScan(
    basePackages = "com.example.spring_cert_notes.core.annotation",
    useDefaultFilters = false,  // Disable @Component, @Service, etc.
    includeFilters = @ComponentScan.Filter(
        type = FilterType.ANNOTATION,
        classes = MyCustomAnnotation.class
    )
)
class OnlyCustomAnnotationConfig {
    // ONLY scans @MyCustomAnnotation, ignores @Component, @Service, etc.
}

// ============================================================
// CONFIG 7: Type-safe basePackageClasses
// ============================================================
@Configuration
@ComponentScan(
    basePackageClasses = ExcludedComponent.class  // Type-safe package reference
)
class TypeSafePackageConfig {
    // Uses class reference instead of string package name
    // Refactoring-safe!
}
