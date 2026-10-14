<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Check if user session already exists
    javax.servlet.http.HttpSession currentSession = request.getSession(false);
    if (currentSession != null && currentSession.getAttribute("currentUser") != null) {
        response.sendRedirect(request.getContextPath() + "/students");
    } else {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
%>
