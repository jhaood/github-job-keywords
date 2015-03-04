package com.aestheticsw.jobkeywords.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Locale;

import org.junit.Test;

import com.aestheticsw.jobkeywords.domain.SearchParameters;

public class SearchParametersTest {

    @Test
    public void constructor() {
        SearchParameters param1 = new SearchParameters("query", 20, 1, Locale.FRANCE, "Lyon", 0, "date");

        assertEquals("query", param1.getQuery());
        assertEquals(20, param1.getJobCount());
        assertEquals(1, param1.getStart());
        assertEquals(Locale.FRANCE, param1.getLocale());
        // note that Lyon should have been converted to lower case
        assertEquals("lyon", param1.getCity());
        assertEquals(0, param1.getRadius());
        assertEquals("date", param1.getSort());
    }

    @Test
    public void equals() {
        SearchParameters param1 = new SearchParameters("query", 20, 1, Locale.FRANCE, "Lyon", 0, "date");
        SearchParameters param2 = new SearchParameters("query", 20, 1, Locale.FRANCE, "Lyon", 0, "date");
        assertEquals(param1, param2);
        assertNotEquals(param1, null);
        assertNotEquals(null, param1);
        assertEquals(param1, param1);

        SearchParameters param3 = new SearchParameters("uery", 20, 1, Locale.FRANCE, "Lyon", 0, "date");
        assertNotEquals(param1, param3);
        SearchParameters param4 = new SearchParameters("query", 21, 1, Locale.FRANCE, "Lyon", 0, "date");
        assertNotEquals(param1, param4);
        SearchParameters param5 = new SearchParameters("query", 20, 0, Locale.FRANCE, "Lyon", 0, "date");
        assertNotEquals(param1, param5);
        SearchParameters param6 = new SearchParameters("query", 20, 1, Locale.US, "Lyon", 0, "date");
        assertNotEquals(param1, param6);
        SearchParameters param7 = new SearchParameters("query", 20, 1, Locale.FRANCE, "Lyo", 0, "date");
        assertNotEquals(param1, param7);
        SearchParameters param8 = new SearchParameters("query", 20, 1, Locale.FRANCE, "Lyon", 1, "date");
        assertNotEquals(param1, param8);
        SearchParameters param9 = new SearchParameters("query", 20, 1, Locale.FRANCE, "Lyon", 0, "datee");
        assertNotEquals(param1, param9);
    }

    @Test
    public void hashcode() {
        SearchParameters param1 = new SearchParameters("query", 20, 1, Locale.FRANCE, "Lyon", 0, "date");
        SearchParameters param2 = new SearchParameters("query", 20, 1, Locale.FRANCE, "Lyon", 0, "date");
        assertEquals(param1.hashCode(), param2.hashCode());
        assertNotEquals(param1.hashCode(), 0);
        assertNotEquals(0, param1.hashCode());
        assertEquals(param1.hashCode(), param1.hashCode());

        SearchParameters param3 = new SearchParameters("uery", 20, 1, Locale.FRANCE, "Lyon", 0, "date");
        assertNotEquals(param1.hashCode(), param3.hashCode());
        SearchParameters param4 = new SearchParameters("query", 21, 1, Locale.FRANCE, "Lyon", 0, "date");
        assertNotEquals(param1.hashCode(), param4.hashCode());
        SearchParameters param5 = new SearchParameters("query", 20, 0, Locale.FRANCE, "Lyon", 0, "date");
        assertNotEquals(param1.hashCode(), param5.hashCode());
        SearchParameters param6 = new SearchParameters("query", 20, 1, Locale.US, "Lyon", 0, "date");
        assertNotEquals(param1.hashCode(), param6.hashCode());
        SearchParameters param7 = new SearchParameters("query", 20, 1, Locale.FRANCE, "Lyo", 0, "date");
        assertNotEquals(param1.hashCode(), param7.hashCode());
        SearchParameters param8 = new SearchParameters("query", 20, 1, Locale.FRANCE, "Lyon", 1, "date");
        assertNotEquals(param1.hashCode(), param8.hashCode());
        SearchParameters param9 = new SearchParameters("query", 20, 1, Locale.FRANCE, "Lyon", 0, "datee");
        assertNotEquals(param1.hashCode(), param9.hashCode());
    }
}
