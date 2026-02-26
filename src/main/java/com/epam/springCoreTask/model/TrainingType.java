package com.epam.springCoreTask.model;

public class TrainingType {

    private String name;

    public TrainingType() {
    }

    public TrainingType(String trainingTypeName) {
        this.name = trainingTypeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String trainingTypeName) {
        this.name = trainingTypeName;
    }

    @Override
    public String toString() {
        return "TrainingType{" +
                "trainingTypeName='" + name + '\'' +
                '}';
    }
}
