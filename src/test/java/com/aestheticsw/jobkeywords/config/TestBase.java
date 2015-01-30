package com.aestheticsw.jobkeywords.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.aestheticsw.jobkeywords.JobKeywordsApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JobKeywordsApplication.class)
@WebAppConfiguration
public class TestBase {
    @Test
    public void noopTestIgnoreThis() {
    }
}
