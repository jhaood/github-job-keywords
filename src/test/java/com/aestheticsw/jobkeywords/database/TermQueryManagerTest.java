package com.aestheticsw.jobkeywords.database;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.aestheticsw.jobkeywords.config.TestBase;
import com.aestheticsw.jobkeywords.database.TermQueryManager;
import com.aestheticsw.jobkeywords.domain.termfrequency.SearchParameters;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermFrequency;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermFrequencyResults;

public class TermQueryManagerTest extends TestBase {

    @Autowired
    private TermQueryManager termQueryManager;

    @Test
    public void loadContext() {
        assertNotNull(termQueryManager);
    }

    @Test
    public void accumulateTermFrequencyResults() {
        SearchParameters param1 = new SearchParameters("query-one", 1, 1, Locale.US, null, 0, null);
        SearchParameters param1_2 = new SearchParameters("query-one", 1, 2, Locale.US, null, 0, null);
        SearchParameters param2 = new SearchParameters("query-TWO", 1, 1, Locale.US, null, 0, null);

        TermFrequency tf1 = new TermFrequency(new String[] { "java", "3", "1" });
        TermFrequency tf2 = new TermFrequency(new String[] { "spring", "2", "1" });
        List<TermFrequency> list1 = new ArrayList();
        list1.add(tf1);
        list1.add(tf2);

        TermFrequency tf3 = new TermFrequency(new String[] { "java", "7", "1" });
        TermFrequency tf4 = new TermFrequency(new String[] { "spring", "8", "1" });
        List<TermFrequency> list2 = new ArrayList();
        list2.add(tf3);
        list2.add(tf4);

        termQueryManager.accumulateTermFrequencyResults(param1, list1);
        termQueryManager.accumulateTermFrequencyResults(param1_2, list1);

        TermFrequencyResults results = termQueryManager.getAccumulatedResults(param1);
        assertNotNull(results);
        assertNull(termQueryManager.getAccumulatedResults(param2));

        List<TermFrequency> sortedList = results.getSortedList(new TermFrequency.FrequencyComparator());
        assertNotNull(sortedList);
        assertEquals(6, sortedList.get(0).getFrequency());
        assertEquals("java", sortedList.get(0).getTerm());

        assertEquals(4, sortedList.get(1).getFrequency());
        assertEquals("spring", sortedList.get(1).getTerm());

        termQueryManager.accumulateTermFrequencyResults(param2, list2);

        assertNotNull(termQueryManager.getAccumulatedResults(param1));
        results = termQueryManager.getAccumulatedResults(param2);
        assertNotNull(results);

        sortedList = results.getSortedList(new TermFrequency.FrequencyComparator());
        assertNotNull(sortedList);

        assertEquals(8, sortedList.get(0).getFrequency());
        assertEquals("spring", sortedList.get(0).getTerm());

        assertEquals(7, sortedList.get(1).getFrequency());
        assertEquals("java", sortedList.get(1).getTerm());
    }
}
