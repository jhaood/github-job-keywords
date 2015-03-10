package com.aestheticsw.jobkeywords.service.termextractor.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aestheticsw.jobkeywords.config.DatabaseTestConfiguration;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequency;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyResults;

@RunWith(SpringJUnit4ClassRunner.class)
@DatabaseTestConfiguration
public class TermFrequencyResultsDataManagerTest {

    @Autowired
    private TermFrequencyResultsDataManager termFrequencyResultsDataManager;

    @Autowired
    DataSource dataSource;

    @Before
    public void setup() {
    }

    @After
    @Transactional
    public void dumpDatabase() throws SQLException, DatabaseUnitException, FileNotFoundException, IOException {
        // full database export
        Connection jdbcConnection = dataSource.getConnection();
        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
        IDataSet fullDataSet = connection.createDataSet();
        FlatXmlDataSet.write(fullDataSet, new FileOutputStream(
            "./target/TermFrequencyResultsDataManagerTest-dbunit.xml"));
    }

    @Test
    public void loadContext() {
        assertNotNull(termFrequencyResultsDataManager);
    }

    @Test
    public void accumulateTermFrequencyResults() {
        QueryKey key1 = new QueryKey("query-six", Locale.US, "");
        SearchParameters param1 = new SearchParameters(key1, 1, 1, 0, "");

        {
            TermFrequency tf1 = new TermFrequency(new String[] { "java", "3", "1" });
            TermFrequency tf2 = new TermFrequency(new String[] { "spring", "2", "1" });
            List<TermFrequency> list1 = new ArrayList<>();
            list1.add(tf1);
            list1.add(tf2);

            termFrequencyResultsDataManager.accumulateTermFrequencyResults(param1, list1);

            SearchParameters param1_2 = new SearchParameters(key1, 1, 2, 0, "");
            termFrequencyResultsDataManager.accumulateTermFrequencyResults(param1_2, list1);
        }

        QueryKey key3 = new QueryKey("query-seven", Locale.US, "");
        SearchParameters param2 = new SearchParameters(key3, 1, 1, 0, "");
        {

            // confirm that adding an empty results-list ignores the params and list.
            termFrequencyResultsDataManager.accumulateTermFrequencyResults(param2, new ArrayList<>());

            TermFrequencyResults results = termFrequencyResultsDataManager.getAccumulatedResults(param2.getQueryKey());
            assertNull(results);
        }
        {
            // assert param1's results - should have accumulated list1 twice
            TermFrequencyResults results = termFrequencyResultsDataManager.getAccumulatedResults(param1.getQueryKey());
            assertNotNull(results);
            assertEquals(2, results.getSortedTermFrequencyList().size());

            List<TermFrequency> sortedList =
                results.getSortedTermFrequencyList(new TermFrequency.FrequencyComparator());
            assertNotNull(sortedList);
            assertEquals(6, sortedList.get(0).getFrequency());
            assertEquals("java", sortedList.get(0).getTerm());

            assertEquals(4, sortedList.get(1).getFrequency());
            assertEquals("spring", sortedList.get(1).getTerm());
        }
        {
            // add to param2's results

            TermFrequency tf3 = new TermFrequency(new String[] { "java", "7", "1" });
            TermFrequency tf4 = new TermFrequency(new String[] { "spring", "8", "1" });
            List<TermFrequency> list2 = new ArrayList<>();
            list2.add(tf3);
            list2.add(tf4);

            termFrequencyResultsDataManager.accumulateTermFrequencyResults(param2, list2);
        }
        {
            // confirm that param2's results are separate from param1's results 

            TermFrequencyResults results = termFrequencyResultsDataManager.getAccumulatedResults(param2.getQueryKey());
            assertNotNull(results);

            List<TermFrequency> sortedList =
                results.getSortedTermFrequencyList(new TermFrequency.FrequencyComparator());
            assertNotNull(sortedList);

            assertEquals(8, sortedList.get(0).getFrequency());
            assertEquals("spring", sortedList.get(0).getTerm());

            assertEquals(7, sortedList.get(1).getFrequency());
            assertEquals("java", sortedList.get(1).getTerm());
        }
    }

}
