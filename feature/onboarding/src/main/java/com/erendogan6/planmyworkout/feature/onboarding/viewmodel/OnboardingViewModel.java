package com.erendogan6.planmyworkout.feature.onboarding.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.core.model.Result;
import com.erendogan6.planmyworkout.feature.onboarding.data.OnboardingRepository;
import com.erendogan6.planmyworkout.feature.onboarding.model.OnboardingChoice;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the onboarding screens.
 */
@HiltViewModel
public class OnboardingViewModel extends ViewModel {

    private final OnboardingRepository onboardingRepository;
    private final MutableLiveData<Result<Boolean>> saveResult = new MutableLiveData<>();

    @Inject
    public OnboardingViewModel(OnboardingRepository onboardingRepository) {
        this.onboardingRepository = onboardingRepository;
    }

    /**
     * Save the user's onboarding choice.
     *
     * @param choice The selected onboarding choice
     * @return LiveData with the result of the save operation
     */
    public LiveData<Result<Boolean>> saveOnboardingChoice(OnboardingChoice choice) {
        // Set loading state
        saveResult.setValue(Result.loading(false));

        // Save the choice and observe the result
        LiveData<Result<Boolean>> result = onboardingRepository.saveOnboardingChoice(choice);
        result.observeForever(saveResult::setValue);

        return saveResult;
    }

    /**
     * Get the result of the save operation.
     *
     * @return LiveData with the result
     */
    public LiveData<Result<Boolean>> getSaveResult() {
        return saveResult;
    }
}
