package com.aestheticsw.jobkeywords.domain.termfrequency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Locale;

import org.junit.Test;

import com.aestheticsw.jobkeywords.domain.termfrequency.QueryKey;

public class QueryKeyTest {

    @Test
    public void constructor() {
        QueryKey param1 = new QueryKey("query", Locale.FRANCE, "Lyon");

        assertEquals("query", param1.getQuery());
        assertEquals(Locale.FRANCE, param1.getLocale());
        assertEquals("Lyon", param1.getCity());
    }

    @Test
    public void equals() {
        QueryKey param1 = new QueryKey("query", Locale.FRANCE, "Lyon");
        QueryKey param2 = new QueryKey("query", Locale.FRANCE, "Lyon");
        assertEquals(param1, param2);
        assertNotEquals(param1, null);
        assertNotEquals(null, param1);
        assertEquals(param1, param1);

        QueryKey param3 = new QueryKey("uery", Locale.FRANCE, "Lyon");
        assertNotEquals(param1, param3);
        QueryKey param6 = new QueryKey("query", Locale.US, "Lyon");
        assertNotEquals(param1, param6);
        QueryKey param7 = new QueryKey("query", Locale.FRANCE, "Lyo");
        assertNotEquals(param1, param7);
    }

    @Test
    public void hashcode() {
        QueryKey param1 = new QueryKey("query", Locale.FRANCE, "Lyon");
        QueryKey param2 = new QueryKey("query", Locale.FRANCE, "Lyon");
        assertEquals(param1.hashCode(), param2.hashCode());
        assertNotEquals(param1.hashCode(), 0);
        assertNotEquals(0, param1.hashCode());
        assertEquals(param1.hashCode(), param1.hashCode());

        QueryKey param3 = new QueryKey("uery", Locale.FRANCE, "Lyon");
        assertNotEquals(param1.hashCode(), param3.hashCode());
        QueryKey param6 = new QueryKey("query", Locale.US, "Lyon");
        assertNotEquals(param1.hashCode(), param6.hashCode());
        QueryKey param7 = new QueryKey("query", Locale.FRANCE, "Lyo");
        assertNotEquals(param1.hashCode(), param7.hashCode());
    }

}
