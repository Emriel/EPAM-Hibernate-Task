package com.epam.springCoreTask.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.model.TrainingType;
import com.epam.springCoreTask.repository.TrainerRepository;
import com.epam.springCoreTask.util.PasswordGenerator;
import com.epam.springCoreTask.util.UsernameGenerator;


@Service
public class TrainerService {

    private static final Logger log = LoggerFactory.getLogger(TrainerService.class);

    private TrainerRepository trainerRepository;
    private UsernameGenerator usernameGenerator;
    private PasswordGenerator passwordGenerator;

    @Autowired
    public void setTrainerRepository(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Autowired
    public void setUsernameGenerator(UsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
    }

    @Autowired
    public void setPasswordGenerator(PasswordGenerator passwordGenerator) { 
        this.passwordGenerator = passwordGenerator;
    }

    @Transactional
    public Trainer createTrainer(String firstName, String lastName, TrainingType specialization) {
        log.debug("Creating trainer: {} {}, specialization: {}", firstName, lastName, specialization);
        
        List<String> existingUsernames = trainerRepository.findAll().stream()
                .map(Trainer::getUsername)
                .toList();
        
        String username = usernameGenerator.generateUsername(firstName, lastName, existingUsernames);
        String password = passwordGenerator.generatePassword();
        
        Trainer trainer = new Trainer();
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setSpecialization(specialization);
        trainer.setUsername(username);
        trainer.setPassword(password);
        trainer.setActive(true);
        
        Trainer createdTrainer = trainerRepository.save(trainer);
        log.info("Trainer created successfully: userId={}, username={}", createdTrainer.getId(), createdTrainer.getUsername());
        
        return createdTrainer;
    }

    @Transactional
    public Trainer updateTrainer(Trainer trainer) {
        log.debug("Updating trainer: userId={}, username={}", trainer.getId(), trainer.getUsername());
        
        Trainer updatedTrainer = trainerRepository.save(trainer);
        log.info("Trainer updated successfully: userId={}", updatedTrainer.getId());
        
        return updatedTrainer;
    }

    public Trainer getTrainerByUsername(String username) {
        log.debug("Fetching trainer by username: {}", username);
        
        Trainer trainer = trainerRepository.findByUsername(username).orElse(null);
        if (trainer != null) {
            log.debug("Trainer found: username={}", trainer.getUsername());
        } else {
            log.warn("Trainer not found with username: {}", username);
        }
        
        return trainer;
    }

    public List<Trainer> getAllTrainers() {
        log.debug("Fetching all trainers");
        
        List<Trainer> trainers = trainerRepository.findAll();
        log.debug("Found {} trainers", trainers.size());
        
        return trainers;
    }

    public boolean authenticateTrainer(String username, String password) {
        log.debug("Authenticating trainer: username={}", username);
        
        Trainer trainer = trainerRepository.findByUsername(username).orElse(null);
        if (trainer != null) {
            boolean isAuthenticated = trainer.getPassword().equals(password);
            log.debug("Authentication result for username {}: {}", username, isAuthenticated);
            return isAuthenticated;
        } else {
            log.warn("Trainer not found with username: {}", username);
        }
        
        return false;
    }

    @Transactional
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        log.debug("Changing password for trainer: username={}", username);
        
        if (!authenticateTrainer(username, oldPassword)) {
            log.warn("Password change failed - authentication failed for username: {}", username);
            return false;
        }
        
        Trainer trainer = trainerRepository.findByUsername(username).orElse(null);
        if (trainer != null) {
            trainer.setPassword(newPassword);
            trainerRepository.save(trainer);
            log.info("Password changed successfully for trainer: username={}", username);
            return true;
        }
        
        return false;
    }

    @Transactional
    public boolean toggleActive(String username, boolean isActive) {
        log.debug("Toggling active status for trainer: username={}, isActive={}", username, isActive);
        
        Trainer trainer = trainerRepository.findByUsername(username).orElse(null);
        if (trainer != null) {
            trainer.setActive(isActive);
            trainerRepository.save(trainer);
            log.info("Active status toggled successfully for trainer: username={}, isActive={}", username, isActive);
            return true;
        } else {
            log.warn("Trainer not found with username: {}", username);
        }
        
        return false;
    }
}
