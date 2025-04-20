package com.erendogan6.planmyworkout.feature.workout.usecase;

import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLog;
import com.erendogan6.planmyworkout.feature.workout.repository.WorkoutRepository;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Use case for getting the latest exercise log.
 */
@Singleton
public class GetLatestExerciseLogUseCase {

    private final WorkoutRepository repository;

    @Inject
    public GetLatestExerciseLogUseCase(WorkoutRepository repository) {
        this.repository = repository;
    }

    /**
     * Execute the use case to get the latest exercise log.
     *
     * @param planId The plan ID
     * @param exerciseId The exercise ID
     * @return Task with the latest exercise log
     */
    public Task<ExerciseLog> execute(String planId, String exerciseId) {
        return repository.getLatestExerciseLog(planId, exerciseId);
    }
}