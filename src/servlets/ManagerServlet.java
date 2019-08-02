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

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


		ArrayList<TimeShift> recordedShifts = new ArrayList<TimeShift>();
		SimpleDateFormat htmlDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		SimpleDateFormat sqlFormatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		//establish a connection
		//TODO: create one connection per login, and store connection inside the session
		Connection conn = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		HttpSession session = req.getSession();
		Integer employeeId = 0;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", DBCredentials.USERNAME, DBCredentials.PASSWORD);
			stmt = conn.prepareCall("{call list_timesheet_for_employee(?)}");
			
			if(req.getParameter("employee_id")!=null) {
				employeeId = Integer.parseInt((String)req.getParameter("employee_id"));
				session.setAttribute("employee_id", Integer.parseInt((String)req.getParameter("employee_id")));
			}else if(session.getAttribute("employee_id")!=null) {
				employeeId = (Integer)session.getAttribute("employee_id");
			}
			stmt.setInt(1, employeeId);
			rs = stmt.executeQuery();

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

			//TODO: send the set of beans to the jtsl on the jsp page
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

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ArrayList<TimeShift> recordedShifts = new ArrayList<TimeShift>();
		SimpleDateFormat htmlDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		SimpleDateFormat sqlFormatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		//establish a connection
		//TODO: create one connection per login, and store connection inside the session
		Connection conn = null;
		CallableStatement calledStmt = null;
		PreparedStatement preppedStmt = null;
		ResultSet rs = null;
		HttpSession session = req.getSession();
		Boolean changesMade = false;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", "root", "halloffame421");
			calledStmt = conn.prepareCall("{call list_timesheet_for_employee(?)}");
			Integer employeeId = Integer.parseInt((String)req.getParameter("employee_id"));
			calledStmt.setInt(1, employeeId);
			rs = calledStmt.executeQuery();

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
				
				System.out.println("Clock in time and form in models " + shift.getClockIn());
				System.out.println("Clock out time and form in models " + shift.getClockOut());
				System.out.println("Clock in time and form in jsp " + req.getParameter(String.valueOf(shift.getPunchInId())));
				System.out.println("Clock in time and form in jsp " + req.getParameter(String.valueOf(shift.getPunchOutId())));
				System.out.println();
				
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
			
			if(changesMade) {
				session.setAttribute("headURL","edit_timesheet_success.jsp");
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
				requestDispatcher.forward(req, resp);
			}else {
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
