package com.aestheticsw.jobkeywords.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.aestheticsw.jobkeywords.service.termextractor.config.RestClientConfiguration;
import com.aestheticsw.jobkeywords.shared.config.LogInjectorConfiguration;

//TODO rename to ITBase

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RestClientConfiguration.class, LogInjectorConfiguration.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
public class TestBase {
    @Test
    public void noopTestIgnoreThis() {
    }
}
