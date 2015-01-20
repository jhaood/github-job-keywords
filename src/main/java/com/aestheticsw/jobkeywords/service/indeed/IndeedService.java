package com.aestheticsw.jobkeywords.service.indeed;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
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

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
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

    // TODO use HttpComponentsClientHttpRequestFactory to create the RestTemplate
    // HttpClient httpClient = HttpClientBuilder.create().build();
    // ClientHttpRequestFactory requestFactory = new
    // HttpComponentsClientHttpRequestFactory(httpClient);

    private RestTemplate restTemplate;

    @Autowired
    private XPathOperations xpathTemplate;

    /*
     * public FlickrClient(RestOperations restTemplate, XPathOperations xpathTemplate) {
     * this.restTemplate = restTemplate;
     * this.xpathTemplate = xpathTemplate;
     * }
     */

    public IndeedService() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        restTemplate = new RestTemplate(factory);

        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> converter : messageConverters) {
            if (converter.canRead(Source.class, MediaType.APPLICATION_XML)) {
                // converter.canRead(Source.class, MediaType.TEXT_HTML);
                if (! (converter instanceof SourceHttpMessageConverter<?>)) {
                    throw new RuntimeException("Unknown XML Converter type: " + converter.getClass().getName());
                }

                SourceHttpMessageConverter<?> xmlConverter = (SourceHttpMessageConverter<?>) converter;
                List<MediaType> mediaTypes = new ArrayList<MediaType>(xmlConverter.getSupportedMediaTypes());
                mediaTypes.add(MediaType.TEXT_HTML);
                xmlConverter.setSupportedMediaTypes(mediaTypes);
            }
        }

        // restTemplate.setMessageConverters(messageConverters);
    }

    // Commented out the MappingJackson2XmlHttpMessageConverter stuff...
    public IndeedResponse getIndeedJobList() {
        /*
         * List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
         * for (HttpMessageConverter<?> converter : messageConverters) {
         * if (converter.canRead(IndeedResponse.class, MediaType.APPLICATION_XML)) {
         * if (converter instanceof MappingJackson2XmlHttpMessageConverter || converter instanceof
         * MappingJackson2HttpMessageConverter) {
         * MappingJackson2XmlHttpMessageConverter xmlConverter =
         * (MappingJackson2XmlHttpMessageConverter) converter;
         * xmlConverter.getObjectMapper()
         * .configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
         * xmlConverter.getObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY
         * , true);
         * xmlConverter.getObjectMapper().configure(DeserializationFeature.
         * ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
         * true);
         * // xmlConverter.getObjectMapper().registerModule(new XmlWhitespaceModule());
         * }
         * }
         * }
         */

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

    public void doIt(String apiKey, String searchTerm) {
        List<BufferedImage> photos = searchPhotos(apiKey, searchTerm);
        showPhotos(searchTerm, photos);
    }

    // http://api.indeed.com/ads/apigetjobs?publisher=1652353865637104&jobkeys=5e418a95a6a772c1&v=2&format=xml
    public String getIndeedJobDetails(String url) {
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> converter : messageConverters) {
            if (converter.canRead(Source.class, MediaType.APPLICATION_XML)
                    && ! converter.canRead(Source.class, MediaType.TEXT_HTML)) {
                if (! (converter instanceof SourceHttpMessageConverter<?>)) {
                    continue;
                    // throw new RuntimeException("Unknown XML HttPmessageConverter type: " + converter.getClass().getName());
                }
                
                SourceHttpMessageConverter<?> xmlConverter = (SourceHttpMessageConverter<?>) converter;
                List<MediaType> mediaTypes = new ArrayList<MediaType>(xmlConverter.getSupportedMediaTypes());
                mediaTypes.add(MediaType.TEXT_HTML);
                xmlConverter.setSupportedMediaTypes(mediaTypes);
            }
        }

        Source photos = restTemplate.getForObject(url, Source.class);
        
        final String jobXPath = "/html/body/table[2]/tr/td[1]/div[@id=\"job_header\"]";
        List<String> evaluate = xpathTemplate.evaluate(jobXPath, photos, new NodeMapper() {
            public Object mapNode(Node node, int i) throws DOMException {
                Element photo = (Element) node;

                Map<String, String> variables = new HashMap<String, String>(3);
                variables.put("server", photo.getAttribute("server"));
                variables.put("id", photo.getAttribute("id"));
                variables.put("secret", photo.getAttribute("secret"));

                return restTemplate.getForObject(jobXPath, String.class, variables);
            }
        });

        return evaluate.get(0);
    }

    @SuppressWarnings("unchecked")
    private List<BufferedImage> searchPhotos(String apiKey, String searchTerm) {
        String photoSearchUrl = "http://www.flickr.com/services/rest?method=flickr.photos.search&api+key={api-key}&tags={tag}&per_page=10";
        Source photos = restTemplate.getForObject(photoSearchUrl, Source.class, apiKey, searchTerm);

        final String photoUrl = "http://static.flickr.com/{server}/{id}_{secret}_m.jpg";
        return (List<BufferedImage>) xpathTemplate.evaluate("//photo", photos, new NodeMapper() {
            public Object mapNode(Node node, int i) throws DOMException {
                Element photo = (Element) node;

                Map<String, String> variables = new HashMap<String, String>(3);
                variables.put("server", photo.getAttribute("server"));
                variables.put("id", photo.getAttribute("id"));
                variables.put("secret", photo.getAttribute("secret"));

                return restTemplate.getForObject(photoUrl, BufferedImage.class, variables);
            }
        });
    }

    private void showPhotos(String searchTerm, List<BufferedImage> imageList) {
        JFrame frame = new JFrame(searchTerm + " photos");
        frame.setLayout(new GridLayout(2, imageList.size() / 2));
        for (BufferedImage image : imageList) {
            frame.add(new JLabel(new ImageIcon(image)));
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
