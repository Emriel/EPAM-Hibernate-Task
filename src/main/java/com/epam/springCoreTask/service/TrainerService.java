package com.epam.springCoreTask.service;

import java.util.List;

import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.model.TrainingType;

public interface TrainerService {
    Trainer createTrainer(String firstName, String lastName, TrainingType specialization);

    Trainer updateTrainer(Trainer trainer);

    Trainer getTrainerByUsername(String username);

    List<Trainer> getAllTrainers();
}
