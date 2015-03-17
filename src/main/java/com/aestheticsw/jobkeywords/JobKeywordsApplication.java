package com.aestheticsw.jobkeywords;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The JobKeywordsApplication is the primary configuration class for both the spring-boot
 * application runtime and for all the integration tests.
 * 
 * There is no easy way to separate the integ-test configuration from the Application config.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */

// The @SpringBootApplication annotation is not necessary

@Configuration
@EnableAutoConfiguration(exclude = {LiquibaseAutoConfiguration.class})
@ComponentScan(basePackages = "com.aestheticsw")
public class JobKeywordsApplication {

    @Log
    private Logger log;

    @Value("${app.name}")
    private String appName;

    public static void main(String[] args) {
        SpringApplication.run(JobKeywordsApplication.class, args);
    }
}
