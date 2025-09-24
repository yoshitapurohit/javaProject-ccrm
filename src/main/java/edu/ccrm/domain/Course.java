package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Course class representing academic courses
 * Demonstrates encapsulation and composition
 */
public class Course {
    private String courseId;
    private String courseCode;
    private String title;
    private String description;
    private int credits;
    private String department;
    private String semester;
    private String instructorId;
    private Set<String> prerequisites; // Course IDs
    private Set<String> enrolledStudents; // Student IDs
    private int maxEnrollment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Course(String courseId, String courseCode, String title, String description,
                 int credits, String department, String semester) {
        this.courseId = Objects.requireNonNull(courseId, "Course ID cannot be null");
        this.courseCode = Objects.requireNonNull(courseCode, "Course code cannot be null");
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.description = description;
        this.credits = credits;
        this.department = Objects.requireNonNull(department, "Department cannot be null");
        this.semester = Objects.requireNonNull(semester, "Semester cannot be null");
        this.prerequisites = new HashSet<>();
        this.enrolledStudents = new HashSet<>();
        this.maxEnrollment = 50; // default
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // Assertions for validation
        assert credits > 0 && credits <= 6 : "Credits must be between 1 and 6";
        assert !courseId.trim().isEmpty() : "Course ID cannot be empty";
        assert !courseCode.trim().isEmpty() : "Course code cannot be empty";
    }

    // Builder pattern implementation (nested class)
    public static class Builder {
        private String courseId;
        private String courseCode;
        private String title;
        private String description;
        private int credits;
        private String department;
        private String semester;
        private String instructorId;
        private int maxEnrollment = 50;

        public Builder courseId(String courseId) {
            this.courseId = courseId;
            return this;
        }

        public Builder courseCode(String courseCode) {
            this.courseCode = courseCode;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder credits(int credits) {
            this.credits = credits;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder semester(String semester) {
            this.semester = semester;
            return this;
        }

        public Builder instructorId(String instructorId) {
            this.instructorId = instructorId;
            return this;
        }

        public Builder maxEnrollment(int maxEnrollment) {
            this.maxEnrollment = maxEnrollment;
            return this;
        }

        public Course build() {
            Course course = new Course(courseId, courseCode, title, description, 
                                     credits, department, semester);
            course.setInstructorId(instructorId);
            course.setMaxEnrollment(maxEnrollment);
            return course;
        }
    }

    // Encapsulated getters and setters
    public String getCourseId() { return courseId; }

    public String getCourseCode() { return courseCode; }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public int getCredits() { return credits; }

    public void setCredits(int credits) {
        assert credits > 0 && credits <= 6 : "Credits must be between 1 and 6";
        this.credits = credits;
        this.updatedAt = LocalDateTime.now();
    }

    public String getDepartment() { return department; }

    public void setDepartment(String department) {
        this.department = Objects.requireNonNull(department, "Department cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public String getSemester() { return semester; }

    public void setSemester(String semester) {
        this.semester = Objects.requireNonNull(semester, "Semester cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public String getInstructorId() { return instructorId; }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
        this.updatedAt = LocalDateTime.now();
    }

    public int getMaxEnrollment() { return maxEnrollment; }

    public void setMaxEnrollment(int maxEnrollment) {
        assert maxEnrollment > 0 : "Max enrollment must be positive";
        this.maxEnrollment = maxEnrollment;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Prerequisites management
    public void addPrerequisite(String courseId) {
        Objects.requireNonNull(courseId, "Prerequisite course ID cannot be null");
        prerequisites.add(courseId);
        this.updatedAt = LocalDateTime.now();
    }

    public void removePrerequisite(String courseId) {
        prerequisites.remove(courseId);
        this.updatedAt = LocalDateTime.now();
    }

    public Set<String> getPrerequisites() {
        return new HashSet<>(prerequisites); // Defensive copy
    }

    public boolean hasPrerequisite(String courseId) {
        return prerequisites.contains(courseId);
    }

    // Enrollment management
    public boolean enrollStudent(String studentId) {
        if (enrolledStudents.size() >= maxEnrollment) {
            return false; // Course is full
        }
        boolean added = enrolledStudents.add(studentId);
        if (added) {
            this.updatedAt = LocalDateTime.now();
        }
        return added;
    }

    public void unenrollStudent(String studentId) {
        if (enrolledStudents.remove(studentId)) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    public Set<String> getEnrolledStudents() {
        return new HashSet<>(enrolledStudents); // Defensive copy
    }

    public boolean isStudentEnrolled(String studentId) {
        return enrolledStudents.contains(studentId);
    }

    public int getCurrentEnrollment() {
        return enrolledStudents.size();
    }

    public int getAvailableSeats() {
        return maxEnrollment - enrolledStudents.size();
    }

    public boolean isFull() {
        return enrolledStudents.size() >= maxEnrollment;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return Objects.equals(courseId, course.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }

    @Override
    public String toString() {
        return String.format("Course{id='%s', code='%s', title='%s', credits=%d, dept='%s', sem='%s', enrollment=%d/%d}", 
                           courseId, courseCode, title, credits, department, semester, 
                           enrolledStudents.size(), maxEnrollment);
    }
}