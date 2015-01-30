package com.aestheticsw.jobkeywords.web.thymeleaf;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import net.exacode.spring.logging.inject.Log;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aestheticsw.jobkeywords.domain.indeed.JobListResponse;
import com.aestheticsw.jobkeywords.domain.termfrequency.QueryKey;
import com.aestheticsw.jobkeywords.domain.termfrequency.SearchParameters;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermFrequencyResults;
import com.aestheticsw.jobkeywords.domain.termfrequency.TermList;
import com.aestheticsw.jobkeywords.service.indeed.IndeedService;
import com.aestheticsw.jobkeywords.service.termextractor.TermExtractorService;
import com.aestheticsw.jobkeywords.utils.SearchUtils;

@Controller
@RequestMapping(value = "/job")
public class JobController {
    @Log
    private Logger log;

    @Autowired
    private IndeedService indeedService;

    @Autowired
    private TermExtractorService termExtractorService;

    // @RequestMapping(value = "/indeed", method = RequestMethod.GET, produces = {
    // MediaType.APPLICATION_JSON_VALUE,
    // MediaType.APPLICATION_XML_VALUE })
    @RequestMapping(value = "/joblist", method = RequestMethod.GET)
    public String
            getIndeedJobList(Map<String, Object> model,
                    @RequestParam(required = false, defaultValue = "Java Spring") String query, @RequestParam(
                            required = false, defaultValue = "2") int jobCount, @RequestParam(required = false,
                            defaultValue = "0") int start,
                    @RequestParam(required = false, defaultValue = "US") String country,
                    @RequestParam(required = false) String city,
                    @RequestParam(required = false, defaultValue = "0") int radius,
                    @RequestParam(required = false) String sort) {

        Locale locale = Locale.US;
        if (country != null) {
            locale = SearchUtils.lookupLocaleByCountry(country);
        }

        SearchParameters params = new SearchParameters(query, jobCount, start, locale, city, radius, sort);

        JobListResponse jobListResponse = indeedService.getIndeedJobList(params);
        model.put("joblist", jobListResponse);

        return "joblist";
    }

    @RequestMapping(value = "/keywords", method = RequestMethod.GET)
    public String showKeywordsPage(@ModelAttribute SearchFormBean searchFormBean) {
        return "keywords";
    }

    @RequestMapping(value = "/keywords", method = RequestMethod.POST)
    public ModelAndView
            getTermListForSearchParameters(@Valid SearchFormBean searchFormBean, BindingResult result,
                    RedirectAttributes redirect,
                    @RequestParam(required = false) boolean isAjaxRequest,
                    @RequestParam(required = false, defaultValue = "Java Spring") String query, 
                    @RequestParam(
                            required = false, defaultValue = "2") int jobCount, 
                    @RequestParam(required = false,
                            defaultValue = "0") int start,
                    @RequestParam(required = false, defaultValue = "US") String country,
                    @RequestParam(required = false) String city,
                    @RequestParam(required = false, defaultValue = "0") int radius,
                    @RequestParam(required = false) String sort) throws IOException {
        if (result.hasErrors()) {
            return new ModelAndView("keywords", "formErrors", result.getAllErrors());
        }

        Locale locale = Locale.US;
        if (StringUtils.isNoneEmpty(country)) {
            locale = SearchUtils.lookupLocaleByCountry(country);
        }
        SearchParameters params = new SearchParameters(query, jobCount, start, locale, city, radius, sort);

        TermList termList = termExtractorService.extractTerms(params);

        if (isAjaxRequest) {
            // This only draws the inner fragment - doesn't replace without page reload
            return new ModelAndView("keywords :: query-results", "results", termList);
        }
        return new ModelAndView("keywords", "results", termList);
    }

    @RequestMapping(value = "/keywords-accumulated", method = RequestMethod.GET)
    public String getAccumulatedTermFrequencyResults(Map<String, Object> model, @RequestParam(required = false,
            defaultValue = "Java Spring") String query,
            @RequestParam(required = false, defaultValue = "US") String country,
            @RequestParam(required = false) String city) throws IOException {

        Locale locale = Locale.US;
        if (country != null) {
            locale = SearchUtils.lookupLocaleByCountry(country);
        }
        QueryKey queryKey = new QueryKey(query, locale, city);

        TermFrequencyResults results = termExtractorService.getAccumulatedTermFrequencyResults(queryKey);

        return "keywords-accumulated";
    }

}