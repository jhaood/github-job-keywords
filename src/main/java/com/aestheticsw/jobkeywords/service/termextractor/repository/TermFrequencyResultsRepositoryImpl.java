package com.aestheticsw.jobkeywords.service.termextractor.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;

public class TermFrequencyResultsRepositoryImpl implements TermFrequencyResultsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QueryKey> findDistinctQueryKeys() {
        TypedQuery<QueryKey> query =
            entityManager.createQuery(
                "select new com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey(key.query, key.locale, key.city) "
                    + " from QueryKey key group by key.query, key.locale, key.city", QueryKey.class);

        List<QueryKey> results = query.getResultList();
        return results;
    }

}
