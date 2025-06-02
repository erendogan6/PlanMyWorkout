package com.erendogan6.planmyworkout.feature.onboarding.repository;

import com.erendogan6.planmyworkout.feature.onboarding.model.AiWorkoutPlanRequest;
import com.erendogan6.planmyworkout.feature.onboarding.model.AiWorkoutPlanResponse;
import com.erendogan6.planmyworkout.feature.onboarding.service.AiWorkoutPlanService;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AiWorkoutRepository {

    private final AiWorkoutPlanService aiService;

    @Inject
    public AiWorkoutRepository(AiWorkoutPlanService aiService) {
        this.aiService = aiService;
    }

    public Task<AiWorkoutPlanResponse> generateWorkoutPlan(AiWorkoutPlanRequest request) {
        return aiService.generateWorkoutPlan(request);
    }
}