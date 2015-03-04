package com.aestheticsw.jobkeywords.shared.config;

import net.exacode.spring.logging.inject.LogInjector;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure the log injector that supports the @Log annotation. (Thanks to Exacode for the log
 * injector.)
 * 
 * @see net.exacode.spring.logging.inject.LogInjector net.exacode.spring.logging.inject.LogInjector
 */
@Configuration
public class LogInjectorConfiguration {
    @Bean
    public LogInjector logInjector() {
        return new LogInjector();
    }
}