package com.aestheticsw.jobkeywords.service.termextractor.indeed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.aestheticsw.jobkeywords.config.TestBase;
import com.aestheticsw.jobkeywords.domain.termfrequency.SearchParameters;

//TODO rename to *IT
public class IndeedClientTest extends TestBase {

    @Autowired
    private IndeedClient service;

    @Test
    public void loadContext() {
        assertNotNull(service);
    }

    @Test
    public void getIndeedJobList() {
        SearchParameters params = new SearchParameters("java spring", 5, 0, Locale.US, null, 0, null);
        JobListResponse jobListResponse = service.getIndeedJobList(params);
        assertNotNull(jobListResponse);
        assertTrue(jobListResponse.hasResults());
        assertEquals(5, jobListResponse.getResults().getResults().size());
    }
}
