<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Prevent back-button caching
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    // If already logged in, redirect directly to portal
    javax.servlet.http.HttpSession existingSession = request.getSession(false);
    if (existingSession != null && existingSession.getAttribute("currentUser") != null) {
        response.sendRedirect(request.getContextPath() + "/students");
        return;
    }
    String errorParam = request.getParameter("error");
    String logoutParam = request.getParameter("logout");
    String errorMessage = (String) request.getAttribute("errorMessage");
    String enteredUsername = (String) request.getAttribute("enteredUsername");
    if (enteredUsername == null) enteredUsername = "";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Student Profile Portal</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css?v=light1">
</head>
<body>
    <div class="login-wrapper">
        <div class="login-card">
            <div class="login-header">
                <div class="login-icon-badge">??</div>
                <h2>Student Profile Portal</h2>
            </div>
            <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
                <div class="alert alert-error"><span>??</span><div><%= errorMessage %></div></div>
            <% } else if ("unauthorized".equalsIgnoreCase(errorParam)) { %>
                <div class="alert alert-error"><span>??</span><div>Access Denied! Please log in to view student profiles.</div></div>
            <% } else if ("true".equalsIgnoreCase(logoutParam)) { %>
                <div class="alert alert-success"><span>?</span><div>You have been logged out securely. Session ended.</div></div>
            <% } %>
            <form action="<%= request.getContextPath() %>/login" method="POST" autocomplete="off">
                <div class="form-group">
                    <label class="form-label" for="username">Username</label>
                    <input type="text" id="username" name="username" class="form-control"
                           placeholder="Enter your username (e.g. admin)"
                           value="<%= enteredUsername %>" required autofocus>
                </div>
                <div class="form-group">
                    <label class="form-label" for="password">Password</label>
                    <input type="password" id="password" name="password" class="form-control"
                           placeholder="Enter your password" required>
                </div>
                <button type="submit" class="btn-primary" id="btn-login">Sign In to Portal</button>
            </form>
            <div class="demo-hints">
                <h4>Default Database Credentials</h4>
                <ul>
                    <li>Admin: <code>admin</code> / <code>admin123</code></li>
                    <li>Faculty: <code>faculty</code> / <code>faculty123</code></li>
                    <li>Student: <code>amirtha</code> / <code>amirtha123</code></li>
                </ul>
            </div>
        </div>
    </div>
</body>
</html>
