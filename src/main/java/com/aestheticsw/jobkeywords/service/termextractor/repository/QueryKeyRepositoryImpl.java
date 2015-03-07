package com.aestheticsw.jobkeywords.service.termextractor.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;

public class QueryKeyRepositoryImpl implements QueryKeyRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    // TODO convert to compound primary key for QueryKey
    public QueryKey findByCompoundKey(QueryKey queryKey) {
        TypedQuery<QueryKey> query =
            entityManager.createQuery(
                "select key from QueryKey key where key.query = ?0 and key.locale = ?1 and key.city = ?2", QueryKey.class);
        query.setParameter(0, queryKey.getQuery());
        query.setParameter(1, queryKey.getLocale());
        query.setParameter(2, queryKey.getCity());
        
        // Avoid the EmptyResultDataAccessException by pulling a list and using only the single element
        List<QueryKey> results = query.getResultList();
        if (results.size() > 1) {
            throw new IllegalStateException("Found >1 QueryKey");
        } else if (results.size() == 0) {
            return null;
        } 
        return results.get(0);
    }

}
