package com.example.spring_cert_notes.core.profile;

import com.example.spring_cert_notes.Prefixes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * ADVANCED SpEL Examples
 * <p>
 * Demonstrates:
 * - Collection filtering and projection
 * - Bean method invocation
 * - Safe navigation operator (?.)
 * - Type references (T())
 * - Complex conditional logic
 */
@Component
public class AdvancedSpELExamples {
    
    // COLLECTION FILTERING
    // Filter list where value > 5
    @Value("#{T(java.util.Arrays).asList(1,2,3,4,5,6,7,8,9,10).?[#this > 5]}")
    private List<Integer> filteredNumbers;
    
    // COLLECTION PROJECTION
    // Get first element that matches condition
    @Value("#{T(java.util.Arrays).asList(1,2,3,4,5).^[#this > 2]}")
    private Integer firstMatch;
    
    // Get last element that matches condition
    @Value("#{T(java.util.Arrays).asList(1,2,3,4,5).$[#this > 2]}")
    private Integer lastMatch;
    
    // BEAN REFERENCE
    // Reference another bean's property (if exists)
    @Value("#{@propertySourcesConfig != null ? 'Bean exists' : 'Bean not found'}")
    private String beanReference;
    
    // STATIC METHOD CALL
    @Value("#{T(java.lang.System).getProperty('java.version')}")
    private String javaVersionStatic;
    
    @Value("#{T(java.util.UUID).randomUUID().toString()}")
    private String randomUUID;
    
    // COMPLEX CONDITIONAL
    @Value("#{systemProperties['os.name'].toLowerCase().contains('windows') ? 'Windows' : 'Unix-like'}")
    private String osType;
    
    // NESTED PROPERTIES
    @Value("#{systemProperties['java.version'].substring(0, systemProperties['java.version'].indexOf('.'))}")
    private String javaMajorVersion;
    
    // NULL-SAFE NAVIGATION
    @Value("#{systemProperties['non.existent']?.toUpperCase()}")
    private String nullSafeNavigation;
    
    // INLINE LIST
    @Value("#{{'dev', 'test', 'staging', 'prod'}}")
    private List<String> inlineList;
    
    // INLINE MAP
    @Value("#{{key1: 'value1', key2: 'value2'}}")
    private Map<String, String> inlineMap;
    
    // MATHEMATICAL FUNCTIONS
    @Value("#{T(java.lang.Math).PI}")
    private double pi;
    
    @Value("#{T(java.lang.Math).sqrt(16)}")
    private double squareRoot;
    
    @Value("#{T(java.lang.Math).pow(2, 8)}")
    private double power;
    
    // STRING MATCHING
    @Value("#{'Spring Framework'.matches('.*Framework.*')}")
    private boolean stringMatches;
    
    // INSTANCEOF CHECK (using T())
    @Value("#{T(java.lang.String).class.getName()}")
    private String stringClassName;
    
    @PostConstruct
    public void displayAdvancedSpEL() {
        System.out.println("\n" + Prefixes.CORE_SPEL + "=== Advanced SpEL Examples ===");
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "COLLECTION OPERATIONS:");
        System.out.println(Prefixes.CORE_SPEL + "  Filtered (>5): " + filteredNumbers);
        System.out.println(Prefixes.CORE_SPEL + "  First Match (>2): " + firstMatch);
        System.out.println(Prefixes.CORE_SPEL + "  Last Match (>2): " + lastMatch);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "BEAN REFERENCE:");
        System.out.println(Prefixes.CORE_SPEL + "  Bean Check: " + beanReference);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "STATIC METHODS:");
        System.out.println(Prefixes.CORE_SPEL + "  Java Version: " + javaVersionStatic);
        System.out.println(Prefixes.CORE_SPEL + "  Random UUID: " + randomUUID);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "COMPLEX CONDITIONALS:");
        System.out.println(Prefixes.CORE_SPEL + "  OS Type: " + osType);
        System.out.println(Prefixes.CORE_SPEL + "  Java Major Version: " + javaMajorVersion);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "NULL-SAFE:");
        System.out.println(Prefixes.CORE_SPEL + "  Null Safe Result: " + nullSafeNavigation);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "INLINE COLLECTIONS:");
        System.out.println(Prefixes.CORE_SPEL + "  Inline List: " + inlineList);
        System.out.println(Prefixes.CORE_SPEL + "  Inline Map: " + inlineMap);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "MATH FUNCTIONS:");
        System.out.println(Prefixes.CORE_SPEL + "  PI: " + pi);
        System.out.println(Prefixes.CORE_SPEL + "  sqrt(16): " + squareRoot);
        System.out.println(Prefixes.CORE_SPEL + "  2^8: " + power);
        
        System.out.println("\n" + Prefixes.CORE_SPEL + "STRING MATCHING:");
        System.out.println(Prefixes.CORE_SPEL + "  Matches: " + stringMatches);
        System.out.println(Prefixes.CORE_SPEL + "  String Class: " + stringClassName);
    }
}
