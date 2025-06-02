package com.erendogan6.planmyworkout.feature.onboarding.usecase;

import com.erendogan6.planmyworkout.feature.onboarding.model.PlanCreationState;
import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlan;
import com.erendogan6.planmyworkout.feature.onboarding.repository.WorkoutPlanRepository;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

public class CreateOwnPlanUseCase {

    private final WorkoutPlanRepository repository;
    private final FirebaseAuth firebaseAuth;

    @Inject
    public CreateOwnPlanUseCase(WorkoutPlanRepository repository, FirebaseAuth firebaseAuth) {
        this.repository = repository;
        this.firebaseAuth = firebaseAuth;
    }

    public Task<Void> execute(PlanCreationState planState) {
        TaskCompletionSource<Void> taskSource = new TaskCompletionSource<>();

        // Validate plan state
        if (!planState.isValid()) {
            taskSource.setException(new IllegalArgumentException("Plan is not valid. Please fill all required fields."));
            return taskSource.getTask();
        }

        // Get current user
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            taskSource.setException(new IllegalStateException("User not authenticated"));
            return taskSource.getTask();
        }

        // Convert to WorkoutPlan and save
        WorkoutPlan workoutPlan = planState.toWorkoutPlan();

        repository.saveUserWorkoutPlan(currentUser.getUid(), workoutPlan, new WorkoutPlanRepository.SavePlanCallback() {
            @Override
            public void onSuccess() {
                taskSource.setResult(null);
            }

            @Override
            public void onError(Exception e) {
                taskSource.setException(e);
            }
        });

        return taskSource.getTask();
    }
}