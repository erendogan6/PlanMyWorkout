package com.erendogan6.planmyworkout.feature.workout.usecase;

import com.erendogan6.planmyworkout.feature.workout.repository.WorkoutRepository;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Use case for updating an existing exercise log.
 */
@Singleton
public class UpdateExerciseLogUseCase {

    private final WorkoutRepository repository;

    @Inject
    public UpdateExerciseLogUseCase(WorkoutRepository repository) {
        this.repository = repository;
    }

    /**
     * Execute the use case to update an exercise log.
     *
     * @param planId The plan ID
     * @param exerciseId The exercise ID
     * @param logId The log ID
     * @param weight The weight used
     * @param reps The number of reps completed
     * @param notes Optional notes about the exercise
     * @return Task indicating success or failure
     */
    public Task<Void> execute(String planId, String exerciseId, String logId, double weight, int reps, String notes) {
        return repository.updateExerciseLog(planId, exerciseId, logId, weight, reps, notes);
    }
}
