package com.student.model;

import java.io.Serializable;

/**
 * Model class representing a Student entity.
 * Holds personal details, academic metrics, and file paths for image/documents.
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String regNo;
    private String name;
    private String email;
    private String department;
    private int yearOfStudy;
    private double cgpa;
    private String profileImagePath;
    private String documentPath;
    private String documentName;
    private String status;

    public Student() {
    }

    public Student(int id, String regNo, String name, String email, String department, 
                   int yearOfStudy, double cgpa, String profileImagePath, 
                   String documentPath, String documentName, String status) {
        this.id = id;
        this.regNo = regNo;
        this.name = name;
        this.email = email;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.cgpa = cgpa;
        this.profileImagePath = profileImagePath;
        this.documentPath = documentPath;
        this.documentName = documentName;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Helper method for conditional checks
    public boolean hasDocument() {
        return documentPath != null && !documentPath.trim().isEmpty();
    }

    // Helper method for conditional academic distinction check
    public String getGradeClassification() {
        if (cgpa >= 9.0) {
            return "First Class with Distinction (Honors)";
        } else if (cgpa >= 7.5) {
            return "First Class";
        } else if (cgpa >= 6.0) {
            return "Second Class";
        } else {
            return "Pass";
        }
    }
}
