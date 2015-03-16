package com.aestheticsw.jobkeywords.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.aestheticsw.jobkeywords.shared.config.LogInjectorConfiguration;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
// TODO ??? HibernateJpaAutoConfiguration.class 
@ContextConfiguration(classes = { LiquibaseJobDatabaseConfiguration.class, HibernateJpaAutoConfiguration.class,
    LiquibaseHbm2ddlDatabaseConfiguration.class, LogInjectorConfiguration.class })
// @TransactionConfiguration(defaultRollback = true, transactionManager = "transactionManager")
// listeners = { DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class,
@TestExecutionListeners(listeners = { DirtiesContextTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class }, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public @interface LiquibaseTestBehavior {

}
