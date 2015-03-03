package com.aestheticsw.jobkeywords.service.simple;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * This is a test fixture for converting JSON into a simple domain class. This allows the
 * SimpleRestController to experiment with returning XML or Json to a HTTP client. 
 */
@Component
public class SimpleRestService {

    @Log
    private Logger log;

    private RestTemplate restTemplate;

    public SimpleRestService() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        restTemplate = new RestTemplate(factory);
    }

    public PivotalJsonPage getPage() {
        PivotalJsonPage page =
            restTemplate.getForObject("http://graph.facebook.com/pivotalsoftware", PivotalJsonPage.class);

        System.out.println("Name:    " + page.getName());
        System.out.println("About:   " + page.getAbout());
        System.out.println("Phone:   " + page.getPhone());
        System.out.println("Website: " + page.getWebsite());

        return page;
    }

}