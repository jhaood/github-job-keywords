package com.aestheticsw.jobkeywords.client.rest;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.aestheticsw.jobkeywords.config.TestBase;

/*
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JobKeywordsApplication.class)
@WebAppConfiguration
*/
public class RestClientTest extends TestBase {

    @Autowired
    private RestClient restClient;
    
    @Test
    public void contextLoads() {
    }

    @Test
    public void getPage() {
        assertNotNull(restClient.getPage());
    }

    @Test
    public void getIndeed() {
        assertNotNull(restClient.getIndeed());
    }

}
