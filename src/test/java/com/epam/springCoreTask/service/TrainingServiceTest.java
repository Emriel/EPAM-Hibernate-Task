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

import com.epam.springCoreTask.dao.TrainingDAO;
import com.epam.springCoreTask.model.Training;
import com.epam.springCoreTask.model.TrainingType;
import com.epam.springCoreTask.service.impl.TrainingServiceImpl;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDAO trainingDAO;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Training testTraining;
    private UUID testTrainingId;
    private UUID testTraineeId;
    private UUID testTrainerId;
    private TrainingType testTrainingType;

    @BeforeEach
    void setUp() {
        testTrainingId = UUID.randomUUID();
        testTraineeId = UUID.randomUUID();
        testTrainerId = UUID.randomUUID();

        testTrainingType = new TrainingType();
        testTrainingType.setName("Cardio");

        testTraining = new Training();
        testTraining.setTrainingId(testTrainingId);
        testTraining.setTraineeId(testTraineeId);
        testTraining.setTrainerId(testTrainerId);
        testTraining.setTrainingName("Morning Workout");
        testTraining.setTrainingType(testTrainingType);
        testTraining.setTrainingDate(LocalDate.now());
        testTraining.setTrainingDuration(60);
    }

    @Test
    void testCreateTraining_Success() {
        // Arrange
        String trainingName = "Morning Workout";
        LocalDate trainingDate = LocalDate.now();
        int trainingDuration = 60;

        when(trainingDAO.save(any(Training.class))).thenReturn(testTraining);

        // Act
        Training result = trainingService.createTraining(testTraineeId, testTrainerId,
                trainingName, testTrainingType, trainingDate, trainingDuration);

        // Assert
        assertNotNull(result);
        assertEquals(trainingName, result.getTrainingName());
        assertEquals(testTraineeId, result.getTraineeId());
        assertEquals(testTrainerId, result.getTrainerId());
        assertEquals(trainingDuration, result.getTrainingDuration());
        verify(trainingDAO).save(any(Training.class));
    }

    @Test
    void testCreateTraining_GeneratesTrainingId() {
        // Arrange
        when(trainingDAO.save(any(Training.class))).thenAnswer(invocation -> {
            Training training = invocation.getArgument(0);
            assertNotNull(training.getTrainingId());
            return training;
        });

        // Act
        Training result = trainingService.createTraining(testTraineeId, testTrainerId,
                "Test Training", testTrainingType, LocalDate.now(), 30);

        // Assert
        assertNotNull(result);
        verify(trainingDAO).save(any(Training.class));
    }

    @Test
    void testCreateTraining_WithAllFields() {
        // Arrange
        String trainingName = "Evening Run";
        LocalDate trainingDate = LocalDate.of(2026, 3, 15);
        int trainingDuration = 45;

        when(trainingDAO.save(any(Training.class))).thenReturn(testTraining);

        // Act
        Training result = trainingService.createTraining(testTraineeId, testTrainerId,
                trainingName, testTrainingType, trainingDate, trainingDuration);

        // Assert
        assertNotNull(result);
        verify(trainingDAO).save(argThat(training -> training.getTraineeId().equals(testTraineeId) &&
                training.getTrainerId().equals(testTrainerId) &&
                training.getTrainingName().equals(trainingName) &&
                training.getTrainingType().equals(testTrainingType) &&
                training.getTrainingDate().equals(trainingDate) &&
                training.getTrainingDuration() == trainingDuration));
    }

    @Test
    void testGetTrainingById_Found() {
        // Arrange
        when(trainingDAO.findById(testTrainingId)).thenReturn(testTraining);

        // Act
        Training result = trainingService.getTrainingById(testTrainingId);

        // Assert
        assertNotNull(result);
        assertEquals(testTrainingId, result.getTrainingId());
        assertEquals("Morning Workout", result.getTrainingName());
        verify(trainingDAO).findById(testTrainingId);
    }

    @Test
    void testGetTrainingById_NotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(trainingDAO.findById(nonExistentId)).thenReturn(null);

        // Act
        Training result = trainingService.getTrainingById(nonExistentId);

        // Assert
        assertNull(result);
        verify(trainingDAO).findById(nonExistentId);
    }

    @Test
    void testGetAllTrainings_Success() {
        // Arrange
        Training training2 = new Training();
        training2.setTrainingId(UUID.randomUUID());
        training2.setTrainingName("Afternoon Session");

        List<Training> trainings = List.of(testTraining, training2);
        when(trainingDAO.findAll()).thenReturn(trainings);

        // Act
        List<Training> result = trainingService.getAllTrainings();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(trainingDAO).findAll();
    }

    @Test
    void testGetAllTrainings_EmptyList() {
        // Arrange
        when(trainingDAO.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Training> result = trainingService.getAllTrainings();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(trainingDAO).findAll();
    }

    @Test
    void testCreateTraining_VerifyAllFieldsSet() {
        // Arrange
        when(trainingDAO.save(any(Training.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Training result = trainingService.createTraining(
                testTraineeId,
                testTrainerId,
                "Test Training",
                testTrainingType,
                LocalDate.of(2026, 2, 10),
                90);

        // Assert
        assertNotNull(result.getTrainingId());
        assertEquals(testTraineeId, result.getTraineeId());
        assertEquals(testTrainerId, result.getTrainerId());
        assertEquals("Test Training", result.getTrainingName());
        assertEquals(testTrainingType, result.getTrainingType());
        assertEquals(LocalDate.of(2026, 2, 10), result.getTrainingDate());
        assertEquals(90, result.getTrainingDuration());
    }
}
