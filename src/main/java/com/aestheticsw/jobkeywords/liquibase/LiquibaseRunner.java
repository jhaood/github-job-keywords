package com.aestheticsw.jobkeywords.liquibase;

import javax.annotation.PostConstruct;

import liquibase.exception.LiquibaseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnClass({ LiquibaseActuator.class })
public class LiquibaseRunner {
    @Autowired
    private LiquibaseActuator liquibase;
    
    @Value("${jobkeywords.liquibase.update}")
    private Boolean runLiquibaseUpdate;

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
