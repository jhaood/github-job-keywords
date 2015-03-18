package com.aestheticsw.jobkeywords.service.termextractor.repository;

import java.util.List;

import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;

public interface TermFrequencyResultsRepositoryCustom {

    List<QueryKey> findDistinctQueryKeys();

    void deleteByQueryKey(QueryKey queryKey);
}
