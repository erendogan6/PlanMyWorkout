package com.erendogan6.planmyworkout.feature.profile.repository;

import com.erendogan6.planmyworkout.core.model.WorkoutPlan;
import com.erendogan6.planmyworkout.feature.profile.model.UserProfile;
import com.erendogan6.planmyworkout.feature.profile.model.WorkoutStats;
import com.google.android.gms.tasks.Task;

/**
 * Repository interface for profile operations.
 * Defines all profile-related operations that can be performed.
 */
public interface ProfileRepository {

    /**
     * Gets the current user's profile information.
     */
    Task<UserProfile> getUserProfile();

    /**
     * Gets the current user's active workout plan.
     */
    Task<WorkoutPlan> getCurrentWorkoutPlan();

    /**
     * Gets the user's workout statistics.
     */
    Task<WorkoutStats> getWorkoutStats();

    /**
     * Updates the user's profile information.
     */
    Task<Void> updateUserProfile(UserProfile userProfile);

    /**
     * Signs out the current user.
     */
    Task<Void> logout();

    /**
     * Deletes the current user's account and all associated data.
     */
    Task<Void> deleteAccount();

    /**
     * Gets the current authenticated user's ID.
     */
    String getCurrentUserId();
}