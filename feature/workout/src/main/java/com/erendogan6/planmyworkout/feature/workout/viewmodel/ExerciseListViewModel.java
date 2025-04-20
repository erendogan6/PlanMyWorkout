package com.erendogan6.planmyworkout.feature.workout.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.feature.workout.model.Exercise;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseWithProgress;
import com.erendogan6.planmyworkout.feature.workout.model.WorkoutPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the exercise list screen.
 */
@HiltViewModel
public class ExerciseListViewModel extends ViewModel {

    private final MutableLiveData<WorkoutPlan> workoutPlan = new MutableLiveData<>();
    private final MutableLiveData<List<ExerciseWithProgress>> exercises = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public ExerciseListViewModel() {
        // Required empty constructor for Hilt
    }

    /**
     * Load exercises for the given plan ID.
     */
    public void loadExercisesForPlan(String planId) {
        isLoading.setValue(true);
        
        // In a real app, this would fetch from a repository
        // For now, we'll create mock data
        WorkoutPlan plan = getMockWorkoutPlan(planId);
        workoutPlan.setValue(plan);
        
        if (plan != null && plan.getExercises() != null) {
            List<ExerciseWithProgress> exercisesWithProgress = createMockExercisesWithProgress(plan.getExercises());
            exercises.setValue(exercisesWithProgress);
        } else {
            exercises.setValue(new ArrayList<>());
        }
        
        isLoading.setValue(false);
    }
    
    /**
     * Create a mock workout plan.
     */
    private WorkoutPlan getMockWorkoutPlan(String planId) {
        WorkoutPlan plan = new WorkoutPlan(
                planId,
                "Full Body Workout",
                "A comprehensive full body workout targeting all major muscle groups",
                "Intermediate",
                3,
                8
        );
        
        List<Exercise> exerciseList = new ArrayList<>();
        exerciseList.add(new Exercise("ex1", "Barbell Squat", "A compound lower body exercise", "Legs", "", 3, 10, 90));
        exerciseList.add(new Exercise("ex2", "Bench Press", "A compound upper body exercise", "Chest", "", 3, 10, 90));
        exerciseList.add(new Exercise("ex3", "Deadlift", "A compound full body exercise", "Back", "", 3, 8, 120));
        exerciseList.add(new Exercise("ex4", "Pull-ups", "A compound upper body exercise", "Back", "", 3, 8, 90));
        exerciseList.add(new Exercise("ex5", "Overhead Press", "A compound upper body exercise", "Shoulders", "", 3, 10, 90));
        exerciseList.add(new Exercise("ex6", "Dumbbell Lunges", "A unilateral lower body exercise", "Legs", "", 3, 12, 60));
        
        plan.setExercises(exerciseList);
        return plan;
    }
    
    /**
     * Create mock exercises with progress data.
     */
    private List<ExerciseWithProgress> createMockExercisesWithProgress(List<Exercise> exercises) {
        List<ExerciseWithProgress> result = new ArrayList<>();
        Random random = new Random();

        for (Exercise exercise : exercises) {
            // Randomly decide if this exercise has previous attempts
            boolean hasLastTry = random.nextBoolean();

            if (hasLastTry) {
                // Generate random weight (between 20 and 150 kg)
                double weight = 20 + random.nextInt(131);
                // Generate random reps (between 1 and 12)
                int reps = 1 + random.nextInt(12);

                result.add(ExerciseWithProgress.fromExerciseWithProgress(exercise, weight, reps));
            } else {
                result.add(ExerciseWithProgress.fromExercise(exercise));
            }
        }

        return result;
    }

    /**
     * Set the workout plan.
     */
    public void setWorkoutPlan(WorkoutPlan plan) {
        workoutPlan.setValue(plan);
    }

    /**
     * Set the exercises for the workout plan.
     */
    public void setExercises(List<ExerciseWithProgress> exerciseList) {
        exercises.setValue(exerciseList);
    }

    /**
     * Get the workout plan LiveData.
     */
    public LiveData<WorkoutPlan> getWorkoutPlan() {
        return workoutPlan;
    }

    /**
     * Get the exercises LiveData.
     */
    public LiveData<List<ExerciseWithProgress>> getExercises() {
        return exercises;
    }

    /**
     * Get the loading state LiveData.
     */
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
