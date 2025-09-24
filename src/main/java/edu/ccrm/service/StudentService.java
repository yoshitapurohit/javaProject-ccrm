package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.config.AppConfig;
import edu.ccrm.util.ValidationUtils;
import java.util.*;
import java.util.stream.Collectors;
import java.util.function.Predicate;

/**
 * Service class for student operations
 * Demonstrates business logic layer and functional programming
 */
public class StudentService {

    private final Map<String, Student> students;
    private final AppConfig config;

    public StudentService() {
        this.students = new HashMap<>();
        this.config = AppConfig.getInstance();
    }

    /**
     * Creates a new student with validation
     */
    public Student createStudent(String id, String name, String email, 
                               String registrationNumber, int year, String department) 
                               throws IllegalArgumentException {

        // Validation using utility class
        if (!ValidationUtils.isValidId(id)) {
            throw new IllegalArgumentException("Invalid student ID");
        }
        if (!ValidationUtils.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!ValidationUtils.isValidRegistrationNumber(registrationNumber)) {
            throw new IllegalArgumentException("Invalid registration number format");
        }
        if (!ValidationUtils.isValidYear(year)) {
            throw new IllegalArgumentException("Invalid year (must be 1-4)");
        }

        // Check for duplicate ID
        if (students.containsKey(id)) {
            throw new IllegalArgumentException("Student with ID " + id + " already exists");
        }

        // Check for duplicate registration number
        boolean regNumberExists = students.values().stream()
                .anyMatch(s -> s.getRegistrationNumber().equals(registrationNumber));

        if (regNumberExists) {
            throw new IllegalArgumentException("Registration number " + registrationNumber + " already exists");
        }

        Student student = new Student(id, name, email, registrationNumber, year, department);
        students.put(id, student);

        return student;
    }

    /**
     * Updates an existing student
     */
    public Student updateStudent(String id, String name, String email, 
                               int year, String department) throws IllegalArgumentException {

        Student student = students.get(id);
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + id);
        }

        if (name != null && !name.trim().isEmpty()) {
            student.setName(name);
        }
        if (email != null && ValidationUtils.isValidEmail(email)) {
            student.setEmail(email);
        }
        if (ValidationUtils.isValidYear(year)) {
            student.setYear(year);
        }
        if (department != null && !department.trim().isEmpty()) {
            student.setDepartment(department);
        }

        return student;
    }

    /**
     * Gets student by ID
     */
    public Optional<Student> getStudent(String id) {
        return Optional.ofNullable(students.get(id));
    }

    /**
     * Gets all students
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    /**
     * Gets active students using functional programming
     */
    public List<Student> getActiveStudents() {
        return students.values().stream()
                .filter(Student::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Searches students by various criteria using predicates
     */
    public List<Student> searchStudents(Predicate<Student> criteria) {
        return students.values().stream()
                .filter(criteria)
                .collect(Collectors.toList());
    }

    /**
     * Gets students by department
     */
    public List<Student> getStudentsByDepartment(String department) {
        return searchStudents(s -> s.getDepartment().equalsIgnoreCase(department));
    }

    /**
     * Gets students by year
     */
    public List<Student> getStudentsByYear(int year) {
        return searchStudents(s -> s.getYear() == year);
    }

    /**
     * Gets students with GPA above threshold
     */
    public List<Student> getStudentsWithGPAAbove(double threshold) {
        return searchStudents(s -> s.calculateGPA() >= threshold);
    }

    /**
     * Enrolls student in course with credit limit validation
     */
    public void enrollStudentInCourse(String studentId, Course course) 
                                    throws DuplicateEnrollmentException, MaxCreditLimitExceededException {

        Student student = students.get(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }

        // Check if already enrolled
        if (student.isEnrolledIn(course.getCourseId())) {
            throw new DuplicateEnrollmentException(
                "Student " + studentId + " is already enrolled in course " + course.getCourseId());
        }

        // Calculate current credits
        int currentCredits = calculateCurrentCredits(student);
        int newTotalCredits = currentCredits + course.getCredits();

        // Check credit limit
        if (newTotalCredits > config.getMaxCreditsPerSemester()) {
            throw new MaxCreditLimitExceededException(
                currentCredits, course.getCredits(), config.getMaxCreditsPerSemester());
        }

        // Enroll student
        student.enrollInCourse(course.getCourseId());
        course.enrollStudent(studentId);
    }

    /**
     * Unenrolls student from course
     */
    public void unenrollStudentFromCourse(String studentId, Course course) {
        Student student = students.get(studentId);
        if (student != null) {
            student.unenrollFromCourse(course.getCourseId());
            course.unenrollStudent(studentId);
        }
    }

    /**
     * Assigns grade to student for a course
     */
    public void assignGrade(String studentId, String courseId, Grade grade) {
        Student student = students.get(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }

        if (!student.isEnrolledIn(courseId)) {
            throw new IllegalArgumentException("Student is not enrolled in course: " + courseId);
        }

        student.setGrade(courseId, grade);
    }

    /**
     * Generates transcript for student
     */
    public String generateTranscript(String studentId) {
        Student student = students.get(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }

        StringBuilder transcript = new StringBuilder();
        transcript.append("TRANSCRIPT\n");
        transcript.append("=========\n");
        transcript.append(String.format("Student: %s (%s)\n", student.getName(), student.getRegistrationNumber()));
        transcript.append(String.format("Department: %s, Year: %d\n", student.getDepartment(), student.getYear()));
        transcript.append(String.format("Email: %s\n", student.getEmail()));
        transcript.append("\nCourses and Grades:\n");
        transcript.append("-----------------\n");

        Map<String, Grade> grades = student.getAllGrades();
        if (grades.isEmpty()) {
            transcript.append("No grades recorded.\n");
        } else {
            grades.forEach((courseId, grade) -> 
                transcript.append(String.format("Course: %s - Grade: %s\n", courseId, grade)));
        }

        transcript.append(String.format("\nOverall GPA: %.2f\n", student.calculateGPA()));
        transcript.append(String.format("Passed Courses: %d\n", student.getPassedCourses().size()));

        return transcript.toString();
    }

    /**
     * Calculates current semester credits for student
     */
    private int calculateCurrentCredits(Student student) {
        // This is a simplified version - in real implementation, 
        // you'd need course information to get credit values
        return student.getEnrolledCourses().size() * 3; // Assuming 3 credits per course
    }

    /**
     * Gets enrollment statistics
     */
    public Map<String, Object> getEnrollmentStatistics() {
        Map<String, Object> stats = new HashMap<>();

        List<Student> activeStudents = getActiveStudents();
        stats.put("totalStudents", students.size());
        stats.put("activeStudents", activeStudents.size());
        stats.put("inactiveStudents", students.size() - activeStudents.size());

        // Department-wise distribution
        Map<String, Long> departmentDistribution = activeStudents.stream()
                .collect(Collectors.groupingBy(Student::getDepartment, Collectors.counting()));
        stats.put("departmentDistribution", departmentDistribution);

        // Year-wise distribution
        Map<Integer, Long> yearDistribution = activeStudents.stream()
                .collect(Collectors.groupingBy(Student::getYear, Collectors.counting()));
        stats.put("yearDistribution", yearDistribution);

        // Average GPA
        double averageGPA = activeStudents.stream()
                .mapToDouble(Student::calculateGPA)
                .average()
                .orElse(0.0);
        stats.put("averageGPA", averageGPA);

        return stats;
    }

    /**
     * Deactivates student account
     */
    public void deactivateStudent(String studentId) {
        Student student = students.get(studentId);
        if (student != null) {
            student.setActive(false);
        }
    }

    /**
     * Activates student account
     */
    public void activateStudent(String studentId) {
        Student student = students.get(studentId);
        if (student != null) {
            student.setActive(true);
        }
    }

    /**
     * Removes student (for testing purposes)
     */
    public void removeStudent(String studentId) {
        students.remove(studentId);
    }

    /**
     * Gets total number of students
     */
    public int getStudentCount() {
        return students.size();
    }

    /**
     * Loads students from list (for import functionality)
     */
    public void loadStudents(List<Student> studentList) {
        students.clear();
        studentList.forEach(student -> students.put(student.getId(), student));
    }
}