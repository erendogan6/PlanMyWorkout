package com.erendogan6.planmyworkout.presentation.plan.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.domain.model.Exercise;
import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.model.WorkoutPlan;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the workout plan detail screen.
 * This class follows the MVVM architecture pattern and handles the business logic for the workout plan detail screen.
 */
@HiltViewModel
public class WorkoutPlanDetailViewModel extends ViewModel {

    private final MutableLiveData<WorkoutPlan> workoutPlan = new MutableLiveData<>();
    private final MutableLiveData<List<Exercise>> exercises = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> savePlanSuccess = new MutableLiveData<>();

    @Inject
    public WorkoutPlanDetailViewModel() {
        // Required empty constructor for Hilt
    }

    /**
     * Set the workout plan to display.
     * @param plan The workout plan to display
     */
    public void setWorkoutPlan(WorkoutPlan plan) {
        workoutPlan.setValue(plan);
        loadExercisesForPlan(plan);
    }

    /**
     * Load exercises for the given workout plan.
     * This is currently using mock data, but would fetch from a repository in a real app.
     * @param plan The workout plan to load exercises for
     */
    private void loadExercisesForPlan(WorkoutPlan plan) {
        // In a real app, this would fetch from a repository
        exercises.setValue(getMockExercises(plan));
    }

    /**
     * Generate mock exercises for a workout plan.
     * @param plan The workout plan to generate exercises for
     * @return A list of mock exercises
     */
    private List<Exercise> getMockExercises(WorkoutPlan plan) {
        List<Exercise> exerciseList = new ArrayList<>();

        // Add mock exercises based on the plan type
        if (plan.getName().contains("Full Body")) {
            // Full body workout exercises
            exerciseList.add(new Exercise("ex1", "Barbell Squat", "A compound lower body exercise that targets the quadriceps, hamstrings, and glutes", "Legs", "", 3, 10, 90));
            exerciseList.add(new Exercise("ex2", "Bench Press", "A compound upper body exercise that targets the chest, shoulders, and triceps", "Chest", "", 3, 10, 90));
            exerciseList.add(new Exercise("ex3", "Deadlift", "A compound full body exercise that targets the back, glutes, and hamstrings", "Back", "", 3, 8, 120));
            exerciseList.add(new Exercise("ex4", "Pull-ups", "A compound upper body exercise that targets the back and biceps", "Back", "", 3, 8, 90));
            exerciseList.add(new Exercise("ex5", "Overhead Press", "A compound upper body exercise that targets the shoulders and triceps", "Shoulders", "", 3, 10, 90));
            exerciseList.add(new Exercise("ex6", "Dumbbell Lunges", "A unilateral lower body exercise that targets the quadriceps, hamstrings, and glutes", "Legs", "", 3, 12, 60));
        } else if (plan.getName().contains("Push Pull")) {
            // Push day exercises
            exerciseList.add(new Exercise("ex7", "Incline Bench Press", "A compound upper body exercise that targets the upper chest, shoulders, and triceps", "Chest", "", 4, 8, 90));
            exerciseList.add(new Exercise("ex8", "Dumbbell Shoulder Press", "A compound upper body exercise that targets the shoulders and triceps", "Shoulders", "", 4, 10, 90));
            exerciseList.add(new Exercise("ex9", "Tricep Pushdowns", "An isolation exercise that targets the triceps", "Arms", "", 3, 12, 60));
            exerciseList.add(new Exercise("ex10", "Lateral Raises", "An isolation exercise that targets the lateral deltoids", "Shoulders", "", 3, 15, 60));
            exerciseList.add(new Exercise("ex11", "Chest Flyes", "An isolation exercise that targets the chest", "Chest", "", 3, 12, 60));
        } else if (plan.getName().contains("Upper Lower")) {
            // Upper body exercises
            exerciseList.add(new Exercise("ex12", "Barbell Rows", "A compound upper body exercise that targets the back and biceps", "Back", "", 4, 8, 90));
            exerciseList.add(new Exercise("ex13", "Incline Dumbbell Press", "A compound upper body exercise that targets the upper chest, shoulders, and triceps", "Chest", "", 4, 10, 90));
            exerciseList.add(new Exercise("ex14", "Pull-ups", "A compound upper body exercise that targets the back and biceps", "Back", "", 3, 8, 90));
            exerciseList.add(new Exercise("ex15", "Dips", "A compound upper body exercise that targets the chest, shoulders, and triceps", "Chest", "", 3, 10, 90));
            exerciseList.add(new Exercise("ex16", "Face Pulls", "A compound upper body exercise that targets the rear deltoids and upper back", "Shoulders", "", 3, 15, 60));
        } else {
            // Default exercises for any other plan
            exerciseList.add(new Exercise("ex17", "Push-ups", "A bodyweight exercise that targets the chest, shoulders, and triceps", "Chest", "", 3, 15, 60));
            exerciseList.add(new Exercise("ex18", "Bodyweight Squats", "A bodyweight exercise that targets the quadriceps, hamstrings, and glutes", "Legs", "", 3, 20, 60));
            exerciseList.add(new Exercise("ex19", "Plank", "A core exercise that targets the abdominals and lower back", "Core", "", 3, 30, 60));
            exerciseList.add(new Exercise("ex20", "Mountain Climbers", "A cardio exercise that targets the core, shoulders, and legs", "Full Body", "", 3, 20, 30));
            exerciseList.add(new Exercise("ex21", "Burpees", "A full body exercise that targets multiple muscle groups and provides cardiovascular benefits", "Full Body", "", 3, 15, 60));
        }

        return exerciseList;
    }

    /**
     * Get the workout plan LiveData.
     * @return LiveData containing the workout plan
     */
    public LiveData<WorkoutPlan> getWorkoutPlan() {
        return workoutPlan;
    }

    /**
     * Get the exercises LiveData.
     * @return LiveData containing the list of exercises
     */
    public LiveData<List<Exercise>> getExercises() {
        return exercises;
    }

    /**
     * Get the loading state LiveData.
     * @return LiveData containing the loading state
     */
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    /**
     * Get the save plan success LiveData.
     * @return LiveData containing the save plan success state
     */
    public LiveData<Boolean> getSavePlanSuccess() {
        return savePlanSuccess;
    }

    /**
     * Save the selected workout plan to Firestore.
     * This would save the plan to the user's account in a real app.
     */
    public void savePlan() {
        isLoading.setValue(true);

        // Simulate network delay
        new Thread(() -> {
            try {
                Thread.sleep(1500); // Simulate network delay

                // In a real app, this would save to Firestore
                // users/{userId}/plans/{planId} -> full plan data
                // users/{userId}/mainPlanId = planId

                // Simulate success
                savePlanSuccess.postValue(true);
                isLoading.postValue(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
                savePlanSuccess.postValue(false);
                isLoading.postValue(false);
            }
        }).start();
    }
}
