package com.erendogan6.planmyworkout.presentation.onboarding;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.domain.model.OnboardingChoice;
import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.usecase.onboarding.GetOnboardingChoiceUseCase;
import com.erendogan6.planmyworkout.domain.usecase.onboarding.SaveOnboardingChoiceUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the Onboarding screen.
 */
@HiltViewModel
public class OnboardingViewModel extends ViewModel {

    private final SaveOnboardingChoiceUseCase saveOnboardingChoiceUseCase;
    private final GetOnboardingChoiceUseCase getOnboardingChoiceUseCase;

    private final MutableLiveData<Result<Boolean>> _saveResult = new MutableLiveData<>();
    private final MutableLiveData<Result<OnboardingChoice>> _onboardingChoice = new MutableLiveData<>();

    // Keep track of LiveData observers and their corresponding LiveData objects to prevent memory leaks
    private final java.util.Map<androidx.lifecycle.Observer<?>, androidx.lifecycle.LiveData<?>> observerMap = new java.util.HashMap<>();

    @Inject
    public OnboardingViewModel(SaveOnboardingChoiceUseCase saveOnboardingChoiceUseCase,
                              GetOnboardingChoiceUseCase getOnboardingChoiceUseCase) {
        this.saveOnboardingChoiceUseCase = saveOnboardingChoiceUseCase;
        this.getOnboardingChoiceUseCase = getOnboardingChoiceUseCase;
    }

    /**
     * Get the save result as a LiveData.
     *
     * @return LiveData of save result
     */
    public LiveData<Result<Boolean>> getSaveResult() {
        return _saveResult;
    }

    /**
     * Get the onboarding choice as a LiveData.
     *
     * @return LiveData of onboarding choice
     */
    public LiveData<Result<OnboardingChoice>> getOnboardingChoice() {
        return _onboardingChoice;
    }

    /**
     * Save the user's onboarding choice.
     *
     * @param choice The user's onboarding choice
     */
    public void saveOnboardingChoice(OnboardingChoice choice) {
        // Check if choice is null
        if (choice == null) {
            Log.e("OnboardingViewModel", "Choice is null");
            _saveResult.setValue(Result.error("Choice cannot be null", false));
            return;
        }

        Log.d("OnboardingViewModel", "Saving onboarding choice: " + choice.name());

        // Set loading state
        _saveResult.setValue(Result.loading(false));

        // Execute save onboarding choice use case and observe the result
        LiveData<Result<Boolean>> resultLiveData = saveOnboardingChoiceUseCase.execute(choice);
        androidx.lifecycle.Observer<Result<Boolean>> observer = result -> {
            if (result != null) {
                Log.d("OnboardingViewModel", "Save result: " + result.getStatus() + ", message: " + result.getMessage());
                _saveResult.setValue(result);
            }
        };
        resultLiveData.observeForever(observer);
        observerMap.put(observer, resultLiveData);
    }

    /**
     * Check if the user has completed onboarding.
     */
    public void checkOnboardingStatus() {
        // Set loading state
        _onboardingChoice.setValue(Result.loading(null));

        // Execute get onboarding choice use case and observe the result
        LiveData<Result<OnboardingChoice>> resultLiveData = getOnboardingChoiceUseCase.execute();
        androidx.lifecycle.Observer<Result<OnboardingChoice>> observer = result -> {
            if (result != null) {
                _onboardingChoice.setValue(result);
            }
        };
        resultLiveData.observeForever(observer);
        observerMap.put(observer, resultLiveData);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Remove all observers to prevent memory leaks
        for (java.util.Map.Entry<androidx.lifecycle.Observer<?>, androidx.lifecycle.LiveData<?>> entry : observerMap.entrySet()) {
            androidx.lifecycle.Observer<?> observer = entry.getKey();
            androidx.lifecycle.LiveData<?> liveData = entry.getValue();

            // This is not type-safe, but it's the best we can do with generics
            try {
                // Use raw types to remove the observer from its LiveData
                ((androidx.lifecycle.LiveData) liveData).removeObserver((androidx.lifecycle.Observer) observer);
                Log.d("OnboardingViewModel", "Successfully removed observer");
            } catch (Exception e) {
                Log.e("OnboardingViewModel", "Error removing observer: " + e.getMessage());
            }
        }
        observerMap.clear();
    }
}