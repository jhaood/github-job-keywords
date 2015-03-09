package com.aestheticsw.jobkeywords;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.aestheticsw.jobkeywords.config.ServiceTestCategory;

@Category(ServiceTestCategory.class)
@SpringApplicationConfiguration(classes = JobKeywordsApplication.class)
@WebAppConfiguration
public class JobKeywordsApplicationTest extends ServiceTestCategory {

    @Test
    public void contextLoads() {
    }

}
