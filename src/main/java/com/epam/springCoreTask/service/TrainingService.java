package com.epam.springCoreTask.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.springCoreTask.model.Trainee;
import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.model.Training;
import com.epam.springCoreTask.model.TrainingType;
import com.epam.springCoreTask.repository.TrainingRepository;

@Service
public class TrainingService {

    private static final Logger log = LoggerFactory.getLogger(TrainingService.class);
    
    private TrainingRepository trainingRepository;

    @Autowired
    public void setTrainingRepository(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Transactional
    public Training createTraining(Trainee trainee, Trainer trainer, String trainingName, 
                                   TrainingType trainingType, LocalDate trainingDate, int trainingDuration) {
        log.debug("Creating training: name={}, traineeId={}, trainerId={}, date={}, duration={}", 
                trainingName, trainee.getId(), trainer.getId(), trainingDate, trainingDuration);
        
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

    public Training getTrainingById(Long id) {
        log.debug("Fetching training by id: {}", id);
        
        Training training = trainingRepository.findById(id).orElse(null);
        if (training != null) {
            log.debug("Training found: name={}", training.getTrainingName());
        } else {
            log.warn("Training not found with id: {}", id);
        }
        
        return training;
    }

    public List<Training> getAllTrainings() {
        log.debug("Fetching all trainings");
        
        List<Training> trainings = trainingRepository.findAll();
        log.debug("Found {} trainings", trainings.size());
        
        return trainings;
    }

    public List<Training> getTraineeTrainings(String username, LocalDate fromDate, LocalDate toDate, 
                                               String trainerName, String trainingType) {
        log.debug("Fetching trainings for trainee: {}, fromDate={}, toDate={}, trainerName={}, trainingType={}", 
                username, fromDate, toDate, trainerName, trainingType);
        
        List<Training> trainings = trainingRepository.findTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);
        log.debug("Found {} trainings for trainee: {}", trainings.size(), username);
        
        return trainings;
    }

    public List<Training> getTrainerTrainings(String username, LocalDate fromDate, LocalDate toDate, String traineeName) {
        log.debug("Fetching trainings for trainer: {}, fromDate={}, toDate={}, traineeName={}", 
                username, fromDate, toDate, traineeName);
        
        List<Training> trainings = trainingRepository.findTrainerTrainings(username, fromDate, toDate, traineeName);
        log.debug("Found {} trainings for trainer: {}", trainings.size(), username);
        
        return trainings;
    }
}
