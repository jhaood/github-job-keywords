/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.service.termextractor.repository;

import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;

public interface QueryKeyRepositoryCustom {

    QueryKey findByCompoundKey(QueryKey queryKey);

}
