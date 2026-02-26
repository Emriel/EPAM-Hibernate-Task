package com.epam.springCoreTask.service.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.springCoreTask.dao.TrainerDAO;
import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.service.TrainerService;
import com.epam.springCoreTask.util.PasswordGenerator;
import com.epam.springCoreTask.util.UsernameGenerator;

@Service
public class TrainerServiceImpl implements TrainerService {

    private static final Logger log = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private TrainerDAO trainerDAO;
    private UsernameGenerator usernameGenerator;
    private PasswordGenerator passwordGenerator;

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void setUsernameGenerator(UsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
    }

    @Autowired
    public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    public Trainer createTrainer(String firstName, String lastName, String specialization) {
        log.debug("Creating trainer: {} {}, specialization: {}", firstName, lastName, specialization);

        List<String> existingUsernames = trainerDAO.findAllUsernames();

        String username = usernameGenerator.generateUsername(firstName, lastName, existingUsernames);
        String password = passwordGenerator.generatePassword();

        Trainer trainer = new Trainer();
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setSpecialization(specialization);
        trainer.setUsername(username);
        trainer.setPassword(password);
        trainer.setActive(true);

        Trainer createdTrainer = trainerDAO.save(trainer);
        log.info("Trainer created successfully: userId={}, username={}", createdTrainer.getUserId(),
                createdTrainer.getUsername());

        return createdTrainer;
    }

    public Trainer updateTrainer(Trainer trainer) {
        log.debug("Updating trainer: userId={}, username={}", trainer.getUserId(), trainer.getUsername());

        Trainer updatedTrainer = trainerDAO.save(trainer);
        log.info("Trainer updated successfully: userId={}", trainer.getUserId());

        return updatedTrainer;
    }

    public Trainer getTrainerById(UUID id) {
        log.debug("Fetching trainer by id: {}", id);

        Trainer trainer = trainerDAO.findById(id);
        if (trainer != null) {
            log.debug("Trainer found: username={}", trainer.getUsername());
        } else {
            log.warn("Trainer not found with id: {}", id);
        }

        return trainer;
    }

    public List<Trainer> getAllTrainers() {
        log.debug("Fetching all trainers");

        List<Trainer> trainers = trainerDAO.findAll();
        log.debug("Found {} trainers", trainers.size());

        return trainers;
    }
}
