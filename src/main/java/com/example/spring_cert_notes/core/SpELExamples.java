package com.example.spring_cert_notes.core;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.util.List;

/**
 * EXAMPLE 3: Spring Expression Language (SpEL) Examples
 * <p>
 * SpEL syntax: #{ expression }
 * Property syntax: ${ property.name }
 * <p>
 * Demonstrates 10+ common SpEL expressions:
 * 1. Literal values
 * 2. System properties
 * 3. Environment variables
 * 4. Mathematical operations
 * 5. String operations
 * 6. Boolean logic
 * 7. Conditional (ternary) operator
 * 8. Regular expressions
 * 9. Collections
 * 10. Bean references
 * 11. Method invocation
 * 12. Safe navigation operator
 */
@Component
public class SpELExamples {
    
    // 1. LITERAL VALUES
    @Value("#{100}")
    private int literalNumber;
    
    @Value("#{'Hello SpEL'}")
    private String literalString;
    
    @Value("#{true}")
    private boolean literalBoolean;
    
    // 2. SYSTEM PROPERTIES
    @Value("#{systemProperties['user.name']}")
    private String userName;
    
    @Value("#{systemProperties['user.country'] ?: 'US'}")
    private String userCountry;
    
    // 3. ENVIRONMENT VARIABLES
    @Value("#{systemEnvironment['JAVA_HOME'] ?: 'Not Set'}")
    private String javaHome;
    
    // 4. MATHEMATICAL OPERATIONS
    @Value("#{10 + 20}")
    private int addition;
    
    @Value("#{100 * 2}")
    private int multiplication;
    
    @Value("#{(10 + 20) * 2}")
    private int complexMath;
    
    @Value("#{T(java.lang.Math).random() * 100}")
    private double randomNumber;
    
    @Value("#{T(java.lang.Math).max(10, 20)}")
    private int maxValue;
    
    // 5. STRING OPERATIONS
    @Value("#{'Hello'.concat(' World')}")
    private String concatenation;
    
    @Value("#{'SPRING'.toLowerCase()}")
    private String lowercase;
    
    @Value("#{'spring'.toUpperCase()}")
    private String uppercase;
    
    @Value("#{'Hello World'.substring(0, 5)}")
    private String substring;
    
    @Value("#{'Hello World'.length()}")
    private int stringLength;
    
    // 6. BOOLEAN LOGIC
    @Value("#{10 > 5}")
    private boolean comparison;
    
    @Value("#{10 > 5 and 20 < 30}")
    private boolean andLogic;
    
    @Value("#{10 > 5 or 20 > 30}")
    private boolean orLogic;
    
    @Value("#{!(10 > 5)}")
    private boolean notLogic;
    
    // 7. CONDITIONAL (TERNARY) OPERATOR
    @Value("#{10 > 5 ? 'Yes' : 'No'}")
    private String ternary;
    
    @Value("${app.environment:dev}")
    private String environment;
    
    @Value("#{environment == 'prod' ? 100 : 10}")
    private int conditionalValue;
    
    // 8. REGULAR EXPRESSIONS
    @Value("#{'john@example.com' matches '[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}'}")
    private boolean isValidEmail;
    
    // 9. COLLECTIONS (using T() for static access)
    @Value("#{T(java.util.Arrays).asList('dev', 'test', 'prod')}")
    private List<String> environments;
    
    // 10. ELVIS OPERATOR (null-safe)
    @Value("#{systemProperties['non.existent.property'] ?: 'default-value'}")
    private String elvisOperator;
    
    // 11. PROPERTY REFERENCE IN SpEL
    @Value("#{'${app.name:MyApp}'.toUpperCase()}")
    private String appNameUpper;
    
    // 12. COMPLEX EXPRESSION
    @Value("#{T(java.time.LocalDateTime).now().toString()}")
    private String currentDateTime;
    
    @PostConstruct
    public void displaySpELResults() {
        System.out.println("\n" + Prefixes.CORE_SPEL + "=== SpEL Examples Results ===");
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "1. LITERALS:");
        System.out.println(Prefixes.CORE_SPEL + "  Number: " + literalNumber);
        System.out.println(Prefixes.CORE_SPEL + "  String: " + literalString);
        System.out.println(Prefixes.CORE_SPEL + "  Boolean: " + literalBoolean);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "2. SYSTEM PROPERTIES:");
        System.out.println(Prefixes.CORE_SPEL + "  User Name: " + userName);
        System.out.println(Prefixes.CORE_SPEL + "  User Country: " + userCountry);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "3. ENVIRONMENT:");
        System.out.println(Prefixes.CORE_SPEL + "  JAVA_HOME: " + javaHome);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "4. MATH OPERATIONS:");
        System.out.println(Prefixes.CORE_SPEL + "  10 + 20 = " + addition);
        System.out.println(Prefixes.CORE_SPEL + "  100 * 2 = " + multiplication);
        System.out.println(Prefixes.CORE_SPEL + "  (10 + 20) * 2 = " + complexMath);
        System.out.println(Prefixes.CORE_SPEL + "  Random: " + String.format("%.2f", randomNumber));
        System.out.println(Prefixes.CORE_SPEL + "  Max(10, 20) = " + maxValue);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "5. STRING OPERATIONS:");
        System.out.println(Prefixes.CORE_SPEL + "  Concat: " + concatenation);
        System.out.println(Prefixes.CORE_SPEL + "  Lowercase: " + lowercase);
        System.out.println(Prefixes.CORE_SPEL + "  Uppercase: " + uppercase);
        System.out.println(Prefixes.CORE_SPEL + "  Substring: " + substring);
        System.out.println(Prefixes.CORE_SPEL + "  Length: " + stringLength);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "6. BOOLEAN LOGIC:");
        System.out.println(Prefixes.CORE_SPEL + "  10 > 5: " + comparison);
        System.out.println(Prefixes.CORE_SPEL + "  AND: " + andLogic);
        System.out.println(Prefixes.CORE_SPEL + "  OR: " + orLogic);
        System.out.println(Prefixes.CORE_SPEL + "  NOT: " + notLogic);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "7. CONDITIONAL:");
        System.out.println(Prefixes.CORE_SPEL + "  Ternary: " + ternary);
        System.out.println(Prefixes.CORE_SPEL + "  Environment: " + environment);
        System.out.println(Prefixes.CORE_SPEL + "  Conditional Value: " + conditionalValue);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "8. REGEX:");
        System.out.println(Prefixes.CORE_SPEL + "  Valid Email: " + isValidEmail);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "9. COLLECTIONS:");
        System.out.println(Prefixes.CORE_SPEL + "  Environments: " + environments);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "10. ELVIS OPERATOR:");
        System.out.println(Prefixes.CORE_SPEL + "  Elvis: " + elvisOperator);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "11. PROPERTY + SpEL:");
        System.out.println(Prefixes.CORE_SPEL + "  App Name Upper: " + appNameUpper);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "12. COMPLEX:");
        System.out.println(Prefixes.CORE_SPEL + "  Current DateTime: " + currentDateTime);
    }
}
