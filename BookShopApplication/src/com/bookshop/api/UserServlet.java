package com.bookshop.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookshop.model.User;
import com.bookshop.service.UserService;


public class UserServlet extends HttpServlet{
	private UserService us=new UserService();
	
	@Override
	protected void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException {
		String action=req.getParameter("action");
		if("login".equals(action)) {
			handleLogin(req,res);
		}else if("register".equals(action)) {
			handleRegistration(req,res);
		}
	}
	
	private void handleLogin(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	    String email = req.getParameter("email");
	    String password = req.getParameter("password");

	    User u = us.getUserByEmail(email);

	    if (u != null && us.loginUser(email, password)) {
	        HttpSession s = req.getSession();
	        s.setAttribute("currentUser", u);  // save the user object
	        s.setAttribute("role", u.getRole());  // save the role separately for easy access

	        if ("admin".equalsIgnoreCase(u.getRole())) {
	            res.sendRedirect(req.getContextPath() + "/jsp/addbook.jsp");
	        } else if ("customer".equalsIgnoreCase(u.getRole())) {
	            // 🔥 Redirect to your BookListServlet, not booklist.jsp!
	            res.sendRedirect(req.getContextPath() + "/api/booklist");
	        } else {
	            req.setAttribute("errorMessage", "Unknown user role.");
	            req.getRequestDispatcher("/jsp/LoginOrRegister.jsp").forward(req, res);
	        }
	    } else {
	        req.setAttribute("errorMessage", "Invalid email or password");
	        req.getRequestDispatcher("/jsp/LoginOrRegister.jsp").forward(req, res);
	    }
	}

	private void handleRegistration(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException {
		String name=req.getParameter("name");
		String email=req.getParameter("email");
		String password=req.getParameter("password");
		String role=req.getParameter("role");
		if(us.registerUser(name, email, password, role)) {
			res.sendRedirect("/jsp/LoginOrRegister.jsp");
		}else {
			req.setAttribute("errorMessage", "Email already exists.Please try a different one.");
			req.getRequestDispatcher("/jsp/LoginOrRegister.jsp").forward(req, res);
		}
	}
}
