package com.aestheticsw.jobkeywords.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * This class isn't needed yet because Spring Boot enables MVC.
 * But this will allow changing the converters and other stuff.
 */
@Configuration
// @EnableWebMvc disabled the ContentNegotiatingViewResolver so have to configure it back into place
@EnableWebMvc
// @EnableAutoConfiguration()
// Add these annotations when spring-data gets plugged in
// @EnableSpringDataWebSupport
// @EnableJpaRepositories(basePackages = "com.aestheticsw.jobkeywords.dao")
public class WebConfiguration extends WebMvcConfigurerAdapter {

    /* Manual configuraiton of Thymeleaf & JSPs breaks the correct default behavior... 
     * I probably need to fix this later...
     */
//    @Bean
//    public TemplateResolver templateResolver(){
//        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
//        templateResolver.setPrefix("classpath:/templates/");
//        templateResolver.setSuffix(".html");
//        // templateResolver.setTemplateMode("HTML5");
// 
//        return templateResolver;
//    }
//    @Bean
//    public SpringTemplateEngine templateEngine(){
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.setTemplateResolver(templateResolver());
//        return templateEngine;
//    }
//    @Bean
//    public ViewResolver viewResolver(){
//        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver() ;
//        viewResolver.setTemplateEngine(templateEngine());
//        viewResolver.setOrder(1);
// 
//        return viewResolver;
//    }

    // Will map to the JSP page: "WEB-INF/views/accounts/list.jsp"
    // Disable JSPs completely... can't use JSPs with device-specific view resolver in spring autoconfiguraiton...  
//    @Value("${spring.view.prefix:}")
//    private String prefix = "";
//    @Value("${spring.view.suffix:}")
//    private String suffix = "";
//    @Bean(name = "jspViewResolver")
//    public ViewResolver getJspViewResolver() {
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setPrefix(prefix);
//        viewResolver.setSuffix(suffix);
//        viewResolver.setOrder(100);
//        return viewResolver;
//    }

    /**
     * @see http
     *      ://stackoverflow.com/questions/27789277/how-to-handle-html-page-using-contentnegotiation
     *      -but-not-through-jsp-internalvi
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.setOrder(0);
        registry.addResourceHandler("/static/css/**").addResourceLocations("/static/css/");
        registry.addResourceHandler("/static/script/**").addResourceLocations("/static/script/");
        registry.addResourceHandler("/static/image/**").addResourceLocations("/static/image/");
        // disabling top-level HTML - replacing with Thymeleaf templates
        // registry.addResourceHandler("/**/*.html").addResourceLocations("/");
    }

    /**
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
                .defaultContentType(MediaType.APPLICATION_JSON).useJaf(false)
                .mediaType("xml", MediaType.APPLICATION_XML).mediaType("json", MediaType.APPLICATION_JSON);
    }

    /**
     * Don't need special configuration of the MessageConverter for the REST controllers. 
     * 
     * KEEP THIS CODE FOR WHEN SOMETHING DOES NEED TO GET TWEAKED. 
     * 
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
