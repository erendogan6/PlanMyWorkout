package com.erendogan6.planmyworkout.feature.workout.usecase;

import com.erendogan6.planmyworkout.feature.workout.model.ExerciseWithProgress;
import com.erendogan6.planmyworkout.feature.workout.repository.WorkoutRepository;
import com.google.android.gms.tasks.Task;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Use case for getting exercises for a workout plan.
 */
@Singleton
public class GetExercisesForPlanUseCase {

    private final WorkoutRepository repository;

    @Inject
    public GetExercisesForPlanUseCase(WorkoutRepository repository) {
        this.repository = repository;
    }

    /**
     * Execute the use case to get exercises for a plan.
     *
     * @param planId The plan ID
     * @return Task with the list of exercises with progress
     */
    public Task<List<ExerciseWithProgress>> execute(String planId) {
        return repository.getExercisesForPlan(planId);
    }
}