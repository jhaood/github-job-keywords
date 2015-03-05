package com.aestheticsw.jobkeywords.service.termextractor.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * QueryKeyList is not persistent, It is only used to serialize QueryKeys into a JSON string. 
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class QueryKeyList {
    private List<QueryKey> queryKeyList;

    public QueryKeyList(Collection<QueryKey> queryKeys) {
        if (queryKeys == null) {
            queryKeys = new ArrayList<>();
        }
        queryKeyList = new ArrayList<QueryKey>(queryKeys);
    }

    public QueryKeyList(Collection<QueryKey> queryKeys, Comparator<QueryKey> comparator) {
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
