package com.aestheticsw.jobkeywords.service.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.aestheticsw.jobkeywords.domain.termfrequency.QueryKey;

public class TermQueryRepositoryImpl implements TermQueryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QueryKey> findDistinctQueryKeys() {
        TypedQuery<QueryKey> query =
            entityManager
                .createQuery(
                    "select new com.aestheticsw.jobkeywords.domain.termfrequency.QueryKey(key.query, key.locale, key.city) "
                    + " from QueryKey key group by key.query, key.locale, key.city",
                    QueryKey.class);

        List<QueryKey> results = query.getResultList();
        return results;
    }

}
