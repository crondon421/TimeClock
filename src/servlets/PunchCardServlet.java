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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Create connection
		resp.setContentType("text/html");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HttpSession session = req.getSession();


		Employee user = (Employee)session.getAttribute("user");
		if(user == null) {
			session.setAttribute("headURL","login.jsp");
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(req, resp);
		}

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", DBCredentials.USERNAME, DBCredentials.PASSWORD);
			stmt = conn.prepareStatement("select * from clock where PunchTime=(Select MAX(PunchTime)from clock where EmployeeID=?)");
			stmt.setInt(1, user.getEmployeeId());
			rs = stmt.executeQuery();


			while (rs.next()) {
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
			try {
				rs.close();
				conn.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}


	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Create connection
		resp.setContentType("text/html");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HttpSession session = req.getSession();
		SimpleDateFormat sqlFormatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		SimpleDateFormat javaDateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
		Date d1 = null;
		Date d2 = new Date();
		TimePunch lastPunch = new TimePunch();


		Employee user = (Employee)session.getAttribute("user");

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", "root", "halloffame421");
			stmt = conn.prepareStatement("select * from clock where PunchTime=(Select MAX(PunchTime)from clock where EmployeeID=?)");
			stmt.setInt(1, user.getEmployeeId());
			rs = stmt.executeQuery();

			while (rs.next()) {
				//TODO: Create the bean
				lastPunch.setEmployeeId(rs.getInt("EmployeeID"));
				lastPunch.setNeutralPunch(rs.getString("PunchTime"));
				lastPunch.setPunchType(rs.getBoolean("PunchType"));
				d1 = sqlFormatter.parse(lastPunch.getNeutralPunch());
			}

			if (d1 != null) {
				long timeElapsed = d2.getTime() - d1.getTime();
				long diffMinutes = timeElapsed / (60 * 1000);
				if (diffMinutes >= 5) {
					//do clockout/clockin procedure
					stmt = conn.prepareStatement("INSERT INTO clock(EmployeeID, PunchTime, PunchType) VALUES(?, ?, ?)");
					if(lastPunch.isPunchType() == true) {
						stmt.setInt(1, user.getEmployeeId());
						Date javaDate = javaDateFormatter.parse(d2.toString());
						String sqlDateString = sqlFormatter.format(javaDate);
						stmt.setString(2, sqlDateString);
						stmt.setBoolean(3, false);
						stmt.execute();
						session.setAttribute("headURL", "employee_portal.jsp");
					}
					if(lastPunch.isPunchType() == false) {
						stmt.setInt(1, user.getEmployeeId());
						Date javaDate = javaDateFormatter.parse(d2.toString());
						String sqlDateString = sqlFormatter.format(javaDate);
						stmt.setString(2, sqlDateString);
						stmt.setBoolean(3, true);
						stmt.execute();
						session.setAttribute("headURL", "employee_portal.jsp");
					}

				}else {
					//redirect to a page that then redirects to /PunchCard after 10 seconds
					session.setAttribute("headURL", "clock-cooldown.jsp");

				}
			}else {
				stmt = conn.prepareStatement("INSERT INTO clock(EmployeeID, PunchTime, PunchType) VALUES(?, ?, ?)");
				stmt.setInt(1, user.getEmployeeId());
				Date javaDate = javaDateFormatter.parse(d2.toString());
				String sqlDateString = sqlFormatter.format(javaDate);
				stmt.setString(2, sqlDateString);
				stmt.setBoolean(3, true);
				stmt.execute();
				session.setAttribute("headURL", "employee_portal.jsp");
			}




			RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(req, resp);

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}



}
