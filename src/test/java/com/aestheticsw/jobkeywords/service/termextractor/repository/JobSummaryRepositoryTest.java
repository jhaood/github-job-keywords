package com.aestheticsw.jobkeywords.service.termextractor.repository;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import com.aestheticsw.jobkeywords.config.DatabaseTestCategory;

@Category(DatabaseTestCategory.class)
public class JobSummaryRepositoryTest extends DatabaseTestCategory {

    @Autowired
    JobSummaryRepository jobSummaryRepository;

    @Test
    public void loadContext() {
        assertNotNull(jobSummaryRepository);
    }

}
