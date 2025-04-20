package com.erendogan6.planmyworkout.feature.workout.repository;

import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLog;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseWithProgress;
import com.erendogan6.planmyworkout.feature.workout.model.WorkoutPlan;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository for workout-related data.
 */
@Singleton
public class WorkoutRepository {

    @Inject
    public WorkoutRepository() {
        // Default constructor
    }

    /**
     * Get a workout plan by ID.
     */
    public WorkoutPlan getWorkoutPlan(String planId) {
        // In a real app, this would fetch from Firestore
        // For now, return a mock plan
        return createMockWorkoutPlan(planId);
    }

    /**
     * Get exercises for a workout plan.
     */
    public List<ExerciseWithProgress> getExercisesForPlan(String planId) {
        // In a real app, this would fetch from Firestore
        // For now, return mock exercises
        List<ExerciseWithProgress> exercises = new ArrayList<>();
        exercises.add(new ExerciseWithProgress("ex1", "Barbell Squat",
                "A compound lower body exercise", "Legs", "",
                3, 10, 90, 80.0, 8));
        exercises.add(new ExerciseWithProgress("ex2", "Bench Press",
                "A compound upper body exercise", "Chest", "",
                3, 10, 90, 70.0, 8));
        exercises.add(new ExerciseWithProgress("ex3", "Deadlift",
                "A compound full body exercise", "Back", "",
                3, 8, 120, 100.0, 6));
        return exercises;
    }

    /**
     * Get an exercise by ID.
     */
    public ExerciseWithProgress getExercise(String exerciseId) {
        // In a real app, this would fetch from Firestore
        // For now, return a mock exercise
        return null; // This will be implemented in the ViewModel for now
    }

    /**
     * Save an exercise log.
     */
    public void saveExerciseLog(String exerciseId, ExerciseLog log) {
        // In a real app, this would save to Firestore
        // For now, do nothing
    }

    /**
     * Create a mock workout plan.
     */
    private WorkoutPlan createMockWorkoutPlan(String planId) {
        // In a real app, this would come from Firestore
        return new WorkoutPlan(planId, "Mock Workout Plan", "A mock workout plan for testing", "Intermediate", 4, 8);
    }
}
