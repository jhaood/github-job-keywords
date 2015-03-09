package com.aestheticsw.jobkeywords.service.simple;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import com.aestheticsw.jobkeywords.config.ServiceTestCategory;

// @ComponentScan(basePackages = { "com.aestheticsw.jobkeywords.service.simple" })
@Category(ServiceTestCategory.class)
public class SimpleRestServiceTest extends ServiceTestCategory {

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
