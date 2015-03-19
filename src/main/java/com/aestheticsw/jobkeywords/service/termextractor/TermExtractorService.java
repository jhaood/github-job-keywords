package com.aestheticsw.jobkeywords.service.termextractor;

import java.util.List;

import com.aestheticsw.jobkeywords.service.termextractor.domain.JobSummary;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKeyList;
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyList;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyResults;
import com.aestheticsw.jobkeywords.service.termextractor.impl.indeed.IndeedQueryException;

/**
 * This is the public interface for the web controllers to hit the service. 
 * 
 * @see com.aestheticsw.jobkeywords.service.termextractor.repository.TermFrequencyResultsDataManager
 *
 * @author Jim Alexander (jhaood@gmail.com)
 */
public interface TermExtractorService {

    TermFrequencyList extractTerms(SearchParameters params) throws IndeedQueryException;

    TermFrequencyResults getAccumulatedTermFrequencyResults(QueryKey queryKey);

    QueryKeyList getSearchHistory();

    List<JobSummary> getIndeedJobSummaryList(SearchParameters params);

}