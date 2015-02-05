/**
 * <pre>
 * Here's what the data model looks like, as a hierarchical text discription. 
 * 
 * TermQueryRepository - manages the accumulation of search results.
 *    The Repository holds a map of QueryKey to TermFrequencyResults.  
 *              A "unique query-key is a subset of the full search parameters that uniquely identifies
 *              a TermFrequencyResults that will accumulate results from subsequent searches.  
 *    Map<QueryKey, TermFrequencyResults>
 *    
 * TermFrequencyResults - Represents a collection of TermFrequency results for the accumulated 
 *    searches that were searched based on the same QueryKey. The QueryKey is a subset of a given 
 *    set of SearchParameters. 
 * 
 *    Contains a Map of TermFrequency objects and a List of SearchParameters.
 *              The map allows a fast look of a TermFrequency object by term-name.   
 *              The Map is indexed on term-name and maps to the TermFrequency instance 
 *    The List saves the search parameters for each search that has been accumulated 
 *              into this set of results.  
 *    Map<term-name, TermFrequency>
 *    List<SearchParameters>
 * </pre>
 */
package com.aestheticsw.jobkeywords.domain;