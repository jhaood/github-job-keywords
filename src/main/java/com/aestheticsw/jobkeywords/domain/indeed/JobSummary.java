package com.aestheticsw.jobkeywords.domain.indeed;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


// @JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
public class JobSummary {
    private String jobTitle;
    private String company;
    private String city;
    private String state;
    private String source;
    private String date;
    private String snippet;
    private String url;
    private String jobkey;
    private Boolean sponsored;

    public JobSummary() {
        super();
    }

    public JobSummary(String jobTitle, String company, String city, String state, String source, String date,
            String snippet, String url, String jobkey, Boolean sponsored) {
        super();
        this.jobTitle = jobTitle;
        this.company = company;
        this.city = city;
        this.state = state;
        this.source = source;
        this.date = date;
        this.snippet = snippet;
        this.url = url;
        this.jobkey = jobkey;
        this.sponsored = sponsored;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getSource() {
        return source;
    }

    public String getDate() {
        return date;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getUrl() {
        return url;
    }

    public String getJobkey() {
        return jobkey;
    }

    public Boolean getSponsored() {
        return sponsored;
    }

}
