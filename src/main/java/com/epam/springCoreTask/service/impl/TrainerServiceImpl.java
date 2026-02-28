package com.epam.springCoreTask.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.model.TrainingType;
import com.epam.springCoreTask.model.User;
import com.epam.springCoreTask.repository.TrainerRepository;
import com.epam.springCoreTask.repository.TrainingTypeRepository;
import com.epam.springCoreTask.service.TrainerService;
import com.epam.springCoreTask.util.PasswordGenerator;
import com.epam.springCoreTask.util.UsernameGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final UsernameGenerator usernameGenerator;
    private final PasswordGenerator passwordGenerator;

    @Override
    public Trainer createTrainer(String firstName, String lastName, String specialization) {
        log.debug("Creating trainer: {} {}, specialization: {}", firstName, lastName, specialization);

        List<String> existingUsernames = trainerRepository.findAllUsernames();

        String username = usernameGenerator.generateUsername(firstName, lastName, existingUsernames);
        String password = passwordGenerator.generatePassword();

        // Find or create TrainingType
        TrainingType trainingType = trainingTypeRepository.findByName(specialization)
                .orElseGet(() -> {
                    TrainingType newType = new TrainingType();
                    newType.setName(specialization);
                    return trainingTypeRepository.save(newType);
                });

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setActive(true);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(trainingType);

        Trainer createdTrainer = trainerRepository.save(trainer);
        log.info("Trainer created successfully: id={}, username={}", createdTrainer.getId(),
                createdTrainer.getUser().getUsername());

        return createdTrainer;
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        log.debug("Updating trainer: id={}, username={}", trainer.getId(), trainer.getUser().getUsername());

        Trainer updatedTrainer = trainerRepository.save(trainer);
        log.info("Trainer updated successfully: id={}", trainer.getId());

        return updatedTrainer;
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer getTrainerById(Long id) {
        log.debug("Fetching trainer by id: {}", id);

        return trainerRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getAllTrainers() {
        log.debug("Fetching all trainers");

        List<Trainer> trainers = trainerRepository.findAll();
        log.debug("Found {} trainers", trainers.size());

        return trainers;
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer authenticateTrainer(String username, String password) {
        log.debug("Authenticating trainer: username={}", username);

        Trainer trainer = trainerRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> {
                    log.warn("Authentication failed for trainer: username={}", username);
                    return new IllegalArgumentException("Invalid username or password");
                });

        log.info("Trainer authenticated successfully: username={}", username);
        return trainer;
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer getTrainerByUsername(String username) {
        log.debug("Fetching trainer by username: {}", username);

        return trainerRepository.findByUser_Username(username)
                .orElseThrow(() -> {
                    log.warn("Trainer not found: username={}", username);
                    return new IllegalArgumentException("Trainer not found with username: " + username);
                });
    }

    @Override
    public void changeTrainerPassword(String username, String oldPassword, String newPassword) {
        log.debug("Changing password for trainer: username={}", username);

        Trainer trainer = trainerRepository.findByUsernameAndPassword(username, oldPassword)
                .orElseThrow(() -> {
                    log.warn("Password change failed - invalid credentials: username={}", username);
                    return new IllegalArgumentException("Invalid username or password");
                });

        trainer.getUser().setPassword(newPassword);
        trainerRepository.save(trainer);

        log.info("Password changed successfully for trainer: username={}", username);
    }

    @Override
    public void activateTrainer(String username) {
        log.debug("Activating trainer: username={}", username);

        Trainer trainer = getTrainerByUsername(username);
        trainer.getUser().setActive(true);
        trainerRepository.save(trainer);

        log.info("Trainer activated successfully: username={}", username);
    }

    @Override
    public void deactivateTrainer(String username) {
        log.debug("Deactivating trainer: username={}", username);

        Trainer trainer = getTrainerByUsername(username);
        trainer.getUser().setActive(false);
        trainerRepository.save(trainer);

        log.info("Trainer deactivated successfully: username={}", username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getTrainersNotAssignedToTrainee(String traineeUsername) {
        log.debug("Fetching trainers not assigned to trainee: traineeUsername={}", traineeUsername);

        List<Trainer> trainers = trainerRepository.findTrainersNotAssignedToTrainee(traineeUsername);
        log.info("Found {} unassigned trainers for trainee: {}", trainers.size(), traineeUsername);

        return trainers;
    }
}
