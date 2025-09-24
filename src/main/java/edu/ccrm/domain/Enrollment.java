package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Enrollment class representing student-course relationships
 * Demonstrates composition and association
 */
public class Enrollment {
    private String enrollmentId;
    private String studentId;
    private String courseId;
    private LocalDateTime enrollmentDate;
    private Grade grade;
    private boolean isCompleted;
    private String semester;

    public Enrollment(String enrollmentId, String studentId, String courseId, String semester) {
        this.enrollmentId = Objects.requireNonNull(enrollmentId, "Enrollment ID cannot be null");
        this.studentId = Objects.requireNonNull(studentId, "Student ID cannot be null");
        this.courseId = Objects.requireNonNull(courseId, "Course ID cannot be null");
        this.semester = Objects.requireNonNull(semester, "Semester cannot be null");
        this.enrollmentDate = LocalDateTime.now();
        this.isCompleted = false;

        assert !enrollmentId.trim().isEmpty() : "Enrollment ID cannot be empty";
    }

    // Getters and setters
    public String getEnrollmentId() { return enrollmentId; }

    public String getStudentId() { return studentId; }

    public String getCourseId() { return courseId; }

    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }

    public Grade getGrade() { return grade; }

    public void setGrade(Grade grade) {
        this.grade = grade;
        if (grade != null) {
            this.isCompleted = true;
        }
    }

    public boolean isCompleted() { return isCompleted; }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    public String getSemester() { return semester; }

    public void setSemester(String semester) {
        this.semester = Objects.requireNonNull(semester, "Semester cannot be null");
    }

    public boolean isPassing() {
        return grade != null && grade.isPassing();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Enrollment that = (Enrollment) obj;
        return Objects.equals(enrollmentId, that.enrollmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enrollmentId);
    }

    @Override
    public String toString() {
        return String.format("Enrollment{id='%s', student='%s', course='%s', semester='%s', grade=%s, completed=%s}", 
                           enrollmentId, studentId, courseId, semester, grade, isCompleted);
    }
}