package com.aestheticsw.jobkeywords.web.thymeleaf;

import org.hibernate.validator.constraints.NotEmpty;

public class SearchFormBean {
    @NotEmpty(message = "Query expression is required.")
    private String query;

    private int jobCount;

    private int start;

    private String country;

    private String city;

    private int radius;

    private String sort;

    private boolean hadFormErrors;
    
    public boolean isHadFormErrors() {
        return hadFormErrors;
    }

    public boolean getHadFormErrors() {
        return isHadFormErrors();
    }

    public void setHadFormErrors(boolean hadFormErrors) {
        this.hadFormErrors = hadFormErrors;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = (query != null) ? query.toLowerCase() : null;
    }

    public int getJobCount() {
        return jobCount;
    }

    public void setJobCount(int jobCount) {
        this.jobCount = jobCount;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = (country != null) ? country.toUpperCase() : null;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = (city != null) ? city.toLowerCase() : null;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

}
