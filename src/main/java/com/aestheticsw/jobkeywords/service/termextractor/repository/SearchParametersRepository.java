/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.service.termextractor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;

public interface SearchParametersRepository extends JpaRepository<SearchParameters, Long>,
        SearchParametersRepositoryCustom {

    SearchParameters save(SearchParameters searchParameters);

}
