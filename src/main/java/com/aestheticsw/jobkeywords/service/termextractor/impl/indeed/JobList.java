/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com) Apache Version 2
 * license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.service.termextractor.impl.indeed;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aestheticsw.jobkeywords.service.termextractor.domain.JobSummary;

/**
 * JobList is a JSON deserialization object that makes it easy to deserialize the array of
 * job-summaries.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@XmlRootElement(name = "jobSummaries")
@XmlAccessorType(XmlAccessType.FIELD)
public class JobList {

    @XmlElement(name = "result")
    private List<JobSummary> jobSummaries;

    public JobList() {
        super();
    }

    public JobList(List<JobSummary> jobSummaries) {
        super();
        this.jobSummaries = jobSummaries;
    }

    public boolean hasResults() {
        return (jobSummaries != null && jobSummaries.size() > 0);
    }

    public List<JobSummary> getResults() {
        return jobSummaries;
    }

}
