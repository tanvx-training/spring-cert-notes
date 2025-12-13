package com.example.spring_cert_notes.core.lifecycle;

import com.example.spring_cert_notes.Prefixes;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * COMPLETE BEAN LIFECYCLE DEMONSTRATION
 * 
 * This bean implements all lifecycle interfaces to show the complete
 * 11-step bean lifecycle in Spring.
 * 
 * LIFECYCLE ORDER:
 * 1. Instantiation (Constructor)
 * 2. Populate Properties (Dependency Injection)
 * 3. BeanNameAware.setBeanName()
 * 4. BeanFactoryAware.setBeanFactory()
 * 5. ApplicationContextAware.setApplicationContext()
 * 6. BeanPostProcessor.postProcessBeforeInitialization()
 * 7. @PostConstruct
 * 8. InitializingBean.afterPropertiesSet()
 * 9. Custom init-method
 * 10. BeanPostProcessor.postProcessAfterInitialization()
 * 11. Bean is READY
 * ...
 * 12. @PreDestroy
 * 13. DisposableBean.destroy()
 * 14. Custom destroy-method
 */
@Component
public class SampleLifecycleBean implements 
        BeanNameAware,
        BeanFactoryAware,
        ApplicationContextAware,
        InitializingBean,
        DisposableBean {
    
    private int step = 0;
    private String beanName;
    private BeanFactory beanFactory;
    private ApplicationContext applicationContext;
    
    // Step 1: Instantiation
    public SampleLifecycleBean() {
        System.out.println(Prefixes.CORE_LIFECYCLE + 
            "[" + (++step) + "] Constructor: Bean instantiated");
    }
    
    // Step 3: BeanNameAware
    @Override
    public void setBeanName(String name) {
        this.beanName = name;
        System.out.println(Prefixes.CORE_LIFECYCLE + 
            "[" + (++step) + "] BeanNameAware.setBeanName(): " + name);
    }
    
    // Step 4: BeanFactoryAware
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        System.out.println(Prefixes.CORE_LIFECYCLE + 
            "[" + (++step) + "] BeanFactoryAware.setBeanFactory()");
    }
    
    // Step 5: ApplicationContextAware
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.applicationContext = ctx;
        System.out.println(Prefixes.CORE_LIFECYCLE + 
            "[" + (++step) + "] ApplicationContextAware.setApplicationContext()");
    }
    
    // Step 6: BeanPostProcessor.postProcessBeforeInitialization() - handled by CustomBeanPostProcessor
    
    // Step 7: @PostConstruct
    @PostConstruct
    public void postConstruct() {
        System.out.println(Prefixes.CORE_LIFECYCLE + 
            "[" + (++step) + "] @PostConstruct: Initialization callback");
    }
    
    // Step 8: InitializingBean
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(Prefixes.CORE_LIFECYCLE + 
            "[" + (++step) + "] InitializingBean.afterPropertiesSet()");
    }
    
    // Step 9: Custom init-method would be called here if configured
    
    // Step 10: BeanPostProcessor.postProcessAfterInitialization() - handled by CustomBeanPostProcessor
    
    // Step 11: Bean is READY
    public void doWork() {
        System.out.println(Prefixes.CORE_BEAN + 
            "SampleLifecycleBean: Doing work (bean is ready)");
    }
    
    // Step 12: @PreDestroy
    @PreDestroy
    public void preDestroy() {
        System.out.println(Prefixes.CORE_LIFECYCLE + 
            "[DESTROY-1] @PreDestroy: Cleanup callback");
    }
    
    // Step 13: DisposableBean
    @Override
    public void destroy() throws Exception {
        System.out.println(Prefixes.CORE_LIFECYCLE + 
            "[DESTROY-2] DisposableBean.destroy()");
    }
    
    // Getters for verification
    public String getBeanName() { return beanName; }
    public BeanFactory getBeanFactory() { return beanFactory; }
    public ApplicationContext getApplicationContext() { return applicationContext; }
}
