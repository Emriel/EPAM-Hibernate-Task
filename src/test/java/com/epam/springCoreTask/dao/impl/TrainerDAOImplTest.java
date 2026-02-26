package com.epam.springCoreTask.dao.impl;

import com.epam.springCoreTask.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class TrainerDAOImplTest {

    private TrainerDAOImpl trainerDAO;
    private ConcurrentHashMap<UUID, Trainer> trainerStorage;

    @BeforeEach
    void setUp() {
        trainerStorage = new ConcurrentHashMap<>();
        trainerDAO = new TrainerDAOImpl();
        trainerDAO.setTrainerStorage(trainerStorage);
    }

    @Test
    void testSave_NewTrainer_Successfully() {
        Trainer trainer = new Trainer("John", "Doe", "john.doe", "password123", true, "Fitness");

        Trainer savedTrainer = trainerDAO.save(trainer);

        assertNotNull(savedTrainer, "Saved trainer should not be null");
        assertEquals(trainer, savedTrainer, "Saved trainer should be the same as input");
        assertTrue(trainerStorage.containsKey(trainer.getUserId()), "Storage should contain the trainer");
        assertEquals(1, trainerStorage.size(), "Storage should have one trainer");
    }

    @Test
    void testSave_UpdateExistingTrainer_Successfully() {
        Trainer trainer = new Trainer("Jane", "Smith", "jane.smith", "pass456", true, "Yoga");
        trainerDAO.save(trainer);

        trainer.setSpecialization("Pilates");
        Trainer updatedTrainer = trainerDAO.save(trainer);

        assertEquals("Pilates", updatedTrainer.getSpecialization(), "Specialization should be updated");
        assertEquals(1, trainerStorage.size(), "Storage should still have one trainer");
    }

    @Test
    void testFindById_ExistingTrainer_ReturnsTrainer() {
        Trainer trainer = new Trainer("Bob", "Johnson", "bob.johnson", "pass789", true, "Boxing");
        trainerStorage.put(trainer.getUserId(), trainer);

        Trainer foundTrainer = trainerDAO.findById(trainer.getUserId());

        assertNotNull(foundTrainer, "Found trainer should not be null");
        assertEquals(trainer.getUserId(), foundTrainer.getUserId(), "User IDs should match");
        assertEquals("Bob", foundTrainer.getFirstName(), "First name should match");
        assertEquals("Boxing", foundTrainer.getSpecialization(), "Specialization should match");
    }

    @Test
    void testFindById_NonExistingTrainer_ReturnsNull() {
        UUID randomId = UUID.randomUUID();

        Trainer foundTrainer = trainerDAO.findById(randomId);

        assertNull(foundTrainer, "Non-existing trainer should return null");
    }

    @Test
    void testFindAll_EmptyStorage_ReturnsEmptyList() {
        List<Trainer> trainers = trainerDAO.findAll();

        assertNotNull(trainers, "Result should not be null");
        assertTrue(trainers.isEmpty(), "Result should be empty list");
    }

    @Test
    void testFindAll_WithMultipleTrainers_ReturnsAllTrainers() {
        Trainer trainer1 = new Trainer("Alice", "Williams", "alice.w", "pass1", true, "Cardio");
        Trainer trainer2 = new Trainer("Charlie", "Brown", "charlie.b", "pass2", true, "Strength");
        Trainer trainer3 = new Trainer("David", "Miller", "david.m", "pass3", true, "CrossFit");

        trainerStorage.put(trainer1.getUserId(), trainer1);
        trainerStorage.put(trainer2.getUserId(), trainer2);
        trainerStorage.put(trainer3.getUserId(), trainer3);

        List<Trainer> trainers = trainerDAO.findAll();

        assertNotNull(trainers, "Result should not be null");
        assertEquals(3, trainers.size(), "Should return all 3 trainers");
        assertTrue(trainers.contains(trainer1), "Should contain trainer1");
        assertTrue(trainers.contains(trainer2), "Should contain trainer2");
        assertTrue(trainers.contains(trainer3), "Should contain trainer3");
    }

    @Test
    void testFindAll_ReturnsNewList() {
        Trainer trainer = new Trainer("Eve", "Davis", "eve.d", "pass4", true, "Swimming");
        trainerStorage.put(trainer.getUserId(), trainer);

        List<Trainer> trainers1 = trainerDAO.findAll();
        List<Trainer> trainers2 = trainerDAO.findAll();

        assertNotSame(trainers1, trainers2, "Each call should return a new list instance");
    }

    @Test
    void testSave_MultipleTrainers_AllStoredCorrectly() {
        Trainer trainer1 = new Trainer("Tom", "Anderson", "tom.a", "pass123", true, "Tennis");
        Trainer trainer2 = new Trainer("Sarah", "Wilson", "sarah.w", "pass456", true, "Dance");

        trainerDAO.save(trainer1);
        trainerDAO.save(trainer2);

        assertEquals(2, trainerStorage.size(), "Storage should have two trainers");
        assertNotNull(trainerDAO.findById(trainer1.getUserId()), "Trainer1 should be findable");
        assertNotNull(trainerDAO.findById(trainer2.getUserId()), "Trainer2 should be findable");
    }

    @Test
    void testFindById_WithMultipleTrainers_ReturnsCorrectOne() {
        Trainer trainer1 = new Trainer("Mike", "Taylor", "mike.t", "pass111", true, "Running");
        Trainer trainer2 = new Trainer("Linda", "Moore", "linda.m", "pass222", true, "Cycling");

        trainerStorage.put(trainer1.getUserId(), trainer1);
        trainerStorage.put(trainer2.getUserId(), trainer2);

        Trainer found = trainerDAO.findById(trainer2.getUserId());

        assertEquals("Linda", found.getFirstName(), "Should find correct trainer");
        assertEquals("Cycling", found.getSpecialization(), "Should have correct specialization");
    }
}
