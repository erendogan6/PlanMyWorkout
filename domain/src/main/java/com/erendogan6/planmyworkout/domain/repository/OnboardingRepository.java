package com.erendogan6.planmyworkout.domain.repository;

import androidx.lifecycle.LiveData;

import com.erendogan6.planmyworkout.domain.model.OnboardingChoice;
import com.erendogan6.planmyworkout.domain.model.Result;

/**
 * Repository interface for onboarding operations.
 * This interface is part of the domain layer and defines the contract for onboarding operations.
 */
public interface OnboardingRepository {

    /**
     * Get the user's onboarding choice.
     *
     * @return LiveData emitting Result of the onboarding choice
     */
    LiveData<Result<OnboardingChoice>> getOnboardingChoice();

    /**
     * Save the user's onboarding choice.
     *
     * @param choice The onboarding choice to save
     * @return LiveData emitting Result of the save operation
     */
    LiveData<Result<Boolean>> saveOnboardingChoice(OnboardingChoice choice);
}
