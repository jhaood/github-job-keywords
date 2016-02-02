/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.service.simple;


import org.slf4j.Logger;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.aestheticsw.jobkeywords.shared.config.Log;

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
            restTemplate.getForObject("https://graph.facebook.com/pivotalsoftware"
                + "?access_token=CAACEdEose0cBAM4TJ3G9k10LCZAA8jUQ8xu9bLZBkcaTYsTso9ZCWy4tMdlgJ2MBXcgJWO2vhsrHZBjPkXVTrLjHZBGbw5rd8lV4ljl7uvzWNChl1ZCJKZCcmeqIqwNVFxIDZCzBekm3t7TTSiv40WEQZB5YkENUVdUPA0aANQbDpOVbjHJ3ZAFswIVsLz2icQwIwZD", 
                PivotalJsonResponse.class);

        System.out.println("Name:    " + response.getName());
        System.out.println("About:   " + response.getAbout());
        System.out.println("Phone:   " + response.getPhone());
        System.out.println("Website: " + response.getWebsite());

        return response;
    }

}