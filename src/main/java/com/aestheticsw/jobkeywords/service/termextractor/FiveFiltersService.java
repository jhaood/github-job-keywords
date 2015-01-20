package com.aestheticsw.jobkeywords.service.termextractor;

import java.util.Collections;
import java.util.List;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.aestheticsw.jobkeywords.domain.PivotalJsonPage;
import com.aestheticsw.jobkeywords.domain.indeed.IndeedResponse;
import com.aestheticsw.jobkeywords.service.rest.XUserAgentInterceptor;

@Component
public class FiveFiltersService {

    @Log
    private Logger log;

    private RestTemplate restTemplate;

    public FiveFiltersService() {
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

    // Commented out the MappingJackson2XmlHttpMessageConverter stuff... 
    public IndeedResponse getIndeed() {
        String query = "http://api.indeed.com/ads/apisearch?publisher=1652353865637104&q=java&v=2";

        restTemplate.setInterceptors(Collections.singletonList(new XUserAgentInterceptor()));

        IndeedResponse indeedResponse = restTemplate.getForObject(query, IndeedResponse.class);

        log.debug("Response: " + indeedResponse);

        return indeedResponse;
    }
}