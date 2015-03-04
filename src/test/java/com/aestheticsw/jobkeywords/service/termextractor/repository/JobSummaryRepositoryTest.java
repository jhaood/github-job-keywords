package com.aestheticsw.jobkeywords.service.termextractor.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.aestheticsw.jobkeywords.config.DatabaseConfiguration;

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, 
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class})

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DatabaseConfiguration.class })
public class JobSummaryRepositoryTest {

    @Autowired
    JobSummaryRepository jobSummaryRepository;

    @Test
    public void loadContext() {
    }

}
