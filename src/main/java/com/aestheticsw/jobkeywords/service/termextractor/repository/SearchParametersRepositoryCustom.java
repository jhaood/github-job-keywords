package com.aestheticsw.jobkeywords.service.termextractor.repository;

import java.sql.Connection;

import com.aestheticsw.jobkeywords.service.termextractor.domain.SearchParameters;

/**
 * org.apache.tomcat.jdbc.pool.DataSource@64d4f7c7{ConnectionPool[defaultAutoCommit=null;
 * defaultReadOnly=null; defaultTransactionIsolation=-1; defaultCatalog=null;
 * driverClassName=org.h2.Driver; maxActive=100; maxIdle=100; minIdle=10; initialSize=10;
 * maxWait=30000; testOnBorrow=false; testOnReturn=false; timeBetweenEvictionRunsMillis=5000;
 * numTestsPerEvictionRun=0; minEvictableIdleTimeMillis=60000; testWhileIdle=false;
 * testOnConnect=false; password=********; url=jdbc:h2:mem:keywordtestdb;MODE=MYSQL; username=;
 * validationQuery=null; validationQueryTimeout=-1; validatorClassName=null;
 * validationInterval=30000; accessToUnderlyingConnectionAllowed=true; removeAbandoned=false;
 * removeAbandonedTimeout=60; logAbandoned=false; connectionProperties=null; initSQL=null;
 * jdbcInterceptors=null; jmxEnabled=true; fairQueue=true; useEquals=true;
 * abandonWhenPercentageFull=0; maxAge=0; useLock=false; dataSource=null; dataSourceJNDI=null;
 * suspectTimeout=0; alternateUsernameAllowed=false; commitOnReturn=false; rollbackOnReturn=false;
 * useDisposableConnectionFacade=true; logValidationErrors=false; propagateInterruptState=false;
 * ignoreExceptionOnPreLoad=false; }
 *
 * TransactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED
 * 
 * @author Jim Alexander (jhaood@gmail.com)
 */
public interface SearchParametersRepositoryCustom {
    // TRANSACTION_READ_UNCOMMITTED = -1 
    // TRANSACTION_READ_COMMITTED = -2
    // TODO FIXME ? what's this about ? 
    public  Object conn = Connection.TRANSACTION_READ_UNCOMMITTED;
    
    SearchParameters findByCompoundKey(SearchParameters searchParameters);

}
