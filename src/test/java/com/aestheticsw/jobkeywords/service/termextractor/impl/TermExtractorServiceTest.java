package com.aestheticsw.jobkeywords.service.termextractor.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
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
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequency;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyResults;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermList;
import com.aestheticsw.jobkeywords.service.termextractor.impl.TermExtractorServiceImpl;
import com.aestheticsw.jobkeywords.service.termextractor.impl.fivefilters.FiveFiltersClient;
import com.aestheticsw.jobkeywords.service.termextractor.impl.indeed.IndeedClient;
import com.aestheticsw.jobkeywords.service.termextractor.impl.indeed.IndeedQueryException;
import com.aestheticsw.jobkeywords.service.termextractor.impl.indeed.JobList;
import com.aestheticsw.jobkeywords.service.termextractor.impl.indeed.JobListResponse;
import com.aestheticsw.jobkeywords.service.termextractor.repository.QueryKeyRepository;
import com.aestheticsw.jobkeywords.service.termextractor.repository.TermFrequencyResultsDataManager;
import com.aestheticsw.jobkeywords.service.termextractor.repository.TermFrequencyResultsRepository;
import com.aestheticsw.jobkeywords.utils.FileUtils;

public class TermExtractorServiceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TermExtractorServiceImpl serviceUnderTest;

    @Mock
    private TermFrequencyResultsDataManager termFrequencyResultsDataManager;

    @Mock
    private IndeedClient indeedClient;

    @Mock
    private FiveFiltersClient fiveFiltersClient;

    @Mock
    private TermFrequencyResultsRepository termFrequencyResultsRepository;

    @Mock
    private QueryKeyRepository queryKeyRepository;

    private JobListResponse jobListResponse;

    private String jobDetailsString;

    private TermList termList;

    private String[][] termListStringArray;

    private QueryKey queryKey;

    private TermFrequencyResults termFrequencyResults;

    public TermExtractorServiceTest() {
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
            Mockito.spy(new TermExtractorServiceImpl(termFrequencyResultsDataManager, indeedClient, fiveFiltersClient));

        // manually set the logger because Spring doesn't instantiate the serviceUnderTest
        serviceUnderTest.log = LoggerFactory.getLogger(serviceUnderTest.getClass());

        List<JobSummary> jobSummaries = new ArrayList<JobSummary>();
        jobSummaries.add(new JobSummary("Senior Engineer", "Aesthetic Software, Inc", "San Francisco", "CA", null,
            null, "Back end development", null, null, false));
        JobList jobList = new JobList(jobSummaries);

        jobListResponse = new JobListResponse("San Francisco", false, false, 1, 0, 1, 0, jobList);

        jobDetailsString = FileUtils.getClassResourceAsString("indeed-content.html", this);

        termListStringArray = new String[][] { { "term one", "4", "2" }, { "term two", "2", "2" } };

        List<TermFrequency> termFrequencyList = new ArrayList<>();
        for (int i = 0; i < termListStringArray.length; i++) {
            termFrequencyList.add(new TermFrequency(termListStringArray[i]));
        }
        termList = new TermList(termFrequencyList);

        queryKey = new QueryKey("query", Locale.US, "city");
        termFrequencyResults = new TermFrequencyResults(queryKey);

        when(indeedClient.getIndeedJobList(Mockito.any(SearchParameters.class))).thenReturn(jobListResponse);
        when(fiveFiltersClient.executeFiveFiltersPost(Mockito.any(String.class))).thenReturn(termListStringArray);
        when(fiveFiltersClient.getTermList(Mockito.any(String.class), Mockito.any(Locale.class))).thenReturn(termList);

        // when(termFrequencyResultsDataManager.accumulateTermFrequencyResults(Mockito.any(SearchParameters.class), Mockito.any(List.class))).thenReturn(null);
        when(termFrequencyResultsDataManager.getAccumulatedResults(Mockito.any(QueryKey.class))).thenReturn(termFrequencyResults);
        when(queryKeyRepository.findByCompoundKey(Mockito.any(QueryKey.class))).thenReturn(queryKey);
        when(termFrequencyResultsRepository.findByQueryKey(Mockito.any(QueryKey.class))).thenReturn(termFrequencyResults);

        when(indeedClient.getIndeedJobDetails(Mockito.any(String.class))).thenReturn(jobDetailsString);

        // given(serviceUnderTest.extractTerms(Mockito.any(SearchParameters.class))).willReturn(termList);;
    }

    @Test
    public void loadContext() {
        assertNotNull(serviceUnderTest);
    }

    @Test
    public void extractTerms() throws IOException {
        // when(this.indeedService.getIndeedJobList(Mockito.any(SearchParameters.class))).thenReturn(jobListResponse);

        SearchParameters params1 = new SearchParameters("java", 1, 0, Locale.US, null, 0, null);
        TermList extractedTermList;
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
        assertEquals(4, firstTerm.getFrequency());

        TermFrequency secondTerm = extractedTerms.get(1);
        assertEquals("term two", secondTerm.getTerm());
        assertEquals(2, secondTerm.getFrequency());

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

}
