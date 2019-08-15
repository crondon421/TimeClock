<!--
Filename: login.jsp
Author: Christian Rondon
Date: 8/14/2019
Description: This file contains the interface of the login container. 
After entering the username and password and logging, the user will be redirected to
LoginServlet class to process the POST HTTP request -->

<div class="login-container">
	<div class="login-topbar">Time Clock Application</div>
	<div class="login-instructions">Please login to view your Time
		Sheet</div>
	<div class="login-content">
		<form id="login-form" action="LoginServlet" method="POST">
			<div class="login-input-container">
				<input type="text" class="login-input-field" name="username" placeholder="username"/>
			</div>
			<div class="login-input-container">
				<input type="password" class="login-input-field" name="password" placeholder="password">
			</div>
			<div class="login-button-container">
				<input type="submit" value="Login">
			</div>
		</form>
	</div>
</div>