package edu.ccrm.util;

import java.nio.file.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Utility class demonstrating recursion and file operations
 * Uses NIO.2 APIs for file system operations
 */
public final class RecursionUtils {

    private RecursionUtils() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    /**
     * Recursively calculates factorial
     * Demonstrates basic recursion
     */
    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        }
        if (n <= 1) {
            return 1; // Base case
        }
        return n * factorial(n - 1); // Recursive case
    }

    /**
     * Recursively generates directory tree structure
     * Demonstrates file system recursion with NIO.2
     */
    public static void printDirectoryTree(Path rootPath, String indent) throws IOException {
        if (!Files.exists(rootPath)) {
            throw new IllegalArgumentException("Path does not exist: " + rootPath);
        }

        System.out.println(indent + rootPath.getFileName());

        if (Files.isDirectory(rootPath)) {
            try (Stream<Path> paths = Files.list(rootPath)) {
                paths.sorted()
                     .forEach(path -> {
                         try {
                             printDirectoryTree(path, indent + "  ");
                         } catch (IOException e) {
                             System.err.println("Error accessing: " + path + " - " + e.getMessage());
                         }
                     });
            }
        }
    }

    /**
     * Recursively counts files in directory
     * Uses Files.walk() for deep traversal
     */
    public static long countFilesRecursively(Path directory) throws IOException {
        if (!Files.isDirectory(directory)) {
            return Files.exists(directory) ? 1 : 0;
        }

        try (Stream<Path> paths = Files.walk(directory)) {
            return paths.filter(Files::isRegularFile).count();
        }
    }

    /**
     * Recursively finds files by extension
     */
    public static List<Path> findFilesByExtension(Path directory, String extension) throws IOException {
        if (!Files.isDirectory(directory)) {
            return Collections.emptyList();
        }

        List<Path> result = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(directory)) {
            paths.filter(Files::isRegularFile)
                 .filter(path -> path.toString().toLowerCase().endsWith(extension.toLowerCase()))
                 .forEach(result::add);
        }
        return result;
    }

    /**
     * Recursively calculates total directory size
     */
    public static long calculateDirectorySize(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            return 0;
        }

        if (!Files.isDirectory(directory)) {
            return Files.size(directory);
        }

        try (Stream<Path> paths = Files.walk(directory)) {
            return paths.filter(Files::isRegularFile)
                       .mapToLong(path -> {
                           try {
                               return Files.size(path);
                           } catch (IOException e) {
                               System.err.println("Error getting size for: " + path);
                               return 0;
                           }
                       })
                       .sum();
        }
    }

    /**
     * Recursively creates directory structure
     * Demonstrates recursive directory creation
     */
    public static void createDirectoryStructure(Path basePath, List<String> subdirectories) throws IOException {
        Files.createDirectories(basePath); // Create base directory if it doesn't exist

        for (String subdir : subdirectories) {
            Path subdirPath = basePath.resolve(subdir);
            Files.createDirectories(subdirPath);

            // Recursively create nested structure if subdir contains path separators
            if (subdir.contains("/") || subdir.contains("\\")) {
                String[] parts = subdir.split("[/\\\\]");
                Path currentPath = basePath;
                for (String part : parts) {
                    currentPath = currentPath.resolve(part);
                    Files.createDirectories(currentPath);
                }
            }
        }
    }

    /**
     * Recursively deletes directory and all contents
     * Uses post-order traversal to delete files before directories
     */
    public static void deleteDirectoryRecursively(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            return;
        }

        if (Files.isRegularFile(directory)) {
            Files.delete(directory);
            return;
        }

        try (Stream<Path> paths = Files.walk(directory)) {
            paths.sorted(Comparator.reverseOrder()) // Delete files before directories
                 .forEach(path -> {
                     try {
                         Files.delete(path);
                     } catch (IOException e) {
                         System.err.println("Failed to delete: " + path + " - " + e.getMessage());
                     }
                 });
        }
    }
}