package com.epam.springCoreTask.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trainings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    
    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "training_name")
    private String trainingName;
    @ManyToOne
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingType;
    @Column(name = "training_date")
    private LocalDate trainingDate;
    @Column(name = "training_duration")
    private int trainingDuration;
}
