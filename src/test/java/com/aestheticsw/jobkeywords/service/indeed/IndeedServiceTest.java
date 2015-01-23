package com.aestheticsw.jobkeywords.service.indeed;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.aestheticsw.jobkeywords.config.TestBase;

public class IndeedServiceTest extends TestBase {

    @Autowired
    private IndeedService service;

    @Test
    public void loadContext() {
        assertNotNull(service);
    }
}
