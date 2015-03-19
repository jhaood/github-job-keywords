package com.aestheticsw.jobkeywords.service.termextractor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class holds the summary of a given job returned from the indeed.com API. This class is both
 * a JPA @Entity as well as a REST serialization class that Jackson can marshal into and out of JSON
 * or XML.
 * <p/>
 * JobSummary is immutable.
 */
@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class JobSummary {

    @Id
    @GeneratedValue
    private Long id;

    // JobSummary is immutable. 
    // TODO @Version shouldn't be needed but hibernate used to update versions on both ends of relationships. 
    @Version
    private int version;

    private String jobTitle;
    private String company;
    private String city;
    private String state;
    private String source;
    private String date;
    @Column(name = "snippet", length = 500)
    private String snippet;
    // Indeed URLs are huge... 
    @Column(name = "url", length = 500)
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
