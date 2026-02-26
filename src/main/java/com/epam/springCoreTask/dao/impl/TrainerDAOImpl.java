package com.epam.springCoreTask.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epam.springCoreTask.dao.TrainerDAO;
import com.epam.springCoreTask.model.Trainer;

@Repository
public class TrainerDAOImpl implements TrainerDAO {

    private ConcurrentHashMap<UUID, Trainer> trainerStorage;

    @Autowired
    public void setTrainerStorage(ConcurrentHashMap<UUID, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Override
    public Trainer save(Trainer trainer) {
        trainerStorage.put(trainer.getUserId(), trainer);
        return trainer;
    }

    @Override
    public Trainer findById(UUID id) {
        return trainerStorage.get(id);
    }

    @Override
    public List<Trainer> findAll() {
        return new ArrayList<>(trainerStorage.values());
    }

    @Override
    public List<String> findAllUsernames() {
        return trainerStorage.values().stream()
                .map(Trainer::getUsername)
                .toList();
    }
}
