package com.aestheticsw.jobkeywords.config;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.xml.xpath.MyJaxp13XPathTemplate;
import org.springframework.xml.xpath.XPathOperations;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;

@Configuration
public class ServiceConfiguration {

    /*
     * <bean id="xpathTemplate" class="org.springframework.xml.xpath.Jaxp13XPathTemplate"/>
     */
    @Bean(name = "xpathTempate")
    public XPathOperations getXPathTemplate() {
        return new MyJaxp13XPathTemplate(); 
    }

    @SuppressWarnings("restriction")
    @Bean(name = "documentBuilderFactory")
    public DocumentBuilderFactory getDocumentBuilderFactory() {
        DocumentBuilderFactory factory = new DocumentBuilderFactoryImpl() {
            @Override
            public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
                DocumentBuilder builder = super.newDocumentBuilder();
                builder.setErrorHandler(new ErrorHandler() {
                    @Log
                    private Logger log;

                    @Override
                    public void warning(SAXParseException exception) throws SAXException {
                        log.warn("WARNING parsing exception ignored", exception);
                    }

                    @Override
                    public void error(SAXParseException exception) throws SAXException {
                        log.error("WARNING parsing exception ignored", exception);
                    }

                    @Override
                    public void fatalError(SAXParseException exception) throws SAXException {
                        log.error("WARNING parsing exception ignored", exception);
                    }
                    
                });
                return builder;
            }
        };
        return factory;
    }
}
