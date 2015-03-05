package com.aestheticsw.jobkeywords.service.termextractor;

import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKeyList;
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyResults;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyList;
import com.aestheticsw.jobkeywords.service.termextractor.impl.indeed.IndeedQueryException;

public interface TermExtractorService {

    TermFrequencyList extractTerms(SearchParameters params) throws IndeedQueryException;

    TermFrequencyResults getAccumulatedTermFrequencyResults(QueryKey queryKey);

    QueryKeyList getSearchHistory();

}