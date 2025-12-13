package com.example.spring_cert_notes.core.lifecycle;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * CUSTOM BEANPOSTPROCESSOR
 * 
 * BeanPostProcessor allows custom modification of bean instances:
 * - Called for EVERY bean in the container
 * - postProcessBeforeInitialization: Before @PostConstruct
 * - postProcessAfterInitialization: After @PostConstruct
 * 
 * Use cases:
 * - Logging bean creation
 * - Wrapping beans with proxies
 * - Validating bean configuration
 * - Custom initialization logic
 */
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor, Ordered {
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // Called BEFORE @PostConstruct and InitializingBean.afterPropertiesSet()
        if (isOurBean(beanName)) {
            System.out.println(Prefixes.CORE_LIFECYCLE + 
                "[BeanPostProcessor] BEFORE init: " + beanName + " (" + bean.getClass().getSimpleName() + ")");
        }
        
        // Can modify or replace the bean here
        // Return the bean (original or modified)
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // Called AFTER @PostConstruct and InitializingBean.afterPropertiesSet()
        if (isOurBean(beanName)) {
            System.out.println(Prefixes.CORE_LIFECYCLE + 
                "[BeanPostProcessor] AFTER init: " + beanName + " (" + bean.getClass().getSimpleName() + ")");
        }
        
        // This is where AOP proxies are typically created
        // Return the bean (original or wrapped in proxy)
        return bean;
    }
    
    @Override
    public int getOrder() {
        // Lower value = higher priority
        // Ordered.HIGHEST_PRECEDENCE = Integer.MIN_VALUE
        // Ordered.LOWEST_PRECEDENCE = Integer.MAX_VALUE
        return Ordered.LOWEST_PRECEDENCE;
    }
    
    private boolean isOurBean(String beanName) {
        return beanName.contains("Lifecycle") || 
               beanName.contains("Sample") ||
               beanName.contains("Proxy");
    }
}
