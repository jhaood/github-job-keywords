/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.web.rest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aestheticsw.jobkeywords.service.simple.PivotalJsonResponse;
import com.aestheticsw.jobkeywords.service.simple.SimpleRestService;
import com.aestheticsw.jobkeywords.shared.config.Log;

/**
 * This controller experiments with JSON serialization and relies on the same data model class 
 * for JSON desearialization in the SimpleRestService. 
 */
@RestController
@RequestMapping(value = "/rest")
public class SimpleRestController {

    @Log
    private Logger log;

    private SimpleRestService simpleRestService;

    @Autowired
    public SimpleRestController(SimpleRestService simpleRestService) {
        super();
        this.simpleRestService = simpleRestService;
    }

    @RequestMapping(value = "/pivotal", method = RequestMethod.GET, produces = "application/json")
    public PivotalJsonResponse getPage() {
        log.info("/page endpoint");

        PivotalJsonResponse page = simpleRestService.getPage();
        return page;
    }

}
