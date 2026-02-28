package com.epam.springCoreTask.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.epam.springCoreTask.model.Training;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findByTrainee_User_Username(String traineeUsername);

    List<Training> findByTrainer_User_Username(String trainerUsername);

    @Query("SELECT t FROM Training t WHERE t.trainee.user.username = :traineeUsername " +
           "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
           "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
           "AND (:trainerName IS NULL OR t.trainer.user.firstName LIKE %:trainerName% OR t.trainer.user.lastName LIKE %:trainerName%) " +
           "AND (:trainingTypeName IS NULL OR t.trainingType.name = :trainingTypeName)")
    List<Training> findTraineeTrainings(@Param("traineeUsername") String traineeUsername,
                                       @Param("fromDate") LocalDate fromDate,
                                       @Param("toDate") LocalDate toDate,
                                       @Param("trainerName") String trainerName,
                                       @Param("trainingTypeName") String trainingTypeName);

    @Query("SELECT t FROM Training t WHERE t.trainer.user.username = :trainerUsername " +
           "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
           "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
           "AND (:traineeName IS NULL OR t.trainee.user.firstName LIKE %:traineeName% OR t.trainee.user.lastName LIKE %:traineeName%)")
    List<Training> findTrainerTrainings(@Param("trainerUsername") String trainerUsername,
                                       @Param("fromDate") LocalDate fromDate,
                                       @Param("toDate") LocalDate toDate,
                                       @Param("traineeName") String traineeName);
}
