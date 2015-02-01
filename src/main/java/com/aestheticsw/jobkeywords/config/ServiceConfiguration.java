package com.aestheticsw.jobkeywords.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.XPathOperations;

@Configuration
public class ServiceConfiguration {

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
        // if (converter.canRead(IndeedResponse.class, MediaType.APPLICATION_XML)) {
        // if (converter instanceof AbstractJackson2HttpMessageConverter) {
        // AbstractJackson2HttpMessageConverter xmlConverter =
        // (AbstractJackson2HttpMessageConverter) converter;
        // *
        // xmlConverter.getObjectMapper()
        // .configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        // xmlConverter.getObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
        // true);
        // xmlConverter.getObjectMapper().configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
        // true);
        // *
        //
        // // xmlConverter.getObjectMapper().registerModule(new XmlWhitespaceModule());
        // }
        // }

        return restTemplate;
    }

    /*
     * this was an attempt to bypass XHTML parsing exceptions from malformed HTML from Indeed.
     */
    // @SuppressWarnings("restriction")
    // @Bean(name = "documentBuilderFactory")
    // public DocumentBuilderFactory getDocumentBuilderFactory() {
    // DocumentBuilderFactory factory = new DocumentBuilderFactoryImpl() {
    // @Override
    // public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
    // DocumentBuilder builder = super.newDocumentBuilder();
    // builder.setErrorHandler(new ErrorHandler() {
    // @Log
    // private Logger log;
    //
    // @Override
    // public void warning(SAXParseException exception) throws SAXException {
    // log.warn("WARNING parsing exception ignored", exception);
    // }
    //
    // @Override
    // public void error(SAXParseException exception) throws SAXException {
    // log.error("WARNING parsing exception ignored", exception);
    // }
    //
    // @Override
    // public void fatalError(SAXParseException exception) throws SAXException {
    // log.error("WARNING parsing exception ignored", exception);
    // }
    // });
    // return builder;
    // }
    // };
    // return factory;
    // }
}
