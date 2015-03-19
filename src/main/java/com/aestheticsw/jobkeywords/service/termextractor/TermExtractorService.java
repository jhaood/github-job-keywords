/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
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
 * This is the public Java API for the Spring-MVC @Controllers. 
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