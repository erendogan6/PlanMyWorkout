package com.erendogan6.planmyworkout.feature.home.data;

import com.erendogan6.planmyworkout.core.util.FirestoreManager;
import com.erendogan6.planmyworkout.core.model.WorkoutPlan;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository for home screen repository.
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
        return Tasks.forResult(null);
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
                        // Return null if no plan ID is found
                        return null;
                    });
        }
        // Create a task that returns null if no user is logged in
        return Tasks.forResult(null);
    }

    /**
     * Get a workout plan by ID.
     *
     * @param planId The plan ID
     * @return Task with the workout plan
     */
    public Task<WorkoutPlan> getWorkoutPlan(String planId) {
        String userId = firestoreManager.getCurrentUserId();
        if (userId == null || planId == null) {
            return Tasks.forResult(null);
        }

        // Get the plan from the user's plans collection
        return firestore.collection("users")
                .document(userId)
                .collection("plans")
                .document(planId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Create a new WorkoutPlan object and populate it manually
                            WorkoutPlan plan = new WorkoutPlan();
                            plan.setId(document.getId());
                            plan.setName(document.getString("name"));
                            plan.setDescription(document.getString("description"));
                            plan.setDifficulty(document.getString("difficulty"));

                            // Get days per week and duration
                            Long daysPerWeek = document.getLong("days");
                            if (daysPerWeek != null) {
                                plan.setDaysPerWeek(daysPerWeek.intValue());
                            }

                            Long durationWeeks = document.getLong("durationWeeks");
                            if (durationWeeks != null) {
                                plan.setDurationWeeks(durationWeeks.intValue());
                            }

                            // Get weekly schedule
                            List<String> weeklySchedule = (List<String>) document.get("weeklySchedule");
                            plan.setWeeklySchedule(weeklySchedule);

                            // Get exercises
                            List<Map<String, Object>> exercisesData = (List<Map<String, Object>>) document.get("exercises");
                            if (exercisesData != null) {
                                List<String> exerciseNames = new ArrayList<>();
                                for (Map<String, Object> exerciseData : exercisesData) {
                                    String exerciseName = (String) exerciseData.get("name");
                                    if (exerciseName != null) {
                                        exerciseNames.add(exerciseName);
                                    }
                                }
                                plan.setExerciseNames(exerciseNames);
                            }

                            return plan;
                        }
                    }
                    return null;
                });
    }

    public String getCurrentUserId() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return currentUser != null ? currentUser.getUid() : "";
    }

    public Task<Void> updatePlanName(String planId, String newName) {
        return firestore.collection("users")
                .document(getCurrentUserId())
                .collection("plans")
                .document(planId)
                .update("name", newName);
    }

    public Task<Void> deletePlan(String planId) {
        TaskCompletionSource<Void> taskSource = new TaskCompletionSource<>();

        String userId = getCurrentUserId();
        if (userId.isEmpty()) {
            taskSource.setException(new IllegalStateException("User not authenticated"));
            return taskSource.getTask();
        }

        // Delete the plan from user's plans collection
        firestore.collection("users")
                .document(userId)
                .collection("plans")
                .document(planId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Also remove from user's current plan if it was the current one
                    firestore.collection("users")
                            .document(userId)
                            .update("currentPlanId", null)
                            .addOnSuccessListener(aVoid2 -> taskSource.setResult(null))
                            .addOnFailureListener(taskSource::setException);
                })
                .addOnFailureListener(taskSource::setException);

        return taskSource.getTask();
    }

}
