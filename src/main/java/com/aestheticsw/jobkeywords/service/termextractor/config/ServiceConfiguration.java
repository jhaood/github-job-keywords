/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.service.termextractor.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Service layer configuration which extends the RestClientConfiguration.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@Configuration
@ComponentScan(basePackages = { "com.aestheticsw.jobkeywords.service.termextractor.impl",
    "com.aestheticsw.jobkeywords.service.simple" })
public class ServiceConfiguration extends RestClientConfiguration {

}
