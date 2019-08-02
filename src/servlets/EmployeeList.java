/*
 *  File: EmployeeList.java
 * Date: 8/2/2019
 * Author: Christian Rondon
 * Description: This Java Servlet is used to handle the request of a display of a list of employees that are stored in the database.
 * 
 */



package servlets;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;
import models.DBCredentials;

@WebServlet("/EmployeeList")
public class EmployeeList extends HttpServlet{



	@Override
	/* This doGet() method is used to retrieve the list of employees for display and later editing of each employees time sheet.
	 * The method purposely excludes the employee from the list that is logged in so the user cannot edit their own hours.
	 * */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Initiation of local variables
		ArrayList<Employee> rows = new ArrayList<Employee>(); 
		Connection myConn = null; 
		Statement myStmt = null; 
		ResultSet myRs = null;
		RequestDispatcher requestDispatcher;
		HttpSession session = req.getSession();
		Employee user = (Employee)session.getAttribute("user");	//retrieval of the bean stored for the user that is logged in

		//If the user stored is null, the session timed out.
		if(user == null) {
			session.setAttribute("headURL","login.jsp"); //set the view to the login screen
			requestDispatcher = req.getRequestDispatcher("index.jsp"); //redirect to index.jsp
			requestDispatcher.forward(req, resp);
		}

		//try block will attempt to create a connection to the database, retrieve the list of employees and forward the list 
		try {
			
			//set credentials and send query to the database
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", DBCredentials.USERNAME, DBCredentials.PASSWORD);
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from employee");
			
			/*handles the result set by instantiating a class Employee for each row of data and adding the class
			 * to the array list.
			 */
			while (myRs.next()) {
				Employee newEmployee = new Employee();
				newEmployee.setEmployeeId(myRs.getInt("EmployeeID"));
				newEmployee.setFirstName(myRs.getString("FirstName"));
				newEmployee.setLastName(myRs.getString("LastName"));
				newEmployee.setEmployeeType(myRs.getString("EmployeeType"));
				if(user.getEmployeeId() != newEmployee.getEmployeeId()) {
					rows.add(newEmployee);
				}
			}

			//store the list of employees and forward it to employees.jsp
			req.setAttribute("rows", rows);
			session.setAttribute("headURL","employees.jsp");
			requestDispatcher = req.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(req, resp);
			
		}catch (Exception e) {//catches any exceptions found while communicating with the database.
			System.out.println("EXCEPTION");
			System.out.println(e);
		}
		finally {
			try {//close the result set and connection
				myConn.close();
				myRs.close();
			} catch (SQLException e) {//catches exceptions while attempting to close a result set and connection.
				e.printStackTrace();
			}
		}

	}

}
