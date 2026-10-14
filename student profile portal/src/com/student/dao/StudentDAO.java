package com.student.dao;

import com.student.model.Student;
import com.student.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student profile management.
 * Implements Task 1: Connect to MySQL using JDBC to fetch student details and associated file paths.
 */
public class StudentDAO {

    /**
     * Fetches all student records and associated file paths from MySQL using JDBC.
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT id, reg_no, name, email, department, year_of_study, cgpa, " +
                         "profile_image_path, document_path, document_name, status " +
                         "FROM students ORDER BY id ASC";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setRegNo(rs.getString("reg_no"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                s.setDepartment(rs.getString("department"));
                s.setYearOfStudy(rs.getInt("year_of_study"));
                s.setCgpa(rs.getDouble("cgpa"));
                s.setProfileImagePath(rs.getString("profile_image_path"));
                s.setDocumentPath(rs.getString("document_path"));
                s.setDocumentName(rs.getString("document_name"));
                s.setStatus(rs.getString("status"));
                students.add(s);
            }
            System.out.println("[StudentDAO] Successfully fetched " + students.size() + " student records from MySQL.");
        } catch (Exception e) {
            System.err.println("[StudentDAO] JDBC Query failed or MySQL is unreachable: " + e.getMessage());
            // Fallback mock dataset ensures students can demonstrate the portal even if MySQL is offline
            students = getMockStudents();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            DBConnection.closeConnection(conn);
        }

        return students;
    }

    /**
     * Fetches a single student by Registration Number.
     */
    public Student getStudentByRegNo(String regNo) {
        if (regNo == null) return null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT id, reg_no, name, email, department, year_of_study, cgpa, " +
                         "profile_image_path, document_path, document_name, status " +
                         "FROM students WHERE reg_no = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, regNo.trim());
            rs = ps.executeQuery();

            if (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setRegNo(rs.getString("reg_no"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                s.setDepartment(rs.getString("department"));
                s.setYearOfStudy(rs.getInt("year_of_study"));
                s.setCgpa(rs.getDouble("cgpa"));
                s.setProfileImagePath(rs.getString("profile_image_path"));
                s.setDocumentPath(rs.getString("document_path"));
                s.setDocumentName(rs.getString("document_name"));
                s.setStatus(rs.getString("status"));
                return s;
            }
        } catch (Exception e) {
            System.err.println("[StudentDAO] Error fetching student by regNo: " + e.getMessage());
            for (Student s : getMockStudents()) {
                if (s.getRegNo().equalsIgnoreCase(regNo)) return s;
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            DBConnection.closeConnection(conn);
        }
        return null;
    }

    /**
     * Fetches a single student by ID.
     */
    public Student getStudentById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT id, reg_no, name, email, department, year_of_study, cgpa, " +
                         "profile_image_path, document_path, document_name, status " +
                         "FROM students WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setRegNo(rs.getString("reg_no"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                s.setDepartment(rs.getString("department"));
                s.setYearOfStudy(rs.getInt("year_of_study"));
                s.setCgpa(rs.getDouble("cgpa"));
                s.setProfileImagePath(rs.getString("profile_image_path"));
                s.setDocumentPath(rs.getString("document_path"));
                s.setDocumentName(rs.getString("document_name"));
                s.setStatus(rs.getString("status"));
                return s;
            }
        } catch (Exception e) {
            System.err.println("[StudentDAO] Error fetching student by id: " + e.getMessage());
            for (Student s : getMockStudents()) {
                if (s.getId() == id) return s;
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            DBConnection.closeConnection(conn);
        }
        return null;
    }

    /**
     * Offline fallback mock data matching database.sql
     */
    private List<Student> getMockStudents() {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, "24CS016", "Amirtha Varshini K V", "amirthavarshini@example.com",
                "Computer Science & Engineering (AIML)", 3, 9.45,
                "uploads/images/student1.jpg", "uploads/documents/transcript_24cs016.pdf",
                "Academic_Transcript_Sem5.pdf", "Active"));

        list.add(new Student(2, "24CS004", "Rahul Kumar", "rahul.kumar@example.com",
                "Computer Science & Engineering", 3, 8.62,
                "uploads/images/student2.jpg", "uploads/documents/project_report_24cs004.pdf",
                "AI_Healthcare_Project_Report.pdf", "Active"));

        list.add(new Student(3, "24CS030", "Priya Sharma", "priya.sharma@example.com",
                "Information Technology", 4, 9.18,
                "uploads/images/student3.jpg", "uploads/documents/certificate_24cs030.pdf",
                "Internship_Completion_Certificate.pdf", "Active"));

        list.add(new Student(4, "24CS008", "David Miller", "david.miller@example.com",
                "Artificial Intelligence & Data Science", 2, 7.35,
                "uploads/images/student4.jpg", null, null, "Active"));

        list.add(new Student(5, "24CS045", "Ananya Ramesh", "ananya.ramesh@example.com",
                "Computer Science & Engineering", 4, 8.90,
                "uploads/images/default_avatar.jpg", null, null, "On Leave"));

        return list;
    }
}
