package com.aestheticsw.jobkeywords.web.thymeleaf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.exacode.spring.logging.inject.Log;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import com.aestheticsw.jobkeywords.service.termextractor.TermExtractorService;
import com.aestheticsw.jobkeywords.service.termextractor.indeed.IndeedQueryException;
import com.aestheticsw.jobkeywords.service.termextractor.indeed.IndeedService;
import com.aestheticsw.jobkeywords.utils.SearchUtils;

/**
 * This Spring MVC controller is integrated into the Thymeleaf configuration to produce an HTML5 Web
 * interface for a browser running on a desktop, smartphones or tablet.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
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

    /**
     * This exception handler passes the exception message to Thymeleaf, but suppresses the stack
     * trace from the UI.
     */
    @ExceptionHandler(Throwable.class)
    public ModelAndView handleError(HttpServletRequest req, Throwable exception) {
        log.error("Request: " + req.getRequestURL() + " raised " + exception, exception);

        ModelAndView mav = new ModelAndView();
        mav.addObject("message", exception.getMessage());
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("exception");
        return mav;
    }

    /**
     * TODO - This method isn't integrated into the UI right now, but will be soon.
     */
    // TODO replace the discrete attributes with the Thymeleaf SearchFormBean.
    @RequestMapping(value = "/joblist", method = RequestMethod.GET)
    public String getIndeedJobList(Map<String, Object> model, @RequestParam(
            required = false, defaultValue = "Java Spring") String query, @RequestParam(
            required = false, defaultValue = "2") int jobCount,
            @RequestParam(required = false, defaultValue = "0") int start, @RequestParam(
                    required = false, defaultValue = "US") String country, @RequestParam(required = false) String city,
            @RequestParam(required = false, defaultValue = "0") int radius, @RequestParam(required = false) String sort) {

        Locale locale = Locale.US;
        if (country != null) {
            locale = SearchUtils.lookupLocaleByCountry(country);
        }

        SearchParameters params = new SearchParameters(query, jobCount, start, locale, city, radius, sort);

        JobListResponse jobListResponse = indeedService.getIndeedJobList(params);
        model.put("joblist", jobListResponse);

        return "joblist";
    }

    /**
     * Render the search page in its entirety for a GET http request.
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String showKeywordsPage(@ModelAttribute SearchFormBean searchFormBean) {
        initializeSearchFormBean(searchFormBean);
        return "keywords";
    }

    /**
     * The search page relies on an AJAX call to submit the form-parameters and redraw only a
     * portion of the HTML page.
     * 
     * If a validation error occurs for an initial search then the Javascript must correctly handle
     * the case where a subsequent search corrects the validation error and draws the response.
     * Correcting a validation error requires the Javascript to redraw both the <form> and the
     * results <div> HTML.
     *
     * If the search page is in a clean state then the search results only require redrawing the
     * <div> that contains the results. When an initial validation error occurs, only the <div>
     * that contains the form must be redrawn. When an initial valication is subsequencly corrected
     * then both <div>s must be redrawn. 
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView getTermListForSearchParameters(@Valid SearchFormBean searchFormBean, BindingResult result,
            RedirectAttributes redirect, @RequestParam(required = false) boolean isAjaxRequest) {

        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("searchFormBean", searchFormBean);
        if (result.hasErrors()) {
            searchFormBean.setHadFormErrors(true);
            modelMap.put("formErrors", result.getAllErrors());

            // Return the whole HTML page and let the Javascript select which <div> tags it wants to render. 
            return new ModelAndView("keywords", modelMap);
        }

        Locale locale = Locale.US;
        if (StringUtils.isNoneEmpty(searchFormBean.getCountry())) {
            locale = SearchUtils.lookupLocaleByCountry(searchFormBean.getCountry());
        }
        SearchParameters params =
            new SearchParameters(searchFormBean.getQuery(), searchFormBean.getJobCount(), searchFormBean.getStart(),
                locale, searchFormBean.getCity(), searchFormBean.getRadius(), searchFormBean.getSort());

        TermList termList;
        try {
            termList = termExtractorService.extractTerms(params);
        } catch (IndeedQueryException e) {
            searchFormBean.setHadFormErrors(true);
            result.addError(new FieldError("searchFormBean", "query", "No results found, check query expression: "
                + params));
            modelMap.put("formErrors", result.getAllErrors());
            
            // Return the whole HTML page and let the Javascript select which <div> tags it wants to render. 
            return new ModelAndView("keywords", modelMap);
        }

        searchFormBean.setHadFormErrors(false);
        modelMap.put("results", termList);
        if (isAjaxRequest) {
            // Return only the results fragment. 
            return new ModelAndView("keywords :: query-results", modelMap);
        }
        return new ModelAndView("keywords", modelMap);
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