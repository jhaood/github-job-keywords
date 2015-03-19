package com.aestheticsw.jobkeywords;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.aestheticsw.jobkeywords.config.ServiceTestBehavior;

/**
 * This is the beginning of an integration test that loads the entire application runtime context
 * because of the @SpringApplication annotation. The class name (*IT.java) isn't quite appropriate
 * here but this test must NOT be run as part of the surefire integration tests because those tests
 * load a single spring app-context for every test in the suite.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
// TODO FIXME Run the *IT tests from an integration phase of surefire or failsafe
// TODO this can't be run with the *Test classes because this baby loads the full spring-boot
// application configuration and the spring-runner runs all the tests within a single app-context.

//TODO figure out @IntegrationTest... Only difference should be IntegrationTestPropertiesListener  
//@IntegrationTest

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JobKeywordsApplication.class)
@WebAppConfiguration
@ServiceTestBehavior
public class JobKeywordsApplicationIT {

    @Test
    public void contextLoads() {
    }

}
