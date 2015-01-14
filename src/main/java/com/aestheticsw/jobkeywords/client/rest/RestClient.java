package com.aestheticsw.jobkeywords.client.rest;

import java.util.List;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.aestheticsw.jobkeywords.domain.IndeedResponse;
import com.aestheticsw.jobkeywords.domain.PivotalJsonPage;

@Component
public class RestClient {

    // TODO use HttpComponentsClientHttpRequestFactory to create the RestTemplate
    // HttpClient httpClient = HttpClientBuilder.create().build();
    // ClientHttpRequestFactory requestFactory = new
    // HttpComponentsClientHttpRequestFactory(httpClient);

    private RestTemplate restTemplate;

    public RestClient() {
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
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

    // MappingJackson2XmlHttpMessageConverter
    // http://api.indeed.com/ads/apisearch?publisher=1652353865637104&q=java&l=austin%2C+tx&sort=&radius=
    // &st=&jt=&start=&limit=&fromage=&filter=&latlong=1&co=us&chnl=&userip=1.2.3.4&useragent=Mozilla/%2F4.0%28Firefox%29&v=2
    public IndeedResponse getIndeed() {
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        
        IndeedResponse indeedResponse = restTemplate
                .getForObject("http://api.indeed.com/ads/apisearch?publisher=1652353865637104&q=java&l=austin%2C+tx" + "&sort=&radius=&st=&jt=&start=&limit=&fromage=&filter=&latlong=1&co=us&chnl=&userip=1.2.3.4"
                        + "&useragent=Mozilla/%2F4.0%28Firefox%29&v=2",
                        IndeedResponse.class);

        System.out.println("Response: " + indeedResponse);

        return indeedResponse;
    }

}