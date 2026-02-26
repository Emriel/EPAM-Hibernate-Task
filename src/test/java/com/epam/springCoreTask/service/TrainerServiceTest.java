package com.epam.springCoreTask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.springCoreTask.dao.TrainerDAO;
import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.service.impl.TrainerServiceImpl;
import com.epam.springCoreTask.util.PasswordGenerator;
import com.epam.springCoreTask.util.UsernameGenerator;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private UsernameGenerator usernameGenerator;

    @Mock
    private PasswordGenerator passwordGenerator;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer testTrainer;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testTrainer = new Trainer();
        testTrainer.setFirstName("Jane");
        testTrainer.setLastName("Smith");
        testTrainer.setUsername("Jane.Smith");
        testTrainer.setPassword("password123");
        testTrainer.setSpecialization("Yoga");
        testTrainer.setActive(true);
    }

    @Test
    void testCreateTrainer_Success() {
        // Arrange
        String firstName = "Jane";
        String lastName = "Smith";
        String specialization = "Yoga";

        when(trainerDAO.findAllUsernames()).thenReturn(List.of());
        when(usernameGenerator.generateUsername(eq(firstName), eq(lastName), anyList())).thenReturn("Jane.Smith");
        when(passwordGenerator.generatePassword()).thenReturn("password123");
        when(trainerDAO.save(any(Trainer.class))).thenReturn(testTrainer);

        // Act
        Trainer result = trainerService.createTrainer(firstName, lastName, specialization);

        // Assert
        assertNotNull(result);
        assertEquals("Jane.Smith", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertEquals("Yoga", result.getSpecialization());
        assertTrue(result.isActive());
        verify(trainerDAO).findAllUsernames();
        verify(usernameGenerator).generateUsername(eq(firstName), eq(lastName), anyList());
        verify(passwordGenerator).generatePassword();
        verify(trainerDAO).save(any(Trainer.class));
    }

    @Test
    void testCreateTrainer_WithExistingUsername() {
        // Arrange
        String firstName = "Jane";
        String lastName = "Smith";
        String specialization = "Yoga";

        when(trainerDAO.findAllUsernames()).thenReturn(List.of("Jane.Smith"));
        when(usernameGenerator.generateUsername(eq(firstName), eq(lastName), anyList())).thenReturn("Jane.Smith1");
        when(passwordGenerator.generatePassword()).thenReturn("password123");
        when(trainerDAO.save(any(Trainer.class))).thenReturn(testTrainer);

        // Act
        Trainer result = trainerService.createTrainer(firstName, lastName, specialization);

        // Assert
        assertNotNull(result);
        verify(usernameGenerator).generateUsername(eq(firstName), eq(lastName), anyList());
        verify(trainerDAO).save(any(Trainer.class));
    }

    @Test
    void testUpdateTrainer_Success() {
        // Arrange
        when(trainerDAO.save(testTrainer)).thenReturn(testTrainer);

        // Act
        Trainer result = trainerService.updateTrainer(testTrainer);

        // Assert
        assertNotNull(result);
        assertEquals(testTrainer.getUsername(), result.getUsername());
        assertEquals(testTrainer.getSpecialization(), result.getSpecialization());
        verify(trainerDAO).save(testTrainer);
    }

    @Test
    void testGetTrainerById_Found() {
        // Arrange
        when(trainerDAO.findById(testId)).thenReturn(testTrainer);

        // Act
        Trainer result = trainerService.getTrainerById(testId);

        // Assert
        assertNotNull(result);
        assertEquals(testTrainer.getUsername(), result.getUsername());
        assertEquals(testTrainer.getSpecialization(), result.getSpecialization());
        verify(trainerDAO).findById(testId);
    }

    @Test
    void testGetTrainerById_NotFound() {
        // Arrange
        when(trainerDAO.findById(testId)).thenReturn(null);

        // Act
        Trainer result = trainerService.getTrainerById(testId);

        // Assert
        assertNull(result);
        verify(trainerDAO).findById(testId);
    }

    @Test
    void testGetAllTrainers_Success() {
        // Arrange
        List<Trainer> trainers = List.of(testTrainer, new Trainer());
        when(trainerDAO.findAll()).thenReturn(trainers);

        // Act
        List<Trainer> result = trainerService.getAllTrainers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(trainerDAO).findAll();
    }

    @Test
    void testGetAllTrainers_EmptyList() {
        // Arrange
        when(trainerDAO.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Trainer> result = trainerService.getAllTrainers();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(trainerDAO).findAll();
    }

    @Test
    void testCreateTrainer_GeneratesUsernameAndPassword() {
        // Arrange
        String firstName = "Mike";
        String lastName = "Johnson";
        String specialization = "Boxing";

        when(trainerDAO.findAllUsernames()).thenReturn(List.of());
        when(usernameGenerator.generateUsername(eq(firstName), eq(lastName), anyList())).thenReturn("Mike.Johnson");
        when(passwordGenerator.generatePassword()).thenReturn("abcd123456");
        when(trainerDAO.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Trainer result = trainerService.createTrainer(firstName, lastName, specialization);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getUsername());
        assertNotNull(result.getPassword());
        verify(passwordGenerator).generatePassword();
    }
}
