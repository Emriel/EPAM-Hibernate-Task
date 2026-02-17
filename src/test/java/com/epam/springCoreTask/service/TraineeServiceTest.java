package com.epam.springCoreTask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.springCoreTask.model.Trainee;
import com.epam.springCoreTask.repository.TraineeRepository;
import com.epam.springCoreTask.repository.TrainerRepository;
import com.epam.springCoreTask.service.impl.TraineeServiceImpl;
import com.epam.springCoreTask.util.PasswordGenerator;
import com.epam.springCoreTask.util.UsernameGenerator;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UsernameGenerator usernameGenerator;

    @Mock
    private PasswordGenerator passwordGenerator;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee testTrainee;
    private Long testId;

    @BeforeEach
    void setUp() {
        testId = 1L;
        testTrainee = new Trainee();
        testTrainee.setId(testId);
        testTrainee.setFirstName("John");
        testTrainee.setLastName("Doe");
        testTrainee.setUsername("John.Doe");
        testTrainee.setPassword("password123");
        testTrainee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testTrainee.setAddress("123 Main St");
        testTrainee.setActive(true);
    }

    @Test
    void testCreateTrainee_Success() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        String address = "123 Main St";
        
        List<Trainee> existingTrainees = new ArrayList<>();
        when(traineeRepository.findAll()).thenReturn(existingTrainees);
        when(usernameGenerator.generateUsername(eq(firstName), eq(lastName), anyList())).thenReturn("John.Doe");
        when(passwordGenerator.generatePassword()).thenReturn("password123");
        when(traineeRepository.save(any(Trainee.class))).thenReturn(testTrainee);

        // Act
        Trainee result = traineeService.createTrainee(firstName, lastName, dateOfBirth, address);

        // Assert
        assertNotNull(result);
        assertEquals("John.Doe", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertTrue(result.isActive());
        verify(traineeRepository).findAll();
        verify(usernameGenerator).generateUsername(eq(firstName), eq(lastName), anyList());
        verify(passwordGenerator).generatePassword();
        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void testCreateTrainee_WithExistingUsername() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        String address = "123 Main St";
        
        Trainee existingTrainee = new Trainee();
        existingTrainee.setUsername("John.Doe");
        List<Trainee> existingTrainees = List.of(existingTrainee);
        
        when(traineeRepository.findAll()).thenReturn(existingTrainees);
        when(usernameGenerator.generateUsername(eq(firstName), eq(lastName), anyList())).thenReturn("John.Doe1");
        when(passwordGenerator.generatePassword()).thenReturn("password123");
        when(traineeRepository.save(any(Trainee.class))).thenReturn(testTrainee);

        // Act
        Trainee result = traineeService.createTrainee(firstName, lastName, dateOfBirth, address);

        // Assert
        assertNotNull(result);
        verify(usernameGenerator).generateUsername(eq(firstName), eq(lastName), anyList());
        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void testUpdateTrainee_Success() {
        // Arrange
        when(traineeRepository.save(testTrainee)).thenReturn(testTrainee);

        // Act
        Trainee result = traineeService.updateTrainee(testTrainee);

        // Assert
        assertNotNull(result);
        assertEquals(testTrainee.getUsername(), result.getUsername());
        verify(traineeRepository).save(testTrainee);
    }

    @Test
    void testGetTraineeByUsername_Found() {
        // Arrange
        when(traineeRepository.findByUsername("John.Doe")).thenReturn(Optional.of(testTrainee));

        // Act
        Trainee result = traineeService.getTraineeByUsername("John.Doe");

        // Assert
        assertNotNull(result);
        assertEquals(testTrainee.getUsername(), result.getUsername());
        verify(traineeRepository).findByUsername("John.Doe");
    }

    @Test
    void testGetTraineeByUsername_NotFound() {
        // Arrange
        when(traineeRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act
        Trainee result = traineeService.getTraineeByUsername("nonexistent");

        // Assert
        assertNull(result);
        verify(traineeRepository).findByUsername("nonexistent");
    }

    @Test
    void testAuthenticateTrainee_Success() {
        // Arrange
        when(traineeRepository.findByUsername("John.Doe")).thenReturn(Optional.of(testTrainee));

        // Act
        boolean result = traineeService.authenticateTrainee("John.Doe", "password123");

        // Assert
        assertTrue(result);
        verify(traineeRepository).findByUsername("John.Doe");
    }

    @Test
    void testAuthenticateTrainee_WrongPassword() {
        // Arrange
        when(traineeRepository.findByUsername("John.Doe")).thenReturn(Optional.of(testTrainee));

        // Act
        boolean result = traineeService.authenticateTrainee("John.Doe", "wrongpassword");

        // Assert
        assertFalse(result);
        verify(traineeRepository).findByUsername("John.Doe");
    }

    @Test
    void testAuthenticateTrainee_UserNotFound() {
        // Arrange
        when(traineeRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act
        boolean result = traineeService.authenticateTrainee("nonexistent", "password123");

        // Assert
        assertFalse(result);
        verify(traineeRepository).findByUsername("nonexistent");
    }

    @Test
    void testChangePassword_Success() {
        // Arrange
        when(traineeRepository.findByUsername("John.Doe")).thenReturn(Optional.of(testTrainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(testTrainee);

        // Act
        boolean result = traineeService.changePassword("John.Doe", "password123", "newpassword");

        // Assert
        assertTrue(result);
        verify(traineeRepository, times(2)).findByUsername("John.Doe");
        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void testChangePassword_WrongOldPassword() {
        // Arrange
        when(traineeRepository.findByUsername("John.Doe")).thenReturn(Optional.of(testTrainee));

        // Act
        boolean result = traineeService.changePassword("John.Doe", "wrongpassword", "newpassword");

        // Assert
        assertFalse(result);
        verify(traineeRepository).findByUsername("John.Doe");
        verify(traineeRepository, never()).save(any(Trainee.class));
    }

    @Test
    void testToggleActive_Success() {
        // Arrange
        when(traineeRepository.findByUsername("John.Doe")).thenReturn(Optional.of(testTrainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(testTrainee);

        // Act
        boolean result = traineeService.toggleActive("John.Doe", false);

        // Assert
        assertTrue(result);
        verify(traineeRepository).findByUsername("John.Doe");
        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void testDeleteByUsername_Success() {
        // Arrange
        when(traineeRepository.existsByUsername("John.Doe")).thenReturn(true);

        // Act
        boolean result = traineeService.deleteByUsername("John.Doe");

        // Assert
        assertTrue(result);
        verify(traineeRepository).existsByUsername("John.Doe");
        verify(traineeRepository).deleteByUsername("John.Doe");
    }

    @Test
    void testDeleteByUsername_NotFound() {
        // Arrange
        when(traineeRepository.existsByUsername("nonexistent")).thenReturn(false);

        // Act
        boolean result = traineeService.deleteByUsername("nonexistent");

        // Assert
        assertFalse(result);
        verify(traineeRepository).existsByUsername("nonexistent");
        verify(traineeRepository, never()).deleteByUsername(anyString());
    }

    @Test
    void testGetAllTrainees_Success() {
        // Arrange
        List<Trainee> trainees = List.of(testTrainee, new Trainee());
        when(traineeRepository.findAll()).thenReturn(trainees);

        // Act
        List<Trainee> result = traineeService.getAllTrainees();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(traineeRepository).findAll();
    }

    @Test
    void testGetAllTrainees_EmptyList() {
        // Arrange
        when(traineeRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Trainee> result = traineeService.getAllTrainees();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(traineeRepository).findAll();
    }
}
