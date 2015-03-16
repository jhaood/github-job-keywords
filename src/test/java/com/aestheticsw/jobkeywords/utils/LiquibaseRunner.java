package com.aestheticsw.jobkeywords.utils;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;

import liquibase.exception.LiquibaseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aestheticsw.jobkeywords.config.LiquibaseTestBehavior;

@RunWith(SpringJUnit4ClassRunner.class)
@LiquibaseTestBehavior
public class LiquibaseRunner {
//    @Autowired
//    private QueryKeyRepository queryKeyRepository;
    
    @Autowired
    private LiquibaseActuator liquibaseActuator;
    
    // DON"T ENABLE YET
    @Test
    public void updateSchema() throws LiquibaseException {
        liquibaseActuator.setDropFirst(false);
        liquibaseActuator.setShouldRun(true);
        liquibaseActuator.afterPropertiesSet();
    }

    // @Test
    public void recordSchemaDifferencesBetweenJavaAndLiquibase() throws LiquibaseException, SQLException, IOException, ParserConfigurationException {
        liquibaseActuator.recordSchemaDifferencesBetweenJavaAndLiquibase();
    }
    
    /*
    @Test
    @Ignore
    public void runLiquibaseChangesets() throws LiquibaseException, SQLException {
        liquibaseUpdate(jpaRule, "");
    }

    @Test
    public void validateChangeSet() throws LiquibaseException, SQLException {

        liquibaseUpdate(jpaRule, "seed");

        // TODO FIXME add assertions 
    }

    public static void liquibaseUpdate(final JpaRule jpaRule,
                                       final String contexts)
            throws LiquibaseException, SQLException {
        liquibaseUpdate(jpaRule, contexts, false);
    }

    public static void liquibaseUpdate(final JpaRule jpaRule,
                                       final String contexts,
                                       final boolean dropCreate)
            throws LiquibaseException, SQLException {
        final Liquibase liquibaseActuator = new Liquibase("changelog/liquibaseActuator-master.xml",
                new ClassLoaderResourceAccessor(),
                new JdbcConnection(getConnection(jpaRule.getEntityManager())));
        if (dropCreate) {
            liquibaseActuator.dropAll();
        }
        liquibaseActuator.update(contexts);
    }
    */
}
