package com.aestheticsw.jobkeywords.service.termextractor.impl;

import java.util.List;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aestheticsw.jobkeywords.service.termextractor.TermExtractorService;
import com.aestheticsw.jobkeywords.service.termextractor.domain.JobSummary;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKeyList;
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyList;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyResults;
import com.aestheticsw.jobkeywords.service.termextractor.impl.fivefilters.FiveFiltersClient;
import com.aestheticsw.jobkeywords.service.termextractor.impl.indeed.IndeedClient;
import com.aestheticsw.jobkeywords.service.termextractor.impl.indeed.IndeedQueryException;
import com.aestheticsw.jobkeywords.service.termextractor.repository.JobSummaryRepository;
import com.aestheticsw.jobkeywords.service.termextractor.repository.TermFrequencyResultsDataManager;

/**
 * The TermExtracorService is the primary public interface that wraps both the Indeed and
 * FiveFilters services.
 * <p/>
 * See the
 * {@link com.aestheticsw.jobkeywords.service.termextractor.repository.TermFrequencyResultsDataManager}
 * for a discussion of how transient and persistent objects are handled.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@Service
public class TermExtractorServiceImpl implements TermExtractorService {

    @Log
    public Logger log;

    private TermFrequencyResultsDataManager termFrequencyResultsDataManager;

    private JobSummaryRepository jobSummaryRepository;

    private IndeedClient indeedClient;

    private FiveFiltersClient fiveFiltersClient;

    @Autowired
    public TermExtractorServiceImpl(TermFrequencyResultsDataManager termFrequencyResultsDataManager,
            JobSummaryRepository jobSummaryRepository, IndeedClient indeedClient, FiveFiltersClient fiveFiltersClient) {
        this.termFrequencyResultsDataManager = termFrequencyResultsDataManager;
        this.jobSummaryRepository = jobSummaryRepository;
        this.indeedClient = indeedClient;
        this.fiveFiltersClient = fiveFiltersClient;
    }

    /**
     * @see com.aestheticsw.jobkeywords.service.termextractor.TermExtractorService#extractTerms(com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters)
     */
    @Override
    public TermFrequencyList extractTerms(SearchParameters params) throws IndeedQueryException {

        List<JobSummary> jobSummaries = getIndeedClient().getIndeedJobSummaryList(params);

        log.info("Indeed returned jobCount=" + ((jobSummaries != null) ? jobSummaries.size() : "0 (no results)"));
        if (jobSummaries == null || jobSummaries.size() == 0) {
            throw new IndeedQueryException("Query returned no results: " + params);
        }

        jobSummaryRepository.save(jobSummaries);

        StringBuilder combinedJobDetails = new StringBuilder();
        for (int index = 0; index < params.getJobCount() && index < jobSummaries.size(); index++) {
            JobSummary job = jobSummaries.get(index);
            String jobDetails = getIndeedClient().getIndeedJobDetails(job.getUrl());
            combinedJobDetails.append(jobDetails);
            combinedJobDetails.append("\n ");
        }

        TermFrequencyList terms =
            getFiveFiltersService().getTermFrequencyList(combinedJobDetails.toString(),
                params.getQueryKey().getLocale());

        getTermFrequencyResultsDataManager().accumulateTermFrequencyResults(params, terms.getTerms());

        return terms;
    }

    /**
     * @see com.aestheticsw.jobkeywords.service.termextractor.TermExtractorService#getAccumulatedTermFrequencyResults(com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey)
     */
    @Override
    public TermFrequencyResults getAccumulatedTermFrequencyResults(QueryKey queryKey) {
        return getTermFrequencyResultsDataManager().getAccumulatedResults(queryKey);
    }

    /**
     * @see com.aestheticsw.jobkeywords.service.termextractor.TermExtractorService#getSearchHistory()
     */
    // TODO return List<QueryKey> - but would removing the intermediate object break the REST serializer ? 
    @Override
    public QueryKeyList getSearchHistory() {
        return new QueryKeyList(getTermFrequencyResultsDataManager().getSearchHistory());
    }

    /**
     * @see com.aestheticsw.jobkeywords.service.termextractor.TermExtractorService#getIndeedJobSummaryList(SearchParameters)
     */
    @Override
    public List<JobSummary> getIndeedJobSummaryList(SearchParameters params) {
        return indeedClient.getIndeedJobSummaryList(params);
    }

    /**
     * Always use this getter because Spring must proxy the class to manage transactions.
     */
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
