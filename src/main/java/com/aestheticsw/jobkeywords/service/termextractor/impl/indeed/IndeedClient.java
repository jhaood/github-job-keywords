package com.aestheticsw.jobkeywords.service.termextractor.impl.indeed;

import java.io.IOException;

import net.exacode.spring.logging.inject.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.XPathOperations;

import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;

/**
 * Return either a list of jobs, or return the job-details for a specifig job-ID.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@Component
public class IndeedClient {

    @Log
    private Logger log;

    private RestTemplate restTemplate;

    private XPathOperations xpathTemplate;

    @Autowired
    public IndeedClient(RestTemplate restTemplate, XPathOperations xpathTemplate) {
        this.restTemplate = restTemplate;
        this.xpathTemplate = xpathTemplate;

        // Not needed now that converters are working properly.. but might be useful later to tweak
        // config
        //
        // List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        // for (HttpMessageConverter<?> converter : messageConverters) {
        // if (converter.canRead(Source.class, MediaType.APPLICATION_XML)) {
        // // converter.canRead(Source.class, MediaType.TEXT_HTML);
        // if (! (converter instanceof SourceHttpMessageConverter<?>)) {
        // throw new RuntimeException("Unknown XML Converter type: " +
        // converter.getClass().getName());
        // }
        //
        // SourceHttpMessageConverter<?> xmlConverter = (SourceHttpMessageConverter<?>) converter;
        // List<MediaType> mediaTypes = new
        // ArrayList<MediaType>(xmlConverter.getSupportedMediaTypes());
        // mediaTypes.add(MediaType.TEXT_HTML);
        // xmlConverter.setSupportedMediaTypes(mediaTypes);
        // }
        // }

        // restTemplate.setMessageConverters(messageConverters);
    }

    /**
     * <pre>
     * Example search parameters...
     * 
     * "http://api.indeed.com/ads/apisearch?publisher=1652353865637104&q=java&l=austin%2C+tx" +
     * "&sort=&radius=&st=&jt=&start=&limit=&fromage=&filter=&latlong=1&co=us&chnl=&userip=1.2.3.4"
     * + "&useragent=Mozilla/%2F4.0%28Firefox%29&v=2";
     * </pre>
     */
    public JobListResponse getIndeedJobList(SearchParameters params) {
        StringBuilder queryUrl = new StringBuilder();
        String query = params.getQuery();

        queryUrl.append("http://api.indeed.com/ads/apisearch?publisher=1652353865637104&v=2&q=").append(query);
        queryUrl.append("&limit=").append(params.getJobCount());
        queryUrl.append("&start=").append(params.getStart());
        if (params.getLocale() != null) {
            queryUrl.append("&co=").append(params.getLocale().getCountry());
        }
        if (params.getCity() != null) {
            queryUrl.append("&l=").append(params.getCity());
        }
        if (params.getRadius() > 0) {
            queryUrl.append("&radius=").append(params.getRadius());
        }
        if (params.getSort() != null) {
            queryUrl.append("&sort=").append(params.getSort());
        }

        log.debug("Indeed job-list query: " + queryUrl);

        // XUserAgentInterceptor isn't needed now, but might be useful some time.
        // restTemplate.setInterceptors(Collections.singletonList(new XUserAgentInterceptor()));

        JobListResponse jobListResponse = restTemplate.getForObject(queryUrl.toString(), JobListResponse.class);
        log.debug("Response: " + jobListResponse);

        if (jobListResponse.getTotalResults() == 0) {
            throw new IllegalArgumentException("Query retrieved no results from Indeed: " + queryUrl);
        }

        return jobListResponse;
    }

    /**
     * Return the job-details sub-section of the HTML that Indeed returns. This method takes a URL
     * that Indeed
     * returns for each JobSummary returned by getIndeedJobList() above.
     * 
     * 
     * This method is dependent upon the JSoup library which can consume malformed
     * HTML and XML with invalid syntax.
     * 
     * JSoup can't be tested easily.
     * 
     * TODO convert from JSoup to HtmlCleaner
     */
    public String getIndeedJobDetails(String url) {
        log.debug("Indeed job-details query: " + url);
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException("Indeed-search or JSoup response parser failed, URL: " + url, e);
        }
        Elements jobHeader = doc.select("#job_header > b > font");
        Elements jobSummary = doc.select("#job_summary");
        StringBuilder sb = new StringBuilder();
        sb.append(jobHeader.toString()).append("\n");
        sb.append(jobSummary.toString());
        return sb.toString();
    }

}
