package com.student.servlet;

import com.student.dao.UserDAO;
import com.student.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles User Login and creates authenticated HTTP Sessions.
 * Implements Task 5: Ensure session handling so that only logged-in users can access the portal securely.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/LoginServlet"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // If already logged in, redirect straight to portal
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            response.sendRedirect(request.getContextPath() + "/students");
            return;
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate credentials against MySQL using JDBC via UserDAO
        User authenticatedUser = userDAO.authenticate(username, password);

        if (authenticatedUser != null) {
            // 1. Create a brand new authenticated HTTP Session
            HttpSession session = request.getSession(true);
            
            // 2. Store user session attributes
            session.setAttribute("currentUser", authenticatedUser);
            session.setAttribute("username", authenticatedUser.getUsername());
            session.setAttribute("displayName", authenticatedUser.getFullName());
            session.setAttribute("userRole", authenticatedUser.getRole());
            session.setAttribute("loginTime", new java.util.Date());
            
            // Session timeout set to 30 minutes (1800 seconds)
            session.setMaxInactiveInterval(30 * 60);

            System.out.println("[LoginServlet] Session created for user: " + authenticatedUser.getUsername() + 
                               ", Session ID: " + session.getId());

            // 3. Redirect to the secure student portal servlet
            response.sendRedirect(request.getContextPath() + "/students");
        } else {
            // Authentication failed: return to login page with error message
            request.setAttribute("errorMessage", "Invalid Username or Password! Please try again.");
            request.setAttribute("enteredUsername", username);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
