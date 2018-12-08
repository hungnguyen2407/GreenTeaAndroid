package com.greentea.model;

public class Faculty {
    private String facultyId;
    private String facultyName;

    public Faculty(String facultyId, String facultyName) {
        this.facultyId = facultyId;
        this.facultyName = facultyName;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }
}
