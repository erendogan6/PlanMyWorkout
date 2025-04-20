package com.erendogan6.planmyworkout.data.di;

import com.erendogan6.planmyworkout.data.repository.PlanRepositoryImpl;
import com.erendogan6.planmyworkout.domain.repository.PlanRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Hilt module for providing repository implementations.
 * Note: AuthRepository and OnboardingRepository are provided by the app module's FirebaseModule
 */
@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {

    @Binds
    @Singleton
    public abstract PlanRepository bindPlanRepository(PlanRepositoryImpl planRepositoryImpl);
}
