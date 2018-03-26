/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.service.termextractor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;

public interface QueryKeyRepository extends JpaRepository<QueryKey, Long>, QueryKeyRepositoryCustom {

    QueryKey save(QueryKey queryKey);
    
    // @Query sucks - the syntax is basically undocumented beyond the most simple scenarios. 
    // @Query("select key from #{#entityName} key where key.query = :queryKey.query and key.locale = :queryKey.locale and key.city = :queryKey.city")
    // QueryKey findByCompoundKey(@Param("queryKey") QueryKey queryKey);
    
}
