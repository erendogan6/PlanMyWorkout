package com.erendogan6.planmyworkout.domain.repository;

import com.erendogan6.planmyworkout.domain.model.WorkoutPlan;

import java.util.List;

/**
 * Repository interface for workout plan operations.
 * This interface is part of the domain layer and defines the contract for workout plan operations.
 */
public interface PlanRepository {

    /**
     * Get a list of ready-made workout plans.
     *
     * @return List of WorkoutPlan objects
     */
    List<WorkoutPlan> getReadyMadePlans();
}
