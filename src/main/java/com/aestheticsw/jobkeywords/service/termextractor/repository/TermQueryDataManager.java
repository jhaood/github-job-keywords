package com.aestheticsw.jobkeywords.service.termextractor.repository;

import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aestheticsw.jobkeywords.domain.termfrequency.QueryKey;
import com.aestheticsw.jobkeywords.domain.termfrequency.SearchParameters;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermFrequency;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermFrequencyResults;

/**
 * The TermQueryDataManager maintains the database of results.
 * <p/>
 * 
 * The TermQueryDataManager class maintains a Map that differentiates results based on the unique
 * search parameters. A unique search is for a given query-expression and location.
 * <p/>
 * 
 * The need to accumulate results across multiple searches comes from a limitation of Indeed.com.
 * Indeed.com will return a maximum of 25 jobs at a time. The user can query multiple times in order
 * to accumulate results across 50, 100 or 200 job descriptions.
 * <p/>
 * 
 * Please see the TermFrequencyResults class for details about how searches are accumulated.
 * <p/>
 * 
 * @see TermFrequencyResults
 * @author Jim Alexander (jhaood@gmail.com)
 */
@Component
@Transactional
// TODO rename to TermFrequencyResultsDataManager
public class TermQueryDataManager {

    // public access for tests to inject or mock a logger.
    @Log
    public Logger log;

    private QueryKeyRepository queryKeyRepository;
    
    private TermQueryRepository termQueryRepository;

    @Autowired
    public TermQueryDataManager(QueryKeyRepository queryKeyRepository, TermQueryRepository termQueryRepository) {
        super();
        this.queryKeyRepository = queryKeyRepository;
        this.termQueryRepository = termQueryRepository;
    }

    public void accumulateTermFrequencyResults(SearchParameters searchParameters, List<TermFrequency> termFrequencyList) {
        if (termFrequencyList.size() == 0) {
            log.info("Ignoring search with ZERO results: " + searchParameters.toString());
            return;
        }
        QueryKey queryKey = searchParameters.getQueryKey();

        // TODO this isn't right... use compound primary key for QueryKey
        QueryKey dbQueryKey = queryKeyRepository.findByCompoundKey(queryKey);
        if (dbQueryKey == null) {
            dbQueryKey = queryKeyRepository.save(queryKey);
        }
        
        TermFrequencyResults dbTermFrequencyResults;
        synchronized (termQueryRepository) {
            dbTermFrequencyResults = termQueryRepository.findByQueryKey(dbQueryKey);
            if (dbTermFrequencyResults == null) {
                dbTermFrequencyResults = new TermFrequencyResults(dbQueryKey);
                dbTermFrequencyResults = termQueryRepository.save(dbTermFrequencyResults);
            }
        }
        dbTermFrequencyResults.accumulateTermFrequencyList(searchParameters, termFrequencyList);
    }

    public TermFrequencyResults getAccumulatedResults(QueryKey queryKey) {
        // TODO this isn't right... have to use compound primary key for QueryKey
        QueryKey dbQueryKey = queryKeyRepository.findByCompoundKey(queryKey);
        if (dbQueryKey == null) {
            dbQueryKey = queryKeyRepository.save(queryKey);
        }
        TermFrequencyResults results = termQueryRepository.findByQueryKey(dbQueryKey);

        if (results == null) {
            results = new TermFrequencyResults(dbQueryKey);
            termQueryRepository.save(results);
        }
        return results;
    }

    public List<QueryKey> getSearchHistory() {
        // Set<QueryKey> queryKeys = termFrequencyResultsMap.keySet();
        List<QueryKey> queryKeys = termQueryRepository.findDistinctQueryKeys();
        Collections.sort(queryKeys, new QueryKey.QueryKeyComparator());
        return queryKeys;
    }
}
