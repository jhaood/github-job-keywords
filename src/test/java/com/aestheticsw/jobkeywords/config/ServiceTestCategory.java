package com.aestheticsw.jobkeywords.config;

import org.springframework.test.context.ContextConfiguration;

import com.aestheticsw.jobkeywords.service.termextractor.config.DatabaseConfiguration;
import com.aestheticsw.jobkeywords.service.termextractor.config.RestClientConfiguration;
import com.aestheticsw.jobkeywords.service.termextractor.config.ServiceConfiguration;

@ContextConfiguration(classes = { ServiceConfiguration.class, RestClientConfiguration.class, DatabaseConfiguration.class })
public class ServiceTestCategory extends SpringContextTestCategory {

}
