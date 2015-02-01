package com.aestheticsw.jobkeywords.domain.termfrequency;

import java.util.Comparator;
import java.util.Locale;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class QueryKey {

    private String query;
    private Locale locale;
    private String city;

    private HashCodeBuilder hashCodeBuilder;
    private String toStringCache;

    public QueryKey(String query, Locale locale, String city) {
        super();
        this.query = query;
        this.locale = locale;
        this.city = city;

        hashCodeBuilder = new HashCodeBuilder(17, 37).append(query).append(locale).append(city);

        StringBuilder builder = new StringBuilder("Search-params: ");
        builder.append(locale == null ? "" : locale + ", ");
        builder.append(city == null ? "" : city + ", ");
        builder.append(query == null ? "'" : query + "', ");
        toStringCache = builder.toString();
    }

    @Override
    public int hashCode() {
        return hashCodeBuilder.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != getClass()) {
            return false;
        }
        QueryKey otherSearchParameters = (QueryKey) other;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(query, otherSearchParameters.query).append(locale, otherSearchParameters.locale)
            .append(city, otherSearchParameters.city);
        return builder.isEquals();
    }

    @Override
    public String toString() {
        return toStringCache;
    }

    public String getQuery() {
        return query;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getCity() {
        return city;
    }

    public static class QueryKeyComparator implements Comparator<QueryKey> {

        @Override
        public int compare(QueryKey key1, QueryKey key2) {
            CompareToBuilder builder = new CompareToBuilder();
            builder.append(key1.query, key2.query);
            builder.append((key1.locale != null) ? key1.locale.getCountry() : null,
                (key2.locale != null) ? key2.locale.getCountry() : null);
            builder.append(key1.city, key2.city);
            return builder.build();
        }
    }

}
