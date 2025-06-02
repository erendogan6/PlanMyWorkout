package com.erendogan6.planmyworkout.feature.onboarding.usecase;

import com.erendogan6.planmyworkout.feature.onboarding.model.AiWorkoutPlanResponse;
import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlan;
import com.erendogan6.planmyworkout.feature.onboarding.repository.WorkoutPlanRepository;
import com.erendogan6.planmyworkout.feature.onboarding.util.AiPlanConverter;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

public class SaveAiWorkoutPlanUseCase {

    private final WorkoutPlanRepository repository;
    private final FirebaseAuth firebaseAuth;

    @Inject
    public SaveAiWorkoutPlanUseCase(WorkoutPlanRepository repository, FirebaseAuth firebaseAuth) {
        this.repository = repository;
        this.firebaseAuth = firebaseAuth;
    }

    public Task<Void> execute(AiWorkoutPlanResponse aiPlan) {
        TaskCompletionSource<Void> taskSource = new TaskCompletionSource<>();

        // Get current user
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            taskSource.setException(new IllegalStateException("User not authenticated"));
            return taskSource.getTask();
        }

        // Convert AI plan to WorkoutPlan
        WorkoutPlan workoutPlan = AiPlanConverter.convertAiPlanToWorkoutPlan(aiPlan);

        // Save to Firebase
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