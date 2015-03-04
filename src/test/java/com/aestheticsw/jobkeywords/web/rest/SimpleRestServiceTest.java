package com.aestheticsw.jobkeywords.web.rest;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.aestheticsw.jobkeywords.config.TestBase;
import com.aestheticsw.jobkeywords.service.simple.SimpleRestService;

/*
 * Use TestBase instead of annotating every class.
 * @RunWith(SpringJUnit4ClassRunner.class)
 * @SpringApplicationConfiguration(classes = JobKeywordsApplication.class)
 * @WebAppConfiguration
 */
//TODO rename to *IT
public class SimpleRestServiceTest extends TestBase {

    @Autowired
    private SimpleRestService simpleRestService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void getPage() {
        assertNotNull(simpleRestService.getPage());
    }

}
