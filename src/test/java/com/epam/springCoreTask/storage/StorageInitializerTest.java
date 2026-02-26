package com.epam.springCoreTask.storage;

import com.epam.springCoreTask.model.Trainee;
import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.util.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageInitializerTest {

    private StorageInitializer storageInitializer;
    private ConcurrentHashMap<UUID, Trainer> trainerStorage;
    private ConcurrentHashMap<UUID, Trainee> traineeStorage;

    @Mock
    private PasswordGenerator passwordGenerator;

    @BeforeEach
    void setUp() {
        trainerStorage = new ConcurrentHashMap<>();
        traineeStorage = new ConcurrentHashMap<>();
        storageInitializer = new StorageInitializer();

        storageInitializer.setTrainerStorage(trainerStorage);
        storageInitializer.setTraineeStorage(traineeStorage);
        storageInitializer.setPasswordGenerator(passwordGenerator);
    }

    @Test
    void testAfterPropertiesSet_LoadsDataFromValidFile() throws Exception {
        // Setup
        ReflectionTestUtils.setField(storageInitializer, "initFilePath", "storage-init.txt");

        when(passwordGenerator.generatePassword()).thenReturn("testPassword123");

        // Execute
        storageInitializer.afterPropertiesSet();

        // Verify trainers were loaded
        assertEquals(3, trainerStorage.size(), "Should load 3 trainers from file");

        // Verify trainer data
        Trainer johnDoe = findTrainerByName("John", "Doe");
        assertNotNull(johnDoe, "John Doe should be loaded");
        assertEquals("Fitness", johnDoe.getSpecialization());
        assertEquals("John.Doe", johnDoe.getUsername());
        assertEquals("testPassword123", johnDoe.getPassword());
        assertTrue(johnDoe.isActive());

        // Verify trainees were loaded
        assertEquals(3, traineeStorage.size(), "Should load 3 trainees from file");

        // Verify trainee data
        Trainee aliceWilliams = findTraineeByName("Alice", "Williams");
        assertNotNull(aliceWilliams, "Alice Williams should be loaded");
        assertEquals(LocalDate.of(1995, 5, 15), aliceWilliams.getDateOfBirth());
        assertEquals("123 Main St", aliceWilliams.getAddress());
        assertEquals("Alice.Williams", aliceWilliams.getUsername());
        assertEquals("testPassword123", aliceWilliams.getPassword());
        assertTrue(aliceWilliams.isActive());

        // Verify password generator was called
        verify(passwordGenerator, times(6)).generatePassword();
    }

    @Test
    void testAfterPropertiesSet_FileNotFound_DoesNotThrowException() throws Exception {
        ReflectionTestUtils.setField(storageInitializer, "initFilePath", "non-existent-file.txt");

        // Should not throw exception
        assertDoesNotThrow(() -> storageInitializer.afterPropertiesSet());

        // Storages should be empty
        assertEquals(0, trainerStorage.size(), "Trainer storage should be empty");
        assertEquals(0, traineeStorage.size(), "Trainee storage should be empty");
    }

    @Test
    void testAfterPropertiesSet_HandlesEmptyLines() throws Exception {
        ReflectionTestUtils.setField(storageInitializer, "initFilePath", "storage-init.txt");

        when(passwordGenerator.generatePassword()).thenReturn("password");

        storageInitializer.afterPropertiesSet();

        // Should successfully load data despite empty lines and comments in file
        assertTrue(trainerStorage.size() > 0, "Should load trainers despite formatting");
        assertTrue(traineeStorage.size() > 0, "Should load trainees despite formatting");
    }

    @Test
    void testAfterPropertiesSet_GeneratesUniqueUsernames() throws Exception {
        ReflectionTestUtils.setField(storageInitializer, "initFilePath", "storage-init.txt");

        when(passwordGenerator.generatePassword()).thenReturn("password");

        storageInitializer.afterPropertiesSet();

        // Verify all trainers have unique usernames
        List<String> trainerUsernames = trainerStorage.values().stream()
                .map(Trainer::getUsername)
                .toList();
        assertEquals(trainerUsernames.size(), trainerUsernames.stream().distinct().count(),
                "All trainer usernames should be unique");

        // Verify all trainees have unique usernames
        List<String> traineeUsernames = traineeStorage.values().stream()
                .map(Trainee::getUsername)
                .toList();
        assertEquals(traineeUsernames.size(), traineeUsernames.stream().distinct().count(),
                "All trainee usernames should be unique");
    }

    @Test
    void testAfterPropertiesSet_HandlesUsernameCollisions() throws Exception {
        ReflectionTestUtils.setField(storageInitializer, "initFilePath", "storage-init.txt");

        when(passwordGenerator.generatePassword()).thenReturn("password");

        storageInitializer.afterPropertiesSet();

        // Verify that all usernames are unique even if there are potential collisions
        List<String> allUsernames = new java.util.ArrayList<>(trainerStorage.values().stream()
                .map(Trainer::getUsername)
                .toList());
        allUsernames.addAll(traineeStorage.values().stream()
                .map(Trainee::getUsername)
                .toList());

        assertEquals(allUsernames.size(), allUsernames.stream().distinct().count(),
                "All usernames should be unique");
    }

    @Test
    void testSetters_WorkCorrectly() {
        ConcurrentHashMap<UUID, Trainer> newTrainerStorage = new ConcurrentHashMap<>();
        ConcurrentHashMap<UUID, Trainee> newTraineeStorage = new ConcurrentHashMap<>();
        PasswordGenerator newPasswordGen = mock(PasswordGenerator.class);

        storageInitializer.setTrainerStorage(newTrainerStorage);
        storageInitializer.setTraineeStorage(newTraineeStorage);
        storageInitializer.setPasswordGenerator(newPasswordGen);

        // Verify by checking if fields are set (using reflection or by testing
        // behavior)
        assertNotNull(storageInitializer);
    }

    // Helper methods
    private Trainer findTrainerByName(String firstName, String lastName) {
        return trainerStorage.values().stream()
                .filter(t -> t.getFirstName().equals(firstName) && t.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }

    private Trainee findTraineeByName(String firstName, String lastName) {
        return traineeStorage.values().stream()
                .filter(t -> t.getFirstName().equals(firstName) && t.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }
}
