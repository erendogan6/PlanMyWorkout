package com.erendogan6.planmyworkout.feature.workout.usecase;

import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLog;
import com.erendogan6.planmyworkout.feature.workout.repository.WorkoutRepository;
import com.google.android.gms.tasks.Task;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Use case for getting all exercise logs.
 */
@Singleton
public class GetExerciseLogsUseCase {

    private final WorkoutRepository repository;

    @Inject
    public GetExerciseLogsUseCase(WorkoutRepository repository) {
        this.repository = repository;
    }

    /**
     * Execute the use case to get all exercise logs.
     *
     * @param planId The plan ID
     * @param exerciseId The exercise ID
     * @return Task with the list of exercise logs
     */
    public Task<List<ExerciseLog>> execute(String planId, String exerciseId) {
        return repository.getExerciseLogs(planId, exerciseId);
    }
}
