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
    
    private final MutableLiveData<List<WorkoutPlan>> workoutPlans = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isEmpty = new MutableLiveData<>(false);
    
    @Inject
    public ReadyMadePlansViewModel(GetReadyMadePlansUseCase getReadyMadePlansUseCase) {
        this.getReadyMadePlansUseCase = getReadyMadePlansUseCase;
    }
    
    /**
     * Load workout plans from the use case.
     */
    public void loadWorkoutPlans() {
        isLoading.setValue(true);
        
        // Simulate network delay
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Simulate network delay
                
                // Get workout plans from use case
                List<WorkoutPlan> plans = getReadyMadePlansUseCase.execute();
                
                // Update LiveData based on result
                if (plans != null && !plans.isEmpty()) {
                    workoutPlans.postValue(plans);
                    isEmpty.postValue(false);
                } else {
                    isEmpty.postValue(true);
                }
                
                isLoading.postValue(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
                isLoading.postValue(false);
                isEmpty.postValue(true);
            }
        }).start();
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
}
