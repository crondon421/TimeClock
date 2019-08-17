/*
 * Filename: ManagerServlet.java
 * Description: This servlets doGet() method processes the display for recorded time shifts for the selected employee.
 * 				Additionally, the servlets doPost() will store any changes made to the employees time card.
 * */
package servlets;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.TimeShift;
import models.DBCredentials;

@WebServlet("/Manager")
public class ManagerServlet extends HttpServlet{

	/*
	 * The doGet() method retrieves the employee id from the HTTP Request attribute,
	 * then retrieves the employees time sheet information from the database.
	 * */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		
		//Date and Time formatters for both html and sql
		SimpleDateFormat htmlDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		SimpleDateFormat sqlFormatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		
		
		/* Initialization of connection and session objects*/
		Connection conn = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		HttpSession session = req.getSession();
		Integer employeeId = 0;
		//ArrayList to store javabeans that represent employees recorded timeshifts
		ArrayList<TimeShift> recordedShifts = new ArrayList<TimeShift>();
		
		try {//Attempt to establish a connection to the mysql database
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", DBCredentials.USERNAME, DBCredentials.PASSWORD);
			stmt = conn.prepareCall("{call list_timesheet_for_employee(?)}");
			
			/*Retrieve the employees timesheet for editing based on their  
			 * employee ID
			 * */
			if(req.getParameter("employee_id")!=null) {
				employeeId = Integer.parseInt((String)req.getParameter("employee_id"));
				session.setAttribute("employee_id", Integer.parseInt((String)req.getParameter("employee_id")));
			}else if(session.getAttribute("employee_id")!=null) {
				employeeId = (Integer)session.getAttribute("employee_id");
			}
			stmt.setInt(1, employeeId);
			rs = stmt.executeQuery();

			while (rs.next()) {//loop stores each recorded shift into ArrayList
				TimeShift ts = new TimeShift();
				ts.setEmployeeId(rs.getInt("employee_id"));
				Date sqlInDate = sqlFormatter.parse(rs.getString("punch_in"));
				ts.setClockIn(htmlDateFormatter.format(sqlInDate));
				Date sqlOutDate = sqlFormatter.parse(rs.getString("punch_out"));
				ts.setClockOut(htmlDateFormatter.format(sqlOutDate));
				ts.setPunchInId(rs.getInt("punch_in_id"));
				ts.setPunchOutId(rs.getInt("punch_out_id"));
				ts.setHoursWorked(rs.getDouble("hours_worked"));
				//TODO: add to set of beans
				recordedShifts.add(ts);
			} 

			//Sets the punches into the request attributes for display in the JSP view
			req.setAttribute("punches", recordedShifts);
			req.setAttribute("employee_id", employeeId);
			session.setAttribute("headURL","edit_time_sheet.jsp");
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(req, resp);

		}catch (Exception e) {
			System.out.println("EXCEPTION");
			e.printStackTrace();
		}finally {
			try {
				conn.close();
				rs.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/*
	 * The doPost() method receives the edited timesheet information sent from edit_time_sheet.jsp 
	 * and compares it with the timesheet information from the database. If any of the dates and times
	 * are different, then a sql statement is sent to the database to update the information*/
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		SimpleDateFormat htmlDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		SimpleDateFormat sqlFormatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		//initialization of the session and connection objects
		Connection conn = null;
		CallableStatement calledStmt = null;
		PreparedStatement preppedStmt = null;
		ResultSet rs = null;
		HttpSession session = req.getSession();
		Boolean changesMade = false;
		ArrayList<TimeShift> recordedShifts = new ArrayList<TimeShift>();
		
		try {//Attempt to make a connection and call to the database
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", DBCredentials.USERNAME, DBCredentials.PASSWORD);
			calledStmt = conn.prepareCall("{call list_timesheet_for_employee(?)}");
			Integer employeeId = Integer.parseInt((String)req.getParameter("employee_id"));
			calledStmt.setInt(1, employeeId);
			rs = calledStmt.executeQuery();
			
			/*
			 * Iterates through the result set returned by the database
			 * and prepares data for comparison to the changes made
			 * to the data.*/
			while (rs.next()) {
				TimeShift ts = new TimeShift();
				ts.setEmployeeId(rs.getInt("employee_id"));
				Date sqlInDate = sqlFormatter.parse(rs.getString("punch_in"));
				ts.setClockIn(htmlDateFormatter.format(sqlInDate));
				Date sqlOutDate = sqlFormatter.parse(rs.getString("punch_out"));
				ts.setClockOut(htmlDateFormatter.format(sqlOutDate));
				ts.setPunchInId(rs.getInt("punch_in_id"));
				ts.setPunchOutId(rs.getInt("punch_out_id"));
				ts.setHoursWorked(rs.getDouble("hours_worked"));
				//TODO: add to set of beans
				recordedShifts.add(ts);
			}
			
			for(TimeShift shift : recordedShifts) {
				//compare the stored timepunch info to the changes made based on punchID
				if (!shift.getClockIn().equals(req.getParameter(String.valueOf(shift.getPunchInId())))) {
					System.out.println("Clock in times different, changing");
					System.out.println();
					Date htmlInDate = htmlDateFormatter.parse(req.getParameter(String.valueOf(shift.getPunchInId())));
					String sqlStringDate = sqlFormatter.format(htmlInDate);
					preppedStmt = conn.prepareStatement("UPDATE clock SET PunchTime=? WHERE PunchId=?");
					preppedStmt.setString(1, sqlStringDate);
					preppedStmt.setInt(2, shift.getPunchInId());
					shift.setClockIn(req.getParameter(String.valueOf(shift.getPunchInId())));
					preppedStmt.executeUpdate();
					changesMade = true;
				}
				//compare the stored timepunch info to the changes made based on punchID
				if (!shift.getClockOut().equals(req.getParameter(String.valueOf(shift.getPunchOutId())))) {
					System.out.println("Clock out times different, changing");
					System.out.println();
					Date htmlOutDate = htmlDateFormatter.parse(req.getParameter(String.valueOf(shift.getPunchOutId())));
					String sqlStringDate = sqlFormatter.format(htmlOutDate);
					preppedStmt = conn.prepareStatement("UPDATE clock SET PunchTime=? WHERE PunchId=?");
					preppedStmt.setString(1, sqlStringDate);
					preppedStmt.setInt(2, shift.getPunchOutId());
					shift.setClockOut(req.getParameter(String.valueOf(shift.getPunchOutId())));
					preppedStmt.executeUpdate();
					changesMade = true;
				}
			}
			
			if(changesMade) {//if the changes that were made are successful, direct to the success page
				session.setAttribute("headURL","edit_timesheet_success.jsp");
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
				requestDispatcher.forward(req, resp);
			}else {//if the changes made had an error, direct to the error page
				session.setAttribute("headURL", "edit_timesheet_error.jsp");
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
				requestDispatcher.forward(req, resp);
			}

		}catch (Exception e) {
			System.out.println("EXCEPTION");
			e.printStackTrace();
		}finally {
			try {
				conn.close();
				rs.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	



}
