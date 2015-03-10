package com.aestheticsw.jobkeywords.service.termextractor.impl.indeed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aestheticsw.jobkeywords.config.ServiceTestCategory;
import com.aestheticsw.jobkeywords.config.ServiceTestConfiguration;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;

@RunWith(SpringJUnit4ClassRunner.class)
@ServiceTestConfiguration
@Category(ServiceTestCategory.class)
public class IndeedClientTest {

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
