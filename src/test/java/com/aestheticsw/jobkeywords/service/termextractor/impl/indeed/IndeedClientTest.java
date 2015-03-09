package com.aestheticsw.jobkeywords.service.termextractor.impl.indeed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import com.aestheticsw.jobkeywords.config.ServiceTestCategory;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;

@Category(ServiceTestCategory.class)
public class IndeedClientTest extends ServiceTestCategory {

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
