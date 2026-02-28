package com.epam.springCoreTask.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.epam.springCoreTask.model.TrainingType;
import com.epam.springCoreTask.repository.TrainingTypeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public void run(String... args) {
        initializeTrainingTypes();
    }

    private void initializeTrainingTypes() {
        log.info("Initializing training types...");

        String[] trainingTypes = {
            "FITNESS",
            "YOGA",
            "ZUMBA",
            "STRETCHING",
            "RESISTANCE",
            "CARDIO",
            "PILATES",
            "CROSSFIT"
        };

        for (String typeName : trainingTypes) {
            if (!trainingTypeRepository.existsByName(typeName)) {
                TrainingType trainingType = new TrainingType();
                trainingType.setName(typeName);
                trainingTypeRepository.save(trainingType);
                log.debug("Training type created: {}", typeName);
            } else {
                log.debug("Training type already exists: {}", typeName);
            }
        }

        log.info("Training types initialization completed. Total types: {}", trainingTypeRepository.count());
    }
}
