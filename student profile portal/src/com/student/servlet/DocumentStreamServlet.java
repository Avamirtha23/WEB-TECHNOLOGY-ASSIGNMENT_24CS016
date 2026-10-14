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
 * Servlet responsible for retrieving and streaming student uploaded documents (PDFs, certs, etc.).
 * Implements Task 2: Retrieve and stream documents from the folder where they are stored.
 */
@WebServlet(name = "DocumentStreamServlet", urlPatterns = {"/DocumentStreamServlet", "/stream-document"})
public class DocumentStreamServlet extends HttpServlet {
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
        String downloadParam = request.getParameter("download");
        boolean isDownload = "true".equalsIgnoreCase(downloadParam);

        Student student = null;
        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idParam.trim());
                student = studentDAO.getStudentById(id);
            } catch (NumberFormatException ignored) {}
        } else if (regNo != null && !regNo.trim().isEmpty()) {
            student = studentDAO.getStudentByRegNo(regNo.trim());
        }

        if (student == null || student.getDocumentPath() == null || student.getDocumentPath().trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No document associated with this student record.");
            return;
        }

        File docFile = resolveDocumentFile(student.getDocumentPath(), request);

        if (docFile == null || !docFile.exists() || !docFile.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Document file not found on server.");
            return;
        }

        // Detect Content-Type MIME
        String mimeType = Files.probeContentType(docFile.toPath());
        if (mimeType == null) {
            mimeType = "application/pdf";
        }
        response.setContentType(mimeType);
        response.setContentLengthLong(docFile.length());

        String displayFileName = (student.getDocumentName() != null && !student.getDocumentName().isEmpty())
                ? student.getDocumentName() : docFile.getName();

        // If download=true, set attachment header; otherwise inline for browser PDF viewer
        String dispositionType = isDownload ? "attachment" : "inline";
        response.setHeader("Content-Disposition", dispositionType + "; filename=\"" + displayFileName + "\"");

        // Stream document bytes
        try (FileInputStream in = new FileInputStream(docFile);
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        }
    }

    private File resolveDocumentFile(String relativePath, HttpServletRequest request) {
        if (relativePath == null || relativePath.trim().isEmpty() || relativePath.contains("..")) {
            return null;
        }

        String contextPath = request.getServletContext().getRealPath("/" + relativePath);
        if (contextPath != null) {
            File file = new File(contextPath);
            if (file.exists()) return file;
        }

        File localFile = new File(relativePath);
        if (localFile.exists()) return localFile;

        File directFile = new File("e:/web technology assignments2/ex3/" + relativePath);
        if (directFile.exists()) return directFile;

        return null;
    }
}
