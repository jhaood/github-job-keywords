package com.aestheticsw.jobkeywords.service.termextractor.fivefilters;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.aestheticsw.jobkeywords.config.TestBase;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermList;
import com.aestheticsw.jobkeywords.utils.FileUtils;

public class FiveFiltersServiceTest extends TestBase {

    @Autowired
    private FiveFiltersService service;

    @Test
    public void appContext() {
        assertNotNull(service);
    }

    @Test
    public void termExtractorDeserialization() throws FileNotFoundException {
        String content = FileUtils.getClassResourceAsString("../simple-content.html", this);
        TermList terms = service.getTermList(content, Locale.US);

        assertNotNull(terms);
    }

    @Test
    public void realJobTerms() throws FileNotFoundException {
        String content = FileUtils.getClassResourceAsString("../indeed-content.html", this);
        TermList terms = service.getTermList(content, Locale.US);

        assertNotNull(terms);
    }

    // disabled because I can't find any way to break the content - yet.
    // @Test
    public void brokenJobTerms() throws FileNotFoundException {
        String content = FileUtils.getClassResourceAsString("../broken-content.html", this);
        try {
            TermList terms = service.getTermList(content, Locale.US);
            fail("Expected FiveFilters to throw exception");
        } catch (RuntimeException expected) {
            assertNotNull(expected);
            assertTrue(expected.getMessage().contains("invalid content"));
            assertTrue(expected.getMessage().contains("index="));
        }
    }
}
