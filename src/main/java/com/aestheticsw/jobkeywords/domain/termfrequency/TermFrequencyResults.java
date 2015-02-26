package com.aestheticsw.jobkeywords.domain.termfrequency;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aestheticsw.jobkeywords.service.database.TermQueryDataManager;
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
 * @see TermQueryDataManager
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity
public class TermFrequencyResults {

    private static Logger log = LoggerFactory.getLogger(TermFrequencyResults.class);

    // TODO id-generator ? 
    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long id;
    
    /**
     * The QueryKey that identifies the unique query-string and location that can be accumulated
     * into a given TermFrequencyResults instance.
     */
    @ManyToOne(optional = false)
    // TODO What's @NaturalId about ? 
    // @NaturalId
    private QueryKey queryKey;

    // , mappedBy = "termFrequencyResultsId"
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="TERM_FREQUENCY_RESULTS_ID", referencedColumnName="ID")
    private List<SearchParameters> searchParametersList = new ArrayList<>();

    // , mappedBy = "termFrequencyResultsId"
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapKey(name = "term")
    @JoinColumn(name="TERM_FREQUENCY_RESULTS_ID", referencedColumnName="ID")
    private Map<String, TermFrequency> termFrequencyMap = new HashMap<>();

    @SuppressWarnings("unused")
    private TermFrequencyResults() {
        super();
    }

    public TermFrequencyResults(QueryKey queryKey) {
        this.queryKey = queryKey;
    }

    /**
     * This method should only be called by the TermQueryDataManager and testing code. 
     */
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
                TermFrequency existingTermFrequency = termFrequencyMap.get(termFrequency.getTerm());
                if (existingTermFrequency == null) {
                    existingTermFrequency = new TermFrequency(termFrequency.getTerm(), termFrequency.getWordCount());
                    termFrequencyMap.put(existingTermFrequency.getTerm(), existingTermFrequency);
                }
                existingTermFrequency.addFrequency(termFrequency.getFrequency());
            }
        }
    }

    public boolean hasResults() {
        return (termFrequencyMap.size() > 0);
    }

    public List<TermFrequency> getSortedTermFrequencyList() {
        return getSortedTermFrequencyList(new TermFrequency.FrequencyComparator());
    }

    public List<TermFrequency> getSortedTermFrequencyList(Comparator<? super TermFrequency> comparator) {
        List<TermFrequency> list = new ArrayList<>(termFrequencyMap.values());
        list.sort(comparator);
        return list;
    }

    public QueryKey getQueryKey() {
        return queryKey;
    }

    public List<SearchParameters> getSearchParametersList() {
        return searchParametersList;
    }
}
