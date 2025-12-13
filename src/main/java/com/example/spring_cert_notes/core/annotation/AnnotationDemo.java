package com.example.spring_cert_notes.core.annotation;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * DEMO: Annotation-Based Config & Stereotypes
 * <p>
 * Run this class to see:
 * 1. Stereotype annotations in action
 * 2. Lifecycle callbacks order
 * 3. Component scanning with filters
 */
public class AnnotationDemo {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   SPRING CORE - ANNOTATION-BASED CONFIG & STEREOTYPES     ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        // Demo 1: Basic scanning with all stereotypes
        demoBasicScanning();
        
        // Demo 2: Include filter for custom annotation
        demoIncludeFilter();
        
        // Demo 3: Exclude filter
        demoExcludeFilter();
        
        System.out.println("\n✓ Demo completed!");
    }
    
    private static void demoBasicScanning() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 1: BASIC COMPONENT SCANNING");
        System.out.println("=".repeat(60));
        System.out.println(Prefixes.CORE_SCAN + "Scanning with default filters (@Component, @Service, etc.)");
        
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(BasicScanConfig.class);
        
        System.out.println("\n" + Prefixes.CORE_BEAN + "Registered beans:");
        String[] beanNames = context.getBeanDefinitionNames();
        for (String name : beanNames) {
            if (!name.contains("Config") && !name.contains("springframework")) {
                System.out.println(Prefixes.CORE_BEAN + "  - " + name);
            }
        }
        
        context.close();
    }
    
    private static void demoIncludeFilter() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 2: INCLUDE FILTER - Custom Annotation");
        System.out.println("=".repeat(60));
        System.out.println(Prefixes.CORE_SCAN + "Including @MyCustomAnnotation classes");
        
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(IncludeFilterConfig.class);
        
        // Check if CustomAnnotatedBean is registered
        boolean hasCustomBean = context.containsBean("customAnnotatedBean");
        System.out.println(Prefixes.CORE_SCAN + "CustomAnnotatedBean registered: " + hasCustomBean);
        
        if (hasCustomBean) {
            CustomAnnotatedBean bean = context.getBean(CustomAnnotatedBean.class);
            bean.execute();
        }
        
        context.close();
    }
    
    private static void demoExcludeFilter() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO 3: EXCLUDE FILTER - By Class Type");
        System.out.println("=".repeat(60));
        System.out.println(Prefixes.CORE_SCAN + "Excluding ExcludedComponent");
        
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(ExcludeByClassConfig.class);
        
        // Check if ExcludedComponent is NOT registered
        boolean hasExcluded = context.containsBean("excludedComponent");
        System.out.println(Prefixes.CORE_SCAN + "ExcludedComponent registered: " + hasExcluded);
        System.out.println(Prefixes.CORE_SCAN + "Exclusion " + (hasExcluded ? "FAILED" : "SUCCESSFUL"));
        
        context.close();
    }
}
