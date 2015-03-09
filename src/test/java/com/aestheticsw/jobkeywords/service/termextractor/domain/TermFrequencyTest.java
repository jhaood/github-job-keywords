package com.aestheticsw.jobkeywords.service.termextractor.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.aestheticsw.jobkeywords.config.UnitTestCategory;

@Category(UnitTestCategory.class)
public class TermFrequencyTest {

    @Test
    public void constructor() {
        TermFrequency tf1 = new TermFrequency(new String[] { "one", "1", "2" });
        assertEquals("one", tf1.getTerm());
        assertEquals(1, tf1.getFrequency());
        assertEquals(2, tf1.getWordCount());
    }

    @Test(expected = RuntimeException.class)
    public void constructorFailure1() {
        TermFrequency tf1 = new TermFrequency(new String[] { "one", "1" });
    }

    @Test(expected = NumberFormatException.class)
    public void constructorFailure2() {
        TermFrequency tf1 = new TermFrequency(new String[] { "one", "one", "1" });
    }

    @Test(expected = NumberFormatException.class)
    public void constructorFailure3() {
        TermFrequency tf1 = new TermFrequency(new String[] { "one", "1", "two" });
    }

    @Test
    public void frequencyComparator() {
        Comparator<TermFrequency> comp = new TermFrequency.FrequencyComparator();
        TermFrequency tf1 = new TermFrequency(new String[] { "a", "2", "1" });
        TermFrequency tf1_1 = new TermFrequency(new String[] { "a", "2", "1" });
        assertEquals(0, comp.compare(tf1, tf1_1));

        TermFrequency tf2 = new TermFrequency(new String[] { "a", "1", "2" });
        // order by descending term-frequency
        assertTrue(comp.compare(tf1, tf2) < 0);

        TermFrequency tf3 = new TermFrequency(new String[] { "a", "2", "2" });
        // order by descending word-count
        assertTrue(comp.compare(tf1, tf3) > 0);

        TermFrequency tf4 = new TermFrequency(new String[] { "b", "1", "2" });
        // order by ascending term-string alphanumerics
        assertTrue(comp.compare(tf2, tf4) < 0);

    }

    @Test
    public void alphaComparator() {
        Comparator<TermFrequency> comp = new TermFrequency.TermAlphaComparator();
        TermFrequency tf1 = new TermFrequency(new String[] { "a", "2", "1" });
        TermFrequency tf1_1 = new TermFrequency(new String[] { "a", "2", "1" });
        assertEquals(0, comp.compare(tf1, tf1_1));

        TermFrequency tf4 = new TermFrequency(new String[] { "b", "3", "2" });
        assertTrue(comp.compare(tf1, tf4) < 0);
    }

    @Test
    public void termComplexityComparator() {
        Comparator<TermFrequency> comp = new TermFrequency.TermComplexityComparator();
        TermFrequency tf1 = new TermFrequency(new String[] { "a", "2", "1" });
        TermFrequency tf1_1 = new TermFrequency(new String[] { "a", "2", "1" });
        assertEquals(0, comp.compare(tf1, tf1_1));

        TermFrequency tf2 = new TermFrequency(new String[] { "a", "1", "2" });
        // order by descending word-count
        assertTrue(comp.compare(tf1, tf2) > 0);

        TermFrequency tf3 = new TermFrequency(new String[] { "a", "3", "1" });
        // frequency shouldn't matter for complexity
        assertEquals(0, comp.compare(tf1, tf3));

        TermFrequency tf4 = new TermFrequency(new String[] { "b", "1", "2" });
        // order by ascending term-string alphanumeric
        assertTrue(comp.compare(tf2, tf4) < 0);

    }

}
