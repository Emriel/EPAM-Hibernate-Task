package com.epam.springCoreTask.facade.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
import com.epam.springCoreTask.service.TraineeService;
import com.epam.springCoreTask.service.TrainerService;
import com.epam.springCoreTask.service.TrainingService;

@ExtendWith(MockitoExtension.class)
class GymFacadeImplTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private GymFacadeImpl gymFacade;

    private Trainee testTrainee;
    private Trainer testTrainer;
    private Training testTraining;
    private UUID traineeId;
    private UUID trainerId;
    private UUID trainingId;
    private TrainingType fitnessType;
    private TrainingType yogaType;

    @BeforeEach
    void setUp() {
        traineeId = UUID.randomUUID();
        trainerId = UUID.randomUUID();
        trainingId = UUID.randomUUID();
        fitnessType = new TrainingType("Fitness");
        yogaType = new TrainingType("Yoga");

        testTrainee = new Trainee();
        testTrainee.setFirstName("John");
        testTrainee.setLastName("Doe");
        testTrainee.setUsername("John.Doe");
        testTrainee.setPassword("password123");
        testTrainee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testTrainee.setAddress("123 Main St");
        testTrainee.setActive(true);

        testTrainer = new Trainer();
        testTrainer.setFirstName("Jane");
        testTrainer.setLastName("Smith");
        testTrainer.setUsername("Jane.Smith");
        testTrainer.setPassword("password456");
        testTrainer.setSpecialization("Fitness");
        testTrainer.setActive(true);

        testTraining = new Training(traineeId, trainerId, trainingId, "Morning Workout",
                fitnessType, LocalDate.now(), 60);
    }

    @Test
    void testCreateTraineeProfile_Success() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        String address = "123 Main St";

        when(traineeService.createTrainee(firstName, lastName, dateOfBirth, address))
                .thenReturn(testTrainee);

        // Act
        Trainee result = gymFacade.createTraineeProfile(firstName, lastName, dateOfBirth, address);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("John.Doe", result.getUsername());
        assertEquals(dateOfBirth, result.getDateOfBirth());
        assertEquals(address, result.getAddress());
        verify(traineeService).createTrainee(firstName, lastName, dateOfBirth, address);
    }

    @Test
    void testCreateTrainerProfile_Success() {
        // Arrange
        String firstName = "Jane";
        String lastName = "Smith";
        String specialization = "Fitness";

        when(trainerService.createTrainer(firstName, lastName, specialization))
                .thenReturn(testTrainer);

        // Act
        Trainer result = gymFacade.createTrainerProfile(firstName, lastName, specialization);

        // Assert
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("Jane.Smith", result.getUsername());
        assertEquals(specialization, result.getSpecialization());
        verify(trainerService).createTrainer(firstName, lastName, specialization);
    }

    @Test
    void testCreateTrainingSession_Success() {
        // Arrange
        String trainingName = "Morning Workout";
        TrainingType trainingType = fitnessType;
        LocalDate trainingDate = LocalDate.now();
        int trainingDuration = 60;

        when(traineeService.getTraineeById(traineeId)).thenReturn(testTrainee);
        when(trainerService.getTrainerById(trainerId)).thenReturn(testTrainer);
        when(trainingService.createTraining(traineeId, trainerId, trainingName, trainingType,
                trainingDate, trainingDuration)).thenReturn(testTraining);

        // Act
        Training result = gymFacade.createTrainingSession(traineeId, trainerId, trainingName,
                trainingType, trainingDate, trainingDuration);

        // Assert
        assertNotNull(result);
        assertEquals(trainingName, result.getTrainingName());
        assertEquals(trainingType.getName(), result.getTrainingType().getName());
        assertEquals(trainingDate, result.getTrainingDate());
        assertEquals(trainingDuration, result.getTrainingDuration());
        verify(traineeService).getTraineeById(traineeId);
        verify(trainerService).getTrainerById(trainerId);
        verify(trainingService).createTraining(traineeId, trainerId, trainingName, trainingType,
                trainingDate, trainingDuration);
    }

    @Test
    void testCreateTrainingSession_TraineeNotFound_ThrowsException() {
        // Arrange
        String trainingName = "Morning Workout";
        TrainingType trainingType = fitnessType;
        LocalDate trainingDate = LocalDate.now();
        int trainingDuration = 60;

        when(traineeService.getTraineeById(traineeId)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gymFacade.createTrainingSession(traineeId, trainerId, trainingName, trainingType,
                    trainingDate, trainingDuration);
        });

        assertEquals("Trainee not found with id: " + traineeId, exception.getMessage());
        verify(traineeService).getTraineeById(traineeId);
        verify(trainerService, never()).getTrainerById(any());
        verify(trainingService, never()).createTraining(any(), any(), any(), any(), any(), anyInt());
    }

    @Test
    void testCreateTrainingSession_TrainerNotFound_ThrowsException() {
        // Arrange
        String trainingName = "Morning Workout";
        TrainingType trainingType = fitnessType;
        LocalDate trainingDate = LocalDate.now();
        int trainingDuration = 60;

        when(traineeService.getTraineeById(traineeId)).thenReturn(testTrainee);
        when(trainerService.getTrainerById(trainerId)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gymFacade.createTrainingSession(traineeId, trainerId, trainingName, trainingType,
                    trainingDate, trainingDuration);
        });

        assertEquals("Trainer not found with id: " + trainerId, exception.getMessage());
        verify(traineeService).getTraineeById(traineeId);
        verify(trainerService).getTrainerById(trainerId);
        verify(trainingService, never()).createTraining(any(), any(), any(), any(), any(), anyInt());
    }

    @Test
    void testGetTraineeTrainings_Success() {
        // Arrange
        UUID anotherTraineeId = UUID.randomUUID();
        Training training1 = new Training(traineeId, trainerId, UUID.randomUUID(), "Session 1",
                fitnessType, LocalDate.now(), 60);
        Training training2 = new Training(traineeId, trainerId, UUID.randomUUID(), "Session 2",
                yogaType, LocalDate.now(), 45);
        Training training3 = new Training(anotherTraineeId, trainerId, UUID.randomUUID(), "Session 3",
                fitnessType, LocalDate.now(), 30);

        List<Training> allTrainings = Arrays.asList(training1, training2, training3);
        when(trainingService.getAllTrainings()).thenReturn(allTrainings);

        // Act
        List<Training> result = gymFacade.getTraineeTrainings(traineeId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(t -> t.getTraineeId().equals(traineeId)));
        verify(trainingService).getAllTrainings();
    }

    @Test
    void testGetTraineeTrainings_NoTrainings() {
        // Arrange
        when(trainingService.getAllTrainings()).thenReturn(Arrays.asList());

        // Act
        List<Training> result = gymFacade.getTraineeTrainings(traineeId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(trainingService).getAllTrainings();
    }

    @Test
    void testGetTrainerTrainings_Success() {
        // Arrange
        UUID anotherTrainerId = UUID.randomUUID();
        Training training1 = new Training(traineeId, trainerId, UUID.randomUUID(), "Session 1",
                fitnessType, LocalDate.now(), 60);
        Training training2 = new Training(traineeId, trainerId, UUID.randomUUID(), "Session 2",
                yogaType, LocalDate.now(), 45);
        Training training3 = new Training(traineeId, anotherTrainerId, UUID.randomUUID(), "Session 3",
                fitnessType, LocalDate.now(), 30);

        List<Training> allTrainings = Arrays.asList(training1, training2, training3);
        when(trainingService.getAllTrainings()).thenReturn(allTrainings);

        // Act
        List<Training> result = gymFacade.getTrainerTrainings(trainerId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(t -> t.getTrainerId().equals(trainerId)));
        verify(trainingService).getAllTrainings();
    }

    @Test
    void testGetTrainerTrainings_NoTrainings() {
        // Arrange
        when(trainingService.getAllTrainings()).thenReturn(Arrays.asList());

        // Act
        List<Training> result = gymFacade.getTrainerTrainings(trainerId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(trainingService).getAllTrainings();
    }

    @Test
    void testUpdateTraineeProfile_Success() {
        // Arrange
        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setFirstName("John");
        updatedTrainee.setLastName("Doe");
        updatedTrainee.setAddress("456 New Address");

        when(traineeService.updateTrainee(testTrainee)).thenReturn(updatedTrainee);

        // Act
        Trainee result = gymFacade.updateTraineeProfile(testTrainee);

        // Assert
        assertNotNull(result);
        assertEquals("456 New Address", result.getAddress());
        verify(traineeService).updateTrainee(testTrainee);
    }

    @Test
    void testUpdateTrainerProfile_Success() {
        // Arrange
        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setFirstName("Jane");
        updatedTrainer.setLastName("Smith");
        updatedTrainer.setSpecialization("Yoga");

        when(trainerService.updateTrainer(testTrainer)).thenReturn(updatedTrainer);

        // Act
        Trainer result = gymFacade.updateTrainerProfile(testTrainer);

        // Assert
        assertNotNull(result);
        assertEquals("Yoga", result.getSpecialization());
        verify(trainerService).updateTrainer(testTrainer);
    }

    @Test
    void testDeleteTraineeProfile_Success() {
        // Arrange
        when(traineeService.getTraineeById(traineeId)).thenReturn(testTrainee);

        // Act
        gymFacade.deleteTraineeProfile(traineeId);

        // Assert
        verify(traineeService).getTraineeById(traineeId);
        verify(traineeService).deleteTrainee(testTrainee);
    }

    @Test
    void testDeleteTraineeProfile_TraineeNotFound_ShouldNotDelete() {
        // Arrange
        when(traineeService.getTraineeById(traineeId)).thenReturn(null);

        // Act
        gymFacade.deleteTraineeProfile(traineeId);

        // Assert
        verify(traineeService).getTraineeById(traineeId);
        verify(traineeService, never()).deleteTrainee(any());
    }

    @Test
    void testGetTraineeById_Success() {
        // Arrange
        when(traineeService.getTraineeById(traineeId)).thenReturn(testTrainee);

        // Act
        Trainee result = gymFacade.getTraineeById(traineeId);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(traineeService).getTraineeById(traineeId);
    }

    @Test
    void testGetTraineeById_NotFound() {
        // Arrange
        when(traineeService.getTraineeById(traineeId)).thenReturn(null);

        // Act
        Trainee result = gymFacade.getTraineeById(traineeId);

        // Assert
        assertNull(result);
        verify(traineeService).getTraineeById(traineeId);
    }

    @Test
    void testGetTrainerById_Success() {
        // Arrange
        when(trainerService.getTrainerById(trainerId)).thenReturn(testTrainer);

        // Act
        Trainer result = gymFacade.getTrainerById(trainerId);

        // Assert
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        verify(trainerService).getTrainerById(trainerId);
    }

    @Test
    void testGetTrainerById_NotFound() {
        // Arrange
        when(trainerService.getTrainerById(trainerId)).thenReturn(null);

        // Act
        Trainer result = gymFacade.getTrainerById(trainerId);

        // Assert
        assertNull(result);
        verify(trainerService).getTrainerById(trainerId);
    }

    @Test
    void testGetTrainingById_Success() {
        // Arrange
        when(trainingService.getTrainingById(trainingId)).thenReturn(testTraining);

        // Act
        Training result = gymFacade.getTrainingById(trainingId);

        // Assert
        assertNotNull(result);
        assertEquals("Morning Workout", result.getTrainingName());
        assertEquals("Fitness", result.getTrainingType().getName());
        verify(trainingService).getTrainingById(trainingId);
    }

    @Test
    void testGetTrainingById_NotFound() {
        // Arrange
        when(trainingService.getTrainingById(trainingId)).thenReturn(null);

        // Act
        Training result = gymFacade.getTrainingById(trainingId);

        // Assert
        assertNull(result);
        verify(trainingService).getTrainingById(trainingId);
    }

    @Test
    void testGetAllTrainees_Success() {
        // Arrange
        Trainee trainee1 = new Trainee();
        trainee1.setFirstName("John");
        trainee1.setLastName("Doe");

        Trainee trainee2 = new Trainee();
        trainee2.setFirstName("Alice");
        trainee2.setLastName("Johnson");

        List<Trainee> trainees = Arrays.asList(trainee1, trainee2);
        when(traineeService.getAllTrainees()).thenReturn(trainees);

        // Act
        List<Trainee> result = gymFacade.getAllTrainees();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(traineeService).getAllTrainees();
    }

    @Test
    void testGetAllTrainees_EmptyList() {
        // Arrange
        when(traineeService.getAllTrainees()).thenReturn(Arrays.asList());

        // Act
        List<Trainee> result = gymFacade.getAllTrainees();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(traineeService).getAllTrainees();
    }

    @Test
    void testGetAllTrainers_Success() {
        // Arrange
        Trainer trainer1 = new Trainer();
        trainer1.setFirstName("Jane");
        trainer1.setLastName("Smith");

        Trainer trainer2 = new Trainer();
        trainer2.setFirstName("Bob");
        trainer2.setLastName("Brown");

        List<Trainer> trainers = Arrays.asList(trainer1, trainer2);
        when(trainerService.getAllTrainers()).thenReturn(trainers);

        // Act
        List<Trainer> result = gymFacade.getAllTrainers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(trainerService).getAllTrainers();
    }

    @Test
    void testGetAllTrainers_EmptyList() {
        // Arrange
        when(trainerService.getAllTrainers()).thenReturn(Arrays.asList());

        // Act
        List<Trainer> result = gymFacade.getAllTrainers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(trainerService).getAllTrainers();
    }

    @Test
    void testGetAllTrainings_Success() {
        // Arrange
        Training training1 = new Training(traineeId, trainerId, UUID.randomUUID(), "Session 1",
                fitnessType, LocalDate.now(), 60);
        Training training2 = new Training(traineeId, trainerId, UUID.randomUUID(), "Session 2",
                yogaType, LocalDate.now(), 45);

        List<Training> trainings = Arrays.asList(training1, training2);
        when(trainingService.getAllTrainings()).thenReturn(trainings);

        // Act
        List<Training> result = gymFacade.getAllTrainings();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(trainingService).getAllTrainings();
    }

    @Test
    void testGetAllTrainings_EmptyList() {
        // Arrange
        when(trainingService.getAllTrainings()).thenReturn(Arrays.asList());

        // Act
        List<Training> result = gymFacade.getAllTrainings();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(trainingService).getAllTrainings();
    }
}
