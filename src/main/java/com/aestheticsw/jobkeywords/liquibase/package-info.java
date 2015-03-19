/**
 * Liquibase is implemented as part of the web application so it can run in a production
 * environment. Several features are supported:
 * <ul>
 * <li>"update" - Apply the Liquibase changesets to a production database.</li>
 * <li>"validate" - Validate the Liquibase changesets against the production database.</li>
 * <li>"diff" - Compare two schemas generated entirely by Hibernate's HBM2DDL and by the Liquibase
 * changesets.</li>
 * </ul>
 * 
 * The Liquibase support is written as if it is an independent application. However it is not a
 * separate utility because I didn't want to increase the complexity of the maven POM.
 * 
 * @see com.aestheticsw.jobkeywords.liquibase.LiquibaseActuator
 * @see com.aestheticsw.jobkeywords.liquibase.LiquibaseRuntimeConfiguration 
 */

package com.aestheticsw.jobkeywords.liquibase;