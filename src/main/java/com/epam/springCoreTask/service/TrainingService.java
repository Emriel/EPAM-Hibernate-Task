package com.epam.springCoreTask.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.epam.springCoreTask.model.Training;
import com.epam.springCoreTask.model.TrainingType;

public interface TrainingService {

    Training createTraining(UUID traineeId, UUID trainerId, String trainingName,
            TrainingType trainingType, LocalDate trainingDate, int trainingDuration);

    Training getTrainingById(UUID id);

    List<Training> getAllTrainings();
}
