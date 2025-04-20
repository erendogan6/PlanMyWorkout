package com.erendogan6.planmyworkout.feature.workout.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.feature.workout.model.ExerciseWithProgress;
import com.erendogan6.planmyworkout.feature.workout.model.WorkoutPlan;
import com.erendogan6.planmyworkout.feature.workout.usecase.GetExercisesForPlanUseCase;
import com.erendogan6.planmyworkout.feature.workout.usecase.GetWorkoutPlanUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the exercise list screen.
 */
@HiltViewModel
public class ExerciseListViewModel extends ViewModel {

    private final GetWorkoutPlanUseCase getWorkoutPlanUseCase;
    private final GetExercisesForPlanUseCase getExercisesForPlanUseCase;
    private final MutableLiveData<WorkoutPlan> workoutPlan = new MutableLiveData<>();
    private final MutableLiveData<List<ExerciseWithProgress>> exercises = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public ExerciseListViewModel(
            GetWorkoutPlanUseCase getWorkoutPlanUseCase,
            GetExercisesForPlanUseCase getExercisesForPlanUseCase) {
        this.getWorkoutPlanUseCase = getWorkoutPlanUseCase;
        this.getExercisesForPlanUseCase = getExercisesForPlanUseCase;
    }

    /**
     * Load exercises for the given plan ID.
     */
    public void loadExercisesForPlan(String planId) {
        if (planId == null || planId.isEmpty()) {
            errorMessage.setValue("Invalid plan ID");
            return;
        }

        isLoading.setValue(true);
        exercises.setValue(new ArrayList<>()); // Clear existing exercises

        // Load the workout plan from Firestore
        getWorkoutPlanUseCase.execute(planId)
                .addOnSuccessListener(plan -> {
                    if (plan != null) {
                        workoutPlan.setValue(plan);

                        // Load exercises for the plan
                        getExercisesForPlanUseCase.execute(planId)
                                .addOnSuccessListener(exerciseList -> {
                                    exercises.setValue(exerciseList);
                                    isLoading.setValue(false);
                                })
                                .addOnFailureListener(e -> {
                                    errorMessage.setValue("Failed to load exercises: " + e.getMessage());
                                    exercises.setValue(new ArrayList<>());
                                    isLoading.setValue(false);
                                });
                    } else {
                        errorMessage.setValue("Workout plan not found");
                        isLoading.setValue(false);
                    }
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue("Failed to load workout plan: " + e.getMessage());
                    isLoading.setValue(false);
                });
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

    /**
     * Get the error message LiveData.
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}