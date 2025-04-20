package com.erendogan6.planmyworkout.feature.onboarding.usecase;

import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlan;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Use case for getting ready-made workout plans.
 */
@Singleton
public class GetReadyMadePlansUseCase {

    @Inject
    public GetReadyMadePlansUseCase() {
        // Required empty constructor for Hilt
    }

    /**
     * Execute the use case to get ready-made workout plans.
     * @return List of ready-made workout plans
     */
    public List<WorkoutPlan> execute() {
        // In a real app, this would fetch from a repository
        return getMockWorkoutPlans();
    }

    /**
     * Generate mock workout plans for testing.
     * @return List of mock workout plans
     */
    private List<WorkoutPlan> getMockWorkoutPlans() {
        List<WorkoutPlan> plans = new ArrayList<>();

        // Add mock workout plans
        plans.add(new WorkoutPlan(
                "plan1",
                "Beginner Full Body",
                "Perfect for beginners, this plan focuses on building strength and endurance with 3 workouts per week.",
                "Beginner",
                3,
                8
        ));

        plans.add(new WorkoutPlan(
                "plan2",
                "Intermediate Push Pull Legs",
                "A 6-day split routine targeting different muscle groups each day for intermediate lifters.",
                "Intermediate",
                6,
                12
        ));

        plans.add(new WorkoutPlan(
                "plan3",
                "Advanced Upper Lower Split",
                "A 4-day split routine alternating between upper and lower body workouts for advanced lifters.",
                "Advanced",
                4,
                10
        ));

        plans.add(new WorkoutPlan(
                "plan4",
                "HIIT Cardio",
                "High-intensity interval training for fat loss and cardiovascular health.",
                "All Levels",
                3,
                6
        ));

        plans.add(new WorkoutPlan(
                "plan5",
                "Strength Focus",
                "A program designed to maximize strength gains with compound movements.",
                "Intermediate",
                4,
                12
        ));

        return plans;
    }
}
