package com.aestheticsw.jobkeywords.service.termextractor.repository;

import java.util.List;

import com.aestheticsw.jobkeywords.domain.termfrequency.QueryKey;

// TODO rename to TermFrequenceResultsRepositoryCustom
public interface TermQueryRepositoryCustom {

    List<QueryKey> findDistinctQueryKeys();

}
