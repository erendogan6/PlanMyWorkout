package com.erendogan6.planmyworkout.feature.onboarding.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlan;
import com.erendogan6.planmyworkout.feature.onboarding.usecase.GetReadyMadePlansUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the ready-made plans screen.
 * This class follows the MVVM architecture pattern and handles the business logic for the ready-made plans screen.
 */
@HiltViewModel
public class ReadyMadePlansViewModel extends ViewModel {

    private final GetReadyMadePlansUseCase getReadyMadePlansUseCase;

    final MutableLiveData<List<WorkoutPlan>> workoutPlans = new MutableLiveData<>();
    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);
    final MutableLiveData<Boolean> isEmpty = new MutableLiveData<>(false);
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public ReadyMadePlansViewModel(GetReadyMadePlansUseCase getReadyMadePlansUseCase) {
        this.getReadyMadePlansUseCase = getReadyMadePlansUseCase;
    }

    /**
     * Load workout plans from the use case.
     */
    public void loadWorkoutPlans() {
        isLoading.setValue(true);
        errorMessage.setValue(null); // Clear any previous error

        // Try to load from Firestore first
        getReadyMadePlansUseCase.execute(new GetReadyMadePlansUseCase.GetReadyMadePlansCallback() {
            @Override
            public void onSuccess(List<WorkoutPlan> plans) {
                if (plans != null && !plans.isEmpty()) {
                    workoutPlans.postValue(plans);
                    isEmpty.postValue(false);
                } else {
                    // If Firestore returns empty, show empty state
                    isEmpty.postValue(true);
                }
                isLoading.postValue(false);
            }

            @Override
            public void onError(Exception e) {
                // If Firestore fails, show error
                isEmpty.postValue(true);
                errorMessage.postValue("Failed to load workout plans: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    /**
     * Get the workout plans LiveData.
     * @return LiveData containing the list of workout plans
     */
    public LiveData<List<WorkoutPlan>> getWorkoutPlans() {
        return workoutPlans;
    }

    /**
     * Get the loading state LiveData.
     * @return LiveData containing the loading state
     */
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    /**
     * Get the empty state LiveData.
     * @return LiveData containing the empty state
     */
    public LiveData<Boolean> getIsEmpty() {
        return isEmpty;
    }

    /**
     * Get the error message LiveData.
     * @return LiveData containing the error message
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
