package com.epam.springCoreTask.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epam.springCoreTask.dao.TrainingDAO;
import com.epam.springCoreTask.model.Training;

@Repository
public class TrainingDAOImpl implements TrainingDAO {

    private ConcurrentHashMap<UUID, Training> trainingStorage;

    @Autowired
    public void setTrainingStorage(ConcurrentHashMap<UUID, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    @Override
    public Training save(Training training) {
        trainingStorage.put(training.getTrainingId(), training);
        return training;
    }

    @Override
    public Training findById(UUID id) {
        return trainingStorage.get(id);
    }

    @Override
    public List<Training> findAll() {
        return new ArrayList<>(trainingStorage.values());
    }
}
