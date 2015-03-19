package com.aestheticsw.jobkeywords.liquibase;

import javax.annotation.PostConstruct;

import liquibase.exception.LiquibaseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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
