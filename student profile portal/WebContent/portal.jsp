<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.student.model.Student" %>
<%@ page import="com.student.model.User" %>
<%
    // Prevent back-button caching
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    // Task 5: Session Handling - Verify authenticated user session
    javax.servlet.http.HttpSession currentSession = request.getSession(false);
    if (currentSession == null || currentSession.getAttribute("currentUser") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=unauthorized");
        return;
    }
    User currentUser = (User) currentSession.getAttribute("currentUser");
    String displayName = (String) currentSession.getAttribute("displayName");
    String userRole = (String) currentSession.getAttribute("userRole");

    // Fetch the student list populated by StudentListServlet via JDBC (Task 1)
    List<Student> studentList = (List<Student>) request.getAttribute("studentList");
    if (studentList == null) {
        response.sendRedirect(request.getContextPath() + "/students");
        return;
    }

    String searchParam = (String) request.getAttribute("searchParam");
    String deptParam = (String) request.getAttribute("deptParam");
    if (searchParam == null) searchParam = "";
    if (deptParam == null) deptParam = "ALL";
%>
                        <!DOCTYPE html>
                        <html lang="en">

                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Student Profiles</title>
                            <!-- Google Fonts Inter -->
                            <link rel="preconnect" href="https://fonts.googleapis.com">
                            <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
                            <link
                                href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap"
                                rel="stylesheet">
                            <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css?v=light1">
                        </head>

                        <body>

                            <!-- Task 5: Authenticated User Header & Session Controls -->
                            <header class="navbar">
                                <a href="<%= request.getContextPath() %>/students" class="nav-brand">
                                    <div class="nav-logo-badge">🎓</div>
                                    <div class="nav-title">
                                        <h1>Student Profile & Records Portal</h1>
                                    </div>
                                </a>

                                <div class="nav-user">
                                    <div class="user-badge">
                                        <span class="user-avatar-dot"></span>
                                        <div class="user-details">
                                            <div class="user-name">
                                                <%= displayName !=null ? displayName : currentUser.getUsername() %>
                                            </div>
                                            <div class="user-role">
                                                <%= userRole !=null ? userRole : "Authorized User" %> &bull; Session
                                                    Active
                                            </div>
                                        </div>
                                    </div>
                                    <a href="<%= request.getContextPath() %>/logout" class="btn-logout" id="btn-logout"
                                        title="End Session">
                                        <span>🚪</span> Logout
                                    </a>
                                </div>
                            </header>

                            <main class="main-container">
                                <!-- Page Title & Requirement Badges -->
                                <section class="page-header">
                                    <div class="header-text">
                                        <h2>Student Directory</h2>
                                    </div>
                                </section>

                                <!-- Filter & Search Controls -->
                                <section class="filter-bar">
                                    <form action="<%= request.getContextPath() %>/students" method="GET"
                                        class="filter-form">
                                        <div class="search-input-wrapper">
                                            <span class="search-icon">🔍</span>
                                            <input type="text" name="search" class="search-input"
                                                placeholder="Search by student name, Reg. No, or email..."
                                                value="<%= searchParam %>">
                                        </div>

                                        <select name="department" class="dept-select">
                                            <option value="ALL" <%="ALL" .equals(deptParam) ? "selected" : "" %>>All
                                                Departments</option>
                                            <option value="Computer Science & Engineering"
                                                <%="Computer Science & Engineering" .equals(deptParam) ? "selected" : ""
                                                %>>CSE</option>
                                            <option value="Computer Science & Engineering (AIML)"
                                                <%="Computer Science & Engineering (AIML)" .equals(deptParam)
                                                ? "selected" : "" %>>CSE (AIML)</option>
                                            <option value="Information Technology" <%="Information Technology"
                                                .equals(deptParam) ? "selected" : "" %>>IT</option>
                                            <option value="Artificial Intelligence & Data Science"
                                                <%="Artificial Intelligence & Data Science" .equals(deptParam)
                                                ? "selected" : "" %>>AI &amp; DS</option>
                                        </select>

                                        <button type="submit" class="btn-filter">Filter</button>
                                        <% if (!searchParam.isEmpty() || !"ALL".equals(deptParam)) { %>
                                            <a href="<%= request.getContextPath() %>/students" class="btn-reset">Clear
                                                Filters</a>
                                            <% } %>
                                    </form>

                                    <div class="stats-counter">
                                        Showing <strong>
                                            <%= studentList.size() %>
                                        </strong> student record(s)
                                    </div>
                                </section>

                                <!-- ====================================================================
             Task 4: Looping and Conditional Logic
             Iterates through student profiles and applies conditional formatting
             ==================================================================== -->
                                <section class="students-grid">
                                    <% if (studentList==null || studentList.isEmpty()) { %>
                                        <div class="empty-state">
                                            <div class="empty-icon">📂</div>
                                            <h3>No Student Records Found</h3>
                                            <p>No profiles match your current search criteria. Try modifying your
                                                filters or clearing search terms.</p>
                                        </div>
                                        <% } else { for (Student student : studentList) { String gradeBadgeClass; String
                                            gradeLabel; if (student.getCgpa()>= 9.0) {
                                            gradeBadgeClass = "badge-distinction";
                                            gradeLabel = "Distinction (Honors)";
                                            } else if (student.getCgpa() >= 7.5) {
                                            gradeBadgeClass = "badge-firstclass";
                                            gradeLabel = "First Class";
                                            } else {
                                            gradeBadgeClass = "badge-secondclass";
                                            gradeLabel = "Second Class";
                                            }

                                            boolean isActive = "Active".equalsIgnoreCase(student.getStatus());
                                            %>
                                            <article class="student-card">
                                                <div>
                                                    <!-- Card Top: Avatar & Identification -->
                                                    <div class="card-top">
                                                        <div class="avatar-wrapper">
                                                            <!-- Task 3: Display student profile dynamically with <img> tag pointing to ImageStreamServlet -->
                                                            <img src="<%= request.getContextPath() %>/stream-image?id=<%= student.getId() %>"
                                                                alt="<%= student.getName() %>" class="student-avatar"
                                                                loading="lazy" />
                                                            <span class="status-indicator <%= isActive ? "status-active" : "status-inactive" %>" title="Status: <%= student.getStatus() %>"></span>
                                                        </div>

                                                        <div class="card-title-group">
                                                            <h3 class="student-name" title="<%= student.getName() %>">
                                                                <%= student.getName() %>
                                                            </h3>
                                                            <div class="student-regno">
                                                                <%= student.getRegNo() %>
                                                            </div>
                                                            <div class="badge-group">
                                                                <span class="badge <%= gradeBadgeClass %>">
                                                                    <%= gradeLabel %>
                                                                </span>
                                                                <span class="badge badge-status">
                                                                    <%= student.getStatus() %>
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <!-- Card Body: Academic Details -->
                                                    <div class="card-details">
                                                        <div class="detail-row">
                                                            <span class="detail-label">Department</span>
                                                            <span class="detail-value"
                                                                title="<%= student.getDepartment() %>">
                                                                <%= student.getDepartment() %>
                                                            </span>
                                                        </div>
                                                        <div class="detail-row">
                                                            <span class="detail-label">Year of Study</span>
                                                            <span class="detail-value">Year <%= student.getYearOfStudy()
                                                                    %></span>
                                                        </div>
                                                        <div class="detail-row">
                                                            <span class="detail-label">Cumulative GPA</span>
                                                            <span class="detail-value"><span class="cgpa-pill">
                                                                    <%= String.format("%.2f", student.getCgpa()) %>
                                                                </span> / 10.0</span>
                                                        </div>
                                                        <div class="detail-row">
                                                            <span class="detail-label">Email</span>
                                                            <span class="detail-value"
                                                                title="<%= student.getEmail() %>">
                                                                <%= student.getEmail() %>
                                                            </span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <!-- Task 4: Conditional Logic 4 - Document Attachment Check -->
                                                <div class="card-footer">
                                                    <% if (student.hasDocument()) { %>
                                                        <div class="doc-status-box doc-present">
                                                            <span class="doc-name">
                                                                <span>📄</span>
                                                                <%= student.getDocumentName() !=null ?
                                                                    student.getDocumentName() : "Official Document" %>
                                                            </span>
                                                        </div>
                                                        <!-- Task 2: Retrieve and stream documents from folder via DocumentStreamServlet -->
                                                        <div class="btn-group">
                                                            <a href="<%= request.getContextPath() %>/stream-document?id=<%= student.getId() %>"
                                                                target="_blank" class="btn-doc-view"
                                                                title="View Document in new tab">
                                                                <span>👁️</span> View File
                                                            </a>
                                                            <a href="<%= request.getContextPath() %>/stream-document?id=<%= student.getId() %>&download=true"
                                                                class="btn-doc-download"
                                                                title="Download file to computer">
                                                                <span>⬇️</span> Download
                                                            </a>
                                                        </div>
                                                        <% } else { %>
                                                            <div class="doc-status-box doc-missing">
                                                                <span class="doc-name">
                                                                    <span>⚠️</span> No Document Uploaded
                                                                </span>
                                                                <span class="badge"
                                                                    style="background: #fff1f2; color: #9f1239;">Pending</span>
                                                            </div>
                                                            <% } %>
                                                </div>
                                            </article>
                                            <% } } %>
                                </section>
                            </main>

                            <footer class="portal-footer">
                                <p>&copy; 2026 Student Profile & Records Portal &bull; Session Protected</p>
                            </footer>

                            <script src="<%= request.getContextPath() %>/js/app.js"></script>
                        </body>

                        </html>