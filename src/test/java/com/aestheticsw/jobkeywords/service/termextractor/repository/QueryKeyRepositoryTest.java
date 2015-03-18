package com.aestheticsw.jobkeywords.service.termextractor.repository;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.aestheticsw.jobkeywords.config.DatabaseTestBehavior;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.github.springtestdbunit.DbUnitTestExecutionListener;

// @DatabaseSetup(...dataset...)
// @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { ...dataaset... })
// @DirtiesContext

@RunWith(SpringJUnit4ClassRunner.class)
@DatabaseTestBehavior
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class }, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public class QueryKeyRepositoryTest {

    @Autowired
    QueryKeyRepository queryKeyRepository;

    @Autowired
    DataSource dataSource;

    @After
    @Transactional
    public void dumpDatabase() throws SQLException, DatabaseUnitException, FileNotFoundException, IOException {
        // full database export
        Connection jdbcConnection = dataSource.getConnection();
        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
        IDataSet fullDataSet = connection.createDataSet();
        FlatXmlDataSet.write(fullDataSet, new FileOutputStream("./target/QueryKeyRepositoryTest-dbunit.xml"));
    }

    @Test
    public void loadContext() {
        assertNotNull(queryKeyRepository);
    }

    @Test
    public void persist() {
        QueryKey dbKey;
        QueryKey key = new QueryKey("query-eight", Locale.US, "city");
        {
            QueryKey retrievedKey = queryKeyRepository.findByCompoundKey(key);
            if (retrievedKey == null) {
                dbKey = queryKeyRepository.save(key);
                assertNotNull(dbKey);
                assertNotNull(dbKey.getId());
            } else {
                dbKey = retrievedKey;
            }
        }

        {
            QueryKey retrievedKey = queryKeyRepository.findOne(dbKey.getId());
            assertNotNull(retrievedKey);
            assertThat(
                retrievedKey,
                is(allOf(hasProperty("query", is(key.getQuery())), hasProperty("locale", is(key.getLocale())),
                    hasProperty("city", is(key.getCity())))));
        }
        {
            QueryKey retrievedKey = queryKeyRepository.findByCompoundKey(key);
            assertNotNull(retrievedKey);
            assertThat(
                retrievedKey,
                is(allOf(hasProperty("query", is(key.getQuery())), hasProperty("locale", is(key.getLocale())),
                    hasProperty("city", is(key.getCity())))));
        }
    }

    @Test
    public void findByCompoundKey() {
        QueryKey dbKey;
        QueryKey key = new QueryKey("query-nine", Locale.US, "city");
        {
            QueryKey retrievedKey = queryKeyRepository.findByCompoundKey(key);
            if (retrievedKey == null) {
                dbKey = queryKeyRepository.save(key);
                assertNotNull(dbKey);
                assertNotNull(dbKey.getId());
            } else {
                dbKey = retrievedKey;
            }
        }

        {
            QueryKey retrievedKey = queryKeyRepository.findOne(dbKey.getId());
            assertNotNull(retrievedKey);
            assertThat(
                retrievedKey,
                is(allOf(hasProperty("query", is(key.getQuery())), hasProperty("locale", is(key.getLocale())),
                    hasProperty("city", is(key.getCity())))));
        }
        {
            QueryKey retrievedKey = queryKeyRepository.findByCompoundKey(key);
            assertNotNull(retrievedKey);
            assertThat(
                retrievedKey,
                is(allOf(hasProperty("query", is(key.getQuery())), hasProperty("locale", is(key.getLocale())),
                    hasProperty("city", is(key.getCity())))));
        }
    }

}
