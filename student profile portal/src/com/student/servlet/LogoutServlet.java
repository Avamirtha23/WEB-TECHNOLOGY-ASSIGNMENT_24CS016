package com.student.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles User Logout and invalidates existing HTTP Sessions.
 * Implements Task 5: Ensure session handling so that only logged-in users can access the portal securely.
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout", "/LogoutServlet"})
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processLogout(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processLogout(request, response);
    }

    private void processLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Retrieve current active session without creating a new one
        HttpSession session = request.getSession(false);
        if (session != null) {
            System.out.println("[LogoutServlet] Invalidating session: " + session.getId());
            session.removeAttribute("currentUser");
            session.removeAttribute("username");
            session.removeAttribute("displayName");
            session.removeAttribute("userRole");
            session.invalidate(); // Invalidate the entire session
        }

        // Set cache control headers to prevent back-button navigation after logout
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // Redirect user to login page with logout confirmation parameter
        response.sendRedirect(request.getContextPath() + "/login.jsp?logout=true");
    }
}
