package com.aestheticsw.jobkeywords.service.termextractor.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * SearchParameter holds the full set of parameters for a given search.
 * <p/>
 * 
 * This class is distinct from the QueryKey class because Indeed will only return a maxiumum of 25
 * jobs per REST call. The UI allows the user to search for the same QueryKey values but with
 * different SearchParameter values in order to span more than 25 jobs.
 * <p/>
 * 
 * SearchParameters is an immutable class that can be used as an index in a Map.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 * @see com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "query_key_id", "jobCount", "start", "radius", "sort" }))
public class SearchParameters {

    @Id
    @GeneratedValue
    private Long id;

    // SearchParameters is immutable but it does have a foreign-key relationship so define @Version 
    @Version
    private int version;

    // @Column(name = "TERM_FREQUENCY_RESULTS_ID")
    // private Long termFrequencyResultsId;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private QueryKey queryKey;

    private int jobCount;
    private int start;
    private int radius;
    @Column(nullable = false)
    private String sort = "";

    @JsonIgnore
    @Transient
    private transient HashCodeBuilder hashCodeBuilderCache;

    @JsonIgnore
    @Transient
    private transient String toStringCache;

    @SuppressWarnings("unused")
    private SearchParameters() {
        super();
    }

    public SearchParameters(QueryKey queryKey, int jobCount, int start, int radius, String sort) {
        super();

        this.queryKey = queryKey;
        this.jobCount = jobCount;
        this.start = start;
        this.radius = radius;
        if (sort != null) {
            this.sort = sort;
        }
    }

    @Override
    public int hashCode() {
        if (hashCodeBuilderCache == null) {
            hashCodeBuilderCache =
                new HashCodeBuilder(17, 37).append(queryKey).append(jobCount).append(start).append(radius).append(sort);
        }
        return hashCodeBuilderCache.hashCode();
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

        // Can't cache the EqualsBuilder because it depends on the argument 'otherSearchParameters'
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        equalsBuilder.append(queryKey, otherSearchParameters.queryKey).append(jobCount, otherSearchParameters.jobCount)
            .append(start, otherSearchParameters.start).append(radius, otherSearchParameters.radius)
            .append(sort, otherSearchParameters.sort);
        return equalsBuilder.isEquals();
    }

    @Override
    public String toString() {
        if (toStringCache == null) {
            StringBuilder builder = new StringBuilder("SearchParams: '");
            builder.append(queryKey != null ? queryKey.toString() + "', " : "'");
            builder.append("start: " + start + ", ");
            builder.append("count: " + jobCount + ", query:'");
            builder.append(radius != 0 ? "radius: " + radius + ", " : "");
            builder.append(StringUtils.isNotEmpty(sort) ? "sort: " + sort + ", " : "");
            toStringCache = builder.toString();
        }
        return toStringCache;
    }

    public QueryKey getQueryKey() {
        return queryKey;
    }

    public int getJobCount() {
        return jobCount;
    }

    public int getStart() {
        return start;
    }

    public int getRadius() {
        return radius;
    }

    public String getSort() {
        return sort;
    }
}
