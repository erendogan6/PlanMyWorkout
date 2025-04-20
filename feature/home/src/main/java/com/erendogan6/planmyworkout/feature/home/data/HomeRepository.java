package com.erendogan6.planmyworkout.feature.home.data;

import com.erendogan6.planmyworkout.core.util.FirestoreManager;
import com.erendogan6.planmyworkout.feature.home.model.WorkoutPlan;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository for home screen data.
 */
@Singleton
public class HomeRepository {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;
    private final FirestoreManager firestoreManager;

    @Inject
    public HomeRepository(FirebaseAuth firebaseAuth, FirebaseFirestore firestore, FirestoreManager firestoreManager) {
        this.firebaseAuth = firebaseAuth;
        this.firestore = firestore;
        this.firestoreManager = firestoreManager;
    }

    /**
     * Get the current user's name.
     *
     * @return Task with the user's name
     */
    public Task<String> getUserName() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            return firestore.collection("users")
                    .document(user.getUid())
                    .get()
                    .continueWith(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists() && document.contains("name")) {
                                return document.getString("name");
                            }
                        }
                        return user.getDisplayName() != null ? user.getDisplayName() : "User";
                    });
        }
        // Create a task that returns a default value
        return Tasks.call(() -> "User");
    }

    /**
     * Get the current user's active workout plan ID.
     *
     * @return Task with the plan ID
     */
    public Task<String> getCurrentPlanId() {
        String userId = firestoreManager.getCurrentUserId();
        if (userId != null) {
            return firestore.collection("users")
                    .document(userId)
                    .get()
                    .continueWith(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists() && document.contains("mainPlanId")) {
                                return document.getString("mainPlanId");
                            }
                        }
                        // Return a mock plan ID for testing
                        return "mock_plan_1";
                    });
        }
        // Create a task that returns a mock plan ID for testing
        return Tasks.call(() -> "mock_plan_1");
    }

    /**
     * Get a workout plan by ID.
     *
     * @param planId The plan ID
     * @return Task with the workout plan
     */
    public Task<WorkoutPlan> getWorkoutPlan(String planId) {
        // Try to get the plan from Firestore first
        return firestore.collection("plans")
                .document(planId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            WorkoutPlan plan = document.toObject(WorkoutPlan.class);
                            if (plan != null) {
                                plan.setId(document.getId());
                                return plan;
                            }
                        }
                    }

                    // If no plan found or error, return a mock plan for testing
                    return createMockWorkoutPlan(planId);
                });
    }

    /**
     * Create a mock workout plan for testing.
     *
     * @param planId The plan ID
     * @return A mock workout plan
     */
    private WorkoutPlan createMockWorkoutPlan(String planId) {
        WorkoutPlan plan = new WorkoutPlan();
        plan.setId(planId);
        plan.setName("Full Body Workout");
        plan.setDescription("A comprehensive full body workout targeting all major muscle groups");
        plan.setFrequency("3 days/week");
        plan.setDifficulty("Intermediate");
        return plan;
    }
}
