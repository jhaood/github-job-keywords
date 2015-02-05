package com.aestheticsw.jobkeywords.config;

import net.exacode.spring.logging.inject.LogInjector;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure the log injector that supports the @Log annotation
 */
@Configuration
public class LogInjectorConfiguration {
    @Bean
    public LogInjector logInjector() {
        return new LogInjector();
    }
}