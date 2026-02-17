package com.epam.springCoreTask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.model.TrainingType;
import com.epam.springCoreTask.repository.TrainerRepository;
import com.epam.springCoreTask.service.impl.TrainerServiceImpl;
import com.epam.springCoreTask.util.PasswordGenerator;
import com.epam.springCoreTask.util.UsernameGenerator;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UsernameGenerator usernameGenerator;

    @Mock
    private PasswordGenerator passwordGenerator;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer testTrainer;
    private TrainingType testSpecialization;
    private Long testId;

    @BeforeEach
    void setUp() {
        testId = 1L;
        testSpecialization = new TrainingType();
        testSpecialization.setId(1L);
        testSpecialization.setName("Yoga");
        
        testTrainer = new Trainer();
        testTrainer.setId(testId);
        testTrainer.setFirstName("Jane");
        testTrainer.setLastName("Smith");
        testTrainer.setUsername("Jane.Smith");
        testTrainer.setPassword("password123");
        testTrainer.setSpecialization(testSpecialization);
        testTrainer.setActive(true);
    }

    @Test
    void testCreateTrainer_Success() {
        // Arrange
        String firstName = "Jane";
        String lastName = "Smith";
        
        List<Trainer> existingTrainers = new ArrayList<>();
        when(trainerRepository.findAll()).thenReturn(existingTrainers);
        when(usernameGenerator.generateUsername(eq(firstName), eq(lastName), anyList())).thenReturn("Jane.Smith");
        when(passwordGenerator.generatePassword()).thenReturn("password123");
        when(trainerRepository.save(any(Trainer.class))).thenReturn(testTrainer);

        // Act
        Trainer result = trainerService.createTrainer(firstName, lastName, testSpecialization);

        // Assert
        assertNotNull(result);
        assertEquals("Jane.Smith", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertEquals(testSpecialization, result.getSpecialization());
        assertTrue(result.isActive());
        verify(trainerRepository).findAll();
        verify(usernameGenerator).generateUsername(eq(firstName), eq(lastName), anyList());
        verify(passwordGenerator).generatePassword();
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void testUpdateTrainer_Success() {
        // Arrange
        when(trainerRepository.save(testTrainer)).thenReturn(testTrainer);

        // Act
        Trainer result = trainerService.updateTrainer(testTrainer);

        // Assert
        assertNotNull(result);
        assertEquals(testTrainer.getUsername(), result.getUsername());
        verify(trainerRepository).save(testTrainer);
    }

    @Test
    void testGetTrainerByUsername_Found() {
        // Arrange
        when(trainerRepository.findByUsername("Jane.Smith")).thenReturn(Optional.of(testTrainer));

        // Act
        Trainer result = trainerService.getTrainerByUsername("Jane.Smith");

        // Assert
        assertNotNull(result);
        assertEquals(testTrainer.getUsername(), result.getUsername());
        verify(trainerRepository).findByUsername("Jane.Smith");
    }

    @Test
    void testAuthenticateTrainer_Success() {
        // Arrange
        when(trainerRepository.findByUsername("Jane.Smith")).thenReturn(Optional.of(testTrainer));

        // Act
        boolean result = trainerService.authenticateTrainer("Jane.Smith", "password123");

        // Assert
        assertTrue(result);
        verify(trainerRepository).findByUsername("Jane.Smith");
    }

    @Test
    void testChangePassword_Success() {
        // Arrange
        when(trainerRepository.findByUsername("Jane.Smith")).thenReturn(Optional.of(testTrainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(testTrainer);

        // Act
        boolean result = trainerService.changePassword("Jane.Smith", "password123", "newpassword");

        // Assert
        assertTrue(result);
        verify(trainerRepository, times(2)).findByUsername("Jane.Smith");
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void testToggleActive_Success() {
        // Arrange
        when(trainerRepository.findByUsername("Jane.Smith")).thenReturn(Optional.of(testTrainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(testTrainer);

        // Act
        boolean result = trainerService.toggleActive("Jane.Smith", false);

        // Assert
        assertTrue(result);
        verify(trainerRepository).findByUsername("Jane.Smith");
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void testGetAllTrainers_Success() {
        // Arrange
        List<Trainer> trainers = List.of(testTrainer, new Trainer());
        when(trainerRepository.findAll()).thenReturn(trainers);

        // Act
        List<Trainer> result = trainerService.getAllTrainers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(trainerRepository).findAll();
    }
}
