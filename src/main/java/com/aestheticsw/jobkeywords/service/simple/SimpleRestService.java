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

    public PivotalJsonResponse getPage() {
        PivotalJsonResponse response =
            restTemplate.getForObject("http://graph.facebook.com/pivotalsoftware", PivotalJsonResponse.class);

        System.out.println("Name:    " + response.getName());
        System.out.println("About:   " + response.getAbout());
        System.out.println("Phone:   " + response.getPhone());
        System.out.println("Website: " + response.getWebsite());

        return response;
    }

}