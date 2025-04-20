package com.erendogan6.planmyworkout.feature.onboarding.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.core.util.FirestoreManager;
import com.erendogan6.planmyworkout.feature.onboarding.model.Exercise;
import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlan;
import com.erendogan6.planmyworkout.feature.onboarding.usecase.GetReadyMadePlansUseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the workout plan detail screen.
 * This class follows the MVVM architecture pattern and handles the business logic for the workout plan detail screen.
 */
@HiltViewModel
public class PlanDetailViewModel extends ViewModel {

    private final GetReadyMadePlansUseCase getReadyMadePlansUseCase;
    private final FirestoreManager firestoreManager;

    private final MutableLiveData<WorkoutPlan> workoutPlan = new MutableLiveData<>();
    private final MutableLiveData<List<Exercise>> exercises = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> savePlanSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public PlanDetailViewModel(GetReadyMadePlansUseCase getReadyMadePlansUseCase, FirestoreManager firestoreManager) {
        this.getReadyMadePlansUseCase = getReadyMadePlansUseCase;
        this.firestoreManager = firestoreManager;
    }

    /**
     * Load plan details for the given plan ID.
     * @param planId The ID of the plan to load
     */
    public void loadPlanDetails(String planId) {
        isLoading.setValue(true);
        errorMessage.setValue(null); // Clear any previous error

        // Load from Firestore
        getReadyMadePlansUseCase.execute(new GetReadyMadePlansUseCase.GetReadyMadePlansCallback() {
            @Override
            public void onSuccess(List<WorkoutPlan> plans) {
                WorkoutPlan selectedPlan = null;
                for (WorkoutPlan plan : plans) {
                    if (plan.getId().equals(planId)) {
                        selectedPlan = plan;
                        break;
                    }
                }

                // Update LiveData
                if (selectedPlan != null) {
                    final WorkoutPlan finalPlan = selectedPlan;
                    workoutPlan.postValue(finalPlan);
                    // Use the exercises from the plan
                    exercises.postValue(finalPlan.getExercises());
                } else {
                    errorMessage.postValue("Plan not found");
                }

                isLoading.postValue(false);
            }

            @Override
            public void onError(Exception e) {
                errorMessage.postValue("Failed to load plan: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    // No mock exercises - we only use real data from Firestore

    /**
     * Save the selected workout plan to Firestore.
     */
    public void savePlan() {
        isLoading.setValue(true);

        // Get the current user ID
        String userId = firestoreManager.getCurrentUserId();
        if (userId == null) {
            isLoading.setValue(false);
            savePlanSuccess.setValue(false);
            return;
        }

        // Get the current workout plan
        WorkoutPlan plan = workoutPlan.getValue();
        if (plan == null) {
            isLoading.setValue(false);
            savePlanSuccess.setValue(false);
            return;
        }

        // Create a map to store the plan data
        Map<String, Object> planData = new HashMap<>();
        planData.put("id", plan.getId());
        planData.put("name", plan.getName());
        planData.put("description", plan.getDescription());
        planData.put("difficulty", plan.getDifficulty());
        planData.put("daysPerWeek", plan.getDaysPerWeek());
        planData.put("durationWeeks", plan.getDurationWeeks());

        // Add weekly schedule if available
        if (plan.getWeeklySchedule() != null) {
            planData.put("weeklySchedule", plan.getWeeklySchedule());
        }

        // Add exercises if available
        if (plan.getExercises() != null) {
            List<Map<String, Object>> exercisesData = new ArrayList<>();
            for (Exercise exercise : plan.getExercises()) {
                Map<String, Object> exerciseData = new HashMap<>();
                exerciseData.put("name", exercise.getName());
                exerciseData.put("description", exercise.getDescription());
                exerciseData.put("muscleGroup", exercise.getMuscleGroup());
                exerciseData.put("sets", exercise.getSets());
                exerciseData.put("reps", exercise.getRepsPerSet());
                exerciseData.put("restSeconds", exercise.getRestSeconds());
                exerciseData.put("unit", exercise.getUnit());
                exercisesData.add(exerciseData);
            }
            planData.put("exercises", exercisesData);
        }

        // Save the plan to Firestore
        firestoreManager.saveUserData(userId, "plans", plan.getId(), planData)
                .observeForever(result -> {
                    if (result.isSuccess()) {
                        // Also save the plan ID as the main plan directly in the user document
                        Map<String, Object> mainPlanData = new HashMap<>();
                        mainPlanData.put("mainPlanId", plan.getId());

                        firestoreManager.saveData("users", userId, mainPlanData)
                                .observeForever(mainPlanResult -> {
                                    isLoading.setValue(false);
                                    savePlanSuccess.setValue(mainPlanResult.isSuccess());
                                });
                    } else {
                        isLoading.setValue(false);
                        savePlanSuccess.setValue(false);
                    }
                });
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
     * Get the error message LiveData.
     * @return LiveData containing the error message
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
