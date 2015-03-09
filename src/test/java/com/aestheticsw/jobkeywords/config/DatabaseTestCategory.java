package com.aestheticsw.jobkeywords.config;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.aestheticsw.jobkeywords.service.termextractor.config.DatabaseConfiguration;

@ContextConfiguration(classes = { DatabaseConfiguration.class })
@TestExecutionListeners({ TransactionalTestExecutionListener.class })
public class DatabaseTestCategory extends SpringContextTestCategory {

}
