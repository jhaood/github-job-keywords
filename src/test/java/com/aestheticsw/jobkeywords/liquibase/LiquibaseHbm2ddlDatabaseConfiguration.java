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

//    @Autowired
//    private DataSource dataSource;
    
    @Autowired
    private DataSource hbm2ddlDataSource;
    
//     @Autowired
//     private EntityManagerFactoryBuilder entityManagerFactoryBuilder;
    
//    @Value("${datasource.jobkeywords.url}")
//    private String datasourceUrl;
//
//    @Value("${datasource.jobkeywords.driverClassName}")
//    private String driverClassName;
//
//    @Value("${datasource.jobkeywords.username}")
//    private String userName;
//
//    @Value("${datasource.jobkeywords.password}")
//    private String password;
/*
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory(entityManagerFactoryBuilder).getNativeEntityManagerFactory());
        // return new JpaTransactionManager();
    }
*/    
    /*
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }
    */

    /*
    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(datasourceUrl);
        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.username(userName);
        dataSourceBuilder.password(password);

        return dataSourceBuilder.build();
    }
    */

    /*
     * hibernate.hbm2ddl.auto=create-drop
     * hibernate.transaction.jta.platform=org.hibernate.engine.transaction
     * .jta.platform.internal.NoJtaPlatform@3d40a3b4
     * hibernate.ejb.naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringNamingStrategy
     */
    /*
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
*/
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder entityBuilder) {
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", "create");
        jpaProperties.put("hibernate.ejb.naming_strategy",
            "org.springframework.boot.orm.jpa.hibernate.SpringNamingStrategy");
        
        Builder builder = entityBuilder.dataSource(hbm2ddlDataSource);
        builder.jta(false);
        builder = builder.packages("com.aestheticsw.jobkeywords.service.termextractor.domain");
        // TODO remove this PU-name
        builder.persistenceUnit("liquibase");
        builder = builder.properties(jpaProperties);
        LocalContainerEntityManagerFactoryBean factoryBean = builder.build();
        return factoryBean;
    }

    /*
    @Bean
    public LocalContainerEntityManagerFactoryBean exampleEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource())
                .packages(QueryKey.class)
                .persistenceUnit("example")
                .build();
    }
    */

    /*
     * CAN"T DEFINE THIS DATASOURCE here because Spring Bean Post Processor SOMETIMES instantiates @Bean
     * config before injecting @Value config.
     * 
     * @Bean public DataSource JUNKembeddedH2DataSource() { DataSourceBuilder dataSourceBuilder =
     *       DataSourceBuilder.create(); dataSourceBuilder.url(embeddedH2DatasourceUrl);
     *       dataSourceBuilder.driverClassName(embeddedH2DriverClassName);
     *       dataSourceBuilder.username(embeddedH2UserName);
     *       dataSourceBuilder.password(embeddedH2Password);
     * 
     *       return dataSourceBuilder.build(); }
     */
/*
    @Bean
    public LiquibaseActuator liquibase() {
        final LiquibaseActuator springLiquibase = new LiquibaseActuator();
        springLiquibase.setDataSource(hbm2ddlDataSource);
        springLiquibase.setChangeLog("liquibase/changelog-master.xml");
        springLiquibase.setContexts("schema");
        springLiquibase.setDropFirst(false);
        springLiquibase.setShouldRun(false);
        return springLiquibase;
    }
*/
}
