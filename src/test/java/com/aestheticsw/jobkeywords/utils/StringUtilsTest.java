package com.aestheticsw.jobkeywords.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class StringUtilsTest {
    
    @Test
    public void locale() {
        
        assertNotNull(StringUtils.lookupLocaleByCountry("US"));
        assertEquals("US", StringUtils.lookupLocaleByCountry("US").getCountry());
        assertEquals("en", StringUtils.lookupLocaleByCountry("US").getLanguage());

        assertNotNull(StringUtils.lookupLocaleByCountry("FR"));
        assertEquals("FR", StringUtils.lookupLocaleByCountry("FR").getCountry());
        assertEquals("fr", StringUtils.lookupLocaleByCountry("FR").getLanguage());

        assertNotNull(StringUtils.lookupLocaleByCountry("GB"));
        assertEquals("GB", StringUtils.lookupLocaleByCountry("GB").getCountry());
        assertEquals("en", StringUtils.lookupLocaleByCountry("GB").getLanguage());

        assertNotNull(StringUtils.lookupLocaleByCountry("IE"));
        assertEquals("IE", StringUtils.lookupLocaleByCountry("IE").getCountry());
        assertEquals("en", StringUtils.lookupLocaleByCountry("IE").getLanguage());

        assertNotNull(StringUtils.lookupLocaleByCountry("DE"));
        assertEquals("DE", StringUtils.lookupLocaleByCountry("DE").getCountry());
        assertEquals("de", StringUtils.lookupLocaleByCountry("DE").getLanguage());

        assertNotNull(StringUtils.lookupLocaleByCountry("CH"));
        assertEquals("CH", StringUtils.lookupLocaleByCountry("CH").getCountry());
        assertEquals("fr", StringUtils.lookupLocaleByCountry("CH").getLanguage());

    }

}
