package com.epam.springCoreTask.facade.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.epam.springCoreTask.facade.GymFacade;
import com.epam.springCoreTask.model.Trainee;
import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.model.Training;
import com.epam.springCoreTask.model.TrainingType;
import com.epam.springCoreTask.service.TraineeService;
import com.epam.springCoreTask.service.TrainerService;
import com.epam.springCoreTask.service.TrainingService;

@Component
public class GymFacadeImpl implements GymFacade {

    private static final Logger log = LoggerFactory.getLogger(GymFacadeImpl.class);

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymFacadeImpl(TraineeService traineeService,
            TrainerService trainerService,
            TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public Trainee createTraineeProfile(String firstName, String lastName,
            LocalDate dateOfBirth, String address) {
        log.info("Creating trainee profile through facade: {} {}", firstName, lastName);
        return traineeService.createTrainee(firstName, lastName, dateOfBirth, address);
    }

    public Trainer createTrainerProfile(String firstName, String lastName,
            String specialization) {
        log.info("Creating trainer profile through facade: {} {}", firstName, lastName);
        return trainerService.createTrainer(firstName, lastName, specialization);
    }

    public Training createTrainingSession(UUID traineeId, UUID trainerId,
            String trainingName, TrainingType trainingType,
            LocalDate trainingDate, int trainingDuration) {
        log.info("Creating training session through facade: {}", trainingName);

        Trainee trainee = traineeService.getTraineeById(traineeId);
        if (trainee == null) {
            log.error("Trainee not found with id: {}", traineeId);
            throw new IllegalArgumentException("Trainee not found with id: " + traineeId);
        }

        Trainer trainer = trainerService.getTrainerById(trainerId);
        if (trainer == null) {
            log.error("Trainer not found with id: {}", trainerId);
            throw new IllegalArgumentException("Trainer not found with id: " + trainerId);
        }

        log.debug("Both trainee and trainer validated, creating training session");
        return trainingService.createTraining(traineeId, trainerId, trainingName,
                trainingType, trainingDate, trainingDuration);
    }

    public List<Training> getTraineeTrainings(UUID traineeId) {
        log.info("Fetching all trainings for trainee: {}", traineeId);

        List<Training> allTrainings = trainingService.getAllTrainings();
        return allTrainings.stream()
                .filter(training -> training.getTraineeId().equals(traineeId))
                .collect(Collectors.toList());
    }

    public List<Training> getTrainerTrainings(UUID trainerId) {
        log.info("Fetching all trainings for trainer: {}", trainerId);

        List<Training> allTrainings = trainingService.getAllTrainings();
        return allTrainings.stream()
                .filter(training -> training.getTrainerId().equals(trainerId))
                .collect(Collectors.toList());
    }

    public Trainee updateTraineeProfile(Trainee trainee) {
        log.info("Updating trainee profile through facade: {}", trainee.getUserId());
        return traineeService.updateTrainee(trainee);
    }

    public Trainer updateTrainerProfile(Trainer trainer) {
        log.info("Updating trainer profile through facade: {}", trainer.getUserId());
        return trainerService.updateTrainer(trainer);
    }

    public void deleteTraineeProfile(UUID traineeId) {
        log.info("Deleting trainee profile and associated trainings: {}", traineeId);

        Trainee trainee = traineeService.getTraineeById(traineeId);
        if (trainee == null) {
            log.warn("Trainee not found for deletion: {}", traineeId);
            return;
        }

        traineeService.deleteTrainee(trainee);
        log.info("Trainee profile deleted successfully: {}", traineeId);
    }

    public Trainee getTraineeById(UUID id) {
        return traineeService.getTraineeById(id);
    }

    public Trainer getTrainerById(UUID id) {
        return trainerService.getTrainerById(id);
    }

    public Training getTrainingById(UUID id) {
        return trainingService.getTrainingById(id);
    }

    public List<Trainee> getAllTrainees() {
        return traineeService.getAllTrainees();
    }

    public List<Trainer> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    public List<Training> getAllTrainings() {
        return trainingService.getAllTrainings();
    }
}
