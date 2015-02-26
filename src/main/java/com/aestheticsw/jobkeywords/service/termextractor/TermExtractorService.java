package com.aestheticsw.jobkeywords.service.termextractor;

import java.util.List;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aestheticsw.jobkeywords.domain.indeed.JobListResponse;
import com.aestheticsw.jobkeywords.domain.indeed.JobSummary;
import com.aestheticsw.jobkeywords.domain.termfrequency.QueryKey;
import com.aestheticsw.jobkeywords.domain.termfrequency.QueryList;
import com.aestheticsw.jobkeywords.domain.termfrequency.SearchParameters;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermFrequencyResults;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermList;
import com.aestheticsw.jobkeywords.service.database.TermQueryDataManager;
import com.aestheticsw.jobkeywords.service.termextractor.fivefilters.FiveFiltersService;
import com.aestheticsw.jobkeywords.service.termextractor.indeed.IndeedQueryException;
import com.aestheticsw.jobkeywords.service.termextractor.indeed.IndeedService;

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

    private TermQueryDataManager termQueryDataManager;

    private IndeedService indeedService;

    private FiveFiltersService fiveFiltersService;

    @Autowired
    public TermExtractorService(TermQueryDataManager termQueryDataManager, IndeedService indeedService,
            FiveFiltersService fiveFiltersService) {
        this.termQueryDataManager = termQueryDataManager;
        this.indeedService = indeedService;
        this.fiveFiltersService = fiveFiltersService;
    }

    public TermList extractTerms(SearchParameters params) throws IndeedQueryException {

        JobListResponse jobListResponse = getIndeedService().getIndeedJobList(params);

        List<JobSummary> jobSummaries = jobListResponse.getResults().getResults();

        log.info("Indeed returned jobCount=" + ((jobSummaries != null) ? jobSummaries.size() : "0 (no results)"));
        if (jobSummaries == null || jobSummaries.size() == 0) {
            throw new IndeedQueryException("Query returned no results: " + params);
        }

        StringBuilder combinedJobDetails = new StringBuilder();
        for (int index = 0; index < params.getJobCount() && index < jobSummaries.size(); index++) {
            JobSummary job = jobSummaries.get(index);
            String jobDetails = getIndeedService().getIndeedJobDetails(job.getUrl());
            combinedJobDetails.append(jobDetails);
            combinedJobDetails.append("\n ");
        }

        TermList terms = getFiveFiltersService().getTermList(combinedJobDetails.toString(), params.getLocale());

        getTermQueryRepository().accumulateTermFrequencyResults(params, terms.getTerms());

        return terms;
    }

    public TermFrequencyResults getAccumulatedTermFrequencyResults(QueryKey queryKey) {
        return getTermQueryRepository().getAccumulatedResults(queryKey);
    }

    public QueryList getSearchHistory() {
        return getTermQueryRepository().getSearchHistory();
    }

    private TermQueryDataManager getTermQueryRepository() {
        return termQueryDataManager;
    }

    private IndeedService getIndeedService() {
        return indeedService;
    }

    private FiveFiltersService getFiveFiltersService() {
        return fiveFiltersService;
    }

}
