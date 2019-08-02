/*
 *  File: LoginServlet.java
 * Date: 8/2/2019
 * Author: Christian Rondon
 * Description: This Java Servlet is used to authenticate the login information entered by the user to access their clock information.
 * 
 */



package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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



@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	@Override
	/*
	 * doPost() method is used to compare the entered login information with each each employees login information to find a username and password match.
	 * After finding a match, the method determines whether the user has admin/HR privileges or regular employee privileges.
	 * */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Instantiation of local variables.
		ArrayList<Employee> employeeList = new ArrayList<Employee>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet myRs = null;
		HttpSession session = req.getSession();
		RequestDispatcher requestDispatcher = null;

		//try method is the beginning of the connection and retrieval of the employee list from the database.
		try {
			
			//connect to database and execute the query for a full list of employees
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", DBCredentials.USERNAME, DBCredentials.PASSWORD);
			stmt = conn.createStatement();
			myRs = stmt.executeQuery("select * from employee");

			/* Boilerplate code used to handle the list of employees retrieved from the database.
			 * These blocks will be similar to many across the application, and can be prevented by the use
			 * of an Object Relational Mapping tool such as Hibernate.
			 */
			while (myRs.next()) {
				Employee newEmployee = new Employee();
				newEmployee.setEmployeeId(myRs.getInt("EmployeeID"));
				newEmployee.setFirstName(myRs.getString("FirstName"));
				newEmployee.setLastName(myRs.getString("LastName"));
				newEmployee.setEmployeeType(myRs.getString("EmployeeType"));
				employeeList.add(newEmployee);
			}
			
			//iterate through the employeelist to attempt to find a match in username/password
			for(Employee emp : employeeList) {
				System.out.println(emp.getLastName());
				System.out.println(req.getParameter("password"));
				if (emp.getFirstName().equals(req.getParameter("username"))) {
					if(emp.getLastName().equals(req.getParameter("password"))) {
						System.out.println("Still working 2");
						String empType = emp.getEmployeeType();
						if (empType.equals("hr") || empType.equals("admin")) {
							System.out.println("SUCCESS");
							session.setAttribute("user", emp);
							session.setAttribute("rows", employeeList);
							session.setAttribute("headURL", "admin_portal.jsp");
							requestDispatcher = req.getRequestDispatcher("index.jsp");
							requestDispatcher.forward(req, resp);
							return;
						}
						else {
							System.out.println("Success");
							session.setAttribute("user", emp);
							session.setAttribute("headURL", "employee_portal.jsp");
							requestDispatcher = req.getRequestDispatcher("index.jsp");
							requestDispatcher.forward(req, resp);
							return;
						}
					}
				}
			}
			requestDispatcher = req.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(req, resp);
			
			
			//TODO: close the result set
		}catch (Exception e) {
			System.out.println("EXCEPTION");
			System.out.println(e);
		}
		finally {
			
			try {
				conn.close();
				myRs.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
