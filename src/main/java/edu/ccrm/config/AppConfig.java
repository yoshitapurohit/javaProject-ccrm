package edu.ccrm.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Application configuration using Singleton design pattern
 * Demonstrates singleton implementation and configuration management
 */
public class AppConfig {
    private static AppConfig instance;
    private Properties properties;

    // Default configuration values
    private int maxCreditsPerSemester = 24;
    private int maxCourseEnrollment = 50;
    private String dataDirectory = "data";
    private String backupDirectory = "backups";

    // Private constructor for Singleton
    private AppConfig() {
        loadConfiguration();
    }

    // Thread-safe singleton implementation
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    private void loadConfiguration() {
        properties = new Properties();

        // Try to load from properties file
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);

                // Override defaults with properties file values
                maxCreditsPerSemester = Integer.parseInt(
                    properties.getProperty("max.credits.per.semester", String.valueOf(maxCreditsPerSemester)));
                maxCourseEnrollment = Integer.parseInt(
                    properties.getProperty("max.course.enrollment", String.valueOf(maxCourseEnrollment)));
                dataDirectory = properties.getProperty("data.directory", dataDirectory);
                backupDirectory = properties.getProperty("backup.directory", backupDirectory);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Could not load configuration: " + e.getMessage());
            System.err.println("Using default configuration values.");
        }
    }

    // Configuration getters
    public int getMaxCreditsPerSemester() {
        return maxCreditsPerSemester;
    }

    public int getMaxCourseEnrollment() {
        return maxCourseEnrollment;
    }

    public String getDataDirectory() {
        return dataDirectory;
    }

    public String getBackupDirectory() {
        return backupDirectory;
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    // Configuration setters (for runtime modifications)
    public void setMaxCreditsPerSemester(int maxCreditsPerSemester) {
        assert maxCreditsPerSemester > 0 : "Max credits must be positive";
        this.maxCreditsPerSemester = maxCreditsPerSemester;
    }

    public void setMaxCourseEnrollment(int maxCourseEnrollment) {
        assert maxCourseEnrollment > 0 : "Max enrollment must be positive";
        this.maxCourseEnrollment = maxCourseEnrollment;
    }

    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cannot clone Singleton instance");
    }

    @Override
    public String toString() {
        return String.format("AppConfig{maxCredits=%d, maxEnrollment=%d, dataDir='%s', backupDir='%s'}", 
                           maxCreditsPerSemester, maxCourseEnrollment, dataDirectory, backupDirectory);
    }
}