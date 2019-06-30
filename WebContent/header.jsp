<html lang="en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link href="css/styles.css" rel="stylesheet" type="text/css">
<header>
<h1>Time Clock</h1>
<div class="nav-container">
<c:if test="${not empty user}">
  Welcome, <c:out value="${user.firstName} ${user.lastName}"/>!
  <c:if test="${user.employeeType == 'admin' || user.employeeType == 'hr'}">
  <a href="EmployeeList">List of Employees</a></c:if>
  <a href="TimeSheet">Time sheet</a>
  <a href="PunchCard">Punch in/out</a>
  <a href="Logout">Logout</a>
</c:if>
</div>
</header>
</html>