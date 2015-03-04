/**
 * The term-frequency data model represents the extracted terms for a unique set of job-search
 * parameters.
 * 
 * The results for multiple searches can be accumulated as long as the query-string and location are
 * the same.
 * <p/>
 * 
 * The TermFrequency class is central to the data model. A given instance holds the term and the
 * number of occurrences determined by the keyword extractor service.
 * <p/>
 * 
 * A job search is defined as a query expression, location and several parameters that the Indeed
 * API exposes as stored in the SearchParameters class. Location is composed of a Country, City and
 * Radius around the City. Additional search parameters allow the request to page through several
 * batches of results. The Indeed API will only return a max of 25 jobs so the client may need to
 * initiate multiple searches to accumulate significant keyword data.
 * <p/>
 * 
 * Since the Indeed API is limited to 25 jobs per search, this app can accumulate results from
 * multiple searches as long as the Query, Country &amp; City are the same. The QueryKey class defines
 * the subset of search parameters that makes it easy to accumulate along this reduced set of
 * parameters.
 * 
 * @see com.aestheticsw.jobkeywords.domain.TermFrequencyResults
 * @see com.aestheticsw.jobkeywords.domain.SearchParameters
 * @see com.aestheticsw.jobkeywords.domain.QueryKey
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */

package com.aestheticsw.jobkeywords.domain;

