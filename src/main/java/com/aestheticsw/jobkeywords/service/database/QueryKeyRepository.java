package com.aestheticsw.jobkeywords.service.database;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aestheticsw.jobkeywords.domain.termfrequency.QueryKey;

// @Repository
public interface QueryKeyRepository extends JpaRepository<QueryKey, Long>, QueryKeyRepositoryCustom {

    QueryKey save(QueryKey queryKey);
    
    // @Query sucks - the syntax is basically undocumented beyond the most simple scenarios. 
    // @Query("select key from #{#entityName} key where key.query = :queryKey.query and key.locale = :queryKey.locale and key.city = :queryKey.city")
    // QueryKey findByCompoundKey(@Param("queryKey") QueryKey queryKey);
    
}
