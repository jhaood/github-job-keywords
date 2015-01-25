package com.aestheticsw.jobkeywords.service.termextractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aestheticsw.jobkeywords.database.TermQueryManager;
import com.aestheticsw.jobkeywords.domain.indeed.JobListResponse;
import com.aestheticsw.jobkeywords.domain.indeed.JobSummary;
import com.aestheticsw.jobkeywords.domain.termfrequency.QueryKey;
import com.aestheticsw.jobkeywords.domain.termfrequency.SearchParameters;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermFrequency;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermFrequencyResults;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermList;
import com.aestheticsw.jobkeywords.service.indeed.IndeedService;

@Service
public class TermExtractorService {

    @Log
    private Logger log;

    @Autowired
    private TermQueryManager termQueryManager;

    @Autowired
    private IndeedService indeedService;

    @Autowired
    private FiveFiltersService fiveFiltersService;

    public TermList extractTerms(SearchParameters params) throws IOException {
        
        JobListResponse jobListResponse = indeedService.getIndeedJobList(params);

        List<JobSummary> jobSummaries = jobListResponse.getResults().getResults();
        log.info("Indeed returned jobCount=" + jobSummaries.size());

        StringBuilder combinedJobDetails = new StringBuilder();
        for (int index = 0; index < params.getJobCount() && index < jobSummaries.size(); index++) {
            JobSummary job = jobSummaries.get(index);
            String jobDetails = indeedService.getIndeedJobDetails(job.getUrl());
            combinedJobDetails.append(jobDetails);
            combinedJobDetails.append("\n ");
        }

        TermList terms = fiveFiltersService.getTermList(combinedJobDetails.toString(), params.getLocale());
        if (terms == null) {
            return new TermList(new ArrayList<TermFrequency>());
        }

        termQueryManager.accumulateTermFrequencyResults(params, terms.getTerms());
        
        return terms;
    }
    
    public TermFrequencyResults getAccumulatedTermFrequencyResults(QueryKey queryKey) {
        return termQueryManager.getAccumulatedResults(queryKey);
    }
}
