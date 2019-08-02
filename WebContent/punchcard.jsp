<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<body>
	<div class="punchcard-view">
		<c:set var="disabled" value="disabled='disabled'" />
		<div class="last-punch-info">
			<c:if test="${not empty user }">
  Hello <c:out value="${user.firstName}" />
				<br />
  You last 
  <c:choose>
					<c:when test="${lastPunch.punchType==true}">
						<font color="green"> Clocked in</font>
					</c:when>
					<c:when test="${lastPunch.punchType==false}">
						<font color="red"> Clocked out</font>
					</c:when>
				</c:choose> at <c:out value="${lastPunch.neutralPunch}" />
			</c:if>
		</div>
		<div class="button-container">
		  <form action="PunchCard" method="post">
			<button <c:out value="${lastPunch.punchType==true ? disabled : '' }"/>>Clock in</button> 
			<button <c:out value="${lastPunch.punchType==false||lastPunch.punchType==null ? disabled : '' }"/>>Clock out</button>
			</form>
		</div>
	</div>
</body>
