package com.aestheticsw.jobkeywords.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "response")
// @XmlAccessorType(XmlAccessType.FIELD)
// @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndeedResponse {
    @JacksonXmlProperty
    private String location;

    @JacksonXmlProperty(localName = "dupefilter")
    private Boolean dupeFilter;
    @JacksonXmlProperty(localName = "totalresults")
    private int totalResults;
    @JacksonXmlProperty
    private int start;
    @JacksonXmlProperty
    private int end;

    @JacksonXmlProperty(localName = "pagenumber")
    private int pageNumber;

    // can't use Array or ArrayList as type = must use simple array or else whitespace isn't
    // ignored.
    @JacksonXmlProperty(localName = "results")
    @JacksonXmlElementWrapper(localName = "result", useWrapping = true)
    // @XmlElementWrapper(name = "results")
    // @XmlElement(name = "result")
    private Result[] results;

    public String getLocation() {
        return location;
    }

    public Boolean getDupeFilter() {
        return dupeFilter;
    }

    public int getTotalResults() {
        return totalResults;
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

    public Result[] getResults() {
        return results;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDupeFilter(Boolean dupeFilter) {
        this.dupeFilter = dupeFilter;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
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

    public void setResults(Result[] results) {
        this.results = results;
    }

}
