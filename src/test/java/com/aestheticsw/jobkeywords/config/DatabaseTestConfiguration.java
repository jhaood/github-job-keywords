/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.Configuration;

//Must use java config instead of XML files in order to leverage profiles 
//@ImportResource({ "classpath:application-context-job-database.xml" })

@Configuration
@EnableAutoConfiguration(exclude = { LiquibaseAutoConfiguration.class })
public class DatabaseTestConfiguration {
}
