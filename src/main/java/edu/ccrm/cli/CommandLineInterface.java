package edu.ccrm.cli;

import edu.ccrm.domain.*;
import edu.ccrm.service.StudentService;
import edu.ccrm.io.FileService;
import edu.ccrm.util.ComparatorUtils;
import edu.ccrm.config.AppConfig;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Command Line Interface for CCRM system
 * Demonstrates user interaction and system integration
 */
public class CommandLineInterface {

    private final Scanner scanner;
    private final StudentService studentService;
    private final FileService fileService;
    private final AppConfig config;
    private final Map<String, Course> courses;
    private final Map<String, Instructor> instructors;

    public CommandLineInterface() {
        this.scanner = new Scanner(System.in);
        this.studentService = new StudentService();
        this.fileService = new FileService();
        this.config = AppConfig.getInstance();
        this.courses = new HashMap<>();
        this.instructors = new HashMap<>();

        initializeSampleData();
    }

    /**
     * Main application entry point
     */
    public void start() {
        System.out.println("=================================");
        System.out.println("Campus Course & Records Manager");
        System.out.println("=================================");

        while (true) {
            try {
                showMainMenu();
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1 -> handleStudentManagement();
                    case 2 -> handleCourseManagement();
                    case 3 -> handleEnrollmentManagement();
                    case 4 -> handleGradeManagement();
                    case 5 -> handleFileOperations();
                    case 6 -> handleReports();
                    case 7 -> showSystemInfo();
                    case 0 -> {
                        System.out.println("Thank you for using CCRM!");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment Management");
        System.out.println("4. Grade Management");
        System.out.println("5. File Operations");
        System.out.println("6. Reports");
        System.out.println("7. System Information");
        System.out.println("0. Exit");
    }

    private void handleStudentManagement() {
        while (true) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Add Student");
            System.out.println("2. Update Student");
            System.out.println("3. View Student");
            System.out.println("4. List All Students");
            System.out.println("5. Search Students");
            System.out.println("6. Deactivate Student");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter choice: ");

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> updateStudent();
                case 3 -> viewStudent();
                case 4 -> listAllStudents();
                case 5 -> searchStudents();
                case 6 -> deactivateStudent();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addStudent() {
        try {
            System.out.println("\n--- Add New Student ---");
            String id = getStringInput("Student ID: ");
            String name = getStringInput("Name: ");
            String email = getStringInput("Email: ");
            String regNumber = getStringInput("Registration Number (format: 2023CSE001): ");
            int year = getIntInput("Year (1-4): ");
            String department = getStringInput("Department: ");

            Student student = studentService.createStudent(id, name, email, regNumber, year, department);
            System.out.println("Student created successfully: " + student.getDisplayInfo());

        } catch (Exception e) {
            System.err.println("Error creating student: " + e.getMessage());
        }
    }

    private void updateStudent() {
        try {
            String id = getStringInput("Enter Student ID to update: ");
            Optional<Student> studentOpt = studentService.getStudent(id);

            if (studentOpt.isEmpty()) {
                System.out.println("Student not found.");
                return;
            }

            Student student = studentOpt.get();
            System.out.println("Current info: " + student.getDisplayInfo());
            System.out.println("Leave fields empty to keep current values.");

            String name = getStringInput("New Name: ");
            String email = getStringInput("New Email: ");
            int year = getIntInputWithDefault("New Year (1-4): ", student.getYear());
            String department = getStringInput("New Department: ");

            studentService.updateStudent(id, name.isEmpty() ? null : name, 
                                       email.isEmpty() ? null : email, year, 
                                       department.isEmpty() ? null : department);

            System.out.println("Student updated successfully.");

        } catch (Exception e) {
            System.err.println("Error updating student: " + e.getMessage());
        }
    }

    private void viewStudent() {
        String id = getStringInput("Enter Student ID: ");
        Optional<Student> studentOpt = studentService.getStudent(id);

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            System.out.println("\n" + student.getDisplayInfo());
            System.out.println("Enrolled Courses: " + student.getEnrolledCourses().size());
            System.out.println("Current GPA: " + String.format("%.2f", student.calculateGPA()));
        } else {
            System.out.println("Student not found.");
        }
    }

    private void listAllStudents() {
        System.out.println("\n--- All Students ---");
        System.out.println("1. All Students");
        System.out.println("2. Active Students Only");
        System.out.println("3. Sort by Name");
        System.out.println("4. Sort by GPA");

        int choice = getIntInput("Choose display option: ");
        List<Student> students;

        switch (choice) {
            case 1 -> students = studentService.getAllStudents();
            case 2 -> students = studentService.getActiveStudents();
            case 3 -> {
                students = studentService.getAllStudents();
                students.sort(ComparatorUtils.BY_NAME);
            }
            case 4 -> {
                students = studentService.getAllStudents();
                students.sort(ComparatorUtils.BY_GPA);
            }
            default -> students = studentService.getAllStudents();
        }

        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            students.forEach(student -> System.out.println(student.getDisplayInfo()));
            System.out.println("Total: " + students.size() + " students");
        }
    }

    private void searchStudents() {
        System.out.println("\n--- Search Students ---");
        System.out.println("1. By Department");
        System.out.println("2. By Year");
        System.out.println("3. By GPA Range");

        int choice = getIntInput("Search by: ");
        List<Student> results;

        switch (choice) {
            case 1 -> {
                String department = getStringInput("Department: ");
                results = studentService.getStudentsByDepartment(department);
            }
            case 2 -> {
                int year = getIntInput("Year: ");
                results = studentService.getStudentsByYear(year);
            }
            case 3 -> {
                double minGPA = getDoubleInput("Minimum GPA: ");
                results = studentService.getStudentsWithGPAAbove(minGPA);
            }
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        if (results.isEmpty()) {
            System.out.println("No students found matching criteria.");
        } else {
            results.forEach(student -> System.out.println(student.getDisplayInfo()));
            System.out.println("Found: " + results.size() + " students");
        }
    }

    private void deactivateStudent() {
        String id = getStringInput("Enter Student ID to deactivate: ");
        Optional<Student> studentOpt = studentService.getStudent(id);

        if (studentOpt.isPresent()) {
            studentService.deactivateStudent(id);
            System.out.println("Student deactivated successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }

    private void handleCourseManagement() {
        System.out.println("\n--- Course Management ---");
        System.out.println("Feature implemented with basic course creation.");

        // Create a sample course
        String courseId = getStringInput("Course ID: ");
        String courseCode = getStringInput("Course Code (e.g., CS101): ");
        String title = getStringInput("Course Title: ");
        String description = getStringInput("Description: ");
        int credits = getIntInput("Credits (1-6): ");
        String department = getStringInput("Department: ");
        String semester = getStringInput("Semester: ");

        try {
            Course course = new Course.Builder()
                    .courseId(courseId)
                    .courseCode(courseCode)
                    .title(title)
                    .description(description)
                    .credits(credits)
                    .department(department)
                    .semester(semester)
                    .build();

            courses.put(courseId, course);
            System.out.println("Course created: " + course);
        } catch (Exception e) {
            System.err.println("Error creating course: " + e.getMessage());
        }
    }

    private void handleEnrollmentManagement() {
        System.out.println("\n--- Enrollment Management ---");

        if (courses.isEmpty()) {
            System.out.println("No courses available. Please create courses first.");
            return;
        }

        String studentId = getStringInput("Student ID: ");

        System.out.println("Available courses:");
        courses.values().forEach(course -> System.out.println(course.getCourseId() + " - " + course.getTitle()));

        String courseId = getStringInput("Course ID to enroll in: ");
        Course course = courses.get(courseId);

        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        try {
            studentService.enrollStudentInCourse(studentId, course);
            System.out.println("Student enrolled successfully.");
        } catch (Exception e) {
            System.err.println("Enrollment error: " + e.getMessage());
        }
    }

    private void handleGradeManagement() {
        System.out.println("\n--- Grade Management ---");

        String studentId = getStringInput("Student ID: ");
        Optional<Student> studentOpt = studentService.getStudent(studentId);

        if (studentOpt.isEmpty()) {
            System.out.println("Student not found.");
            return;
        }

        Student student = studentOpt.get();
        Set<String> enrolledCourses = student.getEnrolledCourses();

        if (enrolledCourses.isEmpty()) {
            System.out.println("Student is not enrolled in any courses.");
            return;
        }

        System.out.println("Enrolled courses: " + enrolledCourses);
        String courseId = getStringInput("Course ID: ");

        if (!enrolledCourses.contains(courseId)) {
            System.out.println("Student is not enrolled in this course.");
            return;
        }

        System.out.println("Available grades: S, A, B, C, D, F");
        String gradeStr = getStringInput("Grade: ").toUpperCase();

        try {
            Grade grade = Grade.valueOf(gradeStr);
            studentService.assignGrade(studentId, courseId, grade);
            System.out.println("Grade assigned successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid grade. Please use S, A, B, C, D, or F.");
        }
    }

    private void handleFileOperations() {
        System.out.println("\n--- File Operations ---");
        System.out.println("1. Export Students to CSV");
        System.out.println("2. Import Students from CSV");
        System.out.println("3. Create Backup");
        System.out.println("4. List Backups");
        System.out.println("5. Restore from Backup");

        int choice = getIntInput("Choose operation: ");

        try {
            switch (choice) {
                case 1 -> {
                    String filename = getStringInput("Export filename (e.g., students.csv): ");
                    fileService.exportStudentsToCSV(studentService.getAllStudents(), filename);
                    System.out.println("Students exported successfully.");
                }
                case 2 -> {
                    String filename = getStringInput("Import filename: ");
                    List<Student> importedStudents = fileService.importStudentsFromCSV(filename);
                    studentService.loadStudents(importedStudents);
                    System.out.println("Students imported: " + importedStudents.size());
                }
                case 3 -> {
                    fileService.createBackup();
                }
                case 4 -> {
                    var backups = fileService.listBackups();
                    if (backups.isEmpty()) {
                        System.out.println("No backups found.");
                    } else {
                        System.out.println("Available backups:");
                        backups.forEach(backup -> System.out.println("- " + backup.getFileName()));
                    }
                }
                case 5 -> {
                    var backups = fileService.listBackups();
                    if (backups.isEmpty()) {
                        System.out.println("No backups available.");
                        return;
                    }
                    backups.forEach(backup -> System.out.println("- " + backup.getFileName()));
                    String backupName = getStringInput("Backup name to restore: ");
                    fileService.restoreFromBackup(backupName);
                }
                default -> System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.err.println("File operation error: " + e.getMessage());
        }
    }

    private void handleReports() {
        System.out.println("\n--- Reports ---");
        System.out.println("1. Student Statistics");
        System.out.println("2. Generate Transcript");
        System.out.println("3. Department Summary");

        int choice = getIntInput("Choose report: ");

        switch (choice) {
            case 1 -> showStudentStatistics();
            case 2 -> generateTranscript();
            case 3 -> showDepartmentSummary();
            default -> System.out.println("Invalid choice.");
        }
    }

    private void showStudentStatistics() {
        Map<String, Object> stats = studentService.getEnrollmentStatistics();

        System.out.println("\n=== Student Statistics ===");
        System.out.println("Total Students: " + stats.get("totalStudents"));
        System.out.println("Active Students: " + stats.get("activeStudents"));
        System.out.println("Inactive Students: " + stats.get("inactiveStudents"));
        System.out.println("Average GPA: " + String.format("%.2f", (Double) stats.get("averageGPA")));

        @SuppressWarnings("unchecked")
        Map<String, Long> deptDist = (Map<String, Long>) stats.get("departmentDistribution");
        System.out.println("\nDepartment Distribution:");
        deptDist.forEach((dept, count) -> System.out.println("  " + dept + ": " + count));

        @SuppressWarnings("unchecked")
        Map<Integer, Long> yearDist = (Map<Integer, Long>) stats.get("yearDistribution");
        System.out.println("\nYear Distribution:");
        yearDist.forEach((year, count) -> System.out.println("  Year " + year + ": " + count));
    }

    private void generateTranscript() {
        String studentId = getStringInput("Student ID for transcript: ");
        try {
            String transcript = studentService.generateTranscript(studentId);
            System.out.println("\n" + transcript);
        } catch (Exception e) {
            System.err.println("Error generating transcript: " + e.getMessage());
        }
    }

    private void showDepartmentSummary() {
        Map<String, List<Student>> deptGroups = studentService.getActiveStudents()
                .stream()
                .collect(Collectors.groupingBy(Student::getDepartment));

        System.out.println("\n=== Department Summary ===");
        deptGroups.forEach((dept, students) -> {
            double avgGPA = students.stream()
                    .mapToDouble(Student::calculateGPA)
                    .average()
                    .orElse(0.0);

            System.out.printf("%s: %d students, Avg GPA: %.2f%n", 
                            dept, students.size(), avgGPA);
        });
    }

    private void showSystemInfo() {
        System.out.println("\n=== System Information ===");
        System.out.println("CCRM Version: 1.0");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Configuration: " + config);
        System.out.println("Total Students: " + studentService.getStudentCount());
        System.out.println("Total Courses: " + courses.size());
        System.out.println("Total Instructors: " + instructors.size());
    }

    private void initializeSampleData() {
        // Create sample students
        try {
            studentService.createStudent("S001", "John Doe", "john@example.com", "2023CSE001", 2, "Computer Science");
            studentService.createStudent("S002", "Jane Smith", "jane@example.com", "2023EEE001", 1, "Electronics");
            studentService.createStudent("S003", "Bob Johnson", "bob@example.com", "2023MEC001", 3, "Mechanical");
        } catch (Exception e) {
            System.err.println("Error initializing sample data: " + e.getMessage());
        }

        // Create sample courses
        Course cs101 = new Course.Builder()
                .courseId("CS101")
                .courseCode("CS101")
                .title("Introduction to Programming")
                .description("Basic programming concepts")
                .credits(3)
                .department("Computer Science")
                .semester("Fall 2023")
                .build();

        courses.put("CS101", cs101);

        System.out.println("Sample data initialized.");
    }

    // Helper methods for input handling
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private int getIntInputWithDefault(String prompt, int defaultValue) {
        System.out.print(prompt + " (current: " + defaultValue + "): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Main method to run the application
     */
    public static void main(String[] args) {
        // Enable assertions
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);

        try {
            CommandLineInterface cli = new CommandLineInterface();
            cli.start();
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}