package com.epam.springCoreTask.storage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.epam.springCoreTask.model.Trainee;
import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.util.PasswordGenerator;

@Component
public class StorageInitializer implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(StorageInitializer.class);

    @Value("${storage.init.file}")
    private String initFilePath;

    private ConcurrentHashMap<UUID, Trainer> trainerStorage;
    private ConcurrentHashMap<UUID, Trainee> traineeStorage;
    private PasswordGenerator passwordGenerator;

    @Autowired
    public void setTrainerStorage(ConcurrentHashMap<UUID, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Autowired
    public void setTraineeStorage(ConcurrentHashMap<UUID, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    @Autowired
    public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Initializing storage from file: {}", initFilePath);
        loadDataFromFile();
        log.info("Storage initialization complete. Trainers: {}, Trainees: {}",
                trainerStorage.size(), traineeStorage.size());
    }

    private void loadDataFromFile() throws Exception {
        ClassPathResource resource = new ClassPathResource(initFilePath);

        if (!resource.exists()) {
            log.warn("Initialization file not found: {}", initFilePath);
            return;
        }

        try (InputStream inputStream = resource.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                processLine(line);
            }
        }
    }

    private void processLine(String line) {
        String[] parts = line.split("\\|");

        if (parts.length == 0) {
            return;
        }

        String type = parts[0].trim();

        try {
            switch (type) {
                case "TRAINER":
                    if (parts.length >= 4) {
                        createTrainer(parts[1].trim(), parts[2].trim(), parts[3].trim());
                    }
                    break;
                case "TRAINEE":
                    if (parts.length >= 5) {
                        createTrainee(parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim());
                    }
                    break;
                default:
                    log.debug("Unknown type in file: {}", type);
            }
        } catch (Exception e) {
            log.error("Error processing line: {}", line, e);
        }
    }

    private void createTrainer(String firstName, String lastName, String specialization) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int counter = 1;

        boolean usernameExists = true;
        while (usernameExists) {
            usernameExists = false;
            for (Trainer eachTrainer : trainerStorage.values()) {
                if (eachTrainer.getUsername().equals(username)) {
                    usernameExists = true;
                    username = baseUsername + counter;
                    counter++;
                    break;
                }
            }
        }

        String password = passwordGenerator.generatePassword();

        Trainer trainer = new Trainer();
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setSpecialization(specialization);
        trainer.setUsername(username);
        trainer.setPassword(password);
        trainer.setActive(true);

        trainerStorage.put(trainer.getUserId(), trainer);
        log.debug("Loaded trainer: {} (username: {})", firstName + " " + lastName, username);
    }

    private void createTrainee(String firstName, String lastName, String dateOfBirthStr, String address) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int counter = 1;

        boolean usernameExists = true;
        while (usernameExists) {
            usernameExists = false;
            for (Trainee eachTrainee : traineeStorage.values()) {
                if (eachTrainee.getUsername().equals(username)) {
                    usernameExists = true;
                    username = baseUsername + counter;
                    counter++;
                    break;
                }
            }
        }

        String password = passwordGenerator.generatePassword();

        LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr);

        Trainee trainee = new Trainee();
        trainee.setFirstName(firstName);
        trainee.setLastName(lastName);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress(address);
        trainee.setUsername(username);
        trainee.setPassword(password);
        trainee.setActive(true);

        traineeStorage.put(trainee.getUserId(), trainee);
        log.debug("Loaded trainee: {} (username: {})", firstName + " " + lastName, username);
    }
}
