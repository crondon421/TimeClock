/*
 * Filename: TimeSheetServlet.java
 * Author: Christian Rondon
 * Date: 8/17/2019
 * Description: This Servlet is used to retrieve time sheet information for the user that is currently logged in.
 * 				This information is uneditable by the employee, as employees should not be able to edit their own time 
 * 				sheets.
 * */

package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;
import models.TimeShift;
import models.DBCredentials;


@WebServlet("/TimeSheet")
public class TimeSheetServlet extends HttpServlet{
	
	/*
	 * doGet() method retrieves all started and completed shifts and stores them as an arraylist
	 * inside the HttpServletRequest attribute for display in the view (time_sheet.jsp).
	 * */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//establish connection to database, instantiates the resultset and current Http session
		Connection myConn = null; 
		CallableStatement myStmt = null; 
		ResultSet myRs = null;
		HttpSession session = req.getSession();
		//Arraylist stores the shifts that have been completed by the employee
		ArrayList<TimeShift> recordedShifts = new ArrayList<TimeShift>();

		

		Employee user = (Employee) session.getAttribute("user");

		if(user == null) {//if the user is null, the session has expired and require to re-login
			session.setAttribute("headURL","login.jsp");
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(req, resp);
		}

		try {//Attempts to establish the connection to the database, execute the query and store each row into the arraylist

			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", DBCredentials.USERNAME, DBCredentials.PASSWORD);
			myStmt = myConn.prepareCall("{call list_timesheet_for_employee(?)}");
			myStmt.setInt(1, user.getEmployeeId());

			myRs = myStmt.executeQuery();
			
			while (myRs.next()) {//Iterate through each row in the result set returned from database
				TimeShift timeShift = new TimeShift();
				timeShift.setEmployeeId(myRs.getInt("employee_id"));
				timeShift.setClockIn(myRs.getString("punch_in"));
				timeShift.setClockOut(myRs.getString("punch_out"));
				timeShift.setHoursWorked(myRs.getDouble("hours_worked"));
				recordedShifts.add(timeShift); //add the timeShift javabean to the arraylist of shifts
			}
			req.setAttribute("punches", recordedShifts);
			session.setAttribute("headURL","time_sheet.jsp");
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(req, resp);
		}catch (Exception e) {
			System.out.println("EXCEPTION");
			e.printStackTrace();
		}
		finally {
			try {//attempt to close the connection and result set
				myConn.close();
				myRs.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
