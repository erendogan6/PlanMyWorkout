package com.erendogan6.planmyworkout.domain.usecase.onboarding;

import androidx.lifecycle.LiveData;

import com.erendogan6.planmyworkout.domain.model.OnboardingChoice;
import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.repository.OnboardingRepository;
import com.erendogan6.planmyworkout.domain.usecase.base.NoParamsUseCase;

import javax.inject.Inject;

/**
 * Use case for getting the user's onboarding choice.
 */
public class GetOnboardingChoiceUseCase implements NoParamsUseCase<LiveData<Result<OnboardingChoice>>> {
    
    private final OnboardingRepository onboardingRepository;
    
    @Inject
    public GetOnboardingChoiceUseCase(OnboardingRepository onboardingRepository) {
        this.onboardingRepository = onboardingRepository;
    }
    
    @Override
    public LiveData<Result<OnboardingChoice>> execute() {
        return onboardingRepository.getOnboardingChoice();
    }
}
