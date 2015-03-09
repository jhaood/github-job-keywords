package com.aestheticsw.jobkeywords.service.termextractor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.XPathOperations;

/**
 * Service layer configuration. This class CAN override the default configuration from spring-boot.
 * <p/>
 * 
 * Most of the spring-boot and spring-framework automatic configuration is used reliably now.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@Configuration
@ComponentScan(basePackages = { "com.aestheticsw.jobkeywords.service.termextractor.impl.fivefilters",
    "com.aestheticsw.jobkeywords.service.termextractor.impl.indeed"})
public class RestClientConfiguration {

    /*
     * <bean id="xpathTemplate" class="org.springframework.xml.xpath.Jaxp13XPathTemplate"/>
     */
    @Bean(name = "xpathTempate")
    public XPathOperations getXPathTemplate() {
        return new Jaxp13XPathTemplate();
    }

    @Bean(name = "restTemplate")
    public RestTemplate getRestTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(factory);

        // KEEP THIS CODE FOR WHEN SOMETHING DOES NEED TO GET CONFIGURED.
        //
        // List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        // for (HttpMessageConverter<?> converter : messageConverters) {
        //
        // if (converter.canRead(JobListResponse.class, MediaType.APPLICATION_XML)) {
        // if (converter instanceof AbstractJaxb2HttpMessageConverter) {
        // AbstractJaxb2HttpMessageConverter xmlConverter = (AbstractJaxb2HttpMessageConverter)
        // converter;
        // xmlConverter.canRead(JobListResponse.class, MediaType.APPLICATION_XML);
        // }
        // }
        // }

        return restTemplate;
    }

}
