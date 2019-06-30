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
		
		ArrayList<Employee> rows = new ArrayList<Employee>();
		//Step 1: set the content type
		resp.setContentType("text/html");

		//Step 2: get the printwriter
		PrintWriter out = resp.getWriter();

		//Step 3: Generate HTML content
		
		
		//
		Connection myConn = null; 
		Statement myStmt = null; 
		ResultSet myRs = null;
		
		
		HttpSession session = req.getSession();
		
		
		try {
			
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", "root", "halloffame421");
			
			myStmt = myConn.createStatement();
			
			myRs = myStmt.executeQuery("select * from employees");
			
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
			session.setAttribute("headURL","employees.jsp");
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(req, resp);
			//TODO: close the result set
		}catch (Exception e) {
			System.out.println("EXCEPTION");
			System.out.println(e);
		}
		finally {
			try {
				myConn.close();
				myRs.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
