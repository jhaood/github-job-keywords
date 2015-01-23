package com.aestheticsw.jobkeywords.web.rest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aestheticsw.jobkeywords.domain.PivotalJsonPage;
import com.aestheticsw.jobkeywords.domain.fivefilters.TermList;
import com.aestheticsw.jobkeywords.domain.indeed.IndeedResponse;
import com.aestheticsw.jobkeywords.domain.indeed.Result;
import com.aestheticsw.jobkeywords.service.indeed.IndeedService;
import com.aestheticsw.jobkeywords.service.rest.RestClient;
import com.aestheticsw.jobkeywords.service.termextractor.FiveFiltersService;
import com.aestheticsw.jobkeywords.utils.StringUtils;

@RestController
@RequestMapping(value = "/job")
public class JobController {
    @Log
    private Logger log;

    @Autowired
    private IndeedService indeedService;

    @Autowired
    private FiveFiltersService fiveFiltersService;

    @Autowired
    private RestClient restClient;

    @RequestMapping(value = "/pivotal", method = RequestMethod.GET, produces = "application/json")
    public PivotalJsonPage getPage() {
        log.info("/page endpoint");

        PivotalJsonPage page = restClient.getPage();
        return page;
    }

    // @RequestMapping(value = "/indeed", method = RequestMethod.GET, produces = {
    // MediaType.APPLICATION_JSON_VALUE,
    // MediaType.APPLICATION_XML_VALUE })
    @RequestMapping(value = "/joblist", method = RequestMethod.GET)
    public IndeedResponse
            getIndeedJobList(@RequestParam(required = false, defaultValue = "Java Spring") String query, @RequestParam(
                    required = false, defaultValue = "2") int jobCount, @RequestParam(required = false,
                    defaultValue = "0") int start, @RequestParam(required = false, defaultValue = "us") String country,
                    @RequestParam(required = false) String city,
                    @RequestParam(required = false, defaultValue = "0") int radius,
                    @RequestParam(required = false) String sort) {
        log.info("/indeed endpoint");

        Locale locale = Locale.US;
        if (country != null) {
            locale = StringUtils.lookupLocaleByCountry(country);
        }
        
        IndeedResponse indeedResponse = indeedService.getIndeedJobList(query, jobCount, start, locale, city, radius, sort);
        return indeedResponse;
    }

    // @RequestMapping(value = "/indeed", method = RequestMethod.GET, produces = {
    // MediaType.APPLICATION_JSON_VALUE,
    // MediaType.APPLICATION_XML_VALUE })
    @RequestMapping(value = "/job", method = RequestMethod.GET)
    public TermList
            getIndeedJobDetails(@RequestParam(required = false, defaultValue = "Java Spring") String query,
                    @RequestParam(required = false, defaultValue = "2") int jobCount, @RequestParam(required = false,
                            defaultValue = "0") int start,
                    @RequestParam(required = false, defaultValue = "us") String country,
                    @RequestParam(required = false) String city,
                    @RequestParam(required = false, defaultValue = "0") int radius,
                    @RequestParam(required = false) String sort) throws IOException {
        log.info("/job endpoint");

        Locale locale = Locale.US;
        if (country != null) {
            locale = StringUtils.lookupLocaleByCountry(country);
        }

        IndeedResponse indeedResponse = indeedService.getIndeedJobList(query, jobCount, start, locale, city, radius, sort);

        List<Result> results = indeedResponse.getResults().getResults();
        log.info("Indeed returned jobCount=" + results.size());
        
        StringBuilder combinedJobDetails = new StringBuilder();
        for (int index = 0; index < jobCount && index < results.size(); index++) {
            Result job = results.get(index);
            String jobDetails = indeedService.getIndeedJobDetails(job.getUrl());
            combinedJobDetails.append(jobDetails);
            combinedJobDetails.append("\n ");
        }

        TermList terms = fiveFiltersService.getKeywords(combinedJobDetails.toString(), locale);

        return terms;
    }

}