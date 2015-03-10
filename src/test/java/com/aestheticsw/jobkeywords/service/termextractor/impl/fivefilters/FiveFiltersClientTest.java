package com.aestheticsw.jobkeywords.service.termextractor.impl.fivefilters;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aestheticsw.jobkeywords.config.ServiceTestConfiguration;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyList;
import com.aestheticsw.jobkeywords.utils.FileUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ServiceTestConfiguration
public class FiveFiltersClientTest {

    @Autowired
    private FiveFiltersClient fiveFiltersClient;

    @Test
    public void appContext() {
        assertNotNull(fiveFiltersClient);
    }

    @Test
    public void termExtractorDeserialization() throws FileNotFoundException {
        String content = FileUtils.getClassResourceAsString("../simple-content.html", this);
        TermFrequencyList terms = fiveFiltersClient.getTermFrequencyList(content, Locale.US);

        assertNotNull(terms);
    }

    @Test
    public void realJobTerms() throws FileNotFoundException {
        String content = FileUtils.getClassResourceAsString("../indeed-content.html", this);
        TermFrequencyList terms = fiveFiltersClient.getTermFrequencyList(content, Locale.US);

        assertNotNull(terms);
    }

    // disabled because I can't find any way to break the content - yet.
    // @Test
    public void brokenJobTerms() throws FileNotFoundException {
        String content = FileUtils.getClassResourceAsString("../broken-content.html", this);
        try {
            TermFrequencyList terms = fiveFiltersClient.getTermFrequencyList(content, Locale.US);
            fail("Expected FiveFilters to throw exception");
        } catch (RuntimeException expected) {
            assertNotNull(expected);
            assertTrue(expected.getMessage().contains("invalid content"));
            assertTrue(expected.getMessage().contains("index="));
        }
    }
}
