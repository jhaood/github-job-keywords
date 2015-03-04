package com.aestheticsw.jobkeywords.service.termextractor.impl;

import java.util.List;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aestheticsw.jobkeywords.service.termextractor.TermExtractorService;
import com.aestheticsw.jobkeywords.service.termextractor.domain.JobSummary;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryList;
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyResults;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermList;
import com.aestheticsw.jobkeywords.service.termextractor.impl.fivefilters.FiveFiltersClient;
import com.aestheticsw.jobkeywords.service.termextractor.impl.indeed.IndeedClient;
import com.aestheticsw.jobkeywords.service.termextractor.impl.indeed.IndeedQueryException;
import com.aestheticsw.jobkeywords.service.termextractor.impl.indeed.JobListResponse;
import com.aestheticsw.jobkeywords.service.termextractor.repository.TermFrequencyResultsDataManager;

/**
 * The TermExtracorService is the primary public interface that wraps both the Indeed and
 * FiveFilters services.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@Service
public class TermExtractorServiceImpl implements TermExtractorService {

    @Log
    public Logger log;

    private TermFrequencyResultsDataManager termFrequencyResultsDataManager;

    private IndeedClient indeedClient;

    private FiveFiltersClient fiveFiltersClient;

    @Autowired
    public TermExtractorServiceImpl(TermFrequencyResultsDataManager termFrequencyResultsDataManager, IndeedClient indeedClient,
            FiveFiltersClient fiveFiltersClient) {
        this.termFrequencyResultsDataManager = termFrequencyResultsDataManager;
        this.indeedClient = indeedClient;
        this.fiveFiltersClient = fiveFiltersClient;
    }

    /* (non-Javadoc)
     * @see com.aestheticsw.jobkeywords.service.termextractor.TermExtractorService#extractTerms(com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters)
     */
    @Override
    public TermList extractTerms(SearchParameters params) throws IndeedQueryException {

        JobListResponse jobListResponse = getIndeedClient().getIndeedJobList(params);

        List<JobSummary> jobSummaries = jobListResponse.getResults().getResults();

        log.info("Indeed returned jobCount=" + ((jobSummaries != null) ? jobSummaries.size() : "0 (no results)"));
        if (jobSummaries == null || jobSummaries.size() == 0) {
            throw new IndeedQueryException("Query returned no results: " + params);
        }

        StringBuilder combinedJobDetails = new StringBuilder();
        for (int index = 0; index < params.getJobCount() && index < jobSummaries.size(); index++) {
            JobSummary job = jobSummaries.get(index);
            String jobDetails = getIndeedClient().getIndeedJobDetails(job.getUrl());
            combinedJobDetails.append(jobDetails);
            combinedJobDetails.append("\n ");
        }

        TermList terms = getFiveFiltersService().getTermList(combinedJobDetails.toString(), params.getLocale());

        getTermFrequencyResultsDataManager().accumulateTermFrequencyResults(params, terms.getTerms());

        return terms;
    }

    /* (non-Javadoc)
     * @see com.aestheticsw.jobkeywords.service.termextractor.TermExtractorService#getAccumulatedTermFrequencyResults(com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey)
     */
    @Override
    public TermFrequencyResults getAccumulatedTermFrequencyResults(QueryKey queryKey) {
        return getTermFrequencyResultsDataManager().getAccumulatedResults(queryKey);
    }

    /* (non-Javadoc)
     * @see com.aestheticsw.jobkeywords.service.termextractor.TermExtractorService#getSearchHistory()
     */
    @Override
    public QueryList getSearchHistory() {
        return new QueryList(getTermFrequencyResultsDataManager().getSearchHistory());
    }

    private TermFrequencyResultsDataManager getTermFrequencyResultsDataManager() {
        return termFrequencyResultsDataManager;
    }

    private IndeedClient getIndeedClient() {
        return indeedClient;
    }

    private FiveFiltersClient getFiveFiltersService() {
        return fiveFiltersClient;
    }

}
