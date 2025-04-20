package com.erendogan6.planmyworkout.feature.onboarding.di;

import com.erendogan6.planmyworkout.feature.onboarding.repository.WorkoutPlanRepository;
import com.erendogan6.planmyworkout.feature.onboarding.repository.WorkoutPlanRepositoryImpl;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Hilt module for the onboarding feature.
 */
@Module
@InstallIn(SingletonComponent.class)
public class OnboardingModule {

    // FirebaseFirestore is already provided by CoreModule

    @Provides
    @Singleton
    public WorkoutPlanRepository provideWorkoutPlanRepository(FirebaseFirestore firestore) {
        return new WorkoutPlanRepositoryImpl(firestore);
    }
}
