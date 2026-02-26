package com.epam.springCoreTask.dao.impl;

import com.epam.springCoreTask.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class TraineeDAOImplTest {

    private TraineeDAOImpl traineeDAO;
    private ConcurrentHashMap<UUID, Trainee> traineeStorage;

    @BeforeEach
    void setUp() {
        traineeStorage = new ConcurrentHashMap<>();
        traineeDAO = new TraineeDAOImpl();
        traineeDAO.setTraineeStorage(traineeStorage);
    }

    @Test
    void testSave_NewTrainee_Successfully() {
        Trainee trainee = new Trainee("John", "Doe", "john.doe", "password123", true,
                LocalDate.of(1990, 5, 15), "123 Main St");

        Trainee savedTrainee = traineeDAO.save(trainee);

        assertNotNull(savedTrainee, "Saved trainee should not be null");
        assertEquals(trainee, savedTrainee, "Saved trainee should be the same as input");
        assertTrue(traineeStorage.containsKey(trainee.getUserId()), "Storage should contain the trainee");
        assertEquals(1, traineeStorage.size(), "Storage should have one trainee");
    }

    @Test
    void testSave_UpdateExistingTrainee_Successfully() {
        Trainee trainee = new Trainee("John", "Doe", "john.doe", "password123", true,
                LocalDate.of(1990, 5, 15), "123 Main St");
        traineeDAO.save(trainee);

        trainee.setAddress("456 Oak Ave");
        Trainee updatedTrainee = traineeDAO.save(trainee);

        assertEquals("456 Oak Ave", updatedTrainee.getAddress(), "Address should be updated");
        assertEquals(1, traineeStorage.size(), "Storage should still have one trainee");
    }

    @Test
    void testFindById_ExistingTrainee_ReturnsTrainee() {
        Trainee trainee = new Trainee("Jane", "Smith", "jane.smith", "pass456", true,
                LocalDate.of(1995, 8, 20), "789 Elm St");
        traineeStorage.put(trainee.getUserId(), trainee);

        Trainee foundTrainee = traineeDAO.findById(trainee.getUserId());

        assertNotNull(foundTrainee, "Found trainee should not be null");
        assertEquals(trainee.getUserId(), foundTrainee.getUserId(), "User IDs should match");
        assertEquals("Jane", foundTrainee.getFirstName(), "First name should match");
    }

    @Test
    void testFindById_NonExistingTrainee_ReturnsNull() {
        UUID randomId = UUID.randomUUID();

        Trainee foundTrainee = traineeDAO.findById(randomId);

        assertNull(foundTrainee, "Non-existing trainee should return null");
    }

    @Test
    void testDelete_ExistingTrainee_ReturnsTrue() {
        Trainee trainee = new Trainee("Bob", "Johnson", "bob.johnson", "pass789", true,
                LocalDate.of(1988, 3, 10), "321 Pine St");
        traineeStorage.put(trainee.getUserId(), trainee);

        boolean deleted = traineeDAO.delete(trainee.getUserId());

        assertTrue(deleted, "Delete should return true for existing trainee");
        assertFalse(traineeStorage.containsKey(trainee.getUserId()), "Storage should not contain deleted trainee");
        assertEquals(0, traineeStorage.size(), "Storage should be empty");
    }

    @Test
    void testDelete_NonExistingTrainee_ReturnsFalse() {
        UUID randomId = UUID.randomUUID();

        boolean deleted = traineeDAO.delete(randomId);

        assertFalse(deleted, "Delete should return false for non-existing trainee");
    }

    @Test
    void testFindAll_EmptyStorage_ReturnsEmptyList() {
        List<Trainee> trainees = traineeDAO.findAll();

        assertNotNull(trainees, "Result should not be null");
        assertTrue(trainees.isEmpty(), "Result should be empty list");
    }

    @Test
    void testFindAll_WithMultipleTrainees_ReturnsAllTrainees() {
        Trainee trainee1 = new Trainee("Alice", "Williams", "alice.w", "pass1", true,
                LocalDate.of(1992, 7, 5), "111 First St");
        Trainee trainee2 = new Trainee("Charlie", "Brown", "charlie.b", "pass2", true,
                LocalDate.of(1991, 11, 25), "222 Second St");
        Trainee trainee3 = new Trainee("David", "Miller", "david.m", "pass3", true,
                LocalDate.of(1993, 2, 14), "333 Third St");

        traineeStorage.put(trainee1.getUserId(), trainee1);
        traineeStorage.put(trainee2.getUserId(), trainee2);
        traineeStorage.put(trainee3.getUserId(), trainee3);

        List<Trainee> trainees = traineeDAO.findAll();

        assertNotNull(trainees, "Result should not be null");
        assertEquals(3, trainees.size(), "Should return all 3 trainees");
        assertTrue(trainees.contains(trainee1), "Should contain trainee1");
        assertTrue(trainees.contains(trainee2), "Should contain trainee2");
        assertTrue(trainees.contains(trainee3), "Should contain trainee3");
    }

    @Test
    void testFindAll_ReturnsNewList() {
        Trainee trainee = new Trainee("Eve", "Davis", "eve.d", "pass4", true,
                LocalDate.of(1994, 9, 30), "444 Fourth St");
        traineeStorage.put(trainee.getUserId(), trainee);

        List<Trainee> trainees1 = traineeDAO.findAll();
        List<Trainee> trainees2 = traineeDAO.findAll();

        assertNotSame(trainees1, trainees2, "Each call should return a new list instance");
    }
}
