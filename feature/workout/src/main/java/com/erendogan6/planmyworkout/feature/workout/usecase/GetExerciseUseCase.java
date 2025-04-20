package com.erendogan6.planmyworkout.feature.workout.usecase;

import com.erendogan6.planmyworkout.feature.workout.model.ExerciseWithProgress;
import com.erendogan6.planmyworkout.feature.workout.repository.WorkoutRepository;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Use case for getting an exercise by ID.
 */
@Singleton
public class GetExerciseUseCase {

    private final WorkoutRepository repository;

    @Inject
    public GetExerciseUseCase(WorkoutRepository repository) {
        this.repository = repository;
    }

    /**
     * Execute the use case to get an exercise.
     *
     * @param planId The plan ID
     * @param exerciseId The exercise ID
     * @return Task with the exercise with progress
     */
    public Task<ExerciseWithProgress> execute(String planId, String exerciseId) {
        return repository.getExercise(planId, exerciseId);
    }
}