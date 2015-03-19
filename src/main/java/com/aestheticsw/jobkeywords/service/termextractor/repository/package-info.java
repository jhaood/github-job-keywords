/**
 * The JPA repositories are configured to save and retrieve job summaries and term-frequency results
 * in a database. Configuration classes and property-files control whether the repositories connect
 * to either an embedded H2 database or a live MySQL server.
 * <p/>
 * The
 * {@link com.aestheticsw.jobkeywords.service.termextractor.repository.TermFrequencyResultsDataManager}
 * class provides the primary API that the service-implementation should use to manage
 * TermFrequencyResults is
 * 
 * Spring-data repository implementations are automatically generated for all these repositories.
 * Customization was required because the simple CRUD API is not good enough for some use-cases.
 * <p/>
 * 
 * The transaction manager is automatically configured to open read-only or read-write transactions
 * for the auto-generated repository implementations. BUT CUSTOM REPOSITORY implementations require
 * specific @Transactional annotations to open read-write transactions at the repository API
 * boundary.
 * <p/>
 * 
 * @see com.aestheticsw.jobkeywords.service.termextractor.repository.TermFrequencyResultsDataManager
 * @see com.aestheticsw.jobkeywords.service.termextractor.repository.TermFrequencyResultsRepositoryImpl#deleteByQueryKey(com.aestheticsw.jobkeywords.service.termextractor.domain.QueryKey)
 */

package com.aestheticsw.jobkeywords.service.termextractor.repository;