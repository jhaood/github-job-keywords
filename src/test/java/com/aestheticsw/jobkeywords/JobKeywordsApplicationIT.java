package com.aestheticsw.jobkeywords;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.aestheticsw.jobkeywords.config.ServiceTestBehavior;

// TODO FIXME Run the *IT tests from an integration phase of surefire or failsafe
// TODO this can't be run with the *Test classes because this baby loads the full spring-boot
// application configuration and the spring-runner runs all the tests within a single app-context.
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JobKeywordsApplication.class)
// TODO figure out @IntegrationTest... Only difference should be IntegrationTestPropertiesListener  
// @IntegrationTest
@WebAppConfiguration
@ServiceTestBehavior
public class JobKeywordsApplicationIT {

    @Test
    public void contextLoads() {
    }

}
