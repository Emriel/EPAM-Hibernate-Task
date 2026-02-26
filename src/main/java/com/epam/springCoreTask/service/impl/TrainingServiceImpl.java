package com.epam.springCoreTask.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.springCoreTask.dao.TrainingDAO;
import com.epam.springCoreTask.model.Training;
import com.epam.springCoreTask.model.TrainingType;
import com.epam.springCoreTask.service.TrainingService;

@Service
public class TrainingServiceImpl implements TrainingService {

    private static final Logger log = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private TrainingDAO trainingDAO;

    @Autowired
    public void setTrainingDAO(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    public Training createTraining(UUID traineeId, UUID trainerId, String trainingName,
            TrainingType trainingType, LocalDate trainingDate, int trainingDuration) {
        log.debug("Creating training: name={}, traineeId={}, trainerId={}, date={}, duration={}",
                trainingName, traineeId, trainerId, trainingDate, trainingDuration);

        Training training = new Training();
        training.setTrainingId(UUID.randomUUID());
        training.setTraineeId(traineeId);
        training.setTrainerId(trainerId);
        training.setTrainingName(trainingName);
        training.setTrainingType(trainingType);
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(trainingDuration);

        Training createdTraining = trainingDAO.save(training);
        log.info("Training created successfully: trainingId={}, name={}",
                createdTraining.getTrainingId(), trainingName);

        return createdTraining;
    }

    public Training getTrainingById(UUID id) {
        log.debug("Fetching training by id: {}", id);

        Training training = trainingDAO.findById(id);
        if (training != null) {
            log.debug("Training found: name={}", training.getTrainingName());
        } else {
            log.warn("Training not found with id: {}", id);
        }

        return training;
    }

    public List<Training> getAllTrainings() {
        log.debug("Fetching all trainings");

        List<Training> trainings = trainingDAO.findAll();
        log.debug("Found {} trainings", trainings.size());

        return trainings;
    }

}
