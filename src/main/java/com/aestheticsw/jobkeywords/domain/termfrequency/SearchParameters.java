package com.aestheticsw.jobkeywords.domain.termfrequency;

import java.util.Locale;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * SearchParameter holds the full set of parameters for a given search. <p/>
 * 
 * This class is distinct from
 * the QueryKey class because Indeed will only return a maxiumum of 25 jobs per REST call. The UI
 * allows
 * the user to search multiple times for the same QueryKey values in order to span more than 25
 * jobs. <p/>
 * 
 * SearchParameters is an immutable class that can be used as an index in a Map.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 * @see QueryKey
 */
public class SearchParameters {
    private String query;
    private int jobCount;
    private int start;
    private Locale locale;
    private String city;
    private int radius;
    private String sort;

    private QueryKey queryKeyCache;

    private HashCodeBuilder hashCodeBuilder;
    private String toStringCache;

    public SearchParameters(String query, int jobCount, int start, Locale locale, String city, int radius, String sort) {
        super();
        // force query to lower case (query can't be null so don't test for null)
        this.query = query.toLowerCase();
        this.jobCount = jobCount;
        this.start = start;
        this.locale = locale;
        // force city to lower case
        if (city != null) {
            city = city.toLowerCase();
        }
        this.city = city;
        this.radius = radius;
        this.sort = sort;

        queryKeyCache = new QueryKey(query, locale, city);
        hashCodeBuilder =
            new HashCodeBuilder(17, 37).append(query).append(jobCount).append(start).append(locale).append(city)
                .append(radius).append(sort);

        StringBuilder builder = new StringBuilder("Search-params: ");
        builder.append(locale == null ? "" : locale + ", ");
        builder.append(city == null ? "" : city + ", ");
        builder.append("start: " + start + ", ");
        builder.append("count: " + jobCount + ", query:'");
        builder.append(query == null ? "'" : query + "', ");
        builder.append(radius == 0 ? "" : "radius: " + radius + ", ");
        builder.append(sort == null ? "" : "sort: " + sort + ", ");
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
        SearchParameters otherSearchParameters = (SearchParameters) other;

        // TODO cache this EqualsBuilder for performance reasons. ! ! !
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(query, otherSearchParameters.query).append(jobCount, otherSearchParameters.jobCount)
            .append(start, otherSearchParameters.start).append(locale, otherSearchParameters.locale)
            .append(city, otherSearchParameters.city).append(radius, otherSearchParameters.radius)
            .append(sort, otherSearchParameters.sort);
        return builder.isEquals();
    }

    @Override
    public String toString() {
        return toStringCache;
    }

    public QueryKey getQueryKey() {
        return queryKeyCache;
    }

    public String getQuery() {
        return query;
    }

    public int getJobCount() {
        return jobCount;
    }

    public int getStart() {
        return start;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getCity() {
        return city;
    }

    public int getRadius() {
        return radius;
    }

    public String getSort() {
        return sort;
    }
}
