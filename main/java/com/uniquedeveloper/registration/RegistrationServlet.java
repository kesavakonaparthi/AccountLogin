package com.uniquedeveloper.registration;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String usrName = request.getParameter("name");
		String usrMail = request.getParameter("email");
		String usrPassword = request.getParameter("pass");
		String usrRePasswrd = request.getParameter("re_pass");
		String usrMble = request.getParameter("contact");
		RequestDispatcher dispatcher = null;
		Connection connection = null;
	
		if (usrName == null || usrName.equals("")) {
			request.setAttribute("status", "invalidName");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);
		}
		
		if (usrMail == null || usrMail.equals("")) {
			request.setAttribute("status", "invalidMail");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);
		}
		
		if (usrPassword == null || usrPassword.equals("")) {
			request.setAttribute("status", "invalidPassword");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);	
		} else if (!usrPassword.equals(usrRePasswrd)) {
			request.setAttribute("status", "invalidRePassword");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);
		}
		
		if (usrMble == null || usrMble.equals("")) {
			request.setAttribute("status", "invalidMbleNumber");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);
		} else if (usrMble.length() > 10) {
			request.setAttribute("status", "mustBeTenDigits");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response); 
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/twitter?useSSL=false", "root", "12345");
			PreparedStatement pStmnt = connection.prepareStatement("insert into users(uname, umail, upasswrd, umobile) values (?, ?, ?, ?) ");
			pStmnt.setString(1, usrName);
			pStmnt.setString(2, usrMail);
			pStmnt.setString(3, usrPassword);
			pStmnt.setString(4, usrMble);
			
			int rowCount = pStmnt.executeUpdate();
			dispatcher = request.getRequestDispatcher("registration.jsp");
			if (rowCount > 0) {
				request.setAttribute("status", "success");
			} else {
				request.setAttribute("status", "failed");
			}
			dispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}