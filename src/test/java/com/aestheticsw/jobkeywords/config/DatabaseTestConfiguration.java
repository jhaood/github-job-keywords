package com.aestheticsw.jobkeywords.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.aestheticsw.jobkeywords.utils.LiquibaseActuator;

@Configuration
@ImportResource({ "classpath:application-context-job-database.xml" })
public class DatabaseTestConfiguration {
    @Autowired
    private DataSource dataSource;

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    /*
     * CAN"T DEFINE THIS DATASOURCE here because Spring Bean Post Processor SOMETIMES instantiates @Bean
     * config before injecting @Value config.
     * 
     * @Bean public DataSource liquibaseDataSource() { DataSourceBuilder dataSourceBuilder =
     *       DataSourceBuilder.create(); dataSourceBuilder.url(embeddedH2DatasourceUrl);
     *       dataSourceBuilder.driverClassName(embeddedH2DriverClassName);
     *       dataSourceBuilder.username(embeddedH2UserName);
     *       dataSourceBuilder.password(embeddedH2Password);
     * 
     *       return dataSourceBuilder.build(); }
     */

    @Bean
    public LiquibaseActuator liquibase() {
        final LiquibaseActuator springLiquibase = new LiquibaseActuator();
        springLiquibase.setDataSource(dataSource);
        springLiquibase.setChangeLog("liquibase/changelog-master.xml");
        springLiquibase.setContexts("schema");
        springLiquibase.setDropFirst(false);
        springLiquibase.setShouldRun(false);
        return springLiquibase;
    }
}
