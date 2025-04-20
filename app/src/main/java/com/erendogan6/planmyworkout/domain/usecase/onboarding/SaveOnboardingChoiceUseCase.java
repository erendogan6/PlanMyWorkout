package com.erendogan6.planmyworkout.domain.usecase.onboarding;

import androidx.lifecycle.LiveData;

import com.erendogan6.planmyworkout.data.repository.FirestoreManager;
import com.erendogan6.planmyworkout.domain.model.OnboardingChoice;
import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.usecase.base.LiveDataUseCase;

import javax.inject.Inject;

/**
 * Use case for saving the user's onboarding choice.
 */
public class SaveOnboardingChoiceUseCase implements LiveDataUseCase<OnboardingChoice, Result<Boolean>> {
    
    private final FirestoreManager firestoreManager;
    
    @Inject
    public SaveOnboardingChoiceUseCase(FirestoreManager firestoreManager) {
        this.firestoreManager = firestoreManager;
    }
    
    @Override
    public LiveData<Result<Boolean>> execute(OnboardingChoice choice) {
        return firestoreManager.saveOnboardingChoice(choice);
    }
}
