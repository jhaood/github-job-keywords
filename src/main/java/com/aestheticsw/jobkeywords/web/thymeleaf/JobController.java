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
import com.aestheticsw.jobkeywords.domain.termfrequency.QueryList;
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

    private IndeedService indeedService;

    private TermExtractorService termExtractorService;

    @Autowired
    public JobController(IndeedService indeedService, TermExtractorService termExtractorService) {
        super();
        this.indeedService = indeedService;
        this.termExtractorService = termExtractorService;
    }

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

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String showKeywordsPage(@ModelAttribute SearchFormBean searchFormBean) {
        initializeSearchFormBean(searchFormBean);
        return "keywords";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView getTermListForSearchParameters(@Valid SearchFormBean searchFormBean, BindingResult result,
            RedirectAttributes redirect, @RequestParam(required = false) boolean isAjaxRequest) throws IOException {
        if (result.hasErrors()) {
            return new ModelAndView("keywords", "formErrors", result.getAllErrors());
        }

        Locale locale = Locale.US;
        if (StringUtils.isNoneEmpty(searchFormBean.getCountry())) {
            locale = SearchUtils.lookupLocaleByCountry(searchFormBean.getCountry());
        }
        SearchParameters params = new SearchParameters(searchFormBean.getQuery(),
                searchFormBean.getJobCount(),
                searchFormBean.getStart(),
                locale,
                searchFormBean.getCity(),
                searchFormBean.getRadius(),
                searchFormBean.getSort());

        TermList termList = termExtractorService.extractTerms(params);

        if (isAjaxRequest) {
            return new ModelAndView("keywords :: query-results", "results", termList);
        }
        return new ModelAndView("keywords", "results", termList);
    }

    @RequestMapping(value = "/accumulated", method = RequestMethod.GET)
    public String showAccumulatedPage(@ModelAttribute SearchFormBean searchFormBean) {
        // initialize the SearchFormBean because the UI would invoke the POST controller-method
        // from the history-page in order to prefill the SearchFormBean.
        initializeSearchFormBean(searchFormBean);
        return "accumulated";
    }

    @RequestMapping(value = "/accumulated", method = RequestMethod.POST)
    public ModelAndView getAccumulatedTermFrequencyResults(@Valid SearchFormBean searchFormBean, BindingResult result,
            RedirectAttributes redirect, @RequestParam(required = false) boolean isAjaxRequest) throws IOException {

        Locale locale = Locale.US;
        if (searchFormBean.getCountry() != null) {
            locale = SearchUtils.lookupLocaleByCountry(searchFormBean.getCountry());
        }
        QueryKey queryKey = new QueryKey(searchFormBean.getQuery(), locale, searchFormBean.getCity());

        TermFrequencyResults results = termExtractorService.getAccumulatedTermFrequencyResults(queryKey);

        if (isAjaxRequest) {
            return new ModelAndView("accumulated :: query-results", "results", results);
        }
        return new ModelAndView("accumulated", "results", results);
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ModelAndView getSearchHistory(@ModelAttribute SearchFormBean searchFormBean) {

        QueryList results = termExtractorService.getSearchHistory();

        return new ModelAndView("history", "results", results);
    }

    private void initializeSearchFormBean(SearchFormBean searchFormBean) {
        if (StringUtils.isEmpty(searchFormBean.getQuery())) {
            searchFormBean.setQuery("senior software (developer or engineer)");
        }
        if (searchFormBean.getJobCount() == 0) {
            searchFormBean.setJobCount(1);
        }
        if (searchFormBean.getCountry() == null) {
            searchFormBean.setCountry(Locale.US.getCountry());
        }
    }
}