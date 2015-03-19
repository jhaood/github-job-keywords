/**
 * This outer-most package contains the application configuration file which allows Spring-Boot to
 * launch the Web container and this application.
 * <p/>
 * The package hierarchy is broken up into a few top-level packages. The service layer is separated
 * from web layer which allows web controllers to access the service API as though a very simple
 * interface. This architecture is derived from recent publications about micro-services.
 * <p/>
 * This package contains the following sub-packages:
 * <ul>
 * <li>{@link com.aestheticsw.jobkeywords.liquibase} - The web application runtime can apply
 * the Liquibase changesets in production environments that don't support Maven or the build tools.</li>
 * 
 * <li>{@link com.aestheticsw.jobkeywords.service} - Exposes the JAVA API for the
 * term-extractor service and also provides a few other simple services.</li>
 * 
 * <li>{@link com.aestheticsw.jobkeywords.shared} - Application-wide classes that support REST
 * and Log-files.</li>
 * 
 * <li>{@link com.aestheticsw.jobkeywords.web} - Provides REST and HTML5 interfaces.</li>
 * </ul>
 * 
 * @see com.aestheticsw.jobkeywords.service.termextractor.config
 * @see com.aestheticsw.jobkeywords.web.config
 */

package com.aestheticsw.jobkeywords;