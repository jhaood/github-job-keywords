package com.aestheticsw.jobkeywords.web.rest;

import java.io.IOException;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aestheticsw.jobkeywords.domain.PivotalJsonPage;
import com.aestheticsw.jobkeywords.domain.indeed.IndeedResponse;
import com.aestheticsw.jobkeywords.domain.indeed.Result;
import com.aestheticsw.jobkeywords.service.indeed.IndeedService;
import com.aestheticsw.jobkeywords.service.rest.RestClient;
import com.aestheticsw.jobkeywords.service.termextractor.FiveFiltersService;

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

    @RequestMapping(value = "/joblist", method = RequestMethod.GET)
    // @RequestMapping(value = "/indeed", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE,
            // MediaType.APPLICATION_XML_VALUE })
    public IndeedResponse getIndeedJobList() {
        log.info("/indeed endpoint");

        IndeedResponse indeedResponse = indeedService.getIndeedJobList();
        return indeedResponse;
    }

    @RequestMapping(value = "/job", method = RequestMethod.GET)
    // @RequestMapping(value = "/indeed", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE,
            // MediaType.APPLICATION_XML_VALUE })
    public String getIndeedJobDetails() throws IOException {
        log.info("/job endpoint");
        
        IndeedResponse jobListResponse = getIndeedJobList();
        Result job = jobListResponse.getResults().getResults().get(0);

        String jobDetails = indeedService.getIndeedJobDetails(job.getUrl());
        
        String keywordJson = fiveFiltersService.getKeywords(jobDetails);
        return keywordJson;
    }

}