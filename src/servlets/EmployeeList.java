package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;

@WebServlet("/EmployeeList")
public class EmployeeList extends HttpServlet{



	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ArrayList rows = new ArrayList();
		//Step 1: set the content type
		resp.setContentType("text/html");

		//Step 2: get the printwriter
		PrintWriter out = resp.getWriter();

		//Step 3: Generate HTML content
		try {
			
			
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost/timeclockdb", "root", "halloffame421");

			Statement myStmt = myConn.createStatement();

			ResultSet myRs = myStmt.executeQuery("select * from employees");
			
			HttpSession session = req.getSession();
			
			
			while (myRs.next()) {
				
				//TODO: Create the bean
				Employee newEmployee = new Employee();
				newEmployee.setEmployeeId(myRs.getInt("EmployeeID"));
				newEmployee.setFirstName(myRs.getString("FirstName"));
				newEmployee.setLastName(myRs.getString("LastName"));
				newEmployee.setEmployeeType(myRs.getString("EmployeeType"));
				//TODO: add to set of beans
				rows.add(newEmployee);
			}
			
			//TODO: send the set of beans to the jtsl on the jsp page
			req.setAttribute("rows", rows);
			session.setAttribute("headURL","admin_portal.jsp");
			
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(req, resp);
			//TODO: close the result set
			myRs.close();
			//TODO: close the connection
			myConn.close();
		}catch (Exception e) {
			System.out.println("EXCEPTION");
			System.out.println(e);
		}
		finally {
		}

	}

	public void getEmployees() {

	}
}
