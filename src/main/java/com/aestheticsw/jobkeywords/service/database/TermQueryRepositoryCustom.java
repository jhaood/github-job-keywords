package com.aestheticsw.jobkeywords.service.database;

import java.util.List;

import com.aestheticsw.jobkeywords.domain.termfrequency.QueryKey;

public interface TermQueryRepositoryCustom {

    List<QueryKey> findDistinctQueryKeys();

}
