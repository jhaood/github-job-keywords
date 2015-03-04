package com.aestheticsw.jobkeywords.service.termextractor;

import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryList;
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyResults;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermList;
import com.aestheticsw.jobkeywords.service.termextractor.impl.indeed.IndeedQueryException;

public interface TermExtractorService {

    TermList extractTerms(SearchParameters params) throws IndeedQueryException;

    TermFrequencyResults getAccumulatedTermFrequencyResults(QueryKey queryKey);

    QueryList getSearchHistory();

}