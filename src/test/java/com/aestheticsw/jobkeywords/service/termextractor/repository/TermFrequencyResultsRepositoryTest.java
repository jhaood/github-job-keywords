/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.service.termextractor.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import com.aestheticsw.jobkeywords.config.DatabaseTestBehavior;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequency;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyResults;

@RunWith(SpringJUnit4ClassRunner.class)
@DatabaseTestBehavior
@TransactionConfiguration(defaultRollback = false, transactionManager = "transactionManager")
public class TermFrequencyResultsRepositoryTest {

    @Autowired
    private TermFrequencyResultsRepository termFrequencyResultsRepository;

    @Autowired
    private SearchParametersRepository searchParametersRepository;

    @Autowired
    private QueryKeyRepository queryKeyRepository;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Test
    public void loadContext() {
        assertNotNull(termFrequencyResultsRepository);
        assertNotNull(searchParametersRepository);
        assertNotNull(transactionManager);
    }

    @Test
    public void saveAndRetrieve() {
        TermFrequency tf1 = new TermFrequency(new String[] { "java", "3", "1" });
        TermFrequency tf2 = new TermFrequency(new String[] { "spring", "2", "1" });
        List<TermFrequency> list1 = new ArrayList<>();
        list1.add(tf1);
        list1.add(tf2);

        TransactionStatus status = transactionManager.getTransaction(null);

        QueryKey key1 = new QueryKey("query-one", Locale.US, "");
        {
            QueryKey dbKey = queryKeyRepository.findByCompoundKey(key1);
            if (dbKey != null) {
                key1 = dbKey;
            }
        }
        SearchParameters param1 = new SearchParameters(key1, 1, 1, 0, "");
        {
            SearchParameters dbParam = searchParametersRepository.findByCompoundKey(param1);
            if (dbParam != null) {
                param1 = dbParam;
            }
        }
        {
            TermFrequencyResults tfr;
            if (key1.getId() != null) {
                TermFrequencyResults dbTfr;
                dbTfr = termFrequencyResultsRepository.findByQueryKey(key1);
                if (dbTfr != null) {
                    termFrequencyResultsRepository.delete(dbTfr);
                }
            }
        }
        transactionManager.commit(status);
        status = transactionManager.getTransaction(null);
        {
            {
                QueryKey dbKey = queryKeyRepository.findByCompoundKey(key1);
                if (dbKey != null) {
                    key1 = dbKey;
                }
            }
            {
                SearchParameters dbParam = searchParametersRepository.findByCompoundKey(param1);
                if (dbParam != null) {
                    param1 = dbParam;
                }
            }

            TermFrequencyResults tfr;
            tfr = new TermFrequencyResults(key1);
            tfr.accumulateTermFrequencyList(param1, list1);

            termFrequencyResultsRepository.save(tfr);
        }

        transactionManager.commit(status);
        status = transactionManager.getTransaction(null);

        {
            TermFrequencyResults dbTfr1 = termFrequencyResultsRepository.findByQueryKey(param1.getQueryKey());
            assertNotNull(dbTfr1);

            assertNotNull(dbTfr1.getQueryKey());
            List<SearchParameters> dbSearchParameters = dbTfr1.getSearchParametersList();
            assertNotNull(dbSearchParameters);
            // Can't assert the size because this test-class doesn't roll back - MySQL may already have 2 SearchParams. 
            // assertEquals(1, dbSearchParameters.size());
            assertTrue(dbSearchParameters.contains(param1));

            List<TermFrequency> dbTerms = dbTfr1.getSortedTermFrequencyList();
            assertNotNull(dbTerms);
            assertEquals(2, dbTerms.size());
            assertEquals("java", dbTerms.get(0).getTerm());
            assertEquals(3, dbTerms.get(0).getFrequency());
            assertEquals("spring", dbTerms.get(1).getTerm());
            assertEquals(2, dbTerms.get(1).getFrequency());
        }

        transactionManager.commit(status);
        status = transactionManager.getTransaction(null);

        {
            QueryKey dbKey = queryKeyRepository.findByCompoundKey(key1);
            SearchParameters param1_2 = new SearchParameters(dbKey, 1, 2, 0, "");
            {
                SearchParameters dbParam = searchParametersRepository.findByCompoundKey(param1_2);
                if (dbParam != null) {
                    param1_2 = dbParam;
                }
            }

            TermFrequencyResults dbTfr1 = termFrequencyResultsRepository.findByQueryKey(dbKey);

            dbTfr1.accumulateTermFrequencyList(param1_2, list1);

            termFrequencyResultsRepository.save(dbTfr1);
        }

        transactionManager.commit(status);
        status = transactionManager.getTransaction(null);

        {
            TermFrequencyResults dbTfr2 = termFrequencyResultsRepository.findByQueryKey(param1.getQueryKey());
            assertNotNull(dbTfr2);

            assertNotNull(dbTfr2.getQueryKey());
            List<SearchParameters> dbSearchParameters = dbTfr2.getSearchParametersList();
            assertNotNull(dbSearchParameters);
            assertEquals(2, dbSearchParameters.size());

            List<TermFrequency> dbTerms = dbTfr2.getSortedTermFrequencyList();
            assertNotNull(dbTerms);
            assertEquals(2, dbTerms.size());
            assertEquals("java", dbTerms.get(0).getTerm());
            assertEquals(6, dbTerms.get(0).getFrequency());
            assertEquals("spring", dbTerms.get(1).getTerm());
            assertEquals(4, dbTerms.get(1).getFrequency());
        }
    }

    @Test
    public void distinctQueryKeys() {

        TransactionStatus status = transactionManager.getTransaction(null);

        TermFrequency tf1 = new TermFrequency(new String[] { "java", "3", "1" });
        TermFrequency tf2 = new TermFrequency(new String[] { "spring", "2", "1" });
        List<TermFrequency> list1 = new ArrayList<>();
        list1.add(tf1);
        list1.add(tf2);

        QueryKey key1 = new QueryKey("query-two", Locale.US, "");
        {
            QueryKey dbKey = queryKeyRepository.findByCompoundKey(key1);
            if (dbKey != null) {
                key1 = dbKey;
            }
        }
        SearchParameters param1 = new SearchParameters(key1, 1, 1, 0, "");
        {
            SearchParameters dbParam = searchParametersRepository.findByCompoundKey(param1);
            if (dbParam != null) {
                param1 = dbParam;
            }
        }
        {
            TermFrequencyResults tfr;
            if (key1.getId() != null) {
                TermFrequencyResults dbTfr;
                dbTfr = termFrequencyResultsRepository.findByQueryKey(key1);
                if (dbTfr != null) {
                    termFrequencyResultsRepository.delete(dbTfr);
                }
            }
        }
        transactionManager.commit(status);
        status = transactionManager.getTransaction(null);

        {
            {
                QueryKey dbKey = queryKeyRepository.findByCompoundKey(key1);
                if (dbKey != null) {
                    key1 = dbKey;
                }
            }
            {
                SearchParameters dbParam = searchParametersRepository.findByCompoundKey(param1);
                if (dbParam != null) {
                    param1 = dbParam;
                }
            }

            TermFrequencyResults tfr;
            if (key1.getId() != null) {
                tfr = termFrequencyResultsRepository.findByQueryKey(key1);
                if (tfr == null) {
                    tfr = new TermFrequencyResults(key1);
                }
            } else {
                tfr = new TermFrequencyResults(key1);
            }
            tfr.accumulateTermFrequencyList(param1, list1);
            termFrequencyResultsRepository.save(tfr);
        }
        transactionManager.commit(status);
        status = transactionManager.getTransaction(null);
        {
            QueryKey dbKey1 = queryKeyRepository.findByCompoundKey(key1);
            SearchParameters param1_2 = new SearchParameters(dbKey1, 1, 2, 0, "");

            TermFrequencyResults tfr = termFrequencyResultsRepository.findByQueryKey(dbKey1);
            assertNotNull(tfr);
            tfr.accumulateTermFrequencyList(param1_2, list1);
            termFrequencyResultsRepository.save(tfr);
        }
        transactionManager.commit(status);
        status = transactionManager.getTransaction(null);
        {
            QueryKey key2 = new QueryKey("query-three", Locale.US, "");
            {
                QueryKey dbKey = queryKeyRepository.findByCompoundKey(key2);
                if (dbKey != null) {
                    key2 = dbKey;
                }
            }
            SearchParameters param2 = new SearchParameters(key2, 1, 1, 0, "");
            {
                SearchParameters dbParam = searchParametersRepository.findByCompoundKey(param2);
                if (dbParam != null) {
                    param2 = dbParam;
                }
            }

            TermFrequencyResults tfr;
            if (key2.getId() != null) {
                tfr = termFrequencyResultsRepository.findByQueryKey(key2);
                if (tfr == null) {
                    tfr = new TermFrequencyResults(key2);
                }
            } else {
                tfr = new TermFrequencyResults(key2);
            }
            tfr.accumulateTermFrequencyList(param2, list1);
            termFrequencyResultsRepository.save(tfr);
        }
    }

    // expected exception handling doesn't work correctly if @Transaction method will commit. BUG in Spring. 
    @Test(expected = DataIntegrityViolationException.class)
    // This is required to avoid a RollbackException after the last transaction completes
    @Rollback(true)
    public void distinctQueryKeyException() {
        QueryKey key1 = new QueryKey("query-four", Locale.US, "");
        SearchParameters param1 = new SearchParameters(key1, 1, 1, 0, "");

        SearchParameters param1_2 = new SearchParameters(key1, 1, 2, 0, "");

        QueryKey key3 = new QueryKey("query-five", Locale.US, "");
        SearchParameters param2 = new SearchParameters(key3, 1, 1, 0, "");

        TermFrequency tf1 = new TermFrequency(new String[] { "java", "3", "1" });
        TermFrequency tf2 = new TermFrequency(new String[] { "spring", "2", "1" });
        List<TermFrequency> list1 = new ArrayList<>();
        list1.add(tf1);
        list1.add(tf2);

        TransactionStatus status = transactionManager.getTransaction(null);
        {
            TermFrequencyResults tfr = new TermFrequencyResults(key1);
            tfr.accumulateTermFrequencyList(param1, list1);
            termFrequencyResultsRepository.save(tfr);
        }
        transactionManager.commit(status);
        status = transactionManager.getTransaction(null);
        {
            TermFrequencyResults tfr = new TermFrequencyResults(key1);
            tfr.accumulateTermFrequencyList(param1_2, list1);
            termFrequencyResultsRepository.save(tfr);
            fail("Expected constraint violation exception on the term_frequency_results.query_key_id foreign-key constraint");
        }
    }
}
