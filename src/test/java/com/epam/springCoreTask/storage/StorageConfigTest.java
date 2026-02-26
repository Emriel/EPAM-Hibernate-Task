package com.epam.springCoreTask.storage;

import com.epam.springCoreTask.model.Trainee;
import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.model.Training;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StorageConfigTest {

        @Autowired
        private ApplicationContext applicationContext;

        @Autowired

        private ConcurrentHashMap<UUID, Trainer> trainerStorage;

        @Autowired
        private ConcurrentHashMap<UUID, Trainee> traineeStorage;

        @Autowired
        private ConcurrentHashMap<UUID, Training> trainingStorage;

        @Test
        void testTrainerStorageBean_IsCreated() {
                assertNotNull(trainerStorage, "Trainer storage bean should be created");
                assertTrue(trainerStorage instanceof ConcurrentHashMap,
                                "Trainer storage should be a ConcurrentHashMap");
        }

        @Test
        void testTraineeStorageBean_IsCreated() {
                assertNotNull(traineeStorage, "Trainee storage bean should be created");
                assertTrue(traineeStorage instanceof ConcurrentHashMap,
                                "Trainee storage should be a ConcurrentHashMap");
        }

        @Test
        void testTrainingStorageBean_IsCreated() {
                assertNotNull(trainingStorage, "Training storage bean should be created");
                assertTrue(trainingStorage instanceof ConcurrentHashMap,
                                "Training storage should be a ConcurrentHashMap");
        }

        @Test
        @SuppressWarnings("unchecked")
        void testAllStorageBeans_AreSingletons() {
                ConcurrentHashMap<UUID, Trainer> retrievedTrainerStorage = 
                                (ConcurrentHashMap<UUID, Trainer>) applicationContext.getBean("trainerStorage");

                assertSame(trainerStorage, retrievedTrainerStorage,
                                "Injected trainer storage should be same singleton instance");

                ConcurrentHashMap<UUID, Trainee> retrievedTraineeStorage = 
                                (ConcurrentHashMap<UUID, Trainee>) applicationContext.getBean("traineeStorage");

                assertSame(traineeStorage, retrievedTraineeStorage,
                                "Injected trainee storage should be same singleton instance");

                ConcurrentHashMap<UUID, Training> retrievedTrainingStorage = 
                                (ConcurrentHashMap<UUID, Training>) applicationContext.getBean("trainingStorage");

                assertSame(trainingStorage, retrievedTrainingStorage,
                                "Injected training storage should be same singleton instance");
        }

        @Test
        void testStorageBeans_AreInitializedEmpty() {
                assertNotNull(trainerStorage, "Trainer storage should not be null");
                assertNotNull(traineeStorage, "Trainee storage should not be null");
                assertNotNull(trainingStorage, "Training storage should not be null");
        }

        @Test
        void testTrainerStorage_SupportsBasicOperations() {
                Trainer testTrainer = new Trainer();
                testTrainer.setFirstName("Test");
                testTrainer.setLastName("Trainer");

                UUID id = testTrainer.getUserId();
                trainerStorage.put(id, testTrainer);

                assertTrue(trainerStorage.containsKey(id), "Should be able to add trainer");
                assertEquals(testTrainer, trainerStorage.get(id), "Should retrieve the same trainer");

                trainerStorage.remove(id);
                assertFalse(trainerStorage.containsKey(id), "Should be able to remove trainer");
        }

        @Test
        void testTraineeStorage_SupportsBasicOperations() {
                Trainee testTrainee = new Trainee();
                testTrainee.setFirstName("Test");
                testTrainee.setLastName("Trainee");

                UUID id = testTrainee.getUserId();
                traineeStorage.put(id, testTrainee);

                assertTrue(traineeStorage.containsKey(id), "Should be able to add trainee");
                assertEquals(testTrainee, traineeStorage.get(id), "Should retrieve the same trainee");

                traineeStorage.remove(id);
                assertFalse(traineeStorage.containsKey(id), "Should be able to remove trainee");
        }

        @Test
        void testTrainingStorage_SupportsBasicOperations() {
                Training testTraining = new Training();
                testTraining.setTrainingName("Test Training");

                UUID id = UUID.randomUUID();
                testTraining.setTrainingId(id);
                trainingStorage.put(id, testTraining);

                assertTrue(trainingStorage.containsKey(id), "Should be able to add training");
                assertEquals(testTraining, trainingStorage.get(id), "Should retrieve the same training");

                trainingStorage.remove(id);
                assertFalse(trainingStorage.containsKey(id), "Should be able to remove training");
        }

        @Test
        void testStorageConfig_BeanNamesAreCorrect() {
                assertTrue(applicationContext.containsBean("trainerStorage"),
                                "Should have bean named 'trainerStorage'");
                assertTrue(applicationContext.containsBean("traineeStorage"),
                                "Should have bean named 'traineeStorage'");
                assertTrue(applicationContext.containsBean("trainingStorage"),
                                "Should have bean named 'trainingStorage'");
        }
}
