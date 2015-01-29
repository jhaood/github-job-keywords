package com.aestheticsw.jobkeywords.web.rest;

import java.io.IOException;
import java.util.Locale;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aestheticsw.jobkeywords.domain.indeed.JobListResponse;
import com.aestheticsw.jobkeywords.domain.termfrequency.QueryKey;
import com.aestheticsw.jobkeywords.domain.termfrequency.SearchParameters;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermFrequencyResults;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermList;
import com.aestheticsw.jobkeywords.service.indeed.IndeedService;
import com.aestheticsw.jobkeywords.service.termextractor.TermExtractorService;
import com.aestheticsw.jobkeywords.utils.SearchUtils;

@RestController
@RequestMapping(value = "/rest/job")
public class JobRestController {
    @Log
    private Logger log;

    @Autowired
    private IndeedService indeedService;

    @Autowired
    private TermExtractorService termExtractorService;

    // produces = {MediaType.APPLICATION_JSON_VALUE,
    // produces = {MediaType.APPLICATION_XML_VALUE })
    @RequestMapping(value = "/joblist", method = RequestMethod.GET)
    public JobListResponse
            getIndeedJobList(@RequestParam(required = false, defaultValue = "Java Spring") String query, @RequestParam(
                    required = false, defaultValue = "2") int jobCount, @RequestParam(required = false,
                    defaultValue = "0") int start, @RequestParam(required = false, defaultValue = "US") String country,
                    @RequestParam(required = false) String city,
                    @RequestParam(required = false, defaultValue = "0") int radius,
                    @RequestParam(required = false) String sort) {
        log.info("/indeed endpoint");

        Locale locale = Locale.US;
        if (country != null) {
            locale = SearchUtils.lookupLocaleByCountry(country);
        }

        SearchParameters params = new SearchParameters(query, jobCount, start, locale, city, radius, sort);

        JobListResponse jobListResponse = indeedService.getIndeedJobList(params);
        return jobListResponse;
    }

    // produces = {MediaType.APPLICATION_JSON_VALUE,
    // produces = {MediaType.APPLICATION_XML_VALUE })
    @RequestMapping(value = "/terms", method = RequestMethod.GET)
    public TermList
            getTermListForSearchParameters(@RequestParam(required = false, defaultValue = "Java Spring") String query,
                    @RequestParam(required = false, defaultValue = "2") int jobCount, @RequestParam(required = false,
                            defaultValue = "0") int start,
                    @RequestParam(required = false, defaultValue = "US") String country,
                    @RequestParam(required = false) String city,
                    @RequestParam(required = false, defaultValue = "0") int radius,
                    @RequestParam(required = false) String sort) throws IOException {
        log.info("/job endpoint");

        Locale locale = Locale.US;
        if (country != null) {
            locale = SearchUtils.lookupLocaleByCountry(country);
        }
        SearchParameters params = new SearchParameters(query, jobCount, start, locale, city, radius, sort);

        TermList terms = termExtractorService.extractTerms(params);

        return terms;
    }

    @RequestMapping(value = "/accumulatedterms", method = RequestMethod.GET)
    public TermFrequencyResults getAccumulatedTermFrequencyResults(@RequestParam(required = false,
            defaultValue = "Java Spring") String query,
            @RequestParam(required = false, defaultValue = "US") String country,
            @RequestParam(required = false) String city) throws IOException {
        log.info("/job endpoint");

        Locale locale = Locale.US;
        if (country != null) {
            locale = SearchUtils.lookupLocaleByCountry(country);
        }
        QueryKey queryKey = new QueryKey(query, locale, city);

        TermFrequencyResults results = termExtractorService.getAccumulatedTermFrequencyResults(queryKey);

        return results;
    }

}