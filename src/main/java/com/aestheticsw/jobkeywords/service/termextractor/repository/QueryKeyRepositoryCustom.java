package com.aestheticsw.jobkeywords.service.termextractor.repository;

import com.aestheticsw.jobkeywords.domain.termfrequency.QueryKey;

public interface QueryKeyRepositoryCustom {

    QueryKey findByCompoundKey(QueryKey queryKey);

}
