/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com) Apache Version 2
 * license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.service.termextractor.impl.indeed;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * JobListResponse is a JSON deserialization object that is the root of the object graph returned
 * from Indeed.com.
 * 
 * <pre>
 * The Indeed data model is small: 
 * 
 *      JobListResponse - contains a few attributes and a list of job summaries
 * 
 *      JobList - A simple list of job summary objects
 * 
 *      JobSummary - Represents a single job posting. Contains the company name, the location, 
 *      a brief description and a URL that points at the detailed description.  
 * 
 * This data model is instantiated from Indeed.com's JSON response format:
 * 
 * {
 *         "dupeFilter": true,
 *         "start": 1,
 *         "end": 2,
 *         "pageNumber": 0,
 *         "totalResults": 11232
 *         "results": 
 *         {
 *                 "results": 
 *                 [
 *                         {
 *                                 "jobTitle": null,
 *                                 "company": "Goldman Sachs",
 *                                 "city": "New York",
 *                                 "state": "NY",
 *                                 "source": "Goldman Sachs",
 *                                 "date": "Sat, 31 Jan 2015 08:52:00 GMT",
 *                                 "snippet": "Spring Dependency Injection... ",
 *                                 "url": "http://www.indeed.com/viewjob?jk=8dba1...",
 *                                 "jobkey": "8dba120e3a4d55d4",
 *                                 "sponsored": false
 *                         },
 *                         {
 *                                 ...
 *                         }
 *                 ]
 *         },
 * }
 * </pre>
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
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

    // can't use Array or ArrayList as the collection-type. Have to use a first-class object for
    // deserialization to be easy
    @XmlElement(name = "results")
    private JobList results;

    public JobListResponse() {
        super();
    }

    public JobListResponse(String location, Boolean dupeFilter, Boolean highlight, Integer totalresults, int start,
            int end, int pageNumber, JobList results) {
        super();
        this.location = location;
        this.dupeFilter = dupeFilter;
        this.highlight = highlight;
        this.totalresults = totalresults;
        this.start = start;
        this.end = end;
        this.pageNumber = pageNumber;
        this.results = results;
    }

    public boolean hasResults() {
        return (results != null && results.hasResults());
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
