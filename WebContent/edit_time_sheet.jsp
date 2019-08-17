<!-- 
Filename: edit_time_sheet.jsp
Author:Christian Rondon
Date: 8/17/2019
Description: This file serves as the view for the timesheet editor for a chosen employee by the manager or HR 
			representative. 
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.ArrayList"%>

<body>

	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<form action="Manager" method="post">
	  
		<table>
			<tr>
				<th>Employee ID</th>
				<th>Clocked in</th>
				<th>Clocked out</th>
				<th>Hours worked</th>
			</tr>
			<c:forEach var="punch" items="${punches}">
			 <input type="hidden" name="employee_id" value="${punch.employeeId}">
				<tr>
					<td><c:out value="${punch.employeeId}" /></td>
					<td><c:out value="${punch.punchInId}"/><input type="datetime-local" value="${punch.clockIn}" name="${punch.punchInId}">
					</td>
					<td><c:out value="${punch.punchOutId}"/><input type="datetime-local" value="${punch.clockOut}" name="${punch.punchOutId}">
					</td>
					<td><c:out value="${punch.hoursWorked}" /></td>
				</tr>
			</c:forEach>
		</table>
		<button>Cancel</button>
		<button type="submit">Edit</button>

	</form>
</body>
