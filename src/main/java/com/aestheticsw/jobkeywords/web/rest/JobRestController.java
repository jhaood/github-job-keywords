/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.web.rest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aestheticsw.jobkeywords.service.termextractor.TermExtractorService;
import com.aestheticsw.jobkeywords.service.termextractor.domain.JobSummary;
import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyList;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyResults;
import com.aestheticsw.jobkeywords.service.termextractor.impl.indeed.IndeedQueryException;
import com.aestheticsw.jobkeywords.service.termextractor.support.SearchUtils;
import com.aestheticsw.jobkeywords.shared.config.Log;

/**
 * This REST controller exposes the Indeed job list or the extracted terms to a RESTful client.
 * 
 * This controller experiments with JSON and XML serialization.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@RestController
@RequestMapping(value = "/rest/job")
public class JobRestController {
    @Log
    private Logger log;

    private TermExtractorService termExtractorService;

    @Autowired
    public JobRestController(TermExtractorService termExtractorService) {
        super();
        this.termExtractorService = termExtractorService;
    }

    // Don't specify the "produces" media type - allow the configuraiton to detect which format 
    // is being requested by the client. 
    // 
    // produces = {MediaType.APPLICATION_JSON_VALUE,
    // produces = {MediaType.APPLICATION_XML_VALUE })
    @RequestMapping(value = "/joblist", method = RequestMethod.GET)
    public List<JobSummary> getIndeedJobSummaryList(@RequestParam(required = false, defaultValue = "Java Spring") String query,
            @RequestParam(required = false, defaultValue = "2") int jobCount, @RequestParam(
                    required = false, defaultValue = "0") int start, @RequestParam(
                    required = false, defaultValue = "US") String country, @RequestParam(required = false) String city,
            @RequestParam(required = false, defaultValue = "0") int radius, @RequestParam(required = false) String sort) {
        log.info("/indeed endpoint");

        Locale locale = Locale.US;
        if (country != null) {
            // force to upper case !
            locale = SearchUtils.lookupLocaleByCountry(country.toUpperCase());
        }

        SearchParameters params =
            new SearchParameters(new QueryKey(query, locale, city), jobCount, start, radius, sort);

        List<JobSummary> jobSummaryList = termExtractorService.getIndeedJobSummaryList(params);
        return jobSummaryList;
    }

    // produces = {MediaType.APPLICATION_JSON_VALUE,
    // produces = {MediaType.APPLICATION_XML_VALUE })
    @RequestMapping(value = "/terms", method = RequestMethod.GET)
    public TermFrequencyList getTermFrequencyListForSearchParameters(@RequestParam(
            required = false, defaultValue = "Java Spring") String query, @RequestParam(
            required = false, defaultValue = "2") int jobCount,
            @RequestParam(required = false, defaultValue = "0") int start, @RequestParam(
                    required = false, defaultValue = "US") String country, @RequestParam(required = false) String city,
            @RequestParam(required = false, defaultValue = "0") int radius, @RequestParam(required = false) String sort)
            throws IOException {
        log.info("/job endpoint");

        Locale locale = Locale.US;
        if (country != null) {
            locale = SearchUtils.lookupLocaleByCountry(country.toUpperCase());
        }
        SearchParameters params = new SearchParameters(new QueryKey(query, locale, city), jobCount, start, radius, sort);

        TermFrequencyList terms;
        try {
            terms = termExtractorService.extractTerms(params);
        } catch (IndeedQueryException e) {
            throw new RuntimeException(e);
        }

        return terms;
    }

    @RequestMapping(value = "/accumulatedterms", method = RequestMethod.GET)
    public TermFrequencyResults getAccumulatedTermFrequencyResults(@RequestParam(
            required = false, defaultValue = "Java Spring") String query, @RequestParam(
            required = false, defaultValue = "US") String country, @RequestParam(required = false) String city)
            throws IOException {
        log.info("/job endpoint");

        Locale locale = Locale.US;
        if (country != null) {
            locale = SearchUtils.lookupLocaleByCountry(country.toUpperCase());
        }
        QueryKey queryKey = new QueryKey(query, locale, city);

        TermFrequencyResults results = termExtractorService.getAccumulatedTermFrequencyResults(queryKey);

        return results;
    }

}