package com.aestheticsw.jobkeywords.service.termextractor.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Locale;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.aestheticsw.jobkeywords.config.UnitTestCategory;

@Category(UnitTestCategory.class)
public class SearchParametersTest {

    @Test
    public void constructor() {
        QueryKey aKey = new QueryKey("query", Locale.FRANCE, "Lyon");
        SearchParameters param1 = new SearchParameters(aKey, 20, 1, 0, "date");

        assertEquals("query", param1.getQueryKey().getQuery());
        assertEquals(20, param1.getJobCount());
        assertEquals(1, param1.getStart());
        assertEquals(Locale.FRANCE, param1.getQueryKey().getLocale());
        // note that Lyon should have been converted to lower case
        assertEquals("lyon", param1.getQueryKey().getCity());
        assertEquals(0, param1.getRadius());
        assertEquals("date", param1.getSort());
    }

    @Test
    public void equals() {
        QueryKey key1 = new QueryKey("query", Locale.FRANCE, "Lyon");
        SearchParameters param1 = new SearchParameters(key1, 20, 1, 0, "date");
        SearchParameters param2 = new SearchParameters(key1, 20, 1, 0, "date");
        assertEquals(param1, param2);
        assertNotEquals(param1, null);
        assertNotEquals(null, param1);
        assertEquals(param1, param1);

        QueryKey key2 = new QueryKey("uery", Locale.FRANCE, "Lyon");
        SearchParameters param3 = new SearchParameters(key2, 20, 1, 0, "date");
        assertNotEquals(param1, param3);
        SearchParameters param4 = new SearchParameters(key1, 21, 1, 0, "date");
        assertNotEquals(param1, param4);
        SearchParameters param5 = new SearchParameters(key1, 20, 0, 0, "date");
        assertNotEquals(param1, param5);
        QueryKey key3 = new QueryKey("query", Locale.US, "Lyon");
        SearchParameters param6 = new SearchParameters(key3, 20, 1, 0, "date");
        assertNotEquals(param1, param6);
        QueryKey key4 = new QueryKey("query", Locale.FRANCE, "Lyo");
        SearchParameters param7 = new SearchParameters(key4, 20, 1, 0, "date");
        assertNotEquals(param1, param7);
        SearchParameters param8 = new SearchParameters(key1, 20, 1, 1, "date");
        assertNotEquals(param1, param8);
        SearchParameters param9 = new SearchParameters(key1, 20, 1, 0, "datee");
        assertNotEquals(param1, param9);
    }

    @Test
    public void hashcode() {
        QueryKey key1 = new QueryKey("query", Locale.FRANCE, "Lyon");
        SearchParameters param1 = new SearchParameters(key1, 20, 1, 0, "date");
        SearchParameters param2 = new SearchParameters(key1, 20, 1, 0, "date");
        assertEquals(param1.hashCode(), param2.hashCode());
        assertNotEquals(param1.hashCode(), 0);
        assertNotEquals(0, param1.hashCode());
        assertEquals(param1.hashCode(), param1.hashCode());

        QueryKey key2 = new QueryKey("uery", Locale.FRANCE, "Lyon");
        SearchParameters param3 = new SearchParameters(key2, 20, 1, 0, "date");
        assertNotEquals(param1.hashCode(), param3.hashCode());
        SearchParameters param4 = new SearchParameters(key1, 21, 1, 0, "date");
        assertNotEquals(param1.hashCode(), param4.hashCode());
        SearchParameters param5 = new SearchParameters(key1, 20, 0, 0, "date");
        assertNotEquals(param1.hashCode(), param5.hashCode());
        QueryKey key3 = new QueryKey("query", Locale.US, "Lyon");
        SearchParameters param6 = new SearchParameters(key3, 20, 1, 0, "date");
        assertNotEquals(param1.hashCode(), param6.hashCode());
        QueryKey key4 = new QueryKey("query", Locale.FRANCE, "Lyo");
        SearchParameters param7 = new SearchParameters(key4, 20, 1, 0, "date");
        assertNotEquals(param1.hashCode(), param7.hashCode());
        SearchParameters param8 = new SearchParameters(key1, 20, 1, 1, "date");
        assertNotEquals(param1.hashCode(), param8.hashCode());
        SearchParameters param9 = new SearchParameters(key1, 20, 1, 0, "datee");
        assertNotEquals(param1.hashCode(), param9.hashCode());
    }
}
