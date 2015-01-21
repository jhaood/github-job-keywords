package com.aestheticsw.jobkeywords.service.termextractor;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class FiveFiltersService {

    @Log
    private Logger log;

    @Autowired
    private RestTemplate restTemplate;

    public String getKeywords(String content) {
        String query = "http://termextract.fivefilters.org/extract.php";
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("text", content);
        params.add("output", "json");
        String response = restTemplate.postForObject(query, params, String.class);

        log.debug("Response: " + response);

        return response;
    }
}