package com.aestheticsw.config;

import org.springframework.logging.inject.LogInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogInjectorConfiguration {
    @Bean
    public LogInjector logInjector() {
        return new LogInjector();
    }
}