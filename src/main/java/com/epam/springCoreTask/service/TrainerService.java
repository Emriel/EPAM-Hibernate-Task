package com.epam.springCoreTask.service;

import java.util.List;

import com.epam.springCoreTask.model.Trainer;

public interface TrainerService {
    Trainer createTrainer(String firstName, String lastName, String specialization);

    Trainer updateTrainer(Trainer trainer);

    Trainer getTrainerById(java.util.UUID id);

    List<Trainer> getAllTrainers();
}
