package com.aestheticsw.jobkeywords.liquibase;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * This configuration only runs if the "liquibase" profile is active.
 * 
 * This config class triggers spring to build and inject the LiquibaseActuator which, in turn builds
 * and injects the LiquibaseRunner.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */

// TODO ? move config outside of jobkeywords package because @CompnentScan(com.aestheticsw.jobkeywords) processes this liquibase configuration

@Profile("liquibase")
@Configuration
// @EnableAutoConfiguration pulls in LiquibaseAutoConfiguration which was excluded by JobKeywordsApplication and the test configurations. 
@EnableAutoConfiguration
public class LiquibaseRuntimeConfiguration {

    @Autowired
    private DataSource dataSource;

    @Value("${jobkeywords.liquibase.update}")
    private Boolean runLiquibaseUpdate;

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

    @Bean
    public LiquibaseRunner liquibaseRunner() {
        return new LiquibaseRunner(liquibase(), runLiquibaseUpdate);
    }
}
