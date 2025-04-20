package com.erendogan6.planmyworkout.domain.usecase.plan;

import com.erendogan6.planmyworkout.domain.model.WorkoutPlan;
import com.erendogan6.planmyworkout.domain.repository.PlanRepository;
import com.erendogan6.planmyworkout.domain.usecase.base.NoParamsUseCase;

import java.util.List;

import javax.inject.Inject;

/**
 * Use case for retrieving ready-made workout plans.
 */
public class GetReadyMadePlansUseCase implements NoParamsUseCase<List<WorkoutPlan>> {

    private final PlanRepository planRepository;

    @Inject
    public GetReadyMadePlansUseCase(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    /**
     * Execute the use case to get a list of ready-made workout plans.
     * @return List of WorkoutPlan objects
     */
    @Override
    public List<WorkoutPlan> execute() {
        return planRepository.getReadyMadePlans();
    }
}
