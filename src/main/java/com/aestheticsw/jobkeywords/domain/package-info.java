/**
 * The data model is divided into support for the Indeed API and the term-frequency data.
 * 
 * ./indeed/ - The data model for Indeed.com's job lists. This is tuned for desearializing XML from the
 * Indeed REST API.
 * <p/>
 * 
 * ./termfrequency/ - The keyword extraction data model that represents either the results from a
 * single search, or the accumulated results across multiple searches for a given query-expression
 * and location.
 * <p/>
 * 
 */
package com.aestheticsw.jobkeywords.domain;