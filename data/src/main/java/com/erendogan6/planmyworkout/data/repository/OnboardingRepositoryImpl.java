package com.erendogan6.planmyworkout.data.repository;

import androidx.lifecycle.LiveData;

import com.erendogan6.planmyworkout.domain.model.OnboardingChoice;
import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.repository.OnboardingRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of OnboardingRepository.
 * This class is part of the data layer and provides concrete implementation of onboarding operations.
 */
@Singleton
public class OnboardingRepositoryImpl implements OnboardingRepository {

    private final FirestoreManager firestoreManager;

    @Inject
    public OnboardingRepositoryImpl(FirestoreManager firestoreManager) {
        this.firestoreManager = firestoreManager;
    }

    @Override
    public LiveData<Result<OnboardingChoice>> getOnboardingChoice() {
        return firestoreManager.getOnboardingChoice();
    }

    @Override
    public LiveData<Result<Boolean>> saveOnboardingChoice(OnboardingChoice choice) {
        return firestoreManager.saveOnboardingChoice(choice);
    }
}
