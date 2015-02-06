package com.aestheticsw.jobkeywords.domain.termfrequency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class TermFrequencyResultsTest {

    @Test
    public void accumulate() {
        SearchParameters param1 = new SearchParameters("query", 1, 1, Locale.US, null, 0, null);
        SearchParameters param2 = new SearchParameters("query", 1, 2, Locale.US, null, 0, null);
        SearchParameters param3 = new SearchParameters("query", 1, 3, Locale.US, null, 0, null);

        TermFrequency tf1 = new TermFrequency(new String[] { "java", "3", "1" });
        TermFrequency tf2 = new TermFrequency(new String[] { "spring", "2", "1" });
        TermFrequency tf3 = new TermFrequency(new String[] { "hadoop", "1", "1" });
        List<TermFrequency> list1 = new ArrayList<>();
        list1.add(tf1);
        list1.add(tf2);
        List<TermFrequency> list2 = new ArrayList<>();
        list2.add(tf2);
        list2.add(tf3);

        TermFrequencyResults results = new TermFrequencyResults(param1.getQueryKey());
        results.accumulateTermFrequencyList(param1, list1);
        results.accumulateTermFrequencyList(param2, list1);
        results.accumulateTermFrequencyList(param3, list2);

        List<TermFrequency> sortedList = results.getSortedTermFrequencyList(new TermFrequency.FrequencyComparator());
        assertNotNull(sortedList);
        assertEquals(6, sortedList.get(0).getFrequency());
        assertEquals("java", sortedList.get(0).getTerm());

        assertEquals(6, sortedList.get(1).getFrequency());
        assertEquals("spring", sortedList.get(1).getTerm());

        assertEquals(1, sortedList.get(2).getFrequency());
        assertEquals("hadoop", sortedList.get(2).getTerm());

        // adding param2 a second time should get ignored.
        results.accumulateTermFrequencyList(param2, list2);
        sortedList = results.getSortedTermFrequencyList(new TermFrequency.FrequencyComparator());

        assertEquals(6, sortedList.get(1).getFrequency());
        assertEquals("spring", sortedList.get(1).getTerm());

        assertEquals(1, sortedList.get(2).getFrequency());
        assertEquals("hadoop", sortedList.get(2).getTerm());

    }
}
