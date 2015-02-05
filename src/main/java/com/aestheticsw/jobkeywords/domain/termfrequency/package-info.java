/**
 * The term-frequency data model represents the accumulated results for a unique set of search
 * parameters.
 * 
 * The TermFrequency class is central to the data model. A given instance holds the term and the
 * number of occurrences determined by the keyword extractor service.
 * 
 * A job search is defined as a query expression, location and several parameters that the
 * Indeed API exposes as stored in the SearchParameters class. Location is composed of a Country,
 * City and Radius around the City. Additional search parameters allow the request to page through
 * several batches of results. The Indeed API will only return a max of 25 jobs so the client may
 * need to initiate multiple searches to accumulate significant keyword data.
 * 
 * Since the Indeed API is limited to 25 jobs per search, this app can accumulate results
 * from multiple searches as long as the Query, Country & City are the same. The QueryKey class
 * defines the subset of search parameters that makes it easy to accumulate along this reduced
 * set of parameters. 
 * 
 * @see SearchParameters
 * @see QueryKey
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */

package com.aestheticsw.jobkeywords.domain.termfrequency;

