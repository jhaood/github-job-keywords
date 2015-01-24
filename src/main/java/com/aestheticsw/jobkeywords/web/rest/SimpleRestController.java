package com.aestheticsw.jobkeywords.web.rest;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aestheticsw.jobkeywords.domain.PivotalJsonPage;
import com.aestheticsw.jobkeywords.service.rest.SimpleRestService;

@RestController
@RequestMapping(value = "/rest")
public class SimpleRestController {

    @Log
    private Logger log;

    @Autowired
    private SimpleRestService simpleRestService;
    
    @RequestMapping(value = "/pivotal", method = RequestMethod.GET, produces = "application/json")
    public PivotalJsonPage getPage() {
        log.info("/page endpoint");

        PivotalJsonPage page = simpleRestService.getPage();
        return page;
    }

}
