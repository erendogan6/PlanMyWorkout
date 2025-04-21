package com.erendogan6.planmyworkout.feature.home.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.feature.home.data.HomeRepository;
import com.erendogan6.planmyworkout.feature.home.model.WorkoutPlan;

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
}
