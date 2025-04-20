package com.erendogan6.planmyworkout.feature.workout.repository;

import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLog;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseWithProgress;
import com.erendogan6.planmyworkout.feature.workout.model.WorkoutPlan;
import com.google.android.gms.tasks.Task;

import java.util.List;

/**
 * Repository interface for workout-related operations.
 */
public interface WorkoutRepository {

    /**
     * Get a workout plan by ID.
     *
     * @param planId The plan ID
     * @return Task with the workout plan
     */
    Task<WorkoutPlan> getWorkoutPlan(String planId);

    /**
     * Get an exercise by ID from a specific plan.
     *
     * @param planId The plan ID
     * @param exerciseId The exercise ID
     * @return Task with the exercise with progress
     */
    Task<ExerciseWithProgress> getExercise(String planId, String exerciseId);

    /**
     * Get exercises for a workout plan.
     *
     * @param planId The plan ID
     * @return Task with the list of exercises with progress
     */
    Task<List<ExerciseWithProgress>> getExercisesForPlan(String planId);

    /**
     * Get the latest exercise log for a specific exercise in a plan.
     *
     * @param planId The plan ID
     * @param exerciseId The exercise ID
     * @return Task with the latest exercise log
     */
    Task<ExerciseLog> getLatestExerciseLog(String planId, String exerciseId);

    /**
     * Save a new exercise log for a specific exercise in a plan.
     *
     * @param planId The plan ID
     * @param exerciseId The exercise ID
     * @param weight The weight used
     * @param reps The number of reps completed
     * @param notes Optional notes about the exercise
     * @return Task indicating success or failure
     */
    Task<Void> saveExerciseLog(String planId, String exerciseId, double weight, int reps, String notes);
}