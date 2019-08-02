<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.ArrayList"%>

<body>

	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<form>
		<table>
			<tr>
				<th>Employee ID</th>
				<th>Employee name</th>
				<th>Employee type</th>
				<th>View Time Sheet</th>
			</tr>
			<c:forEach var="row" items="${rows}">
				<c:if test="${row.employeeId != user.employeeId}">
					<tr>
						<td><c:out value="${row.employeeId}" /></td>
						<td><c:out value="${row.firstName} ${row.lastName}" /></td>
						<td><c:out value="${row.employeeType}" /></td>

						<td class="form-td">

							<form class="table-form" method="get">
								<input type="hidden" name="employee_id"
									value="${row.employeeId}">
								<button type="submit" formaction="Manager">Edit</button>
							</form>

						</td>
					</tr>
				</c:if>
			</c:forEach>
		</table>
	</form>
</body>
