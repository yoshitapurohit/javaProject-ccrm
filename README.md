# Campus Course & Records Manager (CCRM)

## Project Overview

The Campus Course & Records Manager (CCRM) is a comprehensive Java SE console-based application designed to manage academic operations for educational institutions. The system handles student management, course administration, enrollment processing, grade management, and file operations with modern Java features and design patterns.

## Features

### Core Functionality

- **Student Management**: Create, update, view, search, and manage student records.
- **Course Management**: Handle course creation, updates, and administration
- **Enrollment System**: Manage student-course enrollments with credit limits and prerequisites
- **Grade Management**: Record grades, calculate GPAs, and generate transcripts
- **File Operations**: Import/export data in CSV format, backup and restore operations
- **Reporting**: Generate comprehensive reports and statistics

### Technical Features

- **Object-Oriented Design**: Comprehensive implementation of OOP principles
- **Modern Java Features**: Streams API, Lambda expressions, Date/Time API, NIO.2
- **Design Patterns**: Singleton, Builder, and other enterprise patterns
- **Exception Handling**: Custom exceptions with robust error management
- **Functional Programming**: Extensive use of predicates, comparators, and streams
- **File I/O**: Modern NIO.2 APIs for efficient file operations

## Java Concepts Demonstrated

### OOP Principles

- **Encapsulation**: Private fields with controlled access via getters/setters
- **Inheritance**: Abstract Person class extended by Student and Instructor
- **Abstraction**: Abstract classes and interfaces for common behaviors
- **Polymorphism**: Method overriding and runtime type resolution

### Advanced Java Features

- **Enums**: Grade enumeration with methods and fields
- **Collections**: HashMap, HashSet, ArrayList with generic types
- **Streams API**: Filtering, mapping, collecting, and reduction operations
- **Lambda Expressions**: Functional programming for cleaner code
- **Date/Time API**: Modern temporal handling with LocalDateTime
- **NIO.2**: Path-based file operations and directory traversal
- **Exception Handling**: Custom exceptions and try-with-resources
- **Nested Classes**: Builder pattern implementation
- **Assertions**: Runtime validation and debugging support

### Design Patterns

- **Singleton Pattern**: AppConfig for global configuration management
- **Builder Pattern**: Course object construction with optional parameters
- **Factory Methods**: Utility classes for object creation and comparison

## Project Structure

```
CCRM_Project/
├── src/main/java/edu/ccrm/
│   ├── cli/                    # Command Line Interface
│   │   └── CommandLineInterface.java
│   ├── config/                 # Configuration Management
│   │   └── AppConfig.java
│   ├── domain/                 # Core Domain Objects
│   │   ├── Person.java         # Abstract base class
│   │   ├── Student.java        # Student entity
│   │   ├── Instructor.java     # Instructor entity
│   │   ├── Course.java         # Course entity
│   │   ├── Enrollment.java     # Enrollment relationship
│   │   ├── Grade.java          # Grade enumeration
│   │   └── *Exception.java     # Custom exceptions
│   ├── io/                     # File I/O Operations
│   │   └── FileService.java
│   ├── service/                # Business Logic Layer
│   │   └── StudentService.java
│   └── util/                   # Utility Classes
│       ├── ValidationUtils.java
│       ├── ComparatorUtils.java
│       └── RecursionUtils.java
├── data/                       # Data files directory
├── backups/                    # Backup files directory
└── README.md
```

## Installation and Setup

### Prerequisites

- Java Development Kit (JDK) 17 or later
- IDE (Eclipse, IntelliJ IDEA, or VS Code)
- Command line terminal

### Setup Instructions

0.  **Java Installation**
    Download the Java JDK Installer

    Visit the official Oracle website and download the latest Java JDK for Windows as a .exe installer file (commonly JDK 21, 23, or 25 in 2025).

Run the Installer

    Locate the downloaded .exe file (e.g., jdk-23_windows-x64_bin.exe) in your Downloads folder and double-click it to launch the setup wizard.

    Follow the on-screen prompts, clicking "Next" and then "Install" to complete the installation. Accept the default installation path or specify a different one if required.

Set JAVA_HOME Environment Variable

    After installation, right-click the Start menu, choose "System," then go to "Advanced system settings" and click "Environment Variables."

    Under "System variables," click "New," enter JAVA_HOME as the variable name, and paste the JDK path (e.g., C:\Program Files\Java\jdk-23). Do not include the bin folder here.

Update the PATH Variable

    In the same "Environment Variables" window, select the "Path" variable under "System variables" and click "Edit."

    Add a new entry for your JDK's bin directory (e.g., C:\Program Files\Java\jdk-23\bin)

![WhatsApp Image 2025-09-24 at 10 23 47_4ab5896a](https://github.com/user-attachments/assets/394b52e4-df3d-4d6d-b2d0-ddffefa9f4f0)

![WhatsApp Image 2025-09-24 at 10 23 47_b58fe291](https://github.com/user-attachments/assets/eb89b4da-e52d-4785-a0bd-ebed726cdbea)
![WhatsApp Image 2025-09-24 at 10 24 04_7d010c18](https://github.com/user-attachments/assets/106ed56b-d06b-4c93-ac55-09c7d157cdfc)
![WhatsApp Image 2025-09-24 at 10 24 18_8f9b2d41](https://github.com/user-attachments/assets/95c53dc5-bd31-4530-9e1c-8b6bd6e4d4d9)

![WhatsApp Image 2025-09-24 at 10 24 54_04e0dff9](https://github.com/user-attachments/assets/dc761127-833c-4530-b533-295bc01ae62e)
![WhatsApp Image 2025-09-24 at 10 26 35_4df1b35a](https://github.com/user-attachments/assets/da0f3bea-bf03-4a9d-a58e-7beafd023ed1)

1. **Extract the Project**

   ```bash
   unzip CCRM_Project.zip
   cd CCRM_Project
   ```

2. **Compile the Project**

   ```bash
   javac -d build -sourcepath src/main/java src/main/java/edu/ccrm/cli/CommandLineInterface.java
   ```

3. **Run the Application**

   ```bash
   java -ea -cp build edu.ccrm.cli.CommandLineInterface
   ```

   Note: The `-ea` flag enables assertions for validation checking.

### Eclipse Setup

1. **Import Project**

   - File → Import → Existing Projects into Workspace
   - Select the CCRM_Project directory
   - Click Finish

2. **Enable Assertions**

   - Right-click project → Run As → Run Configurations
   - Go to Arguments tab
   - Add `-ea` to VM arguments

3. **Run Application**
   - Right-click CommandLineInterface.java
   - Run As → Java Application

## Usage Guide

### Main Menu Options

1. **Student Management**

   - Add new students with validation
   - Update existing student information
   - View student details and transcripts
   - Search students by various criteria
   - Manage student activation status

2. **Course Management**

   - Create new courses with builder pattern
   - Update course information
   - Assign instructors to courses
   - Manage course prerequisites

3. **Enrollment Management**

   - Enroll students in courses
   - Validate credit limits and prerequisites
   - Handle enrollment conflicts and exceptions
   - Unenroll students from courses

4. **Grade Management**

   - Assign grades to enrolled students
   - Calculate GPAs automatically
   - Generate comprehensive transcripts
   - Track academic progress

5. **File Operations**

   - Export data to CSV format
   - Import data from CSV files
   - Create timestamped backups
   - Restore from previous backups

6. **Reports and Statistics**
   - Student enrollment statistics
   - Department-wise summaries
   - GPA distributions and analysis
   - System information and metrics

## Sample Data

The application initializes with sample data including:

- 3 sample students from different departments
- 1 sample course (CS101 - Introduction to Programming)
- Demonstration of enrollment and grading features

## Key Implementation Highlights

### Functional Programming Examples

```java
// Lambda expressions for sorting
students.sort((s1, s2) -> s1.getName().compareTo(s2.getName()));

// Stream operations for filtering
List<Student> topStudents = students.stream()
    .filter(s -> s.calculateGPA() > 8.0)
    .collect(Collectors.toList());

// Method references
students.sort(Comparator.comparing(Student::getName));
```

### Design Pattern Usage

```java
// Singleton pattern
AppConfig config = AppConfig.getInstance();

// Builder pattern
Course course = new Course.Builder()
    .courseId("CS101")
    .courseCode("CS101")
    .title("Programming")
    .credits(3)
    .build();
```

### Exception Handling

```java
try {
    studentService.enrollStudentInCourse(studentId, course);
} catch (DuplicateEnrollmentException e) {
    System.err.println("Already enrolled: " + e.getMessage());
} catch (MaxCreditLimitExceededException e) {
    System.err.println("Credit limit exceeded: " + e.getMessage());
}
```

## Technical Specifications

- **Java Version**: Compatible with Java 17+
- **Architecture**: Layered architecture with clear separation of concerns
- **Persistence**: File-based storage using CSV format
- **Concurrency**: Thread-safe singleton implementation
- **Memory Management**: Defensive copying and proper resource handling
- **Error Handling**: Comprehensive exception hierarchy with meaningful messages

## System Requirements

### Java Platform Evolution

- **Java ME**: Mobile/embedded devices (not applicable to this project)
- **Java SE**: Standard Edition - used for this desktop application
- **Java EE**: Enterprise Edition - for web/enterprise applications

### JDK vs JRE vs JVM

- **JDK**: Development kit including compiler, debugger, and tools
- **JRE**: Runtime environment for executing Java applications
- **JVM**: Virtual machine that executes Java bytecode

## Future Enhancements

- Database integration using JDBC
- Web interface using Spring Boot
- REST API for external integrations
- Advanced reporting with charts and graphs
- Multi-user support with authentication
- Real-time notifications and alerts

## Contributing

This project demonstrates comprehensive Java programming concepts and serves as an educational reference for object-oriented design, modern Java features, and software engineering best practices.

## License

This project is developed for educational purposes and demonstrates Java programming proficiency across all major language features and design patterns.

---

**Author**: Yoshita Purohit
**Course**: Programming in Java
**Date**: September 2025  
**Version**: 1.0
