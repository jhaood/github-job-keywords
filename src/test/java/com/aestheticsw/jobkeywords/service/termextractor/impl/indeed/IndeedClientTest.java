package com.aestheticsw.jobkeywords.service.termextractor.impl.indeed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.aestheticsw.jobkeywords.config.TestBase;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;

//TODO rename to *IT
public class IndeedClientTest extends TestBase {

    @Autowired
    private IndeedClient indeedClient;

    @Test
    public void loadContext() {
        assertNotNull(indeedClient);
    }

    @Test
    public void getIndeedJobList() {
        QueryKey key = new QueryKey("java spring", Locale.US, null);
        SearchParameters params = new SearchParameters(key, 5, 0, 0, null);
        JobListResponse jobListResponse = indeedClient.getIndeedJobList(params);
        assertNotNull(jobListResponse);
        assertTrue(jobListResponse.hasResults());
        assertEquals(5, jobListResponse.getResults().getResults().size());
    }
}
