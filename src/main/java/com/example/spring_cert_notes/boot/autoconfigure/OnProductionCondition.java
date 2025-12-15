package com.example.spring_cert_notes.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Custom Condition extending SpringBootCondition
 * 
 * SpringBootCondition provides better logging và debugging support
 */
public class OnProductionCondition extends SpringBootCondition {
    
    @Override
    public ConditionOutcome getMatchOutcome(
            ConditionContext context, 
            AnnotatedTypeMetadata metadata) {
        
        String env = context.getEnvironment().getProperty("app.environment", "development");
        
        ConditionMessage.Builder message = ConditionMessage
            .forCondition("OnProductionCondition");
        
        if ("production".equalsIgnoreCase(env)) {
            return ConditionOutcome.match(
                message.foundExactly("production environment")
            );
        }
        
        return ConditionOutcome.noMatch(
            message.didNotFind("production environment")
                   .items("current: " + env)
        );
    }
}
