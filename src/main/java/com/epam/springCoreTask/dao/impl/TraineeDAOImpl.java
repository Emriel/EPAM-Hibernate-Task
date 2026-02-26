package com.epam.springCoreTask.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epam.springCoreTask.dao.TraineeDAO;
import com.epam.springCoreTask.model.Trainee;

@Repository
public class TraineeDAOImpl implements TraineeDAO {

    private ConcurrentHashMap<UUID, Trainee> traineeStorage;

    @Autowired
    public void setTraineeStorage(ConcurrentHashMap<UUID, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    @Override
    public Trainee save(Trainee trainee) {
        traineeStorage.put(trainee.getUserId(), trainee);
        return trainee;
    }

    @Override
    public Trainee findById(UUID id) {
        return traineeStorage.get(id);
    }

    @Override
    public boolean delete(UUID id) {
        return traineeStorage.remove(id) != null;
    }

    @Override
    public List<Trainee> findAll() {
        return new ArrayList<>(traineeStorage.values());
    }

    @Override
    public List<String> findAllUsernames() {
        return traineeStorage.values().stream()
                .map(Trainee::getUsername)
                .toList();
    }
}
