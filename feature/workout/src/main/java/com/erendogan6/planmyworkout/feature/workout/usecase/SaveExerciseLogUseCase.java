package com.erendogan6.planmyworkout.feature.workout.usecase;

import com.erendogan6.planmyworkout.feature.workout.repository.WorkoutRepository;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Use case for saving an exercise log.
 */
@Singleton
public class SaveExerciseLogUseCase {

    private final WorkoutRepository repository;

    @Inject
    public SaveExerciseLogUseCase(WorkoutRepository repository) {
        this.repository = repository;
    }

    /**
     * Execute the use case to save an exercise log.
     *
     * @param planId The plan ID
     * @param exerciseId The exercise ID
     * @param weight The weight used
     * @param reps The number of reps completed
     * @param notes Optional notes about the exercise
     * @return Task indicating success or failure
     */
    public Task<Void> execute(String planId, String exerciseId, double weight, int reps, String notes) {
        return repository.saveExerciseLog(planId, exerciseId, weight, reps, notes);
    }
}