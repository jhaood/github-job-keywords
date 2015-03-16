package com.aestheticsw.jobkeywords.service.termextractor.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = { "com.aestheticsw.jobkeywords.service.termextractor.config",
"com.aestheticsw.jobkeywords.service.termextractor.repository" })
@EnableJpaRepositories(
        value = "com.aestheticsw.jobkeywords.service.termextractor.repository",
        queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND)
@EnableJpaAuditing
@EnableTransactionManagement
public class RepositoryConfiguration {

}
