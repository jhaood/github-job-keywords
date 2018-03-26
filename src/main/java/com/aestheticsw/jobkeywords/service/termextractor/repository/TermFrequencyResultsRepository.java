/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */

package com.aestheticsw.jobkeywords.service.termextractor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyResults;

public interface TermFrequencyResultsRepository extends JpaRepository<TermFrequencyResults, Long>,
        TermFrequencyResultsRepositoryCustom {

    // TODO imlement pageable API - Page<TermFrequencyResults> findAll(Pageable pageable);
    
    List<TermFrequencyResults> findAll();

    TermFrequencyResults findByQueryKey(QueryKey queryKey);

    TermFrequencyResults save(TermFrequencyResults termFrequencyResults);

    void delete(TermFrequencyResults entity);

}
