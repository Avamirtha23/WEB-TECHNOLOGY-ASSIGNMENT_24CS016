package com.student.servlet;

import com.student.dao.StudentDAO;
import com.student.model.Student;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Servlet responsible for retrieving and streaming student profile images.
 * Implements Task 2: Retrieve and stream images from the folder where they are stored.
 * Implements Task 3: Serves image data for <img> tags dynamically.
 */
@WebServlet(name = "ImageStreamServlet", urlPatterns = {"/ImageStreamServlet", "/stream-image"})
public class ImageStreamServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StudentDAO studentDAO;

    @Override
    public void init() throws ServletException {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String regNo = request.getParameter("regNo");
        String idParam = request.getParameter("id");
        String relativePath = null;

        // 1. Fetch student image path from Database via DAO
        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idParam.trim());
                Student student = studentDAO.getStudentById(id);
                if (student != null) {
                    relativePath = student.getProfileImagePath();
                }
            } catch (NumberFormatException ignored) {}
        } else if (regNo != null && !regNo.trim().isEmpty()) {
            Student student = studentDAO.getStudentByRegNo(regNo.trim());
            if (student != null) {
                relativePath = student.getProfileImagePath();
            }
        }

        // 2. Resolve image file from disk
        File imageFile = resolveImageFile(relativePath, request);

        // 3. If file exists, stream the image bytes to response output stream
        if (imageFile != null && imageFile.exists() && imageFile.isFile()) {
            streamFile(imageFile, response);
        } else {
            // Stream default placeholder SVG avatar
            streamFallbackAvatar(response);
        }
    }

    /**
     * Resolves the actual File on disk given the relative path.
     * Checks multiple plausible locations: web application context, workspace root, uploads folder.
     */
    private File resolveImageFile(String relativePath, HttpServletRequest request) {
        if (relativePath == null || relativePath.trim().isEmpty()) {
            return null;
        }

        // Security check against directory traversal
        if (relativePath.contains("..")) {
            return null;
        }

        // Try ServletContext deployment directory
        String contextPath = request.getServletContext().getRealPath("/" + relativePath);
        if (contextPath != null) {
            File file = new File(contextPath);
            if (file.exists()) return file;
        }

        // Try direct file path relative to current working directory
        File localFile = new File(relativePath);
        if (localFile.exists()) return localFile;

        // Try e:/web technology assignments2/ex3/
        File directFile = new File("e:/web technology assignments2/ex3/" + relativePath);
        if (directFile.exists()) return directFile;

        return null;
    }

    /**
     * Streams binary file contents to HttpServletResponse with appropriate MIME headers.
     */
    private void streamFile(File file, HttpServletResponse response) throws IOException {
        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) {
            if (file.getName().toLowerCase().endsWith(".png")) contentType = "image/png";
            else if (file.getName().toLowerCase().endsWith(".webp")) contentType = "image/webp";
            else contentType = "image/jpeg";
        }

        response.setContentType(contentType);
        response.setContentLengthLong(file.length());
        response.setHeader("Cache-Control", "public, max-age=86400"); // Cache image for 1 day

        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        }
    }

    /**
     * Streams a high quality SVG avatar if the requested image is missing.
     */
    private void streamFallbackAvatar(HttpServletResponse response) throws IOException {
        response.setContentType("image/svg+xml");
        String svg = "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100' width='100' height='100'>"
                + "<circle cx='50' cy='50' r='50' fill='#94a3b8'/>"
                + "<circle cx='50' cy='38' r='18' fill='#ffffff'/>"
                + "<ellipse cx='50' cy='82' rx='32' ry='22' fill='#ffffff'/>"
                + "</svg>";
        response.getOutputStream().write(svg.getBytes());
        response.getOutputStream().flush();
    }
}
