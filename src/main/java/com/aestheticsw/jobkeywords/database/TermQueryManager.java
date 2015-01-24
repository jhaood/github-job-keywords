package com.aestheticsw.jobkeywords.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.aestheticsw.jobkeywords.domain.termfrequency.QueryKey;
import com.aestheticsw.jobkeywords.domain.termfrequency.SearchParameters;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermFrequency;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermFrequencyResults;

/**
 * <pre>
 * TermQueryManager - manages the accumulation of search results. 
 *              For each unique query-key, holds 1 TermFrequencyResults object
 *              A "unique query-key is a subset of the full search parameters that uniquely identifies
 *              a TermFrequencyResults that will accumulate results from subsequent searches.  
 *    Map<QueryKey, TermFrequencyResults>
 *    
 * TermFrequencyResults - Contains a Map and a List. 
 *              The Map of term-name to TermFrequency instance for fast lookup 
 *              The List saves the search parameters for each search that have been accumulated 
 *              into this set of results.  
 *    Map<term-name, TermFrequency>
 *    List<SearchParameters>
 * </pre>
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@Component
public class TermQueryManager {

    @Log
    private Logger log;

    private Map<QueryKey, TermFrequencyResults> termFrequencyResultsMap = new HashMap<>();

    public void
            accumulateTermFrequencyResults(SearchParameters searchParameters, List<TermFrequency> termFrequencyList) {
        QueryKey queryKey = searchParameters.getQueryKey();
        TermFrequencyResults termFrequencyResults;
        synchronized (termFrequencyResultsMap) {
            termFrequencyResults = termFrequencyResultsMap.get(queryKey);
            if (termFrequencyResults == null) {
                termFrequencyResults = new TermFrequencyResults();
                termFrequencyResultsMap.put(queryKey, termFrequencyResults);
            }
        }
        termFrequencyResults.accumulateTermFrequencyList(searchParameters, termFrequencyList);
    }

    public TermFrequencyResults getAccumulatedResults(SearchParameters searchParameters) {
        QueryKey queryKey = searchParameters.getQueryKey();
        return termFrequencyResultsMap.get(queryKey);
    }
}
