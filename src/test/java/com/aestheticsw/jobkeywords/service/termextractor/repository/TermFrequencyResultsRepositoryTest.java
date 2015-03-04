package com.aestheticsw.jobkeywords.service.termextractor.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
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
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequency;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyResults;
import com.aestheticsw.jobkeywords.service.termextractor.repository.QueryKeyRepository;
import com.aestheticsw.jobkeywords.service.termextractor.repository.TermFrequencyResultsRepository;

// TODO rename to *IT
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DatabaseConfiguration.class })
public class TermFrequencyResultsRepositoryTest {

    @Autowired
    private TermFrequencyResultsRepository termFrequencyResultsRepository;

    @Autowired
    private QueryKeyRepository queryKeyRepository;

    @Test
    public void loadContext() {
        assertNotNull(termFrequencyResultsRepository);
    }

    @Test
    public void saveAndRetrieve() {
        TermFrequency tf1 = new TermFrequency(new String[] { "java", "3", "1" });
        TermFrequency tf2 = new TermFrequency(new String[] { "spring", "2", "1" });
        List<TermFrequency> list1 = new ArrayList<>();
        list1.add(tf1);
        list1.add(tf2);

        QueryKey dbKey1;
        TermFrequencyResults dbTfr1;
        {
            SearchParameters param1 = new SearchParameters("query-one", 1, 1, Locale.US, "", 0, "");

            dbKey1 = queryKeyRepository.save(param1.getQueryKey());

            TermFrequencyResults tfr1 = new TermFrequencyResults(dbKey1);
            tfr1.accumulateTermFrequencyList(param1, list1);

            termFrequencyResultsRepository.save(tfr1);

            dbTfr1 = termFrequencyResultsRepository.findByQueryKey(dbKey1);
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

        {
            SearchParameters param1_2 = new SearchParameters("query-one", 1, 2, Locale.US, "", 0, "");

            dbTfr1.accumulateTermFrequencyList(param1_2, list1);

            termFrequencyResultsRepository.save(dbTfr1);

            TermFrequencyResults dbTfr2 = termFrequencyResultsRepository.findByQueryKey(dbKey1);
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
        SearchParameters param1 = new SearchParameters("query-one", 1, 1, Locale.US, "", 0, "");
        SearchParameters param1_2 = new SearchParameters("query-one", 1, 2, Locale.US, "", 0, "");
        SearchParameters param2 = new SearchParameters("query-TWO", 1, 1, Locale.US, "", 0, "");

        TermFrequency tf1 = new TermFrequency(new String[] { "java", "3", "1" });
        TermFrequency tf2 = new TermFrequency(new String[] { "spring", "2", "1" });
        List<TermFrequency> list1 = new ArrayList<>();
        list1.add(tf1);
        list1.add(tf2);

        {
            QueryKey dbKey1 = queryKeyRepository.save(param1.getQueryKey());

            TermFrequencyResults tfr = new TermFrequencyResults(dbKey1);
            tfr.accumulateTermFrequencyList(param1, list1);
            termFrequencyResultsRepository.save(tfr);
        }

        {
            QueryKey dbKey1_2 = queryKeyRepository.save(param1_2.getQueryKey());

            TermFrequencyResults tfr = new TermFrequencyResults(dbKey1_2);
            tfr.accumulateTermFrequencyList(param1_2, list1);
            termFrequencyResultsRepository.save(tfr);
        }

        {
            QueryKey dbKey2 = queryKeyRepository.save(param2.getQueryKey());

            TermFrequencyResults tfr = new TermFrequencyResults(dbKey2);
            tfr.accumulateTermFrequencyList(param2, list1);
            termFrequencyResultsRepository.save(tfr);
        }
        
        List<QueryKey> keys = termFrequencyResultsRepository.findDistinctQueryKeys();
        assertNotNull(keys);
        assertEquals(2, keys.size());
    }
}
