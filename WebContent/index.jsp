<!doctype html>
<html lang="en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=yes">

<title>Time clock project</title>
<link rel="stylesheet" type="text/css" href="css/styles.css" />
</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="view">
	 <div class="pages">
			<c:set var="headURL" scope="application" value="login.jsp" />
			<jsp:include page="${headURL}"></jsp:include>
		</div>
	</div>

<footer>
  <%@ include file="footer.jsp"%>
</footer>
</body>
</html>