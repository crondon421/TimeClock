/*
 * Filename: LogoutServlet.java
 * Author: Christian Rondon
 * Date: 8/17/2019
 * Description: This servlet serves to log the user out of their account. It will kill
 * 				the session and redirect the user to login.jsp
 */
package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Logout")
public class LogoutServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().invalidate();
        resp.sendRedirect("index.jsp");
	}
}
