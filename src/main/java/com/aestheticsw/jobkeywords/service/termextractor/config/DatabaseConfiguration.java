package com.aestheticsw.jobkeywords.service.termextractor.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
// Can't pull in properties here because can't override with a subsequent method-level prop-file
// @PropertySource("classpath:application.properties")
@ComponentScan(basePackages = { "com.aestheticsw.jobkeywords.service.termextractor.config",
    "com.aestheticsw.jobkeywords.service.termextractor.repository" })
@EntityScan(basePackages = { "com.aestheticsw.jobkeywords.service.termextractor.domain" })
@EnableJpaRepositories(
        value = "com.aestheticsw.jobkeywords.service.termextractor.repository",
        queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND)
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfiguration {

    /**
     * Spring tests aren't loading profile-specific property files - DAMN IT. So force it to load
     * the correct ones.
     */
    @Profile("!mysql")
    @Configuration
    @PropertySource({"classpath:application.properties"})
    public static class H2Profile {
    }
    
    /**
     * Spring tests aren't loading profile-specific property files - DAMN IT. So force it to load
     * the correct ones.
     */
    @Profile("mysql")
    @Configuration
    @PropertySource({"classpath:application.properties", "classpath:application-mysql.properties" })
    public static class MysqlProfile {
    }

    @Value("${datasource.jobkeywords.url}")
    private String datasourceUrl;

    @Value("${datasource.jobkeywords.driverClassName}")
    private String driverClassName;

    @Value("${datasource.jobkeywords.username}")
    private String userName;

    @Value("${datasource.jobkeywords.password}")
    private String password;

    @Bean
    // @ConfigurationProperties(prefix = "datasource.test", ignoreUnknownFields = true)
    public DataSource primaryDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(datasourceUrl);
        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.username(userName);
        dataSourceBuilder.password(password);

        return dataSourceBuilder.build();
    }

    /*
     * EmbeddedDatabaseBuilder doesn't work ... 
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }
    */
}
