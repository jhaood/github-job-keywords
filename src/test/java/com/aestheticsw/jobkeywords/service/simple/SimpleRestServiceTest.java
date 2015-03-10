package com.aestheticsw.jobkeywords.service.simple;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aestheticsw.jobkeywords.config.ServiceTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ServiceTestConfiguration
public class SimpleRestServiceTest  {

    @Autowired
    private SimpleRestService simpleRestService;

    @Test
    public void loadContext() {
        assertNotNull(simpleRestService);
    }

    @Test
    public void getPage() {
        assertNotNull(simpleRestService.getPage());
    }

}
