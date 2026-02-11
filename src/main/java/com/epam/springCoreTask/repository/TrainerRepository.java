package com.epam.springCoreTask.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.springCoreTask.model.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUsername(String username);

    boolean existsByUsername(String username);
    
}
