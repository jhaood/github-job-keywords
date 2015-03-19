/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.liquibase;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder.Builder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@PropertySource({ "classpath:application-mysql.properties" })
@ImportResource({ "classpath:liquibase/application-context-job-database.xml" })
@EntityScan(basePackages = { "com.aestheticsw.jobkeywords.service.termextractor.domain" })
public class LiquibaseJobDatabaseConfiguration {

    @Autowired
    private DataSource dataSource;
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder entityBuilder) {
        // .persistenceUnit("default").build();
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", "");
        jpaProperties.put("hibernate.ejb.naming_strategy",
            "org.springframework.boot.orm.jpa.hibernate.SpringNamingStrategy");
        
        Builder builder = entityBuilder.dataSource(dataSource);
        builder.jta(false);
        builder = builder.packages("com.aestheticsw.jobkeywords.service.termextractor.domain");
        builder.persistenceUnit("jobkeywords");
        builder = builder.properties(jpaProperties);
        LocalContainerEntityManagerFactoryBean factoryBean = builder.build();
        return factoryBean;
    }

    @Bean
    public LiquibaseActuator liquibase() {
        final LiquibaseActuator springLiquibase = new LiquibaseActuator();
        springLiquibase.setDataSource(dataSource);
        springLiquibase.setChangeLog("classpath:liquibase/changelog-master.xml");
        springLiquibase.setContexts("schema");
        springLiquibase.setDropFirst(false);
        springLiquibase.setShouldRun(false);
        return springLiquibase;
    }
}
