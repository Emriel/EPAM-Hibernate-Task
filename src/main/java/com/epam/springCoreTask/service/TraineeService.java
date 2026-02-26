package com.epam.springCoreTask.service;

import java.util.List;
import java.util.UUID;

import com.epam.springCoreTask.model.Trainee;

public interface TraineeService {

    Trainee createTrainee(String firstName, String lastName, java.time.LocalDate dateOfBirth, String address);

    Trainee updateTrainee(Trainee trainee);

    void deleteTrainee(Trainee trainee);

    Trainee getTraineeById(UUID id);

    List<Trainee> getAllTrainees();
}
