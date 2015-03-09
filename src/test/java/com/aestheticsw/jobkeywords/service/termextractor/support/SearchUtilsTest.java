package com.aestheticsw.jobkeywords.service.termextractor.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.aestheticsw.jobkeywords.config.UnitTestCategory;

@Category(UnitTestCategory.class)
public class SearchUtilsTest {

    @Test
    public void locale() {

        assertNotNull(SearchUtils.lookupLocaleByCountry("US"));
        assertEquals("US", SearchUtils.lookupLocaleByCountry("US").getCountry());
        assertEquals("en", SearchUtils.lookupLocaleByCountry("US").getLanguage());

        assertNotNull(SearchUtils.lookupLocaleByCountry("FR"));
        assertEquals("FR", SearchUtils.lookupLocaleByCountry("FR").getCountry());
        assertEquals("fr", SearchUtils.lookupLocaleByCountry("FR").getLanguage());

        assertNotNull(SearchUtils.lookupLocaleByCountry("GB"));
        assertEquals("GB", SearchUtils.lookupLocaleByCountry("GB").getCountry());
        assertEquals("en", SearchUtils.lookupLocaleByCountry("GB").getLanguage());

        assertNotNull(SearchUtils.lookupLocaleByCountry("IE"));
        assertEquals("IE", SearchUtils.lookupLocaleByCountry("IE").getCountry());
        assertEquals("en", SearchUtils.lookupLocaleByCountry("IE").getLanguage());

        assertNotNull(SearchUtils.lookupLocaleByCountry("DE"));
        assertEquals("DE", SearchUtils.lookupLocaleByCountry("DE").getCountry());
        assertEquals("de", SearchUtils.lookupLocaleByCountry("DE").getLanguage());

        assertNotNull(SearchUtils.lookupLocaleByCountry("CH"));
        assertEquals("CH", SearchUtils.lookupLocaleByCountry("CH").getCountry());
        assertEquals("fr", SearchUtils.lookupLocaleByCountry("CH").getLanguage());

    }

}
