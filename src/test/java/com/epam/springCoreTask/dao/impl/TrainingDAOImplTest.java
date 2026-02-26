package com.epam.springCoreTask.dao.impl;

import com.epam.springCoreTask.model.Training;
import com.epam.springCoreTask.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDAOImplTest {

        private TrainingDAOImpl trainingDAO;
        private ConcurrentHashMap<UUID, Training> trainingStorage;

        @BeforeEach
        void setUp() {
                trainingStorage = new ConcurrentHashMap<>();
                trainingDAO = new TrainingDAOImpl();
                trainingDAO.setTrainingStorage(trainingStorage);
        }

        @Test
        void testSave_NewTraining_Successfully() {
                UUID traineeId = UUID.randomUUID();
                UUID trainerId = UUID.randomUUID();
                UUID trainingId = UUID.randomUUID();
                TrainingType type = new TrainingType("Cardio");

                Training training = new Training(traineeId, trainerId, trainingId, "Morning Run",
                                type, LocalDate.of(2026, 2, 15), 60);

                Training savedTraining = trainingDAO.save(training);

                assertNotNull(savedTraining, "Saved training should not be null");
                assertEquals(training, savedTraining, "Saved training should be the same as input");
                assertTrue(trainingStorage.containsKey(trainingId), "Storage should contain the training");
                assertEquals(1, trainingStorage.size(), "Storage should have one training");
        }

        @Test
        void testSave_UpdateExistingTraining_Successfully() {
                UUID traineeId = UUID.randomUUID();
                UUID trainerId = UUID.randomUUID();
                UUID trainingId = UUID.randomUUID();
                TrainingType type = new TrainingType("Strength");

                Training training = new Training(traineeId, trainerId, trainingId, "Weight Lifting",
                                type, LocalDate.of(2026, 2, 16), 45);
                trainingDAO.save(training);

                training.setTrainingName("Advanced Weight Lifting");
                Training updatedTraining = trainingDAO.save(training);

                assertEquals("Advanced Weight Lifting", updatedTraining.getTrainingName(),
                                "Training name should be updated");
                assertEquals(1, trainingStorage.size(), "Storage should still have one training");
        }

        @Test
        void testFindById_ExistingTraining_ReturnsTraining() {
                UUID traineeId = UUID.randomUUID();
                UUID trainerId = UUID.randomUUID();
                UUID trainingId = UUID.randomUUID();
                TrainingType type = new TrainingType("Yoga");

                Training training = new Training(traineeId, trainerId, trainingId, "Morning Yoga",
                                type, LocalDate.of(2026, 2, 17), 90);
                trainingStorage.put(trainingId, training);

                Training foundTraining = trainingDAO.findById(trainingId);

                assertNotNull(foundTraining, "Found training should not be null");
                assertEquals(trainingId, foundTraining.getTrainingId(), "Training IDs should match");
                assertEquals("Morning Yoga", foundTraining.getTrainingName(), "Training name should match");
                assertEquals(90, foundTraining.getTrainingDuration(), "Duration should match");
        }

        @Test
        void testFindById_NonExistingTraining_ReturnsNull() {
                UUID randomId = UUID.randomUUID();

                Training foundTraining = trainingDAO.findById(randomId);

                assertNull(foundTraining, "Non-existing training should return null");
        }

        @Test
        void testFindAll_EmptyStorage_ReturnsEmptyList() {
                List<Training> trainings = trainingDAO.findAll();

                assertNotNull(trainings, "Result should not be null");
                assertTrue(trainings.isEmpty(), "Result should be empty list");
        }

        @Test
        void testFindAll_WithMultipleTrainings_ReturnsAllTrainings() {
                UUID traineeId1 = UUID.randomUUID();
                UUID traineeId2 = UUID.randomUUID();
                UUID trainerId = UUID.randomUUID();

                Training training1 = new Training(traineeId1, trainerId, UUID.randomUUID(),
                                "Boxing Session", new TrainingType("Boxing"), LocalDate.of(2026, 2, 10), 60);
                Training training2 = new Training(traineeId2, trainerId, UUID.randomUUID(),
                                "Swimming Lesson", new TrainingType("Swimming"), LocalDate.of(2026, 2, 11), 45);
                Training training3 = new Training(traineeId1, trainerId, UUID.randomUUID(),
                                "CrossFit Training", new TrainingType("CrossFit"), LocalDate.of(2026, 2, 12), 75);

                trainingStorage.put(training1.getTrainingId(), training1);
                trainingStorage.put(training2.getTrainingId(), training2);
                trainingStorage.put(training3.getTrainingId(), training3);

                List<Training> trainings = trainingDAO.findAll();

                assertNotNull(trainings, "Result should not be null");
                assertEquals(3, trainings.size(), "Should return all 3 trainings");
                assertTrue(trainings.contains(training1), "Should contain training1");
                assertTrue(trainings.contains(training2), "Should contain training2");
                assertTrue(trainings.contains(training3), "Should contain training3");
        }

        @Test
        void testFindAll_ReturnsNewList() {
                UUID traineeId = UUID.randomUUID();
                UUID trainerId = UUID.randomUUID();
                Training training = new Training(traineeId, trainerId, UUID.randomUUID(),
                                "Dance Class", new TrainingType("Dance"), LocalDate.of(2026, 2, 13), 60);
                trainingStorage.put(training.getTrainingId(), training);

                List<Training> trainings1 = trainingDAO.findAll();
                List<Training> trainings2 = trainingDAO.findAll();

                assertNotSame(trainings1, trainings2, "Each call should return a new list instance");
        }

        @Test
        void testSave_MultipleTrainings_AllStoredCorrectly() {
                UUID traineeId = UUID.randomUUID();
                UUID trainerId = UUID.randomUUID();

                Training training1 = new Training(traineeId, trainerId, UUID.randomUUID(),
                                "Morning Run", new TrainingType("Cardio"), LocalDate.of(2026, 2, 14), 30);
                Training training2 = new Training(traineeId, trainerId, UUID.randomUUID(),
                                "Evening Gym", new TrainingType("Strength"), LocalDate.of(2026, 2, 14), 90);

                trainingDAO.save(training1);
                trainingDAO.save(training2);

                assertEquals(2, trainingStorage.size(), "Storage should have two trainings");
                assertNotNull(trainingDAO.findById(training1.getTrainingId()), "Training1 should be findable");
                assertNotNull(trainingDAO.findById(training2.getTrainingId()), "Training2 should be findable");
        }

        @Test
        void testFindById_WithMultipleTrainings_ReturnsCorrectOne() {
                UUID traineeId = UUID.randomUUID();
                UUID trainerId = UUID.randomUUID();

                Training training1 = new Training(traineeId, trainerId, UUID.randomUUID(),
                                "Pilates", new TrainingType("Flexibility"), LocalDate.of(2026, 2, 15), 60);
                Training training2 = new Training(traineeId, trainerId, UUID.randomUUID(),
                                "Zumba", new TrainingType("Dance"), LocalDate.of(2026, 2, 16), 45);

                trainingStorage.put(training1.getTrainingId(), training1);
                trainingStorage.put(training2.getTrainingId(), training2);

                Training found = trainingDAO.findById(training2.getTrainingId());

                assertEquals("Zumba", found.getTrainingName(), "Should find correct training");
                assertEquals("Dance", found.getTrainingType().getName(),
                                "Should have correct training type");
        }
}
