package com.aestheticsw.jobkeywords.service.termextractor.repository;

import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;

public interface SearchParametersRepositoryCustom {
    
    SearchParameters findByCompoundKey(SearchParameters searchParameters);

}
