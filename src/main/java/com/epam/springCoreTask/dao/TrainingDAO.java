package com.epam.springCoreTask.dao;

import java.util.List;
import java.util.UUID;

import com.epam.springCoreTask.model.Training;

public interface TrainingDAO {

    Training save(Training training);

    Training findById(UUID id);

    List<Training> findAll();

}
