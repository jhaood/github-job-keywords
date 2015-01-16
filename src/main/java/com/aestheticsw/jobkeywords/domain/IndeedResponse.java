package com.aestheticsw.jobkeywords.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "response")
@XmlAccessorType(XmlAccessType.FIELD)
// @XmlJavaTypeAdapter(CollapsedStringAdapter.class) 
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndeedResponse {
    @XmlElement(required = true)
    private String location;

    private String dupeFilter;

    @XmlElement(required = true)
    private int totalResults;
    @XmlElement(required = true)
    private int start;
    @XmlElement(required = true)
    private int end;

    private int pageNumber;

    // @XmlJavaTypeAdapter(CollapsedStringAdapter.class) 
    @XmlElementWrapper(name = "results")
    @XmlElement(name = "result")
    @JsonDeserialize(as=ArrayList.class, contentAs=Result.class)
    private List<Result> results;

    public String getLocation() {
        return location;
    }

    public String getDupeFilter() {
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

    public List<Result> getResults() {
        return results;
    }
}
