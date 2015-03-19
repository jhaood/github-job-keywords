package com.aestheticsw.jobkeywords.liquibase;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
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
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.serializer.core.xml.XMLChangeLogSerializer;
import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class supports schema validation and evolution using Liquibase.
 * 
 * Liquibase may run either as part of the web container runtime, or as a pseudo unit-test or from
 * maven.
 * 
 * Maven runs the Liquibase plugin only to validate the live schema, "job_db". The maven pom.xml
 * configures the profiles which control how Liquibase behaves during testing and when the container
 * runtime is launched.
 * 
 * DEV builds are fast. The default maven profile ("dev-h2") doesn't run liquibase and creates an H2
 * embedded database which is configured by hbm2ddl. See the POM for details about the profiles.
 * 
 * The production AWS linux machines do not support Maven or any of the build tools. The
 * web-container runtime will lauch Liquibase to validate the current schema. The schema can be
 * updated from the command line: "-Djobkeywords.liquibase.update=true"
 * 
 * The web-container runtime does not allow differences to be generated. The PROD-DB must be copied
 * to a developer machine where maven and the pseudo-tests can generate the differences.
 * 
 * <pre>
 * 
 * Dev Mode: 
 *      Dev builds may use hbm2ddl to generate DB - or may use Liquibase to update "job_db".  
 *      Some tests leave test-data behind.  
 * 
 * The live mysql database - defined inside localhost (localhost is the "job_db_instance")
 * 
 * TODO define 2 database schema names: Prod: job_db_prod, Dev: job_db_dev
 *      
 * The configuration for the Liquibase pseudo-tests use 3 database schemas:
 *      job_db
 *      hbm2ddl_db
 *      liquobase_db
 * 
 * HBM2DDL-VALIDATE: always validates live DB: job_db 
 * HBM2DDL-CREATE: never create or update PROD live DB
 * 
 * Liquibase diff's between 2 fresh databases. These are always created from scratch:  
 *      liquibase_db & hbm2ddl_db
 * 
 * </pre>
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
public class LiquibaseActuator extends SpringLiquibase {
    @Log
    public Logger log;

    private DataSource hbm2ddlDataSource;

    private DataSource liquibaseDataSource;

    @Autowired
    private void setHbm2ddlDataSource(DataSource hbm2ddlDataSource, DataSource liquibaseDataSource) {
        this.hbm2ddlDataSource = hbm2ddlDataSource;
        this.liquibaseDataSource = liquibaseDataSource;
    }

    public void validate() {
        Liquibase liquibase;
        try {
            liquibase = super.createLiquibase(dataSource.getConnection());
        } catch (LiquibaseException | SQLException e) {
            throw new RuntimeException("Liquibase could not be instantiated", e);
        }
        try {
            liquibase.validate();
            Writer statusWriter = new StringWriter();
            liquibase.reportStatus(true, "", statusWriter);
            log.info("\nLiquibase Status: \n\n" + statusWriter.toString());

            Writer unexpectedWriter = new StringWriter();
            liquibase.reportUnexpectedChangeSets(true, "", unexpectedWriter);
            log.info("\nLiquibase Unexpected Changesets: \n\n" + unexpectedWriter.toString());
        } catch (LiquibaseException e) {
            throw new RuntimeException("Liquibase could not validate", e);
        }
    }

    public void forceUpdate() throws LiquibaseException {
        ConfigurationProperty shouldRunProperty =
            LiquibaseConfiguration.getInstance().getProperty(GlobalConfiguration.class, GlobalConfiguration.SHOULD_RUN);

        if (!shouldRunProperty.getValue(Boolean.class)) {
            // don't override the global configuraiton... 
            // LiquibaseConfiguration.getInstance().getConfiguration(GlobalConfiguration.class).setValue(GlobalConfiguration.SHOULD_RUN, true);
            log.warn("Can't override GlobalConfiguraiton when forcing liquibase to run");
            return;
        }
        if (!shouldRun) {
            log.info("Liquibase forcing 'shouldRun' " + "property on Spring Liquibase Bean: " + getBeanName());
            shouldRun = true;
        }
        super.afterPropertiesSet();
    }

    public void recordSchemaDifferencesBetweenHbm2ddlAndLiquibase() throws SQLException, LiquibaseException,
            IOException, ParserConfigurationException {
        Database referenceConnection = createDatabase(liquibaseDataSource.getConnection());
        Database comparisonConnection = createDatabase(hbm2ddlDataSource.getConnection());

        Liquibase liquibaseObject =
            new Liquibase("liquibase/changelog-master.xml", new ClassLoaderResourceAccessor(), referenceConnection);
        liquibaseObject.dropAll();
        liquibaseObject.update("");

        DiffResult result =
            DiffGeneratorFactory.getInstance().compare(comparisonConnection, referenceConnection, new CompareControl());

        DiffToChangeLog changeLog = new DiffToChangeLog(result, new DiffOutputControl(false, false, false));

        changeLog.print(new File("target/schemaDifferences.xml").getAbsolutePath(), new XMLChangeLogSerializer());

        changeLog.print(System.out);
    }
}
