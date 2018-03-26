/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.service.termextractor.impl.indeed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aestheticsw.jobkeywords.config.ServiceTestBehavior;
import com.aestheticsw.jobkeywords.service.termextractor.domain.JobSummary;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;

@RunWith(SpringJUnit4ClassRunner.class)
@ServiceTestBehavior
@IntegrationTest
public class IndeedClientTest {

    @Autowired
    private IndeedClient indeedClient;

    @Test
    public void loadContext() {
        assertNotNull(indeedClient);
    }

    @Test
    public void getIndeedJobSummaryList() {
        QueryKey key = new QueryKey("java spring", Locale.US, null);
        SearchParameters params = new SearchParameters(key, 5, 0, 0, null);
        List<JobSummary> jobSummaryList = indeedClient.getIndeedJobSummaryList(params);
        assertNotNull(jobSummaryList);
        assertEquals(5, jobSummaryList.size());
    }

    @Test
    public void getIndeedJobDetails() {
        QueryKey key = new QueryKey("java spring", Locale.US, null);
        SearchParameters params = new SearchParameters(key, 10, 0, 0, null);
        List<JobSummary> jobSummaryList = indeedClient.getIndeedJobSummaryList(params);
        assertNotNull(jobSummaryList);
//        assertEquals(1, jobSummaryList.size());
        JobSummary jobSummary = jobSummaryList.get(2);
        String jobDetailsUrl = jobSummary.getUrl();
        assertNotNull(jobDetailsUrl);

        String details = indeedClient.getIndeedJobDetails(jobDetailsUrl);
        assertNotNull(details);
        assertTrue(StringUtils.isNotEmpty(details));
    }
}
