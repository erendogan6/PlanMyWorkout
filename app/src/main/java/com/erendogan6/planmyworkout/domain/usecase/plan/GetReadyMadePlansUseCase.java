package com.erendogan6.planmyworkout.domain.usecase.plan;

import com.erendogan6.planmyworkout.domain.model.WorkoutPlan;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Use case for retrieving ready-made workout plans.
 * Currently returns mock data, but could be extended to fetch from a repository.
 */
public class GetReadyMadePlansUseCase {

    @Inject
    public GetReadyMadePlansUseCase() {
        // Required empty constructor for Hilt
    }

    /**
     * Execute the use case to get a list of ready-made workout plans.
     * @return List of WorkoutPlan objects
     */
    public List<WorkoutPlan> execute() {
        return getMockWorkoutPlans();
    }

    /**
     * Generate mock workout plan data.
     * @return List of mock WorkoutPlan objects
     */
    private List<WorkoutPlan> getMockWorkoutPlans() {
        List<WorkoutPlan> plans = new ArrayList<>();
        
        // Add mock workout plans
        plans.add(new WorkoutPlan(
                "plan1",
                "Full Body Beginner",
                "A 3-day full body routine designed for beginners to build strength and muscle",
                "Beginner",
                3,
                8
        ));
        
        plans.add(new WorkoutPlan(
                "plan2",
                "Push Pull Legs",
                "A 6-day strength split targeting different muscle groups each day",
                "Intermediate",
                6,
                12
        ));
        
        plans.add(new WorkoutPlan(
                "plan3",
                "Upper Lower Split",
                "A 4-day training program alternating between upper and lower body workouts",
                "Intermediate",
                4,
                10
        ));
        
        plans.add(new WorkoutPlan(
                "plan4",
                "5x5 Strength Program",
                "A classic strength building program focusing on compound movements",
                "Beginner to Intermediate",
                3,
                12
        ));
        
        plans.add(new WorkoutPlan(
                "plan5",
                "HIIT Cardio Plan",
                "High-intensity interval training to improve cardiovascular fitness and burn fat",
                "All Levels",
                4,
                6
        ));
        
        plans.add(new WorkoutPlan(
                "plan6",
                "Bodyweight Fitness",
                "No equipment needed for this full-body workout plan using only bodyweight exercises",
                "Beginner",
                3,
                8
        ));
        
        return plans;
    }
}
