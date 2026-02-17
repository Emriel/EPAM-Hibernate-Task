package com.epam.springCoreTask.service;

import java.time.LocalDate;
import java.util.List;

import com.epam.springCoreTask.model.Trainee;
import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.model.Training;
import com.epam.springCoreTask.model.TrainingType;

public interface TrainingService {
    Training createTraining(Trainee trainee, Trainer trainer, String trainingName,
            TrainingType trainingType, LocalDate trainingDate, int trainingDuration);

    Training getTrainingById(Long id);

    List<Training> getAllTrainings();

    List<Training> getTraineeTrainings(String username, LocalDate fromDate, LocalDate toDate,
            String trainerName, String trainingType);

    List<Training> getTrainerTrainings(String username, LocalDate fromDate, LocalDate toDate, String traineeName);
}