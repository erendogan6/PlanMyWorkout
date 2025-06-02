package com.erendogan6.planmyworkout.feature.onboarding.di;

import com.erendogan6.planmyworkout.feature.onboarding.repository.AiWorkoutRepository;
import com.erendogan6.planmyworkout.feature.onboarding.service.AiWorkoutPlanService;
import com.erendogan6.planmyworkout.feature.onboarding.usecase.GenerateAiWorkoutPlanUseCase;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class AiModule {

    @Provides
    @Singleton
    public AiWorkoutPlanService provideAiWorkoutPlanService() {
        return new AiWorkoutPlanService();
    }

    @Provides
    @Singleton
    public AiWorkoutRepository provideAiWorkoutRepository(AiWorkoutPlanService service) {
        return new AiWorkoutRepository(service);
    }

    @Provides
    public GenerateAiWorkoutPlanUseCase provideGenerateAiWorkoutPlanUseCase(AiWorkoutRepository repository) {
        return new GenerateAiWorkoutPlanUseCase(repository);
    }
}