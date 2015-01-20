<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<body>
	Error status: ${status} at ${timestamp}
	<br>
	Error: ${error}	
	<br>
	Page: ${path}
	<br>
	Message: ${message}
</body>

</html>
