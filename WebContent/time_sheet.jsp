<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.ArrayList" %>

<body>
<br/><br/><br/><br/><br/><br/>
<table id="timesheet-table">
<thead><th>Employee ID</th><th>Clocked in</th><th>Clocked out</th><th>Hours worked</th></thead>
<c:forEach var="punch" items="${punches}" >
  <tr>
  <td><c:out value="${punch.employeeId}"/></td><td><c:out value="${punch.clockIn}"/></td>
  <td><c:out value="${punch.clockOut}"/></td> <td><c:out value="${punch.hoursWorked}"/></td>
  </tr>
</c:forEach>
</table>
</body>
