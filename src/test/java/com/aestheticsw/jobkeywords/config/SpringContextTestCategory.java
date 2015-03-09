package com.aestheticsw.jobkeywords.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.aestheticsw.jobkeywords.shared.config.LogInjectorConfiguration;

// package-access because it can't be used as a test-category or base class
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { LogInjectorConfiguration.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
class SpringContextTestCategory {
    @Test
    public void noopTestIgnoreThis() {
    }
}
