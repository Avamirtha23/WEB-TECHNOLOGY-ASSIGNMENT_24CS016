package com.student.dao;

import com.student.model.User;
import com.student.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for User authentication and verification.
 * Implements Task 1 & Task 5: JDBC retrieval for secure session handling.
 */
public class UserDAO {

    /**
     * Authenticates a user with given username and password using MySQL via JDBC.
     * Includes a graceful fallback for demonstrations if MySQL is offline.
     */
    public User authenticate(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT id, username, full_name, role FROM users WHERE username = ? AND password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username.trim());
            ps.setString(2, password.trim());
            rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));
                System.out.println("[UserDAO] Authenticated user via MySQL JDBC: " + username);
                return user;
            }
        } catch (Exception e) {
            System.err.println("[UserDAO] Database query failed: " + e.getMessage());
            // Graceful offline fallback for lab demos if MySQL service is not running
            if ("admin".equalsIgnoreCase(username) && "admin123".equals(password)) {
                System.out.println("[UserDAO] Authenticated via built-in admin credentials (offline fallback)");
                return new User(1, "admin", "System Administrator", "Administrator");
            } else if ("amirtha".equalsIgnoreCase(username) && "amirtha123".equals(password)) {
                System.out.println("[UserDAO] Authenticated via student coordinator credentials (offline fallback)");
                return new User(2, "amirtha", "Amirtha Varshini K V", "Student Coordinator");
            } else if ("faculty".equalsIgnoreCase(username) && "faculty123".equals(password)) {
                System.out.println("[UserDAO] Authenticated via faculty credentials (offline fallback)");
                return new User(3, "faculty", "Dr. Rajeshkumar S", "Faculty Advisor");
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            DBConnection.closeConnection(conn);
        }

        return null;
    }
}
