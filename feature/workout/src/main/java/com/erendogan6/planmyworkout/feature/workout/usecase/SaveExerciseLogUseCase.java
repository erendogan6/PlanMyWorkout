package com.erendogan6.planmyworkout.feature.workout.usecase;

import com.erendogan6.planmyworkout.feature.workout.repository.WorkoutRepository;
import com.google.android.gms.tasks.Task;

import java.util.Date;

import javax.inject.Inject;

/**
 * Use case for saving exercise logs.
 */
public class SaveExerciseLogUseCase {
    private final WorkoutRepository workoutRepository;

    @Inject
    public SaveExerciseLogUseCase(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public Task<Void> execute(String planId, String exerciseId, double weight, int reps, String notes, Date timestamp) {
        return workoutRepository.saveExerciseLog(planId, exerciseId, weight, reps, notes, timestamp);
    }
}