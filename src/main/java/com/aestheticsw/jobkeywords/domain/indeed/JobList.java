package com.aestheticsw.jobkeywords.domain.indeed;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "jobSummaries")
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
