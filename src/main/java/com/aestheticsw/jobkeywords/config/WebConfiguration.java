package com.aestheticsw.jobkeywords.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Configures the web layer to expose the CSS and Javascript files and to render either JSON or XML
 * in response to REST calls.
 */
@Configuration
// The @EnableWebMvc annotation disabled the automatic configuration of the
// ContentNegotiatingViewResolver so this class has to re-configure the content negotiator
@EnableWebMvc
// TODO is @EnableAutoConfiguration is always on by default ?
// @EnableAutoConfiguration()
// TODO Add these annotations when spring-data gets plugged in
// TODO perhaps these annotations should be split among the service and web configuraiton. ? ?
// @EnableSpringDataWebSupport
// @EnableJpaRepositories(basePackages = "com.aestheticsw.jobkeywords.dao")
public class WebConfiguration extends WebMvcConfigurerAdapter {

    /**
     * <pre>
     * @see http://stackoverflow.com/questions/27789277/how-to-handle-html-page-using-contentnegotiation-but-not-through-jsp-internal
     * </pre>
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.setOrder(0);
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/javadoc/**").addResourceLocations("classpath:/static/javadoc/");
        registry.addResourceHandler("/image/**").addResourceLocations("/static/image/");
    }

    /**
     * The configureContentNegotiation() method configures the request-URL extensions to pull
     * either XML or JSON from the rest controllers.
     * 
     * This allows the client to specify either *.json
     * or *.xml which will pull the correct type - but only as long as the controller method does
     * not constrain the return type with the "produces" annotation attribute.
     * 
     * - Enables path extension. Note that favor does not mean use one approach in preference to
     * another, it just enables or disables it. The order of checking is always path extension,
     * parameter, Accept header.
     * 
     * - Enable the use of the URL parameter but instead of using the default parameter, format, we
     * will use mediaType instead.
     * 
     * - Ignore the Accept header completely. This is often the best approach if most of your
     * clients are actually web-browsers (typically making REST calls via AJAX).
     * 
     * - Don't use the JAF, instead specify the media type mappings manually - we wish to
     * support JSON and XML - and HTML (via Thymeleaf templates).
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(true).favorParameter(true).parameterName("mediaType").ignoreAcceptHeader(true)
            .defaultContentType(MediaType.APPLICATION_JSON).useJaf(false).mediaType("xml", MediaType.APPLICATION_XML)
            .mediaType("json", MediaType.APPLICATION_JSON);
    }

    /*
     * Don't need special configuration of the MessageConverter for the REST controllers.
     * KEEP THIS CODE FOR WHEN DESERIALIZATION or SERIALIZATION EVENTUALLY NEEDS TO GET TWEAKED
     * Chrome json plugin strips the quotes off attribute-names. Have to pull JSON response from
     * source or from Dev-Tools.
     */
    // @Override
    // public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    // final MappingJackson2HttpMessageConverter converter = new
    // MappingJackson2HttpMessageConverter();
    //
    // final ObjectMapper objectMapper = new ObjectMapper();
    // objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    // // objectMapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
    //
    // converter.setObjectMapper(objectMapper);
    // converters.add(converter);
    // super.configureMessageConverters(converters);
    // }

}
