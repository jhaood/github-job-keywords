package com.aestheticsw.jobkeywords.service.termextractor;

import java.util.List;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aestheticsw.jobkeywords.domain.JobSummary;
import com.aestheticsw.jobkeywords.domain.QueryKey;
import com.aestheticsw.jobkeywords.domain.QueryList;
import com.aestheticsw.jobkeywords.domain.SearchParameters;
import com.aestheticsw.jobkeywords.domain.TermFrequencyResults;
import com.aestheticsw.jobkeywords.domain.TermList;
import com.aestheticsw.jobkeywords.service.termextractor.fivefilters.FiveFiltersClient;
import com.aestheticsw.jobkeywords.service.termextractor.indeed.IndeedClient;
import com.aestheticsw.jobkeywords.service.termextractor.indeed.IndeedQueryException;
import com.aestheticsw.jobkeywords.service.termextractor.indeed.JobListResponse;
import com.aestheticsw.jobkeywords.service.termextractor.repository.TermFrequencyResultsDataManager;

/**
 * The TermExtracorService is the primary public interface that wraps both the Indeed and
 * FiveFilters services.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@Service
public class TermExtractorService {

    @Log
    Logger log;

    private TermFrequencyResultsDataManager termFrequencyResultsDataManager;

    private IndeedClient indeedClient;

    private FiveFiltersClient fiveFiltersClient;

    @Autowired
    public TermExtractorService(TermFrequencyResultsDataManager termFrequencyResultsDataManager, IndeedClient indeedClient,
            FiveFiltersClient fiveFiltersClient) {
        this.termFrequencyResultsDataManager = termFrequencyResultsDataManager;
        this.indeedClient = indeedClient;
        this.fiveFiltersClient = fiveFiltersClient;
    }

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

    public TermFrequencyResults getAccumulatedTermFrequencyResults(QueryKey queryKey) {
        return getTermFrequencyResultsDataManager().getAccumulatedResults(queryKey);
    }

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
