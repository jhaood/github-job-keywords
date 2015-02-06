package com.aestheticsw.jobkeywords.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.aestheticsw.jobkeywords.domain.termfrequency.QueryKey;
import com.aestheticsw.jobkeywords.domain.termfrequency.QueryList;
import com.aestheticsw.jobkeywords.domain.termfrequency.SearchParameters;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermFrequency;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermFrequencyResults;

/**
 * The TermQueryResponsitory keeps an in-memory data store of the results that
 * have been accumulated since the server began running. <p/>
 * 
 * The TermQueryRepository class maintains a Map that differentiates results based
 * on the unique search parameters. A unique search is for a given query-expression and location. <p/>
 * 
 * The need to accumulate results across multiple searches comes from a limitation of Indeed.com.
 * Indeed.com will return a maximum of 25 jobs at a time. The user can query multiple times in
 * order to accumulate results across 50, 100 or 200 job descriptions. <p/>
 * 
 * Please see the TermFrequencyResults class for details about how searches are accumulated. <p/>
 * 
 * TODO Add JPA configure MySQL.
 * 
 * @see TermFrequencyResults
 * @author Jim Alexander (jhaood@gmail.com)
 */
@Component
public class TermQueryRepository {

    // public access for tests to inject or mock a logger.
    @Log
    public Logger log;

    private Map<QueryKey, TermFrequencyResults> termFrequencyResultsMap = new HashMap<>();

    public void accumulateTermFrequencyResults(SearchParameters searchParameters, List<TermFrequency> termFrequencyList) {
        if (termFrequencyList.size() == 0) {
            log.info("Ignoring search with ZERO results: " + searchParameters.toString());
            return;
        }
        QueryKey queryKey = searchParameters.getQueryKey();
        TermFrequencyResults termFrequencyResults;
        synchronized (termFrequencyResultsMap) {
            termFrequencyResults = termFrequencyResultsMap.get(queryKey);
            if (termFrequencyResults == null) {
                termFrequencyResults = new TermFrequencyResults(queryKey);
                termFrequencyResultsMap.put(queryKey, termFrequencyResults);
            }
        }
        termFrequencyResults.accumulateTermFrequencyList(searchParameters, termFrequencyList);
    }

    public TermFrequencyResults getAccumulatedResults(QueryKey queryKey) {
        TermFrequencyResults results = termFrequencyResultsMap.get(queryKey);

        if (results == null) {
            for (QueryKey key : termFrequencyResultsMap.keySet()) {
                log.debug("equals: " + key.equals(queryKey) + ", key: " + key + "MVC queryKey: " + queryKey);
            }
            return new TermFrequencyResults(queryKey);
        }
        return results;
    }

    public QueryList getSearchHistory() {
        Set<QueryKey> queryKeys = termFrequencyResultsMap.keySet();
        return new QueryList(queryKeys, new QueryKey.QueryKeyComparator());
    }
}
