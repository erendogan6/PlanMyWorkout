package com.erendogan6.planmyworkout.feature.onboarding.repository;

import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlan;

import java.util.List;

/**
 * Repository interface for workout plans.
 */
public interface WorkoutPlanRepository {
    
    /**
     * Interface for callback when fetching workout plans.
     */
    interface WorkoutPlansCallback {
        void onSuccess(List<WorkoutPlan> workoutPlans);
        void onError(Exception e);
    }
    
    /**
     * Get ready-made workout plans from Firestore.
     * @param callback Callback to handle the result
     */
    void getReadyMadeWorkoutPlans(WorkoutPlansCallback callback);
}
