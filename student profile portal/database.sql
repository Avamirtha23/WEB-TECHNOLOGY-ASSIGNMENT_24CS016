-- ====================================================================
-- Web Technology Lab - Exercise 03 / Assignment 03
-- Student Profile Portal Database Schema & Seed Data
-- ====================================================================

-- 1. Create Database if not exists
CREATE DATABASE IF NOT EXISTS student_portal;
USE student_portal;

-- 2. Drop existing tables if needed for a clean setup
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS users;

-- 3. Users table for Authentication and Session Handling
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(30) DEFAULT 'Faculty',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Students table for Profile Details and File Streaming Paths
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
    status VARCHAR(20) DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Insert Default Portal Users (Password: admin123, faculty123)
INSERT INTO users (username, password, full_name, role) VALUES 
('admin', 'admin123', 'System Administrator', 'Administrator'),
('faculty', 'faculty123', 'Dr. Rajeshkumar S', 'Faculty Advisor'),
('amirtha', 'amirtha123', 'Amirtha Varshini K V', 'Student Coordinator');

-- 6. Insert Sample Student Profiles with Associated File Paths
INSERT INTO students (reg_no, name, email, department, year_of_study, cgpa, profile_image_path, document_path, document_name, status) VALUES
(
    '24CS016', 
    'Amirtha Varshini K V', 
    'amirthavarshini@example.com', 
    'Computer Science & Engineering (AIML)', 
    3, 
    9.45, 
    'uploads/images/student1.jpg', 
    'uploads/documents/transcript_24cs016.pdf', 
    'Academic_Transcript_Sem5.pdf', 
    'Active'
),
(
    '24CS004', 
    'Rahul Kumar', 
    'rahul.kumar@example.com', 
    'Computer Science & Engineering', 
    3, 
    8.62, 
    'uploads/images/student2.jpg', 
    'uploads/documents/project_report_24cs004.pdf', 
    'AI_Healthcare_Project_Report.pdf', 
    'Active'
),
(
    '24CS030', 
    'Priya Sharma', 
    'priya.sharma@example.com', 
    'Information Technology', 
    4, 
    9.18, 
    'uploads/images/student3.jpg', 
    'uploads/documents/certificate_24cs030.pdf', 
    'Internship_Completion_Certificate.pdf', 
    'Active'
),
(
    '24CS008', 
    'David Miller', 
    'david.miller@example.com', 
    'Artificial Intelligence & Data Science', 
    2, 
    7.35, 
    'uploads/images/student4.jpg', 
    NULL, 
    NULL, 
    'Active'
),
(
    '24CS045', 
    'Ananya Ramesh', 
    'ananya.ramesh@example.com', 
    'Computer Science & Engineering', 
    4, 
    8.90, 
    'uploads/images/default_avatar.jpg', 
    NULL, 
    NULL, 
    'On Leave'
);

-- Verify records
SELECT * FROM users;
SELECT * FROM students;
