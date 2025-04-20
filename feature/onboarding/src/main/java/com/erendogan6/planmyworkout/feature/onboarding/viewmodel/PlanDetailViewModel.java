package com.erendogan6.planmyworkout.feature.onboarding.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.core.util.FirestoreManager;
import com.erendogan6.planmyworkout.feature.onboarding.model.Exercise;
import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlan;
import com.erendogan6.planmyworkout.feature.onboarding.usecase.GetReadyMadePlansUseCase;
import com.erendogan6.planmyworkout.feature.onboarding.usecase.SavePlanUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the workout plan detail screen.
 * This class follows the MVVM architecture pattern and handles the business logic for the workout plan detail screen.
 */
@HiltViewModel
public class PlanDetailViewModel extends ViewModel {

    private final GetReadyMadePlansUseCase getReadyMadePlansUseCase;
    private final SavePlanUseCase savePlanUseCase;
    private final FirestoreManager firestoreManager;

    final MutableLiveData<WorkoutPlan> workoutPlan = new MutableLiveData<>();
    final MutableLiveData<List<Exercise>> exercises = new MutableLiveData<>();
    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    final MutableLiveData<Boolean> savePlanSuccess = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public PlanDetailViewModel(
            GetReadyMadePlansUseCase getReadyMadePlansUseCase,
            SavePlanUseCase savePlanUseCase,
            FirestoreManager firestoreManager) {
        this.getReadyMadePlansUseCase = getReadyMadePlansUseCase;
        this.savePlanUseCase = savePlanUseCase;
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
                    workoutPlan.postValue(selectedPlan);
                    // Use the exercises from the plan
                    exercises.postValue(selectedPlan.getExercises());
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

    /**
     * Save the selected workout plan to Firestore using the repository.
     */
    public void savePlan() {
        isLoading.setValue(true);

        // Get the current user ID
        String userId = firestoreManager.getCurrentUserId();
        if (userId == null) {
            isLoading.setValue(false);
            errorMessage.setValue("User not logged in");
            savePlanSuccess.setValue(false);
            return;
        }

        // Get the current workout plan
        WorkoutPlan plan = workoutPlan.getValue();
        if (plan == null) {
            isLoading.setValue(false);
            errorMessage.setValue("No workout plan selected");
            savePlanSuccess.setValue(false);
            return;
        }

        // Use the SavePlanUseCase to save the plan
        savePlanUseCase.execute(userId, plan, new SavePlanUseCase.SavePlanCallback() {
            @Override
            public void onSuccess() {
                isLoading.postValue(false);
                savePlanSuccess.postValue(true);
            }

            @Override
            public void onError(Exception e) {
                isLoading.postValue(false);
                errorMessage.postValue("Failed to save plan: " + e.getMessage());
                savePlanSuccess.postValue(false);
            }
        });
    }

    // Getter methods remain the same
    public LiveData<WorkoutPlan> getWorkoutPlan() {
        return workoutPlan;
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercises;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getSavePlanSuccess() {
        return savePlanSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}