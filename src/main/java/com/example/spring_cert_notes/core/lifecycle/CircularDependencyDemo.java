package com.example.spring_cert_notes.core.lifecycle;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * CIRCULAR DEPENDENCY DEMONSTRATION
 * 
 * Circular dependency: A depends on B, B depends on A
 * 
 * Solutions:
 * 1. @Lazy - Creates proxy, delays actual injection
 * 2. Setter Injection - Allows partial construction
 * 3. @PostConstruct - Inject after construction
 * 4. Refactor - Best solution, eliminate the cycle
 */

// ============================================================
// SOLUTION 1: @Lazy Annotation (RECOMMENDED)
// ============================================================
@Component
class ServiceX {
    private final ServiceY serviceY;
    
    @Autowired
    public ServiceX(@Lazy ServiceY serviceY) {
        // ServiceY is a PROXY here, not the actual bean
        this.serviceY = serviceY;
        System.out.println(Prefixes.CORE_DI + "ServiceX: Constructor - ServiceY injected (lazy proxy)");
    }
    
    public void doX() {
        System.out.println(Prefixes.CORE_BEAN + "ServiceX: Doing X");
        // Actual ServiceY is resolved here when first used
        serviceY.doY();
    }
}

@Component
class ServiceY {
    private final ServiceX serviceX;
    
    @Autowired
    public ServiceY(ServiceX serviceX) {
        this.serviceX = serviceX;
        System.out.println(Prefixes.CORE_DI + "ServiceY: Constructor - ServiceX injected");
    }
    
    public void doY() {
        System.out.println(Prefixes.CORE_BEAN + "ServiceY: Doing Y");
    }
}

// ============================================================
// SOLUTION 2: Setter Injection
// ============================================================
@Component
class ServiceM {
    private ServiceN serviceN;
    
    public ServiceM() {
        System.out.println(Prefixes.CORE_DI + "ServiceM: Constructor (no dependencies yet)");
    }
    
    @Autowired
    public void setServiceN(ServiceN serviceN) {
        this.serviceN = serviceN;
        System.out.println(Prefixes.CORE_DI + "ServiceM: Setter - ServiceN injected");
    }
    
    public void doM() {
        System.out.println(Prefixes.CORE_BEAN + "ServiceM: Doing M");
    }
}

@Component
class ServiceN {
    private ServiceM serviceM;
    
    public ServiceN() {
        System.out.println(Prefixes.CORE_DI + "ServiceN: Constructor (no dependencies yet)");
    }
    
    @Autowired
    public void setServiceM(ServiceM serviceM) {
        this.serviceM = serviceM;
        System.out.println(Prefixes.CORE_DI + "ServiceN: Setter - ServiceM injected");
    }
    
    public void doN() {
        System.out.println(Prefixes.CORE_BEAN + "ServiceN: Doing N");
    }
}

// ============================================================
// SOLUTION 3: ObjectProvider / Provider (Lazy Resolution)
// ============================================================
@Component
class ServiceP {
    private final org.springframework.beans.factory.ObjectProvider<ServiceQ> serviceQProvider;
    
    @Autowired
    public ServiceP(org.springframework.beans.factory.ObjectProvider<ServiceQ> serviceQProvider) {
        this.serviceQProvider = serviceQProvider;
        System.out.println(Prefixes.CORE_DI + "ServiceP: Constructor - ObjectProvider injected");
    }
    
    public void doP() {
        System.out.println(Prefixes.CORE_BEAN + "ServiceP: Doing P");
        // ServiceQ is resolved lazily when getObject() is called
        serviceQProvider.getObject().doQ();
    }
}

@Component
class ServiceQ {
    private final ServiceP serviceP;
    
    @Autowired
    public ServiceQ(ServiceP serviceP) {
        this.serviceP = serviceP;
        System.out.println(Prefixes.CORE_DI + "ServiceQ: Constructor - ServiceP injected");
    }
    
    public void doQ() {
        System.out.println(Prefixes.CORE_BEAN + "ServiceQ: Doing Q");
    }
}
