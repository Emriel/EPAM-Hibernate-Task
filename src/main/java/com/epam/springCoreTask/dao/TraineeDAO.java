package com.epam.springCoreTask.dao;

import java.util.List;
import java.util.UUID;

import com.epam.springCoreTask.model.Trainee;

public interface TraineeDAO {

    Trainee save(Trainee trainee);

    Trainee findById(UUID id);

    boolean delete(UUID id);

    List<Trainee> findAll();

    List<String> findAllUsernames();

}
