package com.aestheticsw.jobkeywords.service.termextractor.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

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
@TransactionConfiguration(defaultRollback = false)
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
    @Transactional
    public void saveAndRetrieve() {
        TermFrequency tf1 = new TermFrequency(new String[] { "java", "3", "1" });
        TermFrequency tf2 = new TermFrequency(new String[] { "spring", "2", "1" });
        List<TermFrequency> list1 = new ArrayList<>();
        list1.add(tf1);
        list1.add(tf2);

        TransactionStatus status = transactionManager.getTransaction(null);

        // QueryKey dbKey1;
        QueryKey key = new QueryKey("query-one", Locale.US, "");
        SearchParameters param1 = new SearchParameters(key, 1, 1, 0, "");

        {
            TermFrequencyResults tfr1 = new TermFrequencyResults(param1.getQueryKey());
            tfr1.accumulateTermFrequencyList(param1, list1);

            termFrequencyResultsRepository.save(tfr1);
        }

        transactionManager.commit(status);
        status = transactionManager.getTransaction(null);

        {
            TermFrequencyResults dbTfr1 = termFrequencyResultsRepository.findByQueryKey(param1.getQueryKey());
            assertNotNull(dbTfr1);

            assertNotNull(dbTfr1.getQueryKey());
            List<SearchParameters> dbSearchParameters = dbTfr1.getSearchParametersList();
            assertNotNull(dbSearchParameters);
            assertEquals(1, dbSearchParameters.size());

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
            QueryKey dbKey = queryKeyRepository.findByCompoundKey(key);
            SearchParameters param1_2 = new SearchParameters(dbKey, 1, 2, 0, "");

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
    @Transactional
    public void distinctQueryKeys() {
        QueryKey key1 = new QueryKey("query-two", Locale.US, "");
        SearchParameters param1 = new SearchParameters(key1, 1, 1, 0, "");

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
            SearchParameters param2 = new SearchParameters(key2, 1, 1, 0, "");

            TermFrequencyResults tfr = new TermFrequencyResults(param2.getQueryKey());
            tfr.accumulateTermFrequencyList(param2, list1);
            termFrequencyResultsRepository.save(tfr);
        }
    }

    // expected exception handling doesn't work correctly if @Transaction method will commit. BUG in Spring. 
    @Test(expected = DataIntegrityViolationException.class)
    // This is required to avoid a RollbackException 
    @Rollback(true)
    @Transactional
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
