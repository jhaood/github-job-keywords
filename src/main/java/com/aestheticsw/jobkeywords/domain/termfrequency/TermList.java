package com.aestheticsw.jobkeywords.domain.termfrequency;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * TermList is only used for deserialization from the TermExtractorService. This class is not
 * persistent.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
//TODO change NON_EMPTY because it should be Include.EMPTY
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TermList {
    private List<TermFrequency> terms;

    public TermList(List<TermFrequency> terms) {
        if (terms == null) {
            terms = new ArrayList<>();
        }
        this.terms = terms;
    }

    public TermList sort(Comparator<TermFrequency> comparator) {
        terms.sort(comparator);
        return this;
    }

    /**
     * This method makes it easy to generate a blacklist regular expression. The blacklist is saved
     * by the FiveFiltersService.
     */
    public String createRegExpForNewTerms(String existingRegEx) {
        StringBuilder builder = new StringBuilder();
        for (Iterator<TermFrequency> termIter = terms.iterator(); termIter.hasNext();) {
            TermFrequency term = termIter.next();
            if (existingRegEx.contains(term.getTerm())) {
                continue;
            }
            builder.append(term.getTerm());
            if (termIter.hasNext()) {
                builder.append("|");
            }
        }
        return builder.toString();
    }

    public boolean hasResults() {
        return (terms.size() > 0);
    }

    public List<TermFrequency> getTerms() {
        return terms;
    }
}
