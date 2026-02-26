package com.epam.springCoreTask.dao;

import java.util.List;
import java.util.UUID;

import com.epam.springCoreTask.model.Trainer;

public interface TrainerDAO {

    Trainer save(Trainer trainer);

    Trainer findById(UUID id);

    List<Trainer> findAll();

    List<String> findAllUsernames();

}
