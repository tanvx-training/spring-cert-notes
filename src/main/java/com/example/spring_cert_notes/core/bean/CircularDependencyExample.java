package com.example.spring_cert_notes.core.bean;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * EXERCISE 2: HANDLING CIRCULAR DEPENDENCY
 * <p>
 * Circular dependency occurs when:
 * - Bean A depends on Bean B
 * - Bean B depends on Bean A
 * <p>
 * SOLUTIONS:
 * 1. Use @Lazy annotation
 * 2. Use Setter Injection instead of Constructor Injection
 * 3. Refactor code to eliminate circular dependency (BEST PRACTICE)
 */

// SOLUTION 1: Use @Lazy to break circular dependency
@Component
class ServiceA {
    private final ServiceB serviceB;

    // @Lazy creates a proxy, only injects when serviceB is actually used
    @Autowired
    public ServiceA(@Lazy ServiceB serviceB) {
        this.serviceB = serviceB;
        System.out.println(Prefixes.CORE_DI + "ServiceA: Constructor - ServiceB injected (lazy)");
    }

    public void doSomething() {
        System.out.println(Prefixes.CORE_BEAN + "ServiceA: Working...");
        serviceB.doSomethingElse();
    }
}

@Component
class ServiceB {
    private final ServiceA serviceA;

    @Autowired
    public ServiceB(ServiceA serviceA) {
        this.serviceA = serviceA;
        System.out.println(Prefixes.CORE_DI + "ServiceB: Constructor - ServiceA injected");
    }

    public void doSomethingElse() {
        System.out.println(Prefixes.CORE_BEAN + "ServiceB: Working on something else...");
    }
}

// SOLUTION 2: Use Setter Injection
@Component
class ServiceC {
    private ServiceD serviceD;

    @Autowired
    public void setServiceD(ServiceD serviceD) {
        this.serviceD = serviceD;
        System.out.println(Prefixes.CORE_DI + "ServiceC: Setter Injection - ServiceD injected");
    }

    public void process() {
        System.out.println(Prefixes.CORE_BEAN + "ServiceC: Processing...");
        serviceD.execute();
    }
}

@Component
class ServiceD {
    private ServiceC serviceC;

    @Autowired
    public void setServiceC(ServiceC serviceC) {
        this.serviceC = serviceC;
        System.out.println(Prefixes.CORE_DI + "ServiceD: Setter Injection - ServiceC injected");
    }

    public void execute() {
        System.out.println(Prefixes.CORE_BEAN + "ServiceD: Executing...");
    }
}
