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


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		ArrayList<TimeShift> recordedShifts = new ArrayList<TimeShift>();
		//Step 1: set the content type
		resp.setContentType("text/html");

		//Step 2: get the printwriter
		PrintWriter out = resp.getWriter();

		//Step 3: Generate HTML content


		//establish connection to database
		Connection myConn = null; 
		CallableStatement myStmt = null; 
		ResultSet myRs = null;


		HttpSession session = req.getSession();

		Employee user = (Employee) session.getAttribute("user");

		if(user == null) {
			session.setAttribute("headURL","login.jsp");
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(req, resp);
		}

		try {

			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", DBCredentials.USERNAME, DBCredentials.PASSWORD);

			myStmt = myConn.prepareCall("{call list_timesheet_for_employee(?)}");



			myStmt.setInt(1, user.getEmployeeId());

			myRs = myStmt.executeQuery();

			while (myRs.next()) {
				//TODO: Create the bean
				TimeShift timeShift = new TimeShift();
				timeShift.setEmployeeId(myRs.getInt("employee_id"));
				timeShift.setClockIn(myRs.getString("punch_in"));
				timeShift.setClockOut(myRs.getString("punch_out"));
				timeShift.setHoursWorked(myRs.getDouble("hours_worked"));

				//TODO: add to set of beans
				recordedShifts.add(timeShift);
			}



			//TODO: send the set of beans to the jtsl on the jsp page
			req.setAttribute("punches", recordedShifts);
			session.setAttribute("headURL","time_sheet.jsp");
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(req, resp);
			//TODO: close the result set
		}catch (Exception e) {
			System.out.println("EXCEPTION");
			e.printStackTrace();
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

	@Override

	//TODO: SET THIS UP TO ADD a webpage
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//establish connection to database
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		HttpSession session = req.getSession();

		try {
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", "root", "halloffame421");
			myStmt = myConn.prepareCall("select max(TimePunch) as LastPunch from clock where EmployeeID=?");

		}catch (Exception e) {
			System.out.println("EXCEPTION");
			System.out.println(e);
		}

	}

}
