package com.aestheticsw.jobkeywords.service.indeed;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.xml.transform.Source;

import net.exacode.spring.logging.inject.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathOperations;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.aestheticsw.jobkeywords.domain.indeed.IndeedResponse;
import com.aestheticsw.jobkeywords.service.rest.XUserAgentInterceptor;

@Component
public class IndeedService {

    @Log
    private Logger log;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private XPathOperations xpathTemplate;

    public IndeedService() {
        /*
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        restTemplate = new RestTemplate(factory);
        */
        // List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        // for (HttpMessageConverter<?> converter : messageConverters) {
        // if (converter.canRead(Source.class, MediaType.APPLICATION_XML)) {
        // // converter.canRead(Source.class, MediaType.TEXT_HTML);
        // if (! (converter instanceof SourceHttpMessageConverter<?>)) {
        // throw new RuntimeException("Unknown XML Converter type: " +
        // converter.getClass().getName());
        // }
        //
        // SourceHttpMessageConverter<?> xmlConverter = (SourceHttpMessageConverter<?>) converter;
        // List<MediaType> mediaTypes = new
        // ArrayList<MediaType>(xmlConverter.getSupportedMediaTypes());
        // mediaTypes.add(MediaType.TEXT_HTML);
        // xmlConverter.setSupportedMediaTypes(mediaTypes);
        // }
        // }

        // restTemplate.setMessageConverters(messageConverters);
    }

    // Commented out the MappingJackson2XmlHttpMessageConverter stuff...
    public IndeedResponse getIndeedJobList() {
        /*
         * More search parameters...
         * String query =
         * "http://api.indeed.com/ads/apisearch?publisher=1652353865637104&q=java&l=austin%2C+tx"
         * +
         * "&sort=&radius=&st=&jt=&start=&limit=&fromage=&filter=&latlong=1&co=us&chnl=&userip=1.2.3.4"
         * + "&useragent=Mozilla/%2F4.0%28Firefox%29&v=2";
         */
        String query = "http://api.indeed.com/ads/apisearch?publisher=1652353865637104&q=java&v=2";

        restTemplate.setInterceptors(Collections.singletonList(new XUserAgentInterceptor()));

        /*
         * More HttpClient attempts at controlling headers.
         * HttpHeaders headers = new HttpHeaders();
         * headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
         * HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
         * String stringResponse = restTemplate.exchange(query, HttpMethod.GET, entity,
         * String.class).getBody();
         */
        String stringResponse = restTemplate.getForObject(query, String.class);

        IndeedResponse indeedResponse = restTemplate.getForObject(query, IndeedResponse.class);

        log.debug("Response: " + indeedResponse);

        return indeedResponse;
    }

    public String getIndeedJobDetails(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements jobHeader = doc.select("#job_header > b > font");
        Elements jobSummary = doc.select("#job_summary");
        StringBuilder sb = new StringBuilder();
        sb.append(jobHeader.toString().replace("<br>", " ")).append("\n");
        sb.append(jobSummary.toString().replace("<br>", " "));
        return sb.toString();
    }

}
