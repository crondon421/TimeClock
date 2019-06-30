package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;
@WebServlet("/PunchCard")
public class PunchCardServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//Create connection
		resp.setContentType("text/html");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HttpSession session = req.getSession();
		PrintWriter out = resp.getWriter();
		
		
		Employee user = (Employee)session.getAttribute("user");
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timeclockdb", "root", "halloffame421");
			stmt = conn.prepareStatement("select MAX(PunchTime) as LastPunch from clock where EmployeeID=?");
			stmt.setInt(1, user.getEmployeeId());
			rs = stmt.executeQuery();
			
			
			while (rs.next()) {
				session.setAttribute("LastPunch", rs.getString("LastPunch"));
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
//		if(user.isWorking()) {
//			user.setWorking(false);
//		}else {
//			user.setWorking(true);
//		}
		
		
		
	}
	
	
	
}
