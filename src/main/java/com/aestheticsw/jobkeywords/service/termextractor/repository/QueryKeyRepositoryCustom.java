package com.aestheticsw.jobkeywords.service.termextractor.repository;

import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;

public interface QueryKeyRepositoryCustom {

    QueryKey findByCompoundKey(QueryKey queryKey);

}
