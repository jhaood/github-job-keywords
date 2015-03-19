package com.aestheticsw.jobkeywords.service.termextractor.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA repository and database configuration.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */

// Can't pull in properties at class level because can't override with a subsequent method-level @PropertySource
// @PropertySource("classpath:application.properties")
// TODO add audit columns after user-authentication is added 
// @EnableJpaAuditing

@Configuration
@ComponentScan(basePackages = { "com.aestheticsw.jobkeywords.service.termextractor.repository" })
@EntityScan(basePackages = { "com.aestheticsw.jobkeywords.service.termextractor.domain" })
@EnableJpaRepositories(
        value = "com.aestheticsw.jobkeywords.service.termextractor.repository",
        queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND)
@EnableTransactionManagement
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

    /**
     * The default DataSource configuration uses transaction-isolation = READ_UNCOMMITTED. This may
     * need to change.
     * 
     * <pre>
     * spring's TransactionDefinition defines: 
     * ISOLATION_READ_UNCOMMITTED = -1 (= ISOLATION_DEFAULT)
     * ISOLATION_READ_COMMITTED = -2
     * </pre>
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

    /* Let spring configure the "transactionManager" bean
    @Bean
    public PlatformTransactionManager transactionManager() {
         return new JpaTransactionManager();
    }
    */

    /*
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder entityBuilder) {
        // .persistenceUnit("default").build();
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", "");
        jpaProperties.put("hibernate.ejb.naming_strategy",
            "org.springframework.boot.orm.jpa.hibernate.SpringNamingStrategy");

        Builder builder = entityBuilder.dataSource(dataSource());
        // builder.jta(true);
        builder = builder.packages("com.aestheticsw.jobkeywords.service.termextractor.domain");
        builder.persistenceUnit("jobkeywords");
        builder = builder.properties(jpaProperties);
        LocalContainerEntityManagerFactoryBean factoryBean = builder.build();
        return factoryBean;
    }
    */

    /* EmbeddedDatabaseBuilder doesn't work ... 
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }
    */
}
