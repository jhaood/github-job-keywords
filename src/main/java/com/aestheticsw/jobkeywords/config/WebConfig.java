package com.aestheticsw.jobkeywords.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * This class isn't needed yet because Spring Boot enables MVC.
 * But this will allow changing the converters and other stuff.
 */
@EnableWebMvc
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    /**
     * Total customization - see below for explanation.
     * 
     * This can be used if @RequestMapping doesn't specify products=...
     * 
     * @Override
     *           public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
     *           configurer.favorPathExtension(false).
     *           favorParameter(true).
     *           parameterName("mediaType").
     *           ignoreAcceptHeader(true).
     *           useJaf(false).
     *           defaultContentType(MediaType.APPLICATION_JSON).
     *           mediaType("xml", MediaType.APPLICATION_XML).
     *           mediaType("json", MediaType.APPLICATION_JSON);
     *           }
     */
}
