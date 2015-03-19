# github-public
This web application extracts keywords from job descriptions on indeed.com. 

The app demonstrates how to integrate several REST APIs with the Spring Boot and Spring-4.1 framework. The app is running on Amazon's AWS cloud. 

Job Keyword Search App: http://54.148.208.180:8080/job/search

Job Keyword App JavaDoc: http://54.148.208.180:8080/javadoc/index.html

Job Keyword App Source Code: https://github.com/jhaood/github-public

Here are a few command lines to help build and deploy the app:

	"mvn clean javadoc:javadoc install" => build and run tests against an embedded H2 database
	
	"mvn clean javadoc:javadoc install -P dev" => run tests against the MySQL "job_db" database
	
	"mvn -DskipTests spring-boot:run -P prod-liquibase" => launch the app and run liquibase-validate 
	
	"mvn -DskipTests spring-boot:run -P prod-liquibase -Djobkeywords.liquibase.update=true"
			=> launch the app and run a liquibase-update with Spring Boot configures the app-context. 
		
	"java -Dspring.profiles.active=mysql,liquibase -Dspring.jpa.hibernate.ddl-auto=validate 
		-Djobkeywords.liquibase.update=false -jar /home/ec2-user/job-keywords-2.0-SNAPSHOT.jar"
			=> launch the JAR as a Spring Boot app and run HBM2DDL-validate and liquibase-validate
		
	"java -Dspring.profiles.active=mysql,liquibase -Dspring.jpa.hibernate.ddl-auto=none 
		-Djobkeywords.liquibase.update=true -jar /home/ec2-user/job-keywords-2.0-SNAPSHOT.jar"
			=> launch the Spring Boot app and run liquibase-update (HBM2DDL does NOT run)


Spring Boot: 

Spring-Boot version "1.2.1.RELEASE" launches a web container and configures the app-context based on a ton of automatic configuration. The job-keywords application defines specific Configuration classes to override or extend the auto-generated configuration from Spring-Boot. 

Testing: 

The naming conventions for test classes is

*UnitTest - a true unit test that may mock dependent classes. A unit test can test a cluster of classes but must not require Spring or any external resources. An external resource is something that lives outside the JVM.

*Test - an integration test that requires spring to configure the class or it's dependencies. The integration test may or may not hit resources outside the JVM.

*IT - An integration test that lives outside the web-app and requires a container to be launched by the failsafe plugin. The only *IT.java test is JobKeywordsApplicationIT - which itself is not a true out-of-container integration test.


