package com.aestheticsw.jobkeywords.service.database;

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

import com.aestheticsw.jobkeywords.config.DatabaseConfiguration;
import com.aestheticsw.jobkeywords.domain.termfrequency.QueryKey;

//TODO add DbUnitTestExecutionListener.class 
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, 
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class})
// @SpringApplicationConfiguration(classes = JobKeywordsApplication.class)
// @DatabaseSetup(ItemRepositoryIT.DATASET)
// @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { ItemRepositoryIT.DATASET })
// @DirtiesContext

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DatabaseConfiguration.class })
public class QueryKeyRepositoryTest {

    @Autowired
    QueryKeyRepository queryKeyRepository;

    @Test
    public void loadContext() {
    }

    @Test 
    public void persist() {
        QueryKey key = new QueryKey("query", Locale.US, "city");
        QueryKey dbKey = queryKeyRepository.save(key);
        assertNotNull(dbKey);
        QueryKey retrievedKey = queryKeyRepository.findOne(dbKey.getId());
        assertNotNull(retrievedKey);
        retrievedKey = queryKeyRepository.findByCompoundKey(key);
        assertNotNull(retrievedKey);
    }
}
