package com.aestheticsw.jobkeywords.service.termextractor.domain;

import java.util.Comparator;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * QueryKey represents the unique parameters for a query expression and a location.
 * <p/>
 * 
 * This class is a subset of SearchParameters. This subset is needed because results from multiple
 * searches can be accumulated for a given query-string and location. Indeed will only return a max
 * of 25 jobs per search. So the application allows the user to search across several pages of
 * results in order to span more than 25 jobs.
 * <p/>
 * 
 * The QueryTermRepository accumulates results from multiple searches. The QueryKey is the index
 * into the repository's map of unique search expressions for a given city and country.
 * <p/>
 * 
 * QueryKey is an immutable class that can be used an an index in a Map.
 * <p/>
 * 
 * This class also defines a public Comparator that allow the UI to sort a list of QueryKeys.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "query", "locale", "city" }))
public class QueryKey {

    @Id
    @GeneratedValue
    private Long id;

    // TODO QueryKey is immutable. 
    // TODO @Version shouldn't be needed even though hibernate does try to update versions on both ends of relationships. 
    @Version
    private int version;

    @Column(nullable = false)
    private String query = "";

    @Column(nullable = false)
    private Locale locale = Locale.US;

    @Column(nullable = false)
    private String city = "";

    @JsonIgnore
    @Transient
    private transient HashCodeBuilder hashCodeBuilder;

    @JsonIgnore
    @Transient
    private transient String toStringCache;

    private QueryKey() {
        super();
    }

    public QueryKey(String query, Locale locale, String city) {
        super();
        // force query to lower case (query can't be null so don't test for null)
        this.query = query.toLowerCase();
        this.locale = locale;
        // force city to lower case
        if (city != null) {
            city = city.toLowerCase();
        }
        this.city = city;
    }

    @Override
    public int hashCode() {
        if (hashCodeBuilder == null) {
            hashCodeBuilder = new HashCodeBuilder(17, 37).append(query).append(locale).append(city);
        }
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
        QueryKey otherQueryKey = (QueryKey) other;

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(query, otherQueryKey.query).append(locale, otherQueryKey.locale)
            .append(city, otherQueryKey.city);
        return builder.isEquals();
    }

    @Override
    public String toString() {
        if (toStringCache == null) {
            StringBuilder builder = new StringBuilder("QueryKey: '");
            builder.append(query != null ? query + "', " : "'");
            builder.append(locale != null ? locale + ", " : "");
            builder.append(city != null ? city + ", " : "");
            toStringCache = builder.toString();
        }
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

    /**
     * This comparator sorts first by Query-string, then by Country (aka Locale), then by City.
     */
    public static class QueryKeyComparator implements Comparator<QueryKey> {

        @Override
        public int compare(QueryKey key1, QueryKey key2) {
            // The CompareToBuilder can't be cached - which is OK because this Comparator isn't used
            // often
            CompareToBuilder builder = new CompareToBuilder();
            builder.append(key1.query, key2.query);
            builder.append((key1.locale != null) ? key1.locale.getCountry() : null,
                (key2.locale != null) ? key2.locale.getCountry() : null);
            builder.append(key1.city, key2.city);
            return builder.build();
        }
    }

    public Long getId() {
        return id;
    }

}
