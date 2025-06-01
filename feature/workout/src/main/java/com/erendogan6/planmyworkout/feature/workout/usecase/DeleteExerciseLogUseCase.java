package com.erendogan6.planmyworkout.feature.workout.usecase;

import com.erendogan6.planmyworkout.feature.workout.repository.WorkoutRepository;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Use case for deleting an exercise log.
 */
@Singleton
public class DeleteExerciseLogUseCase {

    private final WorkoutRepository repository;

    @Inject
    public DeleteExerciseLogUseCase(WorkoutRepository repository) {
        this.repository = repository;
    }

    /**
     * Execute the use case to delete an exercise log.
     *
     * @param planId The plan ID
     * @param exerciseId The exercise ID
     * @param logId The log ID
     * @return Task indicating success or failure
     */
    public Task<Void> execute(String planId, String exerciseId, String logId) {
        return repository.deleteExerciseLog(planId, exerciseId, logId);
    }
}