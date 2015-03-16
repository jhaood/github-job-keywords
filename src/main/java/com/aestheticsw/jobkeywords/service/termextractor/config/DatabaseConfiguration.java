package com.aestheticsw.jobkeywords.service.termextractor.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder.Builder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

/**
 * JPA repository and database configuration.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@Configuration
@EnableAutoConfiguration
// Can't pull in properties HERE because can't override with a subsequent method-level prop-file
// @PropertySource("classpath:application.properties")
@EntityScan(basePackages = { "com.aestheticsw.jobkeywords.service.termextractor.domain" })
public class DatabaseConfiguration {

    /**
     * Spring tests aren't loading profile-specific property files - DAMN IT. So force it to load
     * the correct ones.
     */
    @Profile("!mysql")
    @Configuration
    @PropertySource({ "classpath:application.properties", "classpath:application-h2.properties" })
    public static class H2Profile {
    }

    /**
     * Spring tests aren't loading profile-specific property files - DAMN IT. So force it to load
     * the correct ones.
     */
    @Profile("mysql")
    @Configuration
    @PropertySource({ "classpath:application.properties", "classpath:application-mysql.properties" })
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

    /*
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }
    */

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(datasourceUrl);
        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.username(userName);
        dataSourceBuilder.password(password);

        return dataSourceBuilder.build();
    }

    /*
     * hibernate.hbm2ddl.auto=create-drop
     * hibernate.transaction.jta.platform=org.hibernate.engine.transaction
     * .jta.platform.internal.NoJtaPlatform@3d40a3b4
     * hibernate.ejb.naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringNamingStrategy
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder entityBuilder) {
        // .persistenceUnit("default").build();
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", "none");
        jpaProperties.put("hibernate.ejb.naming_strategy",
            "org.springframework.boot.orm.jpa.hibernate.SpringNamingStrategy");
        Builder builder = entityBuilder.dataSource(dataSource());
        builder.jta(false);
        builder = builder.packages("com.aestheticsw.jobkeywords.service.termextractor.domain");
        builder = builder.properties(jpaProperties);
        LocalContainerEntityManagerFactoryBean factoryBean = builder.build();
        return factoryBean;
    }


    /*
     * EmbeddedDatabaseBuilder doesn't work ... 
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }
    */

}
