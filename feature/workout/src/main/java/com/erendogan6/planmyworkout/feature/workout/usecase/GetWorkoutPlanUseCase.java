package com.erendogan6.planmyworkout.feature.workout.usecase;

import com.erendogan6.planmyworkout.feature.workout.model.WorkoutPlan;
import com.erendogan6.planmyworkout.feature.workout.repository.WorkoutRepository;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Use case for getting a workout plan by ID.
 */
@Singleton
public class GetWorkoutPlanUseCase {

    private final WorkoutRepository repository;

    @Inject
    public GetWorkoutPlanUseCase(WorkoutRepository repository) {
        this.repository = repository;
    }

    /**
     * Execute the use case to get a workout plan.
     *
     * @param planId The plan ID
     * @return Task with the workout plan
     */
    public Task<WorkoutPlan> execute(String planId) {
        return repository.getWorkoutPlan(planId);
    }
}