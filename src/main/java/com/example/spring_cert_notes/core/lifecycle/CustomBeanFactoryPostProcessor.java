package com.example.spring_cert_notes.core.lifecycle;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * CUSTOM BEANFACTORYPOSTPROCESSOR
 * 
 * BeanFactoryPostProcessor allows modification of bean DEFINITIONS:
 * - Called BEFORE any beans are instantiated
 * - Can modify bean metadata (scope, lazy, properties, etc.)
 * - Cannot access bean instances (they don't exist yet!)
 * 
 * Use cases:
 * - Modify bean definitions programmatically
 * - Register additional bean definitions
 * - Override property values
 * - Change bean scope
 */
@Component
public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("\n" + Prefixes.CORE_LIFECYCLE + 
            "=== BeanFactoryPostProcessor: Modifying Bean Definitions ===");
        
        // Get all bean definition names
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        System.out.println(Prefixes.CORE_LIFECYCLE + 
            "Total bean definitions: " + beanNames.length);
        
        // Example: Modify a specific bean definition
        for (String beanName : beanNames) {
            if (beanName.equals("sampleLifecycleBean")) {
                BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
                
                System.out.println(Prefixes.CORE_LIFECYCLE + 
                    "Modifying bean definition: " + beanName);
                System.out.println(Prefixes.CORE_LIFECYCLE + 
                    "  Original scope: " + bd.getScope());
                System.out.println(Prefixes.CORE_LIFECYCLE + 
                    "  Is lazy: " + bd.isLazyInit());
                
                // Example modifications (commented out to not affect demo):
                // bd.setScope("prototype");
                // bd.setLazyInit(true);
                // bd.getPropertyValues().add("propertyName", "newValue");
            }
        }
        
        System.out.println(Prefixes.CORE_LIFECYCLE + 
            "=== BeanFactoryPostProcessor: Complete ===\n");
    }
}
