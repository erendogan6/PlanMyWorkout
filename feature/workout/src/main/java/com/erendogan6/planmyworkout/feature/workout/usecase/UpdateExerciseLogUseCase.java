package com.erendogan6.planmyworkout.feature.workout.usecase;

import com.erendogan6.planmyworkout.feature.workout.repository.WorkoutRepository;
import com.google.android.gms.tasks.Task;

import java.util.Date;

import javax.inject.Inject;

/**
 * Use case for updating exercise logs.
 */
public class UpdateExerciseLogUseCase {
    private final WorkoutRepository workoutRepository;

    @Inject
    public UpdateExerciseLogUseCase(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public Task<Void> execute(String planId, String exerciseId, String logId, double weight, int reps, String notes, Date timestamp) {
        return workoutRepository.updateExerciseLog(planId, exerciseId, logId, weight, reps, notes, timestamp);
    }
}