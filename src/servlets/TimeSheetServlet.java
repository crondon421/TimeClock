package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;
import models.TimePunch;


@WebServlet("/TimeSheet")
public class TimeSheetServlet extends HttpServlet{
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		ArrayList<TimePunch> punches = new ArrayList<TimePunch>();
		//Step 1: set the content type
		resp.setContentType("text/html");

		//Step 2: get the printwriter
		PrintWriter out = resp.getWriter();

		//Step 3: Generate HTML content


		//
		Connection myConn = null; 
		CallableStatement myStmt = null; 
		ResultSet myRs = null;


		HttpSession session = req.getSession();


		try {

			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", "root", "halloffame421");

			myStmt = myConn.prepareCall("{call list_timesheet_for_employee(?)}");
			
			Employee user = (Employee) session.getAttribute("user");
			
			myStmt.setInt(1, user.getEmployeeId());
			
			myRs = myStmt.executeQuery();
			
			while (myRs.next()) {
				
				TimePunch punch = new TimePunch();
				//TODO: Create the bean
				punch.setEmployeeId(myRs.getInt("employee_id"));
				punch.setFirstName(myRs.getString("first_name"));
				punch.setLastName(myRs.getString("last_name"));
				punch.setClockIn(myRs.getString("punch_in"));
				punch.setClockOut(myRs.getString("punch_out"));
				punch.setHoursWorked(myRs.getDouble("hours_worked"));
				
				//TODO: add to set of beans
				punches.add(punch);
			}

			//TODO: send the set of beans to the jtsl on the jsp page
			req.setAttribute("punches", punches);
			session.setAttribute("headURL","time_sheet.jsp");
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
