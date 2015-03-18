package com.aestheticsw.jobkeywords.service.termextractor.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;

public class SearchParametersRepositoryImpl implements SearchParametersRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    // TODO convert to compound primary key for SearchParameters
    public SearchParameters findByCompoundKey(SearchParameters searchParameters) {
        QueryKey key = searchParameters.getQueryKey();

        TypedQuery<SearchParameters> query =
            entityManager
                .createQuery(
                    "select param from SearchParameters param, QueryKey key where param.queryKey = key and key.query = ?0 and key.locale = ?1 and key.city = ?2 "
                        + " and param.jobCount = ?3 and param.start = ?4 and param.radius = ?5 and param.sort = ?6 ",
                    SearchParameters.class);
        query.setParameter(0, key.getQuery());
        query.setParameter(1, key.getLocale());
        query.setParameter(2, key.getCity());
        query.setParameter(3, searchParameters.getJobCount());
        query.setParameter(4, searchParameters.getStart());
        query.setParameter(5, searchParameters.getRadius());
        query.setParameter(6, searchParameters.getSort());

        // Avoid the EmptyResultDataAccessException by pulling a list and using only the single element
        List<SearchParameters> results = query.getResultList();
        if (results.size() > 1) {
            throw new IllegalStateException("Found >1 SearchParameters");
        } else if (results.size() == 0) {
            return null;
        }
        return results.get(0);
    }

}
