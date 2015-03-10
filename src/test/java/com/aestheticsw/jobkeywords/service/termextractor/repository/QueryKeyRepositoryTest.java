package com.aestheticsw.jobkeywords.service.termextractor.repository;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aestheticsw.jobkeywords.config.DatabaseTestCategory;
import com.aestheticsw.jobkeywords.config.DatabaseTestConfiguration;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;

// TODO add DbUnitTestExecutionListener.class
//@DatabaseSetup(...dataset...)
//@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { ...dataaset... })
//@DirtiesContext

@RunWith(SpringJUnit4ClassRunner.class)
@DatabaseTestConfiguration
@Category(DatabaseTestCategory.class)
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
