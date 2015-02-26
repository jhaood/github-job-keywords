package com.aestheticsw.jobkeywords.service.database;

import com.aestheticsw.jobkeywords.domain.termfrequency.QueryKey;

public interface QueryKeyRepositoryCustom {

    QueryKey findByCompoundKey(QueryKey queryKey);

}
