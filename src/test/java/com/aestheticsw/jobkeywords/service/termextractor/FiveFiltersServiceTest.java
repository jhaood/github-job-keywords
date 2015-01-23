package com.aestheticsw.jobkeywords.service.termextractor;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.aestheticsw.jobkeywords.config.TestBase;
import com.aestheticsw.jobkeywords.domain.fivefilters.TermList;
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
        String content = FileUtils.getResourceAsString("simple-content.html", this);
        TermList terms = service.getKeywords(content, Locale.US);
        
        assertNotNull(terms);
    }

    @Test
    public void realJobTerms() throws FileNotFoundException {
        String content = FileUtils.getResourceAsString("indeed-content.html", this);
        TermList terms = service.getKeywords(content, Locale.US);
        
        assertNotNull(terms);
    }
}
