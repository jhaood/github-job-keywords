package com.aestheticsw.jobkeywords.service.termextractor.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = { "com.aestheticsw.jobkeywords.service.termextractor.config", "com.aestheticsw.jobkeywords.service.termextractor.repository" })
@EntityScan(basePackages = { "com.aestheticsw.jobkeywords.service.termextractor.domain" })
@EnableJpaRepositories(
        value = "com.aestheticsw.jobkeywords.service.termextractor.repository",
        queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND)
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfiguration {
    
    @Value("${datasource.jobkeywords.url}")
    private String datasourceUrl;

    @Value("${datasource.jobkeywords.driverClassName}")
    private String driverClassName;

    @Bean
    // @ConfigurationProperties(prefix = "datasource.test", ignoreUnknownFields = false)
    public DataSource primaryDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(datasourceUrl);
        dataSourceBuilder.driverClassName(driverClassName);
        // dataSourceBuilder.
        
        return dataSourceBuilder.build();   
    }

    /*
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }
    */
}
