package com.erendogan6.planmyworkout.feature.onboarding.usecase;

import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlan;
import com.erendogan6.planmyworkout.feature.onboarding.repository.WorkoutPlanRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Use case for saving a workout plan as the user's main plan.
 */
@Singleton
public class SavePlanUseCase {

    private final WorkoutPlanRepository repository;

    @Inject
    public SavePlanUseCase(WorkoutPlanRepository repository) {
        this.repository = repository;
    }

    /**
     * Interface for callback when saving a workout plan.
     */
    public interface SavePlanCallback {
        void onSuccess();
        void onError(Exception e);
    }

    /**
     * Execute the use case to save a workout plan.
     * @param userId The ID of the user
     * @param plan The workout plan to save
     * @param callback Callback to handle the result
     */
    public void execute(String userId, WorkoutPlan plan, SavePlanCallback callback) {
        repository.saveUserWorkoutPlan(userId, plan, new WorkoutPlanRepository.SavePlanCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }
}