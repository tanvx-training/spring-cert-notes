package com.example.spring_cert_notes.core.bean;

import com.example.spring_cert_notes.Prefixes;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * EXAMPLE 3: Separate configuration class for DataSource
 * Will be imported into AppConfig
 */
@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        System.out.println(Prefixes.CORE_BEAN + "DataSourceConfig: Creating DataSource bean");
        return new DataSource("jdbc:mysql://localhost:3306/mydb", "root", "password");
    }

    /**
     * Simple inner class for demonstration
     */
    public static class DataSource {
        @Getter
        private String url;
        private String username;
        private String password;

        public DataSource(String url, String username, String password) {
            this.url = url;
            this.username = username;
            this.password = password;
        }

        public void connect() {
            System.out.println(Prefixes.CORE_BEAN + "DataSource: Connecting to " + url);
        }

    }
}
