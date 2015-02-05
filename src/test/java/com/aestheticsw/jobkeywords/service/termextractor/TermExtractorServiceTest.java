package com.aestheticsw.jobkeywords.service.termextractor;

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

import com.aestheticsw.jobkeywords.database.TermQueryRepository;
import com.aestheticsw.jobkeywords.domain.indeed.JobList;
import com.aestheticsw.jobkeywords.domain.indeed.JobListResponse;
import com.aestheticsw.jobkeywords.domain.indeed.JobSummary;
import com.aestheticsw.jobkeywords.domain.termfrequency.SearchParameters;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermFrequency;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermFrequencyResults;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermList;
import com.aestheticsw.jobkeywords.service.termextractor.fivefilters.FiveFiltersService;
import com.aestheticsw.jobkeywords.service.termextractor.indeed.IndeedQueryException;
import com.aestheticsw.jobkeywords.service.termextractor.indeed.IndeedService;
import com.aestheticsw.jobkeywords.utils.FileUtils;

public class TermExtractorServiceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TermExtractorService service;

    private TermQueryRepository termQueryRepository;

    @Mock
    private IndeedService indeedService;

    @Mock
    private FiveFiltersService fiveFiltersService;

    private JobListResponse jobListResponse;

    private String jobDetailsString;

    private TermList termList;

    private String[][] termListStringArray;

    public TermExtractorServiceTest() {
    }

    @Before
    public void initializeTests() {
        MockitoAnnotations.initMocks(this);

        termQueryRepository = Mockito.spy(new TermQueryRepository());
        termQueryRepository.log = LoggerFactory.getLogger(termQueryRepository.getClass());

        service = Mockito.spy(new TermExtractorService(termQueryRepository, indeedService, fiveFiltersService));
        service.log = LoggerFactory.getLogger(service.getClass());

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

        when(indeedService.getIndeedJobList(Mockito.any(SearchParameters.class))).thenReturn(jobListResponse);
        when(fiveFiltersService.executeFiveFiltersPost(Mockito.any(String.class))).thenReturn(termListStringArray);
        when(fiveFiltersService.getTermList(Mockito.any(String.class), Mockito.any(Locale.class))).thenReturn(termList);
        // when(termQueryRepository.accumulateTermFrequencyResults(Mockito.any(SearchParameters.class),
        // Mockito.any(List.class)))

        when(indeedService.getIndeedJobDetails(Mockito.any(String.class))).thenReturn(jobDetailsString);

        // given(service.extractTerms(Mockito.any(SearchParameters.class))).willReturn(termList);;
    }

    @Test
    public void loadContext() {
        assertNotNull(service);
    }

    @Test
    public void extractTerms() throws IOException {
        // when(this.indeedService.getIndeedJobList(Mockito.any(SearchParameters.class))).thenReturn(jobListResponse);

        SearchParameters params1 = new SearchParameters("java", 1, 0, Locale.US, null, 0, null);
        TermList extractedTermList;
        try {
            extractedTermList = service.extractTerms(params1);
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

        // extract a second time to make sure that the Repository accumulates twice as many terms.
        SearchParameters params2 = new SearchParameters("java", 1, 1, Locale.US, null, 0, null);
        try {
            extractedTermList = service.extractTerms(params2);
        } catch (IndeedQueryException e) {
            throw new RuntimeException(e);
        }

        TermFrequencyResults accumulatedResults = termQueryRepository.getAccumulatedResults(params2.getQueryKey());
        assertNotNull(accumulatedResults);

        List<TermFrequency> sortedList = accumulatedResults.getSortedList();
        assertEquals(2, sortedList.size());
        assertEquals("term one", sortedList.get(0).getTerm());
        assertEquals(8, sortedList.get(0).getFrequency());
        assertEquals("term two", sortedList.get(1).getTerm());
        assertEquals(4, sortedList.get(1).getFrequency());
    }

}
