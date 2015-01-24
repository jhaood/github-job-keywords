package com.aestheticsw.jobkeywords.domain.termfrequency;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class TermList {
    private List<TermFrequency> terms;

    public TermList(List<TermFrequency> terms) {
        super();
        this.terms = terms;
    }

    public TermList sort(Comparator<TermFrequency> comparator) {
        terms.sort(comparator);
        return this;
    }
    
    public String createRegExpForNewTerms(String existingRegEx) {
        StringBuilder builder = new StringBuilder();
        for (Iterator<TermFrequency> termIter = terms.iterator(); termIter.hasNext(); ) {
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

    public List<TermFrequency> getTerms() {
        return terms;
    }
}
