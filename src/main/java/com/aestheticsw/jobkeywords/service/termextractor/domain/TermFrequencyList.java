/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.service.termextractor.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * TermFrequencyList is only used for deserialization from the TermExtractorServiceImpl.
 * 
 * This class is not persistent.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class TermFrequencyList {
    private List<TermFrequency> terms;

    public TermFrequencyList(List<TermFrequency> terms) {
        if (terms == null) {
            terms = new ArrayList<>();
        }
        this.terms = terms;
    }

    public TermFrequencyList sort(Comparator<TermFrequency> comparator) {
        terms.sort(comparator);
        return this;
    }

    /**
     * This method makes it easy to generate a blacklist regular expression. The blacklist is saved
     * by the FiveFiltersClient.
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
