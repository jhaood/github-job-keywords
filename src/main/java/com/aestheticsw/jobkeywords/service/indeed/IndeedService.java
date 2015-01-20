package com.aestheticsw.jobkeywords.service.indeed;

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

import com.aestheticsw.jobkeywords.domain.indeed.IndeedResponse;
import com.aestheticsw.jobkeywords.service.rest.XUserAgentInterceptor;

@Component
public class IndeedService {

    @Log
    private Logger log;

    // TODO use HttpComponentsClientHttpRequestFactory to create the RestTemplate
    // HttpClient httpClient = HttpClientBuilder.create().build();
    // ClientHttpRequestFactory requestFactory = new
    // HttpComponentsClientHttpRequestFactory(httpClient);

    private RestTemplate restTemplate;

    public IndeedService() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        restTemplate = new RestTemplate(factory);
    }

    // MappingJackson2XmlHttpMessageConverter
    // http://api.indeed.com/ads/apisearch?publisher=1652353865637104&q=java&l=austin%2C+tx&sort=&radius=
    // &st=&jt=&start=&limit=&fromage=&filter=&latlong=1&co=us&chnl=&userip=1.2.3.4
    // &useragent=Mozilla/%2F4.0%28Firefox%29&v=2
    public IndeedResponse getIndeed() {
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> converter : messageConverters) {
            if (converter.canRead(IndeedResponse.class, MediaType.APPLICATION_XML)) {
                if (converter instanceof MappingJackson2XmlHttpMessageConverter
                        || converter instanceof MappingJackson2HttpMessageConverter) {
                    MappingJackson2XmlHttpMessageConverter xmlConverter = (MappingJackson2XmlHttpMessageConverter) converter;
                    /*
                    xmlConverter.getObjectMapper()
                            .configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
                    xmlConverter.getObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                    xmlConverter.getObjectMapper().configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
                            true);
                    */
                    
                    // xmlConverter.getObjectMapper().registerModule(new XmlWhitespaceModule());
                }
            }
        }

        /*
        String query = "http://api.indeed.com/ads/apisearch?publisher=1652353865637104&q=java&l=austin%2C+tx"
                + "&sort=&radius=&st=&jt=&start=&limit=&fromage=&filter=&latlong=1&co=us&chnl=&userip=1.2.3.4"
                + "&useragent=Mozilla/%2F4.0%28Firefox%29&v=2";
        */
        String query = "http://api.indeed.com/ads/apisearch?publisher=1652353865637104&q=java&v=2";

        restTemplate.setInterceptors(Collections.singletonList(new XUserAgentInterceptor()));

        /*
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        String stringResponse = restTemplate.exchange(query, HttpMethod.GET, entity, String.class).getBody();
        */
        String stringResponse = restTemplate.getForObject(query, String.class);

        IndeedResponse indeedResponse = restTemplate.getForObject(query, IndeedResponse.class);

        log.debug("Response: " + indeedResponse);

        return indeedResponse;
    }
}