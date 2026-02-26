package com.epam.springCoreTask.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.epam.springCoreTask.model.Trainee;
import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.model.Training;
import com.epam.springCoreTask.model.TrainingType;

public interface GymFacade {
    Trainee createTraineeProfile(String firstName, String lastName, LocalDate dateOfBirth, String address);

    Trainer createTrainerProfile(String firstName, String lastName, String specialization);

    Training createTrainingSession(UUID traineeId, UUID trainedId, String trainingName, TrainingType trainingType,
            LocalDate tariningDate, int trainingDuration);

    List<Training> getTraineeTrainings(UUID traineeId);

    List<Training> getTrainerTrainings(UUID trainerId);

    Trainee updateTraineeProfile(Trainee trainee);

    Trainer updateTrainerProfile(Trainer trainer);

    void deleteTraineeProfile(UUID traineeId);

    Trainee getTraineeById(UUID id);

    Trainer getTrainerById(UUID id);

    Training getTrainingById(UUID id);

    List<Trainee> getAllTrainees();

    List<Trainer> getAllTrainers();

    List<Training> getAllTrainings();
}
