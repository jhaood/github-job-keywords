package com.aestheticsw.jobkeywords.service.termextractor.repository;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.aestheticsw.jobkeywords.service.termextractor.config.DatabaseConfiguration;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;

//@DatabaseSetup(...dataset...)
//@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { ...dataaset... })
//@DirtiesContext

// TODO add DbUnitTestExecutionListener.class
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DatabaseConfiguration.class })
public class QueryKeyRepositoryTest {

    @Autowired
    QueryKeyRepository queryKeyRepository;

    @Test
    public void loadContext() {
        assertNotNull(queryKeyRepository);
    }

    @Test
    public void persist() {
        QueryKey key = new QueryKey("query-eights", Locale.US, "city");
        QueryKey dbKey = queryKeyRepository.save(key);
        assertNotNull(dbKey);
        QueryKey retrievedKey = queryKeyRepository.findOne(dbKey.getId());
        assertNotNull(retrievedKey);
        retrievedKey = queryKeyRepository.findByCompoundKey(key);
        assertNotNull(retrievedKey);
    }
}
