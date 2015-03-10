package com.aestheticsw.jobkeywords.service.termextractor.repository;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aestheticsw.jobkeywords.config.DatabaseTestCategory;
import com.aestheticsw.jobkeywords.config.DatabaseTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@DatabaseTestConfiguration
@Category(DatabaseTestCategory.class)
public class JobSummaryRepositoryTest {

    @Autowired
    JobSummaryRepository jobSummaryRepository;

    @Test
    public void loadContext() {
        assertNotNull(jobSummaryRepository);
    }

}
