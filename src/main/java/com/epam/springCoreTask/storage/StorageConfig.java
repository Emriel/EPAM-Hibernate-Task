package com.epam.springCoreTask.storage;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.epam.springCoreTask.model.Trainee;
import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.model.Training;

@Configuration
public class StorageConfig {

    @Bean
    ConcurrentHashMap<UUID, Trainer> trainerStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    ConcurrentHashMap<UUID, Trainee> traineeStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    ConcurrentHashMap<UUID, Training> trainingStorage() {
        return new ConcurrentHashMap<>();
    }
}
