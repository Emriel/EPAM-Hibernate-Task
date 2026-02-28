package com.epam.springCoreTask.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.springCoreTask.model.Trainee;
import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.model.User;
import com.epam.springCoreTask.repository.TraineeRepository;
import com.epam.springCoreTask.repository.TrainerRepository;
import com.epam.springCoreTask.service.TraineeService;
import com.epam.springCoreTask.util.PasswordGenerator;
import com.epam.springCoreTask.util.UsernameGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final UsernameGenerator usernameGenerator;
    private final PasswordGenerator passwordGenerator;

    @Override
    public Trainee createTrainee(String firstName, String lastName, java.time.LocalDate dateOfBirth, String address) {
        log.debug("Creating trainee: {} {}, dateOfBirth: {}, address: {}", firstName, lastName, dateOfBirth, address);

        List<String> existingUsernames = traineeRepository.findAllUsernames();

        String username = usernameGenerator.generateUsername(firstName, lastName, existingUsernames);
        String password = passwordGenerator.generatePassword();

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setActive(true);

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress(address);

        Trainee createdTrainee = traineeRepository.save(trainee);
        log.info("Trainee created successfully: id={}, username={}", createdTrainee.getId(),
                createdTrainee.getUser().getUsername());

        return createdTrainee;
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        log.debug("Updating trainee: id={}, username={}", trainee.getId(), trainee.getUser().getUsername());

        Trainee updatedTrainee = traineeRepository.save(trainee);
        log.info("Trainee updated successfully: id={}", trainee.getId());

        return updatedTrainee;
    }

    @Override
    public void deleteTrainee(Trainee trainee) {
        log.debug("Deleting trainee: id={}, username={}", trainee.getId(), trainee.getUser().getUsername());

        traineeRepository.delete(trainee);
        log.info("Trainee deleted successfully: id={}", trainee.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Trainee getTraineeById(Long id) {
        log.debug("Fetching trainee by id: {}", id);

        return traineeRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> getAllTrainees() {
        log.debug("Fetching all trainees");

        List<Trainee> trainees = traineeRepository.findAll();
        log.debug("Found {} trainees", trainees.size());

        return trainees;
    }

    @Override
    @Transactional(readOnly = true)
    public Trainee authenticateTrainee(String username, String password) {
        log.debug("Authenticating trainee: username={}", username);

        Trainee trainee = traineeRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> {
                    log.warn("Authentication failed for trainee: username={}", username);
                    return new IllegalArgumentException("Invalid username or password");
                });

        log.info("Trainee authenticated successfully: username={}", username);
        return trainee;
    }

    @Override
    @Transactional(readOnly = true)
    public Trainee getTraineeByUsername(String username) {
        log.debug("Fetching trainee by username: {}", username);

        return traineeRepository.findByUser_Username(username)
                .orElseThrow(() -> {
                    log.warn("Trainee not found: username={}", username);
                    return new IllegalArgumentException("Trainee not found with username: " + username);
                });
    }

    @Override
    public void changeTraineePassword(String username, String oldPassword, String newPassword) {
        log.debug("Changing password for trainee: username={}", username);

        Trainee trainee = traineeRepository.findByUsernameAndPassword(username, oldPassword)
                .orElseThrow(() -> {
                    log.warn("Password change failed - invalid credentials: username={}", username);
                    return new IllegalArgumentException("Invalid username or password");
                });

        trainee.getUser().setPassword(newPassword);
        traineeRepository.save(trainee);

        log.info("Password changed successfully for trainee: username={}", username);
    }

    @Override
    public void activateTrainee(String username) {
        log.debug("Activating trainee: username={}", username);

        Trainee trainee = getTraineeByUsername(username);
        trainee.getUser().setActive(true);
        traineeRepository.save(trainee);

        log.info("Trainee activated successfully: username={}", username);
    }

    @Override
    public void deactivateTrainee(String username) {
        log.debug("Deactivating trainee: username={}", username);

        Trainee trainee = getTraineeByUsername(username);
        trainee.getUser().setActive(false);
        traineeRepository.save(trainee);

        log.info("Trainee deactivated successfully: username={}", username);
    }

    @Override
    public void deleteTraineeByUsername(String username) {
        log.debug("Deleting trainee by username: {}", username);

        Trainee trainee = getTraineeByUsername(username);
        traineeRepository.delete(trainee);

        log.info("Trainee deleted successfully: username={}", username);
    }

    @Override
    public void updateTraineeTrainersList(String traineeUsername, List<String> trainerUsernames) {
        log.debug("Updating trainers list for trainee: traineeUsername={}, trainerUsernames={}",
                traineeUsername, trainerUsernames);

        Trainee trainee = getTraineeByUsername(traineeUsername);

        // Clear existing trainers
        trainee.getTrainers().clear();

        // Add new trainers
        if (trainerUsernames != null && !trainerUsernames.isEmpty()) {
            for (String trainerUsername : trainerUsernames) {
                Trainer trainer = trainerRepository.findByUser_Username(trainerUsername)
                        .orElseThrow(() -> new IllegalArgumentException("Trainer not found: " + trainerUsername));
                trainee.getTrainers().add(trainer);
            }
        }

        traineeRepository.save(trainee);
        log.info("Trainers list updated successfully for trainee: username={}, trainers count={}",
                traineeUsername, trainee.getTrainers().size());
    }
}
