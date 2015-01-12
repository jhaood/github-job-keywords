package com.aestheticsw;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.Logger;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class JobKeywordsApplication {
    @Log
    private Logger log;
    
    @Value("${app.name}")
    private String appName;
    
    
    public static void main(String[] args) {
        log
        SpringApplication.run(JobKeywordsApplication.class, args);
    }
}
