package com.aestheticsw.jobkeywords.service.termextractor;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aestheticsw.jobkeywords.database.TermQueryManager;
import com.aestheticsw.jobkeywords.domain.indeed.JobListResponse;
import com.aestheticsw.jobkeywords.domain.indeed.JobSummary;
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

    public TermList extractTerms(String query, int jobCount, int start, Locale locale, String city, int radius,
            String sort) throws IOException {
        
        JobListResponse jobListResponse = indeedService.getIndeedJobList(query,
                jobCount,
                start,
                locale,
                city,
                radius,
                sort);

        List<JobSummary> jobSummaries = jobListResponse.getResults().getResults();
        log.info("Indeed returned jobCount=" + jobSummaries.size());

        StringBuilder combinedJobDetails = new StringBuilder();
        for (int index = 0; index < jobCount && index < jobSummaries.size(); index++) {
            JobSummary job = jobSummaries.get(index);
            String jobDetails = indeedService.getIndeedJobDetails(job.getUrl());
            combinedJobDetails.append(jobDetails);
            combinedJobDetails.append("\n ");
        }

        TermList terms = fiveFiltersService.getTermList(combinedJobDetails.toString(), locale);
        return terms;
    }
}