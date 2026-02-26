package com.epam.springCoreTask.service.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.springCoreTask.dao.TraineeDAO;
import com.epam.springCoreTask.model.Trainee;
import com.epam.springCoreTask.service.TraineeService;
import com.epam.springCoreTask.util.PasswordGenerator;
import com.epam.springCoreTask.util.UsernameGenerator;

@Service
public class TraineeServiceImpl implements TraineeService {

    private static final Logger log = LoggerFactory.getLogger(TraineeServiceImpl.class);

    private TraineeDAO traineeDAO;
    private UsernameGenerator usernameGenerator;
    private PasswordGenerator passwordGenerator;

    @Autowired
    public void setTraineeDAO(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Autowired
    public void setUsernameGenerator(UsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
    }

    @Autowired
    public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    public Trainee createTrainee(String firstName, String lastName, java.time.LocalDate dateOfBirth, String address) {
        log.debug("Creating trainee: {} {}, dateOfBirth: {}, address: {}", firstName, lastName, dateOfBirth, address);

        List<String> existingUsernames = traineeDAO.findAllUsernames();

        String username = usernameGenerator.generateUsername(firstName, lastName, existingUsernames);
        String password = passwordGenerator.generatePassword();

        Trainee trainee = new Trainee();
        trainee.setFirstName(firstName);
        trainee.setLastName(lastName);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress(address);
        trainee.setUsername(username);
        trainee.setPassword(password);
        trainee.setActive(true);

        Trainee createdTrainee = traineeDAO.save(trainee);
        log.info("Trainee created successfully: userId={}, username={}", createdTrainee.getUserId(),
                createdTrainee.getUsername());

        return createdTrainee;
    }

    public Trainee updateTrainee(Trainee trainee) {
        log.debug("Updating trainee: userId={}, username={}", trainee.getUserId(), trainee.getUsername());

        Trainee updatedTrainee = traineeDAO.save(trainee);
        log.info("Trainee updated successfully: userId={}", trainee.getUserId());

        return updatedTrainee;
    }

    public void deleteTrainee(Trainee trainee) {
        log.debug("Deleting trainee: userId={}, username={}", trainee.getUserId(), trainee.getUsername());

        boolean deleted = traineeDAO.delete(trainee.getUserId());
        if (deleted) {
            log.info("Trainee deleted successfully: userId={}", trainee.getUserId());
        } else {
            log.warn("Trainee not found for deletion: userId={}", trainee.getUserId());
        }
    }

    public Trainee getTraineeById(UUID id) {
        log.debug("Fetching trainee by id: {}", id);

        Trainee trainee = traineeDAO.findById(id);
        if (trainee != null) {
            log.debug("Trainee found: username={}", trainee.getUsername());
        } else {
            log.warn("Trainee not found with id: {}", id);
        }

        return trainee;
    }

    public List<Trainee> getAllTrainees() {
        log.debug("Fetching all trainees");

        List<Trainee> trainees = traineeDAO.findAll();
        log.debug("Found {} trainees", trainees.size());

        return trainees;
    }
}
