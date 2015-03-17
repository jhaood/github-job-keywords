package com.aestheticsw.jobkeywords.liquibase;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import liquibase.Liquibase;
import liquibase.configuration.ConfigurationProperty;
import liquibase.configuration.GlobalConfiguration;
import liquibase.configuration.LiquibaseConfiguration;
import liquibase.database.Database;
import liquibase.diff.DiffGeneratorFactory;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.logging.LogFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.serializer.core.xml.XMLChangeLogSerializer;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Liquibase support for the job-keywords mysql database.
 * 
 * <pre>
 * Liquibase runs either from maven or from LiquibaseUpdateRunner as a unit-test. 
 * Dev builds should be fast - hbm2ddl recreates the DEV database every time
 * Release preparation requires LiquibaseUpdateRunner to generate differences, then upgrade live DB.    
 * Prod maven build runs hbm2ddl validation against the live DB - this will fail unless liquibase updates the schema
 * Prod runtime runs hbm2ddl validate against the live DB. But no liquibase. 
 * Prod runtime JVM parameter required to run liquibase and upgrade live db
 * 
 * Dev Mode: 
 *      Always use hbm2ddl to generate DB. Run tests that leave seed-data behind.  
 * 
 * The live mysql database - defined inside localhost (localhost is the "job_db_instance") 
 *      Prod: job_db_prod
 *      Dev: job_db_dev
 *      
 * Liquibase configuration:
 *      Liquibase-job-db
 *      Liquobase-hbm2ddl-db
 * 
 * HBM2DDL always validates live DB: 
 *      Dev: job_db_dev
 *      Prod: job_db_prod
 *      
 * HBM2DDL-CREATE: never create or update PROD live DB
 *      Dev: job_db_dev or hbm2ddl_db
 *      Prod: hbm2ddl_db
 * 
 * liquibase updates applied to job_db_dev or job_db_prod
 * 
 * Liquibase diff's between: 
 *      job_db_dev & hbm2ddl_db 
 * 
 * Test liquibase updates: 
 *      1) copy job_db_prod to job_db_dev
 *      2) apply liquibase to job_db_dev
 *      3) diff against HBM2DDL schema (hbm2ddl_db)
 *      4) run all tests against copy-db (job_db_dev)
 * 
 * </pre>
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
public class LiquibaseActuator extends SpringLiquibase {

    @Autowired
    private DataSource hbm2ddlDataSource;

    public void forceUpdate() throws LiquibaseException {
        ConfigurationProperty shouldRunProperty =
            LiquibaseConfiguration.getInstance().getProperty(GlobalConfiguration.class, GlobalConfiguration.SHOULD_RUN);

        if (!shouldRunProperty.getValue(Boolean.class)) {
            // don't override the global configuraiton... 
            // LiquibaseConfiguration.getInstance().getConfiguration(GlobalConfiguration.class).setValue(GlobalConfiguration.SHOULD_RUN, true);
            log.warning("Can't override GlobalConfiguraiton when forcing liquibase to run");
            return;
        }
        if (!shouldRun) {
            LogFactory.getLogger().info(
                "Liquibase forcing 'shouldRun' " + "property on Spring Liquibase Bean: " + getBeanName());
            shouldRun = true;
            return;
        }
        super.afterPropertiesSet();
    }

    public void recordSchemaDifferencesBetweenJavaAndLiquibase() throws SQLException, LiquibaseException, IOException,
            ParserConfigurationException {
        Database referenceConnection = createDatabase(dataSource.getConnection());
        Database comparisonConnection = createDatabase(hbm2ddlDataSource.getConnection());

        new Liquibase("liquibase/changelog-master.xml", new ClassLoaderResourceAccessor(), referenceConnection)
            .update("base");

        DiffResult result =
            DiffGeneratorFactory.getInstance().compare(comparisonConnection, referenceConnection, new CompareControl());

        DiffToChangeLog changeLog = new DiffToChangeLog(result, new DiffOutputControl(false, false, false));

        changeLog.print(new File("target/schemaDifferences.xml").getAbsolutePath(), new XMLChangeLogSerializer());

        changeLog.print(System.out);
    }
}
