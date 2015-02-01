package com.aestheticsw.jobkeywords.domain.termfrequency;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class TermFrequency {
    private String term;
    private int frequency;
    private int wordCount;

    /**
     * This constructor is used in the data store to accumulate results. 
     * The frequency gets set independently via addFrequency() . 
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
     * Sort by descending term-frequency, word-count but ascending term-string
     * 
     * TODO: cache the CompareToBuilder and update in addFrequency()
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
