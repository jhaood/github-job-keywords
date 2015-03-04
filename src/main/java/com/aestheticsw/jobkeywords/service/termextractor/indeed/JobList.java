package com.aestheticsw.jobkeywords.service.termextractor.indeed;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aestheticsw.jobkeywords.domain.JobSummary;

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
