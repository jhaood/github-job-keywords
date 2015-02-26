package com.aestheticsw.jobkeywords.domain.termfrequency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * QueryList is not persistent, It is only used to sort QueryKeys. 
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
//TODO change NON_EMPTY because it should be Include.EMPTY
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QueryList {
    private List<QueryKey> queryKeyList;

    public QueryList(Collection<QueryKey> queryKeys) {
        if (queryKeys == null) {
            queryKeys = new ArrayList<>();
        }
        queryKeyList = new ArrayList<QueryKey>(queryKeys);
    }

    public QueryList(Collection<QueryKey> queryKeys, Comparator<QueryKey> comparator) {
        this(queryKeys);
        queryKeyList.sort(comparator);
    }

    public boolean hasResults() {
        return queryKeyList.size() > 0;
    }

    public List<QueryKey> getQueryKeyList() {
        return queryKeyList;
    }
}
