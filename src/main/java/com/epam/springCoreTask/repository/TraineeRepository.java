package com.epam.springCoreTask.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.springCoreTask.model.Trainee;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Optional<Trainee> findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);
}
