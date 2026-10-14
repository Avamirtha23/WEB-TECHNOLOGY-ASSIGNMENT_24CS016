package com.student.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Authentication and Security Filter.
 * Implements Task 5: Ensure session handling so that only logged-in users can access the portal securely.
 * Intercepts requests, validates the active HTTP Session, and prevents unauthorized access.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[AuthFilter] Initialized session security filter.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String relativeUri = uri.substring(contextPath.length());

        // Define public endpoints that do not require authentication
        boolean isPublicResource = relativeUri.equals("/") ||
                                   relativeUri.equals("/index.jsp") ||
                                   relativeUri.equals("/login.jsp") ||
                                   relativeUri.equals("/login") ||
                                   relativeUri.equals("/LoginServlet") ||
                                   relativeUri.equals("/logout") ||
                                   relativeUri.startsWith("/css/") ||
                                   relativeUri.startsWith("/js/") ||
                                   relativeUri.endsWith(".css") ||
                                   relativeUri.endsWith(".js") ||
                                   relativeUri.endsWith(".png") ||
                                   relativeUri.endsWith(".ico");

        if (isPublicResource) {
            chain.doFilter(request, response);
            return;
        }

        // Validate active session
        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("currentUser") != null);

        if (isLoggedIn) {
            // Prevent browser back-button caching of protected content
            httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setDateHeader("Expires", 0);

            // User is authenticated, proceed down the filter chain
            chain.doFilter(request, response);
        } else {
            // User is not logged in: redirect to login page with unauthorized flag
            System.out.println("[AuthFilter] Unauthorized access blocked for URI: " + relativeUri);
            httpResponse.sendRedirect(contextPath + "/login.jsp?error=unauthorized");
        }
    }

    @Override
    public void destroy() {
    }
}
