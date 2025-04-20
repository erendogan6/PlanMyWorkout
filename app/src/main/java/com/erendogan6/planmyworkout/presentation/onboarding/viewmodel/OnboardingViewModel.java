package com.erendogan6.planmyworkout.presentation.onboarding.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.domain.model.OnboardingChoice;
import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.usecase.onboarding.SaveOnboardingChoiceUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the onboarding screen.
 */
@HiltViewModel
public class OnboardingViewModel extends ViewModel {

    private final SaveOnboardingChoiceUseCase saveOnboardingChoiceUseCase;

    private final MutableLiveData<OnboardingChoice> onboardingChoiceResult = new MutableLiveData<>();

    @Inject
    public OnboardingViewModel(SaveOnboardingChoiceUseCase saveOnboardingChoiceUseCase) {
        this.saveOnboardingChoiceUseCase = saveOnboardingChoiceUseCase;
    }

    /**
     * Save the user's onboarding choice.
     * @param choice The user's choice
     */
    public void saveOnboardingChoice(OnboardingChoice choice) {
        // Execute save onboarding choice use case
        saveOnboardingChoiceUseCase.execute(choice).observeForever(result -> {
            if (result != null) {
                // Handle the result
                if (result.getStatus() == Result.Status.SUCCESS) {
                    onboardingChoiceResult.postValue(choice);
                }
            }
        });
    }

    /**
     * Get the onboarding choice result LiveData.
     * @return LiveData containing the onboarding choice result
     */
    public LiveData<OnboardingChoice> getOnboardingChoiceResult() {
        return onboardingChoiceResult;
    }
}
