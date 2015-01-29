package com.aestheticsw.jobkeywords.domain.termfrequency;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TermFrequencyResults {

    private static Logger log = LoggerFactory.getLogger(TermFrequencyResults.class);

    private List<SearchParameters> searchParametersList = new ArrayList<>();

    private Map<String, TermFrequency> termFrequencyMap = new HashMap<>();

    public void accumulateTermFrequencyList(SearchParameters searchParameters, List<TermFrequency> termFrequencyList) {
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

    public TermFrequency getForSearchParameters(SearchParameters searchParameters) {
        return termFrequencyMap.get(searchParameters);
    }
    
    public boolean hasResults() {
        return (termFrequencyMap.size() > 0);
    }

    public List<SearchParameters> getSearchParametersList() {
        return searchParametersList;
    }

    public List<TermFrequency> getSortedList() {
        return getSortedList(new TermFrequency.FrequencyComparator());
    }

    public List<TermFrequency> getSortedList(Comparator comparator) {
        List<TermFrequency> list = new ArrayList<>(termFrequencyMap.values());
        list.sort(comparator);
        return list;
    }
}
