package com.aestheticsw.jobkeywords;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// @SpringBootApplication annotation is not necessary

@Configuration
// @EnableAutoConfiguration is Not required for tests, but is required to launch app-JAR 
@EnableAutoConfiguration
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
