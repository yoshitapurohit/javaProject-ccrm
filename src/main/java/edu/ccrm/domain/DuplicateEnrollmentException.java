package edu.ccrm.domain;

/**
 * Custom exception for duplicate enrollment attempts
 */
public class DuplicateEnrollmentException extends Exception {
    public DuplicateEnrollmentException(String message) {
        super(message);
    }

    public DuplicateEnrollmentException(String message, Throwable cause) {
        super(message, cause);
    }
}