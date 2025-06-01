package com.erendogan6.planmyworkout.feature.profile.repository;

import com.erendogan6.planmyworkout.core.model.WorkoutPlan;
import com.erendogan6.planmyworkout.feature.profile.model.UserProfile;
import com.erendogan6.planmyworkout.feature.profile.model.WorkoutStats;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of ProfileRepository using Firebase.
 */
@Singleton
public class ProfileRepositoryImpl implements ProfileRepository {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;

    @Inject
    public ProfileRepositoryImpl(FirebaseAuth firebaseAuth, FirebaseFirestore firestore) {
        this.firebaseAuth = firebaseAuth;
        this.firestore = firestore;
    }

    @Override
    public Task<Void> deleteAccount() {
        String userId = getCurrentUserId();
        if (userId == null) {
            return Tasks.forException(new IllegalStateException("User not authenticated"));
        }

        // First delete user data from Firestore, then delete Firebase Auth account
        return firestore.collection("users")
                .document(userId)
                .delete()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        // Delete Firebase Auth account
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            return user.delete().continueWith(deleteTask -> null);
                        }
                    }
                    return Tasks.forResult(null);
                });
    }

    @Override
    public Task<UserProfile> getUserProfile() {
        String userId = getCurrentUserId();
        if (userId == null) {
            return Tasks.forException(new IllegalStateException("User not authenticated"));
        }

        return firestore.collection("users")
                .document(userId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            return convertDocumentToUserProfile(document);
                        } else {
                            // If document doesn't exist, create from Firebase Auth data
                            return createUserProfileFromAuth();
                        }
                    } else {
                        throw new RuntimeException("Failed to get user profile", task.getException());
                    }
                });
    }

    @Override
    public Task<WorkoutPlan> getCurrentWorkoutPlan() {
        String userId = getCurrentUserId();
        if (userId == null) {
            return Tasks.forException(new IllegalStateException("User not authenticated"));
        }

        return firestore.collection("users")
                .document(userId)
                .get()
                .continueWithTask(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot userDoc = task.getResult();
                        String mainPlanId = userDoc.getString("mainPlanId");

                        if (mainPlanId != null && !mainPlanId.isEmpty()) {
                            // Check if it's a user's custom plan or a ready-made plan
                            return firestore.collection("users")
                                    .document(userId)
                                    .collection("plans")
                                    .document(mainPlanId)
                                    .get()
                                    .continueWithTask(planTask -> {
                                        if (planTask.isSuccessful() && planTask.getResult() != null && planTask.getResult().exists()) {
                                            // User's custom plan
                                            return Tasks.forResult(convertDocumentToWorkoutPlan(planTask.getResult()));
                                        } else {
                                            // Check ready-made plans
                                            return firestore.collection("readyWorkoutPlans")
                                                    .document(mainPlanId)
                                                    .get()
                                                    .continueWith(readyPlanTask -> {
                                                        if (readyPlanTask.isSuccessful() && readyPlanTask.getResult() != null && readyPlanTask.getResult().exists()) {
                                                            return convertDocumentToWorkoutPlan(readyPlanTask.getResult());
                                                        } else {
                                                            return null;
                                                        }
                                                    });
                                        }
                                    });
                        } else {
                            return Tasks.forResult(null);
                        }
                    } else {
                        throw new RuntimeException("Failed to get current workout plan", task.getException());
                    }
                });
    }

    @Override
    public Task<WorkoutStats> getWorkoutStats() {
        String userId = getCurrentUserId();
        if (userId == null) {
            return Tasks.forException(new IllegalStateException("User not authenticated"));
        }

        // Get workout statistics from user's exercise logs
        return firestore.collectionGroup("exerciseLogs")
                .whereEqualTo("userId", userId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        QuerySnapshot querySnapshot = task.getResult();
                        return calculateWorkoutStats(querySnapshot);
                    } else {
                        // Return default stats if failed
                        return new WorkoutStats(0, 0, 0);
                    }
                });
    }

    @Override
    public Task<Void> updateUserProfile(UserProfile userProfile) {
        String userId = getCurrentUserId();
        if (userId == null) {
            return Tasks.forException(new IllegalStateException("User not authenticated"));
        }

        return firestore.collection("users")
                .document(userId)
                .update(
                        "fullName", userProfile.getFullName(),
                        "email", userProfile.getEmail(),
                        "profilePictureUrl", userProfile.getProfilePictureUrl()
                );
    }

    @Override
    public Task<Void> logout() {
        return Tasks.call(() -> {
            firebaseAuth.signOut();
            return null;
        });
    }

    @Override
    public String getCurrentUserId() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return currentUser != null ? currentUser.getUid() : null;
    }

    private UserProfile convertDocumentToUserProfile(DocumentSnapshot document) {
        String fullName = document.getString("fullName");
        String email = document.getString("email");
        String profilePictureUrl = document.getString("profilePictureUrl");

        return new UserProfile(fullName, email, profilePictureUrl);
    }

    private UserProfile createUserProfileFromAuth() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            return new UserProfile(
                    currentUser.getDisplayName(),
                    currentUser.getEmail(),
                    currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : null
            );
        }
        return null;
    }

    private WorkoutPlan convertDocumentToWorkoutPlan(DocumentSnapshot document) {
        String id = document.getId();
        String name = document.getString("name");
        String description = document.getString("description");
        String difficulty = document.getString("difficulty");

        // Get daysPerWeek and durationWeeks from Firestore
        Long daysPerWeekLong = document.getLong("daysPerWeek");
        int daysPerWeek = daysPerWeekLong != null ? daysPerWeekLong.intValue() : 0;

        Long durationWeeksLong = document.getLong("durationWeeks");
        int durationWeeks = durationWeeksLong != null ? durationWeeksLong.intValue() : 0;

        // Use correct constructor parameter order
        return new WorkoutPlan(id, name, description, difficulty, daysPerWeek, durationWeeks);
    }

    private WorkoutStats calculateWorkoutStats(QuerySnapshot querySnapshot) {
        int totalExercises = querySnapshot.size();

        // For now, return basic stats
        // You can enhance this to calculate actual workout sessions and consecutive days
        int totalWorkouts = totalExercises / 5; // Rough estimate
        int consecutiveDays = 0; // TODO: Implement proper calculation

        return new WorkoutStats(totalWorkouts, totalExercises, consecutiveDays);
    }
}