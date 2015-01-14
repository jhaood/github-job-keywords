package com.aestheticsw.jobkeywords.web;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aestheticsw.jobkeywords.client.rest.RestClient;
import com.aestheticsw.jobkeywords.domain.IndeedResponse;
import com.aestheticsw.jobkeywords.domain.PivotalJsonPage;

@RestController
@RequestMapping(value = "/rest")
public class RestClientController {
    @Log
    private Logger log;
        
    @Autowired
    private RestClient restClient;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PivotalJsonPage getPage() {
        log.error("/page endpoint");

        PivotalJsonPage page = restClient.getPage();
        return page;
    }

    @RequestMapping(value = "/indeed", method = RequestMethod.GET)
    public IndeedResponse getIndeed() {
        log.error("/indeed endpoint");

        IndeedResponse indeedResponse = restClient.getIndeed();
        return indeedResponse;
    }

}