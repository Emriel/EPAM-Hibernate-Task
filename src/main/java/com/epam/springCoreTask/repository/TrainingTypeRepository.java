package com.epam.springCoreTask.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.springCoreTask.model.TrainingType;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
    
    Optional<TrainingType> findByTrainingTypeName(String trainingTypeName);
}
