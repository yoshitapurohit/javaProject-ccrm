package edu.ccrm.util;

import java.util.regex.Pattern;

/**
 * Utility class for validation operations
 * Demonstrates static utility methods and constants
 */
public final class ValidationUtils {

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\d[A-Za-z]{2,})$");

    // Course code pattern (e.g., CS101, MATH201)
    private static final Pattern COURSE_CODE_PATTERN = 
        Pattern.compile("^[A-Z]{2,5}\\d{3}$");

    // Registration number pattern (e.g., 2023CSE001)
    private static final Pattern REG_NUMBER_PATTERN = 
        Pattern.compile("^\\d{4}[A-Z]{3}\\d{3}$");

    // Private constructor to prevent instantiation
    private ValidationUtils() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    /**
     * Validates email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates course code format
     */
    public static boolean isValidCourseCode(String courseCode) {
        return courseCode != null && COURSE_CODE_PATTERN.matcher(courseCode).matches();
    }

    /**
     * Validates registration number format
     */
    public static boolean isValidRegistrationNumber(String regNumber) {
        return regNumber != null && REG_NUMBER_PATTERN.matcher(regNumber).matches();
    }

    /**
     * Validates credit range
     */
    public static boolean isValidCreditRange(int credits) {
        return credits > 0 && credits <= 6;
    }

    /**
     * Validates year range for students
     */
    public static boolean isValidYear(int year) {
        return year >= 1 && year <= 4;
    }

    /**
     * Checks if string is null or empty
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Validates ID format (non-null and non-empty)
     */
    public static boolean isValidId(String id) {
        return !isNullOrEmpty(id);
    }
}