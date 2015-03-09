package com.aestheticsw.jobkeywords.service.termextractor.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Service layer configuration.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@Configuration
@ComponentScan(basePackages = { "com.aestheticsw.jobkeywords.service.termextractor.impl" })
public class ServiceConfiguration extends RestClientConfiguration {

}
