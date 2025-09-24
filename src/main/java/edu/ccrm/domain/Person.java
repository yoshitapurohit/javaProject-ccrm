package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Abstract base class for all persons in the CCRM system
 * Demonstrates inheritance hierarchy and abstraction
 */
public abstract class Person {
    protected String id;
    protected String name;
    protected String email;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public Person(String id, String name, String email) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // Assert ID is not empty
        assert !id.trim().isEmpty() : "ID cannot be empty";
        assert validateEmail(email) : "Invalid email format";
    }

    // Abstract method to be implemented by subclasses
    public abstract String getRole();

    public abstract String getDisplayInfo();

    // Encapsulation with proper getters and setters
    public String getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        Objects.requireNonNull(email, "Email cannot be null");
        assert validateEmail(email) : "Invalid email format";
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Validation helper method
    private boolean validateEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s{id='%s', name='%s', email='%s'}", 
                           getClass().getSimpleName(), id, name, email);
    }
}