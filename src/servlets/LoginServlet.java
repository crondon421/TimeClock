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



@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		ArrayList<Employee> employeeList = new ArrayList<Employee>();

		//set content type of response
		resp.setContentType("text/html");
		//Step 2: get the printwriter
		PrintWriter out = resp.getWriter();
		
		System.out.println("This is working");

		//get globals

		Connection conn = null;
		Statement stmt = null;
		ResultSet myRs = null;
		
		HttpSession session = req.getSession();

		try {

			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", "root", "halloffame421");

			stmt = conn.createStatement();

			myRs = stmt.executeQuery("select * from employees");

			while (myRs.next()) {
				System.out.println("Still working 1");
				//TODO: Create the bean
				Employee newEmployee = new Employee();
				newEmployee.setEmployeeId(myRs.getInt("EmployeeID"));
				newEmployee.setFirstName(myRs.getString("FirstName"));
				newEmployee.setLastName(myRs.getString("LastName"));
				newEmployee.setEmployeeType(myRs.getString("EmployeeType"));
				//TODO: add to set of beans
				employeeList.add(newEmployee);
			}
			
			RequestDispatcher requestDispatcher = null;
			for(Employee emp : employeeList) {
				System.out.println(emp.getLastName());
				System.out.println(req.getParameter("password"));
				if (emp.getFirstName().equals(req.getParameter("username"))) {
					if(emp.getLastName().equals(req.getParameter("password"))) {
						System.out.println("Still working 2");
						String empType = emp.getEmployeeType();
						out.println(empType);
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
