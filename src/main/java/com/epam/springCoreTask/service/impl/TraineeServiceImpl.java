package com.epam.springCoreTask.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.springCoreTask.model.Trainee;
import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.repository.TraineeRepository;
import com.epam.springCoreTask.repository.TrainerRepository;
import com.epam.springCoreTask.service.TraineeService;
import com.epam.springCoreTask.util.PasswordGenerator;
import com.epam.springCoreTask.util.UsernameGenerator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TraineeServiceImpl implements TraineeService{
    
    private TraineeRepository traineeRepository;
    private TrainerRepository trainerRepository;
    private UsernameGenerator usernameGenerator;
    private PasswordGenerator passwordGenerator;

    @Autowired
    public void setTraineeRepository(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

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
    public Trainee createTrainee(String firstName, String lastName, LocalDate dateOfBirth, String address) {
        log.debug("Creating trainee: {} {}, dateOfBirth: {}, address: {}", firstName, lastName, dateOfBirth, address);
        
        List<String> existingUsernames = traineeRepository.findAll().stream()
                .map(Trainee::getUsername)
                .toList();
        
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
        
        Trainee createdTrainee = traineeRepository.save(trainee);
        log.info("Trainee created successfully: id={}, username={}", createdTrainee.getId(), createdTrainee.getUsername());
        
        return createdTrainee;
    }

    public Trainee getTraineeByUsername(String username) {
        log.debug("Fetching trainee by username: {}", username);
        
        Trainee trainee = traineeRepository.findByUsername(username).orElse(null);
        if (trainee != null) {
            log.debug("Trainee found: id={}", trainee.getId());
        } else {
            log.warn("Trainee not found with username: {}", username);
        }
        
        return trainee;
    }

    public boolean authenticateTrainee(String username, String password) {
        log.debug("Authenticating trainee: username={}", username);
        
        Trainee trainee = traineeRepository.findByUsername(username).orElse(null);
        if (trainee != null && trainee.getPassword().equals(password)) {
            log.info("Trainee authenticated successfully: username={}", username);
            return true;
        }
        
        log.warn("Authentication failed for trainee: username={}", username);
        return false;
    }

    @Transactional
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        log.debug("Changing password for trainee: username={}", username);
        
        if (!authenticateTrainee(username, oldPassword)) {
            log.warn("Password change failed - authentication failed for username: {}", username);
            return false;
        }
        
        Trainee trainee = traineeRepository.findByUsername(username).orElse(null);
        if (trainee != null) {
            trainee.setPassword(newPassword);
            traineeRepository.save(trainee);
            log.info("Password changed successfully for trainee: username={}", username);
            return true;
        }
        
        return false;
    }

    @Transactional
    public Trainee updateTrainee(Trainee trainee) {
        log.debug("Updating trainee: id={}, username={}", trainee.getId(), trainee.getUsername());
        
        Trainee updatedTrainee = traineeRepository.save(trainee);
        log.info("Trainee updated successfully: id={}", trainee.getId());
        
        return updatedTrainee;
    }

    @Transactional
    public boolean toggleActive(String username, boolean isActive) {
        log.debug("Toggling active status for trainee: username={}, isActive={}", username, isActive);
        
        Trainee trainee = traineeRepository.findByUsername(username).orElse(null);
        if (trainee != null) {
            trainee.setActive(isActive);
            traineeRepository.save(trainee);
            log.info("Trainee active status updated: username={}, isActive={}", username, isActive);
            return true;
        }
        
        log.warn("Trainee not found for toggling active: username={}", username);
        return false;
    }

    @Transactional
    public boolean deleteByUsername(String username) {
        log.debug("Deleting trainee by username: {}", username);
        
        if (traineeRepository.existsByUsername(username)) {
            traineeRepository.deleteByUsername(username);
            log.info("Trainee deleted successfully: username={}", username);
            return true;
        }
        
        log.warn("Trainee not found for deletion: username={}", username);
        return false;
    }

    public List<Trainee> getAllTrainees() {
        log.debug("Fetching all trainees");
        
        List<Trainee> trainees = traineeRepository.findAll();
        log.debug("Found {} trainees", trainees.size());
        
        return trainees;
    }

    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        log.debug("Fetching unassigned trainers for trainee: {}", traineeUsername);
        
        Trainee trainee = traineeRepository.findByUsername(traineeUsername).orElse(null);
        if (trainee == null) {
            log.warn("Trainee not found with username: {}", traineeUsername);
            return List.of();
        }
        
        List<Trainer> allTrainers = trainerRepository.findAll();
        
        List<Trainer> unassignedTrainers = allTrainers.stream()
                .filter(trainer -> !trainee.getTrainers().contains(trainer))
                .collect(Collectors.toList());
        
        log.debug("Found {} unassigned trainers for trainee: {}", unassignedTrainers.size(), traineeUsername);
        return unassignedTrainers;
    }

    @Transactional
    public boolean updateTrainersList(String traineeUsername, List<String> trainerUsernames) {
        log.debug("Updating trainers list for trainee: {}, trainers: {}", traineeUsername, trainerUsernames);
        
        Trainee trainee = traineeRepository.findByUsername(traineeUsername).orElse(null);
        if (trainee == null) {
            log.warn("Trainee not found with username: {}", traineeUsername);
            return false;
        }
        
        trainee.getTrainers().clear();
        
        for (String trainerUsername : trainerUsernames) {
            Trainer trainer = trainerRepository.findByUsername(trainerUsername).orElse(null);
            if (trainer != null) {
                trainee.getTrainers().add(trainer);
            } else {
                log.warn("Trainer not found with username: {}", trainerUsername);
            }
        }
        
        traineeRepository.save(trainee);
        log.info("Trainers list updated successfully for trainee: {}, assigned {} trainers", 
                traineeUsername, trainee.getTrainers().size());
        
        return true;
    }
    
}
