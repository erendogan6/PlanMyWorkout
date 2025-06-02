package com.erendogan6.planmyworkout.feature.onboarding.usecase;

import com.erendogan6.planmyworkout.feature.onboarding.model.AiWorkoutPlanRequest;
import com.erendogan6.planmyworkout.feature.onboarding.model.AiWorkoutPlanResponse;
import com.erendogan6.planmyworkout.feature.onboarding.repository.AiWorkoutRepository;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

public class GenerateAiWorkoutPlanUseCase {

    private final AiWorkoutRepository repository;

    @Inject
    public GenerateAiWorkoutPlanUseCase(AiWorkoutRepository repository) {
        this.repository = repository;
    }

    public Task<AiWorkoutPlanResponse> execute(AiWorkoutPlanRequest request) {
        return repository.generateWorkoutPlan(request);
    }
}