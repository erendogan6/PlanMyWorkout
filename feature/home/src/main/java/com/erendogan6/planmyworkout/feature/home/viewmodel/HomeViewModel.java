package com.erendogan6.planmyworkout.feature.home.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.feature.home.data.HomeRepository;
import com.erendogan6.planmyworkout.core.model.WorkoutPlan;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the home screen.
 */
@HiltViewModel
public class HomeViewModel extends ViewModel {

    private final HomeRepository homeRepository;
    private final MutableLiveData<String> userName = new MutableLiveData<>();
    private final MutableLiveData<WorkoutPlan> currentPlan = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> planUpdateSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private String currentPlanId;

    @Inject
    public HomeViewModel(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
        loadUserData();
        loadCurrentPlan();
    }

    private void loadUserData() {
        isLoading.setValue(true);
        homeRepository.getUserName().addOnSuccessListener(name -> {
            userName.setValue(name);
            // Don't hide loading here as we're still loading the plan
        });
    }

    private void loadCurrentPlan() {
        isLoading.setValue(true);
        homeRepository.getCurrentPlanId().addOnSuccessListener(planId -> {
            if (planId != null && !planId.isEmpty()) {
                currentPlanId = planId;
                homeRepository.getWorkoutPlan(planId).addOnSuccessListener(plan -> {
                    currentPlan.setValue(plan);
                    isLoading.setValue(false);
                }).addOnFailureListener(e -> {
                    isLoading.setValue(false);
                });
            } else {
                isLoading.setValue(false);
            }
        }).addOnFailureListener(e -> {
            isLoading.setValue(false);
        });
    }

    /**
     * Update the current plan's name.
     */
    public void updatePlanName(String newName) {
        if (currentPlanId == null || newName == null || newName.trim().isEmpty()) {
            errorMessage.setValue("Invalid plan name");
            return;
        }

        isLoading.setValue(true);
        homeRepository.updatePlanName(currentPlanId, newName.trim())
                .addOnSuccessListener(aVoid -> {
                    WorkoutPlan plan = currentPlan.getValue();
                    if (plan != null) {
                        plan.setName(newName.trim());
                        currentPlan.setValue(plan);
                    }
                    planUpdateSuccess.setValue(true);
                    isLoading.setValue(false);
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue("Failed to update plan name: " + e.getMessage());
                    isLoading.setValue(false);
                });
    }

    /**
     * Delete the current plan.
     */
    public void deletePlan() {
        if (currentPlanId == null) {
            errorMessage.setValue("No plan to delete");
            return;
        }

        isLoading.setValue(true);
        homeRepository.deletePlan(currentPlanId)
                .addOnSuccessListener(aVoid -> {
                    currentPlan.setValue(null);
                    currentPlanId = null;
                    isLoading.setValue(false);
                    planUpdateSuccess.setValue(true);
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue("Failed to delete plan: " + e.getMessage());
                    isLoading.setValue(false);
                });
    }

    /**
     * Get the user's name LiveData.
     *
     * @return LiveData with the user's name
     */
    public LiveData<String> getUserName() {
        return userName;
    }

    /**
     * Get the current workout plan LiveData.
     *
     * @return LiveData with the current workout plan
     */
    public LiveData<WorkoutPlan> getCurrentPlan() {
        return currentPlan;
    }

    /**
     * Get the current plan ID.
     *
     * @return The current plan ID or null if no plan is selected
     */
    public String getCurrentPlanId() {
        return currentPlanId;
    }

    /**
     * Get the loading state LiveData.
     *
     * @return LiveData with the loading state
     */
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    /**
     * Get the plan update success LiveData.
     *
     * @return LiveData with the plan update success state
     */
    public LiveData<Boolean> getPlanUpdateSuccess() {
        return planUpdateSuccess;
    }

    /**
     * Get the error message LiveData.
     *
     * @return LiveData with error messages
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}