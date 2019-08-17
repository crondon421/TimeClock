/*
 * Filename: PunchCardServlet.java
 * Author: Christian Rondon
 * Date: 8/17/19
 * Description: This servlet is a handler for the requests made from punchcard.jsp. 
 * 				The doGet() method queries the database for the last punch entered by the employee.
 * 				If the employee clocked out last, the clock in button will be enabled in the view.
 * 				If the employee clocked in last, the clock out button will be enabled.
 * 				The doPost() method punches the punch card accordingly and stores the new punch
 * 				in the database.
 * */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;
import models.TimePunch;
import models.TimeShift;
import models.DBCredentials;
@WebServlet("/PunchCard")
public class PunchCardServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	/*
	 * doGet() method retrieves last punch data from the database for the employee that is logged in
	 * and returns the punchcard.jsp view accordingly.*/
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//instantiation of the connection, prepared statement, resultset and HttpSession.
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HttpSession session = req.getSession();

		Employee user = (Employee)session.getAttribute("user");//Javabean used for the employee that is logged in.
		
		if(user == null) { //if user is null, the session has expired and the user has to log in again.
			session.setAttribute("headURL","login.jsp");
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(req, resp);
		}

		try {//Attempts to establish a connection, and retreive data necessary to find the employees most recent punch.
			
			//establishing a connection to the database.
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", DBCredentials.USERNAME, DBCredentials.PASSWORD);
			stmt = conn.prepareStatement("select * from clock where PunchTime=(Select MAX(PunchTime)from clock where EmployeeID=?)");
			stmt.setInt(1, user.getEmployeeId());
			rs = stmt.executeQuery();

			while (rs.next()) {//records the last punch by the employee into a JavaBean.
				TimePunch punch = new TimePunch();
				//TODO: Create the bean
				punch.setEmployeeId(rs.getInt("EmployeeID"));
				punch.setNeutralPunch(rs.getString("PunchTime"));
				punch.setPunchType(rs.getBoolean("PunchType"));
				session.setAttribute("lastPunch", punch);
			}
			session.setAttribute("headURL", "punchcard.jsp");
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(req, resp);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {//attempts to close the resultset and connection if the try block was successful.
				rs.close();
				conn.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}


	}

	/*
	 * doPost() method analyzes the TimePunch bean to determine whether the employees most recent
	 * punch was in or out, and punches the timecard accordingly, adding a new punch time
	 * to the database. 
	 */ 
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Date and Time formatters
		SimpleDateFormat sqlFormatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		SimpleDateFormat javaDateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
		Date d1 = null;
		Date d2 = new Date(); //d2 is the current date and time
		
		//Instantiate the connection, mysql prepared statement, resultset and HttpSession
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HttpSession session = req.getSession();
		
		//Objects that represent user information and most recent time punch
		Employee user = (Employee)session.getAttribute("user");
		TimePunch lastPunch = new TimePunch();
		
		try {//Attempt to establish a connection to the mysql database
			
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", DBCredentials.USERNAME, DBCredentials.PASSWORD);
			stmt = conn.prepareStatement("select * from clock where PunchTime=(Select MAX(PunchTime)from clock where EmployeeID=?)");
			stmt.setInt(1, user.getEmployeeId());
			rs = stmt.executeQuery();

			while (rs.next()) {//store the latest punch into a java object
				lastPunch.setEmployeeId(rs.getInt("EmployeeID"));
				lastPunch.setNeutralPunch(rs.getString("PunchTime"));
				lastPunch.setPunchType(rs.getBoolean("PunchType"));
				d1 = sqlFormatter.parse(lastPunch.getNeutralPunch());
			}

			if (d1 != null) {
				long timeElapsed = d2.getTime() - d1.getTime();
				long diffMinutes = timeElapsed / (60 * 1000);
				if (diffMinutes >= 5) {//Checks to see that five minutes has elapsed since the most recent punch
					//do clockout/clockin procedure
					stmt = conn.prepareStatement("INSERT INTO clock(EmployeeID, PunchTime, PunchType) VALUES(?, ?, ?)");
					if(lastPunch.isPunchType() == true) {//if the user is clocked in, clock the user out
						stmt.setInt(1, user.getEmployeeId());
						Date javaDate = javaDateFormatter.parse(d2.toString());
						String sqlDateString = sqlFormatter.format(javaDate);
						stmt.setString(2, sqlDateString);
						stmt.setBoolean(3, false);//set clock type to false(clocked out)
						stmt.execute();
						session.setAttribute("headURL", "employee_portal.jsp");
					}
					if(lastPunch.isPunchType() == false) {//if the user is clocked out, clock the user in
						stmt.setInt(1, user.getEmployeeId());
						Date javaDate = javaDateFormatter.parse(d2.toString());
						String sqlDateString = sqlFormatter.format(javaDate);
						stmt.setString(2, sqlDateString);
						stmt.setBoolean(3, true);//set clock type to true(clocked in)
						stmt.execute();
						session.setAttribute("headURL", "employee_portal.jsp");
					}

				}else {
					//redirect to a page that then redirects to /PunchCard after 10 seconds
					session.setAttribute("headURL", "clock-cooldown.jsp");

				}
			}else {//if no clock in information is found, clock the employee in for the first time
				stmt = conn.prepareStatement("INSERT INTO clock(EmployeeID, PunchTime, PunchType) VALUES(?, ?, ?)");
				stmt.setInt(1, user.getEmployeeId());
				Date javaDate = javaDateFormatter.parse(d2.toString());
				String sqlDateString = sqlFormatter.format(javaDate);
				stmt.setString(2, sqlDateString);
				stmt.setBoolean(3, true);//set clock type to true(clocked in)
				stmt.execute();
				session.setAttribute("headURL", "employee_portal.jsp");
			}
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(req, resp);

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {//attempt to clock the resultset and connection
				rs.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}



}
