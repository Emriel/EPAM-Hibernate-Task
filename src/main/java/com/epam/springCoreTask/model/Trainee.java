package com.epam.springCoreTask.model;

import java.time.LocalDate;
import java.util.UUID;

public class Trainee extends User {

    private LocalDate dateOfBirth;
    private String address;
    private UUID userId;

    public Trainee() {
        super();
        this.userId = UUID.randomUUID();
    }

    public Trainee(String firstName, String lastName, String username, String password, boolean isActive,
            LocalDate dateOfBirth, String address) {
        super(firstName, lastName, username, password, isActive);
        this.userId = UUID.randomUUID();
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "userId=" + userId +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", Username='" + getUsername() + '\'' +
                ", isActive=" + isActive() +
                '}';
    }

}
