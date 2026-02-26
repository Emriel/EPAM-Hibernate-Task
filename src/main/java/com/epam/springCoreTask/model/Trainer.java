package com.epam.springCoreTask.model;

import java.util.UUID;

public class Trainer extends User {

    private UUID userId;
    private String specialization;

    public Trainer() {
        super();
        this.userId = UUID.randomUUID();
    }

    public Trainer(String firstName, String lastName, String username, String password, boolean isActive,
            String specialization) {
        super(firstName, lastName, username, password, isActive);
        this.userId = UUID.randomUUID();
        this.specialization = specialization;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "userId=" + userId +
                ", specialization='" + specialization + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", Username='" + getUsername() + '\'' +
                ", isActive=" + isActive() +
                '}';
    }

}
