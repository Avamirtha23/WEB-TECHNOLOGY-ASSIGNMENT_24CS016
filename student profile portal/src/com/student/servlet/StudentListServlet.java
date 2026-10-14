package com.student.servlet;

import com.student.dao.StudentDAO;
import com.student.model.Student;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller Servlet for loading and filtering student profiles.
 * Implements Task 1: Connect to MySQL using JDBC to fetch student details and associated file paths.
 * Implements Task 4: Supports dynamic listing, filtering, and data passing to JSP.
 */
@WebServlet(name = "StudentListServlet", urlPatterns = {"/students", "/StudentPortal"})
public class StudentListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StudentDAO studentDAO;

    @Override
    public void init() throws ServletException {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Fetch all students from MySQL via JDBC
        List<Student> allStudents = studentDAO.getAllStudents();
        List<Student> filteredList = new ArrayList<>();

        String search = request.getParameter("search");
        String department = request.getParameter("department");

        // Conditional filtering
        for (Student s : allStudents) {
            boolean matchesSearch = true;
            boolean matchesDept = true;

            if (search != null && !search.trim().isEmpty()) {
                String query = search.trim().toLowerCase();
                matchesSearch = s.getName().toLowerCase().contains(query) || 
                                s.getRegNo().toLowerCase().contains(query) ||
                                s.getEmail().toLowerCase().contains(query);
            }

            if (department != null && !department.trim().isEmpty() && !"ALL".equalsIgnoreCase(department)) {
                matchesDept = s.getDepartment().equalsIgnoreCase(department.trim());
            }

            if (matchesSearch && matchesDept) {
                filteredList.add(s);
            }
        }

        // Attach attributes to request scope for JSP rendering
        request.setAttribute("studentList", filteredList);
        request.setAttribute("totalCount", allStudents.size());
        request.setAttribute("filteredCount", filteredList.size());
        request.setAttribute("searchParam", search != null ? search : "");
        request.setAttribute("deptParam", department != null ? department : "ALL");

        // Forward to the JSP portal page
        request.getRequestDispatcher("/portal.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
