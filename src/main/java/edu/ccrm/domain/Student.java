package edu.ccrm.domain;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Student class extending Person
 * Demonstrates inheritance, encapsulation, and polymorphism
 */
public class Student extends Person {
    private String registrationNumber;
    private int year;
    private String department;
    private boolean isActive;
    private Set<String> enrolledCourses; // Course IDs
    private Map<String, Grade> courseGrades; // Course ID -> Grade mapping

    public Student(String id, String name, String email, String registrationNumber, 
                  int year, String department) {
        super(id, name, email);
        this.registrationNumber = Objects.requireNonNull(registrationNumber, "Registration number cannot be null");
        this.year = year;
        this.department = Objects.requireNonNull(department, "Department cannot be null");
        this.isActive = true;
        this.enrolledCourses = new HashSet<>();
        this.courseGrades = new HashMap<>();

        // Assertions for validation
        assert year > 0 && year <= 4 : "Year must be between 1 and 4";
        assert !registrationNumber.trim().isEmpty() : "Registration number cannot be empty";
    }

    // Polymorphic implementation of abstract methods
    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public String getDisplayInfo() {
        return String.format("Student: %s (%s) - Year %d, %s - Status: %s", 
                           name, registrationNumber, year, department, 
                           isActive ? "Active" : "Inactive");
    }

    // Encapsulated getters and setters
    public String getRegistrationNumber() { return registrationNumber; }

    public int getYear() { return year; }

    public void setYear(int year) {
        assert year > 0 && year <= 4 : "Year must be between 1 and 4";
        this.year = year;
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public String getDepartment() { return department; }

    public void setDepartment(String department) {
        this.department = Objects.requireNonNull(department, "Department cannot be null");
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public boolean isActive() { return isActive; }

    public void setActive(boolean active) {
        this.isActive = active;
        this.updatedAt = java.time.LocalDateTime.now();
    }

    // Course enrollment methods
    public void enrollInCourse(String courseId) {
        Objects.requireNonNull(courseId, "Course ID cannot be null");
        enrolledCourses.add(courseId);
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public void unenrollFromCourse(String courseId) {
        enrolledCourses.remove(courseId);
        courseGrades.remove(courseId);
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public Set<String> getEnrolledCourses() {
        return new HashSet<>(enrolledCourses); // Defensive copy
    }

    public boolean isEnrolledIn(String courseId) {
        return enrolledCourses.contains(courseId);
    }

    // Grade management methods
    public void setGrade(String courseId, Grade grade) {
        if (enrolledCourses.contains(courseId)) {
            courseGrades.put(courseId, grade);
            this.updatedAt = java.time.LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("Student is not enrolled in course: " + courseId);
        }
    }

    public Grade getGrade(String courseId) {
        return courseGrades.get(courseId);
    }

    public Map<String, Grade> getAllGrades() {
        return new HashMap<>(courseGrades); // Defensive copy
    }

    // GPA calculation using streams and lambdas
    public double calculateGPA() {
        if (courseGrades.isEmpty()) return 0.0;

        return courseGrades.values()
                .stream()
                .mapToDouble(Grade::getGradePoints)
                .average()
                .orElse(0.0);
    }

    // Get passed courses using functional programming
    public Set<String> getPassedCourses() {
        return courseGrades.entrySet()
                .stream()
                .filter(entry -> entry.getValue().isPassing())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return String.format("Student{id='%s', name='%s', regNum='%s', year=%d, dept='%s', active=%s, courses=%d}", 
                           id, name, registrationNumber, year, department, isActive, enrolledCourses.size());
    }
}