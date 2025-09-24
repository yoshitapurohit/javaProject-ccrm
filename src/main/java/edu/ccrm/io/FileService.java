package edu.ccrm.io;

import edu.ccrm.domain.*;
import edu.ccrm.config.AppConfig;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * File operations service using NIO.2 APIs
 * Demonstrates modern I/O operations and exception handling
 */
public class FileService {

    private final AppConfig config;
    private final Path dataDirectory;
    private final Path backupDirectory;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public FileService() {
        this.config = AppConfig.getInstance();
        this.dataDirectory = Paths.get(config.getDataDirectory());
        this.backupDirectory = Paths.get(config.getBackupDirectory());

        try {
            Files.createDirectories(dataDirectory);
            Files.createDirectories(backupDirectory);
        } catch (IOException e) {
            System.err.println("Failed to create directories: " + e.getMessage());
        }
    }

    /**
     * Exports students to CSV format
     */
    public void exportStudentsToCSV(List<Student> students, String filename) throws IOException {
        Path filePath = dataDirectory.resolve(filename);

        List<String> lines = new ArrayList<>();
        lines.add("ID,Name,Email,RegistrationNumber,Year,Department,Active,CreatedAt");

        lines.addAll(students.stream()
                .map(this::studentToCsvLine)
                .collect(Collectors.toList()));

        Files.write(filePath, lines, StandardCharsets.UTF_8, 
                   StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Students exported to: " + filePath);
    }

    /**
     * Imports students from CSV format
     */
    public List<Student> importStudentsFromCSV(String filename) throws IOException {
        Path filePath = dataDirectory.resolve(filename);

        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filePath);
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        if (lines.isEmpty()) {
            return Collections.emptyList();
        }

        // Skip header line
        return lines.stream()
                .skip(1)
                .map(this::csvLineToStudent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Exports courses to CSV format
     */
    public void exportCoursesToCSV(List<Course> courses, String filename) throws IOException {
        Path filePath = dataDirectory.resolve(filename);

        List<String> lines = new ArrayList<>();
        lines.add("CourseID,CourseCode,Title,Description,Credits,Department,Semester,InstructorID,MaxEnrollment,CurrentEnrollment");

        lines.addAll(courses.stream()
                .map(this::courseToCsvLine)
                .collect(Collectors.toList()));

        Files.write(filePath, lines, StandardCharsets.UTF_8,
                   StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Courses exported to: " + filePath);
    }

    /**
     * Imports courses from CSV format
     */
    public List<Course> importCoursesFromCSV(String filename) throws IOException {
        Path filePath = dataDirectory.resolve(filename);

        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filePath);
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        if (lines.isEmpty()) {
            return Collections.emptyList();
        }

        return lines.stream()
                .skip(1) // Skip header
                .map(this::csvLineToCourse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Creates backup of data directory with timestamp
     */
    public void createBackup() throws IOException {
        String timestamp = LocalDateTime.now().format(dateFormatter);
        Path backupPath = backupDirectory.resolve("backup_" + timestamp);

        Files.createDirectories(backupPath);

        // Copy all files from data directory to backup
        try (var paths = Files.walk(dataDirectory)) {
            paths.filter(Files::isRegularFile)
                 .forEach(source -> {
                     try {
                         Path destination = backupPath.resolve(dataDirectory.relativize(source));
                         Files.createDirectories(destination.getParent());
                         Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                     } catch (IOException e) {
                         System.err.println("Failed to backup file: " + source + " - " + e.getMessage());
                     }
                 });
        }

        System.out.println("Backup created at: " + backupPath);
    }

    /**
     * Lists all available backup directories
     */
    public List<Path> listBackups() throws IOException {
        if (!Files.exists(backupDirectory)) {
            return Collections.emptyList();
        }

        try (var paths = Files.list(backupDirectory)) {
            return paths.filter(Files::isDirectory)
                       .filter(path -> path.getFileName().toString().startsWith("backup_"))
                       .sorted(Comparator.reverseOrder()) // Newest first
                       .collect(Collectors.toList());
        }
    }

    /**
     * Restores data from a specific backup
     */
    public void restoreFromBackup(String backupName) throws IOException {
        Path backupPath = backupDirectory.resolve(backupName);

        if (!Files.exists(backupPath) || !Files.isDirectory(backupPath)) {
            throw new IOException("Backup not found: " + backupName);
        }

        // Clear current data directory
        if (Files.exists(dataDirectory)) {
            try (var paths = Files.walk(dataDirectory)) {
                paths.sorted(Comparator.reverseOrder())
                     .forEach(path -> {
                         try {
                             Files.delete(path);
                         } catch (IOException e) {
                             System.err.println("Failed to delete: " + path);
                         }
                     });
            }
        }

        Files.createDirectories(dataDirectory);

        // Copy files from backup to data directory
        try (var paths = Files.walk(backupPath)) {
            paths.filter(Files::isRegularFile)
                 .forEach(source -> {
                     try {
                         Path destination = dataDirectory.resolve(backupPath.relativize(source));
                         Files.createDirectories(destination.getParent());
                         Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                     } catch (IOException e) {
                         System.err.println("Failed to restore file: " + source);
                     }
                 });
        }

        System.out.println("Data restored from backup: " + backupName);
    }

    // Helper methods for CSV conversion
    private String studentToCsvLine(Student student) {
        return String.format("%s,%s,%s,%s,%d,%s,%s,%s",
                           escapeCSV(student.getId()),
                           escapeCSV(student.getName()),
                           escapeCSV(student.getEmail()),
                           escapeCSV(student.getRegistrationNumber()),
                           student.getYear(),
                           escapeCSV(student.getDepartment()),
                           student.isActive(),
                           student.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    private Student csvLineToStudent(String line) {
        try {
            String[] parts = parseCSVLine(line);
            if (parts.length >= 7) {
                Student student = new Student(
                    parts[0], // id
                    parts[1], // name
                    parts[2], // email
                    parts[3], // registration number
                    Integer.parseInt(parts[4]), // year
                    parts[5]  // department
                );
                student.setActive(Boolean.parseBoolean(parts[6]));
                return student;
            }
        } catch (Exception e) {
            System.err.println("Error parsing student line: " + line + " - " + e.getMessage());
        }
        return null;
    }

    private String courseToCsvLine(Course course) {
        return String.format("%s,%s,%s,%s,%d,%s,%s,%s,%d,%d",
                           escapeCSV(course.getCourseId()),
                           escapeCSV(course.getCourseCode()),
                           escapeCSV(course.getTitle()),
                           escapeCSV(course.getDescription()),
                           course.getCredits(),
                           escapeCSV(course.getDepartment()),
                           escapeCSV(course.getSemester()),
                           course.getInstructorId() != null ? escapeCSV(course.getInstructorId()) : "",
                           course.getMaxEnrollment(),
                           course.getCurrentEnrollment());
    }

    private Course csvLineToCourse(String line) {
        try {
            String[] parts = parseCSVLine(line);
            if (parts.length >= 9) {
                Course.Builder builder = new Course.Builder()
                    .courseId(parts[0])
                    .courseCode(parts[1])
                    .title(parts[2])
                    .description(parts[3])
                    .credits(Integer.parseInt(parts[4]))
                    .department(parts[5])
                    .semester(parts[6])
                    .maxEnrollment(Integer.parseInt(parts[8]));

                if (!parts[7].trim().isEmpty()) {
                    builder.instructorId(parts[7]);
                }

                return builder.build();
            }
        } catch (Exception e) {
            System.err.println("Error parsing course line: " + line + " - " + e.getMessage());
        }
        return null;
    }

    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());

        return result.toArray(new String[0]);
    }
}