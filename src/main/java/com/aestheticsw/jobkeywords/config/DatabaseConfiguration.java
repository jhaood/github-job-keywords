package com.aestheticsw.jobkeywords.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA repository and database configuration.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@Configuration
@ComponentScan(basePackages = { "com.aestheticsw.jobkeywords.domain", "com.aestheticsw.jobkeywords.service" })
@EnableAutoConfiguration
@EntityScan(basePackages = { "com.aestheticsw.jobkeywords.domain" })
@EnableJpaRepositories(
        value = "com.aestheticsw.jobkeywords.service",
        queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND)
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfiguration {
/*
    @Bean
    @ConfigurationProperties(prefix="datasource.test")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
*/        
}
