package edu.ccrm.util;

import edu.ccrm.domain.*;
import java.util.Comparator;

/**
 * Utility class providing various comparators for domain objects
 * Demonstrates functional programming with lambdas and method references
 */
public final class ComparatorUtils {

    // Private constructor to prevent instantiation
    private ComparatorUtils() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    // Student comparators using lambda expressions
    public static final Comparator<Student> BY_NAME = (s1, s2) -> s1.getName().compareTo(s2.getName());

    public static final Comparator<Student> BY_YEAR = Comparator.comparingInt(Student::getYear);

    // Corrected variable name from BY_DEPARTMENT to BY_STUDENT_DEPARTMENT for clarity
    public static final Comparator<Student> BY_STUDENT_DEPARTMENT = (s1, s2) -> s1.getDepartment().compareTo(s2.getDepartment());

    public static final Comparator<Student> BY_REGISTRATION_NUMBER = 
        Comparator.comparing(Student::getRegistrationNumber);

    public static final Comparator<Student> BY_GPA = 
        Comparator.comparingDouble(Student::calculateGPA).reversed(); // Higher GPA first

    // Course comparators using method references and lambdas
    public static final Comparator<Course> BY_COURSE_CODE = Comparator.comparing(Course::getCourseCode);

    public static final Comparator<Course> BY_TITLE = Comparator.comparing(Course::getTitle);

    public static final Comparator<Course> BY_CREDITS = Comparator.comparingInt(Course::getCredits).reversed();

    // ERROR 1 FIX: Renamed BY_DEPARTMENT to BY_COURSE_DEPARTMENT to avoid duplicate variable name
    public static final Comparator<Course> BY_COURSE_DEPARTMENT = Comparator.comparing(Course::getDepartment);

    public static final Comparator<Course> BY_ENROLLMENT = 
        Comparator.comparingInt(Course::getCurrentEnrollment).reversed();

    // Instructor comparators
    public static final Comparator<Instructor> BY_INSTRUCTOR_NAME = 
        Comparator.comparing(Instructor::getName);

    public static final Comparator<Instructor> BY_INSTRUCTOR_DEPARTMENT = 
        Comparator.comparing(Instructor::getDepartment);

    public static final Comparator<Instructor> BY_COURSE_LOAD = 
        Comparator.comparingInt(Instructor::getCourseLoad).reversed();

    // Enrollment comparators
    public static final Comparator<Enrollment> BY_ENROLLMENT_DATE = 
        Comparator.comparing(Enrollment::getEnrollmentDate);

    public static final Comparator<Enrollment> BY_SEMESTER = 
        Comparator.comparing(Enrollment::getSemester);

    // Composite comparators using thenComparing
    // Updated to use the more specific student department comparator name
    public static final Comparator<Student> BY_DEPARTMENT_THEN_YEAR = 
        BY_STUDENT_DEPARTMENT.thenComparing(BY_YEAR);

    // ERROR 2 FIX: Used the correctly named BY_COURSE_DEPARTMENT to resolve the type mismatch
    public static final Comparator<Course> BY_DEPARTMENT_THEN_CODE = 
        BY_COURSE_DEPARTMENT.thenComparing(BY_COURSE_CODE);

    // Factory methods for custom comparators
    public static Comparator<Student> byYearAndDepartment(boolean yearAscending, boolean deptAscending) {
        // Updated to use the renamed student department comparator
        Comparator<Student> yearComp = yearAscending ? BY_YEAR : BY_YEAR.reversed();
        Comparator<Student> deptComp = deptAscending ? BY_STUDENT_DEPARTMENT : BY_STUDENT_DEPARTMENT.reversed();
        return yearComp.thenComparing(deptComp);
    }

    public static Comparator<Course> byCreditsThenEnrollment() {
        return BY_CREDITS.thenComparing(BY_ENROLLMENT);
    }
}