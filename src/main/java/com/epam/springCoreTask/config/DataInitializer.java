package com.epam.springCoreTask.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.springCoreTask.model.TrainingType;
import com.epam.springCoreTask.repository.TrainingTypeRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {
    
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    
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
                type.setTrainingTypeName(typeName);
                trainingTypeRepository.save(type);
                log.debug("Created training type: {}", typeName);
            }
            
            log.info("Successfully initialized {} training types", trainingTypes.size());
        } else {
            log.info("TrainingType table already contains data. Skipping initialization.");
        }
    }
}
