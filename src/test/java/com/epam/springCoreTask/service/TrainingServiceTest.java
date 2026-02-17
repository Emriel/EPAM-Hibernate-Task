package com.epam.springCoreTask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.springCoreTask.model.Trainee;
import com.epam.springCoreTask.model.Trainer;
import com.epam.springCoreTask.model.Training;
import com.epam.springCoreTask.model.TrainingType;
import com.epam.springCoreTask.repository.TrainingRepository;
import com.epam.springCoreTask.service.impl.TrainingServiceImpl;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Training testTraining;
    private Trainee testTrainee;
    private Trainer testTrainer;
    private TrainingType testTrainingType;
    private Long testId;

    @BeforeEach
    void setUp() {
        testId = 1L;
        
        testTrainee = new Trainee();
        testTrainee.setId(1L);
        testTrainee.setUsername("john.doe");
        
        testTrainer = new Trainer();
        testTrainer.setId(2L);
        testTrainer.setUsername("jane.smith");
        
        testTrainingType = new TrainingType();
        testTrainingType.setId(1L);
        testTrainingType.setName("Cardio");
        
        testTraining = new Training();
        testTraining.setId(testId);
        testTraining.setTrainee(testTrainee);
        testTraining.setTrainer(testTrainer);
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
        
        when(trainingRepository.save(any(Training.class))).thenReturn(testTraining);

        // Act
        Training result = trainingService.createTraining(testTrainee, testTrainer, 
                trainingName, testTrainingType, trainingDate, trainingDuration);

        // Assert
        assertNotNull(result);
        assertEquals(trainingName, result.getTrainingName());
        assertEquals(testTrainee, result.getTrainee());
        assertEquals(testTrainer, result.getTrainer());
        assertEquals(trainingDuration, result.getTrainingDuration());
        verify(trainingRepository).save(any(Training.class));
    }

    @Test
    void testGetTrainingById_Found() {
        // Arrange
        when(trainingRepository.findById(testId)).thenReturn(Optional.of(testTraining));

        // Act
        Training result = trainingService.getTrainingById(testId);

        // Assert
assertNotNull(result);
        assertEquals(testTraining.getTrainingName(), result.getTrainingName());
        verify(trainingRepository).findById(testId);
    }

    @Test
    void testGetTrainingById_NotFound() {
        // Arrange
        when(trainingRepository.findById(testId)).thenReturn(Optional.empty());

        // Act
        Training result = trainingService.getTrainingById(testId);

        // Assert
        assertNull(result);
        verify(trainingRepository).findById(testId);
    }

    @Test
    void testGetAllTrainings_Success() {
        // Arrange
        List<Training> trainings = List.of(testTraining, new Training());
        when(trainingRepository.findAll()).thenReturn(trainings);

        // Act
        List<Training> result = trainingService.getAllTrainings();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(trainingRepository).findAll();
    }

    @Test
    void testGetTraineeTrainings_Success() {
        // Arrange
        List<Training> trainings = List.of(testTraining);
        when(trainingRepository.findTraineeTrainings(anyString(), any(), any(), any(), any())).thenReturn(trainings);

        // Act
        List<Training> result = trainingService.getTraineeTrainings("john.doe", null, null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(trainingRepository).findTraineeTrainings(anyString(), any(), any(), any(), any());
    }

    @Test
    void testGetTrainerTrainings_Success() {
        // Arrange
        List<Training> trainings = List.of(testTraining);
        when(trainingRepository.findTrainerTrainings(anyString(), any(), any(), any())).thenReturn(trainings);

        // Act
        List<Training> result = trainingService.getTrainerTrainings("jane.smith", null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(trainingRepository).findTrainerTrainings(anyString(), any(), any(), any());
    }
}
