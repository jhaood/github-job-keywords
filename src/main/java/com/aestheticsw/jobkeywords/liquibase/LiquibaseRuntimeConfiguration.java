package com.aestheticsw.jobkeywords.liquibase;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("liquibase")
@Configuration
@EnableAutoConfiguration
// @PropertySource({ "classpath:application-mysql.properties" })
// @ImportResource({ "classpath:liquibase/application-context-job-database.xml" })
// @EntityScan(basePackages = { "com.aestheticsw.jobkeywords.service.termextractor.domain" })
public class LiquibaseRuntimeConfiguration {

    @Autowired
    private DataSource dataSource;
    
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
