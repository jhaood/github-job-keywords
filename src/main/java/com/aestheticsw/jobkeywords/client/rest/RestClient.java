package com.aestheticsw.jobkeywords.client.rest;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.aestheticsw.jobkeywords.domain.Page;

@Component
public class RestClient {
    
    private RestTemplate restTemplate;

    public RestClient () {
        restTemplate = new RestTemplate();
    }
    
    public Page execute() {
        Page page = restTemplate.getForObject("http://graph.facebook.com/pivotalsoftware", Page.class);
        
        System.out.println("Name:    " + page.getName());
        System.out.println("About:   " + page.getAbout());
        System.out.println("Phone:   " + page.getPhone());
        System.out.println("Website: " + page.getWebsite());
        
        return page;
    }

}