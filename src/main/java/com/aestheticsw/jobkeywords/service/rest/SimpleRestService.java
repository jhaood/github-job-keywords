package com.aestheticsw.jobkeywords.service.rest;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.aestheticsw.jobkeywords.domain.PivotalJsonPage;

@Component
public class SimpleRestService {

    @Log
    private Logger log;

    // TODO use HttpComponentsClientHttpRequestFactory to create the RestTemplate
    // HttpClient httpClient = HttpClientBuilder.create().build();
    // ClientHttpRequestFactory requestFactory = new
    // HttpComponentsClientHttpRequestFactory(httpClient);

    private RestTemplate restTemplate;

    public SimpleRestService() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        restTemplate = new RestTemplate(factory);
    }

    public PivotalJsonPage getPage() {
        PivotalJsonPage page = restTemplate.getForObject("http://graph.facebook.com/pivotalsoftware",
                PivotalJsonPage.class);

        System.out.println("Name:    " + page.getName());
        System.out.println("About:   " + page.getAbout());
        System.out.println("Phone:   " + page.getPhone());
        System.out.println("Website: " + page.getWebsite());

        return page;
    }

}