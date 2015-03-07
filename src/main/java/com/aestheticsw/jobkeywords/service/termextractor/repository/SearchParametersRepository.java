package com.aestheticsw.jobkeywords.service.termextractor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;

public interface SearchParametersRepository extends JpaRepository<SearchParameters, Long>,
        SearchParametersRepositoryCustom {

    SearchParameters save(SearchParameters searchParameters);

}
