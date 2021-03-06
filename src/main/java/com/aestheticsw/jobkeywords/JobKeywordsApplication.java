/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.aestheticsw.jobkeywords.shared.config.Log;

/**
 * The JobKeywordsApplication is the primary configuration class for the Spring-Boot application
 * runtime.
 * <p/>
 * The application-level configuration does not load when integration tests run because the uses of @ComponentScan
 * and @ContextConfiguration are limited and specific.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */

// The @SpringBootApplication annotation is not necessary

@Configuration
@EnableAutoConfiguration(exclude = { LiquibaseAutoConfiguration.class })
@ComponentScan(basePackages = "com.aestheticsw.jobkeywords")
public class JobKeywordsApplication {

    @Log
    private Logger log;

    @Value("${app.name}")
    private String appName;

    public static void main(String[] args) {
        SpringApplication.run(JobKeywordsApplication.class, args);
    }
}
