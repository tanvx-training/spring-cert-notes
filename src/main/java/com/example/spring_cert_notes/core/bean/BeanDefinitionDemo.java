package com.example.spring_cert_notes.core.bean;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * DEMO: 5 DIFFERENT WAYS TO DEFINE BEANS
 * <p>
 * Run this class to see the results
 */
public class BeanDefinitionDemo {

    public static void main(String[] args) {
        System.out.println("=== DEMO: 5 WAYS TO DEFINE BEANS ===\n");

        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(DemoConfig.class);

        System.out.println("\n--- Getting beans from context ---");
        
        // Way 1: @Component
        ComponentBean bean1 = context.getBean(ComponentBean.class);
        bean1.doWork();

        // Way 2: @Bean in @Configuration
        ConfigBean bean2 = context.getBean(ConfigBean.class);
        bean2.doWork();

        // Way 3: @Bean with method name
        SimpleBean bean3 = context.getBean("customBeanName", SimpleBean.class);
        bean3.doWork();

        // Way 4: Factory method
        FactoryBean bean4 = context.getBean(FactoryBean.class);
        bean4.doWork();

        // Way 5: Programmatic registration
        ProgrammaticBean bean5 = context.getBean(ProgrammaticBean.class);
        bean5.doWork();

        System.out.println("\n--- Checking Singleton vs Prototype ---");
        
        // Singleton: same instance
        ConfigBean singleton1 = context.getBean(ConfigBean.class);
        ConfigBean singleton2 = context.getBean(ConfigBean.class);
        System.out.println(Prefixes.CORE_BEAN + "Singleton - Same instance? " + (singleton1 == singleton2));

        // Prototype: different instances
        SimpleBean prototype1 = context.getBean("customBeanName", SimpleBean.class);
        SimpleBean prototype2 = context.getBean("customBeanName", SimpleBean.class);
        System.out.println(Prefixes.CORE_BEAN + "Prototype - Same instance? " + (prototype1 == prototype2));

        context.close();
    }

    // WAY 1: Using @Component (and stereotypes: @Service, @Repository, @Controller)
    @Component
    static class ComponentBean {
        public void doWork() {
            System.out.println(Prefixes.CORE_BEAN + "ComponentBean: Created using @Component");
        }
    }

    // WAY 2, 3, 4: Using @Configuration and @Bean
    @Configuration
    static class DemoConfig {

        // WAY 2: @Bean method in @Configuration
        @Bean
        public ConfigBean configBean() {
            return new ConfigBean();
        }

        // WAY 3: @Bean with custom name and scope
        @Bean(name = "customBeanName")
        @org.springframework.context.annotation.Scope("prototype")
        public SimpleBean simpleBean() {
            return new SimpleBean();
        }

        // WAY 4: Factory method pattern
        @Bean
        public FactoryBean factoryBean() {
            return FactoryBean.createInstance();
        }

        // WAY 5: Programmatic bean registration
        @Bean
        public ProgrammaticBean programmaticBean() {
            // Can have complex logic here
            ProgrammaticBean bean = new ProgrammaticBean();
            bean.setConfigValue("Configured programmatically");
            return bean;
        }
    }

    static class ConfigBean {
        public void doWork() {
            System.out.println(Prefixes.CORE_BEAN + "ConfigBean: Created using @Bean in @Configuration");
        }
    }

    static class SimpleBean {
        public void doWork() {
            System.out.println(Prefixes.CORE_BEAN + "SimpleBean: Created with custom name and prototype scope");
        }
    }

    static class FactoryBean {
        private FactoryBean() {}
        
        public static FactoryBean createInstance() {
            System.out.println(Prefixes.CORE_BEAN + "FactoryBean: Created via factory method");
            return new FactoryBean();
        }
        
        public void doWork() {
            System.out.println(Prefixes.CORE_BEAN + "FactoryBean: Working");
        }
    }

    static class ProgrammaticBean {
        private String configValue;
        
        public void setConfigValue(String value) {
            this.configValue = value;
        }
        
        public void doWork() {
            System.out.println(Prefixes.CORE_BEAN + "ProgrammaticBean: " + configValue);
        }
    }
}
