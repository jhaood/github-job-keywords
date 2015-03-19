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
 * This class supports schema evolution and validation using Liquibase.
 * <p/>
 * Liquibase may run from several contexts:
 * <ul>
 * <li>from the maven plugin</li>
 * <li>as part of the web container runtime</li>
 * <li>as a pseudo unit-test. This was built first and makes it easy to launch Liquibase by itself
 * from the maven command line.</li>
 * </ul>
 * <p/>
 * Maven runs the Liquibase plugin only to validate the live schema (MySQL: "job_db").
 * <p/>
 * The maven pom.xml configures several profiles which control how Liquibase behaves when launched
 * as a pseudo-test and when the container runtime is launched. The default maven profile ("dev-h2")
 * doesn't run liquibase and creates an H2 embedded database which is configured by hbm2ddl. The
 * "dev" profile attaches to the MySQL database and triggers HBM2DDL to validate the schema. The
 * "prod-liquibase" profile runs Liquibase from the Spring Boot launcher and turns off HBM2DDL.
 * <p/>
 * See the maven POM for details about the build profiles.
 * <p/>
 * The production AWS linux machines do not support Maven or any of the build tools. The
 * web-container runtime will lauch Liquibase to validate the current schema. The schema can be
 * updated from the command line: "-Djobkeywords.liquibase.update=true"
 * <p/>
 * The web-container runtime does not allow differences to be generated. The PROD-DB must be copied
 * to a developer machine where maven and the pseudo-tests can generate the differences.
 * 
 * <pre>
 * Development builds against a local DEV database: 
 *      - Dev builds are launched with differnt maven profiles to configure hbm2ddl or use Liquibase 
 *              to update the MySQL database.   
 *      - Some tests leave test-data in the MySQL database because a few tests commit their data 
 *              instead of rolling back.  
 * 
 * The live schema ("job_db") is defined in the localhost MySQL database instance 
 * (The MySQL server running on "localhost" the "job_db_instance")
 * 
 * The configuration for the Liquibase pseudo-tests use 3 database schemas:
 *      - job_db
 *      - hbm2ddl_db
 *      - liquobase_db
 * 
 * HBM2DDL-VALIDATE: always validate the MySQL DB: job_db 
 * HBM2DDL-CREATE: never run "create" "update" against a PRODUCTION database. 
 * 
 * Liquibase diff's between 2 fresh databases. These are always created from scratch:  
 *      liquibase_db & hbm2ddl_db
 * 
 * </pre>
 * 
 * TODO define 2 database schema names: Prod: job_db_prod, Dev: job_db_dev
 * 
 * @see com.aestheticsw.jobkeywords.liquibase.LiquibaseDiffRunner 
 * @see com.aestheticsw.jobkeywords.liquibase.LiquibaseUpdateRunner 
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
