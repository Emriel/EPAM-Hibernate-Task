package com.epam.springCoreTask.service;

import java.time.LocalDate;
import java.util.List;

import com.epam.springCoreTask.model.Trainee;
import com.epam.springCoreTask.model.Trainer;

public interface TraineeService {
    Trainee createTrainee(String firstName, String lastName, LocalDate dateOfBirth, String address);

    Trainee getTraineeByUsername(String username);

    Trainee updateTrainee(Trainee trainee);

    boolean deleteByUsername(String username);

    List<Trainee> getAllTrainees();

    List<Trainer> getUnassignedTrainers(String traineeUsername);

    boolean updateTrainersList(String traineeUsername, List<String> trainerUsernames);

    
}
