package com.aestheticsw.jobkeywords.domain.termfrequency;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aestheticsw.jobkeywords.database.TermQueryRepository;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * TermFrequencyResults holds the results for a unique query and location. The identifying key for a
 * TermFrequencyResult instance is a QueryKey.<p/>
 * 
 * The results may be accumiulated across multiple
 * searches where the unique key for a given TermFrequenceResult is a QueryKey.<p/>
 * 
 * This class also holds
 * a list of SearchParameters in case the results have been accumulated across multiple searches.
 * 
 * @see TermQueryRepository
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TermFrequencyResults {

    private static Logger log = LoggerFactory.getLogger(TermFrequencyResults.class);

    /**
     * The QueryKey that identifies the unique query-string and location that can be accumulated
     * into a given TermFrequencyResults instance.
     */
    private QueryKey queryKey;

    private List<SearchParameters> searchParametersList = new ArrayList<>();

    private Map<String, TermFrequency> termFrequencyMap = new HashMap<>();

    public TermFrequencyResults(QueryKey queryKey) {
        this.queryKey = queryKey;
    }

    public void accumulateTermFrequencyList(SearchParameters searchParameters, List<TermFrequency> termFrequencyList) {
        if (!queryKey.equals(searchParameters.getQueryKey())) {
            throw new IllegalArgumentException("Attempt to add terms for inappropriate SearchParameters: '"
                + searchParameters.toString() + "', results-QueryKey: '" + queryKey.toString() + "'");
        }
        synchronized (searchParametersList) {
            if (searchParametersList.contains(searchParameters)) {
                log.warn("Already accumulated results for search-params: " + searchParameters);
                return;
            }
            searchParametersList.add(searchParameters);
        }
        synchronized (termFrequencyMap) {
            for (TermFrequency termFrequency : termFrequencyList) {
                TermFrequency storedTermFrequency = termFrequencyMap.get(termFrequency.getTerm());
                if (storedTermFrequency == null) {
                    storedTermFrequency = new TermFrequency(termFrequency.getTerm(), termFrequency.getWordCount());
                    termFrequencyMap.put(storedTermFrequency.getTerm(), storedTermFrequency);
                }
                storedTermFrequency.addFrequency(termFrequency.getFrequency());
            }
        }
    }

    public boolean hasResults() {
        return (termFrequencyMap.size() > 0);
    }

    public List<TermFrequency> getSortedTermFrequencyList() {
        return getSortedTermFrequencyList(new TermFrequency.FrequencyComparator());
    }

    public List<TermFrequency> getSortedTermFrequencyList(Comparator comparator) {
        List<TermFrequency> list = new ArrayList<>(termFrequencyMap.values());
        list.sort(comparator);
        return list;
    }

    QueryKey getQueryKey() {
        return queryKey;
    }

    List<SearchParameters> getSearchParametersList() {
        return searchParametersList;
    }
}
