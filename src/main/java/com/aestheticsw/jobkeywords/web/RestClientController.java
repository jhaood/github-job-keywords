package com.aestheticsw.jobkeywords.web;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aestheticsw.jobkeywords.client.rest.RestClient;
import com.aestheticsw.jobkeywords.domain.Page;

@RestController
@RequestMapping(value = "/rest")
public class RestClientController {
    @Log
    private Logger log;
        
    @Autowired
    private RestClient restClient;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page getPage() {
        log.error("/page endpoint");

        Page page = restClient.execute();
        return page;
    }

}