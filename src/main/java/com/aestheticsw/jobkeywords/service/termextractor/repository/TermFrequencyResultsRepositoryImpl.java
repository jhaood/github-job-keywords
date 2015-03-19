package com.aestheticsw.jobkeywords.service.termextractor.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey;
import com.aestheticsw.jobkeywords.service.termextractor.domain.TermFrequencyResults;

public class TermFrequencyResultsRepositoryImpl implements TermFrequencyResultsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TermFrequencyResultsRepository springRepository;

    @Override
    public List<QueryKey> findDistinctQueryKeys() {
        // TODO convert queries into cacheable named queries with query-params
        TypedQuery<QueryKey> query =
            entityManager.createQuery(
                "select new com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey(key.query, key.locale, key.city) "
                    + " from QueryKey key group by key.query, key.locale, key.city", QueryKey.class);

        List<QueryKey> results = query.getResultList();
        return results;
    }

    @Transactional
    public void deleteByQueryKey(QueryKey queryKey) {
        /* HQL doesn't honor cascade or orphanRemoval @OneToMany attributes 
        String hql = "delete from TermFrequencyResults where queryKey = :key";
        */
        TermFrequencyResults results = springRepository.findByQueryKey(queryKey);
        if (results == null) {
            throw new RuntimeException("Delete didn't find any TermFrequencyResult objects, queryKey=" + queryKey);
        }
        springRepository.delete(results);
    }
}
