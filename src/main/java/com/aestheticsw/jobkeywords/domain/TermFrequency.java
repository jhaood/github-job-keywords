package com.aestheticsw.jobkeywords.domain;

import java.util.Comparator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * The TermFrequency class holds a single term returned from the keyword extractor and holds the
 * Frequency and word-count for the term.
 * <p/>
 * 
 * This class also defines a few Comparators that can sort in different ways. The typical sort
 * that's most interesting is ordered by descending frequency. Other Comparators allow sorting by
 * term-string and the "complexity" of a term (a multi-word term is more complex than a single word)
 * <p/>
 * 
 * This class is not immutable because it is used by the TermFrequencyResultsDataManager to add up the
 * frequency of occurrences across multiple searches for the same QueryKey.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@Entity
public class TermFrequency {

    @Id
    @GeneratedValue
    private Long id;

    // @Column(name = "TERM_FREQUENCY_RESULTS_ID")
    // private Long termFrequencyResultsId;

    private String term;
    private int frequency;
    private int wordCount;

    @SuppressWarnings("unused")
    private TermFrequency() {
        super();
    }

    /**
     * This constructor is used in the data repository to accumulate results. The frequency gets set
     * independently via addFrequency() .
     */
    public TermFrequency(String term, int wordCount) {
        this.term = term;
        this.wordCount = wordCount;
        this.frequency = 0;
    }

    public TermFrequency(String[] innerArray) {
        if (innerArray == null || innerArray.length != 3) {
            throw new RuntimeException("Invalid FiveFilters response structure: " + innerArray);
        }
        term = innerArray[0];
        frequency = Integer.parseInt(innerArray[1]);
        wordCount = Integer.parseInt(innerArray[2]);
    }

    @Override
    public String toString() {
        return super.toString() + " term: " + term + ", freq: " + frequency;
    }

    public String getTerm() {
        return term;
    }

    public int getFrequency() {
        return frequency;
    }

    public int addFrequency(int count) {
        frequency += count;
        return frequency;
    }

    public int getWordCount() {
        return wordCount;
    }

    /**
     * Sort by descending term-frequency, descending word-count and ascending term-string
     */
    public static class FrequencyComparator implements Comparator<TermFrequency> {
        @Override
        public int compare(TermFrequency term1, TermFrequency term2) {
            // compute for descending frequency order
            CompareToBuilder builder = new CompareToBuilder();
            builder.append(term2.frequency, term1.frequency);

            // compute for descending word-count order
            builder.append(term2.wordCount, term1.wordCount);

            // and finally sort by the term, alphabetically, ascending.
            builder.append(term1.term, term2.term);
            return builder.toComparison();
        }
    }

    /**
     * Sort by ascending term-name
     */
    public static class TermAlphaComparator implements Comparator<TermFrequency> {
        @Override
        public int compare(TermFrequency term1, TermFrequency term2) {
            return term1.getTerm().compareTo(term2.getTerm());
        }
    }

    /**
     * Sort by descending word-count, then ascending term-name
     */
    public static class TermComplexityComparator implements Comparator<TermFrequency> {
        @Override
        public int compare(TermFrequency term1, TermFrequency term2) {
            CompareToBuilder builder = new CompareToBuilder();
            builder.append(term2.wordCount, term1.wordCount);
            builder.append(term1.term, term2.term);
            return builder.toComparison();
        }
    }

}
