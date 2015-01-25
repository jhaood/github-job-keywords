package com.aestheticsw.jobkeywords.domain.indeed;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;

// @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
// @JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JobListResponse {
    @XmlElement
    private String location;

    @XmlElement(name = "dupefilter")
    private Boolean dupeFilter;

    @XmlElement(name = "highlight")
    private Boolean highlight;

    @XmlElement(name = "totalresults")
    private Integer totalresults;

    @XmlElement
    private int start;

    @XmlElement
    private int end;

    @XmlElement(name = "pagenumber")
    private int pageNumber;

    // can't use Array or ArrayList as type = must use simple array or else whitespace isn't
    // ignored.
    @XmlElement(name = "results")
    private JobList results;

    public boolean hasResults() {
        if (results == null || results.getResults() == null || results.getResults().size() == 0) {
            return false;
        }
        return true;
    }

    public String getLocation() {
        return location;
    }

    public Boolean getDupeFilter() {
        return dupeFilter;
    }

    public int getTotalResults() {
        return totalresults;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public JobList getResults() {
        return results;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDupeFilter(Boolean dupeFilter) {
        this.dupeFilter = dupeFilter;
    }

    public void setTotalResults(int totalResults) {
        this.totalresults = totalResults;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setResults(JobList results) {
        this.results = results;
    }

}
