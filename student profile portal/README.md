# Student Profile & Records Portal (Java Servlets, JDBC, JSP, MySQL)

**Course**: U21CS504 - Web Technologies Lab  
**Assignment / Exercise**: Experiment 03 - Scenario 3  
**Marks**: 16 | **Bloom's Level**: AP (Apply) | **Course Outcome**: CO5  

---

## 📌 Problem Statement & Requirements Checklist

| Requirement # | Task Description | Implementation File(s) | Status |
|---|---|---|---|
| **Task 1** | Connect to MySQL using JDBC to fetch student details and associated file paths. | `DBConnection.java`, `StudentDAO.java`, `database.sql` | ✅ Complete |
| **Task 2** | Retrieve and stream images/documents from the folder where they are stored. | `ImageStreamServlet.java`, `DocumentStreamServlet.java` | ✅ Complete |
| **Task 3** | Display student profiles dynamically in an HTML/JSP page with `<img>` tags pointing to the Servlet. | `portal.jsp` (`<img src="stream-image?id=..." />`) | ✅ Complete |
| **Task 4** | Implement looping and conditional logic to list multiple students and their files. | `portal.jsp` (for-loop, distinction badges, document existence check) | ✅ Complete |
| **Task 5** | Ensure session handling so that only logged-in users can access the portal securely. | `LoginServlet.java`, `LogoutServlet.java`, `AuthFilter.java` | ✅ Complete |

---

## 🏗️ Architecture & Component Design

```
                     ┌──────────────────────────────────────────────┐
                     │              Web Browser (Client)            │
                     └──────────────────────┬───────────────────────┘
                                            │ HTTP Requests (Login / Stream / View)
                                            ▼
                     ┌──────────────────────────────────────────────┐
                     │         AuthFilter (Session Security)        │
                     │ (Blocks unauthorized requests, checks cookie)│
                     └──────────────────────┬───────────────────────┘
                                            │
         ┌──────────────────┬───────────────┴───────────────┬──────────────────┐
         ▼                  ▼                               ▼                  ▼
┌──────────────────┐ ┌───────────────┐            ┌──────────────────┐ ┌──────────────────┐
│   LoginServlet   │ │ StudentList   │            │ImageStreamServlet│ │DocumentStream    │
│  & LogoutServlet │ │    Servlet    │            │                  │ │     Servlet      │
└────────┬─────────┘ └───────┬───────┘            └────────┬─────────┘ └────────┬─────────┘
         │ Authenticate      │ Fetch List                  │ Stream Image       │ Stream PDF
         ▼                   ▼                             ▼                    ▼
┌──────────────────┐ ┌──────────────────────────────────────────────┐ ┌──────────────────┐
│  HttpSession     │ │               StudentDAO                     │ │  Local Disk      │
│  Management      │ │          (JDBC PreparedStatements)           │ │ (uploads/ folder)│
└──────────────────┘ └──────────────────────┬───────────────────────┘ └──────────────────┘
                                            │
                                            ▼
                             ┌──────────────────────────────┐
                             │    MySQL Database Server     │
                             │  (student_portal database)   │
                             └──────────────────────────────┘
```

---

## 📂 Project Directory Structure

```
e:\web technology assignments2\ex3\
│
├── database.sql                    # MySQL database schema and seed records
├── build.bat                       # 1-click Windows CMD compile & deploy script
├── build.ps1                       # Automated PowerShell build & deploy script
├── StudentPortal.war               # Pre-built WAR archive ready for Tomcat
├── README.md                       # Complete documentation & viva voce guide
│
├── uploads/                        # Physical storage folder on disk
│   ├── images/                     # Student profile photos
│   │   ├── student1.jpg
│   │   ├── student2.jpg
│   │   ├── student3.jpg
│   │   ├── student4.jpg
│   │   └── default_avatar.jpg
│   └── documents/                  # Uploaded academic documents / PDFs
│       ├── transcript_24cs016.pdf
│       ├── project_report_24cs004.pdf
│       └── certificate_24cs030.pdf
│
├── src/com/student/
│   ├── model/
│   │   ├── Student.java            # Student model bean (fields, getters/setters, helpers)
│   │   └── User.java               # Portal authenticated user bean
│   ├── util/
│   │   └── DBConnection.java       # JDBC connection manager
│   ├── dao/
│   │   ├── StudentDAO.java         # JDBC queries for students (with demo fallback)
│   │   └── UserDAO.java            # JDBC authentication query
│   ├── servlet/
│   │   ├── LoginServlet.java       # Handles login & creates HttpSession
│   │   ├── LogoutServlet.java      # Invalidates session & clears cookies
│   │   ├── StudentListServlet.java # Controller: fetches data via JDBC & forwards to JSP
│   │   ├── ImageStreamServlet.java # Streams student profile pictures dynamically
│   │   └── DocumentStreamServlet.java # Streams uploaded PDF files (inline/attachment)
│   └── filter/
│       └── AuthFilter.java         # Security filter protecting endpoints
│
└── WebContent/                     # Web application root
    ├── css/
    │   └── style.css               # Modern glassmorphism UI styling
    ├── js/
    │   └── app.js                  # Client-side UI enhancements
    ├── index.jsp                   # Smart redirect based on session state
    ├── login.jsp                   # Modern login interface with alerts
    ├── portal.jsp                  # Main JSP rendering profiles dynamically
    ├── uploads/                    # Deployed media files
    └── WEB-INF/
        ├── web.xml                 # Deployment descriptor
        ├── classes/                # Compiled .class files
        └── lib/
            └── mysql-connector-j-8.3.0.jar
```

---

## 🗄️ Database Setup (MySQL)

1. Open **MySQL Command Line Client** or **MySQL Workbench**.
2. Run the provided script [database.sql](file:///e:/web%20technology%20assignments2/ex3/database.sql):
   ```sql
   SOURCE e:/web technology assignments2/ex3/database.sql;
   ```
3. Or manually copy and paste the SQL commands from `database.sql`:
   ```sql
   CREATE DATABASE IF NOT EXISTS student_portal;
   USE student_portal;

   CREATE TABLE users (
       id INT AUTO_INCREMENT PRIMARY KEY,
       username VARCHAR(50) NOT NULL UNIQUE,
       password VARCHAR(100) NOT NULL,
       full_name VARCHAR(100) NOT NULL,
       role VARCHAR(30) DEFAULT 'Faculty'
   );

   CREATE TABLE students (
       id INT AUTO_INCREMENT PRIMARY KEY,
       reg_no VARCHAR(20) NOT NULL UNIQUE,
       name VARCHAR(100) NOT NULL,
       email VARCHAR(100) NOT NULL,
       department VARCHAR(50) NOT NULL,
       year_of_study INT NOT NULL,
       cgpa DECIMAL(3, 2) NOT NULL,
       profile_image_path VARCHAR(255) NOT NULL,
       document_path VARCHAR(255),
       document_name VARCHAR(150),
       status VARCHAR(20) DEFAULT 'Active'
   );
   ```

### Pre-seeded Portal Logins:
- **Admin**: `admin` / `admin123`
- **Faculty**: `faculty` / `faculty123`
- **Student Coordinator**: `amirtha` / `amirtha123`

---

## 🚀 How to Run the Application

### Option A: Using the Automated Build Script (Recommended)
1. Run `build.bat` or `build.ps1` in PowerShell:
   ```powershell
   cd "e:\web technology assignments2\ex3"
   .\build.ps1
   ```
2. Start Apache Tomcat:
   ```powershell
   & "C:\Users\amirt\Downloads\apache-tomcat-10.1.61-windows-x64\apache-tomcat-10.1.61\bin\startup.bat"
   ```
3. Open your browser and navigate to:
   ```
   http://localhost:8080/StudentPortal/
   ```

### Option B: Deploying WAR directly
Copy `StudentPortal.war` to Tomcat's `webapps/` folder and start Tomcat.

---

## 📝 Code Explanation & Requirement Walkthrough

### 1. Connecting to MySQL using JDBC (Task 1)
In `DBConnection.java`:
```java
Class.forName("com.mysql.cj.jdbc.Driver");
Connection conn = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/student_portal?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
    "root", "root"
);
```
In `StudentDAO.java`:
```java
PreparedStatement ps = conn.prepareStatement(
    "SELECT id, reg_no, name, email, department, year_of_study, cgpa, " +
    "profile_image_path, document_path, document_name, status FROM students"
);
ResultSet rs = ps.executeQuery();
```

### 2. Streaming Images & Documents via Servlets (Task 2)
In `ImageStreamServlet.java`:
- The servlet reads the file path from the database, opens a `FileInputStream`, sets the `Content-Type` header (e.g. `image/jpeg`), and writes binary chunks to `response.getOutputStream()`:
```java
response.setContentType("image/jpeg");
try (FileInputStream in = new FileInputStream(imageFile);
     OutputStream out = response.getOutputStream()) {
    byte[] buffer = new byte[8192];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
    }
    out.flush();
}
```

### 3. Displaying Profiles Dynamically with `<img>` pointing to Servlet (Task 3)
In `portal.jsp`:
```jsp
<img src="<%= request.getContextPath() %>/stream-image?id=<%= student.getId() %>" 
     alt="<%= student.getName() %>" 
     class="student-avatar" />
```
The browser requests the image directly from the servlet URL `/stream-image?id=...`, which streams back the binary JPEG/PNG bytes dynamically!

### 4. Looping and Conditional Logic (Task 4)
In `portal.jsp`:
- **Looping Logic**:
```jsp
<% for (Student student : studentList) { %>
    <!-- Dynamic Student Card -->
<% } %>
```
- **Conditional Document Check**:
```jsp
<% if (student.hasDocument()) { %>
    <a href="stream-document?id=<%= student.getId() %>" target="_blank">View File</a>
    <a href="stream-document?id=<%= student.getId() %>&download=true">Download</a>
<% } else { %>
    <span class="badge">No Document Uploaded</span>
<% } %>
```
- **Conditional Academic Distinction**:
```jsp
<%
    if (student.getCgpa() >= 9.0) {
        // First Class with Distinction (Gold badge)
    } else if (student.getCgpa() >= 7.5) {
        // First Class (Emerald badge)
    } else {
        // Second Class (Blue badge)
    }
%>
```

### 5. Session Handling & Security (Task 5)
- **Login (`LoginServlet.java`)**:
```java
HttpSession session = request.getSession(true);
session.setAttribute("currentUser", authenticatedUser);
session.setAttribute("username", authenticatedUser.getUsername());
session.setMaxInactiveInterval(30 * 60); // 30 minutes
```
- **Auth Filter (`AuthFilter.java`)**:
```java
HttpSession session = req.getSession(false);
if (session == null || session.getAttribute("currentUser") == null) {
    res.sendRedirect(req.getContextPath() + "/login.jsp?error=unauthorized");
    return;
}
```
- **Logout (`LogoutServlet.java`)**:
```java
HttpSession session = request.getSession(false);
if (session != null) {
    session.invalidate();
}
response.sendRedirect("login.jsp?logout=true");
```

---

## 🎯 Viva Voce Questions & Answers

### Q1: What is the role of JDBC in this application?
**Ans**: JDBC (Java Database Connectivity) provides an API (`java.sql`) that allows the Java Servlet application to connect to the MySQL database, execute SQL queries via `PreparedStatement`, and process result sets (`ResultSet`) to populate Java domain objects (`Student`, `User`).

### Q2: Why stream images through a Servlet instead of using static image URLs?
**Ans**:
1. **Security & Access Control**: The Servlet checks whether the user has an active authenticated HTTP session before serving the file. Direct static files cannot be easily protected by application session filters.
2. **Decoupled Physical Storage**: Files can be stored outside the web server root or on a private network share, preventing users from guessing URLs.
3. **Dynamic Content Negotiation**: The servlet can dynamically resize, water-mark, or return a fallback default avatar if the file is missing.

### Q3: How does binary streaming work in Servlets?
**Ans**: We use `response.getOutputStream()` (an instance of `ServletOutputStream`) instead of `response.getWriter()`. Binary data is read from disk via `FileInputStream` into a byte buffer (e.g. 8KB) and written directly to the response output stream with the appropriate MIME `Content-Type` header.

### Q4: What is the difference between `request.getSession(true)` and `request.getSession(false)`?
**Ans**:
- `request.getSession(true)` (or `request.getSession()`): Returns the current session associated with this request, or creates and returns a brand new session if none exists. (Used during Login).
- `request.getSession(false)`: Returns the current session if one exists, or returns `null` if no valid session exists. (Used during authorization checks and Logout).

### Q5: How do you prevent browser back-button access after logout?
**Ans**: By configuring HTTP response headers that disable client-side and proxy caching:
```java
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
response.setHeader("Pragma", "no-cache"); // HTTP 1.0
response.setDateHeader("Expires", 0); // Proxies
```
This forces the browser to re-request the page from the server upon back navigation, triggering the `AuthFilter` which redirects unauthenticated requests to `login.jsp`.
