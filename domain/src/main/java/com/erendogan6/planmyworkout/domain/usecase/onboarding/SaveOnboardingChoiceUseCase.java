package com.erendogan6.planmyworkout.domain.usecase.onboarding;

import androidx.lifecycle.LiveData;

import com.erendogan6.planmyworkout.domain.model.OnboardingChoice;
import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.repository.OnboardingRepository;
import com.erendogan6.planmyworkout.domain.usecase.base.LiveDataUseCase;

import javax.inject.Inject;

/**
 * Use case for saving the user's onboarding choice.
 */
public class SaveOnboardingChoiceUseCase implements LiveDataUseCase<OnboardingChoice, Result<Boolean>> {
    
    private final OnboardingRepository onboardingRepository;
    
    @Inject
    public SaveOnboardingChoiceUseCase(OnboardingRepository onboardingRepository) {
        this.onboardingRepository = onboardingRepository;
    }
    
    @Override
    public LiveData<Result<Boolean>> execute(OnboardingChoice choice) {
        return onboardingRepository.saveOnboardingChoice(choice);
    }
}
