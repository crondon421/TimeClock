package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;


public class EmployeeServlet extends HttpServlet{


	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//set local variables
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		//set values for sql connection
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet myRs = null;
		HttpSession session = req.getSession();
		
		int operationCount = 0;
		
		try {
			
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", "root", "halloffame421");
			stmt = conn.prepareStatement("select * from employees where employeeid=?");
			Employee emp = (Employee)session.getAttribute("employee");
			stmt.setInt(1, emp.getEmployeeId());
			myRs = stmt.executeQuery();
			
			while (myRs.next()) {
				
				//TODO: Create the bean
				emp = new Employee();
				emp.setEmployeeId(myRs.getInt("EmployeeID"));
				emp.setFirstName(myRs.getString("FirstName"));
				emp.setLastName(myRs.getString("LastName"));
				emp.setEmployeeType(myRs.getString("EmployeeType"));
				//TODO: add to set of beans
				session.setAttribute("employee", emp);
				operationCount++;
			}
			
			System.out.println("Employee populated in " + operationCount + " operations.");
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if (conn !=null ){
				try {
					myRs.close();
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
