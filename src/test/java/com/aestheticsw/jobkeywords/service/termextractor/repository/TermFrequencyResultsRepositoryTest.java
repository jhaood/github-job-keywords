package com.aestheticsw.jobkeywords.service.termextractor.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import com.aestheticsw.jobkeywords.config.DatabaseTestCategory;
import com.aestheticsw.jobkeywords.config.DatabaseTestConfiguration;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequency;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyResults;

// TODO rename to *IT

@RunWith(SpringJUnit4ClassRunner.class)
@DatabaseTestConfiguration
@Category(DatabaseTestCategory.class)
public class TermFrequencyResultsRepositoryTest {

    @Autowired
    private TermFrequencyResultsRepository termFrequencyResultsRepository;

    @Autowired
    private SearchParametersRepository searchParametersRepository;

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
            SearchParameters param1_2 = new SearchParameters(key, 1, 2, 0, "");

            TermFrequencyResults dbTfr1 = termFrequencyResultsRepository.findByQueryKey(param1.getQueryKey());

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

        SearchParameters param1_2 = new SearchParameters(key1, 1, 2, 0, "");

        QueryKey key3 = new QueryKey("query-three", Locale.US, "");
        SearchParameters param2 = new SearchParameters(key3, 1, 1, 0, "");

        TermFrequency tf1 = new TermFrequency(new String[] { "java", "3", "1" });
        TermFrequency tf2 = new TermFrequency(new String[] { "spring", "2", "1" });
        List<TermFrequency> list1 = new ArrayList<>();
        list1.add(tf1);
        list1.add(tf2);

        TransactionStatus status = transactionManager.getTransaction(null);
        {
            // SearchParameters dbParam1 = searchParametersRepository.save(param1);
            // QueryKey dbKey1 = dbParam1.getQueryKey();

            TermFrequencyResults tfr = new TermFrequencyResults(key1);
            tfr.accumulateTermFrequencyList(param1, list1);
            termFrequencyResultsRepository.save(tfr);
        }
        transactionManager.rollback(status);
        status = transactionManager.getTransaction(null);
        {
            // SearchParameters dbParam1_2 = searchParametersRepository.save(param1_2);
            // QueryKey dbKey1_2 = dbParam1_2.getQueryKey();

            TermFrequencyResults tfr = termFrequencyResultsRepository.findByQueryKey(param1_2.getQueryKey());
            assertNotNull(tfr);
            tfr.accumulateTermFrequencyList(param1_2, list1);
            termFrequencyResultsRepository.save(tfr);
        }
        transactionManager.commit(status);
        status = transactionManager.getTransaction(null);
        {
            // SearchParameters dbParam2 = searchParametersRepository.save(param2);
            // QueryKey dbKey2 = dbParam2.getQueryKey();

            TermFrequencyResults tfr = new TermFrequencyResults(param2.getQueryKey());
            tfr.accumulateTermFrequencyList(param2, list1);
            termFrequencyResultsRepository.save(tfr);
        }
    }

    @Test(expected = DataIntegrityViolationException.class)
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
            // SearchParameters dbParam1 = searchParametersRepository.save(param1);
            // QueryKey dbKey1 = dbParam1.getQueryKey();

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
