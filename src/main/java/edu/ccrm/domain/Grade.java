package edu.ccrm.domain;

/**
 * Enumeration for grades in the CCRM system
 * Demonstrates enum usage with methods and fields
 */
public enum Grade {
    S(10.0, "Excellent"),
    A(9.0, "Very Good"),
    B(8.0, "Good"),
    C(7.0, "Average"),
    D(6.0, "Below Average"),
    F(0.0, "Fail");

    private final double gradePoints;
    private final String description;

    // Enum constructor
    Grade(double gradePoints, String description) {
        this.gradePoints = gradePoints;
        this.description = description;
    }

    public double getGradePoints() {
        return gradePoints;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPassing() {
        return this != F;
    }

    @Override
    public String toString() {
        return name() + " (" + gradePoints + ")";
    }
}