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
@PropertySource({ "classpath:liquibase/application-hbm2ddl.properties" })
@ImportResource({ "classpath:liquibase/application-context-hbm2ddl-database.xml" })
@EntityScan(basePackages = { "com.aestheticsw.jobkeywords.service.termextractor.domain" })
public class LiquibaseHbm2ddlDatabaseConfiguration {

    @Autowired
    private DataSource hbm2ddlDataSource;

    @Autowired
    private DataSource liquibaseDataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder entityBuilder) {
        Map<String, String> jpaProperties = new HashMap<>();
        // this insures that the DB will always be recreated 
        jpaProperties.put("hibernate.hbm2ddl.auto", "create-drop");
        jpaProperties.put("hibernate.ejb.naming_strategy",
            "org.springframework.boot.orm.jpa.hibernate.SpringNamingStrategy");

        Builder builder = entityBuilder.dataSource(hbm2ddlDataSource);
        builder.jta(false);
        builder = builder.packages("com.aestheticsw.jobkeywords.service.termextractor.domain");
        builder.persistenceUnit("hbm2ddl");
        builder = builder.properties(jpaProperties);
        LocalContainerEntityManagerFactoryBean factoryBean = builder.build();
        return factoryBean;
    }

}
