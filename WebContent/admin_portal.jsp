<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.ArrayList" %>

<body>
<table>
<tr><th>Employee ID</th><th>Employee name</th><th>Employee type</th></tr>
<c:forEach var="row" items="${rows}" >
  <tr>
  <td><c:out value="${row.employeeId}"/></td> <td><c:out value="${row.firstName} ' ' ${row.lastName}"/></td><td><c:out value="${row.employeeType}"/></td>
  </tr>
</c:forEach>
</body>
