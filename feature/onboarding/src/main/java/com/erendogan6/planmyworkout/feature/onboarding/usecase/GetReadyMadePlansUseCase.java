package com.erendogan6.planmyworkout.feature.onboarding.usecase;

import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlan;
import com.erendogan6.planmyworkout.feature.onboarding.repository.WorkoutPlanRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Use case for getting ready-made workout plans.
 */
@Singleton
public class GetReadyMadePlansUseCase {

    private final WorkoutPlanRepository repository;

    @Inject
    public GetReadyMadePlansUseCase(WorkoutPlanRepository repository) {
        this.repository = repository;
    }

    /**
     * Interface for callback when fetching workout plans.
     */
    public interface GetReadyMadePlansCallback {
        void onSuccess(List<WorkoutPlan> workoutPlans);
        void onError(Exception e);
    }

    /**
     * Execute the use case to get ready-made workout plans.
     * @param callback Callback to handle the result
     */
    public void execute(GetReadyMadePlansCallback callback) {
        repository.getReadyMadeWorkoutPlans(new WorkoutPlanRepository.WorkoutPlansCallback() {
            @Override
            public void onSuccess(List<WorkoutPlan> workoutPlans) {
                callback.onSuccess(workoutPlans);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    // No mock data - we only use real data from Firestore
}
