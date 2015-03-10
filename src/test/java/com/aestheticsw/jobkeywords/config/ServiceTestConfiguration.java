package com.aestheticsw.jobkeywords.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.aestheticsw.jobkeywords.service.termextractor.config.DatabaseConfiguration;
import com.aestheticsw.jobkeywords.service.termextractor.config.RestClientConfiguration;
import com.aestheticsw.jobkeywords.service.termextractor.config.ServiceConfiguration;
import com.aestheticsw.jobkeywords.shared.config.LogInjectorConfiguration;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration(classes = { ServiceConfiguration.class, RestClientConfiguration.class,
    DatabaseConfiguration.class, LogInjectorConfiguration.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
public @interface ServiceTestConfiguration {

}
