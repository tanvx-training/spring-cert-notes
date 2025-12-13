package com.example.spring_cert_notes.core.lifecycle;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

/**
 * PROXY INSPECTOR UTILITY
 * 
 * Utility class to inspect and identify proxy types.
 * Uses Spring's AopUtils for proxy detection.
 */
@Component
public class ProxyInspector {
    
    /**
     * Inspect a bean and print proxy information
     */
    public void inspect(String beanName, Object bean) {
        System.out.println("\n" + Prefixes.CORE_AOP + "=== Inspecting: " + beanName + " ===");
        
        // Check if it's a proxy
        boolean isProxy = AopUtils.isAopProxy(bean);
        System.out.println(Prefixes.CORE_AOP + "Is AOP Proxy: " + isProxy);
        
        if (isProxy) {
            // Check proxy type
            boolean isJdkProxy = AopUtils.isJdkDynamicProxy(bean);
            boolean isCglibProxy = AopUtils.isCglibProxy(bean);
            
            System.out.println(Prefixes.CORE_AOP + "Is JDK Dynamic Proxy: " + isJdkProxy);
            System.out.println(Prefixes.CORE_AOP + "Is CGLIB Proxy: " + isCglibProxy);
            
            // Get target class
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            System.out.println(Prefixes.CORE_AOP + "Target Class: " + targetClass.getName());
            
            // Get proxy class
            System.out.println(Prefixes.CORE_AOP + "Proxy Class: " + bean.getClass().getName());
            
            // Get ultimate target class (unwrap all proxies)
            Class<?> ultimateTarget = AopProxyUtils.ultimateTargetClass(bean);
            System.out.println(Prefixes.CORE_AOP + "Ultimate Target: " + ultimateTarget.getName());
            
            // Show interfaces for JDK proxy
            if (isJdkProxy) {
                Class<?>[] interfaces = bean.getClass().getInterfaces();
                System.out.println(Prefixes.CORE_AOP + "Implemented Interfaces:");
                for (Class<?> iface : interfaces) {
                    System.out.println(Prefixes.CORE_AOP + "  - " + iface.getName());
                }
            }
            
            // Show superclass for CGLIB proxy
            if (isCglibProxy) {
                Class<?> superclass = bean.getClass().getSuperclass();
                System.out.println(Prefixes.CORE_AOP + "Superclass: " + superclass.getName());
            }
        } else {
            System.out.println(Prefixes.CORE_AOP + "Bean Class: " + bean.getClass().getName());
            System.out.println(Prefixes.CORE_AOP + "Not a proxy - direct instance");
        }
    }
    
    /**
     * Alternative check using Java reflection
     */
    public boolean isJdkProxyUsingReflection(Object bean) {
        return Proxy.isProxyClass(bean.getClass());
    }
    
    /**
     * Check if class name contains CGLIB signature
     */
    public boolean isCglibProxyByClassName(Object bean) {
        return bean.getClass().getName().contains("$$EnhancerBySpringCGLIB$$") ||
               bean.getClass().getName().contains("$$SpringCGLIB$$");
    }
}
