package com.example.spring_cert_notes.core.annotation;

import com.example.spring_cert_notes.Prefixes;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * STEREOTYPE ANNOTATIONS EXAMPLES
 * <p>
 * All stereotypes are specializations of @Component:
 * 
 * @Component - Generic component (base)
 *    ├── @Service - Business logic layer
 *    ├── @Repository - Data access layer (+ exception translation)
 *    └── @Controller - Web layer (MVC controllers)
 *        └── @RestController - REST API controllers (@Controller + @ResponseBody)
 */

// ============================================================
// 1. @COMPONENT - Generic Spring-managed component
// ============================================================
@Component
class GenericComponent {
    
    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_SCAN + "GenericComponent: @Component - Generic bean");
    }
    
    public void doWork() {
        System.out.println(Prefixes.CORE_BEAN + "GenericComponent: Doing generic work");
    }
}

// ============================================================
// 2. @SERVICE - Business/Service layer
// ============================================================
@Service
class OrderService {
    
    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_SCAN + "OrderService: @Service - Business logic layer");
    }
    
    public void processOrder(String orderId) {
        System.out.println(Prefixes.CORE_BEAN + "OrderService: Processing order " + orderId);
    }
}

@Service("customServiceName")  // Custom bean name
class PaymentService {
    
    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_SCAN + "PaymentService: @Service with custom name 'customServiceName'");
    }
}

// ============================================================
// 3. @REPOSITORY - Data Access layer
// ============================================================
@Repository
class OrderRepository {
    
    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_SCAN + "OrderRepository: @Repository - Data access layer");
        System.out.println(Prefixes.CORE_SCAN + "  Note: @Repository enables exception translation");
    }
    
    public void save(Object entity) {
        System.out.println(Prefixes.CORE_BEAN + "OrderRepository: Saving entity");
    }
}

// ============================================================
// 4. @CONTROLLER - Web/MVC layer
// ============================================================
@Controller
class OrderController {
    
    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_SCAN + "OrderController: @Controller - Web MVC layer");
    }
}

// ============================================================
// 5. CUSTOM STEREOTYPE - Using @CustomStereotype
// ============================================================
@CustomStereotype
class MyCustomComponent {
    
    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_SCAN + "MyCustomComponent: @CustomStereotype - Custom stereotype");
    }
}

// ============================================================
// 6. CUSTOM @Gateway STEREOTYPE
// ============================================================
@Gateway
class PaymentGateway {
    
    @PostConstruct
    public void init() {
        System.out.println(Prefixes.CORE_SCAN + "PaymentGateway: @Gateway - Custom gateway stereotype");
    }
    
    public void processPayment() {
        System.out.println(Prefixes.CORE_BEAN + "PaymentGateway: Processing payment via gateway");
    }
}
