/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.service.termextractor.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;

import com.aestheticsw.jobkeywords.service.termextractor.domain.JobSummary;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKeyList;
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequency;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyList;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyResults;
import com.aestheticsw.jobkeywords.service.termextractor.impl.fivefilters.FiveFiltersClient;
import com.aestheticsw.jobkeywords.service.termextractor.impl.indeed.IndeedClient;
import com.aestheticsw.jobkeywords.service.termextractor.impl.indeed.IndeedQueryException;
import com.aestheticsw.jobkeywords.service.termextractor.repository.JobSummaryRepository;
import com.aestheticsw.jobkeywords.service.termextractor.repository.QueryKeyRepository;
import com.aestheticsw.jobkeywords.service.termextractor.repository.TermFrequencyResultsDataManager;
import com.aestheticsw.jobkeywords.service.termextractor.repository.TermFrequencyResultsRepository;
import com.aestheticsw.jobkeywords.utils.FileUtils;

/**
 * The naming conventions for test classes is
 * <p/>
 * 
 * *UnitTest - a true unit test that may mock dependent classes. A unit test can test a cluster of
 * classes but must not require Spring or any external resources. An external resource is something
 * that lives outside the JVM.
 * <p/>
 * 
 * *Test - an integration test that requires spring to configure the class or it's dependencies. The
 * integration test may or may not hit resources outside the JVM.
 * <p/>
 * 
 * *IT - An integration test that lives outside the web-app and requires a container to be launched
 * by the failsafe plugin. The only *IT.java test is JobKeywordsApplicationIT - which itself is not
 * a true out-of-container integration test.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
public class TermExtractorServiceUnitTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TermExtractorServiceImpl serviceUnderTest;

    @Mock
    private TermFrequencyResultsDataManager termFrequencyResultsDataManager;

    @Mock
    private JobSummaryRepository jobSummaryRepository;

    @Mock
    private IndeedClient indeedClient;

    @Mock
    private FiveFiltersClient fiveFiltersClient;

    @Mock
    private TermFrequencyResultsRepository termFrequencyResultsRepository;

    @Mock
    private QueryKeyRepository queryKeyRepository;

    private List<JobSummary> jobSummaryList;

    private String jobDetailsString;

    private TermFrequencyList termFrequencyList;

    private String[][] termListStringArray;

    private QueryKey queryKey;
    private SearchParameters searchParameters;

    private TermFrequencyResults termFrequencyResults;

    public TermExtractorServiceUnitTest() {
    }

    @SuppressWarnings("unchecked")
    @Before
    public void initializeTests() {
        MockitoAnnotations.initMocks(this);

        /*
        termFrequencyResultsDataManager = Mockito.spy(new TermFrequencyResultsDataManager(queryKeyRepository, termFrequencyResultsRepository));
        // manually set the logger because Spring doesn't instantiate the manager
        termFrequencyResultsDataManager.log = LoggerFactory.getLogger(termFrequencyResultsDataManager.getClass());
        */

        serviceUnderTest =
            Mockito.spy(new TermExtractorServiceImpl(termFrequencyResultsDataManager, jobSummaryRepository,
                indeedClient, fiveFiltersClient));

        // manually set the logger because Spring doesn't instantiate the serviceUnderTest
        serviceUnderTest.log = LoggerFactory.getLogger(serviceUnderTest.getClass());

        List<JobSummary> jobSummaryList = new ArrayList<JobSummary>();
        jobSummaryList.add(new JobSummary("Senior Engineer", "Aesthetic Software, Inc", "San Francisco", "CA", null,
            null, "Back end development", null, null, false));

        jobDetailsString = FileUtils.getClassResourceAsString("indeed-content.html", this);

        termListStringArray = new String[][] { { "term one", "2", "2" }, { "term two", "4", "2" } };

        List<TermFrequency> termFrequencies = new ArrayList<>();
        for (int i = 0; i < termListStringArray.length; i++) {
            termFrequencies.add(new TermFrequency(termListStringArray[i]));
        }
        termFrequencyList = new TermFrequencyList(termFrequencies);

        queryKey = new QueryKey("query", Locale.US, "city");
        searchParameters = new SearchParameters(queryKey, 1, 0, 0, "");
        termFrequencyResults = new TermFrequencyResults(queryKey);
        termFrequencyResults.accumulateTermFrequencyList(searchParameters, termFrequencies);

        when(indeedClient.getIndeedJobSummaryList(Mockito.any(SearchParameters.class))).thenReturn(jobSummaryList);
        when(fiveFiltersClient.executeFiveFiltersPost(Mockito.any(String.class))).thenReturn(termListStringArray);
        when(fiveFiltersClient.getTermFrequencyList(Mockito.any(String.class), Mockito.any(Locale.class))).thenReturn(
            termFrequencyList);

        // when(termFrequencyResultsDataManager.accumulateTermFrequencyResults(Mockito.any(SearchParameters.class), Mockito.any(List.class))).thenReturn(null);
        when(termFrequencyResultsDataManager.getAccumulatedResults(Mockito.any(QueryKey.class))).thenReturn(
            termFrequencyResults);
        when(termFrequencyResultsDataManager.getSearchHistory()).thenReturn(Collections.singletonList(queryKey));

        when(queryKeyRepository.findByCompoundKey(Mockito.any(QueryKey.class))).thenReturn(queryKey);
        when(termFrequencyResultsRepository.findByQueryKey(Mockito.any(QueryKey.class))).thenReturn(
            termFrequencyResults);

        when(indeedClient.getIndeedJobDetails(Mockito.any(String.class))).thenReturn(jobDetailsString);

        // given(serviceUnderTest.extractTerms(Mockito.any(SearchParameters.class))).willReturn(termFrequencyList);;
    }

    @Test
    public void initializeTest() {
        assertNotNull(serviceUnderTest);
    }

    @Test
    public void extractTerms() throws IOException {
        // when(this.indeedService.getIndeedJobList(Mockito.any(SearchParameters.class))).thenReturn(jobSummaryList);

        QueryKey key = new QueryKey("java", Locale.US, null);
        SearchParameters params1 = new SearchParameters(key, 1, 0, 0, null);
        TermFrequencyList extractedTermList;
        try {
            extractedTermList = serviceUnderTest.extractTerms(params1);
        } catch (IndeedQueryException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(extractedTermList);
        List<TermFrequency> extractedTerms = extractedTermList.getTerms();
        assertNotNull(extractedTerms);
        assertEquals(2, extractedTerms.size());
        TermFrequency firstTerm = extractedTerms.get(0);
        assertEquals("term one", firstTerm.getTerm());
        assertEquals(2, firstTerm.getFrequency());

        TermFrequency secondTerm = extractedTerms.get(1);
        assertEquals("term two", secondTerm.getTerm());
        assertEquals(4, secondTerm.getFrequency());

        /* This doesn't test anything because of the mocking 
         * 
        // extract a second time to make sure that the Repository accumulates twice as many terms.
        SearchParameters params2 = new SearchParameters("java", 1, 1, Locale.US, null, 0, null);
        try {
            extractedTermList = serviceUnderTest.extractTerms(params2);
        } catch (IndeedQueryException e) {
            throw new RuntimeException(e);
        }

        extractedTerms = extractedTermList.getTerms();
        assertNotNull(extractedTerms);
        assertEquals(2, extractedTerms.size());
        firstTerm = extractedTerms.get(0);
        assertEquals("term one", firstTerm.getTerm());
        assertEquals(4, firstTerm.getFrequency());

        secondTerm = extractedTerms.get(1);
        assertEquals("term two", secondTerm.getTerm());
        assertEquals(2, secondTerm.getFrequency());
        */
    }

    @Test
    public void getAccumulatedTermFrequencyResults() throws IOException {
        // when(this.indeedService.getIndeedJobList(Mockito.any(SearchParameters.class))).thenReturn(jobSummaryList);

        QueryKey key = new QueryKey("java", Locale.US, null);
        TermFrequencyResults termFrequencyResults;
        termFrequencyResults = serviceUnderTest.getAccumulatedTermFrequencyResults(key);
        assertNotNull(termFrequencyResults);
        assertNotNull(termFrequencyResults.getQueryKey());
        assertEquals(queryKey, termFrequencyResults.getQueryKey());
        assertNotNull(termFrequencyResults.getSearchParametersList());
        assertEquals(1, termFrequencyResults.getSearchParametersList().size());

        List<TermFrequency> termFrequencyList = termFrequencyResults.getSortedTermFrequencyList();
        assertNotNull(termFrequencyList);
        assertEquals(2, termFrequencyList.size());
        assertEquals("term two", termFrequencyList.get(0).getTerm());
    }

    @Test
    public void getSearchHistory() {
        QueryKeyList queryKeyList = serviceUnderTest.getSearchHistory();
        assertNotNull(queryKeyList);
        assertEquals(1, queryKeyList.getQueryKeyList().size());
        assertEquals(queryKey, queryKeyList.getQueryKeyList().get(0));
    }

}
