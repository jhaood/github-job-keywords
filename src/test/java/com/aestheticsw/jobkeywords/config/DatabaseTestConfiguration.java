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
