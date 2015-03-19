/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.liquibase;

import liquibase.exception.LiquibaseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@LiquibaseTestBehavior
public class LiquibaseUpdateRunner {
    
    @Autowired
    private LiquibaseActuator liquibaseActuator;
    
    @Test
    public void updateSchema() throws LiquibaseException {
        liquibaseActuator.setDropFirst(false);
        liquibaseActuator.forceUpdate();
    }

}
