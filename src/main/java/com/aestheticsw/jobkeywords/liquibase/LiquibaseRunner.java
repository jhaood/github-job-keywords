/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.liquibase;

import javax.annotation.PostConstruct;

import liquibase.exception.LiquibaseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * This Spring Bean will run liquibase immediately after Spring completes loading the app-context.
 * <p/> 
 * The default behavior is to run "validate" but an "update" can be run if the spring property
 * "jobkeywords.liquibase.update=true" as defined in the application.properties or as set from the
 * JVM command line.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
public class LiquibaseRunner {
    private LiquibaseActuator liquibase;

    private Boolean runLiquibaseUpdate;

    @Autowired
    public LiquibaseRunner(LiquibaseActuator liquibase,
            @Value("${jobkeywords.liquibase.update}") Boolean runLiquibaseUpdate) {
        this.liquibase = liquibase;
        this.runLiquibaseUpdate = runLiquibaseUpdate;
    }

    @PostConstruct
    public void runLiquibase() {
        liquibase.validate();
        if (runLiquibaseUpdate != null && runLiquibaseUpdate) {
            try {
                liquibase.forceUpdate();
            } catch (LiquibaseException e) {
                throw new RuntimeException("Liquibase update failed", e);
            }
        }
    }

}
