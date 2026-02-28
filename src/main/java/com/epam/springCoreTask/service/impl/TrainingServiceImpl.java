package com.epam.springCoreTask.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.springCoreTask.model.Trainee;
import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.model.Training;
import com.epam.springCoreTask.model.TrainingType;
import com.epam.springCoreTask.repository.TraineeRepository;
import com.epam.springCoreTask.repository.TrainerRepository;
import com.epam.springCoreTask.repository.TrainingRepository;
import com.epam.springCoreTask.service.TrainingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    @Override
    public Training createTraining(Long traineeId, Long trainerId, String trainingName,
            TrainingType trainingType, LocalDate trainingDate, int trainingDuration) {
        log.debug("Creating training: name={}, traineeId={}, trainerId={}, date={}, duration={}",
                trainingName, traineeId, trainerId, trainingDate, trainingDuration);

        Trainee trainee = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found with id: " + traineeId));
        
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found with id: " + trainerId));

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(trainingName);
        training.setTrainingType(trainingType);
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(trainingDuration);

        Training createdTraining = trainingRepository.save(training);
        log.info("Training created successfully: id={}, name={}", createdTraining.getId(), trainingName);

        return createdTraining;
    }

    @Override
    @Transactional(readOnly = true)
    public Training getTrainingById(Long id) {
        log.debug("Fetching training by id: {}", id);

        return trainingRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getAllTrainings() {
        log.debug("Fetching all trainings");

        List<Training> trainings = trainingRepository.findAll();
        log.debug("Found {} trainings", trainings.size());

        return trainings;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTraineeTrainingsWithCriteria(String traineeUsername, LocalDate fromDate,
            LocalDate toDate, String trainerName, String trainingTypeName) {
        log.debug("Fetching trainee trainings with criteria: traineeUsername={}, fromDate={}, toDate={}, trainerName={}, trainingType={}",
                traineeUsername, fromDate, toDate, trainerName, trainingTypeName);

        List<Training> trainings = trainingRepository.findTraineeTrainings(
                traineeUsername, fromDate, toDate, trainerName, trainingTypeName);

        log.info("Found {} trainings for trainee: {}", trainings.size(), traineeUsername);
        return trainings;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainingsWithCriteria(String trainerUsername, LocalDate fromDate,
            LocalDate toDate, String traineeName) {
        log.debug("Fetching trainer trainings with criteria: trainerUsername={}, fromDate={}, toDate={}, traineeName={}",
                trainerUsername, fromDate, toDate, traineeName);

        List<Training> trainings = trainingRepository.findTrainerTrainings(
                trainerUsername, fromDate, toDate, traineeName);

        log.info("Found {} trainings for trainer: {}", trainings.size(), trainerUsername);
        return trainings;
    }

}
