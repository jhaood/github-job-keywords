package com.aestheticsw.jobkeywords.service.termextractor.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
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
import com.aestheticsw.jobkeywords.config.LogInjectorConfiguration;
import com.aestheticsw.jobkeywords.domain.SearchParameters;
import com.aestheticsw.jobkeywords.domain.TermFrequency;
import com.aestheticsw.jobkeywords.domain.TermFrequencyResults;
import com.aestheticsw.jobkeywords.service.termextractor.repository.TermFrequencyResultsDataManager;

//TODO rename to *IT
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, 
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DatabaseConfiguration.class, LogInjectorConfiguration.class })
public class TermFrequencyResultsDataManagerTest {

    @Autowired
    private TermFrequencyResultsDataManager termFrequencyResultsDataManager;

    @Before 
    public void setup() {
    }
    
    @Test
    public void loadContext() {
        assertNotNull(termFrequencyResultsDataManager);
    }

    @Test
    public void accumulateTermFrequencyResults() {
        SearchParameters param1 = new SearchParameters("query-one", 1, 1, Locale.US, "", 0, "");
        SearchParameters param1_2 = new SearchParameters("query-one", 1, 2, Locale.US, "", 0, "");
        SearchParameters param2 = new SearchParameters("query-TWO", 1, 1, Locale.US, "", 0, "");

        TermFrequency tf1 = new TermFrequency(new String[] { "java", "3", "1" });
        TermFrequency tf2 = new TermFrequency(new String[] { "spring", "2", "1" });
        List<TermFrequency> list1 = new ArrayList<>();
        list1.add(tf1);
        list1.add(tf2);

        TermFrequency tf3 = new TermFrequency(new String[] { "java", "7", "1" });
        TermFrequency tf4 = new TermFrequency(new String[] { "spring", "8", "1" });
        List<TermFrequency> list2 = new ArrayList<>();
        list2.add(tf3);
        list2.add(tf4);

        termFrequencyResultsDataManager.accumulateTermFrequencyResults(param1, list1);
        termFrequencyResultsDataManager.accumulateTermFrequencyResults(param1_2, list1);

        // confirm that adding an empty results-list ignores the params and list.
        termFrequencyResultsDataManager.accumulateTermFrequencyResults(param2, new ArrayList<>());

        TermFrequencyResults results = termFrequencyResultsDataManager.getAccumulatedResults(param1.getQueryKey());
        assertNotNull(results);
        assertFalse(termFrequencyResultsDataManager.getAccumulatedResults(param2.getQueryKey()).hasResults());
        assertEquals(0, termFrequencyResultsDataManager.getAccumulatedResults(param2.getQueryKey()).getSortedTermFrequencyList().size());

        // assert that the manager ignored param2's empty results
        assertEquals(2, results.getSortedTermFrequencyList().size());

        List<TermFrequency> sortedList = results.getSortedTermFrequencyList(new TermFrequency.FrequencyComparator());
        assertNotNull(sortedList);
        assertEquals(6, sortedList.get(0).getFrequency());
        assertEquals("java", sortedList.get(0).getTerm());

        assertEquals(4, sortedList.get(1).getFrequency());
        assertEquals("spring", sortedList.get(1).getTerm());

        termFrequencyResultsDataManager.accumulateTermFrequencyResults(param2, list2);

        assertNotNull(termFrequencyResultsDataManager.getAccumulatedResults(param1.getQueryKey()));
        results = termFrequencyResultsDataManager.getAccumulatedResults(param2.getQueryKey());
        assertNotNull(results);

        sortedList = results.getSortedTermFrequencyList(new TermFrequency.FrequencyComparator());
        assertNotNull(sortedList);

        assertEquals(8, sortedList.get(0).getFrequency());
        assertEquals("spring", sortedList.get(0).getTerm());

        assertEquals(7, sortedList.get(1).getFrequency());
        assertEquals("java", sortedList.get(1).getTerm());
    }
}
