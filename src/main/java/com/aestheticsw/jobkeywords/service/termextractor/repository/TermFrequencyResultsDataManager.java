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
 * The TermFrequencyResultsDataManager class maintains a Map that differentiates results based on
 * the unique search parameters. A unique search is for a given query-expression and location.
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

    private SearchParametersRepository searchParametersRepository;

    private TermFrequencyResultsRepository termFrequencyResultsRepository;

    @Autowired
    public TermFrequencyResultsDataManager(QueryKeyRepository queryKeyRepository,
            SearchParametersRepository searchParametersRepository,
            TermFrequencyResultsRepository termFrequencyResultsRepository) {
        super();
        this.queryKeyRepository = queryKeyRepository;
        this.searchParametersRepository = searchParametersRepository;
        this.termFrequencyResultsRepository = termFrequencyResultsRepository;
    }

    /**
     * SearchParameters.QueryKey might be persistent.
     * 
     * SearchParamters may be persistent, if it is persistent then QueryKey would also be
     * persistent.
     * 
     * <pre>
     * 
     * The separation between transient and persistent objects: 
     * - All classes outside the @Transactional Repositories and Managers are transient. 
     * POST / PUT: 
     * - UI -> creates domain objects as transient objects and passes through @Transactional interfaces
     * - @Transactional classes must lookup by column-values to pull persistent copies. 
     * - @Transactional classes follow the "unit of work" pattern.
     * 
     * GET:
     * - @Transactional classes return detatched persistent objects. 
     * - UI displays values from detatched persistent objects 
     * - UI never stores detatched persisent objects in Session... ! ! !
     * - UI never passes detatched persistent objects back to @Transactional layer.   
     * 
     * </pre>
     */
    public void accumulateTermFrequencyResults(SearchParameters searchParameters, List<TermFrequency> termFrequencyList) {
        if (termFrequencyList.size() == 0) {
            log.info("Ignoring search with ZERO results: " + searchParameters.toString());
            return;
        }
        QueryKey queryKey = searchParameters.getQueryKey();
        
        // pull existing entities from DB...

        SearchParameters dbSearchParameters = searchParametersRepository.findByCompoundKey(searchParameters);
        if (dbSearchParameters == null) {
            QueryKey dbQueryKey = queryKeyRepository.findByCompoundKey(queryKey);
            if (dbQueryKey == null) {
                dbQueryKey = queryKey;
            }
            dbSearchParameters =
                new SearchParameters(dbQueryKey, searchParameters.getJobCount(), searchParameters.getStart(),
                    searchParameters.getRadius(), searchParameters.getSort());
            dbSearchParameters = searchParametersRepository.save(dbSearchParameters);
        }
        
        // persist new or updated entities

        TermFrequencyResults dbTermFrequencyResults;
        synchronized (termFrequencyResultsRepository) {
            dbTermFrequencyResults = termFrequencyResultsRepository.findByQueryKey(dbSearchParameters.getQueryKey());
            if (dbTermFrequencyResults == null) {
                dbTermFrequencyResults = new TermFrequencyResults(dbSearchParameters.getQueryKey());
                dbTermFrequencyResults = termFrequencyResultsRepository.save(dbTermFrequencyResults);
            }
        }
        dbTermFrequencyResults.accumulateTermFrequencyList(dbSearchParameters, termFrequencyList);
    }

    public TermFrequencyResults getAccumulatedResults(QueryKey queryKey) {
        QueryKey dbQueryKey = queryKeyRepository.findByCompoundKey(queryKey);
        if (dbQueryKey == null) {
            return null;
        }
        TermFrequencyResults results = termFrequencyResultsRepository.findByQueryKey(dbQueryKey);
        return results;
    }

    public List<QueryKey> getSearchHistory() {
        // Set<QueryKey> queryKeys = termFrequencyResultsMap.keySet();
        List<QueryKey> queryKeys = termFrequencyResultsRepository.findDistinctQueryKeys();
        Collections.sort(queryKeys, new QueryKey.QueryKeyComparator());
        return queryKeys;
    }
}
