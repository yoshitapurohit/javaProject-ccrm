package edu.ccrm.domain;

import java.util.*;

/**
 * Instructor class extending Person
 * Demonstrates inheritance and polymorphism
 */
public class Instructor extends Person {
    private String employeeId;
    private String department;
    private String specialization;
    private Set<String> assignedCourses; // Course IDs

    public Instructor(String id, String name, String email, String employeeId,
                     String department, String specialization) {
        super(id, name, email);
        this.employeeId = Objects.requireNonNull(employeeId, "Employee ID cannot be null");
        this.department = Objects.requireNonNull(department, "Department cannot be null");
        this.specialization = specialization;
        this.assignedCourses = new HashSet<>();

        assert !employeeId.trim().isEmpty() : "Employee ID cannot be empty";
    }

    @Override
    public String getRole() {
        return "Instructor";
    }

    @Override
    public String getDisplayInfo() {
        return String.format("Instructor: %s (%s) - %s Department - Specialization: %s - Courses: %d", 
                           name, employeeId, department, 
                           specialization != null ? specialization : "None", 
                           assignedCourses.size());
    }

    // Getters and setters
    public String getEmployeeId() { return employeeId; }

    public String getDepartment() { return department; }

    public void setDepartment(String department) {
        this.department = Objects.requireNonNull(department, "Department cannot be null");
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public String getSpecialization() { return specialization; }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
        this.updatedAt = java.time.LocalDateTime.now();
    }

    // Course assignment methods
    public void assignCourse(String courseId) {
        Objects.requireNonNull(courseId, "Course ID cannot be null");
        assignedCourses.add(courseId);
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public void unassignCourse(String courseId) {
        assignedCourses.remove(courseId);
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public Set<String> getAssignedCourses() {
        return new HashSet<>(assignedCourses); // Defensive copy
    }

    public boolean isAssignedTo(String courseId) {
        return assignedCourses.contains(courseId);
    }

    public int getCourseLoad() {
        return assignedCourses.size();
    }

    @Override
    public String toString() {
        return String.format("Instructor{id='%s', name='%s', empId='%s', dept='%s', courses=%d}", 
                           id, name, employeeId, department, assignedCourses.size());
    }
}