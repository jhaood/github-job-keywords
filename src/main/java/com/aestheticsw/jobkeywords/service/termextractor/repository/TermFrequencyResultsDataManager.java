package com.aestheticsw.jobkeywords.service.termextractor.repository;

import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequency;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyResults;

/**
 * The TermFrequencyResultsDataManager maintains the database of results.
 * <p/>
 * 
 * The TermFrequencyResultsDataManager class maintains a Map that differentiates results based on the unique
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
public class TermFrequencyResultsDataManager {

    // public access for tests to inject or mock a logger.
    @Log
    public Logger log;

    private QueryKeyRepository queryKeyRepository;
    
    private TermFrequencyResultsRepository termFrequencyResultsRepository;

    @Autowired
    public TermFrequencyResultsDataManager(QueryKeyRepository queryKeyRepository, TermFrequencyResultsRepository termFrequencyResultsRepository) {
        super();
        this.queryKeyRepository = queryKeyRepository;
        this.termFrequencyResultsRepository = termFrequencyResultsRepository;
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
        synchronized (termFrequencyResultsRepository) {
            dbTermFrequencyResults = termFrequencyResultsRepository.findByQueryKey(dbQueryKey);
            if (dbTermFrequencyResults == null) {
                dbTermFrequencyResults = new TermFrequencyResults(dbQueryKey);
                dbTermFrequencyResults = termFrequencyResultsRepository.save(dbTermFrequencyResults);
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
        TermFrequencyResults results = termFrequencyResultsRepository.findByQueryKey(dbQueryKey);

        if (results == null) {
            results = new TermFrequencyResults(dbQueryKey);
            termFrequencyResultsRepository.save(results);
        }
        return results;
    }

    public List<QueryKey> getSearchHistory() {
        // Set<QueryKey> queryKeys = termFrequencyResultsMap.keySet();
        List<QueryKey> queryKeys = termFrequencyResultsRepository.findDistinctQueryKeys();
        Collections.sort(queryKeys, new QueryKey.QueryKeyComparator());
        return queryKeys;
    }
}
