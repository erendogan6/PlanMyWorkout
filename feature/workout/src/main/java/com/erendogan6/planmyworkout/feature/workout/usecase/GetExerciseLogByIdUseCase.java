package com.erendogan6.planmyworkout.feature.workout.usecase;

import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLog;
import com.erendogan6.planmyworkout.feature.workout.repository.WorkoutRepository;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

/**
 * Use case for getting a specific exercise log by ID.
 */
public class GetExerciseLogByIdUseCase {
    private final WorkoutRepository workoutRepository;

    @Inject
    public GetExerciseLogByIdUseCase(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public Task<ExerciseLog> execute(String planId, String exerciseId, String logId) {
        return workoutRepository.getExerciseLogById(planId, exerciseId, logId);
    }
}