package com.aestheticsw.jobkeywords.service.termextractor.repository;

import java.util.List;

import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;

/**
 * The transaction semantics for custom repositories are not managed by Spring like auto-generated
 * Repository implementations. Custom repositories must declare methods @Transactions if the update
 * the DB.
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
public interface TermFrequencyResultsRepositoryCustom {

    List<QueryKey> findDistinctQueryKeys();

    void deleteByQueryKey(QueryKey queryKey);
}
