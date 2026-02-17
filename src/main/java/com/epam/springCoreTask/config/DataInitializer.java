package com.epam.springCoreTask.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.springCoreTask.model.TrainingType;
import com.epam.springCoreTask.repository.TrainingTypeRepository;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataInitializer {
        
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;
    
    @PostConstruct
    public void init() {
        log.info("Initializing database with default data...");
        
        if (trainingTypeRepository.count() == 0) {
            log.info("TrainingType table is empty. Seeding default training types...");
            
            List<String> trainingTypes = List.of(
                "Fitness",
                "Yoga", 
                "Zumba",
                "Stretching",
                "Resistance",
                "Cardio",
                "Pilates",
                "CrossFit",
                "Boxing",
                "Swimming"
            );
            
            for (String typeName : trainingTypes) {
                TrainingType type = new TrainingType();
                type.setName(typeName);
                trainingTypeRepository.save(type);
                log.debug("Created training type: {}", typeName);
            }
            
            log.info("Successfully initialized {} training types", trainingTypes.size());
        } else {
            log.info("TrainingType table already contains data. Skipping initialization.");
        }
    }
}
