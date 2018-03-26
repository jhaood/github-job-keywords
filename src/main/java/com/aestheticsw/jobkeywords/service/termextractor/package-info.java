/**
 * The termextractor package follows a micro-services architecture. The top-level packages define
 * the API ({@link com.aestheticsw.jobkeywords.service.termextractor.TermExtractorService}), the
 * domain classes passed through the API and the configuration to run the service.
 * <p/>
 * The JPA repository is also a top-level package but should probably be buried inside the service
 * "impl" package because client's of the service-API should never call the repository methods
 * themselves.
 * <p/>
 * The following packages:
 * <ul>
 * <li>{@link com.aestheticsw.jobkeywords.service.termextractor.config} - Configuration classes
 * required for a client to invoke the {@link TermExtractorServoce} API</li>
 * <li>{@link com.aestheticsw.jobkeywords.service.termextractor.domain} - The data model required
 * for clients to call the {@link TermExtractorServoce} API</li>
 * <li>{@link com.aestheticsw.jobkeywords.service.termextractor.impl} - Buries the implementation
 * classes behind the {@link TermExtractorServoce} API</li>
 * <li>{@link com.aestheticsw.jobkeywords.service.termextractor.repository} - Contains the JPA
 * repositories.</li>
 * <li>{@link com.aestheticsw.jobkeywords.service.termextractor.support} - Random support classes</li>
 * </ul>
 * 
 * TODO Move the repository into the service/impl package. This would change the config.
 */
package com.aestheticsw.jobkeywords.service.termextractor;