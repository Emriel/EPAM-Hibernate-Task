package com.epam.springCoreTask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.springCoreTask.dao.TraineeDAO;
import com.epam.springCoreTask.model.Trainee;
import com.epam.springCoreTask.service.impl.TraineeServiceImpl;
import com.epam.springCoreTask.util.PasswordGenerator;
import com.epam.springCoreTask.util.UsernameGenerator;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private UsernameGenerator usernameGenerator;

    @Mock
    private PasswordGenerator passwordGenerator;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee testTrainee;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testTrainee = new Trainee();
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

        when(traineeDAO.findAllUsernames()).thenReturn(List.of());
        when(usernameGenerator.generateUsername(eq(firstName), eq(lastName), anyList())).thenReturn("John.Doe");
        when(passwordGenerator.generatePassword()).thenReturn("password123");
        when(traineeDAO.save(any(Trainee.class))).thenReturn(testTrainee);

        // Act
        Trainee result = traineeService.createTrainee(firstName, lastName, dateOfBirth, address);

        // Assert
        assertNotNull(result);
        assertEquals("John.Doe", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertTrue(result.isActive());
        verify(traineeDAO).findAllUsernames();
        verify(usernameGenerator).generateUsername(eq(firstName), eq(lastName), anyList());
        verify(passwordGenerator).generatePassword();
        verify(traineeDAO).save(any(Trainee.class));
    }

    @Test
    void testCreateTrainee_WithExistingUsername() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        String address = "123 Main St";

        when(traineeDAO.findAllUsernames()).thenReturn(List.of("John.Doe"));
        when(usernameGenerator.generateUsername(eq(firstName), eq(lastName), anyList())).thenReturn("John.Doe1");
        when(passwordGenerator.generatePassword()).thenReturn("password123");
        when(traineeDAO.save(any(Trainee.class))).thenReturn(testTrainee);

        // Act
        Trainee result = traineeService.createTrainee(firstName, lastName, dateOfBirth, address);

        // Assert
        assertNotNull(result);
        verify(usernameGenerator).generateUsername(eq(firstName), eq(lastName), anyList());
        verify(traineeDAO).save(any(Trainee.class));
    }

    @Test
    void testUpdateTrainee_Success() {
        // Arrange
        when(traineeDAO.save(testTrainee)).thenReturn(testTrainee);

        // Act
        Trainee result = traineeService.updateTrainee(testTrainee);

        // Assert
        assertNotNull(result);
        assertEquals(testTrainee.getUsername(), result.getUsername());
        verify(traineeDAO).save(testTrainee);
    }

    @Test
    void testDeleteTrainee_Success() {
        // Arrange
        when(traineeDAO.delete(testTrainee.getUserId())).thenReturn(true);

        // Act
        traineeService.deleteTrainee(testTrainee);

        // Assert
        verify(traineeDAO).delete(testTrainee.getUserId());
    }

    @Test
    void testDeleteTrainee_NotFound() {
        // Arrange
        when(traineeDAO.delete(testTrainee.getUserId())).thenReturn(false);

        // Act
        traineeService.deleteTrainee(testTrainee);

        // Assert
        verify(traineeDAO).delete(testTrainee.getUserId());
    }

    @Test
    void testGetTraineeById_Found() {
        // Arrange
        when(traineeDAO.findById(testId)).thenReturn(testTrainee);

        // Act
        Trainee result = traineeService.getTraineeById(testId);

        // Assert
        assertNotNull(result);
        assertEquals(testTrainee.getUsername(), result.getUsername());
        verify(traineeDAO).findById(testId);
    }

    @Test
    void testGetTraineeById_NotFound() {
        // Arrange
        when(traineeDAO.findById(testId)).thenReturn(null);

        // Act
        Trainee result = traineeService.getTraineeById(testId);

        // Assert
        assertNull(result);
        verify(traineeDAO).findById(testId);
    }

    @Test
    void testGetAllTrainees_Success() {
        // Arrange
        List<Trainee> trainees = List.of(testTrainee, new Trainee());
        when(traineeDAO.findAll()).thenReturn(trainees);

        // Act
        List<Trainee> result = traineeService.getAllTrainees();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(traineeDAO).findAll();
    }

    @Test
    void testGetAllTrainees_EmptyList() {
        // Arrange
        when(traineeDAO.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Trainee> result = traineeService.getAllTrainees();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(traineeDAO).findAll();
    }
}
